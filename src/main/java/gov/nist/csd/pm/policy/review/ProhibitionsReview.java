package gov.nist.csd.pm.policy.review;

import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;

import java.util.List;

public interface ProhibitionsReview {

    List<Prohibition> getInheritedProhibitionsFor(String subject) throws PMException;
    List<Prohibition> getProhibitionsWithContainer(String container) throws PMException;

}
