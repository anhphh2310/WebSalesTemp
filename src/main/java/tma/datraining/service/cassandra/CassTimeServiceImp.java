package tma.datraining.service.cassandra;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tma.datraining.model.cassandra.CassTime;
import tma.datraining.repository.cassandra.CassTimeRepo;

@Service
public class CassTimeServiceImp implements CassTimeService {

	@Autowired
	private CassTimeRepo cassRepo;

	@Override
	public List<CassTime> list() {
		List<CassTime> list = new ArrayList<>();
		cassRepo.findAll().forEach(e -> list.add(e));
		return list;
	}

	@Override
	public CassTime get(UUID id) {
		CassTime time = null;
		time = cassRepo.findById(id).get();
		return time;
	}

	@Override
	public UUID save(CassTime time) {
		cassRepo.save(time);
		return time.getTimeId();
	}

	@Override
	public void update(UUID id, CassTime time) {
		// TODO Auto-generated method stub
		cassRepo.save(time);
	}

	@Override
	public void delete(UUID id) {
		// TODO Auto-generated method stub
		cassRepo.delete(cassRepo.findById(id).get());
	}

}
