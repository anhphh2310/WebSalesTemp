package tma.datraining.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

public class SalesDTO {

	private UUID salesId;
	private UUID product;
	private UUID time;
	private UUID location;
	private BigDecimal dollars;
	private Timestamp createAt;
	private Timestamp modifiedAt;

	public SalesDTO() {
		super();
	}

	public UUID getSalesId() {
		return salesId;
	}

	public void setSalesId(UUID salesId) {
		this.salesId = salesId;
	}

	public UUID getProduct() {
		return product;
	}

	public void setProduct(UUID product) {
		this.product = product;
	}

	public UUID getTime() {
		return time;
	}

	public void setTime(UUID time) {
		this.time = time;
	}

	public UUID getLocation() {
		return location;
	}

	public void setLocation(UUID location) {
		this.location = location;
	}

	public BigDecimal getDollars() {
		return dollars;
	}

	public void setDollars(BigDecimal dollars) {
		this.dollars = dollars;
	}

	public Timestamp getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Timestamp createAt) {
		this.createAt = createAt;
	}

	public Timestamp getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Timestamp modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	@Override
	public String toString() {
		return "SalesDTO [salesId=" + salesId + ", product=" + product + ", time=" + time + ", location=" + location
				+ ", dollars=" + dollars + ", createAt=" + createAt + ", modifiedAt=" + modifiedAt + "]";
	}

	
}
