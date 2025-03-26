package categoryDAO;

import dao.DBConnection;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import model.Products;

public class CategoryDAO {
    private EntityManager em;

    public CategoryDAO() {
        em = DBConnection.getEntityManager();
    }

    public List<Products> categorizeProductWithWeather(List<Products> products, String condition) {
        String[] hotKeywords
                = {
                    "ngắn", "sơ", "jean", "kaki", "thể thao", "croptop", "sát", "nắng"
                };
        String[] coldKeywords
                = {
                    "khoác", "jean", "kaki", "len", "hoodie", "dài tay"
                };
        List<Products> newProducts = new ArrayList<>();
        for (Products p : products) {
            if (condition.equals("hot")) {
                for (String hotKeyword : hotKeywords) {
                    if (p.getProductName().toLowerCase().contains(hotKeyword)) {
                        newProducts.add(p);
                    }
                }
            } else if (condition.equals("cold")) {
                for (String coldKeyword : coldKeywords) {
                    if (p.getProductName().toLowerCase().contains(coldKeyword)) {
                        newProducts.add(p);
                    }
                }
            }
        }
        return newProducts;
    }

    public List<Products> categorizeProducts(int categoryID, String keyword) {
        TypedQuery<Products> query = em.createNamedQuery("ProductCategories.categorizeProducts", Products.class);
        query.setParameter("categoryID", categoryID);
        query.setParameter("keyword", "%" + keyword + "%");
        return query.getResultList();
    }
}