package gov.nist.csd.pm.core.pap.pml.function.basic.builtin;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.op.arg.NodeIdFormalParameter;
import gov.nist.csd.pm.core.pap.pml.function.basic.PMLBasicFunction;
import java.util.List;

public class Name extends PMLBasicFunction {

    public static final NodeIdFormalParameter NODE_PARAM =
        new NodeIdFormalParameter("id");

    public Name() {
        super(
            "name",
            STRING_TYPE,
            List.of(NODE_PARAM)
        );
    }

    @Override
    public String execute(PAP pap, Args args) throws PMException {
        long id = args.get(NODE_PARAM);
        return pap.query().graph().getNodeById(id).getName();
    }
}
