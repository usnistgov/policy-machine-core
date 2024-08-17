package gov.nist.csd.pm.pap.executable;

import gov.nist.csd.pm.pap.exception.PMException;

import java.util.Map;

public interface AdminExecutor {

    Object executeAdminExecutable(AdminExecutable<?> adminExecutable, Map<String, Object> operands) throws PMException;

}
