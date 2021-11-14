<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<body class="sub_page">
<div class="hero_area">
    <%@ include file="../components/header.jsp" %>
</div>

<h1 style="margin-top: 10%; display: flex; justify-content: center">404 status error</h1>
<h1 style="margin-bottom: 5%; margin-top: 3%; display: flex; justify-content: center">requested page not found</h1>
<div class="col-2 offset-5">
    <a class="btn btn-outline-secondary"
       style="width: 100%; display: flex; justify-content: center"
       href="${pageContext.request.contextPath}/home.do" role="button">Home</a>
</div>
</body>
</html>
