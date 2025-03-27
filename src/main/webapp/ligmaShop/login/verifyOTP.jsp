<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Xác thực OTP</title>
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link href="//fonts.googleapis.com/css?family=Sirin+Stencil" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css">
    <link href="${pageContext.request.contextPath}/resource/css/style.css" rel="stylesheet" type="text/css" media="all" />
</head>
<body>
    <div class="container demo-1">
        <div class="content">
            <div id="large-header" class="large-header">
                <h1>Xác thực OTP</h1>
                <div class="main-agileits">
                    <div class="form-w3-agile">
                        <h2>Nhập mã OTP</h2>
                        <c:if test="${not empty message}">
                            <div class="alert ${param.error != null ? 'alert-danger' : 'alert-success'}">
                                <c:out value="${message}"/>
                            </div>
                        </c:if>
                        <form action="${pageContext.request.contextPath}/authservlet" method="POST">
                            <input type="hidden" name="action" value="forgotPassword"/>
                            <input type="hidden" name="email" value="${sessionScope.email}"/>
                            <div class="form-sub-w3">
                                <input type="text" name="otp" placeholder="Mã OTP" required />
                                <div class="icon-w3">
                                    <i class="fa fa-key" aria-hidden="true"></i>
                                </div>
                            </div>
                            <div class="form-sub-w3">
                                <input type="password" name="newPassword" placeholder="Mật khẩu mới" required />
                                <div class="icon-w3">
                                    <i class="fa fa-unlock-alt" aria-hidden="true"></i>
                                </div>
                            </div>
                            <div class="form-sub-w3">
                                <input type="password" name="confirmPassword" placeholder="Xác nhận mật khẩu" required />
                                <div class="icon-w3">
                                    <i class="fa fa-unlock-alt" aria-hidden="true"></i>
                                </div>
                            </div>
                            <div class="submit-w3l">
                                <input type="submit" value="Xác nhận">
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>