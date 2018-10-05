package tma.datraining.service.cassandra;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tma.datraining.model.cassandra.CassLocation;
import tma.datraining.repository.cassandra.CassLocationRepo;

@Service
public class CassLocationServiceImp implements CassLocationService {

	@Autowired
	private CassLocationRepo cassRepo;
	
	@Override
	public List<CassLocation> list() {
		List<CassLocation> list = new ArrayList<>();
		cassRepo.findAll().forEach(e -> list.add(e));
		return list;
	}

	@Override
	public CassLocation get(UUID id) {
		CassLocation loca = null;
		loca = cassRepo.findById(id).get();
		return loca;
		
	}

	@Override
	public UUID save(CassLocation location) {
		cassRepo.save(location);	
		return location.getLocationId();
	}

	@Override
	public void update(UUID id, CassLocation location) {
		cassRepo.save(location);
	}

	@Override
	public void delete(UUID id) {
		cassRepo.delete(cassRepo.findById(id).get());
		
	}

}
