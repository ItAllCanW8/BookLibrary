<%--
  Created by IntelliJ IDEA.
  User: ya
  Date: 30.10.2021
  Time: 14:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body class="sub_page">
<div class="hero_area">
    <%@ include file="components/header.jsp" %>
    <c:set var="books" scope="page" value="${books}"/>
</div>

<section class="course_section layout_padding-bottom">
    <div class="side_img">
        <img src="${pageContext.request.contextPath}/images/layout/side-img.png" alt=""/>
    </div>
    <div class="container">
        <h3>
            Search for books
        </h3>

        <hr style="width:100%;text-align:left;margin-left:0">

        <form id="searchForm" method="POST"
              action="${pageContext.request.contextPath}/search.do">

            <label for="inputBookTitle">Title</label>
            <input type="text" id="inputBookTitle" name="bookTitle"
                   class="form-control field"
                   placeholder="3 - 255 characters"
                   required/>

            <label for="inputBookAuthors">Author(-s)</label>
            <div class="form-group mt-1">
                <input type="text" id="inputBookAuthors" name="bookAuthors"
                       class="form-control field"
                       placeholder="3 - 255 characters"
                       required/>
            </div>

            <label for="inputBookGenres">Genre(-s)</label>
            <div class="form-group mt-1">
                <input type="text" id="inputBookGenres" name="bookGenres"
                       class="form-control field"
                       placeholder="3 - 255 characters"
                       required/>
            </div>

            <label for="inputBookDescription">Description</label>
            <textarea id="inputBookDescription" name="bookDescription" class="form-control"
                      rows="5"
                      placeholder="up to 1000 characters"
                      minlength="3"
                      maxlength="1000">${book.description}</textarea>
            <button type="submit" id="searchButt" class="btn btn-outline-success" style="margin-top: 1%">Search</button>
        </form>

        <div class="heading_container" style="display: none">
            <div class="row align-items-start" style="width:100%;">
                <hr style="width:100%;text-align:left;margin-left:0">
                <h3>
                    Search result
                </h3>
                <hr style="width:100%;text-align:left;margin-left:0">

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

        <div class="event_container" style="display: none">
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

<script src="${pageContext.request.contextPath}/js/search_page.js"></script>

<%--<script type="application/javascript">--%>
<%--    --%>
<%--</script>--%>

</body>
</html>
