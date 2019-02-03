package jp.omiya.persec.query.common;

/**
 * Expression の評価結果を格納します。
 *
 */
public class Evaluated {

	public static final Evaluated TRUE_VALUE = new Evaluated(null, Boolean.TRUE);
	public static final Evaluated FALSE_VALUE = new Evaluated(null, Boolean.FALSE);

	public final String propertyName;
	private final Object evaluatedValue;
	public final boolean hasValue;

	public Evaluated(String propertyName, Object evaluatedValue) {

		if (Config.LOGGING_EVALUATION) {
			QueryUtil.log("Evaluated : propertyName[" + propertyName + "] evaluatedValue[" + evaluatedValue + "]");
		}

		if (propertyName != null && evaluatedValue == null) {
			this.propertyName = propertyName;
			this.evaluatedValue = null;
			this.hasValue = false;
		} else if (propertyName == null || propertyName.isEmpty()) {
			this.propertyName = null;
			this.evaluatedValue = evaluatedValue;
			this.hasValue = true;
		} else {
			throw new IllegalArgumentException();
		}
	}

	public Object getValue() {
		if (!hasValue) {
			throw new RuntimeException();
		}
		return evaluatedValue;
	}

	public static Evaluated is(boolean val) {
		return val ? TRUE_VALUE : FALSE_VALUE;
	}

	public static Evaluated valueOf(Object obj) {
		return new Evaluated(null, obj);
	}


}
