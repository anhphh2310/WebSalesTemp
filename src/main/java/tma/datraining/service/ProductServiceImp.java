package tma.datraining.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import tma.datraining.exception.NotFoundDataException;
import tma.datraining.model.Product;
import tma.datraining.model.QProduct;
import tma.datraining.model.cassandra.CassProduct;
import tma.datraining.repository.ProductRepository;
import tma.datraining.repository.cassandra.CassProductRepo;

@Service
@Transactional
public class ProductServiceImp implements ProductService{

	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private CassProductRepo cassRepo;
	
	@PersistenceContext
	private EntityManager entityManger;
	
	@Override
	public List<Product> list() {
		List<Product> list = new ArrayList<>();
		productRepo.findAll().forEach(e ->list.add(e));
		return list;
	}

	@Override
	public Product save(Product product) {
		productRepo.save(product);
		return product;
	}

	@Override
	public Product get(UUID id) {
		// TODO Auto-generated method stub
		Product pro = null;
//		List<Product> list = new ArrayList<>();
//		productRepo.findAll().forEach(e ->list.add(e));
//		for (Product product : list) {
//			if(product.getProductId().toString().equals(id.toString())) {
//				pro = product;
//			}
//		}
		pro = productRepo.findByProductId(id);
		if(pro == null)
			throw new NotFoundDataException("Product id ");
		return pro;
	}

	@Override
	public Product update(UUID id, Product product) {
		// TODO Auto-generated method stub
		return productRepo.save(product);
	}

	@Override
	public UUID delete(UUID id) {
		// TODO Auto-generated method stub
		productRepo.delete(productRepo.findByProductId(id));
		return id;
	}

	@Override
	public Product findByClassProduct(String classProduct) {
		Product product = new Product();
		product = productRepo.findByClassProduct(classProduct);
		if(product == null) {
			throw new NotFoundDataException("");
		}
		return product;
	}

	@Override
	public List<CassProduct> listCass() {
		// TODO Auto-generated method stub
		return (List<CassProduct>) cassRepo.findAll();
	}

	@Override
	public CassProduct saveCass(CassProduct product) {
		cassRepo.save(product);
		return product;
	}

	@Override
	public CassProduct getCass(UUID id) {
		// TODO Auto-generated method stub
		CassProduct product = null;
		product = cassRepo.findById(id).get();
		return product;
	}

	@Override
	public CassProduct updateCass(UUID id, CassProduct product) {
		// TODO Auto-generated method stub
		return cassRepo.save(product);
	}

	@Override
	public UUID deleteCass(UUID id) {
		// TODO Auto-generated method stub
		cassRepo.delete(cassRepo.findById(id).get());
		return id;
	}

	@Override
	public List<Product> findByInventory(String inventory) {
		List<Product> list = productRepo.findByInventory(inventory);
		if(list == null)
			throw new NotFoundDataException("");
		return list;
	}

	@Override
	public List<Product> getProductsByQueryDsl(Predicate predicate) {
		List<Product> result = new ArrayList<>();
		productRepo.findAll(predicate).forEach(e -> result.add(e));
		return result;
	}

	@Override
	public Product getProductByQueryDsl(Predicate predicate) {
		Optional<Product> product = productRepo.findOne(predicate);
		if(product.isPresent() == false)
			throw new NotFoundDataException("ProductId ");
		return product.get();
	}
	@Override
	public List<Product> getProductsByQueryDslSortClass() {
		JPAQuery<Product> query = new JPAQuery<Product>(entityManger);
		QProduct qProduct = QProduct.product;
		List<Product> result = query.from(qProduct).orderBy(qProduct.classProduct.asc()).fetch();
		return result;
	}

	@Override
	public List<Product> getProductsByQueryDslSortInventory() {
		JPAQuery<Product> query = new JPAQuery<Product>(entityManger);
		QProduct qProduct = QProduct.product;
		List<Product> result = query.from(qProduct).orderBy(qProduct.inventory.asc()).fetch();
		return result;
	}

	@Override
	public void updateByQueryDsl(UUID id, Product product) {
		JPAQueryFactory query = new JPAQueryFactory(entityManger);
		QProduct qProduct = QProduct.product;
		query.update(qProduct).where(qProduct.productId.eq(id))
		.set(qProduct.classProduct, product.getClassProduct())
		.set(qProduct.inventory, product.getInventory())
		.set(qProduct.item, product.getItem())
		.set(qProduct.modifiedAt,product.getModifiedAt())
		.set(qProduct.createAt, product.getCreateAt())
		.execute();
		
	}

	@Override
	public UUID deleteByQueryDsl(UUID id) {
		JPAQueryFactory query = new JPAQueryFactory(entityManger);
		QProduct qProduct = QProduct.product;
		query.delete(qProduct).where(qProduct.productId.eq(id)).execute();
		return id;
	}

}
