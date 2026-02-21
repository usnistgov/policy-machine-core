package gov.nist.csd.pm.core.impl.grpc.client;

import gov.nist.csd.pm.core.pap.modification.GraphModification;
import gov.nist.csd.pm.core.pap.modification.ObligationsModification;
import gov.nist.csd.pm.core.pap.modification.OperationsModification;
import gov.nist.csd.pm.core.pap.modification.PolicyModification;
import gov.nist.csd.pm.core.pap.modification.ProhibitionsModification;
import gov.nist.csd.pm.proto.v1.pdp.adjudication.AdminAdjudicationServiceGrpc;
import gov.nist.csd.pm.proto.v1.pdp.adjudication.AdminAdjudicationServiceGrpc.AdminAdjudicationServiceBlockingStub;

public class GrpcPolicyModifier implements PolicyModification {

    private AdminAdjudicationServiceGrpc.AdminAdjudicationServiceBlockingStub stub;

    public GrpcPolicyModifier(AdminAdjudicationServiceBlockingStub stub) {
        this.stub = stub;
    }

    @Override
    public GrpcGraphModifier graph() {
        return new GrpcGraphModifier(stub);
    }

    @Override
    public GrpcProhibitionsModifier prohibitions() {
        return new GrpcProhibitionsModifier(stub);
    }

    @Override
    public GrpcObligationsModifier obligations() {
        return new GrpcObligationsModifier(stub);
    }

    @Override
    public GrpcOperationsModifier operations() {
        return new GrpcOperationsModifier(stub);
    }
}
