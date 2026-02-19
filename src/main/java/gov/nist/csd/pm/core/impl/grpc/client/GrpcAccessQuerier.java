package gov.nist.csd.pm.core.impl.grpc.client;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.impl.grpc.util.FromProtoUtil;
import gov.nist.csd.pm.core.impl.grpc.util.ToProtoUtil;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.query.AccessQuery;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pap.query.model.explain.Explain;
import gov.nist.csd.pm.core.pap.query.model.subgraph.SubgraphPrivileges;
import gov.nist.csd.pm.proto.v1.pdp.query.ComputeACLRequest;
import gov.nist.csd.pm.proto.v1.pdp.query.ComputeACLResponse;
import gov.nist.csd.pm.proto.v1.pdp.query.ComputeAdjacentAscendantPrivilegesRequest;
import gov.nist.csd.pm.proto.v1.pdp.query.ComputeAdjacentAscendantPrivilegesResponse;
import gov.nist.csd.pm.proto.v1.pdp.query.ComputeAdjacentDescendantPrivilegesRequest;
import gov.nist.csd.pm.proto.v1.pdp.query.ComputeAdjacentDescendantPrivilegesResponse;
import gov.nist.csd.pm.proto.v1.pdp.query.ComputeCapabilityListRequest;
import gov.nist.csd.pm.proto.v1.pdp.query.ComputeCapabilityListResponse;
import gov.nist.csd.pm.proto.v1.pdp.query.ComputeDeniedPrivilegesRequest;
import gov.nist.csd.pm.proto.v1.pdp.query.ComputeDeniedPrivilegesResponse;
import gov.nist.csd.pm.proto.v1.pdp.query.ComputeDestinationAttributesRequest;
import gov.nist.csd.pm.proto.v1.pdp.query.ComputeDestinationAttributesResponse;
import gov.nist.csd.pm.proto.v1.pdp.query.ComputePersonalObjectSystemRequest;
import gov.nist.csd.pm.proto.v1.pdp.query.ComputePersonalObjectSystemResponse;
import gov.nist.csd.pm.proto.v1.pdp.query.ComputePrivilegesRequest;
import gov.nist.csd.pm.proto.v1.pdp.query.ComputePrivilegesResponse;
import gov.nist.csd.pm.proto.v1.pdp.query.ComputeSubgraphPrivilegesRequest;
import gov.nist.csd.pm.proto.v1.pdp.query.ComputeSubgraphPrivilegesResponse;
import gov.nist.csd.pm.proto.v1.pdp.query.ExplainRequest;
import gov.nist.csd.pm.proto.v1.pdp.query.ExplainResponse;
import gov.nist.csd.pm.proto.v1.pdp.query.PolicyQueryServiceGrpc.PolicyQueryServiceBlockingStub;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GrpcAccessQuerier implements AccessQuery {

    private PolicyQueryServiceBlockingStub blockingStub;

    public GrpcAccessQuerier(PolicyQueryServiceBlockingStub blockingStub) {
        this.blockingStub = blockingStub;
    }

    @Override
    public AccessRightSet computePrivileges(UserContext userCtx, TargetContext targetCtx) {
        ComputePrivilegesRequest request = ComputePrivilegesRequest.newBuilder()
            .setUserCtx(ToProtoUtil.toUserContextProto(userCtx))
            .setTargetCtx(ToProtoUtil.toTargetContextProto(targetCtx))
            .build();
        ComputePrivilegesResponse computePrivilegesResponse = blockingStub.computePrivileges(request);
        return new AccessRightSet(computePrivilegesResponse.getPrivilegesList());
    }

    @Override
    public List<AccessRightSet> computePrivileges(UserContext userCtx, List<TargetContext> targetCtxs) {
        ComputePrivilegesRequest.Builder builder = ComputePrivilegesRequest.newBuilder()
            .setUserCtx(ToProtoUtil.toUserContextProto(userCtx));

        List<AccessRightSet> sets = new ArrayList<>();
        for (TargetContext targetCtx : targetCtxs) {
            ComputePrivilegesRequest request = builder.setTargetCtx(ToProtoUtil.toTargetContextProto(targetCtx))
                .build();

            ComputePrivilegesResponse computePrivilegesResponse = blockingStub.computePrivileges(request);
            sets.add(new AccessRightSet(computePrivilegesResponse.getPrivilegesList()));
        }

        return sets;
    }

    @Override
    public AccessRightSet computeDeniedPrivileges(UserContext userCtx, TargetContext targetCtx) {
        ComputeDeniedPrivilegesRequest request = ComputeDeniedPrivilegesRequest.newBuilder()
            .setUserCtx(ToProtoUtil.toUserContextProto(userCtx))
            .setTargetCtx(ToProtoUtil.toTargetContextProto(targetCtx))
            .build();
        ComputeDeniedPrivilegesResponse response = blockingStub.computeDeniedPrivileges(request);
        return new AccessRightSet(response.getPrivilegesList());
    }

    @Override
    public Map<Long, AccessRightSet> computeCapabilityList(UserContext userCtx) {
        ComputeCapabilityListRequest request = ComputeCapabilityListRequest.newBuilder()
            .setUserCtx(ToProtoUtil.toUserContextProto(userCtx))
            .build();
        ComputeCapabilityListResponse response = blockingStub.computeCapabilityList(request);
        return FromProtoUtil.nodePrivilegesToIdMap(response.getNodePrivilegesList());
    }

    @Override
    public Map<Long, AccessRightSet> computeACL(TargetContext targetCtx) {
        ComputeACLRequest request = ComputeACLRequest.newBuilder()
            .setTargetCtx(ToProtoUtil.toTargetContextProto(targetCtx))
            .build();
        ComputeACLResponse response = blockingStub.computeACL(request);
        return FromProtoUtil.nodePrivilegesToIdMap(response.getNodePrivilegesList());
    }

    @Override
    public Map<Long, AccessRightSet> computeDestinationAttributes(UserContext userCtx) {
        ComputeDestinationAttributesRequest request = ComputeDestinationAttributesRequest.newBuilder()
            .setUserCtx(ToProtoUtil.toUserContextProto(userCtx))
            .build();
        ComputeDestinationAttributesResponse response = blockingStub.computeDestinationAttributes(request);
        return FromProtoUtil.nodePrivilegesToIdMap(response.getNodePrivilegesList());
    }

    @Override
    public SubgraphPrivileges computeSubgraphPrivileges(UserContext userCtx, long root) {
        ComputeSubgraphPrivilegesRequest request = ComputeSubgraphPrivilegesRequest.newBuilder()
            .setUserCtx(ToProtoUtil.toUserContextProto(userCtx))
            .setRoot(ToProtoUtil.toNodeRefProto(root))
            .build();
        ComputeSubgraphPrivilegesResponse response = blockingStub.computeSubgraphPrivileges(request);
        return FromProtoUtil.fromProtoSubgraphPrivileges(response.getSubgraphPrivileges());
    }

    @Override
    public Map<Node, AccessRightSet> computeAdjacentAscendantPrivileges(UserContext userCtx, long root) throws
                                                                                                        PMException {
        ComputeAdjacentAscendantPrivilegesRequest request = ComputeAdjacentAscendantPrivilegesRequest.newBuilder()
            .setUserCtx(ToProtoUtil.toUserContextProto(userCtx))
            .setRoot(ToProtoUtil.toNodeRefProto(root))
            .build();
        ComputeAdjacentAscendantPrivilegesResponse response = blockingStub.computeAdjacentAscendantPrivileges(request);
        return FromProtoUtil.nodePrivilegesToNodeMap(response.getNodePrivilegesList());
    }

    @Override
    public Map<Node, AccessRightSet> computeAdjacentDescendantPrivileges(UserContext userCtx, long root) throws
                                                                                                         PMException {
        ComputeAdjacentDescendantPrivilegesRequest request = ComputeAdjacentDescendantPrivilegesRequest.newBuilder()
            .setUserCtx(ToProtoUtil.toUserContextProto(userCtx))
            .setRoot(ToProtoUtil.toNodeRefProto(root))
            .build();
        ComputeAdjacentDescendantPrivilegesResponse response = blockingStub.computeAdjacentDescendantPrivileges(request);
        return FromProtoUtil.nodePrivilegesToNodeMap(response.getNodePrivilegesList());
    }

    @Override
    public Explain explain(UserContext userCtx, TargetContext targetCtx) {
        ExplainRequest request = ExplainRequest.newBuilder()
            .setUserCtx(ToProtoUtil.toUserContextProto(userCtx))
            .setTargetCtx(ToProtoUtil.toTargetContextProto(targetCtx))
            .build();
        ExplainResponse response = blockingStub.explain(request);
        return FromProtoUtil.fromProtoExplainResponse(response);
    }

    @Override
    public Map<Node, AccessRightSet> computePersonalObjectSystem(UserContext userCtx) {
        ComputePersonalObjectSystemRequest request = ComputePersonalObjectSystemRequest.newBuilder()
            .setUserCtx(ToProtoUtil.toUserContextProto(userCtx))
            .build();
        ComputePersonalObjectSystemResponse response = blockingStub.computePersonalObjectSystem(request);
        return FromProtoUtil.nodePrivilegesToNodeMap(response.getNodePrivilegesList());
    }
}
