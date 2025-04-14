package gov.nist.csd.pm.pap.function.op;

import static gov.nist.csd.pm.pap.function.arg.type.ArgType.STRING_TYPE;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.AdminFunction;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.op.arg.IdNodeFormalParameter;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;

public abstract class Operation<R, A extends Args> extends AdminFunction<R, A> {

    public static final FormalParameter<String> NAME_PARAM = new FormalParameter<>("name", STRING_TYPE);
    public static final IdNodeFormalParameter NODE_ID_PARAM = new IdNodeFormalParameter("nodeId");

    public Operation(String name, List<FormalParameter<?>> formalParameters) {
        super(name, formalParameters);
    }

    public abstract void canExecute(PAP pap, UserContext userCtx, A args) throws PMException;

}

