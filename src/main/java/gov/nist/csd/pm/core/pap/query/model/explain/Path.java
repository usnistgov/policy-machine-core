package gov.nist.csd.pm.core.pap.query.model.explain;

import com.google.gson.GsonBuilder;
import gov.nist.csd.pm.core.common.graph.node.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Path extends ArrayList<Node> {

    public Path(int initialCapacity) {
        super(initialCapacity);
    }

    public Path() {
    }

    public Path(Node... p) {
        this(new ArrayList<>(List.of(p)));
    }

    public Path(Collection<Node> c) {
        super(c);
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}
