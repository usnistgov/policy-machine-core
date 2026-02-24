package gov.nist.csd.pm.core.impl.grpc.client;

import static gov.nist.csd.pm.core.impl.grpc.client.GrpcHeaders.buildHeaders;

import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import gov.nist.csd.pm.proto.v1.pdp.query.PolicyQueryServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.stub.MetadataUtils;

public class GrpcQueryPDP implements PolicyQuery {

    private GrpcPolicyQuerier grpcPolicyQuerier;

    public GrpcQueryPDP(ManagedChannel managedChannel, String user, String process) {
        this.grpcPolicyQuerier = new GrpcPolicyQuerier(
            PolicyQueryServiceGrpc.newBlockingStub(managedChannel)
                .withInterceptors(MetadataUtils.newAttachHeadersInterceptor(buildHeaders(user, process))));
    }

    @Override
    public GrpcAccessQuerier access() {
        return grpcPolicyQuerier.access();
    }

    public GrpcSelfAccessQuerier selfAccess() {
        return grpcPolicyQuerier.selfAccess();
    }

    @Override
    public GrpcGraphQuerier graph() {
        return grpcPolicyQuerier.graph();
    }

    @Override
    public GrpcProhibitionsQuerier prohibitions() {
        return grpcPolicyQuerier.prohibitions();
    }

    @Override
    public GrpcObligationsQuerier obligations() {
        return grpcPolicyQuerier.obligations();
    }

    @Override
    public GrpcOperationsQuerier operations() {
        return grpcPolicyQuerier.operations();
    }
}
