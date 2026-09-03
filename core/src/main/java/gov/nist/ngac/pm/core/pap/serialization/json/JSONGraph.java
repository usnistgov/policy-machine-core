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

/**
 * JSON DTO for the graph, with nodes grouped by type.
 */
public class JSONGraph {

    List<JSONNode> pcs;
    List<JSONNode> uas;
    List<JSONNode> oas;
    List<JSONNode> users;
    List<JSONNode> objects;

    public JSONGraph(List<JSONNode> pcs,
                     List<JSONNode> uas,
                     List<JSONNode> oas,
                     List<JSONNode> users,
                     List<JSONNode> objects) {
        this.pcs = pcs;
        this.uas = uas;
        this.oas = oas;
        this.users = users;
        this.objects = objects;
    }

    public JSONGraph() {
        this.pcs = new ArrayList<>();
        this.uas = new ArrayList<>();
        this.oas = new ArrayList<>();
        this.users = new ArrayList<>();
        this.objects = new ArrayList<>();
    }

    public List<JSONNode> getPcs() {
        return pcs;
    }

    public void setPcs(List<JSONNode> pcs) {
        this.pcs = pcs;
    }

    public List<JSONNode> getUas() {
        return uas;
    }

    public void setUas(List<JSONNode> uas) {
        this.uas = uas;
    }

    public List<JSONNode> getOas() {
        return oas;
    }

    public void setOas(List<JSONNode> oas) {
        this.oas = oas;
    }

    public List<JSONNode> getUsers() {
        return users;
    }

    public void setUsers(List<JSONNode> users) {
        this.users = users;
    }

    public List<JSONNode> getObjects() {
        return objects;
    }

    public void setObjects(List<JSONNode> objects) {
        this.objects = objects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JSONGraph jsonGraph)) return false;
        return Objects.equals(pcs, jsonGraph.pcs) && Objects.equals(uas, jsonGraph.uas) && Objects.equals(oas, jsonGraph.oas) && Objects.equals(users, jsonGraph.users) && Objects.equals(objects, jsonGraph.objects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pcs, uas, oas, users, objects);
    }
}
