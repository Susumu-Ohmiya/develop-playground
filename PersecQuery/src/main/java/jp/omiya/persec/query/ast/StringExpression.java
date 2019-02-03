package jp.omiya.persec.query.ast;

import jp.omiya.persec.query.common.Evaluated;
import jp.omiya.persec.query.common.QueryRecord;
import jp.omiya.persec.query.common.ValueObject;

/**
 * 文字列りテラル
 */
public final class StringExpression extends ValueObject implements Expression {

	public final String string;

	public StringExpression(String string) {
		this.string = string;
	}

	@Override
	public String getSql() {
		return "'" + string + "'";
	}

	@Override
	public Evaluated evaluate(QueryRecord record) {
		return Evaluated.valueOf(string);
	}

}
