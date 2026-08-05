package gov.nist.ngac.pm.core.impl.grpc.util;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.graph.node.Node;
import gov.nist.ngac.pm.core.common.graph.node.NodeType;
import gov.nist.ngac.pm.core.common.prohibition.NodeProhibition;
import gov.nist.ngac.pm.core.common.prohibition.ProcessProhibition;
import gov.nist.ngac.pm.core.common.prohibition.Prohibition;
import gov.nist.ngac.pm.core.epp.EventContext;
import gov.nist.ngac.pm.core.pap.obligation.event.EventContextUser;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.graph.Association;
import gov.nist.ngac.pm.core.pap.obligation.Obligation;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes;
import gov.nist.ngac.pm.core.pap.operation.arg.type.ListType;
import gov.nist.ngac.pm.core.pap.operation.arg.type.MapType;
import gov.nist.ngac.pm.core.pap.operation.arg.type.Type;
import gov.nist.ngac.pm.core.pap.operation.param.FormalParameter;
import gov.nist.ngac.pm.core.pap.query.model.context.AnonymousTargetContext;
import gov.nist.ngac.pm.core.pap.query.model.context.AnonymousUserContext;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeTargetContext;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;
import gov.nist.ngac.pm.core.pap.query.model.context.TargetContext;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import gov.nist.ngac.pm.core.pap.query.model.explain.Explain;
import gov.nist.ngac.pm.core.pap.query.model.explain.ExplainAssociation;
import gov.nist.ngac.pm.core.pap.query.model.explain.ExplainNode;
import gov.nist.ngac.pm.core.pap.query.model.explain.Path;
import gov.nist.ngac.pm.core.pap.query.model.explain.PolicyClassExplain;
import gov.nist.ngac.pm.core.pap.query.model.subgraph.Subgraph;
import gov.nist.ngac.pm.core.pap.query.model.subgraph.SubgraphPrivileges;
import gov.nist.ngac.pm.proto.v1.model.NodeRef;
import gov.nist.ngac.pm.proto.v1.model.Value;
import gov.nist.ngac.pm.proto.v1.model.ValueList;
import gov.nist.ngac.pm.proto.v1.model.ValueMap;
import gov.nist.ngac.pm.proto.v1.pdp.query.ExplainResponse;
import gov.nist.ngac.pm.proto.v1.pdp.query.NodePrivileges;
import gov.nist.ngac.pm.proto.v1.pdp.query.Param;
import gov.nist.ngac.pm.proto.v1.pdp.query.ParamType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Converts gRPC wire proto messages into their corresponding core policy-machine model types.
 */
public class FromProtoUtil {

    /**
     * Converts a proto user context into its core {@link UserContext} variant; conjunctive user contexts
     * are rejected since they are no longer supported.
     *
     * @param pap unused
     * @param userCtxProto the proto user context to convert
     * @return the converted user context
     * @throws PMException never; declared for parity with other conversion methods
     */
    public static UserContext fromUserContextProto(PAP pap,
                                                   gov.nist.ngac.pm.proto.v1.pdp.query.UserContext userCtxProto) throws PMException {
        String process = userCtxProto.getProcess();

        return switch (userCtxProto.getUserCase()) {
            case ID -> NodeUserContext.of(userCtxProto.getId(), process);
            case NAME -> NodeUserContext.of(userCtxProto.getName(), process);
            case ATTRIBUTE_IDS -> AnonymousUserContext.ofIds(new HashSet<>(userCtxProto.getAttributeIds().getValuesList()), process);
            case ATTRIBUTE_NAMES -> AnonymousUserContext.ofNames(new HashSet<>(userCtxProto.getAttributeNames().getValuesList()), process);
            case CONJUNCTIVE -> throw new IllegalArgumentException("conjunctive user context is no longer supported");
            case USER_NOT_SET -> throw new IllegalArgumentException("user context not set");
        };
    }

    /**
     * Converts a proto target context into its core {@link TargetContext} variant.
     *
     * @param pap unused
     * @param targetCtxProto the proto target context to convert
     * @return the converted target context
     * @throws PMException never; declared for parity with other conversion methods
     */
    public static TargetContext fromTargetContextProto(PAP pap,
                                                       gov.nist.ngac.pm.proto.v1.pdp.query.TargetContext targetCtxProto) throws PMException {
        return switch (targetCtxProto.getTargetCase()) {
            case ID -> NodeTargetContext.of(targetCtxProto.getId());
            case NAME -> NodeTargetContext.of(targetCtxProto.getName());
            case ATTRIBUTE_IDS -> AnonymousTargetContext.ofIds(new HashSet<>(targetCtxProto.getAttributeIds().getValuesList()));
            case ATTRIBUTE_NAMES -> AnonymousTargetContext.ofNames(new HashSet<>(targetCtxProto.getAttributeNames().getValuesList()));
            case TARGET_NOT_SET -> throw new IllegalArgumentException("target context not set");
        };
    }

    /**
     * Resolves a proto {@link NodeRef} to a node id, querying the PAP by name if the ref carries a name
     * rather than an id.
     *
     * @param pap the PAP to query when the ref carries a name
     * @param nodeRef the node reference to resolve
     * @return the resolved node id
     * @throws PMException if the name lookup fails
     */
    public static long resolveNodeRefId(PAP pap, NodeRef nodeRef) throws PMException {
        return switch (nodeRef.getRefCase()) {
            case ID -> nodeRef.getId();
            case NAME -> pap.query().graph().getNodeByName(nodeRef.getName()).getId();
            case REF_NOT_SET -> throw new IllegalArgumentException("node reference not set");
        };
    }

    /**
     * Resolves each {@link NodeRef} in the list to a node id via {@link #resolveNodeRefId}.
     *
     * @param pap the PAP to query for any refs that carry a name
     * @param nodeRefs the node references to resolve
     * @return the resolved node ids, in the same order as the input list
     * @throws PMException if a name lookup fails
     */
    public static List<Long> resolveNodeRefIdList(PAP pap, List<NodeRef> nodeRefs) throws PMException {
        List<Long> nodeRefIds = new ArrayList<>();
        for (NodeRef nr : nodeRefs) {
            nodeRefIds.add(resolveNodeRefId(pap, nr));
        }

        return nodeRefIds;
    }

    /**
     * Converts a proto node into its core {@link Node} representation.
     *
     * @param protoNode the proto node to convert
     * @return the converted node
     */
    public static Node fromProtoNode(gov.nist.ngac.pm.proto.v1.model.Node protoNode) {
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

    /**
     * Flattens a list of proto node-privileges entries into a map keyed by node id.
     *
     * @param nodePrivilegesList the proto node-privileges entries to convert
     * @return a map from node id to the node's access rights
     */
    public static Map<Long, AccessRightSet> nodePrivilegesToIdMap(List<NodePrivileges> nodePrivilegesList) {
        Map<Long, AccessRightSet> result = new HashMap<>();
        for (NodePrivileges np : nodePrivilegesList) {
            result.put(np.getNode().getId(), new AccessRightSet(np.getArsetList()));
        }
        return result;
    }

    /**
     * Flattens a list of proto node-privileges entries into a map keyed by the full {@link Node}.
     *
     * @param nodePrivilegesList the proto node-privileges entries to convert
     * @return a map from node to the node's access rights
     */
    public static Map<Node, AccessRightSet> nodePrivilegesToNodeMap(List<NodePrivileges> nodePrivilegesList) {
        Map<Node, AccessRightSet> result = new HashMap<>();
        for (NodePrivileges np : nodePrivilegesList) {
            result.put(fromProtoNode(np.getNode()), new AccessRightSet(np.getArsetList()));
        }
        return result;
    }

    /**
     * Recursively converts a proto subgraph-privileges tree into its core {@link SubgraphPrivileges}
     * representation.
     *
     * @param proto the proto subgraph-privileges tree to convert
     * @return the converted subgraph-privileges tree
     */
    public static SubgraphPrivileges fromProtoSubgraphPrivileges(
            gov.nist.ngac.pm.proto.v1.pdp.query.SubgraphPrivileges proto) {
        Node node = fromProtoNode(proto.getNode());
        AccessRightSet privileges = new AccessRightSet(proto.getArsetList());
        List<SubgraphPrivileges> ascendants = proto.getAscendantsList().stream()
            .map(FromProtoUtil::fromProtoSubgraphPrivileges)
            .collect(Collectors.toList());
        return new SubgraphPrivileges(node, privileges, ascendants);
    }

    /**
     * Converts an explain query proto response into its core {@link Explain} representation.
     *
     * @param response the proto explain response to convert
     * @return the converted explain result
     */
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

    /**
     * Converts a proto policy-class explanation into its core {@link PolicyClassExplain} representation.
     *
     * @param proto the proto policy-class explanation to convert
     * @return the converted policy-class explanation
     */
    public static PolicyClassExplain fromProtoPolicyClassExplain(
            gov.nist.ngac.pm.proto.v1.pdp.query.PolicyClassExplain proto) {
        Node pc = fromProtoNode(proto.getPc());
        AccessRightSet arset = new AccessRightSet(proto.getArsetList());
        Collection<List<ExplainNode>> paths = proto.getPathsList().stream()
            .map(explainNodePath -> explainNodePath.getNodesList().stream()
                .map(FromProtoUtil::fromProtoExplainNode)
                .collect(Collectors.<ExplainNode>toList()))
            .collect(Collectors.toList());
        return new PolicyClassExplain(pc, arset, paths);
    }

    /**
     * Converts a proto explain node into its core {@link ExplainNode} representation.
     *
     * @param proto the proto explain node to convert
     * @return the converted explain node
     */
    public static ExplainNode fromProtoExplainNode(gov.nist.ngac.pm.proto.v1.pdp.query.ExplainNode proto) {
        Node node = fromProtoNode(proto.getNode());
        Collection<ExplainAssociation> associations = proto.getAssociationsList().stream()
            .map(FromProtoUtil::fromProtoExplainAssociation)
            .collect(Collectors.toList());
        return new ExplainNode(node, associations);
    }

    /**
     * Converts a proto explain association into its core {@link ExplainAssociation} representation.
     *
     * @param proto the proto explain association to convert
     * @return the converted explain association
     */
    public static ExplainAssociation fromProtoExplainAssociation(
            gov.nist.ngac.pm.proto.v1.pdp.query.ExplainAssociation proto) {
        Node ua = fromProtoNode(proto.getUa());
        AccessRightSet arset = new AccessRightSet(proto.getArsetList());
        Collection<Path> userPaths = proto.getUserPathsList().stream()
            .map(protoPath -> {
                Path path = new Path();
                for (gov.nist.ngac.pm.proto.v1.model.Node n : protoPath.getNodesList()) {
                    path.add(fromProtoNode(n));
                }
                return path;
            })
            .collect(Collectors.toList());
        return new ExplainAssociation(ua, arset, userPaths);
    }

    /**
     * Converts a proto prohibition into its core {@link Prohibition} representation, resolving to a
     * {@link ProcessProhibition} or {@link NodeProhibition} depending on whether a process is set.
     *
     * @param proto the proto prohibition to convert
     * @return the converted prohibition
     */
    public static Prohibition fromProtoProhibition(gov.nist.ngac.pm.proto.v1.model.Prohibition proto) {
        String name = proto.getName();
        AccessRightSet arset = new AccessRightSet(proto.getArsetList());
        Set<Long> inclusionSet = proto.getInclusionSetList().stream()
            .map(gov.nist.ngac.pm.proto.v1.model.Node::getId)
            .collect(Collectors.toSet());
        Set<Long> exclusionSet = proto.getExclusionSetList().stream()
            .map(gov.nist.ngac.pm.proto.v1.model.Node::getId)
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

    /**
     * Converts a proto association into its core {@link Association} representation.
     *
     * @param proto the proto association to convert
     * @return the converted association
     */
    public static Association fromAssociationProto(gov.nist.ngac.pm.proto.v1.model.Association proto) {
        long source = proto.getUa().getId();
        long target = proto.getTarget().getId();
        AccessRightSet arset = new AccessRightSet(proto.getArsetList());
        return new Association(source, target, arset);
    }

    /**
     * Recursively converts a proto subgraph tree into its core {@link Subgraph} representation.
     *
     * @param proto the proto subgraph tree to convert
     * @return the converted subgraph tree
     */
    public static Subgraph fromSubgraphProto(gov.nist.ngac.pm.proto.v1.pdp.query.Subgraph proto) {
        Node node = fromProtoNode(proto.getNode());
        List<Subgraph> subgraphs = proto.getSubgraphsList().stream()
            .map(FromProtoUtil::fromSubgraphProto)
            .collect(Collectors.toList());
        return new Subgraph(node, subgraphs);
    }

    /**
     * Converts a proto obligation into its core {@link Obligation} representation. Only the name and
     * author are populated; the proto's PML source field is not parsed back into rules here.
     *
     * @param proto the proto obligation to convert
     * @return the converted obligation, with rules left empty
     */
    public static Obligation fromObligationProto(gov.nist.ngac.pm.proto.v1.model.Obligation proto) {
        Obligation obligation = new Obligation();
        obligation.setName(proto.getName());
        if (proto.hasAuthor()) {
            obligation.setAuthor(NodeUserContext.of(proto.getAuthor().getId()));
        }
        return obligation;
    }

    /**
     * Converts a proto parameter type into its core {@link Type}, recursing into list and map element
     * types; an unset type case is treated as {@link BasicTypes#ANY_TYPE}.
     *
     * @param paramType the proto parameter type to convert
     * @return the converted type
     */
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

    /**
     * Converts a proto formal parameter into its core {@link FormalParameter}, mapping the node-id and
     * node-name parameter kinds to their underlying long/string types.
     *
     * @param param the proto formal parameter to convert
     * @return the converted formal parameter
     */
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

    /**
     * Converts each proto parameter in the list via {@link #fromProtoParam}.
     *
     * @param params the proto formal parameters to convert
     * @return the converted formal parameters, in the same order
     */
    public static List<FormalParameter<?>> fromProtoParams(List<Param> params) {
        List<FormalParameter<?>> formalParams = new ArrayList<>();
        for (Param param : params) {
            formalParams.add(fromProtoParam(param));
        }
        return formalParams;
    }

    /**
     * Converts a proto value map into a plain string-keyed map via {@link #fromValue}.
     *
     * @param valueMap the proto value map to convert
     * @return the converted map
     */
    public static Map<String, Object> fromValueMap(ValueMap valueMap) {
        return fromValueMap(valueMap.getValuesMap());
    }

    /**
     * Converts each proto value in the map via {@link #fromValue}.
     *
     * @param map the proto values to convert, keyed by string
     * @return the converted map
     */
    public static Map<String, Object> fromValueMap(Map<String, Value> map) {
        Map<String, Object> converted = new HashMap<>();
        for (Entry<String, Value> entry : map.entrySet()) {
            converted.put(entry.getKey(), fromValue(entry.getValue()));
        }
        return converted;
    }

    /**
     * Converts a proto value to its plain Java equivalent (long, String, boolean, List, or Map),
     * recursing into list and map values.
     *
     * @param value the proto value to convert
     * @return the converted value
     */
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
        for(Entry<String, Value> e : valueMap.getValuesMap().entrySet()) {
            result.put(e.getKey(), fromValue(e.getValue()));
        }

        return result;
    }





    /**
     * Converts a proto EPP event context into its core {@link EventContext} representation.
     *
     * @param proto the proto event context to convert
     * @return the converted event context
     */
    public static EventContext fromEventContextProto(gov.nist.ngac.pm.proto.v1.epp.EventContext proto) {
        String process = proto.getProcess();

        EventContextUser user = switch (proto.getUserCase()) {
            case USER_NAME -> new EventContextUser(proto.getUserName(), process);
            case USER_ATTRS -> new EventContextUser(proto.getUserAttrs().getValuesList(), process);
            case USER_NOT_SET -> throw new IllegalStateException("User not set");
        };

        Map<String, Object> args = fromValueMap(proto.getArgs());

        return new EventContext(user, proto.getOpName(), args);
    }
}
