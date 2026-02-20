package gov.nist.csd.pm.core.pap.operation.reqcap;

import static gov.nist.csd.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.param.NodeIdFormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeIdListFormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeNameFormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeNameListFormalParameter;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.util.TestPAP;
import java.util.List;
import org.junit.jupiter.api.Test;

class RequiredPrivilegeOnParameterTest {

    @Test
    void testIsSatisfiedWithNodeIdParam() throws PMException {
        MemoryPAP pap = new TestPAP();
        String pml = """
                set resource access rights ["read"]
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create oa "oa1" in ["pc1"]
                associate "ua1" to "oa1" with ["read"]
                create u "u1" in ["ua1"]
                """;
        pap.executePML(new UserContext(id("u1")), pml);

        NodeIdFormalParameter param = new NodeIdFormalParameter("target");
        RequiredPrivilegeOnParameter reqPriv = new RequiredPrivilegeOnParameter(
            param, new AccessRightSet("read")
        );

        Args args = new Args();
        args.put(param, id("oa1"));

        assertTrue(reqPriv.isSatisfied(pap, new UserContext(id("u1")), args));
    }

    @Test
    void testIsSatisfiedWithNodeNameParam() throws PMException {
        MemoryPAP pap = new TestPAP();
        String pml = """
                set resource access rights ["read"]
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create oa "oa1" in ["pc1"]
                associate "ua1" to "oa1" with ["read"]
                create u "u1" in ["ua1"]
                """;
        pap.executePML(new UserContext(id("u1")), pml);

        NodeNameFormalParameter param = new NodeNameFormalParameter("target");
        RequiredPrivilegeOnParameter reqPriv = new RequiredPrivilegeOnParameter(
            param, new AccessRightSet("read")
        );

        Args args = new Args();
        args.put(param, "oa1");

        assertTrue(reqPriv.isSatisfied(pap, new UserContext(id("u1")), args));
    }

    @Test
    void testIsSatisfiedWithNodeIdListParam() throws PMException {
        MemoryPAP pap = new TestPAP();
        String pml = """
                set resource access rights ["read"]
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create oa "oa1" in ["pc1"]
                create oa "oa2" in ["pc1"]
                associate "ua1" to "oa1" with ["read"]
                associate "ua1" to "oa2" with ["read"]
                create u "u1" in ["ua1"]
                """;
        pap.executePML(new UserContext(id("u1")), pml);

        NodeIdListFormalParameter param = new NodeIdListFormalParameter("targets");
        RequiredPrivilegeOnParameter reqPriv = new RequiredPrivilegeOnParameter(
            param, new AccessRightSet("read")
        );

        Args args = new Args();
        args.put(param, List.of(id("oa1"), id("oa2")));

        assertTrue(reqPriv.isSatisfied(pap, new UserContext(id("u1")), args));
    }

    @Test
    void testIsSatisfiedWithNodeNameListParam() throws PMException {
        MemoryPAP pap = new TestPAP();
        String pml = """
                set resource access rights ["read"]
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create oa "oa1" in ["pc1"]
                create oa "oa2" in ["pc1"]
                associate "ua1" to "oa1" with ["read"]
                associate "ua1" to "oa2" with ["read"]
                create u "u1" in ["ua1"]
                """;
        pap.executePML(new UserContext(id("u1")), pml);

        NodeNameListFormalParameter param = new NodeNameListFormalParameter("targets");
        RequiredPrivilegeOnParameter reqPriv = new RequiredPrivilegeOnParameter(
            param, new AccessRightSet("read")
        );

        Args args = new Args();
        args.put(param, List.of("oa1", "oa2"));

        assertTrue(reqPriv.isSatisfied(pap, new UserContext(id("u1")), args));
    }

    @Test
    void testIsSatisfiedReturnsFalseWhenOneNodeFails() throws PMException {
        MemoryPAP pap = new TestPAP();
        String pml = """
                set resource access rights ["read"]
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create oa "oa1" in ["pc1"]
                create oa "oa2" in ["pc1"]
                associate "ua1" to "oa1" with ["read"]
                create u "u1" in ["ua1"]
                """;

        pap.executePML(new UserContext(id("u1")), pml);

        NodeNameListFormalParameter param = new NodeNameListFormalParameter("targets");
        RequiredPrivilegeOnParameter reqPriv = new RequiredPrivilegeOnParameter(
            param, new AccessRightSet("read")
        );

        Args args = new Args();
        args.put(param, List.of("oa1", "oa2"));

        assertFalse(reqPriv.isSatisfied(pap, new UserContext(id("u1")), args));
    }
}
