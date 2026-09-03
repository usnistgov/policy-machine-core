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

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.obligation.Obligation;
import gov.nist.ngac.pm.core.pap.query.NodeLookup;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;
import java.util.Objects;

/**
 * JSON DTO for an obligation, storing its author as a resolved node id and its body as raw PML text.
 */
public class JSONObligation {

    /**
     * Converts a live obligation into its JSON DTO, resolving its author to a node id.
     *
     * @param o the obligation to convert; its author must be a {@link NodeUserContext}
     * @param graphQuery used to resolve the author's node id
     * @return the JSON DTO
     * @throws PMException if resolving the author's node id fails
     */
    public static JSONObligation fromObligation(Obligation o, NodeLookup graphQuery) throws PMException {
        long authorId = ((NodeUserContext) o.getAuthor()).resolveNodeIds(graphQuery).iterator().next();
        return new JSONObligation(o.getName(), authorId, o.toString());
    }

    private String name;
    private long author;
    private String pml;

    public JSONObligation() {
    }

    public JSONObligation(String name, long author, String pml) {
        this.name = name;
        this.author = author;
        this.pml = pml;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAuthor() {
        return author;
    }

    public void setAuthor(long author) {
        this.author = author;
    }

    public String getPml() {
        return pml;
    }

    public void setPml(String pml) {
        this.pml = pml;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JSONObligation that)) return false;
        return author == that.author && 
               Objects.equals(name, that.name) && 
               Objects.equals(pml, that.pml);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, author, pml);
    }

    @Override
    public String toString() {
        return "JSONObligation{" +
                "name='" + name + '\'' +
                ", author=" + author +
                ", pml='" + pml + '\'' +
                '}';
    }
} 