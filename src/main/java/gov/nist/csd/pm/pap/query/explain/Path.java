package gov.nist.csd.pm.pap.query.explain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Path extends ArrayList<String> {

    public Path(int initialCapacity) {
        super(initialCapacity);
    }

    public Path() {
    }

    public Path(String ... p) {
        this(new ArrayList<>(List.of(p)));
    }

    public Path(Collection<? extends String> c) {
        super(c);
    }
}
