package gov.nist.csd.pm.pap.pml.function;

import gov.nist.csd.pm.pap.pml.function.arg.PMLFormalArg;
import gov.nist.csd.pm.pap.pml.function.arg.WrappedFormalArg;
import java.util.List;

public interface PMLFunctionWrapper {

    List<WrappedFormalArg<?>> getPMLFormalArgs();

}
