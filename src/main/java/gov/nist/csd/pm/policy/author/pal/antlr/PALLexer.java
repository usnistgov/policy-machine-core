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
		ALL_ACCESS_RIGHTS=1, ALL_RESOURCE_ACCESS_RIGHTS=2, ALL_ADMIN_ACCESS_RIGHTS=3, 
		CREATE=4, DELETE=5, BREAK=6, CONTINUE=7, POLICY_ELEMENT=8, RULE=9, WHEN=10, 
		PERFORMS=11, AS=12, ON=13, DO=14, ANY_USER=15, USERS=16, ANY_USER_WITH_ATTRIBUTE=17, 
		PROCESS=18, INTERSECTION=19, UNION=20, SET_RESOURCE_ACCESS_RIGHTS=21, 
		ASSIGN=22, DEASSIGN=23, FROM=24, SET_PROPERTIES=25, OF=26, TO=27, ASSOCIATE=28, 
		AND=29, WITH=30, DISSOCIATE=31, DENY=32, PROHIBITION=33, OBLIGATION=34, 
		ACCESS_RIGHTS=35, POLICY_CLASS=36, OBJECT_ATTRIBUTE=37, USER_ATTRIBUTE=38, 
		OBJECT=39, USER=40, ATTR=41, ANY=42, LET=43, CONST=44, FUNCTION=45, RETURN=46, 
		BOOLEAN=47, TRUE=48, FALSE=49, STRING_TYPE=50, BOOLEAN_TYPE=51, VOID_TYPE=52, 
		ARRAY_TYPE=53, MAP_TYPE=54, FOREACH=55, FOR=56, IN=57, IF=58, ELSE=59, 
		IN_RANGE=60, NUMBER=61, VARIABLE_OR_FUNCTION_NAME=62, STRING=63, DOUBLE_QUOTE_STRING=64, 
		SINGLE_QUOTE_STRING=65, LINE_COMMENT=66, WS=67, COMMA=68, COLON=69, SEMI_COLON=70, 
		OPEN_CURLY=71, CLOSE_CURLY=72, OPEN_BRACKET=73, CLOSE_BRACKET=74, OPEN_ANGLE_BRACKET=75, 
		CLOSE_ANGLE_BRACKET=76, OPEN_PAREN=77, CLOSE_PAREN=78, IS_COMPLEMENT=79, 
		EQUALS=80;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"ALL_ACCESS_RIGHTS", "ALL_RESOURCE_ACCESS_RIGHTS", "ALL_ADMIN_ACCESS_RIGHTS", 
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
			null, "'*'", "'*r'", "'*a'", null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, "','", "':'", "';'", 
			"'{'", "'}'", "'['", "']'", "'<'", "'>'", "'('", "')'", "'!'", "'='"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "ALL_ACCESS_RIGHTS", "ALL_RESOURCE_ACCESS_RIGHTS", "ALL_ADMIN_ACCESS_RIGHTS", 
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2R\u0294\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
		"\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\4P\tP\4Q\tQ\3\2\3\2\3\3\3\3\3"+
		"\3\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6"+
		"\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3"+
		"\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n"+
		"\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r"+
		"\3\r\3\16\3\16\3\16\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\21\3\21\3\21"+
		"\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23"+
		"\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24"+
		"\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3\26"+
		"\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27"+
		"\3\27\3\27\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\31"+
		"\3\31\3\31\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32"+
		"\3\32\3\32\3\32\3\33\3\33\3\33\3\34\3\34\3\34\3\35\3\35\3\35\3\35\3\35"+
		"\3\35\3\35\3\35\3\35\3\35\3\36\3\36\3\36\3\36\3\37\3\37\3\37\3\37\3\37"+
		"\3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3!\3!\3!\3!\3!\3\"\3\"\3\"\3\"\3\"\3"+
		"\"\3\"\3\"\3\"\3\"\3\"\3\"\3#\3#\3#\3#\3#\3#\3#\3#\3#\3#\3#\3$\3$\3$\3"+
		"$\3$\3$\3$\3$\3$\3$\3$\3$\3$\3$\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3"+
		"%\3%\5%\u01ae\n%\3&\3&\3&\3&\3&\3&\5&\u01b6\n&\3\'\3\'\3\'\3\'\3\'\3\'"+
		"\5\'\u01be\n\'\3(\3(\3(\3(\3(\3(\3(\5(\u01c7\n(\3)\3)\3)\3)\3)\5)\u01ce"+
		"\n)\3*\3*\3*\3*\3*\3*\3*\3*\3*\3*\3+\3+\3+\3+\3,\3,\3,\3,\3-\3-\3-\3-"+
		"\3-\3-\3.\3.\3.\3.\3.\3.\3.\3.\3.\3/\3/\3/\3/\3/\3/\3/\3\60\3\60\5\60"+
		"\u01fa\n\60\3\61\3\61\3\61\3\61\3\61\3\62\3\62\3\62\3\62\3\62\3\62\3\63"+
		"\3\63\3\63\3\63\3\63\3\63\3\63\3\64\3\64\3\64\3\64\3\64\3\64\3\64\3\64"+
		"\3\65\3\65\3\65\3\65\3\65\3\66\3\66\3\66\3\66\3\66\3\66\3\67\3\67\3\67"+
		"\3\67\38\38\38\38\38\38\38\38\39\39\39\39\3:\3:\3:\3;\3;\3;\3<\3<\3<\3"+
		"<\3<\3=\3=\3=\3=\3=\3=\3=\3=\3=\3>\6>\u0246\n>\r>\16>\u0247\3?\6?\u024b"+
		"\n?\r?\16?\u024c\3@\3@\5@\u0251\n@\3A\3A\3A\3A\7A\u0257\nA\fA\16A\u025a"+
		"\13A\3A\3A\3B\3B\3B\3B\7B\u0262\nB\fB\16B\u0265\13B\3B\3B\3C\3C\7C\u026b"+
		"\nC\fC\16C\u026e\13C\3C\3C\3C\3C\3D\6D\u0275\nD\rD\16D\u0276\3D\3D\3E"+
		"\3E\3F\3F\3G\3G\3H\3H\3I\3I\3J\3J\3K\3K\3L\3L\3M\3M\3N\3N\3O\3O\3P\3P"+
		"\3Q\3Q\4\u0258\u0263\2R\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f"+
		"\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63"+
		"\33\65\34\67\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-Y.[/]\60_\61a\62"+
		"c\63e\64g\65i\66k\67m8o9q:s;u<w=y>{?}@\177A\u0081B\u0083C\u0085D\u0087"+
		"E\u0089F\u008bG\u008dH\u008fI\u0091J\u0093K\u0095L\u0097M\u0099N\u009b"+
		"O\u009dP\u009fQ\u00a1R\3\2\37\3\2ee\3\2tt\3\2gg\3\2cc\3\2vv\3\2ff\3\2"+
		"nn\3\2dd\3\2mm\3\2qq\3\2pp\3\2kk\3\2ww\3\2rr\3\2{{\3\2\"\"\3\2oo\3\2y"+
		"y\3\2jj\3\2hh\3\2uu\3\2ii\3\2ll\3\2xx\3\2\62;\6\2\62;C\\aac|\4\2\f\f\17"+
		"\17\3\2\f\f\5\2\13\f\17\17\"\"\2\u02a2\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2"+
		"\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2"+
		"\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3"+
		"\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3"+
		"\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65"+
		"\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3"+
		"\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2"+
		"\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2"+
		"[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3"+
		"\2\2\2\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2\2\2q\3\2\2\2\2s\3\2\2"+
		"\2\2u\3\2\2\2\2w\3\2\2\2\2y\3\2\2\2\2{\3\2\2\2\2}\3\2\2\2\2\177\3\2\2"+
		"\2\2\u0081\3\2\2\2\2\u0083\3\2\2\2\2\u0085\3\2\2\2\2\u0087\3\2\2\2\2\u0089"+
		"\3\2\2\2\2\u008b\3\2\2\2\2\u008d\3\2\2\2\2\u008f\3\2\2\2\2\u0091\3\2\2"+
		"\2\2\u0093\3\2\2\2\2\u0095\3\2\2\2\2\u0097\3\2\2\2\2\u0099\3\2\2\2\2\u009b"+
		"\3\2\2\2\2\u009d\3\2\2\2\2\u009f\3\2\2\2\2\u00a1\3\2\2\2\3\u00a3\3\2\2"+
		"\2\5\u00a5\3\2\2\2\7\u00a8\3\2\2\2\t\u00ab\3\2\2\2\13\u00b2\3\2\2\2\r"+
		"\u00b9\3\2\2\2\17\u00bf\3\2\2\2\21\u00c8\3\2\2\2\23\u00d7\3\2\2\2\25\u00dc"+
		"\3\2\2\2\27\u00e1\3\2\2\2\31\u00ea\3\2\2\2\33\u00ed\3\2\2\2\35\u00f0\3"+
		"\2\2\2\37\u00f3\3\2\2\2!\u00f7\3\2\2\2#\u00fa\3\2\2\2%\u0103\3\2\2\2\'"+
		"\u010b\3\2\2\2)\u0118\3\2\2\2+\u011e\3\2\2\2-\u012d\3\2\2\2/\u0134\3\2"+
		"\2\2\61\u013d\3\2\2\2\63\u0142\3\2\2\2\65\u0151\3\2\2\2\67\u0154\3\2\2"+
		"\29\u0157\3\2\2\2;\u0161\3\2\2\2=\u0165\3\2\2\2?\u016a\3\2\2\2A\u0175"+
		"\3\2\2\2C\u017a\3\2\2\2E\u0186\3\2\2\2G\u0191\3\2\2\2I\u01ad\3\2\2\2K"+
		"\u01b5\3\2\2\2M\u01bd\3\2\2\2O\u01c6\3\2\2\2Q\u01cd\3\2\2\2S\u01cf\3\2"+
		"\2\2U\u01d9\3\2\2\2W\u01dd\3\2\2\2Y\u01e1\3\2\2\2[\u01e7\3\2\2\2]\u01f0"+
		"\3\2\2\2_\u01f9\3\2\2\2a\u01fb\3\2\2\2c\u0200\3\2\2\2e\u0206\3\2\2\2g"+
		"\u020d\3\2\2\2i\u0215\3\2\2\2k\u021a\3\2\2\2m\u0220\3\2\2\2o\u0224\3\2"+
		"\2\2q\u022c\3\2\2\2s\u0230\3\2\2\2u\u0233\3\2\2\2w\u0236\3\2\2\2y\u023b"+
		"\3\2\2\2{\u0245\3\2\2\2}\u024a\3\2\2\2\177\u0250\3\2\2\2\u0081\u0252\3"+
		"\2\2\2\u0083\u025d\3\2\2\2\u0085\u0268\3\2\2\2\u0087\u0274\3\2\2\2\u0089"+
		"\u027a\3\2\2\2\u008b\u027c\3\2\2\2\u008d\u027e\3\2\2\2\u008f\u0280\3\2"+
		"\2\2\u0091\u0282\3\2\2\2\u0093\u0284\3\2\2\2\u0095\u0286\3\2\2\2\u0097"+
		"\u0288\3\2\2\2\u0099\u028a\3\2\2\2\u009b\u028c\3\2\2\2\u009d\u028e\3\2"+
		"\2\2\u009f\u0290\3\2\2\2\u00a1\u0292\3\2\2\2\u00a3\u00a4\7,\2\2\u00a4"+
		"\4\3\2\2\2\u00a5\u00a6\7,\2\2\u00a6\u00a7\7t\2\2\u00a7\6\3\2\2\2\u00a8"+
		"\u00a9\7,\2\2\u00a9\u00aa\7c\2\2\u00aa\b\3\2\2\2\u00ab\u00ac\t\2\2\2\u00ac"+
		"\u00ad\t\3\2\2\u00ad\u00ae\t\4\2\2\u00ae\u00af\t\5\2\2\u00af\u00b0\t\6"+
		"\2\2\u00b0\u00b1\t\4\2\2\u00b1\n\3\2\2\2\u00b2\u00b3\t\7\2\2\u00b3\u00b4"+
		"\t\4\2\2\u00b4\u00b5\t\b\2\2\u00b5\u00b6\t\4\2\2\u00b6\u00b7\t\6\2\2\u00b7"+
		"\u00b8\t\4\2\2\u00b8\f\3\2\2\2\u00b9\u00ba\t\t\2\2\u00ba\u00bb\t\3\2\2"+
		"\u00bb\u00bc\t\4\2\2\u00bc\u00bd\t\5\2\2\u00bd\u00be\t\n\2\2\u00be\16"+
		"\3\2\2\2\u00bf\u00c0\t\2\2\2\u00c0\u00c1\t\13\2\2\u00c1\u00c2\t\f\2\2"+
		"\u00c2\u00c3\t\6\2\2\u00c3\u00c4\t\r\2\2\u00c4\u00c5\t\f\2\2\u00c5\u00c6"+
		"\t\16\2\2\u00c6\u00c7\t\4\2\2\u00c7\20\3\2\2\2\u00c8\u00c9\t\17\2\2\u00c9"+
		"\u00ca\t\13\2\2\u00ca\u00cb\t\b\2\2\u00cb\u00cc\t\r\2\2\u00cc\u00cd\t"+
		"\2\2\2\u00cd\u00ce\t\20\2\2\u00ce\u00cf\t\21\2\2\u00cf\u00d0\t\4\2\2\u00d0"+
		"\u00d1\t\b\2\2\u00d1\u00d2\t\4\2\2\u00d2\u00d3\t\22\2\2\u00d3\u00d4\t"+
		"\4\2\2\u00d4\u00d5\t\f\2\2\u00d5\u00d6\t\6\2\2\u00d6\22\3\2\2\2\u00d7"+
		"\u00d8\t\3\2\2\u00d8\u00d9\t\16\2\2\u00d9\u00da\t\b\2\2\u00da\u00db\t"+
		"\4\2\2\u00db\24\3\2\2\2\u00dc\u00dd\t\23\2\2\u00dd\u00de\t\24\2\2\u00de"+
		"\u00df\t\4\2\2\u00df\u00e0\t\f\2\2\u00e0\26\3\2\2\2\u00e1\u00e2\t\17\2"+
		"\2\u00e2\u00e3\t\4\2\2\u00e3\u00e4\t\3\2\2\u00e4\u00e5\t\25\2\2\u00e5"+
		"\u00e6\t\13\2\2\u00e6\u00e7\t\3\2\2\u00e7\u00e8\t\22\2\2\u00e8\u00e9\t"+
		"\26\2\2\u00e9\30\3\2\2\2\u00ea\u00eb\t\5\2\2\u00eb\u00ec\t\26\2\2\u00ec"+
		"\32\3\2\2\2\u00ed\u00ee\t\13\2\2\u00ee\u00ef\t\f\2\2\u00ef\34\3\2\2\2"+
		"\u00f0\u00f1\t\7\2\2\u00f1\u00f2\t\13\2\2\u00f2\36\3\2\2\2\u00f3\u00f4"+
		"\5U+\2\u00f4\u00f5\t\21\2\2\u00f5\u00f6\5Q)\2\u00f6 \3\2\2\2\u00f7\u00f8"+
		"\5Q)\2\u00f8\u00f9\t\26\2\2\u00f9\"\3\2\2\2\u00fa\u00fb\5\37\20\2\u00fb"+
		"\u00fc\t\21\2\2\u00fc\u00fd\t\23\2\2\u00fd\u00fe\t\r\2\2\u00fe\u00ff\t"+
		"\6\2\2\u00ff\u0100\t\24\2\2\u0100\u0101\t\21\2\2\u0101\u0102\5S*\2\u0102"+
		"$\3\2\2\2\u0103\u0104\t\17\2\2\u0104\u0105\t\3\2\2\u0105\u0106\t\13\2"+
		"\2\u0106\u0107\t\2\2\2\u0107\u0108\t\4\2\2\u0108\u0109\t\26\2\2\u0109"+
		"\u010a\t\26\2\2\u010a&\3\2\2\2\u010b\u010c\t\r\2\2\u010c\u010d\t\f\2\2"+
		"\u010d\u010e\t\6\2\2\u010e\u010f\t\4\2\2\u010f\u0110\t\3\2\2\u0110\u0111"+
		"\t\26\2\2\u0111\u0112\t\4\2\2\u0112\u0113\t\2\2\2\u0113\u0114\t\6\2\2"+
		"\u0114\u0115\t\r\2\2\u0115\u0116\t\13\2\2\u0116\u0117\t\f\2\2\u0117(\3"+
		"\2\2\2\u0118\u0119\t\16\2\2\u0119\u011a\t\f\2\2\u011a\u011b\t\r\2\2\u011b"+
		"\u011c\t\13\2\2\u011c\u011d\t\f\2\2\u011d*\3\2\2\2\u011e\u011f\t\26\2"+
		"\2\u011f\u0120\t\4\2\2\u0120\u0121\t\6\2\2\u0121\u0122\t\21\2\2\u0122"+
		"\u0123\t\3\2\2\u0123\u0124\t\4\2\2\u0124\u0125\t\26\2\2\u0125\u0126\t"+
		"\13\2\2\u0126\u0127\t\16\2\2\u0127\u0128\t\3\2\2\u0128\u0129\t\2\2\2\u0129"+
		"\u012a\t\4\2\2\u012a\u012b\t\21\2\2\u012b\u012c\5G$\2\u012c,\3\2\2\2\u012d"+
		"\u012e\t\5\2\2\u012e\u012f\t\26\2\2\u012f\u0130\t\26\2\2\u0130\u0131\t"+
		"\r\2\2\u0131\u0132\t\27\2\2\u0132\u0133\t\f\2\2\u0133.\3\2\2\2\u0134\u0135"+
		"\t\7\2\2\u0135\u0136\t\4\2\2\u0136\u0137\t\5\2\2\u0137\u0138\t\26\2\2"+
		"\u0138\u0139\t\26\2\2\u0139\u013a\t\r\2\2\u013a\u013b\t\27\2\2\u013b\u013c"+
		"\t\f\2\2\u013c\60\3\2\2\2\u013d\u013e\t\25\2\2\u013e\u013f\t\3\2\2\u013f"+
		"\u0140\t\13\2\2\u0140\u0141\t\22\2\2\u0141\62\3\2\2\2\u0142\u0143\t\26"+
		"\2\2\u0143\u0144\t\4\2\2\u0144\u0145\t\6\2\2\u0145\u0146\t\21\2\2\u0146"+
		"\u0147\t\17\2\2\u0147\u0148\t\3\2\2\u0148\u0149\t\13\2\2\u0149\u014a\t"+
		"\17\2\2\u014a\u014b\t\4\2\2\u014b\u014c\t\3\2\2\u014c\u014d\t\6\2\2\u014d"+
		"\u014e\t\r\2\2\u014e\u014f\t\4\2\2\u014f\u0150\t\26\2\2\u0150\64\3\2\2"+
		"\2\u0151\u0152\t\13\2\2\u0152\u0153\t\25\2\2\u0153\66\3\2\2\2\u0154\u0155"+
		"\t\6\2\2\u0155\u0156\t\13\2\2\u01568\3\2\2\2\u0157\u0158\t\5\2\2\u0158"+
		"\u0159\t\26\2\2\u0159\u015a\t\26\2\2\u015a\u015b\t\13\2\2\u015b\u015c"+
		"\t\2\2\2\u015c\u015d\t\r\2\2\u015d\u015e\t\5\2\2\u015e\u015f\t\6\2\2\u015f"+
		"\u0160\t\4\2\2\u0160:\3\2\2\2\u0161\u0162\t\5\2\2\u0162\u0163\t\f\2\2"+
		"\u0163\u0164\t\7\2\2\u0164<\3\2\2\2\u0165\u0166\t\23\2\2\u0166\u0167\t"+
		"\r\2\2\u0167\u0168\t\6\2\2\u0168\u0169\t\24\2\2\u0169>\3\2\2\2\u016a\u016b"+
		"\t\7\2\2\u016b\u016c\t\r\2\2\u016c\u016d\t\26\2\2\u016d\u016e\t\26\2\2"+
		"\u016e\u016f\t\13\2\2\u016f\u0170\t\2\2\2\u0170\u0171\t\r\2\2\u0171\u0172"+
		"\t\5\2\2\u0172\u0173\t\6\2\2\u0173\u0174\t\4\2\2\u0174@\3\2\2\2\u0175"+
		"\u0176\t\7\2\2\u0176\u0177\t\4\2\2\u0177\u0178\t\f\2\2\u0178\u0179\t\20"+
		"\2\2\u0179B\3\2\2\2\u017a\u017b\t\17\2\2\u017b\u017c\t\3\2\2\u017c\u017d"+
		"\t\13\2\2\u017d\u017e\t\24\2\2\u017e\u017f\t\r\2\2\u017f\u0180\t\t\2\2"+
		"\u0180\u0181\t\r\2\2\u0181\u0182\t\6\2\2\u0182\u0183\t\r\2\2\u0183\u0184"+
		"\t\13\2\2\u0184\u0185\t\f\2\2\u0185D\3\2\2\2\u0186\u0187\t\13\2\2\u0187"+
		"\u0188\t\t\2\2\u0188\u0189\t\b\2\2\u0189\u018a\t\r\2\2\u018a\u018b\t\27"+
		"\2\2\u018b\u018c\t\5\2\2\u018c\u018d\t\6\2\2\u018d\u018e\t\r\2\2\u018e"+
		"\u018f\t\13\2\2\u018f\u0190\t\f\2\2\u0190F\3\2\2\2\u0191\u0192\t\5\2\2"+
		"\u0192\u0193\t\2\2\2\u0193\u0194\t\2\2\2\u0194\u0195\t\4\2\2\u0195\u0196"+
		"\t\26\2\2\u0196\u0197\t\26\2\2\u0197\u0198\t\21\2\2\u0198\u0199\t\3\2"+
		"\2\u0199\u019a\t\r\2\2\u019a\u019b\t\27\2\2\u019b\u019c\t\24\2\2\u019c"+
		"\u019d\t\6\2\2\u019d\u019e\t\26\2\2\u019eH\3\2\2\2\u019f\u01a0\t\17\2"+
		"\2\u01a0\u01a1\t\13\2\2\u01a1\u01a2\t\b\2\2\u01a2\u01a3\t\r\2\2\u01a3"+
		"\u01a4\t\2\2\2\u01a4\u01a5\t\20\2\2\u01a5\u01a6\t\21\2\2\u01a6\u01a7\t"+
		"\2\2\2\u01a7\u01a8\t\b\2\2\u01a8\u01a9\t\5\2\2\u01a9\u01aa\t\26\2\2\u01aa"+
		"\u01ae\t\26\2\2\u01ab\u01ac\t\17\2\2\u01ac\u01ae\t\2\2\2\u01ad\u019f\3"+
		"\2\2\2\u01ad\u01ab\3\2\2\2\u01aeJ\3\2\2\2\u01af\u01b0\5O(\2\u01b0\u01b1"+
		"\t\21\2\2\u01b1\u01b2\5S*\2\u01b2\u01b6\3\2\2\2\u01b3\u01b4\t\13\2\2\u01b4"+
		"\u01b6\t\5\2\2\u01b5\u01af\3\2\2\2\u01b5\u01b3\3\2\2\2\u01b6L\3\2\2\2"+
		"\u01b7\u01b8\5Q)\2\u01b8\u01b9\t\21\2\2\u01b9\u01ba\5S*\2\u01ba\u01be"+
		"\3\2\2\2\u01bb\u01bc\t\16\2\2\u01bc\u01be\t\5\2\2\u01bd\u01b7\3\2\2\2"+
		"\u01bd\u01bb\3\2\2\2\u01beN\3\2\2\2\u01bf\u01c0\t\13\2\2\u01c0\u01c1\t"+
		"\t\2\2\u01c1\u01c2\t\30\2\2\u01c2\u01c3\t\4\2\2\u01c3\u01c4\t\2\2\2\u01c4"+
		"\u01c7\t\6\2\2\u01c5\u01c7\t\13\2\2\u01c6\u01bf\3\2\2\2\u01c6\u01c5\3"+
		"\2\2\2\u01c7P\3\2\2\2\u01c8\u01c9\t\16\2\2\u01c9\u01ca\t\26\2\2\u01ca"+
		"\u01cb\t\4\2\2\u01cb\u01ce\t\3\2\2\u01cc\u01ce\t\16\2\2\u01cd\u01c8\3"+
		"\2\2\2\u01cd\u01cc\3\2\2\2\u01ceR\3\2\2\2\u01cf\u01d0\t\5\2\2\u01d0\u01d1"+
		"\t\6\2\2\u01d1\u01d2\t\6\2\2\u01d2\u01d3\t\3\2\2\u01d3\u01d4\t\r\2\2\u01d4"+
		"\u01d5\t\t\2\2\u01d5\u01d6\t\16\2\2\u01d6\u01d7\t\6\2\2\u01d7\u01d8\t"+
		"\4\2\2\u01d8T\3\2\2\2\u01d9\u01da\t\5\2\2\u01da\u01db\t\f\2\2\u01db\u01dc"+
		"\t\20\2\2\u01dcV\3\2\2\2\u01dd\u01de\t\b\2\2\u01de\u01df\t\4\2\2\u01df"+
		"\u01e0\t\6\2\2\u01e0X\3\2\2\2\u01e1\u01e2\t\2\2\2\u01e2\u01e3\t\13\2\2"+
		"\u01e3\u01e4\t\f\2\2\u01e4\u01e5\t\26\2\2\u01e5\u01e6\t\6\2\2\u01e6Z\3"+
		"\2\2\2\u01e7\u01e8\t\25\2\2\u01e8\u01e9\t\16\2\2\u01e9\u01ea\t\f\2\2\u01ea"+
		"\u01eb\t\2\2\2\u01eb\u01ec\t\6\2\2\u01ec\u01ed\t\r\2\2\u01ed\u01ee\t\13"+
		"\2\2\u01ee\u01ef\t\f\2\2\u01ef\\\3\2\2\2\u01f0\u01f1\t\3\2\2\u01f1\u01f2"+
		"\t\4\2\2\u01f2\u01f3\t\6\2\2\u01f3\u01f4\t\16\2\2\u01f4\u01f5\t\3\2\2"+
		"\u01f5\u01f6\t\f\2\2\u01f6^\3\2\2\2\u01f7\u01fa\5a\61\2\u01f8\u01fa\5"+
		"c\62\2\u01f9\u01f7\3\2\2\2\u01f9\u01f8\3\2\2\2\u01fa`\3\2\2\2\u01fb\u01fc"+
		"\t\6\2\2\u01fc\u01fd\t\3\2\2\u01fd\u01fe\t\16\2\2\u01fe\u01ff\t\4\2\2"+
		"\u01ffb\3\2\2\2\u0200\u0201\t\25\2\2\u0201\u0202\t\5\2\2\u0202\u0203\t"+
		"\b\2\2\u0203\u0204\t\26\2\2\u0204\u0205\t\4\2\2\u0205d\3\2\2\2\u0206\u0207"+
		"\t\26\2\2\u0207\u0208\t\6\2\2\u0208\u0209\t\3\2\2\u0209\u020a\t\r\2\2"+
		"\u020a\u020b\t\f\2\2\u020b\u020c\t\27\2\2\u020cf\3\2\2\2\u020d\u020e\t"+
		"\t\2\2\u020e\u020f\t\13\2\2\u020f\u0210\t\13\2\2\u0210\u0211\t\b\2\2\u0211"+
		"\u0212\t\4\2\2\u0212\u0213\t\5\2\2\u0213\u0214\t\f\2\2\u0214h\3\2\2\2"+
		"\u0215\u0216\t\31\2\2\u0216\u0217\t\13\2\2\u0217\u0218\t\r\2\2\u0218\u0219"+
		"\t\7\2\2\u0219j\3\2\2\2\u021a\u021b\t\5\2\2\u021b\u021c\t\3\2\2\u021c"+
		"\u021d\t\3\2\2\u021d\u021e\t\5\2\2\u021e\u021f\t\20\2\2\u021fl\3\2\2\2"+
		"\u0220\u0221\t\22\2\2\u0221\u0222\t\5\2\2\u0222\u0223\t\17\2\2\u0223n"+
		"\3\2\2\2\u0224\u0225\t\25\2\2\u0225\u0226\t\13\2\2\u0226\u0227\t\3\2\2"+
		"\u0227\u0228\t\4\2\2\u0228\u0229\t\5\2\2\u0229\u022a\t\2\2\2\u022a\u022b"+
		"\t\24\2\2\u022bp\3\2\2\2\u022c\u022d\t\25\2\2\u022d\u022e\t\13\2\2\u022e"+
		"\u022f\t\3\2\2\u022fr\3\2\2\2\u0230\u0231\t\r\2\2\u0231\u0232\t\f\2\2"+
		"\u0232t\3\2\2\2\u0233\u0234\t\r\2\2\u0234\u0235\t\25\2\2\u0235v\3\2\2"+
		"\2\u0236\u0237\t\4\2\2\u0237\u0238\t\b\2\2\u0238\u0239\t\26\2\2\u0239"+
		"\u023a\t\4\2\2\u023ax\3\2\2\2\u023b\u023c\t\r\2\2\u023c\u023d\t\f\2\2"+
		"\u023d\u023e\t\21\2\2\u023e\u023f\t\3\2\2\u023f\u0240\t\5\2\2\u0240\u0241"+
		"\t\f\2\2\u0241\u0242\t\27\2\2\u0242\u0243\t\4\2\2\u0243z\3\2\2\2\u0244"+
		"\u0246\t\32\2\2\u0245\u0244\3\2\2\2\u0246\u0247\3\2\2\2\u0247\u0245\3"+
		"\2\2\2\u0247\u0248\3\2\2\2\u0248|\3\2\2\2\u0249\u024b\t\33\2\2\u024a\u0249"+
		"\3\2\2\2\u024b\u024c\3\2\2\2\u024c\u024a\3\2\2\2\u024c\u024d\3\2\2\2\u024d"+
		"~\3\2\2\2\u024e\u0251\5\u0081A\2\u024f\u0251\5\u0083B\2\u0250\u024e\3"+
		"\2\2\2\u0250\u024f\3\2\2\2\u0251\u0080\3\2\2\2\u0252\u0258\7$\2\2\u0253"+
		"\u0254\7^\2\2\u0254\u0257\7$\2\2\u0255\u0257\n\34\2\2\u0256\u0253\3\2"+
		"\2\2\u0256\u0255\3\2\2\2\u0257\u025a\3\2\2\2\u0258\u0259\3\2\2\2\u0258"+
		"\u0256\3\2\2\2\u0259\u025b\3\2\2\2\u025a\u0258\3\2\2\2\u025b\u025c\7$"+
		"\2\2\u025c\u0082\3\2\2\2\u025d\u0263\7)\2\2\u025e\u025f\7^\2\2\u025f\u0262"+
		"\7)\2\2\u0260\u0262\n\34\2\2\u0261\u025e\3\2\2\2\u0261\u0260\3\2\2\2\u0262"+
		"\u0265\3\2\2\2\u0263\u0264\3\2\2\2\u0263\u0261\3\2\2\2\u0264\u0266\3\2"+
		"\2\2\u0265\u0263\3\2\2\2\u0266\u0267\7)\2\2\u0267\u0084\3\2\2\2\u0268"+
		"\u026c\7%\2\2\u0269\u026b\n\35\2\2\u026a\u0269\3\2\2\2\u026b\u026e\3\2"+
		"\2\2\u026c\u026a\3\2\2\2\u026c\u026d\3\2\2\2\u026d\u026f\3\2\2\2\u026e"+
		"\u026c\3\2\2\2\u026f\u0270\7\f\2\2\u0270\u0271\3\2\2\2\u0271\u0272\bC"+
		"\2\2\u0272\u0086\3\2\2\2\u0273\u0275\t\36\2\2\u0274\u0273\3\2\2\2\u0275"+
		"\u0276\3\2\2\2\u0276\u0274\3\2\2\2\u0276\u0277\3\2\2\2\u0277\u0278\3\2"+
		"\2\2\u0278\u0279\bD\3\2\u0279\u0088\3\2\2\2\u027a\u027b\7.\2\2\u027b\u008a"+
		"\3\2\2\2\u027c\u027d\7<\2\2\u027d\u008c\3\2\2\2\u027e\u027f\7=\2\2\u027f"+
		"\u008e\3\2\2\2\u0280\u0281\7}\2\2\u0281\u0090\3\2\2\2\u0282\u0283\7\177"+
		"\2\2\u0283\u0092\3\2\2\2\u0284\u0285\7]\2\2\u0285\u0094\3\2\2\2\u0286"+
		"\u0287\7_\2\2\u0287\u0096\3\2\2\2\u0288\u0289\7>\2\2\u0289\u0098\3\2\2"+
		"\2\u028a\u028b\7@\2\2\u028b\u009a\3\2\2\2\u028c\u028d\7*\2\2\u028d\u009c"+
		"\3\2\2\2\u028e\u028f\7+\2\2\u028f\u009e\3\2\2\2\u0290\u0291\7#\2\2\u0291"+
		"\u00a0\3\2\2\2\u0292\u0293\7?\2\2\u0293\u00a2\3\2\2\2\22\2\u01ad\u01b5"+
		"\u01bd\u01c6\u01cd\u01f9\u0247\u024c\u0250\u0256\u0258\u0261\u0263\u026c"+
		"\u0276\4\2\3\2\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}