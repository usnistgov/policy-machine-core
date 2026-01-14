package gov.nist.csd.pm.core.pap.pml.function.basic.builtin;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.NODE_TYPE;
import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.NodeArg;
import gov.nist.csd.pm.core.pap.pml.function.basic.PMLBasicFunction;
import java.util.List;

public class Name extends PMLBasicFunction {

    public static final FormalParameter<NodeArg<?>> NODE_PARAM =
        new FormalParameter<>("node", NODE_TYPE);

    public Name() {
        super(
            "name",
            STRING_TYPE,
            List.of(NODE_PARAM)
        );
    }

    @Override
    public String execute(PAP pap, Args args) throws PMException {
        NodeArg<?> nodeArg = args.get(NODE_PARAM);
        return nodeArg.getName(pap);
    }
}
