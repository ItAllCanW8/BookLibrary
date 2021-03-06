<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<html>

<body class="sub_page">
<div class="hero_area">
    <%@ include file="components/header.jsp" %>
    <c:set var="book" scope="page" value="${book}"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/autocomplete_form.css"/>
</div>

<section class="about_section layout_padding">
    <div class="side_img">
        <img src="${pageContext.request.contextPath}/images/layout/side-img.png" alt=""/>
    </div>
    <div class="container">

        <div class="row">
            <div class="col-md-6">
                <div class="img_container">
                    <c:if test="${book.id != null}">
                        <div class="img-box b1">
                            <img src="${pageContext.request.contextPath}/load_book_cover.do?bookCover=${book.cover}"
                                 alt=""/>
                        </div>

                        <label for="coverUpload">Upload book cover</label>
                        <form action="${pageContext.request.contextPath}/upload_book_cover.do?bookId=${book.id}"
                              method="post" enctype="multipart/form-data"
                              id="coverUpload">
                            <input type="file" name="bookCover" class="form-control-file"
                                   accept="image/jpeg, image/png"/>
                            <input type="submit" class="btn btn-outline-secondary" value="Upload"/>
                        </form>
                    </c:if>

                    <c:if test="${book.id == null}">
                        <div class="img-box b1">
                            <img src="${pageContext.request.contextPath}/load_book_cover.do?bookCover=default_book_cover.png"
                                 alt=""/>
                        </div>
                    </c:if>
                </div>
            </div>

            <div class="col-md-6">
                <div class="detail-box">
                    <c:if test="${book.id == null}">
                    <form id="addBookForm" method="POST"
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
                                           pattern="[\w\p\s,']{1,255}"
                                           value="${book.title}"/>
                                </h3>


                                <h5>
                                    <label for="inputBookPublisher">Publisher</label>
                                    <div class="form-group mt-1">
                                        <input type="text" id="inputBookPublisher" name="bookPublisher"
                                               class="form-control field"
                                               placeholder="3 - 45 characters"
                                               required
                                               pattern="[??-????-??\w\s,]{2,45}"
                                               value="${book.publisher}"/>
                                    </div>
                                </h5>

                                <h5>
                                    <label for="inputBookPublishDate">Publish date</label>
                                    <div class="form-group mt-1">
                                        <input type="date" id="inputBookPublishDate" name="bookPublishDate"
                                               class="form-control field"
                                               required
                                               max="${currentDate}"
                                               value="${book.publishDate}"/>
                                    </div>
                                </h5>

                                <h5>
                                    <label for="inputBookPageCount">Page count</label>
                                    <div class="form-group mt-1">
                                        <input type="text" id="inputBookPageCount" name="bookPageCount"
                                               class="form-control field"
                                               placeholder="0 - 32767"
                                               required
                                               pattern="[\d]{1,5}"
                                               value="${book.pageCount}"/>
                                    </div>
                                </h5>

                                <h5>
                                    <label for="inputBookISBN">ISBN</label>
                                    <div class="form-group mt-1">
                                        <input type="text" id="inputBookISBN" name="bookISBN"
                                               class="form-control field"
                                               placeholder="978-3-16-148410-0"
                                               required
                                               pattern="(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$"
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
                                               pattern="[\d]{1,5}"
                                               value="${book.totalAmount}"/>
                                    </div>
                                </h5>
                            </div>

                            <h4>
                                <label for="inputBookAuthors">Author(-s)</label>
                                <div class="form-group mt-1">
                                    <input type="text" id="inputBookAuthors" name="bookAuthors"
                                           class="form-control field"
                                           placeholder="3 - 255 characters"
                                           required
                                           pattern="([a-zA-Z'\s]{2,255}[,\s}]?)+"
                                           value="${book.authors}"/>
                                </div>
                            </h4>

                            <h4>
                                <label for="inputBookGenres">Genre(-s)</label>
                                <div class="form-group mt-1">
                                    <input type="text" id="inputBookGenres" name="bookGenres"
                                           class="form-control field"
                                           placeholder="3 - 255 characters"
                                           required
                                           pattern="([a-zA-Z-'\s]{2,255}[,\s}]?)+"
                                           value="${book.genres}"/>
                                </div>
                            </h4>

                            <hr style="width:100%;text-align:left;margin-left:0">
                            <c:if test="${book.id != null}">
                                <h4>
                                    <label>Status:</label>
                                    <div id="bookStatus">${book.status}</div>
                                </h4>
                            </c:if>

                            <h4>
                                <label for="inputBookDescription">Description</label>
                                <textarea id="inputBookDescription" name="bookDescription" class="form-control"
                                          rows="5"
                                          placeholder="up to 1000 characters"
                                          minlength="3"
                                          maxlength="1000">${book.description}</textarea>
                            </h4>

                            <input type="hidden" id="remainingAmount" name="remainingAmount">
                            <input type="hidden" id="bookStatusInput" name="bookStatus">

                            <button type="submit" id="saveToDBButt" class="btn btn-outline-success">Save</button>
                            <a href="${pageContext.request.contextPath}/home.do" class="btn btn-outline-warning"
                               role="button" aria-pressed="true">Discard</a>
                        </form>
                </div>
            </div>
        </div>
    </div>
</section>

<c:if test="${book.id != null}">
    <section class="course_section layout_padding-bottom">
        <div class="container">
            <div class="heading_container">
                <h3>
                    Borrow Records
                </h3>

                <hr style="width:100%;text-align:left;margin-left:0">
                <div>
                    <button type="button" id="addBorrowRecButt" class="btn btn-outline-success" data-bs-toggle="modal"
                            data-bs-target="#addBorrowRecModal" style="margin-top: 1%; margin-bottom: 1%">Add
                    </button>
                </div>
            </div>
            <div class="event_container">
                <table id="borrowRecTable" class="table table-bordered border-secondary">
                    <thead>
                    <tr>
                        <th scope="col">Reader email</th>
                        <th scope="col">Reader name</th>
                        <th scope="col">Borrow date</th>
                        <th scope="col">Due date</th>
                        <th scope="col">Return date</th>
                    </tr>
                    </thead>

                    <tbody>
                    </tbody>
                </table>
            </div>
        </div>

        <div class="modal fade" id="addBorrowRecModal" tabindex="-1" role="dialog"
             aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-dialog-scrollable">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4 class="modal-title">Add borrow record</h4>
                    </div>

                    <div class="modal-body">
                        <label for="readerEmailInput">Reader email</label>
                        <div class="form-group mt-1 search-input">
                            <input type="search" class="form-control field" id="readerEmailInput"
                                   name="readerEmail"
                                   placeholder="start to input..."
                                   pattern="((\w)|(\w[.-]\w))+@((\w)|(\w[.-]\w))+.[a-zA-Z??-????-??]{2,4}"
                                   required>
                            <ul id="matchList" class="autocom-box"></ul>
                        </div>

                        <div class="mt-3">
                            <label for="readerName">Reader name</label>
                        </div>
                        <div class="form-group mt-1">
                            <input type="text" class="form-control field" id="readerName" name="readerName"
                                   pattern="[a-zA-Z]{2,255}">
                        </div>

                        <div class="mt-3">
                            <label for="timePeriodSelect">Time period, months</label>
                        </div>
                        <div class="form-group mt-1">
                            <div class="btn-group">
                                <select id="timePeriodSelect">
                                    <option value="1" selected>1</option>
                                    <option value="2">2</option>
                                    <option value="3">3</option>
                                    <option value="6">6</option>
                                    <option value="12">12</option>
                                </select>
                            </div>
                        </div>
                    </div>

                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" id="closeAddModalButt" data-bs-dismiss="modal">
                            Discard
                        </button>
                        <button type="button" class="btn btn-primary" id="saveBorrowRecButt">Save</button>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="editBorRecModal" tabindex="-1" role="dialog"
             aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4 class="modal-title">Edit borrow record</h4>
                    </div>

                    <div class="modal-body">
                        <label for="readerEmailEdit">Reader email</label>
                        <div class="form-group mt-1 search-input">
                            <input type="text" class="form-control field" id="readerEmailEdit" readonly>
                        </div>

                        <div class="mt-3">
                            <label for="readerNameEdit">Reader name</label>
                        </div>
                        <div class="form-group mt-1">
                            <input type="text" class="form-control field" id="readerNameEdit" readonly>
                        </div>

                        <label for="borrowDate">Borrow date</label>
                        <div class="form-group mt-1">
                            <input type="date" id="borrowDate" class="form-control field" readonly/>
                        </div>

                        <div class="form-group mt-1">
                            <label for="timePeriodEdit">Time period, months</label>
                        </div>
                        <div class="form-group mt-1">
                            <input type="text" class="form-control field" id="timePeriodEdit" readonly>
                        </div>

                        <div class="form-group mt-1">
                            <label for="statusSelect">Status:</label>
                        </div>
                        <div class="form-group mt-1">
                            <div class="btn-group">
                                <select id="statusSelect">
                                    <option value="1" selected>returned</option>
                                    <option value="2">returned and damaged</option>
                                    <option value="3">lost</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-group mt-1">
                            <label for="commentInput">Comment</label>
                        </div>
                        <textarea id="commentInput" class="form-control"
                                  rows="3"
                                  minlength="3"
                                  maxlength="255"></textarea>

                        <input type="hidden" id="readerId">
                    </div>

                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" id="closeEditModalButt" data-bs-dismiss="modal">
                            Close
                        </button>
                        <button type="button" class="btn btn-primary" id="editBorRecButt">Save</button>
                    </div>
                </div>
            </div>
        </div>
    </section>


    <script type="application/javascript">
        let contextPath = `${pageContext.request.contextPath}`;
        const bookId = `${book.id}`;
        let remainingAmount = ${book.remainingAmount};
        let totalAmount = ${book.totalAmount};
        let bookStatus = document.getElementById('bookStatus');
    </script>
    <script src="${pageContext.request.contextPath}/js/book_page.js"></script>
</c:if>

</body>

</html>