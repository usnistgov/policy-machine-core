package gov.nist.csd.pm.policy.model.audit;

import gov.nist.csd.pm.policy.model.access.AccessRightSet;

import java.util.Set;

public class ResolvedPath {
    private String pc;
    private EdgePath path;
    private AccessRightSet ops;

    public ResolvedPath() {

    }

    public ResolvedPath(String pc, EdgePath path, AccessRightSet ops) {
        this.pc = pc;
        this.path = path;
        this.ops = ops;
    }

    public String getPc() {
        return pc;
    }

    public EdgePath getPath() {
        return path;
    }

    public Set<String> getOps() {
        return ops;
    }
}

