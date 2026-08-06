package gov.nist.ngac.pm.core.grpc.client;

import gov.nist.ngac.pm.core.pap.modification.PolicyModification;
import gov.nist.ngac.pm.proto.v1.pdp.adjudication.AdminAdjudicationServiceGrpc;
import gov.nist.ngac.pm.proto.v1.pdp.adjudication.AdminAdjudicationServiceGrpc.AdminAdjudicationServiceBlockingStub;

/**
 * A {@link PolicyModification} that provides gRPC-backed modifiers for each policy sub-area.
 */
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
