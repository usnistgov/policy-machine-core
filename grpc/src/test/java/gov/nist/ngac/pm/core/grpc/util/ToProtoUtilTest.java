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
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.ngac.pm.core.common.graph.node.Node;
import gov.nist.ngac.pm.core.common.graph.node.NodeType;
import gov.nist.ngac.pm.core.common.prohibition.NodeProhibition;
import gov.nist.ngac.pm.core.common.prohibition.ProcessProhibition;
import gov.nist.ngac.pm.core.epp.EventContext;
import gov.nist.ngac.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.obligation.Obligation;
import gov.nist.ngac.pm.core.pap.obligation.event.EventContextUser;
import gov.nist.ngac.pm.core.pap.obligation.event.EventPattern;
import gov.nist.ngac.pm.core.pap.obligation.event.operation.MatchesOperationPattern;
import gov.nist.ngac.pm.core.pap.obligation.event.subject.SubjectPattern;
import gov.nist.ngac.pm.core.pap.obligation.response.ObligationResponse;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.query.model.context.AnonymousTargetContext;
import gov.nist.ngac.pm.core.pap.query.model.context.AnonymousUserContext;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeTargetContext;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;
import gov.nist.ngac.pm.core.pap.query.model.explain.Explain;
import gov.nist.ngac.pm.core.pap.query.model.explain.ExplainAssociation;
import gov.nist.ngac.pm.core.pap.query.model.explain.ExplainNode;
import gov.nist.ngac.pm.core.pap.query.model.explain.Path;
import gov.nist.ngac.pm.core.pap.query.model.explain.PolicyClassExplain;
import gov.nist.ngac.pm.proto.v1.model.NodeRef;
import gov.nist.ngac.pm.proto.v1.model.Value;
import gov.nist.ngac.pm.proto.v1.model.ValueMap;
import gov.nist.ngac.pm.proto.v1.pdp.query.TargetContext;
import gov.nist.ngac.pm.proto.v1.pdp.query.UserContext;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ToProtoUtilTest {

    private PAP pap;
    private long pc1;
    private long ua1;
    private long oa1;

    @BeforeEach
    void setUp() throws Exception {
        pap = new MemoryPAP();
        pc1 = pap.modify().graph().createPolicyClass("pc1");
        ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
        oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
    }

    @Test
    void toUserContextProto_nodeById() {
        UserContext proto = ToProtoUtil.toUserContextProto(NodeUserContext.of(5, "proc"));

        assertEquals(5, proto.getId());
        assertEquals("proc", proto.getProcess());
    }

    @Test
    void toUserContextProto_nodeByName() {
        UserContext proto = ToProtoUtil.toUserContextProto(NodeUserContext.of("u1"));

        assertEquals("u1", proto.getName());
    }

    @Test
    void toUserContextProto_anonymousNames() {
        UserContext proto = ToProtoUtil.toUserContextProto(AnonymousUserContext.ofNames(Set.of("ua1")));

        assertEquals(List.of("ua1"), proto.getAttributeNames().getValuesList());
    }

    @Test
    void toUserContextProto_anonymousIds() {
        UserContext proto = ToProtoUtil.toUserContextProto(AnonymousUserContext.ofIds(Set.of(1L, 2L)));

        assertEquals(Set.of(1L, 2L), Set.copyOf(proto.getAttributeIds().getValuesList()));
    }

    @Test
    void toTargetContextProto_nodeById() {
        TargetContext proto = ToProtoUtil.toTargetContextProto(NodeTargetContext.of(7));

        assertEquals(7, proto.getId());
    }

    @Test
    void toTargetContextProto_nodeByName() {
        TargetContext proto = ToProtoUtil.toTargetContextProto(NodeTargetContext.of("o1"));

        assertEquals("o1", proto.getName());
    }

    @Test
    void toTargetContextProto_anonymousNames() {
        TargetContext proto = ToProtoUtil.toTargetContextProto(AnonymousTargetContext.ofNames(Set.of("oa1")));

        assertEquals(List.of("oa1"), proto.getAttributeNames().getValuesList());
    }

    @Test
    void toTargetContextProto_anonymousIds() {
        TargetContext proto = ToProtoUtil.toTargetContextProto(AnonymousTargetContext.ofIds(Set.of(3L)));

        assertEquals(Set.of(3L), Set.copyOf(proto.getAttributeIds().getValuesList()));
    }

    @Test
    void toNodeRefProto_id() {
        NodeRef ref = ToProtoUtil.toNodeRefProto(9L);

        assertEquals(9, ref.getId());
    }

    @Test
    void toNodeRefProto_name() {
        NodeRef ref = ToProtoUtil.toNodeRefProto("n1");

        assertEquals("n1", ref.getName());
    }

    @Test
    void toNodeProto() {
        Node node = new Node(1, "n1", NodeType.OA, Map.of("k", "v"));

        gov.nist.ngac.pm.proto.v1.model.Node proto = ToProtoUtil.toNodeProto(node);

        assertEquals(1, proto.getId());
        assertEquals("n1", proto.getName());
        assertEquals(gov.nist.ngac.pm.proto.v1.model.NodeType.OA, proto.getType());
        assertEquals(Map.of("k", "v"), proto.getPropertiesMap());
    }

    @Test
    void toValueProto_scalars() {
        assertEquals(1L, ToProtoUtil.toValueProto(1L).getInt64Value());
        assertEquals("s", ToProtoUtil.toValueProto("s").getStringValue());
        assertTrue(ToProtoUtil.toValueProto(true).getBoolValue());
    }

    @Test
    void toValueProto_list() {
        Value value = ToProtoUtil.toValueProto(List.of(1L, 2L));

        assertEquals(2, value.getListValue().getValuesCount());
        assertEquals(1L, value.getListValue().getValues(0).getInt64Value());
    }

    @Test
    void toValueProto_map() {
        Value value = ToProtoUtil.toValueProto(Map.of("k", "v"));

        assertEquals("v", value.getMapValue().getValuesMap().get("k").getStringValue());
    }

    @Test
    void toValueProto_mapNonStringKey() {
        Value value = ToProtoUtil.toValueProto(Map.of(1L, "v"));

        assertEquals("v", value.getMapValue().getValuesMap().get("1").getStringValue());
    }

    @Test
    void toValueProto_unknownType() {
        Value value = ToProtoUtil.toValueProto(new Object());

        assertEquals(Value.DataCase.DATA_NOT_SET, value.getDataCase());
    }

    @Test
    void toValueMapProto() {
        ValueMap proto = ToProtoUtil.toValueMapProto(Map.of("k", 3L));

        assertEquals(3L, proto.getValuesMap().get("k").getInt64Value());
    }

    @Test
    void toStringValueMapProto() {
        Map<String, Value> proto = ToProtoUtil.toStringValueMapProto(Map.of("k", "v"));

        assertEquals("v", proto.get("k").getStringValue());
    }

    @Test
    void toEventContextProto_userName() {
        EventContext eventContext = new EventContext(
            new EventContextUser("u1", "proc"), "op1", Map.of("a", 1L));

        gov.nist.ngac.pm.proto.v1.epp.EventContext proto = ToProtoUtil.toEventContextProto(eventContext);

        assertEquals("u1", proto.getUserName());
        assertEquals("proc", proto.getProcess());
        assertEquals("op1", proto.getOpName());
        assertEquals(1L, proto.getArgs().getValuesMap().get("a").getInt64Value());
    }

    @Test
    void toEventContextProto_userAttrs() {
        EventContext eventContext = new EventContext(
            new EventContextUser(List.of("ua1"), ""), "op1", Map.of());

        gov.nist.ngac.pm.proto.v1.epp.EventContext proto = ToProtoUtil.toEventContextProto(eventContext);

        assertEquals(List.of("ua1"), proto.getUserAttrs().getValuesList());
    }

    @Test
    void toProhibitionProto_node() throws Exception {
        NodeProhibition prohibition = new NodeProhibition(
            "p1", oa1, new AccessRightSet("read"), Set.of(ua1), Set.of(pc1), true);

        gov.nist.ngac.pm.proto.v1.model.Prohibition proto =
            ToProtoUtil.toProhibitionProto(prohibition, pap.query());

        assertEquals("p1", proto.getName());
        assertEquals(oa1, proto.getNode().getId());
        assertTrue(proto.getIsConjunctive());
        assertEquals(List.of("read"), proto.getArsetList());
        assertEquals(1, proto.getInclusionSetCount());
        assertEquals(ua1, proto.getInclusionSet(0).getId());
        assertEquals(pc1, proto.getExclusionSet(0).getId());
        assertFalse(proto.hasProcess());
    }

    @Test
    void toProhibitionProto_process() throws Exception {
        ProcessProhibition prohibition = new ProcessProhibition(
            "p1", ua1, "proc1", new AccessRightSet("write"), Set.of(), Set.of(), false);

        gov.nist.ngac.pm.proto.v1.model.Prohibition proto =
            ToProtoUtil.toProhibitionProto(prohibition, pap.query());

        assertEquals("proc1", proto.getProcess());
        assertFalse(proto.getIsConjunctive());
    }

    @Test
    void toObligationProto() throws Exception {
        long u1 = pap.modify().graph().createUser("u1", List.of(ua1));
        Obligation obligation = new Obligation(
            NodeUserContext.of(u1),
            "obl1",
            new EventPattern(new SubjectPattern(), new MatchesOperationPattern("")),
            new ObligationResponse("evtCtx", List.of()));

        gov.nist.ngac.pm.proto.v1.model.Obligation proto =
            ToProtoUtil.toObligationProto(obligation, pap);

        assertEquals("obl1", proto.getName());
        assertEquals(u1, proto.getAuthor().getId());
    }

    @Test
    void buildExplainProto_null() throws Exception {
        gov.nist.ngac.pm.proto.v1.pdp.query.ExplainResponse proto =
            ToProtoUtil.buildExplainProto(null, pap.query());

        assertEquals(0, proto.getPrivilegesCount());
        assertEquals(0, proto.getPolicyClassesCount());
    }

    @Test
    void buildExplainProto_roundTrip() throws Exception {
        Node ua = new Node(ua1, "ua1", NodeType.UA);
        Node pc = new Node(pc1, "pc1", NodeType.PC);

        ExplainAssociation association = new ExplainAssociation(
            ua, new AccessRightSet("read"), List.of(new Path(ua)));
        ExplainNode explainNode = new ExplainNode(ua, List.of(association));
        PolicyClassExplain policyClass = new PolicyClassExplain(
            pc, new AccessRightSet("read"), List.of(List.of(explainNode)));

        Explain explain = new Explain(
            new AccessRightSet("read"), List.of(policyClass), new AccessRightSet("write"), List.of());

        gov.nist.ngac.pm.proto.v1.pdp.query.ExplainResponse proto =
            ToProtoUtil.buildExplainProto(explain, pap.query());

        assertEquals(List.of("read"), proto.getPrivilegesList());
        assertEquals(List.of("write"), proto.getDeniedPrivilegesList());
        assertEquals(1, proto.getPolicyClassesCount());

        Explain converted = FromProtoUtil.fromProtoExplainResponse(proto);

        assertEquals(new AccessRightSet("read"), converted.getPrivileges());
        assertEquals(new AccessRightSet("write"), converted.getDeniedPrivileges());
        assertEquals(1, converted.getPolicyClasses().size());
    }
}
