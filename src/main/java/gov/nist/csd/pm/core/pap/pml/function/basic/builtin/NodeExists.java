package gov.nist.csd.pm.core.pap.pml.function.basic.builtin;


import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.BOOLEAN_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.pml.function.basic.PMLBasicFunction;
import java.util.List;


public class NodeExists extends PMLBasicFunction {

    public NodeExists() {
        super(
                "nodeExists",
                BOOLEAN_TYPE,
                List.of(NODE_NAME_PARAM)
        );
    }

    @Override
    public Object execute(PAP pap, Args args) throws PMException {
        String value = args.get(NODE_NAME_PARAM);
        return pap.query().graph().nodeExists(value);
    }
}
