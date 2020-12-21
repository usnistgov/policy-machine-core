package gov.nist.csd.pm.pip.memory;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;

import java.util.*;

/**
 * An in memory implementation of the Prohibitions interface, that stores prohibitions in a list.
 */
public class MemProhibitions implements Prohibitions {

    private Map<String, List<Prohibition>> prohibitions;

    public MemProhibitions() {
        this.prohibitions = new HashMap<>();
    }


    /**
     * Add the provided prohibition to the list of prohibitions. The prohibition name cannot be null or empty.
     * The prohibition subject cannot be null or have a null type.
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

        prohibition = new Prohibition(prohibition);
        String subject = prohibition.getSubject();
        List<Prohibition> exPros = this.prohibitions.getOrDefault(subject, new ArrayList<>());
        exPros.add(prohibition);
        this.prohibitions.put(subject, exPros);
    }

    /**
     * @return the list of prohibition objects.
     */
    @Override
    public List<Prohibition> getAll() {
        List<Prohibition> pros = new ArrayList<>();
        for(List<Prohibition> pList : prohibitions.values()) {
            for (Prohibition p : pList) {
                pros.add(new Prohibition(p));
            }
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
                    return new Prohibition(p);
                }
            }
        }
        throw new PMException(String.format("a prohibition does not exist with the name %s", prohibitionName));
    }

    /**
     * Get the Prohibitions the given subject is the direct subject of.
     * @param subject the subject to get the prohibitions for.
     * @return a list of Prohibitions the given entity is the subject of.
     */
    @Override
    public List<Prohibition> getProhibitionsFor(String subject) {
        List<Prohibition> pros = prohibitions.getOrDefault(subject, new ArrayList<>());
        List<Prohibition> ret = new ArrayList<>();
        for (Prohibition p : pros) {
            ret.add(new Prohibition(p));
        }
        return ret;
    }

    /**
     * Update an existing prohibition with the given prohibition object.
     *
     * @param prohibition the prohibition to update.
     * @throws IllegalArgumentException if the provided prohibition is null.
     * @throws IllegalArgumentException if the provided prohibition name is null or empty.
     */
    @Override
    public void update(String prohibitionName, Prohibition prohibition) {
        if (prohibition == null) {
            throw new IllegalArgumentException("a null prohibition was provided when updating a prohibition");
        } else if (prohibitionName == null) {
            throw new IllegalArgumentException("cannot update a prohibition with a null name");
        }

        // set the name of the object to the provided prohibition name
        prohibition.setName(prohibitionName);
        // delete the old prohibition
        delete(prohibition.getName());
        // add the updated prohibition
        add(new Prohibition(prohibition));
    }

    /**
     * Remove the prohibition with the given name from the list.
     *
     * @param prohibitionName the name of the prohibition to delete.
     */
    @Override
    public void delete(String prohibitionName) {
        for(String subject : prohibitions.keySet()) {
            List<Prohibition> ps = prohibitions.get(subject);
            Iterator<Prohibition> iterator = ps.iterator();
            while (iterator.hasNext()) {
                Prohibition p = iterator.next();
                if(p.getName().equals(prohibitionName)) {
                    iterator.remove();
                    prohibitions.put(subject, ps);
                }
            }
        }
    }
}
