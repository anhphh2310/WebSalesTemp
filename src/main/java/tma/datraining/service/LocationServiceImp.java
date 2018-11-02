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
import tma.datraining.model.Location;
import tma.datraining.model.QLocation;
import tma.datraining.model.cassandra.CassLocation;
import tma.datraining.repository.LocationRepository;
import tma.datraining.repository.cassandra.CassLocationRepo;

@Service
@Transactional
public class LocationServiceImp implements LocationService {

	@Autowired
	private LocationRepository locationRepo;

	@Autowired
	private CassLocationRepo cassRepo;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Location> list() {
		List<Location> list = new ArrayList<>();
		locationRepo.findAll().forEach(e -> list.add(e));
		return list;
	}

	@Override
	public Location get(UUID id) {
		Location loca = null;
		loca = locationRepo.findByLocationId(id);
		if (loca == null)
			throw new NotFoundDataException("Location id ");
		return loca;
	}

	@Override
	public Location save(Location location) {
		// TODO Auto-generated method stub
		locationRepo.save(location);
		return location;
	}

	@Override
	public Location update(UUID id, Location location) {
		// TODO Auto-generated method stub
		return locationRepo.save(location);
	}

	@Override
	public UUID delete(UUID id) {
		// TODO Auto-generated method stub
		//locationRepo.delete(locationRepo.findById(id).get());
		locationRepo.delete(locationRepo.findByLocationId(id));
		return id;
	}

	@Override
	public List<CassLocation> listCass() {
		return (List<CassLocation>) cassRepo.findAll();
	}

	@Override
	public CassLocation getCass(UUID id) {
		CassLocation location = null;
		location = cassRepo.findById(id).get();
		return location;
	}

	@Override
	public CassLocation saveCass(CassLocation location) {
		// TODO Auto-generated method stub
		cassRepo.save(location);
		return location;
	}

	@Override
	public CassLocation updateCass(UUID id, CassLocation location) {
		// TODO Auto-generated method stub
		return cassRepo.save(location);
	}

	@Override
	public UUID deleteCass(UUID id) {
		// TODO Auto-generated method stub
		cassRepo.delete(cassRepo.findById(id).get());
		return id;
	}

	@Override
	public List<Location> findByCity(String city) {
		List<Location> list = locationRepo.findByCity(city);
		return list;
	}

	@Override
	public List<Location> findByCountry(String country) {
		List<Location> list = locationRepo.findByCountry(country);
		return list;
	}
	@Override
	public Location getLocationByQueryDsl(Predicate predicate) {
		Optional<Location> result = locationRepo.findOne(predicate);
		if(result.isPresent() == false) {
			throw new NotFoundDataException("LocationId");
		}
		return result.get();
	}

	@Override
	public List<Location> getListLocationsByQueryDsl(Predicate predicate) {
		List<Location> list = new ArrayList<>();
		locationRepo.findAll(predicate).forEach(e -> list.add(e));
		return list;
	}

	@Override
	public List<Location> getListLocationsByQueryDslSortCity() {
		JPAQuery<Location> query = new JPAQuery<Location>(entityManager);
		QLocation qLocation = QLocation.location;
		List<Location> result = query.from(qLocation).orderBy(qLocation.city.asc(), qLocation.country.desc()).fetch();
		return result;
	}

	@Override
	public List<Location> getListLocationsByQueryDslSortCountry() {
		JPAQuery<Location> query = new JPAQuery<Location>(entityManager);
		QLocation qLocation = QLocation.location;
		List<Location> result = query.from(qLocation).orderBy(qLocation.country.asc(), qLocation.city.desc()).fetch();
		return result;
	}

	public void updateByQueryDsl(UUID id, Location location) {
		JPAQueryFactory query = new JPAQueryFactory(entityManager);
		QLocation qLocation = QLocation.location;
		query.update(qLocation).where(qLocation.locationId.eq(id)).set(qLocation.city, location.getCity())
				.set(qLocation.country, location.getCountry()).set(qLocation.modifiedAt, location.getModifiedAt())
				.set(qLocation.createAt, location.getCreateAt())
				.execute();
	}

	@Override
	public UUID deleteByQueryDsl(UUID id) {
		JPAQueryFactory query = new JPAQueryFactory(entityManager);
		QLocation qLocation = QLocation.location;
		query.delete(qLocation).where(qLocation.locationId.eq(id)).execute();
		return id;
	}

}
