package gov.nist.csd.pm.pip.obligations.evr;

import gov.nist.csd.pm.pip.obligations.model.Condition;
import gov.nist.csd.pm.pip.obligations.model.EvrNode;
import gov.nist.csd.pm.pip.obligations.model.NegatedCondition;
import gov.nist.csd.pm.pip.obligations.model.ResponsePattern;
import gov.nist.csd.pm.pip.obligations.model.actions.*;
import gov.nist.csd.pm.pip.obligations.model.functions.Function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pip.obligations.evr.EVRParser.*;

public class PmResponseParser implements ResponseParser {

    private List<EventParser> customEventParsers;
    private List<ResponseParser> customResponseParsers;

    public PmResponseParser(List<EventParser> customEventParsers, List<ResponseParser> customResponseParsers) {
        this.customEventParsers = customEventParsers;
        this.customResponseParsers = customResponseParsers;
    }

    @Override
    public String key() {
        // system responses are handled in the parser and don't need a key
        return "";
    }

    /**
     * condition:
     * condition!:
     * actions:
     */
    public ResponsePattern parse(Map map) throws EVRException {
        ResponsePattern responsePattern = new ResponsePattern();
        if(map == null) {
            return responsePattern;
        }

        if(map.containsKey("condition")) {
            responsePattern.setCondition(parseCondition(map.get("condition")));
        }
        if(map.containsKey("condition!")) {
            responsePattern.setNegatedCondition(parseNegatedCondition(map.get("condition!")));
        }
        if(map.containsKey("actions")) {
            List actionsList = getObject(map.get("actions"), List.class);

            for(Object a : actionsList) {
                Map actionMap = getObject(a, Map.class);
                responsePattern.addAction(parseAction(actionMap));
            }
        }

        return responsePattern;
    }

    /**
     * condition:
     *   - function:
     *   - function:
     *
     *   if all functions evaluate to true the condition is true
     */
    private Condition parseCondition(Object o) throws EVRException {
        List list = getObject(o, List.class);
        List<Function> functions = new ArrayList<>();
        for(Object l : list) {
            Map lMap = getObject(l, Map.class);
            Map funcMap = getObject(lMap.get("function"), Map.class);
            functions.add(parseFunction(funcMap));
        }

        Condition condition = new Condition();
        condition.setCondition(functions);
        return condition;
    }

    /**
     * condition:
     *   - function:
     *   - function:
     *
     *   if all functions evaluate to false the condition is true
     */
    private NegatedCondition parseNegatedCondition(Object o) throws EVRException {
        List list = getObject(o, List.class);
        List<Function> functions = new ArrayList<>();
        for(Object l : list) {
            Map lMap = getObject(l, Map.class);
            Map funcMap = getObject(lMap.get("function"), Map.class);
            functions.add(parseFunction(funcMap));
        }

        NegatedCondition negatedCondition = new NegatedCondition();
        negatedCondition.setCondition(functions);
        return negatedCondition;
    }

    private Action parseAction(Map map) throws EVRException {
        Condition condition = null;
        if (map.containsKey("condition")) {
            condition = parseCondition(map.get("condition"));
        }

        NegatedCondition negatedCondition = null;
        if (map.containsKey("condition!")) {
            negatedCondition = parseNegatedCondition(map.get("condition!"));
        }

        Action action = null;
        if(map.containsKey("create")) {
            action = parseCreateAction(map.get("create"));
        } else if(map.containsKey("assign")) {
            action = parseAssignAction(map.get("assign"));
        } else if(map.containsKey("grant")) {
            action = parseGrantAction(map.get("grant"));
        } else if(map.containsKey("deny")) {
            action = parseDenyAction(map.get("deny"));
        } else if(map.containsKey("delete")) {
            action = parseDeleteAction(map.get("delete"));
        } else if(map.containsKey("function")) {
            action = parseFunctionAction(map.get("function"));
        }

        if(action == null) {
            throw new EVRException("invalid action received " + map);
        }

        if(condition != null) {
            action.setCondition(condition);
        }

        if (negatedCondition != null) {
            action.setNegatedCondition(negatedCondition);
        }

        return action;
    }

    private Action parseFunctionAction(Object o) throws EVRException {
        Map map = getObject(o, Map.class);
        return new FunctionAction(parseFunction(map));
    }

    /**
     * create:
     *   - what:
     *       name:
     *       type:
     *       properties:
     *       --
     *       - function:
     *     where:
     *       name:
     *       type:
     *       function:
     *   ---
     *   - rule:
     */
    private Action parseCreateAction(Object o) throws EVRException {
        if(o == null) {
            throw new EVRException("create action cannot be null or empty");
        }

        CreateAction action = new CreateAction();
        List createActionList = getObject(o, List.class);
        for (Object obj : createActionList) {
            Map map = getObject(obj, Map.class);
            if (map.containsKey("label") &&
                    map.containsKey("event") &&
                    map.containsKey("response")) {
                // use the evrparser to parse the rule
                action.addRule(EVRParser.parseRule(map, customEventParsers, customResponseParsers));
            } else if (map.containsKey("what") &&
                    map.containsKey("where")) {
                action.addCreateNode(new CreateAction.CreateNode(
                        parseEvrNode(getObject(map.get("what"), Map.class)),
                        parseEvrNode(getObject(map.get("where"), Map.class))
                ));
            }
        }

        return action;
    }

    /**
     * The assign action creates a set of assignments in the graph.
     *
     * assign:
     *   - what:
     *     where:
     *
     *
     * the "like" element described in the standard is not implemented
     */
    private Action parseAssignAction(Object o) throws EVRException {
        if(o == null) {
            throw new EVRException("assign action cannot be null or empty");
        }

        AssignAction action = new AssignAction();

        List assignActionList = getObject(o, List.class);
        for (Object assignObj : assignActionList) {
            Map map = getObject(assignObj, Map.class);
            if(!map.containsKey("what")) {
                throw new EVRException("assign action does not have a \"what\" field in " + map);
            } else if(!map.containsKey("where")) {
                throw new EVRException("assign action does not have a \"where\" field in " + map);
            }

            EvrNode what = parseEvrNode(getObject(map.get("what"), Map.class));
            EvrNode where = parseEvrNode(getObject(map.get("where"), Map.class));

            action.addAssignment(new AssignAction.Assignment(what, where));
        }

        return action;
    }

    /**
     * grant:
     *   subject:
     *     name:
     *     type:
     *     --
     *     function:
     *   operations:
     *     - ""
     *     - ""
     *   target:
     *     name:
     *     type:
     *     --
     *     function:
     *
     */
    private Action parseGrantAction(Object o) throws EVRException {
        if(o == null) {
            throw new EVRException("grant action cannot be null or empty");
        }

        Map grantActionMap = getObject(o, Map.class);
        if(!grantActionMap.containsKey("subject")) {
            throw new EVRException("grant action does not contain a \"subject\" field in " + grantActionMap);
        } else if(!grantActionMap.containsKey("target")) {
            throw new EVRException("grant action does not contain a \"target\" field in " + grantActionMap);
        }

        GrantAction action = new GrantAction();

        Map subjectMap = getObject(grantActionMap.get("subject"), Map.class);
        EvrNode subject = parseSubject(subjectMap);
        action.setSubject(subject);

        if(grantActionMap.containsKey("operations")) {
            List opsList = getObject(grantActionMap.get("operations"), List.class);
            List<String> operations = new ArrayList<>();
            for (Object opObj : opsList) {
                operations.add(getObject(opObj, String.class));
            }
            action.setOperations(operations);
        }

        Map targetMap = getObject(grantActionMap.get("target"), Map.class);
        EvrNode target = parseTarget(targetMap);
        action.setTarget(target);

        return action;
    }

    private EvrNode parseSubject(Map map) throws EVRException {
        return parseEvrNode(map);
    }

    private EvrNode parseTarget(Map map) throws EVRException {
        return parseEvrNode(map);
    }

    /**
     * deny:
     *   label:
     *   subject: priority goes 1. function, 2. process, 3. node
     *     function:
     *     ---
     *     process:
     *     ---
     *     name:
     *     type:
     *     properties:
     *   operations:
     *     - ""
     *     - ""
     *   target:
     *     complement: true|false, default false
     *     intersection: true|false, default false
     *     containers:
     *       - name:
     *         type:
     *         complement: true|false
     *       - function:
     *         complement: true|false
     *
     */
    private Action parseDenyAction(Object o) throws EVRException {
        if(o == null) {
            throw new EVRException("deny action cannot be null or empty");
        }

        Map denyActionMap = getObject(o, Map.class);
        if(!denyActionMap.containsKey("label")) {
            throw new EVRException("deny action does not contain a \"label\" field in " + denyActionMap);
        } else if(!denyActionMap.containsKey("subject")) {
            throw new EVRException("deny action does not contain a \"subject\" field in " + denyActionMap);
        } else if(!denyActionMap.containsKey("operations")) {
            throw new EVRException("deny action does not contain a \"operations\" field in " + denyActionMap);
        } else if(!denyActionMap.containsKey("target")) {
            throw new EVRException("deny action does not contain a \"target\" field in " + denyActionMap);
        }

        DenyAction action = new DenyAction();
        Object label = denyActionMap.get("label");
        action.setLabel((String) label);

        Map subjectMap = getObject(denyActionMap.get("subject"), Map.class);
        EvrNode evrNode = parseEvrNode(subjectMap);
        action.setSubject(evrNode);

        List opsList = getObject(denyActionMap.get("operations"), List.class);
        List<String> operations = new ArrayList<>();
        for(Object opObj : opsList) {
            operations.add(getObject(opObj, String.class));
        }
        action.setOperations(operations);

        Map targetMap = getObject(denyActionMap.get("target"), Map.class);
        DenyAction.Target target = parseDenyActionTarget(targetMap);
        action.setTarget(target);

        return action;
    }

    private DenyAction.Target parseDenyActionTarget(Object o) throws EVRException {
        Map targetMap = getObject(o, Map.class);

        DenyAction.Target target = new DenyAction.Target();

        boolean complement = false;
        if(targetMap.containsKey("complement")) {
            complement = getObject(targetMap.get("complement"), Boolean.class);
        }
        target.setComplement(complement);

        boolean intersection = false;
        if(targetMap.containsKey("intersection")) {
            intersection = getObject(targetMap.get("intersection"), Boolean.class);
        }
        target.setIntersection(intersection);

        if(!targetMap.containsKey("containers")) {
            throw new EVRException("no containers were provided in deny action target at " + o);
        }

        List contList = getObject(targetMap.get("containers"), List.class);
        List<DenyAction.Target.Container> containers = parseDenyActionTargetContainers(contList);
        target.setContainers(containers);

        return target;
    }

    private List<DenyAction.Target.Container> parseDenyActionTargetContainers(List list) throws EVRException {
        List<DenyAction.Target.Container> containers = new ArrayList<>();
        for(Object contObj : list) {
            Map contMap = getObject(contObj, Map.class);

            if(contMap.containsKey("function")) {
                Map contFunMap = getObject(contMap.get("function"), Map.class);
                DenyAction.Target.Container container = new DenyAction.Target.Container(parseFunction(contFunMap));
                containers.add(container);
            } else {
                String name = getObject(contMap.get("name"), String.class);
                if (name == null || name.isEmpty()) {
                    throw new EVRException("name cannot be null in deny action target container at " + contMap);
                }

                String type = getObject(contMap.get("type"), String.class);
                if (type == null || type.isEmpty()) {
                    throw new EVRException("type cannot be null in deny action target container at " + contMap);
                }

                Object propsObj = contMap.get("properties");
                Map<String, String> properties = new HashMap<>();
                if(propsObj != null) {
                    Map propsMap = getObject(propsObj, Map.class);
                    for(Object p : propsMap.keySet()) {
                        properties.put(getObject(p, String.class), getObject(propsMap.get(p), String.class));
                    }
                }

                DenyAction.Target.Container container = new DenyAction.Target.Container(name, type, properties);
                if (contMap.containsKey("complement")) {
                    boolean compl = getObject(contMap.get("complement"), Boolean.class);
                    container.setComplement(compl);
                }

                containers.add(container);
            }
        }

        return containers;
    }

    /**
     * delete:
     *   nodes:
     *     - name:
     *       type:
     *   assignments:
     *     - what:
     *         name:
     *         type:
     *       where:
     *         name:
     *         type:
     *   associations:
     *     - subject:
     *       target:
     *   prohibitions:
     *     - ""
     *   rules:
     *     - ""
     */
    private Action parseDeleteAction(Object o) throws EVRException {
        if(o == null) {
            throw new EVRException("delete action cannot be null or empty");
        }

        DeleteAction action = new DeleteAction();
        Map deleteMap = getObject(o, Map.class);
        if (deleteMap.containsKey("nodes")) {
            action.setNodes(parseDeleteNodes(deleteMap.get("nodes")));
        }
        if (deleteMap.containsKey("assignments")) {
            action.setAssignments((AssignAction) parseAssignAction(deleteMap.get("assignments")));
        }
        if (deleteMap.containsKey("associations")) {
            action.setAssociations(parseDeleteAssociations(deleteMap.get("associations")));
        }
        if (deleteMap.containsKey("prohibitions")) {
            action.setProhibitions(parseDeleteLabelList(deleteMap.get("prohibitions")));
        }
        if (deleteMap.containsKey("rules")) {
            action.setRules(parseDeleteLabelList(deleteMap.get("rules")));
        }

        return action;
    }

    private List<String> parseDeleteLabelList(Object o) throws EVRException {
        List<String> labels = new ArrayList<>();
        if (o == null) {
            return labels;
        }

        List list = getObject(o, List.class);
        for (Object listObj : list) {
            String label = getObject(listObj, String.class);
            labels.add(label);
        }

        return labels;
    }

    private List<GrantAction> parseDeleteAssociations(Object o) throws EVRException {
        List<GrantAction> associations = new ArrayList<>();
        if (o == null) {
            return associations;
        }

        List list = getObject(o, List.class);
        for (Object listObj : list) {
            GrantAction action = (GrantAction) parseGrantAction(listObj);
            associations.add(action);
        }

        return associations;
    }

    private List<EvrNode> parseDeleteNodes(Object o) throws EVRException {
        List<EvrNode> nodes = new ArrayList<>();
        if (o == null) {
            return nodes;
        }

        List list = getObject(o, List.class);
        for (Object listObj : list) {
            Map listObjMap = getObject(listObj, Map.class);
            EvrNode evrNode = parseEvrNode(listObjMap);
            nodes.add(evrNode);
        }

        return nodes;
    }

}
