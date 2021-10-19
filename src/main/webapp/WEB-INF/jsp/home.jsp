<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>

<body>
<div class="hero_area">
    <%@ include file="components/header.jsp" %>
    <c:set var="books" scope="page" value="${books}"/>

    <section class=" slider_section position-relative">
        <div id="carouselExampleIndicators" class="carousel slide" data-bs-ride="carousel">
            <ol class="carousel-indicators">
                <li data-bs-target="#carouselExampleIndicators" data-bs-slide-to="0" class="active"></li>
                <li data-bs-target="#carouselExampleIndicators" data-bs-slide-to="1"></li>
                <li data-bs-target="#carouselExampleIndicators" data-bs-slide-to="2"></li>
            </ol>
            <div class="carousel-inner">
                <div class="carousel-item active">
                    <div class="container">
                        <div class="row">
                            <div class="col">
                                <div class="detail-box">
                                    <div>
                                        <h1>L I B R A R Y</h1>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="carousel-item ">
                    <div class="container">
                        <div class="row">
                            <div class="col">
                                <div class="detail-box">
                                    <div>
                                        <h1>E D U C A T I O N</h1>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="carousel-item ">
                    <div class="container">
                        <div class="row">
                            <div class="col">
                                <div class="detail-box">
                                    <div>
                                        <h1>L E I S U R E</h1>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</div>

<section class="course_section layout_padding-bottom">
    <div class="side_img">
        <img src="${pageContext.request.contextPath}/images/layout/side-img.png" alt=""/>
    </div>
    <div class="container">
        <div class="heading_container">
            <h3>
                Book List
            </h3>

            <hr style="width:100%;text-align:left;margin-left:0">

            <div class="row align-items-start" style="width:100%;">
                <div style="display: flex;justify-content: center">
                    <a href="${pageContext.request.contextPath}/book_page.do" class="btn btn-outline-success"
                       role="button" style="width: 40%" aria-pressed="true">Add book</a>
                </div>

                <%--                <button type="button" class="btn btn-secondary" data-bs-toggle="modal"--%>
                <%--                        data-bs-target="#editProfileModal">--%>
                <%--                    <fmt:message key="button.edit"/>--%>
                <%--                </button>--%>

                <div style="display: flex;justify-content: center">
                    <a href="${pageContext.request.contextPath}/book_page.do" class="btn btn-outline-danger"
                       role="button" style="width: 40%; margin-top: 1%; margin-bottom: 1%" aria-pressed="true">Delete Books</a>
                </div>

                <div class="col-2" style="display: flex;justify-content: center">
                    <div class="dropdown">
                        <button class="btn btn-secondary dropdown-toggle" type="button" id="recordsPerPage"
                                data-bs-toggle="dropdown"
                                aria-expanded="false">Records per Page
                        </button>
                        <ul class="dropdown-menu dropdown-menu-dark" aria-labelledby="recordsPerPage">
                            <li><a class="dropdown-item"
                                   href="${pageContext.request.contextPath}/home.do?recordsPerPage=1&filter=${filterMode}">10</a>
                            </li>
                            <li><a class="dropdown-item"
                                   href="${pageContext.request.contextPath}/home.do?recordsPerPage=2&filter=${filterMode}">20</a>
                            </li>
                        </ul>
                    </div>
                </div>

                <div class="col-2" style="display: flex;justify-content: center">
                    <div class="dropdown">
                        <button class="btn btn-secondary dropdown-toggle" type="button" id="filterByAvailability"
                                data-bs-toggle="dropdown"
                                aria-expanded="false">Filter by availability
                        </button>
                        <ul class="dropdown-menu dropdown-menu-dark" aria-labelledby="filterByAvailability">
                            <li><a class="dropdown-item"
                                   href="${pageContext.request.contextPath}/home.do?filter=available&recordsPerPage=${recordsPerPage}&page=${currentPage}">Available</a>
                            </li>
                            <li><a class="dropdown-item"
                                   href="${pageContext.request.contextPath}/home.do?filter=unavailable&recordsPerPage=${recordsPerPage}&page=${currentPage}">Unavailable</a>
                            </li>
                            <li><a class="dropdown-item"
                                   href="${pageContext.request.contextPath}/home.do">All</a></li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>

        <div class="event_container">
            <table id="booksTable" class="table table-dark table-bordered border-secondary">
                <thead>
                <tr>
                    <th scope="col">Title</th>
                    <th scope="col">Author(-s)</th>
                    <th scope="col">Publish date</th>
                    <th scope="col">Remaining amount</th>
                </tr>
                </thead>

                <tbody>
                <c:forEach var="book" items="${books}">
                <tr class="table-secondary">
                    <th scope="row">
                        <a href="${pageContext.request.contextPath}/book_page.do?bookId=${book.id}">
                                ${book.title}
                        </a>
                    </th>
                    <th scope="row">
                        <ul>
                            <c:forEach var="author" items="${book.authors}">
                                <li>${author}</li>
                            </c:forEach>
                        </ul>
                    </th>
                    <th scope="row">${book.publishDate}</th>
                    <th scope="row">${book.remainingAmount}</th>
                <tr>
                    </c:forEach>
                </tbody>
            </table>

            <c:if test="${currentPage != 1}">
                <th><a href="home.do?page=${currentPage - 1}&recordsPerPage=${recordsPerPage}&filter=${filterMode}">Previous</a>
                </th>
            </c:if>
            <c:if test="${currentPage lt numberOfPages}">
                <th>
                    <a href="home.do?page=${currentPage + 1}&recordsPerPage=${recordsPerPage}&filter=${filterMode}">Next</a>
                </th>
            </c:if>

            <table class="table-bordered">
                <tr>
                    <c:forEach begin="1" end="${numberOfPages}" var="i">
                        <c:choose>
                            <c:when test="${currentPage eq i}">
                                <th>${i}</th>
                            </c:when>
                            <c:otherwise>
                                <th>
                                    <a href="home.do?page=${i}&recordsPerPage=${recordsPerPage}&filter=${filterMode}">${i}</a>
                                </th>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </tr>
            </table>
        </div>
    </div>
</section>

</body>

</html>