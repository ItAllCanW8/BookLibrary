let authors = document.getElementById('bookAuthors');
var genres = document.getElementById('bookGenres');

authors.addEventListener('click', remove);
genres.addEventListener('click', remove);

let addAuthorForm = document.getElementById('addAuthorForm');

console.log(authors)
console.log(addAuthorForm)

addAuthorForm.addEventListener('submit', add);

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
    e.preventDefault();

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

// document.getElementById('butt').addEventListener('click', loadText);
//
// function loadText() {
//     var xhr = new XMLHttpRequest();
//
//     xhr.open('GET', 'book_page.do?bookId=${book.id}', true);
//
//     // xhr.onprogress = function (){
//     //     console.log('READYSTATE: ', xhr.readyState);
//     // }
//
//     xhr.onload = function () {
//         console.log('READYSTATE: ', xhr.readyState);
//         if (this.status === 200) {
//             // document.getElementById('text').innerHTML = this.responseText;
//             console.log(this.responseText);
//             var book = JSON.parse(${book});
//             var temp = ${book};
//             var book = JSON.parse(JSON.stringify(temp));
//             var book = ${json};
//             var bookAuthors = book.authors;
//             console.log(bookAuthors);
//         } else {
//             document.getElementById('text').innerHTML = 'FOK';
//         }
//     }
//
//     xhr.onprogress = function () {
//         console.log('READYSTATE: ', xhr.readyState);
//     }
//
//     xhr.onerror = function () {
//         console.log('Req error:(');
//     }
//
//     // xhr.onreadystatechange = function (){
//     //
//     //     if(this.readyState === 4 && this.status === 200){
//     //         console.log(this.responseText);
//     //     }
//     // }
//
//     xhr.send();
// }