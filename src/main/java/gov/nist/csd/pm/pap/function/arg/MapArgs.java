package gov.nist.csd.pm.pap.function.arg;

import java.util.Map;

public class MapArgs extends Args{

    public MapArgs() {
    }

    public MapArgs(Map<FormalParameter<?>, Object> map) {
        super(map);
    }
}
