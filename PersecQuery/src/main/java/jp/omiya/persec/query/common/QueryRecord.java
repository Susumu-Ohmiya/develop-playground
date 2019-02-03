package jp.omiya.persec.query.common;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;

public interface QueryRecord {

	public default Object getItem(String propertyName) {
		try {
			PropertyDescriptor pd = new PropertyDescriptor(propertyName, getClass());
			return pd.getReadMethod().invoke(this, (Object[]) null);

		} catch (IntrospectionException | ReflectiveOperationException | IllegalArgumentException e) {
			throw new RuntimeException(e);
		}
	}

}
