package gov.nist.ngac.pm.core.impl.grpc.client;

import gov.nist.ngac.pm.core.pap.modification.PolicyModification;
import gov.nist.ngac.pm.proto.v1.pdp.adjudication.AdminAdjudicationServiceGrpc;
import gov.nist.ngac.pm.proto.v1.pdp.adjudication.AdminAdjudicationServiceGrpc.AdminAdjudicationServiceBlockingStub;

/**
 * {@link PolicyModification} implementation that vends gRPC-backed modifiers for each policy sub-area,
 * all sharing the same admin adjudication stub.
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
