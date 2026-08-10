package gov.nist.ngac.pm.core.util;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeTargetContext;
import gov.nist.ngac.pm.core.pap.query.model.context.TargetContext;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import gov.nist.ngac.pm.core.pdp.UnauthorizedException;
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

	public void check(UserContext userCtx, long targetId, Collection<String> rightsToCheck) throws PMException {
		check(userCtx, NodeTargetContext.of(targetId), rightsToCheck);
	}

	public void check(UserContext userCtx, long target, AdminAccessRight accessRight) throws PMException {
		check(userCtx, NodeTargetContext.of(target), List.of(accessRight.toString()));
	}

	private void checkOrThrow(UserContext userCtx, TargetContext targetCtx, AccessRightSet computed,
	                          Collection<String> required) throws PMException {
		if (!computed.containsAll(required) || (required.isEmpty() && computed.isEmpty())) {
			throw UnauthorizedException.of(query().graph(), userCtx, targetCtx, computed, required);
		}
	}
}
