package jp.omiya.persec.query.ast;

import java.util.List;

import jp.omiya.persec.query.common.QueryRecord;
import jp.omiya.persec.query.common.TableManager;
import jp.omiya.persec.query.common.ValueObject;

/**
 * テーブル名のモデル
 *
 */
public final class TableRelation extends ValueObject implements Relation {

	public final QualifiedName tableName;

	public TableRelation(QualifiedName tableName) {
		this.tableName = tableName;
	}

	@Override
	public String getSql() {
		return tableName.getSql();
	}

	@Override
	public List<QueryRecord> evaluate() {
		return TableManager.forName((String) tableName.evaluate(null).getValue()).getAllData();
	}
}
