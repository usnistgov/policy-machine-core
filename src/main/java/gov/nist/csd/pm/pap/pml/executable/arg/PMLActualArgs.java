package gov.nist.csd.pm.pap.pml.executable.arg;

import gov.nist.csd.pm.pap.executable.arg.ActualArgs;
import gov.nist.csd.pm.pap.executable.arg.FormalArg;
import gov.nist.csd.pm.pap.pml.value.Value;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class PMLActualArgs extends ActualArgs {

    public PMLActualArgs() {
        super();
    }

    public PMLActualArgs(Map<String, Value> map) {
        super();

        if (map == null) {
            map = new HashMap<>();
        }

        map.forEach((k, v) -> this.put(new PMLFormalArg(k, v.getType()), v));
    }
}
