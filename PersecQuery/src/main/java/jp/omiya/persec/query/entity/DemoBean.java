package jp.omiya.persec.query.entity;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.math.BigDecimal;

import jp.omiya.persec.query.common.QueryRecord;

public class DemoBean implements QueryRecord {

	public String getEffect() {
		return effect;
	}
	public void setEffect(String effect) {
		this.effect = effect;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getSkill1() {
		return skill1;
	}
	public void setSkill1(String skill1) {
		this.skill1 = skill1;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (Field f : this.getClass().getDeclaredFields()) {
			try {
				PropertyDescriptor pd = new PropertyDescriptor(f.getName(), getClass());
				Object val = pd.getReadMethod().invoke(this, (Object[]) null);

				sb.append(f.getName()).append(":").append(val).append("\n");

			} catch (IntrospectionException | ReflectiveOperationException | IllegalArgumentException e) {
				throw new RuntimeException(e);
			}
		}
		return sb.toString();
	}

	private String effect;
	private String target;
	private BigDecimal amount;
	private String skill1;




}
