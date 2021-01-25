package gov.nist.csd.pm.pip.obligations.evr;

import gov.nist.csd.pm.pip.obligations.model.*;
import gov.nist.csd.pm.pip.obligations.model.functions.Arg;
import gov.nist.csd.pm.pip.obligations.model.functions.Function;
import org.yaml.snakeyaml.Yaml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EVRParser {

    public static <T> T getObject(Object o, Class<T> type) throws EVRException {
        if(!type.isInstance(o)) {
            throw new EVRException("expected " + type + " got " + o.getClass() + " at \"" + o + "\"");
        }

        return type.cast(o);
    }

    public static EvrNode parseEvrNode(Map map) throws EVRException {
        if(map == null) {
            throw new EVRException("null EVR node found");
        }

        if(map.containsKey("function")) {
            Map funcMap = getObject(map.get("function"), Map.class);
            return new EvrNode(parseFunction(funcMap));
        } else {
            String name = (String) map.get("name");
            if (name == null || name.isEmpty()) {
                throw new EVRException("name cannot be null at " + map);
            }

            String type = (String) map.get("type");
            if (type == null || type.isEmpty()) {
                throw new EVRException("type cannot be null at " + map);
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

    public static Function parseFunction(Map funcMap) throws EVRException {
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

    protected static Rule parseRule(Map map, List<EventParser> customEventParsers, List<ResponseParser> customResponseParsers) throws EVRException {
        return new EVRParser(customEventParsers, customResponseParsers).parseRule(map);
    }

    private EventParser systemEventParser = new PmEventParser();
    private ResponseParser systemResponseParser;
    private Map<String, EventParser> customEventParsers;
    private Map<String, ResponseParser> customResponseParsers;

    public EVRParser(List<EventParser> customEventParsers, List<ResponseParser> customResponseParsers) {
        setCustomEventParsers(customEventParsers);
        setCustomResponseParsers(customResponseParsers);

        this.systemResponseParser = new PmResponseParser(customEventParsers, customResponseParsers);
    }

    public EVRParser() {
        this.customEventParsers = new HashMap<>();
        this.customResponseParsers = new HashMap<>();

        this.systemResponseParser = new PmResponseParser(new ArrayList<>(), new ArrayList<>());
    }

    private void setCustomEventParsers(List<EventParser> customEventParsers) {
        this.customEventParsers = new HashMap<>();

        if (customEventParsers == null) {
            return;
        }

        for (EventParser parser : customEventParsers) {
            if (this.customEventParsers.containsKey(parser.key())) {
                throw new IllegalArgumentException("EventParser duplicate key: " + parser.key());
            }

            this.customEventParsers.put(parser.key(), parser);
        }
    }

    private void setCustomResponseParsers(List<ResponseParser> customResponseParsers) {
        this.customResponseParsers = new HashMap<>();

        if (customResponseParsers == null) {
            return;
        }

        for (ResponseParser parser : customResponseParsers) {
            if (this.customResponseParsers.containsKey(parser.key())) {
                throw new IllegalArgumentException("ResponseParser duplicate key: " + parser.key());
            }

            this.customResponseParsers.put(parser.key(), parser);
        }
    }

    public Obligation parse(String user, String yml) throws EVRException {
        Yaml yaml = new Yaml();
        Map<Object, Object> map = yaml.load(yml);
        Obligation obligation = new Obligation(user);
        obligation.setSource(yml);

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
    public Rule parseRule(Object o) throws EVRException {
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
        EventPattern eventPattern = parseEvent(map.get("event"));
        rule.setEventPattern(eventPattern);

        if(!map.containsKey("response")) {
            throw new EVRException("no response provided at " + o);
        }
        ResponsePattern responsePattern = parseResponse(map.get("response"));
        rule.setResponsePattern(responsePattern);

        return rule;
    }

    private EventPattern parseEvent(Object o) throws EVRException {
        // this map should be
        //   1. system event
        //   2. custom event with the format: key -> custom tags
        Map map = getObject(o, Map.class);
        if (!isPmEvent(map)) {
            return parseCustomEvent(map);
        }

        return systemEventParser.parse(map);
    }

    private EventPattern parseCustomEvent(Map map) throws EVRException {
        // the given map should only have 1 key
        if (map.size() != 1) {
            throw new EVRException("only one key expected in custom event: " + map);
        }

        String key = (String) map.keySet().iterator().next();

        if (!this.customEventParsers.containsKey(key)) {
            throw new EVRException("unregistered custom event key: " + key);
        }

        EventParser parser = this.customEventParsers.get(key);
        return parser.parse(map);
    }

    private boolean isPmEvent(Map map) {
        return map.containsKey("subject") ||
                map.containsKey("operations") ||
                map.containsKey("policyClass") ||
                map.containsKey("target");
    }

    private ResponsePattern parseResponse(Object o) throws EVRException {
        Map map = getObject(o, Map.class);
        if (!isPmResponse(map)) {
            return parseCustomResponse(map);
        }

        return systemResponseParser.parse(map);
    }

    private ResponsePattern parseCustomResponse(Map map) throws EVRException {
        // the given map should only have 1 key
        if (map.size() != 1) {
            throw new EVRException("only one key expected in custom event: " + map);
        }

        String key = (String) map.keySet().iterator().next();

        if (!this.customResponseParsers.containsKey(key)) {
            throw new EVRException("unregistered custom response key: " + key);
        }

        ResponseParser parser = this.customResponseParsers.get(key);
        return parser.parse(map);
    }

    private boolean isPmResponse(Map map) {
        return map.containsKey("condition") ||
                map.containsKey("condition!") ||
                map.containsKey("actions");
    }
}
