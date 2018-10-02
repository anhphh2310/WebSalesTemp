package tma.datraining.model;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "product")
public class Product {

	private UUID productId;
	private int item;
	private String classProduct;
	private String inventory;
	private Timestamp createAt;
	private Timestamp modifiedAt;
	private Set<Sales> sales;
	
	public Product(int item, String classProduct, String inventory, Timestamp createAt, Timestamp modifiedAt) {
		super();
		this.item = item;
		this.classProduct = classProduct;
		this.inventory = inventory;
		this.createAt = createAt;
		this.modifiedAt = modifiedAt;
	}

	public Product() {
		super();
	}

	@Id
	@GeneratedValue
	@Column(name="product_id",nullable=false, insertable=false)
	public UUID getProductId() {
		return productId;
	}

	public void setProductId(UUID productId) {
		this.productId = productId;
	}
	
	@Column(name="item")
	public int getItem() {
		return item;
	}

	public void setItem(int item) {
		this.item = item;
	}

	@Column(name="class")
	public String getClassProduct() {
		return classProduct;
	}

	public void setClassProduct(String classProduct) {
		this.classProduct = classProduct;
	}

	@Column(name="inventory")
	public String getInventory() {
		return inventory;
	}

	public void setInventory(String inventory) {
		this.inventory = inventory;
	}
	
	@Column(name="create_at")
	public Timestamp getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Timestamp createAt) {
		this.createAt = createAt;
	}
	
	@Column(name="modified_at")
	public Timestamp getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Timestamp modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy="product")
	@JsonIgnore
	public Set<Sales> getSales() {
		return sales;
	}

	@JsonProperty
	public void setSales(Set<Sales> sales) {
		this.sales = sales;
	}

	
}
