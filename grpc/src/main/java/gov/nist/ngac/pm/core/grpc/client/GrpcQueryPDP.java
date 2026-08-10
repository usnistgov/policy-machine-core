package gov.nist.ngac.pm.core.grpc.client;

import static gov.nist.ngac.pm.core.grpc.client.GrpcHeaders.buildHeaders;

import gov.nist.ngac.pm.core.pap.query.PolicyQuery;
import gov.nist.ngac.pm.proto.v1.pdp.query.PolicyQueryServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.stub.MetadataUtils;

/**
 * A {@link PolicyQuery} backed by a gRPC channel that attaches user/process headers to every call.
 */
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
