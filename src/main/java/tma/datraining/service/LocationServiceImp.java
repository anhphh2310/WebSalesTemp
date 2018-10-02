package tma.datraining.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tma.datraining.model.Location;
import tma.datraining.repository.LocationRepository;

@Service
@Transactional
public class LocationServiceImp implements LocationService{

	@Autowired
	private LocationRepository locationRepo;
	
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


}
