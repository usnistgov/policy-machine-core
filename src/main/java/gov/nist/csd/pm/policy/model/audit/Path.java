package gov.nist.csd.pm.policy.model.audit;

import gov.nist.csd.pm.policy.model.graph.relationships.Association;

import java.util.ArrayList;
import java.util.List;

public class Path {
    private List<String> userDagPath;
    private List<String> targetDagPath;
    private Association association;

    public Path() {
        userDagPath = new ArrayList<>();
        targetDagPath = new ArrayList<>();
        association = new Association();
    }

    public Path(List<String> userDagPath, List<String> targetDagPath, Association association) {
        this.userDagPath = userDagPath;
        this.targetDagPath = targetDagPath;
        this.association = association;
    }

    public List<String> getUserDagPath() {
        return userDagPath;
    }

    public void setUserDagPath(List<String> userDagPath) {
        this.userDagPath = userDagPath;
    }

    public List<String> getTargetDagPath() {
        return targetDagPath;
    }

    public void setTargetDagPath(List<String> targetDagPath) {
        this.targetDagPath = targetDagPath;
    }

    public Association getAssociation() {
        return association;
    }

    public void setAssociation(Association association) {
        this.association = association;
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Path)) {
            return false;
        }

        Path p = (Path)o;
        return this.userDagPath.equals(p.userDagPath) && this.targetDagPath.equals(p.targetDagPath)
                && this.association.equals(p.association);
    }
}
