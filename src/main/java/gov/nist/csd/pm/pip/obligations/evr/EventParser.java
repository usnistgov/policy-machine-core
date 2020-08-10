package gov.nist.csd.pm.pip.obligations.evr;

import gov.nist.csd.pm.pip.obligations.model.EventPattern;

import java.util.Map;

public interface EventParser {

    String key();

    EventPattern parse(Map map) throws EVRException;

}
