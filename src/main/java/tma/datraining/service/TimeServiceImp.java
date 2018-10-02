package tma.datraining.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tma.datraining.model.Time;
import tma.datraining.repository.TimeRepository;

@Service
@Transactional
public class TimeServiceImp implements TimeService{

	@Autowired
	private TimeRepository timeRepository;
	
	@Override
	public List<Time> list() {
		List<Time> list = new ArrayList<>();
		timeRepository.findAll().forEach(e -> list.add(e));
		return list;
	}

	@Override
	public UUID save(Time time) {
		timeRepository.save(time);
		return time.getTimeId();
	}

	@Override
	public Time get(UUID id) {
		Time time = null;
		for (Time tim : list()) {
			if(tim.getTimeId().equals(id))
				time = tim;
		}
		return time;
	}

	@Override
	public void update(UUID id, Time time) {
		timeRepository.save(time);
	}

	@Override
	public void delete(UUID id) {
		timeRepository.delete(timeRepository.findById(id).get());
		
	}

	
}
