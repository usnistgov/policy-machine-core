package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.common.exception.BootstrapExistingPolicyException;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.exception.PMRuntimeException;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.AdminFunction;
import gov.nist.csd.pm.pap.function.AdminFunctionExecutor;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.common.tx.Transactional;
import gov.nist.csd.pm.pap.admin.AdminPolicy;
import gov.nist.csd.pm.pap.id.IdGenerator;
import gov.nist.csd.pm.pap.id.RandomIdGenerator;
import gov.nist.csd.pm.pap.modification.PolicyModification;
import gov.nist.csd.pm.pap.pml.PMLCompiler;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.function.operation.PMLOperation;
import gov.nist.csd.pm.pap.pml.function.routine.PMLRoutine;
import gov.nist.csd.pm.pap.pml.scope.FunctionAlreadyDefinedInScopeException;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.query.PolicyQuery;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pap.serialization.PolicyDeserializer;
import gov.nist.csd.pm.pap.serialization.PolicySerializer;
import gov.nist.csd.pm.pap.store.PolicyStore;
import gov.nist.csd.pm.pdp.bootstrap.PolicyBootstrapper;

import java.util.*;
import java.util.stream.Collectors;

import static gov.nist.csd.pm.common.graph.node.NodeType.ANY;
import static gov.nist.csd.pm.common.graph.node.Properties.NO_PROPERTIES;
import static gov.nist.csd.pm.pap.admin.AdminPolicy.ALL_NODES;
import static gov.nist.csd.pm.pap.admin.AdminPolicy.ALL_NODE_NAMES;

public abstract class PAP implements AdminFunctionExecutor, Transactional {

    protected final PolicyStore policyStore;
    private final PolicyModifier modifier;
    private Map<String, PMLOperation> pmlOperations;
    private Map<String, PMLRoutine> pmlRoutines;
    private Map<String, Value> pmlConstants;
    private IdGenerator idGenerator;

    public PAP(PolicyStore policyStore) throws PMException {
        this.idGenerator = new RandomIdGenerator();

        this.policyStore = policyStore;
        this.modifier = new PolicyModifier(policyStore, idGenerator);
        this.pmlOperations = new HashMap<>();
        this.pmlRoutines = new HashMap<>();
        this.pmlConstants = new HashMap<>();

        // verify admin policy
        this.policyStore.verifyAdminPolicy();
    }

    public PAP(PAP pap) throws PMException {
        this(pap.policyStore);
        this.withIdGenerator(pap.idGenerator);
    }

    public PAP withIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
        this.modifier.setIdGenerator(idGenerator);
        return this;
    }

    public PolicyStore policyStore() {
        return policyStore;
    }

    public IdGenerator idGenerator() {
        return this.idGenerator;
    }

    public abstract PolicyQuery query();

    public PolicyModification modify() {
        return modifier;
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
     * @param bootstrapUser the name of the user bootstrapping the policy.
     * @param bootstrapper the PolicyBootstrapper that will build the custom bootstrap policy.
     * @throws PMException if there is an error bootstrapping.
     */
    public void bootstrap(String bootstrapUser, PolicyBootstrapper bootstrapper) throws PMException {
        if(!isPolicyEmpty()) {
            throw new BootstrapExistingPolicyException();
        }

        runTx(tx -> {
            // create bootstrap policy and user
            long pc = tx.modify().graph().createPolicyClass("bootstrap");
            long ua = tx.modify().graph().createUserAttribute("bootstrapper", List.of(pc));
            long bootstrapUserId = tx.modify().graph().createUserAttribute(bootstrapUser, List.of(ua));

            // execute the bootstrapper
            bootstrapper.bootstrap(new UserContext(bootstrapUserId), tx);

            // clean up bootstrap policy
            tx.modify().graph().deassign(bootstrapUserId, List.of(ua));
            tx.modify().graph().deleteNode(ua);
            tx.modify().graph().deleteNode(pc);
        });
    }

    @Override
    public <T> T executeAdminFunction(AdminFunction<T> adminFunction, Args args) throws PMException {
        return adminFunction.execute(this, args);
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
     * @param author The UserContext describing the author of the deserialized policy elements.
     * @param input The string representation of the policy to deserialize.
     * @param policyDeserializer The PolicyDeserializer to apply the input string to the policy.
     * @throws PMException If there is an error deserializing the given inputs string.
     */
    public void deserialize(UserContext author, String input, PolicyDeserializer policyDeserializer) throws PMException {
        beginTx();

        try {
            policyDeserializer.deserialize(this, author, input);
        } catch (PMException e) {
            rollback();
            throw e;
        }

        commit();
    }

    public void setPMLOperations(Map<String, PMLOperation> pmlOperations) throws PMException {
        checkFunctionNames(pmlOperations.keySet());
        this.pmlOperations = pmlOperations;
    }

    public void setPMLOperations(PMLOperation... operations) throws PMException {
        checkFunctionNames(Arrays.stream(operations).map(PMLOperation::getName).collect(Collectors.toSet()));
        for (PMLOperation operation : operations) {
            this.pmlOperations.put(operation.getName(), operation);
        }
    }

    public void setPMLRoutines(Map<String, PMLRoutine> pmlRoutines) throws PMException {
        checkFunctionNames(pmlRoutines.keySet());
        this.pmlRoutines = pmlRoutines;
    }

    public void setPMLRoutines(PMLRoutine... routines) throws PMException {
        checkFunctionNames(Arrays.stream(routines).map(PMLRoutine::getName).collect(Collectors.toSet()));
        for (PMLRoutine routine : routines) {
            this.pmlRoutines.put(routine.getName(), routine);
        }
    }

    public Map<String, PMLOperation> getPMLOperations() throws PMException {
        return pmlOperations;
    }

    public Map<String, PMLRoutine> getPMLRoutines() throws PMException {
        return pmlRoutines;
    }

    private void checkFunctionNames(Collection<String> names) {
        // check no operations conflict with existing operations in the PAP
        pmlOperations.keySet().forEach(name -> {
            if (!names.contains(name)) {
                return;
            }

            try {
                throw new FunctionAlreadyDefinedInScopeException(name);
            } catch (FunctionAlreadyDefinedInScopeException e) {
                throw new PMRuntimeException(e);
            }
        });
    }

    public void setPMLConstants(Map<String, Value> pmlConstants) throws PMException {
        this.pmlConstants = pmlConstants;
    }

    public Map<String, Value> getPMLConstants() throws PMException {
        return pmlConstants;
    }

    public void executePML(UserContext author, String input) throws PMException {
        PMLCompiler pmlCompiler = new PMLCompiler();

        List<PMLStatement> compiledPML = pmlCompiler.compilePML(this, input);

        ExecutionContext ctx = new ExecutionContext(author, this);

        ctx.executeStatements(compiledPML, new Args());
    }

    public List<PMLStatement> compilePML(String input) throws PMException {
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
