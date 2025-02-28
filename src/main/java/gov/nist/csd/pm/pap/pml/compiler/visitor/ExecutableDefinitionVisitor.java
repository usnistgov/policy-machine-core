package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.compiler.Variable;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.pap.pml.executable.PMLExecutableSignature;
import gov.nist.csd.pm.pap.pml.executable.function.PMLFunctionSignature;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLOperationSignature;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLStmtsOperation;
import gov.nist.csd.pm.pap.pml.executable.operation.CheckAndStatementsBlock;
import gov.nist.csd.pm.pap.pml.executable.routine.PMLRoutineSignature;
import gov.nist.csd.pm.pap.pml.executable.routine.PMLStmtsRoutine;
import gov.nist.csd.pm.pap.pml.scope.ExecutableAlreadyDefinedInScopeException;
import gov.nist.csd.pm.pap.pml.statement.CreateExecutableStatement;
import gov.nist.csd.pm.pap.pml.statement.PMLStatementBlock;
import gov.nist.csd.pm.pap.pml.statement.basic.CreateFunctionStatement;
import gov.nist.csd.pm.pap.pml.statement.operation.CreateOperationStatement;
import gov.nist.csd.pm.pap.pml.statement.operation.CreateRoutineStatement;
import gov.nist.csd.pm.pap.pml.type.Type;

import java.util.*;

public class ExecutableDefinitionVisitor extends PMLBaseVisitor<CreateExecutableStatement> {
	public ExecutableDefinitionVisitor(VisitorContext visitorCtx) {
		super(visitorCtx);
	}

	@Override
	public CreateOperationStatement visitOperationDefinitionStatement(PMLParser.OperationDefinitionStatementContext ctx) {
		PMLOperationSignature signature = new SignatureVisitor(visitorCtx)
				.visitOperationSignature(ctx.operationSignature());

		CheckAndStatementsBlock body = parseBody(
				ctx.checkStatementBlock(),
				ctx.statementBlock(),
				signature.getOperands(),
				signature.getOperandTypes(),
				signature.getReturnType()
		);

		return new CreateOperationStatement(new PMLStmtsOperation(
				signature.getFunctionName(),
				signature.getReturnType(),
				signature.getOperands(),
				signature.getNodeOperands(),
				signature.getOperandTypes(),
				body
		));
	}

	@Override
	public CreateRoutineStatement visitRoutineDefinitionStatement(PMLParser.RoutineDefinitionStatementContext ctx) {
		PMLRoutineSignature signature = new SignatureVisitor(visitorCtx)
				.visitRoutineSignature(ctx.routineSignature());

		CheckAndStatementsBlock body = parseBody(
				null,
				ctx.statementBlock(),
				signature.getOperands(),
				signature.getOperandTypes(),
				signature.getReturnType()
		);

		return new CreateRoutineStatement(new PMLStmtsRoutine(
				signature.getFunctionName(),
				signature.getReturnType(),
				signature.getOperands(),
				signature.getOperandTypes(),
				body.getStatements()
		));
	}

	@Override
	public CreateFunctionStatement visitFunctionDefinitionStatement(PMLParser.FunctionDefinitionStatementContext ctx) {
		PMLFunctionSignature signature = new SignatureVisitor(visitorCtx)
				.visitFunctionSignature(ctx.functionSignature());

		CheckAndStatementsBlock body = parseBody(
				ctx.basicStatementBlock(),
				signature.getOperands(),
				signature.getOperandTypes(),
				signature.getReturnType()
		);

		// add to visitor context
		try {
			visitorCtx.scope().local().addExecutable(signature.getFunctionName(), signature);
		} catch (ExecutableAlreadyDefinedInScopeException e) {
			throw new PMLCompilationRuntimeException(ctx, e.getMessage());
		}

		return new CreateFunctionStatement(new PMLStmtsRoutine(
				signature.getFunctionName(),
				signature.getReturnType(),
				signature.getOperands(),
				signature.getOperandTypes(),
				body.getStatements()
		));
	}

	private CheckAndStatementsBlock parseBody(PMLParser.BasicStatementBlockContext ctx,
	                                          List<String> operandNames,
	                                          Map<String, Type> operandTypes,
	                                          Type returnType) {
		// create a new scope for the function body
		VisitorContext localVisitorCtx = initLocalVisitorCtx(operandNames, operandTypes);

		StatementBlockVisitor statementBlockVisitor = new StatementBlockVisitor(localVisitorCtx, returnType);
		StatementBlockVisitor.Result result = statementBlockVisitor.visitBasicStatementBlock(ctx);

		if (!result.allPathsReturned() && !returnType.isVoid()) {
			throw new PMLCompilationRuntimeException(ctx, "not all conditional paths return");
		}

		return new CheckAndStatementsBlock(new PMLStatementBlock(), result.stmts());
	}

	private CheckAndStatementsBlock parseBody(PMLParser.CheckStatementBlockContext checkStatementBlockCtx,
	                                          PMLParser.StatementBlockContext statementBlockCtx,
	                                          List<String> operandNames,
	                                          Map<String, Type> operandTypes,
	                                          Type returnType) {
		VisitorContext localVisitorCtx = initLocalVisitorCtx(operandNames, operandTypes);
		StatementBlockVisitor statementBlockVisitor = new StatementBlockVisitor(localVisitorCtx, returnType);
		StatementBlockVisitor.Result result = statementBlockVisitor.visitStatementBlock(statementBlockCtx);

		if (!result.allPathsReturned() && !returnType.isVoid()) {
			throw new PMLCompilationRuntimeException(statementBlockCtx, "not all conditional paths return");
		}

		// get checks
		PMLStatementBlock checks = new PMLStatementBlock();
		if (checkStatementBlockCtx != null) {
			CheckStatementBlockVisitor checkStatementBlockVisitor = new CheckStatementBlockVisitor(localVisitorCtx);
			checks = checkStatementBlockVisitor.visitCheckStatementBlock(checkStatementBlockCtx);
		}

		return new CheckAndStatementsBlock(checks, result.stmts());
	}

	private VisitorContext initLocalVisitorCtx(List<String> operandNames,
	                                           Map<String, Type> operandTypes) {
		// create a new scope for the function body
		VisitorContext localVisitorCtx = visitorCtx.copy();

		// add the args to the local scope, overwriting any variables with the same ID as the formal args
		for (int i = 0; i < operandNames.size(); i++) {
			String name = operandNames.get(i);
			Type type = operandTypes.get(name);

			localVisitorCtx.scope().addOrOverwriteVariable(
					name,
					new Variable(name, type, false)
			);
		}

		return localVisitorCtx;
	}

	static class SignatureVisitor extends PMLBaseVisitor<PMLExecutableSignature> {

		public SignatureVisitor(VisitorContext visitorCtx) {
			super(visitorCtx);
		}

		@Override
		public PMLOperationSignature visitOperationSignature(PMLParser.OperationSignatureContext ctx) {
			String funcName = ctx.ID().getText();
			Type returnType = parseReturnType(ctx.returnType);
			List<FormalOperand> args = new FormalArgListVisitor(visitorCtx).visitFormalArgList(ctx.formalArgList());
			List<String> operandNames = new ArrayList<>();
			Map<String, Type> operandTypes = new HashMap<>();
			List<String> nodeops = new ArrayList<>();

			for (FormalOperand operand : args) {
				operandNames.add(operand.name);
				operandTypes.put(operand.name, operand.type);

				if (operand.isNodeop) {
					nodeops.add(operand.name);
				}
			}

			writeOperandsToScope(operandNames, operandTypes);

			return new PMLOperationSignature(
					funcName,
					returnType,
					operandNames,
					nodeops,
					operandTypes
			);
		}

		@Override
		public PMLRoutineSignature visitRoutineSignature(PMLParser.RoutineSignatureContext ctx) {
			String funcName = ctx.ID().getText();
			Type returnType = parseReturnType(ctx.returnType);
			List<FormalOperand> args = new FormalArgListVisitor(visitorCtx).visitFormalArgList(ctx.formalArgList());
			List<String> operandNames = new ArrayList<>();
			Map<String, Type> operandTypes = new HashMap<>();

			for (FormalOperand operand : args) {
				operandNames.add(operand.name);
				operandTypes.put(operand.name, operand.type);
			}

			writeOperandsToScope(operandNames, operandTypes);

			return new PMLRoutineSignature(
					funcName,
					returnType,
					operandNames,
					operandTypes
			);
		}

		@Override
		public PMLFunctionSignature visitFunctionSignature(PMLParser.FunctionSignatureContext ctx) {
			String funcName = ctx.ID().getText();
			Type returnType = parseReturnType(ctx.returnType);
			List<FormalOperand> args = new FormalArgListVisitor(visitorCtx).visitFormalArgList(ctx.formalArgList());
			List<String> operandNames = new ArrayList<>();
			Map<String, Type> operandTypes = new HashMap<>();

			for (FormalOperand operand : args) {
				operandNames.add(operand.name);
				operandTypes.put(operand.name, operand.type);
			}

			writeOperandsToScope(operandNames, operandTypes);

			return new PMLFunctionSignature(
					funcName,
					returnType,
					operandNames,
					operandTypes
			);
		}

		private void writeOperandsToScope(List<String> operandNames, Map<String, Type> operandTypes) {
			// write operands to scope for compiling check block
			VisitorContext copy = visitorCtx.copy();
			for (int i = 0; i < operandNames.size(); i++) {
				String name = operandNames.get(i);
				Type type = operandTypes.get(name);

				copy.scope().addOrOverwriteVariable(
						name,
						new Variable(name, type, false)
				);
			}
		}

		private Type parseReturnType(PMLParser.VariableTypeContext variableTypeContext) {
			if (variableTypeContext == null) {
				return Type.voidType();
			}

			return Type.toType(variableTypeContext);
		}
	}

	static class FormalArgListVisitor extends PMLBaseVisitor<List<FormalOperand>> {

		public FormalArgListVisitor(VisitorContext visitorCtx) {
			super(visitorCtx);
		}

		@Override
		public List<FormalOperand> visitFormalArgList(PMLParser.FormalArgListContext ctx) {
			List<FormalOperand> formalArgs = new ArrayList<>();
			Set<String> argNames = new HashSet<>();
			for (int i = 0; i < ctx.formalArg().size(); i++) {
				PMLParser.FormalArgContext formalArgCtx = ctx.formalArg().get(i);
				String name = formalArgCtx.ID().getText();
				boolean isNodeop = formalArgCtx.NODEOP() != null;

				// check that two formal args dont have the same name and that there are no constants with the same name
				if (argNames.contains(name)) {
					throw new PMLCompilationRuntimeException(
							formalArgCtx,
							String.format("formal arg '%s' already defined in signature", name)
					);
				}

				// get arg type
				PMLParser.VariableTypeContext varTypeContext = formalArgCtx.variableType();
				Type type = Type.toType(varTypeContext);

				// req cap if operation
				formalArgs.add(new FormalOperand(name, type, isNodeop));

				argNames.add(name);
			}

			return formalArgs;
		}
	}

	record FormalOperand(String name, Type type, boolean isNodeop) {}

}
