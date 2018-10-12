package diploma.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Finger implements Serializable {

	private Set<Fingerprint> fingerprints;
	private String id;

	public Finger(String id) {

		this.id = id;
		fingerprints = new HashSet<>();
	}

	public String getId() {
		return id;
	}

	public Set<Fingerprint> getFingerprints() {
		return fingerprints;
	}

	public void addFingerprint(Fingerprint fingerprint) {
		fingerprints.add(fingerprint);
	}

	@Override
	public boolean equals(Object o) {

		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Finger finger = (Finger) o;

		return id.equals(finger.id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public String toString() {
		return id;
	}
}
