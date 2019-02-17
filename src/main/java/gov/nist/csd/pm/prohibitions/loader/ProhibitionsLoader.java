package gov.nist.csd.pm.prohibitions.loader;

import gov.nist.csd.pm.exceptions.PMDBException;
import gov.nist.csd.pm.exceptions.PMProhibitionException;
import gov.nist.csd.pm.prohibitions.model.Prohibition;

import java.util.List;

public interface ProhibitionsLoader {

    /**
     * Load prohibitions from a data source into a List of Prohibition objects.
     * @return a list of the prohibitions loaded.
     * @throws PMDBException if there is an error loading the prohibitions from a database.
     * @throws PMProhibitionException if there is an exception loading the prohibitions into memory.
     */
    List<Prohibition> loadProhibitions() throws PMDBException, PMProhibitionException;
}
