package tma.datraining.dto;

import java.sql.Timestamp;
import java.util.UUID;

public class ProductDTO {

	private UUID productId;
	private int item;
	private String classProduct;
	private String inventory;
	private Timestamp createAt;
	private Timestamp modifiedAt;

	public ProductDTO(UUID productId, int item, String classProduct, String inventory, Timestamp createAt,
			Timestamp modifiedAt) {
		super();
		this.productId = productId;
		this.item = item;
		this.classProduct = classProduct;
		this.inventory = inventory;
		this.createAt = createAt;
		this.modifiedAt = modifiedAt;
	}

	public UUID getProductId() {
		return productId;
	}

	public void setProductId(UUID productId) {
		this.productId = productId;
	}

	public int getItem() {
		return item;
	}

	public void setItem(int item) {
		this.item = item;
	}

	public String getClassProduct() {
		return classProduct;
	}

	public void setClassProduct(String classProduct) {
		this.classProduct = classProduct;
	}

	public String getInventory() {
		return inventory;
	}

	public void setInventory(String inventory) {
		this.inventory = inventory;
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

}
