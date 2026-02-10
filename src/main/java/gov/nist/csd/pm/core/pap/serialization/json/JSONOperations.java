package gov.nist.csd.pm.core.pap.serialization.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class JSONOperations {

    private List<String> admin;
    private List<String> resource;
    private List<String> routine;
    private List<String> query;
    private List<String> function;

    public JSONOperations(List<String> admin,
                          List<String> resource,
                          List<String> routine,
                          List<String> query,
                          List<String> function) {
        this.admin = admin;
        this.resource = resource;
        this.routine = routine;
        this.query = query;
        this.function = function;
    }

    public JSONOperations() {
    }

    public List<String> getAdmin() {
        return admin;
    }

    public void setAdmin(List<String> admin) {
        this.admin = admin;
    }

    public List<String> getResource() {
        return resource;
    }

    public void setResource(List<String> resource) {
        this.resource = resource;
    }

    public List<String> getRoutine() {
        return routine;
    }

    public void setRoutine(List<String> routine) {
        this.routine = routine;
    }

    public List<String> getQuery() {
        return query;
    }

    public void setQuery(List<String> query) {
        this.query = query;
    }

    public List<String> getFunction() {
        return function;
    }

    public void setFunction(List<String> function) {
        this.function = function;
    }

    public List<String> getAll() {
        List<String> all = new ArrayList<>();
        Stream.of(admin, resource, routine, query, function)
            .filter(Objects::nonNull)
            .forEach(all::addAll);
        return all;
    }
}
