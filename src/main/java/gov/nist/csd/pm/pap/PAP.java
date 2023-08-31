package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.policy.*;
import gov.nist.csd.pm.policy.exceptions.BootstrapExistingPolicyException;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.PMLExecutable;
import gov.nist.csd.pm.policy.pml.PMLExecutor;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.tx.Transactional;

import java.util.HashSet;
import java.util.Set;

import static gov.nist.csd.pm.pap.AdminPolicy.ALL_NODE_NAMES;
import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.ANY;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.NO_PROPERTIES;

public class PAP implements Transactional, PMLExecutable, Policy {

    protected PolicyStore policyStore;

    public PAP(PolicyStore policyStore) throws PMException {
        this.policyStore = policyStore;
    }

    public void bootstrap(PolicyBootstrapper bootstrapper) throws PMException {
        if(!isPolicyEmpty()) {
            throw new BootstrapExistingPolicyException();
        }

        bootstrapper.bootstrap(this);
    }

    private boolean isPolicyEmpty() throws PMException {
        Set<String> nodes = new HashSet<>(policyStore.graph().search(ANY, NO_PROPERTIES));

        boolean prohibitionsEmpty = policyStore.prohibitions().getAll().isEmpty();
        boolean obligationsEmpty = policyStore.obligations().getAll().isEmpty();

        return (nodes.isEmpty() || (nodes.size() == ALL_NODE_NAMES.size() && nodes.containsAll(ALL_NODE_NAMES))) &&
                prohibitionsEmpty &&
                obligationsEmpty;
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
        void runTx(PAP pap);
    }

    @Override
    public Graph graph() {
        return policyStore.graph();
    }

    @Override
    public Prohibitions prohibitions() {
        return policyStore.prohibitions();
    }

    @Override
    public Obligations obligations() {
        return policyStore.obligations();
    }

    @Override
    public UserDefinedPML userDefinedPML() {
        return policyStore.userDefinedPML();
    }

    @Override
    public PolicySerializer serialize() throws PMException {
        return policyStore.serialize();
    }

    @Override
    public PolicyDeserializer deserialize() throws PMException {
        return new Deserializer(policyStore);
    }

    @Override
    public void reset() throws PMException {
        policyStore.reset();
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

    @Override
    public void executePML(UserContext userContext, String input, FunctionDefinitionStatement... functionDefinitionStatements) throws PMException {
        PMLExecutor.compileAndExecutePML(this, userContext, input, functionDefinitionStatements);
    }

    @Override
    public void executePMLFunction(UserContext userContext, String functionName, Value... args) throws PMException {
        String pml = String.format("%s(%s)", functionName, PMLExecutable.valuesToArgs(args));

        // execute function as pml
        PMLExecutor.compileAndExecutePML(this, userContext, pml);
    }
}
