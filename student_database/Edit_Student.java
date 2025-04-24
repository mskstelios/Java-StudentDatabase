package student_database;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

public class Edit_Student extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField idField, nameField, lastNameField, searchLastNameField;
    private JLabel resultLabel;

    private final String DB_URL = "jdbc:mysql://localhost:3306/student_database";
    private final String DB_USER = "me";
    private final String DB_PASS = "root";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Edit_Student frame = new Edit_Student();
                frame.setVisible(true);
            } catch (Exception e) {
                System.out.println("Σφάλμα εκκίνησης: " + e.getMessage());
            }
        });
    }

    public Edit_Student() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 564, 380);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(86, 78, 69));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel title = new JLabel("ΕΠΕΞΕΡΓΑΣΙΑ ΜΑΘΗΤΗ");
        title.setBounds(170, 10, 200, 25);
        title.setForeground(Color.WHITE);
        contentPane.add(title);

        JLabel searchLabel = new JLabel("Αναζήτηση με επώνυμο:");
        searchLabel.setBounds(30, 50, 150, 20);
        searchLabel.setForeground(Color.WHITE);
        contentPane.add(searchLabel);

        searchLastNameField = new JTextField();
        searchLastNameField.setBounds(180, 50, 180, 25);
        contentPane.add(searchLastNameField);

        JButton searchButton = new JButton("ΑΝΑΖΗΤΗΣΗ");
        searchButton.setBounds(370, 50, 120, 25);
        contentPane.add(searchButton);

        JLabel idLabel = new JLabel("ID Μαθητή:");
        idLabel.setBounds(30, 100, 100, 20);
        idLabel.setForeground(Color.WHITE);
        contentPane.add(idLabel);

        idField = new JTextField();
        idField.setBounds(140, 100, 62, 25);
        idField.setEditable(false); // Μη επεξεργάσιμο
        contentPane.add(idField);

        JLabel nameLabel = new JLabel("Όνομα:");
        nameLabel.setBounds(30, 140, 100, 20);
        nameLabel.setForeground(Color.WHITE);
        contentPane.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(140, 140, 250, 20);
        contentPane.add(nameField);

        JLabel lastNameLabel = new JLabel("Επώνυμο:");
        lastNameLabel.setBounds(30, 180, 100, 20);
        lastNameLabel.setForeground(Color.WHITE);
        contentPane.add(lastNameLabel);

        lastNameField = new JTextField();
        lastNameField.setBounds(140, 180, 250, 20);
        contentPane.add(lastNameField);

        JButton saveButton = new JButton("ΑΠΟΘΗΚΕΥΣΗ");
        saveButton.setBounds(351, 230, 139, 25);
        contentPane.add(saveButton);

        JButton resetButton = new JButton("ΕΠΑΝΑΦΟΡΑ");
        resetButton.setBounds(209, 230, 132, 25);
        contentPane.add(resetButton);

        resultLabel = new JLabel("");
        resultLabel.setBounds(30, 280, 400, 20);
        resultLabel.setForeground(Color.YELLOW);
        contentPane.add(resultLabel);
        
        JButton main_menu = new JButton("ΑΡΧΙΚΗ");
        main_menu.setBounds(91, 231, 89, 23);
        contentPane.add(main_menu);
        main_menu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Κλείνει όλα τα ανοιχτά παράθυρα
				for (Frame frame : Frame.getFrames()) {
					frame.dispose();
				}

				// Άνοιγμα παραθύρου Student_Database
				Student_Database studentDatabaseFrame = new Student_Database();
				studentDatabaseFrame.setVisible(true);
			}
		});

        // Αναζήτηση με βάση επώνυμο
        searchButton.addActionListener((ActionEvent e) -> {
            String searchLastName = searchLastNameField.getText().trim();
            if (!searchLastName.isEmpty()) {
                searchStudent(searchLastName);
            } else {
                resultLabel.setText("Εισάγετε επώνυμο για αναζήτηση.");
                
                
            }
        });

        // Αποθήκευση αλλαγών
        saveButton.addActionListener((ActionEvent e) -> {
            updateStudent();
        });

        // Επαναφορά πεδίων
        resetButton.addActionListener((ActionEvent e) -> {
            clearFields();
        });
    }

    // Αναζήτηση μαθητή βάσει επωνύμου
    private void searchStudent(String lastName) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String query = "SELECT id, student_name, student_last FROM students WHERE student_last = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, lastName);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                idField.setText(String.valueOf(rs.getInt("id")));
                nameField.setText(rs.getString("student_name"));
                lastNameField.setText(rs.getString("student_last"));
                resultLabel.setText("Ο μαθητής βρέθηκε.");
            } else {
                resultLabel.setText("Δεν βρέθηκε μαθητής με αυτό το επώνυμο.");
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            resultLabel.setText("Σφάλμα βάσης: " + e.getMessage());
        }
    }

    // Ενημέρωση μαθητή
    private void updateStudent() {
        if (idField.getText().isEmpty()) {
            resultLabel.setText("Δεν έχει φορτωθεί μαθητής.");
            return;
        }

        int id = Integer.parseInt(idField.getText().trim());
        String name = nameField.getText().trim();
        String lastName = lastNameField.getText().trim();

        // Ελέγχουμε αν υπάρχουν αριθμοί στο όνομα ή επώνυμο
        if (containsNumber(name) || containsNumber(lastName)) {
            resultLabel.setText("Το όνομα ή το επώνυμο δεν μπορεί να περιέχει αριθμούς.");
            return;
        }

        if (name.isEmpty() || lastName.isEmpty()) {
            resultLabel.setText("Συμπληρώστε όλα τα πεδία.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {

            String selectQuery = "SELECT student_name, student_last FROM students WHERE id = ?";
            PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
            selectStmt.setInt(1, id);

            ResultSet rs = selectStmt.executeQuery();
            String currentName = "";
            String currentLastName = "";

            if (rs.next()) {
                currentName = rs.getString("student_name");
                currentLastName = rs.getString("student_last");
            }

            rs.close();
            selectStmt.close();

            if (name.equals(currentName) && lastName.equals(currentLastName)) {
                resultLabel.setText("Η νέα καταχώρηση είναι ακριβώς η ίδια με την προηγούμενη.");
                return;
            }

            String updateQuery = "UPDATE students SET student_name = ?, student_last = ? WHERE id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setString(1, name);
            updateStmt.setString(2, lastName);
            updateStmt.setInt(3, id);

            int rows = updateStmt.executeUpdate();
            if (rows > 0) {
                resultLabel.setText("Ο μαθητής ενημερώθηκε επιτυχώς.");
            } else {
                resultLabel.setText("Η ενημέρωση απέτυχε.");
            }

            updateStmt.close();
        } catch (SQLException e) {
            resultLabel.setText("Σφάλμα SQL: " + e.getMessage());
        }
    }

    // Βοηθητική μέθοδος για τον έλεγχο αν υπάρχει αριθμός σε μια συμβολοσειρά με κανονικές εκφράσεις
    private boolean containsNumber(String str) {
        return str.matches(".*\\d.*"); // Αν περιέχει τουλάχιστον έναν αριθμό
    }

    // Επαναφορά πεδίων
    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        lastNameField.setText("");
        searchLastNameField.setText("");
        resultLabel.setText("");
    }
}
