package jp.omiya.persec.query.ast;

/**
 * 演算子を表します
 *
 */
public enum Op {

	/** plus */
	PLUS("+"),
	/** minus */
	MINUS("-"),
	/** multiply */
	MUL("*"),
	/** divide */
	DIV("/"),
	/** modlus */
	MOD("%"),
	/** negative */
	NEG("-"),
	/** in */
	IN("in"),
	/** not in */
	NOT_IN("not in"),
	/** equal */
	EQ("="),
	/** greater than */
	GT("<"),
	/** less than */
	LT(">"),
	/** greater than equal */
	GE("<="),
	/** less than equal */
	LE(">="),
	/** not equal */
	NE("<>"),
	/** not */
	NOT("not"),
	/** and */
	AND("and"),
	/** or */
	OR("or"),
	/** is */
	IS("is"),
	/** is */
	IS_NOT("is not");

	private String sqlExp;

	Op (String sqlOp) {
		this.sqlExp = sqlOp;
	}

	public String getSql() {
		return sqlExp;
	}
}
