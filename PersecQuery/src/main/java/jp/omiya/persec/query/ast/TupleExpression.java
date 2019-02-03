package jp.omiya.persec.query.ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import jp.omiya.persec.query.common.Evaluated;
import jp.omiya.persec.query.common.QueryRecord;
import jp.omiya.persec.query.common.ValueObject;

/**
 * "(1, 2, 3)" のようなタプル表現のモデルです
 *
 */
public class TupleExpression extends ValueObject implements Expression {

	public final List<Expression> expressions;

	public TupleExpression(List<Expression> expressions) {
		this.expressions = Collections.unmodifiableList(expressions);
	}

	public static TupleExpression of(Expression... expressions) {
		return new TupleExpression(Arrays.asList(expressions));
	}

	@Override
	public String getSql() {
		StringBuilder sb = new StringBuilder();
		for (Expression expression : expressions) {
			sb.append(expression.getSql()).append(",");
		}
		if (sb.length() > 0) {
			sb.delete(sb.length() - 1, sb.length());
		}

		return String.join(" ", "(", sb.toString(), ")");
	}

	@Override
	public Evaluated evaluate(QueryRecord record) {
		List<Object> l = new ArrayList<>();
		for (Expression expression : expressions) {
			Object o = expression.evaluate(record).getValue();
			if (o != null) {
				l.add(o);
			}
		}
		return Evaluated.valueOf(Collections.unmodifiableList(l));
	}

}
