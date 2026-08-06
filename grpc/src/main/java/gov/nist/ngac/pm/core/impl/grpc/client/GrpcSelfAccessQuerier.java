package gov.nist.ngac.pm.core.impl.grpc.client;

import gov.nist.ngac.pm.core.common.graph.node.Node;
import gov.nist.ngac.pm.core.impl.grpc.util.FromProtoUtil;
import gov.nist.ngac.pm.core.impl.grpc.util.ToProtoUtil;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.query.SelfAccessQuery;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeTargetContext;
import gov.nist.ngac.pm.core.pap.query.model.context.TargetContext;
import gov.nist.ngac.pm.core.pap.query.model.subgraph.SubgraphPrivileges;
import gov.nist.ngac.pm.proto.v1.pdp.query.PolicyQueryServiceGrpc.PolicyQueryServiceBlockingStub;
import gov.nist.ngac.pm.proto.v1.pdp.query.SelfComputeAdjacentAscendantPrivilegesRequest;
import gov.nist.ngac.pm.proto.v1.pdp.query.SelfComputeAdjacentAscendantPrivilegesResponse;
import gov.nist.ngac.pm.proto.v1.pdp.query.SelfComputeAdjacentDescendantPrivilegesRequest;
import gov.nist.ngac.pm.proto.v1.pdp.query.SelfComputeAdjacentDescendantPrivilegesResponse;
import gov.nist.ngac.pm.proto.v1.pdp.query.SelfComputePersonalObjectSystemRequest;
import gov.nist.ngac.pm.proto.v1.pdp.query.SelfComputePersonalObjectSystemResponse;
import gov.nist.ngac.pm.proto.v1.pdp.query.SelfComputePrivilegesRequest;
import gov.nist.ngac.pm.proto.v1.pdp.query.SelfComputePrivilegesResponse;
import gov.nist.ngac.pm.proto.v1.pdp.query.SelfComputeSubgraphPrivilegesRequest;
import gov.nist.ngac.pm.proto.v1.pdp.query.SelfComputeSubgraphPrivilegesResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A {@link SelfAccessQuery} that delegates to a remote PDP over gRPC. The caller is identified by the
 * gRPC request headers.
 */
public class GrpcSelfAccessQuerier implements SelfAccessQuery {

    private final PolicyQueryServiceBlockingStub blockingStub;

    public GrpcSelfAccessQuerier(PolicyQueryServiceBlockingStub blockingStub) {
        this.blockingStub = blockingStub;
    }

    @Override
    public AccessRightSet computePrivileges(TargetContext targetCtx) {
        SelfComputePrivilegesRequest request = SelfComputePrivilegesRequest.newBuilder()
            .setTargetCtx(ToProtoUtil.toTargetContextProto(targetCtx))
            .build();
        SelfComputePrivilegesResponse response = blockingStub.selfComputePrivileges(request);
        return new AccessRightSet(response.getPrivilegesList());
    }

    /**
     * Convenience overload of {@link #computePrivileges(TargetContext)} for a target identified by node id.
     *
     * @param id the target node's id
     * @return the caller's privileges on the target
     */
    public AccessRightSet computePrivileges(long id) {
        SelfComputePrivilegesRequest request = SelfComputePrivilegesRequest.newBuilder()
            .setTargetCtx(ToProtoUtil.toTargetContextProto(NodeTargetContext.of(id)))
            .build();
        SelfComputePrivilegesResponse response = blockingStub.selfComputePrivileges(request);
        return new AccessRightSet(response.getPrivilegesList());
    }

    /**
     * Convenience overload of {@link #computePrivileges(TargetContext)} for a target identified by node name.
     *
     * @param name the target node's name
     * @return the caller's privileges on the target
     */
    public AccessRightSet computePrivileges(String name) {
        SelfComputePrivilegesRequest request = SelfComputePrivilegesRequest.newBuilder()
            .setTargetCtx(ToProtoUtil.toTargetContextProto(NodeTargetContext.of(name)))
            .build();
        SelfComputePrivilegesResponse response = blockingStub.selfComputePrivileges(request);
        return new AccessRightSet(response.getPrivilegesList());
    }

    @Override
    public List<AccessRightSet> computePrivileges(List<TargetContext> targetCtxs) {
        List<AccessRightSet> sets = new ArrayList<>();
        for (TargetContext targetCtx : targetCtxs) {
            sets.add(computePrivileges(targetCtx));
        }
        return sets;
    }

    @Override
    public AccessRightSet computeDeniedPrivileges(TargetContext targetCtx) {
        throw new UnsupportedOperationException("selfComputeDeniedPrivileges not supported");
    }

    @Override
    public SubgraphPrivileges computeSubgraphPrivileges(long root) {
        SelfComputeSubgraphPrivilegesRequest request = SelfComputeSubgraphPrivilegesRequest.newBuilder()
            .setRoot(ToProtoUtil.toNodeRefProto(root))
            .build();
        SelfComputeSubgraphPrivilegesResponse response = blockingStub.selfComputeSubgraphPrivileges(request);
        return FromProtoUtil.fromProtoSubgraphPrivileges(response.getSubgraphPrivileges());
    }

    /**
     * Convenience overload of {@link #computeSubgraphPrivileges(long)} for a root identified by node name.
     *
     * @param root the root node's name
     * @return the caller's privileges over the root and its descendant subgraph
     */
    public SubgraphPrivileges computeSubgraphPrivileges(String root) {
        SelfComputeSubgraphPrivilegesRequest request = SelfComputeSubgraphPrivilegesRequest.newBuilder()
            .setRoot(ToProtoUtil.toNodeRefProto(root))
            .build();
        SelfComputeSubgraphPrivilegesResponse response = blockingStub.selfComputeSubgraphPrivileges(request);
        return FromProtoUtil.fromProtoSubgraphPrivileges(response.getSubgraphPrivileges());
    }

    @Override
    public Map<Node, AccessRightSet> computeAdjacentAscendantPrivileges(long root) {
        SelfComputeAdjacentAscendantPrivilegesRequest request = SelfComputeAdjacentAscendantPrivilegesRequest.newBuilder()
            .setRoot(ToProtoUtil.toNodeRefProto(root))
            .build();
        SelfComputeAdjacentAscendantPrivilegesResponse response = blockingStub.selfComputeAdjacentAscendantPrivileges(request);
        return FromProtoUtil.nodePrivilegesToNodeMap(response.getNodePrivilegesList());
    }

    /**
     * Convenience overload of {@link #computeAdjacentAscendantPrivileges(long)} for a root identified by
     * node name.
     *
     * @param root the root node's name
     * @return the caller's privileges on each node directly ascendant to the root
     */
    public Map<Node, AccessRightSet> computeAdjacentAscendantPrivileges(String root) {
        SelfComputeAdjacentAscendantPrivilegesRequest request = SelfComputeAdjacentAscendantPrivilegesRequest.newBuilder()
            .setRoot(ToProtoUtil.toNodeRefProto(root))
            .build();
        SelfComputeAdjacentAscendantPrivilegesResponse response = blockingStub.selfComputeAdjacentAscendantPrivileges(request);
        return FromProtoUtil.nodePrivilegesToNodeMap(response.getNodePrivilegesList());
    }

    @Override
    public Map<Node, AccessRightSet> computeAdjacentDescendantPrivileges(long root) {
        SelfComputeAdjacentDescendantPrivilegesRequest request = SelfComputeAdjacentDescendantPrivilegesRequest.newBuilder()
            .setRoot(ToProtoUtil.toNodeRefProto(root))
            .build();
        SelfComputeAdjacentDescendantPrivilegesResponse response = blockingStub.selfComputeAdjacentDescendantPrivileges(request);
        return FromProtoUtil.nodePrivilegesToNodeMap(response.getNodePrivilegesList());
    }

    @Override
    public Map<Node, AccessRightSet> computePersonalObjectSystem() {
        SelfComputePersonalObjectSystemRequest request = SelfComputePersonalObjectSystemRequest.newBuilder()
            .build();
        SelfComputePersonalObjectSystemResponse response = blockingStub.selfComputePersonalObjectSystem(request);
        return FromProtoUtil.nodePrivilegesToNodeMap(response.getNodePrivilegesList());
    }

    @Override
    public Map<Long, Set<Long>> computeRequiredAttributeSets(TargetContext targetCtx, AccessRightSet privileges) {
        throw new UnsupportedOperationException("selfComputeRequiredAttributeSets not supported");
    }

    @Override
    public Map<Long, AccessRightSet> computeCapabilityList() {
        throw new UnsupportedOperationException("selfComputeCapabilityList not supported");
    }
}
