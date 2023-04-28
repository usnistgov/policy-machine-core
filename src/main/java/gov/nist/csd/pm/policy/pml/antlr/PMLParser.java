// Generated from PML.g4 by ANTLR 4.8
package gov.nist.csd.pm.policy.pml.antlr;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class PMLParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		CREATE=1, DELETE=2, BREAK=3, CONTINUE=4, POLICY_ELEMENT=5, RULE=6, WHEN=7, 
		PERFORMS=8, AS=9, ON=10, DO=11, ANY_USER=12, USERS=13, ANY_USER_WITH_ATTRIBUTE=14, 
		PROCESS=15, INTERSECTION=16, UNION=17, SET_RESOURCE_ACCESS_RIGHTS=18, 
		ASSIGN=19, DEASSIGN=20, FROM=21, SET_PROPERTIES=22, OF=23, TO=24, ASSOCIATE=25, 
		AND=26, WITH=27, DISSOCIATE=28, DENY=29, PROHIBITION=30, OBLIGATION=31, 
		ACCESS_RIGHTS=32, POLICY_CLASS=33, OBJECT_ATTRIBUTE=34, USER_ATTRIBUTE=35, 
		OBJECT=36, USER=37, ATTR=38, ANY=39, LET=40, CONST=41, FUNCTION=42, RETURN=43, 
		BOOLEAN=44, TRUE=45, FALSE=46, STRING_TYPE=47, BOOLEAN_TYPE=48, VOID_TYPE=49, 
		ARRAY_TYPE=50, MAP_TYPE=51, FOREACH=52, FOR=53, IN=54, IF=55, ELSE=56, 
		IN_RANGE=57, NUMBER=58, ID=59, STRING=60, DOUBLE_QUOTE_STRING=61, SINGLE_QUOTE_STRING=62, 
		LINE_COMMENT=63, WS=64, COMMA=65, COLON=66, OPEN_CURLY=67, CLOSE_CURLY=68, 
		OPEN_BRACKET=69, CLOSE_BRACKET=70, OPEN_ANGLE_BRACKET=71, CLOSE_ANGLE_BRACKET=72, 
		OPEN_PAREN=73, CLOSE_PAREN=74, IS_COMPLEMENT=75, EQUALS=76, AND_OP=77, 
		OR_OP=78, EQUALS_OP=79, NOT_EQUALS_OP=80;
	public static final int
		RULE_pml = 0, RULE_statement = 1, RULE_createPolicyStatement = 2, RULE_createAttributeStatement = 3, 
		RULE_createUserOrObjectStatement = 4, RULE_createObligationStatement = 5, 
		RULE_createRuleStatement = 6, RULE_subjectClause = 7, RULE_onClause = 8, 
		RULE_anyPe = 9, RULE_response = 10, RULE_responseBlock = 11, RULE_responseStatement = 12, 
		RULE_createProhibitionStatement = 13, RULE_prohibitionContainerList = 14, 
		RULE_prohibitionContainerExpression = 15, RULE_setNodePropertiesStatement = 16, 
		RULE_assignStatement = 17, RULE_deassignStatement = 18, RULE_associateStatement = 19, 
		RULE_dissociateStatement = 20, RULE_setResourceAccessRightsStatement = 21, 
		RULE_deleteStatement = 22, RULE_deleteType = 23, RULE_nodeType = 24, RULE_deleteRuleStatement = 25, 
		RULE_variableDeclarationStatement = 26, RULE_functionDefinitionStatement = 27, 
		RULE_formalArgList = 28, RULE_formalArg = 29, RULE_formalArgType = 30, 
		RULE_functionReturnStatement = 31, RULE_funcReturnType = 32, RULE_funcBody = 33, 
		RULE_foreachStatement = 34, RULE_forRangeStatement = 35, RULE_breakStatement = 36, 
		RULE_continueStatement = 37, RULE_functionInvokeStatement = 38, RULE_ifStatement = 39, 
		RULE_elseIfStatement = 40, RULE_elseStatement = 41, RULE_variableType = 42, 
		RULE_mapType = 43, RULE_arrayType = 44, RULE_statementBlock = 45, RULE_expression = 46, 
		RULE_array = 47, RULE_map = 48, RULE_mapEntry = 49, RULE_entryReference = 50, 
		RULE_literal = 51, RULE_variableReference = 52, RULE_functionInvoke = 53, 
		RULE_functionInvokeArgs = 54;
	private static String[] makeRuleNames() {
		return new String[] {
			"pml", "statement", "createPolicyStatement", "createAttributeStatement", 
			"createUserOrObjectStatement", "createObligationStatement", "createRuleStatement", 
			"subjectClause", "onClause", "anyPe", "response", "responseBlock", "responseStatement", 
			"createProhibitionStatement", "prohibitionContainerList", "prohibitionContainerExpression", 
			"setNodePropertiesStatement", "assignStatement", "deassignStatement", 
			"associateStatement", "dissociateStatement", "setResourceAccessRightsStatement", 
			"deleteStatement", "deleteType", "nodeType", "deleteRuleStatement", "variableDeclarationStatement", 
			"functionDefinitionStatement", "formalArgList", "formalArg", "formalArgType", 
			"functionReturnStatement", "funcReturnType", "funcBody", "foreachStatement", 
			"forRangeStatement", "breakStatement", "continueStatement", "functionInvokeStatement", 
			"ifStatement", "elseIfStatement", "elseStatement", "variableType", "mapType", 
			"arrayType", "statementBlock", "expression", "array", "map", "mapEntry", 
			"entryReference", "literal", "variableReference", "functionInvoke", "functionInvokeArgs"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, "','", "':'", "'{'", "'}'", "'['", "']'", 
			"'<'", "'>'", "'('", "')'", "'!'", "'='", "'&&'", "'||'", "'=='", "'!='"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "CREATE", "DELETE", "BREAK", "CONTINUE", "POLICY_ELEMENT", "RULE", 
			"WHEN", "PERFORMS", "AS", "ON", "DO", "ANY_USER", "USERS", "ANY_USER_WITH_ATTRIBUTE", 
			"PROCESS", "INTERSECTION", "UNION", "SET_RESOURCE_ACCESS_RIGHTS", "ASSIGN", 
			"DEASSIGN", "FROM", "SET_PROPERTIES", "OF", "TO", "ASSOCIATE", "AND", 
			"WITH", "DISSOCIATE", "DENY", "PROHIBITION", "OBLIGATION", "ACCESS_RIGHTS", 
			"POLICY_CLASS", "OBJECT_ATTRIBUTE", "USER_ATTRIBUTE", "OBJECT", "USER", 
			"ATTR", "ANY", "LET", "CONST", "FUNCTION", "RETURN", "BOOLEAN", "TRUE", 
			"FALSE", "STRING_TYPE", "BOOLEAN_TYPE", "VOID_TYPE", "ARRAY_TYPE", "MAP_TYPE", 
			"FOREACH", "FOR", "IN", "IF", "ELSE", "IN_RANGE", "NUMBER", "ID", "STRING", 
			"DOUBLE_QUOTE_STRING", "SINGLE_QUOTE_STRING", "LINE_COMMENT", "WS", "COMMA", 
			"COLON", "OPEN_CURLY", "CLOSE_CURLY", "OPEN_BRACKET", "CLOSE_BRACKET", 
			"OPEN_ANGLE_BRACKET", "CLOSE_ANGLE_BRACKET", "OPEN_PAREN", "CLOSE_PAREN", 
			"IS_COMPLEMENT", "EQUALS", "AND_OP", "OR_OP", "EQUALS_OP", "NOT_EQUALS_OP"
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
	public String getGrammarFileName() { return "PML.g4"; }

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
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterPml(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitPml(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitPml(this);
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
			setState(113);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << CREATE) | (1L << DELETE) | (1L << BREAK) | (1L << CONTINUE) | (1L << SET_RESOURCE_ACCESS_RIGHTS) | (1L << ASSIGN) | (1L << DEASSIGN) | (1L << SET_PROPERTIES) | (1L << ASSOCIATE) | (1L << DISSOCIATE) | (1L << LET) | (1L << CONST) | (1L << FUNCTION) | (1L << RETURN) | (1L << FOREACH) | (1L << FOR) | (1L << IF) | (1L << ID))) != 0)) {
				{
				{
				setState(110);
				statement();
				}
				}
				setState(115);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(116);
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

	public static class StatementContext extends ParserRuleContext {
		public CreatePolicyStatementContext createPolicyStatement() {
			return getRuleContext(CreatePolicyStatementContext.class,0);
		}
		public CreateAttributeStatementContext createAttributeStatement() {
			return getRuleContext(CreateAttributeStatementContext.class,0);
		}
		public CreateUserOrObjectStatementContext createUserOrObjectStatement() {
			return getRuleContext(CreateUserOrObjectStatementContext.class,0);
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
		public SetResourceAccessRightsStatementContext setResourceAccessRightsStatement() {
			return getRuleContext(SetResourceAccessRightsStatementContext.class,0);
		}
		public DeleteStatementContext deleteStatement() {
			return getRuleContext(DeleteStatementContext.class,0);
		}
		public DeleteRuleStatementContext deleteRuleStatement() {
			return getRuleContext(DeleteRuleStatementContext.class,0);
		}
		public VariableDeclarationStatementContext variableDeclarationStatement() {
			return getRuleContext(VariableDeclarationStatementContext.class,0);
		}
		public FunctionDefinitionStatementContext functionDefinitionStatement() {
			return getRuleContext(FunctionDefinitionStatementContext.class,0);
		}
		public FunctionReturnStatementContext functionReturnStatement() {
			return getRuleContext(FunctionReturnStatementContext.class,0);
		}
		public ForeachStatementContext foreachStatement() {
			return getRuleContext(ForeachStatementContext.class,0);
		}
		public ForRangeStatementContext forRangeStatement() {
			return getRuleContext(ForRangeStatementContext.class,0);
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
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_statement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(140);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				{
				setState(118);
				createPolicyStatement();
				}
				break;
			case 2:
				{
				setState(119);
				createAttributeStatement();
				}
				break;
			case 3:
				{
				setState(120);
				createUserOrObjectStatement();
				}
				break;
			case 4:
				{
				setState(121);
				createObligationStatement();
				}
				break;
			case 5:
				{
				setState(122);
				createProhibitionStatement();
				}
				break;
			case 6:
				{
				setState(123);
				setNodePropertiesStatement();
				}
				break;
			case 7:
				{
				setState(124);
				assignStatement();
				}
				break;
			case 8:
				{
				setState(125);
				deassignStatement();
				}
				break;
			case 9:
				{
				setState(126);
				associateStatement();
				}
				break;
			case 10:
				{
				setState(127);
				dissociateStatement();
				}
				break;
			case 11:
				{
				setState(128);
				setResourceAccessRightsStatement();
				}
				break;
			case 12:
				{
				setState(129);
				deleteStatement();
				}
				break;
			case 13:
				{
				setState(130);
				deleteRuleStatement();
				}
				break;
			case 14:
				{
				setState(131);
				variableDeclarationStatement();
				}
				break;
			case 15:
				{
				setState(132);
				functionDefinitionStatement();
				}
				break;
			case 16:
				{
				setState(133);
				functionReturnStatement();
				}
				break;
			case 17:
				{
				setState(134);
				foreachStatement();
				}
				break;
			case 18:
				{
				setState(135);
				forRangeStatement();
				}
				break;
			case 19:
				{
				setState(136);
				breakStatement();
				}
				break;
			case 20:
				{
				setState(137);
				continueStatement();
				}
				break;
			case 21:
				{
				setState(138);
				functionInvokeStatement();
				}
				break;
			case 22:
				{
				setState(139);
				ifStatement();
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

	public static class CreatePolicyStatementContext extends ParserRuleContext {
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
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterCreatePolicyStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitCreatePolicyStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitCreatePolicyStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreatePolicyStatementContext createPolicyStatement() throws RecognitionException {
		CreatePolicyStatementContext _localctx = new CreatePolicyStatementContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_createPolicyStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(142);
			match(CREATE);
			setState(143);
			match(POLICY_CLASS);
			setState(144);
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

	public static class CreateAttributeStatementContext extends ParserRuleContext {
		public ExpressionContext name;
		public ExpressionContext parents;
		public TerminalNode CREATE() { return getToken(PMLParser.CREATE, 0); }
		public TerminalNode IN() { return getToken(PMLParser.IN, 0); }
		public TerminalNode OBJECT_ATTRIBUTE() { return getToken(PMLParser.OBJECT_ATTRIBUTE, 0); }
		public TerminalNode USER_ATTRIBUTE() { return getToken(PMLParser.USER_ATTRIBUTE, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public CreateAttributeStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_createAttributeStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterCreateAttributeStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitCreateAttributeStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitCreateAttributeStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreateAttributeStatementContext createAttributeStatement() throws RecognitionException {
		CreateAttributeStatementContext _localctx = new CreateAttributeStatementContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_createAttributeStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(146);
			match(CREATE);
			setState(147);
			_la = _input.LA(1);
			if ( !(_la==OBJECT_ATTRIBUTE || _la==USER_ATTRIBUTE) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(148);
			((CreateAttributeStatementContext)_localctx).name = expression(0);
			setState(149);
			match(IN);
			setState(150);
			((CreateAttributeStatementContext)_localctx).parents = expression(0);
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

	public static class CreateUserOrObjectStatementContext extends ParserRuleContext {
		public ExpressionContext name;
		public ExpressionContext parents;
		public TerminalNode CREATE() { return getToken(PMLParser.CREATE, 0); }
		public TerminalNode IN() { return getToken(PMLParser.IN, 0); }
		public TerminalNode USER() { return getToken(PMLParser.USER, 0); }
		public TerminalNode OBJECT() { return getToken(PMLParser.OBJECT, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public CreateUserOrObjectStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_createUserOrObjectStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterCreateUserOrObjectStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitCreateUserOrObjectStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitCreateUserOrObjectStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreateUserOrObjectStatementContext createUserOrObjectStatement() throws RecognitionException {
		CreateUserOrObjectStatementContext _localctx = new CreateUserOrObjectStatementContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_createUserOrObjectStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(152);
			match(CREATE);
			setState(153);
			_la = _input.LA(1);
			if ( !(_la==OBJECT || _la==USER) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(154);
			((CreateUserOrObjectStatementContext)_localctx).name = expression(0);
			setState(155);
			match(IN);
			setState(156);
			((CreateUserOrObjectStatementContext)_localctx).parents = expression(0);
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
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterCreateObligationStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitCreateObligationStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitCreateObligationStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreateObligationStatementContext createObligationStatement() throws RecognitionException {
		CreateObligationStatementContext _localctx = new CreateObligationStatementContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_createObligationStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(158);
			match(CREATE);
			setState(159);
			match(OBLIGATION);
			setState(160);
			expression(0);
			setState(161);
			match(OPEN_CURLY);
			setState(165);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==CREATE) {
				{
				{
				setState(162);
				createRuleStatement();
				}
				}
				setState(167);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(168);
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

	public static class CreateRuleStatementContext extends ParserRuleContext {
		public ExpressionContext ruleName;
		public ExpressionContext performsClause;
		public TerminalNode CREATE() { return getToken(PMLParser.CREATE, 0); }
		public TerminalNode RULE() { return getToken(PMLParser.RULE, 0); }
		public TerminalNode WHEN() { return getToken(PMLParser.WHEN, 0); }
		public SubjectClauseContext subjectClause() {
			return getRuleContext(SubjectClauseContext.class,0);
		}
		public TerminalNode PERFORMS() { return getToken(PMLParser.PERFORMS, 0); }
		public ResponseContext response() {
			return getRuleContext(ResponseContext.class,0);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode ON() { return getToken(PMLParser.ON, 0); }
		public OnClauseContext onClause() {
			return getRuleContext(OnClauseContext.class,0);
		}
		public CreateRuleStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_createRuleStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterCreateRuleStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitCreateRuleStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitCreateRuleStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreateRuleStatementContext createRuleStatement() throws RecognitionException {
		CreateRuleStatementContext _localctx = new CreateRuleStatementContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_createRuleStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(170);
			match(CREATE);
			setState(171);
			match(RULE);
			setState(172);
			((CreateRuleStatementContext)_localctx).ruleName = expression(0);
			setState(173);
			match(WHEN);
			setState(174);
			subjectClause();
			setState(175);
			match(PERFORMS);
			setState(176);
			((CreateRuleStatementContext)_localctx).performsClause = expression(0);
			setState(179);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ON) {
				{
				setState(177);
				match(ON);
				setState(178);
				onClause();
				}
			}

			setState(181);
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

	public static class SubjectClauseContext extends ParserRuleContext {
		public SubjectClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_subjectClause; }
	 
		public SubjectClauseContext() { }
		public void copyFrom(SubjectClauseContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class UserSubjectContext extends SubjectClauseContext {
		public ExpressionContext user;
		public TerminalNode USER() { return getToken(PMLParser.USER, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public UserSubjectContext(SubjectClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterUserSubject(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitUserSubject(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitUserSubject(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class UsersListSubjectContext extends SubjectClauseContext {
		public ExpressionContext users;
		public TerminalNode USERS() { return getToken(PMLParser.USERS, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public UsersListSubjectContext(SubjectClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterUsersListSubject(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitUsersListSubject(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitUsersListSubject(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class UserAttrSubjectContext extends SubjectClauseContext {
		public ExpressionContext attribute;
		public TerminalNode ANY_USER_WITH_ATTRIBUTE() { return getToken(PMLParser.ANY_USER_WITH_ATTRIBUTE, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public UserAttrSubjectContext(SubjectClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterUserAttrSubject(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitUserAttrSubject(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitUserAttrSubject(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ProcessSubjectContext extends SubjectClauseContext {
		public ExpressionContext process;
		public TerminalNode PROCESS() { return getToken(PMLParser.PROCESS, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ProcessSubjectContext(SubjectClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterProcessSubject(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitProcessSubject(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitProcessSubject(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AnyUserSubjectContext extends SubjectClauseContext {
		public TerminalNode ANY_USER() { return getToken(PMLParser.ANY_USER, 0); }
		public AnyUserSubjectContext(SubjectClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterAnyUserSubject(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitAnyUserSubject(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitAnyUserSubject(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SubjectClauseContext subjectClause() throws RecognitionException {
		SubjectClauseContext _localctx = new SubjectClauseContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_subjectClause);
		try {
			setState(192);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ANY_USER:
				_localctx = new AnyUserSubjectContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(183);
				match(ANY_USER);
				}
				break;
			case USER:
				_localctx = new UserSubjectContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(184);
				match(USER);
				setState(185);
				((UserSubjectContext)_localctx).user = expression(0);
				}
				break;
			case USERS:
				_localctx = new UsersListSubjectContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(186);
				match(USERS);
				setState(187);
				((UsersListSubjectContext)_localctx).users = expression(0);
				}
				break;
			case ANY_USER_WITH_ATTRIBUTE:
				_localctx = new UserAttrSubjectContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(188);
				match(ANY_USER_WITH_ATTRIBUTE);
				setState(189);
				((UserAttrSubjectContext)_localctx).attribute = expression(0);
				}
				break;
			case PROCESS:
				_localctx = new ProcessSubjectContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(190);
				match(PROCESS);
				setState(191);
				((ProcessSubjectContext)_localctx).process = expression(0);
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

	public static class OnClauseContext extends ParserRuleContext {
		public OnClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_onClause; }
	 
		public OnClauseContext() { }
		public void copyFrom(OnClauseContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class AnyContainedInContext extends OnClauseContext {
		public AnyPeContext anyPe() {
			return getRuleContext(AnyPeContext.class,0);
		}
		public TerminalNode IN() { return getToken(PMLParser.IN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public AnyContainedInContext(OnClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterAnyContainedIn(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitAnyContainedIn(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitAnyContainedIn(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AnyOfSetContext extends OnClauseContext {
		public AnyPeContext anyPe() {
			return getRuleContext(AnyPeContext.class,0);
		}
		public TerminalNode OF() { return getToken(PMLParser.OF, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public AnyOfSetContext(OnClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterAnyOfSet(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitAnyOfSet(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitAnyOfSet(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AnyPolicyElementContext extends OnClauseContext {
		public AnyPeContext anyPe() {
			return getRuleContext(AnyPeContext.class,0);
		}
		public AnyPolicyElementContext(OnClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterAnyPolicyElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitAnyPolicyElement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitAnyPolicyElement(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PolicyElementContext extends OnClauseContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public PolicyElementContext(OnClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterPolicyElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitPolicyElement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitPolicyElement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OnClauseContext onClause() throws RecognitionException {
		OnClauseContext _localctx = new OnClauseContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_onClause);
		try {
			setState(204);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				_localctx = new PolicyElementContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(194);
				expression(0);
				}
				break;
			case 2:
				_localctx = new AnyPolicyElementContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(195);
				anyPe();
				}
				break;
			case 3:
				_localctx = new AnyContainedInContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(196);
				anyPe();
				setState(197);
				match(IN);
				setState(198);
				expression(0);
				}
				break;
			case 4:
				_localctx = new AnyOfSetContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(200);
				anyPe();
				setState(201);
				match(OF);
				setState(202);
				expression(0);
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

	public static class AnyPeContext extends ParserRuleContext {
		public TerminalNode ANY() { return getToken(PMLParser.ANY, 0); }
		public TerminalNode POLICY_ELEMENT() { return getToken(PMLParser.POLICY_ELEMENT, 0); }
		public AnyPeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_anyPe; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterAnyPe(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitAnyPe(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitAnyPe(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnyPeContext anyPe() throws RecognitionException {
		AnyPeContext _localctx = new AnyPeContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_anyPe);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(206);
			match(ANY);
			setState(207);
			match(POLICY_ELEMENT);
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
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterResponse(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitResponse(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitResponse(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ResponseContext response() throws RecognitionException {
		ResponseContext _localctx = new ResponseContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_response);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(209);
			match(DO);
			setState(210);
			match(OPEN_PAREN);
			setState(211);
			match(ID);
			setState(212);
			match(CLOSE_PAREN);
			setState(213);
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
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterResponseBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitResponseBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitResponseBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ResponseBlockContext responseBlock() throws RecognitionException {
		ResponseBlockContext _localctx = new ResponseBlockContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_responseBlock);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(215);
			match(OPEN_CURLY);
			setState(219);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << CREATE) | (1L << DELETE) | (1L << BREAK) | (1L << CONTINUE) | (1L << SET_RESOURCE_ACCESS_RIGHTS) | (1L << ASSIGN) | (1L << DEASSIGN) | (1L << SET_PROPERTIES) | (1L << ASSOCIATE) | (1L << DISSOCIATE) | (1L << LET) | (1L << CONST) | (1L << FUNCTION) | (1L << RETURN) | (1L << FOREACH) | (1L << FOR) | (1L << IF) | (1L << ID))) != 0)) {
				{
				{
				setState(216);
				responseStatement();
				}
				}
				setState(221);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(222);
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
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterResponseStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitResponseStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitResponseStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ResponseStatementContext responseStatement() throws RecognitionException {
		ResponseStatementContext _localctx = new ResponseStatementContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_responseStatement);
		try {
			setState(227);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(224);
				statement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(225);
				createRuleStatement();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(226);
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

	public static class CreateProhibitionStatementContext extends ParserRuleContext {
		public ExpressionContext name;
		public ExpressionContext subject;
		public ExpressionContext accessRights;
		public ProhibitionContainerListContext containers;
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
		public ProhibitionContainerListContext prohibitionContainerList() {
			return getRuleContext(ProhibitionContainerListContext.class,0);
		}
		public CreateProhibitionStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_createProhibitionStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterCreateProhibitionStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitCreateProhibitionStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitCreateProhibitionStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreateProhibitionStatementContext createProhibitionStatement() throws RecognitionException {
		CreateProhibitionStatementContext _localctx = new CreateProhibitionStatementContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_createProhibitionStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(229);
			match(CREATE);
			setState(230);
			match(PROHIBITION);
			setState(231);
			((CreateProhibitionStatementContext)_localctx).name = expression(0);
			setState(232);
			match(DENY);
			setState(233);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PROCESS) | (1L << USER_ATTRIBUTE) | (1L << USER))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(234);
			((CreateProhibitionStatementContext)_localctx).subject = expression(0);
			setState(235);
			match(ACCESS_RIGHTS);
			setState(236);
			((CreateProhibitionStatementContext)_localctx).accessRights = expression(0);
			setState(237);
			match(ON);
			setState(238);
			_la = _input.LA(1);
			if ( !(_la==INTERSECTION || _la==UNION) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(239);
			match(OF);
			setState(240);
			((CreateProhibitionStatementContext)_localctx).containers = prohibitionContainerList();
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

	public static class ProhibitionContainerListContext extends ParserRuleContext {
		public TerminalNode OPEN_BRACKET() { return getToken(PMLParser.OPEN_BRACKET, 0); }
		public TerminalNode CLOSE_BRACKET() { return getToken(PMLParser.CLOSE_BRACKET, 0); }
		public List<ProhibitionContainerExpressionContext> prohibitionContainerExpression() {
			return getRuleContexts(ProhibitionContainerExpressionContext.class);
		}
		public ProhibitionContainerExpressionContext prohibitionContainerExpression(int i) {
			return getRuleContext(ProhibitionContainerExpressionContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(PMLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(PMLParser.COMMA, i);
		}
		public ProhibitionContainerListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prohibitionContainerList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterProhibitionContainerList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitProhibitionContainerList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitProhibitionContainerList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProhibitionContainerListContext prohibitionContainerList() throws RecognitionException {
		ProhibitionContainerListContext _localctx = new ProhibitionContainerListContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_prohibitionContainerList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(242);
			match(OPEN_BRACKET);
			setState(251);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 44)) & ~0x3f) == 0 && ((1L << (_la - 44)) & ((1L << (BOOLEAN - 44)) | (1L << (NUMBER - 44)) | (1L << (ID - 44)) | (1L << (STRING - 44)) | (1L << (OPEN_CURLY - 44)) | (1L << (OPEN_BRACKET - 44)) | (1L << (IS_COMPLEMENT - 44)))) != 0)) {
				{
				setState(243);
				prohibitionContainerExpression();
				setState(248);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(244);
					match(COMMA);
					setState(245);
					prohibitionContainerExpression();
					}
					}
					setState(250);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(253);
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

	public static class ProhibitionContainerExpressionContext extends ParserRuleContext {
		public ExpressionContext container;
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode IS_COMPLEMENT() { return getToken(PMLParser.IS_COMPLEMENT, 0); }
		public ProhibitionContainerExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prohibitionContainerExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterProhibitionContainerExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitProhibitionContainerExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitProhibitionContainerExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProhibitionContainerExpressionContext prohibitionContainerExpression() throws RecognitionException {
		ProhibitionContainerExpressionContext _localctx = new ProhibitionContainerExpressionContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_prohibitionContainerExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(256);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IS_COMPLEMENT) {
				{
				setState(255);
				match(IS_COMPLEMENT);
				}
			}

			setState(258);
			((ProhibitionContainerExpressionContext)_localctx).container = expression(0);
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
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterSetNodePropertiesStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitSetNodePropertiesStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitSetNodePropertiesStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SetNodePropertiesStatementContext setNodePropertiesStatement() throws RecognitionException {
		SetNodePropertiesStatementContext _localctx = new SetNodePropertiesStatementContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_setNodePropertiesStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(260);
			match(SET_PROPERTIES);
			setState(261);
			match(OF);
			setState(262);
			((SetNodePropertiesStatementContext)_localctx).name = expression(0);
			setState(263);
			match(TO);
			setState(264);
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

	public static class AssignStatementContext extends ParserRuleContext {
		public ExpressionContext childNode;
		public ExpressionContext parentNodes;
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
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterAssignStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitAssignStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitAssignStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssignStatementContext assignStatement() throws RecognitionException {
		AssignStatementContext _localctx = new AssignStatementContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_assignStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(266);
			match(ASSIGN);
			setState(267);
			((AssignStatementContext)_localctx).childNode = expression(0);
			setState(268);
			match(TO);
			setState(269);
			((AssignStatementContext)_localctx).parentNodes = expression(0);
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

	public static class DeassignStatementContext extends ParserRuleContext {
		public ExpressionContext childNode;
		public ExpressionContext parentNodes;
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
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterDeassignStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitDeassignStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitDeassignStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeassignStatementContext deassignStatement() throws RecognitionException {
		DeassignStatementContext _localctx = new DeassignStatementContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_deassignStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(271);
			match(DEASSIGN);
			setState(272);
			((DeassignStatementContext)_localctx).childNode = expression(0);
			setState(273);
			match(FROM);
			setState(274);
			((DeassignStatementContext)_localctx).parentNodes = expression(0);
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
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterAssociateStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitAssociateStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitAssociateStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssociateStatementContext associateStatement() throws RecognitionException {
		AssociateStatementContext _localctx = new AssociateStatementContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_associateStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(276);
			match(ASSOCIATE);
			setState(277);
			((AssociateStatementContext)_localctx).ua = expression(0);
			setState(278);
			match(AND);
			setState(279);
			((AssociateStatementContext)_localctx).target = expression(0);
			setState(280);
			match(WITH);
			setState(281);
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
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterDissociateStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitDissociateStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitDissociateStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DissociateStatementContext dissociateStatement() throws RecognitionException {
		DissociateStatementContext _localctx = new DissociateStatementContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_dissociateStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(283);
			match(DISSOCIATE);
			setState(284);
			((DissociateStatementContext)_localctx).ua = expression(0);
			setState(285);
			match(AND);
			setState(286);
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

	public static class SetResourceAccessRightsStatementContext extends ParserRuleContext {
		public ExpressionContext accessRights;
		public TerminalNode SET_RESOURCE_ACCESS_RIGHTS() { return getToken(PMLParser.SET_RESOURCE_ACCESS_RIGHTS, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public SetResourceAccessRightsStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_setResourceAccessRightsStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterSetResourceAccessRightsStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitSetResourceAccessRightsStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitSetResourceAccessRightsStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SetResourceAccessRightsStatementContext setResourceAccessRightsStatement() throws RecognitionException {
		SetResourceAccessRightsStatementContext _localctx = new SetResourceAccessRightsStatementContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_setResourceAccessRightsStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(288);
			match(SET_RESOURCE_ACCESS_RIGHTS);
			setState(289);
			((SetResourceAccessRightsStatementContext)_localctx).accessRights = expression(0);
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
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterDeleteStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitDeleteStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitDeleteStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeleteStatementContext deleteStatement() throws RecognitionException {
		DeleteStatementContext _localctx = new DeleteStatementContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_deleteStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(291);
			match(DELETE);
			setState(292);
			deleteType();
			setState(293);
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
	public static class DeleteNodeContext extends DeleteTypeContext {
		public NodeTypeContext nodeType() {
			return getRuleContext(NodeTypeContext.class,0);
		}
		public DeleteNodeContext(DeleteTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterDeleteNode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitDeleteNode(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitDeleteNode(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DeleteProhibitionContext extends DeleteTypeContext {
		public TerminalNode PROHIBITION() { return getToken(PMLParser.PROHIBITION, 0); }
		public DeleteProhibitionContext(DeleteTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterDeleteProhibition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitDeleteProhibition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitDeleteProhibition(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DeleteObligationContext extends DeleteTypeContext {
		public TerminalNode OBLIGATION() { return getToken(PMLParser.OBLIGATION, 0); }
		public DeleteObligationContext(DeleteTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterDeleteObligation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitDeleteObligation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitDeleteObligation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeleteTypeContext deleteType() throws RecognitionException {
		DeleteTypeContext _localctx = new DeleteTypeContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_deleteType);
		try {
			setState(298);
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
				setState(295);
				nodeType();
				}
				break;
			case OBLIGATION:
				_localctx = new DeleteObligationContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(296);
				match(OBLIGATION);
				}
				break;
			case PROHIBITION:
				_localctx = new DeleteProhibitionContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(297);
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
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterNodeType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitNodeType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitNodeType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NodeTypeContext nodeType() throws RecognitionException {
		NodeTypeContext _localctx = new NodeTypeContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_nodeType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(300);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << POLICY_CLASS) | (1L << OBJECT_ATTRIBUTE) | (1L << USER_ATTRIBUTE) | (1L << OBJECT) | (1L << USER))) != 0)) ) {
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
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterDeleteRuleStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitDeleteRuleStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitDeleteRuleStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeleteRuleStatementContext deleteRuleStatement() throws RecognitionException {
		DeleteRuleStatementContext _localctx = new DeleteRuleStatementContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_deleteRuleStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(302);
			match(DELETE);
			setState(303);
			match(RULE);
			setState(304);
			((DeleteRuleStatementContext)_localctx).ruleName = expression(0);
			setState(305);
			match(FROM);
			setState(306);
			match(OBLIGATION);
			setState(307);
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

	public static class VariableDeclarationStatementContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(PMLParser.ID, 0); }
		public TerminalNode EQUALS() { return getToken(PMLParser.EQUALS, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode LET() { return getToken(PMLParser.LET, 0); }
		public TerminalNode CONST() { return getToken(PMLParser.CONST, 0); }
		public VariableDeclarationStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableDeclarationStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterVariableDeclarationStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitVariableDeclarationStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitVariableDeclarationStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableDeclarationStatementContext variableDeclarationStatement() throws RecognitionException {
		VariableDeclarationStatementContext _localctx = new VariableDeclarationStatementContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_variableDeclarationStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(310);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LET || _la==CONST) {
				{
				setState(309);
				_la = _input.LA(1);
				if ( !(_la==LET || _la==CONST) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			setState(312);
			match(ID);
			setState(313);
			match(EQUALS);
			setState(314);
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

	public static class FunctionDefinitionStatementContext extends ParserRuleContext {
		public TerminalNode FUNCTION() { return getToken(PMLParser.FUNCTION, 0); }
		public TerminalNode ID() { return getToken(PMLParser.ID, 0); }
		public TerminalNode OPEN_PAREN() { return getToken(PMLParser.OPEN_PAREN, 0); }
		public FormalArgListContext formalArgList() {
			return getRuleContext(FormalArgListContext.class,0);
		}
		public TerminalNode CLOSE_PAREN() { return getToken(PMLParser.CLOSE_PAREN, 0); }
		public FuncBodyContext funcBody() {
			return getRuleContext(FuncBodyContext.class,0);
		}
		public FuncReturnTypeContext funcReturnType() {
			return getRuleContext(FuncReturnTypeContext.class,0);
		}
		public FunctionDefinitionStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionDefinitionStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterFunctionDefinitionStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitFunctionDefinitionStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitFunctionDefinitionStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionDefinitionStatementContext functionDefinitionStatement() throws RecognitionException {
		FunctionDefinitionStatementContext _localctx = new FunctionDefinitionStatementContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_functionDefinitionStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(316);
			match(FUNCTION);
			setState(317);
			match(ID);
			setState(318);
			match(OPEN_PAREN);
			setState(319);
			formalArgList();
			setState(320);
			match(CLOSE_PAREN);
			setState(322);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 39)) & ~0x3f) == 0 && ((1L << (_la - 39)) & ((1L << (ANY - 39)) | (1L << (STRING_TYPE - 39)) | (1L << (BOOLEAN_TYPE - 39)) | (1L << (VOID_TYPE - 39)) | (1L << (MAP_TYPE - 39)) | (1L << (OPEN_BRACKET - 39)))) != 0)) {
				{
				setState(321);
				funcReturnType();
				}
			}

			setState(324);
			funcBody();
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
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterFormalArgList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitFormalArgList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitFormalArgList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FormalArgListContext formalArgList() throws RecognitionException {
		FormalArgListContext _localctx = new FormalArgListContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_formalArgList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(334);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 39)) & ~0x3f) == 0 && ((1L << (_la - 39)) & ((1L << (ANY - 39)) | (1L << (STRING_TYPE - 39)) | (1L << (BOOLEAN_TYPE - 39)) | (1L << (MAP_TYPE - 39)) | (1L << (OPEN_BRACKET - 39)))) != 0)) {
				{
				setState(326);
				formalArg();
				setState(331);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(327);
					match(COMMA);
					setState(328);
					formalArg();
					}
					}
					setState(333);
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

	public static class FormalArgContext extends ParserRuleContext {
		public FormalArgTypeContext formalArgType() {
			return getRuleContext(FormalArgTypeContext.class,0);
		}
		public TerminalNode ID() { return getToken(PMLParser.ID, 0); }
		public FormalArgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_formalArg; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterFormalArg(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitFormalArg(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitFormalArg(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FormalArgContext formalArg() throws RecognitionException {
		FormalArgContext _localctx = new FormalArgContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_formalArg);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(336);
			formalArgType();
			setState(337);
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

	public static class FormalArgTypeContext extends ParserRuleContext {
		public VariableTypeContext variableType() {
			return getRuleContext(VariableTypeContext.class,0);
		}
		public FormalArgTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_formalArgType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterFormalArgType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitFormalArgType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitFormalArgType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FormalArgTypeContext formalArgType() throws RecognitionException {
		FormalArgTypeContext _localctx = new FormalArgTypeContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_formalArgType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(339);
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

	public static class FunctionReturnStatementContext extends ParserRuleContext {
		public TerminalNode RETURN() { return getToken(PMLParser.RETURN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public FunctionReturnStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionReturnStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterFunctionReturnStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitFunctionReturnStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitFunctionReturnStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionReturnStatementContext functionReturnStatement() throws RecognitionException {
		FunctionReturnStatementContext _localctx = new FunctionReturnStatementContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_functionReturnStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(344);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
			case 1:
				{
				setState(341);
				match(RETURN);
				setState(342);
				expression(0);
				}
				break;
			case 2:
				{
				setState(343);
				match(RETURN);
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

	public static class FuncReturnTypeContext extends ParserRuleContext {
		public FuncReturnTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funcReturnType; }
	 
		public FuncReturnTypeContext() { }
		public void copyFrom(FuncReturnTypeContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class VariableReturnTypeContext extends FuncReturnTypeContext {
		public VariableTypeContext variableType() {
			return getRuleContext(VariableTypeContext.class,0);
		}
		public VariableReturnTypeContext(FuncReturnTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterVariableReturnType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitVariableReturnType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitVariableReturnType(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class VoidReturnTypeContext extends FuncReturnTypeContext {
		public TerminalNode VOID_TYPE() { return getToken(PMLParser.VOID_TYPE, 0); }
		public VoidReturnTypeContext(FuncReturnTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterVoidReturnType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitVoidReturnType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitVoidReturnType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FuncReturnTypeContext funcReturnType() throws RecognitionException {
		FuncReturnTypeContext _localctx = new FuncReturnTypeContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_funcReturnType);
		try {
			setState(348);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ANY:
			case STRING_TYPE:
			case BOOLEAN_TYPE:
			case MAP_TYPE:
			case OPEN_BRACKET:
				_localctx = new VariableReturnTypeContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(346);
				variableType();
				}
				break;
			case VOID_TYPE:
				_localctx = new VoidReturnTypeContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(347);
				match(VOID_TYPE);
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

	public static class FuncBodyContext extends ParserRuleContext {
		public TerminalNode OPEN_CURLY() { return getToken(PMLParser.OPEN_CURLY, 0); }
		public TerminalNode CLOSE_CURLY() { return getToken(PMLParser.CLOSE_CURLY, 0); }
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public FuncBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funcBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterFuncBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitFuncBody(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitFuncBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FuncBodyContext funcBody() throws RecognitionException {
		FuncBodyContext _localctx = new FuncBodyContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_funcBody);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(350);
			match(OPEN_CURLY);
			setState(354);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << CREATE) | (1L << DELETE) | (1L << BREAK) | (1L << CONTINUE) | (1L << SET_RESOURCE_ACCESS_RIGHTS) | (1L << ASSIGN) | (1L << DEASSIGN) | (1L << SET_PROPERTIES) | (1L << ASSOCIATE) | (1L << DISSOCIATE) | (1L << LET) | (1L << CONST) | (1L << FUNCTION) | (1L << RETURN) | (1L << FOREACH) | (1L << FOR) | (1L << IF) | (1L << ID))) != 0)) {
				{
				{
				setState(351);
				statement();
				}
				}
				setState(356);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(357);
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

	public static class ForeachStatementContext extends ParserRuleContext {
		public Token key;
		public Token mapValue;
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
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterForeachStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitForeachStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitForeachStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForeachStatementContext foreachStatement() throws RecognitionException {
		ForeachStatementContext _localctx = new ForeachStatementContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_foreachStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(359);
			match(FOREACH);
			setState(360);
			((ForeachStatementContext)_localctx).key = match(ID);
			setState(363);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(361);
				match(COMMA);
				setState(362);
				((ForeachStatementContext)_localctx).mapValue = match(ID);
				}
			}

			setState(365);
			match(IN);
			setState(366);
			expression(0);
			setState(367);
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

	public static class ForRangeStatementContext extends ParserRuleContext {
		public Token lowerBound;
		public ExpressionContext lower;
		public ExpressionContext upper;
		public Token upperBound;
		public TerminalNode FOR() { return getToken(PMLParser.FOR, 0); }
		public TerminalNode ID() { return getToken(PMLParser.ID, 0); }
		public TerminalNode IN_RANGE() { return getToken(PMLParser.IN_RANGE, 0); }
		public TerminalNode COMMA() { return getToken(PMLParser.COMMA, 0); }
		public StatementBlockContext statementBlock() {
			return getRuleContext(StatementBlockContext.class,0);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode OPEN_BRACKET() { return getToken(PMLParser.OPEN_BRACKET, 0); }
		public TerminalNode OPEN_PAREN() { return getToken(PMLParser.OPEN_PAREN, 0); }
		public TerminalNode CLOSE_BRACKET() { return getToken(PMLParser.CLOSE_BRACKET, 0); }
		public TerminalNode CLOSE_PAREN() { return getToken(PMLParser.CLOSE_PAREN, 0); }
		public ForRangeStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forRangeStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterForRangeStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitForRangeStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitForRangeStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForRangeStatementContext forRangeStatement() throws RecognitionException {
		ForRangeStatementContext _localctx = new ForRangeStatementContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_forRangeStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(369);
			match(FOR);
			setState(370);
			match(ID);
			setState(371);
			match(IN_RANGE);
			setState(372);
			((ForRangeStatementContext)_localctx).lowerBound = _input.LT(1);
			_la = _input.LA(1);
			if ( !(_la==OPEN_BRACKET || _la==OPEN_PAREN) ) {
				((ForRangeStatementContext)_localctx).lowerBound = (Token)_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(373);
			((ForRangeStatementContext)_localctx).lower = expression(0);
			setState(374);
			match(COMMA);
			setState(375);
			((ForRangeStatementContext)_localctx).upper = expression(0);
			setState(376);
			((ForRangeStatementContext)_localctx).upperBound = _input.LT(1);
			_la = _input.LA(1);
			if ( !(_la==CLOSE_BRACKET || _la==CLOSE_PAREN) ) {
				((ForRangeStatementContext)_localctx).upperBound = (Token)_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(377);
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

	public static class BreakStatementContext extends ParserRuleContext {
		public TerminalNode BREAK() { return getToken(PMLParser.BREAK, 0); }
		public BreakStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_breakStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterBreakStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitBreakStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitBreakStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BreakStatementContext breakStatement() throws RecognitionException {
		BreakStatementContext _localctx = new BreakStatementContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_breakStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(379);
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

	public static class ContinueStatementContext extends ParserRuleContext {
		public TerminalNode CONTINUE() { return getToken(PMLParser.CONTINUE, 0); }
		public ContinueStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_continueStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterContinueStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitContinueStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitContinueStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ContinueStatementContext continueStatement() throws RecognitionException {
		ContinueStatementContext _localctx = new ContinueStatementContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_continueStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(381);
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
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterFunctionInvokeStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitFunctionInvokeStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitFunctionInvokeStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionInvokeStatementContext functionInvokeStatement() throws RecognitionException {
		FunctionInvokeStatementContext _localctx = new FunctionInvokeStatementContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_functionInvokeStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(383);
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

	public static class IfStatementContext extends ParserRuleContext {
		public ExpressionContext condition;
		public TerminalNode IF() { return getToken(PMLParser.IF, 0); }
		public StatementBlockContext statementBlock() {
			return getRuleContext(StatementBlockContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode IS_COMPLEMENT() { return getToken(PMLParser.IS_COMPLEMENT, 0); }
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
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterIfStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitIfStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitIfStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IfStatementContext ifStatement() throws RecognitionException {
		IfStatementContext _localctx = new IfStatementContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_ifStatement);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(385);
			match(IF);
			setState(387);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IS_COMPLEMENT) {
				{
				setState(386);
				match(IS_COMPLEMENT);
				}
			}

			setState(389);
			((IfStatementContext)_localctx).condition = expression(0);
			setState(390);
			statementBlock();
			setState(394);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,21,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(391);
					elseIfStatement();
					}
					} 
				}
				setState(396);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,21,_ctx);
			}
			setState(398);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ELSE) {
				{
				setState(397);
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
		public TerminalNode IS_COMPLEMENT() { return getToken(PMLParser.IS_COMPLEMENT, 0); }
		public ElseIfStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elseIfStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterElseIfStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitElseIfStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitElseIfStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElseIfStatementContext elseIfStatement() throws RecognitionException {
		ElseIfStatementContext _localctx = new ElseIfStatementContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_elseIfStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(400);
			match(ELSE);
			setState(401);
			match(IF);
			setState(403);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IS_COMPLEMENT) {
				{
				setState(402);
				match(IS_COMPLEMENT);
				}
			}

			setState(405);
			((ElseIfStatementContext)_localctx).condition = expression(0);
			setState(406);
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
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterElseStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitElseStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitElseStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElseStatementContext elseStatement() throws RecognitionException {
		ElseStatementContext _localctx = new ElseStatementContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_elseStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(408);
			match(ELSE);
			setState(409);
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
	public static class MapVarTypeContext extends VariableTypeContext {
		public MapTypeContext mapType() {
			return getRuleContext(MapTypeContext.class,0);
		}
		public MapVarTypeContext(VariableTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterMapVarType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitMapVarType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitMapVarType(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class StringTypeContext extends VariableTypeContext {
		public TerminalNode STRING_TYPE() { return getToken(PMLParser.STRING_TYPE, 0); }
		public StringTypeContext(VariableTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterStringType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitStringType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitStringType(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ArrayVarTypeContext extends VariableTypeContext {
		public ArrayTypeContext arrayType() {
			return getRuleContext(ArrayTypeContext.class,0);
		}
		public ArrayVarTypeContext(VariableTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterArrayVarType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitArrayVarType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitArrayVarType(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BooleanTypeContext extends VariableTypeContext {
		public TerminalNode BOOLEAN_TYPE() { return getToken(PMLParser.BOOLEAN_TYPE, 0); }
		public BooleanTypeContext(VariableTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterBooleanType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitBooleanType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitBooleanType(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AnyTypeContext extends VariableTypeContext {
		public TerminalNode ANY() { return getToken(PMLParser.ANY, 0); }
		public AnyTypeContext(VariableTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterAnyType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitAnyType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitAnyType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableTypeContext variableType() throws RecognitionException {
		VariableTypeContext _localctx = new VariableTypeContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_variableType);
		try {
			setState(416);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STRING_TYPE:
				_localctx = new StringTypeContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(411);
				match(STRING_TYPE);
				}
				break;
			case BOOLEAN_TYPE:
				_localctx = new BooleanTypeContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(412);
				match(BOOLEAN_TYPE);
				}
				break;
			case OPEN_BRACKET:
				_localctx = new ArrayVarTypeContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(413);
				arrayType();
				}
				break;
			case MAP_TYPE:
				_localctx = new MapVarTypeContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(414);
				mapType();
				}
				break;
			case ANY:
				_localctx = new AnyTypeContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(415);
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

	public static class MapTypeContext extends ParserRuleContext {
		public VariableTypeContext keyType;
		public VariableTypeContext valueType;
		public TerminalNode MAP_TYPE() { return getToken(PMLParser.MAP_TYPE, 0); }
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
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterMapType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitMapType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitMapType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MapTypeContext mapType() throws RecognitionException {
		MapTypeContext _localctx = new MapTypeContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_mapType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(418);
			match(MAP_TYPE);
			setState(419);
			match(OPEN_BRACKET);
			setState(420);
			((MapTypeContext)_localctx).keyType = variableType();
			setState(421);
			match(CLOSE_BRACKET);
			setState(422);
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
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterArrayType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitArrayType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitArrayType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayTypeContext arrayType() throws RecognitionException {
		ArrayTypeContext _localctx = new ArrayTypeContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_arrayType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(424);
			match(OPEN_BRACKET);
			setState(425);
			match(CLOSE_BRACKET);
			setState(426);
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
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterStatementBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitStatementBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitStatementBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementBlockContext statementBlock() throws RecognitionException {
		StatementBlockContext _localctx = new StatementBlockContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_statementBlock);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(428);
			match(OPEN_CURLY);
			setState(432);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << CREATE) | (1L << DELETE) | (1L << BREAK) | (1L << CONTINUE) | (1L << SET_RESOURCE_ACCESS_RIGHTS) | (1L << ASSIGN) | (1L << DEASSIGN) | (1L << SET_PROPERTIES) | (1L << ASSOCIATE) | (1L << DISSOCIATE) | (1L << LET) | (1L << CONST) | (1L << FUNCTION) | (1L << RETURN) | (1L << FOREACH) | (1L << FOR) | (1L << IF) | (1L << ID))) != 0)) {
				{
				{
				setState(429);
				statement();
				}
				}
				setState(434);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(435);
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

	public static class ExpressionContext extends ParserRuleContext {
		public ExpressionContext left;
		public ExpressionContext right;
		public VariableReferenceContext variableReference() {
			return getRuleContext(VariableReferenceContext.class,0);
		}
		public FunctionInvokeContext functionInvoke() {
			return getRuleContext(FunctionInvokeContext.class,0);
		}
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode EQUALS_OP() { return getToken(PMLParser.EQUALS_OP, 0); }
		public TerminalNode NOT_EQUALS_OP() { return getToken(PMLParser.NOT_EQUALS_OP, 0); }
		public TerminalNode AND_OP() { return getToken(PMLParser.AND_OP, 0); }
		public TerminalNode OR_OP() { return getToken(PMLParser.OR_OP, 0); }
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitExpression(this);
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
		int _startState = 92;
		enterRecursionRule(_localctx, 92, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(441);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
			case 1:
				{
				setState(438);
				variableReference();
				}
				break;
			case 2:
				{
				setState(439);
				functionInvoke();
				}
				break;
			case 3:
				{
				setState(440);
				literal();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(451);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(449);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
					case 1:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.left = _prevctx;
						_localctx.left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(443);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(444);
						_la = _input.LA(1);
						if ( !(_la==EQUALS_OP || _la==NOT_EQUALS_OP) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(445);
						((ExpressionContext)_localctx).right = expression(3);
						}
						break;
					case 2:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.left = _prevctx;
						_localctx.left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(446);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(447);
						_la = _input.LA(1);
						if ( !(_la==AND_OP || _la==OR_OP) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(448);
						((ExpressionContext)_localctx).right = expression(2);
						}
						break;
					}
					} 
				}
				setState(453);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
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

	public static class ArrayContext extends ParserRuleContext {
		public TerminalNode OPEN_BRACKET() { return getToken(PMLParser.OPEN_BRACKET, 0); }
		public TerminalNode CLOSE_BRACKET() { return getToken(PMLParser.CLOSE_BRACKET, 0); }
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
		public ArrayContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_array; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterArray(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitArray(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitArray(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayContext array() throws RecognitionException {
		ArrayContext _localctx = new ArrayContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_array);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(454);
			match(OPEN_BRACKET);
			setState(463);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 44)) & ~0x3f) == 0 && ((1L << (_la - 44)) & ((1L << (BOOLEAN - 44)) | (1L << (NUMBER - 44)) | (1L << (ID - 44)) | (1L << (STRING - 44)) | (1L << (OPEN_CURLY - 44)) | (1L << (OPEN_BRACKET - 44)))) != 0)) {
				{
				setState(455);
				expression(0);
				setState(460);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(456);
					match(COMMA);
					setState(457);
					expression(0);
					}
					}
					setState(462);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(465);
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

	public static class MapContext extends ParserRuleContext {
		public TerminalNode OPEN_CURLY() { return getToken(PMLParser.OPEN_CURLY, 0); }
		public TerminalNode CLOSE_CURLY() { return getToken(PMLParser.CLOSE_CURLY, 0); }
		public List<MapEntryContext> mapEntry() {
			return getRuleContexts(MapEntryContext.class);
		}
		public MapEntryContext mapEntry(int i) {
			return getRuleContext(MapEntryContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(PMLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(PMLParser.COMMA, i);
		}
		public MapContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_map; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterMap(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitMap(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitMap(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MapContext map() throws RecognitionException {
		MapContext _localctx = new MapContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_map);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(467);
			match(OPEN_CURLY);
			setState(476);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 44)) & ~0x3f) == 0 && ((1L << (_la - 44)) & ((1L << (BOOLEAN - 44)) | (1L << (NUMBER - 44)) | (1L << (ID - 44)) | (1L << (STRING - 44)) | (1L << (OPEN_CURLY - 44)) | (1L << (OPEN_BRACKET - 44)))) != 0)) {
				{
				setState(468);
				mapEntry();
				setState(473);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(469);
					match(COMMA);
					setState(470);
					mapEntry();
					}
					}
					setState(475);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(478);
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

	public static class MapEntryContext extends ParserRuleContext {
		public ExpressionContext key;
		public ExpressionContext value;
		public TerminalNode COLON() { return getToken(PMLParser.COLON, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public MapEntryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mapEntry; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterMapEntry(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitMapEntry(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitMapEntry(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MapEntryContext mapEntry() throws RecognitionException {
		MapEntryContext _localctx = new MapEntryContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_mapEntry);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(480);
			((MapEntryContext)_localctx).key = expression(0);
			setState(481);
			match(COLON);
			setState(482);
			((MapEntryContext)_localctx).value = expression(0);
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

	public static class EntryReferenceContext extends ParserRuleContext {
		public ExpressionContext key;
		public TerminalNode ID() { return getToken(PMLParser.ID, 0); }
		public List<TerminalNode> OPEN_BRACKET() { return getTokens(PMLParser.OPEN_BRACKET); }
		public TerminalNode OPEN_BRACKET(int i) {
			return getToken(PMLParser.OPEN_BRACKET, i);
		}
		public List<TerminalNode> CLOSE_BRACKET() { return getTokens(PMLParser.CLOSE_BRACKET); }
		public TerminalNode CLOSE_BRACKET(int i) {
			return getToken(PMLParser.CLOSE_BRACKET, i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public EntryReferenceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_entryReference; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterEntryReference(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitEntryReference(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitEntryReference(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EntryReferenceContext entryReference() throws RecognitionException {
		EntryReferenceContext _localctx = new EntryReferenceContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_entryReference);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(484);
			match(ID);
			setState(489); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(485);
					match(OPEN_BRACKET);
					setState(486);
					((EntryReferenceContext)_localctx).key = expression(0);
					setState(487);
					match(CLOSE_BRACKET);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(491); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,33,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
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
	public static class MapLiteralContext extends LiteralContext {
		public MapContext map() {
			return getRuleContext(MapContext.class,0);
		}
		public MapLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterMapLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitMapLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitMapLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class StringLiteralContext extends LiteralContext {
		public TerminalNode STRING() { return getToken(PMLParser.STRING, 0); }
		public StringLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterStringLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitStringLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitStringLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BooleanLiteralContext extends LiteralContext {
		public TerminalNode BOOLEAN() { return getToken(PMLParser.BOOLEAN, 0); }
		public BooleanLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterBooleanLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitBooleanLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitBooleanLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ArrayLiteralContext extends LiteralContext {
		public ArrayContext array() {
			return getRuleContext(ArrayContext.class,0);
		}
		public ArrayLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterArrayLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitArrayLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitArrayLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NumberLiteralContext extends LiteralContext {
		public TerminalNode NUMBER() { return getToken(PMLParser.NUMBER, 0); }
		public NumberLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterNumberLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitNumberLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitNumberLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_literal);
		try {
			setState(498);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STRING:
				_localctx = new StringLiteralContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(493);
				match(STRING);
				}
				break;
			case BOOLEAN:
				_localctx = new BooleanLiteralContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(494);
				match(BOOLEAN);
				}
				break;
			case NUMBER:
				_localctx = new NumberLiteralContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(495);
				match(NUMBER);
				}
				break;
			case OPEN_BRACKET:
				_localctx = new ArrayLiteralContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(496);
				array();
				}
				break;
			case OPEN_CURLY:
				_localctx = new MapLiteralContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(497);
				map();
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
	public static class ReferenceByEntryContext extends VariableReferenceContext {
		public EntryReferenceContext entryReference() {
			return getRuleContext(EntryReferenceContext.class,0);
		}
		public ReferenceByEntryContext(VariableReferenceContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterReferenceByEntry(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitReferenceByEntry(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitReferenceByEntry(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ReferenceByIDContext extends VariableReferenceContext {
		public TerminalNode ID() { return getToken(PMLParser.ID, 0); }
		public ReferenceByIDContext(VariableReferenceContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterReferenceByID(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitReferenceByID(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitReferenceByID(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableReferenceContext variableReference() throws RecognitionException {
		VariableReferenceContext _localctx = new VariableReferenceContext(_ctx, getState());
		enterRule(_localctx, 104, RULE_variableReference);
		try {
			setState(502);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
			case 1:
				_localctx = new ReferenceByIDContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(500);
				match(ID);
				}
				break;
			case 2:
				_localctx = new ReferenceByEntryContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(501);
				entryReference();
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
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterFunctionInvoke(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitFunctionInvoke(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitFunctionInvoke(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionInvokeContext functionInvoke() throws RecognitionException {
		FunctionInvokeContext _localctx = new FunctionInvokeContext(_ctx, getState());
		enterRule(_localctx, 106, RULE_functionInvoke);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(504);
			match(ID);
			setState(505);
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

	public static class FunctionInvokeArgsContext extends ParserRuleContext {
		public TerminalNode OPEN_PAREN() { return getToken(PMLParser.OPEN_PAREN, 0); }
		public TerminalNode CLOSE_PAREN() { return getToken(PMLParser.CLOSE_PAREN, 0); }
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
		public FunctionInvokeArgsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionInvokeArgs; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).enterFunctionInvokeArgs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PMLListener ) ((PMLListener)listener).exitFunctionInvokeArgs(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PMLVisitor ) return ((PMLVisitor<? extends T>)visitor).visitFunctionInvokeArgs(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionInvokeArgsContext functionInvokeArgs() throws RecognitionException {
		FunctionInvokeArgsContext _localctx = new FunctionInvokeArgsContext(_ctx, getState());
		enterRule(_localctx, 108, RULE_functionInvokeArgs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(507);
			match(OPEN_PAREN);
			setState(516);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 44)) & ~0x3f) == 0 && ((1L << (_la - 44)) & ((1L << (BOOLEAN - 44)) | (1L << (NUMBER - 44)) | (1L << (ID - 44)) | (1L << (STRING - 44)) | (1L << (OPEN_CURLY - 44)) | (1L << (OPEN_BRACKET - 44)))) != 0)) {
				{
				setState(508);
				expression(0);
				setState(513);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(509);
					match(COMMA);
					setState(510);
					expression(0);
					}
					}
					setState(515);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(518);
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
		case 46:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 2);
		case 1:
			return precpred(_ctx, 1);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3R\u020b\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\3\2\7\2r\n\2\f\2\16\2u\13\2\3"+
		"\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u008f\n\3\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5"+
		"\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\7\7\u00a6\n\7\f\7"+
		"\16\7\u00a9\13\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\5\b\u00b6"+
		"\n\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\5\t\u00c3\n\t\3\n\3\n"+
		"\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\5\n\u00cf\n\n\3\13\3\13\3\13\3\f\3\f"+
		"\3\f\3\f\3\f\3\f\3\r\3\r\7\r\u00dc\n\r\f\r\16\r\u00df\13\r\3\r\3\r\3\16"+
		"\3\16\3\16\5\16\u00e6\n\16\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17"+
		"\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\7\20\u00f9\n\20\f\20\16\20\u00fc"+
		"\13\20\5\20\u00fe\n\20\3\20\3\20\3\21\5\21\u0103\n\21\3\21\3\21\3\22\3"+
		"\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3"+
		"\24\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3\27\3"+
		"\27\3\27\3\30\3\30\3\30\3\30\3\31\3\31\3\31\5\31\u012d\n\31\3\32\3\32"+
		"\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\34\5\34\u0139\n\34\3\34\3\34\3\34"+
		"\3\34\3\35\3\35\3\35\3\35\3\35\3\35\5\35\u0145\n\35\3\35\3\35\3\36\3\36"+
		"\3\36\7\36\u014c\n\36\f\36\16\36\u014f\13\36\5\36\u0151\n\36\3\37\3\37"+
		"\3\37\3 \3 \3!\3!\3!\5!\u015b\n!\3\"\3\"\5\"\u015f\n\"\3#\3#\7#\u0163"+
		"\n#\f#\16#\u0166\13#\3#\3#\3$\3$\3$\3$\5$\u016e\n$\3$\3$\3$\3$\3%\3%\3"+
		"%\3%\3%\3%\3%\3%\3%\3%\3&\3&\3\'\3\'\3(\3(\3)\3)\5)\u0186\n)\3)\3)\3)"+
		"\7)\u018b\n)\f)\16)\u018e\13)\3)\5)\u0191\n)\3*\3*\3*\5*\u0196\n*\3*\3"+
		"*\3*\3+\3+\3+\3,\3,\3,\3,\3,\5,\u01a3\n,\3-\3-\3-\3-\3-\3-\3.\3.\3.\3"+
		".\3/\3/\7/\u01b1\n/\f/\16/\u01b4\13/\3/\3/\3\60\3\60\3\60\3\60\5\60\u01bc"+
		"\n\60\3\60\3\60\3\60\3\60\3\60\3\60\7\60\u01c4\n\60\f\60\16\60\u01c7\13"+
		"\60\3\61\3\61\3\61\3\61\7\61\u01cd\n\61\f\61\16\61\u01d0\13\61\5\61\u01d2"+
		"\n\61\3\61\3\61\3\62\3\62\3\62\3\62\7\62\u01da\n\62\f\62\16\62\u01dd\13"+
		"\62\5\62\u01df\n\62\3\62\3\62\3\63\3\63\3\63\3\63\3\64\3\64\3\64\3\64"+
		"\3\64\6\64\u01ec\n\64\r\64\16\64\u01ed\3\65\3\65\3\65\3\65\3\65\5\65\u01f5"+
		"\n\65\3\66\3\66\5\66\u01f9\n\66\3\67\3\67\3\67\38\38\38\38\78\u0202\n"+
		"8\f8\168\u0205\138\58\u0207\n8\38\38\38\2\3^9\2\4\6\b\n\f\16\20\22\24"+
		"\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNPRTVXZ\\^`bdfhjln\2\f"+
		"\3\2$%\3\2&\'\5\2\21\21%%\'\'\3\2\22\23\3\2#\'\3\2*+\4\2GGKK\4\2HHLL\3"+
		"\2QR\3\2OP\2\u021b\2s\3\2\2\2\4\u008e\3\2\2\2\6\u0090\3\2\2\2\b\u0094"+
		"\3\2\2\2\n\u009a\3\2\2\2\f\u00a0\3\2\2\2\16\u00ac\3\2\2\2\20\u00c2\3\2"+
		"\2\2\22\u00ce\3\2\2\2\24\u00d0\3\2\2\2\26\u00d3\3\2\2\2\30\u00d9\3\2\2"+
		"\2\32\u00e5\3\2\2\2\34\u00e7\3\2\2\2\36\u00f4\3\2\2\2 \u0102\3\2\2\2\""+
		"\u0106\3\2\2\2$\u010c\3\2\2\2&\u0111\3\2\2\2(\u0116\3\2\2\2*\u011d\3\2"+
		"\2\2,\u0122\3\2\2\2.\u0125\3\2\2\2\60\u012c\3\2\2\2\62\u012e\3\2\2\2\64"+
		"\u0130\3\2\2\2\66\u0138\3\2\2\28\u013e\3\2\2\2:\u0150\3\2\2\2<\u0152\3"+
		"\2\2\2>\u0155\3\2\2\2@\u015a\3\2\2\2B\u015e\3\2\2\2D\u0160\3\2\2\2F\u0169"+
		"\3\2\2\2H\u0173\3\2\2\2J\u017d\3\2\2\2L\u017f\3\2\2\2N\u0181\3\2\2\2P"+
		"\u0183\3\2\2\2R\u0192\3\2\2\2T\u019a\3\2\2\2V\u01a2\3\2\2\2X\u01a4\3\2"+
		"\2\2Z\u01aa\3\2\2\2\\\u01ae\3\2\2\2^\u01bb\3\2\2\2`\u01c8\3\2\2\2b\u01d5"+
		"\3\2\2\2d\u01e2\3\2\2\2f\u01e6\3\2\2\2h\u01f4\3\2\2\2j\u01f8\3\2\2\2l"+
		"\u01fa\3\2\2\2n\u01fd\3\2\2\2pr\5\4\3\2qp\3\2\2\2ru\3\2\2\2sq\3\2\2\2"+
		"st\3\2\2\2tv\3\2\2\2us\3\2\2\2vw\7\2\2\3w\3\3\2\2\2x\u008f\5\6\4\2y\u008f"+
		"\5\b\5\2z\u008f\5\n\6\2{\u008f\5\f\7\2|\u008f\5\34\17\2}\u008f\5\"\22"+
		"\2~\u008f\5$\23\2\177\u008f\5&\24\2\u0080\u008f\5(\25\2\u0081\u008f\5"+
		"*\26\2\u0082\u008f\5,\27\2\u0083\u008f\5.\30\2\u0084\u008f\5\64\33\2\u0085"+
		"\u008f\5\66\34\2\u0086\u008f\58\35\2\u0087\u008f\5@!\2\u0088\u008f\5F"+
		"$\2\u0089\u008f\5H%\2\u008a\u008f\5J&\2\u008b\u008f\5L\'\2\u008c\u008f"+
		"\5N(\2\u008d\u008f\5P)\2\u008ex\3\2\2\2\u008ey\3\2\2\2\u008ez\3\2\2\2"+
		"\u008e{\3\2\2\2\u008e|\3\2\2\2\u008e}\3\2\2\2\u008e~\3\2\2\2\u008e\177"+
		"\3\2\2\2\u008e\u0080\3\2\2\2\u008e\u0081\3\2\2\2\u008e\u0082\3\2\2\2\u008e"+
		"\u0083\3\2\2\2\u008e\u0084\3\2\2\2\u008e\u0085\3\2\2\2\u008e\u0086\3\2"+
		"\2\2\u008e\u0087\3\2\2\2\u008e\u0088\3\2\2\2\u008e\u0089\3\2\2\2\u008e"+
		"\u008a\3\2\2\2\u008e\u008b\3\2\2\2\u008e\u008c\3\2\2\2\u008e\u008d\3\2"+
		"\2\2\u008f\5\3\2\2\2\u0090\u0091\7\3\2\2\u0091\u0092\7#\2\2\u0092\u0093"+
		"\5^\60\2\u0093\7\3\2\2\2\u0094\u0095\7\3\2\2\u0095\u0096\t\2\2\2\u0096"+
		"\u0097\5^\60\2\u0097\u0098\78\2\2\u0098\u0099\5^\60\2\u0099\t\3\2\2\2"+
		"\u009a\u009b\7\3\2\2\u009b\u009c\t\3\2\2\u009c\u009d\5^\60\2\u009d\u009e"+
		"\78\2\2\u009e\u009f\5^\60\2\u009f\13\3\2\2\2\u00a0\u00a1\7\3\2\2\u00a1"+
		"\u00a2\7!\2\2\u00a2\u00a3\5^\60\2\u00a3\u00a7\7E\2\2\u00a4\u00a6\5\16"+
		"\b\2\u00a5\u00a4\3\2\2\2\u00a6\u00a9\3\2\2\2\u00a7\u00a5\3\2\2\2\u00a7"+
		"\u00a8\3\2\2\2\u00a8\u00aa\3\2\2\2\u00a9\u00a7\3\2\2\2\u00aa\u00ab\7F"+
		"\2\2\u00ab\r\3\2\2\2\u00ac\u00ad\7\3\2\2\u00ad\u00ae\7\b\2\2\u00ae\u00af"+
		"\5^\60\2\u00af\u00b0\7\t\2\2\u00b0\u00b1\5\20\t\2\u00b1\u00b2\7\n\2\2"+
		"\u00b2\u00b5\5^\60\2\u00b3\u00b4\7\f\2\2\u00b4\u00b6\5\22\n\2\u00b5\u00b3"+
		"\3\2\2\2\u00b5\u00b6\3\2\2\2\u00b6\u00b7\3\2\2\2\u00b7\u00b8\5\26\f\2"+
		"\u00b8\17\3\2\2\2\u00b9\u00c3\7\16\2\2\u00ba\u00bb\7\'\2\2\u00bb\u00c3"+
		"\5^\60\2\u00bc\u00bd\7\17\2\2\u00bd\u00c3\5^\60\2\u00be\u00bf\7\20\2\2"+
		"\u00bf\u00c3\5^\60\2\u00c0\u00c1\7\21\2\2\u00c1\u00c3\5^\60\2\u00c2\u00b9"+
		"\3\2\2\2\u00c2\u00ba\3\2\2\2\u00c2\u00bc\3\2\2\2\u00c2\u00be\3\2\2\2\u00c2"+
		"\u00c0\3\2\2\2\u00c3\21\3\2\2\2\u00c4\u00cf\5^\60\2\u00c5\u00cf\5\24\13"+
		"\2\u00c6\u00c7\5\24\13\2\u00c7\u00c8\78\2\2\u00c8\u00c9\5^\60\2\u00c9"+
		"\u00cf\3\2\2\2\u00ca\u00cb\5\24\13\2\u00cb\u00cc\7\31\2\2\u00cc\u00cd"+
		"\5^\60\2\u00cd\u00cf\3\2\2\2\u00ce\u00c4\3\2\2\2\u00ce\u00c5\3\2\2\2\u00ce"+
		"\u00c6\3\2\2\2\u00ce\u00ca\3\2\2\2\u00cf\23\3\2\2\2\u00d0\u00d1\7)\2\2"+
		"\u00d1\u00d2\7\7\2\2\u00d2\25\3\2\2\2\u00d3\u00d4\7\r\2\2\u00d4\u00d5"+
		"\7K\2\2\u00d5\u00d6\7=\2\2\u00d6\u00d7\7L\2\2\u00d7\u00d8\5\30\r\2\u00d8"+
		"\27\3\2\2\2\u00d9\u00dd\7E\2\2\u00da\u00dc\5\32\16\2\u00db\u00da\3\2\2"+
		"\2\u00dc\u00df\3\2\2\2\u00dd\u00db\3\2\2\2\u00dd\u00de\3\2\2\2\u00de\u00e0"+
		"\3\2\2\2\u00df\u00dd\3\2\2\2\u00e0\u00e1\7F\2\2\u00e1\31\3\2\2\2\u00e2"+
		"\u00e6\5\4\3\2\u00e3\u00e6\5\16\b\2\u00e4\u00e6\5\64\33\2\u00e5\u00e2"+
		"\3\2\2\2\u00e5\u00e3\3\2\2\2\u00e5\u00e4\3\2\2\2\u00e6\33\3\2\2\2\u00e7"+
		"\u00e8\7\3\2\2\u00e8\u00e9\7 \2\2\u00e9\u00ea\5^\60\2\u00ea\u00eb\7\37"+
		"\2\2\u00eb\u00ec\t\4\2\2\u00ec\u00ed\5^\60\2\u00ed\u00ee\7\"\2\2\u00ee"+
		"\u00ef\5^\60\2\u00ef\u00f0\7\f\2\2\u00f0\u00f1\t\5\2\2\u00f1\u00f2\7\31"+
		"\2\2\u00f2\u00f3\5\36\20\2\u00f3\35\3\2\2\2\u00f4\u00fd\7G\2\2\u00f5\u00fa"+
		"\5 \21\2\u00f6\u00f7\7C\2\2\u00f7\u00f9\5 \21\2\u00f8\u00f6\3\2\2\2\u00f9"+
		"\u00fc\3\2\2\2\u00fa\u00f8\3\2\2\2\u00fa\u00fb\3\2\2\2\u00fb\u00fe\3\2"+
		"\2\2\u00fc\u00fa\3\2\2\2\u00fd\u00f5\3\2\2\2\u00fd\u00fe\3\2\2\2\u00fe"+
		"\u00ff\3\2\2\2\u00ff\u0100\7H\2\2\u0100\37\3\2\2\2\u0101\u0103\7M\2\2"+
		"\u0102\u0101\3\2\2\2\u0102\u0103\3\2\2\2\u0103\u0104\3\2\2\2\u0104\u0105"+
		"\5^\60\2\u0105!\3\2\2\2\u0106\u0107\7\30\2\2\u0107\u0108\7\31\2\2\u0108"+
		"\u0109\5^\60\2\u0109\u010a\7\32\2\2\u010a\u010b\5^\60\2\u010b#\3\2\2\2"+
		"\u010c\u010d\7\25\2\2\u010d\u010e\5^\60\2\u010e\u010f\7\32\2\2\u010f\u0110"+
		"\5^\60\2\u0110%\3\2\2\2\u0111\u0112\7\26\2\2\u0112\u0113\5^\60\2\u0113"+
		"\u0114\7\27\2\2\u0114\u0115\5^\60\2\u0115\'\3\2\2\2\u0116\u0117\7\33\2"+
		"\2\u0117\u0118\5^\60\2\u0118\u0119\7\34\2\2\u0119\u011a\5^\60\2\u011a"+
		"\u011b\7\35\2\2\u011b\u011c\5^\60\2\u011c)\3\2\2\2\u011d\u011e\7\36\2"+
		"\2\u011e\u011f\5^\60\2\u011f\u0120\7\34\2\2\u0120\u0121\5^\60\2\u0121"+
		"+\3\2\2\2\u0122\u0123\7\24\2\2\u0123\u0124\5^\60\2\u0124-\3\2\2\2\u0125"+
		"\u0126\7\4\2\2\u0126\u0127\5\60\31\2\u0127\u0128\5^\60\2\u0128/\3\2\2"+
		"\2\u0129\u012d\5\62\32\2\u012a\u012d\7!\2\2\u012b\u012d\7 \2\2\u012c\u0129"+
		"\3\2\2\2\u012c\u012a\3\2\2\2\u012c\u012b\3\2\2\2\u012d\61\3\2\2\2\u012e"+
		"\u012f\t\6\2\2\u012f\63\3\2\2\2\u0130\u0131\7\4\2\2\u0131\u0132\7\b\2"+
		"\2\u0132\u0133\5^\60\2\u0133\u0134\7\27\2\2\u0134\u0135\7!\2\2\u0135\u0136"+
		"\5^\60\2\u0136\65\3\2\2\2\u0137\u0139\t\7\2\2\u0138\u0137\3\2\2\2\u0138"+
		"\u0139\3\2\2\2\u0139\u013a\3\2\2\2\u013a\u013b\7=\2\2\u013b\u013c\7N\2"+
		"\2\u013c\u013d\5^\60\2\u013d\67\3\2\2\2\u013e\u013f\7,\2\2\u013f\u0140"+
		"\7=\2\2\u0140\u0141\7K\2\2\u0141\u0142\5:\36\2\u0142\u0144\7L\2\2\u0143"+
		"\u0145\5B\"\2\u0144\u0143\3\2\2\2\u0144\u0145\3\2\2\2\u0145\u0146\3\2"+
		"\2\2\u0146\u0147\5D#\2\u01479\3\2\2\2\u0148\u014d\5<\37\2\u0149\u014a"+
		"\7C\2\2\u014a\u014c\5<\37\2\u014b\u0149\3\2\2\2\u014c\u014f\3\2\2\2\u014d"+
		"\u014b\3\2\2\2\u014d\u014e\3\2\2\2\u014e\u0151\3\2\2\2\u014f\u014d\3\2"+
		"\2\2\u0150\u0148\3\2\2\2\u0150\u0151\3\2\2\2\u0151;\3\2\2\2\u0152\u0153"+
		"\5> \2\u0153\u0154\7=\2\2\u0154=\3\2\2\2\u0155\u0156\5V,\2\u0156?\3\2"+
		"\2\2\u0157\u0158\7-\2\2\u0158\u015b\5^\60\2\u0159\u015b\7-\2\2\u015a\u0157"+
		"\3\2\2\2\u015a\u0159\3\2\2\2\u015bA\3\2\2\2\u015c\u015f\5V,\2\u015d\u015f"+
		"\7\63\2\2\u015e\u015c\3\2\2\2\u015e\u015d\3\2\2\2\u015fC\3\2\2\2\u0160"+
		"\u0164\7E\2\2\u0161\u0163\5\4\3\2\u0162\u0161\3\2\2\2\u0163\u0166\3\2"+
		"\2\2\u0164\u0162\3\2\2\2\u0164\u0165\3\2\2\2\u0165\u0167\3\2\2\2\u0166"+
		"\u0164\3\2\2\2\u0167\u0168\7F\2\2\u0168E\3\2\2\2\u0169\u016a\7\66\2\2"+
		"\u016a\u016d\7=\2\2\u016b\u016c\7C\2\2\u016c\u016e\7=\2\2\u016d\u016b"+
		"\3\2\2\2\u016d\u016e\3\2\2\2\u016e\u016f\3\2\2\2\u016f\u0170\78\2\2\u0170"+
		"\u0171\5^\60\2\u0171\u0172\5\\/\2\u0172G\3\2\2\2\u0173\u0174\7\67\2\2"+
		"\u0174\u0175\7=\2\2\u0175\u0176\7;\2\2\u0176\u0177\t\b\2\2\u0177\u0178"+
		"\5^\60\2\u0178\u0179\7C\2\2\u0179\u017a\5^\60\2\u017a\u017b\t\t\2\2\u017b"+
		"\u017c\5\\/\2\u017cI\3\2\2\2\u017d\u017e\7\5\2\2\u017eK\3\2\2\2\u017f"+
		"\u0180\7\6\2\2\u0180M\3\2\2\2\u0181\u0182\5l\67\2\u0182O\3\2\2\2\u0183"+
		"\u0185\79\2\2\u0184\u0186\7M\2\2\u0185\u0184\3\2\2\2\u0185\u0186\3\2\2"+
		"\2\u0186\u0187\3\2\2\2\u0187\u0188\5^\60\2\u0188\u018c\5\\/\2\u0189\u018b"+
		"\5R*\2\u018a\u0189\3\2\2\2\u018b\u018e\3\2\2\2\u018c\u018a\3\2\2\2\u018c"+
		"\u018d\3\2\2\2\u018d\u0190\3\2\2\2\u018e\u018c\3\2\2\2\u018f\u0191\5T"+
		"+\2\u0190\u018f\3\2\2\2\u0190\u0191\3\2\2\2\u0191Q\3\2\2\2\u0192\u0193"+
		"\7:\2\2\u0193\u0195\79\2\2\u0194\u0196\7M\2\2\u0195\u0194\3\2\2\2\u0195"+
		"\u0196\3\2\2\2\u0196\u0197\3\2\2\2\u0197\u0198\5^\60\2\u0198\u0199\5\\"+
		"/\2\u0199S\3\2\2\2\u019a\u019b\7:\2\2\u019b\u019c\5\\/\2\u019cU\3\2\2"+
		"\2\u019d\u01a3\7\61\2\2\u019e\u01a3\7\62\2\2\u019f\u01a3\5Z.\2\u01a0\u01a3"+
		"\5X-\2\u01a1\u01a3\7)\2\2\u01a2\u019d\3\2\2\2\u01a2\u019e\3\2\2\2\u01a2"+
		"\u019f\3\2\2\2\u01a2\u01a0\3\2\2\2\u01a2\u01a1\3\2\2\2\u01a3W\3\2\2\2"+
		"\u01a4\u01a5\7\65\2\2\u01a5\u01a6\7G\2\2\u01a6\u01a7\5V,\2\u01a7\u01a8"+
		"\7H\2\2\u01a8\u01a9\5V,\2\u01a9Y\3\2\2\2\u01aa\u01ab\7G\2\2\u01ab\u01ac"+
		"\7H\2\2\u01ac\u01ad\5V,\2\u01ad[\3\2\2\2\u01ae\u01b2\7E\2\2\u01af\u01b1"+
		"\5\4\3\2\u01b0\u01af\3\2\2\2\u01b1\u01b4\3\2\2\2\u01b2\u01b0\3\2\2\2\u01b2"+
		"\u01b3\3\2\2\2\u01b3\u01b5\3\2\2\2\u01b4\u01b2\3\2\2\2\u01b5\u01b6\7F"+
		"\2\2\u01b6]\3\2\2\2\u01b7\u01b8\b\60\1\2\u01b8\u01bc\5j\66\2\u01b9\u01bc"+
		"\5l\67\2\u01ba\u01bc\5h\65\2\u01bb\u01b7\3\2\2\2\u01bb\u01b9\3\2\2\2\u01bb"+
		"\u01ba\3\2\2\2\u01bc\u01c5\3\2\2\2\u01bd\u01be\f\4\2\2\u01be\u01bf\t\n"+
		"\2\2\u01bf\u01c4\5^\60\5\u01c0\u01c1\f\3\2\2\u01c1\u01c2\t\13\2\2\u01c2"+
		"\u01c4\5^\60\4\u01c3\u01bd\3\2\2\2\u01c3\u01c0\3\2\2\2\u01c4\u01c7\3\2"+
		"\2\2\u01c5\u01c3\3\2\2\2\u01c5\u01c6\3\2\2\2\u01c6_\3\2\2\2\u01c7\u01c5"+
		"\3\2\2\2\u01c8\u01d1\7G\2\2\u01c9\u01ce\5^\60\2\u01ca\u01cb\7C\2\2\u01cb"+
		"\u01cd\5^\60\2\u01cc\u01ca\3\2\2\2\u01cd\u01d0\3\2\2\2\u01ce\u01cc\3\2"+
		"\2\2\u01ce\u01cf\3\2\2\2\u01cf\u01d2\3\2\2\2\u01d0\u01ce\3\2\2\2\u01d1"+
		"\u01c9\3\2\2\2\u01d1\u01d2\3\2\2\2\u01d2\u01d3\3\2\2\2\u01d3\u01d4\7H"+
		"\2\2\u01d4a\3\2\2\2\u01d5\u01de\7E\2\2\u01d6\u01db\5d\63\2\u01d7\u01d8"+
		"\7C\2\2\u01d8\u01da\5d\63\2\u01d9\u01d7\3\2\2\2\u01da\u01dd\3\2\2\2\u01db"+
		"\u01d9\3\2\2\2\u01db\u01dc\3\2\2\2\u01dc\u01df\3\2\2\2\u01dd\u01db\3\2"+
		"\2\2\u01de\u01d6\3\2\2\2\u01de\u01df\3\2\2\2\u01df\u01e0\3\2\2\2\u01e0"+
		"\u01e1\7F\2\2\u01e1c\3\2\2\2\u01e2\u01e3\5^\60\2\u01e3\u01e4\7D\2\2\u01e4"+
		"\u01e5\5^\60\2\u01e5e\3\2\2\2\u01e6\u01eb\7=\2\2\u01e7\u01e8\7G\2\2\u01e8"+
		"\u01e9\5^\60\2\u01e9\u01ea\7H\2\2\u01ea\u01ec\3\2\2\2\u01eb\u01e7\3\2"+
		"\2\2\u01ec\u01ed\3\2\2\2\u01ed\u01eb\3\2\2\2\u01ed\u01ee\3\2\2\2\u01ee"+
		"g\3\2\2\2\u01ef\u01f5\7>\2\2\u01f0\u01f5\7.\2\2\u01f1\u01f5\7<\2\2\u01f2"+
		"\u01f5\5`\61\2\u01f3\u01f5\5b\62\2\u01f4\u01ef\3\2\2\2\u01f4\u01f0\3\2"+
		"\2\2\u01f4\u01f1\3\2\2\2\u01f4\u01f2\3\2\2\2\u01f4\u01f3\3\2\2\2\u01f5"+
		"i\3\2\2\2\u01f6\u01f9\7=\2\2\u01f7\u01f9\5f\64\2\u01f8\u01f6\3\2\2\2\u01f8"+
		"\u01f7\3\2\2\2\u01f9k\3\2\2\2\u01fa\u01fb\7=\2\2\u01fb\u01fc\5n8\2\u01fc"+
		"m\3\2\2\2\u01fd\u0206\7K\2\2\u01fe\u0203\5^\60\2\u01ff\u0200\7C\2\2\u0200"+
		"\u0202\5^\60\2\u0201\u01ff\3\2\2\2\u0202\u0205\3\2\2\2\u0203\u0201\3\2"+
		"\2\2\u0203\u0204\3\2\2\2\u0204\u0207\3\2\2\2\u0205\u0203\3\2\2\2\u0206"+
		"\u01fe\3\2\2\2\u0206\u0207\3\2\2\2\u0207\u0208\3\2\2\2\u0208\u0209\7L"+
		"\2\2\u0209o\3\2\2\2(s\u008e\u00a7\u00b5\u00c2\u00ce\u00dd\u00e5\u00fa"+
		"\u00fd\u0102\u012c\u0138\u0144\u014d\u0150\u015a\u015e\u0164\u016d\u0185"+
		"\u018c\u0190\u0195\u01a2\u01b2\u01bb\u01c3\u01c5\u01ce\u01d1\u01db\u01de"+
		"\u01ed\u01f4\u01f8\u0203\u0206";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}