package gov.nist.csd.pm.policy.pml.model.context;

import gov.nist.csd.pm.policy.pml.compiler.error.ErrorLog;
import gov.nist.csd.pm.policy.pml.model.scope.Scope;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ListTokenSource;
import org.antlr.v4.runtime.TokenStream;

import java.util.List;
import java.util.Objects;

public record VisitorContext(CommonTokenStream tokens, Scope scope, ErrorLog errorLog) {

    public VisitorContext() {
        this(new CommonTokenStream(new ListTokenSource(List.of())), new Scope(Scope.Mode.COMPILE), new ErrorLog());
    }

    public VisitorContext copy() {
        // want to persist the error tracker and tokens
        return new VisitorContext(this.tokens, scope.copy(), this.errorLog);
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
