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

import static gov.nist.ngac.pm.core.grpc.client.GrpcHeaders.buildHeaders;

import gov.nist.ngac.pm.proto.v1.pdp.adjudication.ResourceAdjudicationServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.stub.MetadataUtils;
import java.util.Map;

/**
 * Client-side handle for the resource adjudication gRPC service, scoped to a single user/process pair.
 */
public class GrpcResourcePDP {

    private final ManagedChannel managedChannel;
    private final String user;
    private final String process;

    public GrpcResourcePDP(ManagedChannel managedChannel, String user, String process) {
        this.managedChannel = managedChannel;
        this.user = user;
        this.process = process;
    }

    /**
     * Adjudicates a single resource operation by name against the remote PDP.
     *
     * @param name the name of the operation to adjudicate
     * @param args the operation's argument values, keyed by parameter name
     * @return the operation's return value, or null if it has none
     */
    public Object adjudicateOperation(String name, Map<String, Object> args) {
        ResourceAdjudicationServiceGrpc.ResourceAdjudicationServiceBlockingStub stub =
            ResourceAdjudicationServiceGrpc.newBlockingStub(managedChannel)
                .withInterceptors(MetadataUtils.newAttachHeadersInterceptor(buildHeaders(user, process)));
        GrpcResourceAdjudicationService service = new GrpcResourceAdjudicationService(stub);

        return service.adjudicateResourceOperation(name, args);
    }
}
