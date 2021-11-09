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
    private short bookIdFk;
    private String readerEmail;
    private String readerName;

    public BorrowRecord(){}

    public BorrowRecord(short id, String borrowDate, String dueDate, String returnDate,
                        String status, String comment, String readerEmail, String readerName, short bookId) {
        this.id = id;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
        this.comment = comment;
        this.readerEmail = readerEmail;
        this.readerName = readerName;
        this.bookIdFk = bookId;
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

    public short getBookIdFk() {
        return bookIdFk;
    }

    public void setBookIdFk(short bookIdFk) {
        this.bookIdFk = bookIdFk;
    }

    public String getReaderEmail() {
        return readerEmail;
    }

    public void setReaderEmail(String readerEmail) {
        this.readerEmail = readerEmail;
    }

    public String getReaderName() {
        return readerName;
    }

    public void setReaderName(String readerName) {
        this.readerName = readerName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BorrowRecord that = (BorrowRecord) o;

        if (bookIdFk != that.bookIdFk) return false;
        if (!borrowDate.equals(that.borrowDate)) return false;
        if (!dueDate.equals(that.dueDate)) return false;
        if (!Objects.equals(returnDate, that.returnDate)) return false;
        if (!Objects.equals(status, that.status)) return false;
        if (!Objects.equals(comment, that.comment)) return false;
        if (!readerEmail.equals(that.readerEmail)) return false;
        return readerName.equals(that.readerName);
    }

    @Override
    public int hashCode() {
        int result = borrowDate.hashCode();
        result = 31 * result + dueDate.hashCode();
        result = 31 * result + (returnDate != null ? returnDate.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (int) bookIdFk;
        result = 31 * result + readerEmail.hashCode();
        result = 31 * result + readerName.hashCode();
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
                ", readerEmail='" + readerEmail + '\'' +
                ", readerName='" + readerName + '\'' +
                '}';
    }
}
