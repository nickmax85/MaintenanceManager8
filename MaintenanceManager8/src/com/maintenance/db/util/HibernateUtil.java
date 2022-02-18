package com.maintenance.db.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import com.maintenance.model.Anlage;
import com.maintenance.model.Station;
import com.maintenance.model.User;
import com.maintenance.util.ApplicationProperties;

public class HibernateUtil {

	private static final Logger logger = Logger.getLogger(HibernateUtil.class);
	private static StandardServiceRegistry registry;
	private static SessionFactory sessionFactory;

	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			try {
				StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();

				Map<String, String> settings = new HashMap<>();
				settings.put("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");

				String model = ApplicationProperties.getInstance().getProperty("db_model");
				String host = ApplicationProperties.getInstance().getProperty("db_host");
				String port = ApplicationProperties.getInstance().getProperty("db_port");
				settings.put("hibernate.connection.url", "jdbc:mysql://" + host + ":" + port + "/" + model);

				String user = ApplicationProperties.getInstance().getProperty("db_user");
				settings.put("hibernate.connection.username", user);
				String password = ApplicationProperties.getInstance().getProperty("db_password");
				settings.put("hibernate.connection.password", password);

				settings.put("hibernate.show_sql", "true");
				settings.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");

				registryBuilder.applySettings(settings);

				registry = registryBuilder.build();

				MetadataSources sources = new MetadataSources(registry).addAnnotatedClass(Anlage.class)
						.addAnnotatedClass(Station.class).addAnnotatedClass(User.class);

				Metadata metadata = sources.getMetadataBuilder().build();

				sessionFactory = metadata.getSessionFactoryBuilder().build();
			} catch (Exception e) {

				logger.error(e.getMessage());

				if (registry != null) {
					StandardServiceRegistryBuilder.destroy(registry);
				}
			}
		}
		return sessionFactory;
	}

	public static void shutdown() {
		if (registry != null) {
			StandardServiceRegistryBuilder.destroy(registry);
		}
	}
}
