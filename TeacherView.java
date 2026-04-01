
import java.awt.*;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TeacherView {

    public static void show(Teacher teacher) {
        JFrame frame = new JFrame("Teacher Dashboard - Gradebook");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(10, 10));

        // --- TOP PANEL FOR SUBJECT SELECTION ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        topPanel.add(new JLabel("Select Subject to Grade:"));

        String[] subjects = {
            "Machine Learning and Data Analytics using Python", "Object Oriented Programming using Java",
            "Data Structure and Algorithm", "Software Engineering", "Web Application Development",
            "Object Oriented Programming using Java Laboratory", "Data Structure and Algorithm Laboratory"
        };
        JComboBox<String> subjectDropdown = new JComboBox<>(subjects);
        topPanel.add(subjectDropdown);
        frame.add(topPanel, BorderLayout.NORTH);

        // --- CENTER PANEL FOR THE GRADE TABLE ---
        String[] columnNames = {"Student ID", "Student Name", "Marks (0-100)", "Attendance (%)"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2 || column == 3;
            }
        };

        JTable gradeTable = new JTable(tableModel);
        gradeTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gradeTable.setRowHeight(25);
        gradeTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        gradeTable.getColumnModel().getColumn(1).setPreferredWidth(250);
        
        List<Student> students = DatabaseManager.getAllStudents();

        // --- HELPER LAMBDA TO POPULATE TABLE (CORRECTED) ---
        Runnable populateTable = () -> {
            String selectedSubject = (String) subjectDropdown.getSelectedItem();
            Map<String, Integer> marks = DatabaseManager.getMarksForSubject(selectedSubject);
            Map<String, Integer> attendance = DatabaseManager.getAttendanceForSubject(selectedSubject);

            tableModel.setRowCount(0);

            for (Student student : students) {
                // ** THE FIX IS HERE **
                // Get the Integer value first. It will be 'null' if not found.
                Integer markValue = marks.get(student.studentId);
                Integer attendanceValue = attendance.get(student.studentId);
                
                // If the value is null, use an empty string for the table. Otherwise, use the number.
                Object markForTable = (markValue == null) ? "" : markValue;
                Object attendanceForTable = (attendanceValue == null) ? "" : attendanceValue;

                tableModel.addRow(new Object[]{student.studentId, student.name, markForTable, attendanceForTable});
            }
        };

        // --- Add an ActionListener to the dropdown ---
        subjectDropdown.addActionListener(e -> populateTable.run());
        
        // Initial population
        populateTable.run();

        JScrollPane scrollPane = new JScrollPane(gradeTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        frame.add(scrollPane, BorderLayout.CENTER);

        // --- BOTTOM PANEL FOR THE SAVE BUTTON ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        JButton saveButton = new JButton("Save All Changes");
        bottomPanel.add(saveButton);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // --- ACTION LISTENER FOR THE SAVE BUTTON (Unchanged) ---
        saveButton.addActionListener(_ -> {
            if (gradeTable.isEditing()) {
                gradeTable.getCellEditor().stopCellEditing();
            }
            String selectedSubject = (String) subjectDropdown.getSelectedItem();
            int savedCount = 0;
            
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String studentId = (String) tableModel.getValueAt(i, 0);
                String marksStr = tableModel.getValueAt(i, 2).toString();
                String attendanceStr = tableModel.getValueAt(i, 3).toString();

                if (!marksStr.isEmpty() && !attendanceStr.isEmpty()) {
                    try {
                        int marksVal = Integer.parseInt(marksStr);
                        int attendanceVal = Integer.parseInt(attendanceStr);
                        if (marksVal < 0 || marksVal > 100 || attendanceVal < 0 || attendanceVal > 100) {
                            JOptionPane.showMessageDialog(frame, "Error in row " + (i + 1) + ": Marks and attendance must be between 0 and 100.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                            continue;
                        }
                        DatabaseManager.saveMarksAndAttendance(studentId, selectedSubject, marksVal, attendanceVal);
                        savedCount++;
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Error in row " + (i + 1) + ": Please enter valid numbers for marks and attendance.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }
                }
            }
            JOptionPane.showMessageDialog(frame, savedCount + " student records have been saved successfully for " + selectedSubject + ".", "Save Successful", JOptionPane.INFORMATION_MESSAGE);
        });

        frame.setVisible(true);
    }
}