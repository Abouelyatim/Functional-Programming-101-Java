public class Order {

    private Integer orderId;
    private Integer productIndex ;
    private Integer quantity ;
    private Integer unitPrice ;

    public Order(Integer orderId, Integer productIndex, Integer quantity, Integer unitPrice) {
        this.orderId = orderId;
        this.productIndex = productIndex;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getProductIndex() {
        return productIndex;
    }

    public void setProductIndex(Integer productIndex) {
        this.productIndex = productIndex;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }
}
