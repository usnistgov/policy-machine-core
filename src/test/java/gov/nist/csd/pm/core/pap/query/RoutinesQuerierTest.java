package gov.nist.csd.pm.core.pap.query;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.VOID_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gov.nist.csd.pm.core.common.exception.OperationDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.PAPTestInitializer;
import gov.nist.csd.pm.core.pap.operation.AdminOperations;
import gov.nist.csd.pm.core.pap.operation.Operation;
import gov.nist.csd.pm.core.pap.operation.Routine;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.util.SamplePolicy;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public abstract class RoutinesQuerierTest extends PAPTestInitializer {

    static Routine<Void> r1 = new Routine<>("r1", VOID_TYPE, List.of()) {
        @Override
        public Void execute(PAP pap, UserContext userCtx, Args args) throws PMException {
            return null;
        }

    };
    static Routine<Void> r2 = new Routine<>("r2", VOID_TYPE, List.of()) {

        @Override
        public Void execute(PAP pap, UserContext userCtx, Args args) throws PMException {
            return null;
        }

    };

    // must be static: an anonymous class defined inside a non-static test method captures the
    // enclosing test instance, which breaks Neo4j's Java-serialization write path
    static Routine<Void> r3 = new Routine<>("r3", VOID_TYPE, List.of()) {
        @Override
        public Void execute(PAP pap, UserContext userCtx, Args args) throws PMException {
            return null;
        }

    };

    @Test
    void testGetAdminRoutineNames() throws PMException, IOException {
        SamplePolicy.loadSamplePolicyFromPML(pap);

        pap.nativeOperations().register(r1);
        pap.nativeOperations().register(r2);
        pap.modify().operations().createOperation(r1);
        pap.modify().operations().createOperation(r2);

        // getOperationNames() always includes the protected built-ins too, alongside whatever is persisted
        Set<String> adminOpNames = AdminOperations.ADMIN_OPERATIONS.stream()
            .map(Operation::getName)
            .collect(Collectors.toSet());

        Collection<String> names = pap.query().operations().getOperationNames();
        Set<String> expected = new HashSet<>(adminOpNames);
        expected.addAll(Set.of("r1", "r2", "deleteAllProjects", "deleteProject", "createProject", "deleteReadme", "createProjectAdmin"));
        assertEquals(expected, new HashSet<>(names));

        pap.nativeOperations().register(r3);
        pap.modify().operations().createOperation(r3);
        names = pap.query().operations().getOperationNames();
        expected.add("r3");
        assertEquals(expected, new HashSet<>(names));
    }

    @Nested
    class GetAdminRoutine {

        @Test
        void testSuccess() throws PMException, IOException {
            SamplePolicy.loadSamplePolicyFromPML(pap);

            pap.nativeOperations().register(r1);
            pap.modify().operations().createOperation(r1);

            Operation<?> actual = pap.query().operations().getOperation(r1.getName());
            assertEquals(r1, actual);

            pap.nativeOperations().register(r2);
            pap.modify().operations().createOperation(r2);
            actual = pap.query().operations().getOperation(r2.getName());
            assertEquals(r2, actual);
        }

        @Test
        void testRoutineDoesNotExist() throws PMException, IOException {
            SamplePolicy.loadSamplePolicyFromPML(pap);

            assertThrows(OperationDoesNotExistException.class, () -> pap.query().operations().getOperation("r1"));
        }

    }

}