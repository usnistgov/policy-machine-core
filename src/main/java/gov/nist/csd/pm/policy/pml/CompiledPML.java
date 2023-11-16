package gov.nist.csd.pm.policy.pml;

import gov.nist.csd.pm.policy.pml.compiler.Variable;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.function.FunctionSignature;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record CompiledPML(Map<String, Expression> constants, Map<String, FunctionDefinitionStatement> functions, List<PMLStatement> stmts) {

    public CompiledPML() {
        this(new HashMap<>(), new HashMap<>(), new ArrayList<>());
    }

}
