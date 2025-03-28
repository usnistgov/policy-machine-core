package gov.nist.csd.pm.pap.pml.function.builtin;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.ActualArgs;
import gov.nist.csd.pm.pap.pml.function.PMLFunction;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.BoolValue;
import gov.nist.csd.pm.pap.pml.value.Value;

import java.util.List;


public class NodeExists extends PMLFunction {

    public NodeExists() {
        super(
                "nodeExists",
                Type.bool(),
                List.of(NODE_NAME_ARG)
        );
    }

    @Override
    public Value execute(PAP pap, ActualArgs actualArgs) throws PMException {
        return new BoolValue(pap.query().graph().nodeExists(actualArgs.get(NODE_NAME_ARG).getStringValue()));
    }
}
