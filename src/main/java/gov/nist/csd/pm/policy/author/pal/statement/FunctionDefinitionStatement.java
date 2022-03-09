package gov.nist.csd.pm.policy.author.pal.statement;

import gov.nist.csd.pm.policy.author.pal.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.model.function.FormalArgument;
import gov.nist.csd.pm.policy.author.pal.model.function.FunctionExecutor;
import gov.nist.csd.pm.policy.author.PolicyAuthor;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FunctionDefinitionStatement extends PALStatement {

    public static String name(String name) {
        return name;
    }

    public static Type returns(Type type) {
        return type;
    }

    public static List<FormalArgument> args(FormalArgument ... formalArguments) {
        return Arrays.stream(formalArguments).toList();
    }

    private final String functionName;
    private final Type returnType;
    private final List<FormalArgument> args;
    private List<PALStatement> body;
    private FunctionExecutor functionExecutor;
    private boolean isFuncExec;

    public FunctionDefinitionStatement(String functionName, Type returnType, List<FormalArgument> args, List<PALStatement> body) {
        this.functionName = functionName;
        this.returnType = returnType;
        this.args = args;
        this.body = body;
    }

    public FunctionDefinitionStatement(String functionName, Type returnType,
                                       List<FormalArgument> args, FunctionExecutor executor) {
        this.functionName = functionName;
        this.returnType = returnType;
        this.args = args;
        this.functionExecutor = executor;
        this.isFuncExec = true;
    }

    public boolean isFunctionExecutor() {
        return isFuncExec;
    }

    public FunctionExecutor getFunctionExecutor() {
        return functionExecutor;
    }

    public String getFunctionName() {
        return functionName;
    }

    public Type getReturnType() {
        return returnType;
    }

    public List<FormalArgument> getArgs() {
        return args;
    }

    public List<PALStatement> getBody() {
        return body;
    }

    @Override
    public Value execute(ExecutionContext ctx, PolicyAuthor policyAuthor) {
        ctx.addFunction(this);

        return new Value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FunctionDefinitionStatement that = (FunctionDefinitionStatement) o;
        return isFuncExec == that.isFuncExec && Objects.equals(functionName, that.functionName) && Objects.equals(returnType, that.returnType) && Objects.equals(args, that.args) && Objects.equals(body, that.body) && Objects.equals(functionExecutor, that.functionExecutor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(functionName, returnType, args, body, functionExecutor, isFuncExec);
    }
}
