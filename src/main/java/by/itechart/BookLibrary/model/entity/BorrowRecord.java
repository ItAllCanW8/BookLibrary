package by.itechart.BookLibrary.model.entity;

import java.io.Serializable;
import java.util.Objects;

public class BorrowRecord implements Serializable {
    private short id;
    private String borrowDate;
    private String dueDate;
    private String returnDate;
    private String status;
    private String comment;
    private Book book;
    private Reader reader;

    public BorrowRecord(){}

    public BorrowRecord(short id, String borrowDate, String dueDate, String returnDate, String status, String comment,
                        Book book,  Reader reader) {
        this.id = id;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
        this.comment = comment;
        this.book = book;
        this.reader = reader;
    }

    public BorrowRecord(String daysRemain, Book book, Reader reader){
        this.dueDate = daysRemain;
        this.book = book;
        this.reader = reader;
    }

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BorrowRecord that = (BorrowRecord) o;

        if (!Objects.equals(borrowDate, that.borrowDate)) return false;
        if (!dueDate.equals(that.dueDate)) return false;
        if (!Objects.equals(returnDate, that.returnDate)) return false;
        if (!Objects.equals(status, that.status)) return false;
        if (!Objects.equals(comment, that.comment)) return false;
        if (!book.equals(that.book)) return false;
        return reader.equals(that.reader);
    }

    @Override
    public int hashCode() {
        int result = borrowDate != null ? borrowDate.hashCode() : 0;
        result = 31 * result + dueDate.hashCode();
        result = 31 * result + (returnDate != null ? returnDate.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + book.hashCode();
        result = 31 * result + reader.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "BorrowRecord{" +
                "id=" + id +
                ", borrowDate='" + borrowDate + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", returnDate='" + returnDate + '\'' +
                ", status='" + status + '\'' +
                ", comment='" + comment + '\'' +
                ", book=" + book +
                ", reader=" + reader +
                '}';
    }
}
