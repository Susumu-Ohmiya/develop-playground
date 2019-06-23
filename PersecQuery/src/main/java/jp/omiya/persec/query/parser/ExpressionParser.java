package jp.omiya.persec.query.parser;

import static jp.omiya.persec.query.parser.TerminalParser.*;

import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

import org.jparsec.OperatorTable;
import org.jparsec.Parser;
import org.jparsec.Parsers;

import jp.omiya.persec.query.ast.BetweenExpression;
import jp.omiya.persec.query.ast.BinaryExpression;
import jp.omiya.persec.query.ast.Expression;
import jp.omiya.persec.query.ast.FunctionExpression;
import jp.omiya.persec.query.ast.LikeExpression;
import jp.omiya.persec.query.ast.NullExpression;
import jp.omiya.persec.query.ast.NumberExpression;
import jp.omiya.persec.query.ast.Op;
import jp.omiya.persec.query.ast.QualifiedNameExpression;
import jp.omiya.persec.query.ast.StringExpression;
import jp.omiya.persec.query.ast.TupleExpression;
import jp.omiya.persec.query.ast.UnaryExpression;

/**
 * 表記ごとのパーサー
 *
 */
public final class ExpressionParser {

	private static final Parser<Expression> NULL = term("null").<Expression> retn(NullExpression.instance);

	private static final Parser<Expression> NUMBER = TerminalParser.NUMBER.map(NumberExpression::new);

	private static final Parser<Expression> STRING = TerminalParser.STRING.map(StringExpression::new);

	private static final Parser<Expression> QUALIFIED_NAME = TerminalParser.QUALIFIED_NAME
			.map(QualifiedNameExpression::new);

	private static Parser<Expression> functionCall(Parser<Expression> param) {
		return Parsers.sequence(
				TerminalParser.QUALIFIED_NAME, paren(param.sepBy(TerminalParser.term(","))),
				FunctionExpression::new);
	}

	private static Parser<Expression> tuple(Parser<Expression> expr) {
		return paren(expr.sepBy(term(","))).map(TupleExpression::new);
	}

	static <T> Parser<T> paren(Parser<T> parser) {
		return parser.between(term("("), term(")"));
	}

	private static Parser<Expression> arithmetic(Parser<Expression> atom) {
		Parser.Reference<Expression> reference = Parser.newReference();
		Parser<Expression> operand = Parsers.or(paren(reference.lazy()), functionCall(reference.lazy()), atom);
		Parser<Expression> parser = new OperatorTable<Expression>()
				.infixl(binary("+", Op.PLUS), 10)
				.infixl(binary("-", Op.MINUS), 10)
				.infixl(binary("*", Op.MUL), 20)
				.infixl(binary("/", Op.DIV), 20)
				.infixl(binary("%", Op.MOD), 20)
				.prefix(unary("-", Op.NEG), 50)
				.build(operand);
		reference.set(parser);
		return parser;
	}

	static Parser<Expression> expression(Parser<Expression> cond) {
		Parser.Reference<Expression> reference = Parser.newReference();
		Parser<Expression> atom = Parsers.or(STRING, NUMBER, QUALIFIED_NAME);
		Parser<Expression> expression = arithmetic(atom).label("expression");
		reference.set(expression);
		return expression;
	}

	static Parser<Expression> compare(Parser<Expression> expr) {
		return Parsers.or(
				compare(expr, ">", Op.GT), compare(expr, ">=", Op.GE),
				compare(expr, "<", Op.LT), compare(expr, "<=", Op.LE),
				compare(expr, "=", Op.EQ), compare(expr, "<>", Op.NE),
				nullCheck(expr), like(expr), between(expr));
	}

	static Parser<Expression> like(Parser<Expression> expr) {
		return Parsers.sequence(
				expr,
				expr,
				LikeExpression::new);
	}

	static Parser<Expression> nullCheck(Parser<Expression> expr) {
		return Parsers.sequence(
				expr, phrase("is not").retn(Op.IS_NOT).or(phrase("is").retn(Op.IS)), NULL,
				BinaryExpression::new);
	}

	static Parser<Expression> logical(Parser<Expression> expr) {
		Parser.Reference<Expression> ref = Parser.newReference();
		Parser<Expression> parser = new OperatorTable<Expression>()
				.prefix(unary("not", Op.NOT), 30)
				.infixl(binary("and", Op.AND), 20)
				.infixl(binary("or", Op.OR), 10)
				.build(paren(ref.lazy()).or(expr)).label("logical expression");
		ref.set(parser);
		return parser;
	}

	static Parser<Expression> between(Parser<Expression> expr) {
		return Parsers.sequence(
				expr, Parsers.or(term("between").retn(true), phrase("not between").retn(false)),
				expr, term("and").next(expr),
				BetweenExpression::new);
	}

	static Parser<Expression> in(Parser<Expression> expr) {
		return Parsers.sequence(
				expr, term("in").next(tuple(expr)),
				(e, t) -> new BinaryExpression(e, Op.IN, t));
	}

	static Parser<Expression> notIn(Parser<Expression> expr) {
		return Parsers.sequence(
				expr, phrase("not in").next(tuple(expr)),
				(e, t) -> new BinaryExpression(e, Op.NOT_IN, t));
	}

	static Parser<Expression> condition(Parser<Expression> expr) {
		Parser<Expression> atom = Parsers.or(compare(expr), in(expr), notIn(expr));
		return logical(atom);
	}

	/************************** utility methods ****************************/

	private static Parser<Expression> compare(
			Parser<Expression> operand, String name, Op op) {
		return Parsers.sequence(
				operand, term(name).retn(op), operand,
				BinaryExpression::new);
	}

	private static Parser<BinaryOperator<Expression>> binary(String name, Op op) {
		return term(name).retn((l, r) -> new BinaryExpression(l, op, r));
	}

	private static Parser<UnaryOperator<Expression>> unary(String name, Op op) {
		return term(name).retn(e -> new UnaryExpression(op, e));
	}
}
