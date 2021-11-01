const matchList = document.getElementById('matchList');

let readers;
const addBorrowRecButt = document.getElementById('addBorrowRecButt');

const timePeriodSelected = document.getElementById("timePeriodSelect").value;
const readerEmailInput = document.getElementById('readerEmailInput');

addBorrowRecButt.addEventListener('click', () => loadReaders());
readerEmailInput.addEventListener('input', input);

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
    if (readerEmailInput.value.length > 3) {
        const emailInput = readerEmailInput.value;
        const matchingReaders = new Map();

        for (let i = 0; i < readers.length; i++) {
            const reader = readers[i];

            if (reader.email.includes(emailInput)) {
                matchingReaders.set(reader.email, reader.name);
            }
        }

        matchList.innerHTML = '';
        output(matchingReaders);
    }
}

const output = matchingReaders => {
    if (matchingReaders.size > 0) {
        let li;

        matchingReaders.forEach((name, email) => {
            li = `<li id='${email}'> ${email} </li>`;
            matchList.innerHTML += li;
        })
    }
};

function removeBracketsFromStr(str, elementId) {
    document.getElementById(elementId).value = str.replace(/[\[\]]/g, '');
}

removeBracketsFromStr(document.getElementById('inputBookAuthors').value, 'inputBookAuthors');
removeBracketsFromStr(document.getElementById('inputBookGenres').value, 'inputBookGenres');