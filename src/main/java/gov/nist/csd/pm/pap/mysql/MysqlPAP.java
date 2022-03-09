package gov.nist.csd.pm.pap.mysql;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.reviewer.MemoryPolicyReviewer;
import gov.nist.csd.pm.policy.exceptions.PMException;

import java.sql.Connection;

public class MysqlPAP extends PAP {

    public MysqlPAP(Connection connection) throws PMException {
        super(new MysqlConnection(connection));
    }
}
