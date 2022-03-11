package it.kdm.docer.parer.daemon.database;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JobManager {

	@PersistenceContext
	transient private EntityManager em;
	
	private <T> T getSingleResult(TypedQuery<T> query) {
    	try {
    		query.setMaxResults(1);
    		return query.getSingleResult();
    	} catch (EmptyResultDataAccessException ex) {
    		return null;
    	} catch (NoResultException ex) {
    		return null;
    	}
    }
	
	public Job findJob(Long jobId) {
		if (jobId == null) {
			throw new IllegalArgumentException("Cannot find a Job whose id is null");
		}
		
		return em.find(Job.class, jobId);
	}
    
    public Job findJobByDoc(String docId, char azione) {
        if (docId == null) return null;
        TypedQuery<Job> query = em.createQuery("SELECT o FROM Job o WHERE o.docId=:id and o.azione=:azione", Job.class);
        query.setParameter("id", docId);
        query.setParameter("azione", azione);
        return getSingleResult(query);
    }
    
    @Transactional
    public List<Job> getJobsToProcess() {
    	TypedQuery<Job> query = em.createQuery("SELECT o FROM Job o WHERE o.stato='A' " +
				"order by o.docId, o.dataInserimento", Job.class);
    	return query.getResultList();
    }
    
    @Transactional
    public List<Job> getJobsToProcess(int maxResults) {
    	TypedQuery<Job> query = em.createQuery("SELECT o FROM Job o WHERE o.stato='A' " +
				"order by o.docId, o.dataInserimento", Job.class);
    	query.setMaxResults(maxResults);
    	return query.getResultList();
    }
    
    public List<Job> getInterruptedJobs() {
    	TypedQuery<Job> query = em.createQuery("SELECT o FROM Job o WHERE o.stato='W' OR o.stato='X' " +
    											"order by o.docId, o.dataInserimento", Job.class);
    	return query.getResultList();
	}

	public List<Log> search(Calendar startDate, Calendar endDate,
							Character esito, String docId, String descrizione,
							String docType, String errCode,
							String ente, String aoo, int maxRows) {
    	if(startDate == null) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, 0);
    		startDate = cal;
    	}

    	if(endDate == null) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, 9999);
    		endDate = cal;
    	}

    	CriteriaBuilder cb = em.getCriteriaBuilder();

    	CriteriaQuery<Log> q = cb.createQuery(Log.class);
    	Root<Log> log = q.from(Log.class);

    	Expression<Calendar> dataExpr = log.get("dataChiamata");

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(cb.between(dataExpr, cb.literal(startDate),
    									   cb.literal(endDate)));

    	if(esito != null) {
    		Expression<String> expr = log.get("job").get("stato");
    		predicates.add(cb.equal(expr, cb.literal(esito)));
    	}

    	if(docId != null && !docId.isEmpty()) {
    		Expression<Long> expr = log.get("job").get("docId");
    		predicates.add(cb.equal(expr, cb.literal(docId)));
    	}

    	if(docType != null && !docType.equals("")) {
    		Expression<Long> expr = log.get("job").get("tipoDocumento");
    		predicates.add(cb.equal(expr, cb.literal(docType)));
    	}

        if(errCode != null && !errCode.isEmpty()) {
            Expression<String> expr = log.get("errorCode");
            predicates.add(cb.equal(expr, cb.literal(errCode)));
        }

		if (StringUtils.isNotEmpty(ente)) {
			Expression<String> expr = log.get("job").get("codEnte");
			predicates.add(cb.equal(expr, cb.literal(ente)));
		}

		if (StringUtils.isNotEmpty(aoo)) {
			Expression<String> expr = log.get("job").get("codAoo");
			predicates.add(cb.equal(expr, cb.literal(aoo)));
		}

		if (StringUtils.isNotEmpty(descrizione)) {
			Expression<String> expr = log.get("message");
			predicates.add(cb.like(expr, "%" + descrizione + "%"));
		}

		q = q.select(log).where(predicates.toArray(new Predicate[predicates.size()]));

        q = q.orderBy(cb.asc(log.get("job").get("id")),
                  cb.asc(dataExpr));

		q = q.groupBy(log.get("job").get("id"));

    	//TODO: errCode
    	TypedQuery<Log> query = em.createQuery(q).setMaxResults(maxRows);
    	return query.getResultList();
    }

    public List<Job> searchJobs(Calendar startDate, Calendar endDate,
								Character esito, String docId, String descrizione,
								String docType, String errCode,
								String ente, String aoo, int maxRows) {
    	if(startDate == null) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, 0);
    		startDate = cal;
    	}
    	
    	if(endDate == null) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, 9999);
    		endDate = cal;
    	}
    	
    	CriteriaBuilder cb = em.getCriteriaBuilder();
    	
    	CriteriaQuery<Job> q = cb.createQuery(Job.class);
    	Root<Job> job = q.from(Job.class);

    	Expression<Calendar> dataExpr = job.get("dataChiamata");

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(cb.between(dataExpr, cb.literal(startDate),
    									   cb.literal(endDate)));

    	if(esito != null) {
    		Expression<String> expr = job.get("stato");
    		predicates.add(cb.equal(expr, cb.literal(esito)));
    	}
    	
    	if(docId != null && !docId.isEmpty()) {
    		Expression<Long> expr = job.get("docId");
    		predicates.add(cb.equal(expr, cb.literal(docId)));
    	}
    	
    	if(docType != null && !docType.equals("")) {
    		Expression<Long> expr = job.get("tipoDocumento");
    		predicates.add(cb.equal(expr, cb.literal(docType)));
    	}

        if(errCode != null && !errCode.isEmpty()) {
            Expression<String> expr = job.get("errorCode");
            predicates.add(cb.equal(expr, cb.literal(errCode)));
        }

		if (StringUtils.isNotEmpty(descrizione)) {
			Expression<String> expr = job.get("message");
			predicates.add(cb.like(expr, "%" + descrizione + "%"));
		}

		if (StringUtils.isNotEmpty(ente)) {
			Expression<String> expr = job.get("codEnte");
			predicates.add(cb.equal(expr, cb.literal(ente)));
		}

		if (StringUtils.isNotEmpty(aoo)) {
			Expression<String> expr = job.get("codAoo");
			predicates.add(cb.equal(expr, cb.literal(aoo)));
		}

		q = q.select(job).where(predicates.toArray(new Predicate[predicates.size()]));

        q.orderBy(cb.asc(job.get("id")),
                  cb.asc(dataExpr));

    	//TODO: errCode
    	TypedQuery<Job> query = em.createQuery(q).setMaxResults(maxRows);
    	return query.getResultList();
    }
    
    public Log findLog(Long logId) {
		if (logId == null) {
			throw new IllegalArgumentException("Cannot find a Log whose id is null");
		}
		
		return em.find(Log.class, logId);
	}
	
    public List<Log> findLogByDoc(String docId, char azione) {
        if (docId == null) return null;
        TypedQuery<Log> query = em.createQuery("SELECT o FROM Log o WHERE o.job.docId=:id and o.job.azione=:azione", Log.class);
        query.setParameter("id", docId);
        query.setParameter("azione", azione);
        return query.getResultList();
    }
    
    public List<Log> findLogByJob(Long idJob) {
        if (idJob == null) return null;
        TypedQuery<Log> query = em.createQuery("SELECT o FROM Log o WHERE o.job.id=:id", Log.class);
        query.setParameter("id", idJob);
        return query.getResultList();
    }

    @Transactional
	public void saveJob(Job job) {
		em.persist(job);
	}

    @Transactional
	public Job updateJob(Job job) {
		return em.merge(job);
	}
    
    @Transactional
    public void removeJob(Long idJob) {
    	Job job = findJob(idJob);
    	em.remove(job);
    }
    
	@Transactional
	public void saveLog(Log log) {
		em.persist(log);
	}

    @Transactional
	public Log updateLog(Log log) {
        Job job = log.getJob();

        Log updatedLog = em.merge(log);

        if (job != null) {
			job.setErrorCode(log.getErrorCode());
			job.setErrorMessage(log.getMessage());
            em.merge(job);
        }

        return updatedLog;
	}
    
    @Transactional
    public void removeLog(Long idLog) {
    	Log log = findLog(idLog);
    	em.remove(log);
    }
    
    @Transactional
    public void flush() {
    	em.flush();
    }
}
