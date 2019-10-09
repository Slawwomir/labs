package issue.view;

import issue.model.Issue;
import lombok.Getter;
import lombok.Setter;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;


@Named
@RequestScoped
public class IssueView {

    @Getter
    @Setter
    private Issue issue;
}
