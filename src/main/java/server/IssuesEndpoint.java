package server;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import domain.issue.IssueChangedEvent;
import repository.entities.Issue;
import service.PermissionService;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint(value = "/issues")
@Dependent
public class IssuesEndpoint implements Serializable {

    private static Set<IssuesEndpoint> clients = new HashSet<>();

    private Session session;
    private Long userId;
    private Long projectId;

    @Inject
    private PermissionService permissionService;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        clients.add(this);
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        message = message.substring(1, message.length() - 1);
        message = message.replace("\\", "");
        JsonObject jsonObject = new JsonParser().parse(message).getAsJsonObject();
        this.userId = jsonObject.get("userId").getAsLong();
        this.projectId = jsonObject.get("projectId").getAsLong();
    }

    @OnClose
    public void onClose(Session session) {
        clients.remove(this);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
    }

    private boolean isIssueInProject(Issue issue, Long projectId) {
        return issue.getProject().getId().equals(projectId);
    }

    private boolean hasPermissionToIssue(Issue issue, Long userId) {
        return permissionService.hasUserPermissionToIssue(userId, issue.getId(), "getIssue");
    }

    public static void onIssueChanged(@Observes IssueChangedEvent event) {
        clients.parallelStream()
                .filter(client -> client.hasPermissionToIssue(event.getIssue(), client.userId))
                .filter(client -> client.isIssueInProject(event.getIssue(), client.projectId))
                .forEach(client -> {
                    try {
                        client.session.getBasicRemote().sendText("{\"refresh\": \"true\"}"); // ping
                    } catch (IOException e) {
                        throw new RuntimeException("Cannot send text to remote client", e);
                    }
                });
    }
}
