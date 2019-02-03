package jp.omiya.persec.query.common;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jp.omiya.persec.query.ast.Op;

public class QueryUtil {

	@SuppressWarnings("unchecked")
	public static Evaluated operation(Object left, Op operator, Object right) {

		if (Config.LOGGING_EVALUATION) {
			log("Evaluate :" + left + " " + operator + " " + right);
		}

		switch (checkType(operator)) {
		case ARITHMETIC:
			if (!(right instanceof BigDecimal)) {
				throw new RuntimeException("Case of [" + operator.getSql() + "] Right Operand is not BigDecimal : " + ((right != null) ? right.getClass().getName() : "null"));
			}
			BigDecimal rightVal = (BigDecimal) right;

			if (Op.NEG == operator) {
				return Evaluated.valueOf(rightVal.negate());
			} else {
				if (!(left instanceof BigDecimal)) {
					throw new RuntimeException();
				}
			}

			BigDecimal leftVal = (BigDecimal) left;

			switch (operator) {
			case PLUS:
				return Evaluated.valueOf(leftVal.add(rightVal));
			case MINUS:
				return Evaluated.valueOf(leftVal.subtract(rightVal));
			case MUL:
				return Evaluated.valueOf(leftVal.multiply(rightVal));
			case DIV:
				return Evaluated.valueOf(leftVal.divide(rightVal));
			case MOD:
				return Evaluated.valueOf(BigDecimal.valueOf(leftVal.doubleValue() % rightVal.doubleValue()));
			default:
				throw new RuntimeException();
			}

		case LOGICAL:
			if (!(right instanceof Boolean)) {
				throw new RuntimeException();
			}
			boolean _rightVal = ((Boolean) right).booleanValue();

			if (Op.NOT == operator) {
				return Evaluated.valueOf(Boolean.valueOf(!_rightVal));
			} else {
				if (!(left instanceof Boolean)) {
					throw new RuntimeException();
				}
			}
			boolean _leftVal = ((Boolean) left).booleanValue();

			switch (operator) {
			case AND:
				return Evaluated.valueOf(Boolean.valueOf(Boolean.logicalAnd(_rightVal, _leftVal)));
			case OR:
				return Evaluated.valueOf(Boolean.valueOf(Boolean.logicalOr(_rightVal, _leftVal)));
			default:
				throw new RuntimeException();
			}

		case NULLCHECK:
			switch (operator) {
			case IS:
				if (right != null) {
					throw new RuntimeException();
				}
				return Evaluated.valueOf(Boolean.valueOf(left == null));

			case IS_NOT:
				if (right != null) {
					throw new RuntimeException();
				}
				return Evaluated.valueOf(Boolean.valueOf(left != null));
			default:
				throw new RuntimeException();
			}

		case EQUALS:
			switch (operator) {
			case EQ:
				if (left == null && right == null) {
					return Evaluated.TRUE_VALUE;
				}
				return Evaluated.valueOf(Boolean.valueOf((left != null && left.equals(right))));

			case NE:
				if (left == null && right == null) {
					return Evaluated.valueOf(Boolean.valueOf(false));
				}
				return Evaluated.valueOf(Boolean.valueOf((left != null && !left.equals(right))));

			default:
				throw new RuntimeException();
			}

		case COMPARE:
			if (left == null || right == null) {
				return Evaluated.FALSE_VALUE;
			}

			Object __leftVal;
			Object __rightVal;
			if (left instanceof Date || right instanceof Date) {
				if (left instanceof String) {
					try {
						__leftVal = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")).parse((String) left);
					} catch (ParseException e) {
						try {
							__leftVal = (new SimpleDateFormat("yyyy-MM-dd")).parse((String) left);
						} catch (ParseException _e) {
							return Evaluated.FALSE_VALUE;
						}
					}
				} else if (left instanceof Date) {
					__leftVal = left;
				} else {
					throw new RuntimeException();
				}
				if (right instanceof String) {
					try {
						__rightVal = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")).parse((String) right);
					} catch (ParseException e) {
						try {
							__rightVal = (new SimpleDateFormat("yyyy-MM-dd")).parse((String) right);
						} catch (ParseException _e) {
							return Evaluated.FALSE_VALUE;
						}
					}
				} else if (right instanceof Date) {
					__rightVal = right;
				} else {
					throw new RuntimeException();
				}
				switch (operator) {
				case GE:
					return Evaluated.valueOf(Boolean.valueOf(((Date)__leftVal).after((Date) __rightVal) || __leftVal.equals(__rightVal)));
				case GT:
					return Evaluated.valueOf(Boolean.valueOf(((Date)__leftVal).after((Date) __rightVal)));
				case LE:
					return Evaluated.valueOf(Boolean.valueOf(((Date)__leftVal).before((Date) __rightVal) || __leftVal.equals(__rightVal)));
				case LT:
					return Evaluated.valueOf(Boolean.valueOf(((Date)__leftVal).before((Date) __rightVal)));
				default:
					throw new RuntimeException();
				}
			} else {
				if (left instanceof Comparable<?>) {
					__leftVal = left;
					if (right.getClass().equals(left.getClass())) {
						__rightVal = right;
					} else if (right instanceof String) {
						try {
							__rightVal = left.getClass().getConstructor(String.class).newInstance((String) right);
						} catch (Exception e) {
							__rightVal = right;
						}
					} else {
						__rightVal = right;
					}

					switch (operator) {
					case GE:
						return Evaluated.valueOf(Boolean.valueOf(((Comparable<Object>)__leftVal).compareTo(__rightVal) >= 0));
					case GT:
						return Evaluated.valueOf(Boolean.valueOf(((Comparable<Object>)__leftVal).compareTo(__rightVal) > 0));
					case LE:
						return Evaluated.valueOf(Boolean.valueOf(((Comparable<Object>)__leftVal).compareTo(__rightVal) <= 0));
					case LT:
						return Evaluated.valueOf(Boolean.valueOf(((Comparable<Object>)__leftVal).compareTo(__rightVal) < 0));
					default:
						throw new RuntimeException();
					}
				} else if (right instanceof Comparable<?>) {
					__rightVal = right;
					if (left.getClass().equals(right.getClass())) {
						__leftVal = left;
					} else if (right instanceof String) {
						try {
							__leftVal = left.getClass().getConstructor(String.class).newInstance((String) left);
						} catch (Exception e) {
							__leftVal = left;
						}
					} else {
						__leftVal = left;
					}

					switch (operator) {
					case GE:
						return Evaluated.valueOf(Boolean.valueOf(((Comparable<Object>)__rightVal).compareTo(__leftVal) < 0));
					case GT:
						return Evaluated.valueOf(Boolean.valueOf(((Comparable<Object>)__rightVal).compareTo(__leftVal) <= 0));
					case LE:
						return Evaluated.valueOf(Boolean.valueOf(((Comparable<Object>)__rightVal).compareTo(__leftVal) > 0));
					case LT:
						return Evaluated.valueOf(Boolean.valueOf(((Comparable<Object>)__rightVal).compareTo(__leftVal) >= 0));
					default:
						throw new RuntimeException();
					}

				} else {
					throw new RuntimeException();
				}
			}

		case IN:
			if (left == null || right == null) {
				return Evaluated.FALSE_VALUE;
			}

			if (right instanceof List<?>) {
				List<?> ___rightVal = (List<?>) right;
				switch (operator) {
				case IN:
					return Evaluated.valueOf(Boolean.valueOf(___rightVal.contains(left)));
				case NOT_IN:
					return Evaluated.valueOf(Boolean.valueOf(!___rightVal.contains(left)));
				default:
					throw new RuntimeException();
				}

			} else {
				switch (operator) {
				case IN:
					return Evaluated.valueOf(Boolean.valueOf(left != null && left.equals(right)));

				case NOT_IN:
					return Evaluated.valueOf(Boolean.valueOf(left != null && !left.equals(right)));

				default:
					throw new RuntimeException();
				}

			}
		default:
			throw new RuntimeException();
		}

	}

	public static void log(Object o) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		System.out.println(sdf.format(new Date(System.currentTimeMillis())) + " " + String.format("%s", o));
	}

	private static OpType checkType(Op operator) {
		switch (operator) {
		case AND:
		case OR:
		case NOT:
			return OpType.LOGICAL;
		case PLUS:
		case MINUS:
		case MUL:
		case DIV:
		case MOD:
		case NEG:
			return OpType.ARITHMETIC;

		case GE:
		case GT:
		case LE:
		case LT:
			return OpType.COMPARE;

		case EQ:
		case NE:
			return OpType.EQUALS;

		case IN:
		case NOT_IN:
			return OpType.IN;

		case IS:
		case IS_NOT:
			return OpType.NULLCHECK;
		}

		throw new RuntimeException("Unexpected Error.");

	}

	private static enum OpType {
		ARITHMETIC, LOGICAL, NULLCHECK, COMPARE, EQUALS, IN;
	}
}
