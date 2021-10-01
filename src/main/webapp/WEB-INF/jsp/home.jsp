<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>

<body>
<div class="hero_area">
    <%@ include file="components/header.jsp" %>
    <c:set var="books" scope="request" value="${books}"/>

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
                                        <h1>
                                            L I B R A R Y
                                        </h1>
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
                                        <h1>
                                            E D U C A T I O N
                                        </h1>
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
                                        <h1>
                                            L E I S U R E
                                        </h1>
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

            <button type="button" class="btn btn-secondary" style="width: 40%" data-bs-toggle="modal"
                    data-bs-target="#addBookModal">
                Add Book
            </button>

            <hr style="width:100%;text-align:left;margin-left:0">
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
                            <th scope="row">${book.authors}</th>
                            <th scope="row">${book.publishDate}</th>
                            <th scope="row">${book.remainingAmount}</th>
                        <tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</section>

</body>

</html>