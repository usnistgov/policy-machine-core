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
            assertDoesNotThrow(() -> ContextChecker.checkUserContextExists(new UserIdContext(1), graphStore));
        }

        @Test
        void userIdContext_missingNode_throws() {
            assertThrows(NodeDoesNotExistException.class,
                () -> ContextChecker.checkUserContextExists(new UserIdContext(-1), graphStore));
        }

        @Test
        void usernameContext_existingNode_noException() {
            assertDoesNotThrow(() -> ContextChecker.checkUserContextExists(new UsernameContext("u1"), graphStore));
        }

        @Test
        void usernameContext_missingNode_throws() {
            assertThrows(NodeDoesNotExistException.class,
                () -> ContextChecker.checkUserContextExists(new UsernameContext("u2"), graphStore));
        }

        @Test
        void attributeIdsContext_allExist_noException() {
            assertDoesNotThrow(() -> ContextChecker.checkUserContextExists(new AttributeIdsContext(List.of(2L)), graphStore));
        }

        @Test
        void attributeIdsContext_oneMissing_throws() {
            assertThrows(NodeDoesNotExistException.class,
                () -> ContextChecker.checkUserContextExists(new AttributeIdsContext(List.of(2L, -1L)), graphStore));
        }

        @Test
        void attributeNamesContext_allExist_noException() {
            assertDoesNotThrow(() -> ContextChecker.checkUserContextExists(new AttributeNamesContext(List.of("ua1")), graphStore));
        }

        @Test
        void attributeNamesContext_oneMissing_throws() {
            assertThrows(NodeDoesNotExistException.class,
                () -> ContextChecker.checkUserContextExists(new AttributeNamesContext(List.of("ua1", "ua2")), graphStore));
        }

        @Test
        void compositeUserContext_allExist_noException() {
            assertDoesNotThrow(() -> ContextChecker.checkUserContextExists(
                new CompositeUserContext(List.of(new UserIdContext(1), new UsernameContext("ua1"))),
                graphStore));
        }

        @Test
        void compositeUserContext_oneMissing_throws() {
            assertThrows(NodeDoesNotExistException.class,
                () -> ContextChecker.checkUserContextExists(
                    new CompositeUserContext(List.of(new UserIdContext(1), new UserIdContext(-1))),
                    graphStore));
        }
    }

    @Nested
    class CheckTargetContextExists {

        @Test
        void targetIdContext_existingNode_noException() {
            assertDoesNotThrow(() -> ContextChecker.checkTargetContextExists(new TargetIdContext(3), graphStore));
        }

        @Test
        void targetIdContext_missingNode_throws() {
            assertThrows(NodeDoesNotExistException.class,
                () -> ContextChecker.checkTargetContextExists(new TargetIdContext(-1), graphStore));
        }

        @Test
        void targetNameContext_existingNode_noException() {
            assertDoesNotThrow(() -> ContextChecker.checkTargetContextExists(new TargetNameContext("o1"), graphStore));
        }

        @Test
        void targetNameContext_missingNode_throws() {
            assertThrows(NodeDoesNotExistException.class,
                () -> ContextChecker.checkTargetContextExists(new TargetNameContext("u2"), graphStore));
        }

        @Test
        void targetAttributeIdsContext_allExist_noException() {
            assertDoesNotThrow(() -> ContextChecker.checkTargetContextExists(new TargetAttributeIdsContext(List.of(4L)), graphStore));
        }

        @Test
        void targetAttributeIdsContext_oneMissing_throws() {
            assertThrows(NodeDoesNotExistException.class,
                () -> ContextChecker.checkTargetContextExists(new TargetAttributeIdsContext(List.of(4L, -1L)), graphStore));
        }

        @Test
        void targetAttributeNamesContext_allExist_noException() {
            assertDoesNotThrow(() -> ContextChecker.checkTargetContextExists(new TargetAttributeNamesContext(List.of("oa1")), graphStore));
        }

        @Test
        void targetAttributeNamesContext_oneMissing_throws() {
            assertThrows(NodeDoesNotExistException.class,
                () -> ContextChecker.checkTargetContextExists(new TargetAttributeNamesContext(List.of("oa1", "oa2")), graphStore));
        }
    }
}
