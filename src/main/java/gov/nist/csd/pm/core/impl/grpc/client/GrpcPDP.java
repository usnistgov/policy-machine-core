package gov.nist.csd.pm.core.impl.grpc.client;

import io.grpc.ManagedChannel;

public class GrpcPDP {

    private final ManagedChannel adminChannel;
    private final ManagedChannel resourceChannel;
    private final ManagedChannel policyQueryChannel;
    private final ManagedChannel eppChannel;

    public GrpcPDP(ManagedChannel channel) {
        this(channel, channel, channel, channel);
    }

    public GrpcPDP(ManagedChannel adminChannel,
                   ManagedChannel resourceChannel,
                   ManagedChannel queryChannel,
                   ManagedChannel eppChannel) {
        this.adminChannel = adminChannel;
        this.resourceChannel = resourceChannel;
        this.policyQueryChannel = queryChannel;
        this.eppChannel = eppChannel;
    }

    public GrpcAdminPDP admin(String user, String process) {
        return new GrpcAdminPDP(adminChannel, user, process);
    }

    public GrpcResourcePDP resource(String user, String process) {
        return new GrpcResourcePDP(resourceChannel, user, process);
    }

    public GrpcQueryPDP query(String user, String process) {
        return new GrpcQueryPDP(policyQueryChannel, user, process);
    }

    public GrpcEPP epp(String user, String process) {
        return new GrpcEPP(eppChannel, user, process);
    }
}
