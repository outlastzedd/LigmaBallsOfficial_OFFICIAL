package model;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name = "users") // Lowercase
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Users.selectAll", query = "SELECT u FROM Users u"),
        @NamedQuery(name = "Users.selectByID", query = "SELECT u FROM Users u WHERE u.userID = :userID"),
        @NamedQuery(name = "Users.normalLogin", query = "SELECT u FROM Users u WHERE u.email = :email AND u.password = :password"),
        @NamedQuery(name = "Users.GGLogin", query = "SELECT u FROM Users u WHERE u.email = :email"),
        @NamedQuery(name = "Users.checkExisting", query = "SELECT COUNT(u) FROM Users u WHERE u.email = :email OR u.phoneNumber = :phoneNumber")
})

public class Users implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "userid") // Lowercase
    private Integer userID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "fullname") // Lowercase
    private String fullName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "email") // Lowercase
    private String email;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "password") // Lowercase
    private String password;
    @Size(max = 15)
    @Column(name = "phonenumber") // Lowercase
    private String phoneNumber;
    @Size(max = 100)
    @Column(name = "address") // Lowercase
    private String address;
    @Size(max = 10)
    @Column(name = "role") // Lowercase
    private String role;
    @Column(name = "status") // Lowercase
    private Boolean status;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userID")
    private Collection<Reviews> reviewsCollection;
    @OneToMany(mappedBy = "userID")
    private Collection<Productviews> productviewsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userID")
    private Collection<Orders> ordersCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userID")
    private Collection<Cart> cartCollection;

    public Users() {
    }

    public Users(Integer userID) {
        this.userID = userID;
    }

    public Users(Integer userID, String fullName, String email, String password) {
        this.userID = userID;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }

    public Users(String fullName, String email, String password, String phoneNumber, String role, Boolean status) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.status = status;
    }

    public Users(String fullName, String email, String address, String phoneNumber, String role, Boolean status, String password) {
        this.fullName = fullName;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.status = status;
        this.password = password;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getName() {
        return fullName;
    }

    public void setName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @XmlTransient
    public Collection<Reviews> getReviewsCollection() {
        return reviewsCollection;
    }

    public void setReviewsCollection(Collection<Reviews> reviewsCollection) {
        this.reviewsCollection = reviewsCollection;
    }

    @XmlTransient
    public Collection<Productviews> getProductviewsCollection() {
        return productviewsCollection;
    }

    public void setProductviewsCollection(Collection<Productviews> productviewsCollection) {
        this.productviewsCollection = productviewsCollection;
    }

    @XmlTransient
    public Collection<Orders> getOrdersCollection() {
        return ordersCollection;
    }

    public void setOrdersCollection(Collection<Orders> ordersCollection) {
        this.ordersCollection = ordersCollection;
    }

    @XmlTransient
    public Collection<Cart> getCartCollection() {
        return cartCollection;
    }

    public void setCartCollection(Collection<Cart> cartCollection) {
        this.cartCollection = cartCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userID != null ? userID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.userID == null && other.userID != null) || (this.userID != null && !this.userID.equals(other.userID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.resources.Users[ userID=" + userID + " ]";
    }
}