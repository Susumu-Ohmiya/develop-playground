package jp.omiya.persec.query.ast;

import jp.omiya.persec.query.common.Evaluated;
import jp.omiya.persec.query.common.QueryRecord;
import jp.omiya.persec.query.common.QueryUtil;
import jp.omiya.persec.query.common.ValueObject;

/**
 * 単項演算子の表記モデルです
 *
 */
public final class UnaryExpression extends ValueObject implements Expression {

	/** オペランド */
	public final Expression operand;
	/** 演算子 */
	public final Op operator;

	public UnaryExpression(Op operator, Expression operand) {
		this.operand = operand;
		this.operator = operator;
	}

	@Override
	public String getSql() {
		return String.join(" ", operator.getSql(), operand.getSql());
	}

	@Override
	public Evaluated evaluate(QueryRecord record) {
		return QueryUtil.operation(null, operator, operand.evaluate(record).getValue());
	}

}
