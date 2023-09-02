package gov.nist.csd.pm.pap.serialization.json;

import com.google.gson.Gson;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;

import java.util.List;

public class JSONPolicy {

    private JSONGraph graph;
    private List<Prohibition> prohibitions;
    private List<String> obligations;
    private JSONUserDefinedPML userDefinedPML;

    public JSONPolicy() {
    }

    public JSONPolicy(JSONGraph graph, List<Prohibition> prohibitions, List<String> obligations,
                      JSONUserDefinedPML userDefinedPML) {
        this.graph = graph;
        this.prohibitions = prohibitions;
        this.obligations = obligations;
        this.userDefinedPML = userDefinedPML;
    }

    public JSONGraph getGraph() {
        return graph;
    }

    public void setGraph(JSONGraph graph) {
        this.graph = graph;
    }

    public List<Prohibition> getProhibitions() {
        return prohibitions;
    }

    public void setProhibitions(List<Prohibition> prohibitions) {
        this.prohibitions = prohibitions;
    }

    public List<String> getObligations() {
        return obligations;
    }

    public void setObligations(List<String> obligations) {
        this.obligations = obligations;
    }

    public JSONUserDefinedPML getUserDefinedPML() {
        return userDefinedPML;
    }

    public void setUserDefinedPML(JSONUserDefinedPML userDefinedPML) {
        this.userDefinedPML = userDefinedPML;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static JSONPolicy fromJSON(String json) {
        return new Gson().fromJson(json, JSONPolicy.class);
    }
}
