package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductStockInfo {
    private Integer productSizeColorID;
    private Integer productID;
    private Integer sizeID;
    private Integer stock; // Có thể null nếu không có tồn kho

    public ProductStockInfo(Integer productSizeColorID, Integer productID, Integer sizeID, Integer stock) {
        this.productSizeColorID = productSizeColorID;
        this.productID = productID;
        this.sizeID = sizeID;
        this.stock = stock;
    }

    public ProductStockInfo() {
    }

    public Integer getProductSizeColorID() {
        return productSizeColorID;
    }

    public void setProductSizeColorID(Integer productSizeColorID) {
        this.productSizeColorID = productSizeColorID;
    }

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public Integer getSizeID() {
        return sizeID;
    }

    public void setSizeID(Integer sizeID) {
        this.sizeID = sizeID;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
