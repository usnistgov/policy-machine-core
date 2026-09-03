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

import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import java.util.Objects;

/**
 * JSON DTO for an association's target and access rights, nested under a {@link JSONNode}'s outgoing
 * associations.
 */
public class JSONAssociation {

    private long target;
    private AccessRightSet arset;

    public JSONAssociation() {
        arset = new AccessRightSet();
    }

    public JSONAssociation(long target, AccessRightSet arset) {
        this.target = target;
        this.arset = arset;
    }

    public long getTarget() {
        return target;
    }

    public void setTarget(long target) {
        this.target = target;
    }

    public AccessRightSet getArset() {
        return arset;
    }

    public void setArset(AccessRightSet arset) {
        this.arset = arset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JSONAssociation that)) return false;
        return Objects.equals(target, that.target) && Objects.equals(arset, that.arset);
    }

    @Override
    public int hashCode() {
        return Objects.hash(target, arset);
    }
}
