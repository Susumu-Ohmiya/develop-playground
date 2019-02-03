package jp.omiya.persec.query.ast;

import java.util.ArrayList;
import java.util.List;

import jp.omiya.persec.query.common.QueryRecord;
import jp.omiya.persec.query.common.ValueObject;

/**
 * select句のモデルです
 */
public final class Select extends ValueObject implements Relation {

	/** 選択対象 */
	public final Relation rel;
	/** 選択条件 */
	public final Expression where;
	/** ソート条件 */
	public final OrderBy orderBy;

	public Select(Relation relation, Expression where, OrderBy orderBy) {
		this.rel = relation;
		this.where = where;
		this.orderBy = orderBy;
	}

	@Override
	public String getSql() {
		return String.join(" ", "select * from", rel.getSql(), "where", where.getSql(), orderBy.getSql());
	}

	@Override
	public List<QueryRecord> evaluate() {

		List<QueryRecord> data = rel.evaluate();

		List<QueryRecord> tmp = new ArrayList<>();
		for (QueryRecord queryRecord : data) {
			if (((Boolean) where.evaluate(queryRecord).getValue()).booleanValue()) {
				tmp.add(queryRecord);
			}
		}

		// FIXME ソート未実装

		return tmp;
	}
}
