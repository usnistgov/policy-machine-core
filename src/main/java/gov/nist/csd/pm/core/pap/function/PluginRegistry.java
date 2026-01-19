package gov.nist.csd.pm.core.pap.function;

import gov.nist.csd.pm.core.common.exception.FunctionExistsException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.query.OperationsQuery;
import java.util.ArrayList;
import java.util.List;

public class PluginRegistry {

    private final List<BasicFunction<?>> basicFunctions;
    private final List<QueryOperation<?>> queryOperations;
    private final List<AdminOperation<?>> adminOperations;
    private final List<ResourceOperation<?>> resourceOperations;
    private final List<Routine<?>> routines;

    public PluginRegistry() {
        basicFunctions = new ArrayList<>();
        queryOperations = new ArrayList<>();
        adminOperations = new ArrayList<>();
        resourceOperations = new ArrayList<>();
        routines = new ArrayList<>();
    }

    public List<BasicFunction<?>> getBasicFunctions() {
        return basicFunctions;
    }

    public List<QueryOperation<?>> getQueryOperations() {
        return queryOperations;
    }

    public List<AdminOperation<?>> getAdminOperations() {
        return adminOperations;
    }

    public List<ResourceOperation<?>> getResourceOperations() {
        return resourceOperations;
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

    public QueryOperation<?> getQueryOperation(String name) {
        for (QueryOperation<?> queryOperation : queryOperations) {
            if (queryOperation.getName().equals(name)) {
                return queryOperation;
            }
        }

        return null;
    }

    public AdminOperation<?> getAdminOperation(String name) {
        for (AdminOperation<?> op : adminOperations) {
            if (op.getName().equals(name)) {
                return op;
            }
        }

        return null;
    }

    public ResourceOperation<?> getResourceOperation(String name) {
        for (ResourceOperation<?> op : resourceOperations) {
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

    public void registerBasicFunction(OperationsQuery opQuery, BasicFunction<?> basicFunction) throws PMException {
        boolean exists = opQuery.operationExists(basicFunction.getName());
        if (exists) {
            throw new FunctionExistsException(basicFunction.getName());
        }

        basicFunctions.add(basicFunction);
    }

    public void registerQueryFunction(OperationsQuery opQuery, QueryOperation<?> queryOperation) throws PMException {
        boolean exists = opQuery.operationExists(queryOperation.getName());
        if (exists) {
            throw new FunctionExistsException(queryOperation.getName());
        }

        queryOperations.add(queryOperation);
    }

    public void registerAdminOperation(OperationsQuery opQuery, AdminOperation<?> op) throws PMException {
        boolean exists = opQuery.operationExists(op.getName());
        if (exists) {
            throw new FunctionExistsException(op.getName());
        }

        adminOperations.add(op);
    }

    public void registerResourceOperation(OperationsQuery opQuery, ResourceOperation<?> op) throws PMException {
        boolean exists = opQuery.operationExists(op.getName());
        if (exists) {
            throw new FunctionExistsException(op.getName());
        }

        resourceOperations.add(op);
    }

    public void registerRoutine(OperationsQuery opQuery, Routine<?> routine) throws PMException {
        boolean exists = opQuery.operationExists(routine.getName());
        if (exists) {
            throw new FunctionExistsException(routine.getName());
        }

        routines.add(routine);
    }

    public void removeBasicFunction(BasicFunction<?> basicFunction) {
        basicFunctions.removeIf(op -> op.getName().equals(basicFunction.getName()));
    }

    public void removeQueryFunction(QueryOperation<?> queryOperation) {
        queryOperations.removeIf(op -> op.getName().equals(queryOperation.getName()));
    }

    public void removeAdminOperation(String opName) {
        adminOperations.removeIf(op -> op.getName().equals(opName));
    }

    public void removeResourceOperation(String opName) {
        resourceOperations.removeIf(op -> op.getName().equals(opName));
    }

    public void removeRoutine(String routineName) {
        routines.removeIf(routine -> routine.getName().equals(routineName));
    }

    public List<String> getAdminOperationNames() {
        return adminOperations.stream()
            .map(Operation::getName)
            .toList();
    }

    public List<String> getResourceOperationNames() {
        return resourceOperations.stream()
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

    public List<String> getQueryOperationNames() {
        return queryOperations.stream()
            .map(QueryOperation::getName)
            .toList();
    }

    public boolean pluginExists(String pluginName) {
        for (BasicFunction<?> basicFunction : basicFunctions) {
            if (basicFunction.getName().equals(pluginName)) {
                return true;
            }
        }
        for (QueryOperation<?> queryOperation : queryOperations) {
            if (queryOperation.getName().equals(pluginName)) {
                return true;
            }
        }
        for (AdminOperation<?> operation : adminOperations) {
            if (operation.getName().equals(pluginName)) {
                return true;
            }
        }
        for (ResourceOperation<?> operation : resourceOperations) {
            if (operation.getName().equals(pluginName)) {
                return true;
            }
        }
        for (Routine<?> routine : routines) {
            if (routine.getName().equals(pluginName)) {
                return true;
            }
        }

        return false;
    }
}
