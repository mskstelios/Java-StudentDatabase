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

public class Search_Student extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField student_input;
	private JLabel results;

	private final String DB_URL = "jdbc:mysql://localhost:3306/student_database";
	private final String DB_USER = "me";
	private final String DB_PASS = "root";

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				Search_Student frame = new Search_Student();
				frame.setVisible(true);
			} catch (Exception e) {
				System.out.println("Σφάλμα εκκίνησης εφαρμογής: " + e.getMessage());
			}
		});
	}

	public Search_Student() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 494, 306);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(86, 78, 69));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel title = new JLabel("ΑΝΑΖΗΤΗΣΗ ΜΑΘΗΤΩΝ");
		title.setForeground(Color.WHITE);
		title.setBounds(160, 11, 200, 20);
		contentPane.add(title);

		JLabel student_last = new JLabel("ΕΠΙΘΕΤΟ ΜΑΘΗΤΗ:");
		student_last.setForeground(Color.WHITE);
		student_last.setBounds(10, 94, 132, 14);
		contentPane.add(student_last);

		student_input = new JTextField();
		student_input.setBounds(131, 94, 200, 20);
		contentPane.add(student_input);
		student_input.setColumns(10);

		JButton student_search = new JButton("ΑΝΑΖΗΤΗΣΗ");
		student_search.setBounds(324, 232, 139, 23);
		contentPane.add(student_search);

		JButton reset_btn = new JButton("ΕΠΑΝΑΦΟΡΑ");
		reset_btn.setBounds(183, 232, 131, 23);
		contentPane.add(reset_btn);

		JButton main_menu = new JButton("ΑΡΧΙΚΗ");
		main_menu.setBounds(16, 232, 89, 23);
		contentPane.add(main_menu);

		results = new JLabel("ΑΠΟΤΕΛΕΣΜΑ:");
		results.setForeground(Color.WHITE);
		results.setBounds(10, 150, 450, 20);
		contentPane.add(results);

		// Listener για αναζήτηση
		student_search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String lastName = student_input.getText().trim();

				if (lastName.isEmpty()) {
					results.setText("ΑΠΟΤΕΛΕΣΜΑ: Παρακαλώ εισάγετε επίθετο.");
				} else if (lastName.matches(".*\\d.*")) {
					results.setText("ΑΠΟΤΕΛΕΣΜΑ: Το επίθετο δεν πρέπει να περιέχει αριθμούς.");
				} else {
					searchStudentByLastName(lastName);
				}
			}
		});

		// Listener για επαναφορά
		reset_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				student_input.setText("");
				results.setText("ΑΠΟΤΕΛΕΣΜΑ:");
			}
		});

		// Listener για ΑΡΧΙΚΗ
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

	}

	//  Αναζήτηση μαθητή στη βάση
	private void searchStudentByLastName(String lastName) {
		try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
			String query = "SELECT id, student_name, student_last FROM students WHERE LOWER(student_last) = LOWER(?)";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, lastName);

			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				int id = rs.getInt("id");
				String fullName = rs.getString("student_name") + " " + rs.getString("student_last");
				results.setText("ΑΠΟΤΕΛΕΣΜΑ: ID " + id + " - " + fullName);
			} else {
				results.setText("ΑΠΟΤΕΛΕΣΜΑ: Δεν βρέθηκε μαθητής.");
			}

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			results.setText("Σφάλμα σύνδεσης με τη βάση.");
			System.out.println("SQL Error: " + e.getMessage());
		}
	}
}
