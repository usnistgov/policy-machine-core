package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.policy.*;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.PMLExecutable;
import gov.nist.csd.pm.policy.pml.PMLExecutor;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.tx.Transactional;

import java.util.List;

public class PAP implements Transactional, PMLExecutable, Policy {

    protected PolicyStore policyStore;

    public PAP(PolicyStore policyStore) throws PMException {
        this.policyStore = policyStore;

        // enable policy class admin by default
        AdminPolicy.enable(policyStore);
    }

    public void bootstrap(PolicyBootstrapper bootstrapper) throws PMException {
        if(!isPolicyEmpty()) {
            throw new PMException("policy is not empty");
        }

        AdminPolicy.enable(policyStore);

        bootstrapper.bootstrap(this);
    }

    private boolean isPolicyEmpty() throws PMException {
        List<String> policyClasses = policyStore.graph().getPolicyClasses();
        policyClasses.remove(AdminPolicy.ADMIN_POLICY);
        return policyClasses.isEmpty();
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
