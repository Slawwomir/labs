import org.junit.jupiter.api.Test;
import repository.entities.Issue;

public class EntitiesJPATest extends AbstractJPATest {

    @Test
    public void testSomething() {
        Issue issue = entityManager.find(Issue.class, 1L);
        System.out.println(issue);
    }
}
