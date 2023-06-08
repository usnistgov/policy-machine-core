package gov.nist.csd.pm.pap.pml;

import gov.nist.csd.pm.pap.pml.executable.PMLExecutableSignature;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record CompiledPML(Map<String, PMLExecutableSignature> executables, List<PMLStatement> stmts) {

}
