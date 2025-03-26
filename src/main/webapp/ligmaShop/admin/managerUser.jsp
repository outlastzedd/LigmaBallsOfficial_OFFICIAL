<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Users" %>
<%@ page import="userDAO.UserDAO" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <!-- Required meta tags -->
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <title>Ligma Shop</title>
        <!-- plugins:css -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/vendors/feather/feather.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/vendors/mdi/css/materialdesignicons.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/managerUser.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/style1.css">
        <!-- endinject -->
        <link rel="shortcut icon" href="${pageContext.request.contextPath}/resource/images/logomini2.png" />
    </head>
    <body class="with-welcome-text">
        <div class="container-scroller">
            <!-- partial:partials/_navbar.html -->
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
                            <h3 class="welcome-sub-text">Quản Lý Người Dùng</h3>
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
            <!-- partial -->
            <div class="container-fluid page-body-wrapper">
                <!-- partial:partials/_sidebar.html -->
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
                <!-- partial -->
                <div class="main-panel">
                    <div class="content-wrapper">
                        <div class="row">

                            <div class="col-12 grid-margin stretch-card">
                                <div class="card"><!--/*thêm servlet ở đây*/-->
                                    <div class="card-body">

                                        <div class ="boder-user">
                                            <div class="header-container-user">
                                                <h1>Quản Lý Người Dùng</h1>
                                                <a href="${pageContext.request.contextPath}/users?action=add" class="add-user-btn">+ Thêm Người Dùng</a>
                                            </div>

                                            <!-- Display success or error messages -->
                                            <% if (request.getAttribute("message") != null) {%>
                                            <p class="success"><%= request.getAttribute("message")%></p>
                                            <% } %>
                                            <% if (request.getAttribute("error") != null) {%>
                                            <p class="error"><%= request.getAttribute("error")%></p>
                                            <% } %>

                                            <!-- User List Table -->
                                            <table>
                                                <thead>
                                                    <tr>
                                                        <th>ID</th>
                                                        <th>Tên</th>
                                                        <th>Email</th>
                                                        <th>Số Điện Thoại</th>
                                                        <th>Địa chỉ</th>
                                                        <th>Vai Trò</th>
                                                        <th>Trạng Thái</th>
                                                        <th>Hành Động</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <%
                                                        UserDAO userDAO = new UserDAO();
                                                        List<Users> users = userDAO.selectAllUsers();
                                                        if (users != null && !users.isEmpty()) {
                                                            for (Users user : users) {
                                                    %>
                                                    <tr>
                                                        <td><%= user.getUserID()%></td>
                                                        <td><%= user.getName()%></td>
                                                        <td><%= user.getEmail()%></td>
                                                        <td><%= user.getPhoneNumber()%></td>
                                                        <td><%= user.getAddress()%></td>
                                                        <td><%= user.getRole() != null ? user.getRole() : "N/A"%></td>
                                                        <td><%= user.getStatus()%> </td>
                                                        <td>
                                                            <a href="${pageContext.request.contextPath}/users?action=delete&id=<%= user.getUserID()%>" class="action-btn delete-btn"
                                                               onclick="return confirm('Bạn có chắc muốn xóa người dùng này?');">Xóa</a>
                                                            <a href="${pageContext.request.contextPath}/users?action=edit&id=<%= user.getUserID()%>" class="action-btn update-btn">Sửa</a>
                                                        </td>
                                                    </tr>
                                                    <%
                                                        }
                                                    } else {
                                                    %>
                                                    <tr>
                                                        <td colspan="6">Không có người dùng nào.</td>
                                                    </tr>
                                                    <% }%>
                                                </tbody>
                                            </table>

                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!-- content-wrapper ends -->

                            <!-- partial:partials/_footer.html -->
                            <footer class="footer">
                                <div class="d-sm-flex justify-content-center justify-content-sm-between">
                                    <span class="float-none float-sm-end d-block mt-1 mt-sm-0 text-center">Công Ty TNHH Ligma Shop</span>
                                </div>
                            </footer>
                            <!-- partial -->

                        </div>
                    </div>

                    <!-- main-panel ends -->
                </div>
                <!-- page-body-wrapper ends -->
            </div>
            <!-- container-scroller -->
        </div>
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
    </body>
</html>