package gov.nist.csd.pm.pip.prohibitions;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;

import java.util.ArrayList;
import java.util.List;

/**
 * An in memory implementation of the Prohibitions interface, that stores prohibitions in a list.
 */
public class MemProhibitions implements Prohibitions {

    /**
     * Data structure to store prohibitions.
     */
    public List<Prohibition> prohibitions;

    public MemProhibitions() {
        this.prohibitions = new ArrayList<>();
    }

    /**
     * Add the provided prohibition to the list of prohibitions. The prohibition name cannot be null or empty.
     * The prohibition subject cannot be null, have an ID of 0, or have a null type.
     *
     * @param prohibition the prohibition to be created.
     * @throws IllegalArgumentException if the prohibition is null.
     * @throws IllegalArgumentException if the prohibition name is null or is empty.
     * @throws IllegalArgumentException if the prohibition subject is null.
     */
    @Override
    public void add(Prohibition prohibition) {
        if (prohibition == null) {
            throw new IllegalArgumentException("a null prohibition was received when creating a prohibition");
        }
        else if (prohibition.getName() == null || prohibition.getName().isEmpty()) {
            throw new IllegalArgumentException("a null or empty name was provided when creating a prohibition");
        }
        else if (prohibition.getSubject() == null) {
            throw new IllegalArgumentException("a null subject was provided when creating a prohibition");
        }

        prohibitions.add(prohibition);
    }

    /**
     * @return the list of prohibition objects.
     */
    @Override
    public List<Prohibition> getAll() {
        return prohibitions;
    }

    /**
     * @param prohibitionName the name of the Prohibition to retrieve.
     * @return the prohibition with the given name.  If one does not exist, null is returned.
     */
    @Override
    public Prohibition get(String prohibitionName) throws PMException {
        for (Prohibition prohibition : prohibitions) {
            if (prohibition.getName().equals(prohibitionName)) {
                return prohibition;
            }
        }
        throw new PMException(String.format("a prohibition does not exist with the name %s", prohibitionName));
    }

    /**
     * Update an existing prohibition with the same name as the provided prohibition.  The provided prohibition cannot
     * be null and the name of the prohibition cannot be null.
     *
     * @param prohibition the prohibition to update.
     * @throws IllegalArgumentException if the provided prohibition is null.
     * @throws IllegalArgumentException if the provided prohibition name is null or empty.
     */
    @Override
    public void update(Prohibition prohibition) {
        if (prohibition == null) {
            throw new IllegalArgumentException("a null prohibition was provided when updating a prohibition");
        }
        else if (prohibition.getName() == null || prohibition.getName().isEmpty()) {
            throw new IllegalArgumentException("a null name was provided when updating a prohibition");
        }
        // delete the prohibition
        delete(prohibition.getName());
        // add the updated prohibition
        add(prohibition);
    }

    /**
     * Remove the prohibition with the given name from the list.
     *
     * @param prohibitionName the name of the prohibition to delete.
     */
    @Override
    public void delete(String prohibitionName) {
        prohibitions.removeIf((prohibition) -> prohibition.getName().equals(prohibitionName));
    }
}
