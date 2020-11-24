package gov.nist.csd.pm.pip.prohibitions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;

import java.util.*;

public class ProhibitionsSerializer {
    private ProhibitionsSerializer() {}

    /**
     * Given an implementation of the ProhibitionDAO interface, convert the underlying list
     * of prohibitions to a json string.
     *
     * @param dao the Prohibitions to get the list of prohibitions from.
     * @return a string representation of the Prohibitions.
     * @throws PMException if there is an error retrieving the prohibitions from the dao.
     */
    public static String toJson(Prohibitions dao) throws PMException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        List<Prohibition> prohibitions = dao.getAll();
        List<JsonProhibition> jsonProhibitions = new ArrayList<>();
        for (Prohibition prohibition : prohibitions) {
            jsonProhibitions.add(new JsonProhibition(prohibition.getName(), prohibition.getSubject(), prohibition.getOperations(),
                    prohibition.isIntersection(), prohibition.getContainers()));
        }

        return gson.toJson(new JsonProhibitions(jsonProhibitions));
    }

    /**
     * Given a json string and an implementation of the Prohibitions, convert the json string
     * to a list of prohibitions, and use the prohibitions to create each prohibition.
     *
     * @param prohibitions the implementation of the Prohibitions interface that will store the loaded prohibitions
     * @param json the string representation of the prohibitions.
     * @return the provided Prohibitions implementation with the data from the json string.
     * @throws PMException if there is an error adding the prohibitions to the prohibitions.
     */
    public static Prohibitions fromJson(Prohibitions prohibitions, String json) throws PMException {
        JsonProhibitions jsonProhibitions = new Gson().fromJson(json, JsonProhibitions.class);
        for(JsonProhibition jsonProhibition : jsonProhibitions.getProhibitions()) {
            Prohibition.Builder builder = new Prohibition.Builder(jsonProhibition.name, jsonProhibition.subject, new OperationSet(jsonProhibition.ops));

            for (String contName : jsonProhibition.containers.keySet()) {
                builder.addContainer(contName, jsonProhibition.containers.get(contName));
            }

            prohibitions.add(builder.build());
        }
        return prohibitions;
    }

    private static class JsonProhibitions {

        List<JsonProhibition> prohibitions;

        JsonProhibitions(List<JsonProhibition> prohibitions) {
            this.prohibitions = prohibitions;
        }

        List<JsonProhibition> getProhibitions() {
            return prohibitions;
        }
    }

    private static class JsonProhibition {
        private String name;
        private String subject;
        private Set<String> ops;
        private boolean intersection;
        private Map<String, Boolean> containers;

        JsonProhibition() {

        }

        JsonProhibition(String name, String subject, Set<String> ops, boolean intersection, Map<String, Boolean> containers) {
            this.name = name;
            this.subject = subject;
            this.ops = ops;
            this.intersection = intersection;
            this.containers = new HashMap<>();
            this.containers = containers;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public Set<String> getOps() {
            return ops;
        }

        public void setOps(Set<String> ops) {
            this.ops = ops;
        }

        public boolean isIntersection() {
            return intersection;
        }

        public void setIntersection(boolean intersection) {
            this.intersection = intersection;
        }

        public Map<String, Boolean> getContainers() {
            return containers;
        }

        public void setContainers(Map<String, Boolean> containers) {
            this.containers = containers;
        }
    }
}
