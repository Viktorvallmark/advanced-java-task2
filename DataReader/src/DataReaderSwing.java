import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JScrollBar;
import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.opencsv.CSVReader;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import java.awt.Rectangle;
import javax.swing.JEditorPane;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.CardLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.JTextArea;

@SuppressWarnings("unused")
public class DataReaderSwing {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DataReaderSwing window = new DataReaderSwing();
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
	public DataReaderSwing() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
//		Creating JFrame, JPanel, JScrollPane.

		frame = new JFrame("Viktor's data displayer");
		frame.setBounds(100, 100, 1000, 1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		DefaultTableModel table_data=new DefaultTableModel();
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		

		
		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane);
		
		
		//Adding buttons and actionlisteners
		//JSON
		JButton jsonButton = new JButton("JSON");
		panel.add(jsonButton);
		jsonButton.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//Building the JTable after button has been pressed
				
				String filePath = "C:\\Users\\Viktor\\eclipse-workspace\\DataReader\\src\\dataFolder\\sample.json";
				File file = new File(filePath);
				int column = 0;
				
				try {
					Object obj = new JSONParser().parse(new FileReader(file));
					JSONArray billingInfo = (JSONArray) obj;
					//Constructing the column names
					for(int i = 0; i < billingInfo.size(); i++) {
						if(column==0) {
							column++;
							table_data.addColumn("OrderDate");
							table_data.addColumn("Region");
							table_data.addColumn("Rep1");
							table_data.addColumn("Rep2");
							table_data.addColumn("Item");
							table_data.addColumn("Units");
							table_data.addColumn("UnitCost");
							table_data.addColumn("Total");
						}else {
							//Constructing the rows
							Vector<Object> row = new Vector<Object>();
							JSONObject infoObj = (JSONObject) billingInfo.get(i);
							
							row.add(infoObj.get("OrderDate"));
							row.add(infoObj.get("Region"));
							row.add(infoObj.get("Rep1"));
							row.add(infoObj.get("Rep2"));
							row.add(infoObj.get("Item"));
							long units = (long)infoObj.get("Units");
							String unitCost = (String)infoObj.get("UnitCost");
							String total = (String)infoObj.get("Total");
							row.add(units);
							row.add(unitCost.replaceAll(".00", ""));
							if(infoObj.get("Total").equals("")) {
								row.add(String.valueOf(units*Double.valueOf(unitCost)));
							}
							row.add(total.replace(".00", ""));
							table_data.addRow(row);
							
						}
					}
					
			        JTable jsonDataTable=new JTable();
			        jsonDataTable.setModel(table_data);
			        jsonDataTable.setAutoCreateRowSorter(true);
			        scrollPane.setViewportView(jsonDataTable);
			        
				}catch (Exception e3) {
					e3.printStackTrace();
				}
			}});
		
		//CSV
		JButton csvButton = new JButton("CSV");
		panel.add(csvButton);
		

		csvButton.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
					//Building the JTable after button has been pressed
					int line = 0;
					String filePath = "C:\\Users\\Viktor\\eclipse-workspace\\DataReader\\src\\dataFolder\\sample.csv";
					File file = new File(filePath);
					
				try {
					
					FileReader fileRead = new FileReader(file);
					CSVReader csvReader = new CSVReader(fileRead);
					//Looping through the String array produced by the csvreader
					for(String[] csvData : csvReader) {
						if(line==0) {
							line +=1;
							table_data.addColumn(csvData[0]);
							table_data.addColumn(csvData[1]);
							table_data.addColumn(csvData[2]);
							table_data.addColumn(csvData[3]);
							table_data.addColumn(csvData[4]);
							table_data.addColumn(csvData[5]);
							table_data.addColumn(csvData[6]);
							table_data.addColumn(csvData[7]);
						}else {
							Vector<Object> row = new Vector<Object>();
							row.add(csvData[0]);
							row.add(csvData[1]);
							row.add(csvData[2]);
							row.add(csvData[3]);
							row.add(csvData[4]);
							row.add(csvData[5]);
							//Doing some regex to fix "corrupt" data
							row.add(csvData[6].replaceAll(".00", ""));
							if(csvData[7].isBlank()) {
								csvData[7] = String.valueOf(Integer.valueOf(csvData[5])*Float.valueOf(csvData[6]));
							}
							row.add(csvData[7].replace(".00", ""));
							table_data.addRow(row);
						}
					}
			        JTable csvDataTable=new JTable();
			        csvDataTable.setModel(table_data);
			        csvDataTable.setAutoCreateRowSorter(true);
			  		scrollPane.setViewportView(csvDataTable);
					
					csvReader.close();
					
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			}});
	        
	}
	}

