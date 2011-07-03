package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

public class Phone extends Controller {

	public static void index() {
		render();
	}

	public static void send(String phone, String lillygutPhone, String sms) {
		if (phone == null)
			index();
		else
			renderTemplate("Phone/index.html", phone,sms);
	}

}