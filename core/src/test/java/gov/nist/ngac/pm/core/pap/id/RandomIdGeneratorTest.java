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
