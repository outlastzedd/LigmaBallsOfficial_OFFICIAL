<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>LigmaShop - Cart</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/normalize/8.0.1/normalize.min.css">
        <link href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100..900;1,100..900&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/base.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/grid.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/main.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/cart.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css">
    </head>
    <body>
        <div class="app">
            <!-- Header (Reused from User Page) -->
            <%@ include file="../header/header.jsp" %>
            <!-- Cart Content -->
            <div class="app__container">
                <div class="grid wide">
                    <div class="row app__content">
                        <div class="col l-12 m-12 c-12">
                            <h2 class="cart__heading">Giỏ Hàng Của Bạn</h2>

                            <c:if test="${not empty sessionScope.error}">
                                <div class="alert alert-danger">
                                    ${sessionScope.error}
                                    <c:remove var="error" scope="session"/>
                                </div>
                            </c:if>

                            <c:if test="${empty cartItems}">
                                <div class="cart__empty">
                                    <img src="${pageContext.request.contextPath}/resource/images/no-cart.jpg" alt="No items" class="cart__empty-img">
<!--                                    <p>Giỏ hàng của bạn đang trống!</p>-->
                                    <a href="${pageContext.request.contextPath}/products" class="btn btn--primary">Quay lại mua sắm</a>
                                </div>
                            </c:if>

                            <c:if test="${not empty cartItems}">
                                <div class="cart__items">
                                    <table class="cart__table">
                                        <thead>
                                            <tr>
                                                <th>Sản phẩm</th>
                                                <th>Kích thước</th>
                                                <th>Giá</th>
                                                <th>Số lượng</th>
                                                <th>Tổng</th>
                                                <th>Hành động</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:set var="totalPrice" value="0"/>
                                            <c:forEach var="item" items="${cartItems}">
                                                <c:set var="product" value="${item.productSizeColorID.productID}" />
                                                <c:set var="basePrice" value="${product.price-(product.price*product.discount/100)}" />
                                                <c:set var="sizeName" value="${item.productSizeColorID.sizeID.sizeName}" />
                                                <c:set var="priceAdjustment" value="0" />
                                                <c:if test="${sizeName == 'XL'}">
                                                    <c:set var="priceAdjustment" value="50000" />
                                                </c:if>
                                                <c:if test="${sizeName == 'XXL'}">
                                                    <c:set var="priceAdjustment" value="100000" />
                                                </c:if>  
                                                <c:set var="adjustedPrice" value="${basePrice + priceAdjustment}" />                               
                                                <c:set var="discount" value="${product.discount != null ? product.discount : 0}" />
                                                <tr>
                                                    <td>
                                                        <div class="cart__item-info">
                                                            <c:choose>
                                                                <c:when test="${empty item.productSizeColorID || empty item.productSizeColorID.productID || empty item.productSizeColorID.productID.productimagesCollection}">
                                                                    <img src="${pageContext.request.contextPath}/resource/images/user.jpg" class="cart__item-img">
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <c:forEach var="image" items="${item.productSizeColorID.productID.productimagesCollection}" varStatus="imgStatus">
                                                                        <c:if test="${imgStatus.index == 0}">
                                                                            <img src="${image.imageURL}" alt="${item.productSizeColorID.productID.productName}" class="cart__item-img">
                                                                        </c:if>
                                                                    </c:forEach>
                                                                </c:otherwise>
                                                            </c:choose>
                                                            <span>${item.productSizeColorID != null && item.productSizeColorID.productID != null ? item.productSizeColorID.productID.productName : 'Sản phẩm không xác định'}</span>
                                                        </div>
                                                    </td>
                                                    <td>${item.productSizeColorID != null && item.productSizeColorID.sizeID != null ? item.productSizeColorID.sizeID.sizeName : 'N/A'}</td>
                                                    <td>
                                                        <fmt:formatNumber value="${adjustedPrice}" type="number" groupingUsed="true" /> đ
                                                    </td>
                                                    <td>
                                                        <form action="${pageContext.request.contextPath}/cart" method="post" class="update-form">
                                                            <input type="hidden" name="action" value="update">
                                                            <input type="hidden" name="cartItemID" value="${item.cartItemID}">
                                                            <input type="number" name="quantity" min="1" value="${item.quantity}" class="cart__item-quantity" required>
                                                            <button type="submit" class="cart__update-btn btn">Cập nhật</button>
                                                        </form>
                                                    </td>
                                                    <td>
                                                        <fmt:formatNumber value="${adjustedPrice * item.quantity}" type="number" groupingUsed="true" /> đ
                                                        <c:set var="totalPrice" value="${totalPrice+adjustedPrice * item.quantity}"/>

                                                    </td>
                                                    <td>
                                                        <form action="${pageContext.request.contextPath}/cart" method="post" class="remove-form">
                                                            <input type="hidden" name="action" value="remove">
                                                            <input type="hidden" name="cartItemID" value="${item.cartItemID}">
                                                            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                                                            <button type="submit" class="cart__item-remove btn btn--danger">Xóa</button>
                                                        </form>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>

                                    <div class="cart__total">
                                        <span>Tổng cộng:</span>
                                        <span class="cart__total-amount">
                                            <fmt:formatNumber value="${totalPrice != null ? totalPrice : 0}" type="number" groupingUsed="true" /> đ
                                        </span>
                                    </div>

                                    <a href="${pageContext.request.contextPath}/ligmaShop/payment/checkout.jsp" class="btn btn--primary cart__checkout-btn">Thanh Toán</a>
                                </div>
                            </c:if>

                            <script>
                                document.addEventListener('DOMContentLoaded', function () {
                                    document.querySelectorAll('.update-form').forEach(form => {
                                        form.addEventListener('submit', function (event) {
                                            const quantityInput = this.querySelector('input[name="quantity"]');
                                            const quantity = parseInt(quantityInput.value);
                                            if (isNaN(quantity) || quantity < 1) {
                                                event.preventDefault();
                                                alert('Số lượng phải lớn hơn 0.');
                                                quantityInput.focus();
                                            }
                                        });
                                    });
                                    document.querySelectorAll('.remove-form').forEach(form => {
                                        console.log("Remove form found:", form); // Thêm log để kiểm tra
                                        form.addEventListener('submit', function (event) {
                                            console.log("Remove form submitted"); // Thêm log để kiểm tra
                                            setTimeout(() => {
                                                window.location.reload();
                                            }, 500);
                                        });
                                    });
                                });

                            </script>
                        </div>

                    </div>
                </div>
            </div>

            <!-- Footer (Reused from User Page) -->
            <footer class="footer">
                <div class="grid wide">
                    <div class="row">
                        <div class="col l-3 m-3 c-6">
                            <h3 class="footer__heading">Chăm sóc khách hàng</h3>
                            <ul class="footer__list">
                                <li class="footer__list-item"><a href="" class="footer__list-item__link">Đinh Huy Hoàng</a></li>
                                <li class="footer__list-item"><a href="" class="footer__list-item__link">Lê Xuân Hoàng</a></li>
                                <li class="footer__list-item"><a href="" class="footer__list-item__link">Nguyễn Đức Huy Hoàng</a></li>
                                <li class="footer__list-item"><a href="" class="footer__list-item__link">Lê Thành Đạt</a></li>
                                <li class="footer__list-item"><a href="" class="footer__list-item__link">Nguyễn Đình Duy</a></li>
                                <li class="footer__list-item"><a href="" class="footer__list-item__link">Nguyễn Minh Hiếu</a></li>
                            </ul>
                        </div>
                        <div class="col l-3 m-3 c-6">
                            <h3 class="footer__heading">Theo dõi chúng tôi trên</h3>
                            <ul class="footer__list">
                                <li class="footer__list-item"><a href="" class="footer__list-item__link"><i class="footer__list-item-icon fab fa-facebook"></i>Facebook</a></li>
                                <li class="footer__list-item"><a href="" class="footer__list-item__link"><i class="footer__list-item-icon fab fa-instagram"></i>Instagram</a></li>
                                <li class="footer__list-item"><a href="" class="footer__list-item__link"><i class="footer__list-item-icon fab fa-tiktok"></i>Tiktok</a></li>
                            </ul>
                        </div>
                        <div class="col l-3 m-3 c-6">
                            <h3 class="footer__heading">Vào cửa hàng</h3>
                            <div class="footer__download">
                                <img src="${pageContext.request.contextPath}/resource/images/5b6e787c2e5ee052.png" alt="" class="footer__download-qr">
                                <div class="footer__download-apps">
                                    <a href="" class="footer__download-apps-link"><img src="${pageContext.request.contextPath}/resource/images/1fddd5ee3e2ead84.png" alt="Google Play" class="footer__download-apps-img"></a>
                                    <a href="" class="footer__download-apps-link"><img src="${pageContext.request.contextPath}/resource/images/135555214a82d8e1.png" alt="AppStore" class="footer__download-apps-img"></a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="footer__bottom">
                    <div class="grid wide">
                        <p class="footer__text">2025 - Bản quyền thuộc về Công ti Những Vì Tinh Tú LigmaShop</p>
                    </div>
                </div>
            </footer>
        </div>
    </body>
</html>