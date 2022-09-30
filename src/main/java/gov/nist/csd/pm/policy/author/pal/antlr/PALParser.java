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
		EQUALS=77;
	public static final int
		RULE_pal = 0, RULE_stmts = 1, RULE_stmt = 2, RULE_varStmt = 3, RULE_funcDefStmt = 4, 
		RULE_formalArgList = 5, RULE_formalArg = 6, RULE_formalArgType = 7, RULE_funcReturnStmt = 8, 
		RULE_funcReturnType = 9, RULE_funcBody = 10, RULE_foreachStmt = 11, RULE_breakStmt = 12, 
		RULE_continueStmt = 13, RULE_funcCallStmt = 14, RULE_ifStmt = 15, RULE_elseIfStmt = 16, 
		RULE_elseStmt = 17, RULE_varType = 18, RULE_mapType = 19, RULE_arrayType = 20, 
		RULE_stmtBlock = 21, RULE_deleteType = 22, RULE_nodeType = 23, RULE_createPolicyStmt = 24, 
		RULE_createAttrStmt = 25, RULE_createUserOrObjectStmt = 26, RULE_setNodePropsStmt = 27, 
		RULE_assignStmt = 28, RULE_deassignStmt = 29, RULE_associateStmt = 30, 
		RULE_dissociateStmt = 31, RULE_deleteStmt = 32, RULE_createObligationStmt = 33, 
		RULE_createRuleStmt = 34, RULE_subjectClause = 35, RULE_onClause = 36, 
		RULE_anyPe = 37, RULE_response = 38, RULE_responseBlock = 39, RULE_responseStmts = 40, 
		RULE_responseStmt = 41, RULE_deleteRuleStmt = 42, RULE_createProhibitionStmt = 43, 
		RULE_prohibitionContainerList = 44, RULE_prohibitionContainerExpression = 45, 
		RULE_setResourceAccessRightsStmt = 46, RULE_expression = 47, RULE_nameExpressionArray = 48, 
		RULE_nameExpression = 49, RULE_array = 50, RULE_accessRightArray = 51, 
		RULE_accessRight = 52, RULE_map = 53, RULE_mapEntry = 54, RULE_mapEntryRef = 55, 
		RULE_literal = 56, RULE_varRef = 57, RULE_funcCall = 58, RULE_funcCallArgs = 59;
	private static String[] makeRuleNames() {
		return new String[] {
			"pal", "stmts", "stmt", "varStmt", "funcDefStmt", "formalArgList", "formalArg", 
			"formalArgType", "funcReturnStmt", "funcReturnType", "funcBody", "foreachStmt", 
			"breakStmt", "continueStmt", "funcCallStmt", "ifStmt", "elseIfStmt", 
			"elseStmt", "varType", "mapType", "arrayType", "stmtBlock", "deleteType", 
			"nodeType", "createPolicyStmt", "createAttrStmt", "createUserOrObjectStmt", 
			"setNodePropsStmt", "assignStmt", "deassignStmt", "associateStmt", "dissociateStmt", 
			"deleteStmt", "createObligationStmt", "createRuleStmt", "subjectClause", 
			"onClause", "anyPe", "response", "responseBlock", "responseStmts", "responseStmt", 
			"deleteRuleStmt", "createProhibitionStmt", "prohibitionContainerList", 
			"prohibitionContainerExpression", "setResourceAccessRightsStmt", "expression", 
			"nameExpressionArray", "nameExpression", "array", "accessRightArray", 
			"accessRight", "map", "mapEntry", "mapEntryRef", "literal", "varRef", 
			"funcCall", "funcCallArgs"
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
			"'['", "']'", "'<'", "'>'", "'('", "')'", "'!'", "'='"
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
			setState(120);
			stmts();
			setState(121);
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
			setState(126);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << CREATE) | (1L << DELETE) | (1L << BREAK) | (1L << CONTINUE) | (1L << SET_RESOURCE_ACCESS_RIGHTS) | (1L << ASSIGN) | (1L << DEASSIGN) | (1L << SET_PROPERTIES) | (1L << ASSOCIATE) | (1L << DISSOCIATE) | (1L << LET) | (1L << CONST) | (1L << FUNCTION) | (1L << RETURN) | (1L << FOREACH) | (1L << IF) | (1L << IDENTIFIER))) != 0)) {
				{
				{
				setState(123);
				stmt();
				}
				}
				setState(128);
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
			setState(150);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				{
				setState(129);
				varStmt();
				}
				break;
			case 2:
				{
				setState(130);
				funcDefStmt();
				}
				break;
			case 3:
				{
				setState(131);
				funcReturnStmt();
				}
				break;
			case 4:
				{
				setState(132);
				foreachStmt();
				}
				break;
			case 5:
				{
				setState(133);
				breakStmt();
				}
				break;
			case 6:
				{
				setState(134);
				continueStmt();
				}
				break;
			case 7:
				{
				setState(135);
				funcCallStmt();
				}
				break;
			case 8:
				{
				setState(136);
				ifStmt();
				}
				break;
			case 9:
				{
				setState(137);
				createPolicyStmt();
				}
				break;
			case 10:
				{
				setState(138);
				createAttrStmt();
				}
				break;
			case 11:
				{
				setState(139);
				createUserOrObjectStmt();
				}
				break;
			case 12:
				{
				setState(140);
				createObligationStmt();
				}
				break;
			case 13:
				{
				setState(141);
				createProhibitionStmt();
				}
				break;
			case 14:
				{
				setState(142);
				setNodePropsStmt();
				}
				break;
			case 15:
				{
				setState(143);
				assignStmt();
				}
				break;
			case 16:
				{
				setState(144);
				deassignStmt();
				}
				break;
			case 17:
				{
				setState(145);
				deleteStmt();
				}
				break;
			case 18:
				{
				setState(146);
				associateStmt();
				}
				break;
			case 19:
				{
				setState(147);
				dissociateStmt();
				}
				break;
			case 20:
				{
				setState(148);
				setResourceAccessRightsStmt();
				}
				break;
			case 21:
				{
				setState(149);
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
			setState(153);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LET || _la==CONST) {
				{
				setState(152);
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

			setState(155);
			match(IDENTIFIER);
			setState(156);
			match(EQUALS);
			setState(157);
			expression();
			setState(158);
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
			setState(160);
			match(FUNCTION);
			setState(161);
			match(IDENTIFIER);
			setState(162);
			match(OPEN_PAREN);
			setState(163);
			formalArgList();
			setState(164);
			match(CLOSE_PAREN);
			setState(166);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 44)) & ~0x3f) == 0 && ((1L << (_la - 44)) & ((1L << (ANY - 44)) | (1L << (STRING_TYPE - 44)) | (1L << (BOOLEAN_TYPE - 44)) | (1L << (VOID_TYPE - 44)) | (1L << (MAP_TYPE - 44)) | (1L << (OPEN_BRACKET - 44)))) != 0)) {
				{
				setState(165);
				funcReturnType();
				}
			}

			setState(168);
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
			setState(178);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 44)) & ~0x3f) == 0 && ((1L << (_la - 44)) & ((1L << (ANY - 44)) | (1L << (STRING_TYPE - 44)) | (1L << (BOOLEAN_TYPE - 44)) | (1L << (MAP_TYPE - 44)) | (1L << (OPEN_BRACKET - 44)))) != 0)) {
				{
				setState(170);
				formalArg();
				setState(175);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(171);
					match(COMMA);
					setState(172);
					formalArg();
					}
					}
					setState(177);
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
			setState(180);
			formalArgType();
			setState(181);
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
			setState(183);
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
			setState(188);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				{
				setState(185);
				match(RETURN);
				setState(186);
				expression();
				}
				break;
			case 2:
				{
				setState(187);
				match(RETURN);
				}
				break;
			}
			setState(190);
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
			setState(194);
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
				setState(192);
				varType();
				}
				break;
			case VOID_TYPE:
				_localctx = new VoidReturnTypeContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(193);
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
			setState(196);
			match(OPEN_CURLY);
			setState(200);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << CREATE) | (1L << DELETE) | (1L << BREAK) | (1L << CONTINUE) | (1L << SET_RESOURCE_ACCESS_RIGHTS) | (1L << ASSIGN) | (1L << DEASSIGN) | (1L << SET_PROPERTIES) | (1L << ASSOCIATE) | (1L << DISSOCIATE) | (1L << LET) | (1L << CONST) | (1L << FUNCTION) | (1L << RETURN) | (1L << FOREACH) | (1L << IF) | (1L << IDENTIFIER))) != 0)) {
				{
				{
				setState(197);
				stmt();
				}
				}
				setState(202);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(203);
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
			setState(205);
			match(FOREACH);
			setState(206);
			((ForeachStmtContext)_localctx).key = match(IDENTIFIER);
			setState(209);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(207);
				match(COMMA);
				setState(208);
				((ForeachStmtContext)_localctx).mapValue = match(IDENTIFIER);
				}
			}

			setState(211);
			match(IN);
			setState(212);
			expression();
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
			setState(225);
			((IfStmtContext)_localctx).condition = expression();
			setState(226);
			stmtBlock();
			setState(230);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(227);
					elseIfStmt();
					}
					} 
				}
				setState(232);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			}
			setState(234);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ELSE) {
				{
				setState(233);
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
			setState(236);
			match(ELSE);
			setState(237);
			match(IF);
			setState(238);
			((ElseIfStmtContext)_localctx).condition = expression();
			setState(239);
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
			setState(241);
			match(ELSE);
			setState(242);
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
			setState(249);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STRING_TYPE:
				_localctx = new StringTypeContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(244);
				match(STRING_TYPE);
				}
				break;
			case BOOLEAN_TYPE:
				_localctx = new BooleanTypeContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(245);
				match(BOOLEAN_TYPE);
				}
				break;
			case OPEN_BRACKET:
				_localctx = new ArrayVarTypeContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(246);
				arrayType();
				}
				break;
			case MAP_TYPE:
				_localctx = new MapVarTypeContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(247);
				mapType();
				}
				break;
			case ANY:
				_localctx = new AnyTypeContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(248);
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
			setState(251);
			match(MAP_TYPE);
			setState(252);
			match(OPEN_BRACKET);
			setState(253);
			((MapTypeContext)_localctx).keyType = varType();
			setState(254);
			match(CLOSE_BRACKET);
			setState(255);
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
			setState(257);
			match(OPEN_BRACKET);
			setState(258);
			match(CLOSE_BRACKET);
			setState(259);
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
			setState(261);
			match(OPEN_CURLY);
			setState(265);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << CREATE) | (1L << DELETE) | (1L << BREAK) | (1L << CONTINUE) | (1L << SET_RESOURCE_ACCESS_RIGHTS) | (1L << ASSIGN) | (1L << DEASSIGN) | (1L << SET_PROPERTIES) | (1L << ASSOCIATE) | (1L << DISSOCIATE) | (1L << LET) | (1L << CONST) | (1L << FUNCTION) | (1L << RETURN) | (1L << FOREACH) | (1L << IF) | (1L << IDENTIFIER))) != 0)) {
				{
				{
				setState(262);
				stmt();
				}
				}
				setState(267);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(268);
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
			setState(273);
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
				setState(270);
				nodeType();
				}
				break;
			case OBLIGATION:
				_localctx = new DeleteObligationContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(271);
				match(OBLIGATION);
				}
				break;
			case PROHIBITION:
				_localctx = new DeleteProhibitionContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(272);
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
			setState(275);
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
		enterRule(_localctx, 48, RULE_createPolicyStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(277);
			match(CREATE);
			setState(278);
			match(POLICY_CLASS);
			setState(279);
			nameExpression();
			setState(280);
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
		public TerminalNode ASSIGN_TO() { return getToken(PALParser.ASSIGN_TO, 0); }
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
		enterRule(_localctx, 50, RULE_createAttrStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(282);
			match(CREATE);
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
			nameExpression();
			setState(285);
			match(ASSIGN_TO);
			setState(286);
			nameExpressionArray();
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
		public TerminalNode CREATE() { return getToken(PALParser.CREATE, 0); }
		public NameExpressionContext nameExpression() {
			return getRuleContext(NameExpressionContext.class,0);
		}
		public TerminalNode ASSIGN_TO() { return getToken(PALParser.ASSIGN_TO, 0); }
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
		enterRule(_localctx, 52, RULE_createUserOrObjectStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(289);
			match(CREATE);
			setState(290);
			_la = _input.LA(1);
			if ( !(_la==OBJECT || _la==USER) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(291);
			nameExpression();
			setState(292);
			match(ASSIGN_TO);
			setState(293);
			nameExpressionArray();
			setState(294);
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
		enterRule(_localctx, 54, RULE_setNodePropsStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(296);
			match(SET_PROPERTIES);
			setState(297);
			match(OF);
			setState(298);
			nameExpression();
			setState(299);
			match(TO);
			setState(300);
			((SetNodePropsStmtContext)_localctx).properties = expression();
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
		enterRule(_localctx, 56, RULE_assignStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(303);
			match(ASSIGN);
			setState(304);
			((AssignStmtContext)_localctx).child = nameExpression();
			setState(305);
			match(TO);
			setState(306);
			((AssignStmtContext)_localctx).parent = nameExpression();
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
		enterRule(_localctx, 58, RULE_deassignStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(309);
			match(DEASSIGN);
			setState(310);
			((DeassignStmtContext)_localctx).child = nameExpression();
			setState(311);
			match(FROM);
			setState(312);
			((DeassignStmtContext)_localctx).parent = nameExpression();
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

	public static class AssociateStmtContext extends ParserRuleContext {
		public NameExpressionContext ua;
		public NameExpressionContext target;
		public AccessRightArrayContext accessRights;
		public TerminalNode ASSOCIATE() { return getToken(PALParser.ASSOCIATE, 0); }
		public TerminalNode AND() { return getToken(PALParser.AND, 0); }
		public TerminalNode WITH_ACCESS_RIGHTS() { return getToken(PALParser.WITH_ACCESS_RIGHTS, 0); }
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
		enterRule(_localctx, 60, RULE_associateStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(315);
			match(ASSOCIATE);
			setState(316);
			((AssociateStmtContext)_localctx).ua = nameExpression();
			setState(317);
			match(AND);
			setState(318);
			((AssociateStmtContext)_localctx).target = nameExpression();
			setState(319);
			match(WITH_ACCESS_RIGHTS);
			setState(320);
			((AssociateStmtContext)_localctx).accessRights = accessRightArray();
			setState(321);
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
		enterRule(_localctx, 62, RULE_dissociateStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(323);
			match(DISSOCIATE);
			setState(324);
			((DissociateStmtContext)_localctx).ua = nameExpression();
			setState(325);
			match(AND);
			setState(326);
			((DissociateStmtContext)_localctx).target = nameExpression();
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
		enterRule(_localctx, 64, RULE_deleteStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(329);
			match(DELETE);
			setState(330);
			deleteType();
			setState(331);
			nameExpression();
			setState(332);
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
		enterRule(_localctx, 66, RULE_createObligationStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(334);
			match(CREATE);
			setState(335);
			match(OBLIGATION);
			setState(336);
			nameExpression();
			setState(337);
			match(OPEN_CURLY);
			setState(341);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==CREATE) {
				{
				{
				setState(338);
				createRuleStmt();
				}
				}
				setState(343);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(344);
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
		public NameExpressionContext nameExpression() {
			return getRuleContext(NameExpressionContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
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
			setState(346);
			match(CREATE);
			setState(347);
			match(RULE);
			setState(348);
			((CreateRuleStmtContext)_localctx).ruleName = nameExpression();
			setState(349);
			match(WHEN);
			setState(350);
			subjectClause();
			setState(351);
			match(PERFORMS);
			setState(352);
			((CreateRuleStmtContext)_localctx).performsClause = expression();
			setState(355);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ON) {
				{
				setState(353);
				match(ON);
				setState(354);
				onClause();
				}
			}

			setState(357);
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
		enterRule(_localctx, 70, RULE_subjectClause);
		try {
			setState(368);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ANY_USER:
				_localctx = new AnyUserSubjectContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(359);
				match(ANY_USER);
				}
				break;
			case USER:
				_localctx = new UserSubjectContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(360);
				match(USER);
				setState(361);
				((UserSubjectContext)_localctx).user = nameExpression();
				}
				break;
			case USERS:
				_localctx = new UsersListSubjectContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(362);
				match(USERS);
				setState(363);
				((UsersListSubjectContext)_localctx).users = nameExpressionArray();
				}
				break;
			case ANY_USER_WITH_ATTRIBUTE:
				_localctx = new UserAttrSubjectContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(364);
				match(ANY_USER_WITH_ATTRIBUTE);
				setState(365);
				((UserAttrSubjectContext)_localctx).attribute = nameExpression();
				}
				break;
			case PROCESS:
				_localctx = new ProcessSubjectContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(366);
				match(PROCESS);
				setState(367);
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
		enterRule(_localctx, 72, RULE_onClause);
		try {
			setState(380);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
			case 1:
				_localctx = new PolicyElementContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(370);
				nameExpression();
				}
				break;
			case 2:
				_localctx = new AnyPolicyElementContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(371);
				anyPe();
				}
				break;
			case 3:
				_localctx = new AnyContainedInContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(372);
				anyPe();
				setState(373);
				match(IN);
				setState(374);
				nameExpression();
				}
				break;
			case 4:
				_localctx = new AnyOfSetContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(376);
				anyPe();
				setState(377);
				match(OF);
				setState(378);
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
		enterRule(_localctx, 74, RULE_anyPe);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(382);
			match(ANY);
			setState(383);
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
		enterRule(_localctx, 76, RULE_response);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(385);
			match(DO);
			setState(386);
			match(OPEN_PAREN);
			setState(387);
			match(IDENTIFIER);
			setState(388);
			match(CLOSE_PAREN);
			setState(389);
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
		enterRule(_localctx, 78, RULE_responseBlock);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(391);
			match(OPEN_CURLY);
			setState(392);
			responseStmts();
			setState(393);
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
		enterRule(_localctx, 80, RULE_responseStmts);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(398);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << CREATE) | (1L << DELETE) | (1L << BREAK) | (1L << CONTINUE) | (1L << SET_RESOURCE_ACCESS_RIGHTS) | (1L << ASSIGN) | (1L << DEASSIGN) | (1L << SET_PROPERTIES) | (1L << ASSOCIATE) | (1L << DISSOCIATE) | (1L << LET) | (1L << CONST) | (1L << FUNCTION) | (1L << RETURN) | (1L << FOREACH) | (1L << IF) | (1L << IDENTIFIER))) != 0)) {
				{
				{
				setState(395);
				responseStmt();
				}
				}
				setState(400);
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
		enterRule(_localctx, 82, RULE_responseStmt);
		try {
			setState(403);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(401);
				stmt();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(402);
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
		enterRule(_localctx, 84, RULE_deleteRuleStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(405);
			match(DELETE);
			setState(406);
			match(RULE);
			setState(407);
			((DeleteRuleStmtContext)_localctx).ruleName = nameExpression();
			setState(408);
			match(FROM);
			setState(409);
			match(OBLIGATION);
			setState(410);
			((DeleteRuleStmtContext)_localctx).obligationName = nameExpression();
			setState(411);
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
		enterRule(_localctx, 86, RULE_createProhibitionStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(413);
			match(CREATE);
			setState(414);
			match(PROHIBITION);
			setState(415);
			((CreateProhibitionStmtContext)_localctx).name = nameExpression();
			setState(416);
			match(DENY);
			setState(417);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PROCESS) | (1L << USER_ATTRIBUTE) | (1L << USER))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(418);
			((CreateProhibitionStmtContext)_localctx).subject = nameExpression();
			setState(419);
			match(ACCESS_RIGHTS);
			setState(420);
			((CreateProhibitionStmtContext)_localctx).accessRights = accessRightArray();
			setState(421);
			match(ON);
			setState(422);
			_la = _input.LA(1);
			if ( !(_la==INTERSECTION || _la==UNION) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(423);
			match(OF);
			setState(424);
			((CreateProhibitionStmtContext)_localctx).containers = prohibitionContainerList();
			setState(425);
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
		enterRule(_localctx, 88, RULE_prohibitionContainerList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(435);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IDENTIFIER || _la==IS_COMPLEMENT) {
				{
				setState(427);
				prohibitionContainerExpression();
				setState(432);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(428);
					match(COMMA);
					setState(429);
					prohibitionContainerExpression();
					}
					}
					setState(434);
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
		enterRule(_localctx, 90, RULE_prohibitionContainerExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(438);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IS_COMPLEMENT) {
				{
				setState(437);
				match(IS_COMPLEMENT);
				}
			}

			setState(440);
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
		enterRule(_localctx, 92, RULE_setResourceAccessRightsStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(442);
			match(SET_RESOURCE_ACCESS_RIGHTS);
			setState(443);
			accessRightArray();
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
		enterRule(_localctx, 94, RULE_expression);
		try {
			setState(449);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,24,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(446);
				varRef();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(447);
				funcCall();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(448);
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
		enterRule(_localctx, 96, RULE_nameExpressionArray);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(451);
			nameExpression();
			setState(456);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(452);
				match(COMMA);
				setState(453);
				nameExpression();
				}
				}
				setState(458);
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
		enterRule(_localctx, 98, RULE_nameExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(461);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
			case 1:
				{
				setState(459);
				varRef();
				}
				break;
			case 2:
				{
				setState(460);
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
		enterRule(_localctx, 100, RULE_array);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(463);
			match(OPEN_BRACKET);
			setState(472);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 49)) & ~0x3f) == 0 && ((1L << (_la - 49)) & ((1L << (BOOLEAN - 49)) | (1L << (IDENTIFIER - 49)) | (1L << (STRING - 49)) | (1L << (OPEN_CURLY - 49)) | (1L << (OPEN_BRACKET - 49)))) != 0)) {
				{
				setState(464);
				expression();
				setState(469);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(465);
					match(COMMA);
					setState(466);
					expression();
					}
					}
					setState(471);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(474);
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
		enterRule(_localctx, 102, RULE_accessRightArray);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(484);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << IDENTIFIER))) != 0)) {
				{
				setState(476);
				accessRight();
				setState(481);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(477);
					match(COMMA);
					setState(478);
					accessRight();
					}
					}
					setState(483);
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
		enterRule(_localctx, 104, RULE_accessRight);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(486);
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
		enterRule(_localctx, 106, RULE_map);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(488);
			match(OPEN_CURLY);
			setState(497);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 49)) & ~0x3f) == 0 && ((1L << (_la - 49)) & ((1L << (BOOLEAN - 49)) | (1L << (IDENTIFIER - 49)) | (1L << (STRING - 49)) | (1L << (OPEN_CURLY - 49)) | (1L << (OPEN_BRACKET - 49)))) != 0)) {
				{
				setState(489);
				mapEntry();
				setState(494);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(490);
					match(COMMA);
					setState(491);
					mapEntry();
					}
					}
					setState(496);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(499);
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
		enterRule(_localctx, 108, RULE_mapEntry);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(501);
			((MapEntryContext)_localctx).key = expression();
			setState(502);
			match(COLON);
			setState(503);
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
		enterRule(_localctx, 110, RULE_mapEntryRef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(505);
			match(IDENTIFIER);
			setState(510); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(506);
				match(OPEN_BRACKET);
				setState(507);
				((MapEntryRefContext)_localctx).key = expression();
				setState(508);
				match(CLOSE_BRACKET);
				}
				}
				setState(512); 
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
		enterRule(_localctx, 112, RULE_literal);
		try {
			setState(518);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STRING:
				_localctx = new StringLiteralContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(514);
				match(STRING);
				}
				break;
			case BOOLEAN:
				_localctx = new BooleanLiteralContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(515);
				match(BOOLEAN);
				}
				break;
			case OPEN_BRACKET:
				_localctx = new ArrayLiteralContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(516);
				array();
				}
				break;
			case OPEN_CURLY:
				_localctx = new MapLiteralContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(517);
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
		enterRule(_localctx, 114, RULE_varRef);
		try {
			setState(522);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
			case 1:
				_localctx = new ReferenceByIDContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(520);
				match(IDENTIFIER);
				}
				break;
			case 2:
				_localctx = new MapEntryReferenceContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(521);
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
		enterRule(_localctx, 116, RULE_funcCall);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(524);
			match(IDENTIFIER);
			setState(525);
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
		enterRule(_localctx, 118, RULE_funcCallArgs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(527);
			match(OPEN_PAREN);
			setState(536);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 49)) & ~0x3f) == 0 && ((1L << (_la - 49)) & ((1L << (BOOLEAN - 49)) | (1L << (IDENTIFIER - 49)) | (1L << (STRING - 49)) | (1L << (OPEN_CURLY - 49)) | (1L << (OPEN_BRACKET - 49)))) != 0)) {
				{
				setState(528);
				expression();
				setState(533);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(529);
					match(COMMA);
					setState(530);
					expression();
					}
					}
					setState(535);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(538);
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3O\u021f\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\3\2\3\2\3\2\3\3\7\3\177\n\3\f\3\16\3\u0082\13\3\3\4\3\4\3\4\3\4\3\4\3"+
		"\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4\u0099"+
		"\n\4\3\5\5\5\u009c\n\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\5\6"+
		"\u00a9\n\6\3\6\3\6\3\7\3\7\3\7\7\7\u00b0\n\7\f\7\16\7\u00b3\13\7\5\7\u00b5"+
		"\n\7\3\b\3\b\3\b\3\t\3\t\3\n\3\n\3\n\5\n\u00bf\n\n\3\n\3\n\3\13\3\13\5"+
		"\13\u00c5\n\13\3\f\3\f\7\f\u00c9\n\f\f\f\16\f\u00cc\13\f\3\f\3\f\3\r\3"+
		"\r\3\r\3\r\5\r\u00d4\n\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\17\3\17\3\17"+
		"\3\20\3\20\3\20\3\21\3\21\3\21\3\21\7\21\u00e7\n\21\f\21\16\21\u00ea\13"+
		"\21\3\21\5\21\u00ed\n\21\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\24"+
		"\3\24\3\24\3\24\3\24\5\24\u00fc\n\24\3\25\3\25\3\25\3\25\3\25\3\25\3\26"+
		"\3\26\3\26\3\26\3\27\3\27\7\27\u010a\n\27\f\27\16\27\u010d\13\27\3\27"+
		"\3\27\3\30\3\30\3\30\5\30\u0114\n\30\3\31\3\31\3\32\3\32\3\32\3\32\3\32"+
		"\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\34\3\34\3\34\3\34\3\34\3\34\3\34"+
		"\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\36\3\36\3\36\3\36\3\36\3\36\3\37"+
		"\3\37\3\37\3\37\3\37\3\37\3 \3 \3 \3 \3 \3 \3 \3 \3!\3!\3!\3!\3!\3!\3"+
		"\"\3\"\3\"\3\"\3\"\3#\3#\3#\3#\3#\7#\u0156\n#\f#\16#\u0159\13#\3#\3#\3"+
		"$\3$\3$\3$\3$\3$\3$\3$\3$\5$\u0166\n$\3$\3$\3%\3%\3%\3%\3%\3%\3%\3%\3"+
		"%\5%\u0173\n%\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\5&\u017f\n&\3\'\3\'\3\'\3"+
		"(\3(\3(\3(\3(\3(\3)\3)\3)\3)\3*\7*\u018f\n*\f*\16*\u0192\13*\3+\3+\5+"+
		"\u0196\n+\3,\3,\3,\3,\3,\3,\3,\3,\3-\3-\3-\3-\3-\3-\3-\3-\3-\3-\3-\3-"+
		"\3-\3-\3.\3.\3.\7.\u01b1\n.\f.\16.\u01b4\13.\5.\u01b6\n.\3/\5/\u01b9\n"+
		"/\3/\3/\3\60\3\60\3\60\3\60\3\61\3\61\3\61\5\61\u01c4\n\61\3\62\3\62\3"+
		"\62\7\62\u01c9\n\62\f\62\16\62\u01cc\13\62\3\63\3\63\5\63\u01d0\n\63\3"+
		"\64\3\64\3\64\3\64\7\64\u01d6\n\64\f\64\16\64\u01d9\13\64\5\64\u01db\n"+
		"\64\3\64\3\64\3\65\3\65\3\65\7\65\u01e2\n\65\f\65\16\65\u01e5\13\65\5"+
		"\65\u01e7\n\65\3\66\3\66\3\67\3\67\3\67\3\67\7\67\u01ef\n\67\f\67\16\67"+
		"\u01f2\13\67\5\67\u01f4\n\67\3\67\3\67\38\38\38\38\39\39\39\39\39\69\u0201"+
		"\n9\r9\169\u0202\3:\3:\3:\3:\5:\u0209\n:\3;\3;\5;\u020d\n;\3<\3<\3<\3"+
		"=\3=\3=\3=\7=\u0216\n=\f=\16=\u0219\13=\5=\u021b\n=\3=\3=\3=\2\2>\2\4"+
		"\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNP"+
		"RTVXZ\\^`bdfhjlnprtvx\2\t\3\2/\60\3\2(,\3\2)*\3\2+,\5\2\24\24**,,\3\2"+
		"\25\26\4\2\3\5??\2\u0227\2z\3\2\2\2\4\u0080\3\2\2\2\6\u0098\3\2\2\2\b"+
		"\u009b\3\2\2\2\n\u00a2\3\2\2\2\f\u00b4\3\2\2\2\16\u00b6\3\2\2\2\20\u00b9"+
		"\3\2\2\2\22\u00be\3\2\2\2\24\u00c4\3\2\2\2\26\u00c6\3\2\2\2\30\u00cf\3"+
		"\2\2\2\32\u00d9\3\2\2\2\34\u00dc\3\2\2\2\36\u00df\3\2\2\2 \u00e2\3\2\2"+
		"\2\"\u00ee\3\2\2\2$\u00f3\3\2\2\2&\u00fb\3\2\2\2(\u00fd\3\2\2\2*\u0103"+
		"\3\2\2\2,\u0107\3\2\2\2.\u0113\3\2\2\2\60\u0115\3\2\2\2\62\u0117\3\2\2"+
		"\2\64\u011c\3\2\2\2\66\u0123\3\2\2\28\u012a\3\2\2\2:\u0131\3\2\2\2<\u0137"+
		"\3\2\2\2>\u013d\3\2\2\2@\u0145\3\2\2\2B\u014b\3\2\2\2D\u0150\3\2\2\2F"+
		"\u015c\3\2\2\2H\u0172\3\2\2\2J\u017e\3\2\2\2L\u0180\3\2\2\2N\u0183\3\2"+
		"\2\2P\u0189\3\2\2\2R\u0190\3\2\2\2T\u0195\3\2\2\2V\u0197\3\2\2\2X\u019f"+
		"\3\2\2\2Z\u01b5\3\2\2\2\\\u01b8\3\2\2\2^\u01bc\3\2\2\2`\u01c3\3\2\2\2"+
		"b\u01c5\3\2\2\2d\u01cf\3\2\2\2f\u01d1\3\2\2\2h\u01e6\3\2\2\2j\u01e8\3"+
		"\2\2\2l\u01ea\3\2\2\2n\u01f7\3\2\2\2p\u01fb\3\2\2\2r\u0208\3\2\2\2t\u020c"+
		"\3\2\2\2v\u020e\3\2\2\2x\u0211\3\2\2\2z{\5\4\3\2{|\7\2\2\3|\3\3\2\2\2"+
		"}\177\5\6\4\2~}\3\2\2\2\177\u0082\3\2\2\2\u0080~\3\2\2\2\u0080\u0081\3"+
		"\2\2\2\u0081\5\3\2\2\2\u0082\u0080\3\2\2\2\u0083\u0099\5\b\5\2\u0084\u0099"+
		"\5\n\6\2\u0085\u0099\5\22\n\2\u0086\u0099\5\30\r\2\u0087\u0099\5\32\16"+
		"\2\u0088\u0099\5\34\17\2\u0089\u0099\5\36\20\2\u008a\u0099\5 \21\2\u008b"+
		"\u0099\5\62\32\2\u008c\u0099\5\64\33\2\u008d\u0099\5\66\34\2\u008e\u0099"+
		"\5D#\2\u008f\u0099\5X-\2\u0090\u0099\58\35\2\u0091\u0099\5:\36\2\u0092"+
		"\u0099\5<\37\2\u0093\u0099\5B\"\2\u0094\u0099\5> \2\u0095\u0099\5@!\2"+
		"\u0096\u0099\5^\60\2\u0097\u0099\5V,\2\u0098\u0083\3\2\2\2\u0098\u0084"+
		"\3\2\2\2\u0098\u0085\3\2\2\2\u0098\u0086\3\2\2\2\u0098\u0087\3\2\2\2\u0098"+
		"\u0088\3\2\2\2\u0098\u0089\3\2\2\2\u0098\u008a\3\2\2\2\u0098\u008b\3\2"+
		"\2\2\u0098\u008c\3\2\2\2\u0098\u008d\3\2\2\2\u0098\u008e\3\2\2\2\u0098"+
		"\u008f\3\2\2\2\u0098\u0090\3\2\2\2\u0098\u0091\3\2\2\2\u0098\u0092\3\2"+
		"\2\2\u0098\u0093\3\2\2\2\u0098\u0094\3\2\2\2\u0098\u0095\3\2\2\2\u0098"+
		"\u0096\3\2\2\2\u0098\u0097\3\2\2\2\u0099\7\3\2\2\2\u009a\u009c\t\2\2\2"+
		"\u009b\u009a\3\2\2\2\u009b\u009c\3\2\2\2\u009c\u009d\3\2\2\2\u009d\u009e"+
		"\7?\2\2\u009e\u009f\7O\2\2\u009f\u00a0\5`\61\2\u00a0\u00a1\7E\2\2\u00a1"+
		"\t\3\2\2\2\u00a2\u00a3\7\61\2\2\u00a3\u00a4\7?\2\2\u00a4\u00a5\7L\2\2"+
		"\u00a5\u00a6\5\f\7\2\u00a6\u00a8\7M\2\2\u00a7\u00a9\5\24\13\2\u00a8\u00a7"+
		"\3\2\2\2\u00a8\u00a9\3\2\2\2\u00a9\u00aa\3\2\2\2\u00aa\u00ab\5\26\f\2"+
		"\u00ab\13\3\2\2\2\u00ac\u00b1\5\16\b\2\u00ad\u00ae\7C\2\2\u00ae\u00b0"+
		"\5\16\b\2\u00af\u00ad\3\2\2\2\u00b0\u00b3\3\2\2\2\u00b1\u00af\3\2\2\2"+
		"\u00b1\u00b2\3\2\2\2\u00b2\u00b5\3\2\2\2\u00b3\u00b1\3\2\2\2\u00b4\u00ac"+
		"\3\2\2\2\u00b4\u00b5\3\2\2\2\u00b5\r\3\2\2\2\u00b6\u00b7\5\20\t\2\u00b7"+
		"\u00b8\7?\2\2\u00b8\17\3\2\2\2\u00b9\u00ba\5&\24\2\u00ba\21\3\2\2\2\u00bb"+
		"\u00bc\7\62\2\2\u00bc\u00bf\5`\61\2\u00bd\u00bf\7\62\2\2\u00be\u00bb\3"+
		"\2\2\2\u00be\u00bd\3\2\2\2\u00bf\u00c0\3\2\2\2\u00c0\u00c1\7E\2\2\u00c1"+
		"\23\3\2\2\2\u00c2\u00c5\5&\24\2\u00c3\u00c5\78\2\2\u00c4\u00c2\3\2\2\2"+
		"\u00c4\u00c3\3\2\2\2\u00c5\25\3\2\2\2\u00c6\u00ca\7F\2\2\u00c7\u00c9\5"+
		"\6\4\2\u00c8\u00c7\3\2\2\2\u00c9\u00cc\3\2\2\2\u00ca\u00c8\3\2\2\2\u00ca"+
		"\u00cb\3\2\2\2\u00cb\u00cd\3\2\2\2\u00cc\u00ca\3\2\2\2\u00cd\u00ce\7G"+
		"\2\2\u00ce\27\3\2\2\2\u00cf\u00d0\7;\2\2\u00d0\u00d3\7?\2\2\u00d1\u00d2"+
		"\7C\2\2\u00d2\u00d4\7?\2\2\u00d3\u00d1\3\2\2\2\u00d3\u00d4\3\2\2\2\u00d4"+
		"\u00d5\3\2\2\2\u00d5\u00d6\7<\2\2\u00d6\u00d7\5`\61\2\u00d7\u00d8\5,\27"+
		"\2\u00d8\31\3\2\2\2\u00d9\u00da\7\t\2\2\u00da\u00db\7E\2\2\u00db\33\3"+
		"\2\2\2\u00dc\u00dd\7\n\2\2\u00dd\u00de\7E\2\2\u00de\35\3\2\2\2\u00df\u00e0"+
		"\5v<\2\u00e0\u00e1\7E\2\2\u00e1\37\3\2\2\2\u00e2\u00e3\7=\2\2\u00e3\u00e4"+
		"\5`\61\2\u00e4\u00e8\5,\27\2\u00e5\u00e7\5\"\22\2\u00e6\u00e5\3\2\2\2"+
		"\u00e7\u00ea\3\2\2\2\u00e8\u00e6\3\2\2\2\u00e8\u00e9\3\2\2\2\u00e9\u00ec"+
		"\3\2\2\2\u00ea\u00e8\3\2\2\2\u00eb\u00ed\5$\23\2\u00ec\u00eb\3\2\2\2\u00ec"+
		"\u00ed\3\2\2\2\u00ed!\3\2\2\2\u00ee\u00ef\7>\2\2\u00ef\u00f0\7=\2\2\u00f0"+
		"\u00f1\5`\61\2\u00f1\u00f2\5,\27\2\u00f2#\3\2\2\2\u00f3\u00f4\7>\2\2\u00f4"+
		"\u00f5\5,\27\2\u00f5%\3\2\2\2\u00f6\u00fc\7\66\2\2\u00f7\u00fc\7\67\2"+
		"\2\u00f8\u00fc\5*\26\2\u00f9\u00fc\5(\25\2\u00fa\u00fc\7.\2\2\u00fb\u00f6"+
		"\3\2\2\2\u00fb\u00f7\3\2\2\2\u00fb\u00f8\3\2\2\2\u00fb\u00f9\3\2\2\2\u00fb"+
		"\u00fa\3\2\2\2\u00fc\'\3\2\2\2\u00fd\u00fe\7:\2\2\u00fe\u00ff\7H\2\2\u00ff"+
		"\u0100\5&\24\2\u0100\u0101\7I\2\2\u0101\u0102\5&\24\2\u0102)\3\2\2\2\u0103"+
		"\u0104\7H\2\2\u0104\u0105\7I\2\2\u0105\u0106\5&\24\2\u0106+\3\2\2\2\u0107"+
		"\u010b\7F\2\2\u0108\u010a\5\6\4\2\u0109\u0108\3\2\2\2\u010a\u010d\3\2"+
		"\2\2\u010b\u0109\3\2\2\2\u010b\u010c\3\2\2\2\u010c\u010e\3\2\2\2\u010d"+
		"\u010b\3\2\2\2\u010e\u010f\7G\2\2\u010f-\3\2\2\2\u0110\u0114\5\60\31\2"+
		"\u0111\u0114\7&\2\2\u0112\u0114\7%\2\2\u0113\u0110\3\2\2\2\u0113\u0111"+
		"\3\2\2\2\u0113\u0112\3\2\2\2\u0114/\3\2\2\2\u0115\u0116\t\3\2\2\u0116"+
		"\61\3\2\2\2\u0117\u0118\7\6\2\2\u0118\u0119\7(\2\2\u0119\u011a\5d\63\2"+
		"\u011a\u011b\7E\2\2\u011b\63\3\2\2\2\u011c\u011d\7\6\2\2\u011d\u011e\t"+
		"\4\2\2\u011e\u011f\5d\63\2\u011f\u0120\7\"\2\2\u0120\u0121\5b\62\2\u0121"+
		"\u0122\7E\2\2\u0122\65\3\2\2\2\u0123\u0124\7\6\2\2\u0124\u0125\t\5\2\2"+
		"\u0125\u0126\5d\63\2\u0126\u0127\7\"\2\2\u0127\u0128\5b\62\2\u0128\u0129"+
		"\7E\2\2\u0129\67\3\2\2\2\u012a\u012b\7\33\2\2\u012b\u012c\7\34\2\2\u012c"+
		"\u012d\5d\63\2\u012d\u012e\7\35\2\2\u012e\u012f\5`\61\2\u012f\u0130\7"+
		"E\2\2\u01309\3\2\2\2\u0131\u0132\7\30\2\2\u0132\u0133\5d\63\2\u0133\u0134"+
		"\7\35\2\2\u0134\u0135\5d\63\2\u0135\u0136\7E\2\2\u0136;\3\2\2\2\u0137"+
		"\u0138\7\31\2\2\u0138\u0139\5d\63\2\u0139\u013a\7\32\2\2\u013a\u013b\5"+
		"d\63\2\u013b\u013c\7E\2\2\u013c=\3\2\2\2\u013d\u013e\7\36\2\2\u013e\u013f"+
		"\5d\63\2\u013f\u0140\7\37\2\2\u0140\u0141\5d\63\2\u0141\u0142\7 \2\2\u0142"+
		"\u0143\5h\65\2\u0143\u0144\7E\2\2\u0144?\3\2\2\2\u0145\u0146\7!\2\2\u0146"+
		"\u0147\5d\63\2\u0147\u0148\7\37\2\2\u0148\u0149\5d\63\2\u0149\u014a\7"+
		"E\2\2\u014aA\3\2\2\2\u014b\u014c\7\b\2\2\u014c\u014d\5.\30\2\u014d\u014e"+
		"\5d\63\2\u014e\u014f\7E\2\2\u014fC\3\2\2\2\u0150\u0151\7\6\2\2\u0151\u0152"+
		"\7&\2\2\u0152\u0153\5d\63\2\u0153\u0157\7F\2\2\u0154\u0156\5F$\2\u0155"+
		"\u0154\3\2\2\2\u0156\u0159\3\2\2\2\u0157\u0155\3\2\2\2\u0157\u0158\3\2"+
		"\2\2\u0158\u015a\3\2\2\2\u0159\u0157\3\2\2\2\u015a\u015b\7G\2\2\u015b"+
		"E\3\2\2\2\u015c\u015d\7\6\2\2\u015d\u015e\7\f\2\2\u015e\u015f\5d\63\2"+
		"\u015f\u0160\7\r\2\2\u0160\u0161\5H%\2\u0161\u0162\7\16\2\2\u0162\u0165"+
		"\5`\61\2\u0163\u0164\7\17\2\2\u0164\u0166\5J&\2\u0165\u0163\3\2\2\2\u0165"+
		"\u0166\3\2\2\2\u0166\u0167\3\2\2\2\u0167\u0168\5N(\2\u0168G\3\2\2\2\u0169"+
		"\u0173\7\21\2\2\u016a\u016b\7,\2\2\u016b\u0173\5d\63\2\u016c\u016d\7\22"+
		"\2\2\u016d\u0173\5b\62\2\u016e\u016f\7\23\2\2\u016f\u0173\5d\63\2\u0170"+
		"\u0171\7\24\2\2\u0171\u0173\5d\63\2\u0172\u0169\3\2\2\2\u0172\u016a\3"+
		"\2\2\2\u0172\u016c\3\2\2\2\u0172\u016e\3\2\2\2\u0172\u0170\3\2\2\2\u0173"+
		"I\3\2\2\2\u0174\u017f\5d\63\2\u0175\u017f\5L\'\2\u0176\u0177\5L\'\2\u0177"+
		"\u0178\7<\2\2\u0178\u0179\5d\63\2\u0179\u017f\3\2\2\2\u017a\u017b\5L\'"+
		"\2\u017b\u017c\7\34\2\2\u017c\u017d\5b\62\2\u017d\u017f\3\2\2\2\u017e"+
		"\u0174\3\2\2\2\u017e\u0175\3\2\2\2\u017e\u0176\3\2\2\2\u017e\u017a\3\2"+
		"\2\2\u017fK\3\2\2\2\u0180\u0181\7.\2\2\u0181\u0182\7\13\2\2\u0182M\3\2"+
		"\2\2\u0183\u0184\7\20\2\2\u0184\u0185\7L\2\2\u0185\u0186\7?\2\2\u0186"+
		"\u0187\7M\2\2\u0187\u0188\5P)\2\u0188O\3\2\2\2\u0189\u018a\7F\2\2\u018a"+
		"\u018b\5R*\2\u018b\u018c\7G\2\2\u018cQ\3\2\2\2\u018d\u018f\5T+\2\u018e"+
		"\u018d\3\2\2\2\u018f\u0192\3\2\2\2\u0190\u018e\3\2\2\2\u0190\u0191\3\2"+
		"\2\2\u0191S\3\2\2\2\u0192\u0190\3\2\2\2\u0193\u0196\5\6\4\2\u0194\u0196"+
		"\5F$\2\u0195\u0193\3\2\2\2\u0195\u0194\3\2\2\2\u0196U\3\2\2\2\u0197\u0198"+
		"\7\b\2\2\u0198\u0199\7\f\2\2\u0199\u019a\5d\63\2\u019a\u019b\7\32\2\2"+
		"\u019b\u019c\7&\2\2\u019c\u019d\5d\63\2\u019d\u019e\7E\2\2\u019eW\3\2"+
		"\2\2\u019f\u01a0\7\6\2\2\u01a0\u01a1\7%\2\2\u01a1\u01a2\5d\63\2\u01a2"+
		"\u01a3\7$\2\2\u01a3\u01a4\t\6\2\2\u01a4\u01a5\5d\63\2\u01a5\u01a6\7\'"+
		"\2\2\u01a6\u01a7\5h\65\2\u01a7\u01a8\7\17\2\2\u01a8\u01a9\t\7\2\2\u01a9"+
		"\u01aa\7\34\2\2\u01aa\u01ab\5Z.\2\u01ab\u01ac\7E\2\2\u01acY\3\2\2\2\u01ad"+
		"\u01b2\5\\/\2\u01ae\u01af\7C\2\2\u01af\u01b1\5\\/\2\u01b0\u01ae\3\2\2"+
		"\2\u01b1\u01b4\3\2\2\2\u01b2\u01b0\3\2\2\2\u01b2\u01b3\3\2\2\2\u01b3\u01b6"+
		"\3\2\2\2\u01b4\u01b2\3\2\2\2\u01b5\u01ad\3\2\2\2\u01b5\u01b6\3\2\2\2\u01b6"+
		"[\3\2\2\2\u01b7\u01b9\7N\2\2\u01b8\u01b7\3\2\2\2\u01b8\u01b9\3\2\2\2\u01b9"+
		"\u01ba\3\2\2\2\u01ba\u01bb\5d\63\2\u01bb]\3\2\2\2\u01bc\u01bd\7\27\2\2"+
		"\u01bd\u01be\5h\65\2\u01be\u01bf\7E\2\2\u01bf_\3\2\2\2\u01c0\u01c4\5t"+
		";\2\u01c1\u01c4\5v<\2\u01c2\u01c4\5r:\2\u01c3\u01c0\3\2\2\2\u01c3\u01c1"+
		"\3\2\2\2\u01c3\u01c2\3\2\2\2\u01c4a\3\2\2\2\u01c5\u01ca\5d\63\2\u01c6"+
		"\u01c7\7C\2\2\u01c7\u01c9\5d\63\2\u01c8\u01c6\3\2\2\2\u01c9\u01cc\3\2"+
		"\2\2\u01ca\u01c8\3\2\2\2\u01ca\u01cb\3\2\2\2\u01cbc\3\2\2\2\u01cc\u01ca"+
		"\3\2\2\2\u01cd\u01d0\5t;\2\u01ce\u01d0\5v<\2\u01cf\u01cd\3\2\2\2\u01cf"+
		"\u01ce\3\2\2\2\u01d0e\3\2\2\2\u01d1\u01da\7H\2\2\u01d2\u01d7\5`\61\2\u01d3"+
		"\u01d4\7C\2\2\u01d4\u01d6\5`\61\2\u01d5\u01d3\3\2\2\2\u01d6\u01d9\3\2"+
		"\2\2\u01d7\u01d5\3\2\2\2\u01d7\u01d8\3\2\2\2\u01d8\u01db\3\2\2\2\u01d9"+
		"\u01d7\3\2\2\2\u01da\u01d2\3\2\2\2\u01da\u01db\3\2\2\2\u01db\u01dc\3\2"+
		"\2\2\u01dc\u01dd\7I\2\2\u01ddg\3\2\2\2\u01de\u01e3\5j\66\2\u01df\u01e0"+
		"\7C\2\2\u01e0\u01e2\5j\66\2\u01e1\u01df\3\2\2\2\u01e2\u01e5\3\2\2\2\u01e3"+
		"\u01e1\3\2\2\2\u01e3\u01e4\3\2\2\2\u01e4\u01e7\3\2\2\2\u01e5\u01e3\3\2"+
		"\2\2\u01e6\u01de\3\2\2\2\u01e6\u01e7\3\2\2\2\u01e7i\3\2\2\2\u01e8\u01e9"+
		"\t\b\2\2\u01e9k\3\2\2\2\u01ea\u01f3\7F\2\2\u01eb\u01f0\5n8\2\u01ec\u01ed"+
		"\7C\2\2\u01ed\u01ef\5n8\2\u01ee\u01ec\3\2\2\2\u01ef\u01f2\3\2\2\2\u01f0"+
		"\u01ee\3\2\2\2\u01f0\u01f1\3\2\2\2\u01f1\u01f4\3\2\2\2\u01f2\u01f0\3\2"+
		"\2\2\u01f3\u01eb\3\2\2\2\u01f3\u01f4\3\2\2\2\u01f4\u01f5\3\2\2\2\u01f5"+
		"\u01f6\7G\2\2\u01f6m\3\2\2\2\u01f7\u01f8\5`\61\2\u01f8\u01f9\7D\2\2\u01f9"+
		"\u01fa\5`\61\2\u01fao\3\2\2\2\u01fb\u0200\7?\2\2\u01fc\u01fd\7H\2\2\u01fd"+
		"\u01fe\5`\61\2\u01fe\u01ff\7I\2\2\u01ff\u0201\3\2\2\2\u0200\u01fc\3\2"+
		"\2\2\u0201\u0202\3\2\2\2\u0202\u0200\3\2\2\2\u0202\u0203\3\2\2\2\u0203"+
		"q\3\2\2\2\u0204\u0209\7@\2\2\u0205\u0209\7\63\2\2\u0206\u0209\5f\64\2"+
		"\u0207\u0209\5l\67\2\u0208\u0204\3\2\2\2\u0208\u0205\3\2\2\2\u0208\u0206"+
		"\3\2\2\2\u0208\u0207\3\2\2\2\u0209s\3\2\2\2\u020a\u020d\7?\2\2\u020b\u020d"+
		"\5p9\2\u020c\u020a\3\2\2\2\u020c\u020b\3\2\2\2\u020du\3\2\2\2\u020e\u020f"+
		"\7?\2\2\u020f\u0210\5x=\2\u0210w\3\2\2\2\u0211\u021a\7L\2\2\u0212\u0217"+
		"\5`\61\2\u0213\u0214\7C\2\2\u0214\u0216\5`\61\2\u0215\u0213\3\2\2\2\u0216"+
		"\u0219\3\2\2\2\u0217\u0215\3\2\2\2\u0217\u0218\3\2\2\2\u0218\u021b\3\2"+
		"\2\2\u0219\u0217\3\2\2\2\u021a\u0212\3\2\2\2\u021a\u021b\3\2\2\2\u021b"+
		"\u021c\3\2\2\2\u021c\u021d\7M\2\2\u021dy\3\2\2\2(\u0080\u0098\u009b\u00a8"+
		"\u00b1\u00b4\u00be\u00c4\u00ca\u00d3\u00e8\u00ec\u00fb\u010b\u0113\u0157"+
		"\u0165\u0172\u017e\u0190\u0195\u01b2\u01b5\u01b8\u01c3\u01ca\u01cf\u01d7"+
		"\u01da\u01e3\u01e6\u01f0\u01f3\u0202\u0208\u020c\u0217\u021a";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}