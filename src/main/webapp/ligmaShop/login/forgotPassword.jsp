<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quên mật khẩu</title>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link href="//fonts.googleapis.com/css?family=Sirin+Stencil" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css">
    <link href="${pageContext.request.contextPath}/resource/css/style.css" rel="stylesheet" type="text/css"
          media="all"/>
</head>
<body>
<div class="container demo-1">
    <div class="content">
        <div id="large-header" class="large-header">
            <h1>Quên mật khẩu</h1>
            <div class="main-agileits">
                <div class="form-w3-agile">
                    <h2>Nhập email của bạn</h2>
                    <c:if test="${not empty message}">
                        <div class="alert ${param.error != null ? 'alert-danger' : 'alert-success'}">
                            <c:out value="${message}"/>
                        </div>
                    </c:if>
                    <form action="${pageContext.request.contextPath}/authservlet" method="POST">
                        <input type="hidden" name="action" value="forgotPassword"/>
                        <div class="form-sub-w3">
                            <input type="text" name="email" placeholder="Email" required/>
                            <div class="icon-w3">
                                <i class="fa fa-envelope" aria-hidden="true"></i>
                            </div>
                        </div>
                        <div class="submit-w3l">
                            <input type="submit" value="Gửi mã OTP">
                        </div>
                    </form>
                    <p class="p-bottom-w3ls"><a href="${pageContext.request.contextPath}/ligmaShop/login/signIn.jsp">Quay
                        lại đăng nhập</a></p>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>