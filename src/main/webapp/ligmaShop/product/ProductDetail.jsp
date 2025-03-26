<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="productDAO" class="productDAO.ProductDAO" scope="page"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ligma Shop - ${product.productName}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/styleProductDetails.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/grid.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/thanhToan.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/reviewCSS.css">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resource/images/logoLigma.png"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
    <style>
        .home-product-item__img {
            height: 20px;
            aspect-ratio: 1 / 1;
            background-size: cover;
            background-position: center;
            border-radius: 5px;
        }

        .stock {
            margin: 15px 0;
            display: flex;
            align-items: center;
        }

        #quantity-stock {
            width: 100px;
            padding: 8px 12px;
            border: 1px solid #ddd;
            border-radius: 4px;
            text-align: center;
            font-size: 16px;
            background-color: #f9f9f9;
            color: #333;
        }

        #quantity-stock.low-stock {
            border-color: #ff9800;
            background-color: #fff3e0;
            color: #ff9800;
        }

        #quantity-stock.out-of-stock {
            border-color: #ff4444;
            background-color: #ffe6e6;
            color: #ff4444;
        }

        .label-in-stock {
            background-color: #4caf50;
            color: white;
            padding: 2px 8px;
            border-radius: 3px;
            font-size: 12px;
            margin-right: 5px;
            display: inline-block;
        }
    </style>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/chat.css">
    <script src="${pageContext.request.contextPath}/resource/js/chatbox.js" defer></script>
    <!--        <script src="https://www.gstatic.com/dialogflow-console/fast/messenger/bootstrap.js?v=1"></script>-->
    <df-messenger
            intent="WELCOME"
            chat-title="LigmaShop"
            agent-id="42e42aa4-d213-4073-8915-61238c1db98f"
            language-code="vi">
    </df-messenger>
</head>

<body>
<!--chatbox-->
<div id="chat-toggle">💬</div>
<div id="chatbox" class="minimized">
    <div id="messages">
        <div class="message bot-message">Chào bạn! Hỏi mình về sản phẩm nhé!</div>
    </div>
    <div id="input-container">
        <input id="input" type="text" placeholder="Nhập câu hỏi..." onkeydown="if (event.key === 'Enter')
                        sendMessage()">
        <button id="sendButton" onclick="sendMessage()">Gửi</button>
    </div>
</div>
<div class="app">
    <%@ include file="../header/header.jsp" %>

    <%-- Preload Products --%>
    <c:set var="products" value="${productDAO.selectAllProducts()}"/>
    <c:set var="product" value="${singleProduct}"/>
    <div class="container1">
        <div class="product-container">
            <div class="image-container-box">
                <div class="product-image-container">
                    <div class="product-image">
                        <img id="mainImage" src="${product.productimagesCollection[0].imageURL}"
                             alt="${product.productName} Image">
                    </div>
                    <div class="image-overlay">
                        <span class="close-btn">&times;</span>
                        <img class="full-image" src="" alt="Ảnh đầy đủ">
                    </div>
                    <div class="image-box">
                        <div class="additional-images">
                            <c:forEach var="image" items="${product.productimagesCollection}" varStatus="status">
                                <img class="thumbnail" src="${image.imageURL}" alt="Product Thumbnail ${status.count}">
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>

            <div class="product-info">
                <!-- Set initial stockInfo based on default size -->
                <c:if test="${not empty product.productsizecolorCollection && isPriceValid}">
                    <c:forEach var="sizeColor" items="${product.productsizecolorCollection}">
                        <c:if test="${sizeColor.productSizeColorID == defaultProductSizeColorID}">
                            <c:set var="stockInfo" value="${stockMap[sizeColor.productSizeColorID]}"/>
                            <c:set var="stock" value="${stockInfo != null ? stockInfo.stock : 'N/A'}"/>
                        </c:if>
                    </c:forEach>
                </c:if>

                <div class="labels">
                    <span class="label-sale">SALE</span>
                    <span class="label-hot">HOT</span>
                    <c:if test="${stockInfo != null && stockInfo.stock > 0}">
                        <span class="label-stock in-stock">IN STOCK</span>
                    </c:if>
                </div>
                <h1>${product.productName}</h1>
                <c:set var="ratingMap" value="${productDAO.getAverageRatingByProduct()}"/>
                <div class="rating">
                    <c:set var="avgRating"
                           value="${ratingMap[product.productID] != null ? ratingMap[product.productID] : 0}"/>
                    <c:forEach begin="1" end="5" var="i">
                        <c:choose>
                            <c:when test="${i <= avgRating}">
                                <i class="home-product-item__star-gold fas fa-star"></i>
                            </c:when>
                            <c:otherwise>
                                <i class="fa-regular fa-star"></i>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </div>
                <div class="features">
                    <ul>
                        <li>${product.description}</li>
                    </ul>
                </div>

                <fmt:formatNumber var="formattedPriceOriginal" value="${product.price}" type="number"
                                  groupingUsed="false"/>
                <fmt:formatNumber var="formattedPrice" value="${product.price-(product.price*product.discount/100)}"
                                  type="number" groupingUsed="false"/>
                <span id="formattedPrice" style="display: none;">${formattedPrice}</span>
                <span id="formattedPriceOriginal" style="display: none;">${formattedPriceOriginal}</span>

                <c:set var="isPriceValid" value="true"/>
                <c:catch var="exception">
                    <c:set var="testPrice" value="${product.price + 0}"/>
                </c:catch>
                <c:if test="${exception != null}">
                    <c:set var="isPriceValid" value="false"/>
                </c:if>

                <c:if test="${not empty product.productsizecolorCollection && isPriceValid}">
                    <c:set var="uniqueSizes" value=""/>
                    <c:set var="firstSize" value="true"/>
                    <c:set var="foundSizeL" value="false"/>
                    <c:forEach var="sizeColor" items="${product.productsizecolorCollection}">
                        <c:if test="${not fn:contains(uniqueSizes, sizeColor.sizeID.sizeName)}">
                            <c:set var="uniqueSizes" value="${uniqueSizes},${sizeColor.sizeID.sizeName}"/>
                            <c:set var="priceAdjustment" value="0"/>
                            <c:if test="${sizeColor.sizeID.sizeName == 'XL'}">
                                <c:set var="priceAdjustment" value="50000"/>
                            </c:if>
                            <c:if test="${sizeColor.sizeID.sizeName == 'XXL'}">
                                <c:set var="priceAdjustment" value="100000"/>
                            </c:if>
                            <c:if test="${product.price != null}">
                                <c:set var="adjustedPrice" value="${product.price + priceAdjustment}"/>
                            </c:if>
                            <c:if test="${product.price == null}">
                                <c:set var="adjustedPrice" value="0"/>
                            </c:if>
                            <c:if test="${sizeColor.sizeID.sizeName == 'L' && !foundSizeL}">
                                <c:set var="defaultSize" value="${sizeColor.sizeID.sizeName}"/>
                                <c:set var="defaultPrice" value="${adjustedPrice}"/>
                                <c:set var="defaultProductSizeColorID" value="${sizeColor.productSizeColorID}"/>
                                <c:set var="foundSizeL" value="true"/>
                                <c:set var="firstSize" value="false"/>
                            </c:if>
                            <c:if test="${firstSize}">
                                <c:set var="defaultSize" value="${sizeColor.sizeID.sizeName}"/>
                                <c:set var="defaultPrice" value="${adjustedPrice}"/>
                                <c:set var="defaultProductSizeColorID" value="${sizeColor.productSizeColorID}"/>
                                <c:set var="firstSize" value="false"/>
                            </c:if>
                        </c:if>
                    </c:forEach>
                </c:if>
                <c:if test="${empty product.productsizecolorCollection && isPriceValid}">
                    <c:if test="${product.price != null}">
                        <c:set var="defaultPrice" value="${product.price}"/>
                        <c:set var="defaultSize" value="N/A"/>
                        <c:set var="defaultProductSizeColorID" value="0"/>
                    </c:if>
                </c:if>

                <div class="price">
                    <c:if test="${product.price != null && defaultPrice != null && isPriceValid}">
                        <c:set var="discount" value="${product.discount != null ? product.discount : 0}"/>
                        <c:set var="discountedPrice" value="${defaultPrice - (defaultPrice * discount / 100)}"/>
                        <span class="price">
                            <fmt:formatNumber value="${discountedPrice}" type="number" groupingUsed="true"/> ₫
                            <span id="displayPrice" style="display: none;">${discountedPrice}</span>
                        </span>
                    </c:if>
                    <c:if test="${product.price == null || defaultPrice == null || !isPriceValid}">
                        <p>Không có giá hợp lệ cho sản phẩm này. Vui lòng kiểm tra lại dữ liệu.</p>
                    </c:if>
                </div>

                <div class="size-selector">
                    <c:if test="${not empty product.productsizecolorCollection && isPriceValid}">
                        <c:set var="uniqueSizes" value=""/>
                        <c:forEach var="sizeColor" items="${product.productsizecolorCollection}">
                            <c:if test="${not fn:contains(uniqueSizes, sizeColor.sizeID.sizeName)}">
                                <c:set var="uniqueSizes" value="${uniqueSizes},${sizeColor.sizeID.sizeName}"/>
                                <c:set var="sizeStockInfo" value="${stockMap[sizeColor.productSizeColorID]}"/>
                                <c:set var="priceAdjustment" value="0"/>
                                <c:if test="${sizeColor.sizeID.sizeName == 'XL'}">
                                    <c:set var="priceAdjustment" value="50000"/>
                                </c:if>
                                <c:if test="${sizeColor.sizeID.sizeName == 'XXL'}">
                                    <c:set var="priceAdjustment" value="100000"/>
                                </c:if>
                                <c:if test="${product.price != null}">
                                    <c:set var="adjustedPrice" value="${product.price + priceAdjustment}"/>
                                </c:if>
                                <c:if test="${product.price == null}">
                                    <c:set var="adjustedPrice" value="0"/>
                                </c:if>
                                <button class="size-option ${sizeColor.sizeID.sizeName == defaultSize ? 'selected' : ''}"
                                        data-size="${sizeColor.sizeID.sizeName}"
                                        data-price="${adjustedPrice}"
                                        data-stock="${sizeStockInfo != null ? sizeStockInfo.stock : 'N/A'}"
                                        data-product-size-color-id="${sizeColor.productSizeColorID}">
                                        ${sizeColor.sizeID.sizeName}
                                </button>
                            </c:if>
                        </c:forEach>

                        <script>
                            document.addEventListener('DOMContentLoaded', function () {
                                const sizeButtons = document.querySelectorAll('.size-option');
                                const quantityStock = document.getElementById('quantity-stock');
                                const inStockLabel = document.querySelector('.label-in-stock');

                                sizeButtons.forEach(button => {
                                    button.addEventListener('click', function () {
                                        const stock = this.getAttribute('data-stock') !== 'N/A' ?
                                            parseInt(this.getAttribute('data-stock')) : 0;
                                        quantityStock.value = stock;
                                        quantityStock.classList.remove('low-stock', 'out-of-stock');
                                        if (stock <= 5 && stock > 0) {
                                            quantityStock.classList.add('low-stock');
                                        } else if (stock === 0) {
                                            quantityStock.classList.add('out-of-stock');
                                        }
                                        if (inStockLabel) {
                                            inStockLabel.style.display = stock > 0 ? 'inline-block' : 'none';
                                        }
                                        sizeButtons.forEach(btn => btn.classList.remove('selected'));
                                        this.classList.add('selected');
                                    });
                                });

                                const initialStock = parseInt(quantityStock.value) || 0;
                                quantityStock.classList.remove('low-stock', 'out-of-stock');
                                if (initialStock <= 5 && initialStock > 0) {
                                    quantityStock.classList.add('low-stock');
                                } else if (initialStock === 0) {
                                    quantityStock.classList.add('out-of-stock');
                                }
                                if (inStockLabel) {
                                    inStockLabel.style.display = initialStock > 0 ? 'inline-block' : 'none';
                                }
                            });
                        </script>
                    </c:if>
                    <c:if test="${empty product.productsizecolorCollection || !isPriceValid}">
                        <p>Không có kích thước nào cho sản phẩm này hoặc giá không hợp lệ.</p>
                    </c:if>
                </div>

                <div class="quantity-box">
                    <button class="decrease">-</button>
                    <input type="text" id="displayQuantity" value="1" min="1" required>
                    <button class="increase">+</button>
                </div>

                <div class="stock">
                    <label for="quantity-stock" style="font-size: 1.5em; padding-right: 2px">Có sẵn: </label>
                    <input type="number" id="quantity-stock"
                           class="${stockInfo != null && stockInfo.stock <= 5 ? (stockInfo.stock > 0 ? 'low-stock' : 'out-of-stock') : ''}"
                           value="${stockInfo != null ? stockInfo.stock : '0'}"
                           readonly>
                </div>

                <div class="button-container">
                    <c:if test="${not empty defaultPrice}">
                        <c:if test="${product.price != null && defaultPrice != null}">
                            <form action="${pageContext.request.contextPath}/cart" method="post">
                                <input type="hidden" name="action" value="add">
                                <input type="hidden" name="imageURL" id="selectedImageURL"
                                       value="${product.productimagesCollection[0].imageURL}">
                                <input type="hidden" name="productName" value="${product.productName}">
                                <input type="hidden" name="productSizeColorID" id="selectedProductSizeColorID"
                                       value="${defaultProductSizeColorID}">
                                <input type="hidden" name="price" id="selectedPrice" value="${defaultPrice}">
                                <input type="hidden" name="discountedPrice" id="selectedDiscountedPrice"
                                       value="${discountedPrice}">
                                <input type="hidden" name="size" id="selectedSize" value="${defaultSize}">
                                <input type="hidden" name="quantity" id="selectedQuantity" value="1">
                                <button type="submit" class="btn-add">
                                    <i class="fas fa-shopping-cart"></i> Thêm Vào Giỏ
                                </button>
                            </form>
                            <form action="${pageContext.request.contextPath}/cart" method="post">
                                <input type="hidden" name="action" value="buynow">
                                <input type="hidden" name="imageURL" id="buyNowImageURL"
                                       value="${product.productimagesCollection[0].imageURL}">
                                <input type="hidden" name="productName" value="${product.productName}">
                                <input type="hidden" name="productSizeColorID" id="buyNowProductSizeColorID"
                                       value="${defaultProductSizeColorID}">
                                <input type="hidden" name="price" id="buyNowPrice" value="${defaultPrice}">
                                <input type="hidden" name="discountedPrice" id="buyNowDiscountedPrice"
                                       value="${discountedPrice}">
                                <input type="hidden" name="size" id="buyNowSize" value="${defaultSize}">
                                <input type="hidden" name="quantity" id="buyNowQuantity" value="1">
                                <button type="submit" class="btn-add">
                                    <i class="fas fa-shopping-cart"></i> Mua ngay
                                </button>
                            </form>
                        </c:if>
                    </c:if>
                </div>
            </div>
        </div>

        <hr class="separator">

        <div class="review-container">
            <h2>Đánh Giá Sản Phẩm</h2>
            <c:choose>
                <c:when test="${not empty sessionScope.user}">
                    <form action="reviewservlet" method="post">
                        <input type="hidden" name="action" value="add">
                        <input type="hidden" name="productID" value="${product.productID}">
                        <div class="review-stars">
                            <c:forEach begin="1" end="5" var="rating">
                                <input type="radio" name="rating" value="${rating}" id="star${rating}">
                                <label for="star${rating}">★</label>
                            </c:forEach>
                        </div>
                        <button type="submit" class="btn">Gửi Đánh Giá</button>
                    </form>
                </c:when>
                <c:otherwise>
                    <p><strong>Bạn cần <a href="${pageContext.request.contextPath}/ligmaShop/login/signIn.jsp">đăng
                        nhập</a> để gửi đánh giá.</strong></p>
                </c:otherwise>
            </c:choose>
            <br>
            <div class="review-list">
                <c:if test="${empty reviews}">
                    <p>Chưa có đánh giá nào.</p>
                </c:if>
                <c:forEach var="review" items="${reviews}">
                    <div class="review-item">
                        <img src="${pageContext.request.contextPath}/resource/images/avtKhachHang.jpg" alt="Avatar"
                             class="avatar">
                        <p>${review.userID.getName()}</p>
                        <p>
                            <c:forEach begin="1" end="5" var="i">
                                <c:choose>
                                    <c:when test="${i <= review.rating}">
                                        <i class="fas fa-star"></i>
                                    </c:when>
                                    <c:otherwise>
                                        <i class="far fa-star"></i>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </p>
                        <p>${review.comment}</p>
                        <p>${review.reviewDate}</p>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>

    <hr class="separator">

    <div class="container2">
        <h2>Có thể bạn sẽ thích</h2>
        <div class="suggested-products-wrapper">
            <button class="scroll-btn left" onclick="scrollLeft()">&#10094;</button>
            <div class="suggested-products">
                <c:forEach var="product" items="${products}">
                    <div class="product-card">
                        <a href="productDetail?pID=${product.productID}">
                            <img src="${product.productimagesCollection[0].imageURL}" alt="${product.productName}">
                            <p class="product-name">${product.productName}</p>
                            <div class="home-product-item__rating">
                                <c:set var="avgRating"
                                       value="${ratingMap[product.productID] != null ? ratingMap[product.productID] : 0}"/>
                                <c:forEach begin="1" end="5" var="i">
                                    <c:choose>
                                        <c:when test="${i <= avgRating}">
                                            <i class="home-product-item__star-gold fas fa-star"></i>
                                        </c:when>
                                        <c:otherwise>
                                            <i class="fa-regular fa-star"></i>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </div>
                            <p class="product-categories">${product.description}</p>
                            <p class="product-price">${product.price}</p>
                        </a>
                    </div>
                </c:forEach>
            </div>
            <button class="scroll-btn right" onclick="scrollRight()">&#10095;</button>
        </div>
    </div>
</div>

<footer class="footer">
    <div class="grid wide">
        <div class="row">
            <div class="col l-3 m-3 c-6">
                <h3 class="footer__heading">Chăm sóc khách hàng</h3>
                <ul class="footer__list">
                    <li class="footer__list-item">
                        <a href="" class="footer__list-item__link">Đinh Huy Hoàng</a>
                    </li>
                    <li class="footer__list-item">
                        <a href="" class="footer__list-item__link">Lê Xuân Hoàng</a>
                    </li>
                    <li class="footer__list-item">
                        <a href="" class="footer__list-item__link">Nguyễn Đức Huy Hoàng</a>
                    </li>
                    <li class="footer__list-item">
                        <a href="" class="footer__list-item__link">Lê Thành Đạt</a>
                    </li>
                    <li class="footer__list-item">
                        <a href="" class="footer__list-item__link">Nguyễn Đình Duy</a>
                    </li>
                    <li class="footer__list-item">
                        <a href="" class="footer__list-item__link">Nguyễn Minh Hiếu</a>
                    </li>
                </ul>
            </div>
            <div class="col l-3 m-3 c-6">
                <h3 class="footer__heading">Theo dõi chúng tôi trên</h3>
                <ul class="footer__list">
                    <li class="footer__list-item">
                        <a href="" class="footer__list-item__link">
                            <i class="footer__list-item-icon fab fa-facebook"></i>
                            Facebook
                        </a>
                    </li>
                    <li class="footer__list-item">
                        <a href="" class="footer__list-item__link">
                            <i class="footer__list-item-icon fab fa-instagram"></i>
                            Instagram
                        </a>
                    </li>
                    <li class="footer__list-item">
                        <a href="" class="footer__list-item__link">
                            <i class="footer__list-item-icon fab fa-tiktok"></i>
                            Tiktok
                        </a>
                    </li>
                </ul>
            </div>
            <div class="col l-3 m-3 c-6">
                <h3 class="footer__heading">Vào cửa hàng</h3>
                <div class="footer__download">
                    <img src="${pageContext.request.contextPath}/resource/images/qr.png" alt=""
                         class="footer__download-qr">
                    <div class="footer__download-apps">
                        <a href="" class="footer__download-apps-link">
                            <img src="${pageContext.request.contextPath}/resource/images/1fddd5ee3e2ead84.png"
                                 alt="Goggle play"
                                 class="footer__download-apps-img">
                        </a>
                        <a href="" class="footer__download-apps-link">
                            <img src="${pageContext.request.contextPath}/resource/images/135555214a82d8e1.png"
                                 alt="AppStore"
                                 class="footer__download-apps-img">
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="footer__bottom">
        <div class="grid wide">
            <p class="footer__text">2025 - Bản quyền thuộc về Công Ty Những vì Tinh Tú LigmaShop</p>
        </div>
    </div>
</footer>
</div>
<script src="${pageContext.request.contextPath}/resource/js/productDetails.js"></script>
</body>
</html>