package gov.nist.csd.pm.core.impl.grpc.client;

import io.grpc.ManagedChannel;

public class GrpcPDP {

    private ManagedChannel managedChannel;

    public GrpcPDP(ManagedChannel managedChannel) {
        this.managedChannel = managedChannel;
    }

    public GrpcResourcePDP resource(String user, String process) {
        return new GrpcResourcePDP(managedChannel, user, process);
    }

    public GrpcAdminPDP admin(String user, String process) {
        return new GrpcAdminPDP(managedChannel, user, process);
    }
}
