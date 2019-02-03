package jp.omiya.persec.query.ast;

import jp.omiya.persec.query.common.Evaluated;
import jp.omiya.persec.query.common.QueryRecord;
import jp.omiya.persec.query.common.ValueObject;

/**
 * "like" 表記を表します
 *
 * ※本機能ではSQLと異なり、正規表現マッチを行わせます。よって not 検索は出来ません。
 *
 */
public final class LikeExpression extends ValueObject implements Expression {

	/** 左辺 */
	public final Expression expression;

	/** 右辺（正規表現パターン） */
	public final Expression pattern;

	public LikeExpression(Expression expression, Expression pattern) {
		this.expression = expression;
		this.pattern = pattern;
	}

	public String getSql() {
		// REGEXP_LIKE(hr.employees.first_name, '^Ste(v|ph)en$')
		return String.join(" ", "REGEXP_LIKE(" , expression.getSql(), " , " , pattern.getSql() , ")");
	}

	@Override
	public Evaluated evaluate(QueryRecord record) {
		Object leftval = expression.evaluate(record).getValue();
		Object rightVal = pattern.evaluate(record).getValue();

		return Evaluated.is(leftval.toString().matches(rightVal.toString()));
	}
}
