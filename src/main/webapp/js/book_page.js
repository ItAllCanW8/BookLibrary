let borrowRecords;
window.onload = () => loadBorrowRecs();
let oldBorRecLength;

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

    console.log(1);

    let newBorRecLength = borrowRecords.length;

    console.log(2);

    if (newBorRecLength > oldBorRecLength) {
        console.log(3);

        let recsToInsert = [];
        for (let i = oldBorRecLength; i < newBorRecLength; i++) {
            console.log(borrowRecords[i]);
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
            // .then(res => res.json())
            // .then(res => {
            //     // enter you logic when the fetch is successful
            //     console.log(res)
            // })
            // .catch(error => {
            //     // enter your logic for when there is an error (ex. error toast)
            //     console.log(error)
            // })

        // try {
        //     const config = {
        //         method: 'POST',
        //         headers: {
        //             'Accept': 'application/json',
        //             'Content-Type': 'application/json',
        //         },
        //         body: JSON.stringify(recsToInsert)
        //     }
        //     const response = await fetch("add_borrow_records.do", config)
        //     //const json = await response.json()
        //     if (response.ok) {
        //         //return json
        //         return response
        //     } else {
        //         //
        //     }
        // } catch (error) {
        //     //
        // }
    }
}

const loadReaders = async () => {
    if (typeof readers === 'undefined') {
        const res = await fetch('load_readers.do');
        readers = await res.json();
    }
};

const loadBorrowRecs = async () => {
    const res = await fetch('load_borrow_records.do?bookId=' + bookId);
    borrowRecords = await res.json();

    oldBorRecLength = borrowRecords.length;

    await fillBorrowRecTable();
};

function fillBorrowRecTable() {
    let table = document.getElementById('borrowRecTable').getElementsByTagName('tbody')[0]

    let rowCount = table.rows.length;

    for (let i = 0; i < borrowRecords.length; i++) {
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

        console.log(date);
        console.log(date.toLocaleString().replace(',', ''));

        let borrowDateCell = row.insertCell(2);
        borrowDateCell.appendChild(document.createTextNode(date.toLocaleDateString()));

        let dueDateCell = row.insertCell(3);
        date.setMonth(date.getMonth() + parseInt(timePeriodSelected.value));
        dueDateCell.appendChild(document.createTextNode(date.toLocaleDateString()));

        let returnDateCell = row.insertCell(4);
        returnDateCell.appendChild(document.createTextNode('-'));

        let borrowRec = {
            id: borrowRecords.length + 1,
            borrowDate: new Date().toLocaleString().replace(',', ''),
            dueDate: date.toLocaleString().replace(',', ''),
            bookIdFk: bookId,
            readerEmail: readerEmailInput.value,
            readerName: readerNameInput.value
        }

        borrowRecords.push(borrowRec);

        let statusStr = bookStatus.innerText;
        bookStatus.innerText = statusStr.replace(statusStr.match(new RegExp(remainingAmount)).toString(),
            (--remainingAmount).toString());

        console.log(remainingAmount);

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

function removeBracketsFromStr(str, elementId) {
    document.getElementById(elementId).value = str.replace(/[\[\]]/g, '');
}

removeBracketsFromStr(document.getElementById('inputBookAuthors').value, 'inputBookAuthors');
removeBracketsFromStr(document.getElementById('inputBookGenres').value, 'inputBookGenres');