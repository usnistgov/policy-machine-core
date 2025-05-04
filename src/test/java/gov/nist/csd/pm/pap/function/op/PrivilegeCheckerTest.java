package gov.nist.csd.pm.pap.function.op;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.util.TestPAP;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PrivilegeCheckerTest {

	@Test
	void testEmptyToCheck() throws PMException {
		String pml = """
				set resource operations ["read"]
				
				create pc "pc1"
				create ua "ua1" in ["pc1"]
				create ua "ua2" in ["pc1"]
				create oa "oa1" in ["pc1"]
				
				associate "ua1" and "oa1" with ["read"]
				
				create u "u1" in ["ua1"]
				create u "u2" in ["ua2"]
				create o "o1" in ["oa1"]
				""";

		MemoryPAP pap = new TestPAP();
		pap.executePML(new UserContext(id("u1")), pml);

		assertDoesNotThrow(() -> pap.privilegeChecker().check(new UserContext(id("u1")), id("o1"), List.of()));
		assertThrows(PMException.class, () -> pap.privilegeChecker().check(new UserContext(id("u2")), id("o1"), List.of()));
	}

}