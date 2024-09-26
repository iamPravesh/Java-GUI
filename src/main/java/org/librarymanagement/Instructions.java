package org.librarymanagement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Instructions extends Library {
    public static void processInstructions(String filename, Instructions instructions) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("add")) {
                    processAddCommand(line, instructions);
                } else if (line.startsWith("delete")) {
                    processDeleteCommand(line, instructions);
                } else if (line.startsWith("query")) {
                    processQueryCommand(line, instructions);
                } else if (line.startsWith("save")) {
                    instructions.saveToFile("output.txt");
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
        }
    }

    private static void processAddCommand(String line, Instructions instructions) throws Exception {
        String[] parts = line.substring(4).split(";");

        String title = parts[0].split("title ")[1].trim();
        String author = parts[1].split("author ")[1].trim();
        String isbn = parts[2].split("ISBN ")[1].trim();
        String genre = parts[3].split("genre ")[1].trim();
        int year = Integer.parseInt(parts[4].split("year ")[1].trim());

        Book book = new Book(title, author, isbn, genre, year);
        instructions.addBook(book);
    }

    private static void processDeleteCommand(String line, Instructions instructions) throws Exception {
        if (line.contains("title")) {
            String title = line.split("title ")[1].trim();
            instructions.deleteBookByTitle(title);
        } else if (line.contains("ISBN")) {
            String isbn = line.split("ISBN ")[1].trim();
            instructions.deleteBookByIsbn(isbn);
        }
    }

    private static void processQueryCommand(String line, Instructions instructions) throws Exception {
        String[] parts = line.split(" ");
        String queryType = parts[1].trim();
        String queryValue = line.substring(line.indexOf(queryType) + queryType.length()).trim();

        List<Book> queryResults = instructions.queryBooks(queryType, queryValue);
        instructions.saveQueryResultsToFile(queryResults, "report.txt");
    }

    public static void main(String[] args) throws Exception {
        Instructions inst = new Instructions();
        String instructionsFile = "instructions.txt";
        inst.loadBooksFromFile("books.txt");
        processInstructions(instructionsFile, inst);
    }
}
