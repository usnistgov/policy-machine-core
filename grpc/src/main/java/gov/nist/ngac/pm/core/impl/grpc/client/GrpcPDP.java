package gov.nist.ngac.pm.core.impl.grpc.client;

import io.grpc.ManagedChannel;

/**
 * Entry point for a gRPC-backed PDP client, holding one {@link ManagedChannel} per service and vending
 * user/process-scoped handles for admin, resource, query, and EPP operations.
 */
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

    /**
     * Returns an admin adjudication handle scoped to the given user/process.
     *
     * @param user the acting user
     * @param process the acting process
     * @return an admin PDP handle bound to the admin channel
     */
    public GrpcAdminPDP admin(String user, String process) {
        return new GrpcAdminPDP(adminChannel, user, process);
    }

    /**
     * Returns a resource adjudication handle scoped to the given user/process.
     *
     * @param user the acting user
     * @param process the acting process
     * @return a resource PDP handle bound to the resource channel
     */
    public GrpcResourcePDP resource(String user, String process) {
        return new GrpcResourcePDP(resourceChannel, user, process);
    }

    /**
     * Returns a policy query handle scoped to the given user/process.
     *
     * @param user the acting user
     * @param process the acting process
     * @return a query PDP handle bound to the policy query channel
     */
    public GrpcQueryPDP query(String user, String process) {
        return new GrpcQueryPDP(policyQueryChannel, user, process);
    }

    /**
     * Returns an EPP handle scoped to the given user/process.
     *
     * @param user the acting user
     * @param process the acting process
     * @return an EPP handle bound to the EPP channel
     */
    public GrpcEPP epp(String user, String process) {
        return new GrpcEPP(eppChannel, user, process);
    }
}
