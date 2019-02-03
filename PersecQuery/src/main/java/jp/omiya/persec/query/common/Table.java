package jp.omiya.persec.query.common;

import java.util.List;

public interface Table {
	public void refresh();
	public String getQualifiedName();
	public List<QueryRecord> getAllData();
}
