<%@page import="java.util.List" %>
<%@page import="model.Orders" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <title>Quản Lý Đơn Hàng - Ligma Shop</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/vendors/feather/feather.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/vendors/mdi/css/materialdesignicons.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/style1.css">
        <link rel="shortcut icon" href="${pageContext.request.contextPath}/resource/images/logoLigma.png"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/orderAdmin.css">

    </head>
    <body class="with-welcome-text">
        <!-- Phân trang cho all-->
        <c:set var="pageSize" value="15"/>
        <c:set var="currentPage" value="${param.page != null ? param.page : 1}"/>
        <c:set var="totalOrders" value="${fn:length(allOrders)}"/>
        <c:set var="totalPages" value="${(totalOrders + pageSize - 1) / pageSize}"/>

        <c:set var="startIndex" value="${(currentPage - 1) * pageSize}"/>
        <c:set var="endIndex" value="${startIndex + pageSize}"/>

        <c:set var="pageSize" value="15"/>

        <!-- Phân trang cho VNPay -->
        <c:set var="currentPageVnpay" value="${param.pageVnpay != null ? param.pageVnpay : 1}"/>
        <c:set var="totalVnpayOrders" value="${fn:length(vnpayOrders)}"/>
        <c:set var="totalPagesVnpay" value="${(totalVnpayOrders + pageSize - 1) / pageSize}"/>
        <c:set var="startIndexVnpay" value="${(currentPageVnpay - 1) * pageSize}"/>
        <c:set var="endIndexVnpay" value="${startIndexVnpay + pageSize}"/>

        <!-- Phân trang cho COD -->
        <c:set var="currentPageCod" value="${param.pageCod != null ? param.pageCod : 1}"/>
        <c:set var="totalCodOrders" value="${fn:length(codOrders)}"/>
        <c:set var="totalPagesCod" value="${(totalCodOrders + pageSize - 1) / pageSize}"/>
        <c:set var="startIndexCod" value="${(currentPageCod - 1) * pageSize}"/>
        <c:set var="endIndexCod" value="${startIndexCod + pageSize}"/>


        <div class="container-scroller">
            <nav class="navbar default-layout col-lg-12 col-12 p-0 fixed-top d-flex align-items-top flex-row">
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
                </div> <!-- các logo header -->
                <div class="navbar-menu-wrapper d-flex align-items-top">
                    <ul class="navbar-nav">
                        <li class="nav-item fw-semibold d-none d-lg-block ms-0">
                            <h1 class="welcome-text">Xin Chào, ${sessionScope.user.getName()}</h1>
                            <h3 class="welcome-sub-text">Quản Lý Đơn Hàng</h3>
                        </li>
                    </ul>
                    <ul class="navbar-nav ms-auto">

                        <li class="nav-item"><!<!-- icon search -->
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
                        </li> <!<!-- quản lý account -->
                    </ul>
                    <button class="navbar-toggler navbar-toggler-right d-lg-none align-self-center" type="button" data-bs-toggle="offcanvas">
                        <span class="mdi mdi-menu"></span>
                    </button>
                </div>
            </nav>

            <div class="container-fluid page-body-wrapper">
                <nav class="sidebar sidebar-offcanvas" id="sidebar">
                    <!-- Thanh menu -->
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
                </nav>
                <div class="main-panel">
                    <div class="content-wrapper">
                        <div class="row">
                            <div class="col-12 grid-margin stretch-card">
                                <div class="card"><!--/*thêm servlet ở đây*/-->
                                    <div class="card-body">
                                        <div class="col-sm-12">
                                            <h3 class="mb-4">Quản Lý Đơn Hàng</h3>

                                            <!-- Thanh tìm kiếm -->
                                            <form class="search-form" action="${pageContext.request.contextPath}/orderManagement" method="get">
                                                <div class="input-group">
                                                    <input type="text" name="search" class="form-control" placeholder="Nhập UserID, OrderID hoặc Tên người mua" value="${param.search}">
                                                    <button type="submit" class="btn btn-primary">Tìm kiếm</button>
                                                </div>
                                            </form>
                                            <c:set var="activeTab" value="${param.activeTab != null ? param.activeTab : 'all'}"/>

                                            <ul class="nav nav-tabs" id="orderTabs" role="tablist">
                                                <li class="nav-item">
                                                    <a class="nav-link ${activeTab == 'all' ? 'active' : ''}" href="?activeTab=all">Tất Cả Đơn Hàng</a>
                                                </li>
                                                <li class="nav-item">
                                                    <a class="nav-link ${activeTab == 'vnpay' ? 'active' : ''}" href="?activeTab=vnpay">Thanh Toán Qua VNPay</a>
                                                </li>
                                                <li class="nav-item">
                                                    <a class="nav-link ${activeTab == 'cod' ? 'active' : ''}" href="?activeTab=cod">Thanh Toán Qua COD</a>
                                                </li>
                                                <!--backk btton-->
                                                <button class="back-btn-order" onclick="goBack()">Quay Lại</button>
                                            </ul>


                                            <div class="tab-content" id="orderTabContent">
                                                <!-- Tất cả đơn hàng -->
                                                <div class="tab-pane fade ${activeTab == 'all' ? 'show active' : ''}" id="allOrders" role="tabpanel">
                                                    <table class="table">
                                                        <thead>
                                                            <tr>
                                                                <th>ID Đơn Hàng</th>
                                                                <th>Tên Người Mua</th>
                                                                <th>Ngày Đặt</th>
                                                                <th>Tổng Tiền</th>
                                                                <th>Chi Tiết</th>
                                                            </tr>
                                                        </thead>
                                                        <tbody>
                                                            <c:choose>
                                                                <c:when test="${not empty allOrders}">
                                                                    <c:forEach var="order" items="${allOrders}" varStatus="status">
                                                                        <c:if test="${status.index >= startIndex and status.index < endIndex}">
                                                                            <tr>
                                                                                <td>${order.orderID}</td>
                                                                                <td><c:out value="${order.userID != null ? order.userID.name : 'N/A'}" /></td>
                                                                                <td>${order.orderDate}</td>
                                                                                <td>${order.totalAmount} VNĐ</td>
                                                                                <td><a href="${pageContext.request.contextPath}/orderDetails?orderID=${order.orderID}" class="btn btn-info btn-sm">Xem</a></td>
                                                                            </tr>
                                                                        </c:if>
                                                                    </c:forEach>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <tr>
                                                                        <td colspan="5">Không có đơn hàng nào.</td>
                                                                    </tr>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </tbody>
                                                    </table>
                                                    <div class="pagination">
                                                        <c:if test="${currentPage > 1}">
                                                            <a href="?page=${currentPage - 1}" class="btn btn-secondary">« Trước</a>
                                                        </c:if>

                                                        <c:forEach begin="1" end="${totalPages}" var="pageNum">
                                                            <a href="?page=${pageNum}" class="btn ${pageNum == currentPage ? 'btn-primary' : 'btn-light'}">${pageNum}</a>
                                                        </c:forEach>

                                                        <c:if test="${currentPage < totalPages}">
                                                            <a href="?page=${currentPage + 1}" class="btn btn-secondary">Sau »</a>
                                                        </c:if>
                                                    </div>
                                                </div>

                                                <!-- Đơn hàng VNPay -->
                                                <div class="tab-pane fade ${activeTab == 'vnpay' ? 'show active' : ''}" id="vnpayOrders" role="tabpanel">

                                                    <table class="table">
                                                        <thead>
                                                            <tr>
                                                                <th>ID Đơn Hàng</th>
                                                                <th>Tên Người Mua</th>
                                                                <th>Ngày Đặt</th>
                                                                <th>Tổng Tiền</th>
                                                                <th>Chi Tiết</th>
                                                            </tr>
                                                        </thead>
                                                        <tbody>
                                                            <c:choose>
                                                                <c:when test="${not empty vnpayOrders}">
                                                                    <c:forEach var="order" items="${vnpayOrders}" varStatus="status">
                                                                        <c:if test="${status.index >= startIndexVnpay and status.index < endIndexVnpay}">
                                                                            <tr>
                                                                                <td>${order.orderID}</td>
                                                                                <td><c:out value="${order.userID != null ? order.userID.name : 'N/A'}" /></td>
                                                                                <td>${order.orderDate}</td>
                                                                                <td>${order.totalAmount} VNĐ</td>
                                                                                <td><a href="${pageContext.request.contextPath}/orderDetails?orderID=${order.orderID}" class="btn btn-info btn-sm">Xem</a></td>
                                                                            </tr>
                                                                        </c:if>
                                                                    </c:forEach>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <tr>
                                                                        <td colspan="5">Không có đơn hàng nào.</td>
                                                                    </tr>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </tbody>
                                                    </table>

                                                    <!-- Phân trang -->
                                                    <div class="pagination">
                                                        <c:if test="${currentPageVnpay > 1}">
                                                            <a href="?pageVnpay=${currentPageVnpay - 1}&activeTab=vnpay" class="btn btn-secondary">« Trước</a>
                                                        </c:if>

                                                        <c:forEach begin="1" end="${totalPagesVnpay}" var="pageNum">
                                                            <a href="?pageVnpay=${pageNum}&activeTab=vnpay" class="btn ${pageNum == currentPageVnpay ? 'btn-primary' : 'btn-light'}">${pageNum}</a>
                                                        </c:forEach>

                                                        <c:if test="${currentPageVnpay < totalPagesVnpay}">
                                                            <a href="?pageVnpay=${currentPageVnpay + 1}&activeTab=vnpay" class="btn btn-secondary">Sau »</a>
                                                        </c:if>
                                                    </div>


                                                </div>

                                                <!-- Đơn hàng COD -->
                                                <div class="tab-pane fade ${activeTab == 'cod' ? 'show active' : ''}" id="codOrders" role="tabpanel">
                                                    <table class="table">
                                                        <thead>
                                                            <tr>
                                                                <th>ID Đơn Hàng</th>
                                                                <th>Tên Người Mua</th>
                                                                <th>Ngày Đặt</th>
                                                                <th>Tổng Tiền</th>
                                                                <th>Chi Tiết</th>
                                                            </tr>
                                                        </thead>
                                                        <tbody>
                                                            <c:choose>
                                                                <c:when test="${not empty codOrders}">
                                                                    <c:forEach var="order" items="${codOrders}" varStatus="status">
                                                                        <c:if test="${status.index >= startIndexCod and status.index < endIndexCod}">
                                                                            <tr>
                                                                                <td>${order.orderID}</td>
                                                                                <td><c:out value="${order.userID != null ? order.userID.name : 'N/A'}" /></td>
                                                                                <td>${order.orderDate}</td>
                                                                                <td>${order.totalAmount} VNĐ</td>
                                                                                <td><a href="${pageContext.request.contextPath}/orderDetails?orderID=${order.orderID}" class="btn btn-info btn-sm">Xem</a></td>
                                                                            </tr>
                                                                        </c:if>
                                                                    </c:forEach>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <tr>
                                                                        <td colspan="5">Không có đơn hàng nào.</td>
                                                                    </tr>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </tbody>
                                                    </table>

                                                    <!-- Phân trang -->
                                                    <div class="pagination">
                                                        <c:if test="${currentPageCod > 1}">
                                                            <a href="?pageCod=${currentPageCod - 1}&activeTab=cod" class="btn btn-secondary">« Trước</a>
                                                        </c:if>

                                                        <c:forEach begin="1" end="${totalPagesCod}" var="pageNum">
                                                            <a href="?pageCod=${pageNum}&activeTab=cod" class="btn ${pageNum == currentPageCod ? 'btn-primary' : 'btn-light'}">${pageNum}</a>
                                                        </c:forEach>

                                                        <c:if test="${currentPageCod < totalPagesCod}">
                                                            <a href="?pageCod=${currentPageCod + 1}&activeTab=cod" class="btn btn-secondary">Sau »</a>
                                                        </c:if>
                                                    </div>

                                                </div>

                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <footer class="footer">
                                <div class="d-sm-flex justify-content-center justify-content-sm-between">
                                    <span class="text-muted text-center text-sm-left d-block d-sm-inline-block">Premium <a href="#" target="_blank">Link</a> OK</span>
                                    <span class="float-none float-sm-end d-block mt-1 mt-sm-0 text-center">Copyright © 2023. All rights reserved.</span>
                                </div>
                            </footer>

                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script src="${pageContext.request.contextPath}/resource/vendors/js/vendor.bundle.base.js"></script>
        <script src="${pageContext.request.contextPath}/resource/js/off-canvas.js"></script>
        <script src="${pageContext.request.contextPath}/resource/js/template.js"></script>
        <script src="${pageContext.request.contextPath}/resource/js/settings.js"></script>
        <script src="${pageContext.request.contextPath}/resource/js/hoverable-collapse.js"></script>
        <script src="${pageContext.request.contextPath}/resource/js/todolist.js"></script>
        <script>
                                                    function goBack() {
                                                        window.location.href = '${pageContext.request.contextPath}/ordersDayData';
                                                    }
        </script>
    </body>
</html>