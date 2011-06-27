package models;

import javax.persistence.Embeddable;
import javax.persistence.Entity;

import play.db.jpa.Model;

@Embeddable
public class Guts{

	public int quantity;

	public Guts() {
	}
	
	public Guts(int quantity) {
		this.quantity = quantity;
	}

	public Guts add(Guts guts) {
		return new Guts(quantity + guts.quantity);
	}

}
