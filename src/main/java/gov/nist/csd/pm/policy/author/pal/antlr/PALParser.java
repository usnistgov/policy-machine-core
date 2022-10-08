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
		ALL_ACCESS_RIGHTS=1, ALL_RESOURCE_ACCESS_RIGHTS=2, ALL_ADMIN_ACCESS_RIGHTS=3, 
		CREATE=4, DELETE=5, BREAK=6, CONTINUE=7, POLICY_ELEMENT=8, RULE=9, WHEN=10, 
		PERFORMS=11, AS=12, EVENT=13, ON=14, DO=15, ANY_USER=16, USERS=17, ANY_USER_WITH_ATTRIBUTE=18, 
		PROCESS=19, INTERSECTION=20, UNION=21, SET_RESOURCE_ACCESS_RIGHTS=22, 
		ASSIGN=23, DEASSIGN=24, FROM=25, SET_PROPERTIES=26, OF=27, TO=28, ASSOCIATE=29, 
		AND=30, WITH=31, DISSOCIATE=32, DENY=33, PROHIBITION=34, OBLIGATION=35, 
		ACCESS_RIGHTS=36, POLICY_CLASS=37, OBJECT_ATTRIBUTE=38, USER_ATTRIBUTE=39, 
		OBJECT=40, USER=41, ATTR=42, ANY=43, LET=44, CONST=45, FUNCTION=46, RETURN=47, 
		BOOLEAN=48, TRUE=49, FALSE=50, STRING_TYPE=51, BOOLEAN_TYPE=52, VOID_TYPE=53, 
		ARRAY_TYPE=54, MAP_TYPE=55, FOREACH=56, FOR=57, IN=58, IF=59, ELSE=60, 
		IN_RANGE=61, NUMBER=62, IDENTIFIER=63, STRING=64, LINE_COMMENT=65, WS=66, 
		COMMA=67, COLON=68, SEMI_COLON=69, OPEN_CURLY=70, CLOSE_CURLY=71, OPEN_BRACKET=72, 
		CLOSE_BRACKET=73, OPEN_ANGLE_BRACKET=74, CLOSE_ANGLE_BRACKET=75, OPEN_PAREN=76, 
		CLOSE_PAREN=77, IS_COMPLEMENT=78, EQUALS=79;
	public static final int
		RULE_pal = 0, RULE_stmts = 1, RULE_stmt = 2, RULE_varStmt = 3, RULE_funcDefStmt = 4, 
		RULE_formalArgList = 5, RULE_formalArg = 6, RULE_formalArgType = 7, RULE_funcReturnStmt = 8, 
		RULE_funcReturnType = 9, RULE_funcBody = 10, RULE_foreachStmt = 11, RULE_forRangeStmt = 12, 
		RULE_breakStmt = 13, RULE_continueStmt = 14, RULE_funcCallStmt = 15, RULE_ifStmt = 16, 
		RULE_elseIfStmt = 17, RULE_elseStmt = 18, RULE_varType = 19, RULE_mapType = 20, 
		RULE_arrayType = 21, RULE_stmtBlock = 22, RULE_deleteType = 23, RULE_nodeType = 24, 
		RULE_createPolicyStmt = 25, RULE_createAttrStmt = 26, RULE_createUserOrObjectStmt = 27, 
		RULE_setNodePropsStmt = 28, RULE_assignStmt = 29, RULE_deassignStmt = 30, 
		RULE_associateStmt = 31, RULE_dissociateStmt = 32, RULE_deleteStmt = 33, 
		RULE_createObligationStmt = 34, RULE_createRuleStmt = 35, RULE_performsClause = 36, 
		RULE_performsEvent = 37, RULE_subjectClause = 38, RULE_onClause = 39, 
		RULE_anyPe = 40, RULE_response = 41, RULE_responseBlock = 42, RULE_responseStmt = 43, 
		RULE_deleteRuleStmt = 44, RULE_eventSpecificResponse = 45, RULE_createProhibitionStmt = 46, 
		RULE_prohibitionContainerList = 47, RULE_prohibitionContainerExpression = 48, 
		RULE_setResourceAccessRightsStmt = 49, RULE_expression = 50, RULE_nameExpressionArray = 51, 
		RULE_nameExpression = 52, RULE_array = 53, RULE_accessRightArray = 54, 
		RULE_accessRight = 55, RULE_map = 56, RULE_mapEntry = 57, RULE_mapEntryRef = 58, 
		RULE_literal = 59, RULE_varRef = 60, RULE_funcCall = 61, RULE_funcCallArgs = 62, 
		RULE_id = 63, RULE_keywordAsID = 64;
	private static String[] makeRuleNames() {
		return new String[] {
			"pal", "stmts", "stmt", "varStmt", "funcDefStmt", "formalArgList", "formalArg", 
			"formalArgType", "funcReturnStmt", "funcReturnType", "funcBody", "foreachStmt", 
			"forRangeStmt", "breakStmt", "continueStmt", "funcCallStmt", "ifStmt", 
			"elseIfStmt", "elseStmt", "varType", "mapType", "arrayType", "stmtBlock", 
			"deleteType", "nodeType", "createPolicyStmt", "createAttrStmt", "createUserOrObjectStmt", 
			"setNodePropsStmt", "assignStmt", "deassignStmt", "associateStmt", "dissociateStmt", 
			"deleteStmt", "createObligationStmt", "createRuleStmt", "performsClause", 
			"performsEvent", "subjectClause", "onClause", "anyPe", "response", "responseBlock", 
			"responseStmt", "deleteRuleStmt", "eventSpecificResponse", "createProhibitionStmt", 
			"prohibitionContainerList", "prohibitionContainerExpression", "setResourceAccessRightsStmt", 
			"expression", "nameExpressionArray", "nameExpression", "array", "accessRightArray", 
			"accessRight", "map", "mapEntry", "mapEntryRef", "literal", "varRef", 
			"funcCall", "funcCallArgs", "id", "keywordAsID"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'*'", "'*r'", "'*a'", null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, "':'", "';'", "'{'", 
			"'}'", "'['", "']'", "'<'", "'>'", "'('", "')'", "'!'", "'='"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "ALL_ACCESS_RIGHTS", "ALL_RESOURCE_ACCESS_RIGHTS", "ALL_ADMIN_ACCESS_RIGHTS", 
			"CREATE", "DELETE", "BREAK", "CONTINUE", "POLICY_ELEMENT", "RULE", "WHEN", 
			"PERFORMS", "AS", "EVENT", "ON", "DO", "ANY_USER", "USERS", "ANY_USER_WITH_ATTRIBUTE", 
			"PROCESS", "INTERSECTION", "UNION", "SET_RESOURCE_ACCESS_RIGHTS", "ASSIGN", 
			"DEASSIGN", "FROM", "SET_PROPERTIES", "OF", "TO", "ASSOCIATE", "AND", 
			"WITH", "DISSOCIATE", "DENY", "PROHIBITION", "OBLIGATION", "ACCESS_RIGHTS", 
			"POLICY_CLASS", "OBJECT_ATTRIBUTE", "USER_ATTRIBUTE", "OBJECT", "USER", 
			"ATTR", "ANY", "LET", "CONST", "FUNCTION", "RETURN", "BOOLEAN", "TRUE", 
			"FALSE", "STRING_TYPE", "BOOLEAN_TYPE", "VOID_TYPE", "ARRAY_TYPE", "MAP_TYPE", 
			"FOREACH", "FOR", "IN", "IF", "ELSE", "IN_RANGE", "NUMBER", "IDENTIFIER", 
			"STRING", "LINE_COMMENT", "WS", "COMMA", "COLON", "SEMI_COLON", "OPEN_CURLY", 
			"CLOSE_CURLY", "OPEN_BRACKET", "CLOSE_BRACKET", "OPEN_ANGLE_BRACKET", 
			"CLOSE_ANGLE_BRACKET", "OPEN_PAREN", "CLOSE_PAREN", "IS_COMPLEMENT", 
			"EQUALS"
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
		public StmtsContext stmts() {
			return getRuleContext(StmtsContext.class,0);
		}
		public TerminalNode EOF() { return getToken(PALParser.EOF, 0); }
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
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(130);
			stmts();
			setState(131);
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

	public static class StmtsContext extends ParserRuleContext {
		public List<StmtContext> stmt() {
			return getRuleContexts(StmtContext.class);
		}
		public StmtContext stmt(int i) {
			return getRuleContext(StmtContext.class,i);
		}
		public StmtsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stmts; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterStmts(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitStmts(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitStmts(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StmtsContext stmts() throws RecognitionException {
		StmtsContext _localctx = new StmtsContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_stmts);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(136);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ALL_ACCESS_RIGHTS) | (1L << ALL_RESOURCE_ACCESS_RIGHTS) | (1L << ALL_ADMIN_ACCESS_RIGHTS) | (1L << CREATE) | (1L << DELETE) | (1L << BREAK) | (1L << CONTINUE) | (1L << SET_RESOURCE_ACCESS_RIGHTS) | (1L << ASSIGN) | (1L << DEASSIGN) | (1L << SET_PROPERTIES) | (1L << ASSOCIATE) | (1L << DISSOCIATE) | (1L << DENY) | (1L << LET) | (1L << CONST) | (1L << FUNCTION) | (1L << RETURN) | (1L << FOREACH) | (1L << FOR) | (1L << IF) | (1L << IDENTIFIER))) != 0)) {
				{
				{
				setState(133);
				stmt();
				}
				}
				setState(138);
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
		enterRule(_localctx, 4, RULE_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(161);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				{
				setState(139);
				varStmt();
				}
				break;
			case 2:
				{
				setState(140);
				funcDefStmt();
				}
				break;
			case 3:
				{
				setState(141);
				funcReturnStmt();
				}
				break;
			case 4:
				{
				setState(142);
				foreachStmt();
				}
				break;
			case 5:
				{
				setState(143);
				forRangeStmt();
				}
				break;
			case 6:
				{
				setState(144);
				breakStmt();
				}
				break;
			case 7:
				{
				setState(145);
				continueStmt();
				}
				break;
			case 8:
				{
				setState(146);
				funcCallStmt();
				}
				break;
			case 9:
				{
				setState(147);
				ifStmt();
				}
				break;
			case 10:
				{
				setState(148);
				createPolicyStmt();
				}
				break;
			case 11:
				{
				setState(149);
				createAttrStmt();
				}
				break;
			case 12:
				{
				setState(150);
				createUserOrObjectStmt();
				}
				break;
			case 13:
				{
				setState(151);
				createObligationStmt();
				}
				break;
			case 14:
				{
				setState(152);
				createProhibitionStmt();
				}
				break;
			case 15:
				{
				setState(153);
				setNodePropsStmt();
				}
				break;
			case 16:
				{
				setState(154);
				assignStmt();
				}
				break;
			case 17:
				{
				setState(155);
				deassignStmt();
				}
				break;
			case 18:
				{
				setState(156);
				deleteStmt();
				}
				break;
			case 19:
				{
				setState(157);
				associateStmt();
				}
				break;
			case 20:
				{
				setState(158);
				dissociateStmt();
				}
				break;
			case 21:
				{
				setState(159);
				setResourceAccessRightsStmt();
				}
				break;
			case 22:
				{
				setState(160);
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
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
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
		enterRule(_localctx, 6, RULE_varStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(164);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LET || _la==CONST) {
				{
				setState(163);
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

			setState(166);
			id();
			setState(167);
			match(EQUALS);
			setState(168);
			expression();
			setState(169);
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
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
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
		enterRule(_localctx, 8, RULE_funcDefStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(171);
			match(FUNCTION);
			setState(172);
			id();
			setState(173);
			match(OPEN_PAREN);
			setState(174);
			formalArgList();
			setState(175);
			match(CLOSE_PAREN);
			setState(177);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 43)) & ~0x3f) == 0 && ((1L << (_la - 43)) & ((1L << (ANY - 43)) | (1L << (STRING_TYPE - 43)) | (1L << (BOOLEAN_TYPE - 43)) | (1L << (VOID_TYPE - 43)) | (1L << (MAP_TYPE - 43)) | (1L << (OPEN_BRACKET - 43)))) != 0)) {
				{
				setState(176);
				funcReturnType();
				}
			}

			setState(179);
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
		enterRule(_localctx, 10, RULE_formalArgList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(189);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 43)) & ~0x3f) == 0 && ((1L << (_la - 43)) & ((1L << (ANY - 43)) | (1L << (STRING_TYPE - 43)) | (1L << (BOOLEAN_TYPE - 43)) | (1L << (MAP_TYPE - 43)) | (1L << (OPEN_BRACKET - 43)))) != 0)) {
				{
				setState(181);
				formalArg();
				setState(186);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(182);
					match(COMMA);
					setState(183);
					formalArg();
					}
					}
					setState(188);
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
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
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
		enterRule(_localctx, 12, RULE_formalArg);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(191);
			formalArgType();
			setState(192);
			id();
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
		enterRule(_localctx, 14, RULE_formalArgType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(194);
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
		enterRule(_localctx, 16, RULE_funcReturnStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(199);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				{
				setState(196);
				match(RETURN);
				setState(197);
				expression();
				}
				break;
			case 2:
				{
				setState(198);
				match(RETURN);
				}
				break;
			}
			setState(201);
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
		enterRule(_localctx, 18, RULE_funcReturnType);
		try {
			setState(205);
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
				setState(203);
				varType();
				}
				break;
			case VOID_TYPE:
				_localctx = new VoidReturnTypeContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(204);
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
		enterRule(_localctx, 20, RULE_funcBody);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(207);
			match(OPEN_CURLY);
			setState(211);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ALL_ACCESS_RIGHTS) | (1L << ALL_RESOURCE_ACCESS_RIGHTS) | (1L << ALL_ADMIN_ACCESS_RIGHTS) | (1L << CREATE) | (1L << DELETE) | (1L << BREAK) | (1L << CONTINUE) | (1L << SET_RESOURCE_ACCESS_RIGHTS) | (1L << ASSIGN) | (1L << DEASSIGN) | (1L << SET_PROPERTIES) | (1L << ASSOCIATE) | (1L << DISSOCIATE) | (1L << DENY) | (1L << LET) | (1L << CONST) | (1L << FUNCTION) | (1L << RETURN) | (1L << FOREACH) | (1L << FOR) | (1L << IF) | (1L << IDENTIFIER))) != 0)) {
				{
				{
				setState(208);
				stmt();
				}
				}
				setState(213);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(214);
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
		public IdContext key;
		public IdContext mapValue;
		public TerminalNode FOREACH() { return getToken(PALParser.FOREACH, 0); }
		public TerminalNode IN() { return getToken(PALParser.IN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public StmtBlockContext stmtBlock() {
			return getRuleContext(StmtBlockContext.class,0);
		}
		public List<IdContext> id() {
			return getRuleContexts(IdContext.class);
		}
		public IdContext id(int i) {
			return getRuleContext(IdContext.class,i);
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
		enterRule(_localctx, 22, RULE_foreachStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(216);
			match(FOREACH);
			setState(217);
			((ForeachStmtContext)_localctx).key = id();
			setState(220);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(218);
				match(COMMA);
				setState(219);
				((ForeachStmtContext)_localctx).mapValue = id();
				}
			}

			setState(222);
			match(IN);
			setState(223);
			expression();
			setState(224);
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
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
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
		enterRule(_localctx, 24, RULE_forRangeStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(226);
			match(FOR);
			setState(227);
			id();
			setState(228);
			match(IN_RANGE);
			setState(229);
			match(OPEN_BRACKET);
			setState(230);
			((ForRangeStmtContext)_localctx).lower = match(NUMBER);
			setState(231);
			match(COMMA);
			setState(232);
			((ForRangeStmtContext)_localctx).upper = match(NUMBER);
			setState(233);
			match(CLOSE_BRACKET);
			setState(234);
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
		enterRule(_localctx, 26, RULE_breakStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(236);
			match(BREAK);
			setState(237);
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
		enterRule(_localctx, 28, RULE_continueStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(239);
			match(CONTINUE);
			setState(240);
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
		enterRule(_localctx, 30, RULE_funcCallStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(242);
			funcCall();
			setState(243);
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
		enterRule(_localctx, 32, RULE_ifStmt);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(245);
			match(IF);
			setState(246);
			((IfStmtContext)_localctx).condition = expression();
			setState(247);
			stmtBlock();
			setState(251);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(248);
					elseIfStmt();
					}
					} 
				}
				setState(253);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			}
			setState(255);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ELSE) {
				{
				setState(254);
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
		enterRule(_localctx, 34, RULE_elseIfStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(257);
			match(ELSE);
			setState(258);
			match(IF);
			setState(259);
			((ElseIfStmtContext)_localctx).condition = expression();
			setState(260);
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
		enterRule(_localctx, 36, RULE_elseStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(262);
			match(ELSE);
			setState(263);
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
		enterRule(_localctx, 38, RULE_varType);
		try {
			setState(270);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STRING_TYPE:
				_localctx = new StringTypeContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(265);
				match(STRING_TYPE);
				}
				break;
			case BOOLEAN_TYPE:
				_localctx = new BooleanTypeContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(266);
				match(BOOLEAN_TYPE);
				}
				break;
			case OPEN_BRACKET:
				_localctx = new ArrayVarTypeContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(267);
				arrayType();
				}
				break;
			case MAP_TYPE:
				_localctx = new MapVarTypeContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(268);
				mapType();
				}
				break;
			case ANY:
				_localctx = new AnyTypeContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(269);
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
		enterRule(_localctx, 40, RULE_mapType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(272);
			match(MAP_TYPE);
			setState(273);
			match(OPEN_BRACKET);
			setState(274);
			((MapTypeContext)_localctx).keyType = varType();
			setState(275);
			match(CLOSE_BRACKET);
			setState(276);
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
		enterRule(_localctx, 42, RULE_arrayType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(278);
			match(OPEN_BRACKET);
			setState(279);
			match(CLOSE_BRACKET);
			setState(280);
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
		enterRule(_localctx, 44, RULE_stmtBlock);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(282);
			match(OPEN_CURLY);
			setState(286);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ALL_ACCESS_RIGHTS) | (1L << ALL_RESOURCE_ACCESS_RIGHTS) | (1L << ALL_ADMIN_ACCESS_RIGHTS) | (1L << CREATE) | (1L << DELETE) | (1L << BREAK) | (1L << CONTINUE) | (1L << SET_RESOURCE_ACCESS_RIGHTS) | (1L << ASSIGN) | (1L << DEASSIGN) | (1L << SET_PROPERTIES) | (1L << ASSOCIATE) | (1L << DISSOCIATE) | (1L << DENY) | (1L << LET) | (1L << CONST) | (1L << FUNCTION) | (1L << RETURN) | (1L << FOREACH) | (1L << FOR) | (1L << IF) | (1L << IDENTIFIER))) != 0)) {
				{
				{
				setState(283);
				stmt();
				}
				}
				setState(288);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(289);
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
		enterRule(_localctx, 46, RULE_deleteType);
		try {
			setState(294);
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
				setState(291);
				nodeType();
				}
				break;
			case OBLIGATION:
				_localctx = new DeleteObligationContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(292);
				match(OBLIGATION);
				}
				break;
			case PROHIBITION:
				_localctx = new DeleteProhibitionContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(293);
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
		enterRule(_localctx, 48, RULE_nodeType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(296);
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
		public NameExpressionContext nameExpression() {
			return getRuleContext(NameExpressionContext.class,0);
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
		enterRule(_localctx, 50, RULE_createPolicyStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(298);
			match(CREATE);
			setState(299);
			match(POLICY_CLASS);
			setState(300);
			nameExpression();
			setState(301);
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
		public TerminalNode CREATE() { return getToken(PALParser.CREATE, 0); }
		public NameExpressionContext nameExpression() {
			return getRuleContext(NameExpressionContext.class,0);
		}
		public TerminalNode IN() { return getToken(PALParser.IN, 0); }
		public NameExpressionArrayContext nameExpressionArray() {
			return getRuleContext(NameExpressionArrayContext.class,0);
		}
		public TerminalNode SEMI_COLON() { return getToken(PALParser.SEMI_COLON, 0); }
		public TerminalNode OBJECT_ATTRIBUTE() { return getToken(PALParser.OBJECT_ATTRIBUTE, 0); }
		public TerminalNode USER_ATTRIBUTE() { return getToken(PALParser.USER_ATTRIBUTE, 0); }
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
		enterRule(_localctx, 52, RULE_createAttrStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(303);
			match(CREATE);
			setState(304);
			_la = _input.LA(1);
			if ( !(_la==OBJECT_ATTRIBUTE || _la==USER_ATTRIBUTE) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(305);
			nameExpression();
			setState(306);
			match(IN);
			setState(307);
			nameExpressionArray();
			setState(308);
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
		public TerminalNode CREATE() { return getToken(PALParser.CREATE, 0); }
		public NameExpressionContext nameExpression() {
			return getRuleContext(NameExpressionContext.class,0);
		}
		public TerminalNode IN() { return getToken(PALParser.IN, 0); }
		public NameExpressionArrayContext nameExpressionArray() {
			return getRuleContext(NameExpressionArrayContext.class,0);
		}
		public TerminalNode SEMI_COLON() { return getToken(PALParser.SEMI_COLON, 0); }
		public TerminalNode USER() { return getToken(PALParser.USER, 0); }
		public TerminalNode OBJECT() { return getToken(PALParser.OBJECT, 0); }
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
		enterRule(_localctx, 54, RULE_createUserOrObjectStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(310);
			match(CREATE);
			setState(311);
			_la = _input.LA(1);
			if ( !(_la==OBJECT || _la==USER) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(312);
			nameExpression();
			setState(313);
			match(IN);
			setState(314);
			nameExpressionArray();
			setState(315);
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
		public ExpressionContext properties;
		public TerminalNode SET_PROPERTIES() { return getToken(PALParser.SET_PROPERTIES, 0); }
		public TerminalNode OF() { return getToken(PALParser.OF, 0); }
		public NameExpressionContext nameExpression() {
			return getRuleContext(NameExpressionContext.class,0);
		}
		public TerminalNode TO() { return getToken(PALParser.TO, 0); }
		public TerminalNode SEMI_COLON() { return getToken(PALParser.SEMI_COLON, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
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
		enterRule(_localctx, 56, RULE_setNodePropsStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(317);
			match(SET_PROPERTIES);
			setState(318);
			match(OF);
			setState(319);
			nameExpression();
			setState(320);
			match(TO);
			setState(321);
			((SetNodePropsStmtContext)_localctx).properties = expression();
			setState(322);
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
		public NameExpressionContext child;
		public NameExpressionContext parent;
		public TerminalNode ASSIGN() { return getToken(PALParser.ASSIGN, 0); }
		public TerminalNode TO() { return getToken(PALParser.TO, 0); }
		public TerminalNode SEMI_COLON() { return getToken(PALParser.SEMI_COLON, 0); }
		public List<NameExpressionContext> nameExpression() {
			return getRuleContexts(NameExpressionContext.class);
		}
		public NameExpressionContext nameExpression(int i) {
			return getRuleContext(NameExpressionContext.class,i);
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
		enterRule(_localctx, 58, RULE_assignStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(324);
			match(ASSIGN);
			setState(325);
			((AssignStmtContext)_localctx).child = nameExpression();
			setState(326);
			match(TO);
			setState(327);
			((AssignStmtContext)_localctx).parent = nameExpression();
			setState(328);
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
		public NameExpressionContext child;
		public NameExpressionContext parent;
		public TerminalNode DEASSIGN() { return getToken(PALParser.DEASSIGN, 0); }
		public TerminalNode FROM() { return getToken(PALParser.FROM, 0); }
		public TerminalNode SEMI_COLON() { return getToken(PALParser.SEMI_COLON, 0); }
		public List<NameExpressionContext> nameExpression() {
			return getRuleContexts(NameExpressionContext.class);
		}
		public NameExpressionContext nameExpression(int i) {
			return getRuleContext(NameExpressionContext.class,i);
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
		enterRule(_localctx, 60, RULE_deassignStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(330);
			match(DEASSIGN);
			setState(331);
			((DeassignStmtContext)_localctx).child = nameExpression();
			setState(332);
			match(FROM);
			setState(333);
			((DeassignStmtContext)_localctx).parent = nameExpression();
			setState(334);
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
		public NameExpressionContext ua;
		public NameExpressionContext target;
		public AccessRightArrayContext accessRights;
		public TerminalNode ASSOCIATE() { return getToken(PALParser.ASSOCIATE, 0); }
		public TerminalNode AND() { return getToken(PALParser.AND, 0); }
		public TerminalNode WITH() { return getToken(PALParser.WITH, 0); }
		public TerminalNode SEMI_COLON() { return getToken(PALParser.SEMI_COLON, 0); }
		public List<NameExpressionContext> nameExpression() {
			return getRuleContexts(NameExpressionContext.class);
		}
		public NameExpressionContext nameExpression(int i) {
			return getRuleContext(NameExpressionContext.class,i);
		}
		public AccessRightArrayContext accessRightArray() {
			return getRuleContext(AccessRightArrayContext.class,0);
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
		enterRule(_localctx, 62, RULE_associateStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(336);
			match(ASSOCIATE);
			setState(337);
			((AssociateStmtContext)_localctx).ua = nameExpression();
			setState(338);
			match(AND);
			setState(339);
			((AssociateStmtContext)_localctx).target = nameExpression();
			setState(340);
			match(WITH);
			setState(341);
			((AssociateStmtContext)_localctx).accessRights = accessRightArray();
			setState(342);
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
		public NameExpressionContext ua;
		public NameExpressionContext target;
		public TerminalNode DISSOCIATE() { return getToken(PALParser.DISSOCIATE, 0); }
		public TerminalNode AND() { return getToken(PALParser.AND, 0); }
		public TerminalNode SEMI_COLON() { return getToken(PALParser.SEMI_COLON, 0); }
		public List<NameExpressionContext> nameExpression() {
			return getRuleContexts(NameExpressionContext.class);
		}
		public NameExpressionContext nameExpression(int i) {
			return getRuleContext(NameExpressionContext.class,i);
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
		enterRule(_localctx, 64, RULE_dissociateStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(344);
			match(DISSOCIATE);
			setState(345);
			((DissociateStmtContext)_localctx).ua = nameExpression();
			setState(346);
			match(AND);
			setState(347);
			((DissociateStmtContext)_localctx).target = nameExpression();
			setState(348);
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
		public NameExpressionContext nameExpression() {
			return getRuleContext(NameExpressionContext.class,0);
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
		enterRule(_localctx, 66, RULE_deleteStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(350);
			match(DELETE);
			setState(351);
			deleteType();
			setState(352);
			nameExpression();
			setState(353);
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
		public NameExpressionContext nameExpression() {
			return getRuleContext(NameExpressionContext.class,0);
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
		enterRule(_localctx, 68, RULE_createObligationStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(355);
			match(CREATE);
			setState(356);
			match(OBLIGATION);
			setState(357);
			nameExpression();
			setState(358);
			match(OPEN_CURLY);
			setState(362);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==CREATE) {
				{
				{
				setState(359);
				createRuleStmt();
				}
				}
				setState(364);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(365);
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
		public NameExpressionContext ruleName;
		public TerminalNode CREATE() { return getToken(PALParser.CREATE, 0); }
		public TerminalNode RULE() { return getToken(PALParser.RULE, 0); }
		public TerminalNode WHEN() { return getToken(PALParser.WHEN, 0); }
		public SubjectClauseContext subjectClause() {
			return getRuleContext(SubjectClauseContext.class,0);
		}
		public TerminalNode PERFORMS() { return getToken(PALParser.PERFORMS, 0); }
		public PerformsClauseContext performsClause() {
			return getRuleContext(PerformsClauseContext.class,0);
		}
		public ResponseContext response() {
			return getRuleContext(ResponseContext.class,0);
		}
		public NameExpressionContext nameExpression() {
			return getRuleContext(NameExpressionContext.class,0);
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
		enterRule(_localctx, 70, RULE_createRuleStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(367);
			match(CREATE);
			setState(368);
			match(RULE);
			setState(369);
			((CreateRuleStmtContext)_localctx).ruleName = nameExpression();
			setState(370);
			match(WHEN);
			setState(371);
			subjectClause();
			setState(372);
			match(PERFORMS);
			setState(373);
			performsClause();
			setState(376);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ON) {
				{
				setState(374);
				match(ON);
				setState(375);
				onClause();
				}
			}

			setState(378);
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

	public static class PerformsClauseContext extends ParserRuleContext {
		public List<PerformsEventContext> performsEvent() {
			return getRuleContexts(PerformsEventContext.class);
		}
		public PerformsEventContext performsEvent(int i) {
			return getRuleContext(PerformsEventContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(PALParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(PALParser.COMMA, i);
		}
		public PerformsClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_performsClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterPerformsClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitPerformsClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitPerformsClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PerformsClauseContext performsClause() throws RecognitionException {
		PerformsClauseContext _localctx = new PerformsClauseContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_performsClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(380);
			performsEvent();
			setState(383); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(381);
				match(COMMA);
				setState(382);
				performsEvent();
				}
				}
				setState(385); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==COMMA );
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

	public static class PerformsEventContext extends ParserRuleContext {
		public IdContext alias;
		public List<IdContext> id() {
			return getRuleContexts(IdContext.class);
		}
		public IdContext id(int i) {
			return getRuleContext(IdContext.class,i);
		}
		public TerminalNode AS() { return getToken(PALParser.AS, 0); }
		public PerformsEventContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_performsEvent; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterPerformsEvent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitPerformsEvent(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitPerformsEvent(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PerformsEventContext performsEvent() throws RecognitionException {
		PerformsEventContext _localctx = new PerformsEventContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_performsEvent);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(387);
			id();
			{
			setState(388);
			match(AS);
			setState(389);
			((PerformsEventContext)_localctx).alias = id();
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
		public NameExpressionContext user;
		public TerminalNode USER() { return getToken(PALParser.USER, 0); }
		public NameExpressionContext nameExpression() {
			return getRuleContext(NameExpressionContext.class,0);
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
		public NameExpressionArrayContext users;
		public TerminalNode USERS() { return getToken(PALParser.USERS, 0); }
		public NameExpressionArrayContext nameExpressionArray() {
			return getRuleContext(NameExpressionArrayContext.class,0);
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
		public NameExpressionContext attribute;
		public TerminalNode ANY_USER_WITH_ATTRIBUTE() { return getToken(PALParser.ANY_USER_WITH_ATTRIBUTE, 0); }
		public NameExpressionContext nameExpression() {
			return getRuleContext(NameExpressionContext.class,0);
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
		public NameExpressionContext process;
		public TerminalNode PROCESS() { return getToken(PALParser.PROCESS, 0); }
		public NameExpressionContext nameExpression() {
			return getRuleContext(NameExpressionContext.class,0);
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
		enterRule(_localctx, 76, RULE_subjectClause);
		try {
			setState(400);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ANY_USER:
				_localctx = new AnyUserSubjectContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(391);
				match(ANY_USER);
				}
				break;
			case USER:
				_localctx = new UserSubjectContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(392);
				match(USER);
				setState(393);
				((UserSubjectContext)_localctx).user = nameExpression();
				}
				break;
			case USERS:
				_localctx = new UsersListSubjectContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(394);
				match(USERS);
				setState(395);
				((UsersListSubjectContext)_localctx).users = nameExpressionArray();
				}
				break;
			case ANY_USER_WITH_ATTRIBUTE:
				_localctx = new UserAttrSubjectContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(396);
				match(ANY_USER_WITH_ATTRIBUTE);
				setState(397);
				((UserAttrSubjectContext)_localctx).attribute = nameExpression();
				}
				break;
			case PROCESS:
				_localctx = new ProcessSubjectContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(398);
				match(PROCESS);
				setState(399);
				((ProcessSubjectContext)_localctx).process = nameExpression();
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
		public NameExpressionContext nameExpression() {
			return getRuleContext(NameExpressionContext.class,0);
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
		public NameExpressionArrayContext nameExpressionArray() {
			return getRuleContext(NameExpressionArrayContext.class,0);
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
		public NameExpressionContext nameExpression() {
			return getRuleContext(NameExpressionContext.class,0);
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
		enterRule(_localctx, 78, RULE_onClause);
		try {
			setState(412);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
			case 1:
				_localctx = new PolicyElementContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(402);
				nameExpression();
				}
				break;
			case 2:
				_localctx = new AnyPolicyElementContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(403);
				anyPe();
				}
				break;
			case 3:
				_localctx = new AnyContainedInContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(404);
				anyPe();
				setState(405);
				match(IN);
				setState(406);
				nameExpression();
				}
				break;
			case 4:
				_localctx = new AnyOfSetContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(408);
				anyPe();
				setState(409);
				match(OF);
				setState(410);
				nameExpressionArray();
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
		enterRule(_localctx, 80, RULE_anyPe);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(414);
			match(ANY);
			setState(415);
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
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
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
		enterRule(_localctx, 82, RULE_response);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(417);
			match(DO);
			setState(418);
			match(OPEN_PAREN);
			setState(419);
			id();
			setState(420);
			match(CLOSE_PAREN);
			setState(421);
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
		enterRule(_localctx, 84, RULE_responseBlock);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(423);
			match(OPEN_CURLY);
			setState(427);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ALL_ACCESS_RIGHTS) | (1L << ALL_RESOURCE_ACCESS_RIGHTS) | (1L << ALL_ADMIN_ACCESS_RIGHTS) | (1L << CREATE) | (1L << DELETE) | (1L << BREAK) | (1L << CONTINUE) | (1L << EVENT) | (1L << SET_RESOURCE_ACCESS_RIGHTS) | (1L << ASSIGN) | (1L << DEASSIGN) | (1L << SET_PROPERTIES) | (1L << ASSOCIATE) | (1L << DISSOCIATE) | (1L << DENY) | (1L << LET) | (1L << CONST) | (1L << FUNCTION) | (1L << RETURN) | (1L << FOREACH) | (1L << FOR) | (1L << IF) | (1L << IDENTIFIER))) != 0)) {
				{
				{
				setState(424);
				responseStmt();
				}
				}
				setState(429);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(430);
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
		public EventSpecificResponseContext eventSpecificResponse() {
			return getRuleContext(EventSpecificResponseContext.class,0);
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
		enterRule(_localctx, 86, RULE_responseStmt);
		try {
			setState(436);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(432);
				stmt();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(433);
				createRuleStmt();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(434);
				deleteRuleStmt();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(435);
				eventSpecificResponse();
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
		public NameExpressionContext ruleName;
		public NameExpressionContext obligationName;
		public TerminalNode DELETE() { return getToken(PALParser.DELETE, 0); }
		public TerminalNode RULE() { return getToken(PALParser.RULE, 0); }
		public TerminalNode FROM() { return getToken(PALParser.FROM, 0); }
		public TerminalNode OBLIGATION() { return getToken(PALParser.OBLIGATION, 0); }
		public TerminalNode SEMI_COLON() { return getToken(PALParser.SEMI_COLON, 0); }
		public List<NameExpressionContext> nameExpression() {
			return getRuleContexts(NameExpressionContext.class);
		}
		public NameExpressionContext nameExpression(int i) {
			return getRuleContext(NameExpressionContext.class,i);
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
		enterRule(_localctx, 88, RULE_deleteRuleStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(438);
			match(DELETE);
			setState(439);
			match(RULE);
			setState(440);
			((DeleteRuleStmtContext)_localctx).ruleName = nameExpression();
			setState(441);
			match(FROM);
			setState(442);
			match(OBLIGATION);
			setState(443);
			((DeleteRuleStmtContext)_localctx).obligationName = nameExpression();
			setState(444);
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

	public static class EventSpecificResponseContext extends ParserRuleContext {
		public TerminalNode EVENT() { return getToken(PALParser.EVENT, 0); }
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public TerminalNode OPEN_CURLY() { return getToken(PALParser.OPEN_CURLY, 0); }
		public TerminalNode CLOSE_CURLY() { return getToken(PALParser.CLOSE_CURLY, 0); }
		public List<ResponseStmtContext> responseStmt() {
			return getRuleContexts(ResponseStmtContext.class);
		}
		public ResponseStmtContext responseStmt(int i) {
			return getRuleContext(ResponseStmtContext.class,i);
		}
		public EventSpecificResponseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_eventSpecificResponse; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterEventSpecificResponse(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitEventSpecificResponse(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitEventSpecificResponse(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EventSpecificResponseContext eventSpecificResponse() throws RecognitionException {
		EventSpecificResponseContext _localctx = new EventSpecificResponseContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_eventSpecificResponse);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(446);
			match(EVENT);
			setState(447);
			id();
			setState(448);
			match(OPEN_CURLY);
			setState(452);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ALL_ACCESS_RIGHTS) | (1L << ALL_RESOURCE_ACCESS_RIGHTS) | (1L << ALL_ADMIN_ACCESS_RIGHTS) | (1L << CREATE) | (1L << DELETE) | (1L << BREAK) | (1L << CONTINUE) | (1L << EVENT) | (1L << SET_RESOURCE_ACCESS_RIGHTS) | (1L << ASSIGN) | (1L << DEASSIGN) | (1L << SET_PROPERTIES) | (1L << ASSOCIATE) | (1L << DISSOCIATE) | (1L << DENY) | (1L << LET) | (1L << CONST) | (1L << FUNCTION) | (1L << RETURN) | (1L << FOREACH) | (1L << FOR) | (1L << IF) | (1L << IDENTIFIER))) != 0)) {
				{
				{
				setState(449);
				responseStmt();
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

	public static class CreateProhibitionStmtContext extends ParserRuleContext {
		public NameExpressionContext name;
		public NameExpressionContext subject;
		public AccessRightArrayContext accessRights;
		public ProhibitionContainerListContext containers;
		public TerminalNode CREATE() { return getToken(PALParser.CREATE, 0); }
		public TerminalNode PROHIBITION() { return getToken(PALParser.PROHIBITION, 0); }
		public TerminalNode DENY() { return getToken(PALParser.DENY, 0); }
		public TerminalNode ACCESS_RIGHTS() { return getToken(PALParser.ACCESS_RIGHTS, 0); }
		public TerminalNode ON() { return getToken(PALParser.ON, 0); }
		public TerminalNode OF() { return getToken(PALParser.OF, 0); }
		public TerminalNode SEMI_COLON() { return getToken(PALParser.SEMI_COLON, 0); }
		public List<NameExpressionContext> nameExpression() {
			return getRuleContexts(NameExpressionContext.class);
		}
		public NameExpressionContext nameExpression(int i) {
			return getRuleContext(NameExpressionContext.class,i);
		}
		public TerminalNode USER() { return getToken(PALParser.USER, 0); }
		public TerminalNode USER_ATTRIBUTE() { return getToken(PALParser.USER_ATTRIBUTE, 0); }
		public TerminalNode PROCESS() { return getToken(PALParser.PROCESS, 0); }
		public AccessRightArrayContext accessRightArray() {
			return getRuleContext(AccessRightArrayContext.class,0);
		}
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
		enterRule(_localctx, 92, RULE_createProhibitionStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(457);
			match(CREATE);
			setState(458);
			match(PROHIBITION);
			setState(459);
			((CreateProhibitionStmtContext)_localctx).name = nameExpression();
			setState(460);
			match(DENY);
			setState(461);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PROCESS) | (1L << USER_ATTRIBUTE) | (1L << USER))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(462);
			((CreateProhibitionStmtContext)_localctx).subject = nameExpression();
			setState(463);
			match(ACCESS_RIGHTS);
			setState(464);
			((CreateProhibitionStmtContext)_localctx).accessRights = accessRightArray();
			setState(465);
			match(ON);
			setState(466);
			_la = _input.LA(1);
			if ( !(_la==INTERSECTION || _la==UNION) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(467);
			match(OF);
			setState(468);
			((CreateProhibitionStmtContext)_localctx).containers = prohibitionContainerList();
			setState(469);
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
		enterRule(_localctx, 94, RULE_prohibitionContainerList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(479);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ALL_ACCESS_RIGHTS) | (1L << ALL_RESOURCE_ACCESS_RIGHTS) | (1L << ALL_ADMIN_ACCESS_RIGHTS) | (1L << CREATE) | (1L << DELETE) | (1L << ASSIGN) | (1L << DEASSIGN) | (1L << ASSOCIATE) | (1L << DISSOCIATE) | (1L << DENY) | (1L << IDENTIFIER))) != 0) || _la==IS_COMPLEMENT) {
				{
				setState(471);
				prohibitionContainerExpression();
				setState(476);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(472);
					match(COMMA);
					setState(473);
					prohibitionContainerExpression();
					}
					}
					setState(478);
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

	public static class ProhibitionContainerExpressionContext extends ParserRuleContext {
		public NameExpressionContext container;
		public NameExpressionContext nameExpression() {
			return getRuleContext(NameExpressionContext.class,0);
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
		enterRule(_localctx, 96, RULE_prohibitionContainerExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(482);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IS_COMPLEMENT) {
				{
				setState(481);
				match(IS_COMPLEMENT);
				}
			}

			setState(484);
			((ProhibitionContainerExpressionContext)_localctx).container = nameExpression();
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
		public TerminalNode SET_RESOURCE_ACCESS_RIGHTS() { return getToken(PALParser.SET_RESOURCE_ACCESS_RIGHTS, 0); }
		public AccessRightArrayContext accessRightArray() {
			return getRuleContext(AccessRightArrayContext.class,0);
		}
		public TerminalNode SEMI_COLON() { return getToken(PALParser.SEMI_COLON, 0); }
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
		enterRule(_localctx, 98, RULE_setResourceAccessRightsStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(486);
			match(SET_RESOURCE_ACCESS_RIGHTS);
			setState(487);
			accessRightArray();
			setState(488);
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
		enterRule(_localctx, 100, RULE_expression);
		try {
			setState(493);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(490);
				varRef();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(491);
				funcCall();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(492);
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

	public static class NameExpressionArrayContext extends ParserRuleContext {
		public List<NameExpressionContext> nameExpression() {
			return getRuleContexts(NameExpressionContext.class);
		}
		public NameExpressionContext nameExpression(int i) {
			return getRuleContext(NameExpressionContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(PALParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(PALParser.COMMA, i);
		}
		public NameExpressionArrayContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nameExpressionArray; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterNameExpressionArray(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitNameExpressionArray(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitNameExpressionArray(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NameExpressionArrayContext nameExpressionArray() throws RecognitionException {
		NameExpressionArrayContext _localctx = new NameExpressionArrayContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_nameExpressionArray);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(495);
			nameExpression();
			setState(500);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(496);
				match(COMMA);
				setState(497);
				nameExpression();
				}
				}
				setState(502);
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

	public static class NameExpressionContext extends ParserRuleContext {
		public VarRefContext varRef() {
			return getRuleContext(VarRefContext.class,0);
		}
		public FuncCallContext funcCall() {
			return getRuleContext(FuncCallContext.class,0);
		}
		public NameExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nameExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterNameExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitNameExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitNameExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NameExpressionContext nameExpression() throws RecognitionException {
		NameExpressionContext _localctx = new NameExpressionContext(_ctx, getState());
		enterRule(_localctx, 104, RULE_nameExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(505);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,28,_ctx) ) {
			case 1:
				{
				setState(503);
				varRef();
				}
				break;
			case 2:
				{
				setState(504);
				funcCall();
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
		enterRule(_localctx, 106, RULE_array);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(507);
			match(OPEN_BRACKET);
			setState(516);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ALL_ACCESS_RIGHTS) | (1L << ALL_RESOURCE_ACCESS_RIGHTS) | (1L << ALL_ADMIN_ACCESS_RIGHTS) | (1L << CREATE) | (1L << DELETE) | (1L << ASSIGN) | (1L << DEASSIGN) | (1L << ASSOCIATE) | (1L << DISSOCIATE) | (1L << DENY) | (1L << BOOLEAN) | (1L << IDENTIFIER))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (STRING - 64)) | (1L << (OPEN_CURLY - 64)) | (1L << (OPEN_BRACKET - 64)))) != 0)) {
				{
				setState(508);
				expression();
				setState(513);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(509);
					match(COMMA);
					setState(510);
					expression();
					}
					}
					setState(515);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(518);
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

	public static class AccessRightArrayContext extends ParserRuleContext {
		public List<AccessRightContext> accessRight() {
			return getRuleContexts(AccessRightContext.class);
		}
		public AccessRightContext accessRight(int i) {
			return getRuleContext(AccessRightContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(PALParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(PALParser.COMMA, i);
		}
		public AccessRightArrayContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_accessRightArray; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterAccessRightArray(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitAccessRightArray(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitAccessRightArray(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AccessRightArrayContext accessRightArray() throws RecognitionException {
		AccessRightArrayContext _localctx = new AccessRightArrayContext(_ctx, getState());
		enterRule(_localctx, 108, RULE_accessRightArray);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(528);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ALL_ACCESS_RIGHTS) | (1L << ALL_RESOURCE_ACCESS_RIGHTS) | (1L << ALL_ADMIN_ACCESS_RIGHTS) | (1L << CREATE) | (1L << DELETE) | (1L << ASSIGN) | (1L << DEASSIGN) | (1L << ASSOCIATE) | (1L << DISSOCIATE) | (1L << DENY) | (1L << IDENTIFIER))) != 0)) {
				{
				setState(520);
				accessRight();
				setState(525);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(521);
					match(COMMA);
					setState(522);
					accessRight();
					}
					}
					setState(527);
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

	public static class AccessRightContext extends ParserRuleContext {
		public TerminalNode ALL_ACCESS_RIGHTS() { return getToken(PALParser.ALL_ACCESS_RIGHTS, 0); }
		public TerminalNode ALL_RESOURCE_ACCESS_RIGHTS() { return getToken(PALParser.ALL_RESOURCE_ACCESS_RIGHTS, 0); }
		public TerminalNode ALL_ADMIN_ACCESS_RIGHTS() { return getToken(PALParser.ALL_ADMIN_ACCESS_RIGHTS, 0); }
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public AccessRightContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_accessRight; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterAccessRight(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitAccessRight(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitAccessRight(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AccessRightContext accessRight() throws RecognitionException {
		AccessRightContext _localctx = new AccessRightContext(_ctx, getState());
		enterRule(_localctx, 110, RULE_accessRight);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(534);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,33,_ctx) ) {
			case 1:
				{
				setState(530);
				match(ALL_ACCESS_RIGHTS);
				}
				break;
			case 2:
				{
				setState(531);
				match(ALL_RESOURCE_ACCESS_RIGHTS);
				}
				break;
			case 3:
				{
				setState(532);
				match(ALL_ADMIN_ACCESS_RIGHTS);
				}
				break;
			case 4:
				{
				setState(533);
				id();
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
		enterRule(_localctx, 112, RULE_map);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(536);
			match(OPEN_CURLY);
			setState(545);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ALL_ACCESS_RIGHTS) | (1L << ALL_RESOURCE_ACCESS_RIGHTS) | (1L << ALL_ADMIN_ACCESS_RIGHTS) | (1L << CREATE) | (1L << DELETE) | (1L << ASSIGN) | (1L << DEASSIGN) | (1L << ASSOCIATE) | (1L << DISSOCIATE) | (1L << DENY) | (1L << BOOLEAN) | (1L << IDENTIFIER))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (STRING - 64)) | (1L << (OPEN_CURLY - 64)) | (1L << (OPEN_BRACKET - 64)))) != 0)) {
				{
				setState(537);
				mapEntry();
				setState(542);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(538);
					match(COMMA);
					setState(539);
					mapEntry();
					}
					}
					setState(544);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(547);
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
		enterRule(_localctx, 114, RULE_mapEntry);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(549);
			((MapEntryContext)_localctx).key = expression();
			setState(550);
			match(COLON);
			setState(551);
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

	public static class MapEntryRefContext extends ParserRuleContext {
		public ExpressionContext key;
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
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
		public MapEntryRefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mapEntryRef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterMapEntryRef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitMapEntryRef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitMapEntryRef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MapEntryRefContext mapEntryRef() throws RecognitionException {
		MapEntryRefContext _localctx = new MapEntryRefContext(_ctx, getState());
		enterRule(_localctx, 116, RULE_mapEntryRef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(553);
			id();
			setState(558); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(554);
				match(OPEN_BRACKET);
				setState(555);
				((MapEntryRefContext)_localctx).key = expression();
				setState(556);
				match(CLOSE_BRACKET);
				}
				}
				setState(560); 
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

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 118, RULE_literal);
		try {
			setState(566);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STRING:
				_localctx = new StringLiteralContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(562);
				match(STRING);
				}
				break;
			case BOOLEAN:
				_localctx = new BooleanLiteralContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(563);
				match(BOOLEAN);
				}
				break;
			case OPEN_BRACKET:
				_localctx = new ArrayLiteralContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(564);
				array();
				}
				break;
			case OPEN_CURLY:
				_localctx = new MapLiteralContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(565);
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
	public static class MapEntryReferenceContext extends VarRefContext {
		public MapEntryRefContext mapEntryRef() {
			return getRuleContext(MapEntryRefContext.class,0);
		}
		public MapEntryReferenceContext(VarRefContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterMapEntryReference(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitMapEntryReference(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitMapEntryReference(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ReferenceByIDContext extends VarRefContext {
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
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
		enterRule(_localctx, 120, RULE_varRef);
		try {
			setState(570);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,38,_ctx) ) {
			case 1:
				_localctx = new ReferenceByIDContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(568);
				id();
				}
				break;
			case 2:
				_localctx = new MapEntryReferenceContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(569);
				mapEntryRef();
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
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
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
		enterRule(_localctx, 122, RULE_funcCall);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(572);
			id();
			setState(573);
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
		enterRule(_localctx, 124, RULE_funcCallArgs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(575);
			match(OPEN_PAREN);
			setState(584);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ALL_ACCESS_RIGHTS) | (1L << ALL_RESOURCE_ACCESS_RIGHTS) | (1L << ALL_ADMIN_ACCESS_RIGHTS) | (1L << CREATE) | (1L << DELETE) | (1L << ASSIGN) | (1L << DEASSIGN) | (1L << ASSOCIATE) | (1L << DISSOCIATE) | (1L << DENY) | (1L << BOOLEAN) | (1L << IDENTIFIER))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (STRING - 64)) | (1L << (OPEN_CURLY - 64)) | (1L << (OPEN_BRACKET - 64)))) != 0)) {
				{
				setState(576);
				expression();
				setState(581);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(577);
					match(COMMA);
					setState(578);
					expression();
					}
					}
					setState(583);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(586);
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

	public static class IdContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(PALParser.IDENTIFIER, 0); }
		public KeywordAsIDContext keywordAsID() {
			return getRuleContext(KeywordAsIDContext.class,0);
		}
		public IdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_id; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitId(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitId(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdContext id() throws RecognitionException {
		IdContext _localctx = new IdContext(_ctx, getState());
		enterRule(_localctx, 126, RULE_id);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(590);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				{
				setState(588);
				match(IDENTIFIER);
				}
				break;
			case ALL_ACCESS_RIGHTS:
			case ALL_RESOURCE_ACCESS_RIGHTS:
			case ALL_ADMIN_ACCESS_RIGHTS:
			case CREATE:
			case DELETE:
			case ASSIGN:
			case DEASSIGN:
			case ASSOCIATE:
			case DISSOCIATE:
			case DENY:
				{
				setState(589);
				keywordAsID();
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

	public static class KeywordAsIDContext extends ParserRuleContext {
		public TerminalNode ALL_ACCESS_RIGHTS() { return getToken(PALParser.ALL_ACCESS_RIGHTS, 0); }
		public TerminalNode ALL_RESOURCE_ACCESS_RIGHTS() { return getToken(PALParser.ALL_RESOURCE_ACCESS_RIGHTS, 0); }
		public TerminalNode ALL_ADMIN_ACCESS_RIGHTS() { return getToken(PALParser.ALL_ADMIN_ACCESS_RIGHTS, 0); }
		public TerminalNode CREATE() { return getToken(PALParser.CREATE, 0); }
		public TerminalNode DELETE() { return getToken(PALParser.DELETE, 0); }
		public TerminalNode ASSIGN() { return getToken(PALParser.ASSIGN, 0); }
		public TerminalNode DEASSIGN() { return getToken(PALParser.DEASSIGN, 0); }
		public TerminalNode ASSOCIATE() { return getToken(PALParser.ASSOCIATE, 0); }
		public TerminalNode DISSOCIATE() { return getToken(PALParser.DISSOCIATE, 0); }
		public TerminalNode DENY() { return getToken(PALParser.DENY, 0); }
		public KeywordAsIDContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keywordAsID; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterKeywordAsID(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitKeywordAsID(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitKeywordAsID(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeywordAsIDContext keywordAsID() throws RecognitionException {
		KeywordAsIDContext _localctx = new KeywordAsIDContext(_ctx, getState());
		enterRule(_localctx, 128, RULE_keywordAsID);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(592);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ALL_ACCESS_RIGHTS) | (1L << ALL_RESOURCE_ACCESS_RIGHTS) | (1L << ALL_ADMIN_ACCESS_RIGHTS) | (1L << CREATE) | (1L << DELETE) | (1L << ASSIGN) | (1L << DEASSIGN) | (1L << ASSOCIATE) | (1L << DISSOCIATE) | (1L << DENY))) != 0)) ) {
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

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3Q\u0255\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\3\2\3\2\3\2\3\3\7\3\u0089\n\3\f\3\16\3"+
		"\u008c\13\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3"+
		"\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4\u00a4\n\4\3\5\5\5\u00a7\n\5\3\5\3\5"+
		"\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\5\6\u00b4\n\6\3\6\3\6\3\7\3\7\3\7"+
		"\7\7\u00bb\n\7\f\7\16\7\u00be\13\7\5\7\u00c0\n\7\3\b\3\b\3\b\3\t\3\t\3"+
		"\n\3\n\3\n\5\n\u00ca\n\n\3\n\3\n\3\13\3\13\5\13\u00d0\n\13\3\f\3\f\7\f"+
		"\u00d4\n\f\f\f\16\f\u00d7\13\f\3\f\3\f\3\r\3\r\3\r\3\r\5\r\u00df\n\r\3"+
		"\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\17"+
		"\3\17\3\17\3\20\3\20\3\20\3\21\3\21\3\21\3\22\3\22\3\22\3\22\7\22\u00fc"+
		"\n\22\f\22\16\22\u00ff\13\22\3\22\5\22\u0102\n\22\3\23\3\23\3\23\3\23"+
		"\3\23\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\5\25\u0111\n\25\3\26\3\26"+
		"\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\30\3\30\7\30\u011f\n\30\f\30"+
		"\16\30\u0122\13\30\3\30\3\30\3\31\3\31\3\31\5\31\u0129\n\31\3\32\3\32"+
		"\3\33\3\33\3\33\3\33\3\33\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\35\3\35"+
		"\3\35\3\35\3\35\3\35\3\35\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\37\3\37"+
		"\3\37\3\37\3\37\3\37\3 \3 \3 \3 \3 \3 \3!\3!\3!\3!\3!\3!\3!\3!\3\"\3\""+
		"\3\"\3\"\3\"\3\"\3#\3#\3#\3#\3#\3$\3$\3$\3$\3$\7$\u016b\n$\f$\16$\u016e"+
		"\13$\3$\3$\3%\3%\3%\3%\3%\3%\3%\3%\3%\5%\u017b\n%\3%\3%\3&\3&\3&\6&\u0182"+
		"\n&\r&\16&\u0183\3\'\3\'\3\'\3\'\3(\3(\3(\3(\3(\3(\3(\3(\3(\5(\u0193\n"+
		"(\3)\3)\3)\3)\3)\3)\3)\3)\3)\3)\5)\u019f\n)\3*\3*\3*\3+\3+\3+\3+\3+\3"+
		"+\3,\3,\7,\u01ac\n,\f,\16,\u01af\13,\3,\3,\3-\3-\3-\3-\5-\u01b7\n-\3."+
		"\3.\3.\3.\3.\3.\3.\3.\3/\3/\3/\3/\7/\u01c5\n/\f/\16/\u01c8\13/\3/\3/\3"+
		"\60\3\60\3\60\3\60\3\60\3\60\3\60\3\60\3\60\3\60\3\60\3\60\3\60\3\60\3"+
		"\61\3\61\3\61\7\61\u01dd\n\61\f\61\16\61\u01e0\13\61\5\61\u01e2\n\61\3"+
		"\62\5\62\u01e5\n\62\3\62\3\62\3\63\3\63\3\63\3\63\3\64\3\64\3\64\5\64"+
		"\u01f0\n\64\3\65\3\65\3\65\7\65\u01f5\n\65\f\65\16\65\u01f8\13\65\3\66"+
		"\3\66\5\66\u01fc\n\66\3\67\3\67\3\67\3\67\7\67\u0202\n\67\f\67\16\67\u0205"+
		"\13\67\5\67\u0207\n\67\3\67\3\67\38\38\38\78\u020e\n8\f8\168\u0211\13"+
		"8\58\u0213\n8\39\39\39\39\59\u0219\n9\3:\3:\3:\3:\7:\u021f\n:\f:\16:\u0222"+
		"\13:\5:\u0224\n:\3:\3:\3;\3;\3;\3;\3<\3<\3<\3<\3<\6<\u0231\n<\r<\16<\u0232"+
		"\3=\3=\3=\3=\5=\u0239\n=\3>\3>\5>\u023d\n>\3?\3?\3?\3@\3@\3@\3@\7@\u0246"+
		"\n@\f@\16@\u0249\13@\5@\u024b\n@\3@\3@\3A\3A\5A\u0251\nA\3B\3B\3B\2\2"+
		"C\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDF"+
		"HJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\2\t\3\2./\3\2\'+\3\2()\3\2*"+
		"+\5\2\25\25))++\3\2\26\27\6\2\3\7\31\32\37\37\"#\2\u0261\2\u0084\3\2\2"+
		"\2\4\u008a\3\2\2\2\6\u00a3\3\2\2\2\b\u00a6\3\2\2\2\n\u00ad\3\2\2\2\f\u00bf"+
		"\3\2\2\2\16\u00c1\3\2\2\2\20\u00c4\3\2\2\2\22\u00c9\3\2\2\2\24\u00cf\3"+
		"\2\2\2\26\u00d1\3\2\2\2\30\u00da\3\2\2\2\32\u00e4\3\2\2\2\34\u00ee\3\2"+
		"\2\2\36\u00f1\3\2\2\2 \u00f4\3\2\2\2\"\u00f7\3\2\2\2$\u0103\3\2\2\2&\u0108"+
		"\3\2\2\2(\u0110\3\2\2\2*\u0112\3\2\2\2,\u0118\3\2\2\2.\u011c\3\2\2\2\60"+
		"\u0128\3\2\2\2\62\u012a\3\2\2\2\64\u012c\3\2\2\2\66\u0131\3\2\2\28\u0138"+
		"\3\2\2\2:\u013f\3\2\2\2<\u0146\3\2\2\2>\u014c\3\2\2\2@\u0152\3\2\2\2B"+
		"\u015a\3\2\2\2D\u0160\3\2\2\2F\u0165\3\2\2\2H\u0171\3\2\2\2J\u017e\3\2"+
		"\2\2L\u0185\3\2\2\2N\u0192\3\2\2\2P\u019e\3\2\2\2R\u01a0\3\2\2\2T\u01a3"+
		"\3\2\2\2V\u01a9\3\2\2\2X\u01b6\3\2\2\2Z\u01b8\3\2\2\2\\\u01c0\3\2\2\2"+
		"^\u01cb\3\2\2\2`\u01e1\3\2\2\2b\u01e4\3\2\2\2d\u01e8\3\2\2\2f\u01ef\3"+
		"\2\2\2h\u01f1\3\2\2\2j\u01fb\3\2\2\2l\u01fd\3\2\2\2n\u0212\3\2\2\2p\u0218"+
		"\3\2\2\2r\u021a\3\2\2\2t\u0227\3\2\2\2v\u022b\3\2\2\2x\u0238\3\2\2\2z"+
		"\u023c\3\2\2\2|\u023e\3\2\2\2~\u0241\3\2\2\2\u0080\u0250\3\2\2\2\u0082"+
		"\u0252\3\2\2\2\u0084\u0085\5\4\3\2\u0085\u0086\7\2\2\3\u0086\3\3\2\2\2"+
		"\u0087\u0089\5\6\4\2\u0088\u0087\3\2\2\2\u0089\u008c\3\2\2\2\u008a\u0088"+
		"\3\2\2\2\u008a\u008b\3\2\2\2\u008b\5\3\2\2\2\u008c\u008a\3\2\2\2\u008d"+
		"\u00a4\5\b\5\2\u008e\u00a4\5\n\6\2\u008f\u00a4\5\22\n\2\u0090\u00a4\5"+
		"\30\r\2\u0091\u00a4\5\32\16\2\u0092\u00a4\5\34\17\2\u0093\u00a4\5\36\20"+
		"\2\u0094\u00a4\5 \21\2\u0095\u00a4\5\"\22\2\u0096\u00a4\5\64\33\2\u0097"+
		"\u00a4\5\66\34\2\u0098\u00a4\58\35\2\u0099\u00a4\5F$\2\u009a\u00a4\5^"+
		"\60\2\u009b\u00a4\5:\36\2\u009c\u00a4\5<\37\2\u009d\u00a4\5> \2\u009e"+
		"\u00a4\5D#\2\u009f\u00a4\5@!\2\u00a0\u00a4\5B\"\2\u00a1\u00a4\5d\63\2"+
		"\u00a2\u00a4\5Z.\2\u00a3\u008d\3\2\2\2\u00a3\u008e\3\2\2\2\u00a3\u008f"+
		"\3\2\2\2\u00a3\u0090\3\2\2\2\u00a3\u0091\3\2\2\2\u00a3\u0092\3\2\2\2\u00a3"+
		"\u0093\3\2\2\2\u00a3\u0094\3\2\2\2\u00a3\u0095\3\2\2\2\u00a3\u0096\3\2"+
		"\2\2\u00a3\u0097\3\2\2\2\u00a3\u0098\3\2\2\2\u00a3\u0099\3\2\2\2\u00a3"+
		"\u009a\3\2\2\2\u00a3\u009b\3\2\2\2\u00a3\u009c\3\2\2\2\u00a3\u009d\3\2"+
		"\2\2\u00a3\u009e\3\2\2\2\u00a3\u009f\3\2\2\2\u00a3\u00a0\3\2\2\2\u00a3"+
		"\u00a1\3\2\2\2\u00a3\u00a2\3\2\2\2\u00a4\7\3\2\2\2\u00a5\u00a7\t\2\2\2"+
		"\u00a6\u00a5\3\2\2\2\u00a6\u00a7\3\2\2\2\u00a7\u00a8\3\2\2\2\u00a8\u00a9"+
		"\5\u0080A\2\u00a9\u00aa\7Q\2\2\u00aa\u00ab\5f\64\2\u00ab\u00ac\7G\2\2"+
		"\u00ac\t\3\2\2\2\u00ad\u00ae\7\60\2\2\u00ae\u00af\5\u0080A\2\u00af\u00b0"+
		"\7N\2\2\u00b0\u00b1\5\f\7\2\u00b1\u00b3\7O\2\2\u00b2\u00b4\5\24\13\2\u00b3"+
		"\u00b2\3\2\2\2\u00b3\u00b4\3\2\2\2\u00b4\u00b5\3\2\2\2\u00b5\u00b6\5\26"+
		"\f\2\u00b6\13\3\2\2\2\u00b7\u00bc\5\16\b\2\u00b8\u00b9\7E\2\2\u00b9\u00bb"+
		"\5\16\b\2\u00ba\u00b8\3\2\2\2\u00bb\u00be\3\2\2\2\u00bc\u00ba\3\2\2\2"+
		"\u00bc\u00bd\3\2\2\2\u00bd\u00c0\3\2\2\2\u00be\u00bc\3\2\2\2\u00bf\u00b7"+
		"\3\2\2\2\u00bf\u00c0\3\2\2\2\u00c0\r\3\2\2\2\u00c1\u00c2\5\20\t\2\u00c2"+
		"\u00c3\5\u0080A\2\u00c3\17\3\2\2\2\u00c4\u00c5\5(\25\2\u00c5\21\3\2\2"+
		"\2\u00c6\u00c7\7\61\2\2\u00c7\u00ca\5f\64\2\u00c8\u00ca\7\61\2\2\u00c9"+
		"\u00c6\3\2\2\2\u00c9\u00c8\3\2\2\2\u00ca\u00cb\3\2\2\2\u00cb\u00cc\7G"+
		"\2\2\u00cc\23\3\2\2\2\u00cd\u00d0\5(\25\2\u00ce\u00d0\7\67\2\2\u00cf\u00cd"+
		"\3\2\2\2\u00cf\u00ce\3\2\2\2\u00d0\25\3\2\2\2\u00d1\u00d5\7H\2\2\u00d2"+
		"\u00d4\5\6\4\2\u00d3\u00d2\3\2\2\2\u00d4\u00d7\3\2\2\2\u00d5\u00d3\3\2"+
		"\2\2\u00d5\u00d6\3\2\2\2\u00d6\u00d8\3\2\2\2\u00d7\u00d5\3\2\2\2\u00d8"+
		"\u00d9\7I\2\2\u00d9\27\3\2\2\2\u00da\u00db\7:\2\2\u00db\u00de\5\u0080"+
		"A\2\u00dc\u00dd\7E\2\2\u00dd\u00df\5\u0080A\2\u00de\u00dc\3\2\2\2\u00de"+
		"\u00df\3\2\2\2\u00df\u00e0\3\2\2\2\u00e0\u00e1\7<\2\2\u00e1\u00e2\5f\64"+
		"\2\u00e2\u00e3\5.\30\2\u00e3\31\3\2\2\2\u00e4\u00e5\7;\2\2\u00e5\u00e6"+
		"\5\u0080A\2\u00e6\u00e7\7?\2\2\u00e7\u00e8\7J\2\2\u00e8\u00e9\7@\2\2\u00e9"+
		"\u00ea\7E\2\2\u00ea\u00eb\7@\2\2\u00eb\u00ec\7K\2\2\u00ec\u00ed\5.\30"+
		"\2\u00ed\33\3\2\2\2\u00ee\u00ef\7\b\2\2\u00ef\u00f0\7G\2\2\u00f0\35\3"+
		"\2\2\2\u00f1\u00f2\7\t\2\2\u00f2\u00f3\7G\2\2\u00f3\37\3\2\2\2\u00f4\u00f5"+
		"\5|?\2\u00f5\u00f6\7G\2\2\u00f6!\3\2\2\2\u00f7\u00f8\7=\2\2\u00f8\u00f9"+
		"\5f\64\2\u00f9\u00fd\5.\30\2\u00fa\u00fc\5$\23\2\u00fb\u00fa\3\2\2\2\u00fc"+
		"\u00ff\3\2\2\2\u00fd\u00fb\3\2\2\2\u00fd\u00fe\3\2\2\2\u00fe\u0101\3\2"+
		"\2\2\u00ff\u00fd\3\2\2\2\u0100\u0102\5&\24\2\u0101\u0100\3\2\2\2\u0101"+
		"\u0102\3\2\2\2\u0102#\3\2\2\2\u0103\u0104\7>\2\2\u0104\u0105\7=\2\2\u0105"+
		"\u0106\5f\64\2\u0106\u0107\5.\30\2\u0107%\3\2\2\2\u0108\u0109\7>\2\2\u0109"+
		"\u010a\5.\30\2\u010a\'\3\2\2\2\u010b\u0111\7\65\2\2\u010c\u0111\7\66\2"+
		"\2\u010d\u0111\5,\27\2\u010e\u0111\5*\26\2\u010f\u0111\7-\2\2\u0110\u010b"+
		"\3\2\2\2\u0110\u010c\3\2\2\2\u0110\u010d\3\2\2\2\u0110\u010e\3\2\2\2\u0110"+
		"\u010f\3\2\2\2\u0111)\3\2\2\2\u0112\u0113\79\2\2\u0113\u0114\7J\2\2\u0114"+
		"\u0115\5(\25\2\u0115\u0116\7K\2\2\u0116\u0117\5(\25\2\u0117+\3\2\2\2\u0118"+
		"\u0119\7J\2\2\u0119\u011a\7K\2\2\u011a\u011b\5(\25\2\u011b-\3\2\2\2\u011c"+
		"\u0120\7H\2\2\u011d\u011f\5\6\4\2\u011e\u011d\3\2\2\2\u011f\u0122\3\2"+
		"\2\2\u0120\u011e\3\2\2\2\u0120\u0121\3\2\2\2\u0121\u0123\3\2\2\2\u0122"+
		"\u0120\3\2\2\2\u0123\u0124\7I\2\2\u0124/\3\2\2\2\u0125\u0129\5\62\32\2"+
		"\u0126\u0129\7%\2\2\u0127\u0129\7$\2\2\u0128\u0125\3\2\2\2\u0128\u0126"+
		"\3\2\2\2\u0128\u0127\3\2\2\2\u0129\61\3\2\2\2\u012a\u012b\t\3\2\2\u012b"+
		"\63\3\2\2\2\u012c\u012d\7\6\2\2\u012d\u012e\7\'\2\2\u012e\u012f\5j\66"+
		"\2\u012f\u0130\7G\2\2\u0130\65\3\2\2\2\u0131\u0132\7\6\2\2\u0132\u0133"+
		"\t\4\2\2\u0133\u0134\5j\66\2\u0134\u0135\7<\2\2\u0135\u0136\5h\65\2\u0136"+
		"\u0137\7G\2\2\u0137\67\3\2\2\2\u0138\u0139\7\6\2\2\u0139\u013a\t\5\2\2"+
		"\u013a\u013b\5j\66\2\u013b\u013c\7<\2\2\u013c\u013d\5h\65\2\u013d\u013e"+
		"\7G\2\2\u013e9\3\2\2\2\u013f\u0140\7\34\2\2\u0140\u0141\7\35\2\2\u0141"+
		"\u0142\5j\66\2\u0142\u0143\7\36\2\2\u0143\u0144\5f\64\2\u0144\u0145\7"+
		"G\2\2\u0145;\3\2\2\2\u0146\u0147\7\31\2\2\u0147\u0148\5j\66\2\u0148\u0149"+
		"\7\36\2\2\u0149\u014a\5j\66\2\u014a\u014b\7G\2\2\u014b=\3\2\2\2\u014c"+
		"\u014d\7\32\2\2\u014d\u014e\5j\66\2\u014e\u014f\7\33\2\2\u014f\u0150\5"+
		"j\66\2\u0150\u0151\7G\2\2\u0151?\3\2\2\2\u0152\u0153\7\37\2\2\u0153\u0154"+
		"\5j\66\2\u0154\u0155\7 \2\2\u0155\u0156\5j\66\2\u0156\u0157\7!\2\2\u0157"+
		"\u0158\5n8\2\u0158\u0159\7G\2\2\u0159A\3\2\2\2\u015a\u015b\7\"\2\2\u015b"+
		"\u015c\5j\66\2\u015c\u015d\7 \2\2\u015d\u015e\5j\66\2\u015e\u015f\7G\2"+
		"\2\u015fC\3\2\2\2\u0160\u0161\7\7\2\2\u0161\u0162\5\60\31\2\u0162\u0163"+
		"\5j\66\2\u0163\u0164\7G\2\2\u0164E\3\2\2\2\u0165\u0166\7\6\2\2\u0166\u0167"+
		"\7%\2\2\u0167\u0168\5j\66\2\u0168\u016c\7H\2\2\u0169\u016b\5H%\2\u016a"+
		"\u0169\3\2\2\2\u016b\u016e\3\2\2\2\u016c\u016a\3\2\2\2\u016c\u016d\3\2"+
		"\2\2\u016d\u016f\3\2\2\2\u016e\u016c\3\2\2\2\u016f\u0170\7I\2\2\u0170"+
		"G\3\2\2\2\u0171\u0172\7\6\2\2\u0172\u0173\7\13\2\2\u0173\u0174\5j\66\2"+
		"\u0174\u0175\7\f\2\2\u0175\u0176\5N(\2\u0176\u0177\7\r\2\2\u0177\u017a"+
		"\5J&\2\u0178\u0179\7\20\2\2\u0179\u017b\5P)\2\u017a\u0178\3\2\2\2\u017a"+
		"\u017b\3\2\2\2\u017b\u017c\3\2\2\2\u017c\u017d\5T+\2\u017dI\3\2\2\2\u017e"+
		"\u0181\5L\'\2\u017f\u0180\7E\2\2\u0180\u0182\5L\'\2\u0181\u017f\3\2\2"+
		"\2\u0182\u0183\3\2\2\2\u0183\u0181\3\2\2\2\u0183\u0184\3\2\2\2\u0184K"+
		"\3\2\2\2\u0185\u0186\5\u0080A\2\u0186\u0187\7\16\2\2\u0187\u0188\5\u0080"+
		"A\2\u0188M\3\2\2\2\u0189\u0193\7\22\2\2\u018a\u018b\7+\2\2\u018b\u0193"+
		"\5j\66\2\u018c\u018d\7\23\2\2\u018d\u0193\5h\65\2\u018e\u018f\7\24\2\2"+
		"\u018f\u0193\5j\66\2\u0190\u0191\7\25\2\2\u0191\u0193\5j\66\2\u0192\u0189"+
		"\3\2\2\2\u0192\u018a\3\2\2\2\u0192\u018c\3\2\2\2\u0192\u018e\3\2\2\2\u0192"+
		"\u0190\3\2\2\2\u0193O\3\2\2\2\u0194\u019f\5j\66\2\u0195\u019f\5R*\2\u0196"+
		"\u0197\5R*\2\u0197\u0198\7<\2\2\u0198\u0199\5j\66\2\u0199\u019f\3\2\2"+
		"\2\u019a\u019b\5R*\2\u019b\u019c\7\35\2\2\u019c\u019d\5h\65\2\u019d\u019f"+
		"\3\2\2\2\u019e\u0194\3\2\2\2\u019e\u0195\3\2\2\2\u019e\u0196\3\2\2\2\u019e"+
		"\u019a\3\2\2\2\u019fQ\3\2\2\2\u01a0\u01a1\7-\2\2\u01a1\u01a2\7\n\2\2\u01a2"+
		"S\3\2\2\2\u01a3\u01a4\7\21\2\2\u01a4\u01a5\7N\2\2\u01a5\u01a6\5\u0080"+
		"A\2\u01a6\u01a7\7O\2\2\u01a7\u01a8\5V,\2\u01a8U\3\2\2\2\u01a9\u01ad\7"+
		"H\2\2\u01aa\u01ac\5X-\2\u01ab\u01aa\3\2\2\2\u01ac\u01af\3\2\2\2\u01ad"+
		"\u01ab\3\2\2\2\u01ad\u01ae\3\2\2\2\u01ae\u01b0\3\2\2\2\u01af\u01ad\3\2"+
		"\2\2\u01b0\u01b1\7I\2\2\u01b1W\3\2\2\2\u01b2\u01b7\5\6\4\2\u01b3\u01b7"+
		"\5H%\2\u01b4\u01b7\5Z.\2\u01b5\u01b7\5\\/\2\u01b6\u01b2\3\2\2\2\u01b6"+
		"\u01b3\3\2\2\2\u01b6\u01b4\3\2\2\2\u01b6\u01b5\3\2\2\2\u01b7Y\3\2\2\2"+
		"\u01b8\u01b9\7\7\2\2\u01b9\u01ba\7\13\2\2\u01ba\u01bb\5j\66\2\u01bb\u01bc"+
		"\7\33\2\2\u01bc\u01bd\7%\2\2\u01bd\u01be\5j\66\2\u01be\u01bf\7G\2\2\u01bf"+
		"[\3\2\2\2\u01c0\u01c1\7\17\2\2\u01c1\u01c2\5\u0080A\2\u01c2\u01c6\7H\2"+
		"\2\u01c3\u01c5\5X-\2\u01c4\u01c3\3\2\2\2\u01c5\u01c8\3\2\2\2\u01c6\u01c4"+
		"\3\2\2\2\u01c6\u01c7\3\2\2\2\u01c7\u01c9\3\2\2\2\u01c8\u01c6\3\2\2\2\u01c9"+
		"\u01ca\7I\2\2\u01ca]\3\2\2\2\u01cb\u01cc\7\6\2\2\u01cc\u01cd\7$\2\2\u01cd"+
		"\u01ce\5j\66\2\u01ce\u01cf\7#\2\2\u01cf\u01d0\t\6\2\2\u01d0\u01d1\5j\66"+
		"\2\u01d1\u01d2\7&\2\2\u01d2\u01d3\5n8\2\u01d3\u01d4\7\20\2\2\u01d4\u01d5"+
		"\t\7\2\2\u01d5\u01d6\7\35\2\2\u01d6\u01d7\5`\61\2\u01d7\u01d8\7G\2\2\u01d8"+
		"_\3\2\2\2\u01d9\u01de\5b\62\2\u01da\u01db\7E\2\2\u01db\u01dd\5b\62\2\u01dc"+
		"\u01da\3\2\2\2\u01dd\u01e0\3\2\2\2\u01de\u01dc\3\2\2\2\u01de\u01df\3\2"+
		"\2\2\u01df\u01e2\3\2\2\2\u01e0\u01de\3\2\2\2\u01e1\u01d9\3\2\2\2\u01e1"+
		"\u01e2\3\2\2\2\u01e2a\3\2\2\2\u01e3\u01e5\7P\2\2\u01e4\u01e3\3\2\2\2\u01e4"+
		"\u01e5\3\2\2\2\u01e5\u01e6\3\2\2\2\u01e6\u01e7\5j\66\2\u01e7c\3\2\2\2"+
		"\u01e8\u01e9\7\30\2\2\u01e9\u01ea\5n8\2\u01ea\u01eb\7G\2\2\u01ebe\3\2"+
		"\2\2\u01ec\u01f0\5z>\2\u01ed\u01f0\5|?\2\u01ee\u01f0\5x=\2\u01ef\u01ec"+
		"\3\2\2\2\u01ef\u01ed\3\2\2\2\u01ef\u01ee\3\2\2\2\u01f0g\3\2\2\2\u01f1"+
		"\u01f6\5j\66\2\u01f2\u01f3\7E\2\2\u01f3\u01f5\5j\66\2\u01f4\u01f2\3\2"+
		"\2\2\u01f5\u01f8\3\2\2\2\u01f6\u01f4\3\2\2\2\u01f6\u01f7\3\2\2\2\u01f7"+
		"i\3\2\2\2\u01f8\u01f6\3\2\2\2\u01f9\u01fc\5z>\2\u01fa\u01fc\5|?\2\u01fb"+
		"\u01f9\3\2\2\2\u01fb\u01fa\3\2\2\2\u01fck\3\2\2\2\u01fd\u0206\7J\2\2\u01fe"+
		"\u0203\5f\64\2\u01ff\u0200\7E\2\2\u0200\u0202\5f\64\2\u0201\u01ff\3\2"+
		"\2\2\u0202\u0205\3\2\2\2\u0203\u0201\3\2\2\2\u0203\u0204\3\2\2\2\u0204"+
		"\u0207\3\2\2\2\u0205\u0203\3\2\2\2\u0206\u01fe\3\2\2\2\u0206\u0207\3\2"+
		"\2\2\u0207\u0208\3\2\2\2\u0208\u0209\7K\2\2\u0209m\3\2\2\2\u020a\u020f"+
		"\5p9\2\u020b\u020c\7E\2\2\u020c\u020e\5p9\2\u020d\u020b\3\2\2\2\u020e"+
		"\u0211\3\2\2\2\u020f\u020d\3\2\2\2\u020f\u0210\3\2\2\2\u0210\u0213\3\2"+
		"\2\2\u0211\u020f\3\2\2\2\u0212\u020a\3\2\2\2\u0212\u0213\3\2\2\2\u0213"+
		"o\3\2\2\2\u0214\u0219\7\3\2\2\u0215\u0219\7\4\2\2\u0216\u0219\7\5\2\2"+
		"\u0217\u0219\5\u0080A\2\u0218\u0214\3\2\2\2\u0218\u0215\3\2\2\2\u0218"+
		"\u0216\3\2\2\2\u0218\u0217\3\2\2\2\u0219q\3\2\2\2\u021a\u0223\7H\2\2\u021b"+
		"\u0220\5t;\2\u021c\u021d\7E\2\2\u021d\u021f\5t;\2\u021e\u021c\3\2\2\2"+
		"\u021f\u0222\3\2\2\2\u0220\u021e\3\2\2\2\u0220\u0221\3\2\2\2\u0221\u0224"+
		"\3\2\2\2\u0222\u0220\3\2\2\2\u0223\u021b\3\2\2\2\u0223\u0224\3\2\2\2\u0224"+
		"\u0225\3\2\2\2\u0225\u0226\7I\2\2\u0226s\3\2\2\2\u0227\u0228\5f\64\2\u0228"+
		"\u0229\7F\2\2\u0229\u022a\5f\64\2\u022au\3\2\2\2\u022b\u0230\5\u0080A"+
		"\2\u022c\u022d\7J\2\2\u022d\u022e\5f\64\2\u022e\u022f\7K\2\2\u022f\u0231"+
		"\3\2\2\2\u0230\u022c\3\2\2\2\u0231\u0232\3\2\2\2\u0232\u0230\3\2\2\2\u0232"+
		"\u0233\3\2\2\2\u0233w\3\2\2\2\u0234\u0239\7B\2\2\u0235\u0239\7\62\2\2"+
		"\u0236\u0239\5l\67\2\u0237\u0239\5r:\2\u0238\u0234\3\2\2\2\u0238\u0235"+
		"\3\2\2\2\u0238\u0236\3\2\2\2\u0238\u0237\3\2\2\2\u0239y\3\2\2\2\u023a"+
		"\u023d\5\u0080A\2\u023b\u023d\5v<\2\u023c\u023a\3\2\2\2\u023c\u023b\3"+
		"\2\2\2\u023d{\3\2\2\2\u023e\u023f\5\u0080A\2\u023f\u0240\5~@\2\u0240}"+
		"\3\2\2\2\u0241\u024a\7N\2\2\u0242\u0247\5f\64\2\u0243\u0244\7E\2\2\u0244"+
		"\u0246\5f\64\2\u0245\u0243\3\2\2\2\u0246\u0249\3\2\2\2\u0247\u0245\3\2"+
		"\2\2\u0247\u0248\3\2\2\2\u0248\u024b\3\2\2\2\u0249\u0247\3\2\2\2\u024a"+
		"\u0242\3\2\2\2\u024a\u024b\3\2\2\2\u024b\u024c\3\2\2\2\u024c\u024d\7O"+
		"\2\2\u024d\177\3\2\2\2\u024e\u0251\7A\2\2\u024f\u0251\5\u0082B\2\u0250"+
		"\u024e\3\2\2\2\u0250\u024f\3\2\2\2\u0251\u0081\3\2\2\2\u0252\u0253\t\b"+
		"\2\2\u0253\u0083\3\2\2\2,\u008a\u00a3\u00a6\u00b3\u00bc\u00bf\u00c9\u00cf"+
		"\u00d5\u00de\u00fd\u0101\u0110\u0120\u0128\u016c\u017a\u0183\u0192\u019e"+
		"\u01ad\u01b6\u01c6\u01de\u01e1\u01e4\u01ef\u01f6\u01fb\u0203\u0206\u020f"+
		"\u0212\u0218\u0220\u0223\u0232\u0238\u023c\u0247\u024a\u0250";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}