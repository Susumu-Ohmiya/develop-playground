package jp.omiya.persec.query.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.omiya.persec.query.common.QueryRecord;
import jp.omiya.persec.query.common.Table;

public class DemoTable implements Table {

	private static DemoTable instance = new DemoTable();

	private List<QueryRecord> tableData = Collections.synchronizedList(new ArrayList<>());

	@Override
	public void refresh() {
		tableData.clear();

		DemoBean bean1 = new DemoBean();
		bean1.setAmount(new BigDecimal("50"));
		bean1.setEffect("attackup_rate");
		bean1.setTarget("party");
		bean1.setSkill1("HOGE");
		tableData.add(bean1);

		DemoBean bean2 = new DemoBean();
		bean2.setAmount(new BigDecimal("50"));
		bean2.setEffect("attackup_rate");
		bean2.setTarget("self");
		bean2.setSkill1("UP");
		tableData.add(bean2);

		DemoBean bean3 = new DemoBean();
		bean3.setAmount(new BigDecimal("20"));
		bean3.setEffect("attackup_rate");
		bean3.setTarget("self");
		bean3.setSkill1("UP");
		tableData.add(bean3);

		DemoBean bean4 = new DemoBean();
		tableData.add(bean4);
	}

	public static DemoTable getInstance() {
		return instance;
	}

	@Override
	public String getQualifiedName() {
		return "servant";
	}

	@Override
	public List<QueryRecord> getAllData() {
		this.refresh();

		return tableData;
	}


}
