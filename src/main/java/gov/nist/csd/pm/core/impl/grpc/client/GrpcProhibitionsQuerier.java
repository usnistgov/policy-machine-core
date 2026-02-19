package gov.nist.csd.pm.core.impl.grpc.client;

import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.impl.grpc.util.FromProtoUtil;
import gov.nist.csd.pm.core.pap.query.ProhibitionsQuery;
import gov.nist.csd.pm.proto.v1.model.NodeRef;
import gov.nist.csd.pm.proto.v1.pdp.query.GetInheritedProhibitionsRequest;
import gov.nist.csd.pm.proto.v1.pdp.query.GetInheritedProhibitionsResponse;
import gov.nist.csd.pm.proto.v1.pdp.query.GetProhibitionRequest;
import gov.nist.csd.pm.proto.v1.pdp.query.GetProhibitionResponse;
import gov.nist.csd.pm.proto.v1.pdp.query.GetProhibitionsBySubjectRequest;
import gov.nist.csd.pm.proto.v1.pdp.query.GetProhibitionsBySubjectResponse;
import gov.nist.csd.pm.proto.v1.pdp.query.GetProhibitionsRequest;
import gov.nist.csd.pm.proto.v1.pdp.query.GetProhibitionsResponse;
import gov.nist.csd.pm.proto.v1.pdp.query.GetProhibitionsWithContainerRequest;
import gov.nist.csd.pm.proto.v1.pdp.query.GetProhibitionsWithContainerResponse;
import gov.nist.csd.pm.proto.v1.pdp.query.PolicyQueryServiceGrpc.PolicyQueryServiceBlockingStub;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GrpcProhibitionsQuerier implements ProhibitionsQuery {

    private final PolicyQueryServiceBlockingStub blockingStub;

    public GrpcProhibitionsQuerier(PolicyQueryServiceBlockingStub blockingStub) {
        this.blockingStub = blockingStub;
    }

    @Override
    public Collection<Prohibition> getProhibitions() {
        GetProhibitionsResponse response = blockingStub.getProhibitions(GetProhibitionsRequest.newBuilder().build());
        List<Prohibition> prohibitions = new ArrayList<>();
        for (gov.nist.csd.pm.proto.v1.model.Prohibition proto : response.getProhibitionsList()) {
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
        for (gov.nist.csd.pm.proto.v1.model.Prohibition proto : response.getProhibitionsList()) {
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
        for (gov.nist.csd.pm.proto.v1.model.Prohibition proto : response.getProhibitionsList()) {
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
        for (gov.nist.csd.pm.proto.v1.model.Prohibition proto : response.getProhibitionsList()) {
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
        for (gov.nist.csd.pm.proto.v1.model.Prohibition proto : response.getProhibitionsList()) {
            prohibitions.add(FromProtoUtil.fromProtoProhibition(proto));
        }
        return prohibitions;
    }
}
