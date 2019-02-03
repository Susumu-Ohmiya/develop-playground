package jp.omiya.persec.query.ast;

import jp.omiya.persec.query.common.Evaluated;
import jp.omiya.persec.query.common.QueryRecord;
import jp.omiya.persec.query.common.ValueObject;

/**
 * "a.b.c" のような修飾名表記を表します
 *
 */
public final class QualifiedNameExpression extends ValueObject implements Expression {

	/** 修飾名 */
	public final QualifiedName qualifiedName;

	public QualifiedNameExpression(QualifiedName qname) {
		this.qualifiedName = qname;
	}

	public static QualifiedNameExpression of(String... names) {
		return new QualifiedNameExpression(QualifiedName.of(names));
	}

	@Override
	public String getSql() {
		return qualifiedName.getSql();
	}

	@Override
	public Evaluated evaluate(QueryRecord record) {
		return qualifiedName.evaluate(record);
	}
}
