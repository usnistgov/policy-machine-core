package gov.nist.csd.pm.core.pap.query.model.context;

import gov.nist.csd.pm.core.common.exception.NodeDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.store.GraphStore;

public class ContextChecker {

    private ContextChecker() {}

    public static void checkUserContextExists(UserContext ctx, GraphStore graphStore) throws PMException {
        switch (ctx) {
            case UserIdContext c -> {
                if (!graphStore.nodeExists(c.userId())) {
                    throw new NodeDoesNotExistException(c.userId());
                }
            }
            case UsernameContext c -> {
                if (!graphStore.nodeExists(c.username())) {
                    throw new NodeDoesNotExistException(c.username());
                }
            }
            case AttributeIdsContext c -> {
                for (long id : c.attributeIds()) {
                    if (!graphStore.nodeExists(id)) {
                        throw new NodeDoesNotExistException(id);
                    }
                }
            }
            case AttributeNamesContext c -> {
                for (String name : c.attributeNames()) {
                    if (!graphStore.nodeExists(name)) {
                        throw new NodeDoesNotExistException(name);
                    }
                }
            }
            case CompositeUserContext c -> {
                for (UserContext sub : c.contexts()) {
                    checkUserContextExists(sub, graphStore);
                }
            }
        }
    }

    public static void checkTargetContextExists(TargetContext ctx, GraphStore graphStore) throws PMException {
        switch (ctx) {
            case TargetIdContext c -> {
                if (!graphStore.nodeExists(c.targetId())) {
                    throw new NodeDoesNotExistException(c.targetId());
                }
            }
            case TargetNameContext c -> {
                if (!graphStore.nodeExists(c.targetName())) {
                    throw new NodeDoesNotExistException(c.targetName());
                }
            }
            case TargetAttributeIdsContext c -> {
                for (long id : c.attributeIds()) {
                    if (!graphStore.nodeExists(id)) {
                        throw new NodeDoesNotExistException(id);
                    }
                }
            }
            case TargetAttributeNamesContext c -> {
                for (String name : c.attributeNames()) {
                    if (!graphStore.nodeExists(name)) {
                        throw new NodeDoesNotExistException(name);
                    }
                }
            }
        }
    }
}
