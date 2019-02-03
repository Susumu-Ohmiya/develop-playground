package jp.co.isol.omiya.test.copyobject;

public class DestinationObject {

    @FieldName("ITEM")
    private String goods;

    @FieldName("AMOUNT")
    private Integer amount;

    @FieldName("AMOUNT")
    private Integer price;

    /**
     * @return amount
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * @param amount セットする amount
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    /**
     * @return price
     */
    public Integer getPrice() {
        return price;
    }

    /**
     * @param price セットする price
     */
    public void setPrice(Integer price) {
        this.price = price;
    }

    /**
     * @return goods
     */
    public String getGoods() {
        return goods;
    }

    /**
     * @param goods セットする goods
     */
    public void setGoods(String goods) {
        this.goods = goods;
    }
    
    
}
