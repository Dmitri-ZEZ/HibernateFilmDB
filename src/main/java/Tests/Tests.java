package Tests;

import model.Director;
import model.HibernateUtil;
import model.Movie;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.Test;

import javax.persistence.criteria.*;
import java.util.List;

public class Tests {

    @Test
    public void query1(){
        Session session = HibernateUtil.getSessionFactory().openSession();

        Query query1 = session.createQuery("select name, year, directors.size from Movie");

        List<Object[]> movies = query1.getResultList();
        System.out.println("query1");
        for (Object[] a: movies) {
            System.out.println(a[0] + " " + a[1] + " " + a[2]);
        }

        session.close();
    }

    @Test
    public void query2(){
        Session session = HibernateUtil.getSessionFactory().openSession();

        System.out.println("query2");
        Query query2 = session.createQuery("select distinct d from Movie m join m.directors d where m.year>1980 and m.year<1989", Director.class);
        System.out.println(query2.getResultList());

        session.close();
    }

    @Test
    public void query3(){
        Session session = HibernateUtil.getSessionFactory().openSession();

        System.out.println("query3");
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Movie> query = builder.createQuery(Movie.class);
        Root<Movie> movie = query.from(Movie.class);

        query.select(movie);
        query.where(builder.between(movie.get("year"),1995,2000));

        Query<Movie> q = session.createQuery(query);
        List<Movie> list = q.getResultList();
        System.out.println(list);

        session.close();
    }

    @Test
    public void query4(){
        Session session = HibernateUtil.getSessionFactory().openSession();

        System.out.println("query4");
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Double> query = builder.createQuery(Double.class);

        Root<Movie> movie = query.from(Movie.class);
        Join<Movie, Director> director = movie.join("directors");

        query.select(builder.avg(movie.get("rank")));
        query.where(builder.equal(director.get("firstName"), "David"));

        Double q = session.createQuery(query).getSingleResult();

        System.out.println(q);

        session.close();
    }

    @Test
    public void query5(){
        Session session = HibernateUtil.getSessionFactory().openSession();

        System.out.println("query5");
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<String> query = builder.createQuery(String.class);

        Root<Movie> movie = query.from(Movie.class);
        Join<Director, Movie> director = movie.join("directors");

        query.select(director.get("lastName"));
        query.where(builder.and(builder.ge(movie.get("year"), 2000), builder.isNotNull(movie.get("rank"))));

        List<String> q = session.createQuery(query).getResultList();

        System.out.println(q);

        session.close();
    }

    @Test
    public void query6(){
        Session session = HibernateUtil.getSessionFactory().openSession();

        System.out.println("query6");
        CriteriaBuilder builder = session.getCriteriaBuilder();

        CriteriaQuery<Movie> query = builder.createQuery(Movie.class);
        Root<Movie> movie = query.from(Movie.class);

        Subquery<Double> subquery = query.subquery(Double.class);
        Root<Movie> movie2 = subquery.from(Movie.class);

        subquery.select(builder.max(movie2.get("rank")));

        query.select(movie);
        query.where(builder.equal(movie.get("rank"), subquery));

        List<Movie> q = session.createQuery(query).getResultList();

        System.out.println(q);

    }
}
