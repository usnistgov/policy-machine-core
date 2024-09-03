package gov.nist.csd.pm.pap.serialization.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        if (!(o instanceof JSONGraph)) return false;
        JSONGraph jsonGraph = (JSONGraph) o;
        return Objects.equals(pcs, jsonGraph.pcs) && Objects.equals(uas, jsonGraph.uas) && Objects.equals(oas, jsonGraph.oas) && Objects.equals(users, jsonGraph.users) && Objects.equals(objects, jsonGraph.objects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pcs, uas, oas, users, objects);
    }
}
