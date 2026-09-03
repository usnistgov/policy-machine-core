/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pap.serialization.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gov.nist.ngac.pm.core.common.prohibition.Prohibition;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import java.util.List;

/**
 * JSON DTO for a full policy: resource access rights, graph, prohibitions, obligations, and operations.
 */
public class JSONPolicy {

    private AccessRightSet resourceAccessRights;
    private JSONGraph graph;
    private List<Prohibition> prohibitions;
    private List<JSONObligation> obligations;
    private JSONOperations operations;

    public JSONPolicy(AccessRightSet resourceAccessRights,
                      JSONGraph graph,
                      List<Prohibition> prohibitions,
                      List<JSONObligation> obligations,
                      JSONOperations operations) {
        this.resourceAccessRights = resourceAccessRights;
        this.graph = graph;
        this.prohibitions = prohibitions;
        this.obligations = obligations;
        this.operations = operations;
    }

    public AccessRightSet getResourceAccessRights() {
        return resourceAccessRights;
    }

    public void setResourceAccessRights(AccessRightSet resourceAccessRights) {
        this.resourceAccessRights = resourceAccessRights;
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

    public List<JSONObligation> getObligations() {
        return obligations;
    }

    public void setObligations(List<JSONObligation> obligations) {
        this.obligations = obligations;
    }

    public JSONOperations getOperations() {
        return operations;
    }

    public void setOperations(JSONOperations operations) {
        this.operations = operations;
    }

    @Override
    public String toString() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create()
                .toJson(this);
    }

    /**
     * Parses a JSON policy document into a {@link JSONPolicy}.
     */
    public static JSONPolicy fromJSON(String json) {
        return new Gson().fromJson(json, JSONPolicy.class);
    }
}
