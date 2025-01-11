package Final;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

// Drug class
public class Drug implements Serializable {
	// class variables
	private static final long serialVersionUID = 1L;

	// Drug object attributes, instance variables
	private String brandName;
	private String din;
	private String chemicalName;
	private String manufacturer;
	private String dosageForm;
	private String appearance;
	private String strength;
	private String otherAvailableStrengths;
	private String indication;
	private double unitPrice;
	private String imagePath;

	// constructor
	public Drug(String brandName, String din, String chemicalName, String manufacturer, String dosageForm,
			String appearance, String strength, String otherAvailableStrengths, String indication, double unitPrice) {
		this.brandName = brandName;
		this.din = din;
		this.chemicalName = chemicalName;
		this.manufacturer = manufacturer;
		this.dosageForm = dosageForm;
		this.appearance = appearance;
		this.strength = strength;
		this.otherAvailableStrengths = otherAvailableStrengths;
		this.indication = indication;
		this.unitPrice = unitPrice;
		this.imagePath = "images/" + brandName + ".png";
	}

	// getters and setters
	public String getBrandName() {
		return this.brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getDin() {
		return this.din;
	}

	public void setDin(String din) {
		this.din = din;
	}

	public String getChemicalName() {
		return this.chemicalName;
	}

	public void setChemicalName(String chemicalName) {
		this.chemicalName = chemicalName;
	}

	public String getManufacturer() {
		return this.manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getDosageForm() {
		return this.dosageForm;
	}

	public void setDosageForm(String dosageForm) {
		this.dosageForm = dosageForm;
	}

	public String getAppearance() {
		return this.appearance;
	}

	public void setAppearance(String appearance) {
		this.appearance = appearance;
	}

	public String getStrength() {
		return this.strength;
	}

	public void setStrength(String strength) {
		this.strength = strength;
	}

	public String getOtherAvailableStrengths() {
		return this.otherAvailableStrengths;
	}

	public void setOtherAvailableStrengths(String otherAvailableStrengths) {
		this.otherAvailableStrengths = otherAvailableStrengths;
	}

	public String getIndication() {
		return this.indication;
	}

	public void setIndication(String indication) {
		this.indication = indication;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	// toString method, when printing out Drug object
	@Override
	public String toString() {
		return "Drug [brandName=" + brandName + ", din=" + din + ", chemicalName=" + chemicalName + ", manufacturer="
				+ manufacturer + ", dosageForm=" + dosageForm + ", appearance=" + appearance + ", strength=" + strength
				+ ", otherAvailableStrengths=" + otherAvailableStrengths + ", indication=" + indication + ", unitPrice="
				+ unitPrice + ", imagePath=" + imagePath + "]";
	}

	public String getImagePath() {
		return this.imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	// This method reads in the drug file path as a String, and splits the
	// information into a String array. The values in the array are the attributes
	// of the drug.
	public static void readFromCSV() {
		String FILE_PATH = "data/drug-list.csv";
		TreeMap<String, Drug> drugMap = new TreeMap<String, Drug>();
		try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				// Assuming the order is: brandName, chemicalName, strength, din,
				// dosageForm, appearance, otherAvailableStrengths, manufacturer, indication.
				String brandName = values[0].trim();
				String chemicalName = values[1].trim();
				String strength = values[2].trim();
				String din = values[3].trim();
				String dosageForm = values[4].trim();
				String appearance = values[5].trim();
				String otherAvailableStre = values[6].trim();
				String manufacturer = values[7].trim();
				String indication = values[8].trim();
				double unitPrice = Double.parseDouble(values[9].trim());

				Drug drug = new Drug(brandName, din, chemicalName, manufacturer, dosageForm, appearance, strength,
						otherAvailableStre, indication, unitPrice);
				drugMap.put(brandName, drug);
			}

			System.out.println(drugMap.toString());

			// ObjectOutputStream writes the Drug object into the drugMap.
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data/drug-list.dat"));
			oos.writeObject(drugMap);
			oos.close();
			br.close();
		} catch (IOException | NumberFormatException e) {
			e.printStackTrace();
		}
	}

	// Method reads a file and returns a TreeMap, in this case, it is the TreeMap.
	public static Map<String, Drug> readObjectsFromFile() {
		TreeMap<String, Drug> drugMap = null;
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data/drug-list.dat"))) {
			drugMap = (TreeMap<String, Drug>) ois.readObject();
			ois.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		return drugMap;
	}

	// main method
	public static void main(String[] args) {
		readFromCSV();
		System.out.println("From object store: \n" + readObjectsFromFile());
	}
}
