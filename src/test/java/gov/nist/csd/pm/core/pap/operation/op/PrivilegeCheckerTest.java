package gov.nist.csd.pm.core.pap.operation.op;

import static gov.nist.csd.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.util.TestPAP;
import java.util.List;
import org.junit.jupiter.api.Test;

class PrivilegeCheckerTest {

	@Test
	void testEmptyToCheck() throws PMException {
		String pml = """
				set resource access rights ["read"]
				
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