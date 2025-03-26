package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductStockInfo {
    private int productSizeColorID;
    private int productID;
    private int sizeID;
    private BigDecimal productPrice;
    private Integer stock; // Có thể null nếu không có tồn kho
    private LocalDateTime lastUpdated;
    private int totalOrderedQuantity;

    public ProductStockInfo(int productSizeColorID, int productID, int sizeID, BigDecimal productPrice, Integer stock, LocalDateTime lastUpdated, int totalOrderedQuantity) {
        this.productSizeColorID = productSizeColorID;
        this.productID = productID;
        this.sizeID = sizeID;
        this.productPrice = productPrice;
        this.stock = stock;
        this.lastUpdated = lastUpdated;
        this.totalOrderedQuantity = totalOrderedQuantity;
    }

    public ProductStockInfo() {
    }

    // Getters & Setters
    public int getProductSizeColorID() {
        return productSizeColorID;
    }

    public int getProductID() {
        return productID;
    }

    public int getSizeID() {
        return sizeID;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public Integer getStock() {
        return stock;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public int getTotalOrderedQuantity() {
        return totalOrderedQuantity;
    }
}
