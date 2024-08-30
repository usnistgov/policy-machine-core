package gov.nist.csd.pm.pap.pml.executable.operation.builtin;


import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.graph.node.NodeType;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.op.PrivilegeChecker;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLOperation;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.ArrayValue;
import gov.nist.csd.pm.pap.pml.value.StringValue;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.query.UserContext;

import java.util.*;

public class Search extends PMLOperation {
    public Search() {
        super(
                "search",
                Type.array(Type.string()),
                List.of("type", "properties"),
                Map.of("type", Type.string(), "properties", Type.map(Type.string(), Type.string()))
        );
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {

    }

    @Override
    public Value execute(PAP pap, Map<String, Object> operands) throws PMException {
        NodeType nodeType = NodeType.toNodeType(((Value) operands.get("type")).getStringValue());

        Map<Value, Value> propertiesValue = ((Value) operands.get("properties")).getMapValue();

        Map<String, String> properties = new HashMap<>();
        for (Map.Entry<Value, Value> prop : propertiesValue.entrySet()) {
            properties.put(prop.getKey().getStringValue(), prop.getValue().getStringValue());
        }

        Collection<String> search = pap.query().graph().search(nodeType, properties);

        List<Value> ret = new ArrayList<>(search.size());
        for (String s : search) {
            ret.add(new StringValue(s));
        }

        return new ArrayValue(ret, Type.array(Type.string()));
    }
}
