package gov.nist.ngac.pm.core.impl.grpc.client;

import gov.nist.ngac.pm.core.impl.grpc.util.FromProtoUtil;
import gov.nist.ngac.pm.core.pap.obligation.Obligation;
import gov.nist.ngac.pm.core.pap.query.ObligationsQuery;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;
import gov.nist.ngac.pm.proto.v1.model.NodeRef;
import gov.nist.ngac.pm.proto.v1.pdp.query.GetObligationRequest;
import gov.nist.ngac.pm.proto.v1.pdp.query.GetObligationResponse;
import gov.nist.ngac.pm.proto.v1.pdp.query.GetObligationsByAuthorRequest;
import gov.nist.ngac.pm.proto.v1.pdp.query.GetObligationsByAuthorResponse;
import gov.nist.ngac.pm.proto.v1.pdp.query.GetObligationsRequest;
import gov.nist.ngac.pm.proto.v1.pdp.query.GetObligationsResponse;
import gov.nist.ngac.pm.proto.v1.pdp.query.PolicyQueryServiceGrpc.PolicyQueryServiceBlockingStub;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A {@link ObligationsQuery} that delegates to a remote PDP over gRPC.
 */
public class GrpcObligationsQuerier implements ObligationsQuery {

    private final PolicyQueryServiceBlockingStub blockingStub;

    public GrpcObligationsQuerier(PolicyQueryServiceBlockingStub blockingStub) {
        this.blockingStub = blockingStub;
    }

    @Override
    public Collection<Obligation> getObligations() {
        GetObligationsResponse response = blockingStub.getObligations(GetObligationsRequest.newBuilder().build());
        List<Obligation> obligations = new ArrayList<>();
        for (gov.nist.ngac.pm.proto.v1.model.Obligation proto : response.getObligationsList()) {
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
    public Collection<Obligation> getObligationsWithAuthor(NodeUserContext author) {
        NodeRef authorRef = author.getName() != null
            ? NodeRef.newBuilder().setName(author.getName()).build()
            : NodeRef.newBuilder().setId(author.getId()).build();
        GetObligationsByAuthorRequest request = GetObligationsByAuthorRequest.newBuilder()
            .setAuthor(authorRef)
            .build();
        GetObligationsByAuthorResponse response = blockingStub.getObligationsByAuthor(request);
        List<Obligation> obligations = new ArrayList<>();
        for (gov.nist.ngac.pm.proto.v1.model.Obligation proto : response.getObligationsList()) {
            obligations.add(FromProtoUtil.fromObligationProto(proto));
        }
        return obligations;
    }
}
