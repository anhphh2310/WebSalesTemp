package tma.datraining.model.cassandra;

import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import com.datastax.driver.core.DataType;

@Table("location")
public class CassLocation {

	private UUID locationId;
	private String country;
	private String city;
	private DateTime createAt;
	private DateTime modifiedAt;

	public CassLocation(UUID locationId, String country, String city) {
		super();
		this.locationId = locationId;
		this.country = country;
		this.city = city;
		this.createAt = new DateTime(DateTimeZone.forID("Asia/Saigon"));
		this.modifiedAt = new DateTime(DateTimeZone.forID("Asia/Saigon"));
	}

	@PrimaryKey(value="location_id")
	@CassandraType(type=DataType.Name.UUID)
	public UUID getLocationId() {
		return locationId;
	}

	public void setLocationId(UUID locationId) {
		this.locationId = locationId;
	}

	@Column(value="country")
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Column(value="city")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(value = "create_at")
	public DateTime getCreateAt() {
		return createAt;
	}

	public void setCreateAt(DateTime createAt) {
		this.createAt = createAt;
	}

	@Column(value = "modified_at")
	public DateTime getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(DateTime modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

}
