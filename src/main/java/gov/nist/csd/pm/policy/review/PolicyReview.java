package gov.nist.csd.pm.policy.review;

public interface PolicyReview {

    AccessReview access();
    GraphReview graph();
    ProhibitionsReview prohibitions();
    ObligationsReview obligations();

}
