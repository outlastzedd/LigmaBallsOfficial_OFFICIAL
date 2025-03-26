<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <title>LigmaShop - Lịch Sử Giao Dịch</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/base.css">
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Ligma Shop</title>
    <!-- plugins:css -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/vendors/feather/feather.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/resource/vendors/mdi/css/materialdesignicons.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/style1.css">
    <!-- endinject -->
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resource/images/logomini2.png"/>
</head>
<style>
    /*        body {
                display: flex;
                justify-content: center;
                align-items: center;
                min-height: 100vh;
                margin: 0;
                background-color: #f5f5f5;
                font-family: Arial, sans-serif;
            }*/
    .container-profile {
        width: 100%;
        background-color: #fff;
        padding: 20px;
        border-radius: 8px;
        box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    }

    .order-table {
        width: 100%;
        border-collapse: collapse;
    }

    .order-table th, .order-table td {
        border: 1px solid #ddd;
        padding: 10px;
        text-align: left;
        font-size: 15px;
    }

    .order-table th {
        background-color: #f2f2f2;
        font-weight: bold;
    }

    .order-table tr:nth-child(even) {
        background-color: #f9f9f9;
    }

    .order-table tr:hover {
        background-color: #f1f1f1;
    }

    .no-data {
        text-align: center;
        color: #666;
        padding: 20px;
    }

    .order-link {
        color: #007bff;
        text-decoration: none;
    }

    .order-link:hover {
        text-decoration: underline;
    }
</style>
</head>
<body class="with-welcome-text">
<div class="container-scroller">
    <!--        <nav class="navbar default-layout col-lg-12 col-12 p-0 fixed-top d-flex align-items-top flex-row">
            <div class="text-center navbar-brand-wrapper d-flex align-items-center justify-content-start">
                <div class="me-3">
                    <button class="navbar-toggler navbar-toggler align-self-center" type="button" data-bs-toggle="minimize">
                        <span class="icon-menu"></span>
                    </button>
                </div>
                <div>
                    <a class="navbar-brand brand-logo" href="${pageContext.request.contextPath}/test">
                        <img src="${pageContext.request.contextPath}/resource/images/LIGMA SHOP WHITE ON BLACK.png" alt="logo" />
                    </a>
                    <a class="navbar-brand brand-logo-mini" href="${pageContext.request.contextPath}/ligmaShop/admin/adminPage.jsp">
                        <img src="${pageContext.request.contextPath}/resource/images/LIGMA SHOP WHITE ON BLACK.png" alt="logo" />
                    </a>
                </div>
            </div>  các logo header 
            <div class="navbar-menu-wrapper d-flex align-items-top">
                <ul class="navbar-nav">
                    <li class="nav-item fw-semibold d-none d-lg-block ms-0">
                        <h1 class="welcome-text">Xin Chào, ${sessionScope.user.getName()}</h1>
                        <h3 class="welcome-sub-text">Quản Lý Người Dùng</h3>
                    </li>
                </ul>
                <ul class="navbar-nav ms-auto">

                    <li class="nav-item"><! icon search 
                        <form class="search-form" action="#">
                            <i class="icon-search"></i>
                            <input type="search" class="form-control" placeholder="Search Here" title="Search here">
                        </form>
                    </li>


                    <li class="nav-item dropdown d-none d-lg-block user-dropdown">
                        <a class="nav-link" id="UserDropdown" href="#" data-bs-toggle="dropdown" aria-expanded="false">
                            <img class="img-xs rounded-circle" src="${pageContext.request.contextPath}/resource/images/user.jpg" alt="Profile image"> </a>
                        <div class="dropdown-menu dropdown-menu-right navbar-dropdown" aria-labelledby="UserDropdown">
                            <div class="dropdown-header text-center">
                                <img class="img-md rounded-circle" src="${pageContext.request.contextPath}/resource/images/user.jpg" alt="Profile image">
                                <p class="mb-1 mt-3 fw-semibold">${sessionScope.user.getName()}</p>
                                <p class="fw-light text-muted mb-0">${sessionScope.user.getEmail()}</p>
                            </div>
                            <a class="dropdown-item" href="${pageContext.request.contextPath}/authservlet"><i class="dropdown-item-icon mdi mdi-power text-primary me-2"></i>Đăng Xuất</a>
                        </div>
                    </li> <! quản lý account 
                </ul>
                <button class="navbar-toggler navbar-toggler-right d-lg-none align-self-center" type="button" data-bs-toggle="offcanvas">
                    <span class="mdi mdi-menu"></span>
                </button>
            </div>
        </nav>-->
    <div class="container-fluid page-body-wrapper">
        <!--            <nav class="sidebar sidebar-offcanvas" id="sidebar">
                 Thanh menu 
                <ul class="nav">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/ordersDayData">
                            <i class="mdi mdi-grid-large menu-icon"></i>
                            <span class="menu-title">Trang chủ</span>
                        </a>
                    </li>
                    <li class="nav-item nav-category">Công Cụ</li>


                    <li class="nav-item">
                        <a class="nav-link" data-bs-toggle="collapse" href="#auth" aria-expanded="false" aria-controls="auth">
                            <i class="menu-icon mdi mdi-account-circle-outline"></i>
                            <span class="menu-title">Quản lý Người Dùng</span>
                            <i class="menu-arrow"></i>
                        </a>
                        <div class="collapse" id="auth">
                            <ul class="nav flex-column sub-menu">
                                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/ligmaShop/admin/addUser.jsp">Thêm người dùng mới</a></li>

                            </ul>
                        </div>

                    </li>

                    <li class="nav-item">
                        <a class="nav-link" data-bs-toggle="collapse" href="#form-elements" aria-expanded="false" aria-controls="form-elements">
                            <i class="menu-icon mdi mdi-card-text-outline"></i>
                            <span class="menu-title">Quản Lý Sản Phẩm</span>
                            <i class="menu-arrow"></i>
                        </a>
                        <div class="collapse" id="form-elements">
                            <ul class="nav flex-column sub-menu">
                                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/ligmaShop/admin/addNewProduct.jsp">Thêm Sản Phẩm</a></li>
                                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/productManager">Danh sách sản phẩm</a></li>
                                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/orderManagement"> Quản Lý Đơn Hàng </a></li>  

                            </ul>
                        </div>

                    </li>


                </ul>
            </nav>-->
        <!--            <div class="main-panel">
                        <div class="content-wrapper">
                            <div class="row">

                                <div class="col-12 grid-margin stretch-card">
                                    <div class="card">/*thêm servlet ở đây*/
                                        <div class="card-body">-->

        <div class="container-profile">
            <c:choose>
                <c:when test="${empty orderDetailsList}">
                    <p class="no-data">Không có chi tiết đơn hàng nào để hiển thị.</p>
                </c:when>
                <c:otherwise>
                    <button onclick="goBack()"
                            style="margin: 20px; padding: 10px 15px; background-color: #007bff; color: white; border: none; border-radius: 5px; cursor: pointer;">
                        ← Quay lại
                    </button>

                    <script>
                        function goBack() {
                            window.history.back();
                        }
                    </script>
                    <table class="order-table">
                        <thead>
                        <tr>
                            <th>Mã Đơn Hàng</th>
                            <th>Mã Chi Tiết</th>
                            <th>Sản Phẩm</th>
                            <th>Số Lượng</th>
                            <th>Giá</th>
                            <th>Tổng</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="orderDetail" items="${orderDetailsList}">
                            <tr>
                                <td>
                                    <a href="${pageContext.request.contextPath}/orderDetails?orderID=${orderDetail.orderID.orderID}"
                                       class="order-link">
                                            ${orderDetail.orderID.orderID}
                                    </a>
                                </td>
                                <td>${orderDetail.orderDetailID}</td>
                                <td>
                                    <c:out value="${orderDetail.productSizeColorID != null && orderDetail.productSizeColorID.productID != null ? orderDetail.productSizeColorID.productID.productName : 'N/A'}"/>
                                    <c:if test="${orderDetail.productSizeColorID != null}">
                                        (${orderDetail.productSizeColorID.sizeID.sizeName}, ${orderDetail.productSizeColorID.colorID.colorName})
                                    </c:if>
                                </td>
                                <td>${orderDetail.quantity}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${orderDetail.price != null}">
                                            <fmt:formatNumber value="${orderDetail.price}" type="currency"
                                                              currencySymbol="VNĐ"/>
                                        </c:when>
                                        <c:otherwise>N/A</c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${orderDetail.price != null}">
                                            <fmt:formatNumber value="${orderDetail.price * orderDetail.quantity}"
                                                              type="currency" currencySymbol="VNĐ"/>
                                        </c:when>
                                        <c:otherwise>N/A</c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>

        <!--
                                        </div>
                                    </div>
                                </div>
                                <footer class="footer">
                                    <div class="d-sm-flex justify-content-center justify-content-sm-between">
                                        <span class="float-none float-sm-end d-block mt-1 mt-sm-0 text-center">Công Ty TNHH Ligma Shop</span>
                                    </div>
                                </footer>

                            </div>
                        </div>

                         main-panel ends
                    </div>
                     page-body-wrapper ends
                </div>
                 container-scroller
            </div>-->
        <!-- plugins:js -->
        <script src="${pageContext.request.contextPath}/resource/vendors/js/vendor.bundle.base.js"></script>
        <script src="${pageContext.request.contextPath}/resource/vendors/bootstrap-datepicker/bootstrap-datepicker.min.js"></script>
        <script src="${pageContext.request.contextPath}/resource/js/off-canvas.js"></script>
        <script src="${pageContext.request.contextPath}/resource/js/template.js"></script>
        <script src="${pageContext.request.contextPath}/resource/js/settings.js"></script>
        <script src="${pageContext.request.contextPath}/resource/js/hoverable-collapse.js"></script>
        <script src="${pageContext.request.contextPath}/resource/js/todolist.js"></script>
        <script src="${pageContext.request.contextPath}/resource/js/jquery.cookie.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath}/resource/js/dashboard.js"></script>
        <!-- End custom js for this page-->
        <!--</div>-->
    </div>
</div>
</body>
</html>