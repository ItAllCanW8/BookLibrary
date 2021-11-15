let borrowRecords = [];
window.onload = () => loadBorrowRecs();
let initialBorRecLength = 0;

let borrowRecsToUpd = [];

let readers;
let readersToInsert = [];
let isNewReaderPresent = false;

let bookAvailabilityDate;

const titlePattern = /[\w\s,']{1,255}/;
const publisherPattern = /[\w\s,]{2,45}/;
const pageCountPattern = /[\d]{1,5}/;
const isbnPattern = /(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$/;
const authorsPattern = /([a-zA-Z'\s]{2,255}[,\s}]?)+/;
const genresPattern = /([a-zA-Z-'\s]{2,255}[,\s}]?)+/;
const descPattern = /[\w\s.]{0,1000}/;

const readerEmailPattern = /((\w)|(\w[.-]\w))+@((\w)|(\w[.-]\w))+.[a-zA-Zа-яА-Я]{2,4}/;
const readerNamePattern = /[a-zA-Z]{2,255}/;
const commentPattern = /[А-Яа-я\w\s.,#!$%^&*;:{}=\-_`~()]{3,255}/;

const saveToDBButt = document.getElementById('saveToDBButt');
saveToDBButt.addEventListener('click', saveChangesToDB);

const saveBorrowRecButt = document.getElementById('saveBorrowRecButt');
saveBorrowRecButt.addEventListener('click', addBorrowRec);

const editBorRecButt = document.getElementById('editBorRecButt');
editBorRecButt.addEventListener('click', editBorRec)

const matchList = document.getElementById('matchList');

const listItems = document.getElementById('matchList');
listItems.addEventListener('click', itemSelected)

const readerNameInput = document.getElementById('readerName');
const readerEmailInput = document.getElementById('readerEmailInput');
readerEmailInput.addEventListener('input', emailInput);

const addBorrowRecButt = document.getElementById('addBorrowRecButt');
addBorrowRecButt.addEventListener('click', () => loadReaders());

const timePeriodSelected = document.getElementById("timePeriodSelect");

const inputBookTotalAmount = document.getElementById('inputBookTotalAmount');
inputBookTotalAmount.addEventListener('change', totalAmountValidation);

async function saveChangesToDB(e) {
    e.preventDefault();

    if(!isBookFormValid(e)){
        return;
    }

    const newBorRecLength = borrowRecords.length;

    let successCounter = 0;
    let counterExpectedVal = 0;

    let errors = [];

    if (readersToInsert.length > 0) {
        counterExpectedVal++;

        await fetch(`${contextPath}/add_readers.do`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(Array.from(readersToInsert))
        })
            .then(res => res.status === 200)
            .then(res => {
                if (res) {
                    successCounter++;
                }
            })
            .catch(error => {
                errors.push(error.message);
            });
    }

    if (newBorRecLength > initialBorRecLength && successCounter === counterExpectedVal) {
        counterExpectedVal++;

        let borrowRecsToInsert = [];
        for (let i = initialBorRecLength; i < newBorRecLength; i++) {
            borrowRecsToInsert.push(borrowRecords[i]);
        }

        await fetch(`${contextPath}/add_borrow_records.do`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(Array.from(borrowRecsToInsert))
        })
            .then(res => res.status === 200)
            .then(res => {
                if (res) {
                    successCounter++;
                }
            })
            .catch(error => {
                errors.push(error.message);
            });
    }
    if (borrowRecsToUpd.length > 0 && successCounter === counterExpectedVal) {
        counterExpectedVal++;

        await fetch(`${contextPath}/update_borrow_records.do`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(Array.from(borrowRecsToUpd))
        })
            .then(res => res.status === 200)
            .then(res => {
                if (res) {
                    successCounter++;
                }
            })
            .catch(error => {
                errors.push(error.message);
            });
    }

    if(successCounter === counterExpectedVal){
        document.getElementById('remainingAmount').value = remainingAmount;
        document.getElementById('bookStatusInput').value = bookStatus.innerText;

        const bookEditForm = document.getElementById('editBookForm');

        const xhr = new XMLHttpRequest();
        xhr.open("POST", `${contextPath}/edit_book.do?bookId=${bookId}`);
        xhr.onload = function (event) {
            if (event.target.status === 200 && successCounter === counterExpectedVal) {
                // wait for user to press OK than reload page
                if (!alert('ALL CHANGES WERE SAVED!')) {
                    location.reload();
                }
            } else {
                errors.push('OOPS! SOME ERROR OCCURRED DURING SAVING CHANGES.');
            }
        };

        const formData = new FormData(bookEditForm);
        xhr.send(formData);
    }

    if(errors.length > 0){
        let msg = '';

        for (const err of errors) {
            msg += `!!! ${err}\n`;
        }

        alert(msg);
    }
}

function isBookFormValid(e){
    let result = true;

    const title = document.getElementById('inputBookTitle');
    const publisher = document.getElementById('inputBookPublisher');
    const pageCount = document.getElementById('inputBookPageCount');
    const isbn = document.getElementById('inputBookISBN');
    const authors = document.getElementById('inputBookAuthors');
    const genres = document.getElementById('inputBookGenres');
    const desc = document.getElementById('inputBookDescription');

    let errorMsg = '';

    if(!titlePattern.test(title.value)){
        result = false;
        errorMsg += 'BOOK TITLE ISNT VALID!';
    }
    if(!publisherPattern.test(publisher.value)){
        result = false;
        errorMsg += '\nBOOK PUBLISHER ISNT VALID!';
    }
    if(!pageCountPattern.test(pageCount.value)){
        result = false;
        errorMsg += '\nBOOK PAGE COUNT ISNT VALID!';
    }
    if(!isbnPattern.test(isbn.value)){
        result = false;
        errorMsg += '\nBOOK ISBN ISNT VALID!';
    }
    if(!authorsPattern.test(authors.value)){
        result = false;
        errorMsg += '\nBOOK AUTHORS STR ISNT VALID!';
    }
    if(!genresPattern.test(genres.value)){
        result = false;
        errorMsg += '\nBOOK GENRES STR ISNT VALID!';
    }
    if(!descPattern.test(desc.value)){
        result = false;
        errorMsg += '\nBOOK DESC ISNT VALID!';
    }

    if(errorMsg.length > 0){
        alert(errorMsg);
    }

    return result;
}

const loadReaders = async () => {
    if (typeof readers === 'undefined') {
        const res = await fetch(`${contextPath}/load_readers.do`);

        if (res.status === 200) {
            readers = await res.json();
        }
    }
};

const loadBorrowRecs = async () => {
    const res = await fetch(`${contextPath}/load_borrow_records.do?bookId=` + bookId);

    if (res.status === 200) {
        borrowRecords = await res.json();
        initialBorRecLength = borrowRecords.length;

        if (remainingAmount < totalAmount) {
            await loadAvailabilityDate();
        }

        fillBorrowRecTable();
    }
};

function editBorRec() {
    const newComment = document.getElementById('commentInput').value;

    if(newComment === "" || commentPattern.test(newComment)){
        const readerId = document.getElementById('readerId').innerText;
        const statusSelect = document.getElementById('statusSelect');
        const newStatus = statusSelect.options[statusSelect.selectedIndex].text;

        for (let i = 0; i < borrowRecords.length; i++) {
            if (borrowRecords[i].reader.email === readerId.slice(0, -2)) {
                const borrowRecord = borrowRecords[i];
                const oldStatus = borrowRecord.status;
                const oldComment = borrowRecord.comment;
                const email = borrowRecord.reader.email;

                if (oldStatus !== newStatus) {
                    borrowRecord.status = newStatus;

                    const returnDate = toISOLocal(new Date()).slice(0, 19).replace('T', ' ');
                    borrowRecord.returnDate = returnDate;
                    document.getElementById(email + 'RD').innerText = returnDate;
                }

                if (oldComment !== newComment) {
                    borrowRecord.comment = newComment;
                }

                if (oldStatus !== newStatus || oldComment !== newComment) {
                    let j = 0;
                    for (j; j < borrowRecsToUpd.length; j++) {
                        if (borrowRecsToUpd[j].reader.email === email) {
                            const borRecToUpd = borrowRecsToUpd[j];

                            borRecToUpd.status = newStatus;
                            borRecToUpd.returnDate = borrowRecord.returnDate;
                            borRecToUpd.comment = newComment;

                            break;
                        }
                    }

                    if (j === borrowRecsToUpd.length) {
                        borrowRecsToUpd.push(borrowRecord);
                    }
                }

                if (newStatus === 'lost' || newStatus === 'returned and damaged') {
                    totalAmount--;
                    inputBookTotalAmount.value = totalAmount;
                } else {
                    remainingAmount++;
                }

                updateBookStatus();

                break;
            }
        }

        document.getElementById('closeEditModalButt').click();
    } else {
        alert('PLEASE INPUT VALID COMMENT!');
    }
}

function monthDiff(dateFrom, dateTo) {
    return dateTo.getMonth() - dateFrom.getMonth() +
        (12 * (dateTo.getFullYear() - dateFrom.getFullYear()))
}

function createNameDiv(readerName, readerEmail) {
    const nameDiv = document.createElement("div");
    nameDiv.id = readerEmail + 'Id';
    nameDiv.setAttribute('data-bs-toggle', 'modal');
    nameDiv.setAttribute('data-bs-target', '#editBorRecModal');

    nameDiv.onmouseover = () => {
        nameDiv.style.cursor = 'pointer';
    };

    nameDiv.onclick = (e) => {
        document.getElementById('readerId').innerText = e.target.id;

        for (let i = 0; i < borrowRecords.length; i++) {
            if (borrowRecords[i].reader.email === e.target.id.slice(0, -2)) {
                const borrowRecord = borrowRecords[i];

                document.getElementById('readerEmailEdit').value = borrowRecord.reader.email;
                document.getElementById('readerNameEdit').value = borrowRecord.reader.name;
                document.getElementById('borrowDate').value = borrowRecord.borrowDate.slice(0, 10);

                const dueDate = new Date(borrowRecord.dueDate.slice(0, 10));
                const borrowDate = new Date(borrowRecord.borrowDate.slice(0, 10));
                document.getElementById('timePeriodEdit').value = monthDiff(borrowDate, dueDate);

                const statusSelect = document.getElementById('statusSelect');

                const commentInput = document.getElementById('commentInput');
                const editBorRecButt = document.getElementById('editBorRecButt');

                if (typeof borrowRecord.returnDate !== 'undefined') {
                    commentInput.disabled = true;
                    editBorRecButt.hidden = true;
                } else {
                    commentInput.disabled = false;
                    editBorRecButt.hidden = false;
                }

                if (typeof borrowRecord.status === 'undefined') {
                    statusSelect.value = 1;
                } else {
                    statusSelect.selectedIndex = [...statusSelect.options]
                        .findIndex(option => option.text === borrowRecord.status);
                }

                let comment = '';
                if (typeof borrowRecord.comment !== 'undefined') {
                    comment = borrowRecord.comment;
                }

                commentInput.value = comment;

                break;
            }
        }
    };

    nameDiv.appendChild(document.createTextNode(readerName));

    return nameDiv;
}

function fillBorrowRecTable() {
    const table = document.getElementById('borrowRecTable').getElementsByTagName('tbody')[0]

    const rowCount = table.rows.length;

    for (let i = 0; i < borrowRecords.length; i++) {
        const row = table.insertRow(rowCount);

        const emailCell = row.insertCell(0);
        const email = document.createTextNode(borrowRecords[i].reader.email);
        emailCell.appendChild(email);

        const nameCell = row.insertCell(1);
        nameCell.appendChild(createNameDiv(borrowRecords[i].reader.name, borrowRecords[i].reader.email));

        const borrowDateCell = row.insertCell(2);
        borrowDateCell.appendChild(document.createTextNode(new Date(borrowRecords[i].borrowDate).toLocaleDateString()));

        const dueDateCell = row.insertCell(3);
        dueDateCell.appendChild(document.createTextNode(new Date(borrowRecords[i].dueDate).toLocaleDateString()));

        const returnDateCell = row.insertCell(4);
        const returnDateDiv = document.createElement("div");
        returnDateDiv.id = borrowRecords[i].reader.email + 'RD';

        if (typeof borrowRecords[i].returnDate !== 'undefined') {
            const returnDate = toISOLocal(new Date(borrowRecords[i].returnDate)).slice(0, 19).replace('T', ' ');

            returnDateDiv.appendChild(document.createTextNode(returnDate));
        } else {
            returnDateDiv.appendChild(document.createTextNode('-'));
        }
        returnDateCell.appendChild(returnDateDiv);
    }
}

function emailInput() {
    matchList.innerHTML = '';
    readerNameInput.value = '';

    if (readerEmailInput.value.length > 3) {
        const emailInput = readerEmailInput.value;
        const matchingReaders = new Map();

        for (let i = 0; i < readers.length; i++) {
            const reader = readers[i];

            if (reader.email.includes(emailInput)) {
                matchingReaders.set(reader.email, reader.name);
            }
        }

        if (matchingReaders.size !== 0) {
            showMatchingEmails(matchingReaders);
        } else {
            isNewReaderPresent = true;
        }
    }
}

const showMatchingEmails = matchingReaders => {
    if (matchingReaders.size > 0) {
        let li;

        matchingReaders.forEach((name, email) => {
            li = `<li id='${email}' value='${name}'>${email}</li>`;
            matchList.innerHTML += li;
        })
    }
};

function itemSelected(e) {
    matchList.innerHTML = '';
    readerEmailInput.value = e.target.innerText;
    readerNameInput.value = e.target.getAttribute("value");
}

const loadAvailabilityDate = async () => {
    const res = await fetch(`${contextPath}/load_availability_date.do?bookId=${bookId}`);

    if (res.status === 200) {
        const dateStr = await res.json();

        bookAvailabilityDate = new Date(dateStr);
    }
};

function addBorrowRec() {
    if (isNewReaderPresent) {
        if(readerEmailPattern.test(readerEmailInput.value) && readerNamePattern.test(readerNameInput.value)){
            const newReader = {
                email: readerEmailInput.value,
                name: readerNameInput.value
            }

            readersToInsert.push(newReader);
        } else {
            alert('PLEASE INPUT VALID READER FIELDS!');
            return;
        }
    }

    if (remainingAmount <= 0) {
        alert('SORRY, THERE IS NO MORE COPIES OF THIS BOOK REMAIN!');
    } else if (!borrowRecords.some(e => e.reader.email === readerEmailInput.value)) {
        const table = document.getElementById('borrowRecTable').getElementsByTagName('tbody')[0]

        const rowCount = table.rows.length;
        const row = table.insertRow(rowCount);

        const emailCell = row.insertCell(0);
        emailCell.appendChild(document.createTextNode(readerEmailInput.value));

        const nameCell = row.insertCell(1);
        nameCell.appendChild(createNameDiv(readerNameInput.value, readerEmailInput.value));

        const date = new Date();

        const borrowDateCell = row.insertCell(2);
        borrowDateCell.appendChild(document.createTextNode(date.toLocaleDateString()));

        const dueDateCell = row.insertCell(3);
        date.setMonth(date.getMonth() + parseInt(timePeriodSelected.value));
        dueDateCell.appendChild(document.createTextNode(date.toLocaleDateString()));

        if (typeof bookAvailabilityDate === 'undefined' || date.getTime() < bookAvailabilityDate.getTime()) {
            bookAvailabilityDate = date;
        }

        const returnDateCell = row.insertCell(4);
        const returnDateDiv = document.createElement("div");
        returnDateDiv.id = readerEmailInput.value + 'RD';
        returnDateDiv.appendChild(document.createTextNode('-'));
        returnDateCell.appendChild(returnDateDiv);

        const borrowDate = toISOLocal(new Date());

        const borrowRec = {
            id: borrowRecords.length + 1,
            borrowDate: borrowDate,
            dueDate: toISOLocal(date),
            book: {
                id: bookId
            },
            reader: {
                email: readerEmailInput.value,
                name: readerNameInput.value
            }
        }

        borrowRecords.push(borrowRec);

        remainingAmount--;

        updateBookStatus();

        document.getElementById('closeAddModalButt').click();
    } else {
        alert(`READER ${readerNameInput.value} HAS ALREADY BORROWED THIS BOOK!`);
    }
}

function updateBookStatus() {
    if (remainingAmount > 0) {
        bookStatus.innerText = `Available (${remainingAmount} out of ${totalAmount})`;
    } else {
        const options = {year: 'numeric', month: 'long', day: 'numeric'};
        const formattedAvailDate = new Date(bookAvailabilityDate).toLocaleDateString("en", options);

        bookStatus.innerText = `Unavailable (expected to become available on ${formattedAvailDate})`;
    }
}

function totalAmountValidation(e) {
    const newTotalAmount = parseInt(e.target.value);

    if (Number.isInteger(newTotalAmount)) {
        const borrowedAmount = totalAmount - remainingAmount;

        if (newTotalAmount >= borrowedAmount) {
            const difference = totalAmount - newTotalAmount;

            remainingAmount = difference < 0 ? remainingAmount + Math.abs(difference) : remainingAmount - difference;
            totalAmount = newTotalAmount;

            updateBookStatus();
        } else {
            alert("TOTAL AMOUNT MUST BE >= NUMBER OF BORROWED COPIES!");
            inputBookTotalAmount.value = totalAmount;
        }
    } else {
        alert("TOTAL AMOUNT MUST BE A NUMBER!");
        inputBookTotalAmount.value = totalAmount;
    }
}

function toISOLocal(date) {
    const z = n => ('0' + n).slice(-2);
    let off = date.getTimezoneOffset();
    const sign = off > 0 ? '-' : '+';
    off = Math.abs(off);

    return date.getFullYear() + '-'
        + z(date.getMonth() + 1) + '-' +
        z(date.getDate()) + 'T' +
        z(date.getHours()) + ':' +
        z(date.getMinutes()) + ':' +
        z(date.getSeconds()) + '.' +
        sign + z(off / 60 | 0) + ':' + z(off % 60);
}

function removeBracketsFromStr(str, elementId) {
    document.getElementById(elementId).value = str.replace(/[\[\]]/g, '');
}

removeBracketsFromStr(document.getElementById('inputBookAuthors').value, 'inputBookAuthors');
removeBracketsFromStr(document.getElementById('inputBookGenres').value, 'inputBookGenres');