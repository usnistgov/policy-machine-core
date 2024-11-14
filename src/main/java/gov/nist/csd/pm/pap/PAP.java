package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.pap.admin.AdminPolicy;
import gov.nist.csd.pm.pap.executable.AdminExecutable;
import gov.nist.csd.pm.pap.executable.AdminExecutor;
import gov.nist.csd.pm.pap.modification.PolicyModification;
import gov.nist.csd.pm.pap.pml.PMLCompiler;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLOperation;
import gov.nist.csd.pm.pap.pml.executable.routine.PMLRoutine;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.query.PolicyQuery;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pap.serialization.PolicyDeserializer;
import gov.nist.csd.pm.pap.serialization.PolicySerializer;
import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.store.PolicyStore;
import gov.nist.csd.pm.pap.tx.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class PAP implements AdminExecutor, Transactional {

    protected final PolicyStore policyStore;
    private final PolicyModifier modifier;

    private Map<String, PMLOperation> pmlOperations;
    private Map<String, PMLRoutine> pmlRoutines;
    private Map<String, Value> pmlConstants;

    public PAP(PolicyStore policyStore) throws PMException {
        this.policyStore = policyStore;
        this.modifier = new PolicyModifier(policyStore);
        AdminPolicy.verify(modifier);

        this.pmlOperations = new HashMap<>();
        this.pmlRoutines = new HashMap<>();
        this.pmlConstants = new HashMap<>();
    }

    public PAP(PAP pap) throws PMException {
        this(pap.policyStore);
    }

    public PolicyStore policyStore() {
        return policyStore;
    }

    public abstract PolicyQuery query();

    public PolicyModification modify() {
        return modifier;
    }

    public void reset() throws PMException {
        policyStore.reset();

        AdminPolicy.verify(modifier);
    }

    @Override
    public Object executeAdminExecutable(AdminExecutable<?> adminExecutable, Map<String, Object> operands) throws PMException {
        return adminExecutable.execute(this, operands);
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
        this.pmlOperations = pmlOperations;
    }

    public void setPMLOperations(PMLOperation... operations) throws PMException {
        for (PMLOperation operation : operations) {
            this.pmlOperations.put(operation.getName(), operation);
        }
    }

    public void setPMLRoutines(Map<String, PMLRoutine> pmlRoutines) throws PMException {
        this.pmlRoutines = pmlRoutines;
    }

    public void setPMLRoutines(PMLRoutine... routines) throws PMException {
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

    public void setPMLConstants(Map<String, Value> pmlConstants) throws PMException {
        this.pmlConstants = pmlConstants;
    }

    public Map<String, Value> getPMLConstants() throws PMException {
        return pmlConstants;
    }

    public void executePML(UserContext author, String input) throws PMException {
        PMLCompiler pmlCompiler = new PMLCompiler(this);

        List<PMLStatement> compiledPML = pmlCompiler.compilePML(input);

        // execute other statements
        ExecutionContext ctx = new ExecutionContext(author, this);

        ctx.executeStatements(compiledPML, Map.of());
    }

    public void runTx(TxRunner txRunner) throws PMException {
        beginTx();

        try {
            txRunner.runTx(this);

            commit();
        } catch (PMException e) {
            rollback();
            throw e;
        }
    }

    public interface TxRunner {
        void runTx(PAP pap) throws PMException;
    }
}
