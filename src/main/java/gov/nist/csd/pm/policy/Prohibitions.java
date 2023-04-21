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
     * @param label the identifier of this prohibition.
     * @param subject ths subject of the prohibition (user, user attribute, or process).
     * @param accessRightSet the access rights to be denied
     * @param intersection a boolean flag that determines if the intersection of the containers should be denied or not.
     * @param containerConditions the containers to deny the subject access to.
     * @throws PMException
     */
    void createProhibition(String label, ProhibitionSubject subject, AccessRightSet accessRightSet,
                           boolean intersection, ContainerCondition... containerConditions) throws PMException;

    /**
     * Update an existing prohibition.
     *
     * @param label the identifier of this prohibition.
     * @param subject ths subject of the prohibition (user, user attribute, or process).
     * @param accessRightSet the access rights to be denied
     * @param intersection a boolean flag that determines if the intersection of the containers should be denied or not.
     * @param containerConditions the containers to deny the subject access to.
     * @throws PMException
     */
    void updateProhibition(String label, ProhibitionSubject subject, AccessRightSet accessRightSet,
                           boolean intersection, ContainerCondition ... containerConditions) throws PMException;

    /**
     * Delete the prohibition, and remove it from the data structure.
     *
     * @param label The name of the prohibition to delete.
     * @throws PMException
     */
    void deleteProhibition(String label) throws PMException;

    /**
     * Get all prohibitions.
     * @return All prohibitions.
     * @throws PMException
     */
    Map<String, List<Prohibition>> getProhibitions() throws PMException;

    /**
     * Check if a prohibition exists with the given label.
     * @param label The label of the prohibition to check.
     * @return True if a prohibition exists with the given label, false otherwise.
     * @throws PMException
     */
    boolean prohibitionExists(String label) throws PMException;

    /**
     * Get prohibitions with the given subject.
     * @param subject The subject to get the prohibitions for (user, user attribute, process)
     * @return The prohibitions with the given subject.
     * @throws PMException
     */
    List<Prohibition> getProhibitionsWithSubject(String subject) throws PMException;

    /**
     * Get the prohibition with the given label.
     * @param label The label of the prohibition to get.
     * @return The prohibition with the given label.
     * @throws PMException
     */
    Prohibition getProhibition(String label) throws PMException;

}
