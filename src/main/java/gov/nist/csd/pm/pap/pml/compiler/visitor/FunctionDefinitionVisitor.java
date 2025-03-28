package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.compiler.Variable;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.pap.pml.function.PMLFunctionSignature;
import gov.nist.csd.pm.pap.pml.function.arg.PMLFormalArg;
import gov.nist.csd.pm.pap.pml.function.PMLStmtsFunction;
import gov.nist.csd.pm.pap.pml.function.operation.PMLNodeFormalArg;
import gov.nist.csd.pm.pap.pml.function.operation.PMLOperationSignature;
import gov.nist.csd.pm.pap.pml.function.operation.PMLStmtsOperation;
import gov.nist.csd.pm.pap.pml.function.operation.CheckAndStatementsBlock;
import gov.nist.csd.pm.pap.pml.function.routine.PMLRoutineSignature;
import gov.nist.csd.pm.pap.pml.function.routine.PMLStmtsRoutine;
import gov.nist.csd.pm.pap.pml.scope.FunctionAlreadyDefinedInScopeException;
import gov.nist.csd.pm.pap.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.pap.pml.statement.PMLStatementBlock;
import gov.nist.csd.pm.pap.pml.statement.basic.BasicFunctionDefinitionStatement;
import gov.nist.csd.pm.pap.pml.statement.operation.OperationDefinitionStatement;
import gov.nist.csd.pm.pap.pml.statement.operation.RoutineDefinitionStatement;
import gov.nist.csd.pm.pap.pml.type.Type;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.*;

public class FunctionDefinitionVisitor extends PMLBaseVisitor<FunctionDefinitionStatement> {

	private boolean addToCtx;

	public FunctionDefinitionVisitor(VisitorContext visitorCtx, boolean addToCtx) {
		super(visitorCtx);
		this.addToCtx = addToCtx;
	}

	@Override
	public OperationDefinitionStatement visitOperationDefinitionStatement(PMLParser.OperationDefinitionStatementContext ctx) {
		PMLOperationSignature signature = new SignatureVisitor(visitorCtx, addToCtx)
			.visitOperationSignature(ctx.operationSignature());

		CheckAndStatementsBlock body = parseBody(
			ctx.checkStatementBlock(),
			ctx.statementBlock(),
			signature.getReturnType(),
			signature.getFormalArgs()
		);

		return new OperationDefinitionStatement(new PMLStmtsOperation(
			signature.getName(),
			signature.getReturnType(),
			signature.getFormalArgs(),
			body
		));
	}

	@Override
	public RoutineDefinitionStatement visitRoutineDefinitionStatement(PMLParser.RoutineDefinitionStatementContext ctx) {
		PMLRoutineSignature signature = new SignatureVisitor(visitorCtx, addToCtx)
			.visitRoutineSignature(ctx.routineSignature());

		CheckAndStatementsBlock body = parseBody(
			null,
			ctx.statementBlock(),
			signature.getReturnType(),
			signature.getFormalArgs()
		);

		return new RoutineDefinitionStatement(new PMLStmtsRoutine(
			signature.getName(),
			signature.getReturnType(),
			signature.getFormalArgs(),
			body.getStatements()
		));
	}

	@Override
	public BasicFunctionDefinitionStatement visitFunctionDefinitionStatement(PMLParser.FunctionDefinitionStatementContext ctx) {
		PMLFunctionSignature signature = new SignatureVisitor(visitorCtx, addToCtx)
			.visitFunctionSignature(ctx.functionSignature());

		CheckAndStatementsBlock body = parseBody(
			ctx.basicStatementBlock(),
			signature.getReturnType(),
			signature.getFormalArgs()
		);

		return new BasicFunctionDefinitionStatement(new PMLStmtsFunction(
			signature.getName(),
			signature.getReturnType(),
			signature.getFormalArgs(),
			body.getStatements()
		));
	}

	private CheckAndStatementsBlock parseBody(PMLParser.BasicStatementBlockContext ctx,
											  Type returnType,
											  List<PMLFormalArg> formalArgs) {
		// create a new scope for the function body
		VisitorContext localVisitorCtx = initLocalVisitorCtx(formalArgs);

		StatementBlockVisitor statementBlockVisitor = new StatementBlockVisitor(localVisitorCtx, returnType);
		StatementBlockVisitor.Result result = statementBlockVisitor.visitBasicStatementBlock(ctx);

		if (!result.allPathsReturned() && !returnType.isVoid()) {
			throw new PMLCompilationRuntimeException(ctx, "not all conditional paths return");
		}

		return new CheckAndStatementsBlock(new PMLStatementBlock(), result.stmts());
	}

	private CheckAndStatementsBlock parseBody(PMLParser.CheckStatementBlockContext checkStatementBlockCtx,
											  PMLParser.StatementBlockContext statementBlockCtx,
											  Type returnType,
											  List<PMLFormalArg> formalArgs) {
		VisitorContext localVisitorCtx = initLocalVisitorCtx(formalArgs);
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

	private VisitorContext initLocalVisitorCtx(List<PMLFormalArg> formalArgs) {
		// create a new scope for the function body
		VisitorContext localVisitorCtx = visitorCtx.copy();

		// add the args to the local scope, overwriting any variables with the same ID as the formal args
		for (PMLFormalArg formalArg : formalArgs) {
			localVisitorCtx.scope().updateVariable(
				formalArg.getName(),
				new Variable(formalArg.getName(), formalArg.getPmlType(), false)
			);
		}

		return localVisitorCtx;
	}

	static class SignatureVisitor extends PMLBaseVisitor<PMLFunctionSignature> {

		private boolean addToCtx;

		public SignatureVisitor(VisitorContext visitorCtx, boolean addToCtx) {
			super(visitorCtx);
			this.addToCtx = addToCtx;
		}

		@Override
		public PMLOperationSignature visitOperationSignature(PMLParser.OperationSignatureContext ctx) {
			String funcName = ctx.ID().getText();
			Type returnType = parseReturnType(ctx.returnType);
			List<PMLFormalArg> args = new FormalArgListVisitor(visitorCtx).visitFormalArgList(ctx.formalArgList());

			writeArgsToScope(args);

			PMLOperationSignature pmlOperationSignature = new PMLOperationSignature(
				funcName,
				returnType,
				args
			);

			addSignatureToCtx(ctx, funcName, pmlOperationSignature);

			return pmlOperationSignature;
		}

		@Override
		public PMLRoutineSignature visitRoutineSignature(PMLParser.RoutineSignatureContext ctx) {
			String funcName = ctx.ID().getText();
			Type returnType = parseReturnType(ctx.returnType);
			List<PMLFormalArg> args = new FormalArgListVisitor(visitorCtx).visitFormalArgList(ctx.formalArgList());

			writeArgsToScope(args);

			PMLRoutineSignature pmlRoutineSignature = new PMLRoutineSignature(
				funcName,
				returnType,
				args
			);

			addSignatureToCtx(ctx, funcName, pmlRoutineSignature);

			return pmlRoutineSignature;
		}

		@Override
		public PMLFunctionSignature visitFunctionSignature(PMLParser.FunctionSignatureContext ctx) {
			String funcName = ctx.ID().getText();
			Type returnType = parseReturnType(ctx.returnType);
			List<PMLFormalArg> args = new FormalArgListVisitor(visitorCtx).visitFormalArgList(ctx.formalArgList());

			writeArgsToScope(args);

			PMLFunctionSignature pmlFunctionSignature = new PMLFunctionSignature(
				funcName,
				returnType,
				args
			);

			addSignatureToCtx(ctx, funcName, pmlFunctionSignature);

			return pmlFunctionSignature;
		}

		private <T extends ParserRuleContext> void addSignatureToCtx(T ctx, String funcName, PMLFunctionSignature signature) {
			if (!addToCtx) {
				return;
			}

			try {
				visitorCtx.scope().addFunction(funcName, signature);
			} catch (FunctionAlreadyDefinedInScopeException e) {
				visitorCtx.errorLog().addError(ctx, e.getMessage());
			}
		}

		private void writeArgsToScope(List<PMLFormalArg> args) {
			// write operands to scope for compiling check block
			VisitorContext copy = visitorCtx.copy();
            for (PMLFormalArg formalArg : args) {
                copy.scope().updateVariable(
                    formalArg.getName(),
                    new Variable(formalArg.getName(), formalArg.getPmlType(), false)
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

	static class FormalArgListVisitor extends PMLBaseVisitor<List<PMLFormalArg>> {

		public FormalArgListVisitor(VisitorContext visitorCtx) {
			super(visitorCtx);
		}

		@Override
		public List<PMLFormalArg> visitFormalArgList(PMLParser.FormalArgListContext ctx) {
			List<PMLFormalArg> formalArgs = new ArrayList<>();
			Set<String> argNames = new HashSet<>();
			for (int i = 0; i < ctx.formalArg().size(); i++) {
				PMLParser.FormalArgContext formalArgCtx = ctx.formalArg().get(i);
				String name = formalArgCtx.ID().getText();
				boolean isNodeop = formalArgCtx.NODE_ARG() != null;

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

				if (isNodeop) {
					formalArgs.add(new PMLNodeFormalArg(name, type));
				} else {
					formalArgs.add(new PMLFormalArg(name, type));
				}

				argNames.add(name);
			}

			return formalArgs;
		}
	}
}
