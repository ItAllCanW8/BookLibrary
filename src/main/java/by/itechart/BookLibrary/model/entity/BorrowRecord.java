package by.itechart.BookLibrary.model.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class BorrowRecord implements Serializable {
    private short id;
    private LocalDateTime borrowDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnDate;
    private String status;
    private String comment;
    private short bookIdFk;
    private short readerIdFk;

    public BorrowRecord(){}

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public LocalDateTime getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDateTime borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
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

    public short getBookIdFk() {
        return bookIdFk;
    }

    public void setBookIdFk(short bookIdFk) {
        this.bookIdFk = bookIdFk;
    }

    public short getReaderIdFk() {
        return readerIdFk;
    }

    public void setReaderIdFk(short readerIdFk) {
        this.readerIdFk = readerIdFk;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BorrowRecord that = (BorrowRecord) o;

        if (bookIdFk != that.bookIdFk) return false;
        if (readerIdFk != that.readerIdFk) return false;
        if (!borrowDate.equals(that.borrowDate)) return false;
        if (!dueDate.equals(that.dueDate)) return false;
        if (!Objects.equals(returnDate, that.returnDate)) return false;
        if (!Objects.equals(status, that.status)) return false;
        return Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        int result = borrowDate.hashCode();
        result = 31 * result + dueDate.hashCode();
        result = 31 * result + (returnDate != null ? returnDate.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (int) bookIdFk;
        result = 31 * result + (int) readerIdFk;
        return result;
    }

    @Override
    public String toString() {
        return "BorrowRecord{" +
                "id=" + id +
                ", borrowDate=" + borrowDate +
                ", dueDate=" + dueDate +
                ", returnDate=" + returnDate +
                ", status='" + status + '\'' +
                ", comment='" + comment + '\'' +
                ", bookIdFk=" + bookIdFk +
                ", readerIdFk=" + readerIdFk +
                '}';
    }
}
