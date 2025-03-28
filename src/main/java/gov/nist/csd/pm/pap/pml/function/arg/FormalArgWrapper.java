package gov.nist.csd.pm.pap.pml.function.arg;

import gov.nist.csd.pm.pap.function.arg.FormalArg;
import gov.nist.csd.pm.pap.pml.type.Type;
import java.util.ArrayList;
import java.util.List;

public class FormalArgWrapper {

    public static List<PMLFormalArg> wrap(List<FormalArg<?>> formalArgs) {
        List<PMLFormalArg> wrappedFormalArgs = new ArrayList<>();
        for (FormalArg<?> formalArg : formalArgs) {
            wrappedFormalArgs.add(new PMLFormalArg(formalArg.getName(), Type.any()));
        }

        return wrappedFormalArgs;
    }

}
