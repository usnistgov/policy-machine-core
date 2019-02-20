package gov.nist.csd.pm.prohibitions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.prohibitions.model.Prohibition;

import java.util.List;

public class ProhibitionsSerializer {
    private ProhibitionsSerializer() {}

    /**
     * Given an implementation of the ProhibitionDAO interface, convert the underlying list
     * of prohibitions to a json string.
     *
     * @param dao the ProhibitionsDAO to get the list of prohibitions from.
     * @return a string representation of the ProhibitionsDAO.
     * @throws PMException if there is an error retrieving the prohibitions from the dao.
     */
    public static String toJson(ProhibitionsDAO dao) throws PMException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(new JsonProhibitions(dao.getProhibitions()));
    }

    /**
     * Given a json string and an implementation of the ProhibitionsDAO, convert the json string
     * to a list of prohibitions, and use the dao to create each prohibition.
     *
     * @param dao the implementation of the ProhibitionsDAO interface that will store the loaded prohibitions
     * @param json the string representation of the prohibitions.
     * @return the provided ProhibitionsDAO implementation with the data from the json string.
     * @throws PMException if there is an error adding the prohibitions to the dao.
     */
    public static ProhibitionsDAO fromJson(ProhibitionsDAO dao, String json) throws PMException {
        JsonProhibitions jsonProhibitions = new Gson().fromJson(json, JsonProhibitions.class);
        for(Prohibition prohibition : jsonProhibitions.getProhibitions()) {
            dao.createProhibition(prohibition);
        }
        return dao;
    }

    private static class JsonProhibitions {
        List<Prohibition> prohibitions;

        JsonProhibitions(List<Prohibition> prohibitions) {
            this.prohibitions = prohibitions;
        }

        List<Prohibition> getProhibitions() {
            return prohibitions;
        }
    }
}
