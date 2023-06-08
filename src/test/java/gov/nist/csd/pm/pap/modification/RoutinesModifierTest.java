package gov.nist.csd.pm.pap.modification;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PAPTestInitializer;
import gov.nist.csd.pm.pap.exception.RoutineExistsException;
import gov.nist.csd.pm.pap.routine.Routine;
import gov.nist.csd.pm.util.SamplePolicy;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public abstract class RoutinesModifierTest extends PAPTestInitializer {

    @Nested
    class CreateAdminRoutine {

        @Test
        void testSuccess() throws PMException, IOException {
            SamplePolicy.loadSamplePolicyFromPML(pap);

            pap.modify().routines().createAdminRoutine(new Routine<Void>(
                    "routine1",
                    List.of("a", "b")
            ) {
                @Override
                public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
                    return null;
                }
            });

            assertTrue(pap.query().routines().getAdminRoutineNames().contains("routine1"));
        }

        @Test
        void testRoutineExists() throws PMException, IOException {
            SamplePolicy.loadSamplePolicyFromPML(pap);

            pap.modify().routines().createAdminRoutine(new Routine<Void>(
                    "routine1",
                    List.of("a", "b")
            ) {
                @Override
                public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
                    return null;
                }
            });

            assertThrows(RoutineExistsException.class, () -> {
                pap.modify().routines().createAdminRoutine(new Routine<Void>(
                        "routine1",
                        List.of("a", "b")
                ) {
                    @Override
                    public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
                        return null;
                    }
                });
            });
        }

        @Test
        void testTx() throws PMException, IOException {
            SamplePolicy.loadSamplePolicyFromPML(pap);

            pap.runTx(tx -> {
                tx.modify().routines().createAdminRoutine(new Routine<Void>(
                        "routine1",
                        List.of("a", "b")
                ) {
                    @Override
                    public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
                        return null;
                    }
                });
                tx.modify().routines().createAdminRoutine(new Routine<Void>(
                        "routine2",
                        List.of("a", "b")
                ) {
                    @Override
                    public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
                        return null;
                    }
                });
            });

            assertThrows(PMException.class, () -> pap.runTx(tx -> {
                tx.modify().routines().createAdminRoutine(new Routine<Void>(
                        "routine3",
                        List.of("a", "b")
                ) {
                    @Override
                    public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
                        return null;
                    }
                });
                tx.modify().routines().createAdminRoutine(new Routine<Void>(
                        "routine4",
                        List.of("a", "b")
                ) {
                    @Override
                    public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
                        return null;
                    }
                });

                throw new PMException("");
            }));

            assertTrue(pap.query().routines().getAdminRoutineNames().containsAll(List.of("routine1", "routine2")));
            assertFalse(pap.query().routines().getAdminRoutineNames().containsAll(List.of("routine3", "routine4")));
        }
    }

    @Nested
    class DeleteAdminRoutine {

        @Test
        void testSuccess() throws PMException, IOException {
            SamplePolicy.loadSamplePolicyFromPML(pap);
            pap.modify().routines().createAdminRoutine(new Routine<Void>(
                    "routine1",
                    List.of("a", "b")
            ) {
                @Override
                public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
                    return null;
                }
            });

            pap.modify().routines().deleteAdminRoutine("routine1");
            pap.modify().routines().deleteAdminRoutine("routine1");
            assertFalse(pap.query().routines().getAdminRoutineNames().contains("routine1"));
        }

        @Test
        void testTx() throws PMException, IOException {
            SamplePolicy.loadSamplePolicyFromPML(pap);

            pap.runTx(tx -> {
                tx.modify().routines().createAdminRoutine(new Routine<Void>(
                        "routine1",
                        List.of("a", "b")
                ) {
                    @Override
                    public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
                        return null;
                    }
                });
                tx.modify().routines().createAdminRoutine(new Routine<Void>(
                        "routine2",
                        List.of("a", "b")
                ) {
                    @Override
                    public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
                        return null;
                    }
                });
            });

            assertThrows(PMException.class, () -> pap.runTx(tx -> {
                tx.modify().routines().deleteAdminRoutine("routine1");
                tx.modify().routines().deleteAdminRoutine("routine2");

                throw new PMException("");
            }));

            assertTrue(pap.query().routines().getAdminRoutineNames().containsAll(List.of("routine1", "routine2")));
        }
    }

}