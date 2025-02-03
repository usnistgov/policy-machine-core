package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.obligation.EventPattern;
import gov.nist.csd.pm.common.obligation.Response;
import gov.nist.csd.pm.common.obligation.Rule;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.pattern.OperationPattern;
import gov.nist.csd.pm.pap.pml.pattern.operand.OperandPatternExpression;
import gov.nist.csd.pm.pap.pml.pattern.subject.SubjectPattern;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.pap.pml.statement.PMLStatementBlock;
import gov.nist.csd.pm.pap.pml.value.RuleValue;
import gov.nist.csd.pm.pap.pml.value.Value;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class CreateRuleStatement implements PMLStatement {

    protected Expression name;
    protected SubjectPattern subjectPattern;
    protected OperationPattern operationPattern;
    protected Map<String, List<OperandPatternExpression>> operandPattern;
    protected ResponseBlock responseBlock;

    public CreateRuleStatement(Expression name,
                               SubjectPattern subjectPattern,
                               OperationPattern operationPattern,
                               Map<String, List<OperandPatternExpression>> operandPattern,
                               ResponseBlock responseBlock) {
        this.name = name;
        this.subjectPattern = subjectPattern;
        this.operationPattern = operationPattern;
        this.operandPattern = operandPattern;
        this.responseBlock = responseBlock;
    }

    public Expression getName() {
        return name;
    }

    public void setName(Expression name) {
        this.name = name;
    }

    public SubjectPattern getSubjectPattern() {
        return subjectPattern;
    }

    public void setSubjectPattern(SubjectPattern subjectPattern) {
        this.subjectPattern = subjectPattern;
    }

    public OperationPattern getOperationPattern() {
        return operationPattern;
    }

    public void setOperationPattern(OperationPattern operationPattern) {
        this.operationPattern = operationPattern;
    }

    public Map<String, List<OperandPatternExpression>> getOperandPattern() {
        return operandPattern;
    }

    public void setOperandPattern(Map<String, List<OperandPatternExpression>> operandPattern) {
        this.operandPattern = operandPattern;
    }

    public ResponseBlock getResponseBlock() {
        return responseBlock;
    }

    public void setResponseBlock(ResponseBlock responseBlock) {
        this.responseBlock = responseBlock;
    }

    @Override
    public Value execute(ExecutionContext ctx, PAP pap) throws PMException {
        String nameValue = name.execute(ctx, pap).getStringValue();

        return new RuleValue(new Rule(
                nameValue,
                new EventPattern(
                        subjectPattern,
                        operationPattern,
                        new HashMap<>(operandPattern)
                ),
                new Response(responseBlock.evtVar, responseBlock.getStatements())
        ));
    }

    @Override
    public String toFormattedString(int indentLevel) {
        PMLStatementBlock block = new PMLStatementBlock(responseBlock.statements);

        String indent = indent(indentLevel);

        String operandsStr = "";
        for (Map.Entry<String, List<OperandPatternExpression>> operandExpr : operandPattern.entrySet()) {
            if (!operandsStr.isEmpty()) {
                operandsStr += ",\n";
            }

            List<OperandPatternExpression> value = operandExpr.getValue();

            operandsStr += indent(indentLevel+1) +
                     operandExpr.getKey() + ": " + (value.size() == 1 ? value.getFirst() : value);
        }
        operandsStr = operandPattern.isEmpty() ? "" : indent + "on {\n" + operandsStr + "\n" + indent + "}";

        return String.format(
                """
                %screate rule %s
                %swhen %s
                %sperforms %s
                %s
                %sdo (%s) %s""",
                indent, name,
                indent, subjectPattern,
                indent, operationPattern.isAny() ? operationPattern.toString() : "\"" + operationPattern.toString() + "\"",
                operandsStr,
                indent, responseBlock.evtVar, block.toFormattedString(indentLevel)
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CreateRuleStatement that = (CreateRuleStatement) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(subjectPattern, that.subjectPattern) &&
                Objects.equals(operationPattern, that.operationPattern) &&
                Objects.equals(operandPattern, that.operandPattern) &&
                Objects.equals(responseBlock, that.responseBlock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, subjectPattern, operationPattern, operandPattern, responseBlock);
    }

    public static class ResponseBlock implements Serializable {
        protected String evtVar;
        protected List<PMLStatement> statements;

        public ResponseBlock(String evtVar, List<PMLStatement> statements) {
            this.evtVar = evtVar;
            this.statements = statements;
        }

        public String getEvtVar() {
            return evtVar;
        }

        public List<PMLStatement> getStatements() {
            return statements;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ResponseBlock that = (ResponseBlock) o;
            return Objects.equals(evtVar, that.evtVar) && Objects.equals(statements, that.statements);
        }

        @Override
        public int hashCode() {
            return Objects.hash(evtVar, statements);
        }
    }

}