package gov.nist.csd.pm.prohibitions;

import gov.nist.csd.pm.exceptions.PMDBException;
import gov.nist.csd.pm.exceptions.PMProhibitionException;
import gov.nist.csd.pm.prohibitions.loader.ProhibitionsLoader;
import gov.nist.csd.pm.prohibitions.model.Prohibition;

import java.util.List;

/**
 * An in memory implementation of the ProhibitionsDAO interface, that stores prohibitions in a list.
 */
public class MemProhibitionsDAO implements ProhibitionsDAO {

    /**
     * Data structure to store prohibitions.
     */
    public List<Prohibition> prohibitions;

    /**
     * Create a new in-memory prohibitions DAO.  The provided loader will load ny prohibitions from a database.
     * @param loader the ProhibitionsLoader to load any existing prohibitions form a database into memory.
     * @throws PMDBException if there is an error retrieving the prohibitions from the database.
     * @throws PMProhibitionException if there is an error representing the data in the database as a Prohibition object.
     */
    public MemProhibitionsDAO(ProhibitionsLoader loader) throws PMProhibitionException, PMDBException {
        prohibitions = loader.loadProhibitions();
    }

    /**
     * Add the provided prohibition to the list of prohibitions.
     * @param prohibition the prohibition to be created.
     */
    @Override
    public void createProhibition(Prohibition prohibition) {
        prohibitions.add(prohibition);
    }

    /**
     * @return the list of prohibition objects.
     */
    @Override
    public List<Prohibition> getProhibitions() {
        return prohibitions;
    }

    /**
     * @param prohibitionName the name of the Prohibition to retrieve.
     * @return the prohibition with the given name.  If one does not exist, return null.
     */
    @Override
    public Prohibition getProhibition(String prohibitionName) {
        for(Prohibition prohibition : prohibitions) {
            if(prohibition.getName().equals(prohibitionName)) {
                return prohibition;
            }
        }
        return null;
    }

    /**
     * Update an existing prohibition with the same name as the provided prohibition.
     * @param prohibition the prohibition to update.
     */
    @Override
    public void updateProhibition(Prohibition prohibition) {
        for(int i = 0; i < prohibitions.size(); i++) {
            Prohibition p = prohibitions.get(i);
            if(p.getName().equals(prohibition.getName())) {
                prohibitions.set(i, prohibition);
            }
        }
    }

    /**
     * Remove the prohibition with the given name from the list.
     * @param prohibitionName the name of the prohibition to delete.
     */
    @Override
    public void deleteProhibition(String prohibitionName) {
        //find the prohibition with the given name and remove it from the list
        prohibitions.removeIf((prohibition) -> prohibition.getName().equals(prohibitionName));
    }
}
