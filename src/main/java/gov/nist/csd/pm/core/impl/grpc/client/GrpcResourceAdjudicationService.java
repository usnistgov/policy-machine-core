package gov.nist.csd.pm.core.impl.grpc.client;

import gov.nist.csd.pm.core.impl.grpc.util.FromProtoUtil;
import gov.nist.csd.pm.core.impl.grpc.util.ToProtoUtil;
import gov.nist.csd.pm.proto.v1.pdp.adjudication.AdjudicateOperationResponse;
import gov.nist.csd.pm.proto.v1.pdp.adjudication.OperationRequest;
import gov.nist.csd.pm.proto.v1.pdp.adjudication.ResourceAdjudicationServiceGrpc.ResourceAdjudicationServiceBlockingStub;
import java.util.Map;

public class GrpcResourceAdjudicationService {

    private final ResourceAdjudicationServiceBlockingStub blockingStub;

    public GrpcResourceAdjudicationService(ResourceAdjudicationServiceBlockingStub blockingStub) {
        this.blockingStub = blockingStub;
    }

    public Object adjudicateResourceOperation(String name, Map<String, Object> args) {
        OperationRequest request = OperationRequest.newBuilder()
            .setName(name)
            .setArgs(ToProtoUtil.toValueMapProto(args))
            .build();

        AdjudicateOperationResponse response = blockingStub.adjudicateResourceOperation(request);

        if (response.hasValue()) {
            return FromProtoUtil.fromValue(response.getValue());
        }

        return null;
    }
}
