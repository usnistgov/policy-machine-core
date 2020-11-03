package gov.nist.csd.pm.pip.obligations.evr;

import gov.nist.csd.pm.pip.obligations.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The EventParser for Admin/Access events.
 */
public class PmEventParser implements EventParser {

    @Override
    public String key() {
        // system events are handled in the parser and don't need a key
        return "";
    }

    /**
     * subject: object if omitted any subject matches
     * policyClass: object if omitted any policyClass matches
     * operations: array if omitted any operation matches
     * target: object if omitted any target matches
     */
    @Override
    public EventPattern parse(Map map) throws EVRException {
        EventPattern eventPattern = new EventPattern();
        if(map.containsKey("subject")) {
            Subject subject = parseSubject(map.get("subject"));
            eventPattern.setSubject(subject);
        }

        if(map.containsKey("policyClass")) {
            PolicyClass policyClass = parsePolicyClass(map.get("policyClass"));
            eventPattern.setPolicyClass(policyClass);
        }

        if(map.containsKey("operations")) {
            List<String> operations = parseOperations(map.get("operations"));
            eventPattern.setOperations(operations);
        }

        if(map.containsKey("target")) {
            Target target = parseTarget(map.get("target"));
            eventPattern.setTarget(target);
        }

        return eventPattern;
    }

    /**
     * subject:
     *   user:
     *   anyUser:
     *   process:
     */
    private Subject parseSubject(Object o) throws EVRException {
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

        throw new EVRException("invalid subject specification " + map);
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
    private Target parseTarget(Object o) throws EVRException {
        Target target = new Target();
        if(o == null) {
            return target;
        }

        Map map = EVRParser.getObject(o, Map.class);
        if(map.containsKey("containers")){
            target.setContainers(parseContainers(EVRParser.getObject(map.get("containers"), List.class)));
        } else if (map.containsKey("policyElements")) {
            target.setPolicyElements(parsePolicyElements(EVRParser.getObject(map.get("policyElements"), List.class)));
        }

        return target;
    }

    private List<EvrNode> parseContainers(List contList) throws EVRException {
        List<EvrNode> containers = new ArrayList<>();
        for(Object conObj : contList) {
            Map conMap = EVRParser.getObject(conObj, Map.class);
            String name = EVRParser.getObject(conMap.get("name"), String.class);
            String type = EVRParser.getObject(conMap.get("type"), String.class);
            Map propsMap = new HashMap();
            if(conMap.containsKey("properties")) {
                propsMap = EVRParser.getObject(conMap.get("properties"), Map.class);
            }
            Map<String, String> properties = new HashMap<>();
            for(Object propObj : propsMap.keySet()) {
                String value = EVRParser.getObject(propsMap.get(propObj), String.class);
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
    private List<EvrNode> parsePolicyElements(List list) throws EVRException {
        List<EvrNode> policyElements = new ArrayList<>();

        if(list == null) {
            return policyElements;
        }

        // check that each element in the array is a string
        for(Object l : list) {
            policyElements.add(EVRParser.parseEvrNode(EVRParser.getObject(l, Map.class)));
        }

        return policyElements;
    }

    /**
     * operations:
     *   - ""
     *   - ""
     */
    private List<String> parseOperations(Object o) throws EVRException {
        if(o == null) {
            return new ArrayList<>();
        }

        List opsList = EVRParser.getObject(o, List.class);
        List<String> operations = new ArrayList<>();
        for(Object op : opsList) {
            operations.add(EVRParser.getObject(op, String.class));
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
    private PolicyClass parsePolicyClass(Object o) throws EVRException {
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
            List list = EVRParser.getObject(map.get("anyOf"), List.class);
            for(Object obj : list) {
                pcs.add((String) obj);
            }
            policyClass.setAnyOf(pcs);
        } else if(map.containsKey("eachOf")) {
            List<String> pcs = new ArrayList<>();
            List list = EVRParser.getObject(map.get("eachOf"), List.class);
            for(Object obj : list) {
                pcs.add((String) obj);
            }
            policyClass.setEachOf(pcs);
        }

        return policyClass;
    }


    private Subject parseProcess(Object o) throws EVRException {
        String process = EVRParser.getObject(o, String.class);
        return new Subject(new EvrProcess(process));
    }

    private Subject parseSubjectAnyUser(Object o) throws EVRException {
        List<String> anyUser = new ArrayList<>();
        Subject subject = new Subject(anyUser);
        if (o == null) {
            return subject;
        }

        List list = EVRParser.getObject(o, List.class);
        for(Object obj : list) {
            anyUser.add(EVRParser.getObject(obj, String.class));
        }

        return subject;
    }

    private Subject parseSubjectUser(Object o) throws EVRException {
        return new Subject(EVRParser.getObject(o, String.class));
    }
}
