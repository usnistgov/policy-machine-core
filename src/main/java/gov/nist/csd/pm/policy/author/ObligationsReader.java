package gov.nist.csd.pm.policy.author;

import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.obligation.Obligation;

import java.util.List;

public interface ObligationsReader {

    List<Obligation> getAll() throws PMException;
    Obligation get(String label) throws PMException;

}
