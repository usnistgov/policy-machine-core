package gov.nist.ngac.pm.core.pap.store;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.prohibition.Prohibition;
import gov.nist.ngac.pm.core.common.tx.Transactional;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import java.util.Collection;
import java.util.Set;

/**
 * The persistence layer for prohibitions.
 */
public interface ProhibitionsStore extends Transactional {

    void createNodeProhibition(String name,
                               long nodeId,
                               AccessRightSet accessRightSet,
                               Set<Long> inclusionSet,
                               Set<Long> exclusionSet,
                               boolean isConjunctive) throws PMException;
    void createProcessProhibition(String name,
                                  long userId,
                                  String process,
                                  AccessRightSet accessRightSet,
                                  Set<Long> inclusionSet,
                                  Set<Long> exclusionSet,
                                  boolean isConjunctive) throws PMException;
    void deleteProhibition(String name) throws PMException;

    Collection<Prohibition> getAllProhibitions() throws PMException;
    Collection<Prohibition> getNodeProhibitions(long nodeId) throws PMException;
    Collection<Prohibition> getProcessProhibitions(String process) throws PMException;
    Prohibition getProhibition(String name) throws PMException;
    boolean prohibitionExists(String name) throws PMException;
}
