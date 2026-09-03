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

package gov.nist.ngac.pm.core.pap.query;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.VOID_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gov.nist.ngac.pm.core.common.exception.OperationDoesNotExistException;
import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.PAPTestInitializer;
import gov.nist.ngac.pm.core.pap.operation.AdminOperations;
import gov.nist.ngac.pm.core.pap.operation.Operation;
import gov.nist.ngac.pm.core.pap.operation.Routine;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.util.SamplePolicy;
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

        pap.javaOperations().register(r1);
        pap.javaOperations().register(r2);
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

        pap.javaOperations().register(r3);
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

            pap.javaOperations().register(r1);
            pap.modify().operations().createOperation(r1);

            Operation<?> actual = pap.query().operations().getOperation(r1.getName());
            assertEquals(r1, actual);

            pap.javaOperations().register(r2);
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