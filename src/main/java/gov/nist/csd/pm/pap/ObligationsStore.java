package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.policy.Obligations;
import gov.nist.csd.pm.policy.exceptions.*;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.obligation.event.EventPattern;
import gov.nist.csd.pm.policy.model.obligation.event.subject.Subject;
import gov.nist.csd.pm.policy.model.obligation.event.subject.UserAttributesSubject;
import gov.nist.csd.pm.policy.model.obligation.event.subject.UsersSubject;
import gov.nist.csd.pm.policy.model.obligation.event.target.AnyInIntersectionTarget;
import gov.nist.csd.pm.policy.model.obligation.event.target.AnyInUnionTarget;
import gov.nist.csd.pm.policy.model.obligation.event.target.OnTargets;
import gov.nist.csd.pm.policy.model.obligation.event.target.Target;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ObligationsStore extends the {@link Obligations} interface and outlines how a concrete implementation of the interface
 * at the Policy Administration Point (PAP) level of the Policy Machine should behave including input validation and
 * expected exceptions.
 */
public interface ObligationsStore extends Obligations {

    /**
     * Create a new obligation with the given author, name, and rules. The author of the obligation is the user that the
     * responses will be executed as in the EPP. This means the author will need the privileges to carry out each action
     * in the response at the time it's executed. If they do not have sufficient privileges no action in the response
     * will be executed. <p>
     *
     * See {@link Obligations#create(UserContext, String, Rule...)} <p>
     *
     * @throws ObligationNameExistsException If an obligation with the provided name already exists.
     * @throws NodeDoesNotExistException   If the author or any node defined in the provided event patterns does not exist.
     * @throws PMBackendException          If there is an error executing the command in the PIP.
     */
    @Override
    void create(UserContext author, String name, Rule... rules)
            throws ObligationNameExistsException, NodeDoesNotExistException, PMBackendException;

    /**
     * Update the author and rules of the obligation with the given name. This will overwrite any existing rules to the rules
     * provided and update the existing author. <p>
     *
     * See {@link Obligations#update(UserContext, String, Rule...)} <p>
     *
     * @throws ObligationDoesNotExistException If the obligation does not exist.
     * @throws ObligationRuleNameExistsException If two rules have the same name.
     * @throws NodeDoesNotExistException       If any node defined in the provided event patterns does not exist.
     * @throws PMBackendException              If there is an error executing the command in the PIP.
     */
    @Override
    void update(UserContext author, String name, Rule... rules)
            throws ObligationDoesNotExistException, ObligationRuleNameExistsException, NodeDoesNotExistException, PMBackendException;

    /**
     * Delete the obligation with the given name. If the obligation does not exist, no exception is thrown as this is
     * the desired state. <p>
     *
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
     * @throws ObligationNameExistsException If an obligation already exists with the specified name
     * @throws NodeDoesNotExistException   If the author or any specified policy elements in the event patterns don't
     * exist.
     */
    default void checkCreateInput(GraphStore graphStore, UserContext author, String name, Rule... rules)
            throws PMBackendException, ObligationNameExistsException, NodeDoesNotExistException {
        if (exists(name)) {
            throw new ObligationNameExistsException(name);
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
            throws PMBackendException, ObligationDoesNotExistException, NodeDoesNotExistException, ObligationRuleNameExistsException {
        if (!exists(name)) {
            throw new ObligationDoesNotExistException(name);
        }

        // check that there are no duplicate rule names
        Set<String> ruleNames = new HashSet<>();
        for (Rule rule : rules) {
            if (ruleNames.contains(rule.getName())) {
                throw new ObligationRuleNameExistsException(name, rule.getName());
            }

            ruleNames.add(rule.getName());
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
            Subject subject = event.getSubject();
            if (subject instanceof UsersSubject ||
                    subject instanceof UserAttributesSubject) {
                for (String sub : subject.getSubjects()) {
                    if (!graph.nodeExists(sub)) {
                        throw new NodeDoesNotExistException(sub);
                    }
                }
            }

            // check target
            Target target = event.getTarget();
            if (target instanceof AnyInUnionTarget ||
                    target instanceof AnyInIntersectionTarget ||
                    target instanceof OnTargets) {
                for (String tar : target.getTargets()) {
                    if (!graph.nodeExists(tar)) {
                        throw new NodeDoesNotExistException(tar);
                    }
                }
            }
        }
    }
}
