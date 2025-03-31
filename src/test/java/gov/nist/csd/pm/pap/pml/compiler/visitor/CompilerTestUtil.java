package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.pap.pml.PMLContextVisitor;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.exception.PMLCompilationRuntimeException;
import org.antlr.v4.runtime.ParserRuleContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CompilerTestUtil {

    public static void testCompilationError(String input, VisitorContext visitorContext,
                                                int numExpectedErrors, String ... expectedError) {
        ParserRuleContext ctx = PMLContextVisitor.toCtx(input, ParserRuleContext.class);
        StatementVisitor statementVisitor = new StatementVisitor(visitorContext);

        PMLCompilationRuntimeException e = assertThrows(
                PMLCompilationRuntimeException.class,
                () -> statementVisitor.visit(ctx)
        );

        assertEquals(numExpectedErrors, e.getErrors().size());

        for (int i = 0; i < expectedError.length; i++) {
            assertEquals(expectedError[i], e.getErrors().get(i).errorMessage());
        }
    }

}
