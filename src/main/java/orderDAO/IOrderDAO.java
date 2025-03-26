/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package orderDAO;

import jakarta.persistence.NoResultException;
import model.Orderdetails;
import model.Orders;

import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface IOrderDAO {
    
    public int countUser() throws NoResultException;

    List<Orderdetails> getAllOrderDetails();

    List<Orderdetails> getOrderDetailsByUserId(int userID);

    List<Orders> getAllOrders();

    List<Orders> getOrdersByPaymentMethod(int paymentMethodID);

    List<Orders> searchOrdersById(int searchId);

    List<Orders> searchOrdersByName(String name);

    Orders getOrderDetailsById(int orderId);
}
