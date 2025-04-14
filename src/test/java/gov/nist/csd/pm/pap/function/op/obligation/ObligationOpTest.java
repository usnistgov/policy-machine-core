package gov.nist.csd.pm.pap.function.op.obligation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.op.PrivilegeChecker;
import gov.nist.csd.pm.pap.pml.pattern.arg.InArgPattern;
import gov.nist.csd.pm.pap.pml.pattern.arg.NodeArgPattern;
import gov.nist.csd.pm.pap.pml.pattern.subject.LogicalSubjectPatternExpression;
import gov.nist.csd.pm.pap.pml.pattern.subject.SubjectPattern;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pdp.UnauthorizedException;
import gov.nist.csd.pm.util.TestPAP;
import gov.nist.csd.pm.util.TestUserContext;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.pap.function.op.obligation.ObligationOp.checkPatternPrivileges;
import static gov.nist.csd.pm.pap.admin.AdminAccessRights.CREATE_OBLIGATION;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
                associate "ua1" and PM_ADMIN_OBJECT with ["*a"]
                
                create u "u1" in ["ua1"]
                create u "u2" in ["ua2"]
                create o "o1" in ["oa1"]
                """);

        checkPatternPrivileges(pap, new TestUserContext("u1"), new SubjectPattern(), CREATE_OBLIGATION);
        assertThrows(UnauthorizedException.class,
                () -> checkPatternPrivileges(pap, new TestUserContext("u2"), new SubjectPattern(), CREATE_OBLIGATION));

        checkPatternPrivileges(pap, new TestUserContext("u1"), new LogicalSubjectPatternExpression(
                new NodeArgPattern("oa1"),
                new InArgPattern("oa2"),
                true
        ), CREATE_OBLIGATION);

    }

}