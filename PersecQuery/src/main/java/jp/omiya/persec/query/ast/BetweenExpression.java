package jp.omiya.persec.query.ast;

import jp.omiya.persec.query.common.Evaluated;
import jp.omiya.persec.query.common.QueryRecord;
import jp.omiya.persec.query.common.QueryUtil;
import jp.omiya.persec.query.common.ValueObject;

/**
 * 3項演算表記
 *
 * "between A and B" と "not between A and B" を表します。
 */
public final class BetweenExpression extends ValueObject implements Expression {

	/** 左辺値 */
	public final Expression expression;

	/** false の場合 not */
	public final boolean positive;

	/** 第一項値 */
	public final Expression from;

	/** 第二項値 */
	public final Expression to;

	public BetweenExpression(Expression expression, boolean positive, Expression from, Expression to) {
		this.expression = expression;
		this.positive = positive;
		this.from = from;
		this.to = to;
	}

	@Override
	public String getSql() {
		return String.join(" ", (positive ? "between" : "not between"), from.getSql(), "and", to.getSql());
	}

	@Override
	public Evaluated evaluate(QueryRecord record) {
		return Evaluated.is(isMatch(record));
	}

	private boolean isMatch(QueryRecord record) {

		Object target = expression.evaluate(record).getValue();
		Object fromVal = from.evaluate(record).getValue();
		Object toVal = to.evaluate(record).getValue();

		boolean ret1 = ((Boolean) QueryUtil.operation(target ,Op.GE, fromVal).getValue()).booleanValue();
		boolean ret2 = ((Boolean) QueryUtil.operation(target ,Op.LE, toVal).getValue()).booleanValue();

		return ret1 && ret2;
	}

}
