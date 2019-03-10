package gov.nist.csd.pm.audit;

import gov.nist.csd.pm.audit.model.Path;
import gov.nist.csd.pm.exceptions.PMException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PReviewAuditorTest {

    @Test
    void testExplain() throws PMException {
        for(TestCases.TestCase tc : TestCases.getTests()) {
            PReviewAuditor auditor = new PReviewAuditor(tc.graph);
            Map<String, List<Path>> explain = auditor.explain(TestCases.u1ID, TestCases.o1ID);

            for (String pc : tc.expectedPaths.keySet()) {
                List<String> paths = tc.expectedPaths.get(pc);
                assertNotNull(paths, tc.name);

                List<Path> resPaths = explain.get(pc);
                for (String exPathStr : paths) {
                    boolean match = false;
                    for (Path resPath : resPaths) {
                        if(resPath.toString().equals(exPathStr)) {
                            match = true;
                            break;
                        }
                    }
                    assertTrue(match, tc.name + " expected path \"" + exPathStr + "\" but it was not in the results \"" + resPaths + "\"");
                }
            }
        }
    }
}