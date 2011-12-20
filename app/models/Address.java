package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Address {

	public String street;
	public String number; // ile przez ile
	// public String ln; //local number
	public String postCode; // TODO sprawdzanie, czy poproawny w zaleznosci od
							// kraju
	public String city;
	public String country; // TODO enum z funkcją sprawdzania poprawnosci
							// postcode
	public String name;
	public String surname;

	@Id
	@GeneratedValue
	public Long id;

	public Address(User person, String street, String number, String postCode,
			String city, String country) {
		this.street = street;
		this.number = number;
		this.postCode = postCode;
		this.city = city;
		this.country = country;
		this.name = person.getName();
		this.surname = person.getSurname();

	}

}
