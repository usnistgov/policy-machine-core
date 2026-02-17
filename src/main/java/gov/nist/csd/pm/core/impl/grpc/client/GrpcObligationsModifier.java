package gov.nist.csd.pm.core.impl.grpc.client;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.grpc.util.ToProtoUtil;
import gov.nist.csd.pm.core.pap.modification.ObligationsModification;
import gov.nist.csd.pm.core.pap.obligation.Obligation;
import gov.nist.csd.pm.core.pap.obligation.event.EventPattern;
import gov.nist.csd.pm.core.pap.obligation.response.ObligationResponse;
import gov.nist.csd.pm.proto.v1.pdp.adjudication.AdminAdjudicationServiceGrpc.AdminAdjudicationServiceBlockingStub;
import gov.nist.csd.pm.proto.v1.pdp.adjudication.ExecutePMLRequest;
import gov.nist.csd.pm.proto.v1.pdp.adjudication.OperationRequest;
import java.util.Map;

public class GrpcObligationsModifier implements ObligationsModification {

    private final AdminAdjudicationServiceBlockingStub blockingStub;

    public GrpcObligationsModifier(AdminAdjudicationServiceBlockingStub blockingStub) {
        this.blockingStub = blockingStub;
    }

    @Override
    public void createObligation(long authorId, String name, EventPattern eventPattern,
                                 ObligationResponse response) throws PMException {
        ExecutePMLRequest request = ExecutePMLRequest.newBuilder()
            .setPml(new Obligation(authorId, name, eventPattern, response).toString())
            .build();

        blockingStub.executePML(request);
    }

    @Override
    public void deleteObligation(String name) throws PMException {
        OperationRequest request = OperationRequest.newBuilder()
            .setName("delete_obligation")
            .setArgs(ToProtoUtil.toValueMapProto(Map.of(
                "name", name
            )))
            .build();

        blockingStub.adjudicateOperation(request);
    }
}
