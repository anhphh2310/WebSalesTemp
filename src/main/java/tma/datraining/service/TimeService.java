package tma.datraining.service;

import java.util.List;
import java.util.UUID;

import tma.datraining.model.Time;

public interface TimeService {
	
	List<Time> list();
	
	UUID save(Time time);
	
	Time get(UUID id);
	
	void update(UUID id,Time time);

	void delete(UUID id);
}
