package jp.omiya.persec.query.ast;

public enum Functions {

	NOW("now");

	String functionName;

	private Functions(String functionName) {
		this.functionName = functionName;
	}

	public static Functions of(String name) {
		if (name == null) {
			return null;
		}
		for (Functions f: Functions.values()) {
			if (f.functionName.equals(name)) {
				return f;
			}
		}
		return null;
	}
}
