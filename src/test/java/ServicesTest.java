import domain.issue.IssueChangedEvent;
import org.junit.jupiter.api.BeforeAll;
import service.IssueService;
import service.ProjectService;
import service.UserService;

import javax.enterprise.event.Event;
import javax.enterprise.event.NotificationOptions;
import javax.enterprise.util.TypeLiteral;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.concurrent.CompletionStage;

public abstract class ServicesTest extends AbstractJPATest {

    protected static IssueService issueService;
    protected static UserService userService;
    protected static ProjectService projectService;

    @BeforeAll
    public static void initializeServices() throws NoSuchFieldException, IllegalAccessException {
        userService = new UserService();
        final Field userEM = UserService.class.getDeclaredField("entityManager");
        userEM.setAccessible(true);
        userEM.set(userService, AbstractJPATest.entityManager);
        userEM.setAccessible(false);

        projectService = new ProjectService();
        final Field projectEM = ProjectService.class.getDeclaredField("entityManager");
        projectEM.setAccessible(true);
        projectEM.set(projectService, AbstractJPATest.entityManager);
        projectEM.setAccessible(false);

        issueService = new IssueService();
        final Field issueEM = IssueService.class.getDeclaredField("entityManager");
        issueEM.setAccessible(true);
        issueEM.set(issueService, AbstractJPATest.entityManager);
        issueEM.setAccessible(false);
        final Field issueUserService = IssueService.class.getDeclaredField("userService");
        issueUserService.setAccessible(true);
        issueUserService.set(issueService, userService);
        issueUserService.setAccessible(false);
        final Field event = IssueService.class.getDeclaredField("issueChangedEvent");
        event.setAccessible(true);
        event.set(issueService, new Event<IssueChangedEvent>() {
            @Override
            public void fire(IssueChangedEvent issueChangedEvent) {

            }

            @Override
            public <U extends IssueChangedEvent> CompletionStage<U> fireAsync(U u) {
                return null;
            }

            @Override
            public <U extends IssueChangedEvent> CompletionStage<U> fireAsync(U u, NotificationOptions notificationOptions) {
                return null;
            }

            @Override
            public Event<IssueChangedEvent> select(Annotation... annotations) {
                return null;
            }

            @Override
            public <U extends IssueChangedEvent> Event<U> select(Class<U> aClass, Annotation... annotations) {
                return null;
            }

            @Override
            public <U extends IssueChangedEvent> Event<U> select(TypeLiteral<U> typeLiteral, Annotation... annotations) {
                return null;
            }
        });
        event.setAccessible(false);
    }
}
