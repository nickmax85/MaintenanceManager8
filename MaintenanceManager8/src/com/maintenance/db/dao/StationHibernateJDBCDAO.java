package com.maintenance.db.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.maintenance.db.util.DAOException;
import com.maintenance.db.util.HibernateUtil;
import com.maintenance.model.Station;

public class StationHibernateJDBCDAO implements StationHibernateDAO {

	private static final Logger logger = Logger.getLogger(AnlageJDBCDAO.class);

	private Session currentSession;
	private Transaction currentTransaction;

	@Override
	public void delete(Station data) throws DAOException {

		Transaction tr = currentSession.beginTransaction();

		currentSession.delete(data);

		tr.commit();

	}

	@Override
	public Station get(int id) throws DAOException {
		Station data = currentSession.get(Station.class, id);
		return data;

	}

	@Override
	public List<Station> getAll() throws DAOException {
		Transaction transaction = null;

		try {

			transaction = currentSession.beginTransaction();

			CriteriaBuilder builder = currentSession.getCriteriaBuilder();
			CriteriaQuery<Station> query = builder.createQuery(Station.class);

			Root<Station> root = query.from(Station.class);
			query.select(root);

			Query<Station> q = currentSession.createQuery(query);
			List<Station> dataen = q.getResultList();

			transaction.commit();

			return dataen;

		} catch (Exception e) {
			e.printStackTrace();
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {

		}
		return null;

	}

	@Override
	public void insert(Station data) throws DAOException {

		Transaction tr = currentSession.beginTransaction();

		currentSession.save(data);

		tr.commit();

	}

	@Override
	public void update(Station data) throws DAOException {

		Transaction tr = currentSession.beginTransaction();

		currentSession.update(data);

		tr.commit();

	}

	public Session openCurrentSession() {

		currentSession = HibernateUtil.getSessionFactory().openSession();

		return currentSession;

	}

	public void closeCurrentSession() {

		currentSession.close();

	}

}
