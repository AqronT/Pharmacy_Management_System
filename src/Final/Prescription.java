package Final;

import java.io.Serializable;
import java.util.Date;

public class Prescription implements Serializable {
	// class variable
	private static final long serialVersionUID = 1L;

	// Prescription attributes, instance variables
	private String prescriptionId;
	private String instructionCode;
	private Patient patient;
	private Drug drug;
	private Date date;
	private double quantity;
	private long repeat;
	private double charges;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	// getters and setters
	public String getPrescriptionId() {
		return prescriptionId;
	}

	public void setPrescriptionId(String prescriptionId) {
		this.prescriptionId = prescriptionId;
	}

	public String getInstructionCode() {
		return instructionCode;
	}

	public void setInstructionCode(String instructionCode) {
		this.instructionCode = instructionCode;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Drug getDrug() {
		return drug;
	}

	public void setDrug(Drug drug) {
		this.drug = drug;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public long getRepeat() {
		return repeat;
	}

	public void setRepeat(long repeat) {
		this.repeat = repeat;
	}

	public double getCharges() {
		return charges;
	}

	public void setCharges(double charges) {
		this.charges = charges;
	}

	// toString method for Prescription object
	@Override
	public String toString() {
		return "Prescription [prescriptionId=" + prescriptionId + ", instructionCode=" + instructionCode + ", patient="
				+ patient + ", drug=" + drug + ", date=" + date + ", quantity=" + quantity + ", repeat=" + repeat
				+ ", charges=" + charges + "]";
	}

	public Prescription(Patient patient) {
		this.patient = patient;
	}

	// Prescription constructor
	public Prescription(String prescriptionId, String instructionCode, Patient patient, Drug drug, Date date,
			double quantity, long repeat, double charges) {
		this.prescriptionId = prescriptionId;
		this.instructionCode = instructionCode;
		this.patient = patient;
		this.drug = drug;
		this.date = date;
		this.quantity = quantity;
		this.repeat = repeat;
		this.charges = charges;
	}

}
