package tma.datraining.model.cassandra;

import java.math.BigDecimal;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import com.datastax.driver.core.DataType;

@Table("sales")
public class CassSales {

	private UUID productId;
	private UUID timeId;
	private UUID locationId;
	private BigDecimal dollars;
	private DateTime createAt;
	private DateTime modifiedAt;

	public CassSales(UUID productId, UUID timeId, UUID locationId, BigDecimal dollars
			) {
		super();
		this.productId = productId;
		this.timeId = timeId;
		this.locationId = locationId;
		this.dollars = dollars;
		this.createAt = new DateTime(DateTimeZone.forID("Asia/Saigon"));
		this.modifiedAt = new DateTime(DateTimeZone.forID("Asia/Saigon"));
	}

	@PrimaryKeyColumn(name="product_id",type=PrimaryKeyType.PARTITIONED,ordinal=1)
	@CassandraType(type= DataType.Name.UUID)
	public UUID getProductId() {
		return productId;
	}

	public void setProductId(UUID productId) {
		this.productId = productId;
	}
	@PrimaryKeyColumn(name="time_id",type=PrimaryKeyType.CLUSTERED,ordinal=2)
	@CassandraType(type= DataType.Name.UUID)
	public UUID getTimeId() {
		return timeId;
	}

	public void setTimeId(UUID timeId) {
		this.timeId = timeId;
	}
	
	@PrimaryKeyColumn(name="location_id",type=PrimaryKeyType.CLUSTERED,ordinal=3)
	@CassandraType(type= DataType.Name.UUID)
	public UUID getLocationId() {
		return locationId;
	}

	public void setLocationId(UUID locationId) {
		this.locationId = locationId;
	}
	
	@Column(value="dollars")
	@CassandraType(type= DataType.Name.DECIMAL)
	public BigDecimal getDollars() {
		return dollars;
	}

	public void setDollars(BigDecimal dollars) {
		this.dollars = dollars;
	}

	@Column(value="create_at")
	public DateTime getCreateAt() {
		return createAt;
	}

	public void setCreateAt(DateTime createAt) {
		this.createAt = createAt;
	}

	@Column(value="modified_at")
	public DateTime getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(DateTime modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

}
