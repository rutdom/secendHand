package controllers;

import play.*;
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
    
}