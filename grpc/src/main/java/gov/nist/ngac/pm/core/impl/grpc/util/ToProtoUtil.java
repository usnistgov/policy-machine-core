package gov.nist.ngac.pm.core.impl.grpc.util;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.graph.node.Node;
import gov.nist.ngac.pm.core.common.prohibition.NodeProhibition;
import gov.nist.ngac.pm.core.common.prohibition.ProcessProhibition;
import gov.nist.ngac.pm.core.pap.obligation.event.EventContextUser;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.query.PolicyQuery;
import gov.nist.ngac.pm.core.pap.query.model.context.AnonymousTargetContext;
import gov.nist.ngac.pm.core.pap.query.model.context.AnonymousUserContext;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeTargetContext;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;
import gov.nist.ngac.pm.core.pap.query.model.explain.Explain;
import gov.nist.ngac.pm.core.pap.query.model.explain.ExplainAssociation;
import gov.nist.ngac.pm.core.pap.query.model.explain.ExplainNode;
import gov.nist.ngac.pm.core.pap.query.model.explain.Path;
import gov.nist.ngac.pm.core.pap.query.model.explain.PolicyClassExplain;
import gov.nist.ngac.pm.proto.v1.epp.EventContext;
import gov.nist.ngac.pm.proto.v1.model.NodeRef;
import gov.nist.ngac.pm.proto.v1.model.NodeType;
import gov.nist.ngac.pm.proto.v1.model.Obligation;
import gov.nist.ngac.pm.proto.v1.model.Prohibition;
import gov.nist.ngac.pm.proto.v1.model.StringList;
import gov.nist.ngac.pm.proto.v1.model.Value;
import gov.nist.ngac.pm.proto.v1.model.ValueList;
import gov.nist.ngac.pm.proto.v1.model.ValueMap;
import gov.nist.ngac.pm.proto.v1.pdp.query.TargetContext;
import gov.nist.ngac.pm.proto.v1.pdp.query.UserContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Converts core policy machine model types into their corresponding gRPC wire proto messages.
 */
public class ToProtoUtil {

    /**
     * Converts a core user context into its proto representation.
     *
     * @param userCtx the user context to convert
     * @return the converted proto user context
     */
    public static UserContext toUserContextProto(gov.nist.ngac.pm.core.pap.query.model.context.UserContext userCtx) {
        UserContext.Builder builder = UserContext.newBuilder();
        if (userCtx.getProcess() != null) {
            builder.setProcess(userCtx.getProcess());
        }

        switch (userCtx) {
            case NodeUserContext c -> {
                if (c.getName() != null) builder.setName(c.getName());
                else builder.setId(c.getId());
            }
            case AnonymousUserContext c -> {
                if (c.getAttributeNames() != null) {
                    builder.setAttributeNames(
                        gov.nist.ngac.pm.proto.v1.pdp.query.StringList.newBuilder()
                            .addAllValues(c.getAttributeNames()).build()
                    );
                } else {
                    builder.setAttributeIds(
                        gov.nist.ngac.pm.proto.v1.pdp.query.Int64List.newBuilder()
                            .addAllValues(c.getAttributeIds()).build()
                    );
                }
            }
            default -> throw new IllegalArgumentException("unsupported user context type: " + userCtx.getClass());
        }

        return builder.build();
    }

    /**
     * Converts a core target context into its proto representation.
     *
     * @param targetCtx the target context to convert
     * @return the converted proto target context
     */
    public static TargetContext toTargetContextProto(gov.nist.ngac.pm.core.pap.query.model.context.TargetContext targetCtx) {
        TargetContext.Builder builder = TargetContext.newBuilder();

        if (targetCtx instanceof NodeTargetContext ctx) {
            if (ctx.getName() != null) builder.setName(ctx.getName());
            else builder.setId(ctx.getId());
        } else if (targetCtx instanceof AnonymousTargetContext ctx) {
            if (ctx.getAttributeNames() != null) {
                builder.setAttributeNames(
                    gov.nist.ngac.pm.proto.v1.pdp.query.StringList.newBuilder()
                        .addAllValues(ctx.getAttributeNames()).build()
                );
            } else {
                builder.setAttributeIds(
                    gov.nist.ngac.pm.proto.v1.pdp.query.Int64List.newBuilder()
                        .addAllValues(ctx.getAttributeIds()).build()
                );
            }
        }

        return builder.build();
    }

    /**
     * Builds a proto node reference by id.
     *
     * @param id the node id
     * @return a node reference carrying the id
     */
    public static NodeRef toNodeRefProto(long id) {
        return NodeRef.newBuilder()
            .setId(id)
            .build();
    }

    /**
     * Builds a proto node reference by name.
     *
     * @param name the node name
     * @return a node reference carrying the name
     */
    public static NodeRef toNodeRefProto(String name) {
        return NodeRef.newBuilder()
            .setName(name)
            .build();
    }

    /**
     * Converts a core EPP event context into its proto representation.
     *
     * @param eventContext the event context to convert
     * @return the converted proto event context
     */
    public static EventContext toEventContextProto(gov.nist.ngac.pm.core.epp.EventContext eventContext) {
        gov.nist.ngac.pm.proto.v1.epp.EventContext.Builder builder = gov.nist.ngac.pm.proto.v1.epp.EventContext.newBuilder();

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

    /**
     * Converts a plain string-keyed map into a proto value map via {@link #toValueProto}.
     *
     * @param objectMap the map to convert
     * @return the converted proto value map
     */
    public static ValueMap toValueMapProto(Map<String, Object> objectMap) {
        Map<String, Value> converted = new HashMap<>();

        for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
            converted.put(entry.getKey(), toValueProto(entry.getValue()));
        }

        return ValueMap.newBuilder().putAllValues(converted).build();
    }

    /**
     * Same conversion as {@link #toValueMapProto}, but returns the raw entry map rather than a wrapped
     * {@link ValueMap} — used where the target proto field is a map rather than a message.
     *
     * @param objectMap the map to convert
     * @return the converted entries, keyed by string
     */
    public static Map<String, Value> toStringValueMapProto(Map<String, Object> objectMap) {
        Map<String, Value> converted = new HashMap<>();

        for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
            converted.put(entry.getKey(), toValueProto(entry.getValue()));
        }

        return converted;
    }

    /**
     * Converts a plain Java value into its proto {@link Value}, recursing into list and map elements.
     * Non-string map keys are converted to strings, and unrecognized types produce an empty value.
     *
     * @param o the value to convert
     * @return the converted proto value
     */
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

    /**
     * Converts a core obligation into its proto representation, resolving the author to a node id and
     * serializing the obligation to PML.
     *
     * @param obligation the obligation to convert
     * @param pap the PAP used to resolve the author's node id
     * @return the converted proto obligation
     * @throws PMException if the author's node id cannot be resolved
     */
    public static Obligation toObligationProto(gov.nist.ngac.pm.core.pap.obligation.Obligation obligation, PAP pap) throws
                                                                                                              PMException {
        gov.nist.ngac.pm.proto.v1.model.Obligation.Builder builder = gov.nist.ngac.pm.proto.v1.model.Obligation.newBuilder()
            .setName(obligation.getName())
            .setAuthor(toNodeProto(pap.query().graph().getNodeById(
                obligation.getAuthor().resolveNodeIds(pap.query().graph()).iterator().next()
            )))
            .setPml(obligation.toString());
        return builder.build();
    }

    /**
     * Converts a core node into its proto representation.
     *
     * @param node the node to convert
     * @return the converted proto node
     */
    public static gov.nist.ngac.pm.proto.v1.model.Node toNodeProto(Node node) {
        ValueMap.Builder valueMap = ValueMap.newBuilder();

        for (var entry : node.getProperties().entrySet()) {
            valueMap.putValues(entry.getKey(), Value.newBuilder().setStringValue(entry.getValue()).build());
        }

        return gov.nist.ngac.pm.proto.v1.model.Node.newBuilder()
            .setId(node.getId())
            .setName(node.getName())
            .setType(NodeType.valueOf(node.getType().name()))
            .putAllProperties(node.getProperties())
            .build();
    }

    /**
     * Converts a core prohibition into its proto representation.
     *
     * @param prohibition the prohibition to convert
     * @param query used to resolve the inclusion/exclusion set and subject node ids to full nodes
     * @return the converted proto prohibition
     * @throws PMException if a referenced node cannot be resolved
     */
    public static Prohibition toProhibitionProto(gov.nist.ngac.pm.core.common.prohibition.Prohibition prohibition, PolicyQuery query) throws PMException {
        List<gov.nist.ngac.pm.proto.v1.model.Node> inclusionNodes = new ArrayList<>();
        for (long node : prohibition.getInclusionSet()) {
            inclusionNodes.add(toNodeProto(query.graph().getNodeById(node)));
        }

        List<gov.nist.ngac.pm.proto.v1.model.Node> exclusionNodes = new ArrayList<>();
        for (long node : prohibition.getExclusionSet()) {
            exclusionNodes.add(toNodeProto(query.graph().getNodeById(node)));
        }

        gov.nist.ngac.pm.proto.v1.model.Prohibition.Builder builder = gov.nist.ngac.pm.proto.v1.model.Prohibition.newBuilder()
            .setName(prohibition.getName())
            .addAllArset(prohibition.getAccessRightSet())
            .addAllInclusionSet(inclusionNodes)
            .addAllExclusionSet(exclusionNodes)
            .setIsConjunctive(prohibition.isConjunctive());

        if (prohibition instanceof NodeProhibition nodeProhibition) {
            builder.setNode(toNodeProto(query.graph().getNodeById(nodeProhibition.getNodeId())));
        } else {
            builder.setProcess(((ProcessProhibition) prohibition).getProcess());
        }

        return builder.build();
    }

    /**
     * Converts a core {@link Explain} result into its proto response, or an empty response if the
     * explain argument is null.
     *
     * @param explain the explain result to convert, or null
     * @param query used to resolve prohibition subject/inclusion/exclusion node ids to full nodes
     * @return the converted proto explain response
     * @throws PMException if a referenced node cannot be resolved
     */
    public static gov.nist.ngac.pm.proto.v1.pdp.query.ExplainResponse buildExplainProto(Explain explain, PolicyQuery query) throws PMException {
        if (explain == null) {
            return gov.nist.ngac.pm.proto.v1.pdp.query.ExplainResponse.newBuilder().build();
        }

        AccessRightSet privileges = explain.getPrivileges();
        Collection<PolicyClassExplain> policyClasses = explain.getPolicyClasses();
        Collection<gov.nist.ngac.pm.core.common.prohibition.Prohibition> prohibitions = explain.getProhibitions();
        AccessRightSet deniedPrivileges = explain.getDeniedPrivileges();

        List<gov.nist.ngac.pm.proto.v1.pdp.query.PolicyClassExplain> policyClassProtos = new ArrayList<>();
        for (PolicyClassExplain pc : policyClasses) {
            Node pcNode = pc.pc();
            Collection<List<ExplainNode>> paths = pc.paths();
            List<gov.nist.ngac.pm.proto.v1.pdp.query.ExplainNodePath> pathProtos = new ArrayList<>();
            for (List<ExplainNode> path : paths) {
                List<gov.nist.ngac.pm.proto.v1.pdp.query.ExplainNode> explainNodeProtos = new ArrayList<>();
                for (ExplainNode explainNode : path) {
                    List<gov.nist.ngac.pm.proto.v1.pdp.query.ExplainAssociation> explainAssociationProtos = new ArrayList<>();
                    for (ExplainAssociation explainAssociation : explainNode.associations()) {
                        List<gov.nist.ngac.pm.proto.v1.pdp.query.Path> userPathProtos = new ArrayList<>();
                        for (Path userPath : explainAssociation.userPaths()) {
                            List<gov.nist.ngac.pm.proto.v1.model.Node> nodeProtos = new ArrayList<>();
                            for (Node node : userPath) {
                                nodeProtos.add(toNodeProto(node));
                            }

                            userPathProtos.add(gov.nist.ngac.pm.proto.v1.pdp.query.Path.newBuilder()
                                .addAllNodes(nodeProtos)
                                .build());
                        }

                        explainAssociationProtos.add(gov.nist.ngac.pm.proto.v1.pdp.query.ExplainAssociation.newBuilder()
                            .setUa(toNodeProto(explainAssociation.ua()))
                            .addAllArset(explainAssociation.arset())
                            .addAllUserPaths(userPathProtos)
                            .build());
                    }

                    explainNodeProtos.add(gov.nist.ngac.pm.proto.v1.pdp.query.ExplainNode.newBuilder()
                        .setNode(toNodeProto(explainNode.node()))
                        .addAllAssociations(explainAssociationProtos)
                        .build());
                }

                pathProtos.add(gov.nist.ngac.pm.proto.v1.pdp.query.ExplainNodePath.newBuilder()
                    .addAllNodes(explainNodeProtos)
                    .build());
            }

            policyClassProtos.add(gov.nist.ngac.pm.proto.v1.pdp.query.PolicyClassExplain.newBuilder()
                .setPc(toNodeProto(pcNode))
                .addAllArset(pc.arset())
                .addAllPaths(pathProtos)
                .build());
        }

        List<gov.nist.ngac.pm.proto.v1.model.Prohibition> prohibitionProtos = new ArrayList<>();
        for (gov.nist.ngac.pm.core.common.prohibition.Prohibition p : prohibitions) {
            prohibitionProtos.add(toProhibitionProto(p, query));
        }

        return gov.nist.ngac.pm.proto.v1.pdp.query.ExplainResponse.newBuilder()
            .addAllPrivileges(privileges)
            .addAllDeniedPrivileges(deniedPrivileges)
            .addAllPolicyClasses(policyClassProtos)
            .addAllProhibitions(prohibitionProtos)
            .build();
    }
}
