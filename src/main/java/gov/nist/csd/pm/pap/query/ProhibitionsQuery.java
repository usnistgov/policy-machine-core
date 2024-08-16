package gov.nist.csd.pm.pap.query;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.prohibition.Prohibition;

import java.util.Collection;
import java.util.Map;

public interface ProhibitionsQuery {

    /**
     * Get all prohibitions.
     *
     * @return All prohibitions.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    Map<String, Collection<Prohibition>> getProhibitions() throws PMException;

    /**
     * Get prohibitions with the given subject.
     *
     * @param subject The subject to get the prohibitions for (user, user attribute, process)
     * @return The prohibitions with the given subject.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    Collection<Prohibition> getProhibitionsWithSubject(String subject) throws PMException;

    /**
     * Get the prohibition with the given name.
     * @param name The public abstract of the prohibition to get.
     * @return The prohibition with the given public abstract.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    Prohibition getProhibition(String name) throws PMException;

    Collection<Prohibition> getInheritedProhibitionsFor(String subject) throws PMException;
    Collection<Prohibition> getProhibitionsWithContainer(String container) throws PMException;

}
