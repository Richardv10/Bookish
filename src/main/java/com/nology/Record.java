package com.nology;
import java.time.LocalDateTime;


public class Record {
    private int id;
    private String title;
    private LocalDateTime borrowedAt;

    public Record(int id, String bookTitle) {
        this.id = id;
        this.title = title;
        this.borrowedAt = LocalDateTime.now();
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getBorrowedAt() {
        return borrowedAt;
    }

    public void setBorrowedAt(LocalDateTime borrowedAt) {
        this.borrowedAt = borrowedAt;
    }


}
