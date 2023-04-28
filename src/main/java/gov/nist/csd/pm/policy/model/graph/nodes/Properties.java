package gov.nist.csd.pm.policy.model.graph.nodes;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Common properties used for nodes
 */
public class Properties {
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
}
