package gov.nist.csd.pm.core.pap.pml.statement.operation;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.obligation.EventPattern;
import gov.nist.csd.pm.core.pap.obligation.ObligationResponse;
import gov.nist.csd.pm.core.pap.obligation.PMLObligationResponse;
import gov.nist.csd.pm.core.pap.obligation.Rule;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.pml.pattern.OperationPattern;
import gov.nist.csd.pm.core.pap.pml.pattern.arg.ArgPatternExpression;
import gov.nist.csd.pm.core.pap.pml.pattern.subject.SubjectPattern;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatementBlock;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CreateRuleStatement extends PMLStatement<Rule> {

    protected Expression<String> name;
    protected SubjectPattern subjectPattern;
    protected OperationPattern operationPattern;
    protected Map<String, List<ArgPatternExpression>> argPattern;
    protected ResponseBlock responseBlock;

    public CreateRuleStatement(Expression<String> name,
                               SubjectPattern subjectPattern,
                               OperationPattern operationPattern,
                               Map<String, List<ArgPatternExpression>> argPattern,
                               ResponseBlock responseBlock) {
        this.name = name;
        this.subjectPattern = subjectPattern;
        this.operationPattern = operationPattern;
        this.argPattern = argPattern;
        this.responseBlock = responseBlock;
    }

    public Expression<String> getName() {
        return name;
    }

    public void setName(Expression<String> name) {
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

    public Map<String, List<ArgPatternExpression>> getArgPattern() {
        return argPattern;
    }

    public void setArgPattern(Map<String, List<ArgPatternExpression>> argPattern) {
        this.argPattern = argPattern;
    }

    public ResponseBlock getResponseBlock() {
        return responseBlock;
    }

    public void setResponseBlock(ResponseBlock responseBlock) {
        this.responseBlock = responseBlock;
    }

    @Override
    public Rule execute(ExecutionContext ctx, PAP pap) throws PMException {
        String nameValue = name.execute(ctx, pap);

        return new Rule(
            nameValue,
            new EventPattern(
                subjectPattern,
                operationPattern,
                new HashMap<>(argPattern)
            ),
            new PMLObligationResponse(responseBlock.evtVar, responseBlock.getStatements())
        );
    }

    @Override
    public String toFormattedString(int indentLevel) {
        PMLStatementBlock block = new PMLStatementBlock(responseBlock.statements);

        String indent = indent(indentLevel);

        String argStr = "";
        for (Map.Entry<String, List<ArgPatternExpression>> argExpr : argPattern.entrySet()) {
            if (!argStr.isEmpty()) {
                argStr += ",\n";
            }

            List<ArgPatternExpression> value = argExpr.getValue();

            argStr += indent(indentLevel+1) +
                argExpr.getKey() + ": " + (value.size() == 1 ? value.getFirst() : value);
        }
        argStr = argPattern.isEmpty() ? "" : indent + "on {\n" + argStr + "\n" + indent + "}";

        return String.format(
            """
            %screate rule %s
            %swhen %s
            %sperforms %s
            %s
            %sdo (%s) %s""",
            indent, name,
            indent, subjectPattern,
            indent, operationPattern,
            argStr,
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
            Objects.equals(argPattern, that.argPattern) &&
            Objects.equals(responseBlock, that.responseBlock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, subjectPattern, operationPattern, argPattern, responseBlock);
    }

    public static class ResponseBlock implements Serializable {
        protected String evtVar;
        protected List<PMLStatement<?>> statements;

        public ResponseBlock(String evtVar, List<PMLStatement<?>> statements) {
            this.evtVar = evtVar;
            this.statements = statements;
        }

        public String getEvtVar() {
            return evtVar;
        }

        public List<PMLStatement<?>> getStatements() {
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
