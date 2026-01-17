package gov.nist.csd.pm.core.pap.pml.statement.operation;

import static gov.nist.csd.pm.core.pap.function.Operation.NAME_PARAM;
import static gov.nist.csd.pm.core.pap.function.op.obligation.ObligationOp.AUTHOR_PARAM;
import static gov.nist.csd.pm.core.pap.function.op.obligation.ObligationOp.EVENT_PATTERN_PARAM;
import static gov.nist.csd.pm.core.pap.function.op.obligation.ObligationOp.OBLIGATION_RESPONSE_PARAM;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.op.obligation.CreateObligationOp;
import gov.nist.csd.pm.core.pap.obligation.event.EventPattern;
import gov.nist.csd.pm.core.pap.obligation.Obligation;
import gov.nist.csd.pm.core.pap.obligation.event.operation.OperationPattern;
import gov.nist.csd.pm.core.pap.obligation.event.subject.SubjectPattern;
import gov.nist.csd.pm.core.pap.obligation.response.ObligationResponse;
import gov.nist.csd.pm.core.pap.obligation.response.PMLObligationResponse;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatementBlock;
import java.util.Objects;

public class CreateObligationStatement extends OperationStatement {

    private final Expression<String> name;
    private final EventPattern eventPattern;
    private final PMLObligationResponse response;

    public CreateObligationStatement(Expression<String> name,
                                     EventPattern eventPattern,
                                     PMLObligationResponse response) {
        super(new CreateObligationOp());
        this.name = name;
        this.eventPattern = eventPattern;
        this.response = response;
    }

    public Expression<String> getName() {
        return name;
    }

    public EventPattern getEventPattern() {
        return eventPattern;
    }

    public PMLObligationResponse getResponse() {
        return response;
    }

    @Override
    public Args prepareArgs(ExecutionContext ctx, PAP pap) throws PMException {
        String nameStr = name.execute(ctx, pap);

        return new Args()
            .put(AUTHOR_PARAM, ctx.author().getUser())
            .put(NAME_PARAM, nameStr)
            .put(EVENT_PATTERN_PARAM, eventPattern)
            .put(OBLIGATION_RESPONSE_PARAM, response);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CreateObligationStatement that)) {
            return false;
        }

        return Objects.equals(name, that.name) && Objects.equals(eventPattern, that.eventPattern)
            && Objects.equals(response, that.response);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, eventPattern, response);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        PMLStatementBlock block = new PMLStatementBlock(response.getStatements());

        return String.format(
            """
            create obligation %s
            when %s
            performs %s
            do (%s) %s""",
            name,
            subjectPatternToString(indentLevel, eventPattern.getSubjectPattern()),
            operationPatternToString(indentLevel, eventPattern.getOperationPattern()),
            response.getEventCtxVariable(), block.toFormattedString(indentLevel)
        );
    }

    private String subjectPatternToString(int indentLevel, SubjectPattern subjectPattern) {
        return subjectPattern.toFormattedString(indentLevel);
    }

    private String operationPatternToString(int indentLevel, OperationPattern operationPattern) {
        return operationPattern.toFormattedString(indentLevel);
    }

    public static CreateObligationStatement fromObligation(Obligation obligation) {
        EventPattern event = obligation.getEventPattern();
        ObligationResponse response = obligation.getResponse();
        if (!(response instanceof PMLObligationResponse pmlObligationResponse)) {
            throw new IllegalStateException("cannot convert obligation " + obligation.getName() + " to PML because it does not have a PMLObligationResponse response");
        }

        return new CreateObligationStatement(
            new StringLiteralExpression(obligation.getName()),
            new EventPattern(event.getSubjectPattern(), event.getOperationPattern()),
            new PMLObligationResponse(
                pmlObligationResponse.getEventCtxVariable(),
                pmlObligationResponse.getStatements()
            )
        );
    }
}
