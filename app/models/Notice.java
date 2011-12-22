package models;

import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
// ze rekord
public class Notice extends Model {

    @Required
	public String title;
    @Required
    public Date postedAt;

	@Lob
	@Required
    @MaxSize(10000)
	// duuuuzy tekst bedzie
	public String content;

	@ManyToOne
	@Required
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
		this.tags =     new HashSet(Arrays.asList(new String[] { "1", "2", "5" }))  ;
		this.tags.clear();
	}

	public Notice previous() {
		Notice result=Notice.find("postedAt < ? order by postedAt desc", postedAt).first();
		return result;
	}
	 
	@ManyToMany(cascade=CascadeType.PERSIST)
	public Set<Tag> tags;
	
	public Notice tagItWith(String name) {
	    tags.add(Tag.findOrCreateByName(name));
	    return this;
	}
	
	public static List<Notice> findTaggedWith(String... tags) {
	    return Notice.find(
	            "select distinct p from Notice p join p.tags as t where t.name in (:tags) group by p.id, p.author, p.title, p.content,p.postedAt having count(t.id) = :size"
	    ).bind("tags", tags).bind("size", tags.length).fetch();
	}
	
	public Notice next() {
		Notice result=Notice.find("postedAt > ? order by postedAt asc", postedAt).first();
		return result;
	}
	
	public Notice addOffer( String author, int amount, float price) {

		Offer newOffer = new Offer(this, author, amount, price).save();
		this.offers.add(newOffer);
		this.save();
		return this;
	}

	public String toString() {
		return new String(title + " " + content + "\nby " + author
				+ "\nposted at: " + postedAt);
	}
	public static List<Notice> findTaggedWith(String tag) {
	    return Notice.find(
	        "select distinct p from Post p join p.tags as t where t.name = ?", tag
	    ).fetch();
	}
	}

