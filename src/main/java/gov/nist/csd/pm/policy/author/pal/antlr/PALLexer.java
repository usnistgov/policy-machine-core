// Generated from PAL.g4 by ANTLR 4.8
package gov.nist.csd.pm.policy.author.pal.antlr;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class PALLexer extends Lexer {
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
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "CREATE", "UPDATE", "DELETE", "BREAK", "CONTINUE", 
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


	public PALLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "PAL.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2P\u0289\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
		"\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\3\2\3\2\3\3\3\3\3\3\3\4\3\4\3"+
		"\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7"+
		"\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3"+
		"\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\13"+
		"\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3"+
		"\r\3\r\3\16\3\16\3\16\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\21\3\21\3\21"+
		"\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23"+
		"\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24"+
		"\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3\26"+
		"\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27"+
		"\3\27\3\27\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\31"+
		"\3\31\3\31\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32"+
		"\3\32\3\32\3\32\3\33\3\33\3\33\3\34\3\34\3\34\3\35\3\35\3\35\3\35\3\35"+
		"\3\35\3\35\3\35\3\35\3\35\3\36\3\36\3\36\3\36\3\37\3\37\3\37\3\37\3\37"+
		"\3\37\3\37\3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3!\3!\3!\3!\3!\3!\3!\3!\3"+
		"!\3!\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3#\3#\3#"+
		"\3#\3#\3$\3$\3$\3$\3$\3$\3$\3$\3$\3$\3$\3$\3%\3%\3%\3%\3%\3%\3%\3%\3%"+
		"\3%\3%\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3\'\3\'\3\'\3\'\3\'\3"+
		"\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3(\3(\3(\3(\3)\3)\3)\3)\3*\3*\3*\3*\3*"+
		"\3*\3*\3+\3+\3+\3+\3+\3,\3,\3,\3,\3,\3,\3,\3,\3,\3,\3-\3-\3-\3-\3.\3."+
		"\3.\3.\3/\3/\3/\3/\3/\3/\3\60\3\60\3\60\3\60\3\60\3\60\3\60\3\60\3\60"+
		"\3\61\3\61\3\61\3\61\3\61\3\61\3\61\3\62\3\62\5\62\u0205\n\62\3\63\3\63"+
		"\3\63\3\63\3\63\3\64\3\64\3\64\3\64\3\64\3\64\3\65\3\65\3\65\3\65\3\65"+
		"\3\65\3\65\3\66\3\66\3\66\3\66\3\66\3\66\3\66\3\66\3\67\3\67\3\67\3\67"+
		"\3\67\38\38\38\38\38\38\39\39\39\39\3:\3:\3:\3:\3:\3:\3:\3:\3;\3;\3;\3"+
		"<\3<\3<\3=\3=\3=\3=\3=\3>\6>\u0244\n>\r>\16>\u0245\3>\7>\u0249\n>\f>\16"+
		">\u024c\13>\3?\3?\7?\u0250\n?\f?\16?\u0253\13?\3?\3?\3@\3@\7@\u0259\n"+
		"@\f@\16@\u025c\13@\3@\3@\3@\3@\3A\6A\u0263\nA\rA\16A\u0264\3A\3A\3B\3"+
		"B\7B\u026b\nB\fB\16B\u026e\13B\3C\3C\3D\3D\3E\3E\3F\3F\3G\3G\3H\3H\3I"+
		"\3I\3J\3J\3K\3K\3L\3L\3M\3M\3N\3N\3O\3O\2\2P\3\3\5\4\7\5\t\6\13\7\r\b"+
		"\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26"+
		"+\27-\30/\31\61\32\63\33\65\34\67\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S"+
		"+U,W-Y.[/]\60_\61a\62c\63e\64g\65i\66k\67m8o9q:s;u<w=y>{?}@\177A\u0081"+
		"B\u0083C\u0085D\u0087E\u0089F\u008bG\u008dH\u008fI\u0091J\u0093K\u0095"+
		"L\u0097M\u0099N\u009bO\u009dP\3\2\37\4\2EEee\4\2TTtt\4\2GGgg\4\2CCcc\4"+
		"\2VVvv\4\2WWww\4\2RRrr\4\2FFff\4\2NNnn\4\2DDdd\4\2MMmm\4\2QQqq\4\2PPp"+
		"p\4\2KKkk\4\2[[{{\3\2\"\"\4\2OOoo\4\2YYyy\4\2JJjj\4\2HHhh\4\2UUuu\4\2"+
		"IIii\4\2LLll\4\2XXxx\5\2C\\aac|\6\2\62;C\\aac|\4\2))^^\3\2\f\f\5\2\13"+
		"\f\17\17\"\"\2\u028f\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2"+
		"\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3"+
		"\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2"+
		"\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2"+
		"\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2"+
		"\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2"+
		"\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q"+
		"\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2]\3\2"+
		"\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3\2\2\2\2i\3\2\2\2"+
		"\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2\2\2q\3\2\2\2\2s\3\2\2\2\2u\3\2\2\2\2w"+
		"\3\2\2\2\2y\3\2\2\2\2{\3\2\2\2\2}\3\2\2\2\2\177\3\2\2\2\2\u0081\3\2\2"+
		"\2\2\u0083\3\2\2\2\2\u0085\3\2\2\2\2\u0087\3\2\2\2\2\u0089\3\2\2\2\2\u008b"+
		"\3\2\2\2\2\u008d\3\2\2\2\2\u008f\3\2\2\2\2\u0091\3\2\2\2\2\u0093\3\2\2"+
		"\2\2\u0095\3\2\2\2\2\u0097\3\2\2\2\2\u0099\3\2\2\2\2\u009b\3\2\2\2\2\u009d"+
		"\3\2\2\2\3\u009f\3\2\2\2\5\u00a1\3\2\2\2\7\u00a4\3\2\2\2\t\u00a7\3\2\2"+
		"\2\13\u00ae\3\2\2\2\r\u00b5\3\2\2\2\17\u00bc\3\2\2\2\21\u00c2\3\2\2\2"+
		"\23\u00cb\3\2\2\2\25\u00da\3\2\2\2\27\u00df\3\2\2\2\31\u00e4\3\2\2\2\33"+
		"\u00ed\3\2\2\2\35\u00f0\3\2\2\2\37\u00f3\3\2\2\2!\u00f7\3\2\2\2#\u00fa"+
		"\3\2\2\2%\u0103\3\2\2\2\'\u010b\3\2\2\2)\u0118\3\2\2\2+\u011e\3\2\2\2"+
		"-\u012d\3\2\2\2/\u0134\3\2\2\2\61\u013d\3\2\2\2\63\u0142\3\2\2\2\65\u0151"+
		"\3\2\2\2\67\u0154\3\2\2\29\u0157\3\2\2\2;\u0161\3\2\2\2=\u0165\3\2\2\2"+
		"?\u016c\3\2\2\2A\u0177\3\2\2\2C\u0181\3\2\2\2E\u018f\3\2\2\2G\u0194\3"+
		"\2\2\2I\u01a0\3\2\2\2K\u01ab\3\2\2\2M\u01b9\3\2\2\2O\u01c6\3\2\2\2Q\u01ca"+
		"\3\2\2\2S\u01ce\3\2\2\2U\u01d5\3\2\2\2W\u01da\3\2\2\2Y\u01e4\3\2\2\2["+
		"\u01e8\3\2\2\2]\u01ec\3\2\2\2_\u01f2\3\2\2\2a\u01fb\3\2\2\2c\u0204\3\2"+
		"\2\2e\u0206\3\2\2\2g\u020b\3\2\2\2i\u0211\3\2\2\2k\u0218\3\2\2\2m\u0220"+
		"\3\2\2\2o\u0225\3\2\2\2q\u022b\3\2\2\2s\u022f\3\2\2\2u\u0237\3\2\2\2w"+
		"\u023a\3\2\2\2y\u023d\3\2\2\2{\u0243\3\2\2\2}\u024d\3\2\2\2\177\u0256"+
		"\3\2\2\2\u0081\u0262\3\2\2\2\u0083\u0268\3\2\2\2\u0085\u026f\3\2\2\2\u0087"+
		"\u0271\3\2\2\2\u0089\u0273\3\2\2\2\u008b\u0275\3\2\2\2\u008d\u0277\3\2"+
		"\2\2\u008f\u0279\3\2\2\2\u0091\u027b\3\2\2\2\u0093\u027d\3\2\2\2\u0095"+
		"\u027f\3\2\2\2\u0097\u0281\3\2\2\2\u0099\u0283\3\2\2\2\u009b\u0285\3\2"+
		"\2\2\u009d\u0287\3\2\2\2\u009f\u00a0\7,\2\2\u00a0\4\3\2\2\2\u00a1\u00a2"+
		"\7,\2\2\u00a2\u00a3\7t\2\2\u00a3\6\3\2\2\2\u00a4\u00a5\7,\2\2\u00a5\u00a6"+
		"\7c\2\2\u00a6\b\3\2\2\2\u00a7\u00a8\t\2\2\2\u00a8\u00a9\t\3\2\2\u00a9"+
		"\u00aa\t\4\2\2\u00aa\u00ab\t\5\2\2\u00ab\u00ac\t\6\2\2\u00ac\u00ad\t\4"+
		"\2\2\u00ad\n\3\2\2\2\u00ae\u00af\t\7\2\2\u00af\u00b0\t\b\2\2\u00b0\u00b1"+
		"\t\t\2\2\u00b1\u00b2\t\5\2\2\u00b2\u00b3\t\6\2\2\u00b3\u00b4\t\4\2\2\u00b4"+
		"\f\3\2\2\2\u00b5\u00b6\t\t\2\2\u00b6\u00b7\t\4\2\2\u00b7\u00b8\t\n\2\2"+
		"\u00b8\u00b9\t\4\2\2\u00b9\u00ba\t\6\2\2\u00ba\u00bb\t\4\2\2\u00bb\16"+
		"\3\2\2\2\u00bc\u00bd\t\13\2\2\u00bd\u00be\t\3\2\2\u00be\u00bf\t\4\2\2"+
		"\u00bf\u00c0\t\5\2\2\u00c0\u00c1\t\f\2\2\u00c1\20\3\2\2\2\u00c2\u00c3"+
		"\t\2\2\2\u00c3\u00c4\t\r\2\2\u00c4\u00c5\t\16\2\2\u00c5\u00c6\t\6\2\2"+
		"\u00c6\u00c7\t\17\2\2\u00c7\u00c8\t\16\2\2\u00c8\u00c9\t\7\2\2\u00c9\u00ca"+
		"\t\4\2\2\u00ca\22\3\2\2\2\u00cb\u00cc\t\b\2\2\u00cc\u00cd\t\r\2\2\u00cd"+
		"\u00ce\t\n\2\2\u00ce\u00cf\t\17\2\2\u00cf\u00d0\t\2\2\2\u00d0\u00d1\t"+
		"\20\2\2\u00d1\u00d2\t\21\2\2\u00d2\u00d3\t\4\2\2\u00d3\u00d4\t\n\2\2\u00d4"+
		"\u00d5\t\4\2\2\u00d5\u00d6\t\22\2\2\u00d6\u00d7\t\4\2\2\u00d7\u00d8\t"+
		"\16\2\2\u00d8\u00d9\t\6\2\2\u00d9\24\3\2\2\2\u00da\u00db\t\3\2\2\u00db"+
		"\u00dc\t\7\2\2\u00dc\u00dd\t\n\2\2\u00dd\u00de\t\4\2\2\u00de\26\3\2\2"+
		"\2\u00df\u00e0\t\23\2\2\u00e0\u00e1\t\24\2\2\u00e1\u00e2\t\4\2\2\u00e2"+
		"\u00e3\t\16\2\2\u00e3\30\3\2\2\2\u00e4\u00e5\t\b\2\2\u00e5\u00e6\t\4\2"+
		"\2\u00e6\u00e7\t\3\2\2\u00e7\u00e8\t\25\2\2\u00e8\u00e9\t\r\2\2\u00e9"+
		"\u00ea\t\3\2\2\u00ea\u00eb\t\22\2\2\u00eb\u00ec\t\26\2\2\u00ec\32\3\2"+
		"\2\2\u00ed\u00ee\t\r\2\2\u00ee\u00ef\t\16\2\2\u00ef\34\3\2\2\2\u00f0\u00f1"+
		"\t\t\2\2\u00f1\u00f2\t\r\2\2\u00f2\36\3\2\2\2\u00f3\u00f4\5Y-\2\u00f4"+
		"\u00f5\t\21\2\2\u00f5\u00f6\5U+\2\u00f6 \3\2\2\2\u00f7\u00f8\5U+\2\u00f8"+
		"\u00f9\t\26\2\2\u00f9\"\3\2\2\2\u00fa\u00fb\5\37\20\2\u00fb\u00fc\t\21"+
		"\2\2\u00fc\u00fd\t\23\2\2\u00fd\u00fe\t\17\2\2\u00fe\u00ff\t\6\2\2\u00ff"+
		"\u0100\t\24\2\2\u0100\u0101\t\21\2\2\u0101\u0102\5W,\2\u0102$\3\2\2\2"+
		"\u0103\u0104\t\b\2\2\u0104\u0105\t\3\2\2\u0105\u0106\t\r\2\2\u0106\u0107"+
		"\t\2\2\2\u0107\u0108\t\4\2\2\u0108\u0109\t\26\2\2\u0109\u010a\t\26\2\2"+
		"\u010a&\3\2\2\2\u010b\u010c\t\17\2\2\u010c\u010d\t\16\2\2\u010d\u010e"+
		"\t\6\2\2\u010e\u010f\t\4\2\2\u010f\u0110\t\3\2\2\u0110\u0111\t\26\2\2"+
		"\u0111\u0112\t\4\2\2\u0112\u0113\t\2\2\2\u0113\u0114\t\6\2\2\u0114\u0115"+
		"\t\17\2\2\u0115\u0116\t\r\2\2\u0116\u0117\t\16\2\2\u0117(\3\2\2\2\u0118"+
		"\u0119\t\7\2\2\u0119\u011a\t\16\2\2\u011a\u011b\t\17\2\2\u011b\u011c\t"+
		"\r\2\2\u011c\u011d\t\16\2\2\u011d*\3\2\2\2\u011e\u011f\t\26\2\2\u011f"+
		"\u0120\t\4\2\2\u0120\u0121\t\6\2\2\u0121\u0122\t\21\2\2\u0122\u0123\t"+
		"\3\2\2\u0123\u0124\t\4\2\2\u0124\u0125\t\26\2\2\u0125\u0126\t\r\2\2\u0126"+
		"\u0127\t\7\2\2\u0127\u0128\t\3\2\2\u0128\u0129\t\2\2\2\u0129\u012a\t\4"+
		"\2\2\u012a\u012b\t\21\2\2\u012b\u012c\5K&\2\u012c,\3\2\2\2\u012d\u012e"+
		"\t\5\2\2\u012e\u012f\t\26\2\2\u012f\u0130\t\26\2\2\u0130\u0131\t\17\2"+
		"\2\u0131\u0132\t\27\2\2\u0132\u0133\t\16\2\2\u0133.\3\2\2\2\u0134\u0135"+
		"\t\t\2\2\u0135\u0136\t\4\2\2\u0136\u0137\t\5\2\2\u0137\u0138\t\26\2\2"+
		"\u0138\u0139\t\26\2\2\u0139\u013a\t\17\2\2\u013a\u013b\t\27\2\2\u013b"+
		"\u013c\t\16\2\2\u013c\60\3\2\2\2\u013d\u013e\t\25\2\2\u013e\u013f\t\3"+
		"\2\2\u013f\u0140\t\r\2\2\u0140\u0141\t\22\2\2\u0141\62\3\2\2\2\u0142\u0143"+
		"\t\26\2\2\u0143\u0144\t\4\2\2\u0144\u0145\t\6\2\2\u0145\u0146\t\21\2\2"+
		"\u0146\u0147\t\b\2\2\u0147\u0148\t\3\2\2\u0148\u0149\t\r\2\2\u0149\u014a"+
		"\t\b\2\2\u014a\u014b\t\4\2\2\u014b\u014c\t\3\2\2\u014c\u014d\t\6\2\2\u014d"+
		"\u014e\t\17\2\2\u014e\u014f\t\4\2\2\u014f\u0150\t\26\2\2\u0150\64\3\2"+
		"\2\2\u0151\u0152\t\r\2\2\u0152\u0153\t\25\2\2\u0153\66\3\2\2\2\u0154\u0155"+
		"\t\6\2\2\u0155\u0156\t\r\2\2\u01568\3\2\2\2\u0157\u0158\t\5\2\2\u0158"+
		"\u0159\t\26\2\2\u0159\u015a\t\26\2\2\u015a\u015b\t\r\2\2\u015b\u015c\t"+
		"\2\2\2\u015c\u015d\t\17\2\2\u015d\u015e\t\5\2\2\u015e\u015f\t\6\2\2\u015f"+
		"\u0160\t\4\2\2\u0160:\3\2\2\2\u0161\u0162\t\5\2\2\u0162\u0163\t\16\2\2"+
		"\u0163\u0164\t\t\2\2\u0164<\3\2\2\2\u0165\u0166\t\23\2\2\u0166\u0167\t"+
		"\17\2\2\u0167\u0168\t\6\2\2\u0168\u0169\t\24\2\2\u0169\u016a\t\21\2\2"+
		"\u016a\u016b\5K&\2\u016b>\3\2\2\2\u016c\u016d\t\t\2\2\u016d\u016e\t\17"+
		"\2\2\u016e\u016f\t\26\2\2\u016f\u0170\t\26\2\2\u0170\u0171\t\r\2\2\u0171"+
		"\u0172\t\2\2\2\u0172\u0173\t\17\2\2\u0173\u0174\t\5\2\2\u0174\u0175\t"+
		"\6\2\2\u0175\u0176\t\4\2\2\u0176@\3\2\2\2\u0177\u0178\t\5\2\2\u0178\u0179"+
		"\t\26\2\2\u0179\u017a\t\26\2\2\u017a\u017b\t\17\2\2\u017b\u017c\t\27\2"+
		"\2\u017c\u017d\t\16\2\2\u017d\u017e\t\21\2\2\u017e\u017f\t\6\2\2\u017f"+
		"\u0180\t\r\2\2\u0180B\3\2\2\2\u0181\u0182\t\t\2\2\u0182\u0183\t\4\2\2"+
		"\u0183\u0184\t\5\2\2\u0184\u0185\t\26\2\2\u0185\u0186\t\26\2\2\u0186\u0187"+
		"\t\17\2\2\u0187\u0188\t\27\2\2\u0188\u0189\t\16\2\2\u0189\u018a\t\21\2"+
		"\2\u018a\u018b\t\25\2\2\u018b\u018c\t\3\2\2\u018c\u018d\t\r\2\2\u018d"+
		"\u018e\t\22\2\2\u018eD\3\2\2\2\u018f\u0190\t\t\2\2\u0190\u0191\t\4\2\2"+
		"\u0191\u0192\t\16\2\2\u0192\u0193\t\20\2\2\u0193F\3\2\2\2\u0194\u0195"+
		"\t\b\2\2\u0195\u0196\t\3\2\2\u0196\u0197\t\r\2\2\u0197\u0198\t\24\2\2"+
		"\u0198\u0199\t\17\2\2\u0199\u019a\t\13\2\2\u019a\u019b\t\17\2\2\u019b"+
		"\u019c\t\6\2\2\u019c\u019d\t\17\2\2\u019d\u019e\t\r\2\2\u019e\u019f\t"+
		"\16\2\2\u019fH\3\2\2\2\u01a0\u01a1\t\r\2\2\u01a1\u01a2\t\13\2\2\u01a2"+
		"\u01a3\t\n\2\2\u01a3\u01a4\t\17\2\2\u01a4\u01a5\t\27\2\2\u01a5\u01a6\t"+
		"\5\2\2\u01a6\u01a7\t\6\2\2\u01a7\u01a8\t\17\2\2\u01a8\u01a9\t\r\2\2\u01a9"+
		"\u01aa\t\16\2\2\u01aaJ\3\2\2\2\u01ab\u01ac\t\5\2\2\u01ac\u01ad\t\2\2\2"+
		"\u01ad\u01ae\t\2\2\2\u01ae\u01af\t\4\2\2\u01af\u01b0\t\26\2\2\u01b0\u01b1"+
		"\t\26\2\2\u01b1\u01b2\t\21\2\2\u01b2\u01b3\t\3\2\2\u01b3\u01b4\t\17\2"+
		"\2\u01b4\u01b5\t\27\2\2\u01b5\u01b6\t\24\2\2\u01b6\u01b7\t\6\2\2\u01b7"+
		"\u01b8\t\26\2\2\u01b8L\3\2\2\2\u01b9\u01ba\t\b\2\2\u01ba\u01bb\t\r\2\2"+
		"\u01bb\u01bc\t\n\2\2\u01bc\u01bd\t\17\2\2\u01bd\u01be\t\2\2\2\u01be\u01bf"+
		"\t\20\2\2\u01bf\u01c0\t\21\2\2\u01c0\u01c1\t\2\2\2\u01c1\u01c2\t\n\2\2"+
		"\u01c2\u01c3\t\5\2\2\u01c3\u01c4\t\26\2\2\u01c4\u01c5\t\26\2\2\u01c5N"+
		"\3\2\2\2\u01c6\u01c7\5S*\2\u01c7\u01c8\t\21\2\2\u01c8\u01c9\5W,\2\u01c9"+
		"P\3\2\2\2\u01ca\u01cb\5U+\2\u01cb\u01cc\t\21\2\2\u01cc\u01cd\5W,\2\u01cd"+
		"R\3\2\2\2\u01ce\u01cf\t\r\2\2\u01cf\u01d0\t\13\2\2\u01d0\u01d1\t\30\2"+
		"\2\u01d1\u01d2\t\4\2\2\u01d2\u01d3\t\2\2\2\u01d3\u01d4\t\6\2\2\u01d4T"+
		"\3\2\2\2\u01d5\u01d6\t\7\2\2\u01d6\u01d7\t\26\2\2\u01d7\u01d8\t\4\2\2"+
		"\u01d8\u01d9\t\3\2\2\u01d9V\3\2\2\2\u01da\u01db\t\5\2\2\u01db\u01dc\t"+
		"\6\2\2\u01dc\u01dd\t\6\2\2\u01dd\u01de\t\3\2\2\u01de\u01df\t\17\2\2\u01df"+
		"\u01e0\t\13\2\2\u01e0\u01e1\t\7\2\2\u01e1\u01e2\t\6\2\2\u01e2\u01e3\t"+
		"\4\2\2\u01e3X\3\2\2\2\u01e4\u01e5\t\5\2\2\u01e5\u01e6\t\16\2\2\u01e6\u01e7"+
		"\t\20\2\2\u01e7Z\3\2\2\2\u01e8\u01e9\t\n\2\2\u01e9\u01ea\t\4\2\2\u01ea"+
		"\u01eb\t\6\2\2\u01eb\\\3\2\2\2\u01ec\u01ed\t\2\2\2\u01ed\u01ee\t\r\2\2"+
		"\u01ee\u01ef\t\16\2\2\u01ef\u01f0\t\26\2\2\u01f0\u01f1\t\6\2\2\u01f1^"+
		"\3\2\2\2\u01f2\u01f3\t\25\2\2\u01f3\u01f4\t\7\2\2\u01f4\u01f5\t\16\2\2"+
		"\u01f5\u01f6\t\2\2\2\u01f6\u01f7\t\6\2\2\u01f7\u01f8\t\17\2\2\u01f8\u01f9"+
		"\t\r\2\2\u01f9\u01fa\t\16\2\2\u01fa`\3\2\2\2\u01fb\u01fc\t\3\2\2\u01fc"+
		"\u01fd\t\4\2\2\u01fd\u01fe\t\6\2\2\u01fe\u01ff\t\7\2\2\u01ff\u0200\t\3"+
		"\2\2\u0200\u0201\t\16\2\2\u0201b\3\2\2\2\u0202\u0205\5e\63\2\u0203\u0205"+
		"\5g\64\2\u0204\u0202\3\2\2\2\u0204\u0203\3\2\2\2\u0205d\3\2\2\2\u0206"+
		"\u0207\t\6\2\2\u0207\u0208\t\3\2\2\u0208\u0209\t\7\2\2\u0209\u020a\t\4"+
		"\2\2\u020af\3\2\2\2\u020b\u020c\t\25\2\2\u020c\u020d\t\5\2\2\u020d\u020e"+
		"\t\n\2\2\u020e\u020f\t\26\2\2\u020f\u0210\t\4\2\2\u0210h\3\2\2\2\u0211"+
		"\u0212\t\26\2\2\u0212\u0213\t\6\2\2\u0213\u0214\t\3\2\2\u0214\u0215\t"+
		"\17\2\2\u0215\u0216\t\16\2\2\u0216\u0217\t\27\2\2\u0217j\3\2\2\2\u0218"+
		"\u0219\t\13\2\2\u0219\u021a\t\r\2\2\u021a\u021b\t\r\2\2\u021b\u021c\t"+
		"\n\2\2\u021c\u021d\t\4\2\2\u021d\u021e\t\5\2\2\u021e\u021f\t\16\2\2\u021f"+
		"l\3\2\2\2\u0220\u0221\t\31\2\2\u0221\u0222\t\r\2\2\u0222\u0223\t\17\2"+
		"\2\u0223\u0224\t\t\2\2\u0224n\3\2\2\2\u0225\u0226\t\5\2\2\u0226\u0227"+
		"\t\3\2\2\u0227\u0228\t\3\2\2\u0228\u0229\t\5\2\2\u0229\u022a\t\20\2\2"+
		"\u022ap\3\2\2\2\u022b\u022c\t\22\2\2\u022c\u022d\t\5\2\2\u022d\u022e\t"+
		"\b\2\2\u022er\3\2\2\2\u022f\u0230\t\25\2\2\u0230\u0231\t\r\2\2\u0231\u0232"+
		"\t\3\2\2\u0232\u0233\t\4\2\2\u0233\u0234\t\5\2\2\u0234\u0235\t\2\2\2\u0235"+
		"\u0236\t\24\2\2\u0236t\3\2\2\2\u0237\u0238\t\17\2\2\u0238\u0239\t\16\2"+
		"\2\u0239v\3\2\2\2\u023a\u023b\t\17\2\2\u023b\u023c\t\25\2\2\u023cx\3\2"+
		"\2\2\u023d\u023e\t\4\2\2\u023e\u023f\t\n\2\2\u023f\u0240\t\26\2\2\u0240"+
		"\u0241\t\4\2\2\u0241z\3\2\2\2\u0242\u0244\t\32\2\2\u0243\u0242\3\2\2\2"+
		"\u0244\u0245\3\2\2\2\u0245\u0243\3\2\2\2\u0245\u0246\3\2\2\2\u0246\u024a"+
		"\3\2\2\2\u0247\u0249\t\33\2\2\u0248\u0247\3\2\2\2\u0249\u024c\3\2\2\2"+
		"\u024a\u0248\3\2\2\2\u024a\u024b\3\2\2\2\u024b|\3\2\2\2\u024c\u024a\3"+
		"\2\2\2\u024d\u0251\7)\2\2\u024e\u0250\n\34\2\2\u024f\u024e\3\2\2\2\u0250"+
		"\u0253\3\2\2\2\u0251\u024f\3\2\2\2\u0251\u0252\3\2\2\2\u0252\u0254\3\2"+
		"\2\2\u0253\u0251\3\2\2\2\u0254\u0255\7)\2\2\u0255~\3\2\2\2\u0256\u025a"+
		"\7%\2\2\u0257\u0259\n\35\2\2\u0258\u0257\3\2\2\2\u0259\u025c\3\2\2\2\u025a"+
		"\u0258\3\2\2\2\u025a\u025b\3\2\2\2\u025b\u025d\3\2\2\2\u025c\u025a\3\2"+
		"\2\2\u025d\u025e\7\f\2\2\u025e\u025f\3\2\2\2\u025f\u0260\b@\2\2\u0260"+
		"\u0080\3\2\2\2\u0261\u0263\t\36\2\2\u0262\u0261\3\2\2\2\u0263\u0264\3"+
		"\2\2\2\u0264\u0262\3\2\2\2\u0264\u0265\3\2\2\2\u0265\u0266\3\2\2\2\u0266"+
		"\u0267\bA\3\2\u0267\u0082\3\2\2\2\u0268\u026c\7.\2\2\u0269\u026b\7\"\2"+
		"\2\u026a\u0269\3\2\2\2\u026b\u026e\3\2\2\2\u026c\u026a\3\2\2\2\u026c\u026d"+
		"\3\2\2\2\u026d\u0084\3\2\2\2\u026e\u026c\3\2\2\2\u026f\u0270\7<\2\2\u0270"+
		"\u0086\3\2\2\2\u0271\u0272\7=\2\2\u0272\u0088\3\2\2\2\u0273\u0274\7}\2"+
		"\2\u0274\u008a\3\2\2\2\u0275\u0276\7\177\2\2\u0276\u008c\3\2\2\2\u0277"+
		"\u0278\7]\2\2\u0278\u008e\3\2\2\2\u0279\u027a\7_\2\2\u027a\u0090\3\2\2"+
		"\2\u027b\u027c\7>\2\2\u027c\u0092\3\2\2\2\u027d\u027e\7@\2\2\u027e\u0094"+
		"\3\2\2\2\u027f\u0280\7*\2\2\u0280\u0096\3\2\2\2\u0281\u0282\7+\2\2\u0282"+
		"\u0098\3\2\2\2\u0283\u0284\7#\2\2\u0284\u009a\3\2\2\2\u0285\u0286\7?\2"+
		"\2\u0286\u009c\3\2\2\2\u0287\u0288\7\60\2\2\u0288\u009e\3\2\2\2\n\2\u0204"+
		"\u0245\u024a\u0251\u025a\u0264\u026c\4\2\3\2\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}