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

import gov.nist.ngac.pm.core.grpc.util.FromProtoUtil;
import gov.nist.ngac.pm.core.grpc.util.ToProtoUtil;
import gov.nist.ngac.pm.proto.v1.pdp.adjudication.AdjudicateOperationResponse;
import gov.nist.ngac.pm.proto.v1.pdp.adjudication.OperationRequest;
import gov.nist.ngac.pm.proto.v1.pdp.adjudication.ResourceAdjudicationServiceGrpc.ResourceAdjudicationServiceBlockingStub;
import java.util.Map;

/**
 * Wraps a {@link ResourceAdjudicationServiceBlockingStub} to adjudicate resource operations over gRPC.
 */
public class GrpcResourceAdjudicationService {

    private final ResourceAdjudicationServiceBlockingStub blockingStub;

    public GrpcResourceAdjudicationService(ResourceAdjudicationServiceBlockingStub blockingStub) {
        this.blockingStub = blockingStub;
    }

    /**
     * Adjudicates a single resource operation by name against the remote PDP.
     *
     * @param name the name of the operation to adjudicate
     * @param args the operation's argument values, keyed by parameter name
     * @return the operation's return value, or null if it has none
     */
    public Object adjudicateResourceOperation(String name, Map<String, Object> args) {
        OperationRequest request = OperationRequest.newBuilder()
            .setName(name)
            .putAllArgs(ToProtoUtil.toStringValueMapProto(args))
            .build();

        AdjudicateOperationResponse response = blockingStub.adjudicateResourceOperation(request);

        if (response.hasValue()) {
            return FromProtoUtil.fromValue(response.getValue());
        }

        return null;
    }
}
