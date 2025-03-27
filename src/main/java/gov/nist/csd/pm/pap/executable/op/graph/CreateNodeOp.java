package gov.nist.csd.pm.pap.executable.op.graph;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.executable.arg.ActualArgs;
import gov.nist.csd.pm.pap.executable.arg.FormalArg;
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
    
    public ActualArgs actualArgs(String name) {
        ActualArgs actualArgs = new ActualArgs();
        actualArgs.put(NAME_ARG, name);
        return actualArgs;
    }

    public ActualArgs actualArgs(String name, LongArrayList descendants) {
        ActualArgs actualArgs = new ActualArgs();
        actualArgs.put(NAME_ARG, name);
        actualArgs.put(DESCENDANTS_ARG, descendants);
        return actualArgs;
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, ActualArgs actualArgs) throws PMException {
        LongArrayList descendants = actualArgs.get(DESCENDANTS_ARG);
        for (Long nodeId : descendants) {
            privilegeChecker.check(userCtx, nodeId, ar);
        }
    }
}
