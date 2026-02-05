package gov.nist.csd.pm.core.pap;

import static gov.nist.csd.pm.core.common.graph.node.NodeType.ANY;
import static gov.nist.csd.pm.core.common.graph.node.Properties.NO_PROPERTIES;

import gov.nist.csd.pm.core.common.exception.BootstrapExistingPolicyException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.common.tx.Transactional;
import gov.nist.csd.pm.core.pap.admin.AdminPolicy;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.id.IdGenerator;
import gov.nist.csd.pm.core.pap.id.RandomIdGenerator;
import gov.nist.csd.pm.core.pap.modification.GraphModifier;
import gov.nist.csd.pm.core.pap.modification.ObligationsModifier;
import gov.nist.csd.pm.core.pap.modification.OperationsModifier;
import gov.nist.csd.pm.core.pap.modification.PolicyModification;
import gov.nist.csd.pm.core.pap.modification.PolicyModifier;
import gov.nist.csd.pm.core.pap.modification.ProhibitionsModifier;
import gov.nist.csd.pm.core.pap.operation.Operation;
import gov.nist.csd.pm.core.pap.operation.OperationExecutor;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.pml.PMLCompiler;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.core.pap.pml.statement.result.ReturnResult;
import gov.nist.csd.pm.core.pap.pml.statement.result.StatementResult;
import gov.nist.csd.pm.core.pap.query.AccessQuerier;
import gov.nist.csd.pm.core.pap.query.GraphQuerier;
import gov.nist.csd.pm.core.pap.query.ObligationsQuerier;
import gov.nist.csd.pm.core.pap.query.OperationsQuerier;
import gov.nist.csd.pm.core.pap.query.PolicyQuerier;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import gov.nist.csd.pm.core.pap.query.ProhibitionsQuerier;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pap.serialization.PolicyDeserializer;
import gov.nist.csd.pm.core.pap.serialization.PolicySerializer;
import gov.nist.csd.pm.core.pap.store.PolicyStore;
import gov.nist.csd.pm.core.pdp.bootstrap.PolicyBootstrapper;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class PAP implements OperationExecutor, Transactional {

    private PolicyStore policyStore;
    private PolicyModifier modifier;
    private PolicyQuerier querier;
    private PluginRegistry pluginRegistry;

    public PAP(PolicyStore policyStore) throws PMException {
        this.pluginRegistry = new PluginRegistry();

        this.querier = new PolicyQuerier(
            new GraphQuerier(policyStore),
            new ProhibitionsQuerier(policyStore),
            new ObligationsQuerier(policyStore),
            new OperationsQuerier(policyStore, pluginRegistry),
            new AccessQuerier(policyStore)
        );
        this.modifier = new PolicyModifier(
            new GraphModifier(policyStore, new RandomIdGenerator()),
            new ProhibitionsModifier(policyStore),
            new ObligationsModifier(policyStore),
            new OperationsModifier(policyStore, pluginRegistry)
        );
        this.policyStore = policyStore;
        this.pluginRegistry.setOperationsQuery(this.querier.operations());

        // verify admin policy
        AdminPolicy.verifyAdminPolicy(policyStore().graph());
    }

    protected PAP(PAP pap) throws PMException {
        this.policyStore = pap.policyStore();
        this.modifier = pap.modifier;
        this.querier = pap.querier;
        this.pluginRegistry = pap.pluginRegistry;
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

    public PluginRegistry plugins() {
        return pluginRegistry;
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
    public Object executeOperation(Operation<?> operation, Args args) throws PMException {
        return operation.execute(this, args);
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
        boolean resOpsEmpty = query().operations().getResourceAccessRights().isEmpty();

        // ignore admin nodes
        nodes.removeIf(n -> AdminPolicyNode.isAdminPolicyNode(n.getId()));

        // ignore plugin registry ops
        Collection<String> operationNames = query().operations()
            .getOperations()
            .stream()
            .map(Operation::getName)
            .collect(Collectors.toSet());

        operationNames.removeAll(pluginRegistry.getOperationsList().stream().map(Operation::getName).toList());

        boolean opsEmpty = operationNames.isEmpty();

        return nodes.isEmpty()
            && prohibitionsEmpty
            && obligationsEmpty
            && resOpsEmpty
            && opsEmpty;
    }
}
