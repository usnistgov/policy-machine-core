package gov.nist.ngac.pm.core.pap;

import static gov.nist.ngac.pm.core.common.graph.node.NodeType.ANY;
import static gov.nist.ngac.pm.core.common.graph.node.Properties.NO_PROPERTIES;

import gov.nist.ngac.pm.core.common.exception.BootstrapExistingPolicyException;
import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.graph.node.Node;
import gov.nist.ngac.pm.core.common.tx.Transactional;
import gov.nist.ngac.pm.core.pap.admin.AdminPolicy;
import gov.nist.ngac.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.ngac.pm.core.pap.id.IdGenerator;
import gov.nist.ngac.pm.core.pap.id.RandomIdGenerator;
import gov.nist.ngac.pm.core.pap.modification.GraphModifier;
import gov.nist.ngac.pm.core.pap.modification.ObligationsModifier;
import gov.nist.ngac.pm.core.pap.modification.OperationsModifier;
import gov.nist.ngac.pm.core.pap.modification.PolicyModification;
import gov.nist.ngac.pm.core.pap.modification.PolicyModifier;
import gov.nist.ngac.pm.core.pap.modification.ProhibitionsModifier;
import gov.nist.ngac.pm.core.pap.operation.JavaOperationRegistry;
import gov.nist.ngac.pm.core.pap.operation.Operation;
import gov.nist.ngac.pm.core.pap.operation.OperationExecutor;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.pml.PMLCompiler;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.ngac.pm.core.pap.pml.operation.PMLOperation;
import gov.nist.ngac.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.ngac.pm.core.pap.pml.statement.result.ReturnResult;
import gov.nist.ngac.pm.core.pap.pml.statement.result.StatementResult;
import gov.nist.ngac.pm.core.pap.query.AccessQuerier;
import gov.nist.ngac.pm.core.pap.query.GraphQuerier;
import gov.nist.ngac.pm.core.pap.query.ObligationsQuerier;
import gov.nist.ngac.pm.core.pap.query.OperationsQuerier;
import gov.nist.ngac.pm.core.pap.query.PolicyQuerier;
import gov.nist.ngac.pm.core.pap.query.PolicyQuery;
import gov.nist.ngac.pm.core.pap.query.ProhibitionsQuerier;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import gov.nist.ngac.pm.core.pap.serialization.PolicyDeserializer;
import gov.nist.ngac.pm.core.pap.serialization.PolicySerializer;
import gov.nist.ngac.pm.core.pap.store.PolicyStore;
import gov.nist.ngac.pm.core.pdp.bootstrap.PolicyBootstrapper;
import java.util.HashSet;
import java.util.List;

/**
 * PAP exposes the APIs used to interact with the policy in the PIP.
 */
public abstract class PAP implements OperationExecutor, Transactional {

    private PolicyStore policyStore;
    private PolicyModifier modifier;
    private PolicyQuerier querier;
    private JavaOperationRegistry javaOperationRegistry;

    public PAP(PolicyStore policyStore) throws PMException {
        this(policyStore, new JavaOperationRegistry());
    }

    public PAP(PolicyStore policyStore, JavaOperationRegistry javaOperationRegistry) throws PMException {
        this.javaOperationRegistry = javaOperationRegistry;

        // OperationsQuerier resolves its own cross-references (an operation body invoking another operation)
        // by passing itself as the OperationsQuery, so it needs no outside handle back to this PAP.
        // ObligationsQuerier borrows that same capability for obligations, which can invoke operations too.
        OperationsQuerier operationsQuerier = new OperationsQuerier(policyStore, javaOperationRegistry);
        this.querier = new PolicyQuerier(
            new GraphQuerier(policyStore),
            new ProhibitionsQuerier(policyStore),
            new ObligationsQuerier(policyStore, operationsQuerier),
            operationsQuerier,
            new AccessQuerier(policyStore)
        );
        this.modifier = new PolicyModifier(
            new GraphModifier(policyStore, new RandomIdGenerator()),
            new ProhibitionsModifier(policyStore),
            new ObligationsModifier(policyStore),
            new OperationsModifier(policyStore, javaOperationRegistry)
        );
        this.policyStore = policyStore;

        // verify admin policy
        AdminPolicy.verifyAdminPolicy(policyStore().graph());

        // fail-fast: every persisted Java-operation reference must have a live implementation
        // registered in the supplied registry
        validateJavaOperationsAreRegistered();
    }

    protected PAP(PAP pap) throws PMException {
        this.policyStore = pap.policyStore();
        this.modifier = pap.modifier;
        this.querier = pap.querier;
        this.javaOperationRegistry = pap.javaOperationRegistry;
    }

    public PAP withPolicyStore(PolicyStore policyStore) {
        this.policyStore = policyStore;
        return this;
    }

    public PAP withPolicyModifier(PolicyModifier policyModifier) {
        this.modifier = policyModifier;
        return this;
    }

    public PAP withPolicyQuerier(PolicyQuerier policyQuerier) {
        this.querier = policyQuerier;
        return this;
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

    public JavaOperationRegistry javaOperations() {
        return javaOperationRegistry;
    }

    public PAP withIdGenerator(IdGenerator idGenerator) {
        this.modifier.graph().setIdGenerator(idGenerator);
        return this;
    }

    public void reset() throws PMException {
        policyStore.reset();
        AdminPolicy.verifyAdminPolicy(policyStore().graph());
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
        runTx(tx -> {
            // verify the admin nodes exist in the policy
            AdminPolicy.verifyAdminPolicy(tx.policyStore().graph());

            // call bootstrapper
            bootstrapper.bootstrap(tx);
        });
    }

    @Override
    public Object executeOperation(Operation<?> operation, UserContext userCtx, Args args) throws PMException {
        return operation.execute(this, userCtx, args);
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
     * {@link gov.nist.ngac.pm.core.pap.admin.AdminPolicyNode} nodes are assumed to be created and can be referenced in
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
        } catch (Exception e) {
            rollback();
            throw toPMException(e);
        }

        commit();
    }

    public Object executePML(UserContext author, String input) throws PMException {
        List<PMLStatement<?>> compiledPML = compilePML(input);

        ExecutionContext ctx = new ExecutionContext(author, this);
        StatementResult statementResult = ctx.executeStatements(compiledPML, new Args());

        if (statementResult instanceof ReturnResult returnResult) {
            return returnResult.getValue();
        }

        return null;
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
        } catch (Exception e) {
            rollback();
            throw toPMException(e);
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

    private static PMException toPMException(Exception e) {
        if (e instanceof PMException pmException) {
            return pmException;
        }
        return new PMException(e);
    }

    private boolean isPolicyEmpty() throws PMException {
        HashSet<Node> nodes = new HashSet<>(query().graph().search(ANY, NO_PROPERTIES));

        boolean prohibitionsEmpty = query().prohibitions().getProhibitions().isEmpty();
        boolean obligationsEmpty = query().obligations().getObligations().isEmpty();
        boolean resOpsEmpty = query().operations().getResourceAccessRights().isEmpty();

        // ignore admin nodes
        nodes.removeIf(n -> AdminPolicyNode.isAdminPolicyNode(n.getId()));

        // ignore the always-present protected built-ins that query().operations().getOperations() now
        // includes; only persisted rows count towards emptiness
        boolean opsEmpty = policyStore().operations().getOperationNames().isEmpty();

        return nodes.isEmpty()
            && prohibitionsEmpty
            && obligationsEmpty
            && resOpsEmpty
            && opsEmpty;
    }

    /**
     * Cross-check every persisted Java-operation reference against {@link #javaOperationRegistry}, throwing
     * immediately if any name has no live implementation registered.
     */
    private void validateJavaOperationsAreRegistered() throws PMException {
        for (Operation<?> operation : query().operations().getOperations()) {
            if (operation instanceof PMLOperation) {
                continue;
            }

            // return value ignored: get() throws OperationDoesNotExistException if unregistered, which is all we need here
            javaOperationRegistry.get(operation.getName());
        }
    }
}
