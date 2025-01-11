package Final;

import java.util.Comparator;

// Introduction: This class is a comparator for Prescription objects

// Method: The parameter for the compare method are the 2 Prescription objects being compared

// Internal: An integer is returned, which sorts the Patients.
class PrescriptionComparator implements Comparator<Prescription> {
	@Override
	public int compare(Prescription p1, Prescription p2) {
		return p1.getPatient().compareTo(p2.getPatient());
	}
}