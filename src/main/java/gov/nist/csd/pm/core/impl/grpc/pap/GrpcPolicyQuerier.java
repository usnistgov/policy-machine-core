package gov.nist.csd.pm.core.impl.grpc.pap;

import gov.nist.csd.pm.core.pap.query.PolicyQuerier;
import gov.nist.csd.pm.proto.v1.pdp.query.PolicyQueryServiceGrpc.PolicyQueryServiceBlockingStub;

public class GrpcPolicyQuerier extends PolicyQuerier {

    public GrpcPolicyQuerier(PolicyQueryServiceBlockingStub blockingStub) {
        super(
            new GrpcGraphQuerier(blockingStub),
            new GrpcProhibitionsQuerier(blockingStub),
            new GrpcObligationsQuerier(blockingStub),
            new GrpcOperationsQuerier(blockingStub),
            new GrpcAccessQuerier(blockingStub)
        );
    }
}
