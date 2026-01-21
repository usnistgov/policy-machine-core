package gov.nist.csd.pm.core.pap.pml.context;

import gov.nist.csd.pm.core.pap.pml.PMLErrorHandler;
import gov.nist.csd.pm.core.pap.pml.compiler.Variable;
import gov.nist.csd.pm.core.pap.pml.compiler.error.ErrorLog;
import gov.nist.csd.pm.core.pap.pml.operation.PMLOperationSignature;
import gov.nist.csd.pm.core.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.core.pap.pml.scope.Scope;
import java.util.List;
import java.util.Objects;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ListTokenSource;

public record VisitorContext(CommonTokenStream tokens, Scope<Variable, PMLOperationSignature> scope, ErrorLog errorLog, PMLErrorHandler pmlErrorHandler) {

    public VisitorContext(CompileScope scope) {
        this(new CommonTokenStream(new ListTokenSource(List.of())), scope, new ErrorLog(), new PMLErrorHandler());
    }

    public VisitorContext copy() {
        // want to persist the error tracker and tokens
        return new VisitorContext(this.tokens, scope.copy(), this.errorLog, this.pmlErrorHandler);
    }

    public VisitorContext copyFunctionsAndQueriesOnly() {
        return new VisitorContext(this.tokens, scope.copyFunctionsAndQueriesOnly(), this.errorLog, this.pmlErrorHandler);
    }

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
