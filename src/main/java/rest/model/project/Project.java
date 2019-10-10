package rest.model.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


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
