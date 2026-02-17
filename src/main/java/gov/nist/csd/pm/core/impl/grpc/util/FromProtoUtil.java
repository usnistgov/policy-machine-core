package gov.nist.csd.pm.core.impl.grpc.util;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.common.graph.node.NodeType;
import gov.nist.csd.pm.core.pap.graph.Association;
import gov.nist.csd.pm.core.pap.obligation.Obligation;
import gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes;
import gov.nist.csd.pm.core.pap.operation.arg.type.ListType;
import gov.nist.csd.pm.core.pap.operation.arg.type.MapType;
import gov.nist.csd.pm.core.pap.operation.arg.type.Type;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.proto.v1.pdp.query.Param;
import gov.nist.csd.pm.proto.v1.pdp.query.ParamType;
import gov.nist.csd.pm.core.common.prohibition.NodeProhibition;
import gov.nist.csd.pm.core.common.prohibition.ProcessProhibition;
import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.epp.EventContext;
import gov.nist.csd.pm.core.epp.EventContextUser;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pap.query.model.explain.Explain;
import gov.nist.csd.pm.core.pap.query.model.explain.ExplainAssociation;
import gov.nist.csd.pm.core.pap.query.model.explain.ExplainNode;
import gov.nist.csd.pm.core.pap.query.model.explain.Path;
import gov.nist.csd.pm.core.pap.query.model.explain.PolicyClassExplain;
import gov.nist.csd.pm.core.pap.query.model.subgraph.Subgraph;
import gov.nist.csd.pm.core.pap.query.model.subgraph.SubgraphPrivileges;
import gov.nist.csd.pm.proto.v1.model.NodeRef;
import gov.nist.csd.pm.proto.v1.model.Value;
import gov.nist.csd.pm.proto.v1.model.ValueList;
import gov.nist.csd.pm.proto.v1.model.ValueMap;
import gov.nist.csd.pm.proto.v1.pdp.query.ExplainResponse;
import gov.nist.csd.pm.proto.v1.pdp.query.NodePrivileges;
import java.util.*;
import java.util.stream.Collectors;

public class FromProtoUtil {

    public static UserContext fromUserContextProto(PAP pap,
                                                   gov.nist.csd.pm.proto.v1.pdp.query.UserContext userCtxProto) throws PMException {
        String process = userCtxProto.getProcess();

        return switch (userCtxProto.getUserCase()) {
            case USER_NODE ->
                new UserContext(resolveNodeRefId(pap, userCtxProto.getUserNode()), process);
            case USER_ATTRIBUTES ->
                new UserContext(resolveNodeRefIdList(pap, userCtxProto.getUserAttributes().getNodesList()), process);
            case USER_NOT_SET ->
                throw new IllegalArgumentException("user context not set");
        };
    }

    public static TargetContext fromTargetContextProto(PAP pap,
                                                       gov.nist.csd.pm.proto.v1.pdp.query.TargetContext targetCtxProto) throws PMException {
        return switch (targetCtxProto.getTargetCase()) {
            case TARGET_NODE ->
                new TargetContext(resolveNodeRefId(pap, targetCtxProto.getTargetNode()));
            case TARGET_ATTRIBUTES ->
                new TargetContext(resolveNodeRefIdList(pap, targetCtxProto.getTargetAttributes().getNodesList()));
            case TARGET_NOT_SET ->
                throw new IllegalArgumentException("target context not set");
        };
    }

    public static long resolveNodeRefId(PAP pap, NodeRef nodeRef) throws PMException {
        return switch (nodeRef.getRefCase()) {
            case ID -> nodeRef.getId();
            case NAME -> pap.query().graph().getNodeByName(nodeRef.getName()).getId();
            case REF_NOT_SET -> throw new IllegalArgumentException("node reference not set");
        };
    }

    public static List<Long> resolveNodeRefIdList(PAP pap, List<NodeRef> nodeRefs) throws PMException {
        List<Long> nodeRefIds = new ArrayList<>();
        for (NodeRef nr : nodeRefs) {
            nodeRefIds.add(resolveNodeRefId(pap, nr));
        }

        return nodeRefIds;
    }

    public static Node fromProtoNode(gov.nist.csd.pm.proto.v1.model.Node protoNode) {
        NodeType nodeType = switch (protoNode.getType()) {
            case PC -> NodeType.PC;
            case UA -> NodeType.UA;
            case OA -> NodeType.OA;
            case U -> NodeType.U;
            case O -> NodeType.O;
            case ANY -> NodeType.ANY;
            default -> throw new IllegalArgumentException("Unknown node type: " + protoNode.getType());
        };
        return new Node(protoNode.getId(), protoNode.getName(), nodeType, new HashMap<>(protoNode.getPropertiesMap()));
    }

    public static Map<Long, AccessRightSet> nodePrivilegesToIdMap(List<NodePrivileges> nodePrivilegesList) {
        Map<Long, AccessRightSet> result = new HashMap<>();
        for (NodePrivileges np : nodePrivilegesList) {
            result.put(np.getNode().getId(), new AccessRightSet(np.getArsetList()));
        }
        return result;
    }

    public static Map<Node, AccessRightSet> nodePrivilegesToNodeMap(List<NodePrivileges> nodePrivilegesList) {
        Map<Node, AccessRightSet> result = new HashMap<>();
        for (NodePrivileges np : nodePrivilegesList) {
            result.put(fromProtoNode(np.getNode()), new AccessRightSet(np.getArsetList()));
        }
        return result;
    }

    public static SubgraphPrivileges fromProtoSubgraphPrivileges(
            gov.nist.csd.pm.proto.v1.pdp.query.SubgraphPrivileges proto) {
        Node node = fromProtoNode(proto.getNode());
        AccessRightSet privileges = new AccessRightSet(proto.getArsetList());
        List<SubgraphPrivileges> ascendants = proto.getAscendantsList().stream()
            .map(FromProtoUtil::fromProtoSubgraphPrivileges)
            .collect(Collectors.toList());
        return new SubgraphPrivileges(node, privileges, ascendants);
    }

    public static Explain fromProtoExplainResponse(ExplainResponse response) {
        AccessRightSet privileges = new AccessRightSet(response.getPrivilegesList());
        AccessRightSet deniedPrivileges = new AccessRightSet(response.getDeniedPrivilegesList());

        List<PolicyClassExplain> policyClasses = response.getPolicyClassesList().stream()
            .map(FromProtoUtil::fromProtoPolicyClassExplain)
            .collect(Collectors.toList());

        List<Prohibition> prohibitions = response.getProhibitionsList().stream()
            .map(FromProtoUtil::fromProtoProhibition)
            .collect(Collectors.toList());

        return new Explain(privileges, policyClasses, deniedPrivileges, prohibitions);
    }

    public static PolicyClassExplain fromProtoPolicyClassExplain(
            gov.nist.csd.pm.proto.v1.pdp.query.PolicyClassExplain proto) {
        Node pc = fromProtoNode(proto.getPc());
        AccessRightSet arset = new AccessRightSet(proto.getArsetList());
        Collection<List<ExplainNode>> paths = proto.getPathsList().stream()
            .map(explainNodePath -> explainNodePath.getNodesList().stream()
                .map(FromProtoUtil::fromProtoExplainNode)
                .collect(Collectors.<ExplainNode>toList()))
            .collect(Collectors.toList());
        return new PolicyClassExplain(pc, arset, paths);
    }

    public static ExplainNode fromProtoExplainNode(gov.nist.csd.pm.proto.v1.pdp.query.ExplainNode proto) {
        Node node = fromProtoNode(proto.getNode());
        Collection<ExplainAssociation> associations = proto.getAssociationsList().stream()
            .map(FromProtoUtil::fromProtoExplainAssociation)
            .collect(Collectors.toList());
        return new ExplainNode(node, associations);
    }

    public static ExplainAssociation fromProtoExplainAssociation(
            gov.nist.csd.pm.proto.v1.pdp.query.ExplainAssociation proto) {
        Node ua = fromProtoNode(proto.getUa());
        AccessRightSet arset = new AccessRightSet(proto.getArsetList());
        Collection<Path> userPaths = proto.getUserPathsList().stream()
            .map(protoPath -> {
                Path path = new Path();
                for (gov.nist.csd.pm.proto.v1.model.Node n : protoPath.getNodesList()) {
                    path.add(fromProtoNode(n));
                }
                return path;
            })
            .collect(Collectors.toList());
        return new ExplainAssociation(ua, arset, userPaths);
    }

    public static Prohibition fromProtoProhibition(gov.nist.csd.pm.proto.v1.model.Prohibition proto) {
        String name = proto.getName();
        AccessRightSet arset = new AccessRightSet(proto.getArsetList());
        Set<Long> inclusionSet = proto.getInclusionSetList().stream()
            .map(gov.nist.csd.pm.proto.v1.model.Node::getId)
            .collect(Collectors.toSet());
        Set<Long> exclusionSet = proto.getExclusionSetList().stream()
            .map(gov.nist.csd.pm.proto.v1.model.Node::getId)
            .collect(Collectors.toSet());
        boolean isConjunctive = proto.getIsConjunctive();

        if (proto.hasProcess()) {
            return new ProcessProhibition(name, proto.getNode().getId(), proto.getProcess(), arset,
                inclusionSet, exclusionSet, isConjunctive);
        } else {
            return new NodeProhibition(name, proto.getNode().getId(), arset,
                inclusionSet, exclusionSet, isConjunctive);
        }
    }

    public static Association fromAssociationProto(gov.nist.csd.pm.proto.v1.model.Association proto) {
        long source = proto.getUa().getId();
        long target = proto.getTarget().getId();
        AccessRightSet arset = new AccessRightSet(proto.getArsetList());
        return new Association(source, target, arset);
    }

    public static Subgraph fromSubgraphProto(gov.nist.csd.pm.proto.v1.pdp.query.Subgraph proto) {
        Node node = fromProtoNode(proto.getNode());
        List<Subgraph> subgraphs = proto.getSubgraphsList().stream()
            .map(FromProtoUtil::fromSubgraphProto)
            .collect(Collectors.toList());
        return new Subgraph(node, subgraphs);
    }

    public static Obligation fromObligationProto(gov.nist.csd.pm.proto.v1.model.Obligation proto) {
        Obligation obligation = new Obligation();
        obligation.setName(proto.getName());
        if (proto.hasAuthor()) {
            obligation.setAuthorId(proto.getAuthor().getId());
        }
        return obligation;
    }

    public static Type<?> fromProtoParamType(ParamType paramType) {
        return switch (paramType.getTypeCase()) {
            case STRING_TYPE -> BasicTypes.STRING_TYPE;
            case LONG_TYPE -> BasicTypes.LONG_TYPE;
            case BOOLEAN_TYPE -> BasicTypes.BOOLEAN_TYPE;
            case LIST_TYPE -> ListType.of(fromProtoParamType(paramType.getListType().getElementType()));
            case MAP_TYPE -> MapType.of(
                fromProtoParamType(paramType.getMapType().getKeyType()),
                fromProtoParamType(paramType.getMapType().getValueType())
            );
            case ANY_TYPE -> BasicTypes.ANY_TYPE;
            case TYPE_NOT_SET -> BasicTypes.ANY_TYPE;
        };
    }

    public static FormalParameter<?> fromProtoParam(Param param) {
        return switch (param.getKindCase()) {
            case FORMAL_PARAM -> new FormalParameter<>(param.getName(), fromProtoParamType(param.getFormalParam().getType()));
            case NODE_ID_FORMAL_PARAM -> new FormalParameter<>(param.getName(), BasicTypes.LONG_TYPE);
            case NODE_ID_LIST_FORMAL_PARAM -> new FormalParameter<>(param.getName(), ListType.of(BasicTypes.LONG_TYPE));
            case NODE_NAME_FORMAL_PARAM -> new FormalParameter<>(param.getName(), BasicTypes.STRING_TYPE);
            case NODE_NAME_LIST_FORMAL_PARAM -> new FormalParameter<>(param.getName(), ListType.of(BasicTypes.STRING_TYPE));
            case KIND_NOT_SET -> new FormalParameter<>(param.getName(), BasicTypes.ANY_TYPE);
        };
    }

    public static List<FormalParameter<?>> fromProtoParams(List<Param> params) {
        List<FormalParameter<?>> formalParams = new ArrayList<>();
        for (Param param : params) {
            formalParams.add(fromProtoParam(param));
        }
        return formalParams;
    }

    public static Map<String, Object> fromValueMap(ValueMap valueMap) {
        Map<String, Object> converted = new HashMap<>();

        Map<String, Value> values = valueMap.getValuesMap();
        for (Map.Entry<String, Value> entry : values.entrySet()) {
            converted.put(entry.getKey(), fromValue(entry.getValue()));
        }

        return converted;
    }

    public static Object fromValue(Value value) {
        return switch (value.getDataCase()) {
            case INT64_VALUE -> value.getInt64Value();
            case STRING_VALUE -> value.getStringValue();
            case BOOL_VALUE -> value.getBoolValue();
            case LIST_VALUE -> fromListValue(value.getListValue());
            case MAP_VALUE -> fromMapValue(value.getMapValue());
            case DATA_NOT_SET -> throw new IllegalArgumentException("value data field is not set");
        };
    }

    private static List<Object> fromListValue(ValueList valueList) {
        List<Object> result = new ArrayList<>();
        for (Value v : valueList.getValuesList()) {
            result.add(fromValue(v));
        }

        return result;
    }

    private static Map<Object, Object> fromMapValue(ValueMap valueMap) {
        Map<Object, Object> result = new HashMap<>();
        for(Map.Entry<String, Value> e : valueMap.getValuesMap().entrySet()) {
            result.put(e.getKey(), fromValue(e.getValue()));
        }

        return result;
    }

    public static EventContext fromEventContextProto(gov.nist.csd.pm.proto.v1.epp.EventContext proto) {
        String process = proto.getProcess();

        EventContextUser user = switch (proto.getUserCase()) {
            case USER_NAME -> new EventContextUser(proto.getUserName(), process);
            case USER_ATTRS -> new EventContextUser(proto.getUserAttrs().getValuesList(), process);
            case USER_NOT_SET -> throw new IllegalStateException("User not set");
        };

        Map<String, Object> args = new HashMap<>();
        ValueMap protoArgs = proto.getArgs();

        for (Map.Entry<String, Value> e : protoArgs.getValuesMap().entrySet()) {
            String name = e.getKey();
            Value value = e.getValue();

            args.put(name, fromValue(value));
        }

        return new EventContext(user, proto.getOpName(), args);
    }
}
