package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.common.exception.BootstrapExistingPolicyException;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.AdminFunction;
import gov.nist.csd.pm.pap.function.AdminFunctionExecutor;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.common.tx.Transactional;
import gov.nist.csd.pm.pap.admin.AdminPolicy;
import gov.nist.csd.pm.pap.function.arg.NoArgs;
import gov.nist.csd.pm.pap.function.op.PrivilegeChecker;
import gov.nist.csd.pm.pap.id.IdGenerator;
import gov.nist.csd.pm.pap.modification.PolicyModification;
import gov.nist.csd.pm.pap.modification.PolicyModifier;
import gov.nist.csd.pm.pap.pml.PMLCompiler;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.pap.query.PolicyQuerier;
import gov.nist.csd.pm.pap.query.PolicyQuery;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pap.serialization.PolicyDeserializer;
import gov.nist.csd.pm.pap.serialization.PolicySerializer;
import gov.nist.csd.pm.pap.store.PolicyStore;
import gov.nist.csd.pm.pdp.bootstrap.PolicyBootstrapper;

import java.util.*;

import static gov.nist.csd.pm.common.graph.node.NodeType.ANY;
import static gov.nist.csd.pm.common.graph.node.Properties.NO_PROPERTIES;
import static gov.nist.csd.pm.pap.admin.AdminPolicy.ALL_NODES;
import static gov.nist.csd.pm.pap.admin.AdminPolicy.ALL_NODE_NAMES;

public class PAP implements AdminFunctionExecutor, Transactional {

    private final PolicyStore policyStore;
    private final PolicyModifier modifier;
    private final PolicyQuerier querier;
    private final PrivilegeChecker privilegeChecker;

    public PAP(PolicyStore policyStore, PolicyModifier modifier, PolicyQuerier querier, PrivilegeChecker privilegeChecker) throws PMException {
        this.policyStore = policyStore;
        this.modifier = modifier;
        this.querier = querier;
        this.privilegeChecker = privilegeChecker;

        // verify admin policy
        this.policyStore.verifyAdminPolicy();
    }

    public PAP(PolicyQuerier querier, PolicyModifier modifier, PolicyStore policyStore) {
        this.querier = querier;
        this.modifier = modifier;
        this.policyStore = policyStore;
        this.privilegeChecker = new PrivilegeChecker(querier.access());
    }

    public PAP(PAP pap) throws PMException {
        this(pap.policyStore, pap.modifier, pap.querier, pap.privilegeChecker);
    }

    public PolicyQuery query() {
        return querier;
    }

    public PolicyModification modify() {
        return modifier;
    }

    public PolicyStore policyStore() {
        return policyStore;
    }

    public PrivilegeChecker privilegeChecker() {
        return privilegeChecker;
    }

    public PAP withIdGenerator(IdGenerator idGenerator) {
        this.modifier.graph().setIdGenerator(idGenerator);
        return this;
    }

    public void reset() throws PMException {
        policyStore.reset();
        policyStore.verifyAdminPolicy();
    }

    public ExecutionContext buildExecutionContext(UserContext userCtx) throws PMException {
        return new ExecutionContext(userCtx, this);
    }

    /**
     * Bootstrap the policy with the given PolicyBootstrapper object. The bootstrapping user is the user that will
     * go no record as being the author of any obligations created by the bootstrapper. This user will be created outside
     * the PolicyBootstrapper and already exists when the bootstrapper is executed. The bootstrap user must be assigned
     * to attributes within the PolicyBootstrapper or an exception will be thrown.
     * @param bootstrapper the PolicyBootstrapper that will build the custom bootstrap policy.
     * @throws PMException if there is an error bootstrapping.
     */
    public void bootstrap(PolicyBootstrapper bootstrapper) throws PMException {
        if(!isPolicyEmpty()) {
            throw new BootstrapExistingPolicyException();
        }

        // execute the bootstrapper
        runTx(bootstrapper::bootstrap);
    }

    @Override
    public <R, A extends Args> R executeAdminFunction(AdminFunction<R, A> adminFunction,
                                                      Map<String, Object> args) throws PMException {
        return adminFunction.execute(this, adminFunction.validateAndPrepareArgs(args));
    }

    /**
     * Serialize the current policy state with the given PolicySerializer.
     *
     * @param serializer The PolicySerializer used to generate the output String.
     * @return The string representation of the policy.
     * @throws PMException If there is an error during the serialization process.
     */
    public String serialize(PolicySerializer serializer) throws PMException {
        return serializer.serialize(query());
    }

    /**
     * Deserialize the given input string into the current policy state. The user defined in the UserContext needs to exist
     * in the graph created if any obligations are created. If the user does not exist before an obligation is created
     * an exception will be thrown. This method also resets the policy before deserialization. However, the {@link AdminPolicy}
     * nodes are assumed to be created and can be referenced in the input string without explicit creation. If any of the
     * admin policy nodes are created in the input string an exception will be thrown.
     *
     * @param input The string representation of the policy to deserialize.
     * @param policyDeserializer The PolicyDeserializer to apply the input string to the policy.
     * @throws PMException If there is an error deserializing the given inputs string.
     */
    public void deserialize(String input, PolicyDeserializer policyDeserializer) throws PMException {
        beginTx();

        try {
            policyDeserializer.deserialize(this, input);
        } catch (PMException e) {
            rollback();
            throw e;
        }

        commit();
    }

    public void executePML(UserContext author, String input) throws PMException {
        PMLCompiler pmlCompiler = new PMLCompiler();

        List<PMLStatement<?>> compiledPML = pmlCompiler.compilePML(this, input);

        ExecutionContext ctx = new ExecutionContext(author, this);

        ctx.executeStatements(compiledPML, new NoArgs());
    }

    public List<PMLStatement<?>> compilePML(String input) throws PMException {
        PMLCompiler pmlCompiler = new PMLCompiler();

        return pmlCompiler.compilePML(this, input);
    }

    public void runTx(TxRunner tx) throws PMException {
        beginTx();

        try {
            tx.runTx(this);

            commit();
        } catch (PMException e) {
            rollback();
            throw e;
        }
    }

    @Override
    public void beginTx() throws PMException {
        policyStore.beginTx();
    }

    @Override
    public void commit() throws PMException {
        policyStore.commit();
    }

    @Override
    public void rollback() throws PMException {
        policyStore.rollback();
    }

    public interface TxRunner {
        void runTx(PAP pap) throws PMException;
    }

    private boolean isPolicyEmpty() throws PMException {
        HashSet<Node> nodes = new HashSet<>(query().graph().search(ANY, NO_PROPERTIES));

        boolean prohibitionsEmpty = query().prohibitions().getProhibitions().isEmpty();
        boolean obligationsEmpty = query().obligations().getObligations().isEmpty();
        boolean resOpsEmpty = query().operations().getResourceOperations().isEmpty();

        boolean adminOpsEmpty =  query().operations().getAdminOperationNames().isEmpty();
        boolean routinesEmpty = query().routines().getAdminRoutineNames().isEmpty();

        return (nodes.isEmpty() || (nodes.size() == ALL_NODE_NAMES.size() && nodes.containsAll(ALL_NODES))) &&
                prohibitionsEmpty && obligationsEmpty && resOpsEmpty && adminOpsEmpty && routinesEmpty;
    }
}
