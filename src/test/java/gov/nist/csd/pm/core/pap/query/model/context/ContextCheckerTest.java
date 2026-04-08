package gov.nist.csd.pm.core.pap.query.model.context;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gov.nist.csd.pm.core.common.exception.NodeDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.NodeType;
import gov.nist.csd.pm.core.impl.memory.pap.store.MemoryPolicyStore;
import gov.nist.csd.pm.core.pap.store.GraphStore;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ContextCheckerTest {

    private GraphStore graphStore;

    @BeforeEach
    void setup() throws PMException {
        MemoryPolicyStore store = new MemoryPolicyStore();
        graphStore = store.graph();
        graphStore.createNode(1, "u1", NodeType.U);
        graphStore.createNode(2, "ua1", NodeType.UA);
        graphStore.createNode(3, "o1", NodeType.O);
        graphStore.createNode(4, "oa1", NodeType.OA);
    }

    @Nested
    class CheckUserContextExists {

        @Test
        void userIdContext_existingNode_noException() {
            assertDoesNotThrow(() -> ContextChecker.checkUserContextExists(new IdUserContext(1), graphStore));
        }

        @Test
        void userIdContext_missingNode_throws() {
            assertThrows(NodeDoesNotExistException.class,
                () -> ContextChecker.checkUserContextExists(new IdUserContext(-1), graphStore));
        }

        @Test
        void usernameContext_existingNode_noException() {
            assertDoesNotThrow(() -> ContextChecker.checkUserContextExists(new NameUserContext("u1"), graphStore));
        }

        @Test
        void usernameContext_missingNode_throws() {
            assertThrows(NodeDoesNotExistException.class,
                () -> ContextChecker.checkUserContextExists(new NameUserContext("u2"), graphStore));
        }

        @Test
        void attributeIdsContext_allExist_noException() {
            assertDoesNotThrow(() -> ContextChecker.checkUserContextExists(new AttributeIdsUserContext(List.of(2L)), graphStore));
        }

        @Test
        void attributeIdsContext_oneMissing_throws() {
            assertThrows(NodeDoesNotExistException.class,
                () -> ContextChecker.checkUserContextExists(new AttributeIdsUserContext(List.of(2L, -1L)), graphStore));
        }

        @Test
        void attributeNamesContext_allExist_noException() {
            assertDoesNotThrow(() -> ContextChecker.checkUserContextExists(new AttributeNamesUserContext(List.of("ua1")), graphStore));
        }

        @Test
        void attributeNamesContext_oneMissing_throws() {
            assertThrows(NodeDoesNotExistException.class,
                () -> ContextChecker.checkUserContextExists(new AttributeNamesUserContext(List.of("ua1", "ua2")), graphStore));
        }

        @Test
        void compositeUserContext_allExist_noException() {
            assertDoesNotThrow(() -> ContextChecker.checkUserContextExists(
                new ConjunctiveUserContext(List.of(new IdUserContext(1), new NameUserContext("ua1"))),
                graphStore));
        }

        @Test
        void compositeUserContext_oneMissing_throws() {
            assertThrows(NodeDoesNotExistException.class,
                () -> ContextChecker.checkUserContextExists(
                    new ConjunctiveUserContext(List.of(new IdUserContext(1), new IdUserContext(-1))),
                    graphStore));
        }
    }

    @Nested
    class CheckTargetContextExists {

        @Test
        void targetIdContext_existingNode_noException() {
            assertDoesNotThrow(() -> ContextChecker.checkTargetContextExists(new IdTargetContext(3), graphStore));
        }

        @Test
        void targetIdContext_missingNode_throws() {
            assertThrows(NodeDoesNotExistException.class,
                () -> ContextChecker.checkTargetContextExists(new IdTargetContext(-1), graphStore));
        }

        @Test
        void targetNameContext_existingNode_noException() {
            assertDoesNotThrow(() -> ContextChecker.checkTargetContextExists(new NameTargetContext("o1"), graphStore));
        }

        @Test
        void targetNameContext_missingNode_throws() {
            assertThrows(NodeDoesNotExistException.class,
                () -> ContextChecker.checkTargetContextExists(new NameTargetContext("u2"), graphStore));
        }

        @Test
        void targetAttributeIdsContext_allExist_noException() {
            assertDoesNotThrow(() -> ContextChecker.checkTargetContextExists(new AttributeIdsTargetContext(List.of(4L)), graphStore));
        }

        @Test
        void targetAttributeIdsContext_oneMissing_throws() {
            assertThrows(NodeDoesNotExistException.class,
                () -> ContextChecker.checkTargetContextExists(new AttributeIdsTargetContext(List.of(4L, -1L)), graphStore));
        }

        @Test
        void targetAttributeNamesContext_allExist_noException() {
            assertDoesNotThrow(() -> ContextChecker.checkTargetContextExists(new AttributeNamesTargetContext(List.of("oa1")), graphStore));
        }

        @Test
        void targetAttributeNamesContext_oneMissing_throws() {
            assertThrows(NodeDoesNotExistException.class,
                () -> ContextChecker.checkTargetContextExists(new AttributeNamesTargetContext(List.of("oa1", "oa2")), graphStore));
        }
    }
}
