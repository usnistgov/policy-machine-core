package gov.nist.csd.pm.core.pap;

import gov.nist.csd.pm.core.common.exception.BootstrapExistingPolicyException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.AdminFunction;
import gov.nist.csd.pm.core.pap.function.AdminFunctionExecutor;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.common.tx.Transactional;
import gov.nist.csd.pm.core.pap.function.arg.NoArgs;
import gov.nist.csd.pm.core.pap.function.op.PrivilegeChecker;
import gov.nist.csd.pm.core.pap.id.IdGenerator;
import gov.nist.csd.pm.core.pap.modification.PolicyModification;
import gov.nist.csd.pm.core.pap.modification.PolicyModifier;
import gov.nist.csd.pm.core.pap.pml.PMLCompiler;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.core.pap.query.PolicyQuerier;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pap.serialization.PolicyDeserializer;
import gov.nist.csd.pm.core.pap.serialization.PolicySerializer;
import gov.nist.csd.pm.core.pap.store.PolicyStore;
import gov.nist.csd.pm.core.pdp.bootstrap.PolicyBootstrapper;

import java.util.*;

import static gov.nist.csd.pm.core.common.graph.node.NodeType.ANY;
import static gov.nist.csd.pm.core.common.graph.node.Properties.NO_PROPERTIES;

public abstract class PAP implements AdminFunctionExecutor, Transactional {

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
        AdminPolicyNode.verifyAdminPolicy(policyStore().graph());
    }

    public PAP(PolicyQuerier querier, PolicyModifier modifier, PolicyStore policyStore) throws PMException {
        this.querier = querier;
        this.modifier = modifier;
        this.policyStore = policyStore;
        this.privilegeChecker = new PrivilegeChecker(querier.access());

        // verify admin policy
        AdminPolicyNode.verifyAdminPolicy(policyStore().graph());
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
        AdminPolicyNode.verifyAdminPolicy(policyStore().graph());
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

        // verify the admin nodes exist in the policy
        AdminPolicyNode.verifyAdminPolicy(policyStore().graph());

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
     * an exception will be thrown. This method also resets the policy before deserialization. However, the
     * {@link gov.nist.csd.pm.core.pap.admin.AdminPolicyNode} nodes are assumed to be created and can be referenced in
     * the input string without explicit creation. If any of the admin policy nodes are created in the input string an
     * exception will be thrown.
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

        nodes.removeIf(n -> AdminPolicyNode.isAdminPolicyNode(n.getId()));

        return nodes.isEmpty()
            && prohibitionsEmpty
            && obligationsEmpty
            && resOpsEmpty
            && adminOpsEmpty
            && routinesEmpty;
    }
}
