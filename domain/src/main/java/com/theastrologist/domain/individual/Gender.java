package com.theastrologist.domain.individual;

public enum Gender {
	F("Female"),
	FM("Female to male transition"),
	M("Male"),
	MF("Male to female transition"),
	Mix("Multiplets with mixed gender"),
	NA("Not applicable"),
	UNK("Unknown");

	private final String gender;

	Gender(String gender) {
		this.gender = gender;
	}
}
