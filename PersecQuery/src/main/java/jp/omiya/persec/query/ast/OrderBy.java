package jp.omiya.persec.query.ast;

import java.util.Collections;
import java.util.List;

import jp.omiya.persec.query.common.ValueObject;

/**
 * "order by" 句 を表します。
 *
 */
public final class OrderBy extends ValueObject {

	public final List<Item> items;

	public OrderBy(List<Item> items) {
		this.items = Collections.unmodifiableList(items);
	}

	/** order by の要素を表します */
	public static final class Item extends ValueObject {

		/** 項目 */
		public final Expression by;
		/** trueの場合 : asc / falseの場合 : desc */
		public final boolean ascending;

		public Item(Expression by, boolean ascending) {
			this.by = by;
			this.ascending = ascending;
		}

		public String getSql() {
			return String.join(" ", by.getSql(), (ascending ? "asc" : "desc"));
		}
	}

	public String getSql() {
		StringBuilder sb = new StringBuilder();
		for (Item item : items) {
			sb.append(item.getSql()).append(",");
		}
		if (sb.length() > 0) {
			sb.delete(sb.length() - 1, sb.length());
		}

		return String.join(" ", "order by", sb.toString());
	}

}
