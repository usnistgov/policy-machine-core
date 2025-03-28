package gov.nist.csd.pm.pap.pml.function.arg;

import gov.nist.csd.pm.pap.function.arg.ActualArgs;
import gov.nist.csd.pm.pap.pml.value.Value;
import java.util.HashMap;
import java.util.Map;

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
