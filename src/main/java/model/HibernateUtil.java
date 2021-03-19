package model;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {

    private static SessionFactory sessionFactory;// настройка и работа с сессиями (фабрика сессий)

    static {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                // получение реестра сервисов (http://docs.jboss.org/hibernate/orm/5.3/integrationsGuide/html_single/)
                .configure() // настройки из hibernate.cfg.xml
                .build();
        try {
            // MetadataSources - для работы с метаданными маппинга объектов
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy( registry );
            throw new RuntimeException("There is a error building session factory");
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}
