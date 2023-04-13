// Generated from PAL.g4 by ANTLR 4.8
package gov.nist.csd.pm.policy.author.pal.antlr;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class PALParser extends Parser {
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
		IN_RANGE=57, NUMBER=58, VARIABLE_OR_FUNCTION_NAME=59, STRING=60, DOUBLE_QUOTE_STRING=61, 
		SINGLE_QUOTE_STRING=62, LINE_COMMENT=63, WS=64, COMMA=65, COLON=66, SEMI_COLON=67, 
		OPEN_CURLY=68, CLOSE_CURLY=69, OPEN_BRACKET=70, CLOSE_BRACKET=71, OPEN_ANGLE_BRACKET=72, 
		CLOSE_ANGLE_BRACKET=73, OPEN_PAREN=74, CLOSE_PAREN=75, IS_COMPLEMENT=76, 
		EQUALS=77;
	public static final int
		RULE_pal = 0, RULE_stmt = 1, RULE_varStmt = 2, RULE_funcDefStmt = 3, RULE_formalArgList = 4, 
		RULE_formalArg = 5, RULE_formalArgType = 6, RULE_funcReturnStmt = 7, RULE_funcReturnType = 8, 
		RULE_funcBody = 9, RULE_foreachStmt = 10, RULE_forRangeStmt = 11, RULE_breakStmt = 12, 
		RULE_continueStmt = 13, RULE_funcCallStmt = 14, RULE_ifStmt = 15, RULE_elseIfStmt = 16, 
		RULE_elseStmt = 17, RULE_varType = 18, RULE_mapType = 19, RULE_arrayType = 20, 
		RULE_stmtBlock = 21, RULE_deleteType = 22, RULE_nodeType = 23, RULE_createPolicyStmt = 24, 
		RULE_createAttrStmt = 25, RULE_createUserOrObjectStmt = 26, RULE_setNodePropsStmt = 27, 
		RULE_assignStmt = 28, RULE_deassignStmt = 29, RULE_associateStmt = 30, 
		RULE_dissociateStmt = 31, RULE_deleteStmt = 32, RULE_createObligationStmt = 33, 
		RULE_createRuleStmt = 34, RULE_subjectClause = 35, RULE_onClause = 36, 
		RULE_anyPe = 37, RULE_response = 38, RULE_responseBlock = 39, RULE_responseStmt = 40, 
		RULE_deleteRuleStmt = 41, RULE_createProhibitionStmt = 42, RULE_prohibitionContainerList = 43, 
		RULE_prohibitionContainerExpression = 44, RULE_setResourceAccessRightsStmt = 45, 
		RULE_expression = 46, RULE_array = 47, RULE_map = 48, RULE_mapEntry = 49, 
		RULE_entryRef = 50, RULE_literal = 51, RULE_varRef = 52, RULE_funcCall = 53, 
		RULE_funcCallArgs = 54;
	private static String[] makeRuleNames() {
		return new String[] {
			"pal", "stmt", "varStmt", "funcDefStmt", "formalArgList", "formalArg", 
			"formalArgType", "funcReturnStmt", "funcReturnType", "funcBody", "foreachStmt", 
			"forRangeStmt", "breakStmt", "continueStmt", "funcCallStmt", "ifStmt", 
			"elseIfStmt", "elseStmt", "varType", "mapType", "arrayType", "stmtBlock", 
			"deleteType", "nodeType", "createPolicyStmt", "createAttrStmt", "createUserOrObjectStmt", 
			"setNodePropsStmt", "assignStmt", "deassignStmt", "associateStmt", "dissociateStmt", 
			"deleteStmt", "createObligationStmt", "createRuleStmt", "subjectClause", 
			"onClause", "anyPe", "response", "responseBlock", "responseStmt", "deleteRuleStmt", 
			"createProhibitionStmt", "prohibitionContainerList", "prohibitionContainerExpression", 
			"setResourceAccessRightsStmt", "expression", "array", "map", "mapEntry", 
			"entryRef", "literal", "varRef", "funcCall", "funcCallArgs"
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
			null, null, null, null, null, "','", "':'", "';'", "'{'", "'}'", "'['", 
			"']'", "'<'", "'>'", "'('", "')'", "'!'", "'='"
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
			"FOREACH", "FOR", "IN", "IF", "ELSE", "IN_RANGE", "NUMBER", "VARIABLE_OR_FUNCTION_NAME", 
			"STRING", "DOUBLE_QUOTE_STRING", "SINGLE_QUOTE_STRING", "LINE_COMMENT", 
			"WS", "COMMA", "COLON", "SEMI_COLON", "OPEN_CURLY", "CLOSE_CURLY", "OPEN_BRACKET", 
			"CLOSE_BRACKET", "OPEN_ANGLE_BRACKET", "CLOSE_ANGLE_BRACKET", "OPEN_PAREN", 
			"CLOSE_PAREN", "IS_COMPLEMENT", "EQUALS"
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
	public String getGrammarFileName() { return "PAL.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public PALParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class PalContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(PALParser.EOF, 0); }
		public List<StmtContext> stmt() {
			return getRuleContexts(StmtContext.class);
		}
		public StmtContext stmt(int i) {
			return getRuleContext(StmtContext.class,i);
		}
		public PalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterPal(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitPal(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitPal(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PalContext pal() throws RecognitionException {
		PalContext _localctx = new PalContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_pal);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(113);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << CREATE) | (1L << DELETE) | (1L << BREAK) | (1L << CONTINUE) | (1L << SET_RESOURCE_ACCESS_RIGHTS) | (1L << ASSIGN) | (1L << DEASSIGN) | (1L << SET_PROPERTIES) | (1L << ASSOCIATE) | (1L << DISSOCIATE) | (1L << LET) | (1L << CONST) | (1L << FUNCTION) | (1L << RETURN) | (1L << FOREACH) | (1L << FOR) | (1L << IF) | (1L << VARIABLE_OR_FUNCTION_NAME))) != 0)) {
				{
				{
				setState(110);
				stmt();
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

	public static class StmtContext extends ParserRuleContext {
		public VarStmtContext varStmt() {
			return getRuleContext(VarStmtContext.class,0);
		}
		public FuncDefStmtContext funcDefStmt() {
			return getRuleContext(FuncDefStmtContext.class,0);
		}
		public FuncReturnStmtContext funcReturnStmt() {
			return getRuleContext(FuncReturnStmtContext.class,0);
		}
		public ForeachStmtContext foreachStmt() {
			return getRuleContext(ForeachStmtContext.class,0);
		}
		public ForRangeStmtContext forRangeStmt() {
			return getRuleContext(ForRangeStmtContext.class,0);
		}
		public BreakStmtContext breakStmt() {
			return getRuleContext(BreakStmtContext.class,0);
		}
		public ContinueStmtContext continueStmt() {
			return getRuleContext(ContinueStmtContext.class,0);
		}
		public FuncCallStmtContext funcCallStmt() {
			return getRuleContext(FuncCallStmtContext.class,0);
		}
		public IfStmtContext ifStmt() {
			return getRuleContext(IfStmtContext.class,0);
		}
		public CreatePolicyStmtContext createPolicyStmt() {
			return getRuleContext(CreatePolicyStmtContext.class,0);
		}
		public CreateAttrStmtContext createAttrStmt() {
			return getRuleContext(CreateAttrStmtContext.class,0);
		}
		public CreateUserOrObjectStmtContext createUserOrObjectStmt() {
			return getRuleContext(CreateUserOrObjectStmtContext.class,0);
		}
		public CreateObligationStmtContext createObligationStmt() {
			return getRuleContext(CreateObligationStmtContext.class,0);
		}
		public CreateProhibitionStmtContext createProhibitionStmt() {
			return getRuleContext(CreateProhibitionStmtContext.class,0);
		}
		public SetNodePropsStmtContext setNodePropsStmt() {
			return getRuleContext(SetNodePropsStmtContext.class,0);
		}
		public AssignStmtContext assignStmt() {
			return getRuleContext(AssignStmtContext.class,0);
		}
		public DeassignStmtContext deassignStmt() {
			return getRuleContext(DeassignStmtContext.class,0);
		}
		public DeleteStmtContext deleteStmt() {
			return getRuleContext(DeleteStmtContext.class,0);
		}
		public AssociateStmtContext associateStmt() {
			return getRuleContext(AssociateStmtContext.class,0);
		}
		public DissociateStmtContext dissociateStmt() {
			return getRuleContext(DissociateStmtContext.class,0);
		}
		public SetResourceAccessRightsStmtContext setResourceAccessRightsStmt() {
			return getRuleContext(SetResourceAccessRightsStmtContext.class,0);
		}
		public DeleteRuleStmtContext deleteRuleStmt() {
			return getRuleContext(DeleteRuleStmtContext.class,0);
		}
		public StmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StmtContext stmt() throws RecognitionException {
		StmtContext _localctx = new StmtContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(140);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				{
				setState(118);
				varStmt();
				}
				break;
			case 2:
				{
				setState(119);
				funcDefStmt();
				}
				break;
			case 3:
				{
				setState(120);
				funcReturnStmt();
				}
				break;
			case 4:
				{
				setState(121);
				foreachStmt();
				}
				break;
			case 5:
				{
				setState(122);
				forRangeStmt();
				}
				break;
			case 6:
				{
				setState(123);
				breakStmt();
				}
				break;
			case 7:
				{
				setState(124);
				continueStmt();
				}
				break;
			case 8:
				{
				setState(125);
				funcCallStmt();
				}
				break;
			case 9:
				{
				setState(126);
				ifStmt();
				}
				break;
			case 10:
				{
				setState(127);
				createPolicyStmt();
				}
				break;
			case 11:
				{
				setState(128);
				createAttrStmt();
				}
				break;
			case 12:
				{
				setState(129);
				createUserOrObjectStmt();
				}
				break;
			case 13:
				{
				setState(130);
				createObligationStmt();
				}
				break;
			case 14:
				{
				setState(131);
				createProhibitionStmt();
				}
				break;
			case 15:
				{
				setState(132);
				setNodePropsStmt();
				}
				break;
			case 16:
				{
				setState(133);
				assignStmt();
				}
				break;
			case 17:
				{
				setState(134);
				deassignStmt();
				}
				break;
			case 18:
				{
				setState(135);
				deleteStmt();
				}
				break;
			case 19:
				{
				setState(136);
				associateStmt();
				}
				break;
			case 20:
				{
				setState(137);
				dissociateStmt();
				}
				break;
			case 21:
				{
				setState(138);
				setResourceAccessRightsStmt();
				}
				break;
			case 22:
				{
				setState(139);
				deleteRuleStmt();
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

	public static class VarStmtContext extends ParserRuleContext {
		public TerminalNode VARIABLE_OR_FUNCTION_NAME() { return getToken(PALParser.VARIABLE_OR_FUNCTION_NAME, 0); }
		public TerminalNode EQUALS() { return getToken(PALParser.EQUALS, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode SEMI_COLON() { return getToken(PALParser.SEMI_COLON, 0); }
		public TerminalNode LET() { return getToken(PALParser.LET, 0); }
		public TerminalNode CONST() { return getToken(PALParser.CONST, 0); }
		public VarStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterVarStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitVarStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitVarStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarStmtContext varStmt() throws RecognitionException {
		VarStmtContext _localctx = new VarStmtContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_varStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(143);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LET || _la==CONST) {
				{
				setState(142);
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

			setState(145);
			match(VARIABLE_OR_FUNCTION_NAME);
			setState(146);
			match(EQUALS);
			setState(147);
			expression();
			setState(148);
			match(SEMI_COLON);
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

	public static class FuncDefStmtContext extends ParserRuleContext {
		public TerminalNode FUNCTION() { return getToken(PALParser.FUNCTION, 0); }
		public TerminalNode VARIABLE_OR_FUNCTION_NAME() { return getToken(PALParser.VARIABLE_OR_FUNCTION_NAME, 0); }
		public TerminalNode OPEN_PAREN() { return getToken(PALParser.OPEN_PAREN, 0); }
		public FormalArgListContext formalArgList() {
			return getRuleContext(FormalArgListContext.class,0);
		}
		public TerminalNode CLOSE_PAREN() { return getToken(PALParser.CLOSE_PAREN, 0); }
		public FuncBodyContext funcBody() {
			return getRuleContext(FuncBodyContext.class,0);
		}
		public FuncReturnTypeContext funcReturnType() {
			return getRuleContext(FuncReturnTypeContext.class,0);
		}
		public FuncDefStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funcDefStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterFuncDefStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitFuncDefStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitFuncDefStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FuncDefStmtContext funcDefStmt() throws RecognitionException {
		FuncDefStmtContext _localctx = new FuncDefStmtContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_funcDefStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(150);
			match(FUNCTION);
			setState(151);
			match(VARIABLE_OR_FUNCTION_NAME);
			setState(152);
			match(OPEN_PAREN);
			setState(153);
			formalArgList();
			setState(154);
			match(CLOSE_PAREN);
			setState(156);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 39)) & ~0x3f) == 0 && ((1L << (_la - 39)) & ((1L << (ANY - 39)) | (1L << (STRING_TYPE - 39)) | (1L << (BOOLEAN_TYPE - 39)) | (1L << (VOID_TYPE - 39)) | (1L << (MAP_TYPE - 39)) | (1L << (OPEN_BRACKET - 39)))) != 0)) {
				{
				setState(155);
				funcReturnType();
				}
			}

			setState(158);
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
		public List<TerminalNode> COMMA() { return getTokens(PALParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(PALParser.COMMA, i);
		}
		public FormalArgListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_formalArgList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterFormalArgList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitFormalArgList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitFormalArgList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FormalArgListContext formalArgList() throws RecognitionException {
		FormalArgListContext _localctx = new FormalArgListContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_formalArgList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(168);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 39)) & ~0x3f) == 0 && ((1L << (_la - 39)) & ((1L << (ANY - 39)) | (1L << (STRING_TYPE - 39)) | (1L << (BOOLEAN_TYPE - 39)) | (1L << (MAP_TYPE - 39)) | (1L << (OPEN_BRACKET - 39)))) != 0)) {
				{
				setState(160);
				formalArg();
				setState(165);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(161);
					match(COMMA);
					setState(162);
					formalArg();
					}
					}
					setState(167);
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
		public TerminalNode VARIABLE_OR_FUNCTION_NAME() { return getToken(PALParser.VARIABLE_OR_FUNCTION_NAME, 0); }
		public FormalArgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_formalArg; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterFormalArg(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitFormalArg(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitFormalArg(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FormalArgContext formalArg() throws RecognitionException {
		FormalArgContext _localctx = new FormalArgContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_formalArg);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(170);
			formalArgType();
			setState(171);
			match(VARIABLE_OR_FUNCTION_NAME);
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
		public VarTypeContext varType() {
			return getRuleContext(VarTypeContext.class,0);
		}
		public FormalArgTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_formalArgType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterFormalArgType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitFormalArgType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitFormalArgType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FormalArgTypeContext formalArgType() throws RecognitionException {
		FormalArgTypeContext _localctx = new FormalArgTypeContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_formalArgType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(173);
			varType();
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

	public static class FuncReturnStmtContext extends ParserRuleContext {
		public TerminalNode SEMI_COLON() { return getToken(PALParser.SEMI_COLON, 0); }
		public TerminalNode RETURN() { return getToken(PALParser.RETURN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public FuncReturnStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funcReturnStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterFuncReturnStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitFuncReturnStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitFuncReturnStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FuncReturnStmtContext funcReturnStmt() throws RecognitionException {
		FuncReturnStmtContext _localctx = new FuncReturnStmtContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_funcReturnStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(178);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				{
				setState(175);
				match(RETURN);
				setState(176);
				expression();
				}
				break;
			case 2:
				{
				setState(177);
				match(RETURN);
				}
				break;
			}
			setState(180);
			match(SEMI_COLON);
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
	public static class VarReturnTypeContext extends FuncReturnTypeContext {
		public VarTypeContext varType() {
			return getRuleContext(VarTypeContext.class,0);
		}
		public VarReturnTypeContext(FuncReturnTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterVarReturnType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitVarReturnType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitVarReturnType(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class VoidReturnTypeContext extends FuncReturnTypeContext {
		public TerminalNode VOID_TYPE() { return getToken(PALParser.VOID_TYPE, 0); }
		public VoidReturnTypeContext(FuncReturnTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterVoidReturnType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitVoidReturnType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitVoidReturnType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FuncReturnTypeContext funcReturnType() throws RecognitionException {
		FuncReturnTypeContext _localctx = new FuncReturnTypeContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_funcReturnType);
		try {
			setState(184);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ANY:
			case STRING_TYPE:
			case BOOLEAN_TYPE:
			case MAP_TYPE:
			case OPEN_BRACKET:
				_localctx = new VarReturnTypeContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(182);
				varType();
				}
				break;
			case VOID_TYPE:
				_localctx = new VoidReturnTypeContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(183);
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
		public TerminalNode OPEN_CURLY() { return getToken(PALParser.OPEN_CURLY, 0); }
		public TerminalNode CLOSE_CURLY() { return getToken(PALParser.CLOSE_CURLY, 0); }
		public List<StmtContext> stmt() {
			return getRuleContexts(StmtContext.class);
		}
		public StmtContext stmt(int i) {
			return getRuleContext(StmtContext.class,i);
		}
		public FuncBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funcBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterFuncBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitFuncBody(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitFuncBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FuncBodyContext funcBody() throws RecognitionException {
		FuncBodyContext _localctx = new FuncBodyContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_funcBody);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(186);
			match(OPEN_CURLY);
			setState(190);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << CREATE) | (1L << DELETE) | (1L << BREAK) | (1L << CONTINUE) | (1L << SET_RESOURCE_ACCESS_RIGHTS) | (1L << ASSIGN) | (1L << DEASSIGN) | (1L << SET_PROPERTIES) | (1L << ASSOCIATE) | (1L << DISSOCIATE) | (1L << LET) | (1L << CONST) | (1L << FUNCTION) | (1L << RETURN) | (1L << FOREACH) | (1L << FOR) | (1L << IF) | (1L << VARIABLE_OR_FUNCTION_NAME))) != 0)) {
				{
				{
				setState(187);
				stmt();
				}
				}
				setState(192);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(193);
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

	public static class ForeachStmtContext extends ParserRuleContext {
		public Token key;
		public Token mapValue;
		public TerminalNode FOREACH() { return getToken(PALParser.FOREACH, 0); }
		public TerminalNode IN() { return getToken(PALParser.IN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public StmtBlockContext stmtBlock() {
			return getRuleContext(StmtBlockContext.class,0);
		}
		public List<TerminalNode> VARIABLE_OR_FUNCTION_NAME() { return getTokens(PALParser.VARIABLE_OR_FUNCTION_NAME); }
		public TerminalNode VARIABLE_OR_FUNCTION_NAME(int i) {
			return getToken(PALParser.VARIABLE_OR_FUNCTION_NAME, i);
		}
		public TerminalNode COMMA() { return getToken(PALParser.COMMA, 0); }
		public ForeachStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_foreachStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterForeachStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitForeachStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitForeachStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForeachStmtContext foreachStmt() throws RecognitionException {
		ForeachStmtContext _localctx = new ForeachStmtContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_foreachStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(195);
			match(FOREACH);
			setState(196);
			((ForeachStmtContext)_localctx).key = match(VARIABLE_OR_FUNCTION_NAME);
			setState(199);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(197);
				match(COMMA);
				setState(198);
				((ForeachStmtContext)_localctx).mapValue = match(VARIABLE_OR_FUNCTION_NAME);
				}
			}

			setState(201);
			match(IN);
			setState(202);
			expression();
			setState(203);
			stmtBlock();
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

	public static class ForRangeStmtContext extends ParserRuleContext {
		public Token lower;
		public Token upper;
		public TerminalNode FOR() { return getToken(PALParser.FOR, 0); }
		public TerminalNode VARIABLE_OR_FUNCTION_NAME() { return getToken(PALParser.VARIABLE_OR_FUNCTION_NAME, 0); }
		public TerminalNode IN_RANGE() { return getToken(PALParser.IN_RANGE, 0); }
		public TerminalNode OPEN_BRACKET() { return getToken(PALParser.OPEN_BRACKET, 0); }
		public TerminalNode COMMA() { return getToken(PALParser.COMMA, 0); }
		public TerminalNode CLOSE_BRACKET() { return getToken(PALParser.CLOSE_BRACKET, 0); }
		public StmtBlockContext stmtBlock() {
			return getRuleContext(StmtBlockContext.class,0);
		}
		public List<TerminalNode> NUMBER() { return getTokens(PALParser.NUMBER); }
		public TerminalNode NUMBER(int i) {
			return getToken(PALParser.NUMBER, i);
		}
		public ForRangeStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forRangeStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterForRangeStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitForRangeStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitForRangeStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForRangeStmtContext forRangeStmt() throws RecognitionException {
		ForRangeStmtContext _localctx = new ForRangeStmtContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_forRangeStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(205);
			match(FOR);
			setState(206);
			match(VARIABLE_OR_FUNCTION_NAME);
			setState(207);
			match(IN_RANGE);
			setState(208);
			match(OPEN_BRACKET);
			setState(209);
			((ForRangeStmtContext)_localctx).lower = match(NUMBER);
			setState(210);
			match(COMMA);
			setState(211);
			((ForRangeStmtContext)_localctx).upper = match(NUMBER);
			setState(212);
			match(CLOSE_BRACKET);
			setState(213);
			stmtBlock();
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

	public static class BreakStmtContext extends ParserRuleContext {
		public TerminalNode BREAK() { return getToken(PALParser.BREAK, 0); }
		public TerminalNode SEMI_COLON() { return getToken(PALParser.SEMI_COLON, 0); }
		public BreakStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_breakStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterBreakStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitBreakStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitBreakStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BreakStmtContext breakStmt() throws RecognitionException {
		BreakStmtContext _localctx = new BreakStmtContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_breakStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(215);
			match(BREAK);
			setState(216);
			match(SEMI_COLON);
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

	public static class ContinueStmtContext extends ParserRuleContext {
		public TerminalNode CONTINUE() { return getToken(PALParser.CONTINUE, 0); }
		public TerminalNode SEMI_COLON() { return getToken(PALParser.SEMI_COLON, 0); }
		public ContinueStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_continueStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterContinueStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitContinueStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitContinueStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ContinueStmtContext continueStmt() throws RecognitionException {
		ContinueStmtContext _localctx = new ContinueStmtContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_continueStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(218);
			match(CONTINUE);
			setState(219);
			match(SEMI_COLON);
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

	public static class FuncCallStmtContext extends ParserRuleContext {
		public FuncCallContext funcCall() {
			return getRuleContext(FuncCallContext.class,0);
		}
		public TerminalNode SEMI_COLON() { return getToken(PALParser.SEMI_COLON, 0); }
		public FuncCallStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funcCallStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterFuncCallStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitFuncCallStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitFuncCallStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FuncCallStmtContext funcCallStmt() throws RecognitionException {
		FuncCallStmtContext _localctx = new FuncCallStmtContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_funcCallStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(221);
			funcCall();
			setState(222);
			match(SEMI_COLON);
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

	public static class IfStmtContext extends ParserRuleContext {
		public ExpressionContext condition;
		public TerminalNode IF() { return getToken(PALParser.IF, 0); }
		public StmtBlockContext stmtBlock() {
			return getRuleContext(StmtBlockContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode IS_COMPLEMENT() { return getToken(PALParser.IS_COMPLEMENT, 0); }
		public List<ElseIfStmtContext> elseIfStmt() {
			return getRuleContexts(ElseIfStmtContext.class);
		}
		public ElseIfStmtContext elseIfStmt(int i) {
			return getRuleContext(ElseIfStmtContext.class,i);
		}
		public ElseStmtContext elseStmt() {
			return getRuleContext(ElseStmtContext.class,0);
		}
		public IfStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ifStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterIfStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitIfStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitIfStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IfStmtContext ifStmt() throws RecognitionException {
		IfStmtContext _localctx = new IfStmtContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_ifStmt);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(224);
			match(IF);
			setState(226);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IS_COMPLEMENT) {
				{
				setState(225);
				match(IS_COMPLEMENT);
				}
			}

			setState(228);
			((IfStmtContext)_localctx).condition = expression();
			setState(229);
			stmtBlock();
			setState(233);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(230);
					elseIfStmt();
					}
					} 
				}
				setState(235);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			}
			setState(237);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ELSE) {
				{
				setState(236);
				elseStmt();
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

	public static class ElseIfStmtContext extends ParserRuleContext {
		public ExpressionContext condition;
		public TerminalNode ELSE() { return getToken(PALParser.ELSE, 0); }
		public TerminalNode IF() { return getToken(PALParser.IF, 0); }
		public StmtBlockContext stmtBlock() {
			return getRuleContext(StmtBlockContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode IS_COMPLEMENT() { return getToken(PALParser.IS_COMPLEMENT, 0); }
		public ElseIfStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elseIfStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterElseIfStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitElseIfStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitElseIfStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElseIfStmtContext elseIfStmt() throws RecognitionException {
		ElseIfStmtContext _localctx = new ElseIfStmtContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_elseIfStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(239);
			match(ELSE);
			setState(240);
			match(IF);
			setState(242);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IS_COMPLEMENT) {
				{
				setState(241);
				match(IS_COMPLEMENT);
				}
			}

			setState(244);
			((ElseIfStmtContext)_localctx).condition = expression();
			setState(245);
			stmtBlock();
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

	public static class ElseStmtContext extends ParserRuleContext {
		public TerminalNode ELSE() { return getToken(PALParser.ELSE, 0); }
		public StmtBlockContext stmtBlock() {
			return getRuleContext(StmtBlockContext.class,0);
		}
		public ElseStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elseStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterElseStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitElseStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitElseStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElseStmtContext elseStmt() throws RecognitionException {
		ElseStmtContext _localctx = new ElseStmtContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_elseStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(247);
			match(ELSE);
			setState(248);
			stmtBlock();
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

	public static class VarTypeContext extends ParserRuleContext {
		public VarTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varType; }
	 
		public VarTypeContext() { }
		public void copyFrom(VarTypeContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class MapVarTypeContext extends VarTypeContext {
		public MapTypeContext mapType() {
			return getRuleContext(MapTypeContext.class,0);
		}
		public MapVarTypeContext(VarTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterMapVarType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitMapVarType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitMapVarType(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class StringTypeContext extends VarTypeContext {
		public TerminalNode STRING_TYPE() { return getToken(PALParser.STRING_TYPE, 0); }
		public StringTypeContext(VarTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterStringType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitStringType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitStringType(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ArrayVarTypeContext extends VarTypeContext {
		public ArrayTypeContext arrayType() {
			return getRuleContext(ArrayTypeContext.class,0);
		}
		public ArrayVarTypeContext(VarTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterArrayVarType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitArrayVarType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitArrayVarType(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BooleanTypeContext extends VarTypeContext {
		public TerminalNode BOOLEAN_TYPE() { return getToken(PALParser.BOOLEAN_TYPE, 0); }
		public BooleanTypeContext(VarTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterBooleanType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitBooleanType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitBooleanType(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AnyTypeContext extends VarTypeContext {
		public TerminalNode ANY() { return getToken(PALParser.ANY, 0); }
		public AnyTypeContext(VarTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterAnyType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitAnyType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitAnyType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarTypeContext varType() throws RecognitionException {
		VarTypeContext _localctx = new VarTypeContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_varType);
		try {
			setState(255);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STRING_TYPE:
				_localctx = new StringTypeContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(250);
				match(STRING_TYPE);
				}
				break;
			case BOOLEAN_TYPE:
				_localctx = new BooleanTypeContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(251);
				match(BOOLEAN_TYPE);
				}
				break;
			case OPEN_BRACKET:
				_localctx = new ArrayVarTypeContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(252);
				arrayType();
				}
				break;
			case MAP_TYPE:
				_localctx = new MapVarTypeContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(253);
				mapType();
				}
				break;
			case ANY:
				_localctx = new AnyTypeContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(254);
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
		public VarTypeContext keyType;
		public VarTypeContext valueType;
		public TerminalNode MAP_TYPE() { return getToken(PALParser.MAP_TYPE, 0); }
		public TerminalNode OPEN_BRACKET() { return getToken(PALParser.OPEN_BRACKET, 0); }
		public TerminalNode CLOSE_BRACKET() { return getToken(PALParser.CLOSE_BRACKET, 0); }
		public List<VarTypeContext> varType() {
			return getRuleContexts(VarTypeContext.class);
		}
		public VarTypeContext varType(int i) {
			return getRuleContext(VarTypeContext.class,i);
		}
		public MapTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mapType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterMapType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitMapType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitMapType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MapTypeContext mapType() throws RecognitionException {
		MapTypeContext _localctx = new MapTypeContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_mapType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(257);
			match(MAP_TYPE);
			setState(258);
			match(OPEN_BRACKET);
			setState(259);
			((MapTypeContext)_localctx).keyType = varType();
			setState(260);
			match(CLOSE_BRACKET);
			setState(261);
			((MapTypeContext)_localctx).valueType = varType();
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
		public TerminalNode OPEN_BRACKET() { return getToken(PALParser.OPEN_BRACKET, 0); }
		public TerminalNode CLOSE_BRACKET() { return getToken(PALParser.CLOSE_BRACKET, 0); }
		public VarTypeContext varType() {
			return getRuleContext(VarTypeContext.class,0);
		}
		public ArrayTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterArrayType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitArrayType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitArrayType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayTypeContext arrayType() throws RecognitionException {
		ArrayTypeContext _localctx = new ArrayTypeContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_arrayType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(263);
			match(OPEN_BRACKET);
			setState(264);
			match(CLOSE_BRACKET);
			setState(265);
			varType();
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

	public static class StmtBlockContext extends ParserRuleContext {
		public TerminalNode OPEN_CURLY() { return getToken(PALParser.OPEN_CURLY, 0); }
		public TerminalNode CLOSE_CURLY() { return getToken(PALParser.CLOSE_CURLY, 0); }
		public List<StmtContext> stmt() {
			return getRuleContexts(StmtContext.class);
		}
		public StmtContext stmt(int i) {
			return getRuleContext(StmtContext.class,i);
		}
		public StmtBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stmtBlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterStmtBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitStmtBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitStmtBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StmtBlockContext stmtBlock() throws RecognitionException {
		StmtBlockContext _localctx = new StmtBlockContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_stmtBlock);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(267);
			match(OPEN_CURLY);
			setState(271);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << CREATE) | (1L << DELETE) | (1L << BREAK) | (1L << CONTINUE) | (1L << SET_RESOURCE_ACCESS_RIGHTS) | (1L << ASSIGN) | (1L << DEASSIGN) | (1L << SET_PROPERTIES) | (1L << ASSOCIATE) | (1L << DISSOCIATE) | (1L << LET) | (1L << CONST) | (1L << FUNCTION) | (1L << RETURN) | (1L << FOREACH) | (1L << FOR) | (1L << IF) | (1L << VARIABLE_OR_FUNCTION_NAME))) != 0)) {
				{
				{
				setState(268);
				stmt();
				}
				}
				setState(273);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(274);
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
			if ( listener instanceof PALListener ) ((PALListener)listener).enterDeleteNode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitDeleteNode(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitDeleteNode(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DeleteProhibitionContext extends DeleteTypeContext {
		public TerminalNode PROHIBITION() { return getToken(PALParser.PROHIBITION, 0); }
		public DeleteProhibitionContext(DeleteTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterDeleteProhibition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitDeleteProhibition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitDeleteProhibition(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DeleteObligationContext extends DeleteTypeContext {
		public TerminalNode OBLIGATION() { return getToken(PALParser.OBLIGATION, 0); }
		public DeleteObligationContext(DeleteTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterDeleteObligation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitDeleteObligation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitDeleteObligation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeleteTypeContext deleteType() throws RecognitionException {
		DeleteTypeContext _localctx = new DeleteTypeContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_deleteType);
		try {
			setState(279);
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
				setState(276);
				nodeType();
				}
				break;
			case OBLIGATION:
				_localctx = new DeleteObligationContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(277);
				match(OBLIGATION);
				}
				break;
			case PROHIBITION:
				_localctx = new DeleteProhibitionContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(278);
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
		public TerminalNode POLICY_CLASS() { return getToken(PALParser.POLICY_CLASS, 0); }
		public TerminalNode OBJECT_ATTRIBUTE() { return getToken(PALParser.OBJECT_ATTRIBUTE, 0); }
		public TerminalNode USER_ATTRIBUTE() { return getToken(PALParser.USER_ATTRIBUTE, 0); }
		public TerminalNode OBJECT() { return getToken(PALParser.OBJECT, 0); }
		public TerminalNode USER() { return getToken(PALParser.USER, 0); }
		public NodeTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nodeType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterNodeType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitNodeType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitNodeType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NodeTypeContext nodeType() throws RecognitionException {
		NodeTypeContext _localctx = new NodeTypeContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_nodeType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(281);
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

	public static class CreatePolicyStmtContext extends ParserRuleContext {
		public TerminalNode CREATE() { return getToken(PALParser.CREATE, 0); }
		public TerminalNode POLICY_CLASS() { return getToken(PALParser.POLICY_CLASS, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode SEMI_COLON() { return getToken(PALParser.SEMI_COLON, 0); }
		public CreatePolicyStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_createPolicyStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterCreatePolicyStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitCreatePolicyStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitCreatePolicyStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreatePolicyStmtContext createPolicyStmt() throws RecognitionException {
		CreatePolicyStmtContext _localctx = new CreatePolicyStmtContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_createPolicyStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(283);
			match(CREATE);
			setState(284);
			match(POLICY_CLASS);
			setState(285);
			expression();
			setState(286);
			match(SEMI_COLON);
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

	public static class CreateAttrStmtContext extends ParserRuleContext {
		public ExpressionContext name;
		public ExpressionContext parents;
		public TerminalNode CREATE() { return getToken(PALParser.CREATE, 0); }
		public TerminalNode IN() { return getToken(PALParser.IN, 0); }
		public TerminalNode SEMI_COLON() { return getToken(PALParser.SEMI_COLON, 0); }
		public TerminalNode OBJECT_ATTRIBUTE() { return getToken(PALParser.OBJECT_ATTRIBUTE, 0); }
		public TerminalNode USER_ATTRIBUTE() { return getToken(PALParser.USER_ATTRIBUTE, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public CreateAttrStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_createAttrStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterCreateAttrStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitCreateAttrStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitCreateAttrStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreateAttrStmtContext createAttrStmt() throws RecognitionException {
		CreateAttrStmtContext _localctx = new CreateAttrStmtContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_createAttrStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(288);
			match(CREATE);
			setState(289);
			_la = _input.LA(1);
			if ( !(_la==OBJECT_ATTRIBUTE || _la==USER_ATTRIBUTE) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(290);
			((CreateAttrStmtContext)_localctx).name = expression();
			setState(291);
			match(IN);
			setState(292);
			((CreateAttrStmtContext)_localctx).parents = expression();
			setState(293);
			match(SEMI_COLON);
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

	public static class CreateUserOrObjectStmtContext extends ParserRuleContext {
		public ExpressionContext name;
		public ExpressionContext parents;
		public TerminalNode CREATE() { return getToken(PALParser.CREATE, 0); }
		public TerminalNode IN() { return getToken(PALParser.IN, 0); }
		public TerminalNode SEMI_COLON() { return getToken(PALParser.SEMI_COLON, 0); }
		public TerminalNode USER() { return getToken(PALParser.USER, 0); }
		public TerminalNode OBJECT() { return getToken(PALParser.OBJECT, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public CreateUserOrObjectStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_createUserOrObjectStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterCreateUserOrObjectStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitCreateUserOrObjectStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitCreateUserOrObjectStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreateUserOrObjectStmtContext createUserOrObjectStmt() throws RecognitionException {
		CreateUserOrObjectStmtContext _localctx = new CreateUserOrObjectStmtContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_createUserOrObjectStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(295);
			match(CREATE);
			setState(296);
			_la = _input.LA(1);
			if ( !(_la==OBJECT || _la==USER) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(297);
			((CreateUserOrObjectStmtContext)_localctx).name = expression();
			setState(298);
			match(IN);
			setState(299);
			((CreateUserOrObjectStmtContext)_localctx).parents = expression();
			setState(300);
			match(SEMI_COLON);
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

	public static class SetNodePropsStmtContext extends ParserRuleContext {
		public ExpressionContext name;
		public ExpressionContext properties;
		public TerminalNode SET_PROPERTIES() { return getToken(PALParser.SET_PROPERTIES, 0); }
		public TerminalNode OF() { return getToken(PALParser.OF, 0); }
		public TerminalNode TO() { return getToken(PALParser.TO, 0); }
		public TerminalNode SEMI_COLON() { return getToken(PALParser.SEMI_COLON, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public SetNodePropsStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_setNodePropsStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterSetNodePropsStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitSetNodePropsStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitSetNodePropsStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SetNodePropsStmtContext setNodePropsStmt() throws RecognitionException {
		SetNodePropsStmtContext _localctx = new SetNodePropsStmtContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_setNodePropsStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(302);
			match(SET_PROPERTIES);
			setState(303);
			match(OF);
			setState(304);
			((SetNodePropsStmtContext)_localctx).name = expression();
			setState(305);
			match(TO);
			setState(306);
			((SetNodePropsStmtContext)_localctx).properties = expression();
			setState(307);
			match(SEMI_COLON);
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

	public static class AssignStmtContext extends ParserRuleContext {
		public ExpressionContext childNode;
		public ExpressionContext parentNodes;
		public TerminalNode ASSIGN() { return getToken(PALParser.ASSIGN, 0); }
		public TerminalNode TO() { return getToken(PALParser.TO, 0); }
		public TerminalNode SEMI_COLON() { return getToken(PALParser.SEMI_COLON, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public AssignStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterAssignStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitAssignStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitAssignStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssignStmtContext assignStmt() throws RecognitionException {
		AssignStmtContext _localctx = new AssignStmtContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_assignStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(309);
			match(ASSIGN);
			setState(310);
			((AssignStmtContext)_localctx).childNode = expression();
			setState(311);
			match(TO);
			setState(312);
			((AssignStmtContext)_localctx).parentNodes = expression();
			setState(313);
			match(SEMI_COLON);
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

	public static class DeassignStmtContext extends ParserRuleContext {
		public ExpressionContext childNode;
		public ExpressionContext parentNodes;
		public TerminalNode DEASSIGN() { return getToken(PALParser.DEASSIGN, 0); }
		public TerminalNode FROM() { return getToken(PALParser.FROM, 0); }
		public TerminalNode SEMI_COLON() { return getToken(PALParser.SEMI_COLON, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public DeassignStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_deassignStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterDeassignStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitDeassignStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitDeassignStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeassignStmtContext deassignStmt() throws RecognitionException {
		DeassignStmtContext _localctx = new DeassignStmtContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_deassignStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(315);
			match(DEASSIGN);
			setState(316);
			((DeassignStmtContext)_localctx).childNode = expression();
			setState(317);
			match(FROM);
			setState(318);
			((DeassignStmtContext)_localctx).parentNodes = expression();
			setState(319);
			match(SEMI_COLON);
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

	public static class AssociateStmtContext extends ParserRuleContext {
		public ExpressionContext ua;
		public ExpressionContext target;
		public ExpressionContext accessRights;
		public TerminalNode ASSOCIATE() { return getToken(PALParser.ASSOCIATE, 0); }
		public TerminalNode AND() { return getToken(PALParser.AND, 0); }
		public TerminalNode WITH() { return getToken(PALParser.WITH, 0); }
		public TerminalNode SEMI_COLON() { return getToken(PALParser.SEMI_COLON, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public AssociateStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_associateStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterAssociateStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitAssociateStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitAssociateStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssociateStmtContext associateStmt() throws RecognitionException {
		AssociateStmtContext _localctx = new AssociateStmtContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_associateStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(321);
			match(ASSOCIATE);
			setState(322);
			((AssociateStmtContext)_localctx).ua = expression();
			setState(323);
			match(AND);
			setState(324);
			((AssociateStmtContext)_localctx).target = expression();
			setState(325);
			match(WITH);
			setState(326);
			((AssociateStmtContext)_localctx).accessRights = expression();
			setState(327);
			match(SEMI_COLON);
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

	public static class DissociateStmtContext extends ParserRuleContext {
		public ExpressionContext ua;
		public ExpressionContext target;
		public TerminalNode DISSOCIATE() { return getToken(PALParser.DISSOCIATE, 0); }
		public TerminalNode AND() { return getToken(PALParser.AND, 0); }
		public TerminalNode SEMI_COLON() { return getToken(PALParser.SEMI_COLON, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public DissociateStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dissociateStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterDissociateStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitDissociateStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitDissociateStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DissociateStmtContext dissociateStmt() throws RecognitionException {
		DissociateStmtContext _localctx = new DissociateStmtContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_dissociateStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(329);
			match(DISSOCIATE);
			setState(330);
			((DissociateStmtContext)_localctx).ua = expression();
			setState(331);
			match(AND);
			setState(332);
			((DissociateStmtContext)_localctx).target = expression();
			setState(333);
			match(SEMI_COLON);
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

	public static class DeleteStmtContext extends ParserRuleContext {
		public TerminalNode DELETE() { return getToken(PALParser.DELETE, 0); }
		public DeleteTypeContext deleteType() {
			return getRuleContext(DeleteTypeContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode SEMI_COLON() { return getToken(PALParser.SEMI_COLON, 0); }
		public DeleteStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_deleteStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterDeleteStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitDeleteStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitDeleteStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeleteStmtContext deleteStmt() throws RecognitionException {
		DeleteStmtContext _localctx = new DeleteStmtContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_deleteStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(335);
			match(DELETE);
			setState(336);
			deleteType();
			setState(337);
			expression();
			setState(338);
			match(SEMI_COLON);
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

	public static class CreateObligationStmtContext extends ParserRuleContext {
		public TerminalNode CREATE() { return getToken(PALParser.CREATE, 0); }
		public TerminalNode OBLIGATION() { return getToken(PALParser.OBLIGATION, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode OPEN_CURLY() { return getToken(PALParser.OPEN_CURLY, 0); }
		public TerminalNode CLOSE_CURLY() { return getToken(PALParser.CLOSE_CURLY, 0); }
		public List<CreateRuleStmtContext> createRuleStmt() {
			return getRuleContexts(CreateRuleStmtContext.class);
		}
		public CreateRuleStmtContext createRuleStmt(int i) {
			return getRuleContext(CreateRuleStmtContext.class,i);
		}
		public CreateObligationStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_createObligationStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterCreateObligationStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitCreateObligationStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitCreateObligationStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreateObligationStmtContext createObligationStmt() throws RecognitionException {
		CreateObligationStmtContext _localctx = new CreateObligationStmtContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_createObligationStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(340);
			match(CREATE);
			setState(341);
			match(OBLIGATION);
			setState(342);
			expression();
			setState(343);
			match(OPEN_CURLY);
			setState(347);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==CREATE) {
				{
				{
				setState(344);
				createRuleStmt();
				}
				}
				setState(349);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(350);
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

	public static class CreateRuleStmtContext extends ParserRuleContext {
		public ExpressionContext ruleName;
		public ExpressionContext performsClause;
		public TerminalNode CREATE() { return getToken(PALParser.CREATE, 0); }
		public TerminalNode RULE() { return getToken(PALParser.RULE, 0); }
		public TerminalNode WHEN() { return getToken(PALParser.WHEN, 0); }
		public SubjectClauseContext subjectClause() {
			return getRuleContext(SubjectClauseContext.class,0);
		}
		public TerminalNode PERFORMS() { return getToken(PALParser.PERFORMS, 0); }
		public ResponseContext response() {
			return getRuleContext(ResponseContext.class,0);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode ON() { return getToken(PALParser.ON, 0); }
		public OnClauseContext onClause() {
			return getRuleContext(OnClauseContext.class,0);
		}
		public CreateRuleStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_createRuleStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterCreateRuleStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitCreateRuleStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitCreateRuleStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreateRuleStmtContext createRuleStmt() throws RecognitionException {
		CreateRuleStmtContext _localctx = new CreateRuleStmtContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_createRuleStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(352);
			match(CREATE);
			setState(353);
			match(RULE);
			setState(354);
			((CreateRuleStmtContext)_localctx).ruleName = expression();
			setState(355);
			match(WHEN);
			setState(356);
			subjectClause();
			setState(357);
			match(PERFORMS);
			setState(358);
			((CreateRuleStmtContext)_localctx).performsClause = expression();
			setState(361);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ON) {
				{
				setState(359);
				match(ON);
				setState(360);
				onClause();
				}
			}

			setState(363);
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
		public TerminalNode USER() { return getToken(PALParser.USER, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public UserSubjectContext(SubjectClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterUserSubject(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitUserSubject(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitUserSubject(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class UsersListSubjectContext extends SubjectClauseContext {
		public ExpressionContext users;
		public TerminalNode USERS() { return getToken(PALParser.USERS, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public UsersListSubjectContext(SubjectClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterUsersListSubject(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitUsersListSubject(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitUsersListSubject(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class UserAttrSubjectContext extends SubjectClauseContext {
		public ExpressionContext attribute;
		public TerminalNode ANY_USER_WITH_ATTRIBUTE() { return getToken(PALParser.ANY_USER_WITH_ATTRIBUTE, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public UserAttrSubjectContext(SubjectClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterUserAttrSubject(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitUserAttrSubject(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitUserAttrSubject(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ProcessSubjectContext extends SubjectClauseContext {
		public ExpressionContext process;
		public TerminalNode PROCESS() { return getToken(PALParser.PROCESS, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ProcessSubjectContext(SubjectClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterProcessSubject(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitProcessSubject(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitProcessSubject(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AnyUserSubjectContext extends SubjectClauseContext {
		public TerminalNode ANY_USER() { return getToken(PALParser.ANY_USER, 0); }
		public AnyUserSubjectContext(SubjectClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterAnyUserSubject(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitAnyUserSubject(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitAnyUserSubject(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SubjectClauseContext subjectClause() throws RecognitionException {
		SubjectClauseContext _localctx = new SubjectClauseContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_subjectClause);
		try {
			setState(374);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ANY_USER:
				_localctx = new AnyUserSubjectContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(365);
				match(ANY_USER);
				}
				break;
			case USER:
				_localctx = new UserSubjectContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(366);
				match(USER);
				setState(367);
				((UserSubjectContext)_localctx).user = expression();
				}
				break;
			case USERS:
				_localctx = new UsersListSubjectContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(368);
				match(USERS);
				setState(369);
				((UsersListSubjectContext)_localctx).users = expression();
				}
				break;
			case ANY_USER_WITH_ATTRIBUTE:
				_localctx = new UserAttrSubjectContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(370);
				match(ANY_USER_WITH_ATTRIBUTE);
				setState(371);
				((UserAttrSubjectContext)_localctx).attribute = expression();
				}
				break;
			case PROCESS:
				_localctx = new ProcessSubjectContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(372);
				match(PROCESS);
				setState(373);
				((ProcessSubjectContext)_localctx).process = expression();
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
		public TerminalNode IN() { return getToken(PALParser.IN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public AnyContainedInContext(OnClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterAnyContainedIn(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitAnyContainedIn(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitAnyContainedIn(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AnyOfSetContext extends OnClauseContext {
		public AnyPeContext anyPe() {
			return getRuleContext(AnyPeContext.class,0);
		}
		public TerminalNode OF() { return getToken(PALParser.OF, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public AnyOfSetContext(OnClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterAnyOfSet(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitAnyOfSet(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitAnyOfSet(this);
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
			if ( listener instanceof PALListener ) ((PALListener)listener).enterAnyPolicyElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitAnyPolicyElement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitAnyPolicyElement(this);
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
			if ( listener instanceof PALListener ) ((PALListener)listener).enterPolicyElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitPolicyElement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitPolicyElement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OnClauseContext onClause() throws RecognitionException {
		OnClauseContext _localctx = new OnClauseContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_onClause);
		try {
			setState(386);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				_localctx = new PolicyElementContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(376);
				expression();
				}
				break;
			case 2:
				_localctx = new AnyPolicyElementContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(377);
				anyPe();
				}
				break;
			case 3:
				_localctx = new AnyContainedInContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(378);
				anyPe();
				setState(379);
				match(IN);
				setState(380);
				expression();
				}
				break;
			case 4:
				_localctx = new AnyOfSetContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(382);
				anyPe();
				setState(383);
				match(OF);
				setState(384);
				expression();
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
		public TerminalNode ANY() { return getToken(PALParser.ANY, 0); }
		public TerminalNode POLICY_ELEMENT() { return getToken(PALParser.POLICY_ELEMENT, 0); }
		public AnyPeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_anyPe; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterAnyPe(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitAnyPe(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitAnyPe(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnyPeContext anyPe() throws RecognitionException {
		AnyPeContext _localctx = new AnyPeContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_anyPe);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(388);
			match(ANY);
			setState(389);
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
		public TerminalNode DO() { return getToken(PALParser.DO, 0); }
		public TerminalNode OPEN_PAREN() { return getToken(PALParser.OPEN_PAREN, 0); }
		public TerminalNode VARIABLE_OR_FUNCTION_NAME() { return getToken(PALParser.VARIABLE_OR_FUNCTION_NAME, 0); }
		public TerminalNode CLOSE_PAREN() { return getToken(PALParser.CLOSE_PAREN, 0); }
		public ResponseBlockContext responseBlock() {
			return getRuleContext(ResponseBlockContext.class,0);
		}
		public ResponseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_response; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterResponse(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitResponse(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitResponse(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ResponseContext response() throws RecognitionException {
		ResponseContext _localctx = new ResponseContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_response);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(391);
			match(DO);
			setState(392);
			match(OPEN_PAREN);
			setState(393);
			match(VARIABLE_OR_FUNCTION_NAME);
			setState(394);
			match(CLOSE_PAREN);
			setState(395);
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
		public TerminalNode OPEN_CURLY() { return getToken(PALParser.OPEN_CURLY, 0); }
		public TerminalNode CLOSE_CURLY() { return getToken(PALParser.CLOSE_CURLY, 0); }
		public List<ResponseStmtContext> responseStmt() {
			return getRuleContexts(ResponseStmtContext.class);
		}
		public ResponseStmtContext responseStmt(int i) {
			return getRuleContext(ResponseStmtContext.class,i);
		}
		public ResponseBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_responseBlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterResponseBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitResponseBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitResponseBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ResponseBlockContext responseBlock() throws RecognitionException {
		ResponseBlockContext _localctx = new ResponseBlockContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_responseBlock);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(397);
			match(OPEN_CURLY);
			setState(401);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << CREATE) | (1L << DELETE) | (1L << BREAK) | (1L << CONTINUE) | (1L << SET_RESOURCE_ACCESS_RIGHTS) | (1L << ASSIGN) | (1L << DEASSIGN) | (1L << SET_PROPERTIES) | (1L << ASSOCIATE) | (1L << DISSOCIATE) | (1L << LET) | (1L << CONST) | (1L << FUNCTION) | (1L << RETURN) | (1L << FOREACH) | (1L << FOR) | (1L << IF) | (1L << VARIABLE_OR_FUNCTION_NAME))) != 0)) {
				{
				{
				setState(398);
				responseStmt();
				}
				}
				setState(403);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(404);
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

	public static class ResponseStmtContext extends ParserRuleContext {
		public StmtContext stmt() {
			return getRuleContext(StmtContext.class,0);
		}
		public CreateRuleStmtContext createRuleStmt() {
			return getRuleContext(CreateRuleStmtContext.class,0);
		}
		public DeleteRuleStmtContext deleteRuleStmt() {
			return getRuleContext(DeleteRuleStmtContext.class,0);
		}
		public ResponseStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_responseStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterResponseStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitResponseStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitResponseStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ResponseStmtContext responseStmt() throws RecognitionException {
		ResponseStmtContext _localctx = new ResponseStmtContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_responseStmt);
		try {
			setState(409);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(406);
				stmt();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(407);
				createRuleStmt();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(408);
				deleteRuleStmt();
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

	public static class DeleteRuleStmtContext extends ParserRuleContext {
		public ExpressionContext ruleName;
		public ExpressionContext obligationName;
		public TerminalNode DELETE() { return getToken(PALParser.DELETE, 0); }
		public TerminalNode RULE() { return getToken(PALParser.RULE, 0); }
		public TerminalNode FROM() { return getToken(PALParser.FROM, 0); }
		public TerminalNode OBLIGATION() { return getToken(PALParser.OBLIGATION, 0); }
		public TerminalNode SEMI_COLON() { return getToken(PALParser.SEMI_COLON, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public DeleteRuleStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_deleteRuleStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterDeleteRuleStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitDeleteRuleStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitDeleteRuleStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeleteRuleStmtContext deleteRuleStmt() throws RecognitionException {
		DeleteRuleStmtContext _localctx = new DeleteRuleStmtContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_deleteRuleStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(411);
			match(DELETE);
			setState(412);
			match(RULE);
			setState(413);
			((DeleteRuleStmtContext)_localctx).ruleName = expression();
			setState(414);
			match(FROM);
			setState(415);
			match(OBLIGATION);
			setState(416);
			((DeleteRuleStmtContext)_localctx).obligationName = expression();
			setState(417);
			match(SEMI_COLON);
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

	public static class CreateProhibitionStmtContext extends ParserRuleContext {
		public ExpressionContext name;
		public ExpressionContext subject;
		public ExpressionContext accessRights;
		public ProhibitionContainerListContext containers;
		public TerminalNode CREATE() { return getToken(PALParser.CREATE, 0); }
		public TerminalNode PROHIBITION() { return getToken(PALParser.PROHIBITION, 0); }
		public TerminalNode DENY() { return getToken(PALParser.DENY, 0); }
		public TerminalNode ACCESS_RIGHTS() { return getToken(PALParser.ACCESS_RIGHTS, 0); }
		public TerminalNode ON() { return getToken(PALParser.ON, 0); }
		public TerminalNode OF() { return getToken(PALParser.OF, 0); }
		public TerminalNode SEMI_COLON() { return getToken(PALParser.SEMI_COLON, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode USER() { return getToken(PALParser.USER, 0); }
		public TerminalNode USER_ATTRIBUTE() { return getToken(PALParser.USER_ATTRIBUTE, 0); }
		public TerminalNode PROCESS() { return getToken(PALParser.PROCESS, 0); }
		public TerminalNode INTERSECTION() { return getToken(PALParser.INTERSECTION, 0); }
		public TerminalNode UNION() { return getToken(PALParser.UNION, 0); }
		public ProhibitionContainerListContext prohibitionContainerList() {
			return getRuleContext(ProhibitionContainerListContext.class,0);
		}
		public CreateProhibitionStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_createProhibitionStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterCreateProhibitionStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitCreateProhibitionStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitCreateProhibitionStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreateProhibitionStmtContext createProhibitionStmt() throws RecognitionException {
		CreateProhibitionStmtContext _localctx = new CreateProhibitionStmtContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_createProhibitionStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(419);
			match(CREATE);
			setState(420);
			match(PROHIBITION);
			setState(421);
			((CreateProhibitionStmtContext)_localctx).name = expression();
			setState(422);
			match(DENY);
			setState(423);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PROCESS) | (1L << USER_ATTRIBUTE) | (1L << USER))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(424);
			((CreateProhibitionStmtContext)_localctx).subject = expression();
			setState(425);
			match(ACCESS_RIGHTS);
			setState(426);
			((CreateProhibitionStmtContext)_localctx).accessRights = expression();
			setState(427);
			match(ON);
			setState(428);
			_la = _input.LA(1);
			if ( !(_la==INTERSECTION || _la==UNION) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(429);
			match(OF);
			setState(430);
			((CreateProhibitionStmtContext)_localctx).containers = prohibitionContainerList();
			setState(431);
			match(SEMI_COLON);
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
		public TerminalNode OPEN_BRACKET() { return getToken(PALParser.OPEN_BRACKET, 0); }
		public TerminalNode CLOSE_BRACKET() { return getToken(PALParser.CLOSE_BRACKET, 0); }
		public List<ProhibitionContainerExpressionContext> prohibitionContainerExpression() {
			return getRuleContexts(ProhibitionContainerExpressionContext.class);
		}
		public ProhibitionContainerExpressionContext prohibitionContainerExpression(int i) {
			return getRuleContext(ProhibitionContainerExpressionContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(PALParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(PALParser.COMMA, i);
		}
		public ProhibitionContainerListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prohibitionContainerList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterProhibitionContainerList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitProhibitionContainerList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitProhibitionContainerList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProhibitionContainerListContext prohibitionContainerList() throws RecognitionException {
		ProhibitionContainerListContext _localctx = new ProhibitionContainerListContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_prohibitionContainerList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(433);
			match(OPEN_BRACKET);
			setState(442);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 44)) & ~0x3f) == 0 && ((1L << (_la - 44)) & ((1L << (BOOLEAN - 44)) | (1L << (NUMBER - 44)) | (1L << (VARIABLE_OR_FUNCTION_NAME - 44)) | (1L << (STRING - 44)) | (1L << (OPEN_CURLY - 44)) | (1L << (OPEN_BRACKET - 44)) | (1L << (IS_COMPLEMENT - 44)))) != 0)) {
				{
				setState(434);
				prohibitionContainerExpression();
				setState(439);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(435);
					match(COMMA);
					setState(436);
					prohibitionContainerExpression();
					}
					}
					setState(441);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(444);
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
		public TerminalNode IS_COMPLEMENT() { return getToken(PALParser.IS_COMPLEMENT, 0); }
		public ProhibitionContainerExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prohibitionContainerExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterProhibitionContainerExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitProhibitionContainerExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitProhibitionContainerExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProhibitionContainerExpressionContext prohibitionContainerExpression() throws RecognitionException {
		ProhibitionContainerExpressionContext _localctx = new ProhibitionContainerExpressionContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_prohibitionContainerExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(447);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IS_COMPLEMENT) {
				{
				setState(446);
				match(IS_COMPLEMENT);
				}
			}

			setState(449);
			((ProhibitionContainerExpressionContext)_localctx).container = expression();
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

	public static class SetResourceAccessRightsStmtContext extends ParserRuleContext {
		public ExpressionContext accessRights;
		public TerminalNode SET_RESOURCE_ACCESS_RIGHTS() { return getToken(PALParser.SET_RESOURCE_ACCESS_RIGHTS, 0); }
		public TerminalNode SEMI_COLON() { return getToken(PALParser.SEMI_COLON, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public SetResourceAccessRightsStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_setResourceAccessRightsStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterSetResourceAccessRightsStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitSetResourceAccessRightsStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitSetResourceAccessRightsStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SetResourceAccessRightsStmtContext setResourceAccessRightsStmt() throws RecognitionException {
		SetResourceAccessRightsStmtContext _localctx = new SetResourceAccessRightsStmtContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_setResourceAccessRightsStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(451);
			match(SET_RESOURCE_ACCESS_RIGHTS);
			setState(452);
			((SetResourceAccessRightsStmtContext)_localctx).accessRights = expression();
			setState(453);
			match(SEMI_COLON);
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
		public VarRefContext varRef() {
			return getRuleContext(VarRefContext.class,0);
		}
		public FuncCallContext funcCall() {
			return getRuleContext(FuncCallContext.class,0);
		}
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_expression);
		try {
			setState(458);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(455);
				varRef();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(456);
				funcCall();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(457);
				literal();
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

	public static class ArrayContext extends ParserRuleContext {
		public TerminalNode OPEN_BRACKET() { return getToken(PALParser.OPEN_BRACKET, 0); }
		public TerminalNode CLOSE_BRACKET() { return getToken(PALParser.CLOSE_BRACKET, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(PALParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(PALParser.COMMA, i);
		}
		public ArrayContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_array; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterArray(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitArray(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitArray(this);
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
			setState(460);
			match(OPEN_BRACKET);
			setState(469);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 44)) & ~0x3f) == 0 && ((1L << (_la - 44)) & ((1L << (BOOLEAN - 44)) | (1L << (NUMBER - 44)) | (1L << (VARIABLE_OR_FUNCTION_NAME - 44)) | (1L << (STRING - 44)) | (1L << (OPEN_CURLY - 44)) | (1L << (OPEN_BRACKET - 44)))) != 0)) {
				{
				setState(461);
				expression();
				setState(466);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(462);
					match(COMMA);
					setState(463);
					expression();
					}
					}
					setState(468);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(471);
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
		public TerminalNode OPEN_CURLY() { return getToken(PALParser.OPEN_CURLY, 0); }
		public TerminalNode CLOSE_CURLY() { return getToken(PALParser.CLOSE_CURLY, 0); }
		public List<MapEntryContext> mapEntry() {
			return getRuleContexts(MapEntryContext.class);
		}
		public MapEntryContext mapEntry(int i) {
			return getRuleContext(MapEntryContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(PALParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(PALParser.COMMA, i);
		}
		public MapContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_map; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterMap(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitMap(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitMap(this);
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
			setState(473);
			match(OPEN_CURLY);
			setState(482);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 44)) & ~0x3f) == 0 && ((1L << (_la - 44)) & ((1L << (BOOLEAN - 44)) | (1L << (NUMBER - 44)) | (1L << (VARIABLE_OR_FUNCTION_NAME - 44)) | (1L << (STRING - 44)) | (1L << (OPEN_CURLY - 44)) | (1L << (OPEN_BRACKET - 44)))) != 0)) {
				{
				setState(474);
				mapEntry();
				setState(479);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(475);
					match(COMMA);
					setState(476);
					mapEntry();
					}
					}
					setState(481);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(484);
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
		public TerminalNode COLON() { return getToken(PALParser.COLON, 0); }
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
			if ( listener instanceof PALListener ) ((PALListener)listener).enterMapEntry(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitMapEntry(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitMapEntry(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MapEntryContext mapEntry() throws RecognitionException {
		MapEntryContext _localctx = new MapEntryContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_mapEntry);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(486);
			((MapEntryContext)_localctx).key = expression();
			setState(487);
			match(COLON);
			setState(488);
			((MapEntryContext)_localctx).value = expression();
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

	public static class EntryRefContext extends ParserRuleContext {
		public ExpressionContext key;
		public TerminalNode VARIABLE_OR_FUNCTION_NAME() { return getToken(PALParser.VARIABLE_OR_FUNCTION_NAME, 0); }
		public List<TerminalNode> OPEN_BRACKET() { return getTokens(PALParser.OPEN_BRACKET); }
		public TerminalNode OPEN_BRACKET(int i) {
			return getToken(PALParser.OPEN_BRACKET, i);
		}
		public List<TerminalNode> CLOSE_BRACKET() { return getTokens(PALParser.CLOSE_BRACKET); }
		public TerminalNode CLOSE_BRACKET(int i) {
			return getToken(PALParser.CLOSE_BRACKET, i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public EntryRefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_entryRef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterEntryRef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitEntryRef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitEntryRef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EntryRefContext entryRef() throws RecognitionException {
		EntryRefContext _localctx = new EntryRefContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_entryRef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(490);
			match(VARIABLE_OR_FUNCTION_NAME);
			setState(495); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(491);
				match(OPEN_BRACKET);
				setState(492);
				((EntryRefContext)_localctx).key = expression();
				setState(493);
				match(CLOSE_BRACKET);
				}
				}
				setState(497); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==OPEN_BRACKET );
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
			if ( listener instanceof PALListener ) ((PALListener)listener).enterMapLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitMapLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitMapLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class StringLiteralContext extends LiteralContext {
		public TerminalNode STRING() { return getToken(PALParser.STRING, 0); }
		public StringLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterStringLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitStringLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitStringLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BooleanLiteralContext extends LiteralContext {
		public TerminalNode BOOLEAN() { return getToken(PALParser.BOOLEAN, 0); }
		public BooleanLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterBooleanLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitBooleanLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitBooleanLiteral(this);
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
			if ( listener instanceof PALListener ) ((PALListener)listener).enterArrayLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitArrayLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitArrayLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NumberLiteralContext extends LiteralContext {
		public TerminalNode NUMBER() { return getToken(PALParser.NUMBER, 0); }
		public NumberLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterNumberLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitNumberLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitNumberLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_literal);
		try {
			setState(504);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STRING:
				_localctx = new StringLiteralContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(499);
				match(STRING);
				}
				break;
			case BOOLEAN:
				_localctx = new BooleanLiteralContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(500);
				match(BOOLEAN);
				}
				break;
			case NUMBER:
				_localctx = new NumberLiteralContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(501);
				match(NUMBER);
				}
				break;
			case OPEN_BRACKET:
				_localctx = new ArrayLiteralContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(502);
				array();
				}
				break;
			case OPEN_CURLY:
				_localctx = new MapLiteralContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(503);
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

	public static class VarRefContext extends ParserRuleContext {
		public VarRefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varRef; }
	 
		public VarRefContext() { }
		public void copyFrom(VarRefContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class EntryReferenceContext extends VarRefContext {
		public EntryRefContext entryRef() {
			return getRuleContext(EntryRefContext.class,0);
		}
		public EntryReferenceContext(VarRefContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterEntryReference(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitEntryReference(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitEntryReference(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ReferenceByIDContext extends VarRefContext {
		public TerminalNode VARIABLE_OR_FUNCTION_NAME() { return getToken(PALParser.VARIABLE_OR_FUNCTION_NAME, 0); }
		public ReferenceByIDContext(VarRefContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterReferenceByID(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitReferenceByID(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitReferenceByID(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarRefContext varRef() throws RecognitionException {
		VarRefContext _localctx = new VarRefContext(_ctx, getState());
		enterRule(_localctx, 104, RULE_varRef);
		try {
			setState(508);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,33,_ctx) ) {
			case 1:
				_localctx = new ReferenceByIDContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(506);
				match(VARIABLE_OR_FUNCTION_NAME);
				}
				break;
			case 2:
				_localctx = new EntryReferenceContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(507);
				entryRef();
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

	public static class FuncCallContext extends ParserRuleContext {
		public TerminalNode VARIABLE_OR_FUNCTION_NAME() { return getToken(PALParser.VARIABLE_OR_FUNCTION_NAME, 0); }
		public FuncCallArgsContext funcCallArgs() {
			return getRuleContext(FuncCallArgsContext.class,0);
		}
		public FuncCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funcCall; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterFuncCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitFuncCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitFuncCall(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FuncCallContext funcCall() throws RecognitionException {
		FuncCallContext _localctx = new FuncCallContext(_ctx, getState());
		enterRule(_localctx, 106, RULE_funcCall);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(510);
			match(VARIABLE_OR_FUNCTION_NAME);
			setState(511);
			funcCallArgs();
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

	public static class FuncCallArgsContext extends ParserRuleContext {
		public TerminalNode OPEN_PAREN() { return getToken(PALParser.OPEN_PAREN, 0); }
		public TerminalNode CLOSE_PAREN() { return getToken(PALParser.CLOSE_PAREN, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(PALParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(PALParser.COMMA, i);
		}
		public FuncCallArgsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funcCallArgs; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterFuncCallArgs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitFuncCallArgs(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitFuncCallArgs(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FuncCallArgsContext funcCallArgs() throws RecognitionException {
		FuncCallArgsContext _localctx = new FuncCallArgsContext(_ctx, getState());
		enterRule(_localctx, 108, RULE_funcCallArgs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(513);
			match(OPEN_PAREN);
			setState(522);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 44)) & ~0x3f) == 0 && ((1L << (_la - 44)) & ((1L << (BOOLEAN - 44)) | (1L << (NUMBER - 44)) | (1L << (VARIABLE_OR_FUNCTION_NAME - 44)) | (1L << (STRING - 44)) | (1L << (OPEN_CURLY - 44)) | (1L << (OPEN_BRACKET - 44)))) != 0)) {
				{
				setState(514);
				expression();
				setState(519);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(515);
					match(COMMA);
					setState(516);
					expression();
					}
					}
					setState(521);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(524);
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

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3O\u0211\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\3\2\7\2r\n\2\f\2\16\2u\13\2\3"+
		"\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u008f\n\3\3\4\5\4\u0092\n\4\3\4\3\4\3\4\3"+
		"\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\5\5\u009f\n\5\3\5\3\5\3\6\3\6\3\6\7\6\u00a6"+
		"\n\6\f\6\16\6\u00a9\13\6\5\6\u00ab\n\6\3\7\3\7\3\7\3\b\3\b\3\t\3\t\3\t"+
		"\5\t\u00b5\n\t\3\t\3\t\3\n\3\n\5\n\u00bb\n\n\3\13\3\13\7\13\u00bf\n\13"+
		"\f\13\16\13\u00c2\13\13\3\13\3\13\3\f\3\f\3\f\3\f\5\f\u00ca\n\f\3\f\3"+
		"\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\17"+
		"\3\17\3\17\3\20\3\20\3\20\3\21\3\21\5\21\u00e5\n\21\3\21\3\21\3\21\7\21"+
		"\u00ea\n\21\f\21\16\21\u00ed\13\21\3\21\5\21\u00f0\n\21\3\22\3\22\3\22"+
		"\5\22\u00f5\n\22\3\22\3\22\3\22\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24"+
		"\5\24\u0102\n\24\3\25\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\27"+
		"\3\27\7\27\u0110\n\27\f\27\16\27\u0113\13\27\3\27\3\27\3\30\3\30\3\30"+
		"\5\30\u011a\n\30\3\31\3\31\3\32\3\32\3\32\3\32\3\32\3\33\3\33\3\33\3\33"+
		"\3\33\3\33\3\33\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\35\3\35\3\35\3\35"+
		"\3\35\3\35\3\35\3\36\3\36\3\36\3\36\3\36\3\36\3\37\3\37\3\37\3\37\3\37"+
		"\3\37\3 \3 \3 \3 \3 \3 \3 \3 \3!\3!\3!\3!\3!\3!\3\"\3\"\3\"\3\"\3\"\3"+
		"#\3#\3#\3#\3#\7#\u015c\n#\f#\16#\u015f\13#\3#\3#\3$\3$\3$\3$\3$\3$\3$"+
		"\3$\3$\5$\u016c\n$\3$\3$\3%\3%\3%\3%\3%\3%\3%\3%\3%\5%\u0179\n%\3&\3&"+
		"\3&\3&\3&\3&\3&\3&\3&\3&\5&\u0185\n&\3\'\3\'\3\'\3(\3(\3(\3(\3(\3(\3)"+
		"\3)\7)\u0192\n)\f)\16)\u0195\13)\3)\3)\3*\3*\3*\5*\u019c\n*\3+\3+\3+\3"+
		"+\3+\3+\3+\3+\3,\3,\3,\3,\3,\3,\3,\3,\3,\3,\3,\3,\3,\3,\3-\3-\3-\3-\7"+
		"-\u01b8\n-\f-\16-\u01bb\13-\5-\u01bd\n-\3-\3-\3.\5.\u01c2\n.\3.\3.\3/"+
		"\3/\3/\3/\3\60\3\60\3\60\5\60\u01cd\n\60\3\61\3\61\3\61\3\61\7\61\u01d3"+
		"\n\61\f\61\16\61\u01d6\13\61\5\61\u01d8\n\61\3\61\3\61\3\62\3\62\3\62"+
		"\3\62\7\62\u01e0\n\62\f\62\16\62\u01e3\13\62\5\62\u01e5\n\62\3\62\3\62"+
		"\3\63\3\63\3\63\3\63\3\64\3\64\3\64\3\64\3\64\6\64\u01f2\n\64\r\64\16"+
		"\64\u01f3\3\65\3\65\3\65\3\65\3\65\5\65\u01fb\n\65\3\66\3\66\5\66\u01ff"+
		"\n\66\3\67\3\67\3\67\38\38\38\38\78\u0208\n8\f8\168\u020b\138\58\u020d"+
		"\n8\38\38\38\2\29\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62"+
		"\64\668:<>@BDFHJLNPRTVXZ\\^`bdfhjln\2\b\3\2*+\3\2#\'\3\2$%\3\2&\'\5\2"+
		"\21\21%%\'\'\3\2\22\23\2\u021f\2s\3\2\2\2\4\u008e\3\2\2\2\6\u0091\3\2"+
		"\2\2\b\u0098\3\2\2\2\n\u00aa\3\2\2\2\f\u00ac\3\2\2\2\16\u00af\3\2\2\2"+
		"\20\u00b4\3\2\2\2\22\u00ba\3\2\2\2\24\u00bc\3\2\2\2\26\u00c5\3\2\2\2\30"+
		"\u00cf\3\2\2\2\32\u00d9\3\2\2\2\34\u00dc\3\2\2\2\36\u00df\3\2\2\2 \u00e2"+
		"\3\2\2\2\"\u00f1\3\2\2\2$\u00f9\3\2\2\2&\u0101\3\2\2\2(\u0103\3\2\2\2"+
		"*\u0109\3\2\2\2,\u010d\3\2\2\2.\u0119\3\2\2\2\60\u011b\3\2\2\2\62\u011d"+
		"\3\2\2\2\64\u0122\3\2\2\2\66\u0129\3\2\2\28\u0130\3\2\2\2:\u0137\3\2\2"+
		"\2<\u013d\3\2\2\2>\u0143\3\2\2\2@\u014b\3\2\2\2B\u0151\3\2\2\2D\u0156"+
		"\3\2\2\2F\u0162\3\2\2\2H\u0178\3\2\2\2J\u0184\3\2\2\2L\u0186\3\2\2\2N"+
		"\u0189\3\2\2\2P\u018f\3\2\2\2R\u019b\3\2\2\2T\u019d\3\2\2\2V\u01a5\3\2"+
		"\2\2X\u01b3\3\2\2\2Z\u01c1\3\2\2\2\\\u01c5\3\2\2\2^\u01cc\3\2\2\2`\u01ce"+
		"\3\2\2\2b\u01db\3\2\2\2d\u01e8\3\2\2\2f\u01ec\3\2\2\2h\u01fa\3\2\2\2j"+
		"\u01fe\3\2\2\2l\u0200\3\2\2\2n\u0203\3\2\2\2pr\5\4\3\2qp\3\2\2\2ru\3\2"+
		"\2\2sq\3\2\2\2st\3\2\2\2tv\3\2\2\2us\3\2\2\2vw\7\2\2\3w\3\3\2\2\2x\u008f"+
		"\5\6\4\2y\u008f\5\b\5\2z\u008f\5\20\t\2{\u008f\5\26\f\2|\u008f\5\30\r"+
		"\2}\u008f\5\32\16\2~\u008f\5\34\17\2\177\u008f\5\36\20\2\u0080\u008f\5"+
		" \21\2\u0081\u008f\5\62\32\2\u0082\u008f\5\64\33\2\u0083\u008f\5\66\34"+
		"\2\u0084\u008f\5D#\2\u0085\u008f\5V,\2\u0086\u008f\58\35\2\u0087\u008f"+
		"\5:\36\2\u0088\u008f\5<\37\2\u0089\u008f\5B\"\2\u008a\u008f\5> \2\u008b"+
		"\u008f\5@!\2\u008c\u008f\5\\/\2\u008d\u008f\5T+\2\u008ex\3\2\2\2\u008e"+
		"y\3\2\2\2\u008ez\3\2\2\2\u008e{\3\2\2\2\u008e|\3\2\2\2\u008e}\3\2\2\2"+
		"\u008e~\3\2\2\2\u008e\177\3\2\2\2\u008e\u0080\3\2\2\2\u008e\u0081\3\2"+
		"\2\2\u008e\u0082\3\2\2\2\u008e\u0083\3\2\2\2\u008e\u0084\3\2\2\2\u008e"+
		"\u0085\3\2\2\2\u008e\u0086\3\2\2\2\u008e\u0087\3\2\2\2\u008e\u0088\3\2"+
		"\2\2\u008e\u0089\3\2\2\2\u008e\u008a\3\2\2\2\u008e\u008b\3\2\2\2\u008e"+
		"\u008c\3\2\2\2\u008e\u008d\3\2\2\2\u008f\5\3\2\2\2\u0090\u0092\t\2\2\2"+
		"\u0091\u0090\3\2\2\2\u0091\u0092\3\2\2\2\u0092\u0093\3\2\2\2\u0093\u0094"+
		"\7=\2\2\u0094\u0095\7O\2\2\u0095\u0096\5^\60\2\u0096\u0097\7E\2\2\u0097"+
		"\7\3\2\2\2\u0098\u0099\7,\2\2\u0099\u009a\7=\2\2\u009a\u009b\7L\2\2\u009b"+
		"\u009c\5\n\6\2\u009c\u009e\7M\2\2\u009d\u009f\5\22\n\2\u009e\u009d\3\2"+
		"\2\2\u009e\u009f\3\2\2\2\u009f\u00a0\3\2\2\2\u00a0\u00a1\5\24\13\2\u00a1"+
		"\t\3\2\2\2\u00a2\u00a7\5\f\7\2\u00a3\u00a4\7C\2\2\u00a4\u00a6\5\f\7\2"+
		"\u00a5\u00a3\3\2\2\2\u00a6\u00a9\3\2\2\2\u00a7\u00a5\3\2\2\2\u00a7\u00a8"+
		"\3\2\2\2\u00a8\u00ab\3\2\2\2\u00a9\u00a7\3\2\2\2\u00aa\u00a2\3\2\2\2\u00aa"+
		"\u00ab\3\2\2\2\u00ab\13\3\2\2\2\u00ac\u00ad\5\16\b\2\u00ad\u00ae\7=\2"+
		"\2\u00ae\r\3\2\2\2\u00af\u00b0\5&\24\2\u00b0\17\3\2\2\2\u00b1\u00b2\7"+
		"-\2\2\u00b2\u00b5\5^\60\2\u00b3\u00b5\7-\2\2\u00b4\u00b1\3\2\2\2\u00b4"+
		"\u00b3\3\2\2\2\u00b5\u00b6\3\2\2\2\u00b6\u00b7\7E\2\2\u00b7\21\3\2\2\2"+
		"\u00b8\u00bb\5&\24\2\u00b9\u00bb\7\63\2\2\u00ba\u00b8\3\2\2\2\u00ba\u00b9"+
		"\3\2\2\2\u00bb\23\3\2\2\2\u00bc\u00c0\7F\2\2\u00bd\u00bf\5\4\3\2\u00be"+
		"\u00bd\3\2\2\2\u00bf\u00c2\3\2\2\2\u00c0\u00be\3\2\2\2\u00c0\u00c1\3\2"+
		"\2\2\u00c1\u00c3\3\2\2\2\u00c2\u00c0\3\2\2\2\u00c3\u00c4\7G\2\2\u00c4"+
		"\25\3\2\2\2\u00c5\u00c6\7\66\2\2\u00c6\u00c9\7=\2\2\u00c7\u00c8\7C\2\2"+
		"\u00c8\u00ca\7=\2\2\u00c9\u00c7\3\2\2\2\u00c9\u00ca\3\2\2\2\u00ca\u00cb"+
		"\3\2\2\2\u00cb\u00cc\78\2\2\u00cc\u00cd\5^\60\2\u00cd\u00ce\5,\27\2\u00ce"+
		"\27\3\2\2\2\u00cf\u00d0\7\67\2\2\u00d0\u00d1\7=\2\2\u00d1\u00d2\7;\2\2"+
		"\u00d2\u00d3\7H\2\2\u00d3\u00d4\7<\2\2\u00d4\u00d5\7C\2\2\u00d5\u00d6"+
		"\7<\2\2\u00d6\u00d7\7I\2\2\u00d7\u00d8\5,\27\2\u00d8\31\3\2\2\2\u00d9"+
		"\u00da\7\5\2\2\u00da\u00db\7E\2\2\u00db\33\3\2\2\2\u00dc\u00dd\7\6\2\2"+
		"\u00dd\u00de\7E\2\2\u00de\35\3\2\2\2\u00df\u00e0\5l\67\2\u00e0\u00e1\7"+
		"E\2\2\u00e1\37\3\2\2\2\u00e2\u00e4\79\2\2\u00e3\u00e5\7N\2\2\u00e4\u00e3"+
		"\3\2\2\2\u00e4\u00e5\3\2\2\2\u00e5\u00e6\3\2\2\2\u00e6\u00e7\5^\60\2\u00e7"+
		"\u00eb\5,\27\2\u00e8\u00ea\5\"\22\2\u00e9\u00e8\3\2\2\2\u00ea\u00ed\3"+
		"\2\2\2\u00eb\u00e9\3\2\2\2\u00eb\u00ec\3\2\2\2\u00ec\u00ef\3\2\2\2\u00ed"+
		"\u00eb\3\2\2\2\u00ee\u00f0\5$\23\2\u00ef\u00ee\3\2\2\2\u00ef\u00f0\3\2"+
		"\2\2\u00f0!\3\2\2\2\u00f1\u00f2\7:\2\2\u00f2\u00f4\79\2\2\u00f3\u00f5"+
		"\7N\2\2\u00f4\u00f3\3\2\2\2\u00f4\u00f5\3\2\2\2\u00f5\u00f6\3\2\2\2\u00f6"+
		"\u00f7\5^\60\2\u00f7\u00f8\5,\27\2\u00f8#\3\2\2\2\u00f9\u00fa\7:\2\2\u00fa"+
		"\u00fb\5,\27\2\u00fb%\3\2\2\2\u00fc\u0102\7\61\2\2\u00fd\u0102\7\62\2"+
		"\2\u00fe\u0102\5*\26\2\u00ff\u0102\5(\25\2\u0100\u0102\7)\2\2\u0101\u00fc"+
		"\3\2\2\2\u0101\u00fd\3\2\2\2\u0101\u00fe\3\2\2\2\u0101\u00ff\3\2\2\2\u0101"+
		"\u0100\3\2\2\2\u0102\'\3\2\2\2\u0103\u0104\7\65\2\2\u0104\u0105\7H\2\2"+
		"\u0105\u0106\5&\24\2\u0106\u0107\7I\2\2\u0107\u0108\5&\24\2\u0108)\3\2"+
		"\2\2\u0109\u010a\7H\2\2\u010a\u010b\7I\2\2\u010b\u010c\5&\24\2\u010c+"+
		"\3\2\2\2\u010d\u0111\7F\2\2\u010e\u0110\5\4\3\2\u010f\u010e\3\2\2\2\u0110"+
		"\u0113\3\2\2\2\u0111\u010f\3\2\2\2\u0111\u0112\3\2\2\2\u0112\u0114\3\2"+
		"\2\2\u0113\u0111\3\2\2\2\u0114\u0115\7G\2\2\u0115-\3\2\2\2\u0116\u011a"+
		"\5\60\31\2\u0117\u011a\7!\2\2\u0118\u011a\7 \2\2\u0119\u0116\3\2\2\2\u0119"+
		"\u0117\3\2\2\2\u0119\u0118\3\2\2\2\u011a/\3\2\2\2\u011b\u011c\t\3\2\2"+
		"\u011c\61\3\2\2\2\u011d\u011e\7\3\2\2\u011e\u011f\7#\2\2\u011f\u0120\5"+
		"^\60\2\u0120\u0121\7E\2\2\u0121\63\3\2\2\2\u0122\u0123\7\3\2\2\u0123\u0124"+
		"\t\4\2\2\u0124\u0125\5^\60\2\u0125\u0126\78\2\2\u0126\u0127\5^\60\2\u0127"+
		"\u0128\7E\2\2\u0128\65\3\2\2\2\u0129\u012a\7\3\2\2\u012a\u012b\t\5\2\2"+
		"\u012b\u012c\5^\60\2\u012c\u012d\78\2\2\u012d\u012e\5^\60\2\u012e\u012f"+
		"\7E\2\2\u012f\67\3\2\2\2\u0130\u0131\7\30\2\2\u0131\u0132\7\31\2\2\u0132"+
		"\u0133\5^\60\2\u0133\u0134\7\32\2\2\u0134\u0135\5^\60\2\u0135\u0136\7"+
		"E\2\2\u01369\3\2\2\2\u0137\u0138\7\25\2\2\u0138\u0139\5^\60\2\u0139\u013a"+
		"\7\32\2\2\u013a\u013b\5^\60\2\u013b\u013c\7E\2\2\u013c;\3\2\2\2\u013d"+
		"\u013e\7\26\2\2\u013e\u013f\5^\60\2\u013f\u0140\7\27\2\2\u0140\u0141\5"+
		"^\60\2\u0141\u0142\7E\2\2\u0142=\3\2\2\2\u0143\u0144\7\33\2\2\u0144\u0145"+
		"\5^\60\2\u0145\u0146\7\34\2\2\u0146\u0147\5^\60\2\u0147\u0148\7\35\2\2"+
		"\u0148\u0149\5^\60\2\u0149\u014a\7E\2\2\u014a?\3\2\2\2\u014b\u014c\7\36"+
		"\2\2\u014c\u014d\5^\60\2\u014d\u014e\7\34\2\2\u014e\u014f\5^\60\2\u014f"+
		"\u0150\7E\2\2\u0150A\3\2\2\2\u0151\u0152\7\4\2\2\u0152\u0153\5.\30\2\u0153"+
		"\u0154\5^\60\2\u0154\u0155\7E\2\2\u0155C\3\2\2\2\u0156\u0157\7\3\2\2\u0157"+
		"\u0158\7!\2\2\u0158\u0159\5^\60\2\u0159\u015d\7F\2\2\u015a\u015c\5F$\2"+
		"\u015b\u015a\3\2\2\2\u015c\u015f\3\2\2\2\u015d\u015b\3\2\2\2\u015d\u015e"+
		"\3\2\2\2\u015e\u0160\3\2\2\2\u015f\u015d\3\2\2\2\u0160\u0161\7G\2\2\u0161"+
		"E\3\2\2\2\u0162\u0163\7\3\2\2\u0163\u0164\7\b\2\2\u0164\u0165\5^\60\2"+
		"\u0165\u0166\7\t\2\2\u0166\u0167\5H%\2\u0167\u0168\7\n\2\2\u0168\u016b"+
		"\5^\60\2\u0169\u016a\7\f\2\2\u016a\u016c\5J&\2\u016b\u0169\3\2\2\2\u016b"+
		"\u016c\3\2\2\2\u016c\u016d\3\2\2\2\u016d\u016e\5N(\2\u016eG\3\2\2\2\u016f"+
		"\u0179\7\16\2\2\u0170\u0171\7\'\2\2\u0171\u0179\5^\60\2\u0172\u0173\7"+
		"\17\2\2\u0173\u0179\5^\60\2\u0174\u0175\7\20\2\2\u0175\u0179\5^\60\2\u0176"+
		"\u0177\7\21\2\2\u0177\u0179\5^\60\2\u0178\u016f\3\2\2\2\u0178\u0170\3"+
		"\2\2\2\u0178\u0172\3\2\2\2\u0178\u0174\3\2\2\2\u0178\u0176\3\2\2\2\u0179"+
		"I\3\2\2\2\u017a\u0185\5^\60\2\u017b\u0185\5L\'\2\u017c\u017d\5L\'\2\u017d"+
		"\u017e\78\2\2\u017e\u017f\5^\60\2\u017f\u0185\3\2\2\2\u0180\u0181\5L\'"+
		"\2\u0181\u0182\7\31\2\2\u0182\u0183\5^\60\2\u0183\u0185\3\2\2\2\u0184"+
		"\u017a\3\2\2\2\u0184\u017b\3\2\2\2\u0184\u017c\3\2\2\2\u0184\u0180\3\2"+
		"\2\2\u0185K\3\2\2\2\u0186\u0187\7)\2\2\u0187\u0188\7\7\2\2\u0188M\3\2"+
		"\2\2\u0189\u018a\7\r\2\2\u018a\u018b\7L\2\2\u018b\u018c\7=\2\2\u018c\u018d"+
		"\7M\2\2\u018d\u018e\5P)\2\u018eO\3\2\2\2\u018f\u0193\7F\2\2\u0190\u0192"+
		"\5R*\2\u0191\u0190\3\2\2\2\u0192\u0195\3\2\2\2\u0193\u0191\3\2\2\2\u0193"+
		"\u0194\3\2\2\2\u0194\u0196\3\2\2\2\u0195\u0193\3\2\2\2\u0196\u0197\7G"+
		"\2\2\u0197Q\3\2\2\2\u0198\u019c\5\4\3\2\u0199\u019c\5F$\2\u019a\u019c"+
		"\5T+\2\u019b\u0198\3\2\2\2\u019b\u0199\3\2\2\2\u019b\u019a\3\2\2\2\u019c"+
		"S\3\2\2\2\u019d\u019e\7\4\2\2\u019e\u019f\7\b\2\2\u019f\u01a0\5^\60\2"+
		"\u01a0\u01a1\7\27\2\2\u01a1\u01a2\7!\2\2\u01a2\u01a3\5^\60\2\u01a3\u01a4"+
		"\7E\2\2\u01a4U\3\2\2\2\u01a5\u01a6\7\3\2\2\u01a6\u01a7\7 \2\2\u01a7\u01a8"+
		"\5^\60\2\u01a8\u01a9\7\37\2\2\u01a9\u01aa\t\6\2\2\u01aa\u01ab\5^\60\2"+
		"\u01ab\u01ac\7\"\2\2\u01ac\u01ad\5^\60\2\u01ad\u01ae\7\f\2\2\u01ae\u01af"+
		"\t\7\2\2\u01af\u01b0\7\31\2\2\u01b0\u01b1\5X-\2\u01b1\u01b2\7E\2\2\u01b2"+
		"W\3\2\2\2\u01b3\u01bc\7H\2\2\u01b4\u01b9\5Z.\2\u01b5\u01b6\7C\2\2\u01b6"+
		"\u01b8\5Z.\2\u01b7\u01b5\3\2\2\2\u01b8\u01bb\3\2\2\2\u01b9\u01b7\3\2\2"+
		"\2\u01b9\u01ba\3\2\2\2\u01ba\u01bd\3\2\2\2\u01bb\u01b9\3\2\2\2\u01bc\u01b4"+
		"\3\2\2\2\u01bc\u01bd\3\2\2\2\u01bd\u01be\3\2\2\2\u01be\u01bf\7I\2\2\u01bf"+
		"Y\3\2\2\2\u01c0\u01c2\7N\2\2\u01c1\u01c0\3\2\2\2\u01c1\u01c2\3\2\2\2\u01c2"+
		"\u01c3\3\2\2\2\u01c3\u01c4\5^\60\2\u01c4[\3\2\2\2\u01c5\u01c6\7\24\2\2"+
		"\u01c6\u01c7\5^\60\2\u01c7\u01c8\7E\2\2\u01c8]\3\2\2\2\u01c9\u01cd\5j"+
		"\66\2\u01ca\u01cd\5l\67\2\u01cb\u01cd\5h\65\2\u01cc\u01c9\3\2\2\2\u01cc"+
		"\u01ca\3\2\2\2\u01cc\u01cb\3\2\2\2\u01cd_\3\2\2\2\u01ce\u01d7\7H\2\2\u01cf"+
		"\u01d4\5^\60\2\u01d0\u01d1\7C\2\2\u01d1\u01d3\5^\60\2\u01d2\u01d0\3\2"+
		"\2\2\u01d3\u01d6\3\2\2\2\u01d4\u01d2\3\2\2\2\u01d4\u01d5\3\2\2\2\u01d5"+
		"\u01d8\3\2\2\2\u01d6\u01d4\3\2\2\2\u01d7\u01cf\3\2\2\2\u01d7\u01d8\3\2"+
		"\2\2\u01d8\u01d9\3\2\2\2\u01d9\u01da\7I\2\2\u01daa\3\2\2\2\u01db\u01e4"+
		"\7F\2\2\u01dc\u01e1\5d\63\2\u01dd\u01de\7C\2\2\u01de\u01e0\5d\63\2\u01df"+
		"\u01dd\3\2\2\2\u01e0\u01e3\3\2\2\2\u01e1\u01df\3\2\2\2\u01e1\u01e2\3\2"+
		"\2\2\u01e2\u01e5\3\2\2\2\u01e3\u01e1\3\2\2\2\u01e4\u01dc\3\2\2\2\u01e4"+
		"\u01e5\3\2\2\2\u01e5\u01e6\3\2\2\2\u01e6\u01e7\7G\2\2\u01e7c\3\2\2\2\u01e8"+
		"\u01e9\5^\60\2\u01e9\u01ea\7D\2\2\u01ea\u01eb\5^\60\2\u01ebe\3\2\2\2\u01ec"+
		"\u01f1\7=\2\2\u01ed\u01ee\7H\2\2\u01ee\u01ef\5^\60\2\u01ef\u01f0\7I\2"+
		"\2\u01f0\u01f2\3\2\2\2\u01f1\u01ed\3\2\2\2\u01f2\u01f3\3\2\2\2\u01f3\u01f1"+
		"\3\2\2\2\u01f3\u01f4\3\2\2\2\u01f4g\3\2\2\2\u01f5\u01fb\7>\2\2\u01f6\u01fb"+
		"\7.\2\2\u01f7\u01fb\7<\2\2\u01f8\u01fb\5`\61\2\u01f9\u01fb\5b\62\2\u01fa"+
		"\u01f5\3\2\2\2\u01fa\u01f6\3\2\2\2\u01fa\u01f7\3\2\2\2\u01fa\u01f8\3\2"+
		"\2\2\u01fa\u01f9\3\2\2\2\u01fbi\3\2\2\2\u01fc\u01ff\7=\2\2\u01fd\u01ff"+
		"\5f\64\2\u01fe\u01fc\3\2\2\2\u01fe\u01fd\3\2\2\2\u01ffk\3\2\2\2\u0200"+
		"\u0201\7=\2\2\u0201\u0202\5n8\2\u0202m\3\2\2\2\u0203\u020c\7L\2\2\u0204"+
		"\u0209\5^\60\2\u0205\u0206\7C\2\2\u0206\u0208\5^\60\2\u0207\u0205\3\2"+
		"\2\2\u0208\u020b\3\2\2\2\u0209\u0207\3\2\2\2\u0209\u020a\3\2\2\2\u020a"+
		"\u020d\3\2\2\2\u020b\u0209\3\2\2\2\u020c\u0204\3\2\2\2\u020c\u020d\3\2"+
		"\2\2\u020d\u020e\3\2\2\2\u020e\u020f\7M\2\2\u020fo\3\2\2\2&s\u008e\u0091"+
		"\u009e\u00a7\u00aa\u00b4\u00ba\u00c0\u00c9\u00e4\u00eb\u00ef\u00f4\u0101"+
		"\u0111\u0119\u015d\u016b\u0178\u0184\u0193\u019b\u01b9\u01bc\u01c1\u01cc"+
		"\u01d4\u01d7\u01e1\u01e4\u01f3\u01fa\u01fe\u0209\u020c";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}