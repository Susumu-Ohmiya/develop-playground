package jp.co.isol.omiya.test.demo;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class BigDecimalDisplay {

	public static void main(String[] args) {
		
		disp(calc(new BigDecimal("1.001"),new BigDecimal("0.08")));
		disp(calc(new BigDecimal("1.0008"),new BigDecimal("0.08")));
		disp(calc(new BigDecimal("1.08"),new BigDecimal("0.08")));
	}


	public static BigDecimal calc(BigDecimal value,BigDecimal taxRate) {
		
		BigDecimal c = value.setScale(4).divide(BigDecimal.ONE.add(taxRate), BigDecimal.ROUND_HALF_UP);
		
		return c;
	}
	
	public static void disp(BigDecimal value) {

		System.out.println("=====<"+ value +">===========");

		if (value.scale() > 3) {
			value = value.setScale(3, BigDecimal.ROUND_HALF_UP);
		}
		
		System.out.println("Value   :" + value.toPlainString());
	}
}
