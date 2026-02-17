package gov.nist.csd.pm.core.impl.grpc.pap;

import gov.nist.csd.pm.core.impl.grpc.util.FromProtoUtil;
import gov.nist.csd.pm.core.pap.obligation.Obligation;
import gov.nist.csd.pm.core.pap.query.ObligationsQuery;
import gov.nist.csd.pm.proto.v1.model.NodeRef;
import gov.nist.csd.pm.proto.v1.pdp.query.*;
import gov.nist.csd.pm.proto.v1.pdp.query.PolicyQueryServiceGrpc.PolicyQueryServiceBlockingStub;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class GrpcObligationsQuerier implements ObligationsQuery {

    private final PolicyQueryServiceBlockingStub blockingStub;

    public GrpcObligationsQuerier(PolicyQueryServiceBlockingStub blockingStub) {
        this.blockingStub = blockingStub;
    }

    @Override
    public Collection<Obligation> getObligations() {
        GetObligationsResponse response = blockingStub.getObligations(GetObligationsRequest.newBuilder().build());
        List<Obligation> obligations = new ArrayList<>();
        for (gov.nist.csd.pm.proto.v1.model.Obligation proto : response.getObligationsList()) {
            obligations.add(FromProtoUtil.fromObligationProto(proto));
        }
        return obligations;
    }

    @Override
    public Obligation getObligation(String name) {
        GetObligationRequest request = GetObligationRequest.newBuilder()
            .setName(name)
            .build();
        GetObligationResponse response = blockingStub.getObligation(request);
        return FromProtoUtil.fromObligationProto(response.getObligation());
    }

    @Override
    public boolean obligationExists(String name) {
        return getObligations().stream().map(Obligation::getName).collect(Collectors.toSet()).contains(name);
    }

    @Override
    public Collection<Obligation> getObligationsWithAuthor(long author) {
        GetObligationsByAuthorRequest request = GetObligationsByAuthorRequest.newBuilder()
            .setAuthor(NodeRef.newBuilder().setId(author).build())
            .build();
        GetObligationsByAuthorResponse response = blockingStub.getObligationsByAuthor(request);
        List<Obligation> obligations = new ArrayList<>();
        for (gov.nist.csd.pm.proto.v1.model.Obligation proto : response.getObligationsList()) {
            obligations.add(FromProtoUtil.fromObligationProto(proto));
        }
        return obligations;
    }
}
