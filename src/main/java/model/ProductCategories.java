package model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Entity
@Table(name = "productcategories") // Lowercase
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "ProductCategories.findAll", query = "SELECT p FROM ProductCategories p"),
        @NamedQuery(name = "ProductCategories.categorizeProducts",
                query = "SELECT p FROM ProductCategories pc " +
                        "INNER JOIN pc.productID p " +
                        "WHERE pc.categoryID.categoryID = :categoryID " +
                        "AND LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                        "AND pc.productID.status = TRUE"),
})
public class ProductCategories implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "productcategoryid") // Lowercase
    private Integer productCategoryID;
    @JoinColumn(name = "categoryid", referencedColumnName = "categoryid") // Lowercase
    @ManyToOne(optional = false)
    private Categories categoryID;
    @JoinColumn(name = "productid", referencedColumnName = "productid") // Lowercase
    @ManyToOne(optional = false)
    private Products productID;

    public ProductCategories() {
    }

    public ProductCategories(Integer productCategoryID) {
        this.productCategoryID = productCategoryID;
    }

    public Integer getProductCategoryID() {
        return productCategoryID;
    }

    public void setProductCategoryID(Integer productCategoryID) {
        this.productCategoryID = productCategoryID;
    }

    public Categories getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Categories categoryID) {
        this.categoryID = categoryID;
    }

    public Products getProductID() {
        return productID;
    }

    public void setProductID(Products productID) {
        this.productID = productID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productCategoryID != null ? productCategoryID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ProductCategories)) {
            return false;
        }
        ProductCategories other = (ProductCategories) object;
        if ((this.productCategoryID == null && other.productCategoryID != null) || (this.productCategoryID != null && !this.productCategoryID.equals(other.productCategoryID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.resources.ProductCategories[ productCategoryID=" + productCategoryID + " ]";
    }
}