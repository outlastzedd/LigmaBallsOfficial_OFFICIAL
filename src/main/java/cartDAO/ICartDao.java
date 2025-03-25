package cartDAO;

import java.util.List;
import model.Cart;
import model.Cartitems;
import model.Products;
import model.Productsizecolor;
import model.Users;

public interface ICartDao {

      void saveCart(Cart cart);

      Cart getCartByUser(Users user);

      // Moved from CartServlet.java
      void addToCart(Cart cart, int productSizeColorID, int quantity);

      // Moved from CartServlet.java
      void updateCartItem(Cart cart, int cartItemId, int newQuantity);

      // Moved from CartServlet.java
      void clearCartItems(Cart cart, int cartItemId);
}
