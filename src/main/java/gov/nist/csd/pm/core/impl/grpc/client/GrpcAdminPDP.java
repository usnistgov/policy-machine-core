package gov.nist.csd.pm.core.impl.grpc.client;

import static gov.nist.csd.pm.core.impl.grpc.client.GrpcHeaders.buildHeaders;

import gov.nist.csd.pm.core.impl.grpc.util.FromProtoUtil;
import gov.nist.csd.pm.core.impl.grpc.util.ToProtoUtil;
import gov.nist.csd.pm.proto.v1.pdp.adjudication.AdjudicateOperationResponse;
import gov.nist.csd.pm.proto.v1.pdp.adjudication.AdminAdjudicationServiceGrpc;
import gov.nist.csd.pm.proto.v1.pdp.adjudication.AdminAdjudicationServiceGrpc.AdminAdjudicationServiceBlockingStub;
import gov.nist.csd.pm.proto.v1.pdp.adjudication.ExecutePMLRequest;
import gov.nist.csd.pm.proto.v1.pdp.adjudication.ExecutePMLResponse;
import gov.nist.csd.pm.proto.v1.pdp.adjudication.OperationRequest;
import gov.nist.csd.pm.proto.v1.pdp.adjudication.RoutineRequest;
import io.grpc.ManagedChannel;
import io.grpc.stub.MetadataUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GrpcAdminPDP {

    private final ManagedChannel managedChannel;
    private final String user;
    private final String process;

    public GrpcAdminPDP(ManagedChannel managedChannel, String user, String process) {
        this.managedChannel = managedChannel;
        this.user = user;
        this.process = process;
    }

    public Object adjudicateOperation(String name, Map<String, Object> args) {
        AdminAdjudicationServiceBlockingStub stub = AdminAdjudicationServiceGrpc.newBlockingStub(managedChannel)
            .withInterceptors(MetadataUtils.newAttachHeadersInterceptor(buildHeaders(user, process)));

        OperationRequest request = OperationRequest.newBuilder()
            .setName(name)
            .setArgs(ToProtoUtil.toValueMapProto(args))
            .build();

        AdjudicateOperationResponse response = stub.adjudicateOperation(request);

        if (response.hasValue()) {
            return FromProtoUtil.fromValue(response.getValue());
        }

        return null;
    }

    public void adjudicateRoutine(List<gov.nist.csd.pm.core.pdp.adjudication.OperationRequest> operations) {
        AdminAdjudicationServiceBlockingStub stub = AdminAdjudicationServiceGrpc.newBlockingStub(managedChannel)
            .withInterceptors(MetadataUtils.newAttachHeadersInterceptor(buildHeaders(user, process)));

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

        stub.adjudicateRoutine(request);
    }

    public Object executePML(String pml) {
        AdminAdjudicationServiceBlockingStub stub = AdminAdjudicationServiceGrpc.newBlockingStub(managedChannel)
            .withInterceptors(MetadataUtils.newAttachHeadersInterceptor(buildHeaders(user, process)));

        ExecutePMLRequest request = ExecutePMLRequest.newBuilder()
            .setPml(pml)
            .build();

        ExecutePMLResponse response = stub.executePML(request);

        if (response.hasValue()) {
            return FromProtoUtil.fromValue(response.getValue());
        }

        return null;
    }

    public GrpcPolicyModifier modify() {
        return new GrpcPolicyModifier(AdminAdjudicationServiceGrpc.newBlockingStub(managedChannel)
            .withInterceptors(MetadataUtils.newAttachHeadersInterceptor(buildHeaders(user, process))));
    }
}
