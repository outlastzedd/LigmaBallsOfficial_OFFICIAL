package model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Entity
@Table(name = "productimages") // Lowercase
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Productimages.findAll", query = "SELECT p FROM Productimages p"),
        @NamedQuery(name = "Productimages.findByImageID", query = "SELECT p FROM Productimages p WHERE p.imageID = :imageID")
})
public class Productimages implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "imageid") // Lowercase
    private Integer imageID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "imageurl") // Lowercase
    private String imageURL;
    @JoinColumn(name = "productid", referencedColumnName = "productid") // Lowercase
    @ManyToOne(optional = false)
    private Products productID;

    public Productimages() {
    }

    public Productimages(Integer imageID) {
        this.imageID = imageID;
    }

    public Productimages(Integer imageID, String imageURL) {
        this.imageID = imageID;
        this.imageURL = imageURL;
    }

    public Integer getImageID() {
        return imageID;
    }

    public void setImageID(Integer imageID) {
        this.imageID = imageID;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
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
        hash += (imageID != null ? imageID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Productimages)) {
            return false;
        }
        Productimages other = (Productimages) object;
        if ((this.imageID == null && other.imageID != null) || (this.imageID != null && !this.imageID.equals(other.imageID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.resources.Productimages[ imageID=" + imageID + " ]";
    }
}