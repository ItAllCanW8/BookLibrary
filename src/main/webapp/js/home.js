function deleteCheckedBooks(e) {
    e.preventDefault();

    const checkedBooks = [];
    const checkboxes = document.querySelectorAll('input[type=checkbox]:checked');

    for (let i = 0; i < checkboxes.length; i++) {
        checkedBooks.push(checkboxes[i].id);
    }

    const bookIdsInput = document.getElementById('bookIdsInput');
    bookIdsInput.value = checkedBooks;

    if (checkedBooks.length !== 0) {
        document.getElementById('deleteBooksForm').submit();
    } else {
        alert("CHECK SOME BOOKS");
    }
}

const deleteBooksButt = document.getElementById('deleteBooksButt');
deleteBooksButt.addEventListener('click', deleteCheckedBooks);