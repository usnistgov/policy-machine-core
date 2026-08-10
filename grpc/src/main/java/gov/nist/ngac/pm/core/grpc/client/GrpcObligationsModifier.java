package gov.nist.ngac.pm.core.grpc.client;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.grpc.util.ToProtoUtil;
import gov.nist.ngac.pm.core.pap.modification.ObligationsModification;
import gov.nist.ngac.pm.core.pap.obligation.Obligation;
import gov.nist.ngac.pm.proto.v1.pdp.adjudication.AdminAdjudicationServiceGrpc.AdminAdjudicationServiceBlockingStub;
import gov.nist.ngac.pm.proto.v1.pdp.adjudication.ExecutePMLRequest;
import gov.nist.ngac.pm.proto.v1.pdp.adjudication.OperationRequest;
import java.util.Map;

/**
 * A {@link ObligationsModification} that submits each operation as an admin adjudication request over
 * gRPC.
 */
public class GrpcObligationsModifier implements ObligationsModification {

    private final AdminAdjudicationServiceBlockingStub blockingStub;

    public GrpcObligationsModifier(AdminAdjudicationServiceBlockingStub blockingStub) {
        this.blockingStub = blockingStub;
    }

    @Override
    public void createObligation(Obligation obligation) throws PMException {
        ExecutePMLRequest request = ExecutePMLRequest.newBuilder()
            .setPml(obligation.toString())
            .build();

        blockingStub.executePML(request);
    }

    @Override
    public void deleteObligation(String name) throws PMException {
        OperationRequest request = OperationRequest.newBuilder()
            .setName("delete_obligation")
            .putAllArgs(ToProtoUtil.toStringValueMapProto(Map.of(
                "name", name
            )))
            .build();

        blockingStub.adjudicateOperation(request);
    }
}
