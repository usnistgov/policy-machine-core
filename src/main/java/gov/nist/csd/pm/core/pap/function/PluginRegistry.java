package gov.nist.csd.pm.core.pap.function;

import java.util.ArrayList;
import java.util.List;

public class PluginRegistry {

    private final List<BasicFunction<?>> basicFunctions;
    private final List<QueryFunction<?>> queryFunctions;
    private final List<AdminOperation<?>> operations;
    private final List<Routine<?>> routines;

    public PluginRegistry() {
        basicFunctions = new ArrayList<>();
        queryFunctions = new ArrayList<>();
        operations = new ArrayList<>();
        routines = new ArrayList<>();
    }

    public List<BasicFunction<?>> getBasicFunctions() {
        return basicFunctions;
    }

    public List<QueryFunction<?>> getQueryFunctions() {
        return queryFunctions;
    }

    public List<AdminOperation<?>> getOperations() {
        return operations;
    }

    public List<Routine<?>> getRoutines() {
        return routines;
    }

    public BasicFunction<?> getBasicFunction(String name) {
        for (BasicFunction<?> basicFunction : basicFunctions) {
            if (basicFunction.getName().equals(name)) {
                return basicFunction;
            }
        }

        return null;
    }

    public QueryFunction<?> getQueryFunction(String name) {
        for (QueryFunction<?> queryFunction : queryFunctions) {
            if (queryFunction.getName().equals(name)) {
                return queryFunction;
            }
        }

        return null;
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

    public void registerBasicFunction(BasicFunction<?> basicFunction) {
        boolean exists = basicFunctions.stream()
            .anyMatch(existing -> existing.getName().equals(basicFunction.getName()));

        if (exists) {
            throw new IllegalArgumentException(
                "A basic function with the name " + basicFunction.getName() + " is already registered"
            );
        }

        basicFunctions.add(basicFunction);
    }

    public void registerBasicFunction(QueryFunction<?> queryFunction) {
        boolean exists = queryFunctions.stream()
            .anyMatch(existing -> existing.getName().equals(queryFunction.getName()));

        if (exists) {
            throw new IllegalArgumentException(
                "A query function with the name " + queryFunction.getName() + " is already registered"
            );
        }

        queryFunctions.add(queryFunction);
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
        boolean exists = routines.stream()
            .anyMatch(existing -> existing.getName().equals(routine.getName()));

        if (exists) {
            throw new IllegalArgumentException(
                "A routine with the name " + routine.getName() + " is already registered"
            );
        }

        routines.add(routine);
    }

    public void removeBasicFunction(BasicFunction<?> basicFunction) {
        basicFunctions.removeIf(op -> op.getName().equals(basicFunction.getName()));
    }

    public void removeQueryFunction(QueryFunction<?> queryFunction) {
        basicFunctions.removeIf(op -> op.getName().equals(queryFunction.getName()));
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

    public List<String> getBasicFunctionNames() {
        return basicFunctions.stream()
            .map(BasicFunction::getName)
            .toList();
    }

    public List<String> getQueryFunctionNames() {
        return queryFunctions.stream()
            .map(QueryFunction::getName)
            .toList();
    }
}
