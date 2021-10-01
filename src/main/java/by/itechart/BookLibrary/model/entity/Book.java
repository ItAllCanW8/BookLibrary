package by.itechart.BookLibrary.model.entity;

import java.io.Serializable;

public class Book implements Serializable {
    private short id;
    private String cover;
    private String title;
    private String authors;
    private String publisher;
    private String publishDate;
    private String genres;
    private short pageCount;
    private String isbn;
    private String description;
    private short totalAmount;
    private short remainingAmount;
    private String status;

    private Book(){}

    public Book(short id){this.id = id;}

    public Book(String cover, String title, String authors, String publisher, String publishDate, String genres,
                short pageCount, String isbn, String description, short totalAmount, short remainingAmount,
                String status) {
        this.cover = cover;
        this.title = title;
        this.authors = authors;
        this.publisher = publisher;
        this.publishDate = publishDate;
        this.genres = genres;
        this.pageCount = pageCount;
        this.isbn = isbn;
        this.description = description;
        this.totalAmount = totalAmount;
        this.remainingAmount = remainingAmount;
        this.status = status;
    }

    public Book(short id, String title, String authors, String publishDate, short remainingAmount) {
        this(id);
        this.title = title;
        this.authors = authors;
        this.publishDate = publishDate;
        this.remainingAmount = remainingAmount;
    }

    public short getId() {
        return id;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public short getPageCount() {
        return pageCount;
    }

    public void setPageCount(short pageCount) {
        this.pageCount = pageCount;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public short getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(short totalAmount) {
        this.totalAmount = totalAmount;
    }

    public short getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(short remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (id != book.id) return false;
        if (pageCount != book.pageCount) return false;
        if (totalAmount != book.totalAmount) return false;
        if (remainingAmount != book.remainingAmount) return false;
        if (!title.equals(book.title)) return false;
        if (!authors.equals(book.authors)) return false;
        if (!publisher.equals(book.publisher)) return false;
        if (!publishDate.equals(book.publishDate)) return false;
        if (!genres.equals(book.genres)) return false;
        if (!isbn.equals(book.isbn)) return false;
        return status.equals(book.status);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + title.hashCode();
        result = 31 * result + authors.hashCode();
        result = 31 * result + publisher.hashCode();
        result = 31 * result + publishDate.hashCode();
        result = 31 * result + genres.hashCode();
        result = 31 * result + (int) pageCount;
        result = 31 * result + isbn.hashCode();
        result = 31 * result + (int) totalAmount;
        result = 31 * result + (int) remainingAmount;
        result = 31 * result + status.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", cover='" + cover + '\'' +
                ", title='" + title + '\'' +
                ", authors='" + authors + '\'' +
                ", publisher='" + publisher + '\'' +
                ", publishDate='" + publishDate + '\'' +
                ", genres='" + genres + '\'' +
                ", pageCount=" + pageCount +
                ", isbn='" + isbn + '\'' +
                ", description='" + description + '\'' +
                ", totalAmount=" + totalAmount +
                ", remainingAmount=" + remainingAmount +
                ", status='" + status + '\'' +
                '}';
    }
}
