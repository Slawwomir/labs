package security.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class TokenDetails {
    private String id;
    private String username;
    private Set<String> roles;
    private ZonedDateTime issuedTime;
}
