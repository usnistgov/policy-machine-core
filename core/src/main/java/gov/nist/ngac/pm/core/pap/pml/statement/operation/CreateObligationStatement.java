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
 * A PML statement that creates an {@link Obligation}.
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
     * Formats an event pattern's when/performs clause.
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
     * Formats an obligation response's do clause.
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
     * Converts this statement to an {@link Obligation}, using the given author since the statement itself has none.
     *
     * @param author the obligation's author
     * @return the equivalent obligation
     */
    public Obligation toObligation(NodeUserContext author) {
        if (!(name instanceof StringLiteralExpression stringLiteralExpression)) {
            throw new IllegalStateException(
                "cannot convert create obligation statement to an Obligation because its name is not a literal string");
        }

        return new Obligation(author, stringLiteralExpression.getValue(), eventPattern, response);
    }

    /**
     * Builds a statement equivalent to the given {@link Obligation}.
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
