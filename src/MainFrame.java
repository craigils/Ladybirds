import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.JTable;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.JTextField;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTabbedPane;
import java.awt.Panel;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.Canvas;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JPanel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.JScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainFrame {

    private JFrame frame;
    private JTextField txtTitle;
    private JTextField txtAuthorFirst;
    private JTextField txtAuthorLast;
    MySQLAccess dao;
    private JTable tblBookHistory;
    private JTextField txtMember;
    private JTextField txtDate;
    private JTextField txtSearch;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
	EventQueue.invokeLater(new Runnable() {
	    public void run() {
		try {
		    MainFrame window = new MainFrame();
		    window.frame.setVisible(true);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	});
    }

    /**
     * Create the application.
     */
    public MainFrame() throws Exception {
	initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() throws Exception {
	// Main frame dimensions
	frame = new JFrame();
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	frame.setBounds(100, 100, (int) (0.5 * screenSize.getWidth()), (int) (0.8 * screenSize.getHeight()));
	frame.setVisible(true);
	frame.setResizable(false);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	// Tabbed pane dimensions
	JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	tabbedPane.setBounds(0, 0, (int) (0.495 * screenSize.getWidth()), (int) (0.76 * screenSize.getHeight()));
	frame.getContentPane().setLayout(null);
	frame.getContentPane().add(tabbedPane);
	JPanel panel = new JPanel();
	tabbedPane.addTab("Book History", null, panel, null);
	panel.setLayout(null);

	// Scrollpane dimensions
	JScrollPane scrollPane = new JScrollPane();
	scrollPane.setBounds(10, 11, (int) (0.98 * tabbedPane.getWidth()), tabbedPane.getHeight() / 2);
	panel.add(scrollPane);

	// Setting class types per column of table
	DefaultTableModel model = new DefaultTableModel() {
	    public boolean isCellEditable(int row, int column) {
		return false;
	    }

	    final Class<?>[] columnClasses = new Class<?>[] { String.class, String.class, String.class, String.class, Date.class };

	    @Override
	    public Class<?> getColumnClass(int column) {
		return columnClasses[column];
	    }
	};
	tblBookHistory = new JTable(model);

	// Setting action listener for clicking rows of table
	tblBookHistory.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
	    public void valueChanged(ListSelectionEvent event) {
		if (tblBookHistory.getSelectedRow()!=-1) {
		    String surname = tblBookHistory.getValueAt(tblBookHistory.getSelectedRow(), 0).toString();
		    String firstName = tblBookHistory.getValueAt(tblBookHistory.getSelectedRow(), 1).toString();
		    String bookTitle = CapsFirst(tblBookHistory.getValueAt(tblBookHistory.getSelectedRow(), 2).toString());
		    String member = tblBookHistory.getValueAt(tblBookHistory.getSelectedRow(), 3).toString();
		    String dateAdded = tblBookHistory.getValueAt(tblBookHistory.getSelectedRow(), 4).toString();

		    txtAuthorFirst.setText(firstName);
		    txtAuthorLast.setText(surname);
		    txtTitle.setText(bookTitle);
		    txtMember.setText(member);
		    txtDate.setText(dateAdded);
		}

	    }
	});
	tblBookHistory.setAutoCreateRowSorter(true);
	tblBookHistory.setFillsViewportHeight(true);
	scrollPane.setViewportView(tblBookHistory);

	// Populate table
	dao = new MySQLAccess();
	try {
	    dao.readDataBase();
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
	populateTable();

	// Set the date representation in the table
	TableCellRenderer tableCellRenderer = new DefaultTableCellRenderer() {

	    SimpleDateFormat f = new SimpleDateFormat("MMM yyyy");

	    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if (value instanceof Date) {
		    value = f.format(value);
		}
		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	    }
	};
	tblBookHistory.getColumnModel().getColumn(4).setCellRenderer(tableCellRenderer);

	JLabel lblTitle = new JLabel("Title");
	lblTitle.setHorizontalAlignment(SwingConstants.RIGHT);
	lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 15));
	lblTitle.setBounds(10, 543, 130, 29);
	panel.add(lblTitle);

	txtTitle = new JTextField();
	txtTitle.setBounds(150, 543, 285, 32);
	panel.add(txtTitle);
	txtTitle.setColumns(10);

	txtAuthorFirst = new JTextField();
	txtAuthorFirst.setColumns(10);
	txtAuthorFirst.setBounds(150, 583, 285, 32);
	panel.add(txtAuthorFirst);

	JLabel lblAuthorFirst = new JLabel("Author Name");
	lblAuthorFirst.setHorizontalAlignment(SwingConstants.RIGHT);
	lblAuthorFirst.setFont(new Font("Tahoma", Font.PLAIN, 15));
	lblAuthorFirst.setBounds(10, 583, 130, 29);
	panel.add(lblAuthorFirst);

	txtAuthorLast = new JTextField();
	txtAuthorLast.setColumns(10);
	txtAuthorLast.setBounds(150, 626, 285, 32);
	panel.add(txtAuthorLast);

	JLabel lblAuthorLast = new JLabel("Author Surname");
	lblAuthorLast.setHorizontalAlignment(SwingConstants.RIGHT);
	lblAuthorLast.setFont(new Font("Tahoma", Font.PLAIN, 15));
	lblAuthorLast.setBounds(10, 626, 130, 29);
	panel.add(lblAuthorLast);

	JLabel lblMember = new JLabel("Member");
	lblMember.setHorizontalAlignment(SwingConstants.RIGHT);
	lblMember.setFont(new Font("Tahoma", Font.PLAIN, 15));
	lblMember.setBounds(472, 543, 130, 29);
	panel.add(lblMember);

	txtMember = new JTextField();
	txtMember.setColumns(10);
	txtMember.setBounds(612, 543, 195, 32);
	panel.add(txtMember);

	JLabel lblDate = new JLabel("Date");
	lblDate.setHorizontalAlignment(SwingConstants.RIGHT);
	lblDate.setFont(new Font("Tahoma", Font.PLAIN, 15));
	lblDate.setBounds(472, 583, 130, 29);
	panel.add(lblDate);

	txtDate = new JTextField();
	txtDate.setColumns(10);
	txtDate.setBounds(612, 583, 195, 32);
	panel.add(txtDate);

	JLabel lblSearch = new JLabel("Search");
	lblSearch.setHorizontalAlignment(SwingConstants.RIGHT);
	lblSearch.setFont(new Font("Tahoma", Font.PLAIN, 15));
	lblSearch.setBounds(10, 432, 130, 29);
	panel.add(lblSearch);

	txtSearch = new JTextField();
	txtSearch.setColumns(10);
	txtSearch.setBounds(150, 432, 285, 32);
	panel.add(txtSearch);

    }

    String CapsFirst(String str) {
	String[] words = str.split(" ");
	StringBuilder ret = new StringBuilder();
	for (int i = 0; i < words.length; i++) {
	    if (words[i] != null && !words[i].isEmpty()) {
		ret.append(Character.toUpperCase(words[i].charAt(0)));
		ret.append(words[i].substring(1));
		if (i < words.length - 1) {
		    ret.append(' ');
		}
	    }
	}
	return ret.toString();
    }

    private void populateTable() throws Exception {
	// TODO Auto-generated method stub
	// TableModel definition
	String[] tableColumnsName = { "Surname", "First Name", "Book Title", "Member", "Date Added" };
	DefaultTableModel aModel = (DefaultTableModel) tblBookHistory.getModel();
	aModel.setColumnIdentifiers(tableColumnsName);

	// the query
	ResultSet resultSet = dao.statement.executeQuery("SELECT * FROM `TABLE 1`");
	// dao.writeResultSet(resultSet);

	// Loop through the ResultSet and transfer in the Model
	java.sql.ResultSetMetaData rsmd = resultSet.getMetaData();
	int colNo = rsmd.getColumnCount();
	while (resultSet.next()) {
	    Object[] objects = new Object[colNo];
	    for (int i = 0; i < colNo; i++) {
		if (i + 1 == 3) {
		    objects[i] = CapsFirst(resultSet.getString(i + 1));
		} else if (i + 1 == 5) {
		    Date date = new SimpleDateFormat("dd/MM/yyyy").parse(resultSet.getString(i + 1));
		    objects[i] = date;
		} else {
		    objects[i] = resultSet.getObject(i + 1);
		}

	    }
	    aModel.addRow(objects);
	}
    }
}
