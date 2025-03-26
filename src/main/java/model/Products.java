package model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "products") // Lowercase
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Products.selectAll", query = "SELECT p FROM Products p LEFT JOIN FETCH p.productimagesCollection"),
        @NamedQuery(name = "Products.selectByID", query = "SELECT p FROM Products p WHERE p.productID = :productID"),
        @NamedQuery(name = "Products.searchProducts", query = "SELECT p FROM Products p WHERE p.productName LIKE :keyword AND p.status = TRUE"),
        @NamedQuery(name = "Products.findCategoryByWeather", query = "SELECT p FROM Products p JOIN ProductCategories pc ON p.id = pc.productID.productID WHERE pc.categoryID.categoryID = :categoryID AND LOWER(p.productName) LIKE :keyword"),
        @NamedQuery(name = "Products.findCategory", query = "SELECT p FROM Products p JOIN ProductCategories pc ON p.id = pc.productID.productID WHERE pc.categoryID.categoryID = :categoryID"),
        @NamedQuery(name = "Products.selectAllActive", query = "SELECT p FROM Products p LEFT JOIN FETCH p.productimagesCollection WHERE p.status = TRUE"),
        @NamedQuery(name = "Products.getPrice", query = "SELECT p FROM Products p WHERE p.productName LIKE :name AND p.status = TRUE")
})
public class Products implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "productid") // Lowercase
    private Integer productID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "productname") // Lowercase
    private String productName;
    @Size(max = 500)
    @Column(name = "description") // Lowercase
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "price") // Lowercase
    private BigDecimal price;
    @Basic(optional = false)
    @NotNull
    @Column(name = "createddate") // Lowercase
    @Temporal(TemporalType.DATE)
    private Date createdDate;
    @Column(name = "discount") // Lowercase
    private Integer discount;
    @Column(name = "status") // Lowercase
    private Boolean status;
    @Column(name = "rating") // Lowercase
    private BigDecimal rating;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productID")
    private Collection<Reviews> reviewsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productID")
    private Collection<Productsizecolor> productsizecolorCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productID")
    private Collection<Productimages> productimagesCollection;
    @OneToMany(mappedBy = "productID")
    private Collection<Productviews> productviewsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productID")
    private Collection<ProductCategories> productCategoriesCollection;
    @JoinColumn(name = "companyid", referencedColumnName = "companyid") // Lowercase
    @ManyToOne
    private Company companyID;

    public Products() {
    }

    public Products(Integer productID) {
        this.productID = productID;
    }

    public Products(Integer productID, String productName, BigDecimal price, Date createdDate) {
        this.productID = productID;
        this.productName = productName;
        this.price = price;
        this.createdDate = createdDate;
    }

    public Products(String productName, String description, BigDecimal price, Date createdDate, Integer discount, Boolean status, Collection<Productimages> productimagesCollection) {
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.createdDate = createdDate;
        this.discount = discount;
        this.status = status;
        this.productimagesCollection = productimagesCollection;
    }

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    @XmlTransient
    public Collection<Reviews> getReviewsCollection() {
        return reviewsCollection;
    }

    public void setReviewsCollection(Collection<Reviews> reviewsCollection) {
        this.reviewsCollection = reviewsCollection;
    }

    @XmlTransient
    public Collection<Productsizecolor> getProductsizecolorCollection() {
        return productsizecolorCollection;
    }

    public void setProductsizecolorCollection(Collection<Productsizecolor> productsizecolorCollection) {
        this.productsizecolorCollection = productsizecolorCollection;
    }

    @XmlTransient
    public Collection<Productimages> getProductimagesCollection() {
        return productimagesCollection;
    }

    public void setProductimagesCollection(Collection<Productimages> productimagesCollection) {
        this.productimagesCollection = productimagesCollection;
    }

    @XmlTransient
    public Collection<Productviews> getProductviewsCollection() {
        return productviewsCollection;
    }

    public void setProductviewsCollection(Collection<Productviews> productviewsCollection) {
        this.productviewsCollection = productviewsCollection;
    }

    @XmlTransient
    public Collection<ProductCategories> getProductCategoriesCollection() {
        return productCategoriesCollection;
    }

    public void setProductCategoriesCollection(Collection<ProductCategories> productCategoriesCollection) {
        this.productCategoriesCollection = productCategoriesCollection;
    }

    public Company getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Company companyID) {
        this.companyID = companyID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productID != null ? productID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Products)) {
            return false;
        }
        Products other = (Products) object;
        if ((this.productID == null && other.productID != null) || (this.productID != null && !this.productID.equals(other.productID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.resources.Products[ productID=" + productID + " ]";
    }
}