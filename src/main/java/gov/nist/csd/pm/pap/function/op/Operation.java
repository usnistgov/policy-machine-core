package gov.nist.csd.pm.pap.function.op;

import static gov.nist.csd.pm.pap.function.arg.type.SupportedArgTypes.stringType;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.AdminFunction;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.function.arg.FormalArg;
import gov.nist.csd.pm.pap.function.op.arg.IdNodeFormalArg;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;

public abstract class Operation<T> extends AdminFunction<T> {

    public static final FormalArg<String> NAME_ARG = new FormalArg<>("name", stringType());
    public static final IdNodeFormalArg NODE_ARG = new IdNodeFormalArg("node");

    public Operation(String name, List<FormalArg<?>> formalArgs) {
        super(name, formalArgs);
    }

    public abstract void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Args args) throws PMException;

}

