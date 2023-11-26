package in.ser.the_ultimate_scrum_simulator.Pages;
import in.ser.the_ultimate_scrum_simulator.DbWrapper;
import in.ser.the_ultimate_scrum_simulator.UserInterface.MyPanel;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ObserverActivePage extends MyPanel {

    private JFrame parentFrame;

    public ObserverActivePage(JFrame frame) throws SQLException {
        this.parentFrame = frame;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 100, 20));
        addRoundedButtonToContainer(this, "go back", e -> {
            parentFrame.getContentPane().removeAll();
            parentFrame.add(new ObserverMainPage(parentFrame), BorderLayout.CENTER);
            parentFrame.revalidate();
            parentFrame.repaint();
        }, FlowLayout.LEFT, 0);

        addTitleToContainer(this);
        addDescriptionToContainer(this);
    }

    private void addTitleToContainer(MyPanel container) {
        JLabel title = new JLabel("Currently Active Users");
        title.setForeground(Color.BLACK);
        title.setFont(new Font("Space Mono", Font.PLAIN, 75));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(title);
    }

    private void addDescriptionToContainer(MyPanel container) throws SQLException {
        DbWrapper db = new DbWrapper();
        List<String> userList = db.selectAll();

        String[] columnNames = {"Select", "Username"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return (column == 0) ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0; // Only the checkbox column is editable
            }
        };

        for (String user : userList) {
            Object[] row = new Object[]{false, user};
            model.addRow(row);
        }

        JTable table = new JTable(model);
        table.setFont(new Font("Space Mono", Font.PLAIN, 25));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Space Mono", Font.BOLD, 30));
        setColumnWidths(table, 50, 200); // Adjusting the width of columns

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.BLACK, 2), new EmptyBorder(25, 50, 180, 50)));
        container.add(scrollPane);
    }

    private void setColumnWidths(JTable table, int checkboxWidth, int usernameWidth) {
        table.getColumnModel().getColumn(0).setPreferredWidth(checkboxWidth);
        table.getColumnModel().getColumn(1).setPreferredWidth(usernameWidth);
    }
}
