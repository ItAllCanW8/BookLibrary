<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<html>

<body class="sub_page">
<div class="hero_area">
    <%@ include file="components/header.jsp" %>
    <c:set var="book" scope="request" value="${book}"/>
</div>

<section class="about_section layout_padding">
    <div class="side_img">
        <img src="${pageContext.request.contextPath}/images/layout/side-img.png" alt=""/>
    </div>
    <div class="container">

        <div class="row">
            <div class="col-md-6">
                <div class="img_container">
                    <div class="img-box b1">
                        <img src="${pageContext.request.contextPath}/load_book_cover.do?bookCover=${book.cover}"
                             alt=""/>
                    </div>

                    <label for="coverUpload">Upload book cover</label>
                    <form action="${pageContext.request.contextPath}/upload_book_cover.do?bookId=${book.id}"
                          method="post" enctype="multipart/form-data"
                          id="coverUpload">
                        <input type="file" name="bookCover" class="form-control-file" accept="image/jpeg, image/png"/>
                        <input type="submit" class="btn btn-outline-secondary" value="Upload"/>
                    </form>
                </div>
            </div>

            <div class="col-md-6">
                <div class="detail-box">
                    <c:if test="${book.id == null}">
                    <form id="editBookForm" method="POST"
                          action="${pageContext.request.contextPath}/add_book.do">
                        </c:if>

                        <c:if test="${book.id != null}">
                        <form id="editBookForm" method="POST"
                              action="${pageContext.request.contextPath}/edit_book.do?bookId=${book.id}">
                            </c:if>

                            <div class="heading_container">
                                <h3>
                                    <label for="inputBookTitle">Title</label>
                                    <input type="text" id="inputBookTitle" name="bookTitle"
                                           class="form-control field"
                                           placeholder="3 - 255 characters"
                                           required
                                           value="${book.title}" pattern="[А-Яа-я\w\p{Blank}.,]{3,255}"/>
                                </h3>

                                <h5>
                                    <label for="inputBookAuthors">Author(-s)</label>
                                    <div class="form-group mt-1">
                                        <input type="text" id="inputBookAuthors" name="bookAuthors"
                                               class="form-control field"
                                               placeholder="3 - 255 characters"
                                               required
                                               value="${book.authors}" pattern="[А-Яа-яa-zA-Z.,\p{Blank}]{3,255}"/>
                                    </div>
                                </h5>

                                <h5>
                                    <label for="inputBookPublisher">Publisher</label>
                                    <div class="form-group mt-1">
                                        <input type="text" id="inputBookPublisher" name="bookPublisher"
                                               class="form-control field"
                                               placeholder="3 - 45 characters"
                                               required
                                               value="${book.publisher}" pattern="[А-Яа-я\w\p{Blank}.,]{3,45}"/>
                                    </div>
                                </h5>

                                <h5>
                                    <label for="inputBookPublishDate">Publish date</label>
                                    <div class="form-group mt-1">
                                        <input type="date" id="inputBookPublishDate" name="bookPublishDate"
                                               class="form-control field"
                                               required
                                               value="${book.publishDate}"/>
                                    </div>
                                </h5>

                                <h5>
                                    <label for="inputBookGenres">Genre(-s)</label>
                                    <div class="form-group mt-1">
                                        <input type="text" id="inputBookGenres" name="bookGenres"
                                               class="form-control field"
                                               placeholder="3 - 255 characters"
                                               required
                                               value="${book.genres}" pattern="[a-zA-Z.,\p{Blank}]{3,255}"/>
                                    </div>
                                </h5>

                                <h5>
                                    <label for="inputBookPageCount">Page count</label>
                                    <div class="form-group mt-1">
                                        <input type="text" id="inputBookPageCount" name="bookPageCount"
                                               class="form-control field"
                                               placeholder="0 - 32767"
                                               required
                                               value="${book.pageCount}" pattern="[\d]{1,5}"/>
                                    </div>
                                </h5>

                                <h5>
                                    <label for="inputBookISBN">ISBN-13</label>
                                    <div class="form-group mt-1">
                                        <input type="text" id="inputBookISBN" name="bookISBN"
                                               class="form-control field"
                                               placeholder="978-3-16-148410-0"
                                               required
                                               pattern="[\d-]{13,45}"
                                               value="${book.isbn}"/>
                                    </div>
                                </h5>

                                <h5>
                                    <label for="inputBookTotalAmount">Total amount</label>
                                    <div class="form-group mt-1">
                                        <input type="text" id="inputBookTotalAmount" name="bookTotalAmount"
                                               class="form-control field"
                                               placeholder="0 - 32767"
                                               required
                                               value="${book.totalAmount}" pattern="[\d]{1,5}"/>
                                    </div>
                                </h5>

                                <c:if test="${book.id != null}">
                                    <h5>
                                        <label>Status</label>
                                            ${book.status}
                                    </h5>
                                </c:if>
                            </div>

                            <hr style="width:100%;text-align:left;margin-left:0">
                            <h5>
                                <label for="inputBookDescription">Description</label>
                                <textarea id="inputBookDescription" name="bookDescription" class="form-control"
                                          rows="5"
                                          placeholder="up to 1000 characters"
                                          minlength="3"
                                          maxlength="1000">${book.description}
                                </textarea>
                            </h5>

                            <button type="submit" class="btn btn-outline-success">Save</button>
                            <a href="${pageContext.request.contextPath}/home.do" class="btn btn-outline-warning"
                               role="button" aria-pressed="true">Discard</a>
                        </form>
                </div>
            </div>
        </div>
    </div>
</section>

</body>

</html>