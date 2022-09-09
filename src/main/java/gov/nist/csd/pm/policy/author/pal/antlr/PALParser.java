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
		T__0=1, T__1=2, T__2=3, CREATE=4, UPDATE=5, DELETE=6, BREAK=7, CONTINUE=8, 
		POLICY_ELEMENT=9, RULE=10, WHEN=11, PERFORMS=12, ON=13, DO=14, ANY_USER=15, 
		USERS=16, ANY_USER_WITH_ATTRIBUTE=17, PROCESS=18, INTERSECTION=19, UNION=20, 
		SET_RESOURCE_ACCESS_RIGHTS=21, ASSIGN=22, DEASSIGN=23, FROM=24, SET_PROPERTIES=25, 
		OF=26, TO=27, ASSOCIATE=28, AND=29, WITH_ACCESS_RIGHTS=30, DISSOCIATE=31, 
		ASSIGN_TO=32, DEASSIGN_FROM=33, DENY=34, PROHIBITION=35, OBLIGATION=36, 
		ACCESS_RIGHTS=37, POLICY_CLASS=38, OBJECT_ATTRIBUTE=39, USER_ATTRIBUTE=40, 
		OBJECT=41, USER=42, ATTR=43, ANY=44, LET=45, CONST=46, FUNCTION=47, RETURN=48, 
		BOOLEAN=49, TRUE=50, FALSE=51, STRING_TYPE=52, BOOLEAN_TYPE=53, VOID_TYPE=54, 
		ARRAY_TYPE=55, MAP_TYPE=56, FOREACH=57, IN=58, IF=59, ELSE=60, IDENTIFIER=61, 
		STRING=62, LINE_COMMENT=63, WS=64, COMMA=65, COLON=66, SEMI_COLON=67, 
		OPEN_CURLY=68, CLOSE_CURLY=69, OPEN_BRACKET=70, CLOSE_BRACKET=71, OPEN_ANGLE_BRACKET=72, 
		CLOSE_ANGLE_BRACKET=73, OPEN_PAREN=74, CLOSE_PAREN=75, IS_COMPLEMENT=76, 
		EQUALS=77, DOT=78;
	public static final int
		RULE_pal = 0, RULE_stmts = 1, RULE_stmt = 2, RULE_varStmt = 3, RULE_funcDefStmt = 4, 
		RULE_formalArgList = 5, RULE_formalArg = 6, RULE_formalArgType = 7, RULE_funcReturnStmt = 8, 
		RULE_funcReturnType = 9, RULE_funcBody = 10, RULE_foreachStmt = 11, RULE_breakStmt = 12, 
		RULE_continueStmt = 13, RULE_funcCallStmt = 14, RULE_ifStmt = 15, RULE_elseIfStmt = 16, 
		RULE_elseStmt = 17, RULE_varType = 18, RULE_mapType = 19, RULE_arrayType = 20, 
		RULE_stmtBlock = 21, RULE_deleteType = 22, RULE_nodeType = 23, RULE_createStmt = 24, 
		RULE_createPolicyStmt = 25, RULE_createAttrStmt = 26, RULE_createUserOrObjectStmt = 27, 
		RULE_setNodePropsStmt = 28, RULE_assignStmt = 29, RULE_deassignStmt = 30, 
		RULE_associateStmt = 31, RULE_dissociateStmt = 32, RULE_deleteStmt = 33, 
		RULE_createObligationStmt = 34, RULE_createRuleStmt = 35, RULE_subjectClause = 36, 
		RULE_onClause = 37, RULE_anyPe = 38, RULE_response = 39, RULE_responseBlock = 40, 
		RULE_responseStmts = 41, RULE_responseStmt = 42, RULE_deleteRuleStmt = 43, 
		RULE_createProhibitionStmt = 44, RULE_prohibitionContainerList = 45, RULE_prohibitionContainerExpression = 46, 
		RULE_setResourceAccessRightsStmt = 47, RULE_expression = 48, RULE_array = 49, 
		RULE_accessRightArray = 50, RULE_accessRight = 51, RULE_map = 52, RULE_mapEntry = 53, 
		RULE_mapEntryRef = 54, RULE_literal = 55, RULE_varRef = 56, RULE_funcCall = 57, 
		RULE_funcCallArgs = 58;
	private static String[] makeRuleNames() {
		return new String[] {
			"pal", "stmts", "stmt", "varStmt", "funcDefStmt", "formalArgList", "formalArg", 
			"formalArgType", "funcReturnStmt", "funcReturnType", "funcBody", "foreachStmt", 
			"breakStmt", "continueStmt", "funcCallStmt", "ifStmt", "elseIfStmt", 
			"elseStmt", "varType", "mapType", "arrayType", "stmtBlock", "deleteType", 
			"nodeType", "createStmt", "createPolicyStmt", "createAttrStmt", "createUserOrObjectStmt", 
			"setNodePropsStmt", "assignStmt", "deassignStmt", "associateStmt", "dissociateStmt", 
			"deleteStmt", "createObligationStmt", "createRuleStmt", "subjectClause", 
			"onClause", "anyPe", "response", "responseBlock", "responseStmts", "responseStmt", 
			"deleteRuleStmt", "createProhibitionStmt", "prohibitionContainerList", 
			"prohibitionContainerExpression", "setResourceAccessRightsStmt", "expression", 
			"array", "accessRightArray", "accessRight", "map", "mapEntry", "mapEntryRef", 
			"literal", "varRef", "funcCall", "funcCallArgs"
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
			null, null, null, null, null, null, null, "':'", "';'", "'{'", "'}'", 
			"'['", "']'", "'<'", "'>'", "'('", "')'", "'!'", "'='", "'.'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, "CREATE", "UPDATE", "DELETE", "BREAK", "CONTINUE", 
			"POLICY_ELEMENT", "RULE", "WHEN", "PERFORMS", "ON", "DO", "ANY_USER", 
			"USERS", "ANY_USER_WITH_ATTRIBUTE", "PROCESS", "INTERSECTION", "UNION", 
			"SET_RESOURCE_ACCESS_RIGHTS", "ASSIGN", "DEASSIGN", "FROM", "SET_PROPERTIES", 
			"OF", "TO", "ASSOCIATE", "AND", "WITH_ACCESS_RIGHTS", "DISSOCIATE", "ASSIGN_TO", 
			"DEASSIGN_FROM", "DENY", "PROHIBITION", "OBLIGATION", "ACCESS_RIGHTS", 
			"POLICY_CLASS", "OBJECT_ATTRIBUTE", "USER_ATTRIBUTE", "OBJECT", "USER", 
			"ATTR", "ANY", "LET", "CONST", "FUNCTION", "RETURN", "BOOLEAN", "TRUE", 
			"FALSE", "STRING_TYPE", "BOOLEAN_TYPE", "VOID_TYPE", "ARRAY_TYPE", "MAP_TYPE", 
			"FOREACH", "IN", "IF", "ELSE", "IDENTIFIER", "STRING", "LINE_COMMENT", 
			"WS", "COMMA", "COLON", "SEMI_COLON", "OPEN_CURLY", "CLOSE_CURLY", "OPEN_BRACKET", 
			"CLOSE_BRACKET", "OPEN_ANGLE_BRACKET", "CLOSE_ANGLE_BRACKET", "OPEN_PAREN", 
			"CLOSE_PAREN", "IS_COMPLEMENT", "EQUALS", "DOT"
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
			setState(118);
			stmts();
			setState(119);
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
			setState(124);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << CREATE) | (1L << DELETE) | (1L << BREAK) | (1L << CONTINUE) | (1L << SET_RESOURCE_ACCESS_RIGHTS) | (1L << ASSIGN) | (1L << DEASSIGN) | (1L << SET_PROPERTIES) | (1L << ASSOCIATE) | (1L << DISSOCIATE) | (1L << LET) | (1L << CONST) | (1L << FUNCTION) | (1L << RETURN) | (1L << FOREACH) | (1L << IF) | (1L << IDENTIFIER))) != 0)) {
				{
				{
				setState(121);
				stmt();
				}
				}
				setState(126);
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
		public CreateStmtContext createStmt() {
			return getRuleContext(CreateStmtContext.class,0);
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
			setState(144);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				{
				setState(127);
				varStmt();
				}
				break;
			case 2:
				{
				setState(128);
				funcDefStmt();
				}
				break;
			case 3:
				{
				setState(129);
				funcReturnStmt();
				}
				break;
			case 4:
				{
				setState(130);
				foreachStmt();
				}
				break;
			case 5:
				{
				setState(131);
				breakStmt();
				}
				break;
			case 6:
				{
				setState(132);
				continueStmt();
				}
				break;
			case 7:
				{
				setState(133);
				funcCallStmt();
				}
				break;
			case 8:
				{
				setState(134);
				ifStmt();
				}
				break;
			case 9:
				{
				setState(135);
				createStmt();
				}
				break;
			case 10:
				{
				setState(136);
				setNodePropsStmt();
				}
				break;
			case 11:
				{
				setState(137);
				assignStmt();
				}
				break;
			case 12:
				{
				setState(138);
				deassignStmt();
				}
				break;
			case 13:
				{
				setState(139);
				deleteStmt();
				}
				break;
			case 14:
				{
				setState(140);
				associateStmt();
				}
				break;
			case 15:
				{
				setState(141);
				dissociateStmt();
				}
				break;
			case 16:
				{
				setState(142);
				setResourceAccessRightsStmt();
				}
				break;
			case 17:
				{
				setState(143);
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
		public TerminalNode IDENTIFIER() { return getToken(PALParser.IDENTIFIER, 0); }
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
			setState(147);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LET || _la==CONST) {
				{
				setState(146);
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

			setState(149);
			match(IDENTIFIER);
			setState(150);
			match(EQUALS);
			setState(151);
			expression();
			setState(152);
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
		public TerminalNode IDENTIFIER() { return getToken(PALParser.IDENTIFIER, 0); }
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
			setState(154);
			match(FUNCTION);
			setState(155);
			match(IDENTIFIER);
			setState(156);
			match(OPEN_PAREN);
			setState(157);
			formalArgList();
			setState(158);
			match(CLOSE_PAREN);
			setState(160);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 44)) & ~0x3f) == 0 && ((1L << (_la - 44)) & ((1L << (ANY - 44)) | (1L << (STRING_TYPE - 44)) | (1L << (BOOLEAN_TYPE - 44)) | (1L << (VOID_TYPE - 44)) | (1L << (MAP_TYPE - 44)) | (1L << (OPEN_BRACKET - 44)))) != 0)) {
				{
				setState(159);
				funcReturnType();
				}
			}

			setState(162);
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
			setState(172);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 44)) & ~0x3f) == 0 && ((1L << (_la - 44)) & ((1L << (ANY - 44)) | (1L << (STRING_TYPE - 44)) | (1L << (BOOLEAN_TYPE - 44)) | (1L << (MAP_TYPE - 44)) | (1L << (OPEN_BRACKET - 44)))) != 0)) {
				{
				setState(164);
				formalArg();
				setState(169);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(165);
					match(COMMA);
					setState(166);
					formalArg();
					}
					}
					setState(171);
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
		public TerminalNode IDENTIFIER() { return getToken(PALParser.IDENTIFIER, 0); }
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
			setState(174);
			formalArgType();
			setState(175);
			match(IDENTIFIER);
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
			setState(177);
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
			setState(182);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				{
				setState(179);
				match(RETURN);
				setState(180);
				expression();
				}
				break;
			case 2:
				{
				setState(181);
				match(RETURN);
				}
				break;
			}
			setState(184);
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
			setState(188);
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
				setState(186);
				varType();
				}
				break;
			case VOID_TYPE:
				_localctx = new VoidReturnTypeContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(187);
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
			setState(190);
			match(OPEN_CURLY);
			setState(194);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << CREATE) | (1L << DELETE) | (1L << BREAK) | (1L << CONTINUE) | (1L << SET_RESOURCE_ACCESS_RIGHTS) | (1L << ASSIGN) | (1L << DEASSIGN) | (1L << SET_PROPERTIES) | (1L << ASSOCIATE) | (1L << DISSOCIATE) | (1L << LET) | (1L << CONST) | (1L << FUNCTION) | (1L << RETURN) | (1L << FOREACH) | (1L << IF) | (1L << IDENTIFIER))) != 0)) {
				{
				{
				setState(191);
				stmt();
				}
				}
				setState(196);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(197);
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
		public List<TerminalNode> IDENTIFIER() { return getTokens(PALParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(PALParser.IDENTIFIER, i);
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
			setState(199);
			match(FOREACH);
			setState(200);
			((ForeachStmtContext)_localctx).key = match(IDENTIFIER);
			setState(203);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(201);
				match(COMMA);
				setState(202);
				((ForeachStmtContext)_localctx).mapValue = match(IDENTIFIER);
				}
			}

			setState(205);
			match(IN);
			setState(206);
			expression();
			setState(207);
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
			setState(209);
			match(BREAK);
			setState(210);
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
			setState(212);
			match(CONTINUE);
			setState(213);
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
			setState(215);
			funcCall();
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
		enterRule(_localctx, 30, RULE_ifStmt);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(218);
			match(IF);
			setState(219);
			((IfStmtContext)_localctx).condition = expression();
			setState(220);
			stmtBlock();
			setState(224);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(221);
					elseIfStmt();
					}
					} 
				}
				setState(226);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			}
			setState(228);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ELSE) {
				{
				setState(227);
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
		enterRule(_localctx, 32, RULE_elseIfStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(230);
			match(ELSE);
			setState(231);
			match(IF);
			setState(232);
			((ElseIfStmtContext)_localctx).condition = expression();
			setState(233);
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
			setState(235);
			match(ELSE);
			setState(236);
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
			setState(243);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STRING_TYPE:
				_localctx = new StringTypeContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(238);
				match(STRING_TYPE);
				}
				break;
			case BOOLEAN_TYPE:
				_localctx = new BooleanTypeContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(239);
				match(BOOLEAN_TYPE);
				}
				break;
			case OPEN_BRACKET:
				_localctx = new ArrayVarTypeContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(240);
				arrayType();
				}
				break;
			case MAP_TYPE:
				_localctx = new MapVarTypeContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(241);
				mapType();
				}
				break;
			case ANY:
				_localctx = new AnyTypeContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(242);
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
			setState(245);
			match(MAP_TYPE);
			setState(246);
			match(OPEN_BRACKET);
			setState(247);
			((MapTypeContext)_localctx).keyType = varType();
			setState(248);
			match(CLOSE_BRACKET);
			setState(249);
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
			setState(251);
			match(OPEN_BRACKET);
			setState(252);
			match(CLOSE_BRACKET);
			setState(253);
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
			setState(255);
			match(OPEN_CURLY);
			setState(259);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << CREATE) | (1L << DELETE) | (1L << BREAK) | (1L << CONTINUE) | (1L << SET_RESOURCE_ACCESS_RIGHTS) | (1L << ASSIGN) | (1L << DEASSIGN) | (1L << SET_PROPERTIES) | (1L << ASSOCIATE) | (1L << DISSOCIATE) | (1L << LET) | (1L << CONST) | (1L << FUNCTION) | (1L << RETURN) | (1L << FOREACH) | (1L << IF) | (1L << IDENTIFIER))) != 0)) {
				{
				{
				setState(256);
				stmt();
				}
				}
				setState(261);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(262);
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
			setState(267);
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
				setState(264);
				nodeType();
				}
				break;
			case OBLIGATION:
				_localctx = new DeleteObligationContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(265);
				match(OBLIGATION);
				}
				break;
			case PROHIBITION:
				_localctx = new DeleteProhibitionContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(266);
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
			setState(269);
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

	public static class CreateStmtContext extends ParserRuleContext {
		public TerminalNode CREATE() { return getToken(PALParser.CREATE, 0); }
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
		public CreateStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_createStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterCreateStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitCreateStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitCreateStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreateStmtContext createStmt() throws RecognitionException {
		CreateStmtContext _localctx = new CreateStmtContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_createStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(271);
			match(CREATE);
			setState(277);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case POLICY_CLASS:
				{
				setState(272);
				createPolicyStmt();
				}
				break;
			case OBJECT_ATTRIBUTE:
			case USER_ATTRIBUTE:
				{
				setState(273);
				createAttrStmt();
				}
				break;
			case OBJECT:
			case USER:
				{
				setState(274);
				createUserOrObjectStmt();
				}
				break;
			case OBLIGATION:
				{
				setState(275);
				createObligationStmt();
				}
				break;
			case PROHIBITION:
				{
				setState(276);
				createProhibitionStmt();
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

	public static class CreatePolicyStmtContext extends ParserRuleContext {
		public ExpressionContext name;
		public TerminalNode POLICY_CLASS() { return getToken(PALParser.POLICY_CLASS, 0); }
		public TerminalNode SEMI_COLON() { return getToken(PALParser.SEMI_COLON, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
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
			setState(279);
			match(POLICY_CLASS);
			setState(280);
			((CreatePolicyStmtContext)_localctx).name = expression();
			setState(281);
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
		public ExpressionContext assignTo;
		public TerminalNode ASSIGN_TO() { return getToken(PALParser.ASSIGN_TO, 0); }
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
		enterRule(_localctx, 52, RULE_createAttrStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(283);
			_la = _input.LA(1);
			if ( !(_la==OBJECT_ATTRIBUTE || _la==USER_ATTRIBUTE) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(284);
			((CreateAttrStmtContext)_localctx).name = expression();
			setState(285);
			match(ASSIGN_TO);
			setState(286);
			((CreateAttrStmtContext)_localctx).assignTo = expression();
			setState(287);
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
		public ExpressionContext assignTo;
		public TerminalNode ASSIGN_TO() { return getToken(PALParser.ASSIGN_TO, 0); }
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
		enterRule(_localctx, 54, RULE_createUserOrObjectStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(289);
			_la = _input.LA(1);
			if ( !(_la==OBJECT || _la==USER) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(290);
			((CreateUserOrObjectStmtContext)_localctx).name = expression();
			setState(291);
			match(ASSIGN_TO);
			setState(292);
			((CreateUserOrObjectStmtContext)_localctx).assignTo = expression();
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
		enterRule(_localctx, 56, RULE_setNodePropsStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(295);
			match(SET_PROPERTIES);
			setState(296);
			match(OF);
			setState(297);
			((SetNodePropsStmtContext)_localctx).name = expression();
			setState(298);
			match(TO);
			setState(299);
			((SetNodePropsStmtContext)_localctx).properties = expression();
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

	public static class AssignStmtContext extends ParserRuleContext {
		public ExpressionContext child;
		public ExpressionContext assignTo;
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
		enterRule(_localctx, 58, RULE_assignStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(302);
			match(ASSIGN);
			setState(303);
			((AssignStmtContext)_localctx).child = expression();
			setState(304);
			match(TO);
			setState(305);
			((AssignStmtContext)_localctx).assignTo = expression();
			setState(306);
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
		public ExpressionContext child;
		public ExpressionContext deassignFrom;
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
		enterRule(_localctx, 60, RULE_deassignStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(308);
			match(DEASSIGN);
			setState(309);
			((DeassignStmtContext)_localctx).child = expression();
			setState(310);
			match(FROM);
			setState(311);
			((DeassignStmtContext)_localctx).deassignFrom = expression();
			setState(312);
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
		public AccessRightArrayContext accessRights;
		public TerminalNode ASSOCIATE() { return getToken(PALParser.ASSOCIATE, 0); }
		public TerminalNode AND() { return getToken(PALParser.AND, 0); }
		public TerminalNode WITH_ACCESS_RIGHTS() { return getToken(PALParser.WITH_ACCESS_RIGHTS, 0); }
		public TerminalNode SEMI_COLON() { return getToken(PALParser.SEMI_COLON, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
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
			setState(314);
			match(ASSOCIATE);
			setState(315);
			((AssociateStmtContext)_localctx).ua = expression();
			setState(316);
			match(AND);
			setState(317);
			((AssociateStmtContext)_localctx).target = expression();
			setState(318);
			match(WITH_ACCESS_RIGHTS);
			setState(319);
			((AssociateStmtContext)_localctx).accessRights = accessRightArray();
			setState(320);
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
		enterRule(_localctx, 64, RULE_dissociateStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(322);
			match(DISSOCIATE);
			setState(323);
			((DissociateStmtContext)_localctx).ua = expression();
			setState(324);
			match(AND);
			setState(325);
			((DissociateStmtContext)_localctx).target = expression();
			setState(326);
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
		public ExpressionContext name;
		public TerminalNode DELETE() { return getToken(PALParser.DELETE, 0); }
		public DeleteTypeContext deleteType() {
			return getRuleContext(DeleteTypeContext.class,0);
		}
		public TerminalNode SEMI_COLON() { return getToken(PALParser.SEMI_COLON, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
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
			setState(328);
			match(DELETE);
			setState(329);
			deleteType();
			setState(330);
			((DeleteStmtContext)_localctx).name = expression();
			setState(331);
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
		public ExpressionContext label;
		public TerminalNode OBLIGATION() { return getToken(PALParser.OBLIGATION, 0); }
		public TerminalNode OPEN_CURLY() { return getToken(PALParser.OPEN_CURLY, 0); }
		public TerminalNode CLOSE_CURLY() { return getToken(PALParser.CLOSE_CURLY, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
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
			setState(333);
			match(OBLIGATION);
			setState(334);
			((CreateObligationStmtContext)_localctx).label = expression();
			setState(335);
			match(OPEN_CURLY);
			setState(339);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==CREATE) {
				{
				{
				setState(336);
				createRuleStmt();
				}
				}
				setState(341);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(342);
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
		public ExpressionContext label;
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
		enterRule(_localctx, 70, RULE_createRuleStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(344);
			match(CREATE);
			setState(345);
			match(RULE);
			setState(346);
			((CreateRuleStmtContext)_localctx).label = expression();
			setState(347);
			match(WHEN);
			setState(348);
			subjectClause();
			setState(349);
			match(PERFORMS);
			setState(350);
			((CreateRuleStmtContext)_localctx).performsClause = expression();
			setState(353);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ON) {
				{
				setState(351);
				match(ON);
				setState(352);
				onClause();
				}
			}

			setState(355);
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
		enterRule(_localctx, 72, RULE_subjectClause);
		try {
			setState(366);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ANY_USER:
				_localctx = new AnyUserSubjectContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(357);
				match(ANY_USER);
				}
				break;
			case USER:
				_localctx = new UserSubjectContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(358);
				match(USER);
				setState(359);
				((UserSubjectContext)_localctx).user = expression();
				}
				break;
			case USERS:
				_localctx = new UsersListSubjectContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(360);
				match(USERS);
				setState(361);
				((UsersListSubjectContext)_localctx).users = expression();
				}
				break;
			case ANY_USER_WITH_ATTRIBUTE:
				_localctx = new UserAttrSubjectContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(362);
				match(ANY_USER_WITH_ATTRIBUTE);
				setState(363);
				((UserAttrSubjectContext)_localctx).attribute = expression();
				}
				break;
			case PROCESS:
				_localctx = new ProcessSubjectContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(364);
				match(PROCESS);
				setState(365);
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
		enterRule(_localctx, 74, RULE_onClause);
		try {
			setState(378);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
			case 1:
				_localctx = new PolicyElementContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(368);
				expression();
				}
				break;
			case 2:
				_localctx = new AnyPolicyElementContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(369);
				anyPe();
				}
				break;
			case 3:
				_localctx = new AnyContainedInContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(370);
				anyPe();
				setState(371);
				match(IN);
				setState(372);
				expression();
				}
				break;
			case 4:
				_localctx = new AnyOfSetContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(374);
				anyPe();
				setState(375);
				match(OF);
				setState(376);
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
		enterRule(_localctx, 76, RULE_anyPe);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(380);
			match(ANY);
			setState(381);
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
		public TerminalNode IDENTIFIER() { return getToken(PALParser.IDENTIFIER, 0); }
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
		enterRule(_localctx, 78, RULE_response);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(383);
			match(DO);
			setState(384);
			match(OPEN_PAREN);
			setState(385);
			match(IDENTIFIER);
			setState(386);
			match(CLOSE_PAREN);
			setState(387);
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
		public ResponseStmtsContext responseStmts() {
			return getRuleContext(ResponseStmtsContext.class,0);
		}
		public TerminalNode CLOSE_CURLY() { return getToken(PALParser.CLOSE_CURLY, 0); }
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
		enterRule(_localctx, 80, RULE_responseBlock);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(389);
			match(OPEN_CURLY);
			setState(390);
			responseStmts();
			setState(391);
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

	public static class ResponseStmtsContext extends ParserRuleContext {
		public List<ResponseStmtContext> responseStmt() {
			return getRuleContexts(ResponseStmtContext.class);
		}
		public ResponseStmtContext responseStmt(int i) {
			return getRuleContext(ResponseStmtContext.class,i);
		}
		public ResponseStmtsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_responseStmts; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterResponseStmts(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitResponseStmts(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitResponseStmts(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ResponseStmtsContext responseStmts() throws RecognitionException {
		ResponseStmtsContext _localctx = new ResponseStmtsContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_responseStmts);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(396);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << CREATE) | (1L << DELETE) | (1L << BREAK) | (1L << CONTINUE) | (1L << SET_RESOURCE_ACCESS_RIGHTS) | (1L << ASSIGN) | (1L << DEASSIGN) | (1L << SET_PROPERTIES) | (1L << ASSOCIATE) | (1L << DISSOCIATE) | (1L << LET) | (1L << CONST) | (1L << FUNCTION) | (1L << RETURN) | (1L << FOREACH) | (1L << IF) | (1L << IDENTIFIER))) != 0)) {
				{
				{
				setState(393);
				responseStmt();
				}
				}
				setState(398);
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

	public static class ResponseStmtContext extends ParserRuleContext {
		public StmtContext stmt() {
			return getRuleContext(StmtContext.class,0);
		}
		public CreateRuleStmtContext createRuleStmt() {
			return getRuleContext(CreateRuleStmtContext.class,0);
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
		enterRule(_localctx, 84, RULE_responseStmt);
		try {
			setState(401);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(399);
				stmt();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(400);
				createRuleStmt();
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
		enterRule(_localctx, 86, RULE_deleteRuleStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(403);
			match(DELETE);
			setState(404);
			match(RULE);
			setState(405);
			((DeleteRuleStmtContext)_localctx).ruleName = expression();
			setState(406);
			match(FROM);
			setState(407);
			match(OBLIGATION);
			setState(408);
			((DeleteRuleStmtContext)_localctx).obligationName = expression();
			setState(409);
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
		public ExpressionContext label;
		public ExpressionContext subject;
		public AccessRightArrayContext accessRights;
		public ProhibitionContainerListContext containers;
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
		enterRule(_localctx, 88, RULE_createProhibitionStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(411);
			match(PROHIBITION);
			setState(412);
			((CreateProhibitionStmtContext)_localctx).label = expression();
			setState(413);
			match(DENY);
			setState(414);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PROCESS) | (1L << USER_ATTRIBUTE) | (1L << USER))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(415);
			((CreateProhibitionStmtContext)_localctx).subject = expression();
			setState(416);
			match(ACCESS_RIGHTS);
			setState(417);
			((CreateProhibitionStmtContext)_localctx).accessRights = accessRightArray();
			setState(418);
			match(ON);
			setState(419);
			_la = _input.LA(1);
			if ( !(_la==INTERSECTION || _la==UNION) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(420);
			match(OF);
			setState(421);
			((CreateProhibitionStmtContext)_localctx).containers = prohibitionContainerList();
			setState(422);
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
		enterRule(_localctx, 90, RULE_prohibitionContainerList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(432);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 49)) & ~0x3f) == 0 && ((1L << (_la - 49)) & ((1L << (BOOLEAN - 49)) | (1L << (IDENTIFIER - 49)) | (1L << (STRING - 49)) | (1L << (OPEN_CURLY - 49)) | (1L << (OPEN_BRACKET - 49)) | (1L << (IS_COMPLEMENT - 49)))) != 0)) {
				{
				setState(424);
				prohibitionContainerExpression();
				setState(429);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(425);
					match(COMMA);
					setState(426);
					prohibitionContainerExpression();
					}
					}
					setState(431);
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
		enterRule(_localctx, 92, RULE_prohibitionContainerExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(435);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IS_COMPLEMENT) {
				{
				setState(434);
				match(IS_COMPLEMENT);
				}
			}

			setState(437);
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
		enterRule(_localctx, 94, RULE_setResourceAccessRightsStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(439);
			match(SET_RESOURCE_ACCESS_RIGHTS);
			setState(440);
			accessRightArray();
			setState(441);
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
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
	 
		public ExpressionContext() { }
		public void copyFrom(ExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class VariableReferenceContext extends ExpressionContext {
		public VarRefContext varRef() {
			return getRuleContext(VarRefContext.class,0);
		}
		public VariableReferenceContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterVariableReference(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitVariableReference(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitVariableReference(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LiteralExprContext extends ExpressionContext {
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public LiteralExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterLiteralExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitLiteralExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitLiteralExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FunctionCallContext extends ExpressionContext {
		public FuncCallContext funcCall() {
			return getRuleContext(FuncCallContext.class,0);
		}
		public FunctionCallContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).enterFunctionCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PALListener ) ((PALListener)listener).exitFunctionCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PALVisitor ) return ((PALVisitor<? extends T>)visitor).visitFunctionCall(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_expression);
		try {
			setState(446);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,25,_ctx) ) {
			case 1:
				_localctx = new VariableReferenceContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(443);
				varRef();
				}
				break;
			case 2:
				_localctx = new FunctionCallContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(444);
				funcCall();
				}
				break;
			case 3:
				_localctx = new LiteralExprContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(445);
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
		enterRule(_localctx, 98, RULE_array);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(448);
			match(OPEN_BRACKET);
			setState(457);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 49)) & ~0x3f) == 0 && ((1L << (_la - 49)) & ((1L << (BOOLEAN - 49)) | (1L << (IDENTIFIER - 49)) | (1L << (STRING - 49)) | (1L << (OPEN_CURLY - 49)) | (1L << (OPEN_BRACKET - 49)))) != 0)) {
				{
				setState(449);
				expression();
				setState(454);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(450);
					match(COMMA);
					setState(451);
					expression();
					}
					}
					setState(456);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(459);
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
		public TerminalNode OPEN_BRACKET() { return getToken(PALParser.OPEN_BRACKET, 0); }
		public TerminalNode CLOSE_BRACKET() { return getToken(PALParser.CLOSE_BRACKET, 0); }
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
		enterRule(_localctx, 100, RULE_accessRightArray);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(461);
			match(OPEN_BRACKET);
			setState(470);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << IDENTIFIER))) != 0)) {
				{
				setState(462);
				accessRight();
				setState(467);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(463);
					match(COMMA);
					setState(464);
					accessRight();
					}
					}
					setState(469);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(472);
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

	public static class AccessRightContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(PALParser.IDENTIFIER, 0); }
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
		enterRule(_localctx, 102, RULE_accessRight);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(474);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << IDENTIFIER))) != 0)) ) {
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
		enterRule(_localctx, 104, RULE_map);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(476);
			match(OPEN_CURLY);
			setState(485);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 49)) & ~0x3f) == 0 && ((1L << (_la - 49)) & ((1L << (BOOLEAN - 49)) | (1L << (IDENTIFIER - 49)) | (1L << (STRING - 49)) | (1L << (OPEN_CURLY - 49)) | (1L << (OPEN_BRACKET - 49)))) != 0)) {
				{
				setState(477);
				mapEntry();
				setState(482);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(478);
					match(COMMA);
					setState(479);
					mapEntry();
					}
					}
					setState(484);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(487);
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
		enterRule(_localctx, 106, RULE_mapEntry);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(489);
			((MapEntryContext)_localctx).key = expression();
			setState(490);
			match(COLON);
			setState(491);
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
		public TerminalNode IDENTIFIER() { return getToken(PALParser.IDENTIFIER, 0); }
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
		enterRule(_localctx, 108, RULE_mapEntryRef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(493);
			match(IDENTIFIER);
			setState(498); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(494);
				match(OPEN_BRACKET);
				setState(495);
				((MapEntryRefContext)_localctx).key = expression();
				setState(496);
				match(CLOSE_BRACKET);
				}
				}
				setState(500); 
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
		enterRule(_localctx, 110, RULE_literal);
		try {
			setState(506);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STRING:
				_localctx = new StringLiteralContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(502);
				match(STRING);
				}
				break;
			case BOOLEAN:
				_localctx = new BooleanLiteralContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(503);
				match(BOOLEAN);
				}
				break;
			case OPEN_BRACKET:
				_localctx = new ArrayLiteralContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(504);
				array();
				}
				break;
			case OPEN_CURLY:
				_localctx = new MapLiteralContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(505);
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
		public TerminalNode IDENTIFIER() { return getToken(PALParser.IDENTIFIER, 0); }
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
		enterRule(_localctx, 112, RULE_varRef);
		try {
			setState(510);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,34,_ctx) ) {
			case 1:
				_localctx = new ReferenceByIDContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(508);
				match(IDENTIFIER);
				}
				break;
			case 2:
				_localctx = new MapEntryReferenceContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(509);
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
		public TerminalNode IDENTIFIER() { return getToken(PALParser.IDENTIFIER, 0); }
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
		enterRule(_localctx, 114, RULE_funcCall);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(512);
			match(IDENTIFIER);
			setState(513);
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
		enterRule(_localctx, 116, RULE_funcCallArgs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(515);
			match(OPEN_PAREN);
			setState(524);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 49)) & ~0x3f) == 0 && ((1L << (_la - 49)) & ((1L << (BOOLEAN - 49)) | (1L << (IDENTIFIER - 49)) | (1L << (STRING - 49)) | (1L << (OPEN_CURLY - 49)) | (1L << (OPEN_BRACKET - 49)))) != 0)) {
				{
				setState(516);
				expression();
				setState(521);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(517);
					match(COMMA);
					setState(518);
					expression();
					}
					}
					setState(523);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(526);
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3P\u0213\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\3\2\3"+
		"\2\3\2\3\3\7\3}\n\3\f\3\16\3\u0080\13\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3"+
		"\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4\u0093\n\4\3\5\5\5\u0096\n\5"+
		"\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\5\6\u00a3\n\6\3\6\3\6\3\7"+
		"\3\7\3\7\7\7\u00aa\n\7\f\7\16\7\u00ad\13\7\5\7\u00af\n\7\3\b\3\b\3\b\3"+
		"\t\3\t\3\n\3\n\3\n\5\n\u00b9\n\n\3\n\3\n\3\13\3\13\5\13\u00bf\n\13\3\f"+
		"\3\f\7\f\u00c3\n\f\f\f\16\f\u00c6\13\f\3\f\3\f\3\r\3\r\3\r\3\r\5\r\u00ce"+
		"\n\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\17\3\17\3\17\3\20\3\20\3\20\3\21"+
		"\3\21\3\21\3\21\7\21\u00e1\n\21\f\21\16\21\u00e4\13\21\3\21\5\21\u00e7"+
		"\n\21\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24"+
		"\5\24\u00f6\n\24\3\25\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\27"+
		"\3\27\7\27\u0104\n\27\f\27\16\27\u0107\13\27\3\27\3\27\3\30\3\30\3\30"+
		"\5\30\u010e\n\30\3\31\3\31\3\32\3\32\3\32\3\32\3\32\3\32\5\32\u0118\n"+
		"\32\3\33\3\33\3\33\3\33\3\34\3\34\3\34\3\34\3\34\3\34\3\35\3\35\3\35\3"+
		"\35\3\35\3\35\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\37\3\37\3\37\3\37\3"+
		"\37\3\37\3 \3 \3 \3 \3 \3 \3!\3!\3!\3!\3!\3!\3!\3!\3\"\3\"\3\"\3\"\3\""+
		"\3\"\3#\3#\3#\3#\3#\3$\3$\3$\3$\7$\u0154\n$\f$\16$\u0157\13$\3$\3$\3%"+
		"\3%\3%\3%\3%\3%\3%\3%\3%\5%\u0164\n%\3%\3%\3&\3&\3&\3&\3&\3&\3&\3&\3&"+
		"\5&\u0171\n&\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\5\'\u017d\n\'\3("+
		"\3(\3(\3)\3)\3)\3)\3)\3)\3*\3*\3*\3*\3+\7+\u018d\n+\f+\16+\u0190\13+\3"+
		",\3,\5,\u0194\n,\3-\3-\3-\3-\3-\3-\3-\3-\3.\3.\3.\3.\3.\3.\3.\3.\3.\3"+
		".\3.\3.\3.\3/\3/\3/\7/\u01ae\n/\f/\16/\u01b1\13/\5/\u01b3\n/\3\60\5\60"+
		"\u01b6\n\60\3\60\3\60\3\61\3\61\3\61\3\61\3\62\3\62\3\62\5\62\u01c1\n"+
		"\62\3\63\3\63\3\63\3\63\7\63\u01c7\n\63\f\63\16\63\u01ca\13\63\5\63\u01cc"+
		"\n\63\3\63\3\63\3\64\3\64\3\64\3\64\7\64\u01d4\n\64\f\64\16\64\u01d7\13"+
		"\64\5\64\u01d9\n\64\3\64\3\64\3\65\3\65\3\66\3\66\3\66\3\66\7\66\u01e3"+
		"\n\66\f\66\16\66\u01e6\13\66\5\66\u01e8\n\66\3\66\3\66\3\67\3\67\3\67"+
		"\3\67\38\38\38\38\38\68\u01f5\n8\r8\168\u01f6\39\39\39\39\59\u01fd\n9"+
		"\3:\3:\5:\u0201\n:\3;\3;\3;\3<\3<\3<\3<\7<\u020a\n<\f<\16<\u020d\13<\5"+
		"<\u020f\n<\3<\3<\3<\2\2=\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&("+
		"*,.\60\62\64\668:<>@BDFHJLNPRTVXZ\\^`bdfhjlnprtv\2\t\3\2/\60\3\2(,\3\2"+
		")*\3\2+,\5\2\24\24**,,\3\2\25\26\4\2\3\5??\2\u021a\2x\3\2\2\2\4~\3\2\2"+
		"\2\6\u0092\3\2\2\2\b\u0095\3\2\2\2\n\u009c\3\2\2\2\f\u00ae\3\2\2\2\16"+
		"\u00b0\3\2\2\2\20\u00b3\3\2\2\2\22\u00b8\3\2\2\2\24\u00be\3\2\2\2\26\u00c0"+
		"\3\2\2\2\30\u00c9\3\2\2\2\32\u00d3\3\2\2\2\34\u00d6\3\2\2\2\36\u00d9\3"+
		"\2\2\2 \u00dc\3\2\2\2\"\u00e8\3\2\2\2$\u00ed\3\2\2\2&\u00f5\3\2\2\2(\u00f7"+
		"\3\2\2\2*\u00fd\3\2\2\2,\u0101\3\2\2\2.\u010d\3\2\2\2\60\u010f\3\2\2\2"+
		"\62\u0111\3\2\2\2\64\u0119\3\2\2\2\66\u011d\3\2\2\28\u0123\3\2\2\2:\u0129"+
		"\3\2\2\2<\u0130\3\2\2\2>\u0136\3\2\2\2@\u013c\3\2\2\2B\u0144\3\2\2\2D"+
		"\u014a\3\2\2\2F\u014f\3\2\2\2H\u015a\3\2\2\2J\u0170\3\2\2\2L\u017c\3\2"+
		"\2\2N\u017e\3\2\2\2P\u0181\3\2\2\2R\u0187\3\2\2\2T\u018e\3\2\2\2V\u0193"+
		"\3\2\2\2X\u0195\3\2\2\2Z\u019d\3\2\2\2\\\u01b2\3\2\2\2^\u01b5\3\2\2\2"+
		"`\u01b9\3\2\2\2b\u01c0\3\2\2\2d\u01c2\3\2\2\2f\u01cf\3\2\2\2h\u01dc\3"+
		"\2\2\2j\u01de\3\2\2\2l\u01eb\3\2\2\2n\u01ef\3\2\2\2p\u01fc\3\2\2\2r\u0200"+
		"\3\2\2\2t\u0202\3\2\2\2v\u0205\3\2\2\2xy\5\4\3\2yz\7\2\2\3z\3\3\2\2\2"+
		"{}\5\6\4\2|{\3\2\2\2}\u0080\3\2\2\2~|\3\2\2\2~\177\3\2\2\2\177\5\3\2\2"+
		"\2\u0080~\3\2\2\2\u0081\u0093\5\b\5\2\u0082\u0093\5\n\6\2\u0083\u0093"+
		"\5\22\n\2\u0084\u0093\5\30\r\2\u0085\u0093\5\32\16\2\u0086\u0093\5\34"+
		"\17\2\u0087\u0093\5\36\20\2\u0088\u0093\5 \21\2\u0089\u0093\5\62\32\2"+
		"\u008a\u0093\5:\36\2\u008b\u0093\5<\37\2\u008c\u0093\5> \2\u008d\u0093"+
		"\5D#\2\u008e\u0093\5@!\2\u008f\u0093\5B\"\2\u0090\u0093\5`\61\2\u0091"+
		"\u0093\5X-\2\u0092\u0081\3\2\2\2\u0092\u0082\3\2\2\2\u0092\u0083\3\2\2"+
		"\2\u0092\u0084\3\2\2\2\u0092\u0085\3\2\2\2\u0092\u0086\3\2\2\2\u0092\u0087"+
		"\3\2\2\2\u0092\u0088\3\2\2\2\u0092\u0089\3\2\2\2\u0092\u008a\3\2\2\2\u0092"+
		"\u008b\3\2\2\2\u0092\u008c\3\2\2\2\u0092\u008d\3\2\2\2\u0092\u008e\3\2"+
		"\2\2\u0092\u008f\3\2\2\2\u0092\u0090\3\2\2\2\u0092\u0091\3\2\2\2\u0093"+
		"\7\3\2\2\2\u0094\u0096\t\2\2\2\u0095\u0094\3\2\2\2\u0095\u0096\3\2\2\2"+
		"\u0096\u0097\3\2\2\2\u0097\u0098\7?\2\2\u0098\u0099\7O\2\2\u0099\u009a"+
		"\5b\62\2\u009a\u009b\7E\2\2\u009b\t\3\2\2\2\u009c\u009d\7\61\2\2\u009d"+
		"\u009e\7?\2\2\u009e\u009f\7L\2\2\u009f\u00a0\5\f\7\2\u00a0\u00a2\7M\2"+
		"\2\u00a1\u00a3\5\24\13\2\u00a2\u00a1\3\2\2\2\u00a2\u00a3\3\2\2\2\u00a3"+
		"\u00a4\3\2\2\2\u00a4\u00a5\5\26\f\2\u00a5\13\3\2\2\2\u00a6\u00ab\5\16"+
		"\b\2\u00a7\u00a8\7C\2\2\u00a8\u00aa\5\16\b\2\u00a9\u00a7\3\2\2\2\u00aa"+
		"\u00ad\3\2\2\2\u00ab\u00a9\3\2\2\2\u00ab\u00ac\3\2\2\2\u00ac\u00af\3\2"+
		"\2\2\u00ad\u00ab\3\2\2\2\u00ae\u00a6\3\2\2\2\u00ae\u00af\3\2\2\2\u00af"+
		"\r\3\2\2\2\u00b0\u00b1\5\20\t\2\u00b1\u00b2\7?\2\2\u00b2\17\3\2\2\2\u00b3"+
		"\u00b4\5&\24\2\u00b4\21\3\2\2\2\u00b5\u00b6\7\62\2\2\u00b6\u00b9\5b\62"+
		"\2\u00b7\u00b9\7\62\2\2\u00b8\u00b5\3\2\2\2\u00b8\u00b7\3\2\2\2\u00b9"+
		"\u00ba\3\2\2\2\u00ba\u00bb\7E\2\2\u00bb\23\3\2\2\2\u00bc\u00bf\5&\24\2"+
		"\u00bd\u00bf\78\2\2\u00be\u00bc\3\2\2\2\u00be\u00bd\3\2\2\2\u00bf\25\3"+
		"\2\2\2\u00c0\u00c4\7F\2\2\u00c1\u00c3\5\6\4\2\u00c2\u00c1\3\2\2\2\u00c3"+
		"\u00c6\3\2\2\2\u00c4\u00c2\3\2\2\2\u00c4\u00c5\3\2\2\2\u00c5\u00c7\3\2"+
		"\2\2\u00c6\u00c4\3\2\2\2\u00c7\u00c8\7G\2\2\u00c8\27\3\2\2\2\u00c9\u00ca"+
		"\7;\2\2\u00ca\u00cd\7?\2\2\u00cb\u00cc\7C\2\2\u00cc\u00ce\7?\2\2\u00cd"+
		"\u00cb\3\2\2\2\u00cd\u00ce\3\2\2\2\u00ce\u00cf\3\2\2\2\u00cf\u00d0\7<"+
		"\2\2\u00d0\u00d1\5b\62\2\u00d1\u00d2\5,\27\2\u00d2\31\3\2\2\2\u00d3\u00d4"+
		"\7\t\2\2\u00d4\u00d5\7E\2\2\u00d5\33\3\2\2\2\u00d6\u00d7\7\n\2\2\u00d7"+
		"\u00d8\7E\2\2\u00d8\35\3\2\2\2\u00d9\u00da\5t;\2\u00da\u00db\7E\2\2\u00db"+
		"\37\3\2\2\2\u00dc\u00dd\7=\2\2\u00dd\u00de\5b\62\2\u00de\u00e2\5,\27\2"+
		"\u00df\u00e1\5\"\22\2\u00e0\u00df\3\2\2\2\u00e1\u00e4\3\2\2\2\u00e2\u00e0"+
		"\3\2\2\2\u00e2\u00e3\3\2\2\2\u00e3\u00e6\3\2\2\2\u00e4\u00e2\3\2\2\2\u00e5"+
		"\u00e7\5$\23\2\u00e6\u00e5\3\2\2\2\u00e6\u00e7\3\2\2\2\u00e7!\3\2\2\2"+
		"\u00e8\u00e9\7>\2\2\u00e9\u00ea\7=\2\2\u00ea\u00eb\5b\62\2\u00eb\u00ec"+
		"\5,\27\2\u00ec#\3\2\2\2\u00ed\u00ee\7>\2\2\u00ee\u00ef\5,\27\2\u00ef%"+
		"\3\2\2\2\u00f0\u00f6\7\66\2\2\u00f1\u00f6\7\67\2\2\u00f2\u00f6\5*\26\2"+
		"\u00f3\u00f6\5(\25\2\u00f4\u00f6\7.\2\2\u00f5\u00f0\3\2\2\2\u00f5\u00f1"+
		"\3\2\2\2\u00f5\u00f2\3\2\2\2\u00f5\u00f3\3\2\2\2\u00f5\u00f4\3\2\2\2\u00f6"+
		"\'\3\2\2\2\u00f7\u00f8\7:\2\2\u00f8\u00f9\7H\2\2\u00f9\u00fa\5&\24\2\u00fa"+
		"\u00fb\7I\2\2\u00fb\u00fc\5&\24\2\u00fc)\3\2\2\2\u00fd\u00fe\7H\2\2\u00fe"+
		"\u00ff\7I\2\2\u00ff\u0100\5&\24\2\u0100+\3\2\2\2\u0101\u0105\7F\2\2\u0102"+
		"\u0104\5\6\4\2\u0103\u0102\3\2\2\2\u0104\u0107\3\2\2\2\u0105\u0103\3\2"+
		"\2\2\u0105\u0106\3\2\2\2\u0106\u0108\3\2\2\2\u0107\u0105\3\2\2\2\u0108"+
		"\u0109\7G\2\2\u0109-\3\2\2\2\u010a\u010e\5\60\31\2\u010b\u010e\7&\2\2"+
		"\u010c\u010e\7%\2\2\u010d\u010a\3\2\2\2\u010d\u010b\3\2\2\2\u010d\u010c"+
		"\3\2\2\2\u010e/\3\2\2\2\u010f\u0110\t\3\2\2\u0110\61\3\2\2\2\u0111\u0117"+
		"\7\6\2\2\u0112\u0118\5\64\33\2\u0113\u0118\5\66\34\2\u0114\u0118\58\35"+
		"\2\u0115\u0118\5F$\2\u0116\u0118\5Z.\2\u0117\u0112\3\2\2\2\u0117\u0113"+
		"\3\2\2\2\u0117\u0114\3\2\2\2\u0117\u0115\3\2\2\2\u0117\u0116\3\2\2\2\u0118"+
		"\63\3\2\2\2\u0119\u011a\7(\2\2\u011a\u011b\5b\62\2\u011b\u011c\7E\2\2"+
		"\u011c\65\3\2\2\2\u011d\u011e\t\4\2\2\u011e\u011f\5b\62\2\u011f\u0120"+
		"\7\"\2\2\u0120\u0121\5b\62\2\u0121\u0122\7E\2\2\u0122\67\3\2\2\2\u0123"+
		"\u0124\t\5\2\2\u0124\u0125\5b\62\2\u0125\u0126\7\"\2\2\u0126\u0127\5b"+
		"\62\2\u0127\u0128\7E\2\2\u01289\3\2\2\2\u0129\u012a\7\33\2\2\u012a\u012b"+
		"\7\34\2\2\u012b\u012c\5b\62\2\u012c\u012d\7\35\2\2\u012d\u012e\5b\62\2"+
		"\u012e\u012f\7E\2\2\u012f;\3\2\2\2\u0130\u0131\7\30\2\2\u0131\u0132\5"+
		"b\62\2\u0132\u0133\7\35\2\2\u0133\u0134\5b\62\2\u0134\u0135\7E\2\2\u0135"+
		"=\3\2\2\2\u0136\u0137\7\31\2\2\u0137\u0138\5b\62\2\u0138\u0139\7\32\2"+
		"\2\u0139\u013a\5b\62\2\u013a\u013b\7E\2\2\u013b?\3\2\2\2\u013c\u013d\7"+
		"\36\2\2\u013d\u013e\5b\62\2\u013e\u013f\7\37\2\2\u013f\u0140\5b\62\2\u0140"+
		"\u0141\7 \2\2\u0141\u0142\5f\64\2\u0142\u0143\7E\2\2\u0143A\3\2\2\2\u0144"+
		"\u0145\7!\2\2\u0145\u0146\5b\62\2\u0146\u0147\7\37\2\2\u0147\u0148\5b"+
		"\62\2\u0148\u0149\7E\2\2\u0149C\3\2\2\2\u014a\u014b\7\b\2\2\u014b\u014c"+
		"\5.\30\2\u014c\u014d\5b\62\2\u014d\u014e\7E\2\2\u014eE\3\2\2\2\u014f\u0150"+
		"\7&\2\2\u0150\u0151\5b\62\2\u0151\u0155\7F\2\2\u0152\u0154\5H%\2\u0153"+
		"\u0152\3\2\2\2\u0154\u0157\3\2\2\2\u0155\u0153\3\2\2\2\u0155\u0156\3\2"+
		"\2\2\u0156\u0158\3\2\2\2\u0157\u0155\3\2\2\2\u0158\u0159\7G\2\2\u0159"+
		"G\3\2\2\2\u015a\u015b\7\6\2\2\u015b\u015c\7\f\2\2\u015c\u015d\5b\62\2"+
		"\u015d\u015e\7\r\2\2\u015e\u015f\5J&\2\u015f\u0160\7\16\2\2\u0160\u0163"+
		"\5b\62\2\u0161\u0162\7\17\2\2\u0162\u0164\5L\'\2\u0163\u0161\3\2\2\2\u0163"+
		"\u0164\3\2\2\2\u0164\u0165\3\2\2\2\u0165\u0166\5P)\2\u0166I\3\2\2\2\u0167"+
		"\u0171\7\21\2\2\u0168\u0169\7,\2\2\u0169\u0171\5b\62\2\u016a\u016b\7\22"+
		"\2\2\u016b\u0171\5b\62\2\u016c\u016d\7\23\2\2\u016d\u0171\5b\62\2\u016e"+
		"\u016f\7\24\2\2\u016f\u0171\5b\62\2\u0170\u0167\3\2\2\2\u0170\u0168\3"+
		"\2\2\2\u0170\u016a\3\2\2\2\u0170\u016c\3\2\2\2\u0170\u016e\3\2\2\2\u0171"+
		"K\3\2\2\2\u0172\u017d\5b\62\2\u0173\u017d\5N(\2\u0174\u0175\5N(\2\u0175"+
		"\u0176\7<\2\2\u0176\u0177\5b\62\2\u0177\u017d\3\2\2\2\u0178\u0179\5N("+
		"\2\u0179\u017a\7\34\2\2\u017a\u017b\5b\62\2\u017b\u017d\3\2\2\2\u017c"+
		"\u0172\3\2\2\2\u017c\u0173\3\2\2\2\u017c\u0174\3\2\2\2\u017c\u0178\3\2"+
		"\2\2\u017dM\3\2\2\2\u017e\u017f\7.\2\2\u017f\u0180\7\13\2\2\u0180O\3\2"+
		"\2\2\u0181\u0182\7\20\2\2\u0182\u0183\7L\2\2\u0183\u0184\7?\2\2\u0184"+
		"\u0185\7M\2\2\u0185\u0186\5R*\2\u0186Q\3\2\2\2\u0187\u0188\7F\2\2\u0188"+
		"\u0189\5T+\2\u0189\u018a\7G\2\2\u018aS\3\2\2\2\u018b\u018d\5V,\2\u018c"+
		"\u018b\3\2\2\2\u018d\u0190\3\2\2\2\u018e\u018c\3\2\2\2\u018e\u018f\3\2"+
		"\2\2\u018fU\3\2\2\2\u0190\u018e\3\2\2\2\u0191\u0194\5\6\4\2\u0192\u0194"+
		"\5H%\2\u0193\u0191\3\2\2\2\u0193\u0192\3\2\2\2\u0194W\3\2\2\2\u0195\u0196"+
		"\7\b\2\2\u0196\u0197\7\f\2\2\u0197\u0198\5b\62\2\u0198\u0199\7\32\2\2"+
		"\u0199\u019a\7&\2\2\u019a\u019b\5b\62\2\u019b\u019c\7E\2\2\u019cY\3\2"+
		"\2\2\u019d\u019e\7%\2\2\u019e\u019f\5b\62\2\u019f\u01a0\7$\2\2\u01a0\u01a1"+
		"\t\6\2\2\u01a1\u01a2\5b\62\2\u01a2\u01a3\7\'\2\2\u01a3\u01a4\5f\64\2\u01a4"+
		"\u01a5\7\17\2\2\u01a5\u01a6\t\7\2\2\u01a6\u01a7\7\34\2\2\u01a7\u01a8\5"+
		"\\/\2\u01a8\u01a9\7E\2\2\u01a9[\3\2\2\2\u01aa\u01af\5^\60\2\u01ab\u01ac"+
		"\7C\2\2\u01ac\u01ae\5^\60\2\u01ad\u01ab\3\2\2\2\u01ae\u01b1\3\2\2\2\u01af"+
		"\u01ad\3\2\2\2\u01af\u01b0\3\2\2\2\u01b0\u01b3\3\2\2\2\u01b1\u01af\3\2"+
		"\2\2\u01b2\u01aa\3\2\2\2\u01b2\u01b3\3\2\2\2\u01b3]\3\2\2\2\u01b4\u01b6"+
		"\7N\2\2\u01b5\u01b4\3\2\2\2\u01b5\u01b6\3\2\2\2\u01b6\u01b7\3\2\2\2\u01b7"+
		"\u01b8\5b\62\2\u01b8_\3\2\2\2\u01b9\u01ba\7\27\2\2\u01ba\u01bb\5f\64\2"+
		"\u01bb\u01bc\7E\2\2\u01bca\3\2\2\2\u01bd\u01c1\5r:\2\u01be\u01c1\5t;\2"+
		"\u01bf\u01c1\5p9\2\u01c0\u01bd\3\2\2\2\u01c0\u01be\3\2\2\2\u01c0\u01bf"+
		"\3\2\2\2\u01c1c\3\2\2\2\u01c2\u01cb\7H\2\2\u01c3\u01c8\5b\62\2\u01c4\u01c5"+
		"\7C\2\2\u01c5\u01c7\5b\62\2\u01c6\u01c4\3\2\2\2\u01c7\u01ca\3\2\2\2\u01c8"+
		"\u01c6\3\2\2\2\u01c8\u01c9\3\2\2\2\u01c9\u01cc\3\2\2\2\u01ca\u01c8\3\2"+
		"\2\2\u01cb\u01c3\3\2\2\2\u01cb\u01cc\3\2\2\2\u01cc\u01cd\3\2\2\2\u01cd"+
		"\u01ce\7I\2\2\u01cee\3\2\2\2\u01cf\u01d8\7H\2\2\u01d0\u01d5\5h\65\2\u01d1"+
		"\u01d2\7C\2\2\u01d2\u01d4\5h\65\2\u01d3\u01d1\3\2\2\2\u01d4\u01d7\3\2"+
		"\2\2\u01d5\u01d3\3\2\2\2\u01d5\u01d6\3\2\2\2\u01d6\u01d9\3\2\2\2\u01d7"+
		"\u01d5\3\2\2\2\u01d8\u01d0\3\2\2\2\u01d8\u01d9\3\2\2\2\u01d9\u01da\3\2"+
		"\2\2\u01da\u01db\7I\2\2\u01dbg\3\2\2\2\u01dc\u01dd\t\b\2\2\u01ddi\3\2"+
		"\2\2\u01de\u01e7\7F\2\2\u01df\u01e4\5l\67\2\u01e0\u01e1\7C\2\2\u01e1\u01e3"+
		"\5l\67\2\u01e2\u01e0\3\2\2\2\u01e3\u01e6\3\2\2\2\u01e4\u01e2\3\2\2\2\u01e4"+
		"\u01e5\3\2\2\2\u01e5\u01e8\3\2\2\2\u01e6\u01e4\3\2\2\2\u01e7\u01df\3\2"+
		"\2\2\u01e7\u01e8\3\2\2\2\u01e8\u01e9\3\2\2\2\u01e9\u01ea\7G\2\2\u01ea"+
		"k\3\2\2\2\u01eb\u01ec\5b\62\2\u01ec\u01ed\7D\2\2\u01ed\u01ee\5b\62\2\u01ee"+
		"m\3\2\2\2\u01ef\u01f4\7?\2\2\u01f0\u01f1\7H\2\2\u01f1\u01f2\5b\62\2\u01f2"+
		"\u01f3\7I\2\2\u01f3\u01f5\3\2\2\2\u01f4\u01f0\3\2\2\2\u01f5\u01f6\3\2"+
		"\2\2\u01f6\u01f4\3\2\2\2\u01f6\u01f7\3\2\2\2\u01f7o\3\2\2\2\u01f8\u01fd"+
		"\7@\2\2\u01f9\u01fd\7\63\2\2\u01fa\u01fd\5d\63\2\u01fb\u01fd\5j\66\2\u01fc"+
		"\u01f8\3\2\2\2\u01fc\u01f9\3\2\2\2\u01fc\u01fa\3\2\2\2\u01fc\u01fb\3\2"+
		"\2\2\u01fdq\3\2\2\2\u01fe\u0201\7?\2\2\u01ff\u0201\5n8\2\u0200\u01fe\3"+
		"\2\2\2\u0200\u01ff\3\2\2\2\u0201s\3\2\2\2\u0202\u0203\7?\2\2\u0203\u0204"+
		"\5v<\2\u0204u\3\2\2\2\u0205\u020e\7L\2\2\u0206\u020b\5b\62\2\u0207\u0208"+
		"\7C\2\2\u0208\u020a\5b\62\2\u0209\u0207\3\2\2\2\u020a\u020d\3\2\2\2\u020b"+
		"\u0209\3\2\2\2\u020b\u020c\3\2\2\2\u020c\u020f\3\2\2\2\u020d\u020b\3\2"+
		"\2\2\u020e\u0206\3\2\2\2\u020e\u020f\3\2\2\2\u020f\u0210\3\2\2\2\u0210"+
		"\u0211\7M\2\2\u0211w\3\2\2\2\'~\u0092\u0095\u00a2\u00ab\u00ae\u00b8\u00be"+
		"\u00c4\u00cd\u00e2\u00e6\u00f5\u0105\u010d\u0117\u0155\u0163\u0170\u017c"+
		"\u018e\u0193\u01af\u01b2\u01b5\u01c0\u01c8\u01cb\u01d5\u01d8\u01e4\u01e7"+
		"\u01f6\u01fc\u0200\u020b\u020e";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}