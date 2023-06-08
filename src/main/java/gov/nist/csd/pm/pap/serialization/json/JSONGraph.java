package gov.nist.csd.pm.pap.serialization.json;

import java.util.Map;

public class JSONGraph {

    Map<String, JSONPolicyClass> pcs;
    Map<String, JSONNode> uas;
    Map<String, JSONNode> oas;
    Map<String, JSONNode> users;
    Map<String, JSONNode> objects;

    public JSONGraph(Map<String, JSONPolicyClass> pcs,
                     Map<String, JSONNode> uas,
                     Map<String, JSONNode> oas,
                     Map<String, JSONNode> users,
                     Map<String, JSONNode> objects) {
        this.pcs = pcs;
        this.uas = uas;
        this.oas = oas;
        this.users = users;
        this.objects = objects;
    }

    public Map<String, JSONPolicyClass> getPcs() {
        return pcs;
    }

    public void setPcs(Map<String, JSONPolicyClass> pcs) {
        this.pcs = pcs;
    }

    public Map<String, JSONNode> getUas() {
        return uas;
    }

    public void setUas(Map<String, JSONNode> uas) {
        this.uas = uas;
    }

    public Map<String, JSONNode> getOas() {
        return oas;
    }

    public void setOas(Map<String, JSONNode> oas) {
        this.oas = oas;
    }

    public Map<String, JSONNode> getUsers() {
        return users;
    }

    public void setUsers(Map<String, JSONNode> users) {
        this.users = users;
    }

    public Map<String, JSONNode> getObjects() {
        return objects;
    }

    public void setObjects(Map<String, JSONNode> objects) {
        this.objects = objects;
    }
}
