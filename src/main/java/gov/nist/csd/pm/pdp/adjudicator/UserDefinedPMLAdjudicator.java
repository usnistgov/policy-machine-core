package gov.nist.csd.pm.pdp.adjudicator;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.policy.UserDefinedPML;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;

import java.util.Map;

import static gov.nist.csd.pm.pap.SuperPolicy.SUPER_PC_REP;
import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.*;

public class UserDefinedPMLAdjudicator implements UserDefinedPML {
    private final UserContext userCtx;
    private final PAP pap;
    private final AccessRightChecker accessRightChecker;

    public UserDefinedPMLAdjudicator(UserContext userCtx, PAP pap, AccessRightChecker accessRightChecker) {
        this.userCtx = userCtx;
        this.pap = pap;
        this.accessRightChecker = accessRightChecker;
    }

    @Override
    public void addFunction(FunctionDefinitionStatement functionDefinitionStatement) throws PMException {
        accessRightChecker.check(userCtx, SUPER_PC_REP, ADD_FUNCTION);
    }

    @Override
    public void removeFunction(String functionName) throws PMException {
        accessRightChecker.check(userCtx, SUPER_PC_REP, REMOVE_FUNCTION);
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
    public void addConstant(String constantName, Value constantValue) throws PMException {
        accessRightChecker.check(userCtx, SUPER_PC_REP, ADD_CONSTANT);
    }

    @Override
    public void removeConstant(String constName) throws PMException {
        accessRightChecker.check(userCtx, SUPER_PC_REP, REMOVE_CONSTANT);
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
