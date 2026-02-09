package gov.nist.csd.pm.core.pap.serialization.json;

import static gov.nist.csd.pm.core.common.graph.node.NodeType.O;
import static gov.nist.csd.pm.core.common.graph.node.NodeType.OA;
import static gov.nist.csd.pm.core.common.graph.node.NodeType.U;
import static gov.nist.csd.pm.core.common.graph.node.NodeType.UA;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.common.graph.node.NodeType;
import gov.nist.csd.pm.core.pap.graph.Association;
import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.obligation.Obligation;
import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.Function;
import gov.nist.csd.pm.core.pap.operation.Operation;
import gov.nist.csd.pm.core.pap.operation.QueryOperation;
import gov.nist.csd.pm.core.pap.operation.ResourceOperation;
import gov.nist.csd.pm.core.pap.operation.Routine;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatementSerializable;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import gov.nist.csd.pm.core.pap.serialization.PolicySerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONSerializer implements PolicySerializer {

    @Override
    public String serialize(PolicyQuery policyQuery) throws PMException {
        return buildJSONPolicy(policyQuery).toString();
    }

    public JSONPolicy buildJSONPolicy(PolicyQuery policyQuery) throws PMException {
        return new JSONPolicy(
            policyQuery.operations().getResourceAccessRights(),
            buildGraphJSON(policyQuery),
            buildProhibitionsJSON(policyQuery),
            buildObligationsJSON(policyQuery),
            buildOperationsJSON(policyQuery)
        );
    }

    private JSONOperations buildOperationsJSON(PolicyQuery policyQuery) throws PMException {
        List<String> admin = new ArrayList<>();
        List<String> resource = new ArrayList<>();
        List<String> routine = new ArrayList<>();
        List<String> query = new ArrayList<>();
        List<String> basic = new ArrayList<>();

        Collection<Operation<?>> operations = policyQuery.operations().getOperations();
        for (Operation<?> operation : operations) {
            // can only serialize if implements PMLStatementSerializer
            if (!(operation instanceof PMLStatementSerializable serializableOp)) {
                continue;
            }

            switch (operation) {
                case AdminOperation<?> o -> admin.add(serializableOp.toFormattedString(0));
                case Function<?> o -> basic.add(serializableOp.toFormattedString(0));
                case QueryOperation<?> o -> query.add(serializableOp.toFormattedString(0));
                case ResourceOperation<?> o -> resource.add(serializableOp.toFormattedString(0));
                case Routine<?> o -> routine.add(serializableOp.toFormattedString(0));
            }
        }

        JSONOperations jsonOperations = new JSONOperations(admin, resource, routine, query, basic);

        return jsonOperations.getAll().isEmpty() ? null : jsonOperations;
    }

    private List<JSONObligation> buildObligationsJSON(PolicyQuery policyQuery) throws PMException {
        List<JSONObligation> jsonObligations = new ArrayList<>();
        Collection<Obligation> all = policyQuery.obligations().getObligations();
        for (Obligation obligation : all) {
            jsonObligations.add(JSONObligation.fromObligation(obligation));
        }

        return jsonObligations.isEmpty() ? null : jsonObligations;
    }

    private List<Prohibition> buildProhibitionsJSON(PolicyQuery policyQuery) throws PMException {
        List<Prohibition> prohibitions = new ArrayList<>(policyQuery.prohibitions().getProhibitions());
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

        return AdminPolicyNode.isAdminPolicyNode(node) && descendants.size() == 1;
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
                    assoc.target(),
                    assoc.arset())
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
