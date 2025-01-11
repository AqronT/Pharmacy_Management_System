package Final;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.TreeMap;

// patient class
public class Patient implements Serializable {
	// class variables
	private static final long serialVersionUID = 1L;
	// Patient attributes, instance variables
	private String name;
	private String phone;
	private String address;
	private String city;
	private String province;
	private String postalCode;
	private String medicalConditions;
	private Practioner practioner;
	private Date date;

	// toString method for Patient object
	@Override
	public String toString() {
		return "Patient [name=" + name + ", phone=" + phone + ", address=" + address + ", city=" + city + ", province="
				+ province + ", postalCode=" + postalCode + ", practioner=" + practioner + ", date=" + date + "]";
	}

	public Patient(String name) {
		this.name = name;
	}

	// Patient constructor
	public Patient(String name, String phone, String address, String city, String province, String postalCode,
			String medicalConditions, Practioner practioner, Date date) {
		this.name = name;
		this.phone = phone;
		this.address = address;
		this.city = city;
		this.province = province;
		this.postalCode = postalCode;
		this.medicalConditions = medicalConditions;
		this.practioner = practioner;
		this.date = date;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	// getters and setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public Practioner getPractioner() {
		return practioner;
	}

	public void setPractioner(Practioner practioner) {
		this.practioner = practioner;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getMedicalConditions() {
		return medicalConditions;
	}

	public void setMedicalConditions(String medicalConditions) {
		this.medicalConditions = medicalConditions;
	}

	// This method reads in the patient file path as a String, and splits the
	// information into a String array. The values in the array are the attributes
	// of the patient.
	public static void readFromCSV() {
		// SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

		TreeMap<String, Practioner> practionerMap = null;
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data/practioners.dat"))) {
			practionerMap = (TreeMap<String, Practioner>) ois.readObject();
			ois.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		String FILE_PATH = "data/patients.csv";
		TreeMap<String, Patient> patientMap = new TreeMap<String, Patient>();
		try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
			String line;
			while ((line = br.readLine()) != null) {

				String[] values = line.split(",");
				String name = values[0].trim();
				String phone = values[1].trim();
				String address = values[2].trim();
				String city = values[3].trim();
				String province = values[4].trim();
				String postalCode = values[5].trim();
				String medicalConditions = values[6].trim();
				String practionerName = values[7].trim();
				Practioner practioner = practionerMap.get(practionerName);
				Date date = new Date();

				Patient patient = new Patient(name, phone, address, city, province, postalCode, medicalConditions,
						practioner, date);
				patientMap.put(name, patient);
			}

			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data/patients.dat"));
			oos.writeObject(patientMap);
			oos.close();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		readFromCSV();
	}

	// compareTo for patients, Comparable code
	public int compareTo(Patient patient) {
		return this.getName().compareTo(patient.getName());
	}
}
