package gov.nist.csd.pm.pdp.adjudicator;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.PolicyReviewer;
import gov.nist.csd.pm.policy.author.PALAuthor;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.author.pal.PALContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;

import java.util.Map;

import static gov.nist.csd.pm.pap.SuperPolicy.SUPER_OBJECT;
import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.*;

class PAL implements PALAuthor {

    private final UserContext userCtx;
    private final AccessRightChecker accessRightChecker;

    public PAL(UserContext userCtx, PAP pap, PolicyReviewer policyReviewer) {
        this.userCtx = userCtx;
        this.accessRightChecker = new AccessRightChecker(pap, policyReviewer);
    }

    @Override
    public void addFunction(FunctionDefinitionStatement functionDefinitionStatement) throws PMException {
        accessRightChecker.check(userCtx, SUPER_OBJECT, ADD_FUNCTION);
    }

    @Override
    public void removeFunction(String functionName) throws PMException {
        accessRightChecker.check(userCtx, SUPER_OBJECT, REMOVE_FUNCTION);
    }

    @Override
    public Map<String, FunctionDefinitionStatement> getFunctions() throws PMException {
        accessRightChecker.check(userCtx, SUPER_OBJECT, GET_FUNCTIONS);

        return null;
    }

    @Override
    public void addConstant(String constantName, Value constantValue) throws PMException {
        accessRightChecker.check(userCtx, SUPER_OBJECT, ADD_CONSTANT);
    }

    @Override
    public void removeConstant(String constName) throws PMException {
        accessRightChecker.check(userCtx, SUPER_OBJECT, REMOVE_CONSTANT);
    }

    @Override
    public Map<String, Value> getConstants() throws PMException {
        accessRightChecker.check(userCtx, SUPER_OBJECT, GET_CONSTANTS);

        return null;
    }

    @Override
    public PALContext getContext() throws PMException {
        accessRightChecker.check(userCtx, SUPER_OBJECT, GET_CONTEXT);

        return null;
    }
}
