/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package inventoryDAO;

import java.util.List;
import model.ProductStockInfo;

/**
 *
 * @author lapto
 */
public interface IInventoryDAO {
    public List<ProductStockInfo> getProductStockInfo();
}
