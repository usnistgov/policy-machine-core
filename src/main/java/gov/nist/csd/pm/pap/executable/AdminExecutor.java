package gov.nist.csd.pm.pap.executable;

import gov.nist.csd.pm.common.exception.PMException;

import gov.nist.csd.pm.pap.executable.arg.ActualArgs;
import java.util.Map;

public interface AdminExecutor {

    <T> T executeAdminExecutable(AdminExecutable<T> adminExecutable, ActualArgs args) throws PMException;

}
