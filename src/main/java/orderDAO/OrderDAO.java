/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package orderDAO;

import dao.DBConnection;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import model.OrderDayData;
import model.Orderdetails;
import model.Orders;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ADMIN
 */
public class OrderDAO implements IOrderDAO {

    private EntityManager em;

    public OrderDAO() {
        em = DBConnection.getEntityManager();
    }

    @Override
    public int countUser() throws NoResultException {
        Long count = (Long) em.createNamedQuery("Orders.countUsers").getSingleResult();
        return count.intValue(); // hoặc return count nếu bạn muốn kiểu Long
    }

    public List<OrderDayData> getOrderCountsByDay(String startDateStr, String endDateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse(startDateStr);
        Date endDate = sdf.parse(endDateStr);
        String jpql = "SELECT new model.OrderDayData(DAY(o.orderDate), COUNT(o)) " +
                "FROM Orders o " +
                "WHERE o.orderDate BETWEEN :startDate AND :endDate " +
                "GROUP BY DAY(o.orderDate) " +
                "ORDER BY DAY(o.orderDate)";
        TypedQuery<OrderDayData> query = em.createQuery(jpql, OrderDayData.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return query.getResultList();
    }

    public double getTotalRevenue(String startDateStr, String endDateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse(startDateStr);
        Date endDate = sdf.parse(endDateStr);
        String jpql = "SELECT SUM(o.totalAmount) FROM Orders o " +
                "WHERE o.orderDate BETWEEN :startDate AND :endDate";
        TypedQuery<BigDecimal> query = em.createQuery(jpql, BigDecimal.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        BigDecimal result = query.getSingleResult();
        return result != null ? result.doubleValue() : 0.0;
    }

    public int getTotalBuyers(String startDateStr, String endDateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse(startDateStr);
        Date endDate = sdf.parse(endDateStr);
        String jpql = "SELECT COUNT(DISTINCT o.userID) FROM Orders o " +
                "WHERE o.orderDate BETWEEN :startDate AND :endDate";
        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        Long result = query.getSingleResult();
        return result != null ? result.intValue() : 0;
    }

    @Override
    public List<Orderdetails> getAllOrderDetails() {
        TypedQuery<Orderdetails> query = em.createNamedQuery("Orderdetails.findAll", Orderdetails.class);
        return query.getResultList();
    }

    @Override
    public List<Orderdetails> getOrderDetailsByUserId(int userID) {
        TypedQuery<Orderdetails> query = em.createQuery(
                "SELECT od FROM Orderdetails od WHERE od.orderID.userID.userID = :userID", Orderdetails.class);
        query.setParameter("userID", userID);
        return query.getResultList();
    }

    @Override
    public List<Orders> getAllOrders() {
        TypedQuery<Orders> query = em.createQuery("SELECT o FROM Orders o ORDER BY o.orderDate DESC", Orders.class);
        return query.getResultList();
    }

    @Override
    public List<Orders> getOrdersByPaymentMethod(int paymentMethodID) {
        TypedQuery<Orders> query = em.createQuery(
                "SELECT o FROM Orders o WHERE o.paymentMethodID.paymentMethodID = :paymentMethodID ORDER BY o.orderDate DESC", Orders.class);
        query.setParameter("paymentMethodID", paymentMethodID);
        return query.getResultList();
    }

    @Override
    public List<Orders> searchOrdersById(int searchId) {
        TypedQuery<Orders> query = em.createQuery(
                "SELECT o FROM Orders o WHERE o.orderID = :searchId OR o.userID.userID = :searchId ORDER BY o.orderDate DESC", Orders.class);
        query.setParameter("searchId", searchId);
        return query.getResultList();
    }

    @Override
    public List<Orders> searchOrdersByName(String name) {
        TypedQuery<Orders> query = em.createQuery(
                "SELECT o FROM Orders o JOIN FETCH o.userID u JOIN FETCH o.paymentMethodID WHERE u.fullName LIKE :name ORDER BY o.orderDate DESC", Orders.class);
        query.setParameter("name", "%" + name + "%"); // Tìm kiếm gần đúng
        return query.getResultList();
    }

    @Override
    public Orders getOrderDetailsById(int orderId) {
        try {
            TypedQuery<Orders> query = em.createQuery(
                    "SELECT o FROM Orders o " +
                            "JOIN FETCH o.userID " +
                            "JOIN FETCH o.paymentMethodID " +
                            "JOIN FETCH o.orderdetailsCollection od " +
                            "JOIN FETCH od.productSizeColorID psc " +
                            "JOIN FETCH psc.productID " +
                            "WHERE o.orderID = :orderId", Orders.class);
            query.setParameter("orderId", orderId);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
