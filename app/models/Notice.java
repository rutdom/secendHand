package models;

import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity
// ze rekord
public class Notice extends Model {

	public String title;
	public Date postedAt;

	@Lob
	// duuuuzy tekst bedzie
	public String content;

	@ManyToOne
	// duzo ogloszen, jeden autor
	public User author;

	@OneToMany(mappedBy = "notice", cascade = CascadeType.ALL)
	public List<Offer> offers;// = Collections.emptyList();

	public Notice(User author, String title, String content) {
		this.author = author;
		this.title = title;
		this.postedAt = new Date();
		this.content = content;
		this.offers = Offer.findAll();
		this.offers.clear();
	}

	public Notice addOffer(String author, int amount, float price) {

		Offer newOffer = new Offer(this, author, amount, price).save();
		this.offers.add(newOffer);
		this.save();
		return this;
	}

	public String toString() {
		return new String(title + " " + content + "\nby " + author
				+ "\nposted at: " + postedAt);
	}
}
