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

package gov.nist.ngac.pm.core.pap.serialization.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * JSON DTO for the policy's operations, grouped by kind (admin, resource, routine, query, function),
 * each stored as its PML source text.
 */
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

    /**
     * Returns every operation's PML source, across all kinds, in a single flat list.
     */
    public List<String> getAll() {
        List<String> all = new ArrayList<>();
        Stream.of(admin, resource, routine, query, function)
            .filter(Objects::nonNull)
            .forEach(all::addAll);
        return all;
    }
}
