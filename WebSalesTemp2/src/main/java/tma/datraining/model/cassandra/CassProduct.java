package tma.datraining.model.cassandra;

import java.sql.Timestamp;
import java.util.UUID;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import com.datastax.driver.core.DataType.Name;

@Table("product")
public class CassProduct {

	private UUID productId;
	private int item;
	private String classProduct;
	private String inventory;
	private Timestamp createAt;
	private Timestamp modifiedAt;

	public CassProduct(UUID productId, int item, String classProduct, String inventory, Timestamp createAt,
			Timestamp modifiedAt) {
		super();
		this.productId = productId;
		this.item = item;
		this.classProduct = classProduct;
		this.inventory = inventory;
		this.createAt = createAt;
		this.modifiedAt = modifiedAt;
	}

	public CassProduct() {
		super();
	}

	@PrimaryKeyColumn(name="product_id",type=PrimaryKeyType.PARTITIONED,ordinal=1)
	@CassandraType(type=Name.UUID)
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
	@CassandraType(type=Name.TIMESTAMP)
	public Timestamp getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Timestamp createAt) {
		this.createAt = createAt;
	}

	@Column(value="modified_at")
	@CassandraType(type=Name.TIMESTAMP)
	public Timestamp getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Timestamp modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	@Override
	public String toString() {
		return "CassProduct [productId=" + productId + ", item=" + item + ", classProduct=" + classProduct
				+ ", inventory=" + inventory + ", createAt=" + createAt + ", modifiedAt=" + modifiedAt + "]";
	}

}
