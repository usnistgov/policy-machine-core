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
