package models;

import java.util.*;
import javax.persistence.*;

import play.data.validation.Email;
import play.data.validation.Required;
import play.db.jpa.*;

@Entity
public class User extends Model {

	@Email
    @Required
	public String email;
	
	  @Required
	public String password;
	public String fullname;
	public boolean isAdmin;
	public String name;
	public String surname;

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public static User connect(String email, String password) {
		User loged = find("byEmailAndPassword", email, password).first();
		if (loged == null)
			loged = find("byFullnameAndPassword", email, password).first();

		return loged;
	}

	public User(String email, String password, String fullname, String name,
			String surname) {
		this.email = email;
		this.password = password;
		this.fullname = fullname;
		this.name = name;
		this.surname = surname;
	}

	public String toString() {
		return new String(name + " " + surname + " \nLogin: " + fullname
				+ "\nemail: " + email);
	}
}