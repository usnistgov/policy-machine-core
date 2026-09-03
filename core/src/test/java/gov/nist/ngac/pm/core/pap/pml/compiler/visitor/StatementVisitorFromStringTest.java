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

package gov.nist.ngac.pm.core.pap.pml.compiler.visitor;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.ngac.pm.core.pap.obligation.Obligation;
import gov.nist.ngac.pm.core.pap.operation.Operation;
import gov.nist.ngac.pm.core.pap.pml.exception.PMLCompilationException;
import gov.nist.ngac.pm.core.pap.pml.statement.OperationDefinitionStatement;
import gov.nist.ngac.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.ngac.pm.core.pap.pml.statement.operation.CreateObligationStatement;
import gov.nist.ngac.pm.core.pap.query.AccessQuerier;
import gov.nist.ngac.pm.core.pap.query.GraphQuerier;
import gov.nist.ngac.pm.core.pap.query.ObligationsQuerier;
import gov.nist.ngac.pm.core.pap.query.OperationsQuerier;
import gov.nist.ngac.pm.core.pap.query.PolicyQuerier;
import gov.nist.ngac.pm.core.pap.query.ProhibitionsQuerier;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;
import gov.nist.ngac.pm.core.pap.store.PolicyStore;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Round-trip tests (toString() -> narrow-compile -> equivalent object) for the narrow compile entry point,
 * {@link StatementVisitor#fromString}, added to persist Operations/Obligations as PML text per
 * docs/operation-persistence-design.md section 4.
 */
class StatementVisitorFromStringTest {

    private static final NodeUserContext U1 = NodeUserContext.of("u1");

    private MemoryPAP pap;

    @BeforeEach
    void setup() throws PMException {
        pap = new MemoryPAP();
        pap.executePML(U1, """
            create pc "pc1"
            create ua "ua1" in ["pc1"]
            create u "u1" in ["ua1"]
            """);
    }

    @Test
    void testAdminOperationRoundTrip() throws PMException {
        assertOperationRoundTrips("adminop test_admin_op() { }", "test_admin_op");
    }

    @Test
    void testResourceOperationRoundTrip() throws PMException {
        assertOperationRoundTrips("resourceop test_resource_op() { }", "test_resource_op");
    }

    @Test
    void testRoutineRoundTrip() throws PMException {
        assertOperationRoundTrips("routine test_routine() { }", "test_routine");
    }

    @Test
    void testFunctionRoundTrip() throws PMException {
        assertOperationRoundTrips("function test_function() { }", "test_function");
    }

    @Test
    void testQueryOperationRoundTrip() throws PMException {
        assertOperationRoundTrips("query test_query_op() { }", "test_query_op");
    }

    @Test
    void testObligationRoundTrip() throws PMException {
        pap.executePML(U1, """
            create obligation "test_obligation"
            when any user
            performs any operation
            do (evt) { }
            """);

        Obligation original = pap.query().obligations().getObligation("test_obligation");

        PMLStatement<?> compiled = StatementVisitor.fromString(pap.query().operations(), original.toString());
        assertInstanceOf(CreateObligationStatement.class, compiled);

        Obligation recompiled = ((CreateObligationStatement) compiled).toObligation(original.getAuthor());

        assertEquals(original, recompiled);
    }

    @Test
    void testFromStringResolvesCrossReferenceWithoutBulkGetOperationsCall() throws PMException {
        pap.executePML(U1, "adminop existing_op() { }");

        PolicyStore store = pap.policyStore();
        OperationsQuerier throwingOperationsQuerier = new OperationsQuerier(store, pap.javaOperations()) {
            @Override
            public Collection<Operation<?>> getOperations() throws PMException {
                throw new AssertionError("narrow compile entry point must not trigger a bulk getOperations() call");
            }
        };
        pap.withPolicyQuerier(new PolicyQuerier(
            new GraphQuerier(store),
            new ProhibitionsQuerier(store),
            new ObligationsQuerier(store, throwingOperationsQuerier),
            throwingOperationsQuerier,
            new AccessQuerier(store)
        ));

        // references existing_op in its body: proves cross-references are resolved lazily, one name at a
        // time (via a single getOperation(name) call), rather than by seeding the whole symbol table upfront.
        String definitionReferencingExistingOp = "adminop calls_existing_op() { existing_op() }";

        PMLStatement<?> compiled = assertDoesNotThrow(
            () -> StatementVisitor.fromString(throwingOperationsQuerier, definitionReferencingExistingOp));
        assertInstanceOf(OperationDefinitionStatement.class, compiled);
    }

    @Test
    void testQueryOperationBodyCannotLazilyInvokeAdminOperation() throws PMException {
        pap.executePML(U1, "adminop existing_op() { }");

        // query op bodies are restricted to functions/queries only (mirrors CompileScope's
        // copyFunctionsAndQueriesOnly restriction) -- the lazy pap fallback must respect that restriction too,
        // not just the eagerly-seeded builtins/admin-ops map, or it would let a query body invoke an admin
        // operation by going around the restriction via a store lookup.
        String queryOpInvokingAdminOp = "query test_query_op() { existing_op() }";

        assertThrows(PMLCompilationException.class,
            () -> StatementVisitor.fromString(pap.query().operations(), queryOpInvokingAdminOp));
    }

    private void assertOperationRoundTrips(String definitionPml, String name) throws PMException {
        pap.executePML(U1, definitionPml);

        Operation<?> original = pap.query().operations().getOperation(name);

        PMLStatement<?> compiled = StatementVisitor.fromString(pap.query().operations(), original.toString());
        assertInstanceOf(OperationDefinitionStatement.class, compiled);

        Operation<?> recompiled = ((OperationDefinitionStatement) compiled).getOperation();

        assertEquals(original, recompiled);
    }
}
