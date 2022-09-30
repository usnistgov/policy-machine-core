package gov.nist.csd.pm.policy.author.pal.model.context;

import gov.nist.csd.pm.policy.author.pal.compiler.error.ErrorLog;
import gov.nist.csd.pm.policy.author.pal.model.scope.Scope;

import java.util.Objects;

public record VisitorContext(Scope scope, ErrorLog errorLog) {

    public VisitorContext copy() {
        // want to persist the error tracker
        return new VisitorContext(scope.copy(), this.errorLog);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VisitorContext that = (VisitorContext) o;
        return Objects.equals(scope, that.scope) && Objects.equals(errorLog, that.errorLog);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scope, errorLog);
    }
}
