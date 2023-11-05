package gov.nist.csd.pm.policy.serialization.json;

import gov.nist.csd.pm.policy.model.access.AccessRightSet;

import java.util.ArrayList;
import java.util.List;

public class JSONGraph {

    AccessRightSet resourceAccessRights;
    List<JSONPolicyClass> policyClasses;
    List<JSONUserOrObject> users;
    List<JSONUserOrObject> objects;

    public JSONGraph() {
        resourceAccessRights = new AccessRightSet();
        policyClasses = new ArrayList<>();
        users = new ArrayList<>();
        objects = new ArrayList<>();
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
