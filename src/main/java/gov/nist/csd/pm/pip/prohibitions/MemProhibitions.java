package gov.nist.csd.pm.pip.prohibitions;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;

import java.util.*;

/**
 * An in memory implementation of the Prohibitions interface, that stores prohibitions in a list.
 */
public class MemProhibitions implements Prohibitions {

    private Map<Long, List<Prohibition>> prohibitions;

    public MemProhibitions() {
        this.prohibitions = new HashMap<>();
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

        long subjectID = prohibition.getSubject().getSubjectID();
        List<Prohibition> exPros = this.prohibitions.getOrDefault(subjectID, new ArrayList<>());
        exPros.add(prohibition);
        this.prohibitions.put(subjectID, exPros);
    }

    /**
     * @return the list of prohibition objects.
     */
    @Override
    public List<Prohibition> getAll() {
        List<Prohibition> pros = new ArrayList<>();
        for(List<Prohibition> p : prohibitions.values()) {
            pros.addAll(p);
        }
        return pros;
    }

    /**
     * @param prohibitionName the name of the Prohibition to retrieve.
     * @return the prohibition with the given name.  If one does not exist, null is returned.
     */
    @Override
    public Prohibition get(String prohibitionName) throws PMException {
        for (List<Prohibition> ps : prohibitions.values()) {
            for(Prohibition p : ps) {
                if(p.getName().equalsIgnoreCase(prohibitionName)) {
                    return p;
                }
            }
        }
        throw new PMException(String.format("a prohibition does not exist with the name %s", prohibitionName));
    }

    /**
     * Get the Prohibitions the given subject is the direct subject of.
     * @param subjectID the ID of the subject to get the prohibitions for.
     * @return a list of Prohibitions the given entity is the subject of.
     */
    @Override
    public List<Prohibition> getProhibitionsFor(long subjectID) {
        return prohibitions.getOrDefault(subjectID, new ArrayList<>());
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
        for(Long subjectID : prohibitions.keySet()) {
            List<Prohibition> ps = prohibitions.get(subjectID);
            Iterator<Prohibition> iterator = ps.iterator();
            while (iterator.hasNext()) {
                Prohibition p = iterator.next();
                if(p.getName().equals(prohibitionName)) {
                    iterator.remove();
                    prohibitions.put(subjectID, ps);
                }
            }
        }
    }
}
