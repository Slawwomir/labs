package domain.issue;

import lombok.AllArgsConstructor;
import lombok.Data;
import repository.entities.Issue;

@Data
@AllArgsConstructor
public class IssueChangedEvent {

    private Issue issue;
}
