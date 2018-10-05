package tma.datraining.model.cassandra;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import com.datastax.driver.core.DataType;
import com.datastax.driver.core.DataType.Name;

@Table("sales")
public class CassSales {

	private UUID productId;
	private UUID timeId;
	private UUID locationId;
	private BigDecimal dollards;
	private Timestamp createtAt;
	private Timestamp modifiedAt;

	public CassSales(UUID productId, UUID timeId, UUID locationId, BigDecimal dollards, Timestamp createtAt,
			Timestamp modifiedAt) {
		super();
		this.productId = productId;
		this.timeId = timeId;
		this.locationId = locationId;
		this.dollards = dollards;
		this.createtAt = createtAt;
		this.modifiedAt = modifiedAt;
	}

	@PrimaryKeyColumn(name="product_id",type=PrimaryKeyType.PARTITIONED)
	@CassandraType(type= DataType.Name.UUID)
	public UUID getProductId() {
		return productId;
	}

	public void setProductId(UUID productId) {
		this.productId = productId;
	}
	@PrimaryKeyColumn(name="time_id",type=PrimaryKeyType.CLUSTERED)
	@CassandraType(type= DataType.Name.UUID)
	public UUID getTimeId() {
		return timeId;
	}

	public void setTimeId(UUID timeId) {
		this.timeId = timeId;
	}
	
	@PrimaryKeyColumn(name="location_id",type=PrimaryKeyType.CLUSTERED)
	@CassandraType(type= DataType.Name.UUID)
	public UUID getLocationId() {
		return locationId;
	}

	public void setLocationId(UUID locationId) {
		this.locationId = locationId;
	}
	
	@Column(value="dollars")
	@CassandraType(type= DataType.Name.DECIMAL)
	public BigDecimal getDollards() {
		return dollards;
	}

	public void setDollards(BigDecimal dollards) {
		this.dollards = dollards;
	}

	@Column(value="create_at")
	@CassandraType(type=Name.TIMESTAMP)
	public Timestamp getCreatetAt() {
		return createtAt;
	}

	public void setCreatetAt(Timestamp createtAt) {
		this.createtAt = createtAt;
	}

	@Column(value="modified_at")
	@CassandraType(type=Name.TIMESTAMP)
	public Timestamp getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Timestamp modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

}
