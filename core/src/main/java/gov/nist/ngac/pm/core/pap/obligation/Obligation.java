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

package gov.nist.ngac.pm.core.pap.obligation;

import gov.nist.ngac.pm.core.pap.obligation.event.EventPattern;
import gov.nist.ngac.pm.core.pap.obligation.response.ObligationResponse;
import gov.nist.ngac.pm.core.pap.pml.statement.operation.CreateObligationStatement;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;
import java.util.Objects;

/**
 * An object representing an Obligation which has an author, a name (id), event pattern and response.
 */
public class Obligation {

    private NodeUserContext author;
    private String name;
    private EventPattern eventPattern;
    private ObligationResponse response;

    public Obligation() {
    }

    public Obligation(NodeUserContext author, String name, EventPattern eventPattern, ObligationResponse response) {
        this.author = author;
        this.name = name;
        this.eventPattern = eventPattern;
        this.response = response;
    }

    public NodeUserContext getAuthor() {
        return author;
    }

    public void setAuthor(NodeUserContext author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EventPattern getEventPattern() {
        return eventPattern;
    }

    public void setEventPattern(EventPattern eventPattern) {
        this.eventPattern = eventPattern;
    }

    public ObligationResponse getResponse() {
        return response;
    }

    public void setResponse(ObligationResponse response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return CreateObligationStatement.fromObligation(this).toFormattedString(0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Obligation that)) {
            return false;
        }
        return Objects.equals(author, that.author) && Objects.equals(name, that.name) && Objects.equals(
            eventPattern, that.eventPattern) && Objects.equals(response, that.response);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, name, eventPattern, response);
    }
}