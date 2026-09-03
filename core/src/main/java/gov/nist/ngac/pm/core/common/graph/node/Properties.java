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

package gov.nist.ngac.pm.core.common.graph.node;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Common properties used for nodes
 */
public class Properties extends HashMap<String, String> {
    public static final String WILDCARD = "*";
    public static final String REP_PROPERTY = "rep";
    public static final Map<String, String> NO_PROPERTIES = Collections.unmodifiableMap(new HashMap<>());

    /**
     * This method receives an array of strings and pairs consecutive parameters as key, value pairs.
     * For example, calling toProperties('prop1', 'value1', 'prop2', 'value2') would create a property map with two
     * entries.  The first entry will be 'prop1' to 'value1' and the second will be 'prop2' to 'value2'. An
     * IllegalArgumentException will be thrown if any value is null or there is an odd number of values, as this will
     * lead to errors in processing the parameters.
     *
     * @param pairs Array of string values to convert to a HashMap
     * @return a HashMap of the given pairs
     */
    public static Map<String, String> toProperties(String... pairs) {
        HashMap<String, String> props = new HashMap<>();
        for (int i = 0; i < pairs.length - 1; i++) {
            props.put(pairs[i], pairs[++i]);
        }
        return props;
    }

    public Properties(String... pairs) {
        super(toProperties(pairs));
    }

    public Properties(Map<String, String> properties) {
        super(properties);
    }
}
