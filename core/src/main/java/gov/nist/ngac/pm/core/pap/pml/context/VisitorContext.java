package gov.nist.ngac.pm.core.pap.pml.context;

import gov.nist.ngac.pm.core.pap.pml.PMLErrorHandler;
import gov.nist.ngac.pm.core.pap.pml.compiler.Variable;
import gov.nist.ngac.pm.core.pap.pml.compiler.error.ErrorLog;
import gov.nist.ngac.pm.core.pap.pml.operation.PMLOperationSignature;
import gov.nist.ngac.pm.core.pap.pml.scope.CompileScope;
import gov.nist.ngac.pm.core.pap.pml.scope.Scope;
import java.util.List;
import java.util.Objects;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ListTokenSource;

/**
 * Compile-time context threaded through the PML compiler visitors: the token stream being compiled, the
 * current compile {@link Scope}, and the error log/handler to record failures to.
 *
 * @param tokens the token stream being compiled
 * @param scope the current compile scope
 * @param errorLog the log to record compile errors to
 * @param pmlErrorHandler the ANTLR listener that records lexer/parser syntax errors
 */
public record VisitorContext(CommonTokenStream tokens, Scope<Variable, PMLOperationSignature> scope, ErrorLog errorLog, PMLErrorHandler pmlErrorHandler) {

    public VisitorContext(CompileScope scope) {
        this(new CommonTokenStream(new ListTokenSource(List.of())), scope, new ErrorLog(), new PMLErrorHandler());
    }

    /**
     * Returns a new context with a full copy of this context's scope, sharing the same tokens and error
     * log, for compiling a nested block that should see all of this scope's variables and operations.
     */
    public VisitorContext copy() {
        // want to persist the error tracker and tokens
        return new VisitorContext(this.tokens, scope.copy(), this.errorLog, this.pmlErrorHandler);
    }

    /**
     * Returns a new context whose scope contains only this scope's function and query operations, for
     * compiling a nested block that may call queries but not admin/resource operations.
     */
    public VisitorContext copyFunctionsAndQueriesOnly() {
        return new VisitorContext(this.tokens, scope.copyFunctionsAndQueriesOnly(), this.errorLog, this.pmlErrorHandler);
    }

    /**
     * Returns a new context whose scope contains only this scope's function operations, for compiling a
     * nested block (e.g. a function body) that should not see admin/resource/query operations.
     */
    public VisitorContext copyFunctionsOnly() {
        return new VisitorContext(this.tokens, scope.copyFunctionsOnly(), this.errorLog, this.pmlErrorHandler);
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
