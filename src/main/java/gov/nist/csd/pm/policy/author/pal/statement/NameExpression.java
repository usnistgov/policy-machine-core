package gov.nist.csd.pm.policy.author.pal.statement;

import gov.nist.csd.pm.policy.author.PolicyAuthor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.compiler.Variable;
import gov.nist.csd.pm.policy.author.pal.compiler.visitor.FunctionCallVisitor;
import gov.nist.csd.pm.policy.author.pal.compiler.visitor.VariableReferenceVisitor;
import gov.nist.csd.pm.policy.author.pal.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.model.expression.VariableReference;
import gov.nist.csd.pm.policy.author.pal.model.scope.UnknownFunctionInScopeException;
import gov.nist.csd.pm.policy.author.pal.model.scope.UnknownVariableInScopeException;
import gov.nist.csd.pm.policy.exceptions.PMException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class NameExpression extends PALStatement {

    private VariableReference varRef;
    private FunctionStatement functionCall;
    private List<NameExpression> names;

    private boolean create;

    public NameExpression(VariableReference varRef) {
        this.varRef = varRef;
    }

    public NameExpression(FunctionStatement functionCall) {
        this.functionCall = functionCall;
    }

    public NameExpression(NameExpression... name) {
        this.names = Arrays.asList(name);
    }

    public NameExpression() {

    }

    public NameExpression(List<NameExpression> names) {
        this.names = names;
    }

    public boolean isVarRef() {
        return varRef != null;
    }

    public boolean isFuncCall() {
        return functionCall != null;
    }

    public VariableReference getVarRef() {
        return varRef;
    }

    public FunctionStatement getFunctionCall() {
        return functionCall;
    }

    public boolean isCollection() {
        return names != null;
    }

    public List<NameExpression> getNames() {
        return names;
    }

    public boolean isCreate() {
        return create;
    }

    public void setCreate(boolean init) {
        this.create = init;
    }

    /**
     * Initialize the NameExpression as a constant in the compiler scope. This method assumes the passed NameContext
     * is a VariableReference using an ID (not a map reference).
     *
     * This is intended to be used by only the PAL statements that CREATE an entity
     *   - nodes
     *   - prohibitions
     *   - obligations
     *   - rules
     *
     * @param visitorCtx the compiler scope
     * @param nameCtx the antlr context for the name expression
     * @return a compiled NameExpression
     */
    /*public static NameExpression compileAndCreate(VisitorContext visitorCtx,
                                                  PALParser.NameExpressionContext nameCtx) {
        boolean create = false;
        if (nameCtx.varRef() != null) {
            try {
                String var = nameCtx.varRef().getText();

                if (!visitorCtx.scope().variableExists(var)) {
                    if (nameCtx.varRef() instanceof PALParser.ReferenceByIDContext) {
                        create = true;
                        visitorCtx.scope().addVariable(var, Type.string(), true);
                    }
                } else {
                    // check if variable is of type string
                    Type type = visitorCtx.scope().getVariable(var).type();
                    if (!type.isString()) {
                        visitorCtx.errorLog().addError(nameCtx, String.format("expected string got %s", type));
                    }
                }
            } catch (PMException e) {
                visitorCtx.errorLog().addError(nameCtx, e.getMessage());
            }
        }

        NameExpression nameExpression = compile(visitorCtx, nameCtx);
        nameExpression.setCreate(create);
        return nameExpression;
    }*/

    /**
     * Compile an antlr NameContext into a NameExpression.
     *
     * @param visitorCtx the compiler scope
     * @param nameCtx the antlr context for the name expression
     * @return a compiled NameExpression
     */
    public static NameExpression compile(VisitorContext visitorCtx,
                                         PALParser.NameExpressionContext nameCtx) {
        NameAndType nameAndType = new NameAndType();
        try {
            nameAndType = getNameAndType(visitorCtx, nameCtx);
        } catch (UnknownFunctionInScopeException e) {
            visitorCtx.errorLog().addError(nameCtx, e.getMessage());
        }

        if (!(nameAndType.type.isString() || nameAndType.type.isAny())) {
            visitorCtx.errorLog().addError(
                    nameCtx,
                    "name type " + nameAndType.type + " not allowed, only [string]"
            );
        }

        return nameAndType.name;
    }

    /**
     * Compile an antlr NameArrayContext into a NameExpression which will hold a list of NameExpressions.
     *
     * @param visitorCtx the compiler scope
     * @param nameArrayCtx the antlr context for the name expression
     * @return a compiled NameExpression
     */
    public static NameExpression compileArray(VisitorContext visitorCtx,
                                                    PALParser.NameExpressionArrayContext nameArrayCtx) {
        List<PALParser.NameExpressionContext> nameCtxs = nameArrayCtx.nameExpression();
        List<NameAndType> nameAndTypes = new ArrayList<>();
        for (PALParser.NameExpressionContext nameCtx : nameCtxs) {
            try {
                nameAndTypes.add(getNameAndType(visitorCtx, nameCtx));
            } catch (UnknownFunctionInScopeException e) {
                visitorCtx.errorLog().addError(nameArrayCtx, e.getMessage());
                return new NameExpression();
            }

        }

        // if there are more than one names they all need to be strings
        NameExpression compiled = new NameExpression();
        if (nameAndTypes.size() > 1) {
            for (NameAndType nameAndType : nameAndTypes) {
                if (!nameAndType.type.isString()) {
                    visitorCtx.errorLog().addError(nameArrayCtx, "expected list of strings or a single array variable or function call");
                }
            }

            List<NameExpression> arrayNames = new ArrayList<>();
            for (NameAndType nameAndType : nameAndTypes) {
                arrayNames.add(nameAndType.name);
            }

            compiled = new NameExpression(arrayNames);
        } else if (nameAndTypes.size() == 1){
            // if there is only one name then it needs to be a string or a string array variable or a function that returns a string array
            NameAndType nameAndType = nameAndTypes.get(0);
            if (!(nameAndType.type.isString() || (nameAndType.type.isArray() && nameAndType.type.getArrayType().isString()))) {
                visitorCtx.errorLog().addError(nameArrayCtx, "expected a string or a variable or function that returns a string array");
            }

            compiled = nameAndType.name;
        }

        return compiled;
    }

    private static NameAndType getNameAndType(VisitorContext visitorCtx,
                                              PALParser.NameExpressionContext nameCtx) throws UnknownFunctionInScopeException {
        NameExpression name;
        Type type;
        if (nameCtx.funcCall() != null) {
            FunctionStatement functionCall = new FunctionCallVisitor(visitorCtx)
                    .visitFuncCall(nameCtx.funcCall());
            name = new NameExpression(functionCall);

            FunctionDefinitionStatement function = visitorCtx.scope().getFunction(functionCall.getFunctionName());

            type = function.getReturnType();
        } else {
            if (nameCtx.varRef() instanceof PALParser.MapEntryReferenceContext) {
                VariableReference variableReference = new VariableReferenceVisitor(visitorCtx)
                        .visitVarRef(nameCtx.varRef());
                name = new NameExpression(variableReference);
                type = variableReference.getType();
            } else {
                String id = nameCtx.varRef().getText();
                try {
                    Variable variable = visitorCtx.scope().getVariable(id);
                    name = new NameExpression(new VariableReference(id, variable.type()));
                    type = variable.type();
                } catch (UnknownVariableInScopeException e) {
                    // it's ok if the variable doesn't exist it will be considered a literal id
                    name = new NameExpression(new VariableReference(id, Type.string()));
                    name.setCreate(true);
                    type = Type.string();
                }
            }
        }

        return new NameAndType(name, type);
    }

    private static class NameAndType {
        private final NameExpression name;
        private final Type type;

        public NameAndType() {
            this.name = new NameExpression();
            this.type = Type.any();
        }

        public NameAndType(NameExpression name, Type type) {
            this.name = name;
            this.type = type;
        }
    }

    @Override
    public Value execute(ExecutionContext ctx, PolicyAuthor policyAuthor) throws PMException {
        if (isVarRef()) {
            if (isCreate()) {
                ctx.scope().addValue(varRef.getID(), new Value(varRef.getID()));
            }
            return varRef.execute(ctx, policyAuthor);
        } else if (isFuncCall()){
            return functionCall.execute(ctx, policyAuthor);
        } else {
            List<Value> values = new ArrayList<>();
            for (NameExpression name : names) {
                values.add(name.execute(ctx, policyAuthor));
            }

            return new Value(values.toArray(Value[]::new));
        }
    }

    @Override
    public String toString() {
        if (isVarRef()) {
            return varRef.toString();
        } else if (isFuncCall()){
            return functionCall.toString();
        } else {
            StringBuilder s = new StringBuilder();
            for (NameExpression n : names) {
                if (s.length() > 0) {
                    s.append(", ");
                }

                s.append(n.toString());
            }

            return s.toString();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NameExpression that = (NameExpression) o;
        return create == that.create && Objects.equals(varRef, that.varRef) && Objects.equals(functionCall, that.functionCall) && Objects.equals(names, that.names);
    }

    @Override
    public int hashCode() {
        return Objects.hash(varRef, functionCall, names, create);
    }
}
