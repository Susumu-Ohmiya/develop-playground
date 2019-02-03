package jp.omiya.persec.query.ast;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import jp.omiya.persec.query.common.Evaluated;
import jp.omiya.persec.query.common.QueryRecord;
import jp.omiya.persec.query.common.ValueObject;

/**
 * 関数呼び出し表記
 *
 */
public final class FunctionExpression extends ValueObject implements Expression {

	/** 関数名 */
	public final QualifiedName function;

	/** 引数 */
	public final List<Expression> args;

	public FunctionExpression(QualifiedName function, List<Expression> args) {
		this.function = function;
		this.args = Collections.unmodifiableList(args);
	}

	@Override
	public String getSql() {
		StringBuilder sb = new StringBuilder();
		for (Expression expression : args) {
			sb.append(expression.getSql()).append(",");
		}
		if (sb.length() > 0) {
			sb.delete(sb.length() - 1, sb.length());
		}

		return String.join(" ", function.getSql(), "(", sb.toString(), ")");
	}

	@Override
	public Evaluated evaluate(QueryRecord record) {
		Functions f = Functions.of(function.evaluate(null).getValue().toString());
		if (f == null) {
			throw new RuntimeException();
		}
		switch (f) {
		case NOW:
			return Evaluated.valueOf(new Date(System.currentTimeMillis()));
		default:
			throw new RuntimeException();
		}
	}
}
