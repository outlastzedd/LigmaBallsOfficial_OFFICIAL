package inventoryDAO;

import dao.DBConnection;
import jakarta.persistence.EntityManager;
import model.ProductStockInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryDAO implements IInventoryDAO {
    private EntityManager em;
    public InventoryDAO() {
        em = DBConnection.getEntityManager();
    }

    @Override
    public List<ProductStockInfo> getProductStockInfo() {
        return em.createQuery(
                "SELECT NEW model.ProductStockInfo(" +
                        "psc.productSizeColorID, " +
                        "psc.productID.productID, " +
                        "psc.sizeID.sizeID, " +
                        "COALESCE(psc.price, psc.productID.price), " +
                        "i.stock, " +
                        "FUNCTION('TIMESTAMP', i.lastUpdated), " + // Convert Date to LocalDateTime
                        "COALESCE(SUM(od.quantity), 0)) " +
                        "FROM Productsizecolor psc " +
                        "LEFT JOIN psc.inventoryCollection i " +
                        "LEFT JOIN psc.orderdetailsCollection od " +
                        "GROUP BY psc.productSizeColorID, psc.productID.productID, psc.sizeID.sizeID, psc.price, psc.productID.price, i.stock, i.lastUpdated",
                ProductStockInfo.class
        ).getResultList();
    }

    public Map<Integer, ProductStockInfo> getProductStockMap() {
        Map<Integer, ProductStockInfo> stockMap = new HashMap<>();
        for (ProductStockInfo info : getProductStockInfo()) {
            stockMap.put(info.getProductSizeColorID(), info);
        }
        return stockMap;
    }

    public static void main(String[] args) {
        InventoryDAO dao = new InventoryDAO();
        List<ProductStockInfo> list = dao.getProductStockInfo();
        for (ProductStockInfo info : list) {
            System.out.println(info.getProductSizeColorID());
        }
    }
}
