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
import com.querydsl.jpa.impl.JPAQueryFactory;

import tma.datraining.exception.NotFoundDataException;
import tma.datraining.model.Location;
import tma.datraining.model.Product;
import tma.datraining.model.QSales;
import tma.datraining.model.Sales;
import tma.datraining.model.Time;
import tma.datraining.model.cassandra.CassSales;
import tma.datraining.repository.SalesRepository;
import tma.datraining.repository.cassandra.CassSalesRepo;

@Service
@Transactional
public class SalesServiceImp implements SalesService {

	@Autowired
	private SalesRepository salesRepo;

	@Autowired
	private CassSalesRepo cassRepo;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Sales> list() {
		List<Sales> list = new ArrayList<>();
		salesRepo.findAll().forEach(e -> list.add(e));
		return list;
	}

	@Override
	public Sales save(Sales sale) {
		salesRepo.save(sale);
		return sale;
	}

	@Override
	public Sales get(UUID id) {
		Sales sales = salesRepo.findBySalesId(id);
		if (sales == null) {
			throw new NotFoundDataException("");
		}
		return sales;
	}

	@Override
	public Sales update(UUID id, Sales sale) {
		return salesRepo.save(sale);

	}

	@Override
	public UUID delete(UUID id) {
		salesRepo.delete(salesRepo.findBySalesId(id));
		return id;
	}

	@Override
	public List<Sales> findByProduct(Product product) {
		List<Sales> sales = salesRepo.findByProduct(product);
		return sales;
	}

	@Override
	public List<Sales> findByLocation(Location location) {
		List<Sales> sales = salesRepo.findByLocation(location);
		return sales;
	}

	@Override
	public List<Sales> findByTime(Time time) {
		List<Sales> sales = salesRepo.findByTime(time);
		return sales;
	}

	@Override
	public List<CassSales> listCass() {
		// TODO Auto-generated method stub
		return (List<CassSales>) cassRepo.findAll();
	}

	@Override
	public CassSales saveCass(CassSales sale) {
		// TODO Auto-generated method stub
		cassRepo.save(sale);
		return sale;
	}

	@Override
	public CassSales getCass(UUID id) {
		// TODO Auto-generated method stub
		CassSales sales = null;
		sales = cassRepo.findById(id).get();
		return sales;
	}

	@Override
	public CassSales updateCass(UUID id, CassSales sales) {
		// TODO Auto-generated method stub
		return cassRepo.save(sales);
	}

	@Override
	public UUID deleteCass(UUID id) {
		// TODO Auto-generated method stub
		cassRepo.delete(cassRepo.findById(id).get());
		return id;
	}

	@Override
	public Sales getSaleByQueryDsl(Predicate predicate) {
		Optional<Sales> result = salesRepo.findOne(predicate);
		if (result.isPresent() == false)
			throw new NotFoundDataException("Sales ");
		return result.get();
	}

	@Override
	public List<Sales> getListSalesByQueryDsl(Predicate predicate) {
		List<Sales> result = new ArrayList<>();
		salesRepo.findAll(predicate).forEach(e -> result.add(e));
		return result;
	}

	@Override
	public void updateByQueryDsl(UUID id, Sales sales) {
		JPAQueryFactory query = new JPAQueryFactory(entityManager);
		QSales qSales = QSales.sales;
		query.update(qSales).where(qSales.salesId.eq(id)).set(qSales.dollars, sales.getDollars())
				.set(qSales.location, sales.getLocation()).set(qSales.time, sales.getTime())
				.set(qSales.product, sales.getProduct()).set(qSales.createAt, sales.getCreateAt())
				.set(qSales.modifiedAt, sales.getModifiedAt()).execute();

	}

	@Override
	public UUID deleteByQueryDsl(UUID id) {
		JPAQueryFactory query = new JPAQueryFactory(entityManager);
		QSales qSales = QSales.sales;
		query.delete(qSales).where(qSales.salesId.eq(id)).execute();
		return id;
	}
}
