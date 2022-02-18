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
import com.maintenance.model.User;

public class UserHibernateJDBCDAO implements UserHibernateDAO {

	private static final Logger logger = Logger.getLogger(UserHibernateJDBCDAO.class);

	private Session currentSession;

	@Override
	public void delete(User mitarbeiter) throws DAOException {

		Transaction tr = currentSession.beginTransaction();

		currentSession.delete(mitarbeiter);

		tr.commit();

	}

	@Override
	public User get(int id) throws DAOException {
		User data = currentSession.get(User.class, id);
		return data;

	}

	@Override
	public List<User> getAll() throws DAOException {
		Transaction transaction = null;

		try {

			transaction = currentSession.beginTransaction();

			CriteriaBuilder builder = currentSession.getCriteriaBuilder();
			CriteriaQuery<User> query = builder.createQuery(User.class);

			Root<User> root = query.from(User.class);
			query.select(root);
			query.orderBy(builder.asc(root.get("lastName")), builder.asc(root.get("mail")));

			Query<User> q = currentSession.createQuery(query);
			List<User> data = q.getResultList();

			transaction.commit();

			return data;

		} catch (Exception e) {
			e.printStackTrace();
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			currentSession.close();
		}
		return null;

	}

	@Override
	public void insert(User user) throws DAOException {

		Transaction tr = currentSession.beginTransaction();

		currentSession.save(user);

		tr.commit();

	}

	@Override
	public void update(User user) throws DAOException {

		Transaction tr = currentSession.beginTransaction();

		currentSession.update(user);

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
