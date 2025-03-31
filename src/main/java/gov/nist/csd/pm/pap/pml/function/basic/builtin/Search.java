package gov.nist.csd.pm.pap.pml.function.basic.builtin;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.pml.function.arg.PMLFormalArg;
import gov.nist.csd.pm.pap.pml.function.basic.PMLBasicFunction;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.ArrayValue;
import gov.nist.csd.pm.pap.pml.value.Value;

import java.util.*;

public class Search extends PMLBasicFunction {

    public static final PMLFormalArg TYPE_ARG = new PMLFormalArg("type", Type.string());
    public static final PMLFormalArg PROPERTIES_ARG = new PMLFormalArg("properties", Type.map(Type.string(), Type.string()));


    public Search() {
        super(
                "search",
                Type.array(Type.string()),
                List.of(TYPE_ARG, PROPERTIES_ARG)
        );
    }

    @Override
    public Value execute(PAP pap, Args args) throws PMException {
        NodeType nodeType = NodeType.toNodeType(args.get(TYPE_ARG).getStringValue());

        Map<Value, Value> propertiesValue = args.get(PROPERTIES_ARG).getMapValue();

        Map<String, String> properties = new HashMap<>();
        for (Map.Entry<Value, Value> prop : propertiesValue.entrySet()) {
            properties.put(prop.getKey().getStringValue(), prop.getValue().getStringValue());
        }

        Collection<Node> search = pap.query().graph().search(nodeType, properties);

        List<Value> ret = new ArrayList<>(search.size());
        for (Node n : search) {
            ret.add(Value.fromObject(n));
        }

        return new ArrayValue(ret, Type.array(Type.any()));
    }
}
