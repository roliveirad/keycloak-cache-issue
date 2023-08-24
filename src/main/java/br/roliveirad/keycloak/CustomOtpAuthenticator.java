package br.roliveirad.keycloak;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.representations.idm.OAuth2ErrorRepresentation;

public class CustomOtpAuthenticator implements Authenticator {

    public void authenticate(AuthenticationFlowContext context) {

        MultivaluedMap<String, String> formParameters = context.getHttpRequest().getDecodedFormParameters();

        String otp = formParameters.getFirst("totp");
        if (otp == null) {
            Response challengeResponse = errorResponse(Response.Status.UNAUTHORIZED.getStatusCode(), "invalid_grant", "Invalid user credentials");
            context.failure(AuthenticationFlowError.INVALID_USER, challengeResponse);
            return;
        }
        boolean valid = otp.equals("123456");
        if (!valid) {
            Response challengeResponse = errorResponse(Response.Status.UNAUTHORIZED.getStatusCode(), "invalid_grant", "Invalid user credentials");
            context.failure(AuthenticationFlowError.INVALID_USER, challengeResponse);
            return;
        }

        context.success();
    }


    public void action(AuthenticationFlowContext context) {

    }


    public boolean requiresUser() {
        return false;
    }


    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
        return false;
    }


    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {

    }


    public void close() {

    }

    private Response errorResponse(int status, String error, String errorDescription) {
        OAuth2ErrorRepresentation errorRep = new OAuth2ErrorRepresentation(error, errorDescription);
        return Response.status(status).entity(errorRep).type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}
