let saveBorrowRecButt = document.getElementById('saveBorrowRecButt');
saveBorrowRecButt.addEventListener('click', addBorrowRec);

const matchList = document.getElementById('matchList');

let readers;

let listItems = document.getElementById('matchList');
listItems.addEventListener('click', itemSelected)

const readerName = document.getElementById('readerName');
const readerEmailInput = document.getElementById('readerEmailInput');
readerEmailInput.addEventListener('input', input);

const addBorrowRecButt = document.getElementById('addBorrowRecButt');
addBorrowRecButt.addEventListener('click', () => loadReaders());

const timePeriodSelected = document.getElementById("timePeriodSelect").value;

const loadReaders = async () => {
    if (typeof readers === 'undefined') {
        const res = await fetch('load_readers.do');
        readers = await res.json();
    }
};

// function loadReadersInfo(e) {
//     if (typeof readers === 'undefined') {
//         let xhr = new XMLHttpRequest();
//         xhr.open('GET', 'load_readers.do', true);
//
//         xhr.onload = function () {
//             if (this.status === 200) {
//                 readers = JSON.parse(this.responseText);
//                 // console.log(readers[1].name);
//                 console.log(readers);
//             }
//         }
//
//         xhr.send();
//     }
// }

function input(e) {
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

        output(matchingReaders);
    }
}

const output = matchingReaders => {
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

function removeBracketsFromStr(str, elementId) {
    document.getElementById(elementId).value = str.replace(/[\[\]]/g, '');
}

removeBracketsFromStr(document.getElementById('inputBookAuthors').value, 'inputBookAuthors');
removeBracketsFromStr(document.getElementById('inputBookGenres').value, 'inputBookGenres');