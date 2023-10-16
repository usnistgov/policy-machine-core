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

public class CompiledPML {

    private Map<String, Expression> constants;
    private Map<String, FunctionDefinitionStatement> functions;
    private List<PMLStatement> stmts;

    public CompiledPML(Map<String, Expression> constants, Map<String, FunctionDefinitionStatement> functions,
                       List<PMLStatement> stmts) {
        this.constants = constants;
        this.functions = functions;
        this.stmts = stmts;
    }

    public CompiledPML() {
        this(new HashMap<>(), new HashMap<>(), new ArrayList<>());
    }

    public Map<String, Expression> constants() {
        return constants;
    }

    public Map<String, FunctionDefinitionStatement> functions() {
        return functions;
    }

    public List<PMLStatement> stmts() {
        return stmts;
    }
}
