<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="productDAO" class="productDAO.ProductDAO" scope="page"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>LigmaShop</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/normalize/8.0.1/normalize.min.css">
        <link href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100..900;1,100..900&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/base.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/grid.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/main.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css">
        <link rel="shortcut icon" href="${pageContext.request.contextPath}/images/logoLigma.png" />
        <script>
            function previewAvatar(event) {
                var reader = new FileReader();
                reader.onload = function () {
                    var output = document.getElementById('avatarPreview');
                    output.style.backgroundImage = "url('" + reader.result + "')";
                };
                reader.readAsDataURL(event.target.files[0]);
            }
        </script>
    </head>

    <body>
        <div class="app">
            <%@ include file="../header/header.jsp" %>
            <div class="app__container">
                <div class="grid wide">
                    <div class="row sm-gutter app__content">
                        <div class="col l-2 m-0 c-0">
                            <nav class="category">
                                <h3 class="category__heading">
                                    <i class="category__heading-icon fa-solid fa-list"></i>
                                    Tài Khoản Của Tôi
                                </h3>
                                <ul class="category-list">
                                    <li class="category-item category-item--active">
                                        <a href="${pageContext.request.contextPath}/ligmaShop/user/profilePage.jsp" class="category-item__link">Hồ Sơ Cá Nhân</a>
                                    </li>
                                    <li class="category-item">
                                        <a href="${pageContext.request.contextPath}/ligmaShop/user/changePassPage.jsp" class="category-item__link">Đổi Mật Khẩu</a>
                                    </li>
                                    <li class="category-item">
                                        <a href="${pageContext.request.contextPath}/orderDetails" class="category-item__link">Lịch Sử Giao Dịch</a>
                                    </li>
                                </ul>
                            </nav>
                        </div>
                        <script>
                            document.addEventListener("DOMContentLoaded", function () {
                                // Lấy đường dẫn hiện tại, chỉ lấy phần pathname để tránh lỗi so khớp URL đầy đủ
                                let currentPath = window.location.pathname;

                                // Lấy tất cả các thẻ <a> trong menu
                                let links = document.querySelectorAll(".category-item__link");

                                links.forEach(link => {
                                    // Lấy đường dẫn của link (chỉ phần pathname)
                                    let linkPath = new URL(link.href, window.location.origin).pathname;

                                    // Kiểm tra nếu đường dẫn trang hiện tại khớp với đường dẫn của link
                                    if (currentPath === linkPath) {
                                        // Xóa class active của tất cả
                                        document.querySelectorAll(".category-item").forEach(item => {
                                            item.classList.remove("category-item--active");
                                        });

                                        // Thêm class active vào <li> chứa link hiện tại
                                        link.parentElement.classList.add("category-item--active");
                                    }
                                });
                            });

                        </script>
                        <div class="container-profile">
                            <div class="form-container">
                                <c:if test="${param.status == 'success'}">
                                    <div class="alert alert-success">
                                        <c:out value="${message}"/>
                                    </div>
                                </c:if>
                                <c:if test="${param.status == 'failed'}">
                                    <div class="alert alert-danger">
                                        <c:out value="${message}"/>
                                    </div>
                                </c:if>
                                <!-- Phần nội dung form -->
                                <div class="form-content">
                                    <form action="${pageContext.request.contextPath}/users" method="post">
                                        <input type="hidden" name="action" value="updateProfile"/>
                                        <div class="form-group">
                                            <label class="text-form" for="name">Tên</label>
                                            <input type="text" id="name" name="name" value="${user.getName()}" required>
                                        </div>

                                        <div class="form-group">
                                            <label class="text-form" for="email">Email</label>
                                            <input type="email" id="email" name="email" value="${user.getEmail()}" required>
                                        </div>

                                        <div class="form-group">
                                            <label class="text-form" for="phone">Số điện thoại</label>
                                            <input type="text" id="phone" name="phone" value="${user.getPhoneNumber()}" required>
                                        </div>

                                        <!-- Thêm mục địa chỉ -->
                                        <div class="form-group">
                                            <label class="text-form">Địa chỉ</label>
                                            <div class="address-group">
                                                <input type="text" id="address" name="address" value="${user.getAddress()}" placeholder="Số nhà, tên đường..." required>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <button type="submit">Sửa thông tin</button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
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
                                <img src="${pageContext.request.contextPath}/images/qr.png" alt="" class="footer__download-qr">                            
                                <div class="footer__download-apps">
                                    <a href="" class="footer__download-apps-link">
                                        <img src="${pageContext.request.contextPath}/resource/images/1fddd5ee3e2ead84.png" alt="Goggle play" class="footer__download-apps-img">
                                    </a>
                                    <a href="" class="footer__download-apps-link">
                                        <img src="${pageContext.request.contextPath}/resource/images/135555214a82d8e1.png" alt="AppStore" class="footer__download-apps-img">
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="footer__bottom">
                    <div class="grid wide">
                        <p class="footer__text">2025 - Bản quyền thuộc về Công ti Những vì Tinh Tú LigmaShop</p>
                    </div>
                </div>
            </footer>
        </div>
    </body>
</html>