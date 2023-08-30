package br.roliveirad.keycloak;

import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

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

	/**
	 * Jeph's note: both {@link AbstractUserAdapterFederatedStorage#getAttributes} and {@link AbstractUserAdapterFederatedStorage#getAttributeStream}
	 * from the parent class retrieves the 'raw' fields of the user from the federated storage.
	 * Unfortunately, the getAttributeStream does not create a stream out of getAttributes by default*
	 */
	@Override
	public Stream<String> getAttributeStream(String name) {
		Map<String, List<String>> attributes = this.getAttributes();
		return attributes.keySet().stream().filter(attribute -> Objects.equals(attribute, name)).
				map(attribute -> attributes.get(attribute).stream().findFirst().orElse(null));
	}
}
