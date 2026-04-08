package gov.nist.csd.pm.core.pap.query.model.context;

import gov.nist.csd.pm.core.common.exception.NodeDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.store.GraphStore;

public class ContextChecker {

    private ContextChecker() {}

    public static void checkUserContextExists(UserContext ctx, GraphStore graphStore) throws PMException {
        switch (ctx) {
            case IdUserContext c -> {
                if (!graphStore.nodeExists(c.userId())) {
                    throw new NodeDoesNotExistException(c.userId());
                }
            }
            case NameUserContext c -> {
                if (!graphStore.nodeExists(c.username())) {
                    throw new NodeDoesNotExistException(c.username());
                }
            }
            case AttributeIdsUserContext c -> {
                for (long id : c.attributeIds()) {
                    if (!graphStore.nodeExists(id)) {
                        throw new NodeDoesNotExistException(id);
                    }
                }
            }
            case AttributeNamesUserContext c -> {
                for (String name : c.attributeNames()) {
                    if (!graphStore.nodeExists(name)) {
                        throw new NodeDoesNotExistException(name);
                    }
                }
            }
            case ConjunctiveUserContext c -> {
                for (UserContext sub : c.contexts()) {
                    checkUserContextExists(sub, graphStore);
                }
            }
        }
    }

    public static void checkTargetContextExists(TargetContext ctx, GraphStore graphStore) throws PMException {
        switch (ctx) {
            case IdTargetContext c -> {
                if (!graphStore.nodeExists(c.targetId())) {
                    throw new NodeDoesNotExistException(c.targetId());
                }
            }
            case NameTargetContext c -> {
                if (!graphStore.nodeExists(c.targetName())) {
                    throw new NodeDoesNotExistException(c.targetName());
                }
            }
            case AttributeIdsTargetContext c -> {
                for (long id : c.attributeIds()) {
                    if (!graphStore.nodeExists(id)) {
                        throw new NodeDoesNotExistException(id);
                    }
                }
            }
            case AttributeNamesTargetContext c -> {
                for (String name : c.attributeNames()) {
                    if (!graphStore.nodeExists(name)) {
                        throw new NodeDoesNotExistException(name);
                    }
                }
            }
        }
    }
}
