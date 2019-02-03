package jp.omiya.persec.query.ast;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import jp.omiya.persec.query.common.Evaluated;
import jp.omiya.persec.query.common.QueryRecord;
import jp.omiya.persec.query.common.ValueObject;

/**
 * "a.b.c" のような修飾名
 *
 */
public final class QualifiedName extends ValueObject implements Iterable<String> {

	public final List<String> names;

	public QualifiedName(List<String> names) {
		this.names = Collections.unmodifiableList(names);
	}

	public static QualifiedName of(String... names) {
		return new QualifiedName(Arrays.asList(names));
	}

	@Override
	public Iterator<String> iterator() {
		return names.iterator();
	}

	public String getSql() {
		return String.join(".", names);
	}

	public Evaluated evaluate(QueryRecord record) {
		if (record == null) {
			return Evaluated.valueOf(getSql());
		}
		return Evaluated.valueOf(record.getItem(getSql()));
	}
}
