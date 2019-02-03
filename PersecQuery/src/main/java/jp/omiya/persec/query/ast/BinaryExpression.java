package jp.omiya.persec.query.ast;

import jp.omiya.persec.query.common.Evaluated;
import jp.omiya.persec.query.common.QueryRecord;
import jp.omiya.persec.query.common.QueryUtil;
import jp.omiya.persec.query.common.ValueObject;

/**
 * 二項演算子
 *
 */
public final class BinaryExpression extends ValueObject implements Expression {

	/** 左辺 */
	public final Expression left;

	/** 右辺 */
	public final Expression right;

	/** 演算子 */
	public final Op operator;

	public BinaryExpression(Expression left, Op op, Expression right) {
		this.left = left;
		this.operator = op;
		this.right = right;
	}

	@Override
	public String getSql() {
		return String.join(" ", "(", left.getSql(), operator.getSql(), right.getSql(), ")");
//		return String.join(" ", left.getSql(), operator.getSql(), right.getSql());
	}

	@Override
	public Evaluated evaluate(QueryRecord record) {
		Object leftval = left.evaluate(record).getValue();
		Object rightVal = right.evaluate(record).getValue();

		return QueryUtil.operation(leftval, operator, rightVal);
	}
}
