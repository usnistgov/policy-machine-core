// Generated from PMLParser.g4 by ANTLR 4.13.1
package gov.nist.csd.pm.pap.pml.antlr;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class PMLParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		OPERATION=1, NODEOP=2, CHECK=3, ROUTINE=4, CREATE=5, DELETE=6, POLICY_ELEMENT=7, 
		CONTAINED=8, RULE=9, WHEN=10, PERFORMS=11, AS=12, ON=13, IN=14, DO=15, 
		ANY=16, ASCENDANT_OF=17, INTERSECTION=18, UNION=19, PROCESS=20, SET_RESOURCE_OPERATIONS=21, 
		ASSIGN=22, DEASSIGN=23, FROM=24, SET_PROPERTIES=25, WITH_PROPERTIES=26, 
		OF=27, TO=28, ASSOCIATE=29, AND=30, WITH=31, DISSOCIATE=32, DENY=33, PROHIBITION=34, 
		OBLIGATION=35, ACCESS_RIGHTS=36, POLICY_CLASS=37, OBJECT_ATTRIBUTE=38, 
		USER_ATTRIBUTE=39, USER_ATTRIBUTES=40, OBJECT_ATTRIBUTES=41, OBJECT=42, 
		USER=43, ATTRIBUTE=44, ASSOCIATIONS=45, BREAK=46, DEFAULT=47, MAP=48, 
		ELSE=49, CONST=50, IF=51, RANGE=52, CONTINUE=53, FOREACH=54, RETURN=55, 
		VAR=56, STRING_TYPE=57, BOOL_TYPE=58, VOID_TYPE=59, ARRAY_TYPE=60, NIL_LIT=61, 
		TRUE=62, FALSE=63, ID=64, OPEN_PAREN=65, CLOSE_PAREN=66, OPEN_CURLY=67, 
		CLOSE_CURLY=68, OPEN_BRACKET=69, CLOSE_BRACKET=70, ASSIGN_EQUALS=71, COMMA=72, 
		SEMI=73, COLON=74, DOT=75, DECLARE_ASSIGN=76, LOGICAL_OR=77, LOGICAL_AND=78, 
		EQUALS=79, NOT_EQUALS=80, EXCLAMATION=81, PLUS=82, DOUBLE_QUOTE_STRING=83, 
		WS=84, COMMENT=85, LINE_COMMENT=86;
	public static final int
		RULE_pml = 0, RULE_statement = 1, RULE_statementBlock = 2, RULE_createPolicyStatement = 3, 
		RULE_createNonPCStatement = 4, RULE_nonPCNodeType = 5, RULE_createObligationStatement = 6, 
		RULE_createRuleStatement = 7, RULE_subjectPattern = 8, RULE_subjectPatternExpression = 9, 
		RULE_basicSubjectPatternExpr = 10, RULE_operationPattern = 11, RULE_operandPattern = 12, 
		RULE_operandPatternElement = 13, RULE_operandPatternExpressionArray = 14, 
		RULE_operandPatternExpression = 15, RULE_basicOperandPatternExpr = 16, 
		RULE_response = 17, RULE_responseBlock = 18, RULE_responseStatement = 19, 
		RULE_createProhibitionStatement = 20, RULE_setNodePropertiesStatement = 21, 
		RULE_assignStatement = 22, RULE_deassignStatement = 23, RULE_associateStatement = 24, 
		RULE_dissociateStatement = 25, RULE_setResourceOperationsStatement = 26, 
		RULE_deleteStatement = 27, RULE_deleteType = 28, RULE_nodeType = 29, RULE_deleteRuleStatement = 30, 
		RULE_variableDeclarationStatement = 31, RULE_varSpec = 32, RULE_variableAssignmentStatement = 33, 
		RULE_functionDefinitionStatement = 34, RULE_functionSignature = 35, RULE_formalArgList = 36, 
		RULE_formalArg = 37, RULE_returnStatement = 38, RULE_checkStatement = 39, 
		RULE_checkStatementBlock = 40, RULE_idArr = 41, RULE_functionInvokeStatement = 42, 
		RULE_foreachStatement = 43, RULE_breakStatement = 44, RULE_continueStatement = 45, 
		RULE_ifStatement = 46, RULE_elseIfStatement = 47, RULE_elseStatement = 48, 
		RULE_variableType = 49, RULE_mapType = 50, RULE_arrayType = 51, RULE_expression = 52, 
		RULE_expressionList = 53, RULE_literal = 54, RULE_stringLit = 55, RULE_boolLit = 56, 
		RULE_arrayLit = 57, RULE_stringArrayLit = 58, RULE_mapLit = 59, RULE_element = 60, 
		RULE_variableReference = 61, RULE_index = 62, RULE_id = 63, RULE_functionInvoke = 64, 
		RULE_functionInvokeArgs = 65;
	private static String[] makeRuleNames() {
		return new String[] {
			"pml", "statement", "statementBlock", "createPolicyStatement", "createNonPCStatement", 
			"nonPCNodeType", "createObligationStatement", "createRuleStatement", 
			"subjectPattern", "subjectPatternExpression", "basicSubjectPatternExpr", 
			"operationPattern", "operandPattern", "operandPatternElement", "operandPatternExpressionArray", 
			"operandPatternExpression", "basicOperandPatternExpr", "response", "responseBlock", 
			"responseStatement", "createProhibitionStatement", "setNodePropertiesStatement", 
			"assignStatement", "deassignStatement", "associateStatement", "dissociateStatement", 
			"setResourceOperationsStatement", "deleteStatement", "deleteType", "nodeType", 
			"deleteRuleStatement", "variableDeclarationStatement", "varSpec", "variableAssignmentStatement", 
			"functionDefinitionStatement", "functionSignature", "formalArgList", 
			"formalArg", "returnStatement", "checkStatement", "checkStatementBlock", 
			"idArr", "functionInvokeStatement", "foreachStatement", "breakStatement", 
			"continueStatement", "ifStatement", "elseIfStatement", "elseStatement", 
			"variableType", "mapType", "arrayType", "expression", "expressionList", 
			"literal", "stringLit", "boolLit", "arrayLit", "stringArrayLit", "mapLit", 
			"element", "variableReference", "index", "id", "functionInvoke", "functionInvokeArgs"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'operation'", "'nodeop'", "'check'", "'routine'", "'create'", 
			"'delete'", null, "'contained'", "'rule'", "'when'", "'performs'", "'as'", 
			"'on'", "'in'", "'do'", "'any'", "'ascendant of'", null, "'union'", "'process'", 
			"'set resource operations'", "'assign'", "'deassign'", "'from'", "'set properties'", 
			"'with properties'", "'of'", "'to'", "'associate'", "'and'", "'with'", 
			"'dissociate'", "'deny'", "'prohibition'", "'obligation'", "'access rights'", 
			null, null, null, null, null, null, null, "'attribute'", "'associations'", 
			"'break'", "'default'", "'map'", "'else'", "'const'", "'if'", "'range'", 
			"'continue'", "'foreach'", "'return'", "'var'", "'string'", "'bool'", 
			"'void'", "'array'", "'nil'", "'true'", "'false'", null, "'('", "')'", 
			"'{'", "'}'", "'['", "']'", "'='", "','", "';'", "':'", "'.'", "':='", 
			"'||'", "'&&'", "'=='", "'!='", "'!'", "'+'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "OPERATION", "NODEOP", "CHECK", "ROUTINE", "CREATE", "DELETE", 
			"POLICY_ELEMENT", "CONTAINED", "RULE", "WHEN", "PERFORMS", "AS", "ON", 
			"IN", "DO", "ANY", "ASCENDANT_OF", "INTERSECTION", "UNION", "PROCESS", 
			"SET_RESOURCE_OPERATIONS", "ASSIGN", "DEASSIGN", "FROM", "SET_PROPERTIES", 
			"WITH_PROPERTIES", "OF", "TO", "ASSOCIATE", "AND", "WITH", "DISSOCIATE", 
			"DENY", "PROHIBITION", "OBLIGATION", "ACCESS_RIGHTS", "POLICY_CLASS", 
			"OBJECT_ATTRIBUTE", "USER_ATTRIBUTE", "USER_ATTRIBUTES", "OBJECT_ATTRIBUTES", 
			"OBJECT", "USER", "ATTRIBUTE", "ASSOCIATIONS", "BREAK", "DEFAULT", "MAP", 
			"ELSE", "CONST", "IF", "RANGE", "CONTINUE", "FOREACH", "RETURN", "VAR", 
			"STRING_TYPE", "BOOL_TYPE", "VOID_TYPE", "ARRAY_TYPE", "NIL_LIT", "TRUE", 
			"FALSE", "ID", "OPEN_PAREN", "CLOSE_PAREN", "OPEN_CURLY", "CLOSE_CURLY", 
			"OPEN_BRACKET", "CLOSE_BRACKET", "ASSIGN_EQUALS", "COMMA", "SEMI", "COLON", 
			"DOT", "DECLARE_ASSIGN", "LOGICAL_OR", "LOGICAL_AND", "EQUALS", "NOT_EQUALS", 
			"EXCLAMATION", "PLUS", "DOUBLE_QUOTE_STRING", "WS", "COMMENT", "LINE_COMMENT"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "PMLParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public PMLParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PmlContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(PMLParser.EOF, 0); }
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public PmlContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pml; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterPml(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitPml(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitPml(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PmlContext pml() throws RecognitionException {
		PmlContext _localctx = new PmlContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_pml);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(135);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 1)) & ~0x3f) == 0 && ((1L << (_la - 1)) & -9154656955725250503L) != 0)) {
				{
				{
				setState(132);
				statement();
				}
				}
				setState(137);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(138);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatementContext extends ParserRuleContext {
		public VariableAssignmentStatementContext variableAssignmentStatement() {
			return getRuleContext(VariableAssignmentStatementContext.class,0);
		}
		public VariableDeclarationStatementContext variableDeclarationStatement() {
			return getRuleContext(VariableDeclarationStatementContext.class,0);
		}
		public ForeachStatementContext foreachStatement() {
			return getRuleContext(ForeachStatementContext.class,0);
		}
		public ReturnStatementContext returnStatement() {
			return getRuleContext(ReturnStatementContext.class,0);
		}
		public BreakStatementContext breakStatement() {
			return getRuleContext(BreakStatementContext.class,0);
		}
		public ContinueStatementContext continueStatement() {
			return getRuleContext(ContinueStatementContext.class,0);
		}
		public FunctionInvokeStatementContext functionInvokeStatement() {
			return getRuleContext(FunctionInvokeStatementContext.class,0);
		}
		public IfStatementContext ifStatement() {
			return getRuleContext(IfStatementContext.class,0);
		}
		public CreatePolicyStatementContext createPolicyStatement() {
			return getRuleContext(CreatePolicyStatementContext.class,0);
		}
		public CreateNonPCStatementContext createNonPCStatement() {
			return getRuleContext(CreateNonPCStatementContext.class,0);
		}
		public CreateObligationStatementContext createObligationStatement() {
			return getRuleContext(CreateObligationStatementContext.class,0);
		}
		public CreateProhibitionStatementContext createProhibitionStatement() {
			return getRuleContext(CreateProhibitionStatementContext.class,0);
		}
		public SetNodePropertiesStatementContext setNodePropertiesStatement() {
			return getRuleContext(SetNodePropertiesStatementContext.class,0);
		}
		public AssignStatementContext assignStatement() {
			return getRuleContext(AssignStatementContext.class,0);
		}
		public DeassignStatementContext deassignStatement() {
			return getRuleContext(DeassignStatementContext.class,0);
		}
		public AssociateStatementContext associateStatement() {
			return getRuleContext(AssociateStatementContext.class,0);
		}
		public DissociateStatementContext dissociateStatement() {
			return getRuleContext(DissociateStatementContext.class,0);
		}
		public SetResourceOperationsStatementContext setResourceOperationsStatement() {
			return getRuleContext(SetResourceOperationsStatementContext.class,0);
		}
		public DeleteStatementContext deleteStatement() {
			return getRuleContext(DeleteStatementContext.class,0);
		}
		public DeleteRuleStatementContext deleteRuleStatement() {
			return getRuleContext(DeleteRuleStatementContext.class,0);
		}
		public FunctionDefinitionStatementContext functionDefinitionStatement() {
			return getRuleContext(FunctionDefinitionStatementContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_statement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(161);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				{
				setState(140);
				variableAssignmentStatement();
				}
				break;
			case 2:
				{
				setState(141);
				variableDeclarationStatement();
				}
				break;
			case 3:
				{
				setState(142);
				foreachStatement();
				}
				break;
			case 4:
				{
				setState(143);
				returnStatement();
				}
				break;
			case 5:
				{
				setState(144);
				breakStatement();
				}
				break;
			case 6:
				{
				setState(145);
				continueStatement();
				}
				break;
			case 7:
				{
				setState(146);
				functionInvokeStatement();
				}
				break;
			case 8:
				{
				setState(147);
				ifStatement();
				}
				break;
			case 9:
				{
				setState(148);
				createPolicyStatement();
				}
				break;
			case 10:
				{
				setState(149);
				createNonPCStatement();
				}
				break;
			case 11:
				{
				setState(150);
				createObligationStatement();
				}
				break;
			case 12:
				{
				setState(151);
				createProhibitionStatement();
				}
				break;
			case 13:
				{
				setState(152);
				setNodePropertiesStatement();
				}
				break;
			case 14:
				{
				setState(153);
				assignStatement();
				}
				break;
			case 15:
				{
				setState(154);
				deassignStatement();
				}
				break;
			case 16:
				{
				setState(155);
				associateStatement();
				}
				break;
			case 17:
				{
				setState(156);
				dissociateStatement();
				}
				break;
			case 18:
				{
				setState(157);
				setResourceOperationsStatement();
				}
				break;
			case 19:
				{
				setState(158);
				deleteStatement();
				}
				break;
			case 20:
				{
				setState(159);
				deleteRuleStatement();
				}
				break;
			case 21:
				{
				setState(160);
				functionDefinitionStatement();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatementBlockContext extends ParserRuleContext {
		public TerminalNode OPEN_CURLY() { return getToken(PMLParser.OPEN_CURLY, 0); }
		public TerminalNode CLOSE_CURLY() { return getToken(PMLParser.CLOSE_CURLY, 0); }
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public StatementBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statementBlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterStatementBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitStatementBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitStatementBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementBlockContext statementBlock() throws RecognitionException {
		StatementBlockContext _localctx = new StatementBlockContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_statementBlock);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(163);
			match(OPEN_CURLY);
			setState(167);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 1)) & ~0x3f) == 0 && ((1L << (_la - 1)) & -9154656955725250503L) != 0)) {
				{
				{
				setState(164);
				statement();
				}
				}
				setState(169);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(170);
			match(CLOSE_CURLY);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CreatePolicyStatementContext extends ParserRuleContext {
		public ExpressionContext name;
		public TerminalNode CREATE() { return getToken(PMLParser.CREATE, 0); }
		public TerminalNode POLICY_CLASS() { return getToken(PMLParser.POLICY_CLASS, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public CreatePolicyStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_createPolicyStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterCreatePolicyStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitCreatePolicyStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitCreatePolicyStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreatePolicyStatementContext createPolicyStatement() throws RecognitionException {
		CreatePolicyStatementContext _localctx = new CreatePolicyStatementContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_createPolicyStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(172);
			match(CREATE);
			setState(173);
			match(POLICY_CLASS);
			setState(174);
			((CreatePolicyStatementContext)_localctx).name = expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CreateNonPCStatementContext extends ParserRuleContext {
		public ExpressionContext name;
		public ExpressionContext in;
		public TerminalNode CREATE() { return getToken(PMLParser.CREATE, 0); }
		public NonPCNodeTypeContext nonPCNodeType() {
			return getRuleContext(NonPCNodeTypeContext.class,0);
		}
		public TerminalNode IN() { return getToken(PMLParser.IN, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public CreateNonPCStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_createNonPCStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterCreateNonPCStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitCreateNonPCStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitCreateNonPCStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreateNonPCStatementContext createNonPCStatement() throws RecognitionException {
		CreateNonPCStatementContext _localctx = new CreateNonPCStatementContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_createNonPCStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(176);
			match(CREATE);
			setState(177);
			nonPCNodeType();
			setState(178);
			((CreateNonPCStatementContext)_localctx).name = expression(0);
			setState(179);
			match(IN);
			setState(180);
			((CreateNonPCStatementContext)_localctx).in = expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NonPCNodeTypeContext extends ParserRuleContext {
		public TerminalNode OBJECT_ATTRIBUTE() { return getToken(PMLParser.OBJECT_ATTRIBUTE, 0); }
		public TerminalNode USER_ATTRIBUTE() { return getToken(PMLParser.USER_ATTRIBUTE, 0); }
		public TerminalNode OBJECT() { return getToken(PMLParser.OBJECT, 0); }
		public TerminalNode USER() { return getToken(PMLParser.USER, 0); }
		public NonPCNodeTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonPCNodeType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterNonPCNodeType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitNonPCNodeType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitNonPCNodeType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NonPCNodeTypeContext nonPCNodeType() throws RecognitionException {
		NonPCNodeTypeContext _localctx = new NonPCNodeTypeContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_nonPCNodeType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(182);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 14018773254144L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CreateObligationStatementContext extends ParserRuleContext {
		public TerminalNode CREATE() { return getToken(PMLParser.CREATE, 0); }
		public TerminalNode OBLIGATION() { return getToken(PMLParser.OBLIGATION, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode OPEN_CURLY() { return getToken(PMLParser.OPEN_CURLY, 0); }
		public TerminalNode CLOSE_CURLY() { return getToken(PMLParser.CLOSE_CURLY, 0); }
		public List<CreateRuleStatementContext> createRuleStatement() {
			return getRuleContexts(CreateRuleStatementContext.class);
		}
		public CreateRuleStatementContext createRuleStatement(int i) {
			return getRuleContext(CreateRuleStatementContext.class,i);
		}
		public CreateObligationStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_createObligationStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterCreateObligationStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitCreateObligationStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitCreateObligationStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreateObligationStatementContext createObligationStatement() throws RecognitionException {
		CreateObligationStatementContext _localctx = new CreateObligationStatementContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_createObligationStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(184);
			match(CREATE);
			setState(185);
			match(OBLIGATION);
			setState(186);
			expression(0);
			setState(187);
			match(OPEN_CURLY);
			setState(191);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==CREATE) {
				{
				{
				setState(188);
				createRuleStatement();
				}
				}
				setState(193);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(194);
			match(CLOSE_CURLY);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CreateRuleStatementContext extends ParserRuleContext {
		public ExpressionContext ruleName;
		public TerminalNode CREATE() { return getToken(PMLParser.CREATE, 0); }
		public TerminalNode RULE() { return getToken(PMLParser.RULE, 0); }
		public TerminalNode WHEN() { return getToken(PMLParser.WHEN, 0); }
		public SubjectPatternContext subjectPattern() {
			return getRuleContext(SubjectPatternContext.class,0);
		}
		public TerminalNode PERFORMS() { return getToken(PMLParser.PERFORMS, 0); }
		public OperationPatternContext operationPattern() {
			return getRuleContext(OperationPatternContext.class,0);
		}
		public ResponseContext response() {
			return getRuleContext(ResponseContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode ON() { return getToken(PMLParser.ON, 0); }
		public OperandPatternContext operandPattern() {
			return getRuleContext(OperandPatternContext.class,0);
		}
		public CreateRuleStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_createRuleStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterCreateRuleStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitCreateRuleStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitCreateRuleStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreateRuleStatementContext createRuleStatement() throws RecognitionException {
		CreateRuleStatementContext _localctx = new CreateRuleStatementContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_createRuleStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(196);
			match(CREATE);
			setState(197);
			match(RULE);
			setState(198);
			((CreateRuleStatementContext)_localctx).ruleName = expression(0);
			setState(199);
			match(WHEN);
			setState(200);
			subjectPattern();
			setState(201);
			match(PERFORMS);
			setState(202);
			operationPattern();
			setState(205);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ON) {
				{
				setState(203);
				match(ON);
				setState(204);
				operandPattern();
				}
			}

			setState(207);
			response();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SubjectPatternContext extends ParserRuleContext {
		public SubjectPatternContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_subjectPattern; }
	 
		public SubjectPatternContext() { }
		public void copyFrom(SubjectPatternContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AnyUserPatternContext extends SubjectPatternContext {
		public TerminalNode ANY() { return getToken(PMLParser.ANY, 0); }
		public TerminalNode USER() { return getToken(PMLParser.USER, 0); }
		public AnyUserPatternContext(SubjectPatternContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterAnyUserPattern(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitAnyUserPattern(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitAnyUserPattern(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class UserPatternContext extends SubjectPatternContext {
		public TerminalNode USER() { return getToken(PMLParser.USER, 0); }
		public SubjectPatternExpressionContext subjectPatternExpression() {
			return getRuleContext(SubjectPatternExpressionContext.class,0);
		}
		public UserPatternContext(SubjectPatternContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterUserPattern(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitUserPattern(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitUserPattern(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SubjectPatternContext subjectPattern() throws RecognitionException {
		SubjectPatternContext _localctx = new SubjectPatternContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_subjectPattern);
		try {
			setState(213);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ANY:
				_localctx = new AnyUserPatternContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(209);
				match(ANY);
				setState(210);
				match(USER);
				}
				break;
			case USER:
				_localctx = new UserPatternContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(211);
				match(USER);
				setState(212);
				subjectPatternExpression(0);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SubjectPatternExpressionContext extends ParserRuleContext {
		public SubjectPatternExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_subjectPatternExpression; }
	 
		public SubjectPatternExpressionContext() { }
		public void copyFrom(SubjectPatternExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BasicSubjectPatternExpressionContext extends SubjectPatternExpressionContext {
		public BasicSubjectPatternExprContext basicSubjectPatternExpr() {
			return getRuleContext(BasicSubjectPatternExprContext.class,0);
		}
		public BasicSubjectPatternExpressionContext(SubjectPatternExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterBasicSubjectPatternExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitBasicSubjectPatternExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitBasicSubjectPatternExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ParenSubjectPatternExpressionContext extends SubjectPatternExpressionContext {
		public TerminalNode OPEN_PAREN() { return getToken(PMLParser.OPEN_PAREN, 0); }
		public SubjectPatternExpressionContext subjectPatternExpression() {
			return getRuleContext(SubjectPatternExpressionContext.class,0);
		}
		public TerminalNode CLOSE_PAREN() { return getToken(PMLParser.CLOSE_PAREN, 0); }
		public ParenSubjectPatternExpressionContext(SubjectPatternExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterParenSubjectPatternExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitParenSubjectPatternExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitParenSubjectPatternExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NegateSubjectPatternExpressionContext extends SubjectPatternExpressionContext {
		public TerminalNode EXCLAMATION() { return getToken(PMLParser.EXCLAMATION, 0); }
		public SubjectPatternExpressionContext subjectPatternExpression() {
			return getRuleContext(SubjectPatternExpressionContext.class,0);
		}
		public NegateSubjectPatternExpressionContext(SubjectPatternExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterNegateSubjectPatternExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitNegateSubjectPatternExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitNegateSubjectPatternExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class LogicalSubjectPatternExpressionContext extends SubjectPatternExpressionContext {
		public SubjectPatternExpressionContext left;
		public SubjectPatternExpressionContext right;
		public List<SubjectPatternExpressionContext> subjectPatternExpression() {
			return getRuleContexts(SubjectPatternExpressionContext.class);
		}
		public SubjectPatternExpressionContext subjectPatternExpression(int i) {
			return getRuleContext(SubjectPatternExpressionContext.class,i);
		}
		public TerminalNode LOGICAL_AND() { return getToken(PMLParser.LOGICAL_AND, 0); }
		public TerminalNode LOGICAL_OR() { return getToken(PMLParser.LOGICAL_OR, 0); }
		public LogicalSubjectPatternExpressionContext(SubjectPatternExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterLogicalSubjectPatternExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitLogicalSubjectPatternExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitLogicalSubjectPatternExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SubjectPatternExpressionContext subjectPatternExpression() throws RecognitionException {
		return subjectPatternExpression(0);
	}

	private SubjectPatternExpressionContext subjectPatternExpression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		SubjectPatternExpressionContext _localctx = new SubjectPatternExpressionContext(_ctx, _parentState);
		SubjectPatternExpressionContext _prevctx = _localctx;
		int _startState = 18;
		enterRecursionRule(_localctx, 18, RULE_subjectPatternExpression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(223);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IN:
			case PROCESS:
			case DOUBLE_QUOTE_STRING:
				{
				_localctx = new BasicSubjectPatternExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(216);
				basicSubjectPatternExpr();
				}
				break;
			case EXCLAMATION:
				{
				_localctx = new NegateSubjectPatternExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(217);
				match(EXCLAMATION);
				setState(218);
				subjectPatternExpression(3);
				}
				break;
			case OPEN_PAREN:
				{
				_localctx = new ParenSubjectPatternExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(219);
				match(OPEN_PAREN);
				setState(220);
				subjectPatternExpression(0);
				setState(221);
				match(CLOSE_PAREN);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(230);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new LogicalSubjectPatternExpressionContext(new SubjectPatternExpressionContext(_parentctx, _parentState));
					((LogicalSubjectPatternExpressionContext)_localctx).left = _prevctx;
					pushNewRecursionContext(_localctx, _startState, RULE_subjectPatternExpression);
					setState(225);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(226);
					_la = _input.LA(1);
					if ( !(_la==LOGICAL_OR || _la==LOGICAL_AND) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(227);
					((LogicalSubjectPatternExpressionContext)_localctx).right = subjectPatternExpression(2);
					}
					} 
				}
				setState(232);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BasicSubjectPatternExprContext extends ParserRuleContext {
		public BasicSubjectPatternExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_basicSubjectPatternExpr; }
	 
		public BasicSubjectPatternExprContext() { }
		public void copyFrom(BasicSubjectPatternExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class InSubjectContext extends BasicSubjectPatternExprContext {
		public TerminalNode IN() { return getToken(PMLParser.IN, 0); }
		public StringLitContext stringLit() {
			return getRuleContext(StringLitContext.class,0);
		}
		public InSubjectContext(BasicSubjectPatternExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterInSubject(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitInSubject(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitInSubject(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class UsernameSubjectContext extends BasicSubjectPatternExprContext {
		public StringLitContext stringLit() {
			return getRuleContext(StringLitContext.class,0);
		}
		public UsernameSubjectContext(BasicSubjectPatternExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterUsernameSubject(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitUsernameSubject(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitUsernameSubject(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ProcessSubjectContext extends BasicSubjectPatternExprContext {
		public TerminalNode PROCESS() { return getToken(PMLParser.PROCESS, 0); }
		public StringLitContext stringLit() {
			return getRuleContext(StringLitContext.class,0);
		}
		public ProcessSubjectContext(BasicSubjectPatternExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterProcessSubject(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitProcessSubject(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitProcessSubject(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BasicSubjectPatternExprContext basicSubjectPatternExpr() throws RecognitionException {
		BasicSubjectPatternExprContext _localctx = new BasicSubjectPatternExprContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_basicSubjectPatternExpr);
		try {
			setState(238);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IN:
				_localctx = new InSubjectContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(233);
				match(IN);
				setState(234);
				stringLit();
				}
				break;
			case DOUBLE_QUOTE_STRING:
				_localctx = new UsernameSubjectContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(235);
				stringLit();
				}
				break;
			case PROCESS:
				_localctx = new ProcessSubjectContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(236);
				match(PROCESS);
				setState(237);
				stringLit();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OperationPatternContext extends ParserRuleContext {
		public OperationPatternContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operationPattern; }
	 
		public OperationPatternContext() { }
		public void copyFrom(OperationPatternContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AnyOperationContext extends OperationPatternContext {
		public TerminalNode ANY() { return getToken(PMLParser.ANY, 0); }
		public TerminalNode OPERATION() { return getToken(PMLParser.OPERATION, 0); }
		public AnyOperationContext(OperationPatternContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterAnyOperation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitAnyOperation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitAnyOperation(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IDOperationContext extends OperationPatternContext {
		public StringLitContext stringLit() {
			return getRuleContext(StringLitContext.class,0);
		}
		public IDOperationContext(OperationPatternContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterIDOperation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitIDOperation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitIDOperation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OperationPatternContext operationPattern() throws RecognitionException {
		OperationPatternContext _localctx = new OperationPatternContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_operationPattern);
		try {
			setState(243);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ANY:
				_localctx = new AnyOperationContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(240);
				match(ANY);
				setState(241);
				match(OPERATION);
				}
				break;
			case DOUBLE_QUOTE_STRING:
				_localctx = new IDOperationContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(242);
				stringLit();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OperandPatternContext extends ParserRuleContext {
		public TerminalNode OPEN_CURLY() { return getToken(PMLParser.OPEN_CURLY, 0); }
		public TerminalNode CLOSE_CURLY() { return getToken(PMLParser.CLOSE_CURLY, 0); }
		public List<OperandPatternElementContext> operandPatternElement() {
			return getRuleContexts(OperandPatternElementContext.class);
		}
		public OperandPatternElementContext operandPatternElement(int i) {
			return getRuleContext(OperandPatternElementContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(PMLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(PMLParser.COMMA, i);
		}
		public OperandPatternContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operandPattern; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterOperandPattern(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitOperandPattern(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitOperandPattern(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OperandPatternContext operandPattern() throws RecognitionException {
		OperandPatternContext _localctx = new OperandPatternContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_operandPattern);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(245);
			match(OPEN_CURLY);
			setState(254);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ID) {
				{
				setState(246);
				operandPatternElement();
				setState(251);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(247);
					match(COMMA);
					setState(248);
					operandPatternElement();
					}
					}
					setState(253);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(256);
			match(CLOSE_CURLY);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OperandPatternElementContext extends ParserRuleContext {
		public Token key;
		public OperandPatternExpressionContext single;
		public OperandPatternExpressionArrayContext multiple;
		public TerminalNode COLON() { return getToken(PMLParser.COLON, 0); }
		public TerminalNode ID() { return getToken(PMLParser.ID, 0); }
		public OperandPatternExpressionContext operandPatternExpression() {
			return getRuleContext(OperandPatternExpressionContext.class,0);
		}
		public OperandPatternExpressionArrayContext operandPatternExpressionArray() {
			return getRuleContext(OperandPatternExpressionArrayContext.class,0);
		}
		public OperandPatternElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operandPatternElement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterOperandPatternElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitOperandPatternElement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitOperandPatternElement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OperandPatternElementContext operandPatternElement() throws RecognitionException {
		OperandPatternElementContext _localctx = new OperandPatternElementContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_operandPatternElement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(258);
			((OperandPatternElementContext)_localctx).key = match(ID);
			setState(259);
			match(COLON);
			setState(262);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IN:
			case ANY:
			case OPEN_PAREN:
			case EXCLAMATION:
			case DOUBLE_QUOTE_STRING:
				{
				setState(260);
				((OperandPatternElementContext)_localctx).single = operandPatternExpression(0);
				}
				break;
			case OPEN_BRACKET:
				{
				setState(261);
				((OperandPatternElementContext)_localctx).multiple = operandPatternExpressionArray();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OperandPatternExpressionArrayContext extends ParserRuleContext {
		public TerminalNode OPEN_BRACKET() { return getToken(PMLParser.OPEN_BRACKET, 0); }
		public List<OperandPatternExpressionContext> operandPatternExpression() {
			return getRuleContexts(OperandPatternExpressionContext.class);
		}
		public OperandPatternExpressionContext operandPatternExpression(int i) {
			return getRuleContext(OperandPatternExpressionContext.class,i);
		}
		public TerminalNode CLOSE_BRACKET() { return getToken(PMLParser.CLOSE_BRACKET, 0); }
		public List<TerminalNode> COMMA() { return getTokens(PMLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(PMLParser.COMMA, i);
		}
		public OperandPatternExpressionArrayContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operandPatternExpressionArray; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterOperandPatternExpressionArray(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitOperandPatternExpressionArray(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitOperandPatternExpressionArray(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OperandPatternExpressionArrayContext operandPatternExpressionArray() throws RecognitionException {
		OperandPatternExpressionArrayContext _localctx = new OperandPatternExpressionArrayContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_operandPatternExpressionArray);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(264);
			match(OPEN_BRACKET);
			setState(265);
			operandPatternExpression(0);
			setState(270);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(266);
				match(COMMA);
				setState(267);
				operandPatternExpression(0);
				}
				}
				setState(272);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(273);
			match(CLOSE_BRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OperandPatternExpressionContext extends ParserRuleContext {
		public OperandPatternExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operandPatternExpression; }
	 
		public OperandPatternExpressionContext() { }
		public void copyFrom(OperandPatternExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ParenOperandPatternExpressionContext extends OperandPatternExpressionContext {
		public TerminalNode OPEN_PAREN() { return getToken(PMLParser.OPEN_PAREN, 0); }
		public OperandPatternExpressionContext operandPatternExpression() {
			return getRuleContext(OperandPatternExpressionContext.class,0);
		}
		public TerminalNode CLOSE_PAREN() { return getToken(PMLParser.CLOSE_PAREN, 0); }
		public ParenOperandPatternExpressionContext(OperandPatternExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterParenOperandPatternExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitParenOperandPatternExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitParenOperandPatternExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NegateOperandPatternExpressionContext extends OperandPatternExpressionContext {
		public TerminalNode EXCLAMATION() { return getToken(PMLParser.EXCLAMATION, 0); }
		public OperandPatternExpressionContext operandPatternExpression() {
			return getRuleContext(OperandPatternExpressionContext.class,0);
		}
		public NegateOperandPatternExpressionContext(OperandPatternExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterNegateOperandPatternExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitNegateOperandPatternExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitNegateOperandPatternExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BasicOperandPatternExpressionContext extends OperandPatternExpressionContext {
		public BasicOperandPatternExprContext basicOperandPatternExpr() {
			return getRuleContext(BasicOperandPatternExprContext.class,0);
		}
		public BasicOperandPatternExpressionContext(OperandPatternExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterBasicOperandPatternExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitBasicOperandPatternExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitBasicOperandPatternExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class LogicalOperandPatternExpressionContext extends OperandPatternExpressionContext {
		public OperandPatternExpressionContext left;
		public OperandPatternExpressionContext right;
		public List<OperandPatternExpressionContext> operandPatternExpression() {
			return getRuleContexts(OperandPatternExpressionContext.class);
		}
		public OperandPatternExpressionContext operandPatternExpression(int i) {
			return getRuleContext(OperandPatternExpressionContext.class,i);
		}
		public TerminalNode LOGICAL_AND() { return getToken(PMLParser.LOGICAL_AND, 0); }
		public TerminalNode LOGICAL_OR() { return getToken(PMLParser.LOGICAL_OR, 0); }
		public LogicalOperandPatternExpressionContext(OperandPatternExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterLogicalOperandPatternExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitLogicalOperandPatternExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitLogicalOperandPatternExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OperandPatternExpressionContext operandPatternExpression() throws RecognitionException {
		return operandPatternExpression(0);
	}

	private OperandPatternExpressionContext operandPatternExpression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		OperandPatternExpressionContext _localctx = new OperandPatternExpressionContext(_ctx, _parentState);
		OperandPatternExpressionContext _prevctx = _localctx;
		int _startState = 30;
		enterRecursionRule(_localctx, 30, RULE_operandPatternExpression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(283);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IN:
			case ANY:
			case DOUBLE_QUOTE_STRING:
				{
				_localctx = new BasicOperandPatternExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(276);
				basicOperandPatternExpr();
				}
				break;
			case EXCLAMATION:
				{
				_localctx = new NegateOperandPatternExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(277);
				match(EXCLAMATION);
				setState(278);
				operandPatternExpression(3);
				}
				break;
			case OPEN_PAREN:
				{
				_localctx = new ParenOperandPatternExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(279);
				match(OPEN_PAREN);
				setState(280);
				operandPatternExpression(0);
				setState(281);
				match(CLOSE_PAREN);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(290);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new LogicalOperandPatternExpressionContext(new OperandPatternExpressionContext(_parentctx, _parentState));
					((LogicalOperandPatternExpressionContext)_localctx).left = _prevctx;
					pushNewRecursionContext(_localctx, _startState, RULE_operandPatternExpression);
					setState(285);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(286);
					_la = _input.LA(1);
					if ( !(_la==LOGICAL_OR || _la==LOGICAL_AND) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(287);
					((LogicalOperandPatternExpressionContext)_localctx).right = operandPatternExpression(2);
					}
					} 
				}
				setState(292);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BasicOperandPatternExprContext extends ParserRuleContext {
		public BasicOperandPatternExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_basicOperandPatternExpr; }
	 
		public BasicOperandPatternExprContext() { }
		public void copyFrom(BasicOperandPatternExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AnyPolicyElementContext extends BasicOperandPatternExprContext {
		public TerminalNode ANY() { return getToken(PMLParser.ANY, 0); }
		public AnyPolicyElementContext(BasicOperandPatternExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterAnyPolicyElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitAnyPolicyElement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitAnyPolicyElement(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class InPolicyElementContext extends BasicOperandPatternExprContext {
		public TerminalNode IN() { return getToken(PMLParser.IN, 0); }
		public StringLitContext stringLit() {
			return getRuleContext(StringLitContext.class,0);
		}
		public InPolicyElementContext(BasicOperandPatternExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterInPolicyElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitInPolicyElement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitInPolicyElement(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PolicyElementContext extends BasicOperandPatternExprContext {
		public StringLitContext stringLit() {
			return getRuleContext(StringLitContext.class,0);
		}
		public PolicyElementContext(BasicOperandPatternExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterPolicyElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitPolicyElement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitPolicyElement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BasicOperandPatternExprContext basicOperandPatternExpr() throws RecognitionException {
		BasicOperandPatternExprContext _localctx = new BasicOperandPatternExprContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_basicOperandPatternExpr);
		try {
			setState(297);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ANY:
				_localctx = new AnyPolicyElementContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(293);
				match(ANY);
				}
				break;
			case IN:
				_localctx = new InPolicyElementContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(294);
				match(IN);
				setState(295);
				stringLit();
				}
				break;
			case DOUBLE_QUOTE_STRING:
				_localctx = new PolicyElementContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(296);
				stringLit();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ResponseContext extends ParserRuleContext {
		public TerminalNode DO() { return getToken(PMLParser.DO, 0); }
		public TerminalNode OPEN_PAREN() { return getToken(PMLParser.OPEN_PAREN, 0); }
		public TerminalNode ID() { return getToken(PMLParser.ID, 0); }
		public TerminalNode CLOSE_PAREN() { return getToken(PMLParser.CLOSE_PAREN, 0); }
		public ResponseBlockContext responseBlock() {
			return getRuleContext(ResponseBlockContext.class,0);
		}
		public ResponseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_response; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterResponse(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitResponse(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitResponse(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ResponseContext response() throws RecognitionException {
		ResponseContext _localctx = new ResponseContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_response);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(299);
			match(DO);
			setState(300);
			match(OPEN_PAREN);
			setState(301);
			match(ID);
			setState(302);
			match(CLOSE_PAREN);
			setState(303);
			responseBlock();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ResponseBlockContext extends ParserRuleContext {
		public TerminalNode OPEN_CURLY() { return getToken(PMLParser.OPEN_CURLY, 0); }
		public TerminalNode CLOSE_CURLY() { return getToken(PMLParser.CLOSE_CURLY, 0); }
		public List<ResponseStatementContext> responseStatement() {
			return getRuleContexts(ResponseStatementContext.class);
		}
		public ResponseStatementContext responseStatement(int i) {
			return getRuleContext(ResponseStatementContext.class,i);
		}
		public ResponseBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_responseBlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterResponseBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitResponseBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitResponseBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ResponseBlockContext responseBlock() throws RecognitionException {
		ResponseBlockContext _localctx = new ResponseBlockContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_responseBlock);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(305);
			match(OPEN_CURLY);
			setState(309);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 1)) & ~0x3f) == 0 && ((1L << (_la - 1)) & -9154656955725250503L) != 0)) {
				{
				{
				setState(306);
				responseStatement();
				}
				}
				setState(311);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(312);
			match(CLOSE_CURLY);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ResponseStatementContext extends ParserRuleContext {
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public CreateRuleStatementContext createRuleStatement() {
			return getRuleContext(CreateRuleStatementContext.class,0);
		}
		public DeleteRuleStatementContext deleteRuleStatement() {
			return getRuleContext(DeleteRuleStatementContext.class,0);
		}
		public ResponseStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_responseStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterResponseStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitResponseStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitResponseStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ResponseStatementContext responseStatement() throws RecognitionException {
		ResponseStatementContext _localctx = new ResponseStatementContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_responseStatement);
		try {
			setState(317);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(314);
				statement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(315);
				createRuleStatement();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(316);
				deleteRuleStatement();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CreateProhibitionStatementContext extends ParserRuleContext {
		public ExpressionContext name;
		public ExpressionContext subject;
		public ExpressionContext accessRights;
		public ExpressionContext containers;
		public TerminalNode CREATE() { return getToken(PMLParser.CREATE, 0); }
		public TerminalNode PROHIBITION() { return getToken(PMLParser.PROHIBITION, 0); }
		public TerminalNode DENY() { return getToken(PMLParser.DENY, 0); }
		public TerminalNode ACCESS_RIGHTS() { return getToken(PMLParser.ACCESS_RIGHTS, 0); }
		public TerminalNode ON() { return getToken(PMLParser.ON, 0); }
		public TerminalNode OF() { return getToken(PMLParser.OF, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode USER() { return getToken(PMLParser.USER, 0); }
		public TerminalNode USER_ATTRIBUTE() { return getToken(PMLParser.USER_ATTRIBUTE, 0); }
		public TerminalNode PROCESS() { return getToken(PMLParser.PROCESS, 0); }
		public TerminalNode INTERSECTION() { return getToken(PMLParser.INTERSECTION, 0); }
		public TerminalNode UNION() { return getToken(PMLParser.UNION, 0); }
		public CreateProhibitionStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_createProhibitionStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterCreateProhibitionStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitCreateProhibitionStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitCreateProhibitionStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreateProhibitionStatementContext createProhibitionStatement() throws RecognitionException {
		CreateProhibitionStatementContext _localctx = new CreateProhibitionStatementContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_createProhibitionStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(319);
			match(CREATE);
			setState(320);
			match(PROHIBITION);
			setState(321);
			((CreateProhibitionStatementContext)_localctx).name = expression(0);
			setState(322);
			match(DENY);
			setState(323);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 9345849884672L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(324);
			((CreateProhibitionStatementContext)_localctx).subject = expression(0);
			setState(325);
			match(ACCESS_RIGHTS);
			setState(326);
			((CreateProhibitionStatementContext)_localctx).accessRights = expression(0);
			setState(327);
			match(ON);
			setState(328);
			_la = _input.LA(1);
			if ( !(_la==INTERSECTION || _la==UNION) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(329);
			match(OF);
			setState(330);
			((CreateProhibitionStatementContext)_localctx).containers = expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SetNodePropertiesStatementContext extends ParserRuleContext {
		public ExpressionContext name;
		public ExpressionContext properties;
		public TerminalNode SET_PROPERTIES() { return getToken(PMLParser.SET_PROPERTIES, 0); }
		public TerminalNode OF() { return getToken(PMLParser.OF, 0); }
		public TerminalNode TO() { return getToken(PMLParser.TO, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public SetNodePropertiesStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_setNodePropertiesStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterSetNodePropertiesStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitSetNodePropertiesStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitSetNodePropertiesStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SetNodePropertiesStatementContext setNodePropertiesStatement() throws RecognitionException {
		SetNodePropertiesStatementContext _localctx = new SetNodePropertiesStatementContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_setNodePropertiesStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(332);
			match(SET_PROPERTIES);
			setState(333);
			match(OF);
			setState(334);
			((SetNodePropertiesStatementContext)_localctx).name = expression(0);
			setState(335);
			match(TO);
			setState(336);
			((SetNodePropertiesStatementContext)_localctx).properties = expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AssignStatementContext extends ParserRuleContext {
		public ExpressionContext ascendantNode;
		public ExpressionContext descendantNodes;
		public TerminalNode ASSIGN() { return getToken(PMLParser.ASSIGN, 0); }
		public TerminalNode TO() { return getToken(PMLParser.TO, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public AssignStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterAssignStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitAssignStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitAssignStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssignStatementContext assignStatement() throws RecognitionException {
		AssignStatementContext _localctx = new AssignStatementContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_assignStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(338);
			match(ASSIGN);
			setState(339);
			((AssignStatementContext)_localctx).ascendantNode = expression(0);
			setState(340);
			match(TO);
			setState(341);
			((AssignStatementContext)_localctx).descendantNodes = expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DeassignStatementContext extends ParserRuleContext {
		public ExpressionContext ascendantNode;
		public ExpressionContext descendantNodes;
		public TerminalNode DEASSIGN() { return getToken(PMLParser.DEASSIGN, 0); }
		public TerminalNode FROM() { return getToken(PMLParser.FROM, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public DeassignStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_deassignStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterDeassignStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitDeassignStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitDeassignStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeassignStatementContext deassignStatement() throws RecognitionException {
		DeassignStatementContext _localctx = new DeassignStatementContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_deassignStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(343);
			match(DEASSIGN);
			setState(344);
			((DeassignStatementContext)_localctx).ascendantNode = expression(0);
			setState(345);
			match(FROM);
			setState(346);
			((DeassignStatementContext)_localctx).descendantNodes = expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AssociateStatementContext extends ParserRuleContext {
		public ExpressionContext ua;
		public ExpressionContext target;
		public ExpressionContext accessRights;
		public TerminalNode ASSOCIATE() { return getToken(PMLParser.ASSOCIATE, 0); }
		public TerminalNode AND() { return getToken(PMLParser.AND, 0); }
		public TerminalNode WITH() { return getToken(PMLParser.WITH, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public AssociateStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_associateStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterAssociateStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitAssociateStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitAssociateStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssociateStatementContext associateStatement() throws RecognitionException {
		AssociateStatementContext _localctx = new AssociateStatementContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_associateStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(348);
			match(ASSOCIATE);
			setState(349);
			((AssociateStatementContext)_localctx).ua = expression(0);
			setState(350);
			match(AND);
			setState(351);
			((AssociateStatementContext)_localctx).target = expression(0);
			setState(352);
			match(WITH);
			setState(353);
			((AssociateStatementContext)_localctx).accessRights = expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DissociateStatementContext extends ParserRuleContext {
		public ExpressionContext ua;
		public ExpressionContext target;
		public TerminalNode DISSOCIATE() { return getToken(PMLParser.DISSOCIATE, 0); }
		public TerminalNode AND() { return getToken(PMLParser.AND, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public DissociateStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dissociateStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterDissociateStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitDissociateStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitDissociateStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DissociateStatementContext dissociateStatement() throws RecognitionException {
		DissociateStatementContext _localctx = new DissociateStatementContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_dissociateStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(355);
			match(DISSOCIATE);
			setState(356);
			((DissociateStatementContext)_localctx).ua = expression(0);
			setState(357);
			match(AND);
			setState(358);
			((DissociateStatementContext)_localctx).target = expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SetResourceOperationsStatementContext extends ParserRuleContext {
		public ExpressionContext accessRightsArr;
		public TerminalNode SET_RESOURCE_OPERATIONS() { return getToken(PMLParser.SET_RESOURCE_OPERATIONS, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public SetResourceOperationsStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_setResourceOperationsStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterSetResourceOperationsStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitSetResourceOperationsStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitSetResourceOperationsStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SetResourceOperationsStatementContext setResourceOperationsStatement() throws RecognitionException {
		SetResourceOperationsStatementContext _localctx = new SetResourceOperationsStatementContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_setResourceOperationsStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(360);
			match(SET_RESOURCE_OPERATIONS);
			setState(361);
			((SetResourceOperationsStatementContext)_localctx).accessRightsArr = expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DeleteStatementContext extends ParserRuleContext {
		public TerminalNode DELETE() { return getToken(PMLParser.DELETE, 0); }
		public DeleteTypeContext deleteType() {
			return getRuleContext(DeleteTypeContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public DeleteStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_deleteStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterDeleteStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitDeleteStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitDeleteStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeleteStatementContext deleteStatement() throws RecognitionException {
		DeleteStatementContext _localctx = new DeleteStatementContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_deleteStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(363);
			match(DELETE);
			setState(364);
			deleteType();
			setState(365);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DeleteTypeContext extends ParserRuleContext {
		public DeleteTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_deleteType; }
	 
		public DeleteTypeContext() { }
		public void copyFrom(DeleteTypeContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class DeleteNodeContext extends DeleteTypeContext {
		public NodeTypeContext nodeType() {
			return getRuleContext(NodeTypeContext.class,0);
		}
		public DeleteNodeContext(DeleteTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterDeleteNode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitDeleteNode(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitDeleteNode(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class DeleteProhibitionContext extends DeleteTypeContext {
		public TerminalNode PROHIBITION() { return getToken(PMLParser.PROHIBITION, 0); }
		public DeleteProhibitionContext(DeleteTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterDeleteProhibition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitDeleteProhibition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitDeleteProhibition(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class DeleteObligationContext extends DeleteTypeContext {
		public TerminalNode OBLIGATION() { return getToken(PMLParser.OBLIGATION, 0); }
		public DeleteObligationContext(DeleteTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterDeleteObligation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitDeleteObligation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitDeleteObligation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeleteTypeContext deleteType() throws RecognitionException {
		DeleteTypeContext _localctx = new DeleteTypeContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_deleteType);
		try {
			setState(370);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case POLICY_CLASS:
			case OBJECT_ATTRIBUTE:
			case USER_ATTRIBUTE:
			case OBJECT:
			case USER:
				_localctx = new DeleteNodeContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(367);
				nodeType();
				}
				break;
			case OBLIGATION:
				_localctx = new DeleteObligationContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(368);
				match(OBLIGATION);
				}
				break;
			case PROHIBITION:
				_localctx = new DeleteProhibitionContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(369);
				match(PROHIBITION);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NodeTypeContext extends ParserRuleContext {
		public TerminalNode POLICY_CLASS() { return getToken(PMLParser.POLICY_CLASS, 0); }
		public TerminalNode OBJECT_ATTRIBUTE() { return getToken(PMLParser.OBJECT_ATTRIBUTE, 0); }
		public TerminalNode USER_ATTRIBUTE() { return getToken(PMLParser.USER_ATTRIBUTE, 0); }
		public TerminalNode OBJECT() { return getToken(PMLParser.OBJECT, 0); }
		public TerminalNode USER() { return getToken(PMLParser.USER, 0); }
		public NodeTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nodeType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterNodeType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitNodeType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitNodeType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NodeTypeContext nodeType() throws RecognitionException {
		NodeTypeContext _localctx = new NodeTypeContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_nodeType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(372);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 14156212207616L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DeleteRuleStatementContext extends ParserRuleContext {
		public ExpressionContext ruleName;
		public ExpressionContext obligationName;
		public TerminalNode DELETE() { return getToken(PMLParser.DELETE, 0); }
		public TerminalNode RULE() { return getToken(PMLParser.RULE, 0); }
		public TerminalNode FROM() { return getToken(PMLParser.FROM, 0); }
		public TerminalNode OBLIGATION() { return getToken(PMLParser.OBLIGATION, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public DeleteRuleStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_deleteRuleStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterDeleteRuleStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitDeleteRuleStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitDeleteRuleStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeleteRuleStatementContext deleteRuleStatement() throws RecognitionException {
		DeleteRuleStatementContext _localctx = new DeleteRuleStatementContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_deleteRuleStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(374);
			match(DELETE);
			setState(375);
			match(RULE);
			setState(376);
			((DeleteRuleStatementContext)_localctx).ruleName = expression(0);
			setState(377);
			match(FROM);
			setState(378);
			match(OBLIGATION);
			setState(379);
			((DeleteRuleStatementContext)_localctx).obligationName = expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VariableDeclarationStatementContext extends ParserRuleContext {
		public VariableDeclarationStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableDeclarationStatement; }
	 
		public VariableDeclarationStatementContext() { }
		public void copyFrom(VariableDeclarationStatementContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class VarDeclarationContext extends VariableDeclarationStatementContext {
		public TerminalNode VAR() { return getToken(PMLParser.VAR, 0); }
		public List<VarSpecContext> varSpec() {
			return getRuleContexts(VarSpecContext.class);
		}
		public VarSpecContext varSpec(int i) {
			return getRuleContext(VarSpecContext.class,i);
		}
		public TerminalNode OPEN_PAREN() { return getToken(PMLParser.OPEN_PAREN, 0); }
		public TerminalNode CLOSE_PAREN() { return getToken(PMLParser.CLOSE_PAREN, 0); }
		public VarDeclarationContext(VariableDeclarationStatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterVarDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitVarDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitVarDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ShortDeclarationContext extends VariableDeclarationStatementContext {
		public TerminalNode ID() { return getToken(PMLParser.ID, 0); }
		public TerminalNode DECLARE_ASSIGN() { return getToken(PMLParser.DECLARE_ASSIGN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ShortDeclarationContext(VariableDeclarationStatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterShortDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitShortDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitShortDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableDeclarationStatementContext variableDeclarationStatement() throws RecognitionException {
		VariableDeclarationStatementContext _localctx = new VariableDeclarationStatementContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_variableDeclarationStatement);
		int _la;
		try {
			setState(396);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case VAR:
				_localctx = new VarDeclarationContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(381);
				match(VAR);
				setState(391);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case ID:
					{
					setState(382);
					varSpec();
					}
					break;
				case OPEN_PAREN:
					{
					setState(383);
					match(OPEN_PAREN);
					setState(387);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==ID) {
						{
						{
						setState(384);
						varSpec();
						}
						}
						setState(389);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(390);
					match(CLOSE_PAREN);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				break;
			case ID:
				_localctx = new ShortDeclarationContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(393);
				match(ID);
				setState(394);
				match(DECLARE_ASSIGN);
				setState(395);
				expression(0);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VarSpecContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(PMLParser.ID, 0); }
		public TerminalNode ASSIGN_EQUALS() { return getToken(PMLParser.ASSIGN_EQUALS, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public VarSpecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varSpec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterVarSpec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitVarSpec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitVarSpec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarSpecContext varSpec() throws RecognitionException {
		VarSpecContext _localctx = new VarSpecContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_varSpec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(398);
			match(ID);
			setState(399);
			match(ASSIGN_EQUALS);
			setState(400);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VariableAssignmentStatementContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(PMLParser.ID, 0); }
		public TerminalNode ASSIGN_EQUALS() { return getToken(PMLParser.ASSIGN_EQUALS, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode PLUS() { return getToken(PMLParser.PLUS, 0); }
		public VariableAssignmentStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableAssignmentStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterVariableAssignmentStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitVariableAssignmentStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitVariableAssignmentStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableAssignmentStatementContext variableAssignmentStatement() throws RecognitionException {
		VariableAssignmentStatementContext _localctx = new VariableAssignmentStatementContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_variableAssignmentStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(402);
			match(ID);
			setState(404);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PLUS) {
				{
				setState(403);
				match(PLUS);
				}
			}

			setState(406);
			match(ASSIGN_EQUALS);
			setState(407);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionDefinitionStatementContext extends ParserRuleContext {
		public FunctionSignatureContext functionSignature() {
			return getRuleContext(FunctionSignatureContext.class,0);
		}
		public StatementBlockContext statementBlock() {
			return getRuleContext(StatementBlockContext.class,0);
		}
		public CheckStatementBlockContext checkStatementBlock() {
			return getRuleContext(CheckStatementBlockContext.class,0);
		}
		public FunctionDefinitionStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionDefinitionStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterFunctionDefinitionStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitFunctionDefinitionStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitFunctionDefinitionStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionDefinitionStatementContext functionDefinitionStatement() throws RecognitionException {
		FunctionDefinitionStatementContext _localctx = new FunctionDefinitionStatementContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_functionDefinitionStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(409);
			functionSignature();
			setState(411);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,24,_ctx) ) {
			case 1:
				{
				setState(410);
				checkStatementBlock();
				}
				break;
			}
			setState(413);
			statementBlock();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionSignatureContext extends ParserRuleContext {
		public VariableTypeContext returnType;
		public TerminalNode ID() { return getToken(PMLParser.ID, 0); }
		public TerminalNode OPEN_PAREN() { return getToken(PMLParser.OPEN_PAREN, 0); }
		public FormalArgListContext formalArgList() {
			return getRuleContext(FormalArgListContext.class,0);
		}
		public TerminalNode CLOSE_PAREN() { return getToken(PMLParser.CLOSE_PAREN, 0); }
		public TerminalNode ROUTINE() { return getToken(PMLParser.ROUTINE, 0); }
		public TerminalNode OPERATION() { return getToken(PMLParser.OPERATION, 0); }
		public VariableTypeContext variableType() {
			return getRuleContext(VariableTypeContext.class,0);
		}
		public FunctionSignatureContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionSignature; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterFunctionSignature(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitFunctionSignature(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitFunctionSignature(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionSignatureContext functionSignature() throws RecognitionException {
		FunctionSignatureContext _localctx = new FunctionSignatureContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_functionSignature);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(415);
			_la = _input.LA(1);
			if ( !(_la==OPERATION || _la==ROUTINE) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(416);
			match(ID);
			setState(417);
			match(OPEN_PAREN);
			setState(418);
			formalArgList();
			setState(419);
			match(CLOSE_PAREN);
			setState(421);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 16)) & ~0x3f) == 0 && ((1L << (_la - 16)) & 9013800619474945L) != 0)) {
				{
				setState(420);
				((FunctionSignatureContext)_localctx).returnType = variableType();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FormalArgListContext extends ParserRuleContext {
		public List<FormalArgContext> formalArg() {
			return getRuleContexts(FormalArgContext.class);
		}
		public FormalArgContext formalArg(int i) {
			return getRuleContext(FormalArgContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(PMLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(PMLParser.COMMA, i);
		}
		public FormalArgListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_formalArgList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterFormalArgList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitFormalArgList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitFormalArgList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FormalArgListContext formalArgList() throws RecognitionException {
		FormalArgListContext _localctx = new FormalArgListContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_formalArgList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(431);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 432627039204343812L) != 0) || _la==OPEN_BRACKET) {
				{
				setState(423);
				formalArg();
				setState(428);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(424);
					match(COMMA);
					setState(425);
					formalArg();
					}
					}
					setState(430);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FormalArgContext extends ParserRuleContext {
		public VariableTypeContext variableType() {
			return getRuleContext(VariableTypeContext.class,0);
		}
		public TerminalNode ID() { return getToken(PMLParser.ID, 0); }
		public TerminalNode NODEOP() { return getToken(PMLParser.NODEOP, 0); }
		public FormalArgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_formalArg; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterFormalArg(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitFormalArg(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitFormalArg(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FormalArgContext formalArg() throws RecognitionException {
		FormalArgContext _localctx = new FormalArgContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_formalArg);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(434);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NODEOP) {
				{
				setState(433);
				match(NODEOP);
				}
			}

			setState(436);
			variableType();
			setState(437);
			match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ReturnStatementContext extends ParserRuleContext {
		public TerminalNode RETURN() { return getToken(PMLParser.RETURN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ReturnStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_returnStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterReturnStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitReturnStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitReturnStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReturnStatementContext returnStatement() throws RecognitionException {
		ReturnStatementContext _localctx = new ReturnStatementContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_returnStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(439);
			match(RETURN);
			setState(441);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
			case 1:
				{
				setState(440);
				expression(0);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CheckStatementContext extends ParserRuleContext {
		public ExpressionContext ar;
		public ExpressionContext target;
		public TerminalNode CHECK() { return getToken(PMLParser.CHECK, 0); }
		public TerminalNode ON() { return getToken(PMLParser.ON, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public CheckStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_checkStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterCheckStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitCheckStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitCheckStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CheckStatementContext checkStatement() throws RecognitionException {
		CheckStatementContext _localctx = new CheckStatementContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_checkStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(443);
			match(CHECK);
			setState(444);
			((CheckStatementContext)_localctx).ar = expression(0);
			setState(445);
			match(ON);
			setState(446);
			((CheckStatementContext)_localctx).target = expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CheckStatementBlockContext extends ParserRuleContext {
		public TerminalNode OPEN_CURLY() { return getToken(PMLParser.OPEN_CURLY, 0); }
		public TerminalNode CLOSE_CURLY() { return getToken(PMLParser.CLOSE_CURLY, 0); }
		public List<CheckStatementContext> checkStatement() {
			return getRuleContexts(CheckStatementContext.class);
		}
		public CheckStatementContext checkStatement(int i) {
			return getRuleContext(CheckStatementContext.class,i);
		}
		public CheckStatementBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_checkStatementBlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterCheckStatementBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitCheckStatementBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitCheckStatementBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CheckStatementBlockContext checkStatementBlock() throws RecognitionException {
		CheckStatementBlockContext _localctx = new CheckStatementBlockContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_checkStatementBlock);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(448);
			match(OPEN_CURLY);
			setState(452);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==CHECK) {
				{
				{
				setState(449);
				checkStatement();
				}
				}
				setState(454);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(455);
			match(CLOSE_CURLY);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IdArrContext extends ParserRuleContext {
		public TerminalNode OPEN_BRACKET() { return getToken(PMLParser.OPEN_BRACKET, 0); }
		public TerminalNode CLOSE_BRACKET() { return getToken(PMLParser.CLOSE_BRACKET, 0); }
		public List<TerminalNode> ID() { return getTokens(PMLParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(PMLParser.ID, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(PMLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(PMLParser.COMMA, i);
		}
		public IdArrContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_idArr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterIdArr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitIdArr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitIdArr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdArrContext idArr() throws RecognitionException {
		IdArrContext _localctx = new IdArrContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_idArr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(457);
			match(OPEN_BRACKET);
			setState(466);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ID) {
				{
				setState(458);
				match(ID);
				setState(463);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(459);
					match(COMMA);
					setState(460);
					match(ID);
					}
					}
					setState(465);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(468);
			match(CLOSE_BRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionInvokeStatementContext extends ParserRuleContext {
		public FunctionInvokeContext functionInvoke() {
			return getRuleContext(FunctionInvokeContext.class,0);
		}
		public FunctionInvokeStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionInvokeStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterFunctionInvokeStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitFunctionInvokeStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitFunctionInvokeStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionInvokeStatementContext functionInvokeStatement() throws RecognitionException {
		FunctionInvokeStatementContext _localctx = new FunctionInvokeStatementContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_functionInvokeStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(470);
			functionInvoke();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ForeachStatementContext extends ParserRuleContext {
		public Token key;
		public Token value;
		public TerminalNode FOREACH() { return getToken(PMLParser.FOREACH, 0); }
		public TerminalNode IN() { return getToken(PMLParser.IN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public StatementBlockContext statementBlock() {
			return getRuleContext(StatementBlockContext.class,0);
		}
		public List<TerminalNode> ID() { return getTokens(PMLParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(PMLParser.ID, i);
		}
		public TerminalNode COMMA() { return getToken(PMLParser.COMMA, 0); }
		public ForeachStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_foreachStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterForeachStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitForeachStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitForeachStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForeachStatementContext foreachStatement() throws RecognitionException {
		ForeachStatementContext _localctx = new ForeachStatementContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_foreachStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(472);
			match(FOREACH);
			setState(473);
			((ForeachStatementContext)_localctx).key = match(ID);
			setState(476);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(474);
				match(COMMA);
				setState(475);
				((ForeachStatementContext)_localctx).value = match(ID);
				}
			}

			setState(478);
			match(IN);
			setState(479);
			expression(0);
			setState(480);
			statementBlock();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BreakStatementContext extends ParserRuleContext {
		public TerminalNode BREAK() { return getToken(PMLParser.BREAK, 0); }
		public BreakStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_breakStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterBreakStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitBreakStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitBreakStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BreakStatementContext breakStatement() throws RecognitionException {
		BreakStatementContext _localctx = new BreakStatementContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_breakStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(482);
			match(BREAK);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ContinueStatementContext extends ParserRuleContext {
		public TerminalNode CONTINUE() { return getToken(PMLParser.CONTINUE, 0); }
		public ContinueStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_continueStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterContinueStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitContinueStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitContinueStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ContinueStatementContext continueStatement() throws RecognitionException {
		ContinueStatementContext _localctx = new ContinueStatementContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_continueStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(484);
			match(CONTINUE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IfStatementContext extends ParserRuleContext {
		public ExpressionContext condition;
		public TerminalNode IF() { return getToken(PMLParser.IF, 0); }
		public StatementBlockContext statementBlock() {
			return getRuleContext(StatementBlockContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<ElseIfStatementContext> elseIfStatement() {
			return getRuleContexts(ElseIfStatementContext.class);
		}
		public ElseIfStatementContext elseIfStatement(int i) {
			return getRuleContext(ElseIfStatementContext.class,i);
		}
		public ElseStatementContext elseStatement() {
			return getRuleContext(ElseStatementContext.class,0);
		}
		public IfStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ifStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterIfStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitIfStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitIfStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IfStatementContext ifStatement() throws RecognitionException {
		IfStatementContext _localctx = new IfStatementContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_ifStatement);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(486);
			match(IF);
			setState(487);
			((IfStatementContext)_localctx).condition = expression(0);
			setState(488);
			statementBlock();
			setState(492);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,34,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(489);
					elseIfStatement();
					}
					} 
				}
				setState(494);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,34,_ctx);
			}
			setState(496);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ELSE) {
				{
				setState(495);
				elseStatement();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ElseIfStatementContext extends ParserRuleContext {
		public ExpressionContext condition;
		public TerminalNode ELSE() { return getToken(PMLParser.ELSE, 0); }
		public TerminalNode IF() { return getToken(PMLParser.IF, 0); }
		public StatementBlockContext statementBlock() {
			return getRuleContext(StatementBlockContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ElseIfStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elseIfStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterElseIfStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitElseIfStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitElseIfStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElseIfStatementContext elseIfStatement() throws RecognitionException {
		ElseIfStatementContext _localctx = new ElseIfStatementContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_elseIfStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(498);
			match(ELSE);
			setState(499);
			match(IF);
			setState(500);
			((ElseIfStatementContext)_localctx).condition = expression(0);
			setState(501);
			statementBlock();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ElseStatementContext extends ParserRuleContext {
		public TerminalNode ELSE() { return getToken(PMLParser.ELSE, 0); }
		public StatementBlockContext statementBlock() {
			return getRuleContext(StatementBlockContext.class,0);
		}
		public ElseStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elseStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterElseStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitElseStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitElseStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElseStatementContext elseStatement() throws RecognitionException {
		ElseStatementContext _localctx = new ElseStatementContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_elseStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(503);
			match(ELSE);
			setState(504);
			statementBlock();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VariableTypeContext extends ParserRuleContext {
		public VariableTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableType; }
	 
		public VariableTypeContext() { }
		public void copyFrom(VariableTypeContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class MapVarTypeContext extends VariableTypeContext {
		public MapTypeContext mapType() {
			return getRuleContext(MapTypeContext.class,0);
		}
		public MapVarTypeContext(VariableTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterMapVarType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitMapVarType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitMapVarType(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StringTypeContext extends VariableTypeContext {
		public TerminalNode STRING_TYPE() { return getToken(PMLParser.STRING_TYPE, 0); }
		public StringTypeContext(VariableTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterStringType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitStringType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitStringType(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ArrayVarTypeContext extends VariableTypeContext {
		public ArrayTypeContext arrayType() {
			return getRuleContext(ArrayTypeContext.class,0);
		}
		public ArrayVarTypeContext(VariableTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterArrayVarType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitArrayVarType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitArrayVarType(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BooleanTypeContext extends VariableTypeContext {
		public TerminalNode BOOL_TYPE() { return getToken(PMLParser.BOOL_TYPE, 0); }
		public BooleanTypeContext(VariableTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterBooleanType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitBooleanType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitBooleanType(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AnyTypeContext extends VariableTypeContext {
		public TerminalNode ANY() { return getToken(PMLParser.ANY, 0); }
		public AnyTypeContext(VariableTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterAnyType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitAnyType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitAnyType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableTypeContext variableType() throws RecognitionException {
		VariableTypeContext _localctx = new VariableTypeContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_variableType);
		try {
			setState(511);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STRING_TYPE:
				_localctx = new StringTypeContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(506);
				match(STRING_TYPE);
				}
				break;
			case BOOL_TYPE:
				_localctx = new BooleanTypeContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(507);
				match(BOOL_TYPE);
				}
				break;
			case OPEN_BRACKET:
				_localctx = new ArrayVarTypeContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(508);
				arrayType();
				}
				break;
			case MAP:
				_localctx = new MapVarTypeContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(509);
				mapType();
				}
				break;
			case ANY:
				_localctx = new AnyTypeContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(510);
				match(ANY);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MapTypeContext extends ParserRuleContext {
		public VariableTypeContext keyType;
		public VariableTypeContext valueType;
		public TerminalNode MAP() { return getToken(PMLParser.MAP, 0); }
		public TerminalNode OPEN_BRACKET() { return getToken(PMLParser.OPEN_BRACKET, 0); }
		public TerminalNode CLOSE_BRACKET() { return getToken(PMLParser.CLOSE_BRACKET, 0); }
		public List<VariableTypeContext> variableType() {
			return getRuleContexts(VariableTypeContext.class);
		}
		public VariableTypeContext variableType(int i) {
			return getRuleContext(VariableTypeContext.class,i);
		}
		public MapTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mapType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterMapType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitMapType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitMapType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MapTypeContext mapType() throws RecognitionException {
		MapTypeContext _localctx = new MapTypeContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_mapType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(513);
			match(MAP);
			setState(514);
			match(OPEN_BRACKET);
			setState(515);
			((MapTypeContext)_localctx).keyType = variableType();
			setState(516);
			match(CLOSE_BRACKET);
			setState(517);
			((MapTypeContext)_localctx).valueType = variableType();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArrayTypeContext extends ParserRuleContext {
		public TerminalNode OPEN_BRACKET() { return getToken(PMLParser.OPEN_BRACKET, 0); }
		public TerminalNode CLOSE_BRACKET() { return getToken(PMLParser.CLOSE_BRACKET, 0); }
		public VariableTypeContext variableType() {
			return getRuleContext(VariableTypeContext.class,0);
		}
		public ArrayTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterArrayType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitArrayType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitArrayType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayTypeContext arrayType() throws RecognitionException {
		ArrayTypeContext _localctx = new ArrayTypeContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_arrayType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(519);
			match(OPEN_BRACKET);
			setState(520);
			match(CLOSE_BRACKET);
			setState(521);
			variableType();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionContext extends ParserRuleContext {
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
	 
		public ExpressionContext() { }
		public void copyFrom(ExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NegateExpressionContext extends ExpressionContext {
		public TerminalNode EXCLAMATION() { return getToken(PMLParser.EXCLAMATION, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public NegateExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterNegateExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitNegateExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitNegateExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class LogicalExpressionContext extends ExpressionContext {
		public ExpressionContext left;
		public ExpressionContext right;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode LOGICAL_AND() { return getToken(PMLParser.LOGICAL_AND, 0); }
		public TerminalNode LOGICAL_OR() { return getToken(PMLParser.LOGICAL_OR, 0); }
		public LogicalExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterLogicalExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitLogicalExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitLogicalExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PlusExpressionContext extends ExpressionContext {
		public ExpressionContext left;
		public ExpressionContext right;
		public TerminalNode PLUS() { return getToken(PMLParser.PLUS, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public PlusExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterPlusExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitPlusExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitPlusExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FunctionInvokeExpressionContext extends ExpressionContext {
		public FunctionInvokeContext functionInvoke() {
			return getRuleContext(FunctionInvokeContext.class,0);
		}
		public FunctionInvokeExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterFunctionInvokeExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitFunctionInvokeExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitFunctionInvokeExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class VariableReferenceExpressionContext extends ExpressionContext {
		public VariableReferenceContext variableReference() {
			return getRuleContext(VariableReferenceContext.class,0);
		}
		public VariableReferenceExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterVariableReferenceExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitVariableReferenceExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitVariableReferenceExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class LiteralExpressionContext extends ExpressionContext {
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public LiteralExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterLiteralExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitLiteralExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitLiteralExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ParenExpressionContext extends ExpressionContext {
		public TerminalNode OPEN_PAREN() { return getToken(PMLParser.OPEN_PAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode CLOSE_PAREN() { return getToken(PMLParser.CLOSE_PAREN, 0); }
		public ParenExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterParenExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitParenExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitParenExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class EqualsExpressionContext extends ExpressionContext {
		public ExpressionContext left;
		public ExpressionContext right;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode EQUALS() { return getToken(PMLParser.EQUALS, 0); }
		public TerminalNode NOT_EQUALS() { return getToken(PMLParser.NOT_EQUALS, 0); }
		public EqualsExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterEqualsExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitEqualsExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitEqualsExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 104;
		enterRecursionRule(_localctx, 104, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(533);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,37,_ctx) ) {
			case 1:
				{
				_localctx = new FunctionInvokeExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(524);
				functionInvoke();
				}
				break;
			case 2:
				{
				_localctx = new VariableReferenceExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(525);
				variableReference(0);
				}
				break;
			case 3:
				{
				_localctx = new LiteralExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(526);
				literal();
				}
				break;
			case 4:
				{
				_localctx = new NegateExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(527);
				match(EXCLAMATION);
				setState(528);
				expression(5);
				}
				break;
			case 5:
				{
				_localctx = new ParenExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(529);
				match(OPEN_PAREN);
				setState(530);
				expression(0);
				setState(531);
				match(CLOSE_PAREN);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(546);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,39,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(544);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,38,_ctx) ) {
					case 1:
						{
						_localctx = new PlusExpressionContext(new ExpressionContext(_parentctx, _parentState));
						((PlusExpressionContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(535);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(536);
						match(PLUS);
						setState(537);
						((PlusExpressionContext)_localctx).right = expression(4);
						}
						break;
					case 2:
						{
						_localctx = new EqualsExpressionContext(new ExpressionContext(_parentctx, _parentState));
						((EqualsExpressionContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(538);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(539);
						_la = _input.LA(1);
						if ( !(_la==EQUALS || _la==NOT_EQUALS) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(540);
						((EqualsExpressionContext)_localctx).right = expression(3);
						}
						break;
					case 3:
						{
						_localctx = new LogicalExpressionContext(new ExpressionContext(_parentctx, _parentState));
						((LogicalExpressionContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(541);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(542);
						_la = _input.LA(1);
						if ( !(_la==LOGICAL_OR || _la==LOGICAL_AND) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(543);
						((LogicalExpressionContext)_localctx).right = expression(2);
						}
						break;
					}
					} 
				}
				setState(548);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,39,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionListContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(PMLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(PMLParser.COMMA, i);
		}
		public ExpressionListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expressionList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterExpressionList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitExpressionList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitExpressionList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionListContext expressionList() throws RecognitionException {
		ExpressionListContext _localctx = new ExpressionListContext(_ctx, getState());
		enterRule(_localctx, 106, RULE_expressionList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(549);
			expression(0);
			setState(554);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(550);
				match(COMMA);
				setState(551);
				expression(0);
				}
				}
				setState(556);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LiteralContext extends ParserRuleContext {
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
	 
		public LiteralContext() { }
		public void copyFrom(LiteralContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class MapLiteralContext extends LiteralContext {
		public MapLitContext mapLit() {
			return getRuleContext(MapLitContext.class,0);
		}
		public MapLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterMapLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitMapLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitMapLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StringLiteralContext extends LiteralContext {
		public StringLitContext stringLit() {
			return getRuleContext(StringLitContext.class,0);
		}
		public StringLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterStringLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitStringLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitStringLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BoolLiteralContext extends LiteralContext {
		public BoolLitContext boolLit() {
			return getRuleContext(BoolLitContext.class,0);
		}
		public BoolLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterBoolLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitBoolLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitBoolLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ArrayLiteralContext extends LiteralContext {
		public ArrayLitContext arrayLit() {
			return getRuleContext(ArrayLitContext.class,0);
		}
		public ArrayLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterArrayLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitArrayLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitArrayLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 108, RULE_literal);
		try {
			setState(561);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case DOUBLE_QUOTE_STRING:
				_localctx = new StringLiteralContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(557);
				stringLit();
				}
				break;
			case TRUE:
			case FALSE:
				_localctx = new BoolLiteralContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(558);
				boolLit();
				}
				break;
			case OPEN_BRACKET:
				_localctx = new ArrayLiteralContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(559);
				arrayLit();
				}
				break;
			case OPEN_CURLY:
				_localctx = new MapLiteralContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(560);
				mapLit();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StringLitContext extends ParserRuleContext {
		public TerminalNode DOUBLE_QUOTE_STRING() { return getToken(PMLParser.DOUBLE_QUOTE_STRING, 0); }
		public StringLitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringLit; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterStringLit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitStringLit(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitStringLit(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringLitContext stringLit() throws RecognitionException {
		StringLitContext _localctx = new StringLitContext(_ctx, getState());
		enterRule(_localctx, 110, RULE_stringLit);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(563);
			match(DOUBLE_QUOTE_STRING);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BoolLitContext extends ParserRuleContext {
		public TerminalNode TRUE() { return getToken(PMLParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(PMLParser.FALSE, 0); }
		public BoolLitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_boolLit; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterBoolLit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitBoolLit(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitBoolLit(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BoolLitContext boolLit() throws RecognitionException {
		BoolLitContext _localctx = new BoolLitContext(_ctx, getState());
		enterRule(_localctx, 112, RULE_boolLit);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(565);
			_la = _input.LA(1);
			if ( !(_la==TRUE || _la==FALSE) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArrayLitContext extends ParserRuleContext {
		public TerminalNode OPEN_BRACKET() { return getToken(PMLParser.OPEN_BRACKET, 0); }
		public TerminalNode CLOSE_BRACKET() { return getToken(PMLParser.CLOSE_BRACKET, 0); }
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public ArrayLitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayLit; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterArrayLit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitArrayLit(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitArrayLit(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayLitContext arrayLit() throws RecognitionException {
		ArrayLitContext _localctx = new ArrayLitContext(_ctx, getState());
		enterRule(_localctx, 114, RULE_arrayLit);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(567);
			match(OPEN_BRACKET);
			setState(569);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 62)) & ~0x3f) == 0 && ((1L << (_la - 62)) & 2621615L) != 0)) {
				{
				setState(568);
				expressionList();
				}
			}

			setState(571);
			match(CLOSE_BRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StringArrayLitContext extends ParserRuleContext {
		public TerminalNode OPEN_BRACKET() { return getToken(PMLParser.OPEN_BRACKET, 0); }
		public TerminalNode CLOSE_BRACKET() { return getToken(PMLParser.CLOSE_BRACKET, 0); }
		public List<StringLitContext> stringLit() {
			return getRuleContexts(StringLitContext.class);
		}
		public StringLitContext stringLit(int i) {
			return getRuleContext(StringLitContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(PMLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(PMLParser.COMMA, i);
		}
		public StringArrayLitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringArrayLit; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterStringArrayLit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitStringArrayLit(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitStringArrayLit(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringArrayLitContext stringArrayLit() throws RecognitionException {
		StringArrayLitContext _localctx = new StringArrayLitContext(_ctx, getState());
		enterRule(_localctx, 116, RULE_stringArrayLit);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(573);
			match(OPEN_BRACKET);
			setState(582);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DOUBLE_QUOTE_STRING) {
				{
				setState(574);
				stringLit();
				setState(579);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(575);
					match(COMMA);
					setState(576);
					stringLit();
					}
					}
					setState(581);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(584);
			match(CLOSE_BRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MapLitContext extends ParserRuleContext {
		public TerminalNode OPEN_CURLY() { return getToken(PMLParser.OPEN_CURLY, 0); }
		public TerminalNode CLOSE_CURLY() { return getToken(PMLParser.CLOSE_CURLY, 0); }
		public List<ElementContext> element() {
			return getRuleContexts(ElementContext.class);
		}
		public ElementContext element(int i) {
			return getRuleContext(ElementContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(PMLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(PMLParser.COMMA, i);
		}
		public MapLitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mapLit; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterMapLit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitMapLit(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitMapLit(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MapLitContext mapLit() throws RecognitionException {
		MapLitContext _localctx = new MapLitContext(_ctx, getState());
		enterRule(_localctx, 118, RULE_mapLit);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(586);
			match(OPEN_CURLY);
			setState(595);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 62)) & ~0x3f) == 0 && ((1L << (_la - 62)) & 2621615L) != 0)) {
				{
				setState(587);
				element();
				setState(592);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(588);
					match(COMMA);
					setState(589);
					element();
					}
					}
					setState(594);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(597);
			match(CLOSE_CURLY);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ElementContext extends ParserRuleContext {
		public ExpressionContext key;
		public ExpressionContext value;
		public TerminalNode COLON() { return getToken(PMLParser.COLON, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_element; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitElement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitElement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElementContext element() throws RecognitionException {
		ElementContext _localctx = new ElementContext(_ctx, getState());
		enterRule(_localctx, 120, RULE_element);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(599);
			((ElementContext)_localctx).key = expression(0);
			setState(600);
			match(COLON);
			setState(601);
			((ElementContext)_localctx).value = expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VariableReferenceContext extends ParserRuleContext {
		public VariableReferenceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableReference; }
	 
		public VariableReferenceContext() { }
		public void copyFrom(VariableReferenceContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ReferenceByIndexContext extends VariableReferenceContext {
		public VariableReferenceContext variableReference() {
			return getRuleContext(VariableReferenceContext.class,0);
		}
		public IndexContext index() {
			return getRuleContext(IndexContext.class,0);
		}
		public ReferenceByIndexContext(VariableReferenceContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterReferenceByIndex(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitReferenceByIndex(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitReferenceByIndex(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ReferenceByIDContext extends VariableReferenceContext {
		public TerminalNode ID() { return getToken(PMLParser.ID, 0); }
		public ReferenceByIDContext(VariableReferenceContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterReferenceByID(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitReferenceByID(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitReferenceByID(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableReferenceContext variableReference() throws RecognitionException {
		return variableReference(0);
	}

	private VariableReferenceContext variableReference(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		VariableReferenceContext _localctx = new VariableReferenceContext(_ctx, _parentState);
		VariableReferenceContext _prevctx = _localctx;
		int _startState = 122;
		enterRecursionRule(_localctx, 122, RULE_variableReference, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			_localctx = new ReferenceByIDContext(_localctx);
			_ctx = _localctx;
			_prevctx = _localctx;

			setState(604);
			match(ID);
			}
			_ctx.stop = _input.LT(-1);
			setState(610);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,47,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new ReferenceByIndexContext(new VariableReferenceContext(_parentctx, _parentState));
					pushNewRecursionContext(_localctx, _startState, RULE_variableReference);
					setState(606);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(607);
					index();
					}
					} 
				}
				setState(612);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,47,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IndexContext extends ParserRuleContext {
		public IndexContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_index; }
	 
		public IndexContext() { }
		public void copyFrom(IndexContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class DotIndexContext extends IndexContext {
		public IdContext key;
		public TerminalNode DOT() { return getToken(PMLParser.DOT, 0); }
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public DotIndexContext(IndexContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterDotIndex(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitDotIndex(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitDotIndex(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BracketIndexContext extends IndexContext {
		public ExpressionContext key;
		public TerminalNode OPEN_BRACKET() { return getToken(PMLParser.OPEN_BRACKET, 0); }
		public TerminalNode CLOSE_BRACKET() { return getToken(PMLParser.CLOSE_BRACKET, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public BracketIndexContext(IndexContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterBracketIndex(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitBracketIndex(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitBracketIndex(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IndexContext index() throws RecognitionException {
		IndexContext _localctx = new IndexContext(_ctx, getState());
		enterRule(_localctx, 124, RULE_index);
		try {
			setState(619);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case OPEN_BRACKET:
				_localctx = new BracketIndexContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(613);
				match(OPEN_BRACKET);
				setState(614);
				((BracketIndexContext)_localctx).key = expression(0);
				setState(615);
				match(CLOSE_BRACKET);
				}
				break;
			case DOT:
				_localctx = new DotIndexContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(617);
				match(DOT);
				setState(618);
				((DotIndexContext)_localctx).key = id();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IdContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(PMLParser.ID, 0); }
		public IdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_id; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitId(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitId(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdContext id() throws RecognitionException {
		IdContext _localctx = new IdContext(_ctx, getState());
		enterRule(_localctx, 126, RULE_id);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(621);
			match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionInvokeContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(PMLParser.ID, 0); }
		public FunctionInvokeArgsContext functionInvokeArgs() {
			return getRuleContext(FunctionInvokeArgsContext.class,0);
		}
		public FunctionInvokeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionInvoke; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterFunctionInvoke(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitFunctionInvoke(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitFunctionInvoke(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionInvokeContext functionInvoke() throws RecognitionException {
		FunctionInvokeContext _localctx = new FunctionInvokeContext(_ctx, getState());
		enterRule(_localctx, 128, RULE_functionInvoke);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(623);
			match(ID);
			setState(624);
			functionInvokeArgs();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionInvokeArgsContext extends ParserRuleContext {
		public TerminalNode OPEN_PAREN() { return getToken(PMLParser.OPEN_PAREN, 0); }
		public TerminalNode CLOSE_PAREN() { return getToken(PMLParser.CLOSE_PAREN, 0); }
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public FunctionInvokeArgsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionInvokeArgs; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).enterFunctionInvokeArgs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLParserListener ) ((PMLParserListener)listener).exitFunctionInvokeArgs(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLParserVisitor ) return ((PMLParserVisitor<? extends T>)visitor).visitFunctionInvokeArgs(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionInvokeArgsContext functionInvokeArgs() throws RecognitionException {
		FunctionInvokeArgsContext _localctx = new FunctionInvokeArgsContext(_ctx, getState());
		enterRule(_localctx, 130, RULE_functionInvokeArgs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(626);
			match(OPEN_PAREN);
			setState(628);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 62)) & ~0x3f) == 0 && ((1L << (_la - 62)) & 2621615L) != 0)) {
				{
				setState(627);
				expressionList();
				}
			}

			setState(630);
			match(CLOSE_PAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 9:
			return subjectPatternExpression_sempred((SubjectPatternExpressionContext)_localctx, predIndex);
		case 15:
			return operandPatternExpression_sempred((OperandPatternExpressionContext)_localctx, predIndex);
		case 52:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		case 61:
			return variableReference_sempred((VariableReferenceContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean subjectPatternExpression_sempred(SubjectPatternExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean operandPatternExpression_sempred(OperandPatternExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 1:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 2:
			return precpred(_ctx, 3);
		case 3:
			return precpred(_ctx, 2);
		case 4:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean variableReference_sempred(VariableReferenceContext _localctx, int predIndex) {
		switch (predIndex) {
		case 5:
			return precpred(_ctx, 1);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001V\u0279\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018"+
		"\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007\u001b"+
		"\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002\u001e\u0007\u001e"+
		"\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!\u0007!\u0002\"\u0007\"\u0002"+
		"#\u0007#\u0002$\u0007$\u0002%\u0007%\u0002&\u0007&\u0002\'\u0007\'\u0002"+
		"(\u0007(\u0002)\u0007)\u0002*\u0007*\u0002+\u0007+\u0002,\u0007,\u0002"+
		"-\u0007-\u0002.\u0007.\u0002/\u0007/\u00020\u00070\u00021\u00071\u0002"+
		"2\u00072\u00023\u00073\u00024\u00074\u00025\u00075\u00026\u00076\u0002"+
		"7\u00077\u00028\u00078\u00029\u00079\u0002:\u0007:\u0002;\u0007;\u0002"+
		"<\u0007<\u0002=\u0007=\u0002>\u0007>\u0002?\u0007?\u0002@\u0007@\u0002"+
		"A\u0007A\u0001\u0000\u0005\u0000\u0086\b\u0000\n\u0000\f\u0000\u0089\t"+
		"\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0003"+
		"\u0001\u00a2\b\u0001\u0001\u0002\u0001\u0002\u0005\u0002\u00a6\b\u0002"+
		"\n\u0002\f\u0002\u00a9\t\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0006\u0001"+
		"\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0005\u0006\u00be\b\u0006\n"+
		"\u0006\f\u0006\u00c1\t\u0006\u0001\u0006\u0001\u0006\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0003\u0007\u00ce\b\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\b\u0001\b\u0001\b\u0001\b\u0003\b\u00d6\b\b\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0003\t\u00e0\b\t\u0001\t\u0001\t\u0001"+
		"\t\u0005\t\u00e5\b\t\n\t\f\t\u00e8\t\t\u0001\n\u0001\n\u0001\n\u0001\n"+
		"\u0001\n\u0003\n\u00ef\b\n\u0001\u000b\u0001\u000b\u0001\u000b\u0003\u000b"+
		"\u00f4\b\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0005\f\u00fa\b\f\n\f\f"+
		"\f\u00fd\t\f\u0003\f\u00ff\b\f\u0001\f\u0001\f\u0001\r\u0001\r\u0001\r"+
		"\u0001\r\u0003\r\u0107\b\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e"+
		"\u0005\u000e\u010d\b\u000e\n\u000e\f\u000e\u0110\t\u000e\u0001\u000e\u0001"+
		"\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001"+
		"\u000f\u0001\u000f\u0001\u000f\u0003\u000f\u011c\b\u000f\u0001\u000f\u0001"+
		"\u000f\u0001\u000f\u0005\u000f\u0121\b\u000f\n\u000f\f\u000f\u0124\t\u000f"+
		"\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0003\u0010\u012a\b\u0010"+
		"\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011"+
		"\u0001\u0012\u0001\u0012\u0005\u0012\u0134\b\u0012\n\u0012\f\u0012\u0137"+
		"\t\u0012\u0001\u0012\u0001\u0012\u0001\u0013\u0001\u0013\u0001\u0013\u0003"+
		"\u0013\u013e\b\u0013\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0001\u0014\u0001\u0015\u0001\u0015\u0001\u0015\u0001"+
		"\u0015\u0001\u0015\u0001\u0015\u0001\u0016\u0001\u0016\u0001\u0016\u0001"+
		"\u0016\u0001\u0016\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001"+
		"\u0017\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001"+
		"\u0018\u0001\u0018\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001"+
		"\u0019\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001b\u0001\u001b\u0001"+
		"\u001b\u0001\u001b\u0001\u001c\u0001\u001c\u0001\u001c\u0003\u001c\u0173"+
		"\b\u001c\u0001\u001d\u0001\u001d\u0001\u001e\u0001\u001e\u0001\u001e\u0001"+
		"\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001f\u0001\u001f\u0001"+
		"\u001f\u0001\u001f\u0005\u001f\u0182\b\u001f\n\u001f\f\u001f\u0185\t\u001f"+
		"\u0001\u001f\u0003\u001f\u0188\b\u001f\u0001\u001f\u0001\u001f\u0001\u001f"+
		"\u0003\u001f\u018d\b\u001f\u0001 \u0001 \u0001 \u0001 \u0001!\u0001!\u0003"+
		"!\u0195\b!\u0001!\u0001!\u0001!\u0001\"\u0001\"\u0003\"\u019c\b\"\u0001"+
		"\"\u0001\"\u0001#\u0001#\u0001#\u0001#\u0001#\u0001#\u0003#\u01a6\b#\u0001"+
		"$\u0001$\u0001$\u0005$\u01ab\b$\n$\f$\u01ae\t$\u0003$\u01b0\b$\u0001%"+
		"\u0003%\u01b3\b%\u0001%\u0001%\u0001%\u0001&\u0001&\u0003&\u01ba\b&\u0001"+
		"\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001(\u0001(\u0005(\u01c3\b(\n(\f"+
		"(\u01c6\t(\u0001(\u0001(\u0001)\u0001)\u0001)\u0001)\u0005)\u01ce\b)\n"+
		")\f)\u01d1\t)\u0003)\u01d3\b)\u0001)\u0001)\u0001*\u0001*\u0001+\u0001"+
		"+\u0001+\u0001+\u0003+\u01dd\b+\u0001+\u0001+\u0001+\u0001+\u0001,\u0001"+
		",\u0001-\u0001-\u0001.\u0001.\u0001.\u0001.\u0005.\u01eb\b.\n.\f.\u01ee"+
		"\t.\u0001.\u0003.\u01f1\b.\u0001/\u0001/\u0001/\u0001/\u0001/\u00010\u0001"+
		"0\u00010\u00011\u00011\u00011\u00011\u00011\u00031\u0200\b1\u00012\u0001"+
		"2\u00012\u00012\u00012\u00012\u00013\u00013\u00013\u00013\u00014\u0001"+
		"4\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u00034\u0216"+
		"\b4\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u0005"+
		"4\u0221\b4\n4\f4\u0224\t4\u00015\u00015\u00015\u00055\u0229\b5\n5\f5\u022c"+
		"\t5\u00016\u00016\u00016\u00016\u00036\u0232\b6\u00017\u00017\u00018\u0001"+
		"8\u00019\u00019\u00039\u023a\b9\u00019\u00019\u0001:\u0001:\u0001:\u0001"+
		":\u0005:\u0242\b:\n:\f:\u0245\t:\u0003:\u0247\b:\u0001:\u0001:\u0001;"+
		"\u0001;\u0001;\u0001;\u0005;\u024f\b;\n;\f;\u0252\t;\u0003;\u0254\b;\u0001"+
		";\u0001;\u0001<\u0001<\u0001<\u0001<\u0001=\u0001=\u0001=\u0001=\u0001"+
		"=\u0005=\u0261\b=\n=\f=\u0264\t=\u0001>\u0001>\u0001>\u0001>\u0001>\u0001"+
		">\u0003>\u026c\b>\u0001?\u0001?\u0001@\u0001@\u0001@\u0001A\u0001A\u0003"+
		"A\u0275\bA\u0001A\u0001A\u0001A\u0000\u0004\u0012\u001ehzB\u0000\u0002"+
		"\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e"+
		" \"$&(*,.02468:<>@BDFHJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0000\b"+
		"\u0002\u0000&\'*+\u0001\u0000MN\u0003\u0000\u0014\u0014\'\'++\u0001\u0000"+
		"\u0012\u0013\u0002\u0000%\'*+\u0002\u0000\u0001\u0001\u0004\u0004\u0001"+
		"\u0000OP\u0001\u0000>?\u028a\u0000\u0087\u0001\u0000\u0000\u0000\u0002"+
		"\u00a1\u0001\u0000\u0000\u0000\u0004\u00a3\u0001\u0000\u0000\u0000\u0006"+
		"\u00ac\u0001\u0000\u0000\u0000\b\u00b0\u0001\u0000\u0000\u0000\n\u00b6"+
		"\u0001\u0000\u0000\u0000\f\u00b8\u0001\u0000\u0000\u0000\u000e\u00c4\u0001"+
		"\u0000\u0000\u0000\u0010\u00d5\u0001\u0000\u0000\u0000\u0012\u00df\u0001"+
		"\u0000\u0000\u0000\u0014\u00ee\u0001\u0000\u0000\u0000\u0016\u00f3\u0001"+
		"\u0000\u0000\u0000\u0018\u00f5\u0001\u0000\u0000\u0000\u001a\u0102\u0001"+
		"\u0000\u0000\u0000\u001c\u0108\u0001\u0000\u0000\u0000\u001e\u011b\u0001"+
		"\u0000\u0000\u0000 \u0129\u0001\u0000\u0000\u0000\"\u012b\u0001\u0000"+
		"\u0000\u0000$\u0131\u0001\u0000\u0000\u0000&\u013d\u0001\u0000\u0000\u0000"+
		"(\u013f\u0001\u0000\u0000\u0000*\u014c\u0001\u0000\u0000\u0000,\u0152"+
		"\u0001\u0000\u0000\u0000.\u0157\u0001\u0000\u0000\u00000\u015c\u0001\u0000"+
		"\u0000\u00002\u0163\u0001\u0000\u0000\u00004\u0168\u0001\u0000\u0000\u0000"+
		"6\u016b\u0001\u0000\u0000\u00008\u0172\u0001\u0000\u0000\u0000:\u0174"+
		"\u0001\u0000\u0000\u0000<\u0176\u0001\u0000\u0000\u0000>\u018c\u0001\u0000"+
		"\u0000\u0000@\u018e\u0001\u0000\u0000\u0000B\u0192\u0001\u0000\u0000\u0000"+
		"D\u0199\u0001\u0000\u0000\u0000F\u019f\u0001\u0000\u0000\u0000H\u01af"+
		"\u0001\u0000\u0000\u0000J\u01b2\u0001\u0000\u0000\u0000L\u01b7\u0001\u0000"+
		"\u0000\u0000N\u01bb\u0001\u0000\u0000\u0000P\u01c0\u0001\u0000\u0000\u0000"+
		"R\u01c9\u0001\u0000\u0000\u0000T\u01d6\u0001\u0000\u0000\u0000V\u01d8"+
		"\u0001\u0000\u0000\u0000X\u01e2\u0001\u0000\u0000\u0000Z\u01e4\u0001\u0000"+
		"\u0000\u0000\\\u01e6\u0001\u0000\u0000\u0000^\u01f2\u0001\u0000\u0000"+
		"\u0000`\u01f7\u0001\u0000\u0000\u0000b\u01ff\u0001\u0000\u0000\u0000d"+
		"\u0201\u0001\u0000\u0000\u0000f\u0207\u0001\u0000\u0000\u0000h\u0215\u0001"+
		"\u0000\u0000\u0000j\u0225\u0001\u0000\u0000\u0000l\u0231\u0001\u0000\u0000"+
		"\u0000n\u0233\u0001\u0000\u0000\u0000p\u0235\u0001\u0000\u0000\u0000r"+
		"\u0237\u0001\u0000\u0000\u0000t\u023d\u0001\u0000\u0000\u0000v\u024a\u0001"+
		"\u0000\u0000\u0000x\u0257\u0001\u0000\u0000\u0000z\u025b\u0001\u0000\u0000"+
		"\u0000|\u026b\u0001\u0000\u0000\u0000~\u026d\u0001\u0000\u0000\u0000\u0080"+
		"\u026f\u0001\u0000\u0000\u0000\u0082\u0272\u0001\u0000\u0000\u0000\u0084"+
		"\u0086\u0003\u0002\u0001\u0000\u0085\u0084\u0001\u0000\u0000\u0000\u0086"+
		"\u0089\u0001\u0000\u0000\u0000\u0087\u0085\u0001\u0000\u0000\u0000\u0087"+
		"\u0088\u0001\u0000\u0000\u0000\u0088\u008a\u0001\u0000\u0000\u0000\u0089"+
		"\u0087\u0001\u0000\u0000\u0000\u008a\u008b\u0005\u0000\u0000\u0001\u008b"+
		"\u0001\u0001\u0000\u0000\u0000\u008c\u00a2\u0003B!\u0000\u008d\u00a2\u0003"+
		">\u001f\u0000\u008e\u00a2\u0003V+\u0000\u008f\u00a2\u0003L&\u0000\u0090"+
		"\u00a2\u0003X,\u0000\u0091\u00a2\u0003Z-\u0000\u0092\u00a2\u0003T*\u0000"+
		"\u0093\u00a2\u0003\\.\u0000\u0094\u00a2\u0003\u0006\u0003\u0000\u0095"+
		"\u00a2\u0003\b\u0004\u0000\u0096\u00a2\u0003\f\u0006\u0000\u0097\u00a2"+
		"\u0003(\u0014\u0000\u0098\u00a2\u0003*\u0015\u0000\u0099\u00a2\u0003,"+
		"\u0016\u0000\u009a\u00a2\u0003.\u0017\u0000\u009b\u00a2\u00030\u0018\u0000"+
		"\u009c\u00a2\u00032\u0019\u0000\u009d\u00a2\u00034\u001a\u0000\u009e\u00a2"+
		"\u00036\u001b\u0000\u009f\u00a2\u0003<\u001e\u0000\u00a0\u00a2\u0003D"+
		"\"\u0000\u00a1\u008c\u0001\u0000\u0000\u0000\u00a1\u008d\u0001\u0000\u0000"+
		"\u0000\u00a1\u008e\u0001\u0000\u0000\u0000\u00a1\u008f\u0001\u0000\u0000"+
		"\u0000\u00a1\u0090\u0001\u0000\u0000\u0000\u00a1\u0091\u0001\u0000\u0000"+
		"\u0000\u00a1\u0092\u0001\u0000\u0000\u0000\u00a1\u0093\u0001\u0000\u0000"+
		"\u0000\u00a1\u0094\u0001\u0000\u0000\u0000\u00a1\u0095\u0001\u0000\u0000"+
		"\u0000\u00a1\u0096\u0001\u0000\u0000\u0000\u00a1\u0097\u0001\u0000\u0000"+
		"\u0000\u00a1\u0098\u0001\u0000\u0000\u0000\u00a1\u0099\u0001\u0000\u0000"+
		"\u0000\u00a1\u009a\u0001\u0000\u0000\u0000\u00a1\u009b\u0001\u0000\u0000"+
		"\u0000\u00a1\u009c\u0001\u0000\u0000\u0000\u00a1\u009d\u0001\u0000\u0000"+
		"\u0000\u00a1\u009e\u0001\u0000\u0000\u0000\u00a1\u009f\u0001\u0000\u0000"+
		"\u0000\u00a1\u00a0\u0001\u0000\u0000\u0000\u00a2\u0003\u0001\u0000\u0000"+
		"\u0000\u00a3\u00a7\u0005C\u0000\u0000\u00a4\u00a6\u0003\u0002\u0001\u0000"+
		"\u00a5\u00a4\u0001\u0000\u0000\u0000\u00a6\u00a9\u0001\u0000\u0000\u0000"+
		"\u00a7\u00a5\u0001\u0000\u0000\u0000\u00a7\u00a8\u0001\u0000\u0000\u0000"+
		"\u00a8\u00aa\u0001\u0000\u0000\u0000\u00a9\u00a7\u0001\u0000\u0000\u0000"+
		"\u00aa\u00ab\u0005D\u0000\u0000\u00ab\u0005\u0001\u0000\u0000\u0000\u00ac"+
		"\u00ad\u0005\u0005\u0000\u0000\u00ad\u00ae\u0005%\u0000\u0000\u00ae\u00af"+
		"\u0003h4\u0000\u00af\u0007\u0001\u0000\u0000\u0000\u00b0\u00b1\u0005\u0005"+
		"\u0000\u0000\u00b1\u00b2\u0003\n\u0005\u0000\u00b2\u00b3\u0003h4\u0000"+
		"\u00b3\u00b4\u0005\u000e\u0000\u0000\u00b4\u00b5\u0003h4\u0000\u00b5\t"+
		"\u0001\u0000\u0000\u0000\u00b6\u00b7\u0007\u0000\u0000\u0000\u00b7\u000b"+
		"\u0001\u0000\u0000\u0000\u00b8\u00b9\u0005\u0005\u0000\u0000\u00b9\u00ba"+
		"\u0005#\u0000\u0000\u00ba\u00bb\u0003h4\u0000\u00bb\u00bf\u0005C\u0000"+
		"\u0000\u00bc\u00be\u0003\u000e\u0007\u0000\u00bd\u00bc\u0001\u0000\u0000"+
		"\u0000\u00be\u00c1\u0001\u0000\u0000\u0000\u00bf\u00bd\u0001\u0000\u0000"+
		"\u0000\u00bf\u00c0\u0001\u0000\u0000\u0000\u00c0\u00c2\u0001\u0000\u0000"+
		"\u0000\u00c1\u00bf\u0001\u0000\u0000\u0000\u00c2\u00c3\u0005D\u0000\u0000"+
		"\u00c3\r\u0001\u0000\u0000\u0000\u00c4\u00c5\u0005\u0005\u0000\u0000\u00c5"+
		"\u00c6\u0005\t\u0000\u0000\u00c6\u00c7\u0003h4\u0000\u00c7\u00c8\u0005"+
		"\n\u0000\u0000\u00c8\u00c9\u0003\u0010\b\u0000\u00c9\u00ca\u0005\u000b"+
		"\u0000\u0000\u00ca\u00cd\u0003\u0016\u000b\u0000\u00cb\u00cc\u0005\r\u0000"+
		"\u0000\u00cc\u00ce\u0003\u0018\f\u0000\u00cd\u00cb\u0001\u0000\u0000\u0000"+
		"\u00cd\u00ce\u0001\u0000\u0000\u0000\u00ce\u00cf\u0001\u0000\u0000\u0000"+
		"\u00cf\u00d0\u0003\"\u0011\u0000\u00d0\u000f\u0001\u0000\u0000\u0000\u00d1"+
		"\u00d2\u0005\u0010\u0000\u0000\u00d2\u00d6\u0005+\u0000\u0000\u00d3\u00d4"+
		"\u0005+\u0000\u0000\u00d4\u00d6\u0003\u0012\t\u0000\u00d5\u00d1\u0001"+
		"\u0000\u0000\u0000\u00d5\u00d3\u0001\u0000\u0000\u0000\u00d6\u0011\u0001"+
		"\u0000\u0000\u0000\u00d7\u00d8\u0006\t\uffff\uffff\u0000\u00d8\u00e0\u0003"+
		"\u0014\n\u0000\u00d9\u00da\u0005Q\u0000\u0000\u00da\u00e0\u0003\u0012"+
		"\t\u0003\u00db\u00dc\u0005A\u0000\u0000\u00dc\u00dd\u0003\u0012\t\u0000"+
		"\u00dd\u00de\u0005B\u0000\u0000\u00de\u00e0\u0001\u0000\u0000\u0000\u00df"+
		"\u00d7\u0001\u0000\u0000\u0000\u00df\u00d9\u0001\u0000\u0000\u0000\u00df"+
		"\u00db\u0001\u0000\u0000\u0000\u00e0\u00e6\u0001\u0000\u0000\u0000\u00e1"+
		"\u00e2\n\u0001\u0000\u0000\u00e2\u00e3\u0007\u0001\u0000\u0000\u00e3\u00e5"+
		"\u0003\u0012\t\u0002\u00e4\u00e1\u0001\u0000\u0000\u0000\u00e5\u00e8\u0001"+
		"\u0000\u0000\u0000\u00e6\u00e4\u0001\u0000\u0000\u0000\u00e6\u00e7\u0001"+
		"\u0000\u0000\u0000\u00e7\u0013\u0001\u0000\u0000\u0000\u00e8\u00e6\u0001"+
		"\u0000\u0000\u0000\u00e9\u00ea\u0005\u000e\u0000\u0000\u00ea\u00ef\u0003"+
		"n7\u0000\u00eb\u00ef\u0003n7\u0000\u00ec\u00ed\u0005\u0014\u0000\u0000"+
		"\u00ed\u00ef\u0003n7\u0000\u00ee\u00e9\u0001\u0000\u0000\u0000\u00ee\u00eb"+
		"\u0001\u0000\u0000\u0000\u00ee\u00ec\u0001\u0000\u0000\u0000\u00ef\u0015"+
		"\u0001\u0000\u0000\u0000\u00f0\u00f1\u0005\u0010\u0000\u0000\u00f1\u00f4"+
		"\u0005\u0001\u0000\u0000\u00f2\u00f4\u0003n7\u0000\u00f3\u00f0\u0001\u0000"+
		"\u0000\u0000\u00f3\u00f2\u0001\u0000\u0000\u0000\u00f4\u0017\u0001\u0000"+
		"\u0000\u0000\u00f5\u00fe\u0005C\u0000\u0000\u00f6\u00fb\u0003\u001a\r"+
		"\u0000\u00f7\u00f8\u0005H\u0000\u0000\u00f8\u00fa\u0003\u001a\r\u0000"+
		"\u00f9\u00f7\u0001\u0000\u0000\u0000\u00fa\u00fd\u0001\u0000\u0000\u0000"+
		"\u00fb\u00f9\u0001\u0000\u0000\u0000\u00fb\u00fc\u0001\u0000\u0000\u0000"+
		"\u00fc\u00ff\u0001\u0000\u0000\u0000\u00fd\u00fb\u0001\u0000\u0000\u0000"+
		"\u00fe\u00f6\u0001\u0000\u0000\u0000\u00fe\u00ff\u0001\u0000\u0000\u0000"+
		"\u00ff\u0100\u0001\u0000\u0000\u0000\u0100\u0101\u0005D\u0000\u0000\u0101"+
		"\u0019\u0001\u0000\u0000\u0000\u0102\u0103\u0005@\u0000\u0000\u0103\u0106"+
		"\u0005J\u0000\u0000\u0104\u0107\u0003\u001e\u000f\u0000\u0105\u0107\u0003"+
		"\u001c\u000e\u0000\u0106\u0104\u0001\u0000\u0000\u0000\u0106\u0105\u0001"+
		"\u0000\u0000\u0000\u0107\u001b\u0001\u0000\u0000\u0000\u0108\u0109\u0005"+
		"E\u0000\u0000\u0109\u010e\u0003\u001e\u000f\u0000\u010a\u010b\u0005H\u0000"+
		"\u0000\u010b\u010d\u0003\u001e\u000f\u0000\u010c\u010a\u0001\u0000\u0000"+
		"\u0000\u010d\u0110\u0001\u0000\u0000\u0000\u010e\u010c\u0001\u0000\u0000"+
		"\u0000\u010e\u010f\u0001\u0000\u0000\u0000\u010f\u0111\u0001\u0000\u0000"+
		"\u0000\u0110\u010e\u0001\u0000\u0000\u0000\u0111\u0112\u0005F\u0000\u0000"+
		"\u0112\u001d\u0001\u0000\u0000\u0000\u0113\u0114\u0006\u000f\uffff\uffff"+
		"\u0000\u0114\u011c\u0003 \u0010\u0000\u0115\u0116\u0005Q\u0000\u0000\u0116"+
		"\u011c\u0003\u001e\u000f\u0003\u0117\u0118\u0005A\u0000\u0000\u0118\u0119"+
		"\u0003\u001e\u000f\u0000\u0119\u011a\u0005B\u0000\u0000\u011a\u011c\u0001"+
		"\u0000\u0000\u0000\u011b\u0113\u0001\u0000\u0000\u0000\u011b\u0115\u0001"+
		"\u0000\u0000\u0000\u011b\u0117\u0001\u0000\u0000\u0000\u011c\u0122\u0001"+
		"\u0000\u0000\u0000\u011d\u011e\n\u0001\u0000\u0000\u011e\u011f\u0007\u0001"+
		"\u0000\u0000\u011f\u0121\u0003\u001e\u000f\u0002\u0120\u011d\u0001\u0000"+
		"\u0000\u0000\u0121\u0124\u0001\u0000\u0000\u0000\u0122\u0120\u0001\u0000"+
		"\u0000\u0000\u0122\u0123\u0001\u0000\u0000\u0000\u0123\u001f\u0001\u0000"+
		"\u0000\u0000\u0124\u0122\u0001\u0000\u0000\u0000\u0125\u012a\u0005\u0010"+
		"\u0000\u0000\u0126\u0127\u0005\u000e\u0000\u0000\u0127\u012a\u0003n7\u0000"+
		"\u0128\u012a\u0003n7\u0000\u0129\u0125\u0001\u0000\u0000\u0000\u0129\u0126"+
		"\u0001\u0000\u0000\u0000\u0129\u0128\u0001\u0000\u0000\u0000\u012a!\u0001"+
		"\u0000\u0000\u0000\u012b\u012c\u0005\u000f\u0000\u0000\u012c\u012d\u0005"+
		"A\u0000\u0000\u012d\u012e\u0005@\u0000\u0000\u012e\u012f\u0005B\u0000"+
		"\u0000\u012f\u0130\u0003$\u0012\u0000\u0130#\u0001\u0000\u0000\u0000\u0131"+
		"\u0135\u0005C\u0000\u0000\u0132\u0134\u0003&\u0013\u0000\u0133\u0132\u0001"+
		"\u0000\u0000\u0000\u0134\u0137\u0001\u0000\u0000\u0000\u0135\u0133\u0001"+
		"\u0000\u0000\u0000\u0135\u0136\u0001\u0000\u0000\u0000\u0136\u0138\u0001"+
		"\u0000\u0000\u0000\u0137\u0135\u0001\u0000\u0000\u0000\u0138\u0139\u0005"+
		"D\u0000\u0000\u0139%\u0001\u0000\u0000\u0000\u013a\u013e\u0003\u0002\u0001"+
		"\u0000\u013b\u013e\u0003\u000e\u0007\u0000\u013c\u013e\u0003<\u001e\u0000"+
		"\u013d\u013a\u0001\u0000\u0000\u0000\u013d\u013b\u0001\u0000\u0000\u0000"+
		"\u013d\u013c\u0001\u0000\u0000\u0000\u013e\'\u0001\u0000\u0000\u0000\u013f"+
		"\u0140\u0005\u0005\u0000\u0000\u0140\u0141\u0005\"\u0000\u0000\u0141\u0142"+
		"\u0003h4\u0000\u0142\u0143\u0005!\u0000\u0000\u0143\u0144\u0007\u0002"+
		"\u0000\u0000\u0144\u0145\u0003h4\u0000\u0145\u0146\u0005$\u0000\u0000"+
		"\u0146\u0147\u0003h4\u0000\u0147\u0148\u0005\r\u0000\u0000\u0148\u0149"+
		"\u0007\u0003\u0000\u0000\u0149\u014a\u0005\u001b\u0000\u0000\u014a\u014b"+
		"\u0003h4\u0000\u014b)\u0001\u0000\u0000\u0000\u014c\u014d\u0005\u0019"+
		"\u0000\u0000\u014d\u014e\u0005\u001b\u0000\u0000\u014e\u014f\u0003h4\u0000"+
		"\u014f\u0150\u0005\u001c\u0000\u0000\u0150\u0151\u0003h4\u0000\u0151+"+
		"\u0001\u0000\u0000\u0000\u0152\u0153\u0005\u0016\u0000\u0000\u0153\u0154"+
		"\u0003h4\u0000\u0154\u0155\u0005\u001c\u0000\u0000\u0155\u0156\u0003h"+
		"4\u0000\u0156-\u0001\u0000\u0000\u0000\u0157\u0158\u0005\u0017\u0000\u0000"+
		"\u0158\u0159\u0003h4\u0000\u0159\u015a\u0005\u0018\u0000\u0000\u015a\u015b"+
		"\u0003h4\u0000\u015b/\u0001\u0000\u0000\u0000\u015c\u015d\u0005\u001d"+
		"\u0000\u0000\u015d\u015e\u0003h4\u0000\u015e\u015f\u0005\u001e\u0000\u0000"+
		"\u015f\u0160\u0003h4\u0000\u0160\u0161\u0005\u001f\u0000\u0000\u0161\u0162"+
		"\u0003h4\u0000\u01621\u0001\u0000\u0000\u0000\u0163\u0164\u0005 \u0000"+
		"\u0000\u0164\u0165\u0003h4\u0000\u0165\u0166\u0005\u001e\u0000\u0000\u0166"+
		"\u0167\u0003h4\u0000\u01673\u0001\u0000\u0000\u0000\u0168\u0169\u0005"+
		"\u0015\u0000\u0000\u0169\u016a\u0003h4\u0000\u016a5\u0001\u0000\u0000"+
		"\u0000\u016b\u016c\u0005\u0006\u0000\u0000\u016c\u016d\u00038\u001c\u0000"+
		"\u016d\u016e\u0003h4\u0000\u016e7\u0001\u0000\u0000\u0000\u016f\u0173"+
		"\u0003:\u001d\u0000\u0170\u0173\u0005#\u0000\u0000\u0171\u0173\u0005\""+
		"\u0000\u0000\u0172\u016f\u0001\u0000\u0000\u0000\u0172\u0170\u0001\u0000"+
		"\u0000\u0000\u0172\u0171\u0001\u0000\u0000\u0000\u01739\u0001\u0000\u0000"+
		"\u0000\u0174\u0175\u0007\u0004\u0000\u0000\u0175;\u0001\u0000\u0000\u0000"+
		"\u0176\u0177\u0005\u0006\u0000\u0000\u0177\u0178\u0005\t\u0000\u0000\u0178"+
		"\u0179\u0003h4\u0000\u0179\u017a\u0005\u0018\u0000\u0000\u017a\u017b\u0005"+
		"#\u0000\u0000\u017b\u017c\u0003h4\u0000\u017c=\u0001\u0000\u0000\u0000"+
		"\u017d\u0187\u00058\u0000\u0000\u017e\u0188\u0003@ \u0000\u017f\u0183"+
		"\u0005A\u0000\u0000\u0180\u0182\u0003@ \u0000\u0181\u0180\u0001\u0000"+
		"\u0000\u0000\u0182\u0185\u0001\u0000\u0000\u0000\u0183\u0181\u0001\u0000"+
		"\u0000\u0000\u0183\u0184\u0001\u0000\u0000\u0000\u0184\u0186\u0001\u0000"+
		"\u0000\u0000\u0185\u0183\u0001\u0000\u0000\u0000\u0186\u0188\u0005B\u0000"+
		"\u0000\u0187\u017e\u0001\u0000\u0000\u0000\u0187\u017f\u0001\u0000\u0000"+
		"\u0000\u0188\u018d\u0001\u0000\u0000\u0000\u0189\u018a\u0005@\u0000\u0000"+
		"\u018a\u018b\u0005L\u0000\u0000\u018b\u018d\u0003h4\u0000\u018c\u017d"+
		"\u0001\u0000\u0000\u0000\u018c\u0189\u0001\u0000\u0000\u0000\u018d?\u0001"+
		"\u0000\u0000\u0000\u018e\u018f\u0005@\u0000\u0000\u018f\u0190\u0005G\u0000"+
		"\u0000\u0190\u0191\u0003h4\u0000\u0191A\u0001\u0000\u0000\u0000\u0192"+
		"\u0194\u0005@\u0000\u0000\u0193\u0195\u0005R\u0000\u0000\u0194\u0193\u0001"+
		"\u0000\u0000\u0000\u0194\u0195\u0001\u0000\u0000\u0000\u0195\u0196\u0001"+
		"\u0000\u0000\u0000\u0196\u0197\u0005G\u0000\u0000\u0197\u0198\u0003h4"+
		"\u0000\u0198C\u0001\u0000\u0000\u0000\u0199\u019b\u0003F#\u0000\u019a"+
		"\u019c\u0003P(\u0000\u019b\u019a\u0001\u0000\u0000\u0000\u019b\u019c\u0001"+
		"\u0000\u0000\u0000\u019c\u019d\u0001\u0000\u0000\u0000\u019d\u019e\u0003"+
		"\u0004\u0002\u0000\u019eE\u0001\u0000\u0000\u0000\u019f\u01a0\u0007\u0005"+
		"\u0000\u0000\u01a0\u01a1\u0005@\u0000\u0000\u01a1\u01a2\u0005A\u0000\u0000"+
		"\u01a2\u01a3\u0003H$\u0000\u01a3\u01a5\u0005B\u0000\u0000\u01a4\u01a6"+
		"\u0003b1\u0000\u01a5\u01a4\u0001\u0000\u0000\u0000\u01a5\u01a6\u0001\u0000"+
		"\u0000\u0000\u01a6G\u0001\u0000\u0000\u0000\u01a7\u01ac\u0003J%\u0000"+
		"\u01a8\u01a9\u0005H\u0000\u0000\u01a9\u01ab\u0003J%\u0000\u01aa\u01a8"+
		"\u0001\u0000\u0000\u0000\u01ab\u01ae\u0001\u0000\u0000\u0000\u01ac\u01aa"+
		"\u0001\u0000\u0000\u0000\u01ac\u01ad\u0001\u0000\u0000\u0000\u01ad\u01b0"+
		"\u0001\u0000\u0000\u0000\u01ae\u01ac\u0001\u0000\u0000\u0000\u01af\u01a7"+
		"\u0001\u0000\u0000\u0000\u01af\u01b0\u0001\u0000\u0000\u0000\u01b0I\u0001"+
		"\u0000\u0000\u0000\u01b1\u01b3\u0005\u0002\u0000\u0000\u01b2\u01b1\u0001"+
		"\u0000\u0000\u0000\u01b2\u01b3\u0001\u0000\u0000\u0000\u01b3\u01b4\u0001"+
		"\u0000\u0000\u0000\u01b4\u01b5\u0003b1\u0000\u01b5\u01b6\u0005@\u0000"+
		"\u0000\u01b6K\u0001\u0000\u0000\u0000\u01b7\u01b9\u00057\u0000\u0000\u01b8"+
		"\u01ba\u0003h4\u0000\u01b9\u01b8\u0001\u0000\u0000\u0000\u01b9\u01ba\u0001"+
		"\u0000\u0000\u0000\u01baM\u0001\u0000\u0000\u0000\u01bb\u01bc\u0005\u0003"+
		"\u0000\u0000\u01bc\u01bd\u0003h4\u0000\u01bd\u01be\u0005\r\u0000\u0000"+
		"\u01be\u01bf\u0003h4\u0000\u01bfO\u0001\u0000\u0000\u0000\u01c0\u01c4"+
		"\u0005C\u0000\u0000\u01c1\u01c3\u0003N\'\u0000\u01c2\u01c1\u0001\u0000"+
		"\u0000\u0000\u01c3\u01c6\u0001\u0000\u0000\u0000\u01c4\u01c2\u0001\u0000"+
		"\u0000\u0000\u01c4\u01c5\u0001\u0000\u0000\u0000\u01c5\u01c7\u0001\u0000"+
		"\u0000\u0000\u01c6\u01c4\u0001\u0000\u0000\u0000\u01c7\u01c8\u0005D\u0000"+
		"\u0000\u01c8Q\u0001\u0000\u0000\u0000\u01c9\u01d2\u0005E\u0000\u0000\u01ca"+
		"\u01cf\u0005@\u0000\u0000\u01cb\u01cc\u0005H\u0000\u0000\u01cc\u01ce\u0005"+
		"@\u0000\u0000\u01cd\u01cb\u0001\u0000\u0000\u0000\u01ce\u01d1\u0001\u0000"+
		"\u0000\u0000\u01cf\u01cd\u0001\u0000\u0000\u0000\u01cf\u01d0\u0001\u0000"+
		"\u0000\u0000\u01d0\u01d3\u0001\u0000\u0000\u0000\u01d1\u01cf\u0001\u0000"+
		"\u0000\u0000\u01d2\u01ca\u0001\u0000\u0000\u0000\u01d2\u01d3\u0001\u0000"+
		"\u0000\u0000\u01d3\u01d4\u0001\u0000\u0000\u0000\u01d4\u01d5\u0005F\u0000"+
		"\u0000\u01d5S\u0001\u0000\u0000\u0000\u01d6\u01d7\u0003\u0080@\u0000\u01d7"+
		"U\u0001\u0000\u0000\u0000\u01d8\u01d9\u00056\u0000\u0000\u01d9\u01dc\u0005"+
		"@\u0000\u0000\u01da\u01db\u0005H\u0000\u0000\u01db\u01dd\u0005@\u0000"+
		"\u0000\u01dc\u01da\u0001\u0000\u0000\u0000\u01dc\u01dd\u0001\u0000\u0000"+
		"\u0000\u01dd\u01de\u0001\u0000\u0000\u0000\u01de\u01df\u0005\u000e\u0000"+
		"\u0000\u01df\u01e0\u0003h4\u0000\u01e0\u01e1\u0003\u0004\u0002\u0000\u01e1"+
		"W\u0001\u0000\u0000\u0000\u01e2\u01e3\u0005.\u0000\u0000\u01e3Y\u0001"+
		"\u0000\u0000\u0000\u01e4\u01e5\u00055\u0000\u0000\u01e5[\u0001\u0000\u0000"+
		"\u0000\u01e6\u01e7\u00053\u0000\u0000\u01e7\u01e8\u0003h4\u0000\u01e8"+
		"\u01ec\u0003\u0004\u0002\u0000\u01e9\u01eb\u0003^/\u0000\u01ea\u01e9\u0001"+
		"\u0000\u0000\u0000\u01eb\u01ee\u0001\u0000\u0000\u0000\u01ec\u01ea\u0001"+
		"\u0000\u0000\u0000\u01ec\u01ed\u0001\u0000\u0000\u0000\u01ed\u01f0\u0001"+
		"\u0000\u0000\u0000\u01ee\u01ec\u0001\u0000\u0000\u0000\u01ef\u01f1\u0003"+
		"`0\u0000\u01f0\u01ef\u0001\u0000\u0000\u0000\u01f0\u01f1\u0001\u0000\u0000"+
		"\u0000\u01f1]\u0001\u0000\u0000\u0000\u01f2\u01f3\u00051\u0000\u0000\u01f3"+
		"\u01f4\u00053\u0000\u0000\u01f4\u01f5\u0003h4\u0000\u01f5\u01f6\u0003"+
		"\u0004\u0002\u0000\u01f6_\u0001\u0000\u0000\u0000\u01f7\u01f8\u00051\u0000"+
		"\u0000\u01f8\u01f9\u0003\u0004\u0002\u0000\u01f9a\u0001\u0000\u0000\u0000"+
		"\u01fa\u0200\u00059\u0000\u0000\u01fb\u0200\u0005:\u0000\u0000\u01fc\u0200"+
		"\u0003f3\u0000\u01fd\u0200\u0003d2\u0000\u01fe\u0200\u0005\u0010\u0000"+
		"\u0000\u01ff\u01fa\u0001\u0000\u0000\u0000\u01ff\u01fb\u0001\u0000\u0000"+
		"\u0000\u01ff\u01fc\u0001\u0000\u0000\u0000\u01ff\u01fd\u0001\u0000\u0000"+
		"\u0000\u01ff\u01fe\u0001\u0000\u0000\u0000\u0200c\u0001\u0000\u0000\u0000"+
		"\u0201\u0202\u00050\u0000\u0000\u0202\u0203\u0005E\u0000\u0000\u0203\u0204"+
		"\u0003b1\u0000\u0204\u0205\u0005F\u0000\u0000\u0205\u0206\u0003b1\u0000"+
		"\u0206e\u0001\u0000\u0000\u0000\u0207\u0208\u0005E\u0000\u0000\u0208\u0209"+
		"\u0005F\u0000\u0000\u0209\u020a\u0003b1\u0000\u020ag\u0001\u0000\u0000"+
		"\u0000\u020b\u020c\u00064\uffff\uffff\u0000\u020c\u0216\u0003\u0080@\u0000"+
		"\u020d\u0216\u0003z=\u0000\u020e\u0216\u0003l6\u0000\u020f\u0210\u0005"+
		"Q\u0000\u0000\u0210\u0216\u0003h4\u0005\u0211\u0212\u0005A\u0000\u0000"+
		"\u0212\u0213\u0003h4\u0000\u0213\u0214\u0005B\u0000\u0000\u0214\u0216"+
		"\u0001\u0000\u0000\u0000\u0215\u020b\u0001\u0000\u0000\u0000\u0215\u020d"+
		"\u0001\u0000\u0000\u0000\u0215\u020e\u0001\u0000\u0000\u0000\u0215\u020f"+
		"\u0001\u0000\u0000\u0000\u0215\u0211\u0001\u0000\u0000\u0000\u0216\u0222"+
		"\u0001\u0000\u0000\u0000\u0217\u0218\n\u0003\u0000\u0000\u0218\u0219\u0005"+
		"R\u0000\u0000\u0219\u0221\u0003h4\u0004\u021a\u021b\n\u0002\u0000\u0000"+
		"\u021b\u021c\u0007\u0006\u0000\u0000\u021c\u0221\u0003h4\u0003\u021d\u021e"+
		"\n\u0001\u0000\u0000\u021e\u021f\u0007\u0001\u0000\u0000\u021f\u0221\u0003"+
		"h4\u0002\u0220\u0217\u0001\u0000\u0000\u0000\u0220\u021a\u0001\u0000\u0000"+
		"\u0000\u0220\u021d\u0001\u0000\u0000\u0000\u0221\u0224\u0001\u0000\u0000"+
		"\u0000\u0222\u0220\u0001\u0000\u0000\u0000\u0222\u0223\u0001\u0000\u0000"+
		"\u0000\u0223i\u0001\u0000\u0000\u0000\u0224\u0222\u0001\u0000\u0000\u0000"+
		"\u0225\u022a\u0003h4\u0000\u0226\u0227\u0005H\u0000\u0000\u0227\u0229"+
		"\u0003h4\u0000\u0228\u0226\u0001\u0000\u0000\u0000\u0229\u022c\u0001\u0000"+
		"\u0000\u0000\u022a\u0228\u0001\u0000\u0000\u0000\u022a\u022b\u0001\u0000"+
		"\u0000\u0000\u022bk\u0001\u0000\u0000\u0000\u022c\u022a\u0001\u0000\u0000"+
		"\u0000\u022d\u0232\u0003n7\u0000\u022e\u0232\u0003p8\u0000\u022f\u0232"+
		"\u0003r9\u0000\u0230\u0232\u0003v;\u0000\u0231\u022d\u0001\u0000\u0000"+
		"\u0000\u0231\u022e\u0001\u0000\u0000\u0000\u0231\u022f\u0001\u0000\u0000"+
		"\u0000\u0231\u0230\u0001\u0000\u0000\u0000\u0232m\u0001\u0000\u0000\u0000"+
		"\u0233\u0234\u0005S\u0000\u0000\u0234o\u0001\u0000\u0000\u0000\u0235\u0236"+
		"\u0007\u0007\u0000\u0000\u0236q\u0001\u0000\u0000\u0000\u0237\u0239\u0005"+
		"E\u0000\u0000\u0238\u023a\u0003j5\u0000\u0239\u0238\u0001\u0000\u0000"+
		"\u0000\u0239\u023a\u0001\u0000\u0000\u0000\u023a\u023b\u0001\u0000\u0000"+
		"\u0000\u023b\u023c\u0005F\u0000\u0000\u023cs\u0001\u0000\u0000\u0000\u023d"+
		"\u0246\u0005E\u0000\u0000\u023e\u0243\u0003n7\u0000\u023f\u0240\u0005"+
		"H\u0000\u0000\u0240\u0242\u0003n7\u0000\u0241\u023f\u0001\u0000\u0000"+
		"\u0000\u0242\u0245\u0001\u0000\u0000\u0000\u0243\u0241\u0001\u0000\u0000"+
		"\u0000\u0243\u0244\u0001\u0000\u0000\u0000\u0244\u0247\u0001\u0000\u0000"+
		"\u0000\u0245\u0243\u0001\u0000\u0000\u0000\u0246\u023e\u0001\u0000\u0000"+
		"\u0000\u0246\u0247\u0001\u0000\u0000\u0000\u0247\u0248\u0001\u0000\u0000"+
		"\u0000\u0248\u0249\u0005F\u0000\u0000\u0249u\u0001\u0000\u0000\u0000\u024a"+
		"\u0253\u0005C\u0000\u0000\u024b\u0250\u0003x<\u0000\u024c\u024d\u0005"+
		"H\u0000\u0000\u024d\u024f\u0003x<\u0000\u024e\u024c\u0001\u0000\u0000"+
		"\u0000\u024f\u0252\u0001\u0000\u0000\u0000\u0250\u024e\u0001\u0000\u0000"+
		"\u0000\u0250\u0251\u0001\u0000\u0000\u0000\u0251\u0254\u0001\u0000\u0000"+
		"\u0000\u0252\u0250\u0001\u0000\u0000\u0000\u0253\u024b\u0001\u0000\u0000"+
		"\u0000\u0253\u0254\u0001\u0000\u0000\u0000\u0254\u0255\u0001\u0000\u0000"+
		"\u0000\u0255\u0256\u0005D\u0000\u0000\u0256w\u0001\u0000\u0000\u0000\u0257"+
		"\u0258\u0003h4\u0000\u0258\u0259\u0005J\u0000\u0000\u0259\u025a\u0003"+
		"h4\u0000\u025ay\u0001\u0000\u0000\u0000\u025b\u025c\u0006=\uffff\uffff"+
		"\u0000\u025c\u025d\u0005@\u0000\u0000\u025d\u0262\u0001\u0000\u0000\u0000"+
		"\u025e\u025f\n\u0001\u0000\u0000\u025f\u0261\u0003|>\u0000\u0260\u025e"+
		"\u0001\u0000\u0000\u0000\u0261\u0264\u0001\u0000\u0000\u0000\u0262\u0260"+
		"\u0001\u0000\u0000\u0000\u0262\u0263\u0001\u0000\u0000\u0000\u0263{\u0001"+
		"\u0000\u0000\u0000\u0264\u0262\u0001\u0000\u0000\u0000\u0265\u0266\u0005"+
		"E\u0000\u0000\u0266\u0267\u0003h4\u0000\u0267\u0268\u0005F\u0000\u0000"+
		"\u0268\u026c\u0001\u0000\u0000\u0000\u0269\u026a\u0005K\u0000\u0000\u026a"+
		"\u026c\u0003~?\u0000\u026b\u0265\u0001\u0000\u0000\u0000\u026b\u0269\u0001"+
		"\u0000\u0000\u0000\u026c}\u0001\u0000\u0000\u0000\u026d\u026e\u0005@\u0000"+
		"\u0000\u026e\u007f\u0001\u0000\u0000\u0000\u026f\u0270\u0005@\u0000\u0000"+
		"\u0270\u0271\u0003\u0082A\u0000\u0271\u0081\u0001\u0000\u0000\u0000\u0272"+
		"\u0274\u0005A\u0000\u0000\u0273\u0275\u0003j5\u0000\u0274\u0273\u0001"+
		"\u0000\u0000\u0000\u0274\u0275\u0001\u0000\u0000\u0000\u0275\u0276\u0001"+
		"\u0000\u0000\u0000\u0276\u0277\u0005B\u0000\u0000\u0277\u0083\u0001\u0000"+
		"\u0000\u00002\u0087\u00a1\u00a7\u00bf\u00cd\u00d5\u00df\u00e6\u00ee\u00f3"+
		"\u00fb\u00fe\u0106\u010e\u011b\u0122\u0129\u0135\u013d\u0172\u0183\u0187"+
		"\u018c\u0194\u019b\u01a5\u01ac\u01af\u01b2\u01b9\u01c4\u01cf\u01d2\u01dc"+
		"\u01ec\u01f0\u01ff\u0215\u0220\u0222\u022a\u0231\u0239\u0243\u0246\u0250"+
		"\u0253\u0262\u026b\u0274";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}