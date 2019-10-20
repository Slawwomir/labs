package security;

import lombok.Data;

import java.security.Principal;
import java.util.Set;

@Data
public class ApplicationUser implements Principal {
    private final Long id;
    private final String name;
    private final Set<String> roles;
}
