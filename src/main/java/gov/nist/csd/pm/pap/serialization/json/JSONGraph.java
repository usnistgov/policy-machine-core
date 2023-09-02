package gov.nist.csd.pm.pap.serialization.json;

import gov.nist.csd.pm.policy.model.access.AccessRightSet;

import java.util.List;
import java.util.Map;

public class JSONGraph {

    AccessRightSet resourceAccessRights;
    List<JSONPolicyClass> policyClasses;
    List<JSONUserOrObject> users;
    List<JSONUserOrObject> objects;

    public JSONGraph() {
    }

    public JSONGraph(AccessRightSet resourceAccessRights, List<JSONPolicyClass> policyClasses,
                     List<JSONUserOrObject> users,
                     List<JSONUserOrObject> objects) {
        this.resourceAccessRights = resourceAccessRights;
        this.policyClasses = policyClasses;
        this.users = users;
        this.objects = objects;
    }

    public AccessRightSet getResourceAccessRights() {
        return resourceAccessRights;
    }

    public void setResourceAccessRights(AccessRightSet resourceAccessRights) {
        this.resourceAccessRights = resourceAccessRights;
    }

    public List<JSONPolicyClass> getPolicyClasses() {
        return policyClasses;
    }

    public void setPolicyClasses(List<JSONPolicyClass> policyClasses) {
        this.policyClasses = policyClasses;
    }

    public List<JSONUserOrObject> getUsers() {
        return users;
    }

    public void setUsers(List<JSONUserOrObject> users) {
        this.users = users;
    }

    public List<JSONUserOrObject> getObjects() {
        return objects;
    }

    public void setObjects(List<JSONUserOrObject> objects) {
        this.objects = objects;
    }
}
