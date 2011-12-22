import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;

public class BasicTest extends UnitTest {

	@Before
	public void setup() {
		Fixtures.deleteDatabase();
	}

	@Test
	public void testTags() {
	    // Create a new user and save it
		User bob = new User("bob@gmail.com", "secret", "Bob", "Bob", "Smith")
		.save();
	 
	    // Create a new post
		Notice bobNotice = new Notice(bob, "My first post", "Hello world")
		.save();
		Notice bobNotice2 = new Notice(bob, "My 2t post", "Hello world")
		.save();
	 
	    // Well
	    assertEquals(0, Notice.findTaggedWith("Red").size());
	 
	    // Tag it now
	    bobNotice.tagItWith("Red").tagItWith("Blue").save();
	    bobNotice2.tagItWith("Red").tagItWith("Green").save();
	 
	    // Check
	    assertEquals(2, Notice.findTaggedWith("Red").size());
	    assertEquals(1, Notice.findTaggedWith("Blue").size());
	    assertEquals(1, Notice.findTaggedWith("Green").size());
	    assertEquals(1, Notice.findTaggedWith("Red", "Blue").size());
	    assertEquals(1, Notice.findTaggedWith("Red", "Green").size());
	    assertEquals(0, Notice.findTaggedWith("Red", "Green", "Blue").size());
	    assertEquals(0, Notice.findTaggedWith("Green", "Blue").size());
	    List<Map> cloud = Tag.getCloud();
		assertEquals(
		    "[{tag=Blue, pound=1}, {tag=Green, pound=1}, {tag=Red, pound=2}]",
		    cloud.toString()
		);
	}
	
	
	@Test
	public void useTheCommentsRelation() {
		// Create a new user and save it
		User bob = new User("bob@gmail.com", "secret", "Bob", "Bob", "Smith")
				.save();

		// Create a new post
		Notice bobNotice = new Notice(bob, "My first post", "Hello world")
				.save();
		// Post a first comment
		bobNotice.addOffer("Jeff", 110, (float) 1.10);
		bobNotice.addOffer("Tom", 2, (float) 1.4);

		// Count things
		assertEquals(1, User.count());
		assertEquals(1, Notice.count());
		assertEquals(2, Offer.count());

		// Retrieve Bob's post
		bobNotice = Notice.find("byAuthor", bob).first();
		assertNotNull(bobNotice);

		// Navigate to comments
		assertEquals(2, bobNotice.offers.size());
		assertEquals("Jeff", bobNotice.offers.get(0).author);

		// Delete the post
		bobNotice.delete();

		// Check that all comments have been deleted
		assertEquals(1, User.count());
		assertEquals(0, Notice.count());
		assertEquals(0, Offer.count());
	}

	@Test
	public void createNotice() {
		// Create a new user and save it
		User bob = new User("bob@gmail.com", "secret", "Bob", "Bob", "Smith")
				.save();

		// Create a new post
		new Notice(bob, "My first post", "Hello world").save();

		// Test that the post has been created
		assertEquals(1, Notice.count());

		// Retrieve all posts created by Bob
		List<Notice> bobNotices = Notice.find("byAuthor", bob).fetch();

		// Tests
		assertEquals(1, bobNotices.size());
		Notice firstNotice = bobNotices.get(0);
		assertNotNull(firstNotice);
		assertEquals(bob, firstNotice.author);
		assertEquals("My first post", firstNotice.title);
		assertEquals("Hello world", firstNotice.content);
		assertNotNull(firstNotice.postedAt);
	}

	@Test
	public void postComments() {
		// Create a new user and save it
		User bob = new User("bob@gmail.com", "secret", "Bob", "Bob", "Smith")
				.save();

		// Create a new post
		Notice bobNotice = new Notice(bob, "My first post", "Hello world")
				.save();

		// Post a first comment
		new Offer(bobNotice, "Jeff", 1, 1).save();
		new Offer(bobNotice, "Tom", 1, 2).save();

		// Retrieve all comments
		List<Offer> bobNoticeOffers = Offer.find("byNotice", bobNotice).fetch();

		// Tests
		assertEquals(2, bobNoticeOffers.size());

		Offer firstOffer = bobNoticeOffers.get(0);
		assertNotNull(firstOffer);
		assertEquals("Jeff", firstOffer.author);
		assertEquals(1, firstOffer.amount);
		assertNotNull(firstOffer.offerAt);

		Offer secondComment = bobNoticeOffers.get(1);
		assertNotNull(secondComment);
		assertEquals("Tom", secondComment.author);
		assertEquals(1, secondComment.amount);
		assertNotNull(secondComment.offerAt);
		
		
		
	}

	@Test
	public void createAndRetrieveUser() {
		// Create a new user and save it
		new User("bob@gmail.com", "secret", "Bob", "Bob", "Smith").save();

		// Retrieve the user with e-mail address bob@gmail.com
		User bob = User.find("byEmail", "bob@gmail.com").first();

		// Test
		assertNotNull(bob);
		assertEquals("Bob", bob.fullname);
	}

	@Test
	public void tryConnectAsUser() {
		// Create a new user and save it
		new User("bob@gmail.com", "secret", "Bob", "Bob", "Smith").save();

		// Test
		assertNotNull(User.connect("bob@gmail.com", "secret"));
		assertNull(User.connect("bob@gmail.com", "badpassword"));
		assertNull(User.connect("tom@gmail.com", "secret"));

		assertNotNull(User.connect("Bob", "secret"));
		assertNull(User.connect("Bob", "badpassword"));
		assertNull(User.connect("tom@gmail.com", "secret"));
	}

	@Test
	public void fullTest() {
	    Fixtures.loadModels("TestData.yml");
	 
	    // Count things
	//    System.out.println(User.findAll());
	    assertEquals(1, User.count());
	    assertEquals(1, Notice.count());
	    assertEquals(2, Offer.count());
	 
	    // Try to connect as users
	    assertNotNull(User.connect("bob@gmail.com", "secret"));
	    assertNotNull(User.connect("Bob", "secret"));
	    assertNull(User.connect("jeff@gmail.com", "badpassword"));
	    assertNull(User.connect("tom@gmail.com", "secret"));
	 
	    // Find all of Bob's posts
	    List<Notice> bobNotices = Notice.find("author.email", "bob@gmail.com").fetch();
	    assertEquals(1, bobNotices.size());
	 
	    // Find all comments related to Bob's posts
	    List<Offer> bobOffers = Offer.find("notice.author.email", "bob@gmail.com").fetch();
	    assertEquals(2, bobOffers.size());
	 
	    // Find the most recent post
	    Notice frontNotice = Notice.find("order by postedAt desc").first();
	    assertNotNull(frontNotice);
	    assertEquals("My first post", frontNotice.title);
	 
	    // Check that this post has two comments
	    assertEquals(2, frontNotice.offers.size());
	 
	    // Post a new comment
	    frontNotice.addOffer("Jim", 1,1);
	    assertEquals(3, frontNotice.offers.size());
	    assertEquals(3, Offer.count());
	}
	
	
}
