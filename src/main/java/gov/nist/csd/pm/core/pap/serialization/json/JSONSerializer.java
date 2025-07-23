package gov.nist.csd.pm.core.pap.serialization.json;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.common.graph.node.NodeType;
import gov.nist.csd.pm.core.common.graph.relationship.Association;
import gov.nist.csd.pm.core.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.core.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.core.pap.obligation.Obligation;
import gov.nist.csd.pm.core.pap.function.op.Operation;
import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.pap.function.routine.Routine;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatementSerializable;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import gov.nist.csd.pm.core.pap.serialization.PolicySerializer;

import gov.nist.csd.pm.core.pap.serialization.json.JSONProhibition.JSONSubject;
import java.util.*;

import static gov.nist.csd.pm.core.common.graph.node.NodeType.*;

public class JSONSerializer implements PolicySerializer {

    @Override
    public String serialize(PolicyQuery policyQuery) throws PMException {
        return buildJSONPolicy(policyQuery).toString();
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
            Routine<?, ?> routine = policyQuery.routines().getAdminRoutine(adminOperationName);
            if (routine instanceof PMLStatementSerializable pmlStatementSerializable) {
                json.add(pmlStatementSerializable.toFormattedString(0));
            }
        }

        return json.isEmpty() ? null : json;
    }

    private List<String> buildOperationsJSON(PolicyQuery policyQuery) throws PMException {
        // can only serialize if implements PMLStatementSerializer
        List<String> json = new ArrayList<>();

        Collection<String> adminOperationNames = policyQuery.operations().getAdminOperationNames();
        for (String adminOperationName : adminOperationNames) {
            Operation<?, ?> operation = policyQuery.operations().getAdminOperation(adminOperationName);
            if (operation instanceof PMLStatementSerializable pmlStatementSerializable) {
                json.add(pmlStatementSerializable.toFormattedString(0));
            }
        }

        return json.isEmpty() ? null : json;
    }

    private List<JSONObligation> buildObligationsJSON(PolicyQuery policyQuery) throws PMException {
        List<JSONObligation> jsonObligations = new ArrayList<>();
        Collection<Obligation> all = policyQuery.obligations().getObligations();
        for (Obligation obligation : all) {
            jsonObligations.add(JSONObligation.fromObligation(obligation));
        }

        return jsonObligations.isEmpty() ? null : jsonObligations;
    }

    private List<JSONProhibition> buildProhibitionsJSON(PolicyQuery policyQuery) throws PMException {
        List<JSONProhibition> prohibitions = new ArrayList<>();
        Collection<Prohibition> all = policyQuery.prohibitions().getProhibitions();
        for (Prohibition prohibition : all) {
            ProhibitionSubject subject = prohibition.getSubject();
            Collection<ContainerCondition> containers = prohibition.getContainers();

            JSONProhibition jsonProhibition = new JSONProhibition(
                prohibition.getName(),
                JSONSubject.fromProhibitionSubject(subject),
                containers,
                prohibition.getAccessRightSet().stream().toList(),
                prohibition.isIntersection()
            );

            prohibitions.add(jsonProhibition);
        }

        return prohibitions.isEmpty() ? null : prohibitions;
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

        Collection<Node> search = policyQuery.graph().search(type, new HashMap<>());
        for (Node node : search) {
            if (isUnmodifiedAdminNodeOrTarget(policyQuery, node.getId())) {
                continue;
            }

            List<JSONProperty> properties = mapToJsonProperties(node.getProperties());
            Collection<Long> descendants = policyQuery.graph().getAdjacentDescendants(node.getId());

            nodes.add(new JSONNode(node.getId(), node.getName(), properties, descendants, null));
        }

        return nodes.isEmpty() ? null : nodes;
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
        Collection<Long> descendants = policyQuery.graph().getAdjacentDescendants(node);

        return descendants.contains(AdminPolicyNode.PM_ADMIN_PC.nodeId()) && descendants.size() == 1;
    }

    private List<JSONNode> buildUserAttributes(PolicyQuery policyQuery) throws PMException {
        List<JSONNode> userAttributes = new ArrayList<>();

        Collection<Node> search = policyQuery.graph().search(UA, new HashMap<>());
        for (Node node : search) {
            Collection<Long> descendants = policyQuery.graph().getAdjacentDescendants(node.getId());
            Collection<Association> assocList = policyQuery.graph().getAssociationsWithSource(node.getId());
            List<JSONAssociation> jsonAssociations = new ArrayList<>();
            for (Association assoc : assocList) {
                jsonAssociations.add(new JSONAssociation(
                    assoc.getTarget(),
                    assoc.getAccessRightSet())
                );
            }

            List<JSONProperty> properties = mapToJsonProperties(node.getProperties());

            JSONNode jsonNode;
            if (jsonAssociations.isEmpty()) {
                jsonNode = new JSONNode(node.getId(), node.getName(), properties, descendants, null);
            } else {
                jsonNode = new JSONNode(node.getId(), node.getName(), properties, descendants, jsonAssociations);
            }

            userAttributes.add(jsonNode);
        }

        return userAttributes.isEmpty() ? null : userAttributes;
    }

    private List<JSONNode> buildPolicyClasses(PolicyQuery policyQuery) throws PMException {
        List<JSONNode> policyClassesList = new ArrayList<>();

        Collection<Long> policyClasses = policyQuery.graph().getPolicyClasses();
        for (long pc : policyClasses) {
            if (AdminPolicyNode.isAdminPolicyNode(pc)) {
                continue;
            }

            Node n = policyQuery.graph().getNodeById(pc);
            String name = n.getName();
            List<JSONProperty> properties = mapToJsonProperties(n.getProperties());
            JSONNode jsonPolicyClass = new JSONNode(n.getId(), name, properties, null, null);

            policyClassesList.add(jsonPolicyClass);
        }

        return policyClassesList.isEmpty() ? null : policyClassesList;
    }
}
