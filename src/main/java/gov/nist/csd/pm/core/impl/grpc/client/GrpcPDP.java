package gov.nist.csd.pm.core.impl.grpc.client;

import io.grpc.ManagedChannel;

public class GrpcPDP {

    private final ManagedChannel managedChannel;

    public GrpcPDP(ManagedChannel managedChannel) {
        this.managedChannel = managedChannel;
    }

    public GrpcResourcePDP resource() {
        return new GrpcResourcePDP(managedChannel);
    }

    public GrpcAdminPDP admin() {
        return new GrpcAdminPDP(managedChannel);
    }
}
