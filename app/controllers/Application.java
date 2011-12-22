package controllers;

import play.*;
import play.data.validation.Required;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

    public static void index() {
    	//TODO powitanie jakieś  
    	
          List<Notice> freshNotice = Notice.find(
              "order by postedAt desc"
          ).fetch(10);
          render(freshNotice);
    }

    @Before
    static void addDefaults() {
        renderArgs.put("blogTitle", Play.configuration.getProperty("blog.title"));
        renderArgs.put("blogBaseline", Play.configuration.getProperty("blog.baseline"));
    }
    
    public static void show(Long id) {
        Notice notice = Notice.findById(id);
        
        render(notice);
    }
    
    public static void noticeOffer(Long postId,@Required String author,@Required int amount,@Required float price) {
        Notice notice = Notice.findById(postId);
        if (validation.hasErrors()) {
            render("Application/show.html", notice);
        }
        notice.addOffer(author, amount, price);
        flash.success("Thanks for offering %s", author);
        show(postId);
    }
}