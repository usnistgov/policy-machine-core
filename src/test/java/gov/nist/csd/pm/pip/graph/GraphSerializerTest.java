package gov.nist.csd.pm.pip.graph;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.obligations.MemObligations;
import gov.nist.csd.pm.pip.prohibitions.MemProhibitions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;

import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.*;
import static org.junit.jupiter.api.Assertions.*;

class GraphSerializerTest {

    private static Graph graph;

    @BeforeAll
    static void setUp() throws PMException {
        graph = new MemGraph();

        graph.createPolicyClass("pc1", null);
        graph.createNode("ua1", UA, null, "pc1");
        graph.createNode("oa1", OA, null, "pc1");
        graph.createNode("u1", U, null, "ua1");
        graph.createNode("o1", O, null, "oa1");

        graph.associate("ua1", "oa1", new OperationSet("read", "write"));
    }

    @Test
    void testJson() throws PMException {
        graph = new MemGraph();

        graph.createPolicyClass("pc1", null);
        graph.createNode("ua1", UA, null, "pc1");
        graph.createNode("oa1", OA, null, "pc1");
        graph.createNode("u1", U, null, "ua1");
        graph.createNode("o1", O, null, "oa1");

        graph.associate("ua1", "oa1", new OperationSet("read", "write"));

        String json = GraphSerializer.toJson(graph);
        Graph graph = new MemGraph();
        GraphSerializer.fromJson(graph, json);

        assertTrue(graph.getNodes().containsAll(Arrays.asList(
                new Node("u1", U),
                new Node("o1", O),
                new Node("ua1", UA),
                new Node("oa1", OA),
                new Node("pc1", PC)
        )));

        assertTrue(graph.getChildren("pc1").containsAll(Arrays.asList("oa1")));
        assertTrue(graph.getChildren("oa1").contains("o1"));
        assertTrue(graph.getChildren("ua1").contains("u1"));

        assertTrue(graph.getSourceAssociations("ua1").containsKey("oa1"));
        assertTrue(graph.getSourceAssociations("ua1").get("oa1").containsAll(Arrays.asList("read", "write")));
    }

    @Test
    void testSerialize() throws PMException {
        String serialize = GraphSerializer.serialize(graph);
        Graph graph = new MemGraph();
        GraphSerializer.deserialize(graph, serialize);

        assertTrue(graph.getChildren("pc1").containsAll(Arrays.asList("ua1", "oa1")));
        assertTrue(graph.getChildren("oa1").contains("o1"));
        assertTrue(graph.getChildren("ua1").contains("u1"));

        assertTrue(graph.getSourceAssociations("ua1").containsKey("oa1"));
        assertTrue(graph.getSourceAssociations("ua1").get("oa1").containsAll(Arrays.asList("read", "write")));
    }

    @Test
    void test() throws PMException {
        Graph g = GraphSerializer.deserialize(new MemGraph(), cmd);

        PDP pdp = new PDP(new PAP(g, new MemProhibitions(), new MemObligations()), null);

        System.out.println(g.getParents("bob"));
    }

    String cmd = "" +
            "# nodes\n" +
            "node PC RBAC\n" +
            "node PC approvals\n" +
            "node PC super_pc\n" +
            "node PC OU\n" +
            "node PC users\n" +
            "node PC DAC\n" +
            "\n" +
            "node U super {namespace=super}\n" +
            "node U charlie\n" +
            "node U lucy\n" +
            "node U Grp2Mgr\n" +
            "node U betty\n" +
            "node U alice\n" +
            "node U Grp1Mgr\n" +
            "node U engineer1\n" +
            "node U engineer2\n" +
            "node U bob\n" +
            "\n" +
            "node UA Grp1Mgr_UA {manager=true,namespace=OU}\n" +
            "node UA Budget_Office\n" +
            "node UA DivA\n" +
            "node UA lucy_UA\n" +
            "node UA charlie_UA\n" +
            "node UA DivB\n" +
            "node UA PM\n" +
            "node UA bob_UA\n" +
            "node UA Engineers\n" +
            "node UA users_default_UA {namespace=users}\n" +
            "node UA engineer1_UA\n" +
            "node UA OU_default_UA {namespace=OU}\n" +
            "node UA alice_UA\n" +
            "node UA engineer2_UA\n" +
            "node UA Grp1 {group=true}\n" +
            "node UA Grp2 {group=true}\n" +
            "node UA RBAC_default_UA {namespace=RBAC}\n" +
            "node UA DAC_default_UA {namespace=DAC}\n" +
            "node UA super_ua2 {namespace=super}\n" +
            "node UA super_ua1 {namespace=super}\n" +
            "node UA Managers\n" +
            "node UA Grp2Mgr_UA {manager=true,namespace=OU}\n" +
            "node UA approvals_default_UA {namespace=approvals}\n" +
            "node UA betty_UA\n" +
            "\n" +
            "# homes\n" +
            "node OA homes\n" +
            "node OA bob_home {home=bob}\n" +
            "node OA betty_home {home=betty}\n" +
            "node OA Grp2Mgr_home {home=Grp2Mgr}\n" +
            "node OA engineer2_home {home=engineer2}\n" +
            "node OA Grp1Mgr_home {home=Grp1Mgr}\n" +
            "node OA alice_home {home=alice}\n" +
            "node OA charlie_home {home=charlie}\n" +
            "node OA engineer1_home {home=engineer1}\n" +
            "node OA lucy_home {home=lucy}\n" +
            "\n" +
            "#inboxes\n" +
            "node OA inboxes\n" +
            "node OA engineer1_inbox {inbox=engineer1}\n" +
            "node OA Grp1Mgr_inbox {inbox=Grp1Mgr}\n" +
            "node OA betty_inbox {inbox=betty}\n" +
            "node OA charlie_inbox {inbox=charlie}\n" +
            "node OA Grp2Mgr_inbox {inbox=Grp2Mgr}\n" +
            "node OA bob_inbox {inbox=bob}\n" +
            "node OA lucy_inbox {inbox=lucy}\n" +
            "node OA engineer2_inbox {inbox=engineer2}\n" +
            "node OA alice_inbox {inbox=alice}\n" +
            "\n" +
            "#outboxes\n" +
            "node OA outboxes\n" +
            "node OA bob_outbox {outbox=bob}\n" +
            "node OA engineer1_outbox {outbox=engineer1}\n" +
            "node OA betty_outbox {outbox=betty}\n" +
            "node OA charlie_outbox {outbox=charlie}\n" +
            "node OA Grp2Mgr_outbox {outbox=Grp2Mgr}\n" +
            "node OA engineer2_outbox {outbox=engineer2}\n" +
            "node OA lucy_outbox {outbox=lucy}\n" +
            "node OA alice_outbox {outbox=alice}\n" +
            "node OA Grp1Mgr_outbox {outbox=Grp1Mgr}\n" +
            "\n" +
            "#proposals\n" +
            "node OA betty_ba_proposals {ba_proposals=betty}\n" +
            "node OA approved_proposals\n" +
            "node OA bob_proposals {proposals=bob}\n" +
            "node OA lucy_ba_proposals {ba_proposals=lucy}\n" +
            "node OA engineer2_proposals {proposals=engineer2}\n" +
            "node OA Grp2Mgr_proposals {proposals=Grp2Mgr}\n" +
            "node OA charlie_proposals {proposals=charlie}\n" +
            "node OA alice_pm_proposals {pm_proposals=alice}\n" +
            "node OA lucy_proposals {proposals=lucy}\n" +
            "node OA engineer1_proposals {proposals=engineer1}\n" +
            "node OA bob_pm_proposals {pm_proposals=bob}\n" +
            "node OA lucy_ba_proposals {ba_proposals=lucy}\n" +
            "node OA alice_proposals {proposals=alice}\n" +
            "node OA engineer2_engineer_proposals {engineer_proposals=engineer2}\n" +
            "node OA proposals_containers\n" +
            "node OA charlie_ba_proposals {ba_proposals=charlie}\n" +
            "node OA charlie_ba_proposals {ba_proposals=charlie}\n" +
            "node OA Grp1Mgr_proposals {proposals=Grp1Mgr}\n" +
            "node OA betty_proposals {proposals=betty}\n" +
            "node OA submitted_proposals\n" +
            "node OA proposals\n" +
            "node OA engineer1_engineer_proposals {engineer_proposals=engineer1}\n" +
            "\n" +
            "# OU\n" +
            "node OA OU_default_OA {namespace=OU}\n" +
            "node OA storage_options\n" +
            "node OA gcs_option_OA {storage_object=true,storage_option=GCS}\n" +
            "node O gcs_option {storage_object=true,storage_option=GCS}\n" +
            "node OA aws_option_OA {storage_object=true,storage_option=AWS}\n" +
            "node O aws_option {storage_object=true,storage_option=AWS}\n" +
            "\n" +
            "assign storage_options OU\n" +
            "assign gcs_option_OA storage_options\n" +
            "assign aws_option_OA storage_options\n" +
            "assign gcs_option gcs_option_OA\n" +
            "assign aws_option aws_option_OA\n" +
            "\n" +
            "assoc Grp1 gcs_option_OA [read]\n" +
            "assoc Grp2 aws_option_OA [read]\n" +
            "\n" +
            "# RBAC\n" +
            "node OA Abstracts\n" +
            "node OA SOPs\n" +
            "node OA Technical_Plans\n" +
            "node OA Budget_Analyses\n" +
            "node OA proposal_columns\n" +
            "node OA proposal_rows\n" +
            "node OA RBAC_default_OA {namespace=RBAC}\n" +
            "\n" +
            "# DAC\n" +
            "node OA DAC_default_OA {namespace=DAC}\n" +
            "\n" +
            "# Approvals\n" +
            "node OA Grp1Mgr_approvals {approvals=Grp1Mgr}\n" +
            "node OA Grp2Mgr_approvals {approvals=Grp2Mgr}\n" +
            "node OA Grp1Mgr_approved {approved=Grp1Mgr}\n" +
            "node OA approvals_default_OA {namespace=approvals}\n" +
            "node OA BO_approved\n" +
            "node OA BO_approvals\n" +
            "node OA Grp2Mgr_approved {approved=Grp2Mgr}\n" +
            "node OA Mgr_approvals\n" +
            "\n" +
            "node OA users_default_OA {namespace=users}\n" +
            "node OA super_oa {namespace=super}\n" +
            "#node O super {namespace=super}\n" +
            "\n" +
            "#MLS\n" +
            "node PC MLS {policy=mls}\n" +
            "node OA MLS_default_OA {policy=mls}\n" +
            "node UA MLS_default_UA {policy=mls}\n" +
            "assign MLS_default_UA MLS\n" +
            "assign MLS_default_OA MLS\n" +
            "assign super MLS_default_UA\n" +
            "assoc super_ua1 MLS_default_OA [*]\n" +
            "\n" +
            "node UA public_UA {policy=mls}\n" +
            "node UA secret_UA {policy=mls}\n" +
            "node UA top_secret_UA {policy=mls}\n" +
            "\n" +
            "assign top_secret_UA secret_UA\n" +
            "assign secret_UA public_UA\n" +
            "assign public_UA MLS_default_UA\n" +
            "\n" +
            "node OA public_OA {policy=mls}\n" +
            "node OA secret_OA {policy=mls}\n" +
            "node OA top_secret_OA {policy=mls}\n" +
            "\n" +
            "assign public_OA secret_OA\n" +
            "assign secret_OA top_secret_OA\n" +
            "assign top_secret_OA MLS_default_OA\n" +
            "\n" +
            "assoc public_UA public_OA [read, write, assign, assign to, deassign, deassign from]\n" +
            "assoc secret_UA secret_OA [read, write, assign, assign to, deassign, deassign from]\n" +
            "assoc top_secret_UA top_secret_OA [read, write, assign, assign to, deassign, deassign from]\n" +
            "\n" +
            "# mls users\n" +
            "assign bob top_secret_UA\n" +
            "assign engineer1 top_secret_UA\n" +
            "assign engineer2 secret_UA\n" +
            "assign charlie top_secret_UA\n" +
            "assign betty secret_UA\n" +
            "assign Grp1Mgr top_secret_UA\n" +
            "assign Grp2Mgr top_secret_UA\n" +
            "\n" +
            "# assignments\n" +
            "assign Grp2Mgr_approvals Mgr_approvals\n" +
            "assign super OU_default_UA\n" +
            "assign super DAC_default_UA\n" +
            "assign super super_ua2\n" +
            "assign super approvals_default_UA\n" +
            "assign super RBAC_default_UA\n" +
            "assign super super_ua1\n" +
            "assign super users_default_UA\n" +
            "assign bob_outbox outboxes\n" +
            "assign charlie Budget_Office\n" +
            "assign charlie users_default_UA\n" +
            "assign charlie charlie_UA\n" +
            "assign engineer1_outbox outboxes\n" +
            "assign betty_ba_proposals betty_proposals\n" +
            "assign approved_proposals approvals_default_OA\n" +
            "assign bob_proposals proposals_containers\n" +
            "assign Grp1Mgr_UA Managers\n" +
            "assign Grp1Mgr_UA Grp1\n" +
            "assign lucy Budget_Office\n" +
            "assign lucy lucy_UA\n" +
            "assign lucy_UA users_default_UA\n" +
            "assign lucy Budget_Office\n" +
            "assign super_oa super_pc\n" +
            "assign lucy_ba_proposals lucy_proposals\n" +
            "assign Grp2Mgr Grp2Mgr_UA\n" +
            "assign Grp2Mgr users_default_UA\n" +
            "assign Grp1Mgr_approved Grp1Mgr_approvals\n" +
            "assign Abstracts proposal_columns\n" +
            "assign betty betty_UA\n" +
            "assign betty users_default_UA\n" +
            "assign betty Budget_Office\n" +
            "assign bob_home homes\n" +
            "assign Budget_Office OU_default_UA\n" +
            "assign DivA OU_default_UA\n" +
            "assign Grp1Mgr_UA DAC_default_UA\n" +
            "assign engineer2_proposals proposals_containers\n" +
            "assign SOPs proposal_columns\n" +
            "assign lucy_UA DAC_default_UA\n" +
            "assign betty_home homes\n" +
            "assign charlie_UA DAC_default_UA\n" +
            "assign DivB OU_default_UA\n" +
            "assign PM RBAC_default_UA\n" +
            "assign alice alice_UA\n" +
            "assign alice users_default_UA\n" +
            "assign alice Grp2\n" +
            "assign alice Grp1\n" +
            "assign alice PM\n" +
            "assign Grp2Mgr_proposals proposals_containers\n" +
            "assign Grp1Mgr_inbox inboxes\n" +
            "assign bob_UA DAC_default_UA\n" +
            "assign Grp1Mgr Grp1Mgr_UA\n" +
            "assign Grp1Mgr_UA users_default_UA\n" +
            "assign approvals_default_OA approvals\n" +
            "assign betty_inbox inboxes\n" +
            "assign betty_outbox outboxes\n" +
            "assign charlie_outbox outboxes\n" +
            "assign Engineers RBAC\n" +
            "assign Grp2Mgr_outbox outboxes\n" +
            "assign charlie_proposals proposals_containers\n" +
            "assign homes DAC_default_OA\n" +
            "assign users_default_UA users\n" +
            "assign engineer2_outbox outboxes\n" +
            "assign alice_pm_proposals alice_proposals\n" +
            "assign Grp2Mgr_UA DAC_default_UA\n" +
            "assign charlie_inbox inboxes\n" +
            "assign Grp2Mgr_home homes\n" +
            "assign OU_default_OA OU\n" +
            "assign engineer1_UA DAC_default_UA\n" +
            "assign users_default_OA users\n" +
            "assign lucy_proposals proposals_containers\n" +
            "assign engineer1_proposals proposals_containers\n" +
            "assign bob_pm_proposals bob_proposals\n" +
            "assign OU_default_UA OU\n" +
            "assign lucy_ba_proposals lucy_proposals\n" +
            "assign alice_UA DAC_default_UA\n" +
            "assign engineer2_UA DAC_default_UA\n" +
            "assign alice_proposals proposals_containers\n" +
            "assign Grp1 DivA\n" +
            "assign engineer2_engineer_proposals engineer2_proposals\n" +
            "assign Grp2Mgr_inbox inboxes\n" +
            "assign Grp1Mgr_approvals Mgr_approvals\n" +
            "assign engineer2_home homes\n" +
            "assign outboxes DAC_default_OA\n" +
            "assign Grp2 DivB\n" +
            "assign proposals_containers DAC_default_OA\n" +
            "assign charlie_ba_proposals charlie_proposals\n" +
            "assign engineer1_inbox inboxes\n" +
            "assign inboxes DAC_default_OA\n" +
            "assign charlie_ba_proposals charlie_proposals\n" +
            "assign Technical_Plans proposal_columns\n" +
            "\n" +
            "assign BO_approved approved_proposals\n" +
            "\n" +
            "assign RBAC_default_UA RBAC\n" +
            "assign Grp1Mgr_home homes\n" +
            "assign Grp1Mgr_proposals proposals_containers\n" +
            "assign super_ua1 approvals\n" +
            "assign super_ua1 users\n" +
            "assign super_ua1 OU\n" +
            "assign super_ua1 super_pc\n" +
            "assign super_ua1 DAC\n" +
            "assign super_ua1 RBAC\n" +
            "assign lucy_outbox outboxes\n" +
            "assign DAC_default_UA DAC\n" +
            "assign super_ua2 super_pc\n" +
            "assign alice_outbox outboxes\n" +
            "# deleted super_o in core library -- assign super_O super_oa\n" +
            "assign engineer2 Engineers\n" +
            "assign engineer2 users_default_UA\n" +
            "assign engineer2 Grp2\n" +
            "assign bob_inbox inboxes\n" +
            "assign Managers approvals_default_UA\n" +
            "assign Managers RBAC_default_UA\n" +
            "assign betty_proposals proposals_containers\n" +
            "assign Grp2Mgr_UA Managers\n" +
            "assign Grp2Mgr_UA Grp2\n" +
            "assign proposal_rows proposals\n" +
            "assign proposal_columns proposals\n" +
            "assign alice_home homes\n" +
            "assign charlie_home homes\n" +
            "assign submitted_proposals approvals\n" +
            "assign Mgr_approvals submitted_proposals\n" +
            "assign lucy_inbox inboxes\n" +
            "assign RBAC_default_OA RBAC\n" +
            "assign proposals RBAC_default_OA\n" +
            "assign engineer1 Engineers\n" +
            "assign engineer1 users_default_UA\n" +
            "assign engineer1 Grp1\n" +
            "assign approvals_default_UA approvals\n" +
            "assign BO_approvals submitted_proposals\n" +
            "assign Grp2Mgr_approved Grp2Mgr_approvals\n" +
            "assign engineer2_inbox inboxes\n" +
            "assign Budget_Office approvals_default_UA\n" +
            "assign Budget_Office RBAC_default_UA\n" +
            "assign lucy_home homes\n" +
            "assign Grp1Mgr_outbox outboxes\n" +
            "assign DAC_default_OA DAC\n" +
            "assign betty_UA DAC_default_UA\n" +
            "assign engineer1_engineer_proposals engineer1_proposals\n" +
            "assign alice_inbox inboxes\n" +
            "assign bob bob_UA\n" +
            "assign bob users_default_UA\n" +
            "assign bob Grp1\n" +
            "assign bob PM\n" +
            "assign engineer1_home homes\n" +
            "assign Budget_Analyses proposal_columns\n" +
            "\n" +
            "\n" +
            "\n" +
            "# associations\n" +
            "assoc Grp1Mgr_UA Grp1Mgr_approvals [assign to, assign, read]\n" +
            "assoc Grp1Mgr_UA Grp1Mgr_outbox [assign to, read, write, assign]\n" +
            "assoc Grp1Mgr_UA Grp1Mgr_inbox [read, write, assign]\n" +
            "assoc Grp1Mgr_UA Grp1Mgr_home [*]\n" +
            "assoc Grp1Mgr_UA Grp1Mgr_proposals [assign to, read, assign]\n" +
            "\n" +
            "assoc lucy_UA lucy_inbox [read, write, assign]\n" +
            "assoc lucy_UA lucy_ba_proposals [read, write]\n" +
            "assoc lucy_UA lucy_ba_proposals [read, write]\n" +
            "assoc lucy_UA lucy_outbox [assign to, read, write, assign]\n" +
            "assoc lucy_UA lucy_proposals [assign to, read, assign]\n" +
            "assoc lucy_UA lucy_home [*]\n" +
            "\n" +
            "assoc charlie_UA charlie_inbox [read, write, assign]\n" +
            "assoc charlie_UA charlie_ba_proposals [read, write]\n" +
            "assoc charlie_UA charlie_outbox [assign to, read, write, assign]\n" +
            "assoc charlie_UA charlie_ba_proposals [read, write]\n" +
            "assoc charlie_UA charlie_proposals [assign to, read, assign]\n" +
            "assoc charlie_UA charlie_home [*]\n" +
            "\n" +
            "assoc PM proposals [assign to, read, deassign from, deassign, write, assign]\n" +
            "assoc PM approvals_default_OA [read]\n" +
            "assoc PM submitted_proposals [assign to, read, assign]\n" +
            "\n" +
            "assoc bob_UA bob_outbox [assign to, read, write, assign]\n" +
            "assoc bob_UA bob_inbox [read, write, assign]\n" +
            "assoc bob_UA bob_proposals [assign to, read, assign]\n" +
            "assoc bob_UA bob_pm_proposals [assign to, read, deassign from, deassign, write, assign]\n" +
            "assoc bob_UA bob_home [*]\n" +
            "\n" +
            "assoc Engineers Technical_Plans [read, write]\n" +
            "assoc Engineers approvals_default_OA [read]\n" +
            "assoc Engineers Abstracts [read]\n" +
            "assoc Engineers SOPs [read]\n" +
            "assoc Engineers proposal_rows [read]\n" +
            "\n" +
            "assoc Grp2Mgr_UA Grp2Mgr_inbox [read, write, assign]\n" +
            "assoc Grp2Mgr_UA Grp2Mgr_proposals [assign to, read, assign]\n" +
            "assoc Grp2Mgr_UA Grp2Mgr_home [*]\n" +
            "assoc Grp2Mgr_UA Grp2Mgr_outbox [assign to, read, write, assign]\n" +
            "\n" +
            "assoc engineer1_UA engineer1_engineer_proposals [read, write]\n" +
            "assoc engineer1_UA engineer1_home [*]\n" +
            "assoc engineer1_UA engineer1_outbox [assign to, read, write, assign]\n" +
            "assoc engineer1_UA engineer1_proposals [assign to, read, assign]\n" +
            "assoc engineer1_UA engineer1_inbox [read, write, assign]\n" +
            "\n" +
            "assoc alice_UA alice_inbox [read, write, assign]\n" +
            "assoc alice_UA alice_outbox [assign to, read, write, assign]\n" +
            "assoc alice_UA alice_home [*]\n" +
            "assoc alice_UA alice_proposals [assign to, read, assign]\n" +
            "assoc alice_UA alice_pm_proposals [assign to, read, deassign from, deassign, write, assign]\n" +
            "\n" +
            "assoc engineer2_UA engineer2_proposals [assign to, read, assign]\n" +
            "assoc engineer2_UA engineer2_home [*]\n" +
            "assoc engineer2_UA engineer2_inbox [read, write, assign]\n" +
            "assoc engineer2_UA engineer2_outbox [assign to, read, write, assign]\n" +
            "assoc engineer2_UA engineer2_engineer_proposals [read, write]\n" +
            "\n" +
            "assoc super_ua1 OU_default_UA [*]\n" +
            "assoc super_ua1 super_oa [*]\n" +
            "assoc super_ua1 DAC_default_OA [*]\n" +
            "assoc super_ua1 RBAC_default_OA [*]\n" +
            "assoc super_ua1 DAC_default_UA [*]\n" +
            "assoc super_ua1 OU_default_OA [*]\n" +
            "assoc super_ua2 super_ua1 [*]\n" +
            "assoc super_ua1 approvals_default_UA [*]\n" +
            "assoc super_ua1 RBAC_default_UA [*]\n" +
            "assoc super_ua1 users_default_OA [*]\n" +
            "assoc super_ua1 approvals_default_UA [*]\n" +
            "assoc super_ua1 users_default_UA [*]\n" +
            "\n" +
            "assoc DAC_default_UA inboxes [assign to]\n" +
            "assoc DAC_default_UA proposals_containers [assign to, deassign from]\n" +
            "# assoc DAC outboxes [assign to, read, write, assign]\n" +
            "\n" +
            "assoc Managers proposals [read, assign]\n" +
            "\n" +
            "assoc Grp2Mgr_UA Grp2Mgr_approvals [assign to, assign, read]\n" +
            "\n" +
            "assoc approvals_default_UA approved_proposals [read]\n" +
            "\n" +
            "assoc Budget_Office proposals [read, assign]\n" +
            "assoc Budget_Office Technical_Plans [read]\n" +
            "assoc Budget_Office BO_approvals [assign]\n" +
            "assoc Budget_Office BO_approved [assign to]\n" +
            "assoc Budget_Office approvals_default_OA [read]\n" +
            "assoc Budget_Office Abstracts [read]\n" +
            "assoc Budget_Office SOPs [read]\n" +
            "assoc Budget_Office Budget_Analyses [read, write]\n" +
            "\n" +
            "assoc betty_UA betty_proposals [assign to, read, assign]\n" +
            "assoc betty_UA betty_inbox [read, write, assign]\n" +
            "assoc betty_UA betty_ba_proposals [read, write]\n" +
            "assoc betty_UA betty_outbox [assign to, read, write, assign]\n" +
            "assoc betty_UA betty_home [*]";
}