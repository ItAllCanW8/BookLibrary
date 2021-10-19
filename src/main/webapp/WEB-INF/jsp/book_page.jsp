<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<html>

<body class="sub_page">
<div class="hero_area">
    <%@ include file="components/header.jsp" %>
    <c:set var="book" scope="page" value="${book}"/>
</div>

<section class="about_section layout_padding">

    <div class="side_img">
        <img src="${pageContext.request.contextPath}/images/layout/side-img.png" alt=""/>
    </div>
    <div class="container">
        <%--        <button id="butt">Delete</button>--%>
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

                            <h3>
                                <label for="inputBookTitle">Title</label>
                                <input type="text" id="inputBookTitle" name="bookTitle"
                                       class="form-control field"
                                       placeholder="3 - 255 characters"
                                       required
<%--                                       pattern="[А-Яа-я\w\p{Blank}\.,]{3,255}"--%>
                                       value="${book.title}"/>
                            </h3>

                            <h3>
                                <label for="bookAuthors">Author(-s)</label>
<%--                                <button type="button" onclick="add()" class="btn btn-success btn-sm add" id="addAuthor">Add author</button>--%>
                                <ul id="bookAuthors" class="list-group">
                                    <c:forEach var="author" items="${book.authors}">
                                        <li class="list-group-item">${author}
                                            <button
                                                    class="btn btn-danger btn-sm float-right delete">X</button>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </h3>

                            <h3>
                                <label for="inputBookPublisher">Publisher</label>
                                <div class="form-group mt-1">
                                    <input type="text" id="inputBookPublisher" name="bookPublisher"
                                           class="form-control field"
                                           placeholder="3 - 45 characters"
                                           required
<%--                                           pattern="[А-Яа-я\w\p{Blank}\.,]{3,45}"--%>
                                           value="${book.publisher}"/>
                                </div>
                            </h3>

                            <h3>
                                <label for="inputBookPublishDate">Publish date</label>
                                <div class="form-group mt-1">
                                    <input type="date" id="inputBookPublishDate" name="bookPublishDate"
                                           class="form-control field"
                                           required
                                           max="${currentDate}"
                                           value="${book.publishDate}"/>
                                </div>
                            </h3>

                            <h3>
                                <label for="inputBookGenres">Genre(-s)</label>
                                <c:forEach var="genre" items="${book.genres}">
                                    <div class="form-group mt-1">
                                        <input type="text" id="inputBookGenres" name="bookGenres"
                                               class="form-control field"
                                               placeholder="3 - 255 characters"
                                               required
<%--                                               pattern="[a-zA-Z\.,\p{Blank}]{3,255}"--%>
                                               value="${genre}"/>
                                    </div>
                                </c:forEach>
                            </h3>

                            <h3>
                                <label for="inputBookPageCount">Page count</label>
                                <div class="form-group mt-1">
                                    <input type="text" id="inputBookPageCount" name="bookPageCount"
                                           class="form-control field"
                                           placeholder="0 - 32767"
                                           required
<%--                                           pattern="[\d]{1,5}"--%>
                                           value="${book.pageCount}"/>
                                </div>
                            </h3>

                            <h3>
                                <label for="inputBookISBN">ISBN-13</label>
                                <div class="form-group mt-1">
                                    <input type="text" id="inputBookISBN" name="bookISBN"
                                           class="form-control field"
                                           placeholder="978-3-16-148410-0"
                                           required
<%--                                           pattern="[\d-]{13,45}"--%>
                                           value="${book.isbn}"/>
                                </div>
                            </h3>

                            <h3>
                                <label for="inputBookTotalAmount">Total amount</label>
                                <div class="form-group mt-1">
                                    <input type="text" id="inputBookTotalAmount" name="bookTotalAmount"
                                           class="form-control field"
                                           placeholder="0 - 32767"
                                           required
<%--                                           pattern="[\d]{1,5}"--%>
                                           value="${book.totalAmount}"/>
                                </div>
                            </h3>
                            <%--                            </div>--%>

                            <hr style="width:100%;text-align:left;margin-left:0">
                            <c:if test="${book.id != null}">
                                <h3>
                                    <label>Status:</label>
                                        ${book.status}
                                </h3>
                            </c:if>

                            <h5>
                                <label for="inputBookDescription">Description</label>
                                <textarea id="inputBookDescription" name="bookDescription" class="form-control"
                                          rows="5"
                                          placeholder="up to 1000 characters"
                                          minlength="3"
                                          maxlength="1000">${book.description}</textarea>
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

<%--<script src="${pageContext.request.contextPath}/js/scripts.js">--%>

<%--</script>--%>

<script type="application/javascript">
<%--     <label for="addAuthorForm">Add author</label>--%>
<%--                            <form id="addAuthorForm" class="form-inline mb-3">--%>
<%--                                <input type="text" class="form-control mr-2" id="addAuthorInput" />--%>
<%--&lt;%&ndash;                                <input type="submit" class="btn btn-dark" value="Submit" />&ndash;%&gt;--%>
<%--                            </form>--%>
    let authors = document.getElementById('bookAuthors');
    var genres = document.getElementById('bookGenres');

    authors.addEventListener('click', remove);
    genres.addEventListener('click', remove);

    let addAuthor = document.getElementById('addAuthor');
    addAuthor.addEventListener('click', add);

    function remove(e){
        if(e.target.classList.contains('delete')){
            if(e.target.parentElement.parentElement.id === 'bookAuthors'){
                authors.removeChild(e.target.parentElement);
            } else {
                genres.removeChild(e.target.parentElement);
            }
        }
    }

    function add(e){
        // e.preventDefault();

        console.log('ХУЙ')

        let newItem = document.getElementById('addAuthorInput').value;
        let li = document.createElement('li');

        li.className = 'list-group-item';
        li.appendChild(document.createTextNode(newItem));

        var deleteBtn = document.createElement('button');
        deleteBtn.className = 'btn btn-danger btn-sm float-right delete';
        deleteBtn.appendChild(document.createTextNode('X'));

        li.appendChild(deleteBtn);

        authors.appendChild(li);
    }
</script>


</body>

</html>