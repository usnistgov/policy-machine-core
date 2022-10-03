package gov.nist.csd.pm.policy.review;

import gov.nist.csd.pm.policy.exceptions.PMException;

import java.util.List;

public interface GraphReview {

    List<String> getAttributeContainers(String node) throws PMException;
    List<String> getPolicyClassContainers(String node) throws PMException;
    boolean isContained(String subject, String container) throws PMException;

}
