package gov.nist.csd.pm.policy;

import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;

public interface ProhibitionsWriter {

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
    void create(String label, ProhibitionSubject subject, AccessRightSet accessRightSet,
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
    void update(String label, ProhibitionSubject subject, AccessRightSet accessRightSet,
                boolean intersection, ContainerCondition ... containerConditions) throws PMException;

    /**
     * Delete the prohibition, and remove it from the data structure.
     *
     * @param label The name of the prohibition to delete.
     * @throws PMException if there is an error deleting the prohibition.
     */
    void delete(String label) throws PMException;

}
