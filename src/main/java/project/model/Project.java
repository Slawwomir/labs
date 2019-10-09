package project.model;

import issue.model.Issue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Project {

    private Long id;
    private String name;

    public Project(Project project) {
        this.id = project.id;
        this.name = project.name;
    }
}
