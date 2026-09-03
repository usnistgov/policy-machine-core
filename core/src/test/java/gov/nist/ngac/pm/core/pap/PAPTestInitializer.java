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

package gov.nist.ngac.pm.core.pap;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.graph.node.Node;
import java.util.Collection;
import java.util.List;
import java.util.stream.LongStream;
import org.junit.jupiter.api.BeforeEach;

public abstract class PAPTestInitializer {

    protected PAP pap;

    public abstract PAP initializePAP() throws PMException;

    @BeforeEach
    void setup() throws PMException {
        pap = initializePAP();
    }

    protected Node node(String name) throws PMException {
        return pap.query()
                .graph()
                .getNodeByName(name);
    }

    protected long id(String name) throws PMException {
        return pap.query().graph().getNodeByName(name).getId();
    }

    protected List<Long> ids(String ... names) throws PMException {
        long[] ids = new long[names.length];
        for (int i = 0; i < names.length; i++) {
            ids[i] = id(names[i]);
        }

        return LongStream.of(ids)
                .boxed()
                .toList();
    }

    protected void assertIdOfNameInLongArray(Collection<Long> ids, String name) throws PMException {
        assertTrue(ids.contains(id(name)));
    }

    protected void assertIdOfNameNotInLongArray(Collection<Long> ids, String name) throws PMException {
        assertFalse(ids.contains(id(name)));
    }
}
