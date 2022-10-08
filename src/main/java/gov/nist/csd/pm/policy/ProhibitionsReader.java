package gov.nist.csd.pm.policy;

import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;

import java.util.List;
import java.util.Map;

public interface ProhibitionsReader {

    Map<String, List<Prohibition>> getAll() throws PMException;
    List<Prohibition> getWithSubject(String subject) throws PMException;
    Prohibition get(String label) throws PMException;

}
