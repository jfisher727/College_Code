/*
 * Author: Jason Fisher, jfisher2009@my.fit.edu
 * Course: CSE 4051, Fall 2013
 * Project: Proj10, SSID
 */

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;




public class SSDI implements ActionListener {
    private static final int DEFAULT_WIDTH = 600;
    private static final int DEFAULT_HEIGHT = 300;
    private static final int QUERY_WIDTH = 450;
    private static final int QUERY_HEIGHT = 150;

    private static final int MAX_PAGES = 10;
    private static final int TABLE_COLUMNS = 7;
    private static final int TABLE_ROWS = 10;

    //results panel
    private JFrame resultsFrame;
    private JPanel mainPanel;
    private JPanel outputPanel;
    private JTable outputResults;
    private JButton next;
    private JButton previous;
    private int  pageNumber = 0;
    private boolean queryRun = false;
    private JPanel buttonsPanel;
    private JPanel headerPanel;

    //query panel
    private JFrame queryFrame;
    private JPanel queryPanel;
    private String empty = " ";
    private String social = "Social";
    private String lastName = "Last Name";
    private String middleName = "Middle Name";
    private String firstName = "First Name";
    private String suffix = "Suffix";
    private String deathDate = "Death Date";
    private String birthDate = "Birth Date";
    private final String greater = "Greater Than";
    private final String less = "Less Than";
    private final String equal = "Equal To";
    //final private String contains = "Contains";
    private JComboBox<String> fields;
    private JComboBox<String> comparatives;

    private JTextArea userQuery;
    private JButton query;

    //SQL
    static String dbURL =
            "jdbc:mysql://andrew.cs.fit.edu:3306/"
            + "cse4051_ssdi?user=cse4051&password=jdbc2013";
    static java.sql.Connection dbConnection = null;
    ResultSet results;

    public SSDI () {
        resultsGUI ();
        queryGUI ();
    }

    /*
     * Design implementation for the results GUI
     */
    public final void resultsGUI () {
        resultsFrame = new JFrame ();
        resultsFrame.setTitle("Jason Fisher -- Language Database");
        resultsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        resultsFrame.setLocationRelativeTo(null);
        resultsFrame.setResizable(true);
        //resultsFrame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        mainPanel = new JPanel ();
        outputPanel = new JPanel ();
        outputResults = new JTable();
        outputPanel.add(outputResults);

        buttonsPanel = new JPanel ();
        buttonsPanel.setLayout(new FlowLayout());
        next = new JButton ("Next");
        next.addActionListener(this);
        previous = new JButton ("Previous");
        previous.addActionListener(this);
        buttonsPanel.add(previous);
        buttonsPanel.add(next);

        final JLabel socLabel = new JLabel(social);
        final JLabel suffLabel = new JLabel (suffix);
        final JLabel firstLabel = new JLabel (firstName);
        final JLabel lastLabel = new JLabel (lastName);
        final JLabel middleLabel = new JLabel (middleName);
        final JLabel deathLabel = new JLabel (deathDate);
        final JLabel birthLabel = new JLabel (birthDate);

        headerPanel = new JPanel ();
        final GridLayout layout = new GridLayout(0, 7);
        headerPanel.setLayout(layout);

        headerPanel.add(new JPanel().add(socLabel));
        headerPanel.add(new JPanel().add(lastLabel));
        headerPanel.add(new JPanel().add(suffLabel));
        headerPanel.add(new JPanel().add(firstLabel));
        headerPanel.add(new JPanel().add(middleLabel));
        headerPanel.add(new JPanel().add(deathLabel));
        headerPanel.add(new JPanel().add(birthLabel));

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        resultsFrame.add(mainPanel);
        resultsFrame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /*
     * Design implementation for the query gui
     */
    public final void queryGUI () {
        queryFrame = new JFrame();
        queryFrame.setSize(QUERY_WIDTH, QUERY_HEIGHT);
        queryFrame.setTitle("Query Panel");
        queryFrame.setLocationRelativeTo(null);
        queryFrame.setResizable(false);
        queryFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        queryPanel = new JPanel ();
        //queryPanel.setLayout(new FlowLayout());
        queryPanel.setLayout(new BoxLayout(queryPanel, BoxLayout.Y_AXIS));
        userQuery = new JTextArea("Insert Query");
        userQuery.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT / 2);

        final JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new FlowLayout());
        fields = new JComboBox<String> ();
        fields.addItem(empty);
        fields.addItem(social);
        fields.addItem(lastName);
        fields.addItem(middleName);
        fields.addItem(firstName);
        fields.addItem(suffix);
        fields.addItem(deathDate);
        fields.addItem(birthDate);

        comparatives = new JComboBox<String> ();
        comparatives.addItem(empty);
        comparatives.addItem(greater);
        comparatives.addItem(less);
        comparatives.addItem(equal);
        //comparatives.addItem(contains);

        optionsPanel.add(fields);
        optionsPanel.add(comparatives);

        final JPanel button = new JPanel();
        query = new JButton ("Submit");
        query.addActionListener(this);
        button.add(query);

        queryPanel.add(userQuery);
        queryPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        //queryPanel.add(checkBoxes);
        queryPanel.add(optionsPanel);
        queryPanel.add(button);
        queryFrame.add(queryPanel);
    }

    public final void makeVisible () {
        //resultsFrame.pack();
        //resultsFrame.setVisible(true);
        queryFrame.pack();
        queryFrame.setVisible(true);
    }

    /*
     * retreives the input from the text field
     */
    public final String getInput () {
        return userQuery.getText();
    }

    @Override
    public final void actionPerformed (final ActionEvent e) {
        if (e.getSource() == query) {
            processQuery (getInput());
            displayResults(0);
        }
        else if (e.getSource() == previous) {
            if (pageNumber >= 1) {
                pageNumber--;
                displayResults(pageNumber);
            }
        }
        else if (e.getSource() == next) {
            if (queryRun) {
                if (pageNumber < (MAX_PAGES - 1)) {
                    pageNumber++;
                    displayResults(pageNumber);
                }
                else if (pageNumber == MAX_PAGES) {
                    System.out.println("All entries viewed. Create new query to view additional results");
                }
            }
        }
    }

    /*
     * takes the results of the query and displays them in the results GUI
     */
    public final void displayResults (final int pageToPrint) {
        final String [] columnNames = {social, lastName, suffix, firstName,
                middleName, deathDate, birthDate};
        final Object [][] tableData = new Object [TABLE_ROWS][TABLE_COLUMNS];
        int rowsPrinted = 0;
        int pagesSkipped = 0;
        int rowsSkipped = 0;
        try {
            results.last();
            if (results.getRow() < 1) {
                outputPanel.remove(outputResults);
                outputPanel.add(new JLabel("Query returned no results"));
            }
            final java.sql.ResultSetMetaData rsmd = results.getMetaData();
            results.first();
            final int columnCount = rsmd.getColumnCount();
            while (results.next()) {
                if (pagesSkipped < pageToPrint) {
                    if (rowsSkipped < TABLE_ROWS) {
                        for (int i = 1; i <= columnCount; i++) {
                            results.getObject(i);
                        }
                        rowsSkipped++;
                    }
                    else {
                        rowsSkipped = 0;
                        pagesSkipped++;
                    }
                }
                else if (rowsPrinted < MAX_PAGES) {
                    for (int i = 1; i <= columnCount; i++) {
                        tableData[rowsPrinted][i - 1]
                                = results.getObject(i);
                    }
                    rowsPrinted++;
                }
                else {
                    break;
                }
            }
        } catch (final SQLException e) {
            e.printStackTrace();
        }
        outputPanel.remove(outputResults);
        outputResults = new JTable (tableData, columnNames);
        outputPanel.add(outputResults);
        mainPanel.removeAll();
        mainPanel.add(headerPanel);
        mainPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        mainPanel.add(outputResults);
        mainPanel.add(buttonsPanel);
        resultsFrame.setSize(resultsFrame.getWidth(), resultsFrame.getHeight());
        resultsFrame.setVisible(true);
    }

    /*
     * Gets which comparison the user wants to process on the query
     */
    public final String getComparativeParam () {
        String comparative = "";
        if (!comparatives.getSelectedItem().equals(" ")) {
            switch (comparatives.getSelectedItem().toString()) {
            case greater:
                comparative = ">";
                break;
            case less:
                comparative = "<";
                break;
            case equal:
                comparative = "=";
                break;
            /*
            case contains:
                break;
                */
            default:
                break;
            }
        }
        return comparative;
    }

    /*
     * Takes the input from the text box and processes a query
     * based on the input as well as the drop down boxes selected
     */
    public final void processQuery (final String query) {
        queryRun = true;
        java.sql.Statement queryRequest = null;
        String queryStatement = "SELECT * FROM `index` ";
        if (!fields.getSelectedItem().equals(" ")) {
            String fieldSelected = (String) fields.getSelectedItem();
            if (fieldSelected.equals("Social")) {
                fieldSelected = "Number";
            }
            if (fieldSelected.contains(" ")) {
                final String [] names = fieldSelected.split(" ");
                fieldSelected = names [0];
                //for string comparisons we default to equality
                queryStatement += "WHERE "
                + fieldSelected + "='" + query + "'";
            }
            else {
                queryStatement += "WHERE " + fieldSelected;
                final String comparative = getComparativeParam();
                queryStatement += comparative + query;
            }
            queryStatement += " LIMIT 0, 100";
        }
        try {
            dbConnection = DriverManager.getConnection(dbURL);
            queryRequest = dbConnection.createStatement();
            results = queryRequest.executeQuery(queryStatement);
        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main (final String [] args) {
            final SSDI gui = new SSDI ();
            gui.makeVisible();
    }
}
