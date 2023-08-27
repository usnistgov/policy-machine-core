package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.policy.Obligations;
import gov.nist.csd.pm.policy.exceptions.*;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.obligation.event.EventPattern;
import gov.nist.csd.pm.policy.model.obligation.event.EventSubject;
import gov.nist.csd.pm.policy.model.obligation.event.Target;

import java.util.List;

public interface ObligationsStore extends Obligations {

    /**
     * See {@link Obligations#create(UserContext, String, Rule...)} <p>
     *
     * @throws ObligationIdExistsException If an obligation with the provided name already exists.
     * @throws NodeDoesNotExistException   If any node defined in the provided event patterns does not exist.
     * @throws PMBackendException          If there is an error executing the command in the PIP.
     */
    @Override
    void create(UserContext author, String name, Rule... rules)
    throws ObligationIdExistsException, NodeDoesNotExistException, PMBackendException;

    /**
     * See {@link Obligations#update(UserContext, String, Rule...)} <p>
     *
     * @throws ObligationDoesNotExistException If the obligation does not exist.
     * @throws NodeDoesNotExistException       If any node defined in the provided event patterns does not exist.
     * @throws PMBackendException              If there is an error executing the command in the PIP.
     */
    @Override
    void update(UserContext author, String name, Rule... rules)
    throws ObligationDoesNotExistException, NodeDoesNotExistException, PMBackendException;

    /**
     * See {@link Obligations#delete(String)} <p>
     *
     * @throws PMBackendException If there is an error executing the command in the PIP.
     */
    @Override
    void delete(String name) throws PMBackendException;

    /**
     * See {@link Obligations#getAll()} (String)} <p>
     *
     * @throws PMBackendException If there is an error executing the command in the PIP.
     */
    @Override
    List<Obligation> getAll() throws PMBackendException;

    /**
     * See {@link Obligations#create(UserContext, String, Rule...)} <p>
     *
     * @throws PMBackendException If there is an error executing the command in the PIP.
     */
    @Override
    boolean exists(String name) throws PMBackendException;

    /**
     * See {@link Obligations#create(UserContext, String, Rule...)} <p>
     *
     * @throws ObligationDoesNotExistException If an obligation with the provided name does not exists.
     * @throws PMBackendException              If there is an error executing the command in the PIP.
     */
    @Override
    Obligation get(String name) throws ObligationDoesNotExistException, PMBackendException;

    /**
     * Check the obligation being created.
     *
     * @param graphStore The GraphStore used to check if the author and event pattern policy elements exist.
     * @param author     The author of the obligation.
     * @param name         The name of the obligation.
     * @param rules      The rules of the obligation.
     * @throws PMBackendException          If there is an error in the backend implementation.
     * @throws ObligationIdExistsException If an obligation already exists with the specified name
     * @throws NodeDoesNotExistException   If the author or any specified policy elements in the event patterns don't
     * exist.
     */
    default void checkCreateInput(GraphStore graphStore, UserContext author, String name, Rule... rules)
    throws PMBackendException, ObligationIdExistsException, NodeDoesNotExistException {
        if (exists(name)) {
            throw new ObligationIdExistsException(name);
        }

        checkAuthorExists(graphStore, author);
        checkEventPatternAttributesExist(graphStore, rules);
    }

    /**
     * Check the obligation being created.
     *
     * @param graphStore The GraphStore used to check if the author and event pattern policy elements exist.
     * @param author     The author of the obligation.
     * @param name         The name of the obligation.
     * @param rules      The rules of the obligation.
     * @throws PMBackendException              If there is an error in the backend implementation.
     * @throws ObligationDoesNotExistException If the obligation to update does not exist.
     * @throws NodeDoesNotExistException       If the author or any specified policy elements in the event patterns
     * don't exist.
     */
    default void checkUpdateInput(GraphStore graphStore, UserContext author, String name, Rule... rules)
    throws PMBackendException, ObligationDoesNotExistException, NodeDoesNotExistException {
        if (!exists(name)) {
            throw new ObligationDoesNotExistException(name);
        }

        checkAuthorExists(graphStore, author);
        checkEventPatternAttributesExist(graphStore, rules);
    }

    /**
     * Check if the obligation exists. If it doesn't, return false to indicate to the caller that execution should not
     * proceed.
     *
     * @param name The name of the obligation.
     * @return True if the execution should proceed, false otherwise.
     * @throws PMBackendException If there is an error in the backend implementation.
     */
    default boolean checkDeleteInput(String name) throws PMBackendException {
        if (!exists(name)) {
            return false;
        }

        return true;
    }

    /**
     * Check if the obligation exists.
     * @param name The obligation name.
     * @throws PMBackendException If there is an error in the backend implementation.
     * @throws ObligationDoesNotExistException If the obligation does not exist.
     */
    default void checkGetInput(String name) throws PMBackendException, ObligationDoesNotExistException {
        if (!exists(name)) {
            throw new ObligationDoesNotExistException(name);
        }
    }

    private void checkAuthorExists(GraphStore graph, UserContext author)
    throws NodeDoesNotExistException, PMBackendException {
        if (!graph.nodeExists(author.getUser())) {
            throw new NodeDoesNotExistException(author.getUser());
        }
    }

    private void checkEventPatternAttributesExist(GraphStore graph, Rule... rules)
    throws NodeDoesNotExistException, PMBackendException {
        for (Rule rule : rules) {
            EventPattern event = rule.getEventPattern();

            // check subject
            EventSubject subject = event.getSubject();
            switch (subject.getType()) {
                case USERS -> {
                    for (String user : subject.users()) {
                        if (!graph.nodeExists(user)) {
                            throw new NodeDoesNotExistException(user);
                        }
                    }
                }
                case ANY_USER_WITH_ATTRIBUTE -> {
                    if (!graph.nodeExists(subject.anyUserWithAttribute())) {
                        throw new NodeDoesNotExistException(subject.anyUserWithAttribute());
                    }
                }
            }

            // check target
            Target target = event.getTarget();
            switch (target.getType()) {
                case ANY_OF_SET -> {
                    for (String pe : target.anyOfSet()) {
                        if (!graph.nodeExists(pe)) {
                            throw new NodeDoesNotExistException(pe);
                        }
                    }
                }
                case POLICY_ELEMENT -> {
                    if (!graph.nodeExists(target.policyElement())) {
                        throw new NodeDoesNotExistException(target.policyElement());
                    }
                }
                case ANY_CONTAINED_IN -> {
                    if (!graph.nodeExists(target.anyContainedIn())) {
                        throw new NodeDoesNotExistException(target.anyContainedIn());
                    }
                }
            }
        }
    }
}
