package org.librarymanagement;
import java.io.*;
import java.util.*;

public class Library {
    private final Map<String, Book> books;

    public void loadBooksFromFile(String fileName) throws Exception{
        try(BufferedReader br = new BufferedReader(new FileReader(fileName))){
            String line;
            String title = null, author = null, isbn = null, genre = null;
            int year = 0;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("Title:")) {
                    title = line.substring(7).trim();
                } else if (line.startsWith("Author:")) {
                    author = line.substring(8).trim();
                } else if (line.startsWith("ISBN:")) {
                    isbn = line.substring(6).trim();
                } else if (line.startsWith("Genre:")) {
                    genre = line.substring(7).trim();
                } else if (line.startsWith("Year:")) {
                    year = Integer.parseInt(line.substring(6).trim());
                    if (title != null && author != null && isbn != null && genre != null && year != 0) {
                        Book book = new Book(title, author, isbn, genre, year);
                        addBook(book);
                        addActionLogToFile("added book to the books: " + book.toString());
                        title = null;
                        author = null;
                        isbn = null;
                        genre = null;
                        year = 0;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
        }
    }

    public Library() {
        books = new HashMap<>();
    }

    public void addBook(Book book) throws Exception {
        books.put(book.getIsbn(), book);
        System.out.println("Book added/updated: " + book.getTitle());
        addActionLogToFile("Book added/updated: " + book.getTitle());
    }

    public void deleteBookByTitle(String title) throws Exception {
        books.values().removeIf(book -> book.getTitle().equalsIgnoreCase(title));
        System.out.println("Books with title '" + title + "' have been deleted.");
        addActionLogToFile("Books with title '" + title + "' have been deleted.");
    }

    public void deleteBookByIsbn(String isbn) throws Exception {
        books.values().removeIf(book -> book.getIsbn().equalsIgnoreCase(isbn));
        System.out.println("Book with ISBN '" + isbn + "' has been deleted.");
        addActionLogToFile("Book with ISBN '" + isbn + "' has been deleted.");
    }

    public List<Book> getAllBooks() throws Exception {
        return new ArrayList<>(books.values());
    }
    public List<Book> queryBooks(String type, String value) throws Exception {
        List<Book> result = new ArrayList<>();
        switch (type.toLowerCase()) {
            case "title":
                for (Book book : books.values()) {
                    if (book.getTitle().equalsIgnoreCase(value)) {
                        result.add(book);
                    }
                }
                break;
            case "author":
                System.out.println("querying for author: " + value);
                for (Book book : books.values()) {
                    if (book.getAuthor().equalsIgnoreCase(value)) {
                        result.add(book);
                    }
                }
                break;
            case "genre":
                for (Book book : books.values()) {
                    if (book.getGenre().equalsIgnoreCase(value)) {
                        result.add(book);
                    }
                }
                break;
            case "isbn":
                Book book = books.get(value);
                if (book != null) {
                    result.add(book);
                }
                break;
        }
        addActionLogToFile("Queried book for " + type + " with value: " + value);
        return result;
    }

    public void saveToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Book book : books.values()) {
                writer.println(book);
            }
            System.out.println("Library saved to file: " + filename);
        } catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }

    public void addActionLogToFile(String actionLog) throws Exception{
        try(PrintWriter writer = new PrintWriter(new FileWriter("ActionLog.txt", true))){
            writer.println("-----------------------");
            writer.println(actionLog);
        } catch (IOException e){
            System.out.println("Error loading action to the file");
        }
    }

    public void saveQueryResultsToFile(List<Book> queryResults, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename, true))) {
            for (Book book : queryResults) {
                writer.println(book);
            }
            writer.println("---------------------------------");
            System.out.println("Query results saved to file: " + filename);
        } catch (IOException e) {
            System.out.println("Error saving query results to file: " + e.getMessage());
        }
    }
}
