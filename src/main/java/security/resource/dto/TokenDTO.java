package security.resource.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {
    private String token;
    private String username;
    private Long id;
    private List<String> roles;
}
