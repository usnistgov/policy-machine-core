package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.pap.AdminPolicy;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.policy.UserDefinedPML;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;

import java.util.Map;

import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.*;

public class AdjudicatorUserDefinedPML implements UserDefinedPML {
    private final UserContext userCtx;
    private final PAP pap;
    private final AccessRightChecker accessRightChecker;

    public AdjudicatorUserDefinedPML(UserContext userCtx, PAP pap, AccessRightChecker accessRightChecker) {
        this.userCtx = userCtx;
        this.pap = pap;
        this.accessRightChecker = accessRightChecker;
    }

    @Override
    public void createFunction(FunctionDefinitionStatement functionDefinitionStatement) throws PMException {
        accessRightChecker.check(userCtx, AdminPolicy.PML_FUNCTIONS_TARGET, ADD_FUNCTION);
    }

    @Override
    public void deleteFunction(String functionName) throws PMException {
        accessRightChecker.check(userCtx, AdminPolicy.PML_FUNCTIONS_TARGET, REMOVE_FUNCTION);
    }

    @Override
    public Map<String, FunctionDefinitionStatement> getFunctions() throws PMException {
        // accessRightChecker.check(userCtx, SUPER_PC_REP, GET_FUNCTIONS);

        return null;
    }

    @Override
    public FunctionDefinitionStatement getFunction(String name) throws PMException {
        return null;
    }

    @Override
    public void createConstant(String constantName, Value constantValue) throws PMException {
        accessRightChecker.check(userCtx, AdminPolicy.PML_CONSTANTS_TARGET, ADD_CONSTANT);
    }

    @Override
    public void deleteConstant(String constName) throws PMException {
        accessRightChecker.check(userCtx, AdminPolicy.PML_CONSTANTS_TARGET, REMOVE_CONSTANT);
    }

    @Override
    public Map<String, Value> getConstants() throws PMException {
        // accessRightChecker.check(userCtx, SUPER_PC_REP, GET_CONSTANTS);

        return null;
    }

    @Override
    public Value getConstant(String name) throws PMException {
        return null;
    }
}
