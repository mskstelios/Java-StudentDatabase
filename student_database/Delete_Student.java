package student_database;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Delete_Student extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField searchLastNameField;
    private JLabel resultLabel;

    private final String DB_URL = "jdbc:mysql://localhost:3306/student_database";
    private final String DB_USER = "me";
    private final String DB_PASS = "root";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Delete_Student frame = new Delete_Student();
                frame.setVisible(true);
            } catch (Exception e) {
                System.out.println("Σφάλμα εκκίνησης: " + e.getMessage());
            }
        });
    }

    public Delete_Student() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 562, 300);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(86, 78, 69));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel title = new JLabel("ΔΙΑΓΡΑΦΗ ΜΑΘΗΤΗ");
        title.setBounds(210, 11, 150, 25);
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
        
        JButton main_menu = new JButton("ΑΡΧΙΚΗ");
        main_menu.setBounds(416, 225, 120, 25);
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

        resultLabel = new JLabel("");
        resultLabel.setBounds(30, 150, 400, 20);
        resultLabel.setForeground(Color.YELLOW);
        contentPane.add(resultLabel);

        JButton deleteButton = new JButton("ΔΙΑΓΡΑΦΗ");
        deleteButton.setBounds(203, 114, 120, 25);
        contentPane.add(deleteButton);

        // Αναζήτηση μαθητή
        searchButton.addActionListener((ActionEvent e) -> {
            String searchLastName = searchLastNameField.getText().trim();
            if (!searchLastName.isEmpty()) {
                if (isAlpha(searchLastName)) {
                    searchStudent(searchLastName);
                } else {
                    resultLabel.setText("Το επώνυμο δεν μπορεί να περιέχει αριθμούς.");
                }
            } else {
                resultLabel.setText("Εισάγετε επώνυμο για αναζήτηση.");
            }
        });

        // Διαγραφή μαθητή
        deleteButton.addActionListener((ActionEvent e) -> {
            String lastName = searchLastNameField.getText().trim();
            if (!lastName.isEmpty()) {
                if (isAlpha(lastName)) {
                    deleteStudent(lastName);
                } else {
                    resultLabel.setText("Το επώνυμο δεν μπορεί να περιέχει αριθμούς.");
                }
            } else {
                resultLabel.setText("Εισάγετε επώνυμο για διαγραφή.");
            }
        });
    }

    // Βοηθητική μέθοδος για να ελέγξουμε αν το επώνυμο περιέχει μόνο γράμματα
    private boolean isAlpha(String name) {
        return name.matches("[\\p{IsAlphabetic}]+");
    }


    // Αναζήτηση μαθητή βάσει επωνύμου
    private void searchStudent(String lastName) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String query = "SELECT id, student_name, student_last FROM students WHERE student_last = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, lastName);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                resultLabel.setText("Ο μαθητής βρέθηκε: " + rs.getString("student_name") + " " + rs.getString("student_last"));
            } else {
                resultLabel.setText("Δεν βρέθηκε μαθητής με αυτό το επώνυμο.");
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            resultLabel.setText("Σφάλμα βάσης: " + e.getMessage());
        }
    }

    // Διαγραφή μαθητή βάσει επωνύμου
    private void deleteStudent(String lastName) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String query = "DELETE FROM students WHERE student_last = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, lastName);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                resultLabel.setText("Ο μαθητής με το επώνυμο " + lastName + " διαγράφηκε επιτυχώς.");
            } else {
                resultLabel.setText("Δεν βρέθηκε μαθητής με αυτό το επώνυμο για διαγραφή.");
            }

            stmt.close();
        } catch (SQLException e) {
            resultLabel.setText("Σφάλμα SQL: " + e.getMessage());
        }
    }
}
