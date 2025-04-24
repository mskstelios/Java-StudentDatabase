package student_database;

import java.awt.EventQueue;
import java.awt.Frame;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;

public class New_Student extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField id_text;
	private JTextField name_text;
	private JTextField last_text;
	private JButton submit_button;
	private JButton reset_button;
	private JButton main_menu;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					New_Student frame = new New_Student();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public New_Student() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(86, 78, 69));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel Title = new JLabel("ΕΓΓΡΑΦΗ ΜΑΘΗΤΩΝ");
		Title.setForeground(new Color(255, 255, 255));
		Title.setBounds(154, 11, 141, 14);
		contentPane.add(Title);
		
		id_text = new JTextField();
		id_text.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
				    int id = Integer.parseInt(id_text.getText());
				    System.out.println("Valid numeric ID: " + id);
				} catch (NumberFormatException ex) {
				    JOptionPane.showMessageDialog(null, "Το ID πρέπει να είναι αριθμός!", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		id_text.setBounds(78, 97, 172, 20);
		contentPane.add(id_text);
		id_text.setColumns(10);
		
		name_text = new JTextField();
		name_text.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        try {
		            String name = name_text.getText();  

		            if (name.matches(".*\\d.*")) {
		                throw new IllegalArgumentException("Το όνομα δεν πρέπει να περιέχει αριθμούς.");
		            }

		        } catch (IllegalArgumentException ex) {
		            JOptionPane.showMessageDialog(null, ex.getMessage(), "Σφάλμα", JOptionPane.ERROR_MESSAGE);
		        }
		    }
		});
		name_text.setColumns(10);
		name_text.setBounds(78, 128, 172, 20);
		contentPane.add(name_text);

		
		last_text = new JTextField();
		last_text.setColumns(10);
		last_text.setBounds(78, 156, 172, 20);
		contentPane.add(last_text);
		
		JLabel student_id = new JLabel("ID");
		student_id.setForeground(Color.WHITE);
		student_id.setBounds(40, 97, 43, 14);
		contentPane.add(student_id);
		
		JLabel student_name = new JLabel("ΟΝΟΜΑ");
		student_name.setForeground(Color.WHITE);
		student_name.setBounds(23, 128, 54, 14);
		contentPane.add(student_name);
		
		JLabel student_last = new JLabel("ΕΠΙΘΕΤΟ");
		student_last.setForeground(Color.WHITE);
		student_last.setBounds(16, 159, 61, 14);
		contentPane.add(student_last);
		
		submit_button = new JButton("ΥΠΟΒΟΛΗ");
		submit_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StringBuilder errors = new StringBuilder();

			    String id = id_text.getText().trim();
			    String name = name_text.getText().trim();
			    String last = last_text.getText().trim();


			    if (id.isEmpty()) {
			        errors.append("✘ Το πεδίο ID δεν πρέπει να είναι κενό.\n");
			    } else {
			        try {
			            Integer.parseInt(id);
			        } catch (NumberFormatException ex) {
			            errors.append("✘ Το ID πρέπει να είναι αριθμός.\n");
			        }
			    }

			    if (name.isEmpty()) {
			        errors.append("✘ Το πεδίο ΟΝΟΜΑ δεν πρέπει να είναι κενό.\n");
			    } else if (name.matches(".*\\d.*")) {
			        errors.append("✘ Το όνομα δεν πρέπει να περιέχει αριθμούς.\n");
			    }

			    if (last.isEmpty()) {
			        errors.append("✘ Το πεδίο ΕΠΙΘΕΤΟ δεν πρέπει να είναι κενό.\n");
			    } else if (last.matches(".*\\d.*")) {
			        errors.append("✘ Το επώνυμο δεν πρέπει να περιέχει αριθμούς.\n");
			    }

			    if (errors.length() > 0) {
			        JOptionPane.showMessageDialog(null, errors.toString(), "Σφάλματα Εισαγωγής", JOptionPane.ERROR_MESSAGE);
			    } else {
			    	try {
		                // Connect to MySQL Database
		                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/student_database", "me", "root");

		                // Create SQL Insert query
		                String query = "INSERT INTO students (id, student_name, student_last) VALUES (?, ?, ?)";

		                // Prepare statement
		                PreparedStatement pst = conn.prepareStatement(query);
		                pst.setInt(1, Integer.parseInt(id));  
		                pst.setString(2, name);               
		                pst.setString(3, last);               

		                // Execute query
		                int rowsInserted = pst.executeUpdate();

		                if (rowsInserted > 0) {
		                    JOptionPane.showMessageDialog(null, "✓ Επιτυχής καταχώρηση!", "Επιτυχία", JOptionPane.INFORMATION_MESSAGE);
		                } else {
		                    JOptionPane.showMessageDialog(null, "✘ Αποτυχία καταχώρησης!", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
		                }

		                // Close the connection
		                conn.close();
		        

		            } catch (SQLException ex) {
		                JOptionPane.showMessageDialog(null, "Σφάλμα στη σύνδεση με την βάση δεδομένων: " + ex.getMessage(), "Σφάλμα", JOptionPane.ERROR_MESSAGE);
			    }
		    }
		}});
		submit_button.setBounds(291, 227, 117, 23);
		contentPane.add(submit_button);
		
		reset_button = new JButton("ΕΠΑΝΑΦΟΡΑ");
		reset_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				id_text.setText("");
		        name_text.setText("");
		        last_text.setText("");
			}
		});
		reset_button.setBounds(154, 227, 117, 23);
		contentPane.add(reset_button);
		
		main_menu = new JButton("ΑΡΧΙΚΗ");
		main_menu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (Frame frame : JFrame.getFrames()) {
		            if (frame != New_Student.this) {
		                frame.dispose(); 
		            }
		        }
		        
		        // Open the Student_Database window
		        Student_Database studentDatabaseFrame = new Student_Database();
		        studentDatabaseFrame.setVisible(true);
		        New_Student.this.setVisible(false); 
        		
			}
		});
		main_menu.setBounds(16, 227, 89, 23);
		contentPane.add(main_menu);
	}
}
