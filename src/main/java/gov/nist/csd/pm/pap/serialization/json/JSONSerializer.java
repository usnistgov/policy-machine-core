package gov.nist.csd.pm.pap.serialization.json;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.graph.node.Node;
import gov.nist.csd.pm.pap.graph.node.NodeType;
import gov.nist.csd.pm.pap.graph.relationship.Association;
import gov.nist.csd.pm.pap.obligation.Obligation;
import gov.nist.csd.pm.pap.prohibition.Prohibition;
import gov.nist.csd.pm.pap.admin.AdminPolicy;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.op.Operation;
import gov.nist.csd.pm.pap.pml.statement.PMLStatementSerializable;
import gov.nist.csd.pm.pap.routine.Routine;
import gov.nist.csd.pm.pap.serialization.PolicySerializer;
import gov.nist.csd.pm.pap.query.PolicyQuery;

import java.util.*;

import static gov.nist.csd.pm.pap.graph.node.NodeType.*;

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
        Map<String, Collection<Prohibition>> all = policyQuery.prohibitions().getProhibitions();
        for (Collection<Prohibition> value : all.values()) {
            for (Prohibition prohibition : value) {
                prohibitions.add(prohibition.toString());
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

        Collection<String> search = policyQuery.graph().search(type, new HashMap<>());
        for (String node : search) {
            if (isUnmodifiedAdminNodeOrTarget(policyQuery, node)) {
                continue;
            }

            Node n = policyQuery.graph().getNode(node);
            String name = n.getName();
            List<JSONProperty> properties = mapToJsonProperties(n.getProperties());
            Collection<String> descs = new ArrayList<>(policyQuery.graph().getAdjacentDescendants(name));

            nodes.add(new JSONNode(name, properties, descs, null));
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

    private boolean isUnmodifiedAdminNodeOrTarget(PolicyQuery policyQuery, String node) throws PMException {
        Collection<String> descendants = policyQuery.graph().getAdjacentDescendants(node);
        return descendants.contains(AdminPolicyNode.PM_ADMIN_PC.nodeName()) && descendants.size() == 1;
    }

    private List<JSONNode> buildUserAttributes(PolicyQuery policyQuery) throws PMException {
        List<JSONNode> userAttributes = new ArrayList<>();

        Collection<String> search = policyQuery.graph().search(UA, new HashMap<>());
        for (String node : search) {
            Node n = policyQuery.graph().getNode(node);

            String name = n.getName();
            List<JSONProperty> properties = mapToJsonProperties(n.getProperties());
            Collection<String> descendants = policyQuery.graph().getAdjacentDescendants(name);
            Collection<Association> assocList = policyQuery.graph().getAssociationsWithSource(name);
            List<JSONAssociation> jsonAssociations = new ArrayList<>();
            for (Association assoc : assocList) {
                jsonAssociations.add(new JSONAssociation(assoc.getTarget(), assoc.getAccessRightSet()));
            }

            JSONNode jsonNode;
            if (jsonAssociations.isEmpty()) {
                jsonNode = new JSONNode(name, properties, descendants, null);
            } else {
                jsonNode = new JSONNode(name, properties, descendants, jsonAssociations);
            }

            userAttributes.add(jsonNode);
        }

        return userAttributes;
    }

    private List<JSONNode> buildPolicyClasses(PolicyQuery policyQuery) throws PMException {
        List<JSONNode> policyClassesList = new ArrayList<>();

        Collection<String> policyClasses = policyQuery.graph().getPolicyClasses();
        for (String pc : policyClasses) {
            if (AdminPolicy.isAdminPolicyNodeName(pc)) {
                continue;
            }

            Node n = policyQuery.graph().getNode(pc);
            String name = n.getName();
            List<JSONProperty> properties = mapToJsonProperties(n.getProperties());
            JSONNode jsonPolicyClass = new JSONNode(name, properties, null, null);

            policyClassesList.add(jsonPolicyClass);
        }

        return policyClassesList;
    }
}
