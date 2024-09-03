package gov.nist.csd.pm.pap.pml;

import gov.nist.csd.pm.pap.pml.executable.PMLExecutableSignature;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;

import java.util.*;

public final class CompiledPML {
    private final Map<String, PMLExecutableSignature> executables;
    private final List<PMLStatement> stmts;

    public CompiledPML(Map<String, PMLExecutableSignature> executables, List<PMLStatement> stmts) {
        this.executables = executables;
        this.stmts = stmts;
    }

    public Map<String, PMLExecutableSignature> executables() {
        return executables;
    }

    public List<PMLStatement> stmts() {
        return stmts;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (CompiledPML) obj;
        return Objects.equals(this.executables, that.executables) &&
                Objects.equals(this.stmts, that.stmts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(executables, stmts);
    }

    @Override
    public String toString() {
        return "CompiledPML[" +
                "executables=" + executables + ", " +
                "stmts=" + stmts + ']';
    }


}
