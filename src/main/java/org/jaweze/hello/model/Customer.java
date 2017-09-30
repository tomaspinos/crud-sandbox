package org.jaweze.hello.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Customer {

	@Id
	@GeneratedValue
	private Long id;

	private String firstName;

	private String lastName;

	private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
	private Sex sex;

    @Enumerated(EnumType.STRING)
	private MarriageStatus marriageStatus;

	protected Customer() {
	}

	public Customer(String firstName, String lastName, LocalDate birthDate, Sex sex, MarriageStatus marriageStatus) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = birthDate;
		this.sex = sex;
		this.marriageStatus = marriageStatus;
	}

	public Long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public MarriageStatus getMarriageStatus() {
        return marriageStatus;
    }

    public void setMarriageStatus(MarriageStatus marriageStatus) {
        this.marriageStatus = marriageStatus;
    }

    @Override
	public String toString() {
		return String.format("Customer[id=%d, firstName='%s', lastName='%s', birthDate='%s', sex='%s', marriageStatus='%s']",
                id, firstName, lastName, birthDate, sex, marriageStatus);
	}

}
