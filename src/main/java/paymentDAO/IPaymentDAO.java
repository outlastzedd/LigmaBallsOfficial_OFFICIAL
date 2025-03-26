
package paymentDAO;

import java.math.BigDecimal;
import java.util.Map;
import model.Cart;

public interface IPaymentDAO {
    void processPayment(Map<String, String> pendingTransaction, String transactionId, BigDecimal totalAmount, Cart cart, int paymentMethodID);
}
