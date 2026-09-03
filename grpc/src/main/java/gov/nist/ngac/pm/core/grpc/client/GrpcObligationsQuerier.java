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
