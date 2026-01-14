package gov.nist.csd.pm.core.pap.function;

import gov.nist.csd.pm.core.pap.function.op.AdminOperation;
import gov.nist.csd.pm.core.pap.function.op.Operation;
import gov.nist.csd.pm.core.pap.function.routine.Routine;
import java.util.ArrayList;
import java.util.List;

public class PluginRegistry {

    private final List<AdminOperation<?>> operations;
    private final List<Routine<?>> routines;

    public PluginRegistry() {
        operations = new ArrayList<>();
        routines = new ArrayList<>();
    }

    public PluginRegistry(List<AdminOperation<?>> operations, List<Routine<?>> routines) {
        this.operations = operations;
        this.routines = routines;
    }

    public List<AdminOperation<?>> getOperations() {
        return operations;
    }

    public List<Routine<?>> getRoutines() {
        return routines;
    }

    public AdminOperation<?> getOperation(String name) {
        for (AdminOperation<?> op : operations) {
            if (op.getName().equals(name)) {
                return op;
            }
        }

        return null;
    }

    public Routine<?> getRoutine(String name) {
        for (Routine<?> routine : routines) {
            if (routine.getName().equals(name)) {
                return routine;
            }
        }

        return null;
    }

    public void registerOperation(AdminOperation<?> op) {
        boolean exists = operations.stream()
            .anyMatch(existing -> existing.getName().equals(op.getName()));

        if (exists) {
            throw new IllegalArgumentException(
                "An operation with the name " + op.getName() + " is already registered"
            );
        }

        operations.add(op);
    }

    public void registerRoutine(Routine<?> routine) {
        boolean exists = operations.stream()
            .anyMatch(existing -> existing.getName().equals(routine.getName()));

        if (exists) {
            throw new IllegalArgumentException(
                "A routine with the name " + routine.getName() + " is already registered"
            );
        }

        routines.add(routine);
    }

    public void removeOperation(String opName) {
        operations.removeIf(op -> op.getName().equals(opName));
    }

    public void removeRoutine(String routineName) {
        routines.removeIf(routine -> routine.getName().equals(routineName));
    }

    public List<String> getOperationNames() {
        return operations.stream()
            .map(Operation::getName)
            .toList();
    }

    public List<String> getRoutineNames() {
        return routines.stream()
            .map(Routine::getName)
            .toList();
    }
}
