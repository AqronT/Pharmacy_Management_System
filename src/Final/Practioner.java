package Final;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.TreeMap;

public class Practioner implements Serializable {
	// class variables
	private static final long serialVersionUID = 1L;
	private static final String[] types = { "Ontario Doctor", "Ontario Dentist", "Ontario Pharmacist",
			"Nurse Practioner" };

	// instance variables
	private String name;
	private String licenseNumber;
	private String type;
	private String phone;

	// other fields, constructors, getters, and setters

	public Practioner(String name, String licenseNumber, String type, String phone) {
		this.name = name;
		this.licenseNumber = licenseNumber;
		this.type = type;
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "Practioner [name=" + name + ", licenseNumber=" + licenseNumber + ", type=" + type + ", phone=" + phone
				+ "]";
	}

	// This method reads in the practitioner file path as a String, and splits the
	// information into a String array. The values in the array are the attributes
	// of the practitioner.
	public static void readFromCSV() {
		String FILE_PATH = "data/practioners.csv";
		TreeMap<String, Practioner> practionMap = new TreeMap<String, Practioner>();
		try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
			String line;
			while ((line = br.readLine()) != null) {

				String[] values = line.split(",");
				String name = values[0].trim();
				String licenseNumber = values[1].trim();
				String type = values[2].trim();
				String phone = values[3].trim();

				Practioner practioner = new Practioner(name, licenseNumber, type, phone);
				practionMap.put(name, practioner);
			}

			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data/practioners.dat"));
			oos.writeObject(practionMap);
			oos.close();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// more getters and setters
	public static String[] getTypes() {
		return types;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLicenseNumber() {
		return licenseNumber;
	}

	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	// main method
	public static void main(String[] args) {
		readFromCSV();
	}
}
