package gov.nist.csd.pm.pap.pml.function.basic.builtin;


import static gov.nist.csd.pm.pap.pml.function.basic.builtin.Env.KEY_ARG;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.ActualArgs;
import gov.nist.csd.pm.pap.pml.function.arg.PMLFormalArg;
import gov.nist.csd.pm.pap.pml.function.basic.PMLBasicFunction;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.BoolValue;
import gov.nist.csd.pm.pap.pml.value.Value;

import java.util.List;


public class HasPropertyValue extends PMLBasicFunction {

    public static final PMLFormalArg VALUE_ARG = new PMLFormalArg("value", Type.string());

    public HasPropertyValue() {
        super(
                "hasPropertyValue",
                Type.bool(),
                List.of(NODE_NAME_ARG, KEY_ARG, VALUE_ARG)
        );
    }

    @Override
    public Value execute(PAP pap, ActualArgs actualArgs) throws PMException {
        String nodeName = actualArgs.get(NODE_NAME_ARG).getStringValue();
        String key = actualArgs.get(KEY_ARG).getStringValue();
        String value = actualArgs.get(VALUE_ARG).getStringValue();
        Node node = pap.query().graph().getNodeByName(nodeName);
        boolean has = node.getProperties().containsKey(key);
        if (!has) {
            return new BoolValue(false);
        }

        has = node.getProperties().get(key).equals(value);
        return new BoolValue(has);
    }
}
