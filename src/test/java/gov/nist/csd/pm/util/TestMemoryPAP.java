package gov.nist.csd.pm.util;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;

import java.util.List;
import java.util.stream.LongStream;

public class TestMemoryPAP extends MemoryPAP {

	public TestMemoryPAP() throws PMException {
		super();
	}

	public long id(String name) throws PMException {
		return this.query().graph().getNodeByName(name).getId();
	}

	public List<Long> ids(String ... names) throws PMException {
		return TestMemoryPAP.idsInternal(this, names);
	}

	public static long id(PAP pap, String name) throws PMException {
		return TestMemoryPAP.idsInternal(pap, name).getFirst();
	}

	public static List<Long> ids(PAP pap, String ... names) throws PMException {
		return TestMemoryPAP.idsInternal(pap, names);
	}

	private static List<Long> idsInternal(PAP pap, String ... names) throws PMException {
		long[] ids = new long[names.length];
		for (int i = 0; i < names.length; i++) {
			String name = names[i];
			ids[i] = pap.query().graph().getNodeByName(name).getId();
		}

		return LongStream.of(ids)
				.boxed()
				.toList();
	}
}
