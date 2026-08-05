package gov.nist.ngac.pm.core.pap.pml.statement.operation;

import static gov.nist.ngac.pm.core.pap.operation.Operation.NAME_PARAM;
import static gov.nist.ngac.pm.core.pap.operation.obligation.CreateObligationOp.AUTHOR_PARAM;
import static gov.nist.ngac.pm.core.pap.operation.obligation.CreateObligationOp.EVENT_PATTERN_PARAM;
import static gov.nist.ngac.pm.core.pap.operation.obligation.CreateObligationOp.OBLIGATION_RESPONSE_PARAM;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.obligation.Obligation;
import gov.nist.ngac.pm.core.pap.obligation.event.EventPattern;
import gov.nist.ngac.pm.core.pap.obligation.event.operation.OperationPattern;
import gov.nist.ngac.pm.core.pap.obligation.event.subject.SubjectPattern;
import gov.nist.ngac.pm.core.pap.obligation.response.ObligationResponse;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.operation.obligation.CreateObligationOp;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.ngac.pm.core.pap.pml.expression.Expression;
import gov.nist.ngac.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.ngac.pm.core.pap.pml.statement.PMLStatementBlock;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;

import java.util.Objects;

/**
 * PML "create obligation" statement: creates an obligation from a name, event pattern, and response body.
 * Can also be built from (via {@link #fromObligation}) or converted back to (via {@link #toObligation})
 * a live {@link Obligation}.
 */
public class CreateObligationStatement extends OperationStatement {

    private final Expression<String> name;
    private final EventPattern eventPattern;
    private final ObligationResponse response;

    public CreateObligationStatement(Expression<String> name,
                                     EventPattern eventPattern,
                                     ObligationResponse response) {
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

    public ObligationResponse getResponse() {
        return response;
    }

    @Override
    public Args prepareArgs(ExecutionContext ctx, PAP pap) throws PMException {
        String nameStr = name.execute(ctx, pap);

        long authorId = ctx.author().resolveNodeIds(pap.query().graph()).iterator().next();

        return new Args()
            .put(AUTHOR_PARAM, authorId)
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
            %s
            %s""",
            name,
            eventPatternToString(indentLevel, eventPattern),
            responseToString(indentLevel, response)
        );
    }

    /**
     * Formats an event pattern's "when ... performs ..." clause for {@link #toFormattedString}.
     *
     * @param indentLevel the indent level to format at
     * @param eventPattern the event pattern to format
     * @return the formatted clause
     */
    public static String eventPatternToString(int indentLevel, EventPattern eventPattern) {
        return String.format("""
            when %s
            performs %s""",
            subjectPatternToString(indentLevel, eventPattern.getSubjectPattern()),
            operationPatternToString(indentLevel, eventPattern.getOperationPattern()));
    }

    /**
     * Formats an obligation response's "do (...) { ... }" clause for {@link #toFormattedString}.
     *
     * @param indentLevel the indent level to format at
     * @param obligationResponse the response to format
     * @return the formatted clause
     */
    public static String responseToString(int indentLevel, ObligationResponse obligationResponse) {
        return String.format("do (%s) %s",
            obligationResponse.getEventCtxVariable(),
            new PMLStatementBlock(obligationResponse.getStatements()).toFormattedString(indentLevel));
    }

    private static String subjectPatternToString(int indentLevel, SubjectPattern subjectPattern) {
        return subjectPattern.toFormattedString(indentLevel);
    }

    private static String operationPatternToString(int indentLevel, OperationPattern operationPattern) {
        return operationPattern.toFormattedString(indentLevel);
    }

    /**
     * Reconstruct a live {@link Obligation} from this compiled statement. The author is supplied out-of-band
     * since the "create obligation" grammar has no author clause. The mirror of {@link #fromObligation(Obligation)}.
     * @param author The obligation's author, persisted separately from the PML text.
     * @return A live Obligation equivalent to the one that produced this statement's PML text.
     */
    public Obligation toObligation(NodeUserContext author) {
        if (!(name instanceof StringLiteralExpression stringLiteralExpression)) {
            throw new IllegalStateException(
                "cannot convert create obligation statement to an Obligation because its name is not a literal string");
        }

        return new Obligation(author, stringLiteralExpression.getValue(), eventPattern, response);
    }

    /**
     * Builds a "create obligation" statement equivalent to the given live {@link Obligation}. The mirror
     * of {@link #toObligation(NodeUserContext)}.
     *
     * @param obligation the obligation to convert
     * @return the equivalent statement
     * @throws IllegalStateException if the obligation's response is not an {@link ObligationResponse}
     */
    public static CreateObligationStatement fromObligation(Obligation obligation) {
        EventPattern event = obligation.getEventPattern();
        ObligationResponse response = obligation.getResponse();
        if (!(response instanceof ObligationResponse pmlObligationResponse)) {
            throw new IllegalStateException("cannot convert obligation " + obligation.getName() + " to PML because it does not have a PMLObligationResponse response");
        }

        return new CreateObligationStatement(
            new StringLiteralExpression(obligation.getName()),
            new EventPattern(event.getSubjectPattern(), event.getOperationPattern()),
            new ObligationResponse(
                pmlObligationResponse.getEventCtxVariable(),
                pmlObligationResponse.getStatements()
            )
        );
    }
}
