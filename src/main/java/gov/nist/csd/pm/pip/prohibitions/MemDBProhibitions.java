package gov.nist.csd.pm.pip.prohibitions;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.memory.MemProhibitions;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;

import java.util.List;

public class MemDBProhibitions implements Prohibitions{

    private Prohibitions prohibitions;
    private Prohibitions copy_prohibitions;

    public MemDBProhibitions (Prohibitions prohibitions) throws PMException {
        if (!(prohibitions instanceof MemProhibitions)) {
            this.copy_prohibitions = prohibitions;
            MemProhibitions memProhibitions = new MemProhibitions();
            String json = ProhibitionsSerializer.toJson(prohibitions);
            this.prohibitions = ProhibitionsSerializer.fromJson(memProhibitions, json);
        } else {
            this.prohibitions = prohibitions;
        }
    }
    /**
     * Create a new prohibition.
     *
     * @param prohibition The prohibition to be created.
     * @throws PMException if there is an error creating a prohibition.
     */
    @Override
    public void add(Prohibition prohibition) throws PMException {
        if (this.copy_prohibitions != null) {
            this.copy_prohibitions.add(prohibition);
        }
        this.prohibitions.add(prohibition);
    }

    /**
     * Get a list of all prohibitions
     *
     * @return a list of all prohibitions
     * @throws PMException if there is an error getting the prohibitions.
     */
    @Override
    public List<Prohibition> getAll() throws PMException {
        return this.prohibitions.getAll();
    }

    /**
     * Retrieve a Prohibition and return the Object representing it.
     *
     * @param prohibitionName The name of the Prohibition to retrieve.
     * @return the Prohibition with the given name.
     * @throws PMException if there is an error getting the prohibition with the given name.
     */
    @Override
    public Prohibition get(String prohibitionName) throws PMException {
        return this.prohibitions.get(prohibitionName);
    }

    /**
     * Get all of the prohibitions a given entity is the direct subject of.  The subject can be a user, user attribute,
     * or process.
     *
     * @param subject the name of the subject to get the prohibitions for.
     * @return The list of prohibitions the given entity is the subject of.
     */
    @Override
    public List<Prohibition> getProhibitionsFor(String subject) throws PMException {
        return this.prohibitions.getProhibitionsFor(subject);
    }

    /**
     * Update the prohibition with the given name. Prohibition names cannot be updated.
     *
     * @param prohibitionName the name of the prohibition to update.
     * @param prohibition     The prohibition to update.
     * @throws PMException if there is an error updating the prohibition.
     */
    @Override
    public void update(String prohibitionName, Prohibition prohibition) throws PMException {
        if (this.copy_prohibitions != null) {
            this.copy_prohibitions.update(prohibitionName, prohibition);
        }
        this.prohibitions.update(prohibitionName, prohibition);
    }

    /**
     * Delete the prohibition, and remove it from the data structure.
     *
     * @param prohibitionName The name of the prohibition to delete.
     * @throws PMException if there is an error deleting the prohibition.
     */
    @Override
    public void delete(String prohibitionName) throws PMException {
        if (this.copy_prohibitions != null) {
            this.copy_prohibitions.delete(prohibitionName);
        }
        this.prohibitions.delete(prohibitionName);
    }
}
