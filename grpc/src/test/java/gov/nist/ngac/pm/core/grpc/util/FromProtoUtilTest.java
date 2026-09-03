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

package gov.nist.ngac.pm.core.grpc.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.ngac.pm.core.common.graph.node.Node;
import gov.nist.ngac.pm.core.common.graph.node.NodeType;
import gov.nist.ngac.pm.core.common.prohibition.NodeProhibition;
import gov.nist.ngac.pm.core.common.prohibition.ProcessProhibition;
import gov.nist.ngac.pm.core.common.prohibition.Prohibition;
import gov.nist.ngac.pm.core.epp.EventContext;
import gov.nist.ngac.pm.core.impl.memory.pap.MemoryPAP;
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
import gov.nist.ngac.pm.core.pap.query.model.subgraph.Subgraph;
import gov.nist.ngac.pm.core.pap.query.model.subgraph.SubgraphPrivileges;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FromProtoUtilTest {

    private PAP pap;

    @BeforeEach
    void setUp() throws Exception {
        pap = new MemoryPAP();
        pap.modify().graph().createPolicyClass("pc1");
    }

    @Test
    void fromUserContextProto_id() throws Exception {
        var proto = gov.nist.ngac.pm.proto.v1.pdp.query.UserContext.newBuilder()
            .setId(5)
            .setProcess("proc")
            .build();

        UserContext ctx = FromProtoUtil.fromUserContextProto(pap, proto);

        assertEquals(NodeUserContext.of(5, "proc"), ctx);
    }

    @Test
    void fromUserContextProto_name() throws Exception {
        var proto = gov.nist.ngac.pm.proto.v1.pdp.query.UserContext.newBuilder()
            .setName("u1")
            .build();

        UserContext ctx = FromProtoUtil.fromUserContextProto(pap, proto);

        assertEquals(NodeUserContext.of("u1", ""), ctx);
    }

    @Test
    void fromUserContextProto_attributeIds() throws Exception {
        var proto = gov.nist.ngac.pm.proto.v1.pdp.query.UserContext.newBuilder()
            .setAttributeIds(gov.nist.ngac.pm.proto.v1.pdp.query.Int64List.newBuilder().addValues(1).addValues(2))
            .build();

        UserContext ctx = FromProtoUtil.fromUserContextProto(pap, proto);

        assertTrue(ctx instanceof AnonymousUserContext);
        assertEquals(Set.of(1L, 2L), ((AnonymousUserContext) ctx).getAttributeIds());
        assertEquals("", ctx.getProcess());
    }

    @Test
    void fromUserContextProto_attributeNames() throws Exception {
        var proto = gov.nist.ngac.pm.proto.v1.pdp.query.UserContext.newBuilder()
            .setAttributeNames(gov.nist.ngac.pm.proto.v1.pdp.query.StringList.newBuilder().addValues("ua1"))
            .build();

        UserContext ctx = FromProtoUtil.fromUserContextProto(pap, proto);

        assertTrue(ctx instanceof AnonymousUserContext);
        assertEquals(Set.of("ua1"), ((AnonymousUserContext) ctx).getAttributeNames());
    }

    @Test
    void fromUserContextProto_notSet_throws() {
        var proto = gov.nist.ngac.pm.proto.v1.pdp.query.UserContext.newBuilder().build();

        assertThrows(IllegalArgumentException.class, () -> FromProtoUtil.fromUserContextProto(pap, proto));
    }

    @Test
    void fromTargetContextProto_id() throws Exception {
        var proto = gov.nist.ngac.pm.proto.v1.pdp.query.TargetContext.newBuilder().setId(7).build();

        TargetContext ctx = FromProtoUtil.fromTargetContextProto(pap, proto);

        assertTrue(ctx instanceof NodeTargetContext);
        assertEquals(7, ((NodeTargetContext) ctx).getId());
    }

    @Test
    void fromTargetContextProto_name() throws Exception {
        var proto = gov.nist.ngac.pm.proto.v1.pdp.query.TargetContext.newBuilder().setName("o1").build();

        TargetContext ctx = FromProtoUtil.fromTargetContextProto(pap, proto);

        assertTrue(ctx instanceof NodeTargetContext);
        assertEquals("o1", ((NodeTargetContext) ctx).getName());
    }

    @Test
    void fromTargetContextProto_attributeIds() throws Exception {
        var proto = gov.nist.ngac.pm.proto.v1.pdp.query.TargetContext.newBuilder()
            .setAttributeIds(gov.nist.ngac.pm.proto.v1.pdp.query.Int64List.newBuilder().addValues(3))
            .build();

        TargetContext ctx = FromProtoUtil.fromTargetContextProto(pap, proto);

        assertTrue(ctx instanceof AnonymousTargetContext);
        assertEquals(Set.of(3L), ((AnonymousTargetContext) ctx).getAttributeIds());
    }

    @Test
    void fromTargetContextProto_attributeNames() throws Exception {
        var proto = gov.nist.ngac.pm.proto.v1.pdp.query.TargetContext.newBuilder()
            .setAttributeNames(gov.nist.ngac.pm.proto.v1.pdp.query.StringList.newBuilder().addValues("oa1"))
            .build();

        TargetContext ctx = FromProtoUtil.fromTargetContextProto(pap, proto);

        assertTrue(ctx instanceof AnonymousTargetContext);
        assertEquals(Set.of("oa1"), ((AnonymousTargetContext) ctx).getAttributeNames());
    }

    @Test
    void fromTargetContextProto_notSet_throws() {
        var proto = gov.nist.ngac.pm.proto.v1.pdp.query.TargetContext.newBuilder().build();

        assertThrows(IllegalArgumentException.class, () -> FromProtoUtil.fromTargetContextProto(pap, proto));
    }

    @Test
    void resolveNodeRefId_byId() throws Exception {
        var ref = gov.nist.ngac.pm.proto.v1.model.NodeRef.newBuilder().setId(42).build();

        assertEquals(42, FromProtoUtil.resolveNodeRefId(pap, ref));
    }

    @Test
    void resolveNodeRefId_byName() throws Exception {
        long id = pap.modify().graph().createUserAttribute("ua1", List.of(pap.query().graph().getNodeByName("pc1").getId()));
        var ref = gov.nist.ngac.pm.proto.v1.model.NodeRef.newBuilder().setName("ua1").build();

        assertEquals(id, FromProtoUtil.resolveNodeRefId(pap, ref));
    }

    @Test
    void resolveNodeRefId_notSet_throws() {
        var ref = gov.nist.ngac.pm.proto.v1.model.NodeRef.newBuilder().build();

        assertThrows(IllegalArgumentException.class, () -> FromProtoUtil.resolveNodeRefId(pap, ref));
    }

    @Test
    void resolveNodeRefIdList() throws Exception {
        var refs = List.of(
            gov.nist.ngac.pm.proto.v1.model.NodeRef.newBuilder().setId(1).build(),
            gov.nist.ngac.pm.proto.v1.model.NodeRef.newBuilder().setId(2).build()
        );

        assertEquals(List.of(1L, 2L), FromProtoUtil.resolveNodeRefIdList(pap, refs));
    }

    @Test
    void fromProtoNode() {
        var proto = gov.nist.ngac.pm.proto.v1.model.Node.newBuilder()
            .setId(1)
            .setName("n1")
            .setType(gov.nist.ngac.pm.proto.v1.model.NodeType.OA)
            .putProperties("k", "v")
            .build();

        Node node = FromProtoUtil.fromProtoNode(proto);

        assertEquals(new Node(1, "n1", NodeType.OA, Map.of("k", "v")), node);
    }

    @Test
    void nodePrivilegesToIdMap() {
        var protoNode = gov.nist.ngac.pm.proto.v1.model.Node.newBuilder().setId(1).setName("n1")
            .setType(gov.nist.ngac.pm.proto.v1.model.NodeType.U).build();
        var np = gov.nist.ngac.pm.proto.v1.pdp.query.NodePrivileges.newBuilder()
            .setNode(protoNode)
            .addArset("read")
            .build();

        Map<Long, AccessRightSet> result = FromProtoUtil.nodePrivilegesToIdMap(List.of(np));

        assertEquals(new AccessRightSet("read"), result.get(1L));
    }

    @Test
    void nodePrivilegesToNodeMap() {
        var protoNode = gov.nist.ngac.pm.proto.v1.model.Node.newBuilder().setId(1).setName("n1")
            .setType(gov.nist.ngac.pm.proto.v1.model.NodeType.U).build();
        var np = gov.nist.ngac.pm.proto.v1.pdp.query.NodePrivileges.newBuilder()
            .setNode(protoNode)
            .addArset("read")
            .build();

        Map<Node, AccessRightSet> result = FromProtoUtil.nodePrivilegesToNodeMap(List.of(np));

        assertEquals(new AccessRightSet("read"), result.get(FromProtoUtil.fromProtoNode(protoNode)));
    }

    @Test
    void fromProtoSubgraphPrivileges_recursive() {
        var childProtoNode = gov.nist.ngac.pm.proto.v1.model.Node.newBuilder().setId(2).setName("child")
            .setType(gov.nist.ngac.pm.proto.v1.model.NodeType.UA).build();
        var childProto = gov.nist.ngac.pm.proto.v1.pdp.query.SubgraphPrivileges.newBuilder()
            .setNode(childProtoNode)
            .addArset("read")
            .build();

        var rootProtoNode = gov.nist.ngac.pm.proto.v1.model.Node.newBuilder().setId(1).setName("root")
            .setType(gov.nist.ngac.pm.proto.v1.model.NodeType.UA).build();
        var rootProto = gov.nist.ngac.pm.proto.v1.pdp.query.SubgraphPrivileges.newBuilder()
            .setNode(rootProtoNode)
            .addArset("write")
            .addAscendants(childProto)
            .build();

        SubgraphPrivileges result = FromProtoUtil.fromProtoSubgraphPrivileges(rootProto);

        SubgraphPrivileges expected = new SubgraphPrivileges(
            FromProtoUtil.fromProtoNode(rootProtoNode),
            new AccessRightSet("write"),
            List.of(new SubgraphPrivileges(FromProtoUtil.fromProtoNode(childProtoNode), new AccessRightSet("read"), List.of()))
        );

        assertEquals(expected, result);
    }

    @Test
    void fromProtoProhibition_node() {
        var protoNode = gov.nist.ngac.pm.proto.v1.model.Node.newBuilder().setId(1).setName("n1")
            .setType(gov.nist.ngac.pm.proto.v1.model.NodeType.OA).build();
        var proto = gov.nist.ngac.pm.proto.v1.model.Prohibition.newBuilder()
            .setName("p1")
            .setNode(protoNode)
            .addArset("read")
            .setIsConjunctive(true)
            .build();

        Prohibition prohibition = FromProtoUtil.fromProtoProhibition(proto);

        assertTrue(prohibition instanceof NodeProhibition);
        NodeProhibition np = (NodeProhibition) prohibition;
        assertEquals("p1", np.getName());
        assertEquals(1, np.getNodeId());
        assertTrue(np.isConjunctive());
        assertEquals(new AccessRightSet("read"), np.getAccessRightSet());
    }

    @Test
    void fromProtoProhibition_process() {
        var protoNode = gov.nist.ngac.pm.proto.v1.model.Node.newBuilder().setId(3).setName("n1")
            .setType(gov.nist.ngac.pm.proto.v1.model.NodeType.OA).build();
        var proto = gov.nist.ngac.pm.proto.v1.model.Prohibition.newBuilder()
            .setName("p1")
            .setNode(protoNode)
            .setProcess("proc1")
            .addArset("write")
            .build();

        Prohibition prohibition = FromProtoUtil.fromProtoProhibition(proto);

        assertTrue(prohibition instanceof ProcessProhibition);
        ProcessProhibition pp = (ProcessProhibition) prohibition;
        assertEquals(3, pp.getUserId());
        assertEquals("proc1", pp.getProcess());
        assertFalse(pp.isConjunctive());
    }

    @Test
    void fromAssociationProto() {
        var uaNode = gov.nist.ngac.pm.proto.v1.model.Node.newBuilder().setId(1).setName("ua1")
            .setType(gov.nist.ngac.pm.proto.v1.model.NodeType.UA).build();
        var targetNode = gov.nist.ngac.pm.proto.v1.model.Node.newBuilder().setId(2).setName("oa1")
            .setType(gov.nist.ngac.pm.proto.v1.model.NodeType.OA).build();
        var proto = gov.nist.ngac.pm.proto.v1.model.Association.newBuilder()
            .setUa(uaNode)
            .setTarget(targetNode)
            .addArset("read")
            .build();

        Association association = FromProtoUtil.fromAssociationProto(proto);

        assertEquals(new Association(1, 2, new AccessRightSet("read")), association);
    }

    @Test
    void fromSubgraphProto_recursive() {
        var childProtoNode = gov.nist.ngac.pm.proto.v1.model.Node.newBuilder().setId(2).setName("child")
            .setType(gov.nist.ngac.pm.proto.v1.model.NodeType.OA).build();
        var childProto = gov.nist.ngac.pm.proto.v1.pdp.query.Subgraph.newBuilder().setNode(childProtoNode).build();

        var rootProtoNode = gov.nist.ngac.pm.proto.v1.model.Node.newBuilder().setId(1).setName("root")
            .setType(gov.nist.ngac.pm.proto.v1.model.NodeType.OA).build();
        var rootProto = gov.nist.ngac.pm.proto.v1.pdp.query.Subgraph.newBuilder()
            .setNode(rootProtoNode)
            .addSubgraphs(childProto)
            .build();

        Subgraph result = FromProtoUtil.fromSubgraphProto(rootProto);

        Subgraph expected = new Subgraph(
            FromProtoUtil.fromProtoNode(rootProtoNode),
            List.of(new Subgraph(FromProtoUtil.fromProtoNode(childProtoNode), List.of()))
        );

        assertEquals(expected, result);
    }

    @Test
    void fromObligationProto_withAuthor() {
        var authorNode = gov.nist.ngac.pm.proto.v1.model.Node.newBuilder().setId(9).setName("author")
            .setType(gov.nist.ngac.pm.proto.v1.model.NodeType.U).build();
        var proto = gov.nist.ngac.pm.proto.v1.model.Obligation.newBuilder()
            .setName("obl1")
            .setAuthor(authorNode)
            .build();

        Obligation obligation = FromProtoUtil.fromObligationProto(proto);

        assertEquals("obl1", obligation.getName());
        assertEquals(NodeUserContext.of(9), obligation.getAuthor());
    }

    @Test
    void fromObligationProto_withoutAuthor() {
        var proto = gov.nist.ngac.pm.proto.v1.model.Obligation.newBuilder().setName("obl1").build();

        Obligation obligation = FromProtoUtil.fromObligationProto(proto);

        assertEquals("obl1", obligation.getName());
    }

    @Test
    void fromProtoParamType_basic() {
        assertSame(BasicTypes.STRING_TYPE, FromProtoUtil.fromProtoParamType(
            gov.nist.ngac.pm.proto.v1.pdp.query.ParamType.newBuilder()
                .setStringType(gov.nist.ngac.pm.proto.v1.pdp.query.StringType.newBuilder()).build()));
        assertSame(BasicTypes.LONG_TYPE, FromProtoUtil.fromProtoParamType(
            gov.nist.ngac.pm.proto.v1.pdp.query.ParamType.newBuilder()
                .setLongType(gov.nist.ngac.pm.proto.v1.pdp.query.LongType.newBuilder()).build()));
        assertSame(BasicTypes.BOOLEAN_TYPE, FromProtoUtil.fromProtoParamType(
            gov.nist.ngac.pm.proto.v1.pdp.query.ParamType.newBuilder()
                .setBooleanType(gov.nist.ngac.pm.proto.v1.pdp.query.BooleanType.newBuilder()).build()));
        assertSame(BasicTypes.ANY_TYPE, FromProtoUtil.fromProtoParamType(
            gov.nist.ngac.pm.proto.v1.pdp.query.ParamType.newBuilder()
                .setAnyType(gov.nist.ngac.pm.proto.v1.pdp.query.AnyType.newBuilder()).build()));
        assertSame(BasicTypes.ANY_TYPE, FromProtoUtil.fromProtoParamType(
            gov.nist.ngac.pm.proto.v1.pdp.query.ParamType.newBuilder().build()));
    }

    @Test
    void fromProtoParamType_nestedListAndMap() {
        var elementType = gov.nist.ngac.pm.proto.v1.pdp.query.ParamType.newBuilder()
            .setStringType(gov.nist.ngac.pm.proto.v1.pdp.query.StringType.newBuilder()).build();
        var listProto = gov.nist.ngac.pm.proto.v1.pdp.query.ParamType.newBuilder()
            .setListType(gov.nist.ngac.pm.proto.v1.pdp.query.ListType.newBuilder().setElementType(elementType))
            .build();

        Type<?> listType = FromProtoUtil.fromProtoParamType(listProto);

        assertEquals(ListType.of(BasicTypes.STRING_TYPE), listType);

        var mapProto = gov.nist.ngac.pm.proto.v1.pdp.query.ParamType.newBuilder()
            .setMapType(gov.nist.ngac.pm.proto.v1.pdp.query.MapType.newBuilder()
                .setKeyType(elementType)
                .setValueType(elementType))
            .build();

        Type<?> mapType = FromProtoUtil.fromProtoParamType(mapProto);

        assertEquals(MapType.of(BasicTypes.STRING_TYPE, BasicTypes.STRING_TYPE), mapType);
    }

    @Test
    void fromProtoParam_allKinds() {
        var stringType = gov.nist.ngac.pm.proto.v1.pdp.query.ParamType.newBuilder()
            .setStringType(gov.nist.ngac.pm.proto.v1.pdp.query.StringType.newBuilder()).build();

        FormalParameter<?> formal = FromProtoUtil.fromProtoParam(
            gov.nist.ngac.pm.proto.v1.pdp.query.Param.newBuilder().setName("p1")
                .setFormalParam(gov.nist.ngac.pm.proto.v1.pdp.query.FormalParam.newBuilder().setType(stringType))
                .build());
        assertEquals("p1", formal.getName());
        assertSame(BasicTypes.STRING_TYPE, formal.getType());

        FormalParameter<?> nodeId = FromProtoUtil.fromProtoParam(
            gov.nist.ngac.pm.proto.v1.pdp.query.Param.newBuilder().setName("p2")
                .setNodeIdFormalParam(gov.nist.ngac.pm.proto.v1.pdp.query.NodeIdFormalParam.newBuilder())
                .build());
        assertSame(BasicTypes.LONG_TYPE, nodeId.getType());

        FormalParameter<?> nodeIdList = FromProtoUtil.fromProtoParam(
            gov.nist.ngac.pm.proto.v1.pdp.query.Param.newBuilder().setName("p3")
                .setNodeIdListFormalParam(gov.nist.ngac.pm.proto.v1.pdp.query.NodeIdListFormalParam.newBuilder())
                .build());
        assertEquals(ListType.of(BasicTypes.LONG_TYPE), nodeIdList.getType());

        FormalParameter<?> nodeName = FromProtoUtil.fromProtoParam(
            gov.nist.ngac.pm.proto.v1.pdp.query.Param.newBuilder().setName("p4")
                .setNodeNameFormalParam(gov.nist.ngac.pm.proto.v1.pdp.query.NodeNameFormalParam.newBuilder())
                .build());
        assertSame(BasicTypes.STRING_TYPE, nodeName.getType());

        FormalParameter<?> nodeNameList = FromProtoUtil.fromProtoParam(
            gov.nist.ngac.pm.proto.v1.pdp.query.Param.newBuilder().setName("p5")
                .setNodeNameListFormalParam(gov.nist.ngac.pm.proto.v1.pdp.query.NodeNameListFormalParam.newBuilder())
                .build());
        assertEquals(ListType.of(BasicTypes.STRING_TYPE), nodeNameList.getType());

        FormalParameter<?> unset = FromProtoUtil.fromProtoParam(
            gov.nist.ngac.pm.proto.v1.pdp.query.Param.newBuilder().setName("p6").build());
        assertSame(BasicTypes.ANY_TYPE, unset.getType());
    }

    @Test
    void fromProtoParams() {
        var params = List.of(
            gov.nist.ngac.pm.proto.v1.pdp.query.Param.newBuilder().setName("p1")
                .setNodeIdFormalParam(gov.nist.ngac.pm.proto.v1.pdp.query.NodeIdFormalParam.newBuilder())
                .build()
        );

        List<FormalParameter<?>> result = FromProtoUtil.fromProtoParams(params);

        assertEquals(1, result.size());
        assertEquals("p1", result.get(0).getName());
    }

    @Test
    void fromValue_scalarsListAndMap() {
        assertEquals(1L, FromProtoUtil.fromValue(
            gov.nist.ngac.pm.proto.v1.model.Value.newBuilder().setInt64Value(1).build()));
        assertEquals("s", FromProtoUtil.fromValue(
            gov.nist.ngac.pm.proto.v1.model.Value.newBuilder().setStringValue("s").build()));
        assertEquals(true, FromProtoUtil.fromValue(
            gov.nist.ngac.pm.proto.v1.model.Value.newBuilder().setBoolValue(true).build()));

        var listValue = gov.nist.ngac.pm.proto.v1.model.Value.newBuilder()
            .setListValue(gov.nist.ngac.pm.proto.v1.model.ValueList.newBuilder()
                .addValues(gov.nist.ngac.pm.proto.v1.model.Value.newBuilder().setInt64Value(2)))
            .build();
        assertEquals(List.of(2L), FromProtoUtil.fromValue(listValue));

        var mapValue = gov.nist.ngac.pm.proto.v1.model.Value.newBuilder()
            .setMapValue(gov.nist.ngac.pm.proto.v1.model.ValueMap.newBuilder()
                .putValues("k", gov.nist.ngac.pm.proto.v1.model.Value.newBuilder().setStringValue("v").build()))
            .build();
        assertEquals(Map.of("k", "v"), FromProtoUtil.fromValue(mapValue));
    }

    @Test
    void fromValue_notSet_throws() {
        var value = gov.nist.ngac.pm.proto.v1.model.Value.newBuilder().build();

        assertThrows(IllegalArgumentException.class, () -> FromProtoUtil.fromValue(value));
    }

    @Test
    void fromValueMap() {
        var proto = gov.nist.ngac.pm.proto.v1.model.ValueMap.newBuilder()
            .putValues("k", gov.nist.ngac.pm.proto.v1.model.Value.newBuilder().setInt64Value(3).build())
            .build();

        assertEquals(Map.of("k", 3L), FromProtoUtil.fromValueMap(proto));
    }

    @Test
    void fromEventContextProto_userName() {
        var proto = gov.nist.ngac.pm.proto.v1.epp.EventContext.newBuilder()
            .setUserName("u1")
            .setProcess("proc")
            .setOpName("op1")
            .setArgs(gov.nist.ngac.pm.proto.v1.model.ValueMap.newBuilder()
                .putValues("a", gov.nist.ngac.pm.proto.v1.model.Value.newBuilder().setInt64Value(1).build()))
            .build();

        EventContext ctx = FromProtoUtil.fromEventContextProto(proto);

        assertTrue(ctx.user().isUser());
        assertEquals("u1", ctx.user().getName());
        assertEquals("proc", ctx.user().getProcess());
        assertEquals("op1", ctx.opName());
        assertEquals(Map.of("a", 1L), ctx.args());
    }

    @Test
    void fromEventContextProto_userAttrs() {
        var proto = gov.nist.ngac.pm.proto.v1.epp.EventContext.newBuilder()
            .setUserAttrs(gov.nist.ngac.pm.proto.v1.model.StringList.newBuilder().addValues("ua1"))
            .setOpName("op1")
            .build();

        EventContext ctx = FromProtoUtil.fromEventContextProto(proto);

        assertFalse(ctx.user().isUser());
        assertEquals(List.of("ua1"), ctx.user().getAttrs());
    }

    @Test
    void fromEventContextProto_userNotSet_throws() {
        var proto = gov.nist.ngac.pm.proto.v1.epp.EventContext.newBuilder().setOpName("op1").build();

        assertThrows(IllegalStateException.class, () -> FromProtoUtil.fromEventContextProto(proto));
    }
}
