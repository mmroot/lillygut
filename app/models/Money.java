package models;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;

import play.db.jpa.Model;

@Embeddable
public class Money{
	@Column(insertable=false, updatable = false)
	public String currency;
	@Column(insertable=false, updatable = false)
	public BigDecimal amount;

	private Money(String currency, double amount) {
		this.currency = currency;
		this.amount = new BigDecimal(amount);
//		this.amount = amount;
	}

	public static Money CHF(double amount) {
		return new Money("CHF", amount);
	}

	public BigDecimal multiply(int i) {
		return amount.multiply(new BigDecimal(i));
	}

}
