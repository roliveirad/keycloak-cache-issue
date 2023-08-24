package br.roliveirad.keycloak;

import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;

public class CacheIssueUserProvider implements UserStorageProvider, UserLookupProvider, CredentialInputValidator {

	private final KeycloakSession session;
	private final ComponentModel storageProviderModel;

	public CacheIssueUserProvider(KeycloakSession session, ComponentModel storageProviderModel) {
		this.session = session;
		this.storageProviderModel = storageProviderModel;
	}

	public void close() {

	}

	public UserModel getUserById(RealmModel realmModel, String id) {
		return new User(this.session, realmModel, this.storageProviderModel);
	}

	public UserModel getUserByUsername(RealmModel realmModel, String name) {
		return new User(this.session, realmModel, this.storageProviderModel);
	}

	public UserModel getUserByEmail(RealmModel realmModel, String email) {
		return null;
	}

	@Override
	public boolean supportsCredentialType(String credentialType) {
		return PasswordCredentialModel.TYPE.endsWith(credentialType);
	}

	@Override
	public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
		return supportsCredentialType(credentialType);
	}

	@Override
	public boolean isValid(RealmModel realm, UserModel user, CredentialInput credentialInput) {
		StorageId sid = new StorageId(user.getId());
		String username = sid.getExternalId();
		String password = credentialInput.getChallengeResponse();
		return "cacheissue".equals(username) && "123456".equals(password);
	}
}
