package jp.omiya.persec.query.ast;

import jp.omiya.persec.query.common.Evaluated;
import jp.omiya.persec.query.common.QueryRecord;

/**
 * 全表記を表すインタフェース
 */
public interface Expression {

	/**
	 * 表記を評価します。
	 * 条件表記の場合は一致判定を行い、関数表記の場合は関数評価を行います。
	 * 名称オブジェクトの場合は引数にレコードの渡されている場合はその対応する値を返し、nullの場合は修飾名自体を返します。
	 * リテラル表記の場合、値自身を返します。
	 *
	 * @return 評価結果
	 */
	public Evaluated evaluate(QueryRecord record);

	/**
	 * SQLでの表記を返します
	 * @return SQL
	 */
	public String getSql();

}
