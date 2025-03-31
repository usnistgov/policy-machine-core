package gov.nist.csd.pm.pap.pml.function.arg;

import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.pml.value.Value;
import java.util.HashMap;
import java.util.Map;

public class PMLArgs extends Args {

    public PMLArgs() {
        super();
    }

    public PMLArgs(Map<String, Value> map) {
        super();

        if (map == null) {
            map = new HashMap<>();
        }

        map.forEach((k, v) -> {
            PMLFormalArg pmlFormalArg = new PMLFormalArg(k, v.getType());
            this.put(pmlFormalArg, v);
        });
    }
}
