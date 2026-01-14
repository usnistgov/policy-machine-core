package gov.nist.csd.pm.core.pap.pml.function.basic.builtin;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.NODE_TYPE;
import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.NameNodeArg;
import gov.nist.csd.pm.core.pap.function.arg.NodeArg;
import gov.nist.csd.pm.core.pap.pml.function.basic.PMLBasicFunction;
import java.util.List;

public class Node extends PMLBasicFunction {

    public static final FormalParameter<String> NAME_PARAM =
        new FormalParameter<>("name", STRING_TYPE);

    public Node() {
        super(
            "node",
            STRING_TYPE,
            List.of(NAME_PARAM)
        );
    }

    @Override
    public NodeArg<?> execute(PAP pap, Args args) throws PMException {
        String name = args.get(NAME_PARAM);
        return new NameNodeArg(name);
    }
}
