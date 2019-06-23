package jp.omiya.persec.query.parser;

import java.util.List;

import org.jparsec.Parser;
import org.jparsec.Parsers;
import org.jparsec.Scanners;

import jp.omiya.persec.query.ast.Expression;
import jp.omiya.persec.query.ast.OrderBy;
import jp.omiya.persec.query.ast.Relation;
import jp.omiya.persec.query.ast.Select;
import jp.omiya.persec.query.ast.TableRelation;

/**
 * リレーションに対するパーサー
 *
 */
public final class RelationParser {

	private static final Parser<Relation> TABLE = TerminalParser.QUALIFIED_NAME.map(TableRelation::new);

	public static Parser<Relation> ready() {
		Parser.Reference<Expression> conditionRef = Parser.newReference();
		Parser<Expression> orderby = ExpressionParser.expression(conditionRef.lazy());
		Parser<Expression> cond = ExpressionParser.condition(orderby);
		Parser<Relation> relation = select(TABLE, cond, orderby);
		conditionRef.set(cond);
		return relation.from(TerminalParser.TOKENIZER, Scanners.SQL_DELIMITER);
	}

	private static Parser<Relation> selectClause(Parser<Relation> rel) {
		return TerminalParser.term("select").next(rel);
	}

	private static Parser<Expression> whereClause(Parser<Expression> cond) {
		return TerminalParser.term("where").next(cond);
	}

	private static Parser<OrderBy.Item> orderByItem(Parser<Expression> expr) {
		return Parsers.sequence(
				expr,
				Parsers.or(TerminalParser.term("asc").retn(true), TerminalParser.term("desc").retn(false))
						.optional(true),
				OrderBy.Item::new);
	}

	private static Parser<OrderBy> orderByClause(Parser<Expression> expr) {
		return TerminalParser.phrase("order by").next(list(orderByItem(expr))).map(OrderBy::new);
	}

	private static Parser<Relation> select(Parser<Relation> relation, Parser<Expression> cond,
			Parser<Expression> order) {
		return Parsers.sequence(
				selectClause(relation).optional(null),
				whereClause(cond).optional(null),
				orderByClause(order).optional(null),
				Select::new);
	}

	private static <T> Parser<List<T>> list(Parser<T> p) {
		return p.sepBy1(TerminalParser.term(","));
	}
}
