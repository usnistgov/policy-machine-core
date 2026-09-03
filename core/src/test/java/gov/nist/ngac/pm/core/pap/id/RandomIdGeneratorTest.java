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

package gov.nist.ngac.pm.core.pap.id;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.ngac.pm.core.common.graph.node.NodeType;
import java.util.Random;
import org.junit.jupiter.api.Test;

class RandomIdGeneratorTest {

    @Test
    void testGeneratedIdsAreAlwaysNonNegative() {
        RandomIdGenerator generator = new RandomIdGenerator();

        for (int i = 0; i < 10_000; i++) {
            long id = generator.generateId("n" + i, NodeType.O);
            assertTrue(id >= 0);
        }
    }

    @Test
    void testLongMinValueDrawIsSkippedInsteadOfReturnedAsNegative() {
        long[] draws = {Long.MIN_VALUE, Long.MIN_VALUE, 42L};
        int[] callCount = {0};
        Random stubRandom = new Random() {
            @Override
            public long nextLong() {
                return draws[callCount[0]++];
            }
        };

        RandomIdGenerator generator = new RandomIdGenerator(stubRandom);

        long id = generator.generateId("n", NodeType.O);

        assertEquals(3, callCount[0]);
        assertEquals(42L, id);
        assertNotEquals(Long.MIN_VALUE, id);
    }
}
