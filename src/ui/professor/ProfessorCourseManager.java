package ui.professor;

import models.Course;
import service.CourseServiceClient;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ProfessorCourseManager extends JFrame {
    private CourseServiceClient courseService = new CourseServiceClient();
    private JTable courseTable;
    private DefaultTableModel tableModel;

    private JTextField titleField;
    private JTextArea descriptionArea;
    private JComboBox<Course.Level> levelCombo;
    private JButton submitBtn;

    private int professorId;
    private Integer selectedCourseId = null; // used for update

    public ProfessorCourseManager(int professorId) {
        this.professorId = professorId;
        setTitle("📚 Manage Your Courses");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        add(createTopPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);

        refreshCourseTable();
        setVisible(true);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder("➕ Add or Edit Course"));
        topPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        topPanel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1;
        titleField = new JTextField(15);
        topPanel.add(titleField, gbc);

        gbc.gridx = 2;
        topPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 3;
        descriptionArea = new JTextArea(2, 15);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        topPanel.add(scrollPane, gbc);

        gbc.gridx = 4;
        topPanel.add(new JLabel("Level:"), gbc);
        gbc.gridx = 5;
        levelCombo = new JComboBox<>(Course.Level.values());
        topPanel.add(levelCombo, gbc);

        gbc.gridx = 6;
        submitBtn = new JButton("Add Course");
        submitBtn.setBackground(new Color(63, 81, 181));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.addActionListener(e -> {
            if (selectedCourseId == null) {
                addCourse();
            } else {
                updateCourse();
            }
        });
        topPanel.add(submitBtn, gbc);

        return topPanel;
    }

    private JScrollPane createTablePanel() {
        tableModel = new DefaultTableModel(new String[]{"Title", "Description", "Level", "ID", "Action"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };

        courseTable = new JTable(tableModel);
        courseTable.setRowHeight(30);

        // Hide the ID column
        TableColumnModel columnModel = courseTable.getColumnModel();
        columnModel.getColumn(3).setMinWidth(0);
        columnModel.getColumn(3).setMaxWidth(0);
        columnModel.getColumn(3).setWidth(0);

        courseTable.getColumn("Action").setCellRenderer(new ButtonRenderer());
        courseTable.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox()));

        courseTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && courseTable.getSelectedRow() != -1) {
                    int row = courseTable.getSelectedRow();
                    selectedCourseId = (int) tableModel.getValueAt(row, 3);
                    titleField.setText((String) tableModel.getValueAt(row, 0));
                    descriptionArea.setText((String) tableModel.getValueAt(row, 1));
                    levelCombo.setSelectedItem(Course.Level.valueOf((String) tableModel.getValueAt(row, 2)));
                    submitBtn.setText("Update Course");
                }
            }
        });

        return new JScrollPane(courseTable);
    }

    private void refreshCourseTable() {
        tableModel.setRowCount(0);
        List<Course> courses = courseService.getCoursesByProfessorId(professorId);
        if (courses != null) {
            for (Course c : courses) {
                tableModel.addRow(new Object[]{c.getTitle(), c.getDescription(), c.getLevel().name(), c.getId(), "Delete"});
            }
        }
    }

    private void addCourse() {
        String title = titleField.getText().trim();
        String desc = descriptionArea.getText().trim();
        Course.Level level = (Course.Level) levelCombo.getSelectedItem();

        if (title.isEmpty() || desc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        Course newCourse = new Course();
        newCourse.setTitle(title);
        newCourse.setDescription(desc);
        newCourse.setLevel(level);
        newCourse.setProfessorId(professorId);

        boolean success = courseService.createCourse(newCourse);
        if (success) {
            JOptionPane.showMessageDialog(this, "✅ Course added successfully!");
            resetForm();
            refreshCourseTable();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Failed to add course.");
        }
    }

    private void updateCourse() {
        String title = titleField.getText().trim();
        String desc = descriptionArea.getText().trim();
        Course.Level level = (Course.Level) levelCombo.getSelectedItem();

        if (title.isEmpty() || desc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        Course updatedCourse = new Course();
        updatedCourse.setId(selectedCourseId);
        updatedCourse.setTitle(title);
        updatedCourse.setDescription(desc);
        updatedCourse.setLevel(level);
        updatedCourse.setProfessorId(professorId);

        boolean success = courseService.updateCourse(updatedCourse);
        if (success) {
            JOptionPane.showMessageDialog(this, "✅ Course updated successfully!");
            resetForm();
            refreshCourseTable();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Failed to update course.");
        }
    }

    private void resetForm() {
        titleField.setText("");
        descriptionArea.setText("");
        levelCombo.setSelectedIndex(0);
        selectedCourseId = null;
        submitBtn.setText("Add Course");
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setText("Delete");
            setBackground(new Color(244, 67, 54));
            setForeground(Color.WHITE);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private boolean clicked;
        private int selectedRow;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Delete");
            button.setOpaque(true);
            button.setBackground(new Color(244, 67, 54));
            button.setForeground(Color.WHITE);
            button.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            selectedRow = row;
            clicked = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (clicked) {
                String courseTitle = (String) tableModel.getValueAt(selectedRow, 0);
                int courseId = (int) tableModel.getValueAt(selectedRow, 3);

                int confirm = JOptionPane.showConfirmDialog(button,
                        "Are you sure you want to delete \"" + courseTitle + "\"?",
                        "Confirm Deletion", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    boolean success = courseService.deleteCourse(courseId);
                    if (success) {
                        JOptionPane.showMessageDialog(button, "Course deleted successfully.");
                        refreshCourseTable();
                        resetForm();
                    } else {
                        JOptionPane.showMessageDialog(button, "Failed to delete course.");
                    }
                }
            }
            clicked = false;
            return "Delete";
        }

        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }
    }
}
