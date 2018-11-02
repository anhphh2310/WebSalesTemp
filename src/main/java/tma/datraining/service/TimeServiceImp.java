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
import tma.datraining.model.QTime;
import tma.datraining.model.Time;
import tma.datraining.model.cassandra.CassTime;
import tma.datraining.repository.TimeRepository;
import tma.datraining.repository.cassandra.CassTimeRepo;

@Service
@Transactional
public class TimeServiceImp implements TimeService {

	@Autowired
	private TimeRepository timeRepository;

	@Autowired
	private CassTimeRepo cassRepo;

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<Time> list() {
		List<Time> list = new ArrayList<>();
		timeRepository.findAll().forEach(e -> list.add(e));
		return list;
	}

	@Override
	public Time save(Time time) {
		timeRepository.save(time);
		return time;
	}

	@Override
	public Time get(UUID id) {
		Time time = timeRepository.findByTimeId(id);
		if(time == null)
			throw new NotFoundDataException("Time id ");
		return time;
	}

	@Override
	public Time update(UUID id, Time time) {
		return timeRepository.save(time);
		
	}

	@Override
	public UUID delete(UUID id) {
		timeRepository.delete(timeRepository.findByTimeId(id));
		return id;
	}

	@Override
	public List<CassTime> listCass() {
		// TODO Auto-generated method stub
		return (List<CassTime>) cassRepo.findAll();
	}

	@Override
	public CassTime saveCass(CassTime time) {
		// TODO Auto-generated method stub
		cassRepo.save(time);
		return time;
	}

	@Override
	public CassTime getCass(UUID id) {
		CassTime time = null;
		time = cassRepo.findById(id).get();
		return time;
	}

	@Override
	public CassTime updateCass(UUID id, CassTime time) {
		// TODO Auto-generated method stub
		return cassRepo.save(time);
	}

	@Override
	public UUID deleteCass(UUID id) {
		// TODO Auto-generated method stub
		cassRepo.delete(cassRepo.findById(id).get());
		return id;
	}

	@Override
	public List<Time> findByMonth(int month) {
		List<Time> list = timeRepository.findByMonth(month);
		return list;
	}

	@Override
	public List<Time> findByYear(int year) {
		List<Time> list = timeRepository.findByYear(year);
		return list;
	}

	@Override
	public List<Time> findByQuarter(int quarter) {
		List<Time> result = timeRepository.findByQuarter(quarter);
		return result;
	}
	
	//QueryDSL
	@Override
	public Time getTimeByQueryDsl(Predicate predicate) {
		Optional<Time> result = timeRepository.findOne(predicate);
		if(result.isPresent() == false)
			throw new NotFoundDataException("TimeId ");
		return result.get();
	}

	@Override
	public List<Time> getListTimeByQueryDsl(Predicate predicate) {
		List<Time> list = new ArrayList<Time>();
		timeRepository.findAll(predicate).forEach(e -> list.add(e));
		return list;
	}

	@Override
	public List<Time> getTimesByQueryDslSortingMonth() {
		JPAQuery<Time> query = new JPAQuery<Time>(entityManager);
		QTime qTime = QTime.time;
		List<Time> result = query.from(qTime).orderBy(qTime.month.asc()).fetch();
		return result;
	}

	@Override
	public List<Time> getTimesByQueryDslSortingByQuarter() {
		JPAQuery<Time> query = new JPAQuery<Time>(entityManager);
		QTime qTime = QTime.time;
		List<Time> result = query.from(qTime).orderBy(qTime.quarter.asc()).fetch();
		return result;
	}

	@Override
	public List<Time> getTimesByQueryDslSortingByYear() {
		JPAQuery<Time> query = new JPAQuery<Time>(entityManager);
		QTime qTime = QTime.time;
		List<Time> result = query.from(qTime).orderBy(qTime.year.asc()).fetch();
		return result;
	}

	@Override
	public void updateByQueryDsl(UUID id, Time time) {
		JPAQueryFactory query = new JPAQueryFactory(entityManager);
		QTime qTime = QTime.time;
		query.update(qTime).where(qTime.timeId.eq(id))
		.set(qTime.month, time.getMonth())
		.set(qTime.quarter,time.getQuarter())
		.set(qTime.year, time.getYear())
		.set(qTime.modifiedAt,time.getModifiedAt())
		.set(qTime.createAt,time.getCreateAt())
		.execute();
		
	}

	@Override
	public UUID deleteByQueryDsl(UUID id) {
		JPAQueryFactory query = new JPAQueryFactory(entityManager);
		QTime qTime = QTime.time;
		query.delete(qTime).where(qTime.timeId.eq(id)).execute();
		return id;
	}

}
