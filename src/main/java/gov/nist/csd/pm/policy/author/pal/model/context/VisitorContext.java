package gov.nist.csd.pm.policy.author.pal.model.context;

import gov.nist.csd.pm.policy.author.pal.compiler.error.ErrorLog;
import gov.nist.csd.pm.policy.author.pal.compiler.Variable;
import gov.nist.csd.pm.policy.author.pal.compiler.VisitorScope;

import java.util.Objects;

public record VisitorContext(VisitorScope scope, ErrorLog errorLog) {

    public VisitorContext copy() {
        VisitorScope copyScope = new VisitorScope(errorLog);
        for (String varName : this.scope.getVariables().keySet()) {
            Variable var = this.scope.getVariable(varName);
            copyScope.addVariable(varName, var.type(), var.isConst());
        }
        for (String function : this.scope.getFunctions().keySet()) {
            copyScope.addFunction(this.scope.getFunction(function));
        }

        // want to persist the error tracker
        return new VisitorContext(copyScope, this.errorLog);
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
