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

package gov.nist.ngac.pm.core.pap.pml.operation.basic.builtin;

import static gov.nist.ngac.pm.core.pap.pml.operation.basic.PMLFunctionOperation.NODE_NAME_PARAM;
import static gov.nist.ngac.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertEquals;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.modification.GraphModification;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.pml.operation.builtin.GetAssociationsWithSource;
import gov.nist.ngac.pm.core.util.TestPAP;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;

class GetAssociationsWithSourceTest {

    @Test
    void testOk() throws PMException {
        PAP pap = new MemoryPAP();
        GraphModification graph = pap.modify().graph();
        long pc1 = graph.createPolicyClass("pc1");
        long ua1 = graph.createUserAttribute("ua1", List.of(pc1));
        long oa1 = graph.createObjectAttribute("oa1", List.of(pc1));
        graph.associate(ua1, oa1, new AccessRightSet("*"));

        GetAssociationsWithSource getAssociationsWithSource = new GetAssociationsWithSource();
        List<Map<String, Object>> result = getAssociationsWithSource.execute(pap.query(), null, new Args(Map.of(NODE_NAME_PARAM, "ua1")));

        assertEquals(1, result.size());
        assertEquals(Map.of("ua", "ua1", "target", "oa1", "arset", List.of("*")), result.getFirst());
    }

    @Test
    void testWithPML() throws PMException {
        String pml = """
            create pc "pc1"
            create ua "ua1" in ["pc1"]
            create ua "ua2" in ["pc1"]
            create oa "oa1" in ["pc1"]
            associate "ua1" to "oa1" with ["*"]
            
            assocs := get_associations_with_source(node_name="ua1")
            
            foreach assoc in assocs {
                associate "ua2" to "oa1" with assoc.arset
            }
           
            create conjunctive node prohibition "p1"
            deny "ua1"
            arset ["*"]
           
            """;
        MemoryPAP pap = new TestPAP();

        pap.executePML(NodeUserContext.of(0), pml);

        assertEquals(2, pap.query().graph().getAssociationsWithTarget(id("oa1")).size());
    }

}