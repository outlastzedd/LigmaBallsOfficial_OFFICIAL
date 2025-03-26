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
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "reviews") // Lowercase
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Reviews.findAll", query = "SELECT r FROM Reviews r"),
        @NamedQuery(name = "Reviews.findByReviewID", query = "SELECT r FROM Reviews r WHERE r.reviewID = :reviewID"),
        @NamedQuery(name = "Reviews.findByRating", query = "SELECT r FROM Reviews r WHERE r.rating = :rating"),
        @NamedQuery(name = "Reviews.findByComment", query = "SELECT r FROM Reviews r WHERE r.comment = :comment"),
        @NamedQuery(name = "Reviews.findByReviewDate", query = "SELECT r FROM Reviews r WHERE r.reviewDate = :reviewDate")
})
public class Reviews implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "reviewid") // Lowercase
    private Integer reviewID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rating") // Lowercase
    private int rating;
    @Size(max = 500)
    @Column(name = "comment") // Lowercase
    private String comment;
    @Basic(optional = false)
    @NotNull
    @Column(name = "reviewdate") // Lowercase
    @Temporal(TemporalType.DATE)
    private Date reviewDate;
    @JoinColumn(name = "productid", referencedColumnName = "productid") // Lowercase
    @ManyToOne(optional = false)
    private Products productID;
    @JoinColumn(name = "userid", referencedColumnName = "userid") // Lowercase
    @ManyToOne(optional = false)
    private Users userID;

    public Reviews() {
    }

    public Reviews(Integer reviewID) {
        this.reviewID = reviewID;
    }

    public Reviews(Integer reviewID, int rating, Date reviewDate) {
        this.reviewID = reviewID;
        this.rating = rating;
        this.reviewDate = reviewDate;
    }

    public Integer getReviewID() {
        return reviewID;
    }

    public void setReviewID(Integer reviewID) {
        this.reviewID = reviewID;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public Products getProductID() {
        return productID;
    }

    public void setProductID(Products productID) {
        this.productID = productID;
    }

    public Users getUserID() {
        return userID;
    }

    public void setUserID(Users userID) {
        this.userID = userID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reviewID != null ? reviewID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Reviews)) {
            return false;
        }
        Reviews other = (Reviews) object;
        if ((this.reviewID == null && other.reviewID != null) || (this.reviewID != null && !this.reviewID.equals(other.reviewID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.resources.Reviews[ reviewID=" + reviewID + " ]";
    }
}