package it.kdm.docer.registrazione.database;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

@Repository
public class Registrazioni {

    private final static Logger logger	= LoggerFactory.getLogger(Registrazioni.class);

    @PersistenceContext
    transient EntityManager em;

    public Registro cercaRegistro(Registro registro) {
        Registro r=null;
        try {
            em = em.getEntityManagerFactory().createEntityManager();
            Session s = (Session) em.getDelegate();
            TypedQuery<Registro> query = em.createQuery("SELECT r FROM Registro r WHERE r.docId = :docId and r.registro = :registro", Registro.class);
            query.setParameter("docId", registro.getDocId());
            query.setParameter("registro", registro.getRegistro());
            query.setMaxResults(1);
            try {
                List<Registro> registri = query.getResultList();
                if (registri != null && registri.size() > 0) {
                    r = registri.get(0);
                }
            } catch (EmptyResultDataAccessException ex) {
                if (registro != null)
                    logger.warn("registro not found:" + registro.getRegistro());
                else
                    logger.warn("registro not specify value null");
            } catch (NoResultException ex) {
                if (registro != null)
                    logger.warn("registro not found:" + registro.getRegistro());
                else
                    logger.warn("registro not specify value null");
            }
        }catch (Exception a){
            a.printStackTrace();
        }
        return r;
    }

    public long numeroRegistriPrecedenti(Registro registro, boolean perpetuo) {

        String jpql = "SELECT COUNT(r) FROM Registro r WHERE r.ente=:ente and r.aoo=:aoo " + "and r.registro=:registro and r.anno=:anno and r.id<=:id";

        if (perpetuo) {
            jpql = "SELECT COUNT(r) FROM Registro r WHERE r.ente=:ente and r.aoo=:aoo and r.registro=:registro and r.id<=:id";
        }
        em = em.getEntityManagerFactory().createEntityManager();
        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        query.setParameter("ente", registro.getEnte());
        query.setParameter("aoo", registro.getAoo());
        query.setParameter("registro", registro.getRegistro());
        if (!perpetuo) {
            query.setParameter("anno", registro.getAnno());
        }
        query.setParameter("id", registro.getId());

        List<Long> ress = query.getResultList();
        Long res=0L;
        if(ress!=null && ress.size()>0)
            res = ress.get(0);
        em.close();
		return res;
    }

    public void persistiRegistro(Registro registro) {
		Session session = null;
		Transaction tx = null;
		try {
            if(registro.getAnno()!= null && registro.getAnno()>2000){
                Integer annoLast = registro.getAnno();
                registro.setData(Calendar.getInstance());
                registro.setAnno(annoLast);
            }else{
                registro.setData(Calendar.getInstance());

            }
			em = em.getEntityManagerFactory().createEntityManager();
			session = (Session)em.getDelegate();
			tx = session.beginTransaction();
			session.persist(registro);
			tx.commit();
			session.flush();
		}catch (Exception e){
			e.printStackTrace();
			tx.rollback();
		}finally {
			try{
				session.close();
			}catch (Exception e1) {
				e1.printStackTrace();
			}
		}
    }

	public Registro updateRegistro(Registro registro) {
		Session session = null;
    	Transaction tx = null;
    	try {
			em = em.getEntityManagerFactory().createEntityManager();
			session = (Session) em.getDelegate();
			tx = session.beginTransaction();
			registro = em.merge(registro);
			tx.commit();
			session.flush();
		}catch(Exception e){
    		tx.rollback();
		}finally {
    		try{
    			session.close();
			}catch (Exception e1){
    			e1.printStackTrace();
			}
		}
	return registro;
    }


	public Registro registra(Registro registro) throws IOException {
		return registra(registro, false);
	}

    public Registro registra(Registro registro, boolean perpetuo) throws IOException {
	
	Registro old = cercaRegistro(registro);
	if (old == null) {
	    assert registro.getId() == null;
	    persistiRegistro(registro);

	}
	else {
	    old.setOggetto(registro.getOggetto());
	    registro = old;
	}

	assert registro.getId() != null;

	if (registro.getNumero() == null) {
	    long numero = numeroRegistriPrecedenti(registro, perpetuo);
	    registro.setNumero(numero);
	}

	updateRegistro(registro);

	Long record_id = registro.getId();
	// bug fix veloce: la data letta e' recuperata diversa dopo averla
	// scritta
	// eseguo quindi la ricerca dopo l'operazione per essere sicuri di
	// restituire
	// l'effettivo valore inserito nel DB
	registro = cercaRegistro(registro);
	if (registro == null) {
	    throw new IOException("it.kdm.docer.registrazione.database: record " + record_id + " non trovato dopo creazione su tabella 'registro'");
	}
	return registro;

    }
}
