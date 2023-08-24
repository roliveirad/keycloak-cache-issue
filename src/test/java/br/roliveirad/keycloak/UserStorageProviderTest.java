package br.roliveirad.keycloak;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static org.hamcrest.Matchers.notNullValue;

class UserStorageProviderTest {

    private static final String DEFAULT_CACHE_REALM_JSON = "default-cache-realm.json";

    private static final String NO_CACHE_REALM_JSON = "no-cache-realm.json";

    private static final String DOCKER_IMAGE = "quay.io/keycloak/keycloak:latest";

    private static final KeycloakContainer KEYCLOAK_CONTAINER = new KeycloakContainer(DOCKER_IMAGE)
            .withRealmImportFiles(NO_CACHE_REALM_JSON, DEFAULT_CACHE_REALM_JSON)
            .withProviderClassesFrom("target/classes");

    @BeforeAll
    public static void before() {
        KEYCLOAK_CONTAINER.start();
    }

    @AfterAll
    public static void after() {
        KEYCLOAK_CONTAINER.stop();
    }

    @Nested
    class GivenStorageProviderWithCachePolicyNoCache {

        private static String CREATE_TOKEN_NO_CACHE_ENDPOINT = "realms/no-cache-realm/protocol/openid-connect/token";

        @Test
        void givenUserWithOtpRequired_WhenMissingTotp_ShouldReturnUnauthorized() {
            given().when()
                    .contentType(ContentType.URLENC)
                    .accept("*/*")
                    .formParam("grant_type", "password")
                    .formParam("client_id", KeycloakContainer.ADMIN_CLI_CLIENT)
                    .formParam("username", "cacheissue")
                    .formParam("password", "123456")
                    .formParam("scope", "openid")
                    .post(KEYCLOAK_CONTAINER.getAuthServerUrl() + CREATE_TOKEN_NO_CACHE_ENDPOINT)
                    .then().statusCode(UNAUTHORIZED.getStatusCode())
                    .body("error_description", Matchers.equalTo("Invalid user credentials"));
        }

        @Test
        void givenUserWithOtpRequired_WhenSendingTotp_ShouldReturnOk() {
            given().when()
                    .contentType(ContentType.URLENC)
                    .accept("*/*")
                    .formParam("grant_type", "password")
                    .formParam("client_id", KeycloakContainer.ADMIN_CLI_CLIENT)
                    .formParam("username", "cacheissue")
                    .formParam("password", "123456")
                    .formParam("scope", "openid")
                    .formParam("totp", "123456")
                    .post(KEYCLOAK_CONTAINER.getAuthServerUrl() + CREATE_TOKEN_NO_CACHE_ENDPOINT)
                    .then().statusCode(OK.getStatusCode())
                    .body("id_token", notNullValue())
                    .body("access_token", notNullValue());
        }
    }

    @Nested
    class GivenStorageProviderWithCachePolicyDefault {

        private static String CREATE_TOKEN_DEFAULT_CACHE_ENDPOINT = "realms/default-cache-realm/protocol/openid-connect/token";

        @Test
        void givenUserWithOtpRequired_WhenMissingTotp_ShouldReturnUnauthorized() {
            given().when()
                    .contentType(ContentType.URLENC)
                    .accept("*/*")
                    .formParam("grant_type", "password")
                    .formParam("client_id", KeycloakContainer.ADMIN_CLI_CLIENT)
                    .formParam("username", "cacheissue")
                    .formParam("password", "123456")
                    .formParam("scope", "openid")
                    .post(KEYCLOAK_CONTAINER.getAuthServerUrl() + CREATE_TOKEN_DEFAULT_CACHE_ENDPOINT)
                    .then().statusCode(UNAUTHORIZED.getStatusCode())
                    .body("error_description", Matchers.equalTo("Invalid user credentials"));
        }

        @Test
        void givenUserWithOtpRequired_WhenSendingTotp_ShouldReturnOk() {
            given().when()
                    .contentType(ContentType.URLENC)
                    .accept("*/*")
                    .formParam("grant_type", "password")
                    .formParam("client_id", KeycloakContainer.ADMIN_CLI_CLIENT)
                    .formParam("username", "cacheissue")
                    .formParam("password", "123456")
                    .formParam("scope", "openid")
                    .formParam("totp", "123456")
                    .post(KEYCLOAK_CONTAINER.getAuthServerUrl() + CREATE_TOKEN_DEFAULT_CACHE_ENDPOINT)
                    .then().statusCode(OK.getStatusCode())
                    .body("id_token", notNullValue())
                    .body("access_token", notNullValue());
        }

    }




}
