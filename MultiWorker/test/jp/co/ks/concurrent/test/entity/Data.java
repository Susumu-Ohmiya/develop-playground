package jp.co.ks.concurrent.test.entity;

import org.apache.commons.lang3.RandomStringUtils;

public class Data {
	private String str;

	public Data() {


		str = RandomStringUtils.randomAlphanumeric(2000);
	}

	public String getStr() {
		return str;
	}
}
