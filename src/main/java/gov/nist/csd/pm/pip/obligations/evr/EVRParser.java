package gov.nist.csd.pm.pip.obligations.evr;

import gov.nist.csd.pm.pip.obligations.model.*;
import gov.nist.csd.pm.pip.obligations.model.actions.*;
import gov.nist.csd.pm.pip.obligations.model.functions.Arg;
import gov.nist.csd.pm.pip.obligations.model.functions.Function;
import org.yaml.snakeyaml.Yaml;


import java.io.InputStream;
import java.util.*;
import java.util.logging.Logger;

public class EVRParser {

    private static Logger logger = Logger.getLogger(EVRParser.class.getName());

    private static <T> T getObject(Object o, Class<T> type) throws EVRException {
        logger.info(String.format("getting object %s of type %s", o, type));
        if(!type.isInstance(o)) {
            throw new EVRException("expected " + type + " got " + o.getClass() + " at \"" + o + "\"");
        }

        return type.cast(o);
    }

    /**
     * label: string required
     * rules: array
     */
    public static Obligation parse(InputStream is) throws EVRException {
        Yaml yaml = new Yaml();
        Map<Object, Object> map = yaml.load(is);

        Obligation obligation = new Obligation();

        String label = getObject(map.get("label"), String.class);
        if (label == null) {
            throw new EVRException("no label specified for obligation");
        }
        obligation.setLabel(label);

        if(map.containsKey("rules")) {
            List rulesList = getObject(map.get("rules"), List.class);
            List<Rule> rules = new ArrayList<>();

            for(Object rule : rulesList) {
                rules.add(parseRule(rule));
            }

            obligation.setRules(rules);
        }

        return obligation;
    }

    /**
     * label: string required
     * event: object required
     * response: object required
     */
    public static Rule parseRule(Object o) throws EVRException {
        if(!(o instanceof Map)) {
            throw new EVRException("rule should be a map, got " + o.getClass() + " in " + o);
        }

        Map map = (Map)o;
        Rule rule = new Rule();

        String label = getObject(map.get("label"), String.class);
        if (label == null) {
            throw new EVRException("no label provided for rule " + o);
        }
        rule.setLabel(label);

        if(!map.containsKey("event")) {
            throw new EVRException("no event provided at " + o);
        }
        Event event = parseEvent(map.get("event"));
        rule.setEvent(event);

        if(!map.containsKey("response")) {
            throw new EVRException("no response provided at " + o);
        }
        Response response = parseResponse(map.get("response"));
        rule.setResponse(response);

        return rule;
    }

    /**
     * subject: object if omitted any subject matches
     * policyClass: object if omitted any policyClass matches
     * operations: array if omitted any operation matches
     * target: object if omitted any target matches
     */
    protected static Event parseEvent(Object o) throws EVRException {
        if(!(o instanceof Map)) {
            throw new EVRException("event should be a Map, got " + o.getClass() + " in " + o);
        }

        Map map = (Map)o;
        Event event = new Event();
        if(map.containsKey("subject")) {
            Subject subject = parseSubject(map.get("subject"));
            event.setSubject(subject);
        }

        if(map.containsKey("policyClass")) {
            PolicyClass policyClass = parsePolicyClass(map.get("policyClass"));
            event.setPolicyClass(policyClass);
        }

        if(map.containsKey("operations")) {
            List<String> operations = parseOperations(map.get("operations"));
            event.setOperations(operations);
        }

        if(map.containsKey("target")) {
            Target target = parseTarget(map.get("target"));
            event.setTarget(target);
        }

        return event;
    }

    /**
     * subject:
     *   user:
     *   anyUser:
     *   process:
     */
    protected static Subject parseSubject(Object o) throws EVRException {
        // a null map means any subject
        if(o == null) {
            return new Subject();
        } else if(!(o instanceof Map)) {
            throw new EVRException("event subject should be a Map, got " + o.getClass());
        }

        Map map = (Map)o;
        if(map.size() != 1) {
            throw new EVRException("only one element is expected for an event subject, got " + map);
        }

        if(map.containsKey("user")) {
            return parseSubjectUser(map.get("user"));
        } else if(map.containsKey("anyUser")) {
            return parseSubjectAnyUser(map.get("anyUser"));
        } else if(map.containsKey("process")) {
            return parseProcess(map.get("process"));
        }

        throw new EVRException("invalid subject specification");
    }

    /**
     * 1. A specific policy element
     *    policyElements:
     *      - name: name
     *        type: type
     *
     * 2. Any policy element
     *    policyElements: or omit
     *
     * 3. Any policy element that is contained in other policy elements
     *    policyElements: or omit
     *    containers:
     *      -
     *      -
     *
     * 4. Any policy element from a set of policy elements
     *    policyElements:
     *      - name: name
     *        type: type
     *      - name: name
     *        type: type
     *
     * policyElements: array if omitted any policy element will match
     * ---
     * containers: array if omitted any container will match
     *
     * ony one of containers or policyElements is allowed
     * if both are omitted it will be "any policyElement in any container"
     * if containers is present then it will be "any policyElement in the containers",
     *   regardless of if policyElements is present
     * if policyElements is present its "any policyElement from the list provided"
     */
    protected static Target parseTarget(Object o) throws EVRException {
        Target target = new Target();
        if(o == null) {
            return target;
        }

        Map map = getObject(o, Map.class);
        if(map.containsKey("containers")){
            target.setContainers(parseContainers(getObject(map.get("containers"), List.class)));
        } else if (map.containsKey("policyElements")) {
            target.setPolicyElements(parsePolicyElements(getObject(map.get("policyElements"), List.class)));
        }

        return target;
    }

    private static List<EvrNode> parseContainers(List contList) throws EVRException {
        List<EvrNode> containers = new ArrayList<>();
        for(Object conObj : contList) {
            Map conMap = getObject(conObj, Map.class);
            String name = getObject(conMap.get("name"), String.class);
            String type = getObject(conMap.get("type"), String.class);
            Map propsMap = new HashMap();
            if(conMap.containsKey("properties")) {
                propsMap = getObject(conMap.get("properties"), Map.class);
            }
            Map<String, String> properties = new HashMap<>();
            for(Object propObj : propsMap.keySet()) {
                String value = getObject(propsMap.get(propObj), String.class);
                properties.put((String) propObj, value);
            }

            containers.add(new EvrNode(name, type, properties));
        }

        return containers;
    }

    /**
     * policyElements:
     *   - name:
     *     type:
     *   - name:
     *     type:
     */
    private static List<EvrNode> parsePolicyElements(List list) throws EVRException {
        List<EvrNode> policyElements = new ArrayList<>();

        if(list == null) {
            return policyElements;
        }

        // check that each element in the array is a string
        for(Object l : list) {
            policyElements.add(parseEvrNode(getObject(l, Map.class)));
        }

        return policyElements;
    }

    /**
     * operations:
     *   - ""
     *   - ""
     */
    protected static List<String> parseOperations(Object o) throws EVRException {
        if(o == null) {
            return new ArrayList<>();
        }

        List opsList = getObject(o, List.class);
        List<String> operations = new ArrayList<>();
        for(Object op : opsList) {
            operations.add(getObject(op, String.class));
        }

        return operations;
    }

    /**
     * policyClass:
     *   anyOf:
     *   ---
     *   eachOf:
     *
     * One of anyOf/eachOf is allowed, both are arrays of string
     */
    protected static PolicyClass parsePolicyClass(Object o) throws EVRException {
        PolicyClass policyClass = new PolicyClass();
        if(o == null) {
            return policyClass;
        }

        Map map = (Map)o;
        if(map.size() > 1) {
            throw new EVRException("expected one of (anyOf, eachOf), got " + map.keySet());
        }

        if(map.containsKey("anyOf")) {
            List<String> pcs = new ArrayList<>();
            List list = getObject(map.get("anyOf"), List.class);
            for(Object obj : list) {
                pcs.add((String) obj);
            }
            policyClass.setAnyOf(pcs);
        } else if(map.containsKey("eachOf")) {
            List<String> pcs = new ArrayList<>();
            List list = getObject(map.get("eachOf"), List.class);
            for(Object obj : list) {
                pcs.add((String) obj);
            }
            policyClass.setEachOf(pcs);
        }

        return policyClass;
    }


    private static Subject parseProcess(Object o) throws EVRException {
        Long process = getObject(o, Long.class);
        return new Subject(new EvrProcess(process));
    }

    private static Subject parseSubjectAnyUser(Object o) throws EVRException {
        List list = getObject(o, List.class);
        List<String> anyUser = new ArrayList<>();
        for(Object obj : list) {
            anyUser.add(getObject(obj, String.class));
        }

        return new Subject(anyUser);
    }

    private static Subject parseSubjectUser(Object o) throws EVRException {
        return new Subject(getObject(o, String.class));
    }

    /**
     * actions:
     */
    protected static Response parseResponse(Object o) throws EVRException {
        Response response = new Response();
        if(o == null) {
            return response;
        }

        Map responseMap = getObject(o, Map.class);
        if(responseMap.containsKey("condition")) {
            response.setCondition(parseCondition(responseMap.get("condition")));
        }
        if(responseMap.containsKey("actions")) {
            List actionsList = getObject(responseMap.get("actions"), List.class);

            for(Object a : actionsList) {
                Map actionMap = getObject(a, Map.class);
                response.addAction(parseAction(actionMap));
            }
        }

        return response;
    }

    /**
     * condition:
     *   - function:
     *   - function:
     *
     *   if all functions evaluate to true the condition is true
     */
    private static Condition parseCondition(Object o) throws EVRException {
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

    private static Action parseAction(Map map) throws EVRException {
        Condition condition = null;
        if (map.containsKey("condition")) {
            condition = parseCondition(map.get("condition"));
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
        }

        if(action == null) {
            throw new EVRException("invalid action received " + map);
        }

        if(condition != null) {
            action.setCondition(condition);
        }

        return action;
    }

    private static EvrNode parseEvrNode(Map map) throws EVRException {
        if(map == null) {
            throw new EVRException("null EVR node found");
        }

        if(map.containsKey("function")) {
            System.out.println("found function: " + map.get("function"));
            Map funcMap = getObject(map.get("function"), Map.class);
            return new EvrNode(parseFunction(funcMap));
        } else {
            String name = (String) map.get("name");
            if (name == null || name.isEmpty()) {
                throw new EVRException("name cannot be null in create action at " + map);
            }

            String type = (String) map.get("type");
            if (type == null || type.isEmpty()) {
                throw new EVRException("type cannot be null in create action at " + map);
            }

            Object propsObj = map.get("properties");
            Map<String, String> properties = new HashMap<>();
            if(propsObj != null) {
                Map propsMap = getObject(propsObj, Map.class);
                for(Object p : propsMap.keySet()) {
                    properties.put(getObject(p, String.class), getObject(propsMap.get(p), String.class));
                }
            }

            return new EvrNode(name, type, properties);
        }
    }

    /**
     * create:
     *   what: a list of nodes instead of just one
     *     - name:
     *       type:
     *       properties:
     *     --
     *     - function:
     *   where:
     *     - name:
     *       type:
     *     - function:
     *   ---
     *   rule:
     */
    private static Action parseCreateAction(Object o) throws EVRException {
        if(o == null) {
            throw new EVRException("create action cannot be null or empty");
        }

        CreateAction action = new CreateAction();
        Map createActionMap = getObject(o, Map.class);
        if(createActionMap.containsKey("rule")) {
            action.setRule(parseRule(createActionMap.get("rule")));
        } else {
            if (!createActionMap.containsKey("what")) {
                throw new EVRException("create action does not have a \"what\" field in " + createActionMap);
            }

            List whatList = getObject(createActionMap.get("what"), List.class);
            List<EvrNode> whatNodes = new ArrayList<>();
            for(Object whatObj : whatList) {
                EvrNode node = parseEvrNode(getObject(whatObj, Map.class));
                whatNodes.add(node);
            }
            action.setWhat(whatNodes);

            if (createActionMap.containsKey("where")) {
                List whereList = getObject(createActionMap.get("where"), List.class);
                for (Object whereObj : whereList) {
                    Map contMap = getObject(whereObj, Map.class);
                    action.addWhere(parseEvrNode(contMap));
                }
            }
        }

        return action;
    }

    /**
     * The assign action behaves the same as the create action
     *
     * assign:
     *   what: a list of nodes instead of just one
     *   where:
     *
     *
     * the "like" element described in the standard is not implemented
     */
    private static Action parseAssignAction(Object o) throws EVRException {
        if(o == null) {
            throw new EVRException("assign action cannot be null or empty");
        }

        Map assignActionMap = getObject(o, Map.class);
        if(!assignActionMap.containsKey("what")) {
            throw new EVRException("create action does not have a \"what\" field in " + assignActionMap);
        } else if(!assignActionMap.containsKey("where")) {
            throw new EVRException("create action does not have a \"where\" field in " + assignActionMap);
        }

        AssignAction action = new AssignAction();

        List whatList = getObject(assignActionMap.get("what"), List.class);
        List<EvrNode> whatNodes = new ArrayList<>();
        for(Object whatObj : whatList) {
            EvrNode node = parseEvrNode(getObject(whatObj, Map.class));
            whatNodes.add(node);
        }
        action.setWhat(whatNodes);

        List whereList = getObject(assignActionMap.get("where"), List.class);
        for(Object whereObj : whereList) {
            Map contMap = getObject(whereObj, Map.class);
            action.addWhere(parseEvrNode(contMap));
        }

        return action;
    }

    /**
     * grant:
     *   subjects:
     *     - name:
     *       type:
     *     - function:
     *   operations:
     *     - ""
     *     - ""
     *   targets:
     *     - name:
     *       type:
     *     - function:
     *
     *     operations can be ommitted if deleting a grant
     */
    private static Action parseGrantAction(Object o) throws EVRException {
        if(o == null) {
            throw new EVRException("grant action cannot be null or empty");
        }

        Map grantActionMap = getObject(o, Map.class);
        if(!grantActionMap.containsKey("subjects")) {
            throw new EVRException("grant action does not contain a \"subjects\" field in " + grantActionMap);
        } else if(!grantActionMap.containsKey("targets")) {
            throw new EVRException("grant action does not contain a \"targets\" field in " + grantActionMap);
        }

        GrantAction action = new GrantAction();

        List subjectsList = getObject(grantActionMap.get("subjects"), List.class);
        List<EvrNode> subjects = parseSubjects(subjectsList);
        action.setSubjects(subjects);

        if(grantActionMap.containsKey("operations")) {
            List opsList = getObject(grantActionMap.get("operations"), List.class);
            List<String> operations = new ArrayList<>();
            for (Object opObj : opsList) {
                operations.add(getObject(opObj, String.class));
            }
            action.setOperations(operations);
        }

        List targetsList = getObject(grantActionMap.get("targets"), List.class);
        List<EvrNode> targets = parseTargets(targetsList);
        action.setTargets(targets);

        return action;
    }

    private static List<EvrNode> parseSubjects(List list) throws EVRException {
        List<EvrNode> subjects = new ArrayList<>();
        for(Object o : list) {
            Map oMap = getObject(o, Map.class);
            subjects.add(parseEvrNode(oMap));
        }
        return subjects;
    }

    private static List<EvrNode> parseTargets(List list) throws EVRException {
        List<EvrNode> targets = new ArrayList<>();
        for(Object o : list) {
            Map oMap = getObject(o, Map.class);
            targets.add(parseEvrNode(oMap));
        }
        return targets;
    }

    /**
     * deny:
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
    private static Action parseDenyAction(Object o) throws EVRException {
        if(o == null) {
            throw new EVRException("deny action cannot be null or empty");
        }

        Map denyActionMap = getObject(o, Map.class);
        if(!denyActionMap.containsKey("subject")) {
            throw new EVRException("deny action does not contain a \"subject\" field in " + denyActionMap);
        } else if(!denyActionMap.containsKey("operations")) {
            throw new EVRException("deny action does not contain a \"operations\" field in " + denyActionMap);
        } else if(!denyActionMap.containsKey("target")) {
            throw new EVRException("deny action does not contain a \"target\" field in " + denyActionMap);
        }

        DenyAction action = new DenyAction();

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

    private static DenyAction.Target parseDenyActionTarget(Object o) throws EVRException {
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

    private static List<DenyAction.Target.Container> parseDenyActionTargetContainers(List list) throws EVRException {
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
     *   create:
     *   assign:
     *   grant:
     *   deny:
     */
    private static Action parseDeleteAction(Object o) throws EVRException {
        if(o == null) {
            throw new EVRException("delete action cannot be null or empty");
        }

        DeleteAction action = new DeleteAction();
        Map deleteMap = getObject(o, Map.class);
        if(deleteMap.containsKey("create")) {
            action.setAction(parseCreateAction(deleteMap.get("create")));
        } else if(deleteMap.containsKey("assign")) {
            action.setAction(parseAssignAction(deleteMap.get("assign")));
        } else if(deleteMap.containsKey("grant")) {
            action.setAction(parseGrantAction(deleteMap.get("grant")));
        } else if(deleteMap.containsKey("deny")) {
            action.setAction(parseDenyAction(deleteMap.get("deny")));
        }

        return action;
    }

    private static List<String> parseRules(Object o) throws EVRException {
        List list = getObject(o, List.class);
        List<String> rules = new ArrayList<>();
        for(Object obj : list) {
            rules.add(getObject(obj, String.class));
        }
        return rules;
    }

    private static Function parseFunction(Map funcMap) throws EVRException {
        String funcName = getObject(funcMap.get("name"), String.class);

        List<Arg> argList = new ArrayList<>();
        if(funcMap.containsKey("args")) {
            List funcArgList = getObject(funcMap.get("args"), List.class);
            for (Object l : funcArgList) {
                if (l instanceof String) {
                    argList.add(new Arg(getObject(l, String.class)));
                } else if (l instanceof Map) {
                    Map map = getObject(l, Map.class);
                    if (map.containsKey("function")) {
                        argList.add(new Arg(parseFunction(getObject(map.get("function"), Map.class))));
                    }
                }
                else {
                    throw new EVRException("invalid function definition " + funcMap);
                }
            }
        }

        return new Function(funcName, argList);
    }
}
