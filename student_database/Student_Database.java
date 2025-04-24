package student_database;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;

public class Student_Database extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Student_Database frame = new Student_Database();
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
    public Student_Database() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 921, 384);
        contentPane = new JPanel();
        contentPane.setForeground(new Color(255, 255, 255));
        contentPane.setBackground(new Color(86, 78, 69));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        // Title Label
        JLabel title = new JLabel("Πληροφορίες Συστήματος");
        title.setForeground(new Color(255, 255, 255));
        title.setBounds(364, 21, 238, 14);
        contentPane.add(title);
        
        // New Student Button
        JButton new_student = new JButton("Εγγραφή Μαθητή");
        new_student.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		New_Student newStudentFrame = new New_Student();
                newStudentFrame.setVisible(true);
        	}
        });
        new_student.setBounds(10, 294, 190, 23);
        contentPane.add(new_student);
        
        // Search Student Button
        JButton search_student = new JButton("Αναζήτηση Μαθητή");
        search_student.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Search_Student searchStudentFrame = new Search_Student();
                searchStudentFrame.setVisible(true);
            }
        });
        search_student.setBounds(228, 294, 190, 23);
        contentPane.add(search_student);
        
        // Edit Student Button
        JButton edit_student = new JButton("Επεξεργασία Μαθητή");
        edit_student.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Edit_Student editStudentFrame = new Edit_Student();
        		editStudentFrame.setVisible(true);
        	}
        });
        edit_student.setBounds(443, 294, 204, 23);
        contentPane.add(edit_student);
        
        // Text Panel 
        JTextPane textPane = new JTextPane();
        textPane.setForeground(new Color(255, 255, 255));
        textPane.setBackground(new Color(86, 78, 69));
        textPane.setText("Το Σύστημα Βάσης Δεδομένων Σχολείου είναι ένα λογισμικό που έχει σχεδιαστεί για να καταγράφει, " +
        		"να αναζητεί και να επεξεργάζεται πληροφορίες σχετικά με τους μαθητές του σχολείου. " +
        		"Το σύστημα επιτρέπει την εισαγωγή νέων μαθητών, την αποθήκευση και διαχείριση των προσωπικών τους στοιχείων, " +
        		"καθώς και τη δυνατότητα αναζήτησης και τροποποίησης αυτών των δεδομένων. " +
        		"Ειδικότερα, οι χρήστες μπορούν να προσθέτουν πληροφορίες όπως το όνομα, το επώνυμο, το ID και άλλες σχετικές πληροφορίες, " +
        		"να αναζητούν μαθητές με βάση διάφορα κριτήρια και να επεξεργάζονται τις καταχωρημένες πληροφορίες για τη σωστή διαχείριση " +
        		"και ενημέρωση των στοιχείων τους.");

        textPane.setBounds(199, 46, 500, 157);
        textPane.setEditable(false);  
        contentPane.add(textPane);
        
        // Delete Student Button
        JButton delete_student = new JButton("Διαγραφή Μαθητή");
        delete_student.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Delete_Student deleteStudentFrame = new Delete_Student();
                deleteStudentFrame.setVisible(true);
            }
        });
        delete_student.setBounds(671, 294, 190, 23);
        contentPane.add(delete_student);
    }
}
