package gov.nist.csd.pm.core.pap.function.op.obligation;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.CREATE_OBLIGATION;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.CREATE_OBLIGATION_WITH_ANY_PATTERN;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.obligation.EventPattern;
import gov.nist.csd.pm.core.pap.obligation.Rule;
import gov.nist.csd.pm.core.pap.pml.pattern.OperationPattern;
import gov.nist.csd.pm.core.pap.pml.pattern.subject.InSubjectPatternExpression;
import gov.nist.csd.pm.core.pap.pml.pattern.subject.LogicalSubjectPatternExpression;
import gov.nist.csd.pm.core.pap.pml.pattern.subject.SubjectPattern;
import gov.nist.csd.pm.core.pap.pml.pattern.subject.UsernamePatternExpression;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.UnauthorizedException;
import gov.nist.csd.pm.core.util.TestPAP;
import gov.nist.csd.pm.core.util.TestUserContext;
import java.util.List;
import org.junit.jupiter.api.Test;

class ObligationOpTest {

    @Test
    void testCheckPatternPrivileges() throws PMException {
        PAP pap = new TestPAP();

        pap.executePML(new UserContext(6), """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create ua "ua2" in ["pc1"]
                create oa "oa1" in ["pc1"]
                create oa "oa2" in ["pc1"]
                
                associate "ua1" and "oa1" with ["*a"]
                associate "ua1" and "oa2" with ["*a"]
                associate "ua1" and "ua2" with ["*a"]
                associate "ua1" and PM_ADMIN_BASE_OA with ["*a"]
                
                create u "u1" in ["ua1", "ua2"]
                create u "u2" in ["ua2"]
                create o "o1" in ["oa1"]
                """);

        Rule testRule = new Rule("", new EventPattern(new SubjectPattern(), new OperationPattern()), null);
        ObligationOp.checkObligationRulePrivileges(pap, new TestUserContext("u1"), List.of(testRule), CREATE_OBLIGATION, CREATE_OBLIGATION_WITH_ANY_PATTERN);
        assertThrows(UnauthorizedException.class,
                () -> ObligationOp.checkObligationRulePrivileges(pap, new TestUserContext("u2"), List.of(testRule), CREATE_OBLIGATION, CREATE_OBLIGATION_WITH_ANY_PATTERN));

        Rule testRule2 = new Rule("", new EventPattern(new SubjectPattern(new LogicalSubjectPatternExpression(
            new UsernamePatternExpression("u1"),
            new InSubjectPatternExpression("ua1"),
            true
        )), new OperationPattern()), null);
        ObligationOp.checkObligationRulePrivileges(pap, new TestUserContext("u1"), List.of(testRule2), CREATE_OBLIGATION, CREATE_OBLIGATION_WITH_ANY_PATTERN);
    }

}