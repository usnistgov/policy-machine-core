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

package gov.nist.ngac.pm.core.pap.obligation.event;

import java.util.List;
import java.util.Objects;

/**
 * The user who triggered an event, identified by either a name or a set of user attributes, plus the
 * process they acted as. This is required to use the names of nodes in order to make pattern matching
 * in obligations possible.
 */
public class EventContextUser {

    private final String name;
    private final List<String> attrs;
    private final String process;

    public EventContextUser(String name, String process) {
        this.name = name;
        this.attrs = List.of();
        this.process = Objects.requireNonNullElse(process, "");
    }

    public EventContextUser(String name) {
        this.name = name;
        this.attrs = List.of();
        this.process = "";
    }

    public EventContextUser(List<String> attrs, String process) {
        this.name = "";
        this.attrs = attrs;
        this.process = Objects.requireNonNullElse(process, "");
    }

    public EventContextUser(List<String> attrs) {
        this.name = "";
        this.attrs = attrs;
        this.process = "";
    }

    public boolean isUser() {
        return !name.isEmpty();
    }

    public String getName() {
        return name;
    }

    public String getProcess() {
        return process;
    }

    public List<String> getAttrs() {
        return attrs;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof EventContextUser that)) {
            return false;
        }
        return Objects.equals(name, that.name) && Objects.equals(attrs, that.attrs)
            && Objects.equals(process, that.process);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, attrs, process);
    }

    @Override
    public String toString() {
        return "EventContextUser{" +
            "name='" + name + '\'' +
            ", attrs=" + attrs +
            ", process='" + process + '\'' +
            '}';
    }
}
