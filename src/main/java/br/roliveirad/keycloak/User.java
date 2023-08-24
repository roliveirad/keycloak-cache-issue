package br.roliveirad.keycloak;

import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage;

import java.util.List;
import java.util.Map;

public class User extends AbstractUserAdapterFederatedStorage {

	public User(KeycloakSession session, RealmModel realm, ComponentModel storageProviderModel) {
		super(session, realm, storageProviderModel);
	}

	@Override
	public String getId() {
		return StorageId.keycloakId(storageProviderModel, getUsername());
	}

	@Override
	public String getFirstName() {
		return "Cache";
	}

	@Override
	public String getLastName() {
		return "Issue";
	}


	public String getUsername() {
		return "cacheissue";
	}


	public void setUsername(String s) {
	}

	@Override
	public Map<String, List<String>> getAttributes() {
		return Map.of("otp", List.of("true"));
	}
}
