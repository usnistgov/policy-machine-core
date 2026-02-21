package gov.nist.csd.pm.core.impl.grpc.client;

import static gov.nist.csd.pm.core.impl.grpc.client.GrpcHeaders.buildHeaders;

import gov.nist.csd.pm.proto.v1.pdp.query.PolicyQueryServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.stub.MetadataUtils;

public class GrpcQueryPDP {

    private final ManagedChannel managedChannel;
    private final String user;
    private final String process;

    public GrpcQueryPDP(ManagedChannel managedChannel, String user, String process) {
        this.managedChannel = managedChannel;
        this.user = user;
        this.process = process;
    }

    public GrpcPolicyQuerier query() {
        return new GrpcPolicyQuerier(
            PolicyQueryServiceGrpc.newBlockingStub(managedChannel)
                .withInterceptors(MetadataUtils.newAttachHeadersInterceptor(buildHeaders(user, process))));
    }
}
