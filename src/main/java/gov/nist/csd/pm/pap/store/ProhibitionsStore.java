package gov.nist.csd.pm.pap.store;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.common.prohibition.Prohibition;
import gov.nist.csd.pm.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.common.tx.Transactional;

import java.util.Collection;
import java.util.Map;

public interface ProhibitionsStore extends Transactional {

    void createProhibition(String name,
                           ProhibitionSubject subject,
                           AccessRightSet accessRightSet,
                           boolean intersection,
                           Collection<ContainerCondition> containerConditions) throws PMException;
    void deleteProhibition(String name) throws PMException;

    Map<String, Collection<Prohibition>> getProhibitions() throws PMException;
    Prohibition getProhibition(String name) throws PMException;
    boolean prohibitionExists(String name) throws PMException;
}
