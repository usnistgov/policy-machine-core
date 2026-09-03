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

package gov.nist.ngac.pm.core.epp;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.obligation.event.EventContextUser;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A context object for an event that will be processed by the EPP.
 *
 * @param user the user that invoked the operation
 * @param opName the name of the invoked operation
 * @param args the operation's actual argument values, keyed by parameter name
 */
public record EventContext(EventContextUser user, String opName, Map<String, Object> args) {

    /**
     * Returns this event context as a plain map, for use as a PML variable.
     *
     * @return the map, with keys "user", "attrs", "process", "opName", and "args"
     */
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("user", user.getName());
        map.put("attrs", user.getAttrs());
        map.put("process", user.getProcess());
        map.put("opName", opName);
        map.put("args", args);

        return map;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EventContext that = (EventContext) o;
        return Objects.equals(opName, that.opName) && Objects.equals(user,
            that.user) && Objects.equals(args, that.args);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, opName, args);
    }

    public static EventContext fromUserContext(PAP pap,
                                               UserContext userCtx,
                                               String opName,
                                               Map<String, Object> args) throws PMException {
        EventContextUser user = userCtx.toEventContextUser(pap.query().graph());
        return new EventContext(user, opName, args);
    }
}
