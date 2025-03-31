package gov.nist.csd.pm.pap.function.op.graph;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.arg.FormalArg;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import it.unimi.dsi.fastutil.longs.LongArrayList;

import java.util.List;

public abstract class CreateNodeOp extends GraphOp<Long> {

    private final String ar;

    public CreateNodeOp(String name, String ar) {
        super(
                name,
                List.of(NAME_ARG, DESCENDANTS_ARG)
        );

        this.ar = ar;
    }

    public CreateNodeOp(String name, List<FormalArg<?>> formalArgs, String ar) {
        super(
                name,
                formalArgs
        );

        this.ar = ar;
    }
    
    public Args actualArgs(String name) {
        Args args = new Args();
        args.put(NAME_ARG, name);
        return args;
    }

    public Args actualArgs(String name, List<Long> descendants) {
        Args args = new Args();
        args.put(NAME_ARG, name);
        args.put(DESCENDANTS_ARG, descendants);
        return args;
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Args args) throws PMException {
        List<Long> descendants = args.get(DESCENDANTS_ARG);
        for (Long nodeId : descendants) {
            privilegeChecker.check(userCtx, nodeId, ar);
        }
    }
}
