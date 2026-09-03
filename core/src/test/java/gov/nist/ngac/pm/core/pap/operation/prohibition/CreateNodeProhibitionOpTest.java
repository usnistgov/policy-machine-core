/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pap.operation.prohibition;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.VOID_TYPE;
import static gov.nist.ngac.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pdp.UnauthorizedException;
import gov.nist.ngac.pm.core.util.TestPAP;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;

class CreateNodeProhibitionOpTest {

    @Test
    void testMetadata() {
        CreateNodeProhibitionOp op = new CreateNodeProhibitionOp();
        assertEquals("create_node_prohibition", op.getName());
        assertEquals(VOID_TYPE, op.getReturnType());
        assertEquals(6, op.getFormalParameters().size());
        assertEquals("name", op.getFormalParameters().get(0).getName());
        assertEquals("node_id", op.getFormalParameters().get(1).getName());
        assertEquals("arset", op.getFormalParameters().get(2).getName());
        assertEquals("inclusion_set", op.getFormalParameters().get(3).getName());
        assertEquals("exclusion_set", op.getFormalParameters().get(4).getName());
        assertEquals("is_conjunctive", op.getFormalParameters().get(5).getName());
        assertNotNull(op.getRequiredCapabilities());
        assertFalse(op.getRequiredCapabilities().isEmpty());
    }

    @Test
    void testCanExecuteWhenAuthorized() throws PMException {
        MemoryPAP pap = new TestPAP();
        String pml = """
                set resource access rights ["read"]
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create ua "ua2" in ["pc1"]
                create oa "oa1" in ["pc1"]
                associate "ua1" to "ua2" with ["admin:prohibition:node:create"]
                associate "ua1" to "oa1" with ["admin:prohibition:inclusion:create"]
                create u "u1" in ["ua1"]
                """;
        pap.executePML(NodeUserContext.of(id("u1")), pml);

        CreateNodeProhibitionOp op = new CreateNodeProhibitionOp();
        Args args = op.validateArgs(Map.of(
                "name", "pro1",
                "node_id", id("ua2"),
                "arset", List.of("read"),
                "inclusion_set", List.of(id("oa1")),
                "exclusion_set", List.of(),
                "is_conjunctive", false
        ));
        op.canExecute(pap, NodeUserContext.of(id("u1")), args);
    }

    @Test
    void testCanExecuteWhenUnauthorized() throws PMException {
        MemoryPAP pap = new TestPAP();
        String pml = """
                set resource access rights ["read"]
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create ua "ua2" in ["pc1"]
                create oa "oa1" in ["pc1"]
                associate "ua1" to "oa1" with ["read"]
                create u "u1" in ["ua1"]
                create u "u2" in ["ua2"]
                """;
        pap.executePML(NodeUserContext.of(id("u1")), pml);

        CreateNodeProhibitionOp op = new CreateNodeProhibitionOp();
        Args args = op.validateArgs(Map.of(
                "name", "pro1",
                "node_id", id("ua2"),
                "arset", List.of("read"),
                "inclusion_set", List.of(id("oa1")),
                "exclusion_set", List.of(),
                "is_conjunctive", false
        ));
        assertThrows(UnauthorizedException.class, () -> op.canExecute(pap, NodeUserContext.of(id("u2")), args));
    }
}
