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

package gov.nist.ngac.pm.core.pdp;

import gov.nist.ngac.pm.core.common.event.EventPublisher;
import gov.nist.ngac.pm.core.common.event.EventSubscriber;
import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.epp.EventContext;
import java.util.List;

/**
 * {@link EventPublisher} that forwards published events to each registered subscriber in order.
 */
public class PDPEventPublisher implements EventPublisher {

    private final List<EventSubscriber> epps;

    public PDPEventPublisher(List<EventSubscriber> epps) {
        this.epps = epps;
    }

    @Override
    public void addEventSubscriber(EventSubscriber processor) {
        this.epps.add(processor);
    }

    @Override
    public void removeEventSubscriber(EventSubscriber processor) {
        this.epps.remove(processor);
    }

    @Override
    public void publishEvent(EventContext event) throws PMException {
        for (EventSubscriber epp : epps) {
            epp.processEvent(event);
        }
    }
}
