// Aaron Tsang
// ICS4U Final Culminating
// This program runs a prescription management system, which allows for the user to 
// drag and drop different drugs onto a prescription maker and edit, search, and add patients/prescriptions. 
package Final;

// imports
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class MainApp {
	// global variables
	private final static int SEARCH_PATIENT = 0;
	private final static int SEARCH_PRESCRIPTION = 1;
	private final static int REPORT_PATIENT = 0;
	private final static int REPORT_SUMMARY = 1;
	private final static String[] SEARCH_TITLE = { "Patient Search/Manage", "Prescription Search" };
	private final static String[] REPORT_TITLE = { "Patient Report", "System Summary" };
	private final static String[] PROVINCES = { "Alberta", "British Columbia", "Manitoba", "New Brunswick",
			"Newfoundland and Labrador", "Nova Scotia", "Ontario", "Prince Edward Island", "Quebec", "Saskatchewan",
			"Northwest Territories", "Nunavut", "Yukon" };

	private JFrame frame;
	private JDialog searchFrame;
	private JList<Drug> drugList;
	private DefaultListModel<Drug> drugListModel;
	private JPanel prescriptionPanel;
	private JComboBox<Patient> cbPatients;

	// Prescription related fields
	private JComboBox<Map.Entry<String, String>> cbInstructionCodes;
	private DefaultComboBoxModel<Map.Entry<String, String>> instructionCodeDropdownModel;
	private JTextField tfPatientAddress;
	private JTextField tfPatientPhone;
	private JTextField tfPatientConditions;
	private JTextField tfPatientPractioner;
	private JTextField tfDrugName;
	private JTextField tfDate;
	private JFormattedTextField ftfQuantity;
	private JFormattedTextField ftfUnitPrice;
	private JFormattedTextField ftfRepeat;
	private JFormattedTextField ftfCharges;

	// Drug related fields
	private JLabel drugImage = new JLabel();
	private JLabel drugHelp = new JLabel();
	private ImageIcon iiDrugDragNDrop;
	private JTextField tfBrandName;
	private JTextField tfDin;
	private JTextField tfChemicalName;
	private JTextField tfManufacturer;
	private JTextField tfDosageForm;
	private JTextField tfAppearance;
	private JTextField tfStrength;
	private JTextField tfOtherAvailableStrengths;
	private JTextField tfIndication;

	// New Patient related fields
	private JComboBox<Practioner> cbNewPatientPractioners;
	private JComboBox<String> cbNewPatientProvinces;
	private JTextField tfNewPatientName;
	private JTextField tfNewPatientPhone;
	private JTextField tfNewPatientAddress;
	private JTextField tfNewPatientCity;
	private JTextField tfNewPatientPostalCode;
	private JTextField tfNewPatientMedicalConditions;
	private JTextField tfNewPatientPractionerLicenseNumber;
	private JTextField tfNewPatientPractionerType;
	private JTextField tfNewPatientPractionerPhone;

	// Search Prescription related fields
	private JComboBox<String> cbPrescriptionSearchPatients;
	private DefaultComboBoxModel<String> prescriptionSearchPatientDropdownModel;
	private PrescriptionTableModel prescriptionTableModel;
	private JTextField tfPrescriptionSearchPatientAddress;
	private JTextField tfPrescriptionSearchPatientPhone;
	private JTextField tfPrescriptionSearchPatientConditions;
	private JTextField tfPrescriptionSearchPatientPractioner;

	// Search Patient related fields
	private JComboBox<String> cbPatientSearchPatients;
	private DefaultComboBoxModel<String> patientSearchPatientDropdownModel;
	private JTextField tfPatientSearchPatientAddress;
	private JTextField tfPatientSearchPatientCity;
	private JTextField tfPatientSearchPatientPostalCode;
	private JTextField tfPatientSearchPatientPhone;
	private JTextField tfPatientSearchPatientMedicalConditions;
	private JTextField tfPatientSearchPatientPractioner;
	private JTextField tfPatientSearchPatientPractionerType;

	// file paths and maps for storing data
	private final static String DRUG_FILE_PATH = "data/drug-list.dat";
	private final static String PATIENTS_FILE_PATH = "data/patients.dat";
	private final static String PRACTIONERS_FILE_PATH = "data/practioners.dat";
	private final static String PRESCRIPTIONS_FILE_PATH = "data/prescriptions.dat";
	private ArrayList<Map.Entry<String, String>> instructionCodeList;
	private TreeMap<String, Drug> drugMap = null;
	private TreeMap<String, Patient> patientMap = null;
	private TreeMap<String, Practioner> practionerMap = null;
	private TreeMap<String, Prescription> prescriptionMap = null;
	private TreeMap<String, ImageIcon> drugImages;
	private TreeMap<String, String> instructionMap = null;
	private TreeSet<String> prescriptionPatientSet = null;
	private ArrayList<String> prescriptionPatientList = null;
	private ArrayList<Prescription> prescriptionResultList = null;
	private ArrayList<String> patientList = null;
	private Font COURIER_BOLD = new Font("Courier", Font.BOLD, 20);
	private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	private NumberFormat amountFormat = NumberFormat.getInstance();

	// system runtime
	private long startTime, endTime;

	// Introduction: Method reads in from the designated file path
	// Method: The parameter is the file path as a string.
	// Internal: returns the object read from the file, can be Drug, Patient,
	// Practitioner, or Prescription object
	public Object readObjectsFromFile(String filePath) {
		Object obj = null;
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
			obj = ois.readObject();
			ois.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		return obj;
	}

	// Introduction: Method loads the data from the file
	// Method: There are no parameters, the data is saved in a TreeMap, while the
	// patients with a prescription are saved in a TreeSet.
	// Internal: No return value, but the code for creating the help image is done
	// here.
	private void loadData() {
		startTime = System.currentTimeMillis();
		drugMap = (TreeMap<String, Drug>) readObjectsFromFile(DRUG_FILE_PATH);
		patientMap = (TreeMap<String, Patient>) readObjectsFromFile(PATIENTS_FILE_PATH);
		practionerMap = (TreeMap<String, Practioner>) readObjectsFromFile(PRACTIONERS_FILE_PATH);
		prescriptionMap = (TreeMap<String, Prescription>) readObjectsFromFile(PRESCRIPTIONS_FILE_PATH);
		prescriptionPatientSet = new TreeSet<>();
		for (Prescription prescription : prescriptionMap.values()) {
			prescriptionPatientSet.add(prescription.getPatient().getName());
		}
		setupInstructions();

		// Create the help image
		ImageIcon helpImage = new ImageIcon("images/drag-n-drop.png");
		Image newImage = helpImage.getImage().getScaledInstance(1170, 405, Image.SCALE_SMOOTH);
		iiDrugDragNDrop = new ImageIcon(newImage);
	}

	// Introduction: This method populates a TreeMap with instructions for the
	// drugs.

	// Method: There are no parameters, and this method is called in the loadData()
	// method.

	// Internal: No return value, and the instructions are just put in the TreeMap.
	private void setupInstructions() {
		instructionMap = new TreeMap<String, String>();
		instructionMap.put("QD", "once a day");
		instructionMap.put("BID", " twice a day");
		instructionMap.put("TID", " three times a day");
		instructionMap.put("QID", " four times a day");
		instructionMap.put("UF", "until finished");
		instructionMap.put("INS", "i to ii GTTS instil one to two drops");
		instructionMap.put("OU", "each eye");
		instructionMap.put("OD", "right eye");
		instructionMap.put("OS", "left eye");
		instructionMap.put("AU", "each year");
		instructionMap.put("AD", "right ear");
		instructionMap.put("AS", "left ear");
		instructionMap.put("SL", "sublingual/under the tongue");
		instructionMap.put("PR", "rectally");
		instructionMap.put("PRN", "as needed");
		instructionMap.put("UD", "as directed");
		instructionMap.put("PO", "by mouth");
		instructionMap.put("HS", "at bedtime");
		instructionMap.put("APP", "SP apply sparingly");
		instructionMap.put("PF", "puff");
		instructionMap.put("SIG", "direction");
		instructionMap.put("MITTE", "quantity");
	}

	// Introduction: Method creates a JDialog which shows how the drag and drop
	// function works as a picture.

	// Method: There are no parameters, and the picture is stored in a JLabel.

	// Internal: No return value.
	private void showDrugHelpDialog() {
		JDialog dialogFrame = new JDialog(frame, "Help - Drug Drag n' Drop", true);
		JLabel helpImageLabel = new JLabel(iiDrugDragNDrop);

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(helpImageLabel, BorderLayout.CENTER);

		dialogFrame.setContentPane(panel);
		dialogFrame.setSize(1170, 405);
		dialogFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		dialogFrame.setLocationRelativeTo(null);
		dialogFrame.setVisible(true);
	}

	// Introduction: Method creates a JDialog which shows how the drag and drop
	// function works as a picture, but this one appears in the actual system
	// screen, not the menu screen. This is to make it easier for the user, so the
	// user does not have to go back and forth from the menu screen to the system
	// screen.

	// Method: There are no parameters, and the picture is stored in a JLabel.

	// Internal: No return value.
	private void showDrugHelpDialogQuestionMark(Point clickPosition) {
		JDialog dialogFrame = new JDialog(frame, "Help - Drug Drag n' Drop", true);
		JLabel helpImageLabel = new JLabel(iiDrugDragNDrop);

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(helpImageLabel, BorderLayout.CENTER);

		dialogFrame.setContentPane(panel);
		dialogFrame.setSize(1170, 405);
		dialogFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		dialogFrame.setLocationRelativeTo(null);
		dialogFrame.setLocation((int) clickPosition.getX() + 350, (int) clickPosition.getY() + 100);
		dialogFrame.setVisible(true);
	}

	// Introduction: Method sets up all of the drugs in a TreeMap.

	// Method: The parameter is JPanel leftPanel, as the drug list is located in the
	// left panel, and the drugs can be dragged and dropped onto the right panel.

	// Internal: Nothing is returned, but in the middle of the screen, drug
	// information will be shown.
	private void setupDrugList(JPanel leftPanel) {
		drugImages = new TreeMap<String, ImageIcon>();

		// Drug List
		drugListModel = new DefaultListModel<>();
		for (Drug drug : drugMap.values()) {
			drugListModel.addElement(drug);
			ImageIcon icon = new ImageIcon(drug.getImagePath());
			Image newImage = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
			drugImages.put(drug.getBrandName(), new ImageIcon(newImage));
		}

		drugList = new JList<>(drugListModel);
		drugList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		drugList.setCellRenderer(new DrugListRenderer());
		drugList.addListSelectionListener(e -> showDrugDetails(drugList.getSelectedValue()));
		drugList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				int index = drugList.locationToIndex(e.getPoint());
				if (index != -1) {
					Rectangle cellBounds = drugList.getCellBounds(index, index);
					if (cellBounds != null && cellBounds.contains(e.getPoint())) {
						String tooltipText = "Drag the drug to Prescription Panel";
						drugList.setToolTipText(tooltipText);
					} else {
						drugList.setToolTipText(null);
					}
				}
			}
		});
		drugHelp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				showDrugHelpDialogQuestionMark(e.getPoint());
			}
		});
		JScrollPane drugListScrollPanel = new JScrollPane(drugList);
		leftPanel.add(drugListScrollPanel);

		// Drug Details
		tfBrandName = new JTextField();
		tfBrandName.setEditable(false);
		tfDin = new JTextField();
		tfDin.setEditable(false);
		tfChemicalName = new JTextField();
		tfChemicalName.setEditable(false);
		tfManufacturer = new JTextField();
		tfManufacturer.setEditable(false);
		tfDosageForm = new JTextField();
		tfDosageForm.setEditable(false);
		tfAppearance = new JTextField();
		tfAppearance.setEditable(false);
		tfStrength = new JTextField();
		tfStrength.setEditable(false);
		tfOtherAvailableStrengths = new JTextField();
		tfOtherAvailableStrengths.setEditable(false);
		tfIndication = new JTextField();
		tfIndication.setEditable(false);

		// panel that displays drug details
		JPanel drugDetailPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		int y = 0;

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = y++;
		ImageIcon icon = new ImageIcon("images/question.png");
		Image newImage = icon.getImage().getScaledInstance(10, 10, Image.SCALE_SMOOTH);
		drugHelp.setIcon(new ImageIcon(newImage));
		drugDetailPanel.add(drugHelp, gbc);
		gbc.gridx = 0;
		gbc.gridy = y++;
		drugDetailPanel.add(new JLabel("Drug Name: "), gbc);
		gbc.gridx = 1;
		drugDetailPanel.add(tfBrandName, gbc);
		gbc.gridx = 0;
		gbc.gridy = y++;
		drugDetailPanel.add(new JLabel("DIN: "), gbc);
		gbc.gridx = 1;
		drugDetailPanel.add(tfDin, gbc);
		gbc.gridx = 0;
		gbc.gridy = y++;
		drugDetailPanel.add(new JLabel("Chemical Name: "), gbc);
		gbc.gridx = 1;
		drugDetailPanel.add(tfChemicalName, gbc);
		gbc.gridx = 0;
		gbc.gridy = y++;
		drugDetailPanel.add(new JLabel("Manufacturer: "), gbc);
		gbc.gridx = 1;
		drugDetailPanel.add(tfManufacturer, gbc);
		gbc.gridx = 0;
		gbc.gridy = y++;
		drugDetailPanel.add(new JLabel("Dosage Form: "), gbc);
		gbc.gridx = 1;
		drugDetailPanel.add(tfDosageForm, gbc);
		gbc.gridx = 0;
		gbc.gridy = y++;
		drugDetailPanel.add(new JLabel("Appearance: "), gbc);
		gbc.gridx = 1;
		drugDetailPanel.add(tfAppearance, gbc);
		gbc.gridx = 0;
		gbc.gridy = y++;
		drugDetailPanel.add(new JLabel("Strength: "), gbc);
		gbc.gridx = 1;
		drugDetailPanel.add(tfStrength, gbc);
		gbc.gridx = 0;
		gbc.gridy = y++;
		drugDetailPanel.add(new JLabel("Other Available Strengths: "), gbc);
		gbc.gridx = 1;
		drugDetailPanel.add(tfOtherAvailableStrengths, gbc);
		gbc.gridx = 0;
		gbc.gridy = y++;
		drugDetailPanel.add(new JLabel("indication: "), gbc);
		gbc.gridx = 1;
		drugDetailPanel.add(tfIndication, gbc);
		gbc.gridx = 0;
		gbc.gridy = y++;
		drugDetailPanel.add(drugImage, gbc);

		drugList.setSelectedIndex(0);

		leftPanel.add(drugListScrollPanel);
		leftPanel.add(drugDetailPanel);
	}

	// Introduction: Method creates a menu bar, with various buttons.

	// Method: The parameter is JFrame frame, or the main frame.

	// Internal: No return value, some of the buttons are an "Rx" icon which has
	// exit and about drop down buttons. The menu bar also has search/manage and
	// report buttons.
	private void setupMenuBar(JFrame frame) {
		// Create the menu bar
		JMenuBar menuBar = new JMenuBar();

		// Create the "Rx" menu with an icon
		JMenu rxMenu = new JMenu();
		ImageIcon rxIcon = new ImageIcon("images/rx.png");
		Image newImage = rxIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);

		rxMenu.setIcon(new ImageIcon(newImage));

		// Create the "Exit" menu item
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Close the application
				System.exit(0);
			}
		});

		// Create the "About" menu item
		JMenuItem aboutMenuItem = new JMenuItem("About");
		aboutMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Display the About dialog
				showAboutDialog();
			}
		});

		// Add menu items to the "Rx" menu
		rxMenu.add(exitMenuItem);
		rxMenu.addSeparator();
		rxMenu.add(aboutMenuItem);

		// Add the "Rx" menu to the menu bar
		menuBar.add(rxMenu);

		// Create the "Search" menu
		JMenu searchMenu = new JMenu("Search/Manage");

		JMenuItem prescriptionMenuItem = new JMenuItem("Prescription");
		prescriptionMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Open the Prescription search screen
				openSearchScreen(SEARCH_PRESCRIPTION);
			}
		});

		JMenuItem patientMenuItem = new JMenuItem("Patient");
		patientMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Open the Patient search screen
				openSearchScreen(SEARCH_PATIENT);
			}
		});

		// Add search options to the "Search" menu
		searchMenu.add(prescriptionMenuItem);
		searchMenu.add(patientMenuItem);

		// Add the "Search" menu to the menu bar
		menuBar.add(searchMenu);

		// Create the "Report" menu
		JMenu reportMenu = new JMenu("Report");

		JMenuItem patientsMenuItem = new JMenuItem("Patients");
		patientsMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Open the By Customer and Patient report screen
				openReportScreen(REPORT_PATIENT);
			}
		});

		JMenuItem systemSummaryMenuItem = new JMenuItem("System Summary");
		systemSummaryMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Open the System Summary report screen
				openReportScreen(REPORT_SUMMARY);
			}
		});

		// Add report options to the "Report" menu
		reportMenu.add(patientsMenuItem);
		reportMenu.add(systemSummaryMenuItem);

		// Add the "Report" menu to the menu bar
		menuBar.add(reportMenu);

		// Set the menu bar for the frame
		frame.setJMenuBar(menuBar);

	}
	// Introduction: Method gets the list of patients with prescriptions.

	// Method: No parameters, but if the prescriptionPatientList is null, a new
	// ArraryList will be created.

	// Internal: The prescriptionPatientList is returned as an ArrayList<String>

	private ArrayList<String> getPrescriptionPatientList() {
		if (prescriptionPatientList == null) {
			prescriptionPatientList = new ArrayList<String>(prescriptionPatientSet);
		}
		return prescriptionPatientList;
	}

	// Introduction: Method searches for the first 3 letters of a patient's name for
	// patients that have a prescription.

	// Method: The parameter is the String filter, which is the first 3 or more
	// characters of a patient's name.

	// Internal: Nothing is returned. The prescription patient dropdown is
	// autofilled after typing the correct letters.
	private void filterPrescriptionPatients(String filter) {
		ArrayList<String> filteredPrescriptionPatients = new ArrayList<>();
		if (filter.length() >= 3) {
			for (String patient : prescriptionPatientList) {
				if (patient.toLowerCase().contains(filter.toLowerCase())) {
					filteredPrescriptionPatients.add(patient);
				}
			}
			updatePrescriptionPatientDropdown(filteredPrescriptionPatients);
		}
	}

	// Autofills the prescription patient dropDown by clearing all present elements,
	// and adding all required elements attributed to the patient.
	private void updatePrescriptionPatientDropdown(ArrayList<String> patients) {
		prescriptionSearchPatientDropdownModel.removeAllElements();
		for (String patient : patients) {
			prescriptionSearchPatientDropdownModel.addElement(patient);
		}
	}

	// Introduction: Method gets the prescriptions attributed to the patient

	// Method: The parameter is the desired patient object.

	// Internal: No return value, and binary search is used to searh for all
	// prescriptions attributed to the patient.
	public void getPrescriptionsByPatient(Patient patient) {
		if (prescriptionResultList == null) {
			prescriptionResultList = new ArrayList<>();
		} else {
			prescriptionResultList.clear();
		}

		ArrayList<Prescription> prescriptionList = new ArrayList<>(prescriptionMap.values());

		prescriptionList.sort(new PrescriptionComparator());

		int startIndex = Collections.binarySearch(prescriptionList, new Prescription(patient),
				new PrescriptionComparator());

		if (startIndex >= 0) {
			// To find the startIndex of the matched patient since binarySearch only returns
			while (true && startIndex > 0) {
				if (prescriptionList.get(startIndex).equals(prescriptionList.get(startIndex - 1))) {
					startIndex--;
				} else {
					break;
				}
			}

			System.out.println("startIndex: " + startIndex + " " + prescriptionList.size() + " " + patient);
			for (int i = startIndex; i < prescriptionList.size(); i++) {
				Prescription prescription = prescriptionList.get(i);
				System.out.println(prescription.getPatient());
				if (prescription.getPatient().compareTo(patient) == 0) {
					prescriptionResultList.add(prescription);
				} else {
					break; // Stop when a different patient is encountered
				}
			}
		}
	}

	// clears prescription search fields on the graphic screen.
	private void clearPrescriptionSearchFields() {
		updatePrescriptionPatientDropdown(getPrescriptionPatientList());
		cbPrescriptionSearchPatients.setSelectedIndex(-1);
	}

	private void updatePrescriptionSearchPatients() {
		for (String patient : getPrescriptionPatientList()) {
			prescriptionSearchPatientDropdownModel.addElement(patient);
		}
	}

	// Introduction: Method sets up the panel for prescription search.

	// Method: makes a JTable that shows the prescription information, which
	// includes patient address, phone number, and practitioner.

	// Internal: No return value, if the clear button is clicked, the JTable will be
	// emptied.
	private void setupPrescriptionSearchPanel(JPanel searchPanel) {
		prescriptionSearchPatientDropdownModel = new DefaultComboBoxModel<>();
		cbPrescriptionSearchPatients = new JComboBox<>(prescriptionSearchPatientDropdownModel);
		prescriptionTableModel = new PrescriptionTableModel(null);
		JTable presciptionTable = new JTable(prescriptionTableModel);

		// Prescription Fields
		tfPrescriptionSearchPatientAddress = new JTextField();
		tfPrescriptionSearchPatientAddress.setEditable(false);
		tfPrescriptionSearchPatientPhone = new JTextField();
		tfPrescriptionSearchPatientPhone.setEditable(false);
		tfPrescriptionSearchPatientConditions = new JTextField();
		tfPrescriptionSearchPatientConditions.setEditable(false);
		tfPrescriptionSearchPatientPractioner = new JTextField();
		tfPrescriptionSearchPatientPractioner.setEditable(false);

		// Instruction Codes Dropdown
		cbPrescriptionSearchPatients.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = (String) cbPrescriptionSearchPatients.getSelectedItem();
				int index = cbPrescriptionSearchPatients.getSelectedIndex();
				if (index != -1) {
					Patient patient = patientMap.get(name);
					// if patient exists, set text to patient information.
					if (patient != null) {
						tfPrescriptionSearchPatientAddress.setText(patient.getAddress());
						tfPrescriptionSearchPatientPhone.setText(patient.getPhone());
						tfPrescriptionSearchPatientConditions.setText(patient.getMedicalConditions());
						tfPrescriptionSearchPatientPractioner.setText(patient.getPractioner().getName());
						getPrescriptionsByPatient(patient);
						System.out.println("prescriptionResult: " + prescriptionResultList);
						prescriptionTableModel.setData(prescriptionResultList);
					}
				} else {
					tfPrescriptionSearchPatientAddress.setText("");
					tfPrescriptionSearchPatientPhone.setText("");
					tfPrescriptionSearchPatientConditions.setText("");
					tfPrescriptionSearchPatientPractioner.setText("");
					prescriptionTableModel.setData(null);
				}
			}
		});
		cbPrescriptionSearchPatients.setEditable(true);
		cbPrescriptionSearchPatients.setPrototypeDisplayValue("Prototype Display Value Default");
		cbPrescriptionSearchPatients.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String filter = cbPrescriptionSearchPatients.getEditor().getItem().toString();
				filterPrescriptionPatients(filter);
			}
		});
		updatePrescriptionSearchPatients();
		cbPrescriptionSearchPatients.setSelectedIndex(-1);

		// graphical code for interface
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		int y = 0;

		gbc.gridx = 0;
		gbc.gridy = y++;
		searchPanel.add(new JLabel("Select Patient: "), gbc);
		gbc.gridx = 1;
		searchPanel.add(cbPrescriptionSearchPatients, gbc);
		gbc.gridx = 0;
		gbc.gridy = y++;
		searchPanel.add(new JLabel("Patient Address: "), gbc);
		gbc.gridx = 1;
		searchPanel.add(tfPrescriptionSearchPatientAddress, gbc);
		gbc.gridx = 0;
		gbc.gridy = y++;
		searchPanel.add(new JLabel("Patient Phone: "), gbc);
		gbc.gridx = 1;
		searchPanel.add(tfPrescriptionSearchPatientPhone, gbc);
		gbc.gridx = 0;
		gbc.gridy = y++;
		searchPanel.add(new JLabel("Patient Conditions: "), gbc);
		gbc.gridx = 1;
		searchPanel.add(tfPrescriptionSearchPatientConditions, gbc);
		gbc.gridx = 0;
		gbc.gridy = y++;
		searchPanel.add(new JLabel("Practioner: "), gbc);
		gbc.gridx = 1;
		searchPanel.add(tfPrescriptionSearchPatientPractioner, gbc);

		presciptionTable.setSize(100, 50);
		JScrollPane scrollPane = new JScrollPane(presciptionTable);
		// scrollPane.setSize(40, 50);

		gbc.gridx = 0;
		gbc.gridy = y++;
		gbc.gridwidth = 2;
		searchPanel.add(scrollPane, gbc);

		JButton btnClean = new JButton("Clear");
		btnClean.addActionListener(e -> clearPrescriptionSearchFields());
		gbc.gridx = 0;
		gbc.gridy = y++;
		searchPanel.add(btnClean, gbc);

	}

	// Introduction: Method deletes the selected patient.

	// Method: No parameters, the method just checks if the patient has
	// prescriptions or not.

	// Internal: No return value, if the patient has prescriptions, then that
	// patient cannot be deleted.
	private void deletePatient() {
		int index = cbPatientSearchPatients.getSelectedIndex();
		if (index == -1) {
			openErrorDialog("No selected patient");
		} else {
			String patientName = (String) cbPatientSearchPatients.getSelectedItem();
			getPrescriptionsByPatient(patientMap.get(patientName));
			if (prescriptionResultList.size() > 0) {
				openErrorDialog("Patient has prescriptions on file and cannot be deleted");
			} else {
				patientMap.remove(patientName);
				prescriptionPatientSet.remove(patientName);
				updatePatientDropdown(getPatientList());
				cbPatientSearchPatients.setSelectedIndex(-1);
				setPatientDropdownData();
			}
		}
	}

	// Introduction: This method gets a list of all the patients names.
	// Method: No parameters.
	// Internal: Return value is String ArrayList, an AL of patients.
	private ArrayList<String> getPatientList() {
		if (patientList == null) {
			patientList = new ArrayList<String>();
		} else {
			patientList.clear();
		}
		patientList.addAll(patientMap.keySet());
		return patientList;
	}

	// Introduction: This method filters through all patients, regardless of
	// prescriptions.

	// Method: The only parameter is the String filter, which should be the first 3
	// or more letters of a patient's name.

	// Internal: Nothing is returned. The prescription patient dropdown is
	// autofilled after typing the correct letters.
	private void filterPatients(String filter) {
		ArrayList<String> filteredPatients = new ArrayList<>();
		if (filter.length() >= 3) {
			for (String patient : patientList) {
				if (patient.toLowerCase().contains(filter.toLowerCase())) {
					filteredPatients.add(patient);
				}
			}
			updatePatientDropdown(filteredPatients);
		} else if (filter.length() == 0) {
			updatePatientDropdown(getPatientList());
			cbPatientSearchPatients.setSelectedIndex(-1);

		}
	}

	// This method updates the drop down menu accordingly with the selected
	// patient's information.
	private void updatePatientDropdown(ArrayList<String> patients) {
		if (patientSearchPatientDropdownModel != null) {
			patientSearchPatientDropdownModel.removeAllElements();
			for (String patient : patients) {
				patientSearchPatientDropdownModel.addElement(patient);
			}
		}

	}

	// Introduction: Method sets up the search panel for patients.

	// Method: The parameter is the JPanel searchPanel.

	// Internal: The method allows for you to make new patient records, and current
	// patient data will be displayed on the window.
	private void setupPatientSearchPanel(JPanel searchPanel) {
		patientSearchPatientDropdownModel = new DefaultComboBoxModel<>();
		cbPatientSearchPatients = new JComboBox<>(patientSearchPatientDropdownModel);

		// Prescription Fields
		tfPatientSearchPatientAddress = new JTextField();
		tfPatientSearchPatientAddress.setEditable(false);
		tfPatientSearchPatientCity = new JTextField();
		tfPatientSearchPatientCity.setEditable(false);
		tfPatientSearchPatientPostalCode = new JTextField();
		tfPatientSearchPatientPostalCode.setEditable(false);
		tfPatientSearchPatientPhone = new JTextField();
		tfPatientSearchPatientPhone.setEditable(false);
		tfPatientSearchPatientMedicalConditions = new JTextField();
		tfPatientSearchPatientMedicalConditions.setEditable(false);
		tfPatientSearchPatientPractioner = new JTextField();
		tfPatientSearchPatientPractioner.setEditable(false);
		tfPatientSearchPatientPractionerType = new JTextField();
		tfPatientSearchPatientPractionerType.setEditable(false);

		// Instruction Codes Dropdown
		cbPatientSearchPatients.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = (String) cbPatientSearchPatients.getSelectedItem();
				int index = cbPatientSearchPatients.getSelectedIndex();
				if (index != -1) {
					Patient patient = patientMap.get(name);
					if (patient != null && patient.getPractioner() != null) {
						tfPatientSearchPatientAddress.setText(patient.getAddress());
						tfPatientSearchPatientCity.setText(patient.getCity());
						tfPatientSearchPatientPostalCode.setText(patient.getPostalCode());
						tfPatientSearchPatientPhone.setText(patient.getPhone());
						tfPatientSearchPatientMedicalConditions.setText(patient.getMedicalConditions());
						tfPatientSearchPatientPractioner.setText(patient.getPractioner().getName());
						tfPatientSearchPatientPractionerType.setText(patient.getPractioner().getType());
					}
				} else {
					tfPatientSearchPatientAddress.setText("");
					tfPatientSearchPatientCity.setText("");
					tfPatientSearchPatientPostalCode.setText("");
					tfPatientSearchPatientPhone.setText("");
					tfPatientSearchPatientMedicalConditions.setText("");
					tfPatientSearchPatientPractioner.setText("");
					tfPatientSearchPatientPractionerType.setText("");
				}
			}
		});
		cbPatientSearchPatients.setEditable(true);
		cbPatientSearchPatients.setPrototypeDisplayValue("Prototype Display Value Default");
		cbPatientSearchPatients.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String filter = cbPatientSearchPatients.getEditor().getItem().toString();
				filterPatients(filter);
			}
		});
		for (String patient : getPatientList()) {
			patientSearchPatientDropdownModel.addElement(patient);
		}
		cbPatientSearchPatients.setSelectedIndex(-1);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		int y = 0;

		gbc.gridx = 0;
		gbc.gridy = y++;
		searchPanel.add(new JLabel("Select Patient: "), gbc);
		gbc.gridx = 1;
		searchPanel.add(cbPatientSearchPatients, gbc);
		gbc.gridx = 0;
		gbc.gridy = y++;
		searchPanel.add(new JLabel("Address: "), gbc);
		gbc.gridx = 1;
		searchPanel.add(tfPatientSearchPatientAddress, gbc);
		gbc.gridx = 0;
		gbc.gridy = y++;
		searchPanel.add(new JLabel("City: "), gbc);
		gbc.gridx = 1;
		searchPanel.add(tfPatientSearchPatientCity, gbc);
		gbc.gridx = 0;
		gbc.gridy = y++;
		searchPanel.add(new JLabel("Postal Code: "), gbc);
		gbc.gridx = 1;
		searchPanel.add(tfPatientSearchPatientPostalCode, gbc);
		gbc.gridx = 0;
		gbc.gridy = y++;
		searchPanel.add(new JLabel("Phone: "), gbc);
		gbc.gridx = 1;
		searchPanel.add(tfPatientSearchPatientPhone, gbc);
		gbc.gridx = 0;
		gbc.gridy = y++;
		searchPanel.add(new JLabel("Medical Conditions: "), gbc);
		gbc.gridx = 1;
		searchPanel.add(tfPatientSearchPatientMedicalConditions, gbc);
		gbc.gridx = 0;
		gbc.gridy = y++;
		searchPanel.add(new JLabel("Practioner: "), gbc);
		gbc.gridx = 1;
		searchPanel.add(tfPatientSearchPatientPractioner, gbc);
		gbc.gridx = 0;
		gbc.gridy = y++;
		searchPanel.add(new JLabel("Practioner Type: "), gbc);
		gbc.gridx = 1;
		searchPanel.add(tfPatientSearchPatientPractionerType, gbc);

		y += 2;
		gbc.gridy = y++;
		gbc.gridx = 1;
		JButton btnClean = new JButton("Delete");
		btnClean.addActionListener(e -> deletePatient());
		searchPanel.add(btnClean, gbc);
	}

	// Introduction: Method opens a search screen based on the searchType. There is
	// prescription and patient search.

	// Method: Parameter is int searchType, and this method will open the search
	// screen
	// corresponding to searchType.

	// Internal: There is no return value, and this method calls the
	// setupPrescriptionSearchPanel or the setupPatientSearchPanel methods based on
	// int searchType.
	private void openSearchScreen(int searchType) {
		SwingUtilities.invokeLater(() -> {
			searchFrame = new JDialog(frame, SEARCH_TITLE[searchType], true);
			searchFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			JPanel searchPanel = new JPanel(new GridBagLayout());

			switch (searchType) {
			case SEARCH_PRESCRIPTION:
				setupPrescriptionSearchPanel(searchPanel);
				break;
			case SEARCH_PATIENT:
				setupPatientSearchPanel(searchPanel);
				break;
			}

			searchFrame.add(searchPanel);
			searchFrame.setSize(800, 600);
			searchFrame.setVisible(true);

			// Bring the search frame to the front
			searchFrame.toFront();
			searchFrame.requestFocusInWindow();
		});
	}

	// Introduction: Method opens a report screen based on the searchType. There is
	// system summary and patient report.

	// Method: Parameter is int reportType, and this method will open the report
	// screen corresponding to reportType.

	// Internal: There is no return value, and this method uses StringBuilder to
	// display the information required on the report screen.
	private void openReportScreen(int reportType) {
		// Opening a report screen
		StringBuilder sb = new StringBuilder();
		if (reportType == 0) {
			// for reference, knowing all of the patients names, etc.
			sb.append("Name: ").append(" ".repeat(19)).append("Address: ").append(" ".repeat(21))
					.append("Phone Number: ").append(" ".repeat(16)).append("Practitioner: ").append("\n\n");
			for (Patient key : patientMap.values()) {
				sb.append(String.format("%-25s%-30s%-30s%-30s\n\n", key.getName(), key.getAddress(), key.getPhone(),
						key.getPractioner().getName() + ", " + key.getPractioner().getLicenseNumber()));

			}
			// invoke later allows the method to be called afterwards.
			SwingUtilities.invokeLater(() -> {
				JFrame reportFrame = new JFrame(REPORT_TITLE[reportType]);
				reportFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				Font COURIER = new Font("Courier New", Font.PLAIN, 13);

				JPanel titlePanel = new JPanel();

				JPanel mainPanel = new JPanel(new BorderLayout());

				JPanel reportPanel = new JPanel(new BorderLayout());

				JScrollPane reportScrollBar = new JScrollPane(reportPanel);

				JTextArea reportTextArea = new JTextArea(String.valueOf(sb), 1, 4);
				reportTextArea.setEditable(false);
				reportTextArea.setFont(COURIER);

				JLabel title = new JLabel(REPORT_TITLE[reportType]);
				title.setFont(COURIER_BOLD);
				titlePanel.add(title);

				mainPanel.add(titlePanel, BorderLayout.NORTH);

				reportPanel.add(reportTextArea);

				mainPanel.add(reportScrollBar);

				reportFrame.getContentPane().add(mainPanel);

				reportFrame.setSize(800, 800);
				reportFrame.setLocationRelativeTo(frame);

				// Set the search frame to be visible
				reportFrame.setVisible(true);

				// Bring the search frame to the front
				reportFrame.toFront();
				reportFrame.requestFocusInWindow();
			});
		} else if (reportType == 1) {
			endTime = System.currentTimeMillis();
			Date currentDate = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
			String cDate = dateFormat.format(currentDate);
			sb.append("\nNumber of patients: " + patientMap.values().size());
			sb.append("\n\nNumber of prescriptions: " + prescriptionMap.values().size());
			sb.append("\n\nSystem runtime: ").append(endTime - startTime).append(" ms");
			sb.append("\n\nFinished January 21, 2024: ICS4U Culminating");
			sb.append("\n\nCurrent Date: " + cDate);
			// invoke later allows the method to be called afterwards.
			SwingUtilities.invokeLater(() -> {
				JFrame reportFrame = new JFrame(REPORT_TITLE[reportType]);
				reportFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				Font COURIER = new Font("Courier", Font.PLAIN, 13);

				JPanel titlePanel = new JPanel();

				JPanel mainPanel = new JPanel(new BorderLayout());

				JPanel reportPanel = new JPanel(new BorderLayout());

				JScrollPane reportScrollBar = new JScrollPane(reportPanel);

				JTextArea reportTextArea = new JTextArea(String.valueOf(sb));
				reportTextArea.setEditable(false);
				reportTextArea.setFont(COURIER);

				JLabel title = new JLabel(REPORT_TITLE[reportType]);
				title.setFont(COURIER_BOLD);
				titlePanel.add(title);

				mainPanel.add(titlePanel, BorderLayout.NORTH);

				reportPanel.add(reportTextArea);

				mainPanel.add(reportScrollBar);

				reportFrame.getContentPane().add(mainPanel);

				reportFrame.setSize(800, 300);
				reportFrame.setLocationRelativeTo(frame);

				// Set the search frame to be visible
				reportFrame.setVisible(true);

				// Bring the search frame to the front
				reportFrame.toFront();
				reportFrame.requestFocusInWindow();
			});
		}
	}

	// Method opens a error dialog
	private void openErrorDialog(String errorMessage) {
		// Simulate opening a error screen
		JOptionPane.showMessageDialog(frame, errorMessage);
	}

	// Method opens the about dialog, which shows a hint on how the drag and drop
	// works.
	private void showAboutDialog() {
		// Create and show the About dialog
		JDialog aboutDialog = new JDialog(frame, "About", true);
		aboutDialog.setLayout(new BorderLayout());

		JLabel aboutLabel = new JLabel("Prescription App - Prescription Application");
		aboutLabel.setHorizontalAlignment(JLabel.CENTER);

		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				aboutDialog.dispose(); // Close the dialog
			}
		});

		aboutDialog.add(aboutLabel, BorderLayout.CENTER);
		aboutDialog.add(btnClose, BorderLayout.SOUTH);

		aboutDialog.setSize(300, 150);
		aboutDialog.setLocationRelativeTo(frame);
		aboutDialog.setVisible(true);
	}

	// Method gets the required instruction code from the instructionMap.
	private ArrayList<Map.Entry<String, String>> getInstructionCodeList() {
		if (instructionCodeList == null) {
			instructionCodeList = new ArrayList<>(instructionMap.entrySet());
		}
		return instructionCodeList;
	}

	// Method searches for instruction code based on filter string, or the first 2
	// letters of the instruction code
	private void filterInstructionCodes(String filter) {
		if (filter.length() >= 2) {
			ArrayList<Map.Entry<String, String>> filteredInstructionCode = new ArrayList<>();
			for (Map.Entry<String, String> code : instructionMap.entrySet()) {
				if (code.getKey().toLowerCase().contains(filter.toLowerCase())) {
					filteredInstructionCode.add(code);
				}
			}
			updateInstructionCodeDropdown(filteredInstructionCode);
		} else if (filter.length() == 0) {
			updateInstructionCodeDropdown(getInstructionCodeList());
			cbInstructionCodes.setSelectedIndex(-1);
		}
	}

	// Method autofills the instruction code on the graphic window
	private void updateInstructionCodeDropdown(ArrayList<Map.Entry<String, String>> codes) {
		instructionCodeDropdownModel.removeAllElements();
		for (Map.Entry<String, String> code : codes) {
			instructionCodeDropdownModel.addElement(code);
		}
	}

	// Introduction: Method sets up the prescription Panel, displays patient
	// information, and calculates drug charges.

	// Method: The parameter is the JPanel prescriptionPanel, where the method takes
	// in the prescriptionPanel and adds functions to it, like adding a new patient.

	// Internal: Nothing is returned, much of the code is graphical. Some of the
	// information displayed includes address, phone, and drug name.
	private void setupPrescriptionPanel(JPanel prescriptionPanel) {
		// Prescription Fields
		tfPatientAddress = new JTextField();
		tfPatientAddress.setEditable(false);
		tfPatientPhone = new JTextField();
		tfPatientPhone.setEditable(false);
		tfPatientConditions = new JTextField();
		tfPatientConditions.setEditable(false);
		tfPatientPractioner = new JTextField();
		tfPatientPractioner.setEditable(false);
		tfDrugName = new JTextField();
		tfDrugName.setEditable(false);
		instructionCodeDropdownModel = new DefaultComboBoxModel<>();
		cbInstructionCodes = new JComboBox<>(instructionCodeDropdownModel);
		ftfUnitPrice = new JFormattedTextField(amountFormat);
		ftfUnitPrice.setEditable(false);
		ftfQuantity = new JFormattedTextField(amountFormat);
		ftfCharges = new JFormattedTextField(amountFormat);
		tfDate = new JTextField();
		tfDate.setEditable(false);
		ftfRepeat = new JFormattedTextField(amountFormat);
		ftfCharges.setEditable(false);

		// Patient Dropdown
		cbPatients = new JComboBox<>();
		cbPatients.setEditable(false);
		setPatientDropdownData();
		cbPatients.setRenderer(new PatientComboBoxRenderer());
		cbPatients.setSelectedIndex(-1);
		cbPatients.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Patient patient = (Patient) cbPatients.getSelectedItem();
				int selectedIndex = cbPatients.getSelectedIndex();
				cbPatients.setBackground(null);
				System.out.println("cbPatients actionPerformed");
				if (selectedIndex == 0) {
					// Add new patient
					tfPatientAddress.setText("");
					tfPatientPhone.setText("");
					tfPatientConditions.setText("");
					tfPatientPractioner.setText("");
					addNewPatient();
					System.out.println("Add new patient");
				} else {
					if (patient != null) {
						tfPatientAddress.setText(patient.getAddress());
						tfPatientPhone.setText(patient.getPhone());
						tfPatientConditions.setText(patient.getMedicalConditions());
						tfPatientPractioner.setText(patient.getPractioner().getName());
					} else {
						tfPatientAddress.setText("");
						tfPatientPhone.setText("");
						tfPatientConditions.setText("");
						tfPatientPractioner.setText("");
					}
				}
			}
		});

		// Instruction Codes Dropdown
		cbInstructionCodes.setEditable(true);
		cbInstructionCodes.setPrototypeDisplayValue(new AbstractMap.SimpleEntry<>("Prototype Key", "Prototype Value"));
		cbInstructionCodes.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				cbInstructionCodes.setBackground(Color.WHITE);
				String filter = cbInstructionCodes.getEditor().getItem().toString();
				filterInstructionCodes(filter);
			}
		});
		// action listener for choosing instruction code
		cbInstructionCodes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cbInstructionCodes.setBackground(Color.WHITE);
			};
		});
		for (Map.Entry<String, String> code : instructionMap.entrySet()) {
			instructionCodeDropdownModel.addElement(code);
		}
		cbInstructionCodes.setSelectedIndex(-1);

		// Charges related Fields
		ftfQuantity.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				System.out.println("propertyChange: " + ftfQuantity.getValue() + " " + ftfUnitPrice.getValue());
				if (ftfQuantity.getValue() != null) {
					ftfQuantity.setBackground(Color.WHITE);
				}

				if (ftfQuantity.getValue() != null && ftfUnitPrice.getValue() != null) {
					double value = 0;
					if (ftfQuantity.getValue() instanceof Long) {
						value = (double) ((Long) ftfQuantity.getValue()).doubleValue()
								* (double) ftfUnitPrice.getValue();
					} else {
						value = (double) ((double) ftfQuantity.getValue() * (double) ftfUnitPrice.getValue());
					}
					ftfCharges.setValue(value);
				}
			}
		});

		ftfRepeat.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (ftfRepeat.getValue() != null) {
					ftfRepeat.setBackground(Color.WHITE);
				}
			}
		});

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		int y = 0;

		JPanel titlePanel = new JPanel(new GridLayout(1, 1));
		JLabel title = new JLabel("Prescription");
		title.setFont(COURIER_BOLD);
		titlePanel.add(title);

		gbc.gridx = 0;
		gbc.gridy = y++;
		prescriptionPanel.add(titlePanel);
		gbc.gridx = 0;
		gbc.gridy = y++;
		prescriptionPanel.add(new JLabel("Select Patient*: "), gbc);
		gbc.gridx = 1;
		prescriptionPanel.add(cbPatients, gbc);
		gbc.gridx = 0;
		gbc.gridy = y++;
		prescriptionPanel.add(new JLabel("Patient Address: "), gbc);
		gbc.gridx = 1;
		prescriptionPanel.add(tfPatientAddress, gbc);
		gbc.gridx = 0;
		gbc.gridy = y++;
		prescriptionPanel.add(new JLabel("Patient Phone: "), gbc);
		gbc.gridx = 1;
		prescriptionPanel.add(tfPatientPhone, gbc);
		gbc.gridx = 0;
		gbc.gridy = y++;
		prescriptionPanel.add(new JLabel("Patient Conditions: "), gbc);
		gbc.gridx = 1;
		prescriptionPanel.add(tfPatientConditions, gbc);
		gbc.gridx = 0;
		gbc.gridy = y++;
		prescriptionPanel.add(new JLabel("Practioner: "), gbc);
		gbc.gridx = 1;
		prescriptionPanel.add(tfPatientPractioner, gbc);
		gbc.gridx = 0;
		gbc.gridy = y++;
		prescriptionPanel.add(new JLabel("Drug Name*: "), gbc);
		gbc.gridx = 1;
		prescriptionPanel.add(tfDrugName, gbc);
		gbc.gridx = 0;
		gbc.gridy = y++;
		prescriptionPanel.add(new JLabel("Instruction: "), gbc);
		gbc.gridx = 1;
		prescriptionPanel.add(cbInstructionCodes, gbc);
		gbc.gridx = 0;
		gbc.gridy = y++;
		prescriptionPanel.add(new JLabel("Date: "), gbc);
		gbc.gridx = 1;
		prescriptionPanel.add(tfDate, gbc);
		gbc.gridx = 0;
		gbc.gridy = y++;
		prescriptionPanel.add(new JLabel("Quantity*: "), gbc);
		gbc.gridx = 1;
		prescriptionPanel.add(ftfQuantity, gbc);
		gbc.gridx = 0;
		gbc.gridy = y++;
		prescriptionPanel.add(new JLabel("Unit Price: "), gbc);
		gbc.gridx = 1;
		prescriptionPanel.add(ftfUnitPrice, gbc);
		gbc.gridx = 0;
		gbc.gridy = y++;
		prescriptionPanel.add(new JLabel("Repeat*: "), gbc);
		gbc.gridx = 1;
		prescriptionPanel.add(ftfRepeat, gbc);
		gbc.gridx = 0;
		gbc.gridy = y++;
		prescriptionPanel.add(new JLabel("Charges: "), gbc);
		gbc.gridx = 1;
		prescriptionPanel.add(ftfCharges, gbc);
		gbc.gridx = 0;
		gbc.gridy = y++;
		JLabel requiredLabel = new JLabel("* - required");
		requiredLabel.setForeground(Color.RED);
		prescriptionPanel.add(requiredLabel, gbc);

		JButton btnClean = new JButton("Clear");
		btnClean.addActionListener(e -> clearPrescriptionFields());

		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(e -> savePrescription());

		gbc.gridx = 0;
		gbc.gridy = y++;
		prescriptionPanel.add(btnClean, gbc);
		gbc.gridx = 1;
		prescriptionPanel.add(btnSave, gbc);

	}

	// Introduction: This method adds a new patient to the grpahical screen, and
	// calls the actual save patient method, where a new patient is saved to the
	// patient map.

	// Method: There are no parameters, after making the new patient, there is an
	// option to save or clear the current information.

	// Internal: Nothing is returned, most of the code is graphical, made with
	// gridbaglayout.
	private void addNewPatient() {
		JDialog patientFrame = new JDialog(frame, "Add New Patient", true);
		patientFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel patientPanel = new JPanel(new GridBagLayout());

		// New Patient Fields
		tfNewPatientName = new JTextField();
		tfNewPatientPhone = new JTextField();
		tfNewPatientAddress = new JTextField();
		tfNewPatientCity = new JTextField();
		tfNewPatientPostalCode = new JTextField();
		tfNewPatientMedicalConditions = new JTextField();
		tfNewPatientPractionerLicenseNumber = new JTextField();
		tfNewPatientPractionerLicenseNumber.setEditable(false);
		tfNewPatientPractionerType = new JTextField();
		tfNewPatientPractionerType.setEditable(false);
		tfNewPatientPractionerPhone = new JTextField();
		tfNewPatientPractionerPhone.setEditable(false);

		tfNewPatientName.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				if (tfNewPatientName.getText().length() > 0) {
					tfNewPatientName.setBackground(Color.WHITE);
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				if (tfNewPatientName.getText().length() > 0) {
					tfNewPatientName.setBackground(Color.WHITE);
				}
			}

		});
		tfNewPatientPhone.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				if (tfNewPatientPhone.getText().length() > 0) {
					tfNewPatientPhone.setBackground(Color.WHITE);
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				if (tfNewPatientPhone.getText().length() > 0) {
					tfNewPatientPhone.setBackground(Color.WHITE);
				}
			}
		});
		tfNewPatientAddress.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				if (tfNewPatientAddress.getText().length() > 0) {
					tfNewPatientAddress.setBackground(Color.WHITE);
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				if (tfNewPatientAddress.getText().length() > 0) {
					tfNewPatientAddress.setBackground(Color.WHITE);
				}
			}
		});
		tfNewPatientCity.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				if (tfNewPatientCity.getText().length() > 0) {
					tfNewPatientCity.setBackground(Color.WHITE);
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				if (tfNewPatientCity.getText().length() > 0) {
					tfNewPatientCity.setBackground(Color.WHITE);
				}
			}
		});
		tfNewPatientPostalCode.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				if (tfNewPatientPostalCode.getText().length() > 0) {
					tfNewPatientPostalCode.setBackground(Color.WHITE);
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				if (tfNewPatientPostalCode.getText().length() > 0) {
					tfNewPatientPostalCode.setBackground(Color.WHITE);
				}
			}
		});

		cbNewPatientPractioners = new JComboBox<>();
		cbNewPatientPractioners.setEditable(false);
		for (Practioner practioner : practionerMap.values()) {
			cbNewPatientPractioners.addItem(practioner);
		}
		cbNewPatientPractioners.setRenderer(new PractionerComboBoxRenderer());
		cbNewPatientPractioners.setSelectedIndex(-1);
		cbNewPatientPractioners.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Practioner practioner = (Practioner) cbNewPatientPractioners.getSelectedItem();
				cbNewPatientPractioners.setBackground(Color.WHITE);
				if (practioner != null) {
					tfNewPatientPractionerLicenseNumber.setText(practioner.getLicenseNumber());
					tfNewPatientPractionerType.setText(practioner.getType());
					tfNewPatientPractionerPhone.setText(practioner.getPhone());
				} else {
					tfNewPatientPractionerLicenseNumber.setText("");
					tfNewPatientPractionerType.setText("");
					tfNewPatientPractionerPhone.setText("");
				}
			}
		});

		cbNewPatientProvinces = new JComboBox<>(PROVINCES);
		cbNewPatientProvinces.setSelectedIndex(-1);
		cbNewPatientProvinces.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cbNewPatientProvinces.setBackground(Color.WHITE);
			}
		});

		// graphical code
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		int y = 0;

		JPanel titlePanel = new JPanel(new GridLayout(1, 1));
		JLabel title = new JLabel("New Patient");
		title.setFont(COURIER_BOLD);
		titlePanel.add(title);

		gbc.gridx = 0;
		gbc.gridy = y++;
		patientPanel.add(titlePanel);
		gbc.gridx = 0;
		gbc.gridy = y++;
		patientPanel.add(new JLabel("Name*: "), gbc);
		gbc.gridx = 1;
		patientPanel.add(tfNewPatientName, gbc);
		gbc.gridx = 0;
		gbc.gridy = y++;
		patientPanel.add(new JLabel("Address*: "), gbc);
		gbc.gridx = 1;
		patientPanel.add(tfNewPatientAddress, gbc);
		gbc.gridx = 0;
		gbc.gridy = y++;
		patientPanel.add(new JLabel("City*: "), gbc);
		gbc.gridx = 1;
		patientPanel.add(tfNewPatientCity, gbc);
		gbc.gridx = 0;
		gbc.gridy = y++;
		patientPanel.add(new JLabel("Province*: "), gbc);
		gbc.gridx = 1;
		patientPanel.add(cbNewPatientProvinces, gbc);
		gbc.gridx = 0;
		gbc.gridy = y++;
		patientPanel.add(new JLabel("Postal Code*: "), gbc);
		gbc.gridx = 1;
		patientPanel.add(tfNewPatientPostalCode, gbc);
		gbc.gridx = 0;
		gbc.gridy = y++;
		patientPanel.add(new JLabel("Phone*: "), gbc);
		gbc.gridx = 1;
		patientPanel.add(tfNewPatientPhone, gbc);
		gbc.gridx = 0;
		gbc.gridy = y++;
		patientPanel.add(new JLabel("Medical Conditions: "), gbc);
		gbc.gridx = 1;
		patientPanel.add(tfNewPatientMedicalConditions, gbc);
		gbc.gridx = 0;
		gbc.gridy = y++;
		patientPanel.add(new JLabel("Practioner*: "), gbc);
		gbc.gridx = 1;
		patientPanel.add(cbNewPatientPractioners, gbc);
		gbc.gridx = 0;
		gbc.gridy = y++;
		patientPanel.add(new JLabel("Practioner License: "), gbc);
		gbc.gridx = 1;
		patientPanel.add(tfNewPatientPractionerLicenseNumber, gbc);
		gbc.gridx = 0;
		gbc.gridy = y++;
		patientPanel.add(new JLabel("Practioner Type: "), gbc);
		gbc.gridx = 1;
		patientPanel.add(tfNewPatientPractionerType, gbc);
		gbc.gridx = 0;
		gbc.gridy = y++;
		patientPanel.add(new JLabel("Practioner Phone: "), gbc);
		gbc.gridx = 1;
		patientPanel.add(tfNewPatientPractionerPhone, gbc);
		gbc.gridx = 0;
		gbc.gridy = y++;
		JLabel requiredLabel = new JLabel("* - required");
		requiredLabel.setForeground(Color.RED);
		patientPanel.add(requiredLabel, gbc);

		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(e -> clearNewPatientFields());

		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(e -> saveNewPatient(patientFrame));

		gbc.gridx = 0;
		gbc.gridy = y++;
		patientPanel.add(btnClear, gbc);
		gbc.gridx = 1;
		patientPanel.add(btnSave, gbc);

		patientFrame.add(patientPanel);
		patientFrame.setSize(600, 350);
		patientFrame.setVisible(true);
	}

	// Method shows drug details, including brand name, chemical name, etc.
	private void showDrugDetails(Drug drug) {
		tfBrandName.setText(drug.getBrandName());
		tfDin.setText(drug.getDin());
		tfChemicalName.setText(drug.getChemicalName());
		tfManufacturer.setText(drug.getManufacturer());
		tfDosageForm.setText(drug.getDosageForm());
		tfAppearance.setText(drug.getAppearance());
		tfStrength.setText(drug.getStrength());
		tfOtherAvailableStrengths.setText(drug.getOtherAvailableStrengths());
		tfIndication.setText(drug.getIndication());

		drugImage.setIcon(drugImages.get(drug.getBrandName()));
	}

	// Drug List TransferHandler, code for drag and drop
	private class DrugListTransferHandler extends TransferHandler {
		@Override
		protected Transferable createTransferable(JComponent c) {
			JList<Drug> source = (JList<Drug>) c;
			Drug selectedDrug = source.getSelectedValue();
			return new StringSelection(selectedDrug.getBrandName());
		}

		@Override
		public int getSourceActions(JComponent c) {
			return TransferHandler.COPY;
		}
	}

	// Prescription Panel DropTarget
	private class PrescriptionPanelDropTarget extends DropTargetAdapter {
		@Override
		public void drop(DropTargetDropEvent event) {
			Transferable transferable = event.getTransferable();

			try {
				if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
					event.acceptDrop(DnDConstants.ACTION_COPY);
					String drugName = (String) transferable.getTransferData(DataFlavor.stringFlavor);

					// Pass drugName to the prescription screen
					processPrescriptionDrug(drugName);

					event.dropComplete(true);
				} else {
					event.rejectDrop();
				}
			} catch (Exception e) {
				e.printStackTrace();
				event.rejectDrop();
			}
		}
	}

	// This method ensures that after adding a new patient, the combobox is cleared.
	private void setPatientDropdownData() {
		ActionListener listeners[] = cbPatients.getActionListeners();
		ActionListener listener = null;
		// Code disables ActionListener for cbPatients before data refresh
		if (listeners.length > 0) {
			listener = listeners[0];
		}

		if (listener != null) {
			cbPatients.removeActionListener(listener);
		}

		cbPatients.removeAllItems();
		Patient defaultPatient = new Patient("Add New Patient");
		cbPatients.addItem(defaultPatient);
		for (Patient patient : patientMap.values()) {
			cbPatients.addItem(patient);
		}
		cbPatients.setSelectedIndex(-1);

		// Code re-enables ActionListener for cbPatients
		if (listener != null) {
			cbPatients.addActionListener(listener);
		}
	}

	// Method sets the drug prices back to default.
	private void processPrescriptionDrug(String drugName) {
		Drug drug = drugMap.get(drugName);
		tfDrugName.setText(drugName);
		tfDrugName.setBackground(null);
		ftfUnitPrice.setValue(drug.getUnitPrice());
		ftfQuantity.setValue(0.0d);
		tfDate.setText(dateFormat.format(new Date()));
		ftfRepeat.setValue(0L);
		prescriptionPanel.repaint();

		// Dummy method to process the drugName in the prescription screen
		System.out.println("Drug ID added to prescription: " + drug);
	}

	// Method clears the prescription fields on the graphics window.
	private void clearPrescriptionFields() {
		cbPatients.setSelectedIndex(-1);
		cbPatients.setBackground(null);
		tfDrugName.setText("");
		tfDrugName.setBackground(null);
		cbInstructionCodes.setSelectedIndex(-1);
		cbInstructionCodes.setBackground(Color.WHITE);
		ftfQuantity.setText("");
		ftfQuantity.setBackground(Color.WHITE);
		ftfUnitPrice.setText("");
		tfDate.setText("");
		ftfRepeat.setText("");
		ftfRepeat.setBackground(Color.WHITE);
		ftfCharges.setText("");
	}

	// Introduction: Method saves prescription data to prescriptionMap.
	// Method: There are no parameters, most of the code includes error checking,
	// and the graphical depiction of the error check.
	// Internal: No return values, if there are any invalid inputs, the textFields
	// will turn pink, and an error message will pop up.
	private void savePrescription() {
		boolean valid = true;
		String errorTitle = "Prescription Validation Error";
		StringBuilder errorMessages = new StringBuilder(errorTitle).append("\n");

		if (cbPatients.getSelectedIndex() == -1) {
			valid = false;
			errorMessages.append("- Patient must be selected\n");
			cbPatients.setBackground(Color.PINK);
		}
		if (tfDrugName.getText().length() == 0) {
			valid = false;
			errorMessages.append("- Drug Name must be populated\n");
			tfDrugName.setBackground(Color.PINK);
		}
		if (cbInstructionCodes.getSelectedIndex() == -1) {
			valid = false;
			errorMessages.append("- Instruction must be selected\n");
			cbInstructionCodes.setBackground(Color.PINK);
		}
		if (!(ftfQuantity.getValue() instanceof Long) && !(ftfQuantity.getValue() instanceof Double)
				&& !(ftfQuantity.getValue() instanceof Integer)) {
			valid = false;
			errorMessages.append("- Quantity must be numeric\n");
			ftfQuantity.setBackground(Color.PINK);
		} else {
			double quantity;
			if (ftfQuantity.getValue() instanceof Long) {
				quantity = ((Long) ftfQuantity.getValue()).doubleValue();
			} else {
				quantity = (double) ftfQuantity.getValue();
			}
			if (quantity <= 0) {
				valid = false;
				errorMessages.append("- Quantity must be greater than 0\n");
				ftfQuantity.setBackground(Color.PINK);
			}

		}
		if (!(ftfRepeat.getValue() instanceof Long)) {
			valid = false;
			errorMessages.append("- Repeat must be a whole number\n");
			ftfRepeat.setBackground(Color.PINK);
		} else {
			long repeat = ((Long) ftfRepeat.getValue()).longValue();
			if (repeat < 0) {
				valid = false;
				errorMessages.append("- Repeat must be positive\n");
				ftfQuantity.setBackground(Color.PINK);
			}
		}

		if (valid) {
			String prescriptionId = String.valueOf(System.currentTimeMillis());
			Patient patient = (Patient) cbPatients.getSelectedItem();
			Drug drug = drugMap.get(tfDrugName.getText());
			String instructionCode = (String) ((Map.Entry<String, String>) cbInstructionCodes.getSelectedItem())
					.getKey();
			System.out.println("quantity: " + ftfQuantity.getValue());
			double quantity = 0;
			if (ftfQuantity.getValue() instanceof Long) {
				quantity = ((Long) ftfQuantity.getValue()).doubleValue();
			} else {
				quantity = (double) ftfQuantity.getValue();
			}
			double charges = (double) ftfCharges.getValue();
			long repeat = 0;
			repeat = (Long) ftfRepeat.getValue();

			Date date = null;
			try {
				date = dateFormat.parse((String) tfDate.getText());
			} catch (ParseException e) {
				e.printStackTrace();
			}

			Prescription prescription = new Prescription(prescriptionId, instructionCode, patient, drug, date, quantity,
					repeat, charges);

			// Implement your logic to save prescription data
			System.out.println("Prescription: " + prescription);

			if (prescriptionMap == null) {
				prescriptionMap = new TreeMap<String, Prescription>();
			}
			prescriptionMap.put(prescriptionId, prescription);

			// Update source data for prescription patient search dropdown
			prescriptionPatientSet.add(patient.getName());

			// Clear the form after saving
			clearPrescriptionFields();
		} else {
			showErrorDialog(errorTitle, errorMessages.toString());
		}

	}

	// Introduction: Method creates an error dialog and displays it on the screen.
	// Method: The parameters are the String title, and String errorMessages.
	// Internal: There are no return values, much of the code is graphical.
	private void showErrorDialog(String title, String errorMessages) {
		// Create and show the Error dialog
		JDialog errorDialog = new JDialog(frame, title, true);
		errorDialog.setLayout(new BorderLayout());

		JTextArea taErrorMessages = new JTextArea();
		taErrorMessages.setMargin(new Insets(2, 20, 2, 2));
		taErrorMessages.setText(errorMessages);
		taErrorMessages.setEditable(false);

		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				errorDialog.dispose(); // Close the dialog
			}
		});

		errorDialog.add(taErrorMessages, BorderLayout.CENTER);
		errorDialog.add(btnClose, BorderLayout.SOUTH);

		errorDialog.setSize(350, 250);
		errorDialog.setLocationRelativeTo(frame);
		errorDialog.setResizable(false);
		errorDialog.setVisible(true);
	}

	// Method clears the patient fields, and makes text field background white.
	private void clearNewPatientFields() {
		tfNewPatientName.setText("");
		tfNewPatientName.setBackground(Color.WHITE);
		tfNewPatientPhone.setText("");
		tfNewPatientPhone.setBackground(Color.WHITE);
		tfNewPatientAddress.setText("");
		tfNewPatientAddress.setBackground(Color.WHITE);
		tfNewPatientCity.setText("");
		tfNewPatientCity.setBackground(Color.WHITE);
		tfNewPatientPostalCode.setText("");
		tfNewPatientPostalCode.setBackground(Color.WHITE);
		tfNewPatientMedicalConditions.setText("");
		tfNewPatientPractionerLicenseNumber.setText("");
		tfNewPatientPractionerType.setText("");
		tfNewPatientPractionerPhone.setText("");
		cbNewPatientPractioners.setSelectedIndex(-1);
		cbNewPatientPractioners.setBackground(Color.WHITE);
		cbNewPatientProvinces.setSelectedIndex(-1);
		cbNewPatientProvinces.setBackground(Color.WHITE);
	}

	// Introduction: Method checks if the phone number input is valid.
	// Method: The parameter is the phone number in the form of a String.
	// Internal: The return value is a boolean, indicating whether or not the phone
	// number is valid.
	private static boolean isPhoneNumberValid(String phone) {
		// Allow phone number with the following formats:
		// 9876554321
		// (412) 221-2029 space is optional after )
		// 123-345-5789
		String regex = "^(\\d{10}|\\(\\d{3}\\) ?\\d{3}-\\d{4}|\\d{3}-\\d{3}-\\d{4})$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(phone);
		return matcher.matches();
	}

	// Introduction: Method checks if the postal code input is valid.
	// Method: The parameter is the postal code in the form of a String.
	// Internal: The return value is a boolean, indicating whether or not the postal
	// code is valid.
	private static boolean isPostalCodeValid(String postalCode) {
		// e.g M1X2J3 or M1X 2J3 case insensitive
		String regex = "^[A-Za-z]\\d[A-Za-z] ?\\d[A-Za-z]\\d$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(postalCode);
		return matcher.matches();
	}

	// Method saves new patient, parameter is the JDialog patientFrame, and new
	// patient is saved in patientMap.
	// Error check for saving new patient is also located here
	private void saveNewPatient(JDialog patientFrame) {
		boolean valid = true;
		String errorTitle = "Patient Validation Error";
		StringBuilder errorMessages = new StringBuilder(errorTitle).append("\n");

		Practioner practioner = (Practioner) cbNewPatientPractioners.getSelectedItem();
		// Error check code
		if (tfNewPatientName.getText().length() == 0) {
			valid = false;
			errorMessages.append("- Name must not be empty\n");
			tfNewPatientName.setBackground(Color.PINK);
		}
		if (tfNewPatientAddress.getText().length() == 0) {
			valid = false;
			errorMessages.append("- Address must not be empty\n");
			tfNewPatientAddress.setBackground(Color.PINK);
		}
		if (tfNewPatientCity.getText().length() == 0) {
			valid = false;
			errorMessages.append("- Address must not be empty\n");
			tfNewPatientCity.setBackground(Color.PINK);
		}
		if (cbNewPatientProvinces.getSelectedIndex() == -1) {
			valid = false;
			errorMessages.append("- Province must be selected\n");
			cbNewPatientProvinces.setBackground(Color.PINK);
		}
		if (tfNewPatientPostalCode.getText().length() == 0) {
			valid = false;
			errorMessages.append("- Postal Code must not be empty\n");
			tfNewPatientPostalCode.setBackground(Color.PINK);
		} else {
			if (!isPostalCodeValid(tfNewPatientPostalCode.getText())) {
				valid = false;
				errorMessages.append("- Postal Code has invalid format\n");
				errorMessages.append("   e.g. X9X9X9 or X9X 9X9\n");
				tfNewPatientPostalCode.setBackground(Color.PINK);
			}
		}
		if (tfNewPatientPhone.getText().length() == 0) {
			valid = false;
			errorMessages.append("- Phone must not be empty\n");
			tfNewPatientPhone.setBackground(Color.PINK);
		} else {
			if (!isPhoneNumberValid(tfNewPatientPhone.getText())) {
				valid = false;
				errorMessages.append("- Phone has invalid format\n");
				errorMessages.append("   e.g. 9991234567 or 999-123-4567 or (999) 123-4567\n");
				tfNewPatientPhone.setBackground(Color.PINK);
			}
		}
		if (cbNewPatientPractioners.getSelectedIndex() == -1) {
			valid = false;
			errorMessages.append("- Practitioner must be selected\n");
			cbNewPatientPractioners.setBackground(Color.PINK);
		}

		if (valid) {
			// if input is valid
			String name = tfNewPatientName.getText();
			String phone = tfNewPatientPhone.getText();
			String address = tfNewPatientAddress.getText();
			String city = tfNewPatientCity.getText();
			String province = (String) cbNewPatientProvinces.getSelectedItem();
			String postalCode = tfNewPatientPostalCode.getText();
			String medicalConditions = tfNewPatientMedicalConditions.getText();

			Date date = new Date();
			Patient patient = new Patient(name, phone, address, city, province, postalCode, medicalConditions,
					practioner, date);
			// Implement your logic to save prescription data
			System.out.println("New Patient: " + patient);

			if (patientMap == null) {
				patientMap = new TreeMap<String, Patient>();
			}
			patientMap.put(name, patient);
			setPatientDropdownData();

			cbPatients.setSelectedItem(patient);
			updatePatientComboBox(cbPatients);

			updatePatientDropdown(getPatientList());

			patientFrame.dispose();
		} else {
			showErrorDialog(errorTitle, errorMessages.toString());
		}
	}

	// The methods with extends DefaultListCellRenderer are overridden methods, for
	// drugList, patient comboBox and practitioner comboBox.
	private static class DrugListRenderer extends DefaultListCellRenderer {
		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			if (value instanceof Drug) {
				setText(((Drug) value).getBrandName());
			}

			return this;
		}
	}

	private static class PatientComboBoxRenderer extends DefaultListCellRenderer {
		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			if (value instanceof Patient) {
				setText(((Patient) value).getName());
			}

			return this;
		}
	}

	private static class PractionerComboBoxRenderer extends DefaultListCellRenderer {
		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			if (value instanceof Practioner) {
				setText(((Practioner) value).getName());
			}

			return this;
		}
	}

	// This method updates the patient combo box.
	private static void updatePatientComboBox(JComboBox<Patient> comboBox) {
		// Manually fire an ActionEvent on the JComboBox to trigger its ActionListener
		ActionListener[] actionListeners = comboBox.getActionListeners();
		for (ActionListener listener : actionListeners) {
			// Create a dummy ActionEvent and call the actionPerformed method
			listener.actionPerformed(new ActionEvent(comboBox, ActionEvent.ACTION_PERFORMED, "UpdateEvent"));
		}
	}

	// Introduction: Method saves the data when the program is being shut down using
	// ObjectOutputStream.

	// Method: There are no parameters, the only purpose of this method is to save
	// the information from the maps to the binary files.

	// Internal: No return values.
	private void performExitTask() {
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(PATIENTS_FILE_PATH));
			oos.writeObject(patientMap);
			oos.close();

			oos = new ObjectOutputStream(new FileOutputStream(PRESCRIPTIONS_FILE_PATH));
			oos.writeObject(prescriptionMap);
			oos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// This method runs the main screen window frame.
	private void runMain() {
		frame = new JFrame("Prescription App");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1400, 600);
		// Add a WindowListener to handle the window closing event
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				performExitTask();
				// Close the application
				System.exit(0);
			}
		});

		// Menu Bar
		setupMenuBar(frame);

		// Left Panel
		JPanel leftPanel = new JPanel(new GridLayout(1, 2));
		leftPanel.createToolTip();
		setupDrugList(leftPanel);

		// Right Panel
		prescriptionPanel = new JPanel(new GridBagLayout());
		setupPrescriptionPanel(prescriptionPanel);

		// Enable drag and drop for the drug list
		drugList.setDragEnabled(true);
		drugList.setTransferHandler(new DrugListTransferHandler());

		// Enable drop for the prescription panel
		prescriptionPanel.setDropTarget(new DropTarget(prescriptionPanel, new PrescriptionPanelDropTarget()));

		// Main Panel
		JPanel mainPanel = new JPanel(new GridLayout(1, 2));
		mainPanel.add(leftPanel);
		mainPanel.add(prescriptionPanel);

		frame.add(mainPanel);
		frame.setVisible(true);
	}

	// Constructor
	public MainApp() {
		// Load all required data for the app
		loadData();

		String IMAGE_PATH = "images/rx.png";
		JFrame mainScreenFrame = new JFrame("Main Screen");

		mainScreenFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainScreenFrame.setSize(600, 600);
		mainScreenFrame.setResizable(false);

		// Create a JLabel for the background image
		ImageIcon rxIcon = new ImageIcon(IMAGE_PATH);
		Image newImage = rxIcon.getImage().getScaledInstance(600, 600, Image.SCALE_SMOOTH);
		ImageIcon backgroundImage = new ImageIcon(newImage); // Replace with the actual path to your image

		JLabel backgroundLabel = new JLabel(backgroundImage);
		mainScreenFrame.setContentPane(backgroundLabel);

		// Layout manager of content pane set to null to allow absolute positioning
		mainScreenFrame.setLayout(null);

		// Create components
		JLabel label = new JLabel("Prescription Management System", SwingConstants.CENTER);
		label.setFont(new Font("Arial", Font.BOLD, 30)); // Set a larger font for the label

		JButton systemButton = new JButton("Start");
		systemButton.addActionListener(e -> runMain());
		systemButton.setPreferredSize(new Dimension(100, 40));

		JButton aboutButton = new JButton("Help");
		aboutButton.addActionListener(e -> showDrugHelpDialog());
		aboutButton.setPreferredSize(new Dimension(100, 40));

		// Set layout manager for the content pane
		mainScreenFrame.setLayout(new GridBagLayout());

		// Create constraints for the label
		GridBagConstraints labelConstraints = new GridBagConstraints();
		labelConstraints.gridx = 0;
		labelConstraints.gridy = 0;

		// Create constraints for the button panel
		GridBagConstraints buttonPanelConstraints = new GridBagConstraints();
		buttonPanelConstraints.gridx = 0;
		buttonPanelConstraints.gridy = 1;
		buttonPanelConstraints.insets = new Insets(390, 0, 0, 0); // Add space above the button panel

		// Add components to the content pane with constraints
		mainScreenFrame.add(label, labelConstraints);

		// Create a JPanel for the buttons and set its layout to FlowLayout with center
		// alignment
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		Color betterBlue = new Color(4, 46, 81);
		buttonPanel.setBackground(betterBlue);
		buttonPanel.add(systemButton);
		buttonPanel.add(aboutButton);

		// Add the button panel to the content pane with constraints
		mainScreenFrame.add(buttonPanel, buttonPanelConstraints);
		mainScreenFrame.setVisible(true);

	}

	// main
	public static void main(String[] args) {
		// calls constructor
		new MainApp();
	}
}
