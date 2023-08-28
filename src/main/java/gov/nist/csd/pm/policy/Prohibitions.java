package gov.nist.csd.pm.policy;

import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;

import java.util.List;
import java.util.Map;

public interface Prohibitions {

    /**
     * Create a new prohibition.
     *
     * @param name the identifier of this prohibition.
     * @param subject ths subject of the prohibition (user, user attribute, or process).
     * @param accessRightSet the access rights to be denied
     * @param intersection a boolean flag that determines if the intersection of the containers should be denied or not.
     * @param containerConditions the containers to deny the subject access to.
     * @throws PMException If there is an error during the execution process at the implementation level.
     */
    void create(String name, ProhibitionSubject subject, AccessRightSet accessRightSet,
                boolean intersection, ContainerCondition... containerConditions) throws PMException;

    /**
     * Update an existing prohibition.
     *
     * @param name the identifier of this prohibition.
     * @param subject ths subject of the prohibition (user, user attribute, or process).
     * @param accessRightSet the access rights to be denied
     * @param intersection a boolean flag that determines if the intersection of the containers should be denied or not.
     * @param containerConditions the containers to deny the subject access to.
     * @throws PMException If there is an error during the execution process at the implementation level.
     */
    void update(String name, ProhibitionSubject subject, AccessRightSet accessRightSet,
                boolean intersection, ContainerCondition ... containerConditions) throws PMException;

    /**
     * Delete the prohibition.
     *
     * @param name The name of the prohibition to delete.
     * @throws PMException If there is an error during the execution process at the implementation level.
     */
    void delete(String name) throws PMException;

    /**
     * Get all prohibitions.
     *
     * @return All prohibitions.
     * @throws PMException If there is an error during the execution process at the implementation level.
     */
    Map<String, List<Prohibition>> getAll() throws PMException;

    /**
     * Check if a prohibition exists with the given name.
     *
     * @param name The name of the prohibition to check.
     * @return True if a prohibition exists with the given name, false otherwise.
     * @throws PMException If there is an error during the execution process at the implementation level.
     */
    boolean exists(String name) throws PMException;

    /**
     * Get prohibitions with the given subject.
     * @param subject The subject to get the prohibitions for (user, user attribute, process)
     * @return The prohibitions with the given subject.
     * @throws PMException If there is an error during the execution process at the implementation level.
     */
    List<Prohibition> getWithSubject(String subject) throws PMException;

    /**
     * Get the prohibition with the given name.
     * @param name The public abstract of the prohibition to get.
     * @return The prohibition with the given public abstract.
     * @throws PMException If there is an error during the execution process at the implementation level.
     */
    Prohibition get(String name) throws PMException;

}
