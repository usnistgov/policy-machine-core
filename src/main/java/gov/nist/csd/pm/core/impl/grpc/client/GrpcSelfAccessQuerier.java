package gov.nist.csd.pm.core.impl.grpc.client;

import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.impl.grpc.util.FromProtoUtil;
import gov.nist.csd.pm.core.impl.grpc.util.ToProtoUtil;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pap.query.model.subgraph.SubgraphPrivileges;
import gov.nist.csd.pm.core.pdp.query.SelfAccessQuery;
import gov.nist.csd.pm.proto.v1.pdp.query.ComputeAdjacentAscendantPrivilegesRequest;
import gov.nist.csd.pm.proto.v1.pdp.query.ComputeAdjacentAscendantPrivilegesResponse;
import gov.nist.csd.pm.proto.v1.pdp.query.ComputeAdjacentDescendantPrivilegesRequest;
import gov.nist.csd.pm.proto.v1.pdp.query.ComputeAdjacentDescendantPrivilegesResponse;
import gov.nist.csd.pm.proto.v1.pdp.query.ComputeDeniedPrivilegesRequest;
import gov.nist.csd.pm.proto.v1.pdp.query.ComputeDeniedPrivilegesResponse;
import gov.nist.csd.pm.proto.v1.pdp.query.ComputePersonalObjectSystemRequest;
import gov.nist.csd.pm.proto.v1.pdp.query.ComputePersonalObjectSystemResponse;
import gov.nist.csd.pm.proto.v1.pdp.query.ComputePrivilegesRequest;
import gov.nist.csd.pm.proto.v1.pdp.query.ComputePrivilegesResponse;
import gov.nist.csd.pm.proto.v1.pdp.query.ComputeSubgraphPrivilegesRequest;
import gov.nist.csd.pm.proto.v1.pdp.query.ComputeSubgraphPrivilegesResponse;
import gov.nist.csd.pm.proto.v1.pdp.query.PolicyQueryServiceGrpc.PolicyQueryServiceBlockingStub;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GrpcSelfAccessQuerier implements SelfAccessQuery {

    private final PolicyQueryServiceBlockingStub blockingStub;
    private final UserContext userCtx;

    public GrpcSelfAccessQuerier(PolicyQueryServiceBlockingStub blockingStub, UserContext userCtx) {
        this.blockingStub = blockingStub;
        this.userCtx = userCtx;
    }

    @Override
    public AccessRightSet computePrivileges(TargetContext targetCtx) {
        ComputePrivilegesRequest request = ComputePrivilegesRequest.newBuilder()
            .setUserCtx(ToProtoUtil.toUserContextProto(userCtx))
            .setTargetCtx(ToProtoUtil.toTargetContextProto(targetCtx))
            .build();
        ComputePrivilegesResponse response = blockingStub.computePrivileges(request);
        return new AccessRightSet(response.getPrivilegesList());
    }

    @Override
    public List<AccessRightSet> computePrivileges(List<TargetContext> targetCtxs) {
        ComputePrivilegesRequest.Builder builder = ComputePrivilegesRequest.newBuilder()
            .setUserCtx(ToProtoUtil.toUserContextProto(userCtx));

        List<AccessRightSet> sets = new ArrayList<>();
        for (TargetContext targetCtx : targetCtxs) {
            ComputePrivilegesRequest request = builder
                .setTargetCtx(ToProtoUtil.toTargetContextProto(targetCtx))
                .build();
            ComputePrivilegesResponse response = blockingStub.computePrivileges(request);
            sets.add(new AccessRightSet(response.getPrivilegesList()));
        }

        return sets;
    }

    @Override
    public AccessRightSet computeDeniedPrivileges(TargetContext targetCtx) {
        ComputeDeniedPrivilegesRequest request = ComputeDeniedPrivilegesRequest.newBuilder()
            .setUserCtx(ToProtoUtil.toUserContextProto(userCtx))
            .setTargetCtx(ToProtoUtil.toTargetContextProto(targetCtx))
            .build();
        ComputeDeniedPrivilegesResponse response = blockingStub.computeDeniedPrivileges(request);
        return new AccessRightSet(response.getPrivilegesList());
    }

    @Override
    public SubgraphPrivileges computeSubgraphPrivileges(long root) {
        ComputeSubgraphPrivilegesRequest request = ComputeSubgraphPrivilegesRequest.newBuilder()
            .setUserCtx(ToProtoUtil.toUserContextProto(userCtx))
            .setRoot(ToProtoUtil.toNodeRefProto(root))
            .build();
        ComputeSubgraphPrivilegesResponse response = blockingStub.computeSubgraphPrivileges(request);
        return FromProtoUtil.fromProtoSubgraphPrivileges(response.getSubgraphPrivileges());
    }

    @Override
    public Map<Node, AccessRightSet> computeAdjacentAscendantPrivileges(long root) {
        ComputeAdjacentAscendantPrivilegesRequest request = ComputeAdjacentAscendantPrivilegesRequest.newBuilder()
            .setUserCtx(ToProtoUtil.toUserContextProto(userCtx))
            .setRoot(ToProtoUtil.toNodeRefProto(root))
            .build();
        ComputeAdjacentAscendantPrivilegesResponse response = blockingStub.computeAdjacentAscendantPrivileges(request);
        return FromProtoUtil.nodePrivilegesToNodeMap(response.getNodePrivilegesList());
    }

    @Override
    public Map<Node, AccessRightSet> computeAdjacentDescendantPrivileges(long root) {
        ComputeAdjacentDescendantPrivilegesRequest request = ComputeAdjacentDescendantPrivilegesRequest.newBuilder()
            .setUserCtx(ToProtoUtil.toUserContextProto(userCtx))
            .setRoot(ToProtoUtil.toNodeRefProto(root))
            .build();
        ComputeAdjacentDescendantPrivilegesResponse response = blockingStub.computeAdjacentDescendantPrivileges(request);
        return FromProtoUtil.nodePrivilegesToNodeMap(response.getNodePrivilegesList());
    }

    @Override
    public Map<Node, AccessRightSet> computePersonalObjectSystem() {
        ComputePersonalObjectSystemRequest request = ComputePersonalObjectSystemRequest.newBuilder()
            .setUserCtx(ToProtoUtil.toUserContextProto(userCtx))
            .build();
        ComputePersonalObjectSystemResponse response = blockingStub.computePersonalObjectSystem(request);
        return FromProtoUtil.nodePrivilegesToNodeMap(response.getNodePrivilegesList());
    }
}
