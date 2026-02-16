package gov.nist.csd.pm.core.impl.grpc.pap;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.common.prohibition.NodeProhibition;
import gov.nist.csd.pm.core.common.prohibition.ProcessProhibition;
import gov.nist.csd.pm.core.epp.EventContextUser;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import gov.nist.csd.pm.proto.v1.epp.EventContext;
import gov.nist.csd.pm.proto.v1.model.NodeRef;
import gov.nist.csd.pm.proto.v1.model.NodeRefList;
import gov.nist.csd.pm.proto.v1.model.NodeType;
import gov.nist.csd.pm.proto.v1.model.Obligation;
import gov.nist.csd.pm.proto.v1.model.Prohibition;
import gov.nist.csd.pm.proto.v1.model.StringList;
import gov.nist.csd.pm.proto.v1.model.Value;
import gov.nist.csd.pm.proto.v1.model.ValueList;
import gov.nist.csd.pm.proto.v1.model.ValueMap;
import gov.nist.csd.pm.proto.v1.pdp.query.TargetContext;
import gov.nist.csd.pm.proto.v1.pdp.query.UserContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToProtoUtil {

    public static UserContext toUserContextProto(gov.nist.csd.pm.core.pap.query.model.context.UserContext userCtx) {
        UserContext.Builder builder = UserContext.newBuilder();
        if (userCtx.getProcess() == null) {
            builder.setProcess(userCtx.getProcess());
        }

        if (userCtx.isUserDefined()) {
            long userId = userCtx.getUser();

            builder.setUserNode(NodeRef.newBuilder().setId(userId).build());
        } else {
            Collection<Long> attributeIds = userCtx.getAttributeIds();

            builder.setUserAttributes(
                NodeRefList.newBuilder()
                    .addAllNodes(
                        attributeIds.stream().map(id -> NodeRef.newBuilder().setId(id).build()).toList()
                    )
                    .build()
            );
        }

        return builder.build();
    }

    public static TargetContext toTargetContextProto(gov.nist.csd.pm.core.pap.query.model.context.TargetContext targetCtx) {
        TargetContext.Builder builder = TargetContext.newBuilder();

        if (targetCtx.isNode()) {
            long targetId = targetCtx.getTargetId();

            builder.setTargetNode(NodeRef.newBuilder().setId(targetId).build());
        } else {
            Collection<Long> attributeIds = targetCtx.getAttributeIds();

            builder.setTargetAttributes(
                NodeRefList.newBuilder()
                    .addAllNodes(
                        attributeIds.stream().map(id -> NodeRef.newBuilder().setId(id).build()).toList()
                    )
                    .build()
            );
        }

        return builder.build();
    }

    public static NodeRef toNodeRefProto(long id) {
        return NodeRef.newBuilder()
            .setId(id)
            .build();
    }

    public static EventContext toEventContextProto(gov.nist.csd.pm.core.epp.EventContext eventContext) {
        gov.nist.csd.pm.proto.v1.epp.EventContext.Builder builder = gov.nist.csd.pm.proto.v1.epp.EventContext.newBuilder();

        // user
        EventContextUser user = eventContext.user();
        if (user.isUser()) {
            builder.setUserName(user.getName());
        } else {
            builder.setUserAttrs(StringList.newBuilder().addAllValues(user.getAttrs()).build());
        }

        // process
        builder.setProcess(eventContext.user().getProcess());

        // op
        builder.setOpName(eventContext.opName());

        // args
        builder.setArgs(toValueMapProto(eventContext.args()));

        return builder.build();
    }

    public static ValueMap toValueMapProto(Map<String, Object> objectMap) {
        Map<String, Value> converted = new HashMap<>();

        for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
            converted.put(entry.getKey(), toValueProto(entry.getValue()));
        }

        return ValueMap.newBuilder().putAllValues(converted).build();
    }

    public static Value toValueProto(Object o) {
        Value.Builder builder = Value.newBuilder();
        if (o instanceof Long l) {
            return builder.setInt64Value(l).build();
        } else if (o instanceof Boolean b) {
            return builder.setBoolValue(b).build();
        } else if (o instanceof List<?> list) {
            List<Value> values = new ArrayList<>();
            for (Object obj : list) {
                values.add(toValueProto(obj));
            }

            return builder.setListValue(ValueList.newBuilder().addAllValues(values)).build();
        } else if (o instanceof Map<?, ?> map) {
            Map<String, Value> values = new HashMap<>();
            for (var e : map.entrySet()) {
                Object key = e.getKey();
                if (!(key instanceof String)) {
                    // only supports string keys in maps
                    key = key.toString();
                }

                Object value = e.getValue();

                values.put((String) key, toValueProto(value));
            }

            return builder.setMapValue(ValueMap.newBuilder().putAllValues(values)).build();
        } else if (o instanceof String str) {
            return builder.setStringValue(str).build();
        }

        return Value.newBuilder().build();
    }

    public static Obligation toObligationProto(gov.nist.csd.pm.core.pap.obligation.Obligation obligation, PAP pap) throws
                                                                                                              PMException {
        gov.nist.csd.pm.proto.v1.model.Obligation.Builder builder = gov.nist.csd.pm.proto.v1.model.Obligation.newBuilder()
            .setName(obligation.getName())
            .setAuthor(toNodeProto(pap.query().graph().getNodeById(obligation.getAuthorId())))
            .setPml(obligation.toString());
        return builder.build();
    }

    public static gov.nist.csd.pm.proto.v1.model.Node toNodeProto(Node node) {
        ValueMap.Builder valueMap = ValueMap.newBuilder();

        for (var entry : node.getProperties().entrySet()) {
            valueMap.putValues(entry.getKey(), Value.newBuilder().setStringValue(entry.getValue()).build());
        }

        return gov.nist.csd.pm.proto.v1.model.Node.newBuilder()
            .setId(node.getId())
            .setName(node.getName())
            .setType(NodeType.valueOf(node.getType().name()))
            .putAllProperties(node.getProperties())
            .build();
    }

    public static Prohibition toProhibitionProto(gov.nist.csd.pm.core.common.prohibition.Prohibition prohibition, PolicyQuery query) throws PMException {
        List<gov.nist.csd.pm.proto.v1.model.Node> inclusionNodes = new ArrayList<>();
        for (long node : prohibition.getInclusionSet()) {
            inclusionNodes.add(toNodeProto(query.graph().getNodeById(node)));
        }

        List<gov.nist.csd.pm.proto.v1.model.Node> exclusionNodes = new ArrayList<>();
        for (long node : prohibition.getExclusionSet()) {
            exclusionNodes.add(toNodeProto(query.graph().getNodeById(node)));
        }

        gov.nist.csd.pm.proto.v1.model.Prohibition.Builder builder = gov.nist.csd.pm.proto.v1.model.Prohibition.newBuilder()
            .setName(prohibition.getName())
            .addAllArset(prohibition.getAccessRightSet())
            .addAllInclusionSet(inclusionNodes)
            .addAllInclusionSet(exclusionNodes)
            .setIsConjunctive(prohibition.isConjunctive());

        if (prohibition instanceof NodeProhibition nodeProhibition) {
            builder.setNode(toNodeProto(query.graph().getNodeById(nodeProhibition.getNodeId())));
        } else {
            builder.setProcess(((ProcessProhibition) prohibition).getProcess());
        }

        return builder.build();
    }
}
