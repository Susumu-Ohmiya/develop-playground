package jp.omiya.persec.query.ast;

import jp.omiya.persec.query.common.Evaluated;
import jp.omiya.persec.query.common.QueryRecord;

/**
 * "null" を表します。
 *
 */
public final class NullExpression implements Expression {

	private NullExpression() {
	}

	public static final NullExpression instance = new NullExpression();

	@Override
	public String getSql() {
		return "null";
	}

	@Override
	public Evaluated evaluate(QueryRecord record) {
		return Evaluated.valueOf(null);
	}
}
