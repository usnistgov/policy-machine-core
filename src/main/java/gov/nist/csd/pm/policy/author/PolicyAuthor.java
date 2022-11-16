package gov.nist.csd.pm.policy.author;

import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;

public interface PolicyAuthor {

    GraphAuthor graph();
    ProhibitionsAuthor prohibitions();
    ObligationsAuthor obligations();
    PALAuthor pal();

}
