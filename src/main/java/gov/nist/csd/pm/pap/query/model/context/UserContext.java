package gov.nist.csd.pm.pap.query.model.context;

import gov.nist.csd.pm.common.exception.NodeDoesNotExistException;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.store.GraphStore;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class UserContext implements Serializable {

    private long userId;
    private long[] attributeIds;
    private String process;

    public UserContext(long userId, String process) {
        this.userId = userId;
        this.process = process;
    }

    public UserContext(long userId) {
        this.userId = userId;
    }

    public UserContext(long[] attributeIds, String process) {
        this.attributeIds = attributeIds;
        this.process = process;
    }

    public UserContext(long[] attributeIds) {
        this.attributeIds = attributeIds;
    }

    public long getUser() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long[] getAttributeIds() {
        return attributeIds;
    }

    public void setAttributeIds(long[] attributeIds) {
        this.attributeIds = attributeIds;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public boolean isUserDefined() {
        return userId != 0;
    }

    public void checkExists(GraphStore graphStore) throws PMException {
        if (isUserDefined()) {
            if (!graphStore.nodeExists(userId)) {
                throw new NodeDoesNotExistException(userId);
            }
        } else {
            for (long attribute : attributeIds) {
                if (!graphStore.nodeExists(attribute)) {
                    throw new NodeDoesNotExistException(attribute);
                }
            }
        }
    }

    @Override
    public String toString() {
        String s = "%s";
        if (process != null) {
            s += ", process=" + process + "]";
        }

        if (isUserDefined()) {
            return String.format(s, "user=" + userId);
        } else {
            return String.format(s, "attributes=" + attributeIds);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserContext that)) return false;
        return Objects.equals(userId, that.userId) && Objects.equals(attributeIds, that.attributeIds) && Objects.equals(process, that.process);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, attributeIds, process);
    }
}