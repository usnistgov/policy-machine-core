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
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"CREATE", "DELETE", "BREAK", "CONTINUE", "POLICY_ELEMENT", "RULE", "WHEN", 
			"PERFORMS", "AS", "ON", "DO", "ANY_USER", "USERS", "ANY_USER_WITH_ATTRIBUTE", 
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2O\u0286\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
		"\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3"+
		"\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6"+
		"\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3"+
		"\t\3\t\3\n\3\n\3\n\3\13\3\13\3\13\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\16\3\16"+
		"\3\16\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20"+
		"\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21"+
		"\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23"+
		"\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24"+
		"\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\26\3\26"+
		"\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27"+
		"\3\27\3\27\3\27\3\27\3\30\3\30\3\30\3\31\3\31\3\31\3\32\3\32\3\32\3\32"+
		"\3\32\3\32\3\32\3\32\3\32\3\32\3\33\3\33\3\33\3\33\3\34\3\34\3\34\3\34"+
		"\3\34\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\36\3\36"+
		"\3\36\3\36\3\36\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37"+
		"\3\37\3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3"+
		"!\3!\3!\3!\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\5\""+
		"\u01a0\n\"\3#\3#\3#\3#\3#\3#\5#\u01a8\n#\3$\3$\3$\3$\3$\3$\5$\u01b0\n"+
		"$\3%\3%\3%\3%\3%\3%\3%\5%\u01b9\n%\3&\3&\3&\3&\3&\5&\u01c0\n&\3\'\3\'"+
		"\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3(\3(\3(\3(\3)\3)\3)\3)\3*\3*\3*\3*\3"+
		"*\3*\3+\3+\3+\3+\3+\3+\3+\3+\3+\3,\3,\3,\3,\3,\3,\3,\3-\3-\5-\u01ec\n"+
		"-\3.\3.\3.\3.\3.\3/\3/\3/\3/\3/\3/\3\60\3\60\3\60\3\60\3\60\3\60\3\60"+
		"\3\61\3\61\3\61\3\61\3\61\3\61\3\61\3\61\3\62\3\62\3\62\3\62\3\62\3\63"+
		"\3\63\3\63\3\63\3\63\3\63\3\64\3\64\3\64\3\64\3\65\3\65\3\65\3\65\3\65"+
		"\3\65\3\65\3\65\3\66\3\66\3\66\3\66\3\67\3\67\3\67\38\38\38\39\39\39\3"+
		"9\39\3:\3:\3:\3:\3:\3:\3:\3:\3:\3;\6;\u0238\n;\r;\16;\u0239\3<\6<\u023d"+
		"\n<\r<\16<\u023e\3=\3=\5=\u0243\n=\3>\3>\3>\3>\7>\u0249\n>\f>\16>\u024c"+
		"\13>\3>\3>\3?\3?\3?\3?\7?\u0254\n?\f?\16?\u0257\13?\3?\3?\3@\3@\7@\u025d"+
		"\n@\f@\16@\u0260\13@\3@\3@\3@\3@\3A\6A\u0267\nA\rA\16A\u0268\3A\3A\3B"+
		"\3B\3C\3C\3D\3D\3E\3E\3F\3F\3G\3G\3H\3H\3I\3I\3J\3J\3K\3K\3L\3L\3M\3M"+
		"\3N\3N\4\u024a\u0255\2O\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f"+
		"\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63"+
		"\33\65\34\67\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-Y.[/]\60_\61a\62"+
		"c\63e\64g\65i\66k\67m8o9q:s;u<w=y>{?}@\177A\u0081B\u0083C\u0085D\u0087"+
		"E\u0089F\u008bG\u008dH\u008fI\u0091J\u0093K\u0095L\u0097M\u0099N\u009b"+
		"O\3\2\37\3\2ee\3\2tt\3\2gg\3\2cc\3\2vv\3\2ff\3\2nn\3\2dd\3\2mm\3\2qq\3"+
		"\2pp\3\2kk\3\2ww\3\2rr\3\2{{\3\2\"\"\3\2oo\3\2yy\3\2jj\3\2hh\3\2uu\3\2"+
		"ii\3\2ll\3\2xx\3\2\62;\6\2\62;C\\aac|\4\2\f\f\17\17\3\2\f\f\5\2\13\f\17"+
		"\17\"\"\2\u0294\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3"+
		"\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2"+
		"\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3"+
		"\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2"+
		"\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\2"+
		"9\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3"+
		"\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2"+
		"\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\2"+
		"_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3\2\2\2\2i\3\2\2\2\2k\3"+
		"\2\2\2\2m\3\2\2\2\2o\3\2\2\2\2q\3\2\2\2\2s\3\2\2\2\2u\3\2\2\2\2w\3\2\2"+
		"\2\2y\3\2\2\2\2{\3\2\2\2\2}\3\2\2\2\2\177\3\2\2\2\2\u0081\3\2\2\2\2\u0083"+
		"\3\2\2\2\2\u0085\3\2\2\2\2\u0087\3\2\2\2\2\u0089\3\2\2\2\2\u008b\3\2\2"+
		"\2\2\u008d\3\2\2\2\2\u008f\3\2\2\2\2\u0091\3\2\2\2\2\u0093\3\2\2\2\2\u0095"+
		"\3\2\2\2\2\u0097\3\2\2\2\2\u0099\3\2\2\2\2\u009b\3\2\2\2\3\u009d\3\2\2"+
		"\2\5\u00a4\3\2\2\2\7\u00ab\3\2\2\2\t\u00b1\3\2\2\2\13\u00ba\3\2\2\2\r"+
		"\u00c9\3\2\2\2\17\u00ce\3\2\2\2\21\u00d3\3\2\2\2\23\u00dc\3\2\2\2\25\u00df"+
		"\3\2\2\2\27\u00e2\3\2\2\2\31\u00e5\3\2\2\2\33\u00e9\3\2\2\2\35\u00ec\3"+
		"\2\2\2\37\u00f5\3\2\2\2!\u00fd\3\2\2\2#\u010a\3\2\2\2%\u0110\3\2\2\2\'"+
		"\u011f\3\2\2\2)\u0126\3\2\2\2+\u012f\3\2\2\2-\u0134\3\2\2\2/\u0143\3\2"+
		"\2\2\61\u0146\3\2\2\2\63\u0149\3\2\2\2\65\u0153\3\2\2\2\67\u0157\3\2\2"+
		"\29\u015c\3\2\2\2;\u0167\3\2\2\2=\u016c\3\2\2\2?\u0178\3\2\2\2A\u0183"+
		"\3\2\2\2C\u019f\3\2\2\2E\u01a7\3\2\2\2G\u01af\3\2\2\2I\u01b8\3\2\2\2K"+
		"\u01bf\3\2\2\2M\u01c1\3\2\2\2O\u01cb\3\2\2\2Q\u01cf\3\2\2\2S\u01d3\3\2"+
		"\2\2U\u01d9\3\2\2\2W\u01e2\3\2\2\2Y\u01eb\3\2\2\2[\u01ed\3\2\2\2]\u01f2"+
		"\3\2\2\2_\u01f8\3\2\2\2a\u01ff\3\2\2\2c\u0207\3\2\2\2e\u020c\3\2\2\2g"+
		"\u0212\3\2\2\2i\u0216\3\2\2\2k\u021e\3\2\2\2m\u0222\3\2\2\2o\u0225\3\2"+
		"\2\2q\u0228\3\2\2\2s\u022d\3\2\2\2u\u0237\3\2\2\2w\u023c\3\2\2\2y\u0242"+
		"\3\2\2\2{\u0244\3\2\2\2}\u024f\3\2\2\2\177\u025a\3\2\2\2\u0081\u0266\3"+
		"\2\2\2\u0083\u026c\3\2\2\2\u0085\u026e\3\2\2\2\u0087\u0270\3\2\2\2\u0089"+
		"\u0272\3\2\2\2\u008b\u0274\3\2\2\2\u008d\u0276\3\2\2\2\u008f\u0278\3\2"+
		"\2\2\u0091\u027a\3\2\2\2\u0093\u027c\3\2\2\2\u0095\u027e\3\2\2\2\u0097"+
		"\u0280\3\2\2\2\u0099\u0282\3\2\2\2\u009b\u0284\3\2\2\2\u009d\u009e\t\2"+
		"\2\2\u009e\u009f\t\3\2\2\u009f\u00a0\t\4\2\2\u00a0\u00a1\t\5\2\2\u00a1"+
		"\u00a2\t\6\2\2\u00a2\u00a3\t\4\2\2\u00a3\4\3\2\2\2\u00a4\u00a5\t\7\2\2"+
		"\u00a5\u00a6\t\4\2\2\u00a6\u00a7\t\b\2\2\u00a7\u00a8\t\4\2\2\u00a8\u00a9"+
		"\t\6\2\2\u00a9\u00aa\t\4\2\2\u00aa\6\3\2\2\2\u00ab\u00ac\t\t\2\2\u00ac"+
		"\u00ad\t\3\2\2\u00ad\u00ae\t\4\2\2\u00ae\u00af\t\5\2\2\u00af\u00b0\t\n"+
		"\2\2\u00b0\b\3\2\2\2\u00b1\u00b2\t\2\2\2\u00b2\u00b3\t\13\2\2\u00b3\u00b4"+
		"\t\f\2\2\u00b4\u00b5\t\6\2\2\u00b5\u00b6\t\r\2\2\u00b6\u00b7\t\f\2\2\u00b7"+
		"\u00b8\t\16\2\2\u00b8\u00b9\t\4\2\2\u00b9\n\3\2\2\2\u00ba\u00bb\t\17\2"+
		"\2\u00bb\u00bc\t\13\2\2\u00bc\u00bd\t\b\2\2\u00bd\u00be\t\r\2\2\u00be"+
		"\u00bf\t\2\2\2\u00bf\u00c0\t\20\2\2\u00c0\u00c1\t\21\2\2\u00c1\u00c2\t"+
		"\4\2\2\u00c2\u00c3\t\b\2\2\u00c3\u00c4\t\4\2\2\u00c4\u00c5\t\22\2\2\u00c5"+
		"\u00c6\t\4\2\2\u00c6\u00c7\t\f\2\2\u00c7\u00c8\t\6\2\2\u00c8\f\3\2\2\2"+
		"\u00c9\u00ca\t\3\2\2\u00ca\u00cb\t\16\2\2\u00cb\u00cc\t\b\2\2\u00cc\u00cd"+
		"\t\4\2\2\u00cd\16\3\2\2\2\u00ce\u00cf\t\23\2\2\u00cf\u00d0\t\24\2\2\u00d0"+
		"\u00d1\t\4\2\2\u00d1\u00d2\t\f\2\2\u00d2\20\3\2\2\2\u00d3\u00d4\t\17\2"+
		"\2\u00d4\u00d5\t\4\2\2\u00d5\u00d6\t\3\2\2\u00d6\u00d7\t\25\2\2\u00d7"+
		"\u00d8\t\13\2\2\u00d8\u00d9\t\3\2\2\u00d9\u00da\t\22\2\2\u00da\u00db\t"+
		"\26\2\2\u00db\22\3\2\2\2\u00dc\u00dd\t\5\2\2\u00dd\u00de\t\26\2\2\u00de"+
		"\24\3\2\2\2\u00df\u00e0\t\13\2\2\u00e0\u00e1\t\f\2\2\u00e1\26\3\2\2\2"+
		"\u00e2\u00e3\t\7\2\2\u00e3\u00e4\t\13\2\2\u00e4\30\3\2\2\2\u00e5\u00e6"+
		"\5O(\2\u00e6\u00e7\t\21\2\2\u00e7\u00e8\5K&\2\u00e8\32\3\2\2\2\u00e9\u00ea"+
		"\5K&\2\u00ea\u00eb\t\26\2\2\u00eb\34\3\2\2\2\u00ec\u00ed\5\31\r\2\u00ed"+
		"\u00ee\t\21\2\2\u00ee\u00ef\t\23\2\2\u00ef\u00f0\t\r\2\2\u00f0\u00f1\t"+
		"\6\2\2\u00f1\u00f2\t\24\2\2\u00f2\u00f3\t\21\2\2\u00f3\u00f4\5M\'\2\u00f4"+
		"\36\3\2\2\2\u00f5\u00f6\t\17\2\2\u00f6\u00f7\t\3\2\2\u00f7\u00f8\t\13"+
		"\2\2\u00f8\u00f9\t\2\2\2\u00f9\u00fa\t\4\2\2\u00fa\u00fb\t\26\2\2\u00fb"+
		"\u00fc\t\26\2\2\u00fc \3\2\2\2\u00fd\u00fe\t\r\2\2\u00fe\u00ff\t\f\2\2"+
		"\u00ff\u0100\t\6\2\2\u0100\u0101\t\4\2\2\u0101\u0102\t\3\2\2\u0102\u0103"+
		"\t\26\2\2\u0103\u0104\t\4\2\2\u0104\u0105\t\2\2\2\u0105\u0106\t\6\2\2"+
		"\u0106\u0107\t\r\2\2\u0107\u0108\t\13\2\2\u0108\u0109\t\f\2\2\u0109\""+
		"\3\2\2\2\u010a\u010b\t\16\2\2\u010b\u010c\t\f\2\2\u010c\u010d\t\r\2\2"+
		"\u010d\u010e\t\13\2\2\u010e\u010f\t\f\2\2\u010f$\3\2\2\2\u0110\u0111\t"+
		"\26\2\2\u0111\u0112\t\4\2\2\u0112\u0113\t\6\2\2\u0113\u0114\t\21\2\2\u0114"+
		"\u0115\t\3\2\2\u0115\u0116\t\4\2\2\u0116\u0117\t\26\2\2\u0117\u0118\t"+
		"\13\2\2\u0118\u0119\t\16\2\2\u0119\u011a\t\3\2\2\u011a\u011b\t\2\2\2\u011b"+
		"\u011c\t\4\2\2\u011c\u011d\t\21\2\2\u011d\u011e\5A!\2\u011e&\3\2\2\2\u011f"+
		"\u0120\t\5\2\2\u0120\u0121\t\26\2\2\u0121\u0122\t\26\2\2\u0122\u0123\t"+
		"\r\2\2\u0123\u0124\t\27\2\2\u0124\u0125\t\f\2\2\u0125(\3\2\2\2\u0126\u0127"+
		"\t\7\2\2\u0127\u0128\t\4\2\2\u0128\u0129\t\5\2\2\u0129\u012a\t\26\2\2"+
		"\u012a\u012b\t\26\2\2\u012b\u012c\t\r\2\2\u012c\u012d\t\27\2\2\u012d\u012e"+
		"\t\f\2\2\u012e*\3\2\2\2\u012f\u0130\t\25\2\2\u0130\u0131\t\3\2\2\u0131"+
		"\u0132\t\13\2\2\u0132\u0133\t\22\2\2\u0133,\3\2\2\2\u0134\u0135\t\26\2"+
		"\2\u0135\u0136\t\4\2\2\u0136\u0137\t\6\2\2\u0137\u0138\t\21\2\2\u0138"+
		"\u0139\t\17\2\2\u0139\u013a\t\3\2\2\u013a\u013b\t\13\2\2\u013b\u013c\t"+
		"\17\2\2\u013c\u013d\t\4\2\2\u013d\u013e\t\3\2\2\u013e\u013f\t\6\2\2\u013f"+
		"\u0140\t\r\2\2\u0140\u0141\t\4\2\2\u0141\u0142\t\26\2\2\u0142.\3\2\2\2"+
		"\u0143\u0144\t\13\2\2\u0144\u0145\t\25\2\2\u0145\60\3\2\2\2\u0146\u0147"+
		"\t\6\2\2\u0147\u0148\t\13\2\2\u0148\62\3\2\2\2\u0149\u014a\t\5\2\2\u014a"+
		"\u014b\t\26\2\2\u014b\u014c\t\26\2\2\u014c\u014d\t\13\2\2\u014d\u014e"+
		"\t\2\2\2\u014e\u014f\t\r\2\2\u014f\u0150\t\5\2\2\u0150\u0151\t\6\2\2\u0151"+
		"\u0152\t\4\2\2\u0152\64\3\2\2\2\u0153\u0154\t\5\2\2\u0154\u0155\t\f\2"+
		"\2\u0155\u0156\t\7\2\2\u0156\66\3\2\2\2\u0157\u0158\t\23\2\2\u0158\u0159"+
		"\t\r\2\2\u0159\u015a\t\6\2\2\u015a\u015b\t\24\2\2\u015b8\3\2\2\2\u015c"+
		"\u015d\t\7\2\2\u015d\u015e\t\r\2\2\u015e\u015f\t\26\2\2\u015f\u0160\t"+
		"\26\2\2\u0160\u0161\t\13\2\2\u0161\u0162\t\2\2\2\u0162\u0163\t\r\2\2\u0163"+
		"\u0164\t\5\2\2\u0164\u0165\t\6\2\2\u0165\u0166\t\4\2\2\u0166:\3\2\2\2"+
		"\u0167\u0168\t\7\2\2\u0168\u0169\t\4\2\2\u0169\u016a\t\f\2\2\u016a\u016b"+
		"\t\20\2\2\u016b<\3\2\2\2\u016c\u016d\t\17\2\2\u016d\u016e\t\3\2\2\u016e"+
		"\u016f\t\13\2\2\u016f\u0170\t\24\2\2\u0170\u0171\t\r\2\2\u0171\u0172\t"+
		"\t\2\2\u0172\u0173\t\r\2\2\u0173\u0174\t\6\2\2\u0174\u0175\t\r\2\2\u0175"+
		"\u0176\t\13\2\2\u0176\u0177\t\f\2\2\u0177>\3\2\2\2\u0178\u0179\t\13\2"+
		"\2\u0179\u017a\t\t\2\2\u017a\u017b\t\b\2\2\u017b\u017c\t\r\2\2\u017c\u017d"+
		"\t\27\2\2\u017d\u017e\t\5\2\2\u017e\u017f\t\6\2\2\u017f\u0180\t\r\2\2"+
		"\u0180\u0181\t\13\2\2\u0181\u0182\t\f\2\2\u0182@\3\2\2\2\u0183\u0184\t"+
		"\5\2\2\u0184\u0185\t\2\2\2\u0185\u0186\t\2\2\2\u0186\u0187\t\4\2\2\u0187"+
		"\u0188\t\26\2\2\u0188\u0189\t\26\2\2\u0189\u018a\t\21\2\2\u018a\u018b"+
		"\t\3\2\2\u018b\u018c\t\r\2\2\u018c\u018d\t\27\2\2\u018d\u018e\t\24\2\2"+
		"\u018e\u018f\t\6\2\2\u018f\u0190\t\26\2\2\u0190B\3\2\2\2\u0191\u0192\t"+
		"\17\2\2\u0192\u0193\t\13\2\2\u0193\u0194\t\b\2\2\u0194\u0195\t\r\2\2\u0195"+
		"\u0196\t\2\2\2\u0196\u0197\t\20\2\2\u0197\u0198\t\21\2\2\u0198\u0199\t"+
		"\2\2\2\u0199\u019a\t\b\2\2\u019a\u019b\t\5\2\2\u019b\u019c\t\26\2\2\u019c"+
		"\u01a0\t\26\2\2\u019d\u019e\t\17\2\2\u019e\u01a0\t\2\2\2\u019f\u0191\3"+
		"\2\2\2\u019f\u019d\3\2\2\2\u01a0D\3\2\2\2\u01a1\u01a2\5I%\2\u01a2\u01a3"+
		"\t\21\2\2\u01a3\u01a4\5M\'\2\u01a4\u01a8\3\2\2\2\u01a5\u01a6\t\13\2\2"+
		"\u01a6\u01a8\t\5\2\2\u01a7\u01a1\3\2\2\2\u01a7\u01a5\3\2\2\2\u01a8F\3"+
		"\2\2\2\u01a9\u01aa\5K&\2\u01aa\u01ab\t\21\2\2\u01ab\u01ac\5M\'\2\u01ac"+
		"\u01b0\3\2\2\2\u01ad\u01ae\t\16\2\2\u01ae\u01b0\t\5\2\2\u01af\u01a9\3"+
		"\2\2\2\u01af\u01ad\3\2\2\2\u01b0H\3\2\2\2\u01b1\u01b2\t\13\2\2\u01b2\u01b3"+
		"\t\t\2\2\u01b3\u01b4\t\30\2\2\u01b4\u01b5\t\4\2\2\u01b5\u01b6\t\2\2\2"+
		"\u01b6\u01b9\t\6\2\2\u01b7\u01b9\t\13\2\2\u01b8\u01b1\3\2\2\2\u01b8\u01b7"+
		"\3\2\2\2\u01b9J\3\2\2\2\u01ba\u01bb\t\16\2\2\u01bb\u01bc\t\26\2\2\u01bc"+
		"\u01bd\t\4\2\2\u01bd\u01c0\t\3\2\2\u01be\u01c0\t\16\2\2\u01bf\u01ba\3"+
		"\2\2\2\u01bf\u01be\3\2\2\2\u01c0L\3\2\2\2\u01c1\u01c2\t\5\2\2\u01c2\u01c3"+
		"\t\6\2\2\u01c3\u01c4\t\6\2\2\u01c4\u01c5\t\3\2\2\u01c5\u01c6\t\r\2\2\u01c6"+
		"\u01c7\t\t\2\2\u01c7\u01c8\t\16\2\2\u01c8\u01c9\t\6\2\2\u01c9\u01ca\t"+
		"\4\2\2\u01caN\3\2\2\2\u01cb\u01cc\t\5\2\2\u01cc\u01cd\t\f\2\2\u01cd\u01ce"+
		"\t\20\2\2\u01ceP\3\2\2\2\u01cf\u01d0\t\b\2\2\u01d0\u01d1\t\4\2\2\u01d1"+
		"\u01d2\t\6\2\2\u01d2R\3\2\2\2\u01d3\u01d4\t\2\2\2\u01d4\u01d5\t\13\2\2"+
		"\u01d5\u01d6\t\f\2\2\u01d6\u01d7\t\26\2\2\u01d7\u01d8\t\6\2\2\u01d8T\3"+
		"\2\2\2\u01d9\u01da\t\25\2\2\u01da\u01db\t\16\2\2\u01db\u01dc\t\f\2\2\u01dc"+
		"\u01dd\t\2\2\2\u01dd\u01de\t\6\2\2\u01de\u01df\t\r\2\2\u01df\u01e0\t\13"+
		"\2\2\u01e0\u01e1\t\f\2\2\u01e1V\3\2\2\2\u01e2\u01e3\t\3\2\2\u01e3\u01e4"+
		"\t\4\2\2\u01e4\u01e5\t\6\2\2\u01e5\u01e6\t\16\2\2\u01e6\u01e7\t\3\2\2"+
		"\u01e7\u01e8\t\f\2\2\u01e8X\3\2\2\2\u01e9\u01ec\5[.\2\u01ea\u01ec\5]/"+
		"\2\u01eb\u01e9\3\2\2\2\u01eb\u01ea\3\2\2\2\u01ecZ\3\2\2\2\u01ed\u01ee"+
		"\t\6\2\2\u01ee\u01ef\t\3\2\2\u01ef\u01f0\t\16\2\2\u01f0\u01f1\t\4\2\2"+
		"\u01f1\\\3\2\2\2\u01f2\u01f3\t\25\2\2\u01f3\u01f4\t\5\2\2\u01f4\u01f5"+
		"\t\b\2\2\u01f5\u01f6\t\26\2\2\u01f6\u01f7\t\4\2\2\u01f7^\3\2\2\2\u01f8"+
		"\u01f9\t\26\2\2\u01f9\u01fa\t\6\2\2\u01fa\u01fb\t\3\2\2\u01fb\u01fc\t"+
		"\r\2\2\u01fc\u01fd\t\f\2\2\u01fd\u01fe\t\27\2\2\u01fe`\3\2\2\2\u01ff\u0200"+
		"\t\t\2\2\u0200\u0201\t\13\2\2\u0201\u0202\t\13\2\2\u0202\u0203\t\b\2\2"+
		"\u0203\u0204\t\4\2\2\u0204\u0205\t\5\2\2\u0205\u0206\t\f\2\2\u0206b\3"+
		"\2\2\2\u0207\u0208\t\31\2\2\u0208\u0209\t\13\2\2\u0209\u020a\t\r\2\2\u020a"+
		"\u020b\t\7\2\2\u020bd\3\2\2\2\u020c\u020d\t\5\2\2\u020d\u020e\t\3\2\2"+
		"\u020e\u020f\t\3\2\2\u020f\u0210\t\5\2\2\u0210\u0211\t\20\2\2\u0211f\3"+
		"\2\2\2\u0212\u0213\t\22\2\2\u0213\u0214\t\5\2\2\u0214\u0215\t\17\2\2\u0215"+
		"h\3\2\2\2\u0216\u0217\t\25\2\2\u0217\u0218\t\13\2\2\u0218\u0219\t\3\2"+
		"\2\u0219\u021a\t\4\2\2\u021a\u021b\t\5\2\2\u021b\u021c\t\2\2\2\u021c\u021d"+
		"\t\24\2\2\u021dj\3\2\2\2\u021e\u021f\t\25\2\2\u021f\u0220\t\13\2\2\u0220"+
		"\u0221\t\3\2\2\u0221l\3\2\2\2\u0222\u0223\t\r\2\2\u0223\u0224\t\f\2\2"+
		"\u0224n\3\2\2\2\u0225\u0226\t\r\2\2\u0226\u0227\t\25\2\2\u0227p\3\2\2"+
		"\2\u0228\u0229\t\4\2\2\u0229\u022a\t\b\2\2\u022a\u022b\t\26\2\2\u022b"+
		"\u022c\t\4\2\2\u022cr\3\2\2\2\u022d\u022e\t\r\2\2\u022e\u022f\t\f\2\2"+
		"\u022f\u0230\t\21\2\2\u0230\u0231\t\3\2\2\u0231\u0232\t\5\2\2\u0232\u0233"+
		"\t\f\2\2\u0233\u0234\t\27\2\2\u0234\u0235\t\4\2\2\u0235t\3\2\2\2\u0236"+
		"\u0238\t\32\2\2\u0237\u0236\3\2\2\2\u0238\u0239\3\2\2\2\u0239\u0237\3"+
		"\2\2\2\u0239\u023a\3\2\2\2\u023av\3\2\2\2\u023b\u023d\t\33\2\2\u023c\u023b"+
		"\3\2\2\2\u023d\u023e\3\2\2\2\u023e\u023c\3\2\2\2\u023e\u023f\3\2\2\2\u023f"+
		"x\3\2\2\2\u0240\u0243\5{>\2\u0241\u0243\5}?\2\u0242\u0240\3\2\2\2\u0242"+
		"\u0241\3\2\2\2\u0243z\3\2\2\2\u0244\u024a\7$\2\2\u0245\u0246\7^\2\2\u0246"+
		"\u0249\7$\2\2\u0247\u0249\n\34\2\2\u0248\u0245\3\2\2\2\u0248\u0247\3\2"+
		"\2\2\u0249\u024c\3\2\2\2\u024a\u024b\3\2\2\2\u024a\u0248\3\2\2\2\u024b"+
		"\u024d\3\2\2\2\u024c\u024a\3\2\2\2\u024d\u024e\7$\2\2\u024e|\3\2\2\2\u024f"+
		"\u0255\7)\2\2\u0250\u0251\7^\2\2\u0251\u0254\7)\2\2\u0252\u0254\n\34\2"+
		"\2\u0253\u0250\3\2\2\2\u0253\u0252\3\2\2\2\u0254\u0257\3\2\2\2\u0255\u0256"+
		"\3\2\2\2\u0255\u0253\3\2\2\2\u0256\u0258\3\2\2\2\u0257\u0255\3\2\2\2\u0258"+
		"\u0259\7)\2\2\u0259~\3\2\2\2\u025a\u025e\7%\2\2\u025b\u025d\n\35\2\2\u025c"+
		"\u025b\3\2\2\2\u025d\u0260\3\2\2\2\u025e\u025c\3\2\2\2\u025e\u025f\3\2"+
		"\2\2\u025f\u0261\3\2\2\2\u0260\u025e\3\2\2\2\u0261\u0262\7\f\2\2\u0262"+
		"\u0263\3\2\2\2\u0263\u0264\b@\2\2\u0264\u0080\3\2\2\2\u0265\u0267\t\36"+
		"\2\2\u0266\u0265\3\2\2\2\u0267\u0268\3\2\2\2\u0268\u0266\3\2\2\2\u0268"+
		"\u0269\3\2\2\2\u0269\u026a\3\2\2\2\u026a\u026b\bA\3\2\u026b\u0082\3\2"+
		"\2\2\u026c\u026d\7.\2\2\u026d\u0084\3\2\2\2\u026e\u026f\7<\2\2\u026f\u0086"+
		"\3\2\2\2\u0270\u0271\7=\2\2\u0271\u0088\3\2\2\2\u0272\u0273\7}\2\2\u0273"+
		"\u008a\3\2\2\2\u0274\u0275\7\177\2\2\u0275\u008c\3\2\2\2\u0276\u0277\7"+
		"]\2\2\u0277\u008e\3\2\2\2\u0278\u0279\7_\2\2\u0279\u0090\3\2\2\2\u027a"+
		"\u027b\7>\2\2\u027b\u0092\3\2\2\2\u027c\u027d\7@\2\2\u027d\u0094\3\2\2"+
		"\2\u027e\u027f\7*\2\2\u027f\u0096\3\2\2\2\u0280\u0281\7+\2\2\u0281\u0098"+
		"\3\2\2\2\u0282\u0283\7#\2\2\u0283\u009a\3\2\2\2\u0284\u0285\7?\2\2\u0285"+
		"\u009c\3\2\2\2\22\2\u019f\u01a7\u01af\u01b8\u01bf\u01eb\u0239\u023e\u0242"+
		"\u0248\u024a\u0253\u0255\u025e\u0268\4\2\3\2\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}