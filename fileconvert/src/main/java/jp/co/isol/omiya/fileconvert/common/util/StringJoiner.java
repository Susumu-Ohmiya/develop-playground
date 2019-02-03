package jp.co.isol.omiya.fileconvert.common.util;

/**
 * TODO:コメント書く
 */
public final class StringJoiner {

    private final String delimiter;
    private StringBuilder value;

    private String emptyValue;

    /**
    * TODO:コメント書く
    */
    public StringJoiner(CharSequence delimiter) {
        this.delimiter = Objects.requireNonNull(delimiter, "The delimiter must not be null").toString();
        this.emptyValue = "";
    }

    /**
    * TODO:コメント書く
    */
    public StringJoiner setEmptyValue(CharSequence emptyValue) {
        this.emptyValue = Objects.requireNonNull(emptyValue, "The empty value must not be null").toString();
        return this;
    }

    @Override
    public String toString() {
        if (value == null) {
            return emptyValue;
        } else {
            return value.toString();
        }
    }

    /**
    * TODO:コメント書く
    */
    public StringJoiner add(CharSequence newElement) {
        prepareBuilder().append(newElement);
        return this;
    }

    /**
    * TODO:コメント書く
    */
    public StringJoiner merge(StringJoiner other) {
        Objects.requireNonNull(other);
        if (other.value != null) {
            StringBuilder builder = prepareBuilder();
            builder.append(other.value);
        }
        return this;
    }

    private StringBuilder prepareBuilder() {
        if (value != null) {
            value.append(delimiter);
        } else {
            value = new StringBuilder();
        }
        return value;
    }

    /**
    * TODO:コメント書く
    */
    public int length() {
        return (value != null ? value.length() : emptyValue.length());
    }
}
