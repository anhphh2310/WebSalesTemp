package tma.datraining.dto;

import java.sql.Timestamp;
import java.util.UUID;

public class LocationDTO {

	private UUID locationId;
	private String country;
	private String city;
	private Timestamp createAt;
	private Timestamp modifiedAt;

	public LocationDTO() {
		super();
	}

	public UUID getLocationId() {
		return locationId;
	}

	public void setLocationId(UUID locationId) {
		this.locationId = locationId;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
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
		return "LocationDTO [locationId=" + locationId + ", country=" + country + ", city=" + city + ", createAt="
				+ createAt + ", modifiedAt=" + modifiedAt + "]";
	}

	
}
