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
                Books
            </h3>

            <hr style="width:100%;text-align:left;margin-left:0">

            <div class="row align-items-start" style="width:100%;">
                <div style="display: flex;justify-content: center">
                    <a href="${pageContext.request.contextPath}/book_page.do" class="btn btn-outline-success"
                       role="button" style="width: 40%" aria-pressed="true">Add</a>
                </div>

                <div style="display: flex;justify-content: center">
                    <button type="button" class="btn btn-outline-danger" data-bs-toggle="modal"
                            data-bs-target="#deleteBooksModal" style="width: 40%; margin-top: 1%; margin-bottom: 1%">
                        Delete
                    </button>
                </div>

                <div class="col-2" style="display: flex;justify-content: center">
                    <div class="dropdown">
                        <button class="btn btn-secondary dropdown-toggle" type="button" id="recordsPerPage"
                                data-bs-toggle="dropdown"
                                aria-expanded="false">Records per Page
                        </button>
                        <ul class="dropdown-menu dropdown-menu-dark" aria-labelledby="recordsPerPage">
                            <li><a class="dropdown-item"
                                   href="${pageContext.request.contextPath}/home.do?recordsPerPage=10&filter=${filterMode}">10</a>
                            </li>
                            <li><a class="dropdown-item"
                                   href="${pageContext.request.contextPath}/home.do?recordsPerPage=20&filter=${filterMode}">20</a>
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

            <div class="modal fade" id="deleteBooksModal" tabindex="-1" role="dialog"
                 aria-labelledby="exampleModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h4 class="modal-title">Delete books</h4>
                        </div>

                        <div class="modal-body">
                            <form method="post" id="deleteBooksForm"
                                  action="${pageContext.request.contextPath}/delete_books.do">
                                <c:forEach var="book" items="${books}">
                                    <div>
                                        <label for="${book.id}">Title: ${book.title}</label>
                                        <input type="checkbox" id="${book.id}" style="float:right">
                                    </div>
                                </c:forEach>
                                <input type="hidden" name="bookIds" id="bookIdsInput">
                            </form>
                        </div>

                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close
                            </button>
                            <button type="button" class="btn btn-primary" id="deleteBooksButt">Delete selected</button>
                        </div>
                    </div>
                </div>
            </div>

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

<script src="${pageContext.request.contextPath}/js/home.js"></script>

</body>

</html>