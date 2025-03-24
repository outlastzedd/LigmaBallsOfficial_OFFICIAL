package controller;

import dao.DBConnection;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import model.Productimages;
import model.Products;
import org.hibernate.Hibernate;
import productDAO.ProductDAO;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/productManager")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB threshold
        maxFileSize = 1024 * 1024 * 10, // 10MB max file size
        maxRequestSize = 1024 * 1024 * 50) // 50MB max request size
public class ProductManagerServlet extends HttpServlet {
    private ProductDAO productDAO;
    private static final String UPLOAD_DIR = "uploads";
    // Fixed path at the same level as src (adjust base path as needed)
    private static final String BASE_PATH = "D:/++ FPTU/PRJ301/LigmaBallsOfficial_V6/images"; // Windows example

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAO();
    }

    // Get the persistent upload path
    private String getUploadPath() {
        String uploadPath = BASE_PATH + "/" + UPLOAD_DIR;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            System.out.println("Creating uploads directory at: " + uploadPath);
            uploadDir.mkdirs();
        }
        return uploadPath;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        List<Products> products = productDAO.selectAllProducts();
        Map<Integer, List<String>> imageUrls = new HashMap<>();
        Map<Integer, Long> totalSoldMap = productDAO.getTotalSoldByProduct();
        Map<Integer, Double> avgRatingMap = productDAO.getAverageRatingByProduct();

        for (Products product : products) {
            Hibernate.initialize(product.getProductimagesCollection());
            List<String> productImageUrls = new ArrayList<>();
            if (product.getProductimagesCollection() != null && !product.getProductimagesCollection().isEmpty()) {
                for (Productimages image : product.getProductimagesCollection()) {
                    productImageUrls.add(image.getImageURL());
                }
            } else {
                productImageUrls.add("N/A");
            }
            imageUrls.put(product.getProductID(), productImageUrls);
        }

        request.setAttribute("products", products);
        request.setAttribute("imageUrls", imageUrls);
        request.setAttribute("totalSoldMap", totalSoldMap);
        request.setAttribute("avgRatingMap", avgRatingMap);

        if (action == null || action.isEmpty()) {
            request.getRequestDispatcher("/ligmaShop/admin/managerProduct.jsp").forward(request, response);
        } else {
            String idParam = request.getParameter("id");
            int id;
            if (idParam == null || idParam.trim().isEmpty()) {
                request.setAttribute("error", "Mã sản phẩm không được để trống.");
                request.getRequestDispatcher("/ligmaShop/admin/managerProduct.jsp").forward(request, response);
                return;
            }

            try {
                id = Integer.parseInt(idParam);
                if (id <= 0) {
                    throw new NumberFormatException("Mã sản phẩm phải là số nguyên dương.");
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Mã sản phẩm không hợp lệ: " + idParam);
                request.getRequestDispatcher("/ligmaShop/admin/managerProduct.jsp").forward(request, response);
                return;
            }

            Products product = productDAO.selectProduct(id);
            if (product == null) {
                request.setAttribute("error", "Không tìm thấy sản phẩm với mã " + id);
                request.getRequestDispatcher("/ligmaShop/admin/managerProduct.jsp").forward(request, response);
                return;
            }

            switch (action) {
                case "deactivate":
                    product.setStatus(false);
                    productDAO.updateProduct(product);
                    request.setAttribute("message", "Đã vô hiệu hóa sản phẩm thành công.");
                    response.sendRedirect(request.getContextPath() + "/productManager");
                    break;
                case "activate":
                    product.setStatus(true);
                    productDAO.updateProduct(product);
                    request.setAttribute("message", "Đã kích hoạt sản phẩm thành công.");
                    response.sendRedirect(request.getContextPath() + "/productManager");
                    break;
                case "delete":
                    productDAO.updateProduct(product);
                    request.setAttribute("message", "Đã xóa sản phẩm thành công.");
                    response.sendRedirect(request.getContextPath() + "/productManager");
                    break;
                case "edit":
                    Hibernate.initialize(product.getProductimagesCollection());
                    request.setAttribute("product", product);
                    request.getRequestDispatcher("/ligmaShop/admin/editProduct.jsp").forward(request, response);
                    break;
                default:
                    request.setAttribute("error", "Hành động không hợp lệ: " + action);
                    response.sendRedirect(request.getContextPath() + "/productManager");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            Products product = new Products();

            String productName = request.getParameter("name");
            String description = request.getParameter("description");
            String priceStr = request.getParameter("price");
            String discountStr = request.getParameter("discount");
            String createdDateStr = request.getParameter("createdDate");
            String statusStr = request.getParameter("status");

            // Validate and set product fields
            if (productName == null || productName.trim().isEmpty()) {
                request.setAttribute("error", "Tên sản phẩm không được để trống.");
                response.sendRedirect(request.getContextPath() + "/productManager");
                return;
            }
            product.setProductName(productName);
            product.setDescription(description);

            BigDecimal price;
            try {
                price = new BigDecimal(priceStr);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Định dạng giá tiền không hợp lệ: " + priceStr);
                response.sendRedirect(request.getContextPath() + "/productManager");
                return;
            }
            product.setPrice(price);

            Integer discount = null;
            if (discountStr != null && !discountStr.trim().isEmpty()) {
                try {
                    discount = Integer.parseInt(discountStr);
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "Định dạng giảm giá không hợp lệ: " + discountStr);
                    response.sendRedirect(request.getContextPath() + "/productManager");
                    return;
                }
            }
            product.setDiscount(discount);

            if (createdDateStr != null && !createdDateStr.isEmpty()) {
                try {
                    product.setCreatedDate(new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(createdDateStr).getTime()));
                } catch (Exception e) {
                    request.setAttribute("error", "Định dạng ngày tạo không hợp lệ: " + createdDateStr);
                    response.sendRedirect(request.getContextPath() + "/productManager");
                    return;
                }
            } else {
                request.setAttribute("error", "Ngày tạo không được để trống.");
                response.sendRedirect(request.getContextPath() + "/productManager");
                return;
            }

            boolean status = Boolean.parseBoolean(statusStr);
            product.setStatus(status);

            // Handle file uploads to fixed path
            String uploadPath = getUploadPath();
            System.out.println("Upload path: " + uploadPath);

            List<Productimages> images = new ArrayList<>();
            Collection<Part> fileParts = request.getParts();
            System.out.println("Total parts found: " + fileParts.size());
            int imageCount = 0;

            for (Part filePart : fileParts) {
                System.out.println("Part name: " + filePart.getName() + ", Size: " + filePart.getSize());
                if (filePart.getName().equals("images") && filePart.getSize() > 0 && imageCount < 3) {
                    String fileName = extractFileName(filePart);
                    if (fileName != null && !fileName.isEmpty()) {
                        String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
                        String filePath = uploadPath + "/" + uniqueFileName;
                        System.out.println("Saving to: " + filePath);
                        try {
                            filePart.write(filePath);
                            System.out.println("File written to: " + filePath);
                        } catch (IOException e) {
                            System.err.println("Failed to write file: " + e.getMessage());
                            request.setAttribute("error", "Failed to upload file: " + e.getMessage());
                            response.sendRedirect(request.getContextPath() + "/productManager");
                            return;
                        }

                        // Store relative URL for web access
                        String imageUrl = "/" + UPLOAD_DIR + "/" + uniqueFileName;
                        Productimages image = new Productimages();
                        image.setImageURL(imageUrl);
                        image.setProductID(product);
                        images.add(image);
                        imageCount++;
                    }
                }
            }
            System.out.println("Total images processed: " + imageCount);

            product.setProductimagesCollection(images);

            // Save to database
            try {
                productDAO.addProduct(product);
                request.setAttribute("message", "Thêm sản phẩm thành công.");
                response.sendRedirect(request.getContextPath() + "/productManager");
            } catch (Exception e) {
                request.setAttribute("error", "Thêm sản phẩm thất bại: " + e.getMessage());
                response.sendRedirect(request.getContextPath() + "/productManager");
            }
        } else if ("update".equals(action)) {
            String idParam = request.getParameter("id");
            int productId;
            try {
                productId = Integer.parseInt(idParam);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Mã sản phẩm không hợp lệ: " + idParam);
                response.sendRedirect(request.getContextPath() + "/productManager");
                return;
            }

            Products product = productDAO.selectProduct(productId);
            if (product == null) {
                request.setAttribute("error", "Không tìm thấy sản phẩm với mã " + productId);
                response.sendRedirect(request.getContextPath() + "/productManager");
                return;
            }

            String productName = request.getParameter("name");
            String description = request.getParameter("description");
            String priceStr = request.getParameter("price");
            String discountStr = request.getParameter("discount");
            String createdDateStr = request.getParameter("createdDate");
            String statusStr = request.getParameter("status");

            if (productName == null || productName.trim().isEmpty()) {
                request.setAttribute("error", "Tên sản phẩm không được để trống.");
                response.sendRedirect(request.getContextPath() + "/productManager");
                return;
            }

            BigDecimal price;
            try {
                price = new BigDecimal(priceStr);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Định dạng giá tiền không hợp lệ: " + priceStr);
                response.sendRedirect(request.getContextPath() + "/productManager");
                return;
            }

            Integer discount = null;
            if (discountStr != null && !discountStr.trim().isEmpty()) {
                try {
                    discount = Integer.parseInt(discountStr);
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "Định dạng giảm giá không hợp lệ: " + discountStr);
                    response.sendRedirect(request.getContextPath() + "/productManager");
                    return;
                }
            }

            boolean status = Boolean.parseBoolean(statusStr);

            product.setProductName(productName);
            product.setDescription(description);
            product.setPrice(price);
            product.setDiscount(discount);
            product.setStatus(status);

            if (createdDateStr != null && !createdDateStr.isEmpty()) {
                try {
                    product.setCreatedDate(new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(createdDateStr).getTime()));
                } catch (Exception e) {
                    request.setAttribute("error", "Định dạng ngày tạo không hợp lệ: " + createdDateStr);
                    response.sendRedirect(request.getContextPath() + "/productManager");
                    return;
                }
            }

            // Handle file uploads for update
            String uploadPath = getUploadPath();
            System.out.println("Upload path: " + uploadPath);

            Hibernate.initialize(product.getProductimagesCollection());
            List<Productimages> existingImages = new ArrayList<>(product.getProductimagesCollection());
            product.getProductimagesCollection().clear();

            Collection<Part> fileParts = request.getParts();
            System.out.println("Total parts found: " + fileParts.size());
            int newImageCount = 0;

            for (Part filePart : fileParts) {
                System.out.println("Part name: " + filePart.getName() + ", Size: " + filePart.getSize());
                if (filePart.getName().equals("images") && filePart.getSize() > 0 && newImageCount < 3) {
                    String fileName = extractFileName(filePart);
                    if (fileName != null && !fileName.isEmpty()) {
                        String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
                        String filePath = uploadPath + File.separator + uniqueFileName;
                        System.out.println("Saving to: " + filePath);
                        try {
                            filePart.write(filePath);
                            System.out.println("File written to: " + filePath);
                        } catch (IOException e) {
                            System.err.println("Failed to write file: " + e.getMessage());
                            request.setAttribute("error", "Failed to upload file: " + e.getMessage());
                            response.sendRedirect(request.getContextPath() + "/productManager");
                            return;
                        }

                        String imageUrl = "/" + UPLOAD_DIR + "/" + uniqueFileName;
                        Productimages newImage = new Productimages();
                        newImage.setImageURL(imageUrl);
                        newImage.setProductID(product);
                        product.getProductimagesCollection().add(newImage);
                        newImageCount++;
                    }
                }
            }
            System.out.println("Total new images processed: " + newImageCount);

            if (newImageCount == 0 && !existingImages.isEmpty()) {
                product.getProductimagesCollection().addAll(existingImages);
            }

            try {
                productDAO.updateProduct(product);
                request.setAttribute("message", "Cập nhật sản phẩm thành công.");
                response.sendRedirect(request.getContextPath() + "/productManager");
            } catch (Exception e) {
                request.setAttribute("error", "Cập nhật sản phẩm thất bại: " + e.getMessage());
                response.sendRedirect(request.getContextPath() + "/productManager");
            }
        }
    }

    // Utility method to extract file name from Part
    private String extractFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        if (contentDisposition == null) {
            return null;
        }
        String[] items = contentDisposition.split(";");
        for (String item : items) {
            if (item.trim().startsWith("filename")) {
                return item.substring(item.indexOf("=") + 2, item.length() - 1);
            }
        }
        return null;
    }
}