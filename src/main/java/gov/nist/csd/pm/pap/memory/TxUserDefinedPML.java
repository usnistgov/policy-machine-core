package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.policy.UserDefinedPML;
import gov.nist.csd.pm.policy.events.PolicyEvent;
import gov.nist.csd.pm.policy.events.userdefinedpml.CreateConstantEvent;
import gov.nist.csd.pm.policy.events.userdefinedpml.CreateFunctionEvent;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.exceptions.PMRuntimeException;
import gov.nist.csd.pm.policy.pml.value.Value;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;

import java.util.List;
import java.util.Map;

public class TxUserDefinedPML implements UserDefinedPML, BaseMemoryTx {

    private final TxPolicyEventTracker txPolicyEventTracker;
    private final MemoryUserDefinedPMLStore memoryUserDefinedPMLStore;

    public TxUserDefinedPML(TxPolicyEventTracker txPolicyEventTracker, MemoryUserDefinedPMLStore memoryUserDefinedPMLStore) {
        this.txPolicyEventTracker = txPolicyEventTracker;
        this.memoryUserDefinedPMLStore = memoryUserDefinedPMLStore;
    }

    @Override
    public void rollback() {
        List<PolicyEvent> events = txPolicyEventTracker.getEvents();
        for (PolicyEvent event : events) {
            try {
                TxCmd<MemoryUserDefinedPMLStore> txCmd = (TxCmd<MemoryUserDefinedPMLStore>) TxCmd.eventToCmd(event);
                txCmd.rollback(memoryUserDefinedPMLStore);
            } catch (PMException e) {
                // throw runtime exception because there is noway back if the rollback fails
                throw new PMRuntimeException("", e);
            }
        }
    }

    @Override
    public void createFunction(FunctionDefinitionStatement functionDefinitionStatement) {
        txPolicyEventTracker.trackPolicyEvent(new CreateFunctionEvent(functionDefinitionStatement));
    }

    @Override
    public void deleteFunction(String functionName) throws PMException {
        txPolicyEventTracker.trackPolicyEvent(new TxEvents.MemoryDeleteFunctionEvent(memoryUserDefinedPMLStore.getFunctions().get(functionName)));
    }

    @Override
    public Map<String, FunctionDefinitionStatement> getFunctions() {
        return null;
    }

    @Override
    public FunctionDefinitionStatement getFunction(String name) {
        return null;
    }

    @Override
    public void createConstant(String constantName, Value constantValue) {
        txPolicyEventTracker.trackPolicyEvent(new CreateConstantEvent(constantName, constantValue));
    }

    @Override
    public void deleteConstant(String constName) throws PMException {
        txPolicyEventTracker.trackPolicyEvent(new TxEvents.MemoryDeleteConstantEvent(constName, memoryUserDefinedPMLStore.getConstants().get(constName)));
    }

    @Override
    public Map<String, Value> getConstants() {
        return null;
    }

    @Override
    public Value getConstant(String name) {
        return null;
    }
}
