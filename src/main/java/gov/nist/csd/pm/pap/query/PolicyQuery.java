package gov.nist.csd.pm.pap.query;

public interface PolicyQuery {

    AccessQuery access();
    GraphQuery graph();
    ProhibitionsQuery prohibitions();
    ObligationsQuery obligations();
    OperationsQuery operations();
    RoutinesQuery routines();
}
