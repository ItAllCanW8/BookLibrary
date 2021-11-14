document.getElementById('searchLink').classList.add('active');

let books = [];

const loadBooks = async (bookTitle, bookGenres, bookAuthors, bookDesc) => {
    const res = await fetch(
        `${contextPath}/search.do?bookTitle=${bookTitle}&bookGenres=${bookGenres}&bookAuthors=${bookAuthors}&bookDescription=${bookDesc}`);

    if(books.length > 0){
        books.length = 0;
    }

    if (res.status === 200) {
        books = await res.json();
    }

    fillBooksTable(books);
};

function search(e) {
    e.preventDefault();

    const bookTitle = document.getElementById('inputBookTitle').value;
    const bookGenres = document.getElementById('inputBookGenres').value;
    const bookAuthors = document.getElementById('inputBookAuthors').value;
    const bookDesc = document.getElementById('inputBookDescription').value;

    if (!(bookTitle === "" && bookGenres === "" && bookAuthors === "" && bookDesc === "")) {
        loadBooks(bookTitle, bookGenres, bookAuthors, bookDesc);

        document.getElementsByClassName('heading_container')[0].style.display = 'block';
        document.getElementsByClassName('event_container')[0].style.display = 'block';
    } else {
        alert("AT LEAST ONE FIELD SHOULD BE FILLED");
    }
}

function fillBooksTable(books) {
    let table = document.getElementById('booksTable').getElementsByTagName('tbody')[0]
    table.innerHTML = '';

    let rowCount = table.rows.length;

    for (let i = 0; i < books.length; i++) {
        const row = table.insertRow(rowCount);

        const titleCell = row.insertCell(0);
        const bookLink = document.createElement('a');
        bookLink.href = `${contextPath}/book_page.do?bookId=${books[i].id}`;
        bookLink.innerHTML = document.createTextNode(books[i].title).textContent;
        titleCell.appendChild(bookLink);

        const authorsCell = row.insertCell(1);
        const ul = document.createElement('ul');

        for (const author of books[i].authors) {
            const li = document.createElement('li');
            li.innerHTML += author;
            ul.appendChild(li);
        }
        authorsCell.appendChild(ul);

        const publishDateCell = row.insertCell(2);
        publishDateCell.appendChild(document.createTextNode(new Date(books[i].publishDate).toLocaleDateString()));

        const remainingAmountCell = row.insertCell(3);
        const remainingAmount = document.createTextNode(books[i].remainingAmount);
        remainingAmountCell.appendChild(remainingAmount);
    }
}

document.getElementById('availableBooks').addEventListener('click', (e) => {
    e.preventDefault();

    let filteredBooks = [];
    for (const book of books) {
        if(book.remainingAmount > 0){
            filteredBooks.push(book);
        }
    }

    // if(filteredBooks.length > 0){
        fillBooksTable(filteredBooks);
    // }
});

document.getElementById('unavailableBooks').addEventListener('click', (e) => {
    e.preventDefault();

    let filteredBooks = [];
    for (const book of books) {
        if(book.remainingAmount === 0){
            filteredBooks.push(book);
        }
    }

    // if(filteredBooks.length > 0){
        fillBooksTable(filteredBooks);
    // }
});

document.getElementById('allBooks').addEventListener('click', (e) => {
    e.preventDefault();

    fillBooksTable(books);
});

const searchButt = document.getElementById('searchButt');
searchButt.addEventListener('click', search);