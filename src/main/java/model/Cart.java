/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author Asus-FPT
 */
@Entity
@Table(name = "CART")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Cart.findAll", query = "SELECT c FROM Cart c"),
    @NamedQuery(name = "Cart.findByCartID", query = "SELECT c FROM Cart c WHERE c.cartID = :cartID"),
    @NamedQuery(name = "Cart.findByCreatedDate", query = "SELECT c FROM Cart c WHERE c.createdDate = :createdDate")
})
public class Cart implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CartID")
    private Integer cartID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CreatedDate")
    @Temporal(TemporalType.DATE)
    private Date createdDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cartID")
    private Collection<Cartitems> cartitemsCollection;
    @JoinColumn(name = "UserID", referencedColumnName = "UserID")
    @ManyToOne(optional = false)
    private Users userID;

    public Cart()
    {
    }

    public Cart(Integer cartID)
    {
        this.cartID = cartID;
    }

    public Cart(Integer cartID, Date createdDate)
    {
        this.cartID = cartID;
        this.createdDate = createdDate;
    }

    public Integer getCartID()
    {
        return cartID;
    }

    public void setCartID(Integer cartID)
    {
        this.cartID = cartID;
    }

    public Date getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate)
    {
        this.createdDate = createdDate;
    }

    @XmlTransient
    public Collection<Cartitems> getCartitemsCollection()
    {
        return cartitemsCollection;
    }

    public void setCartitemsCollection(Collection<Cartitems> cartitemsCollection)
    {
        this.cartitemsCollection = cartitemsCollection;
    }

    public Users getUserID()
    {
        return userID;
    }

    public void setUserID(Users userID)
    {
        this.userID = userID;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (cartID != null ? cartID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cart))
        {
            return false;
        }
        Cart other = (Cart) object;
        if ((this.cartID == null && other.cartID != null) || (this.cartID != null && !this.cartID.equals(other.cartID)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "model.resources.Cart[ cartID=" + cartID + " ]";
    }
    
}
