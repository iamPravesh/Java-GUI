package org.librarymanagement;

import java.io.Serializable;

public class Book implements Serializable {
    private String title;
    private String author;
    private String isbn;
    private String genre;
    private int year;

    public Book(String title, String author, String isbn, String genre, int year) {
        this.title = title;
        this.author =  author;
        this.isbn = isbn;
        this.genre = genre;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString(){
        return "Title: " + title + "\nAuthor: " + author + "\nISBN: " + isbn + "\nGenre: " + genre + "\nYear of publication: " + year + "\n";
    }
}
