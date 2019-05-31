package gov.nist.csd.pm.epp;

import gov.nist.csd.pm.epp.events.*;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;
import gov.nist.csd.pm.pip.obligations.evr.EVRException;
import gov.nist.csd.pm.pip.obligations.model.functions.Arg;
import gov.nist.csd.pm.pip.obligations.model.functions.Function;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;

import java.util.*;

class FunctionEvaluator {

    private FunctionEvaluator() {}

    public static Object evalFunc(EventContext eventCtx, long userID, long processID, PDP pdp, Function function) throws PMException {
        Object ret;

        switch (function.getName()) {
            case "current_process":
            case "process_default":
                ret = eval_current_process(processID);
                break;
            case "child_of_assign":
                ret = eval_child_of_assign(eventCtx);
                break;
            case "parent_of_assign":
                ret = eval_parent_of_assign(eventCtx);
                break;
            case "child_of_assign_name":
                ret = eval_child_of_assign_name(eventCtx);
                break;
            case "current_user":
                ret = eval_current_user(pdp, userID);
                break;
            case "current_user_name":
                ret = eval_current_user_name(pdp, userID);
                break;
            case "current_target":
                ret = eval_current_target(eventCtx);
                break;
            case "create_node":
                ret = eval_create_node(eventCtx, userID, processID, pdp, function);
                break;
            case "get_node":
                ret = eval_get_node(eventCtx, userID, processID, pdp, function);
                break;
            case "to_props":
                ret = eval_to_props(function.getArgs());
                break;
            case "get_children_of":
                ret = eval_get_children_of(eventCtx, userID, processID, pdp, function);
                break;
            case "is_node_contained_in":
                ret = eval_is_node_contained_in(eventCtx, userID, processID, pdp, function);
                break;
            default:
                throw new PMException("no such function '" + function.getName() + "'");
        }

        return ret;
    }

    private static boolean eval_is_node_contained_in(EventContext eventCtx, long userID, long processID, PDP pdp, Function function) throws PMException {
        List<Arg> args = function.getArgs();
        Arg arg = args.get(0);
        Function f = arg.getFunction();
        List list = (List) evalFunc(eventCtx, userID, processID, pdp, f);
        if(list.isEmpty()) {
            return false;
        }
        Node childNode = (Node) list.get(0);

        arg = args.get(1);
        f = arg.getFunction();
        list = (List) evalFunc(eventCtx, userID, processID, pdp, f);
        if(list.isEmpty()) {
            return false;
        }
        Node parentNode = (Node) list.get(0);

        return pdp.getPAP().getGraphPAP().getChildren(parentNode.getID()).contains(childNode.getID());
    }

    private static List<Node> eval_get_children_of(EventContext eventCtx, long userID, long processID, PDP pdp, Function function) throws PMException {
        Node node = eval_get_node(eventCtx, userID, processID, pdp, function).iterator().next();
        Set<Long> children = pdp.getPAP().getGraphPAP().getChildren(node.getID());
        List<Node> nodes = new ArrayList<>();
        for(Long l : children) {
            nodes.add(pdp.getPAP().getGraphPAP().getNode(l));
        }
        return nodes;
    }

    private static Node eval_create_node(EventContext eventCtx, long userID, long processID, PDP pdp, Function function) throws PMException {
        List<Arg> args = function.getArgs();

        // first arg is the name, can be function
        Arg nameArg = args.get(0);
        String name = nameArg.getValue();
        if(nameArg.getFunction() != null) {
            name = (String) evalFunc(eventCtx, userID, processID, pdp, nameArg.getFunction());
        }

        // second arg is the type, can be function
        Arg typeArg = args.get(1);
        String type = typeArg.getValue();
        if(typeArg.getFunction() != null) {
            type = (String) evalFunc(eventCtx, userID, processID, pdp, typeArg.getFunction());
        }

        // third arg is the properties which is a map that has tom come from a function
        Map<String, String> props = new HashMap<>();
        if(args.size() > 2) {
            Arg propsArg = args.get(2);
            if (propsArg.getFunction() != null) {
                props = (Map) evalFunc(eventCtx, userID, processID, pdp, propsArg.getFunction());
            }
        }

        System.out.println("creating node " + name + " " + type + " " + props);

        long id = new Random().nextLong();
        Graph graph = pdp.getPAP().getGraphPAP();
        return graph.createNode(id, name, NodeType.toNodeType(type), props);
    }

    private static List<Node> eval_get_node(EventContext eventCtx, long userID, long processID, PDP pdp, Function function) throws PMException {
        List<Arg> args = function.getArgs();

        // first arg is name
        Arg arg = args.get(0);
        String name = arg.getValue();
        if(arg.getFunction() != null) {
            name = (String) evalFunc(eventCtx, userID, processID, pdp, arg.getFunction());
        }

        // second arg is type
        arg = args.get(1);
        String type = arg.getValue();
        if(arg.getFunction() != null) {
            type = (String) evalFunc(eventCtx, userID, processID, pdp, arg.getFunction());
        }

        // third arg is properties
        Map<String, String> props = new HashMap<>();
        if(args.size() > 2) {
            arg = args.get(2);
            if (arg.getFunction() != null) {
                props = (Map) evalFunc(eventCtx, userID, processID, pdp, arg.getFunction());
            }
        }

        return new ArrayList<>(pdp.getPAP().getGraphPAP().search(name, type, props));
    }

    /*
    properties are formatted:
    - "key1=value1"
    - "key2=value2"
     */
    private static Map<String, String> eval_to_props(List<Arg> args) {
        Map<String, String> props = new HashMap<>();
        for(Arg arg : args) {
            String value = arg.getValue();
            String[] tokens = value.split(":");
            if(tokens.length == 2) {
                props.put(tokens[0], tokens[1]);
            }
        }
        return props;
    }

    private static List<Node> eval_current_target(EventContext eventCtx) {
        return Arrays.asList(eventCtx.getTarget());
    }

    private static String eval_convert_node_to_type(EventContext eventCtx, long userID, long processID, PDP pdp, Function function) throws PMException {
        List<Arg> args = function.getArgs();

        // first arg should be a string or a function tht returns a string
        Arg arg = args.get(0);
        String name = arg.getValue();
        if(arg.getFunction() != null) {
            name = (String) evalFunc(eventCtx, userID, processID, pdp, function);
        }

        // second arg should be the type of the node to search for
        arg = args.get(1);
        String type = arg.getValue();
        if(arg.getFunction() != null) {
            type = (String) evalFunc(eventCtx, userID, processID, pdp, function);
        }

        Set<Node> search = pdp.getPAP().getGraphPAP().search(name, type, null);
        return search.iterator().next().getName();
    }

    private static Prohibition.Subject eval_current_process(long processID) {
        return new Prohibition.Subject(processID, Prohibition.Subject.Type.PROCESS);
    }

    private static List<Node> eval_current_user(PDP pdp, long userID) throws PMException {
        return Arrays.asList(pdp.getPAP().getGraphPAP().getNode(userID));
    }

    private static String eval_current_user_name(PDP pdp, long userID) throws PMException {
        return pdp.getPAP().getGraphPAP().getNode(userID).getName();
    }

    private static List<Node> eval_child_of_assign(EventContext eventCtx) throws EVRException {
        Node child;
        if(eventCtx instanceof AssignToEvent) {
            child = ((AssignToEvent) eventCtx).getChildNode();
        } else if (eventCtx instanceof AssignEvent) {
            child = eventCtx.getTarget();
        } else if (eventCtx instanceof DeassignFromEvent) {
            child = ((DeassignFromEvent) eventCtx).getChildNode();
        } else if (eventCtx instanceof DeassignEvent) {
            child = eventCtx.getTarget();
        } else {
            throw new EVRException("invalid event context for function child_of_assign. Valid event contexts are AssignTo, " +
                    "Assign, DeassignFrom, and Deassign");
        }

        return Arrays.asList(child);
    }

    private static List<Node> eval_parent_of_assign(EventContext eventCtx) throws EVRException {
        Node parent;
        if(eventCtx instanceof AssignToEvent) {
            parent = ((AssignToEvent) eventCtx).getChildNode();
        } else if (eventCtx instanceof AssignEvent) {
            parent = eventCtx.getTarget();
        } else if (eventCtx instanceof DeassignFromEvent) {
            parent = ((DeassignFromEvent) eventCtx).getChildNode();
        } else if (eventCtx instanceof DeassignEvent) {
            parent = eventCtx.getTarget();
        } else {
            throw new EVRException("invalid event context for function parent_of_assign. Valid event contexts are AssignTo, " +
                    "Assign, DeassignFrom, and Deassign");
        }

        return Arrays.asList(parent);
    }

    private static String eval_child_of_assign_name(EventContext eventCtx) throws EVRException {
        List<Node> result = eval_child_of_assign(eventCtx);
        return result.get(0).getName();
    }
}
