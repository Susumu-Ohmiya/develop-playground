package jp.omiya.persec.query.ast;

import java.util.List;

import jp.omiya.persec.query.common.QueryRecord;

/**
 * テーブルやセレクト結果のようなリレーションのモデルです
 *
 */
public interface Relation {

	public String getSql();

	/**
	 * 表記を評価します。
	 *
	 * @return 評価結果
	 */
	public List<QueryRecord> evaluate();

}
