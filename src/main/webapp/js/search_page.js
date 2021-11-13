document.getElementById('searchLink').classList.add('active');

function search(e) {
    e.preventDefault();

    const bookTitle = document.getElementById('inputBookTitle').value;
    const bookGenres = document.getElementById('inputBookGenres').value;
    const bookAuthors = document.getElementById('inputBookAuthors').value;
    const bookDesc = document.getElementById('inputBookDescription').value;

    if (!(bookTitle === "" && bookGenres === "" && bookAuthors === "" && bookDesc === "")) {

        // document.getElementById('searchForm').submit();

        // const searchForm = document.getElementById('searchForm');

        const xhr = new XMLHttpRequest();
        xhr.open('GET',
            `search.do?bookTitle=${bookTitle}&bookGenres=${bookGenres}&bookAuthors=${bookAuthors}&bookDescription=${bookDesc}`);

        xhr.onload = function (event) {
            if (event.target.status === 200) {
                // // wait for user to press OK than reload page
                // if (!alert('ALL CHANGES WERE SAVED!')) {
                //     location.reload();
                // }
            }
        };

        // const formData = new FormData(searchForm);
        xhr.send();

        document.getElementsByClassName('heading_container')[0].style.display = 'block';
        document.getElementsByClassName('event_container')[0].style.display = 'block';
    } else {
        alert("AT LEAST ONE FIELD SHOULD BE FILLED");
    }
}

let searchButt = document.getElementById('searchButt');
searchButt.addEventListener('click', search);