package jp.co.isol.omiya.test.copyobject;

public class SourceObject {

    @FieldName("ITEM")
    private String item;

    @FieldName("AMOUNT")
    private Integer amout;

    /**
     * @return amout
     */
    public Integer getAmout() {
        return amout;
    }

    /**
     * @param amout セットする amout
     */
    public void setAmout(Integer amout) {
        this.amout = amout;
    }

    /**
     * @return item
     */
    public String getItem() {
        return item;
    }

    /**
     * @param item セットする item
     */
    public void setItem(String item) {
        this.item = item;
    }
}
