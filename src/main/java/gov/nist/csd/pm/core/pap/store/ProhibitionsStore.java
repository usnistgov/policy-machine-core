package gov.nist.csd.pm.core.pap.store;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.core.common.tx.Transactional;
import java.util.Collection;
import java.util.Map;

public interface ProhibitionsStore extends Transactional {

    void createProhibition(String name,
                           ProhibitionSubject subject,
                           AccessRightSet accessRightSet,
                           boolean intersection,
                           Collection<ContainerCondition> containerConditions) throws PMException;
    void deleteProhibition(String name) throws PMException;

    Map<Long, Collection<Prohibition>> getNodeProhibitions() throws PMException;
    Map<String, Collection<Prohibition>> getProcessProhibitions() throws PMException;
    Prohibition getProhibition(String name) throws PMException;
    boolean prohibitionExists(String name) throws PMException;
    Collection<Prohibition> getProhibitionsWithNode(long subject) throws PMException;
    Collection<Prohibition> getProhibitionsWithProcess(String subject) throws PMException;
}
