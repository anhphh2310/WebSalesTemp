package tma.datraining.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tma.datraining.model.Location;
import tma.datraining.model.cassandra.CassLocation;
import tma.datraining.repository.LocationRepository;
import tma.datraining.repository.cassandra.CassLocationRepo;

@Service
@Transactional
public class LocationServiceImp implements LocationService{

	@Autowired
	private LocationRepository locationRepo;
	
	@Autowired
	private CassLocationRepo cassRepo;
	
	@Override
	public List<Location> list() {
		List<Location> list = new ArrayList<>();
		locationRepo.findAll().forEach(e ->list.add(e));
		return list;
	}

	@Override
	public Location get(UUID id) {
		Location loca = null;
		for (Location  location : list()) {
			if(location.getLocationId().equals(id))
				loca = location;
		}
		return loca;
	}

	@Override
	public UUID save(Location location) {
		// TODO Auto-generated method stub
		locationRepo.save(location);
		return location.getLocationId();
	}

	@Override
	public void update(UUID id, Location location) {
		// TODO Auto-generated method stub
		locationRepo.save(location);
	}
	
	@Override
	public void delete(UUID id) {
		// TODO Auto-generated method stub
		locationRepo.delete(locationRepo.findById(id).get());
	}

	@Override
	public List<CassLocation> listCass() {
		return (List<CassLocation>) cassRepo.findAll();
	}

	@Override
	public CassLocation getCass(UUID id) {
		// TODO Auto-generated method stub
		CassLocation location = null;
		location = cassRepo.findById(id).get();
		return location;
	}

	@Override
	public UUID saveCass(CassLocation location) {
		// TODO Auto-generated method stub
		cassRepo.save(location);
		return location.getLocationId();
	}

	@Override
	public void updateCass(UUID id, CassLocation location) {
		// TODO Auto-generated method stub
		cassRepo.save(location);
	}

	@Override
	public void deleteCass(UUID id) {
		// TODO Auto-generated method stub
		cassRepo.delete(cassRepo.findById(id).get());
	}


}
