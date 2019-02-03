package jp.omiya.persec.query.parser;

import static java.util.Arrays.*;

import org.jparsec.Parser;
import org.jparsec.Parsers;
import org.jparsec.Scanners;
import org.jparsec.Terminals;
import org.jparsec.Tokens;

import jp.omiya.persec.query.ast.QualifiedName;

/**
 * 字句・末端表現のパーサー
 */
public final class TerminalParser {

	private static final String[] OPERATORS = {
			"+", "-", "*", "/", "%", ">", "<", "=", ">=", "<=", "<>", ".", ",", "(", ")"
	};

	private static final String[] KEYWORDS = {
			"select", "where", "order", "by", "asc", "desc",
			"and", "or", "not", "in", "between", "is", "null", "like"
	};

	private static final Terminals TERMS = Terminals.operators(OPERATORS).words(Scanners.IDENTIFIER)
			.caseInsensitiveKeywords(asList(KEYWORDS)).build();

	static final Parser<?> TOKENIZER = Parsers.or(
			Terminals.DecimalLiteral.TOKENIZER, Terminals.StringLiteral.SINGLE_QUOTE_TOKENIZER,
			TERMS.tokenizer());

	static final Parser<String> NUMBER = Terminals.DecimalLiteral.PARSER;
	static final Parser<String> STRING = Terminals.StringLiteral.PARSER;

	private static final Parser<String> NAME = Terminals.fragment(Tokens.Tag.RESERVED, Tokens.Tag.IDENTIFIER)
			.or(Terminals.Identifier.PARSER);

	static final Parser<QualifiedName> QUALIFIED_NAME = NAME.sepBy1(term(".")).map(QualifiedName::new);

	static Parser<?> term(String term) {
		return TERMS.token(term);
	}

	static Parser<?> phrase(String phrase) {
		return TERMS.phrase(phrase.split("\\s"));
	}
}
