package tma.datraining.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import tma.datraining.model.Location;
import tma.datraining.model.Product;
import tma.datraining.model.Sales;
import tma.datraining.model.Time;

public interface SalesRepository extends CrudRepository<Sales, UUID> {

	List<Sales> findByProduct(Product product);

	List<Sales> findByLocation(Location location);

	List<Sales> findByTime(Time time);
}
