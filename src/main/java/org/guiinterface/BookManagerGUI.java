package org.guiinterface;

import org.librarymanagement.Book;
import org.librarymanagement.Library;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

public class BookManagerGUI {
    private JTextArea bookListArea;
    private JTextArea logArea;
    private JTextField queryField, deleteField;
    private JComboBox<String> queryTypeComboBox;
    Library library = new Library();

    public BookManagerGUI(){
        createUI();
    }

    private void createUI(){
        JFrame frame = new JFrame("Book Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        // Main panel for the controls
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(240, 240, 240)); // Light background color
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding around components
        gbc.fill = GridBagConstraints.HORIZONTAL; // Allow components to expand horizontally

        // Button to load a file
        JButton addBooksFile = new JButton("Select Books File");
        addBooksFile.addActionListener(new BooksFileAction());
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(addBooksFile, gbc);

        // Button to add a new book
        JButton addBook = new JButton("Add Book");
        addBook.addActionListener(new AddBookUIAction());
        gbc.gridx = 1;
        panel.add(addBook, gbc);

        // Query section: separate panel for search-related components
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new GridBagLayout()); // Changed to GridBagLayout
        searchPanel.setBackground(new Color(240, 240, 240)); // Consistent background
        GridBagConstraints searchGbc = new GridBagConstraints();
        searchGbc.insets = new Insets(5, 5, 5, 5); // Padding around components
        searchGbc.fill = GridBagConstraints.HORIZONTAL;

        searchGbc.gridx = 0;
        searchGbc.gridy = 0;
        searchPanel.add(new JLabel("Query Type:"), searchGbc);

        queryTypeComboBox = new JComboBox<>(new String[]{"Title", "Author", "Genre", "ISBN"});
        searchGbc.gridx = 1;
        searchGbc.gridy = 0;
        searchGbc.weightx = 1.0; // Allow combo box to expand
        searchPanel.add(queryTypeComboBox, searchGbc);

        searchGbc.gridx = 0;
        searchGbc.gridy = 1;
        searchPanel.add(new JLabel("Enter Query:"), searchGbc);

        queryField = new JTextField(15);
        searchGbc.gridx = 1;
        searchGbc.gridy = 1;
        searchGbc.weightx = 1.0; // Allow text field to expand
        searchPanel.add(queryField, searchGbc);

        // Search button
        JButton queryButton = new JButton("Search Book");
        queryButton.addActionListener(new QueryBookAction());
        searchGbc.gridx = 0;
        searchGbc.gridy = 2;
        searchGbc.gridwidth = 2; // Span across two columns
        searchPanel.add(queryButton, searchGbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2; // Span across two columns
        panel.add(searchPanel, gbc);

        // Section for deleting books by ISBN
        JPanel deletePanel = new JPanel(new GridBagLayout()); // Changed to GridBagLayout
        deletePanel.setBackground(new Color(240, 240, 240));

        GridBagConstraints deleteGbc = new GridBagConstraints();
        deleteGbc.insets = new Insets(5, 5, 5, 5); // Padding around components
        deleteGbc.fill = GridBagConstraints.HORIZONTAL;

        deleteGbc.gridx = 0;
        deleteGbc.gridy = 0;
        deletePanel.add(new JLabel("Delete by ISBN:"), deleteGbc);

        deleteField = new JTextField(15);
        deleteGbc.gridx = 1;
        deleteGbc.gridy = 0;
        deleteGbc.weightx = 1.0; // Allow text field to expand
        deletePanel.add(deleteField, deleteGbc);

        JButton deleteButton = new JButton("Delete Book");
        deleteButton.addActionListener(new DeleteBookAction());
        deleteGbc.gridx = 0;
        deleteGbc.gridy = 1;
        deleteGbc.gridwidth = 2; // Span across two columns
        deletePanel.add(deleteButton, deleteGbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // Reset to span across two columns
        panel.add(deletePanel, gbc);

        // Button to save all records to a file
        JButton saveRecordsButton = new JButton("Save Records");
        saveRecordsButton.addActionListener(new SaveRecordsAction());
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1; // This button only occupies one column
        panel.add(saveRecordsButton, gbc);

//        Button to show all the books present
        JButton showAllBooks = new JButton("Show All Books");
        showAllBooks.addActionListener(new ShowRecordsAction());
        gbc.gridx = 1; // Move this to the next column
        panel.add(showAllBooks, gbc);

        // Add main control panel to the frame (top part)
        frame.getContentPane().add(BorderLayout.NORTH, panel);

        // Text area to display query results (book list)
        bookListArea = new JTextArea();
        bookListArea.setEditable(false);  // Make it non-editable
        JScrollPane bookScrollPane = new JScrollPane(bookListArea);  // Scrollable text area

        // Log area to display logs
        logArea = new JTextArea();
        logArea.setEditable(false);  // Logs should also be non-editable
        JScrollPane logScrollPane = new JScrollPane(logArea);  // Scrollable log area

        // JSplitPane to split the book list and log areas
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, bookScrollPane, logScrollPane);
        splitPane.setDividerLocation(1200);  // Set initial divider location (adjust based on preference)

        // Add the split pane to the center part of the frame
        frame.getContentPane().add(BorderLayout.CENTER, splitPane);

        frame.setVisible(true);
    }

    private class BooksFileAction implements ActionListener {
        @Override
        public  void actionPerformed(ActionEvent e){
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    library.loadBooksFromFile(selectedFile.getName());
                    logArea.append("Books file loaded from: " + selectedFile.getName() + "\n");  // Log file load action
                    JOptionPane.showMessageDialog(null, "Books file loaded");
                } catch (Exception ex) {
                    logArea.append("Failed to load books file.\n");  // Log error
                    JOptionPane.showMessageDialog(null, "Books file could not be loaded");
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    private class AddBookUIAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            JDialog addBookDialog = new JDialog();
            addBookDialog.setTitle("Add New Book");
            addBookDialog.setSize(400, 300);
            addBookDialog.setLayout(new GridLayout(7, 2, 5, 5));

            JTextField titleField = new JTextField();
            JTextField authorField = new JTextField();
            JTextField isbnField = new JTextField();
            JTextField genreField = new JTextField();
            JTextField yearField = new JTextField();

            addBookDialog.add(new JLabel("Title:"));
            addBookDialog.add(titleField);
            addBookDialog.add(new JLabel("Author:"));
            addBookDialog.add(authorField);
            addBookDialog.add(new JLabel("ISBN:"));
            addBookDialog.add(isbnField);
            addBookDialog.add(new JLabel("Genre:"));
            addBookDialog.add(genreField);
            addBookDialog.add(new JLabel("Year:"));
            addBookDialog.add(yearField);

            JButton addBookButton = new JButton("Add Book");
            JButton cancelButton = new JButton("Cancel");

            addBookDialog.add(addBookButton);
            addBookDialog.add(cancelButton);

            addBookButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String title = titleField.getText();
                    String author = authorField.getText();
                    String isbn = isbnField.getText();
                    String genre = genreField.getText();
                    int year = Integer.parseInt(yearField.getText());

                    if (!title.isEmpty() && !author.isEmpty() && !Integer.toString(year).isEmpty() && !isbn.isEmpty() && !genre.isEmpty()) {
                        Book book = new Book(title, author, isbn, genre, year);
                        try {
                            library.addBook(book);
                            logArea.append("Book added: " + title + " by " + author + "\n");
                            JOptionPane.showMessageDialog(addBookDialog, "Book added successfully!");
                        } catch (Exception ex) {
                            logArea.append("Failed to add book: " + title + "\n");
                            JOptionPane.showMessageDialog(addBookDialog, "Could not add book. Please try again.");
                            throw new RuntimeException(ex);
                        }
                        addBookDialog.dispose();  // Close the dialog
                    } else {
                        JOptionPane.showMessageDialog(addBookDialog, "Please fill in all fields.");
                    }
                }
            });

            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    addBookDialog.dispose();  // Close the dialog without adding a book
                }
            });

            addBookDialog.setVisible(true);
        }
    }

    private class QueryBookAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String queryType = (String) queryTypeComboBox.getSelectedItem();
            String queryValue = queryField.getText();

            if (queryValue.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a value for the query.");
                return;
            }

            try {
                assert queryType != null;
                List<Book> books = library.queryBooks(queryType.toLowerCase(), queryValue);
                if (books.isEmpty()) {
                    bookListArea.setText("No books found for the query: " + queryValue);
                    logArea.append("No books found for query: " + queryValue + "\n");
                } else {
                    StringBuilder result = new StringBuilder();
                    for (Book book : books) {
                        result.append(book.toString()).append("\n");
                    }
                    bookListArea.setText(result.toString());
                    logArea.append("Query result displayed for: " + queryValue + "\n");
                }
            } catch (Exception ex) {
                logArea.append("Error querying books.\n");
                JOptionPane.showMessageDialog(null, "Error querying books.");
                ex.printStackTrace();
            }
        }
    }

    // Action listener for deleting books
    private class DeleteBookAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String isbn = deleteField.getText();

            if (isbn.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter the ISBN of the book to delete.");
                return;
            }

            try {
                library.deleteBookByIsbn(isbn);
                logArea.append("Book with ISBN: " + isbn + " deleted successfully.\n");
                JOptionPane.showMessageDialog(null, "Book deleted successfully.");
                deleteField.setText("");  // Clear the delete field after action
            } catch (Exception ex) {
                logArea.append("Error deleting book with ISBN: " + isbn + "\n");
                JOptionPane.showMessageDialog(null, "Error deleting book.");
                ex.printStackTrace();
            }
        }
    }

    // Action listener for saving records
    private class SaveRecordsAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    library.saveToFile(selectedFile.getAbsolutePath());  // Assuming this method exists
                    logArea.append("Books saved to file: " + selectedFile.getAbsolutePath() + "\n");  // Log save action
                    JOptionPane.showMessageDialog(null, "Records saved successfully.");
                } catch (Exception ex) {
                    logArea.append("Error saving records.\n");  // Log error
                    JOptionPane.showMessageDialog(null, "Error saving records.");
                    ex.printStackTrace();
                }
            }
        }
    }

    private class ShowRecordsAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            StringBuilder result = new StringBuilder();
            try {
                for (Book book: library.getAllBooks()){
                    result.append(book.toString()).append("\n");
                }
                logArea.append("Showing all books. \n");
                bookListArea.setText(result.toString());
            } catch (Exception ex) {
                logArea.append("Error showing all the books. \n");
                throw new RuntimeException(ex);
            }
        }
    }

    public static void main(String[] args) {
//        new BookManagerGUI();
        SwingUtilities.invokeLater(BookManagerGUI::new);
    }
}
