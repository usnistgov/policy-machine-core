/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.grpc.client;

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
