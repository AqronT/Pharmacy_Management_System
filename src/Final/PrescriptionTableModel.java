package Final;

import java.util.List;

import javax.swing.table.AbstractTableModel;

// prescription table class
public class PrescriptionTableModel extends AbstractTableModel {
	private List<Prescription> prescriptionList;
	private String[] columnNames = { "ID", "Drug Name", "Strength", "Quantity", "Repeat", "Charges" };

	// method gets the number of rows in the PrescriptionTable.
	@Override
	public int getRowCount() {
		if (prescriptionList == null) {
			return 0;
		}
		return prescriptionList.size();
	}

	// method gets the number of columns in the PrescriptionTable.
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	// Introduction: Method gets the value at a certain (row, column) index.
	// Method: Parameters include the rowIndex and columnIndex as ints.
	// Internal: An object is returned, which can be any of the prescription
	// attributes.
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Prescription prescription = prescriptionList.get(rowIndex);

		// different cases get different prescription attributes.
		switch (columnIndex) {
		case 0:
			return prescription.getPrescriptionId();
		case 1:
			return prescription.getDrug().getBrandName();
		case 2:
			return prescription.getDrug().getStrength();
		case 3:
			return prescription.getQuantity();
		case 4:
			return prescription.getRepeat();
		case 5:
			return prescription.getCharges();
		default:
			return null; // Handle additional columns if needed
		}
	}

	// get name of column, which is a parameter in the form of an int.
	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	// instantiates prescriptionList
	public PrescriptionTableModel(List<Prescription> prescriptionList) {
		this.prescriptionList = prescriptionList;
	}

	// setData updates the data in the table
	public void setData(List<Prescription> prescriptionList) {
		this.prescriptionList = prescriptionList;
		fireTableDataChanged(); // Notify the table that the data has changed
	}
}
