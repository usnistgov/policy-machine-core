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

package gov.nist.ngac.pm.core.pap.pml.compiler.visitor.operation;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.ngac.pm.core.pap.operation.param.FormalParameter;
import gov.nist.ngac.pm.core.pap.operation.param.NodeIdFormalParameter;
import gov.nist.ngac.pm.core.pap.operation.param.NodeIdListFormalParameter;
import gov.nist.ngac.pm.core.pap.operation.param.NodeNameFormalParameter;
import gov.nist.ngac.pm.core.pap.operation.param.NodeNameListFormalParameter;
import gov.nist.ngac.pm.core.pap.pml.TestPMLParser;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser.OperationFormalParamListContext;
import gov.nist.ngac.pm.core.pap.pml.context.VisitorContext;
import gov.nist.ngac.pm.core.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.ngac.pm.core.pap.pml.scope.CompileScope;
import java.util.List;
import org.junit.jupiter.api.Test;

class FormalParameterListVisitorTest {

    @Test
    void testOperationFormalParameterList() throws PMException {
        String pml = """
            @Node string a, string b
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
            @Node string a, string b
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
            @Node string a, string b
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
            @Node int64 a, @Node []int64 b, @Node string c, @Node []string d
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
            @Node bool a
            """;
        OperationFormalParamListContext ctx1 = TestPMLParser.parse(pml, OperationFormalParamListContext.class);
        PMLCompilationRuntimeException ex = assertThrows(PMLCompilationRuntimeException.class,
            () -> visitor.visitOperationFormalParamList(ctx1));
        assertEquals(1, ex.getErrors().size());
        assertEquals("@Node annotation cannot be applied to type bool", ex.getErrors().get(0).errorMessage());
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
        String pml = "@Node bool a, @Node bool b";
        OperationFormalParamListContext ctx = TestPMLParser.parse(pml, OperationFormalParamListContext.class);
        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
        FormalParameterListVisitor visitor = new FormalParameterListVisitor(visitorCtx);

        PMLCompilationRuntimeException e = assertThrows(
            PMLCompilationRuntimeException.class,
            () -> visitor.visitOperationFormalParamList(ctx));
        assertEquals(2, e.getErrors().size());
    }
}