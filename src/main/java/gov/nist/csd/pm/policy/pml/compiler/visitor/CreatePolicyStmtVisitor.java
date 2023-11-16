package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.pml.PMLErrorHandler;
import gov.nist.csd.pm.policy.pml.antlr.PMLLexer;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.exception.PMLCompilationException;
import gov.nist.csd.pm.policy.pml.statement.AssociateStatement;
import gov.nist.csd.pm.policy.pml.statement.CreatePolicyStatement;
import gov.nist.csd.pm.policy.pml.type.Type;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreatePolicyStmtVisitor extends PMLBaseVisitor<CreatePolicyStatement> {

    public CreatePolicyStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public CreatePolicyStatement visitCreatePolicyStatement(PMLParser.CreatePolicyStatementContext ctx) {
        Expression name = Expression.compile(visitorCtx, ctx.name, Type.string());
        Expression withProperties = null;
        if (ctx.properties != null) {
            withProperties = Expression.compile(visitorCtx, ctx.properties, Type.map(Type.string(), Type.string()));
        }

        PMLParser.HierarchyContext hierarchyCtx = ctx.hierarchy();
        if (hierarchyCtx == null) {
            return new CreatePolicyStatement(name, withProperties);
        }

        List<CreatePolicyStatement.CreateOrAssignAttributeStatement> uas = new ArrayList<>();
        List<CreatePolicyStatement.CreateOrAssignAttributeStatement> oas = new ArrayList<>();

        try {
            if (hierarchyCtx.userAttrsHierarchy() != null) {
                uas = buildUAHierarchyStatement(name, hierarchyCtx.userAttrsHierarchy());
            }

            if (hierarchyCtx.objectAttrsHierarchy() != null) {
                oas = buildOAHierarchyStatement(name, hierarchyCtx.objectAttrsHierarchy());
            }
        } catch (PMLCompilationException e) {
            return new CreatePolicyStatement(ctx);
        }

        List<AssociateStatement> assocs = new ArrayList<>();
        if (hierarchyCtx.associationsHierarchy() != null) {
            assocs = buildAssociationStatements(hierarchyCtx.associationsHierarchy());
        }

        return new CreatePolicyStatement(name, withProperties, uas, oas, assocs);
    }

    private List<CreatePolicyStatement.CreateOrAssignAttributeStatement> buildUAHierarchyStatement(Expression pc, PMLParser.UserAttrsHierarchyContext hierarchyCtx)
            throws PMLCompilationException {
        PMLParser.HierarchyBlockContext blockCtx = hierarchyCtx.hierarchyBlock();

        List<HierarchyLine> hierarchyLines = getHierarchyBlockLinesFromTokens(blockCtx);

        return buildHierarchy(pc, hierarchyLines, NodeType.UA);
    }

    private List<CreatePolicyStatement.CreateOrAssignAttributeStatement> buildOAHierarchyStatement(Expression pc, PMLParser.ObjectAttrsHierarchyContext hierarchyCtx)
            throws PMLCompilationException {
        PMLParser.HierarchyBlockContext blockCtx = hierarchyCtx.hierarchyBlock();

        List<HierarchyLine> hierarchyLines = getHierarchyBlockLinesFromTokens(blockCtx);

        return buildHierarchy(pc, hierarchyLines, NodeType.OA);
    }

    private List<AssociateStatement> buildAssociationStatements(PMLParser.AssociationsHierarchyContext associationsHierarchy) {
        List<AssociateStatement> associateStatements = new ArrayList<>();

        PMLParser.AssociationsHierarchyBlockContext associationsHierarchyBlockContext =
                associationsHierarchy.associationsHierarchyBlock();
        List<PMLParser.AssociationsHierarchyStatementContext> associationsHierarchyStatementContexts =
                associationsHierarchyBlockContext.associationsHierarchyStatement();
        for (PMLParser.AssociationsHierarchyStatementContext assocCtx : associationsHierarchyStatementContexts) {
            Expression ua = Expression.compile(visitorCtx, assocCtx.ua, Type.string());
            Expression target = Expression.compile(visitorCtx, assocCtx.target, Type.string());
            Expression arset = Expression.compile(visitorCtx, assocCtx.arset, Type.array(Type.string()));

            AssociateStatement associateStatement = new AssociateStatement(ua, target, arset);
            associateStatements.add(associateStatement);
        }

        return associateStatements;
    }

    private List<CreatePolicyStatement.CreateOrAssignAttributeStatement> buildHierarchy(Expression pc, List<HierarchyLine> hierarchyLines, NodeType type)
            throws PMLCompilationException {
        List<CreatePolicyStatement.CreateOrAssignAttributeStatement> attrs = new ArrayList<>();

        Map<Integer, Expression> currentParentIndexes = new HashMap<>();
        currentParentIndexes.put(0, pc);

        for (HierarchyLine line : hierarchyLines) {
            int indent = line.indent();
            int parentIndent = indent-1;

            PMLParser.HierarchyStatementContext hierarchyStatementContext = hierarchyStatementFromString(visitorCtx, line.stmt);
            Expression parentExpr = currentParentIndexes.get(parentIndent);
            CreatePolicyStatement.CreateOrAssignAttributeStatement stmt = buildStatementFromCtx(hierarchyStatementContext, type, parentExpr);

            attrs.add(stmt);

            currentParentIndexes.put(indent, stmt.getName());
        }

        return attrs;
    }

    private CreatePolicyStatement.CreateOrAssignAttributeStatement buildStatementFromCtx(PMLParser.HierarchyStatementContext ctx, NodeType type, Expression parentExpr) {
        Expression propertiesExpr = null;
        if (ctx.properties != null) {
            propertiesExpr = Expression.compile(visitorCtx, ctx.properties, Type.map(Type.string(), Type.string()));
        }

        return new CreatePolicyStatement.CreateOrAssignAttributeStatement(
                Expression.compile(visitorCtx, ctx.name, Type.string()),
                type,
                parentExpr,
                propertiesExpr
        );
    }

    private PMLParser.HierarchyStatementContext hierarchyStatementFromString(VisitorContext visitorCtx, String input)
            throws PMLCompilationException {
        PMLErrorHandler pmlErrorHandler = new PMLErrorHandler();

        PMLLexer lexer = new PMLLexer(CharStreams.fromString(input));
        lexer.removeErrorListeners();
        lexer.addErrorListener(pmlErrorHandler);

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PMLParser parser = new PMLParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(pmlErrorHandler);

        PMLParser.HierarchyStatementContext ctx = parser.hierarchyStatement();

        visitorCtx.errorLog().addErrors(pmlErrorHandler.getErrors());
        if (visitorCtx.errorLog().getErrors().size() > 0) {
            throw new PMLCompilationException(visitorCtx.errorLog());
        }

        return ctx;
    }

    /*
    if map does not have indent, put iindent -> expr
    if indent is same, put indent 0> expr
    else if indent is next, assingn to indent(i-1), set indent -> expr
    if indent is prev, remove from i from map
     */

    private List<HierarchyLine> getHierarchyBlockLinesFromTokens(PMLParser.HierarchyBlockContext blockCtx) {
        List<HierarchyLine> lines = new ArrayList<>();

        int zeroIndentIndex = blockCtx.OPEN_CURLY().getSymbol().getTokenIndex()-3;
        int openIndex = blockCtx.OPEN_CURLY().getSymbol().getTokenIndex()+1;
        int closeIndex = blockCtx.CLOSE_CURLY().getSymbol().getTokenIndex() - 2;

        CommonTokenStream tokens = visitorCtx.tokens();

        Token zeroIndentToken = tokens.get(zeroIndentIndex);
        int zeroIndent = zeroIndentToken.getText().replaceAll("\\n", "").length();

        List<Token> hierarchyTokens = tokens.getTokens(openIndex, closeIndex);
        if (hierarchyTokens == null || hierarchyTokens.isEmpty()) {
            return lines;
        }

        for (int i = 0; i < hierarchyTokens.size(); i++) {
            // normalize the indent to start at 0 within the current block
            Token token = hierarchyTokens.get(i);
            int indentLength = token.getText().replaceAll("\\n", "").length() - zeroIndent;
            if (indentLength == 0 || indentLength % 4 != 0) {
                visitorCtx.errorLog().addError(token.getLine(), token.getCharPositionInLine(), 0, "invalid indentation");
                return new ArrayList<>();
            }

            int normalizedIndent = indentLength / 4;

            String attrStr = "";
            int j;
            for (j = i+1; j < hierarchyTokens.size(); j++) {
                String t = hierarchyTokens.get(j).getText();
                if(t.startsWith("\n")) {
                    break;
                }

                attrStr += t;
            }

            i = j-1;

            if (!attrStr.isEmpty()) {
                lines.add(new HierarchyLine(normalizedIndent, attrStr));
            }
        }

        return lines;
    }

    record HierarchyLine(int indent, String stmt) { }
}
