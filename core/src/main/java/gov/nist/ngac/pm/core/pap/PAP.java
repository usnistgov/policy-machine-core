/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

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
 * The main entry point for reading and modifying a policy.
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

    /**
     * Swaps this PAP's backing policy store.
     *
     * @return this instance, for chaining
     */
    public PAP withPolicyStore(PolicyStore policyStore) {
        this.policyStore = policyStore;
        return this;
    }

    /**
     * Swaps this PAP's policy modifier.
     *
     * @return this instance, for chaining
     */
    public PAP withPolicyModifier(PolicyModifier policyModifier) {
        this.modifier = policyModifier;
        return this;
    }

    /**
     * Swaps this PAP's policy querier.
     *
     * @return this instance, for chaining
     */
    public PAP withPolicyQuerier(PolicyQuerier policyQuerier) {
        this.querier = policyQuerier;
        return this;
    }

    /**
     * Returns the entry point for querying the current policy state.
     */
    public PolicyQuery query() {
        return querier;
    }

    /**
     * Returns the entry point for modifying the current policy state.
     */
    public PolicyModification modify() {
        return modifier;
    }

    /**
     * Returns the backing policy store.
     */
    public PolicyStore policyStore() {
        return policyStore;
    }

    /**
     * Returns the registry of Java operation implementations for this PAP.
     */
    public JavaOperationRegistry javaOperations() {
        return javaOperationRegistry;
    }

    /**
     * Sets the id generator used when creating new nodes.
     *
     * @return this instance, for chaining
     */
    public PAP withIdGenerator(IdGenerator idGenerator) {
        this.modifier.graph().setIdGenerator(idGenerator);
        return this;
    }

    /**
     * Clears all policy state and re-verifies the admin policy structure.
     *
     * @throws PMException if resetting the store or re-verifying the admin policy fails
     */
    public void reset() throws PMException {
        policyStore.reset();
        AdminPolicy.verifyAdminPolicy(policyStore().graph());
    }

    /**
     * Builds an {@link ExecutionContext} for running PML statements as the given user against this PAP.
     */
    public ExecutionContext buildExecutionContext(UserContext userCtx) throws PMException {
        return new ExecutionContext(userCtx, this);
    }

    /**
     * Bootstraps an empty policy using the given {@link PolicyBootstrapper}.
     *
     * @param bootstrapper the bootstrapper that builds the initial policy
     * @throws PMException if the policy is not empty, or if bootstrapping fails
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
     * Serializes the current policy state using the given serializer.
     *
     * @param serializer the serializer to use
     * @return the serialized policy
     * @throws PMException if serialization fails
     */
    public String serialize(PolicySerializer serializer) throws PMException {
        return serializer.serialize(query());
    }

    /**
     * Deserializes the given input into the current policy state, first resetting existing state. Admin
     * policy nodes are assumed to already exist and must not be created by the input.
     *
     * @param input the policy content to deserialize
     * @param policyDeserializer the deserializer to apply
     * @throws PMException if deserialization fails
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

    /**
     * Compiles and executes a PML script as the given user.
     *
     * @param author the user to execute the script as
     * @param input the PML source to compile and execute
     * @return the script's return value, or null if it returns nothing
     * @throws PMException if compilation or execution fails
     */
    public Object executePML(UserContext author, String input) throws PMException {
        List<PMLStatement<?>> compiledPML = compilePML(input);

        ExecutionContext ctx = new ExecutionContext(author, this);
        StatementResult statementResult = ctx.executeStatements(compiledPML, new Args());

        if (statementResult instanceof ReturnResult returnResult) {
            return returnResult.getValue();
        }

        return null;
    }

    /**
     * Compiles a PML script against this PAP's current policy state without executing it.
     *
     * @param input the PML source to compile
     * @return the compiled statements
     * @throws PMException if compilation fails
     */
    public List<PMLStatement<?>> compilePML(String input) throws PMException {
        PMLCompiler pmlCompiler = new PMLCompiler();

        return pmlCompiler.compilePML(this, input);
    }

    /**
     * Runs the given task in a transaction, committing on success or rolling back on failure.
     *
     * @param tx the task to run
     * @throws PMException if the task fails, after the transaction has been rolled back
     */
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

    /**
     * A task run against the active PAP.
     */
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
     * Verifies every persisted Java operation has a registered implementation.
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
