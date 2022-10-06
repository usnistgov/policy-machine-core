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
		PERFORMS=11, ON=12, DO=13, ANY_USER=14, USERS=15, ANY_USER_WITH_ATTRIBUTE=16, 
		PROCESS=17, INTERSECTION=18, UNION=19, SET_RESOURCE_ACCESS_RIGHTS=20, 
		ASSIGN=21, DEASSIGN=22, FROM=23, SET_PROPERTIES=24, OF=25, TO=26, ASSOCIATE=27, 
		AND=28, WITH=29, DISSOCIATE=30, DENY=31, PROHIBITION=32, OBLIGATION=33, 
		ACCESS_RIGHTS=34, POLICY_CLASS=35, OBJECT_ATTRIBUTE=36, USER_ATTRIBUTE=37, 
		OBJECT=38, USER=39, ATTR=40, ANY=41, LET=42, CONST=43, FUNCTION=44, RETURN=45, 
		BOOLEAN=46, TRUE=47, FALSE=48, STRING_TYPE=49, BOOLEAN_TYPE=50, VOID_TYPE=51, 
		ARRAY_TYPE=52, MAP_TYPE=53, FOREACH=54, FOR=55, IN=56, IF=57, ELSE=58, 
		IN_RANGE=59, NUMBER=60, IDENTIFIER=61, STRING=62, LINE_COMMENT=63, WS=64, 
		COMMA=65, COLON=66, SEMI_COLON=67, OPEN_CURLY=68, CLOSE_CURLY=69, OPEN_BRACKET=70, 
		CLOSE_BRACKET=71, OPEN_ANGLE_BRACKET=72, CLOSE_ANGLE_BRACKET=73, OPEN_PAREN=74, 
		CLOSE_PAREN=75, IS_COMPLEMENT=76, EQUALS=77;
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
			"PERFORMS", "ON", "DO", "ANY_USER", "USERS", "ANY_USER_WITH_ATTRIBUTE", 
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
			null, "ALL_ACCESS_RIGHTS", "ALL_RESOURCE_ACCESS_RIGHTS", "ALL_ADMIN_ACCESS_RIGHTS", 
			"CREATE", "DELETE", "BREAK", "CONTINUE", "POLICY_ELEMENT", "RULE", "WHEN", 
			"PERFORMS", "ON", "DO", "ANY_USER", "USERS", "ANY_USER_WITH_ATTRIBUTE", 
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2O\u027f\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
		"\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\3\2\3\2\3\3\3\3\3\3\3\4\3\4\3\4\3\5"+
		"\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3"+
		"\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t"+
		"\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3"+
		"\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\16\3\16\3\16"+
		"\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\21\3\21\3\21"+
		"\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23"+
		"\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24"+
		"\3\24\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25"+
		"\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27"+
		"\3\27\3\27\3\27\3\27\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\31\3\31\3\31"+
		"\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\32\3\32\3\32\3\33"+
		"\3\33\3\33\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\35\3\35"+
		"\3\35\3\35\3\36\3\36\3\36\3\36\3\36\3\37\3\37\3\37\3\37\3\37\3\37\3\37"+
		"\3\37\3\37\3\37\3\37\3 \3 \3 \3 \3 \3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3"+
		"!\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3#\3#\3#\3#\3#\3#\3#\3#"+
		"\3#\3#\3#\3#\3#\3#\3$\3$\3$\3$\3$\3$\3$\3$\3$\3$\3$\3$\3$\3$\5$\u01a5"+
		"\n$\3%\3%\3%\3%\3%\3%\5%\u01ad\n%\3&\3&\3&\3&\3&\3&\5&\u01b5\n&\3\'\3"+
		"\'\3\'\3\'\3\'\3\'\3\'\5\'\u01be\n\'\3(\3(\3(\3(\3(\5(\u01c5\n(\3)\3)"+
		"\3)\3)\3)\3)\3)\3)\3)\3)\3*\3*\3*\3*\3+\3+\3+\3+\3,\3,\3,\3,\3,\3,\3-"+
		"\3-\3-\3-\3-\3-\3-\3-\3-\3.\3.\3.\3.\3.\3.\3.\3/\3/\5/\u01f1\n/\3\60\3"+
		"\60\3\60\3\60\3\60\3\61\3\61\3\61\3\61\3\61\3\61\3\62\3\62\3\62\3\62\3"+
		"\62\3\62\3\62\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\64\3\64\3\64\3"+
		"\64\3\64\3\65\3\65\3\65\3\65\3\65\3\65\3\66\3\66\3\66\3\66\3\67\3\67\3"+
		"\67\3\67\3\67\3\67\3\67\3\67\38\38\38\38\39\39\39\3:\3:\3:\3;\3;\3;\3"+
		";\3;\3<\3<\3<\3<\3<\3<\3<\3<\3<\3=\6=\u023d\n=\r=\16=\u023e\3>\6>\u0242"+
		"\n>\r>\16>\u0243\3?\3?\7?\u0248\n?\f?\16?\u024b\13?\3?\3?\3@\3@\7@\u0251"+
		"\n@\f@\16@\u0254\13@\3@\3@\3@\3@\3A\6A\u025b\nA\rA\16A\u025c\3A\3A\3B"+
		"\3B\7B\u0263\nB\fB\16B\u0266\13B\3C\3C\3D\3D\3E\3E\3F\3F\3G\3G\3H\3H\3"+
		"I\3I\3J\3J\3K\3K\3L\3L\3M\3M\3N\3N\2\2O\3\3\5\4\7\5\t\6\13\7\r\b\17\t"+
		"\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27"+
		"-\30/\31\61\32\63\33\65\34\67\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W"+
		"-Y.[/]\60_\61a\62c\63e\64g\65i\66k\67m8o9q:s;u<w=y>{?}@\177A\u0081B\u0083"+
		"C\u0085D\u0087E\u0089F\u008bG\u008dH\u008fI\u0091J\u0093K\u0095L\u0097"+
		"M\u0099N\u009bO\3\2\37\4\2EEee\4\2TTtt\4\2GGgg\4\2CCcc\4\2VVvv\4\2FFf"+
		"f\4\2NNnn\4\2DDdd\4\2MMmm\4\2QQqq\4\2PPpp\4\2KKkk\4\2WWww\4\2RRrr\4\2"+
		"[[{{\3\2\"\"\4\2OOoo\4\2YYyy\4\2JJjj\4\2HHhh\4\2UUuu\4\2IIii\4\2LLll\4"+
		"\2XXxx\3\2\62;\t\2--/\60\62;B\\^^aac|\4\2))^^\3\2\f\f\5\2\13\f\17\17\""+
		"\"\2\u028a\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2"+
		"\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27"+
		"\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2"+
		"\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2"+
		"\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2"+
		"\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2"+
		"\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S"+
		"\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2"+
		"\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3\2\2\2\2i\3\2\2\2\2k\3\2\2\2"+
		"\2m\3\2\2\2\2o\3\2\2\2\2q\3\2\2\2\2s\3\2\2\2\2u\3\2\2\2\2w\3\2\2\2\2y"+
		"\3\2\2\2\2{\3\2\2\2\2}\3\2\2\2\2\177\3\2\2\2\2\u0081\3\2\2\2\2\u0083\3"+
		"\2\2\2\2\u0085\3\2\2\2\2\u0087\3\2\2\2\2\u0089\3\2\2\2\2\u008b\3\2\2\2"+
		"\2\u008d\3\2\2\2\2\u008f\3\2\2\2\2\u0091\3\2\2\2\2\u0093\3\2\2\2\2\u0095"+
		"\3\2\2\2\2\u0097\3\2\2\2\2\u0099\3\2\2\2\2\u009b\3\2\2\2\3\u009d\3\2\2"+
		"\2\5\u009f\3\2\2\2\7\u00a2\3\2\2\2\t\u00a5\3\2\2\2\13\u00ac\3\2\2\2\r"+
		"\u00b3\3\2\2\2\17\u00b9\3\2\2\2\21\u00c2\3\2\2\2\23\u00d1\3\2\2\2\25\u00d6"+
		"\3\2\2\2\27\u00db\3\2\2\2\31\u00e4\3\2\2\2\33\u00e7\3\2\2\2\35\u00ea\3"+
		"\2\2\2\37\u00ee\3\2\2\2!\u00f1\3\2\2\2#\u00fa\3\2\2\2%\u0102\3\2\2\2\'"+
		"\u010f\3\2\2\2)\u0115\3\2\2\2+\u0124\3\2\2\2-\u012b\3\2\2\2/\u0134\3\2"+
		"\2\2\61\u0139\3\2\2\2\63\u0148\3\2\2\2\65\u014b\3\2\2\2\67\u014e\3\2\2"+
		"\29\u0158\3\2\2\2;\u015c\3\2\2\2=\u0161\3\2\2\2?\u016c\3\2\2\2A\u0171"+
		"\3\2\2\2C\u017d\3\2\2\2E\u0188\3\2\2\2G\u01a4\3\2\2\2I\u01ac\3\2\2\2K"+
		"\u01b4\3\2\2\2M\u01bd\3\2\2\2O\u01c4\3\2\2\2Q\u01c6\3\2\2\2S\u01d0\3\2"+
		"\2\2U\u01d4\3\2\2\2W\u01d8\3\2\2\2Y\u01de\3\2\2\2[\u01e7\3\2\2\2]\u01f0"+
		"\3\2\2\2_\u01f2\3\2\2\2a\u01f7\3\2\2\2c\u01fd\3\2\2\2e\u0204\3\2\2\2g"+
		"\u020c\3\2\2\2i\u0211\3\2\2\2k\u0217\3\2\2\2m\u021b\3\2\2\2o\u0223\3\2"+
		"\2\2q\u0227\3\2\2\2s\u022a\3\2\2\2u\u022d\3\2\2\2w\u0232\3\2\2\2y\u023c"+
		"\3\2\2\2{\u0241\3\2\2\2}\u0245\3\2\2\2\177\u024e\3\2\2\2\u0081\u025a\3"+
		"\2\2\2\u0083\u0260\3\2\2\2\u0085\u0267\3\2\2\2\u0087\u0269\3\2\2\2\u0089"+
		"\u026b\3\2\2\2\u008b\u026d\3\2\2\2\u008d\u026f\3\2\2\2\u008f\u0271\3\2"+
		"\2\2\u0091\u0273\3\2\2\2\u0093\u0275\3\2\2\2\u0095\u0277\3\2\2\2\u0097"+
		"\u0279\3\2\2\2\u0099\u027b\3\2\2\2\u009b\u027d\3\2\2\2\u009d\u009e\7,"+
		"\2\2\u009e\4\3\2\2\2\u009f\u00a0\7,\2\2\u00a0\u00a1\7t\2\2\u00a1\6\3\2"+
		"\2\2\u00a2\u00a3\7,\2\2\u00a3\u00a4\7c\2\2\u00a4\b\3\2\2\2\u00a5\u00a6"+
		"\t\2\2\2\u00a6\u00a7\t\3\2\2\u00a7\u00a8\t\4\2\2\u00a8\u00a9\t\5\2\2\u00a9"+
		"\u00aa\t\6\2\2\u00aa\u00ab\t\4\2\2\u00ab\n\3\2\2\2\u00ac\u00ad\t\7\2\2"+
		"\u00ad\u00ae\t\4\2\2\u00ae\u00af\t\b\2\2\u00af\u00b0\t\4\2\2\u00b0\u00b1"+
		"\t\6\2\2\u00b1\u00b2\t\4\2\2\u00b2\f\3\2\2\2\u00b3\u00b4\t\t\2\2\u00b4"+
		"\u00b5\t\3\2\2\u00b5\u00b6\t\4\2\2\u00b6\u00b7\t\5\2\2\u00b7\u00b8\t\n"+
		"\2\2\u00b8\16\3\2\2\2\u00b9\u00ba\t\2\2\2\u00ba\u00bb\t\13\2\2\u00bb\u00bc"+
		"\t\f\2\2\u00bc\u00bd\t\6\2\2\u00bd\u00be\t\r\2\2\u00be\u00bf\t\f\2\2\u00bf"+
		"\u00c0\t\16\2\2\u00c0\u00c1\t\4\2\2\u00c1\20\3\2\2\2\u00c2\u00c3\t\17"+
		"\2\2\u00c3\u00c4\t\13\2\2\u00c4\u00c5\t\b\2\2\u00c5\u00c6\t\r\2\2\u00c6"+
		"\u00c7\t\2\2\2\u00c7\u00c8\t\20\2\2\u00c8\u00c9\t\21\2\2\u00c9\u00ca\t"+
		"\4\2\2\u00ca\u00cb\t\b\2\2\u00cb\u00cc\t\4\2\2\u00cc\u00cd\t\22\2\2\u00cd"+
		"\u00ce\t\4\2\2\u00ce\u00cf\t\f\2\2\u00cf\u00d0\t\6\2\2\u00d0\22\3\2\2"+
		"\2\u00d1\u00d2\t\3\2\2\u00d2\u00d3\t\16\2\2\u00d3\u00d4\t\b\2\2\u00d4"+
		"\u00d5\t\4\2\2\u00d5\24\3\2\2\2\u00d6\u00d7\t\23\2\2\u00d7\u00d8\t\24"+
		"\2\2\u00d8\u00d9\t\4\2\2\u00d9\u00da\t\f\2\2\u00da\26\3\2\2\2\u00db\u00dc"+
		"\t\17\2\2\u00dc\u00dd\t\4\2\2\u00dd\u00de\t\3\2\2\u00de\u00df\t\25\2\2"+
		"\u00df\u00e0\t\13\2\2\u00e0\u00e1\t\3\2\2\u00e1\u00e2\t\22\2\2\u00e2\u00e3"+
		"\t\26\2\2\u00e3\30\3\2\2\2\u00e4\u00e5\t\13\2\2\u00e5\u00e6\t\f\2\2\u00e6"+
		"\32\3\2\2\2\u00e7\u00e8\t\7\2\2\u00e8\u00e9\t\13\2\2\u00e9\34\3\2\2\2"+
		"\u00ea\u00eb\5S*\2\u00eb\u00ec\t\21\2\2\u00ec\u00ed\5O(\2\u00ed\36\3\2"+
		"\2\2\u00ee\u00ef\5O(\2\u00ef\u00f0\t\26\2\2\u00f0 \3\2\2\2\u00f1\u00f2"+
		"\5\35\17\2\u00f2\u00f3\t\21\2\2\u00f3\u00f4\t\23\2\2\u00f4\u00f5\t\r\2"+
		"\2\u00f5\u00f6\t\6\2\2\u00f6\u00f7\t\24\2\2\u00f7\u00f8\t\21\2\2\u00f8"+
		"\u00f9\5Q)\2\u00f9\"\3\2\2\2\u00fa\u00fb\t\17\2\2\u00fb\u00fc\t\3\2\2"+
		"\u00fc\u00fd\t\13\2\2\u00fd\u00fe\t\2\2\2\u00fe\u00ff\t\4\2\2\u00ff\u0100"+
		"\t\26\2\2\u0100\u0101\t\26\2\2\u0101$\3\2\2\2\u0102\u0103\t\r\2\2\u0103"+
		"\u0104\t\f\2\2\u0104\u0105\t\6\2\2\u0105\u0106\t\4\2\2\u0106\u0107\t\3"+
		"\2\2\u0107\u0108\t\26\2\2\u0108\u0109\t\4\2\2\u0109\u010a\t\2\2\2\u010a"+
		"\u010b\t\6\2\2\u010b\u010c\t\r\2\2\u010c\u010d\t\13\2\2\u010d\u010e\t"+
		"\f\2\2\u010e&\3\2\2\2\u010f\u0110\t\16\2\2\u0110\u0111\t\f\2\2\u0111\u0112"+
		"\t\r\2\2\u0112\u0113\t\13\2\2\u0113\u0114\t\f\2\2\u0114(\3\2\2\2\u0115"+
		"\u0116\t\26\2\2\u0116\u0117\t\4\2\2\u0117\u0118\t\6\2\2\u0118\u0119\t"+
		"\21\2\2\u0119\u011a\t\3\2\2\u011a\u011b\t\4\2\2\u011b\u011c\t\26\2\2\u011c"+
		"\u011d\t\13\2\2\u011d\u011e\t\16\2\2\u011e\u011f\t\3\2\2\u011f\u0120\t"+
		"\2\2\2\u0120\u0121\t\4\2\2\u0121\u0122\t\21\2\2\u0122\u0123\5E#\2\u0123"+
		"*\3\2\2\2\u0124\u0125\t\5\2\2\u0125\u0126\t\26\2\2\u0126\u0127\t\26\2"+
		"\2\u0127\u0128\t\r\2\2\u0128\u0129\t\27\2\2\u0129\u012a\t\f\2\2\u012a"+
		",\3\2\2\2\u012b\u012c\t\7\2\2\u012c\u012d\t\4\2\2\u012d\u012e\t\5\2\2"+
		"\u012e\u012f\t\26\2\2\u012f\u0130\t\26\2\2\u0130\u0131\t\r\2\2\u0131\u0132"+
		"\t\27\2\2\u0132\u0133\t\f\2\2\u0133.\3\2\2\2\u0134\u0135\t\25\2\2\u0135"+
		"\u0136\t\3\2\2\u0136\u0137\t\13\2\2\u0137\u0138\t\22\2\2\u0138\60\3\2"+
		"\2\2\u0139\u013a\t\26\2\2\u013a\u013b\t\4\2\2\u013b\u013c\t\6\2\2\u013c"+
		"\u013d\t\21\2\2\u013d\u013e\t\17\2\2\u013e\u013f\t\3\2\2\u013f\u0140\t"+
		"\13\2\2\u0140\u0141\t\17\2\2\u0141\u0142\t\4\2\2\u0142\u0143\t\3\2\2\u0143"+
		"\u0144\t\6\2\2\u0144\u0145\t\r\2\2\u0145\u0146\t\4\2\2\u0146\u0147\t\26"+
		"\2\2\u0147\62\3\2\2\2\u0148\u0149\t\13\2\2\u0149\u014a\t\25\2\2\u014a"+
		"\64\3\2\2\2\u014b\u014c\t\6\2\2\u014c\u014d\t\13\2\2\u014d\66\3\2\2\2"+
		"\u014e\u014f\t\5\2\2\u014f\u0150\t\26\2\2\u0150\u0151\t\26\2\2\u0151\u0152"+
		"\t\13\2\2\u0152\u0153\t\2\2\2\u0153\u0154\t\r\2\2\u0154\u0155\t\5\2\2"+
		"\u0155\u0156\t\6\2\2\u0156\u0157\t\4\2\2\u01578\3\2\2\2\u0158\u0159\t"+
		"\5\2\2\u0159\u015a\t\f\2\2\u015a\u015b\t\7\2\2\u015b:\3\2\2\2\u015c\u015d"+
		"\t\23\2\2\u015d\u015e\t\r\2\2\u015e\u015f\t\6\2\2\u015f\u0160\t\24\2\2"+
		"\u0160<\3\2\2\2\u0161\u0162\t\7\2\2\u0162\u0163\t\r\2\2\u0163\u0164\t"+
		"\26\2\2\u0164\u0165\t\26\2\2\u0165\u0166\t\13\2\2\u0166\u0167\t\2\2\2"+
		"\u0167\u0168\t\r\2\2\u0168\u0169\t\5\2\2\u0169\u016a\t\6\2\2\u016a\u016b"+
		"\t\4\2\2\u016b>\3\2\2\2\u016c\u016d\t\7\2\2\u016d\u016e\t\4\2\2\u016e"+
		"\u016f\t\f\2\2\u016f\u0170\t\20\2\2\u0170@\3\2\2\2\u0171\u0172\t\17\2"+
		"\2\u0172\u0173\t\3\2\2\u0173\u0174\t\13\2\2\u0174\u0175\t\24\2\2\u0175"+
		"\u0176\t\r\2\2\u0176\u0177\t\t\2\2\u0177\u0178\t\r\2\2\u0178\u0179\t\6"+
		"\2\2\u0179\u017a\t\r\2\2\u017a\u017b\t\13\2\2\u017b\u017c\t\f\2\2\u017c"+
		"B\3\2\2\2\u017d\u017e\t\13\2\2\u017e\u017f\t\t\2\2\u017f\u0180\t\b\2\2"+
		"\u0180\u0181\t\r\2\2\u0181\u0182\t\27\2\2\u0182\u0183\t\5\2\2\u0183\u0184"+
		"\t\6\2\2\u0184\u0185\t\r\2\2\u0185\u0186\t\13\2\2\u0186\u0187\t\f\2\2"+
		"\u0187D\3\2\2\2\u0188\u0189\t\5\2\2\u0189\u018a\t\2\2\2\u018a\u018b\t"+
		"\2\2\2\u018b\u018c\t\4\2\2\u018c\u018d\t\26\2\2\u018d\u018e\t\26\2\2\u018e"+
		"\u018f\t\21\2\2\u018f\u0190\t\3\2\2\u0190\u0191\t\r\2\2\u0191\u0192\t"+
		"\27\2\2\u0192\u0193\t\24\2\2\u0193\u0194\t\6\2\2\u0194\u0195\t\26\2\2"+
		"\u0195F\3\2\2\2\u0196\u0197\t\17\2\2\u0197\u0198\t\13\2\2\u0198\u0199"+
		"\t\b\2\2\u0199\u019a\t\r\2\2\u019a\u019b\t\2\2\2\u019b\u019c\t\20\2\2"+
		"\u019c\u019d\t\21\2\2\u019d\u019e\t\2\2\2\u019e\u019f\t\b\2\2\u019f\u01a0"+
		"\t\5\2\2\u01a0\u01a1\t\26\2\2\u01a1\u01a5\t\26\2\2\u01a2\u01a3\t\17\2"+
		"\2\u01a3\u01a5\t\2\2\2\u01a4\u0196\3\2\2\2\u01a4\u01a2\3\2\2\2\u01a5H"+
		"\3\2\2\2\u01a6\u01a7\5M\'\2\u01a7\u01a8\t\21\2\2\u01a8\u01a9\5Q)\2\u01a9"+
		"\u01ad\3\2\2\2\u01aa\u01ab\t\13\2\2\u01ab\u01ad\t\5\2\2\u01ac\u01a6\3"+
		"\2\2\2\u01ac\u01aa\3\2\2\2\u01adJ\3\2\2\2\u01ae\u01af\5O(\2\u01af\u01b0"+
		"\t\21\2\2\u01b0\u01b1\5Q)\2\u01b1\u01b5\3\2\2\2\u01b2\u01b3\t\16\2\2\u01b3"+
		"\u01b5\t\5\2\2\u01b4\u01ae\3\2\2\2\u01b4\u01b2\3\2\2\2\u01b5L\3\2\2\2"+
		"\u01b6\u01b7\t\13\2\2\u01b7\u01b8\t\t\2\2\u01b8\u01b9\t\30\2\2\u01b9\u01ba"+
		"\t\4\2\2\u01ba\u01bb\t\2\2\2\u01bb\u01be\t\6\2\2\u01bc\u01be\t\13\2\2"+
		"\u01bd\u01b6\3\2\2\2\u01bd\u01bc\3\2\2\2\u01beN\3\2\2\2\u01bf\u01c0\t"+
		"\16\2\2\u01c0\u01c1\t\26\2\2\u01c1\u01c2\t\4\2\2\u01c2\u01c5\t\3\2\2\u01c3"+
		"\u01c5\t\16\2\2\u01c4\u01bf\3\2\2\2\u01c4\u01c3\3\2\2\2\u01c5P\3\2\2\2"+
		"\u01c6\u01c7\t\5\2\2\u01c7\u01c8\t\6\2\2\u01c8\u01c9\t\6\2\2\u01c9\u01ca"+
		"\t\3\2\2\u01ca\u01cb\t\r\2\2\u01cb\u01cc\t\t\2\2\u01cc\u01cd\t\16\2\2"+
		"\u01cd\u01ce\t\6\2\2\u01ce\u01cf\t\4\2\2\u01cfR\3\2\2\2\u01d0\u01d1\t"+
		"\5\2\2\u01d1\u01d2\t\f\2\2\u01d2\u01d3\t\20\2\2\u01d3T\3\2\2\2\u01d4\u01d5"+
		"\t\b\2\2\u01d5\u01d6\t\4\2\2\u01d6\u01d7\t\6\2\2\u01d7V\3\2\2\2\u01d8"+
		"\u01d9\t\2\2\2\u01d9\u01da\t\13\2\2\u01da\u01db\t\f\2\2\u01db\u01dc\t"+
		"\26\2\2\u01dc\u01dd\t\6\2\2\u01ddX\3\2\2\2\u01de\u01df\t\25\2\2\u01df"+
		"\u01e0\t\16\2\2\u01e0\u01e1\t\f\2\2\u01e1\u01e2\t\2\2\2\u01e2\u01e3\t"+
		"\6\2\2\u01e3\u01e4\t\r\2\2\u01e4\u01e5\t\13\2\2\u01e5\u01e6\t\f\2\2\u01e6"+
		"Z\3\2\2\2\u01e7\u01e8\t\3\2\2\u01e8\u01e9\t\4\2\2\u01e9\u01ea\t\6\2\2"+
		"\u01ea\u01eb\t\16\2\2\u01eb\u01ec\t\3\2\2\u01ec\u01ed\t\f\2\2\u01ed\\"+
		"\3\2\2\2\u01ee\u01f1\5_\60\2\u01ef\u01f1\5a\61\2\u01f0\u01ee\3\2\2\2\u01f0"+
		"\u01ef\3\2\2\2\u01f1^\3\2\2\2\u01f2\u01f3\t\6\2\2\u01f3\u01f4\t\3\2\2"+
		"\u01f4\u01f5\t\16\2\2\u01f5\u01f6\t\4\2\2\u01f6`\3\2\2\2\u01f7\u01f8\t"+
		"\25\2\2\u01f8\u01f9\t\5\2\2\u01f9\u01fa\t\b\2\2\u01fa\u01fb\t\26\2\2\u01fb"+
		"\u01fc\t\4\2\2\u01fcb\3\2\2\2\u01fd\u01fe\t\26\2\2\u01fe\u01ff\t\6\2\2"+
		"\u01ff\u0200\t\3\2\2\u0200\u0201\t\r\2\2\u0201\u0202\t\f\2\2\u0202\u0203"+
		"\t\27\2\2\u0203d\3\2\2\2\u0204\u0205\t\t\2\2\u0205\u0206\t\13\2\2\u0206"+
		"\u0207\t\13\2\2\u0207\u0208\t\b\2\2\u0208\u0209\t\4\2\2\u0209\u020a\t"+
		"\5\2\2\u020a\u020b\t\f\2\2\u020bf\3\2\2\2\u020c\u020d\t\31\2\2\u020d\u020e"+
		"\t\13\2\2\u020e\u020f\t\r\2\2\u020f\u0210\t\7\2\2\u0210h\3\2\2\2\u0211"+
		"\u0212\t\5\2\2\u0212\u0213\t\3\2\2\u0213\u0214\t\3\2\2\u0214\u0215\t\5"+
		"\2\2\u0215\u0216\t\20\2\2\u0216j\3\2\2\2\u0217\u0218\t\22\2\2\u0218\u0219"+
		"\t\5\2\2\u0219\u021a\t\17\2\2\u021al\3\2\2\2\u021b\u021c\t\25\2\2\u021c"+
		"\u021d\t\13\2\2\u021d\u021e\t\3\2\2\u021e\u021f\t\4\2\2\u021f\u0220\t"+
		"\5\2\2\u0220\u0221\t\2\2\2\u0221\u0222\t\24\2\2\u0222n\3\2\2\2\u0223\u0224"+
		"\t\25\2\2\u0224\u0225\t\13\2\2\u0225\u0226\t\3\2\2\u0226p\3\2\2\2\u0227"+
		"\u0228\t\r\2\2\u0228\u0229\t\f\2\2\u0229r\3\2\2\2\u022a\u022b\t\r\2\2"+
		"\u022b\u022c\t\25\2\2\u022ct\3\2\2\2\u022d\u022e\t\4\2\2\u022e\u022f\t"+
		"\b\2\2\u022f\u0230\t\26\2\2\u0230\u0231\t\4\2\2\u0231v\3\2\2\2\u0232\u0233"+
		"\t\r\2\2\u0233\u0234\t\f\2\2\u0234\u0235\t\21\2\2\u0235\u0236\t\3\2\2"+
		"\u0236\u0237\t\5\2\2\u0237\u0238\t\f\2\2\u0238\u0239\t\27\2\2\u0239\u023a"+
		"\t\4\2\2\u023ax\3\2\2\2\u023b\u023d\t\32\2\2\u023c\u023b\3\2\2\2\u023d"+
		"\u023e\3\2\2\2\u023e\u023c\3\2\2\2\u023e\u023f\3\2\2\2\u023fz\3\2\2\2"+
		"\u0240\u0242\t\33\2\2\u0241\u0240\3\2\2\2\u0242\u0243\3\2\2\2\u0243\u0241"+
		"\3\2\2\2\u0243\u0244\3\2\2\2\u0244|\3\2\2\2\u0245\u0249\7)\2\2\u0246\u0248"+
		"\n\34\2\2\u0247\u0246\3\2\2\2\u0248\u024b\3\2\2\2\u0249\u0247\3\2\2\2"+
		"\u0249\u024a\3\2\2\2\u024a\u024c\3\2\2\2\u024b\u0249\3\2\2\2\u024c\u024d"+
		"\7)\2\2\u024d~\3\2\2\2\u024e\u0252\7%\2\2\u024f\u0251\n\35\2\2\u0250\u024f"+
		"\3\2\2\2\u0251\u0254\3\2\2\2\u0252\u0250\3\2\2\2\u0252\u0253\3\2\2\2\u0253"+
		"\u0255\3\2\2\2\u0254\u0252\3\2\2\2\u0255\u0256\7\f\2\2\u0256\u0257\3\2"+
		"\2\2\u0257\u0258\b@\2\2\u0258\u0080\3\2\2\2\u0259\u025b\t\36\2\2\u025a"+
		"\u0259\3\2\2\2\u025b\u025c\3\2\2\2\u025c\u025a\3\2\2\2\u025c\u025d\3\2"+
		"\2\2\u025d\u025e\3\2\2\2\u025e\u025f\bA\3\2\u025f\u0082\3\2\2\2\u0260"+
		"\u0264\7.\2\2\u0261\u0263\7\"\2\2\u0262\u0261\3\2\2\2\u0263\u0266\3\2"+
		"\2\2\u0264\u0262\3\2\2\2\u0264\u0265\3\2\2\2\u0265\u0084\3\2\2\2\u0266"+
		"\u0264\3\2\2\2\u0267\u0268\7<\2\2\u0268\u0086\3\2\2\2\u0269\u026a\7=\2"+
		"\2\u026a\u0088\3\2\2\2\u026b\u026c\7}\2\2\u026c\u008a\3\2\2\2\u026d\u026e"+
		"\7\177\2\2\u026e\u008c\3\2\2\2\u026f\u0270\7]\2\2\u0270\u008e\3\2\2\2"+
		"\u0271\u0272\7_\2\2\u0272\u0090\3\2\2\2\u0273\u0274\7>\2\2\u0274\u0092"+
		"\3\2\2\2\u0275\u0276\7@\2\2\u0276\u0094\3\2\2\2\u0277\u0278\7*\2\2\u0278"+
		"\u0096\3\2\2\2\u0279\u027a\7+\2\2\u027a\u0098\3\2\2\2\u027b\u027c\7#\2"+
		"\2\u027c\u009a\3\2\2\2\u027d\u027e\7?\2\2\u027e\u009c\3\2\2\2\17\2\u01a4"+
		"\u01ac\u01b4\u01bd\u01c4\u01f0\u023e\u0243\u0249\u0252\u025c\u0264\4\2"+
		"\3\2\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}