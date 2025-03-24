package gov.nist.csd.pm.pap.op;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pap.serialization.pml.PMLDeserializer;
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
		pap.deserialize(new UserContext(id("u1")), pml, new PMLDeserializer());

		PrivilegeChecker checker = new PrivilegeChecker(pap);
		assertDoesNotThrow(() -> checker.check(new UserContext(id("u1")), "o1", List.of()));
		assertThrows(PMException.class, () -> checker.check(new UserContext(id("u2")), "o1", List.of()));
	}

}