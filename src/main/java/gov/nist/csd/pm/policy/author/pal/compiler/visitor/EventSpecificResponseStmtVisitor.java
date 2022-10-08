package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.model.scope.VariableAlreadyDefinedInScopeException;
import gov.nist.csd.pm.policy.author.pal.statement.CreateRuleStatement;
import gov.nist.csd.pm.policy.author.pal.statement.EventSpecificResponseStatement;
import gov.nist.csd.pm.policy.author.pal.statement.PALStatement;

import java.util.ArrayList;
import java.util.List;

public class EventSpecificResponseStmtVisitor extends PALBaseVisitor<EventSpecificResponseStatement> {

    private final VisitorContext visitorCtx;

    public EventSpecificResponseStmtVisitor(VisitorContext localVisitorCtx) {
        this.visitorCtx = localVisitorCtx;
    }

    @Override
    public EventSpecificResponseStatement visitEventSpecificResponse(PALParser.EventSpecificResponseContext ctx) {
        String event = ctx.event.getText();
        String alias = ctx.alias.getText();

        VisitorContext localVisitorCtx = visitorCtx.copy();
        try {
            localVisitorCtx.scope().addVariable(event, Type.map(Type.string(), Type.any()), true);

            if (!alias.isEmpty()) {
                localVisitorCtx.scope().addVariable(alias, Type.map(Type.string(), Type.any()), true);
            }
        } catch (VariableAlreadyDefinedInScopeException e) {
            visitorCtx.errorLog().addError(ctx, e.getMessage());
        }

        List<PALStatement> stmts = new ArrayList<>();
        List<String> definedEvents = new ArrayList<>();
        for (PALParser.ResponseStmtContext responseStmtCtx : ctx.responseStmt()) {
            PALStatement stmt = null;

            if (responseStmtCtx.stmt() != null) {
                stmt = new StatementVisitor(localVisitorCtx)
                        .visitStmt(responseStmtCtx.stmt());
            } else if (responseStmtCtx.createRuleStmt() != null) {
                stmt = new CreateRuleStmtVisitor(localVisitorCtx)
                        .visitCreateRuleStmt(responseStmtCtx.createRuleStmt());
            } else if (responseStmtCtx.deleteRuleStmt() != null) {
                stmt = new DeleteRuleStmtVisitor(localVisitorCtx)
                        .visitDeleteRuleStmt(responseStmtCtx.deleteRuleStmt());
            } else if (responseStmtCtx.eventSpecificResponse() != null) {
                stmt = new EventSpecificResponseStmtVisitor(localVisitorCtx)
                        .visitEventSpecificResponse(responseStmtCtx.eventSpecificResponse());

                // check if an event specific response has already been defined for this event
                String e = ((EventSpecificResponseStatement) stmt).getEvent();
                if (definedEvents.contains(e)) {
                    visitorCtx.errorLog().addError(responseStmtCtx.eventSpecificResponse(),
                            String.format("event specific response already defined for event %s", e));
                }

                definedEvents.add(e);
            }

            stmts.add(stmt);
        }

        return new EventSpecificResponseStatement(event, alias, new CreateRuleStatement.ResponseBlock(stmts));
    }
}
