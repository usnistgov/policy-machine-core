package gov.nist.csd.pm.policy;
public interface Policy {
    Graph graph();
    Prohibitions prohibitions();
    Obligations obligations();
    UserDefinedPML userDefinedPML();

}
