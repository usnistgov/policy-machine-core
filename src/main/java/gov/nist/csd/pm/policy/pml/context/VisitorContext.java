package gov.nist.csd.pm.policy.pml.context;

import gov.nist.csd.pm.policy.pml.compiler.Variable;
import gov.nist.csd.pm.policy.pml.compiler.error.ErrorLog;
import gov.nist.csd.pm.policy.pml.function.FunctionSignature;
import gov.nist.csd.pm.policy.pml.scope.GlobalScope;
import gov.nist.csd.pm.policy.pml.scope.Scope;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ListTokenSource;

import java.util.List;
import java.util.Objects;

public record VisitorContext(CommonTokenStream tokens, Scope<Variable, FunctionSignature> scope, ErrorLog errorLog) {

    public VisitorContext(Scope<Variable, FunctionSignature> scope) {
        this(new CommonTokenStream(new ListTokenSource(List.of())), scope, new ErrorLog());
    }

    public VisitorContext(GlobalScope<Variable, FunctionSignature> globalScope) {
        this(new CommonTokenStream(new ListTokenSource(List.of())), new Scope<>(globalScope), new ErrorLog());
    }

    public VisitorContext copy() {
        // want to persist the error tracker and tokens
        return new VisitorContext(this.tokens, scope.copy(), this.errorLog);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VisitorContext that = (VisitorContext) o;
        return Objects.equals(tokens, that.tokens) && Objects.equals(
                scope, that.scope) && Objects.equals(errorLog, that.errorLog);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tokens, scope, errorLog);
    }
}
