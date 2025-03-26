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
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "orderdetails") // Lowercase
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Orderdetails.findAll", query = "SELECT o FROM Orderdetails o"),
        @NamedQuery(name = "Orderdetails.findByOrderDetailID", query = "SELECT o FROM Orderdetails o WHERE o.orderDetailID = :orderDetailID"),
        @NamedQuery(name = "Orderdetails.findByQuantity", query = "SELECT o FROM Orderdetails o WHERE o.quantity = :quantity"),
        @NamedQuery(name = "Orderdetails.findByPrice", query = "SELECT o FROM Orderdetails o WHERE o.price = :price")
})
public class Orderdetails implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "orderdetailid") // Lowercase
    private Integer orderDetailID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "quantity") // Lowercase
    private int quantity;
    @Column(name = "price") // Lowercase
    private BigDecimal price;
    @JoinColumn(name = "orderid", referencedColumnName = "orderid") // Lowercase
    @ManyToOne(optional = false)
    private Orders orderID;
    @JoinColumn(name = "productsizecolorid", referencedColumnName = "productsizecolorid") // Lowercase
    @ManyToOne(optional = false)
    private Productsizecolor productSizeColorID;

    public Orderdetails() {
    }

    public Orderdetails(Integer orderDetailID) {
        this.orderDetailID = orderDetailID;
    }

    public Orderdetails(Integer orderDetailID, int quantity) {
        this.orderDetailID = orderDetailID;
        this.quantity = quantity;
    }

    public Integer getOrderDetailID() {
        return orderDetailID;
    }

    public void setOrderDetailID(Integer orderDetailID) {
        this.orderDetailID = orderDetailID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Orders getOrderID() {
        return orderID;
    }

    public void setOrderID(Orders orderID) {
        this.orderID = orderID;
    }

    public Productsizecolor getProductSizeColorID() {
        return productSizeColorID;
    }

    public void setProductSizeColorID(Productsizecolor productSizeColorID) {
        this.productSizeColorID = productSizeColorID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (orderDetailID != null ? orderDetailID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Orderdetails)) {
            return false;
        }
        Orderdetails other = (Orderdetails) object;
        if ((this.orderDetailID == null && other.orderDetailID != null) || (this.orderDetailID != null && !this.orderDetailID.equals(other.orderDetailID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.resources.Orderdetails[ orderDetailID=" + orderDetailID + " ]";
    }
}