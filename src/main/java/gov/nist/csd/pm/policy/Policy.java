package gov.nist.csd.pm.policy;

import gov.nist.csd.pm.policy.exceptions.PMException;

public interface Policy {
    Graph graph();
    Prohibitions prohibitions();
    Obligations obligations();
    UserDefinedPML userDefinedPML();
    PolicySerializer serialize() throws PMException;
    PolicyDeserializer deserialize() throws PMException;
}
