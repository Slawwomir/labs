package rest.resource;

import javax.ws.rs.core.SecurityContext;

public interface Secured {

    SecurityContext getSecurityContext();
}
