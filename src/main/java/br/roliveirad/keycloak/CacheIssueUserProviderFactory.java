package br.roliveirad.keycloak;

import com.google.auto.service.AutoService;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.storage.UserStorageProviderFactory;

@AutoService(UserStorageProviderFactory.class)
public class CacheIssueUserProviderFactory implements UserStorageProviderFactory<CacheIssueUserProvider> {

	public CacheIssueUserProvider create(KeycloakSession keycloakSession, ComponentModel componentModel) {
		return new CacheIssueUserProvider(keycloakSession, componentModel);
	}

	public String getId() {
		return "CacheIssueUserProvider";
	}
}
