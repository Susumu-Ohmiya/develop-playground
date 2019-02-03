package jp.omiya.persec.query.ast;

import jp.omiya.persec.query.common.Evaluated;
import jp.omiya.persec.query.common.QueryRecord;
import jp.omiya.persec.query.common.QueryUtil;
import jp.omiya.persec.query.common.ValueObject;

/**
 * "exists (select ...)" のようなリレーションを項に取る単項演算子の表記モデルです
 *
 */
public final class UnaryRelationalExpression extends ValueObject implements Expression {
	public final Relation relation;
	public final Op operator;

	public UnaryRelationalExpression(Relation relation, Op operator) {
		this.relation = relation;
		this.operator = operator;
	}

	@Override
	public String getSql() {
		return String.join(" ", operator.getSql(), "(", relation.getSql(), ")");
	}

	@Override
	public Evaluated evaluate(QueryRecord record) {
		return QueryUtil.operation(null, operator, relation.evaluate());
	}
}
