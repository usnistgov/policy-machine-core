package gov.nist.csd.pm.pap.store;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.prohibition.ContainerCondition;
import gov.nist.csd.pm.pap.prohibition.Prohibition;
import gov.nist.csd.pm.pap.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.pap.tx.Transactional;

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
