package gov.nist.csd.pm.policy.json;

import com.google.gson.Gson;

public class JSONPolicy {
    private String graph;
    private String prohibitions;
    private String obligations;
    private String userDefinedPML;

    public JSONPolicy() {
    }

    public JSONPolicy(String graph, String prohibitions, String obligations, String userDefinedPML) {
        this.graph = graph;
        this.prohibitions = prohibitions;
        this.obligations = obligations;
        this.userDefinedPML = userDefinedPML;
    }

    public String getGraph() {
        return graph;
    }

    public void setGraph(String graph) {
        this.graph = graph;
    }

    public String getProhibitions() {
        return prohibitions;
    }

    public void setProhibitions(String prohibitions) {
        this.prohibitions = prohibitions;
    }

    public String getObligations() {
        return obligations;
    }

    public void setObligations(String obligations) {
        this.obligations = obligations;
    }

    public String getUserDefinedPML() {
        return userDefinedPML;
    }

    public void setUserDefinedPML(String userDefinedPML) {
        this.userDefinedPML = userDefinedPML;
    }

    public static JSONPolicy fromJson(String json) {
        JSONPolicy jsonPolicy = new Gson().fromJson(json, JSONPolicy.class);
        return jsonPolicy;
    }
}
