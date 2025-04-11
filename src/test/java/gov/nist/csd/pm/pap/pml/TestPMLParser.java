package gov.nist.csd.pm.pap.pml;

import gov.nist.csd.pm.pap.pml.antlr.PMLLexer;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser.PmlContext;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;

/**
 * Utility class for parsing PML expressions in test environments.
 * Unlike PMLContextVisitor, this class directly creates ANTLR parse trees
 * without going through multiple layers that could cause NPEs.
 */
public class TestPMLParser {

    /**
     * Parse a PML expression string directly into an ExpressionContext.
     * 
     * @param pml The PML expression string to parse
     * @return The parsed expression context
     */
    public static PMLParser.ExpressionContext parseExpression(String pml) {
        PMLLexer lexer = new PMLLexer(CharStreams.fromString(pml));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PMLParser parser = new PMLParser(tokens);
        return parser.expression();
    }
    
    /**
     * Parse a PML statement string directly into a StatementContext.
     * 
     * @param pml The PML statement to parse
     * @return The parsed statement context
     */
    public static PMLParser.StatementContext parseStatement(String pml) {
        PMLLexer lexer = new PMLLexer(CharStreams.fromString(pml));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PMLParser parser = new PMLParser(tokens);
        return parser.statement();
    }
    
    /**
     * Parse a complete PML code into a PmlContext.
     * 
     * @param pml The complete PML code to parse
     * @return The parsed PML context
     */
    public static PMLParser.PmlContext parsePml(String pml) {
        PMLLexer lexer = new PMLLexer(CharStreams.fromString(pml));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PMLParser parser = new PMLParser(tokens);
        return parser.pml();
    }
    
    /**
     * Generic method to parse any rule from the PML grammar.
     * 
     * @param pml The PML code to parse
     * @param ruleClass The class of the ParserRuleContext to return
     * @param <T> The type of ParserRuleContext
     * @return The parsed context of type T
     */
    @SuppressWarnings("unchecked")
    public static <T extends ParserRuleContext> T parse(String pml, Class<T> ruleClass) {
        PMLLexer lexer = new PMLLexer(CharStreams.fromString(pml));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PMLParser parser = new PMLParser(tokens);
        
        if (ruleClass == PMLParser.ExpressionContext.class) {
            return (T) parser.expression();
        } else if (ruleClass == PMLParser.StatementContext.class) {
            return (T) parser.statement();
        } else if (ruleClass == PMLParser.PmlContext.class) {
            return (T) parser.pml();
        } else if (ruleClass == PMLParser.VariableReferenceExpressionContext.class) {
            return (T) parser.variableReference();
        } else if (ruleClass == PMLParser.ArrayLiteralContext.class) {
            return (T) parser.arrayLit();
        } else if (ruleClass == PMLParser.MapLiteralContext.class) {
            return (T) parser.mapLit();
        } else {
            throw new IllegalArgumentException("Unsupported parser rule class: " + ruleClass.getName());
        }
    }

    public static <T extends RuleContext> T toCtx(String input, Class<T> t) {
        PMLErrorHandler pmlErrorHandler = new PMLErrorHandler();

        PMLLexer lexer = new PMLLexer(CharStreams.fromString(input));
        lexer.removeErrorListeners();
        lexer.addErrorListener(pmlErrorHandler);

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PMLParser parser = new PMLParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(pmlErrorHandler);

        RuleContext pmlContext = parsePml(input);

        return t.cast(pmlContext);
    }

    public static PMLParser.StatementBlockContext toStatementBlockCtx(String input) {
        PMLErrorHandler pmlErrorHandler = new PMLErrorHandler();

        PMLLexer lexer = new PMLLexer(CharStreams.fromString(input));
        lexer.removeErrorListeners();
        lexer.addErrorListener(pmlErrorHandler);

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PMLParser parser = new PMLParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(pmlErrorHandler);

        return parser.statementBlock();
    }
}