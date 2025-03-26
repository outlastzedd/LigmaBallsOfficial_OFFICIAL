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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "cartitems") // Lowercase for Postgres
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Cartitems.findAll", query = "SELECT c FROM Cartitems c"),
        @NamedQuery(name = "Cartitems.findByCartItemID", query = "SELECT c FROM Cartitems c WHERE c.cartItemID = :cartItemID"),
        @NamedQuery(name = "Cartitems.findByQuantity", query = "SELECT c FROM Cartitems c WHERE c.quantity = :quantity"),
        @NamedQuery(name = "Cartitems.findByAddedDate", query = "SELECT c FROM Cartitems c WHERE c.addedDate = :addedDate")
})
public class Cartitems implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cartitemid") // Lowercase
    private Integer cartItemID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "quantity") // Lowercase
    private int quantity;
    @Basic(optional = false)
    @NotNull
    @Column(name = "addeddate") // Lowercase
    @Temporal(TemporalType.TIMESTAMP)
    private Date addedDate;
    @JoinColumn(name = "cartid", referencedColumnName = "cartid") // Lowercase
    @ManyToOne(optional = false)
    private Cart cartID;
    @JoinColumn(name = "productsizecolorid", referencedColumnName = "productsizecolorid") // Lowercase
    @ManyToOne(optional = false)
    private Productsizecolor productSizeColorID;

    public Cartitems() {}

    public Cartitems(Integer cartItemID) {
        this.cartItemID = cartItemID;
    }

    public Cartitems(Integer cartItemID, int quantity, Date addedDate) {
        this.cartItemID = cartItemID;
        this.quantity = quantity;
        this.addedDate = addedDate;
    }

    public Integer getCartItemID() {
        return cartItemID;
    }

    public void setCartItemID(Integer cartItemID) {
        this.cartItemID = cartItemID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }

    public Cart getCartID() {
        return cartID;
    }

    public void setCartID(Cart cartID) {
        this.cartID = cartID;
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
        hash += (cartItemID != null ? cartItemID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Cartitems))
            return false;
        Cartitems other = (Cartitems) object;
        if ((this.cartItemID == null && other.cartItemID != null) || (this.cartItemID != null && !this.cartItemID.equals(other.cartItemID)))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "model.resources.Cartitems[ cartItemID=" + cartItemID + " ]";
    }
}