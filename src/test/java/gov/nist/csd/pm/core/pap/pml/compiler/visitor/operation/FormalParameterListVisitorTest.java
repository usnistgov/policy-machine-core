package gov.nist.csd.pm.core.pap.pml.compiler.visitor.operation;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeIdFormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeIdListFormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeNameFormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeNameListFormalParameter;
import gov.nist.csd.pm.core.pap.pml.TestPMLParser;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.OperationFormalParamListContext;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.core.pap.pml.scope.CompileScope;
import java.util.List;
import org.junit.jupiter.api.Test;

class FormalParameterListVisitorTest {

    @Test
    void testOperationFormalParameterList() throws PMException {
        String pml = """
            @node string a, string b
            """;
        OperationFormalParamListContext ctx = TestPMLParser.parse(pml, OperationFormalParamListContext.class);

        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
        FormalParameterListVisitor visitor = new FormalParameterListVisitor(visitorCtx);
        List<FormalParameter<?>> actual = visitor.visitOperationFormalParamList(ctx);

        assertEquals(2, actual.size());
        assertEquals(
            List.of(
                new NodeNameFormalParameter("a"),
                new FormalParameter<>("b", STRING_TYPE)
            ),
            actual
        );
    }

    @Test
    void testOperationFormalParameterListNoArs() throws PMException {
        String pml = """
            @node string a, string b
            """;
        OperationFormalParamListContext ctx = TestPMLParser.parse(pml, OperationFormalParamListContext.class);

        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
        FormalParameterListVisitor visitor = new FormalParameterListVisitor(visitorCtx);
        List<FormalParameter<?>> actual = visitor.visitOperationFormalParamList(ctx);

        assertEquals(2, actual.size());
        assertEquals(
            List.of(
                new NodeNameFormalParameter("a"),
                new FormalParameter<>("b", STRING_TYPE)
            ),
            actual
        );

        pml = """
            @node string a, string b
            """;
        ctx = TestPMLParser.parse(pml, OperationFormalParamListContext.class);

        actual = visitor.visitOperationFormalParamList(ctx);

        assertEquals(2, actual.size());
        assertEquals(
            List.of(
                new NodeNameFormalParameter("a"),
                new FormalParameter<>("b", STRING_TYPE)
            ),
            actual
        );
    }

    @Test
    void testNodeParamType() throws PMException {
        String pml = """
            @node int64 a, @node []int64 b, @node string c, @node []string d
            """;
        OperationFormalParamListContext ctx = TestPMLParser.parse(pml, OperationFormalParamListContext.class);

        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
        FormalParameterListVisitor visitor = new FormalParameterListVisitor(visitorCtx);
        List<FormalParameter<?>> actual = visitor.visitOperationFormalParamList(ctx);
        assertEquals(4, actual.size());
        assertEquals(
            List.of(
                new NodeIdFormalParameter("a"),
                new NodeIdListFormalParameter("b"),
                new NodeNameFormalParameter("c"),
                new NodeNameListFormalParameter("d")
            ),
            actual
        );

        pml = """
            @node bool a
            """;
        OperationFormalParamListContext ctx1 = TestPMLParser.parse(pml, OperationFormalParamListContext.class);
        PMLCompilationRuntimeException ex = assertThrows(PMLCompilationRuntimeException.class,
            () -> visitor.visitOperationFormalParamList(ctx1));
        assertEquals(1, ex.getErrors().size());
        assertEquals("@node annotation cannot be applied to type bool", ex.getErrors().get(0).errorMessage());
    }

    @Test
    void testMultipleDuplicateParamNames() throws PMException {
        String pml = "string a, string a, string b, string b";
        OperationFormalParamListContext ctx = TestPMLParser.parse(pml, OperationFormalParamListContext.class);
        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
        FormalParameterListVisitor visitor = new FormalParameterListVisitor(visitorCtx);

        PMLCompilationRuntimeException e = assertThrows(
            PMLCompilationRuntimeException.class,
            () -> visitor.visitOperationFormalParamList(ctx));
        assertEquals(2, e.getErrors().size());
        assertEquals("formal arg 'a' already defined in signature or as a constant",
            e.getErrors().get(0).errorMessage());
        assertEquals("formal arg 'b' already defined in signature or as a constant",
            e.getErrors().get(1).errorMessage());
    }

    @Test
    void testMultipleInvalidNodeAnnotationTypes() throws PMException {
        String pml = "@node bool a, @node bool b";
        OperationFormalParamListContext ctx = TestPMLParser.parse(pml, OperationFormalParamListContext.class);
        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
        FormalParameterListVisitor visitor = new FormalParameterListVisitor(visitorCtx);

        PMLCompilationRuntimeException e = assertThrows(
            PMLCompilationRuntimeException.class,
            () -> visitor.visitOperationFormalParamList(ctx));
        assertEquals(2, e.getErrors().size());
    }
}