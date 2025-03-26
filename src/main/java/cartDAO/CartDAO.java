package cartDAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import model.Cart;
import model.Cartitems;
import model.Productsizecolor;
import dao.DBConnection;
import model.Users;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class CartDAO implements ICartDao {

    private EntityManager em;

    public CartDAO() {
        em = DBConnection.getEntityManager();
    }

    @Override
    public void saveCart(Cart cart) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            if (cart.getCartID() == null) {
                em.persist(cart); // Insert new cart
            } else {
                em.merge(cart); // Update existing cart
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error saving cart: " + e.getMessage(), e);
        }
    }

    @Override
    public Cart getCartByUser(Users user) {
        try {
            em.clear(); // Clear first-level cache
            TypedQuery<Cart> query = em.createNamedQuery(
                    "Cart.getCartByUser", Cart.class);
            query.setParameter("user", user);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching cart: " + e.getMessage(), e);
        }
    }

    public void clearCartItems(Cart cart) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            int deletedCount = em.createNamedQuery("Cart.clearCartItems")
                    .setParameter("cart", cart)
                    .executeUpdate();
            transaction.commit();
            System.out.println("Deleted " + deletedCount + " cart items for cart ID: " + cart.getCartID());
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error clearing cart items: " + e.getMessage(), e);
        }
    }

    // Moved from CartServlet.java
    @Override
    public void addToCart(Cart cart, int productSizeColorID, int quantity) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Productsizecolor productSizeColor = em.find(Productsizecolor.class, productSizeColorID);
            if (productSizeColor == null) {
                throw new IllegalArgumentException("Product with ID " + productSizeColorID + " not found.");
            }
            if (quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than 0.");
            }

            Collection<Cartitems> cartItems = cart.getCartitemsCollection();
            if (cartItems == null) {
                cartItems = new ArrayList<>();
                cart.setCartitemsCollection(cartItems);
            }

            boolean itemExists = false;
            for (Cartitems item : cartItems) {
                if (item.getProductSizeColorID().getProductSizeColorID().equals(productSizeColorID)) {
                    item.setQuantity(item.getQuantity() + quantity);
                    em.merge(item);
                    itemExists = true;
                    break;
                }
            }

            if (!itemExists) {
                Cartitems cartItem = new Cartitems();
                cartItem.setCartID(cart);
                cartItem.setProductSizeColorID(productSizeColor);
                cartItem.setQuantity(quantity);
                cartItem.setAddedDate(new Date());
                cartItems.add(cartItem);
                em.persist(cartItem);
            }

            em.merge(cart); // Ensure cart is updated
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error adding to cart: " + e.getMessage(), e);
        }
    }

    // Moved from CartServlet.java
    @Override
    public void updateCartItem(Cart cart, int cartItemId, int newQuantity) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Collection<Cartitems> cartItems = cart.getCartitemsCollection();
            if (cartItems == null || cartItems.isEmpty()) {
                throw new IllegalStateException("Cart is empty.");
            }

            Cartitems itemToUpdate = null;
            for (Cartitems item : cartItems) {
                if (item.getCartItemID().equals(cartItemId)) {
                    itemToUpdate = item;
                    break;
                }
            }

            if (itemToUpdate == null) {
                throw new IllegalArgumentException("Cart item with ID " + cartItemId + " not found.");
            }

            if (newQuantity > 0) {
                itemToUpdate.setQuantity(newQuantity);
                em.merge(itemToUpdate);
            } else {
                em.remove(itemToUpdate);
                cartItems.remove(itemToUpdate);
            }

            em.merge(cart); // Ensure cart is updated
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error updating cart item: " + e.getMessage(), e);
        }
    }

    // Moved from CartServlet.java
    @Override
    public void clearCartItems(Cart cart, int cartItemId) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Collection<Cartitems> cartItems = cart.getCartitemsCollection();
            if (cartItems != null) {
                Cartitems itemToRemove = null;
                for (Cartitems item : cartItems) {
                    if (item.getCartItemID().equals(cartItemId)) {
                        itemToRemove = item;
                        break;
                    }
                }
                if (itemToRemove != null) {
                    cartItems.remove(itemToRemove);
                    em.remove(em.merge(itemToRemove)); // Merge then remove to ensure entity is managed
                } else {
                    throw new IllegalArgumentException("Cart item with ID " + cartItemId + " not found in cart.");
                }
            }
            em.merge(cart); // Ensure cart is updated
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error removing cart item: " + e.getMessage(), e);
        }
    }
}