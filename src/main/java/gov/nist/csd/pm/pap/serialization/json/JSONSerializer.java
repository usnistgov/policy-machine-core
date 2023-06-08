package gov.nist.csd.pm.pap.serialization.json;

import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.common.graph.relationship.Association;
import gov.nist.csd.pm.common.obligation.Obligation;
import gov.nist.csd.pm.common.prohibition.Prohibition;
import gov.nist.csd.pm.pap.admin.AdminPolicy;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.op.Operation;
import gov.nist.csd.pm.pap.pml.statement.PMLStatementSerializable;
import gov.nist.csd.pm.pap.routine.Routine;
import gov.nist.csd.pm.pap.serialization.PolicySerializer;
import gov.nist.csd.pm.pap.query.PolicyQuery;

import java.util.*;

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

    private Map<String, JSONNode> buildNonUANodes(PolicyQuery policyQuery, NodeType type) throws PMException {
        Map<String, JSONNode> nodes = new HashMap<>();

        Collection<String> search = policyQuery.graph().search(type, new HashMap<>());
        for (String node : search) {
            if (isUnmodifiedAdminNodeOrTarget(policyQuery, node)) {
                continue;
            }

            Node n = policyQuery.graph().getNode(node);

            String name = n.getName();
            Map<String, String> properties = n.getProperties();
            Collection<String> descs = new ArrayList<>(policyQuery.graph().getAdjacentDescendants(name));

            nodes.put(name, new JSONNode(properties, descs));
        }

        return nodes;
    }

    private boolean isUnmodifiedAdminNodeOrTarget(PolicyQuery policyQuery, String node) throws PMException {
        Collection<String> descendants = policyQuery.graph().getAdjacentDescendants(node);
        return descendants.contains(AdminPolicyNode.ADMIN_POLICY.nodeName()) && descendants.size() == 1;
    }

    private Map<String, JSONNode> buildUserAttributes(PolicyQuery policyQuery) throws PMException {
        Map<String, JSONNode> userAttributes = new HashMap<>();

        Collection<String> search = policyQuery.graph().search(UA, new HashMap<>());
        for (String node : search) {
            Node n = policyQuery.graph().getNode(node);

            String name = n.getName();
            Map<String, String> properties = n.getProperties();
            Collection<String> descendants = policyQuery.graph().getAdjacentDescendants(name);
            Collection<Association> assocList = policyQuery.graph().getAssociationsWithSource(name);
            Map<String, AccessRightSet> assocMap = new HashMap<>();

            for (Association assoc : assocList) {
                assocMap.put(assoc.getTarget(), assoc.getAccessRightSet());
            }

            JSONNode jsonNode;
            if (assocMap.isEmpty()) {
                jsonNode = new JSONNode(properties, descendants);
            } else {
                jsonNode = new JSONNode(properties, descendants, assocMap);
            }

            userAttributes.put(name, jsonNode);
        }

        return userAttributes;
    }

    private Map<String, JSONPolicyClass> buildPolicyClasses(PolicyQuery policyQuery) throws PMException {
        Map<String, JSONPolicyClass> policyClassesList = new HashMap<>();

        Collection<String> policyClasses = policyQuery.graph().getPolicyClasses();
        for (String pc : policyClasses) {
            if (AdminPolicy.isAdminPolicyNodeName(pc)) {
                continue;
            }

            JSONPolicyClass jsonPolicyClass = buildJSONPolicyCLass(pc, policyQuery);

            policyClassesList.put(pc, jsonPolicyClass);
        }

        return policyClassesList;
    }

    private JSONPolicyClass buildJSONPolicyCLass(String pc, PolicyQuery policyQuery) throws PMException {
        Node node = policyQuery.graph().getNode(pc);
        JSONPolicyClass jsonPolicyClass = new JSONPolicyClass();
        if (!node.getProperties().isEmpty()) {
            jsonPolicyClass.setProperties(node.getProperties());
        }

        return jsonPolicyClass;
    }
}
