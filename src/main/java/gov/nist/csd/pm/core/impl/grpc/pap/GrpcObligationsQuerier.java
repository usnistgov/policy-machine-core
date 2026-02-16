package gov.nist.csd.pm.core.impl.grpc.pap;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.obligation.Obligation;
import gov.nist.csd.pm.core.pap.query.ObligationsQuerier;
import gov.nist.csd.pm.proto.v1.model.NodeRef;
import gov.nist.csd.pm.proto.v1.pdp.query.*;
import gov.nist.csd.pm.proto.v1.pdp.query.PolicyQueryServiceGrpc.PolicyQueryServiceBlockingStub;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GrpcObligationsQuerier extends ObligationsQuerier {

    private final PolicyQueryServiceBlockingStub blockingStub;

    public GrpcObligationsQuerier(PolicyQueryServiceBlockingStub blockingStub) {
        super(null);
        this.blockingStub = blockingStub;
    }

    @Override
    public Collection<Obligation> getObligations() throws PMException {
        GetObligationsResponse response = blockingStub.getObligations(GetObligationsRequest.newBuilder().build());
        List<Obligation> obligations = new ArrayList<>();
        for (gov.nist.csd.pm.proto.v1.model.Obligation proto : response.getObligationsList()) {
            obligations.add(FromProtoUtil.fromProtoObligation(proto));
        }
        return obligations;
    }

    @Override
    public Obligation getObligation(String name) throws PMException {
        GetObligationRequest request = GetObligationRequest.newBuilder()
            .setName(name)
            .build();
        GetObligationResponse response = blockingStub.getObligation(request);
        return FromProtoUtil.fromProtoObligation(response.getObligation());
    }

    @Override
    public boolean obligationExists(String name) throws PMException {
        try {
            getObligation(name);
            return true;
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.NOT_FOUND) {
                return false;
            }
            throw e;
        }
    }

    @Override
    public Collection<Obligation> getObligationsWithAuthor(long author) throws PMException {
        GetObligationsByAuthorRequest request = GetObligationsByAuthorRequest.newBuilder()
            .setAuthor(NodeRef.newBuilder().setId(author).build())
            .build();
        GetObligationsByAuthorResponse response = blockingStub.getObligationsByAuthor(request);
        List<Obligation> obligations = new ArrayList<>();
        for (gov.nist.csd.pm.proto.v1.model.Obligation proto : response.getObligationsList()) {
            obligations.add(FromProtoUtil.fromProtoObligation(proto));
        }
        return obligations;
    }
}
