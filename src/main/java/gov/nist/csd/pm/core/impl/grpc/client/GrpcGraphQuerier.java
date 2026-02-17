package gov.nist.csd.pm.core.impl.grpc.pap;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.common.graph.node.NodeType;
import gov.nist.csd.pm.core.impl.grpc.util.FromProtoUtil;
import gov.nist.csd.pm.core.pap.graph.Association;
import gov.nist.csd.pm.core.pap.query.GraphQuerier;
import gov.nist.csd.pm.core.pap.query.GraphQuery;
import gov.nist.csd.pm.core.pap.query.model.subgraph.Subgraph;
import gov.nist.csd.pm.proto.v1.model.NodeRef;
import gov.nist.csd.pm.proto.v1.pdp.query.*;
import gov.nist.csd.pm.proto.v1.pdp.query.PolicyQueryServiceGrpc.PolicyQueryServiceBlockingStub;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class GrpcGraphQuerier implements GraphQuery {

    private final PolicyQueryServiceBlockingStub blockingStub;

    public GrpcGraphQuerier(PolicyQueryServiceBlockingStub blockingStub) {
        this.blockingStub = blockingStub;
    }

    @Override
    public boolean nodeExists(long id) throws PMException {
        NodeExistsRequest request = NodeExistsRequest.newBuilder()
            .setNode(NodeRef.newBuilder().setId(id).build())
            .build();
        NodeExistsResponse response = blockingStub.nodeExists(request);
        return response.getExists();
    }

    @Override
    public boolean nodeExists(String name) throws PMException {
        NodeExistsRequest request = NodeExistsRequest.newBuilder()
            .setNode(NodeRef.newBuilder().setName(name).build())
            .build();
        NodeExistsResponse response = blockingStub.nodeExists(request);
        return response.getExists();
    }

    @Override
    public Node getNodeByName(String name) throws PMException {
        GetNodeRequest request = GetNodeRequest.newBuilder()
            .setNode(NodeRef.newBuilder().setName(name).build())
            .build();
        GetNodeResponse response = blockingStub.getNode(request);
        return FromProtoUtil.fromProtoNode(response.getNode());
    }

    @Override
    public long getNodeId(String name) throws PMException {
        GetNodeIdRequest request = GetNodeIdRequest.newBuilder()
            .setName(name)
            .build();
        GetNodeIdResponse response = blockingStub.getNodeId(request);
        return response.getId();
    }

    @Override
    public Node getNodeById(long id) throws PMException {
        GetNodeRequest request = GetNodeRequest.newBuilder()
            .setNode(NodeRef.newBuilder().setId(id).build())
            .build();
        GetNodeResponse response = blockingStub.getNode(request);
        return FromProtoUtil.fromProtoNode(response.getNode());
    }

    @Override
    public Collection<Node> search(NodeType type, Map<String, String> properties) throws PMException {
        SearchNodesRequest.Builder builder = SearchNodesRequest.newBuilder();
        if (type != null) {
            builder.setType(gov.nist.csd.pm.proto.v1.model.NodeType.valueOf(type.name()));
        }
        if (properties != null) {
            builder.putAllProperties(properties);
        }
        SearchNodesResponse response = blockingStub.searchNodes(builder.build());
        List<Node> nodes = new ArrayList<>();
        for (gov.nist.csd.pm.proto.v1.model.Node protoNode : response.getNodesList()) {
            nodes.add(FromProtoUtil.fromProtoNode(protoNode));
        }
        return nodes;
    }

    @Override
    public Collection<Long> getPolicyClasses() throws PMException {
        GetPolicyClassesResponse response = blockingStub.getPolicyClasses(GetPolicyClassesRequest.newBuilder().build());
        List<Long> ids = new ArrayList<>();
        for (gov.nist.csd.pm.proto.v1.model.Node node : response.getPolicyClassesList()) {
            ids.add(node.getId());
        }
        return ids;
    }

    @Override
    public Collection<Long> getAdjacentDescendants(long nodeId) throws PMException {
        GetAdjacentDescendantsRequest request = GetAdjacentDescendantsRequest.newBuilder()
            .setNode(NodeRef.newBuilder().setId(nodeId).build())
            .build();
        GetAdjacentDescendantsResponse response = blockingStub.getAdjacentDescendants(request);
        List<Long> ids = new ArrayList<>();
        for (gov.nist.csd.pm.proto.v1.model.Node node : response.getNodesList()) {
            ids.add(node.getId());
        }
        return ids;
    }

    @Override
    public Collection<Long> getAdjacentAscendants(long nodeId) throws PMException {
        GetAdjacentAscendantsRequest request = GetAdjacentAscendantsRequest.newBuilder()
            .setNode(NodeRef.newBuilder().setId(nodeId).build())
            .build();
        GetAdjacentAscendantsResponse response = blockingStub.getAdjacentAscendants(request);
        List<Long> ids = new ArrayList<>();
        for (gov.nist.csd.pm.proto.v1.model.Node node : response.getNodesList()) {
            ids.add(node.getId());
        }
        return ids;
    }

    @Override
    public Collection<Association> getAssociationsWithSource(long uaId) throws PMException {
        GetAssociationsWithSourceRequest request = GetAssociationsWithSourceRequest.newBuilder()
            .setNode(NodeRef.newBuilder().setId(uaId).build())
            .build();
        GetAssociationsWithSourceResponse response = blockingStub.getAssociationsWithSource(request);
        List<Association> associations = new ArrayList<>();
        for (gov.nist.csd.pm.proto.v1.model.Association protoAssoc : response.getAssociationsList()) {
            associations.add(FromProtoUtil.fromAssociationProto(protoAssoc));
        }
        return associations;
    }

    @Override
    public Collection<Association> getAssociationsWithTarget(long targetId) throws PMException {
        GetAssociationsWithTargetRequest request = GetAssociationsWithTargetRequest.newBuilder()
            .setNode(NodeRef.newBuilder().setId(targetId).build())
            .build();
        GetAssociationsWithTargetResponse response = blockingStub.getAssociationsWithTarget(request);
        List<Association> associations = new ArrayList<>();
        for (gov.nist.csd.pm.proto.v1.model.Association protoAssoc : response.getAssociationsList()) {
            associations.add(FromProtoUtil.fromAssociationProto(protoAssoc));
        }
        return associations;
    }

    @Override
    public Subgraph getAscendantSubgraph(long nodeId) throws PMException {
        GetAscendantSubgraphRequest request = GetAscendantSubgraphRequest.newBuilder()
            .setNode(NodeRef.newBuilder().setId(nodeId).build())
            .build();
        GetAscendantSubgraphResponse response = blockingStub.getAscendantSubgraph(request);
        return FromProtoUtil.fromSubgraphProto(response.getSubgraph());
    }

    @Override
    public Subgraph getDescendantSubgraph(long nodeId) throws PMException {
        GetDescendantSubgraphRequest request = GetDescendantSubgraphRequest.newBuilder()
            .setNode(NodeRef.newBuilder().setId(nodeId).build())
            .build();
        GetDescendantSubgraphResponse response = blockingStub.getDescendantSubgraph(request);
        return FromProtoUtil.fromSubgraphProto(response.getSubgraph());
    }

    @Override
    public Collection<Long> getAttributeDescendants(long nodeId) throws PMException {
        GetAttributeDescendantsRequest request = GetAttributeDescendantsRequest.newBuilder()
            .setNode(NodeRef.newBuilder().setId(nodeId).build())
            .build();
        GetAttributeDescendantsResponse response = blockingStub.getAttributeDescendants(request);
        List<Long> ids = new ArrayList<>();
        for (gov.nist.csd.pm.proto.v1.model.Node node : response.getNodesList()) {
            ids.add(node.getId());
        }
        return ids;
    }

    @Override
    public Collection<Long> getPolicyClassDescendants(long nodeId) throws PMException {
        GetPolicyClassDescendantsRequest request = GetPolicyClassDescendantsRequest.newBuilder()
            .setNode(NodeRef.newBuilder().setId(nodeId).build())
            .build();
        GetPolicyClassDescendantsResponse response = blockingStub.getPolicyClassDescendants(request);
        List<Long> ids = new ArrayList<>();
        for (gov.nist.csd.pm.proto.v1.model.Node node : response.getNodesList()) {
            ids.add(node.getId());
        }
        return ids;
    }

    @Override
    public boolean isAscendant(long ascendantId, long descendantId) throws PMException {
        IsAscendantRequest request = IsAscendantRequest.newBuilder()
            .setAscendant(NodeRef.newBuilder().setId(ascendantId).build())
            .setDescendant(NodeRef.newBuilder().setId(descendantId).build())
            .build();
        IsAscendantResponse response = blockingStub.isAscendant(request);
        return response.getResult();
    }

    @Override
    public boolean isDescendant(long ascendantId, long descendantId) throws PMException {
        IsDescendantRequest request = IsDescendantRequest.newBuilder()
            .setAscendant(NodeRef.newBuilder().setId(ascendantId).build())
            .setDescendant(NodeRef.newBuilder().setId(descendantId).build())
            .build();
        IsDescendantResponse response = blockingStub.isDescendant(request);
        return response.getResult();
    }
}
