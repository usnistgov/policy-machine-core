package gov.nist.csd.pm.pap.pml.context;

import gov.nist.csd.pm.pap.pml.PMLErrorHandler;
import gov.nist.csd.pm.pap.pml.compiler.Variable;
import gov.nist.csd.pm.pap.pml.compiler.error.ErrorLog;
import gov.nist.csd.pm.pap.pml.exception.PMLExecutionException;
import gov.nist.csd.pm.pap.pml.executable.PMLExecutableSignature;
import gov.nist.csd.pm.pap.pml.scope.GlobalScope;
import gov.nist.csd.pm.pap.pml.scope.Scope;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ListTokenSource;

import java.util.List;
import java.util.Objects;

public final class VisitorContext {
    private final CommonTokenStream tokens;
    private final Scope<Variable, PMLExecutableSignature> scope;
    private final ErrorLog errorLog;
    private final PMLErrorHandler pmlErrorHandler;

    public VisitorContext(CommonTokenStream tokens, Scope<Variable, PMLExecutableSignature> scope, ErrorLog errorLog, PMLErrorHandler pmlErrorHandler) {
        this.tokens = tokens;
        this.scope = scope;
        this.errorLog = errorLog;
        this.pmlErrorHandler = pmlErrorHandler;
    }

    public VisitorContext(Scope<Variable, PMLExecutableSignature> scope) {
        this(new CommonTokenStream(new ListTokenSource(List.of())), scope, new ErrorLog(), new PMLErrorHandler());
    }

    public VisitorContext(GlobalScope<Variable, PMLExecutableSignature> globalScope) {
        this(new CommonTokenStream(new ListTokenSource(List.of())), new Scope<>(globalScope), new ErrorLog(), new PMLErrorHandler());
    }

    public VisitorContext copy() {
        // want to persist the error tracker and tokens
        return new VisitorContext(this.tokens, scope.copy(), this.errorLog, this.pmlErrorHandler);
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

    public CommonTokenStream tokens() {
        return tokens;
    }

    public Scope<Variable, PMLExecutableSignature> scope() {
        return scope;
    }

    public ErrorLog errorLog() {
        return errorLog;
    }

    public PMLErrorHandler pmlErrorHandler() {
        return pmlErrorHandler;
    }

    @Override
    public String toString() {
        return "VisitorContext[" +
                "tokens=" + tokens + ", " +
                "scope=" + scope + ", " +
                "errorLog=" + errorLog + ", " +
                "pmlErrorHandler=" + pmlErrorHandler + ']';
    }

}
