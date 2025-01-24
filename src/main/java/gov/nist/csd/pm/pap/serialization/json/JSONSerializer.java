package gov.nist.csd.pm.pap.serialization.json;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.common.graph.relationship.Association;
import gov.nist.csd.pm.common.obligation.Obligation;
import gov.nist.csd.pm.common.prohibition.Prohibition;
import gov.nist.csd.pm.pap.admin.AdminPolicy;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.common.op.Operation;
import gov.nist.csd.pm.pap.pml.statement.PMLStatementSerializable;
import gov.nist.csd.pm.common.routine.Routine;
import gov.nist.csd.pm.pap.pml.statement.operation.CreateProhibitionStatement;
import gov.nist.csd.pm.pap.serialization.PolicySerializer;
import gov.nist.csd.pm.pap.query.PolicyQuery;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static gov.nist.csd.pm.common.graph.node.NodeType.*;

public class JSONSerializer implements PolicySerializer {

    @Override
    public String serialize(PolicyQuery policyQuery) throws PMException {
        return buildJSONPolicy(policyQuery)
                .toString();
    }

    public JSONPolicy buildJSONPolicy(PolicyQuery policyQuery) throws PMException {
        return new JSONPolicy(
                policyQuery.operations().getResourceOperations(),
                buildGraphJSON(policyQuery),
                buildProhibitionsJSON(policyQuery),
                buildObligationsJSON(policyQuery),
                buildOperationsJSON(policyQuery),
                buildRoutinesJSON(policyQuery)
        );
    }

    private List<String> buildRoutinesJSON(PolicyQuery policyQuery) throws PMException {
        // can only serialize if implements PMLStatementSerializer
        List<String> json = new ArrayList<>();

        Collection<String> adminRoutineNames = policyQuery.routines().getAdminRoutineNames();
        for (String adminOperationName : adminRoutineNames) {
            Routine<?> routine = policyQuery.routines().getAdminRoutine(adminOperationName);
            if (routine instanceof PMLStatementSerializable pmlStatementSerializable) {
                json.add(pmlStatementSerializable.toFormattedString(0));
            }
        }

        return json;
    }

    private List<String> buildOperationsJSON(PolicyQuery policyQuery) throws PMException {
        // can only serialize if implements PMLStatementSerializer
        List<String> json = new ArrayList<>();

        Collection<String> adminOperationNames = policyQuery.operations().getAdminOperationNames();
        for (String adminOperationName : adminOperationNames) {
            Operation<?> operation = policyQuery.operations().getAdminOperation(adminOperationName);
            if (operation instanceof PMLStatementSerializable pmlStatementSerializable) {
                json.add(pmlStatementSerializable.toFormattedString(0));
            }
        }

        return json;
    }

    private List<String> buildObligationsJSON(PolicyQuery policyQuery) throws PMException {
        List<String> jsonObligations = new ArrayList<>();
        Collection<Obligation> all = policyQuery.obligations().getObligations();
        for (Obligation obligation : all) {
            jsonObligations.add(obligation.toString());
        }

        return jsonObligations;
    }

    private List<String> buildProhibitionsJSON(PolicyQuery policyQuery) throws PMException {
        List<String> prohibitions = new ArrayList<>();
        Map<Node, Collection<Prohibition>> all = policyQuery.prohibitions().getProhibitions();
        for (Collection<Prohibition> value : all.values()) {
            for (Prohibition prohibition : value) {
                prohibitions.add(CreateProhibitionStatement.fromProhibition(policyQuery, prohibition).toString());
            }
        }

        return prohibitions;
    }

    private JSONGraph buildGraphJSON(PolicyQuery policyQuery) throws PMException {
        return new JSONGraph(
                buildPolicyClasses(policyQuery),
                buildUserAttributes(policyQuery),
                buildNonUANodes(policyQuery, OA),
                buildNonUANodes(policyQuery, U),
                buildNonUANodes(policyQuery, O)
        );
    }

    private List<JSONNode> buildNonUANodes(PolicyQuery policyQuery, NodeType type) throws PMException {
        List<JSONNode> nodes = new ArrayList<>();

        long[] search = policyQuery.graph().search(type, new HashMap<>());
        for (long node : search) {
            if (isUnmodifiedAdminNodeOrTarget(policyQuery, node)) {
                continue;
            }

            Node n = policyQuery.graph().getNodeById(node);
            String name = n.getName();
            List<JSONProperty> properties = mapToJsonProperties(n.getProperties());

            long[] descendants = policyQuery.graph().getAdjacentDescendants(node);
            List<String> descendantNames = LongStream.of(descendants)
                    .mapToObj(m -> {
                        try {
                            return policyQuery.graph().getNodeById(m).getName();
                        } catch (PMException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();

            nodes.add(new JSONNode(name, properties, descendantNames, null));
        }

        return nodes;
    }

    private List<JSONProperty> mapToJsonProperties(Map<String, String> map) {
        List<JSONProperty> jsonProperties = new ArrayList<>();

        if (map == null || map.isEmpty()) {
            return null;
        }

        for (Map.Entry<String, String> entry : map.entrySet()) {
            jsonProperties.add(new JSONProperty(entry.getKey(), entry.getValue()));
        }

        return jsonProperties;
    }

    private boolean isUnmodifiedAdminNodeOrTarget(PolicyQuery policyQuery, long node) throws PMException {
        long[] descendants = policyQuery.graph().getAdjacentDescendants(node);

        return LongStream.of(descendants)
                .boxed()
                .toList()
                .contains(AdminPolicyNode.PM_ADMIN_PC.nodeId()) && descendants.length == 1;
    }

    private List<JSONNode> buildUserAttributes(PolicyQuery policyQuery) throws PMException {
        List<JSONNode> userAttributes = new ArrayList<>();

        long[] search = policyQuery.graph().search(UA, new HashMap<>());
        for (long node : search) {
            Node n = policyQuery.graph().getNodeById(node);

            long[] descendants = policyQuery.graph().getAdjacentDescendants(node);
            List<String> descendantNames = LongStream.of(descendants)
                    .mapToObj(m -> {
                        try {
                            return policyQuery.graph().getNodeById(m).getName();
                        } catch (PMException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();

            Collection<Association> assocList = policyQuery.graph().getAssociationsWithSource(node);
            List<JSONAssociation> jsonAssociations = new ArrayList<>();
            for (Association assoc : assocList) {
                jsonAssociations.add(new JSONAssociation(
                        policyQuery.graph().getNodeById(assoc.getTarget()).getName(),
                        assoc.getAccessRightSet())
                );
            }

            List<JSONProperty> properties = mapToJsonProperties(n.getProperties());

            JSONNode jsonNode;
            if (jsonAssociations.isEmpty()) {
                jsonNode = new JSONNode(n.getName(), properties, descendantNames, null);
            } else {
                jsonNode = new JSONNode(n.getName(), properties, descendantNames, jsonAssociations);
            }

            userAttributes.add(jsonNode);
        }

        return userAttributes;
    }

    private List<JSONNode> buildPolicyClasses(PolicyQuery policyQuery) throws PMException {
        List<JSONNode> policyClassesList = new ArrayList<>();

        long[] policyClasses = policyQuery.graph().getPolicyClasses();
        for (long pc : policyClasses) {
            if (AdminPolicy.isAdminPolicyId(pc)) {
                continue;
            }

            Node n = policyQuery.graph().getNodeById(pc);
            String name = n.getName();
            List<JSONProperty> properties = mapToJsonProperties(n.getProperties());
            JSONNode jsonPolicyClass = new JSONNode(name, properties, null, null);

            policyClassesList.add(jsonPolicyClass);
        }

        return policyClassesList;
    }
}
