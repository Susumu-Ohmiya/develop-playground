package jp.omiya.persec.query.ast;

import java.math.BigDecimal;

import jp.omiya.persec.query.common.Evaluated;
import jp.omiya.persec.query.common.QueryRecord;
import jp.omiya.persec.query.common.ValueObject;

/**
 * 数値を表します
 *
 */
public final class NumberExpression extends ValueObject implements Expression {

	/** 値 */
	public final BigDecimal number;

	public NumberExpression(String number) {
		this.number =new  BigDecimal(number);
	}

	@Override
	public String getSql() {
		return number.toPlainString();
	}

	@Override
	public Evaluated evaluate(QueryRecord record) {
		return Evaluated.valueOf(number);
	}
}
