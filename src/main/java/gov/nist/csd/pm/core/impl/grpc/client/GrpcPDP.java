package gov.nist.csd.pm.core.impl.grpc.client;

import io.grpc.ManagedChannel;

public class GrpcPDP {

    private final ManagedChannel adminAdjudicationChannel;
    private final ManagedChannel resourceChannel;
    private final ManagedChannel policyQueryChannel;
    private final ManagedChannel eppChannel;

    // Convenience: all services on the same channel
    public GrpcPDP(ManagedChannel channel) {
        this(channel, channel, channel, channel);
    }

    // All four channels explicit
    public GrpcPDP(ManagedChannel adminAdjudicationChannel,
                   ManagedChannel resourceChannel,
                   ManagedChannel policyQueryChannel,
                   ManagedChannel eppChannel) {
        this.adminAdjudicationChannel = adminAdjudicationChannel;
        this.resourceChannel          = resourceChannel;
        this.policyQueryChannel       = policyQueryChannel;
        this.eppChannel               = eppChannel;
    }

    private GrpcPDP(Builder builder) {
        this.adminAdjudicationChannel = builder.adminAdjudicationChannel;
        this.resourceChannel          = builder.resourceChannel;
        this.policyQueryChannel       = builder.policyQueryChannel;
        this.eppChannel               = builder.eppChannel;
    }

    public GrpcAdminPDP admin(String user, String process) {
        return new GrpcAdminPDP(adminAdjudicationChannel, user, process);
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

    public static class Builder {

        private final ManagedChannel adminAdjudicationChannel;
        private ManagedChannel resourceChannel;
        private ManagedChannel policyQueryChannel;
        private ManagedChannel eppChannel;

        public Builder(ManagedChannel adminAdjudicationChannel) {
            this.adminAdjudicationChannel = adminAdjudicationChannel;
            this.resourceChannel    = adminAdjudicationChannel;
            this.policyQueryChannel = adminAdjudicationChannel;
            this.eppChannel         = adminAdjudicationChannel;
        }

        public Builder resourceChannel(ManagedChannel c) {
            this.resourceChannel = c;
            return this;
        }

        public Builder policyQueryChannel(ManagedChannel c) {
            this.policyQueryChannel = c;
            return this;
        }

        public Builder eppChannel(ManagedChannel c) {
            this.eppChannel = c;
            return this;
        }

        public GrpcPDP build() {
            return new GrpcPDP(this);
        }
    }
}
