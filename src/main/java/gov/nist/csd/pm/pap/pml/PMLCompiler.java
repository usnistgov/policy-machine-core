package gov.nist.csd.pm.pap.pml;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.op.Operation;
import gov.nist.csd.pm.pap.pml.antlr.PMLLexer;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.compiler.Variable;
import gov.nist.csd.pm.pap.pml.compiler.error.ErrorLog;
import gov.nist.csd.pm.pap.pml.compiler.visitor.PMLVisitor;
import gov.nist.csd.pm.pap.pml.executable.PMLExecutableSignature;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLOperation;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLOperationWrapper;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLStmtsOperation;
import gov.nist.csd.pm.pap.pml.executable.routine.PMLRoutine;
import gov.nist.csd.pm.pap.pml.executable.routine.PMLRoutineWrapper;
import gov.nist.csd.pm.pap.pml.executable.routine.PMLStmtsRoutine;
import gov.nist.csd.pm.pap.pml.scope.CompileGlobalScope;
import gov.nist.csd.pm.pap.pml.scope.GlobalScope;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.exception.PMLCompilationException;
import gov.nist.csd.pm.pap.pml.scope.Scope;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.routine.Routine;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.*;

public class PMLCompiler {

    protected Map<String, PMLExecutableSignature> executableSignatures;
    protected Map<String, Variable> constantVariables;

    public PMLCompiler() {
        executableSignatures = new HashMap<>();
        constantVariables = new HashMap<>();
    }

    public PMLCompiler(PAP pap) throws PMException {
        executableSignatures = new HashMap<>();
        constantVariables = new HashMap<>();

        Map<String, PMLOperation> operations = pap.getPMLOperations();
        for (PMLOperation operation : operations.values()) {
            this.executableSignatures.put(operation.getName(), operation.getSignature());
        }

        Map<String, PMLRoutine> routines = pap.getPMLRoutines();
        for (PMLRoutine routine : routines.values()) {
            this.executableSignatures.put(routine.getName(), routine.getSignature());
        }

        Map<String, Value> constants = pap.getPMLConstants();
        for (Map.Entry<String, Value> constant : constants.entrySet()) {
            this.constantVariables.put(constant.getKey(),
                    new Variable(constant.getKey(), constant.getValue().getType(), true));
        }

        Collection<String> opNames = pap.query().operations().getAdminOperationNames();
        for (String opName : opNames) {
            Operation<?> operation = pap.query().operations().getAdminOperation(opName);
            if (operation instanceof PMLStmtsOperation) {
                PMLStmtsOperation pmlStmtsOperation = (PMLStmtsOperation) operation;
                this.executableSignatures.put(pmlStmtsOperation.getName(), pmlStmtsOperation.getSignature());
            } else {
                this.executableSignatures.put(opName, new PMLOperationWrapper(operation).getSignature());
            }
        }

        Collection<String> routineNames = pap.query().routines().getAdminRoutineNames();
        for (String routineName : routineNames) {
            Routine<?> routine = pap.query().routines().getAdminRoutine(routineName);
            if (routine instanceof PMLStmtsRoutine) {
                PMLStmtsRoutine pmlStmtsRoutine = (PMLStmtsRoutine) routine;
                this.executableSignatures.put(routine.getName(), pmlStmtsRoutine.getSignature());
            } else {
                this.executableSignatures.put(routineName, new PMLRoutineWrapper(routine).getSignature());
            }
        }
    }

    public Map<String, PMLExecutableSignature> getExecutableSignatures() {
        return executableSignatures;
    }

    public Map<String, Variable> getConstantVariables() {
        return constantVariables;
    }

    public List<PMLStatement> compilePML(String input) throws PMException {
        ErrorLog errorLog = new ErrorLog();

        GlobalScope<Variable, PMLExecutableSignature> globalScope = new CompileGlobalScope();
        globalScope.addExecutables(executableSignatures);
        globalScope.addConstants(constantVariables);

        PMLErrorHandler pmlErrorHandler = new PMLErrorHandler();

        PMLLexer lexer = new PMLLexer(CharStreams.fromString(input));
        lexer.removeErrorListeners();
        lexer.addErrorListener(pmlErrorHandler);

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PMLParser parser = new PMLParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(pmlErrorHandler);

        // check for syntax errors
        PMLVisitor pmlVisitor = new PMLVisitor(new VisitorContext(tokens, new Scope<>(globalScope), errorLog, pmlErrorHandler));
        PMLParser.PmlContext pmlCtx = parser.pml();
        if (!pmlErrorHandler.getErrors().isEmpty()) {
            throw new PMLCompilationException(pmlErrorHandler.getErrors());
        }

        // compile
        List<PMLStatement> compiled = pmlVisitor.visitPml(pmlCtx);

        // check for errors encountered during compilation
        if (!errorLog.getErrors().isEmpty()) {
            throw new PMLCompilationException(errorLog.getErrors());
        }

        return compiled;
    }
}
