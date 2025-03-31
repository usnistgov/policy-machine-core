package gov.nist.csd.pm.pap.pml.function.basic.builtin;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.pml.function.basic.PMLBasicFunction;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.BoolValue;
import gov.nist.csd.pm.pap.pml.value.Value;

import java.util.List;


public class NodeExists extends PMLBasicFunction {

    public NodeExists() {
        super(
                "nodeExists",
                Type.bool(),
                List.of(NODE_NAME_ARG)
        );
    }

    @Override
    public Value execute(PAP pap, Args args) throws PMException {
        Value value = args.get(NODE_NAME_ARG);
        return new BoolValue(pap.query().graph().nodeExists(value.getStringValue()));
    }
}
