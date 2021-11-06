let borrowRecords = [];
window.onload = () => loadBorrowRecs();
let oldBorRecLength = 0;

let saveToDBButt = document.getElementById('saveToDBButt');
saveToDBButt.addEventListener('click', saveChangesToDB);

let saveBorrowRecButt = document.getElementById('saveBorrowRecButt');
saveBorrowRecButt.addEventListener('click', addBorrowRec);

const matchList = document.getElementById('matchList');

let readers;

let listItems = document.getElementById('matchList');
listItems.addEventListener('click', itemSelected)

const readerNameInput = document.getElementById('readerName');
const readerEmailInput = document.getElementById('readerEmailInput');
readerEmailInput.addEventListener('input', emailInput);

const addBorrowRecButt = document.getElementById('addBorrowRecButt');
addBorrowRecButt.addEventListener('click', () => loadReaders());

const timePeriodSelected = document.getElementById("timePeriodSelect");

async function saveChangesToDB(e) {
    e.preventDefault();

    let newBorRecLength = borrowRecords.length;

    if (newBorRecLength > oldBorRecLength) {

        let recsToInsert = [];
        for (let i = oldBorRecLength; i < newBorRecLength; i++) {
            recsToInsert.push(borrowRecords[i]);
        }

        console.log(recsToInsert);

        await fetch('add_borrow_records.do', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(Array.from(recsToInsert))
        })
            .then(res => res.ok)
            .then(res => {
                if (res) {
                    alert('CHANGES WERE SAVED!');
                    for (let i = oldBorRecLength; i < newBorRecLength; i++) {
                        borrowRecords.splice(i - 1, 1);
                    }
                }
            })
            .catch(error => {
                alert(error);
            });
    }

    let bookEditForm=document.getElementById('editBookForm');

    let input = document.createElement('input');
    console.log(remainingAmount);
    console.log(bookStatus.innerText);
    input.setAttribute('remainingAmount', remainingAmount);
    input.setAttribute('status', bookStatus.innerText);
    input.setAttribute('type', 'hidden');

    bookEditForm.appendChild(input);//append the input to the form

    document.getElementById('editBookForm').submit();
}

const loadReaders = async () => {
    if (typeof readers === 'undefined') {
        const res = await fetch('load_readers.do');
        readers = await res.json();
    }
};

const loadBorrowRecs = async () => {
    const res = await fetch('load_borrow_records.do?bookId=' + bookId);

    if (res.status === 200) {
        borrowRecords = await res.json();
        oldBorRecLength = borrowRecords.length;

        await fillBorrowRecTable();
    }
};

function fillBorrowRecTable() {
    let table = document.getElementById('borrowRecTable').getElementsByTagName('tbody')[0]

    let rowCount = table.rows.length;

    for (let i = 0; i < borrowRecords.length; i++) {
        let row = table.insertRow(rowCount);

        let emailCell = row.insertCell(0);
        let email = document.createTextNode(borrowRecords[i].readerEmail);
        emailCell.appendChild(email);

        let nameCell = row.insertCell(1);
        let name = document.createTextNode(borrowRecords[i].readerName);

        //TODO create div instead of TextNode (onclick)
        name.id = email + 'Id';
        name.onclick = () => {
            console.log('click');
            document.getElementById('borrowRecTable').focus();
        };

        nameCell.appendChild(name);

        // document.getElementById(email + 'Id').onclick = () => {
        //     console.log('click');
        //     document.getElementById('borrowRecTable').focus();
        // };

        let borrowDateCell = row.insertCell(2);
        borrowDateCell.appendChild(document.createTextNode(new Date(borrowRecords[i].borrowDate).toLocaleDateString()));

        let dueDateCell = row.insertCell(3);
        dueDateCell.appendChild(document.createTextNode(new Date(borrowRecords[i].dueDate).toLocaleDateString()));

        let returnDateCell = row.insertCell(4);
        let returnDateStr = borrowRecords[i].returnDate;

        if (typeof returnDateStr !== 'undefined') {
            returnDateCell.appendChild(document.createTextNode(new Date(returnDateStr).toLocaleString()));
        } else {
            returnDateCell.appendChild(document.createTextNode('-'));
        }
    }
}

function emailInput(e) {
    matchList.innerHTML = '';

    if (readerEmailInput.value.length > 3) {
        const emailInput = readerEmailInput.value;
        const matchingReaders = new Map();

        for (let i = 0; i < readers.length; i++) {
            const reader = readers[i];

            if (reader.email.includes(emailInput)) {
                matchingReaders.set(reader.email, reader.name);
            }
        }

        showMatchingEmails(matchingReaders);
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

function addBorrowRec(e) {
    if (remainingAmount <= 0) {
        alert('SORRY, THERE IS NO MORE COPIES OF THIS BOOK REMAIN!');
    } else if (!borrowRecords.some(e => e.readerEmail === readerEmailInput.value)) {
        let table = document.getElementById('borrowRecTable').getElementsByTagName('tbody')[0]

        let rowCount = table.rows.length;
        let row = table.insertRow(rowCount);

        let emailCell = row.insertCell(0);
        emailCell.appendChild(document.createTextNode(readerEmailInput.value));

        let nameCell = row.insertCell(1);
        nameCell.appendChild(document.createTextNode(readerNameInput.value));

        let date = new Date();

        let borrowDateCell = row.insertCell(2);
        borrowDateCell.appendChild(document.createTextNode(date.toLocaleDateString()));

        let dueDateCell = row.insertCell(3);
        date.setMonth(date.getMonth() + parseInt(timePeriodSelected.value));
        dueDateCell.appendChild(document.createTextNode(date.toLocaleDateString()));

        let returnDateCell = row.insertCell(4);
        returnDateCell.appendChild(document.createTextNode('-'));

        let borrowDate = toISOLocal(new Date()).slice(0, 19);

        let borrowRec = {
            id: borrowRecords.length + 1,
            borrowDate: borrowDate,
            dueDate: toISOLocal(date).slice(0, 19),
            bookIdFk: bookId,
            readerEmail: readerEmailInput.value,
            readerName: readerNameInput.value
        }

        borrowRecords.push(borrowRec);

        let statusStr = bookStatus.innerText;
        bookStatus.innerText = statusStr.replace(statusStr.match(new RegExp(remainingAmount)).toString(),
            (--remainingAmount).toString());

        console.log(remainingAmount);

        // TODO hide modal properly
        // document.getElementById('addBorrowRecModal').style.display = 'none';
        // document.getElementById('addBorrowRecModal');
        // document.getElementById('addBorrowRecModal').classList.remove('show');
        // document.getElementById('addBorrowRecModal').setAttribute('aria-hidden', 'true');
        //
        // const modalBackdrops = document.getElementsByClassName('modal-backdrop');
        // document.body.removeChild(modalBackdrops[0]);
    } else {
        alert(`READER ${readerNameInput.value} HAS ALREADY BORROWED THIS BOOK!`);
    }
}

function toISOLocal(date) {
    let z = n => ('0' + n).slice(-2);
    let off = date.getTimezoneOffset();
    let sign = off > 0 ? '-' : '+';
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