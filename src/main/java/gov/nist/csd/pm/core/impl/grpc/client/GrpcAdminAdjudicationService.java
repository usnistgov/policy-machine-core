package gov.nist.csd.pm.core.impl.grpc.client;

import gov.nist.csd.pm.core.impl.grpc.util.FromProtoUtil;
import gov.nist.csd.pm.core.impl.grpc.util.ToProtoUtil;
import gov.nist.csd.pm.proto.v1.pdp.adjudication.AdjudicateOperationResponse;
import gov.nist.csd.pm.proto.v1.pdp.adjudication.AdminAdjudicationServiceGrpc.AdminAdjudicationServiceBlockingStub;
import gov.nist.csd.pm.proto.v1.pdp.adjudication.ExecutePMLRequest;
import gov.nist.csd.pm.proto.v1.pdp.adjudication.ExecutePMLResponse;
import gov.nist.csd.pm.proto.v1.pdp.adjudication.OperationRequest;
import gov.nist.csd.pm.proto.v1.pdp.adjudication.RoutineRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GrpcAdminAdjudicationService {

    private final AdminAdjudicationServiceBlockingStub blockingStub;

    public GrpcAdminAdjudicationService(AdminAdjudicationServiceBlockingStub blockingStub) {
        this.blockingStub = blockingStub;
    }

    public Object adjudicateOperation(String name, Map<String, Object> args) {
        OperationRequest request = OperationRequest.newBuilder()
            .setName(name)
            .setArgs(ToProtoUtil.toValueMapProto(args))
            .build();

        AdjudicateOperationResponse response = blockingStub.adjudicateOperation(request);

        if (response.hasValue()) {
            return FromProtoUtil.fromValue(response.getValue());
        }

        return null;
    }

    public void adjudicateRoutine(List<gov.nist.csd.pm.core.pdp.adjudication.OperationRequest> operations) {
        List<OperationRequest> requestProtos = new ArrayList<>();
        for (gov.nist.csd.pm.core.pdp.adjudication.OperationRequest req : operations) {
            requestProtos.add(OperationRequest.newBuilder()
                .setName(req.op())
                .setArgs(ToProtoUtil.toValueMapProto(req.args()))
                .build());
        }

        RoutineRequest request = RoutineRequest.newBuilder()
            .addAllOperations(requestProtos)
            .build();

        blockingStub.adjudicateRoutine(request);
    }

    public Object executePML(String pml) {
        ExecutePMLRequest request = ExecutePMLRequest.newBuilder()
            .setPml(pml)
            .build();

        ExecutePMLResponse response = blockingStub.executePML(request);

        if (response.hasValue()) {
            return FromProtoUtil.fromValue(response.getValue());
        }

        return null;
    }
}
