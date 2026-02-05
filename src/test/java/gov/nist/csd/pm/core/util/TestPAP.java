package gov.nist.csd.pm.core.util;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.UnauthorizedException;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class TestPAP extends MemoryPAP {
	public TestPAP() throws PMException {
		withIdGenerator(new TestIdGenerator());
	}

	public void check(UserContext userCtx, TargetContext targetCtx, Collection<String> rightsToCheck) throws PMException {
		AccessRightSet computed = query().access().computePrivileges(userCtx, targetCtx);
		checkOrThrow(userCtx, targetCtx, computed, rightsToCheck);
	}

	public void check(UserContext userCtx, TargetContext targetCtx, String... rightsToCheck) throws PMException {
		AccessRightSet computed = query().access().computePrivileges(userCtx, targetCtx);
		checkOrThrow(userCtx, targetCtx, computed, List.of(rightsToCheck));
	}

	public void check(UserContext userCtx, long targetId, Collection<String> rightsToCheck) throws PMException {
		check(userCtx, new TargetContext(targetId), rightsToCheck);
	}

	public void check(UserContext userCtx, long targetId, String... rightsToCheck) throws PMException {
		check(userCtx, targetId, Arrays.asList(rightsToCheck));
	}

	public void check(UserContext userCtx, UserContext targetUserCtx, Collection<String> rightsToCheck) throws PMException {
		check(userCtx, new TargetContext(targetUserCtx), rightsToCheck);
	}

	public void check(UserContext userCtx, UserContext targetUserCtx, String... rightsToCheck) throws PMException {
		check(userCtx, new TargetContext(targetUserCtx), List.of(rightsToCheck));
	}

	public void check(UserContext userCtx, UserContext targetUserCtx, AdminAccessRight accessRight) throws PMException {
		check(userCtx, new TargetContext(targetUserCtx), List.of(accessRight.toString()));
	}

	public void check(UserContext userCtx, TargetContext targetCtx, AdminAccessRight accessRight) throws PMException {
		check(userCtx, targetCtx, List.of(accessRight.toString()));
	}

	public void check(UserContext userCtx, long target, AdminAccessRight accessRight) throws PMException {
		check(userCtx, new TargetContext(target), List.of(accessRight.toString()));
	}

	public void check(UserContext userCtx, List<Long> targetIds, String... rightsToCheck) throws PMException {
		List<String> rightsList = Arrays.asList(rightsToCheck);
		for (long targetId : targetIds) {
			check(userCtx, targetId, rightsList);
		}
	}

	private void checkOrThrow(UserContext userCtx, TargetContext targetCtx, AccessRightSet computed,
	                          Collection<String> required) throws PMException {
		if (!computed.containsAll(required) || (required.isEmpty() && computed.isEmpty())) {
			throw UnauthorizedException.of(query().graph(), userCtx, targetCtx, computed, required);
		}
	}
}
