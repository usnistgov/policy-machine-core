package gov.nist.csd.pm.policy.model.audit;

import gov.nist.csd.pm.policy.model.graph.relationships.Association;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Path path = (Path) o;
        return Objects.equals(userDagPath, path.userDagPath) && Objects.equals(
                targetDagPath, path.targetDagPath) && Objects.equals(association, path.association);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userDagPath, targetDagPath, association);
    }

    @Override
    public String toString() {
        return "{" +
                "userPath=" + userDagPath +
                ", targetPath=" + targetDagPath +
                ", association=" + association +
                '}';
    }
}
