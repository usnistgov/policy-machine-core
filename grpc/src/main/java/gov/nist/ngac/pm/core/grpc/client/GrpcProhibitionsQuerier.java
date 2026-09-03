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

import gov.nist.ngac.pm.core.common.prohibition.Prohibition;
import gov.nist.ngac.pm.core.grpc.util.FromProtoUtil;
import gov.nist.ngac.pm.core.pap.query.ProhibitionsQuery;
import gov.nist.ngac.pm.proto.v1.model.NodeRef;
import gov.nist.ngac.pm.proto.v1.pdp.query.GetInheritedProhibitionsRequest;
import gov.nist.ngac.pm.proto.v1.pdp.query.GetInheritedProhibitionsResponse;
import gov.nist.ngac.pm.proto.v1.pdp.query.GetProhibitionRequest;
import gov.nist.ngac.pm.proto.v1.pdp.query.GetProhibitionResponse;
import gov.nist.ngac.pm.proto.v1.pdp.query.GetProhibitionsBySubjectRequest;
import gov.nist.ngac.pm.proto.v1.pdp.query.GetProhibitionsBySubjectResponse;
import gov.nist.ngac.pm.proto.v1.pdp.query.GetProhibitionsRequest;
import gov.nist.ngac.pm.proto.v1.pdp.query.GetProhibitionsResponse;
import gov.nist.ngac.pm.proto.v1.pdp.query.GetProhibitionsWithContainerRequest;
import gov.nist.ngac.pm.proto.v1.pdp.query.GetProhibitionsWithContainerResponse;
import gov.nist.ngac.pm.proto.v1.pdp.query.PolicyQueryServiceGrpc.PolicyQueryServiceBlockingStub;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A {@link ProhibitionsQuery} that delegates to a remote PDP over gRPC.
 */
public class GrpcProhibitionsQuerier implements ProhibitionsQuery {

    private final PolicyQueryServiceBlockingStub blockingStub;

    public GrpcProhibitionsQuerier(PolicyQueryServiceBlockingStub blockingStub) {
        this.blockingStub = blockingStub;
    }

    @Override
    public Collection<Prohibition> getProhibitions() {
        GetProhibitionsResponse response = blockingStub.getProhibitions(GetProhibitionsRequest.newBuilder().build());
        List<Prohibition> prohibitions = new ArrayList<>();
        for (gov.nist.ngac.pm.proto.v1.model.Prohibition proto : response.getProhibitionsList()) {
            prohibitions.add(FromProtoUtil.fromProtoProhibition(proto));
        }
        return prohibitions;
    }

    @Override
    public Collection<Prohibition> getNodeProhibitions(long nodeId) {
        GetProhibitionsBySubjectRequest request = GetProhibitionsBySubjectRequest.newBuilder()
            .setNode(NodeRef.newBuilder().setId(nodeId).build())
            .build();
        GetProhibitionsBySubjectResponse response = blockingStub.getProhibitionsBySubject(request);
        List<Prohibition> prohibitions = new ArrayList<>();
        for (gov.nist.ngac.pm.proto.v1.model.Prohibition proto : response.getProhibitionsList()) {
            prohibitions.add(FromProtoUtil.fromProtoProhibition(proto));
        }
        return prohibitions;
    }

    @Override
    public Collection<Prohibition> getProcessProhibitions(String process) {
        GetProhibitionsBySubjectRequest request = GetProhibitionsBySubjectRequest.newBuilder()
            .setProcess(process)
            .build();
        GetProhibitionsBySubjectResponse response = blockingStub.getProhibitionsBySubject(request);
        List<Prohibition> prohibitions = new ArrayList<>();
        for (gov.nist.ngac.pm.proto.v1.model.Prohibition proto : response.getProhibitionsList()) {
            prohibitions.add(FromProtoUtil.fromProtoProhibition(proto));
        }
        return prohibitions;
    }

    @Override
    public Prohibition getProhibition(String name) {
        GetProhibitionRequest request = GetProhibitionRequest.newBuilder()
            .setName(name)
            .build();
        GetProhibitionResponse response = blockingStub.getProhibition(request);
        return FromProtoUtil.fromProtoProhibition(response.getProhibition());
    }

    @Override
    public boolean prohibitionExists(String name) {
        for (Prohibition p : getProhibitions()) {
            if (p.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Collection<Prohibition> getInheritedProhibitionsFor(long subjectId) {
        GetInheritedProhibitionsRequest request = GetInheritedProhibitionsRequest.newBuilder()
            .setSubject(NodeRef.newBuilder().setId(subjectId).build())
            .build();
        GetInheritedProhibitionsResponse response = blockingStub.getInheritedProhibitions(request);
        List<Prohibition> prohibitions = new ArrayList<>();
        for (gov.nist.ngac.pm.proto.v1.model.Prohibition proto : response.getProhibitionsList()) {
            prohibitions.add(FromProtoUtil.fromProtoProhibition(proto));
        }
        return prohibitions;
    }

    @Override
    public Collection<Prohibition> getProhibitionsWithContainer(long containerId) {
        GetProhibitionsWithContainerRequest request = GetProhibitionsWithContainerRequest.newBuilder()
            .setContainer(NodeRef.newBuilder().setId(containerId).build())
            .build();
        GetProhibitionsWithContainerResponse response = blockingStub.getProhibitionsWithContainer(request);
        List<Prohibition> prohibitions = new ArrayList<>();
        for (gov.nist.ngac.pm.proto.v1.model.Prohibition proto : response.getProhibitionsList()) {
            prohibitions.add(FromProtoUtil.fromProtoProhibition(proto));
        }
        return prohibitions;
    }
}
