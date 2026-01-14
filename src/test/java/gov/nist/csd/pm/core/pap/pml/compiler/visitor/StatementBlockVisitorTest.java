package gov.nist.csd.pm.core.pap.pml.compiler.visitor;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.STRING_TYPE;
import static gov.nist.csd.pm.core.pap.pml.TestPMLParser.toStatementBlockCtx;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.compiler.Variable;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.function.PMLFunctionSignature;
import gov.nist.csd.pm.core.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.core.pap.pml.scope.Scope;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class StatementBlockVisitorTest {

    private static Scope<Variable, PMLFunctionSignature> testGlobalScope;

    @BeforeAll
    static void setup() throws PMException {
        testGlobalScope = new CompileScope();
    }

    @Test
    void testFunctionInBlockOk() {
        PMLParser.StatementBlockContext ctx = toStatementBlockCtx(
                """
                {
                    adminop f1() {}
                }
                """
        );
        VisitorContext visitorContext = new VisitorContext(testGlobalScope);
        assertDoesNotThrow(
                () -> new StatementBlockVisitor(visitorContext, STRING_TYPE)
                        .visitStatementBlock(ctx)
        );
    }


}