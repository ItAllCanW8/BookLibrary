package by.itechart.BookLibrary.model.entity;

import java.io.Serializable;
import java.util.Set;

public class Book implements Serializable {
    private short id;
    private String cover;
    private String title;
    private Set<String> authors;
    private String publisher;
    private String publishDate;
    private Set<String> genres;
    private short pageCount;
    private String isbn;
    private String description;
    private short totalAmount;
    private short remainingAmount;
    private String status;

    public Book(){}

    public Book(short id){this.id = id;}

    public Book(String cover, String title, Set<String> authors, String publisher, String publishDate, Set<String> genres,
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

    public Book(short id, String title, Set<String> authors, String publishDate, short remainingAmount) {
        this(id);
        this.title = title;
        this.authors = authors;
        this.publishDate = publishDate;
        this.remainingAmount = remainingAmount;
    }

    public void setId(short id) { this.id = id; }

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

    public Set<String> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<String> authors) {
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

    public Set<String> getGenres() {
        return genres;
    }

    public void setGenres(Set<String> genres) {
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

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (pageCount != book.pageCount) return false;
        if (!title.equals(book.title)) return false;
        if (!authors.equals(book.authors)) return false;
        if (!publisher.equals(book.publisher)) return false;
        if (!publishDate.equals(book.publishDate)) return false;
        if (!genres.equals(book.genres)) return false;
        if (!isbn.equals(book.isbn)) return false;
        return description != null ? description.equals(book.description) : book.description == null;
    }

    @Override
    public int hashCode() {
        return isbn.hashCode();
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", cover='" + cover + '\'' +
                ", title='" + title + '\'' +
                ", authors=" + authors +
                ", publisher='" + publisher + '\'' +
                ", publishDate='" + publishDate + '\'' +
                ", genres=" + genres +
                ", pageCount=" + pageCount +
                ", isbn='" + isbn + '\'' +
                ", description='" + description + '\'' +
                ", totalAmount=" + totalAmount +
                ", remainingAmount=" + remainingAmount +
                ", status='" + status + '\'' +
                '}';
    }
}
