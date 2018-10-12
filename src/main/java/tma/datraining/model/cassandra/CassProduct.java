package tma.datraining.model.cassandra;

import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import com.datastax.driver.core.DataType;

@Table("product")
public class CassProduct {

	private UUID productId;
	private int item;
	private String classProduct;
	private String inventory;
	private DateTime createAt;
	private DateTime modifiedAt;

	public CassProduct(UUID productId, int item, String classProduct, String inventory) {
		super();
		this.productId = productId;
		this.item = item;
		this.classProduct = classProduct;
		this.inventory = inventory;
		this.createAt = new DateTime(DateTimeZone.forID("Asia/Saigon"));
		this.modifiedAt = new DateTime(DateTimeZone.forID("Asia/Saigon"));
	}

	@PrimaryKey(value="product_id")
	@CassandraType(type=DataType.Name.UUID)
	public UUID getProductId() {
		return productId;
	}

	public void setProductId(UUID productId) {
		this.productId = productId;
	}

	@Column(value="item")
	public int getItem() {
		return item;
	}

	public void setItem(int item) {
		this.item = item;
	}

	@Column(value="class")
	public String getClassProduct() {
		return classProduct;
	}

	public void setClassProduct(String classProduct) {
		this.classProduct = classProduct;
	}

	@Column(value="inventory")
	public String getInventory() {
		return inventory;
	}

	public void setInventory(String inventory) {
		this.inventory = inventory;
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
