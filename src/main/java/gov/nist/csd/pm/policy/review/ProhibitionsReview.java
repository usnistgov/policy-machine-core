package gov.nist.csd.pm.policy.review;

import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;

import java.util.List;

public interface ProhibitionsReview {

    // TODO add function for getting prohibitions with given container
    List<Prohibition> getInheritedProhibitionsFor(String subject) throws PMException;


}
