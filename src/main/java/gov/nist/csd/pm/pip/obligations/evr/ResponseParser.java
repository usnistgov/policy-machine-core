package gov.nist.csd.pm.pip.obligations.evr;

import gov.nist.csd.pm.pip.obligations.model.ResponsePattern;

import java.util.Map;

public interface ResponseParser {

    /**
     * The key is the keyword used in the obligation yaml to define the event.
     * @return the key for this event.
     */
    String key();

    /**
     * Parse a yaml map to an ResponsePattern.
     * @param map the map returned from parsing the yaml element. The map will only have one entry and the key will be
     *            the value of key(). The value of the entry will be an Object representing the yaml for the response. The
     *            object can be of any type supported by yaml.
     * @return a ResponsePattern representing the event.
     * @throws EVRException
     */
    ResponsePattern parse(Map map) throws EVRException;

}
