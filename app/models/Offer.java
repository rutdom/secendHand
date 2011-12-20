package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class Offer extends Model {

	public String author;
	public Date offerAt;

	public int amount;
	public float price;

	@ManyToOne
	public Notice notice;

	public Offer(Notice notice, String author, int amount, float price) {
		this.notice = notice;
		this.author = author;
		this.amount = amount;
		this.price = price;
		this.offerAt = new Date();
	}

	public String toString() {
		return new String(author + " buy " + amount + " for " + price + " of "
				+ notice);
	}
}
