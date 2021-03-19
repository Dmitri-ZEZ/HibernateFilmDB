package model;

import com.sun.xml.bind.v2.TODO;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();


        Query query1 = session.createQuery("select name, year, directors.size from Movie");

        List<Object[]> movies = query1.getResultList();
        System.out.println("query1");
        for (Object[] a: movies) {
            System.out.println(a[0] + " " + a[1] + " " + a[2]);
        }

        System.out.println("query2");
        Query query2 = session.createQuery("select distinct d from Movie m join m.directors d where m.year>1980 and m.year<1989", Director.class);
        System.out.println(query2.getResultList());


        System.out.println("query3");
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Movie> query = builder.createQuery(Movie.class);
        Root<Movie> movie = query.from(Movie.class);
        query.select(movie);
        query.where(builder.between(movie.get("year"),1995,2000));
        Query<Movie> q = session.createQuery(query);
        List<Movie> list = q.getResultList();
        System.out.println(list);





        session.getTransaction().commit();

        session.close();
        System.out.println("ok");
        System.exit(0);
    }

}
