/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

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
 * Compile-time context threaded through the PML compiler visitors.
 *
 * @param tokens the token stream being compiled
 * @param scope the current compile scope
 * @param errorLog the log to record compile errors to
 * @param pmlErrorHandler the listener that records syntax errors
 */
public record VisitorContext(CommonTokenStream tokens, Scope<Variable, PMLOperationSignature> scope, ErrorLog errorLog, PMLErrorHandler pmlErrorHandler) {

    public VisitorContext(CompileScope scope) {
        this(new CommonTokenStream(new ListTokenSource(List.of())), scope, new ErrorLog(), new PMLErrorHandler());
    }

    /**
     * Returns a new context with a full copy of this context's scope, for compiling a nested block.
     */
    public VisitorContext copy() {
        // want to persist the error tracker and tokens
        return new VisitorContext(this.tokens, scope.copy(), this.errorLog, this.pmlErrorHandler);
    }

    /**
     * Returns a new context whose scope contains only this scope's function and query operations.
     */
    public VisitorContext copyFunctionsAndQueriesOnly() {
        return new VisitorContext(this.tokens, scope.copyFunctionsAndQueriesOnly(), this.errorLog, this.pmlErrorHandler);
    }

    /**
     * Returns a new context whose scope contains only this scope's function operations.
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
