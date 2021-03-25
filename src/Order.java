public class Order {

    private Integer orderId;
    private Integer productIndex ;
    private Integer quantity ;
    private Integer unitPrice ;
    private Double discount ;

    public Order(Integer orderId, Integer productIndex, Integer quantity, Integer unitPrice, Double discount) {
        this.orderId = orderId;
        this.productIndex = productIndex;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.discount = discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public Integer getProductIndex() {
        return productIndex;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public Double getDiscount() {
        return discount;
    }

}
