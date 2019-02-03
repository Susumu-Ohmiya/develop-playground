package jp.omiya.persec.query.ast;

import jp.omiya.persec.query.common.Evaluated;
import jp.omiya.persec.query.common.QueryRecord;
import jp.omiya.persec.query.common.QueryUtil;
import jp.omiya.persec.query.common.ValueObject;

/**
 * 演算対象に Relation をとる演算子の表記
 *
 */
public final class BinaryRelationalExpression extends ValueObject implements Expression {

	/** 左辺 */
	public final Expression expression;

	/** 演算子 */
	public final Op operator;

	/** 右辺 */
	public final Relation relation;

	public BinaryRelationalExpression(Expression expression, Op operator, Relation relation) {
		this.expression = expression;
		this.operator = operator;
		this.relation = relation;
	}

	@Override
	public String getSql() {
		return String.join(" ", expression.getSql(), operator.getSql(), "(", relation.getSql(), ")");
	}

	@Override
	public Evaluated evaluate(QueryRecord record) {
		Object leftval = expression.evaluate(record).getValue();
		Object rightVal = relation.evaluate();

		return QueryUtil.operation(leftval, operator, rightVal);
	}

}
