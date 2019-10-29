import org.h2.tools.RunScript;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public abstract class AbstractJPATest {

    protected static EntityManagerFactory entityManagerFactory;
    protected static EntityManager entityManager;

    @BeforeAll
    public static void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory("krotka");
        entityManager = entityManagerFactory.createEntityManager();
    }

    @BeforeEach
    public void resetDatabase() {
        Session session = entityManager.unwrap(Session.class);
        session.doWork(connection -> {
            try {
                File data = new File(getClass().getResource("/META-INF/sql/data.sql").getPath());
                RunScript.execute(connection, new FileReader(data));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @AfterAll
    public static void tearDown() {
        entityManager.clear();
        entityManager.close();
        entityManagerFactory.close();
    }
}
