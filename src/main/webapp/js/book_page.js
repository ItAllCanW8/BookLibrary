let borrowRecords;
window.onload = () => loadBorrowRecs();

let saveBorrowRecButt = document.getElementById('saveBorrowRecButt');
saveBorrowRecButt.addEventListener('click', addBorrowRec);

const matchList = document.getElementById('matchList');

let readers;

let listItems = document.getElementById('matchList');
listItems.addEventListener('click', itemSelected)

const readerName = document.getElementById('readerName');
const readerEmailInput = document.getElementById('readerEmailInput');
readerEmailInput.addEventListener('input', emailInput);

const addBorrowRecButt = document.getElementById('addBorrowRecButt');
addBorrowRecButt.addEventListener('click', () => loadReaders());

const timePeriodSelected = document.getElementById("timePeriodSelect");

const loadReaders = async () => {
    if (typeof readers === 'undefined') {
        const res = await fetch('load_readers.do');
        readers = await res.json();
    }
};

const loadBorrowRecs = async () => {
    const urlParams = new URLSearchParams(window.location.search);
    const res = await fetch('load_borrow_records.do?bookId=' + urlParams.get('bookId'));
    borrowRecords = await res.json();
    console.log(borrowRecords);

    await fillBorrowRecTable();
};

function fillBorrowRecTable(){
    let table = document.getElementById('borrowRecTable').getElementsByTagName('tbody')[0]

    let rowCount = table.rows.length;

    for (let i =0; i < borrowRecords.length; i++){
        let row = table.insertRow(rowCount);

        let emailCell = row.insertCell(0);
        emailCell.appendChild(document.createTextNode(borrowRecords[i].readerEmail));

        let nameCell = row.insertCell(1);
        nameCell.appendChild(document.createTextNode(borrowRecords[i].readerName));

        let borrowDateCell = row.insertCell(2);
        borrowDateCell.appendChild(document.createTextNode(new Date(borrowRecords[i].borrowDate).toLocaleDateString()));

        let dueDateCell = row.insertCell(3);
        dueDateCell.appendChild(document.createTextNode(new Date(borrowRecords[i].dueDate).toLocaleDateString()));

        let returnDateCell = row.insertCell(4);
        let returnDateStr = borrowRecords[i].returnDate;

        if(typeof returnDateStr !== 'undefined'){
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
    readerName.value = e.target.getAttribute("value");
}

function addBorrowRec(e) {
    // if(borrowRecords.contai)

    let table = document.getElementById('borrowRecTable').getElementsByTagName('tbody')[0]

    let rowCount = table.rows.length;
    let row = table.insertRow(rowCount);

    let emailCell = row.insertCell(0);
    emailCell.appendChild(document.createTextNode(readerEmailInput.value));

    let nameCell = row.insertCell(1);
    nameCell.appendChild(document.createTextNode(readerName.value));

    let date = new Date();
    let borrowDateCell = row.insertCell(2);
    borrowDateCell.appendChild(document.createTextNode(date.toLocaleDateString()));

    let dueDateCell = row.insertCell(3);
    date.setMonth(date.getMonth() + parseInt(timePeriodSelected.value));
    dueDateCell.appendChild(document.createTextNode(date.toLocaleDateString()));

    let returnDateCell = row.insertCell(4);
    returnDateCell.appendChild(document.createTextNode('-'));
}

function removeBracketsFromStr(str, elementId) {
    document.getElementById(elementId).value = str.replace(/[\[\]]/g, '');
}

removeBracketsFromStr(document.getElementById('inputBookAuthors').value, 'inputBookAuthors');
removeBracketsFromStr(document.getElementById('inputBookGenres').value, 'inputBookGenres');