package security.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class TokenNotValidExceptionMapper implements ExceptionMapper<TokenNotValidException> {

    @Override
    public Response toResponse(TokenNotValidException exception) {
        return Response.status(Response.Status.NETWORK_AUTHENTICATION_REQUIRED)
                .entity("Token is invalid. Create a new token")
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
