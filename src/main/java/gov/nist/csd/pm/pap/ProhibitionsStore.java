package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.policy.Prohibitions;
import gov.nist.csd.pm.policy.exceptions.*;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.GraphStore.checkAccessRightsValid;

public interface ProhibitionsStore extends Prohibitions {

    /**
     * See {@link Prohibitions#create(String, ProhibitionSubject, AccessRightSet, boolean, ContainerCondition...)} <p>
     *
     * @throws ProhibitionExistsException If a prohibition with the given name already exists.
     * @throws ProhibitionSubjectDoesNotExistException If the defined prohibition subject does not exist.
     * @throws ProhibitionContainerDoesNotExistException If a defined container does not exist.
     * @throws UnknownAccessRightException If a provided access right is unknown.
     * @throws PMBackendException If there is an error executing the command in the PIP.
     */
    @Override
    void create(String name, ProhibitionSubject subject, AccessRightSet accessRightSet,
                                boolean intersection, ContainerCondition... containerConditions)
            throws ProhibitionExistsException, ProhibitionSubjectDoesNotExistException,
            ProhibitionContainerDoesNotExistException, UnknownAccessRightException, PMBackendException;

    /**
     * See {@link Prohibitions#update(String, ProhibitionSubject, AccessRightSet, boolean, ContainerCondition...)} <p>
     *
     * @throws ProhibitionDoesNotExistException If a prohibition with the given name does not exist.
     * @throws ProhibitionSubjectDoesNotExistException If the defined prohibition subject does not exist.
     * @throws ProhibitionContainerDoesNotExistException If a defined container does not exist.
     * @throws UnknownAccessRightException If a provided access right is unknown.
     * @throws PMBackendException If there is an error executing the command in the PIP.
     */
    @Override
    void update(String name, ProhibitionSubject subject, AccessRightSet accessRightSet,
                                boolean intersection, ContainerCondition... containerConditions)
            throws ProhibitionDoesNotExistException, ProhibitionSubjectDoesNotExistException,
            ProhibitionContainerDoesNotExistException, UnknownAccessRightException, PMBackendException;

    /**
     * See {@link Prohibitions#delete(String)} <p>
     *
     * @throws PMBackendException If there is an error executing the command in the PIP.
     */
    @Override
    void delete(String name) throws PMBackendException;

    /**
     * See {@link Prohibitions#getAll()} <p>
     *
     * @throws PMBackendException If there is an error executing the command in the PIP.
     */
    @Override
    Map<String, List<Prohibition>> getAll() throws PMBackendException;

    /**
     * See {@link Prohibitions#exists(String)} <p>
     *
     * @throws PMBackendException If there is an error executing the command in the PIP.
     */
    @Override
    boolean exists(String name) throws PMBackendException;

    /**
     * See {@link Prohibitions#getWithSubject(String)} <p>
     *
     * @throws PMBackendException If there is an error executing the command in the PIP.
     */
    @Override
    List<Prohibition> getWithSubject(String subject) throws PMBackendException;

    /**
     * See {@link Prohibitions#get(String)} <p>
     *
     * @throws PMBackendException If there is an error executing the command in the PIP.
     */
    @Override
    Prohibition get(String name) throws ProhibitionDoesNotExistException, PMBackendException;

    /**
     * Check the prohibition being created.
     *
     * @param graph The GraphStore used to check the subject and containers exist.
     * @param name The name of the prohibition.
     * @param subject The subject of the prohibition.
     * @param accessRightSet The denied access rights.
     * @param intersection The boolean flag indicating an evaluation of the union or intersection of the container conditions.
     * @param containerConditions The prohibition container conditions.
     * @throws PMBackendException If there is an error executing the command in the PIP.
     * @throws ProhibitionExistsException If there already exists a prohibition with the same name.
     * @throws UnknownAccessRightException If a provided access right is not known to the policy.
     * @throws ProhibitionSubjectDoesNotExistException If the subject node does not exist.
     * @throws ProhibitionContainerDoesNotExistException If the container node does not exist.
     */
    default void checkCreateInput(GraphStore graph, String name, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection , ContainerCondition ... containerConditions)
    throws PMBackendException, ProhibitionExistsException, UnknownAccessRightException, ProhibitionSubjectDoesNotExistException, ProhibitionContainerDoesNotExistException {
        if (exists(name)) {
            throw new ProhibitionExistsException(name);
        }

        // check the prohibition parameters are valid
        checkAccessRightsValid(graph.getResourceAccessRights(), accessRightSet);
        checkProhibitionSubjectExists(graph, subject);
        checkProhibitionContainersExist(graph, containerConditions);
    }

    /**
     * Check the prohibition being updated.
     *
     * @param graph The GraphStore used to check the subject and containers exist.
     * @param name The name of the prohibition.
     * @param subject The subject of the prohibition.
     * @param accessRightSet The denied access rights.
     * @param intersection The boolean flag indicating an evaluation of the union or intersection of the container conditions.
     * @param containerConditions The prohibition container conditions.
     * @throws PMBackendException If there is an error executing the command in the PIP.
     * @throws ProhibitionDoesNotExistException If the prohibition doesn't exist.
     * @throws UnknownAccessRightException If a provided access right is not known to the policy.
     * @throws ProhibitionSubjectDoesNotExistException If the subject node does not exist.
     * @throws ProhibitionContainerDoesNotExistException If the container node does not exist.
     */
    default void checkUpdateInput(GraphStore graph, String name, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection , ContainerCondition ... containerConditions)
    throws PMBackendException, UnknownAccessRightException, ProhibitionSubjectDoesNotExistException, ProhibitionContainerDoesNotExistException, ProhibitionDoesNotExistException {
        if (!exists(name)) {
            throw new ProhibitionDoesNotExistException(name);
        }

        // check the prohibition parameters are valid
        checkAccessRightsValid(graph.getResourceAccessRights(), accessRightSet);
        checkProhibitionSubjectExists(graph, subject);
        checkProhibitionContainersExist(graph, containerConditions);
    }

    /**
     * Check if the prohibition exists. If it doesn't, return false to indicate to the caller that execution should not
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
     * @param name The obligation id.
     * @throws PMBackendException If there is an error in the backend implementation.
     * @throws ProhibitionDoesNotExistException If the prohibition does not exist.
     */
    default void checkGetInput(String id) throws PMBackendException, ProhibitionDoesNotExistException {
        if (!exists(id)) {
            throw new ProhibitionDoesNotExistException(id);
        }
    }

    default void checkProhibitionSubjectExists(GraphStore graph, ProhibitionSubject subject)
    throws ProhibitionSubjectDoesNotExistException, PMBackendException {
        if (subject.getType() != ProhibitionSubject.Type.PROCESS) {
            if (!graph.nodeExists(subject.getName())) {
                throw new ProhibitionSubjectDoesNotExistException(subject.getName());
            }
        }
    }

    default void checkProhibitionContainersExist(GraphStore graph, ContainerCondition ... containerConditions)
    throws ProhibitionContainerDoesNotExistException, PMBackendException {
        for (ContainerCondition container : containerConditions) {
            if (!graph.nodeExists(container.name())) {
                throw new ProhibitionContainerDoesNotExistException(container.name());
            }
        }
    }
}
