package gov.nist.csd.pm.common.op.obligation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.pml.pattern.operand.InOperandPattern;
import gov.nist.csd.pm.pap.pml.pattern.operand.NodeOperandPattern;
import gov.nist.csd.pm.pap.pml.pattern.subject.LogicalSubjectPatternExpression;
import gov.nist.csd.pm.pap.pml.pattern.subject.SubjectPattern;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pdp.UnauthorizedException;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.common.op.obligation.ObligationOp.checkPatternPrivileges;
import static gov.nist.csd.pm.pap.AdminAccessRights.CREATE_OBLIGATION;
import static org.junit.jupiter.api.Assertions.*;

class ObligationOpTest {

    @Test
    void testCheckPatternPrivileges() throws PMException {
        MemoryPAP pap = new MemoryPAP();

        pap.executePML(new UserContext("u1"), """
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

        PrivilegeChecker privilegeChecker = new PrivilegeChecker(pap);

        checkPatternPrivileges(privilegeChecker, new UserContext("u1"), new SubjectPattern(), CREATE_OBLIGATION);
        assertThrows(UnauthorizedException.class,
                () -> checkPatternPrivileges(privilegeChecker, new UserContext("u2"), new SubjectPattern(), CREATE_OBLIGATION));

        checkPatternPrivileges(privilegeChecker, new UserContext("u1"), new LogicalSubjectPatternExpression(
                new NodeOperandPattern("oa1"),
                new InOperandPattern("oa2"),
                true
        ), CREATE_OBLIGATION);

    }

}