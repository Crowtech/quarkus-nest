package au.com.crowtech.nest.utils;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import au.com.crowtech.quarkus.nest.utils.KeycloakUtils;

public class KeycloakUtilsTest {
	  @Test
	  public void testKeycloak() {
			String keycloakUrl = System.getenv("KEYCLOAK_URL");
			String realm = System.getenv("REALM");
			
			String secret = System.getenv("BACKEND_CLI_SECRET");
			String uuid = "069073e9-5cd2-48f4-96a7-0ddfb465f001";  // sonic555
			String uuid2 = "955f502f-4631-4a57-95a3-278d2ae30739"; // narelle
			String uuid3 = "0cc917bf-033b-4682-8890-3a9802260551"; // adam
			String 	uuid4 = "db001ef3-d224-4556-b752-6a2f999e6d75"; //moe
			String uuid5 = "a477c4b9-d64b-462a-a7a3-5cdc23af2074"; //rachel
			
			String adminPassword = System.getenv("KEYCLOAK_PASSWORD");
			String clientId = "mobile";
			String redirectUrl = "https://www.aible.app";
			try {
				String accessToken = KeycloakUtils.getAccessToken(keycloakUrl, realm, "backend",secret,
						"admin", adminPassword);

				List<String> actions = new ArrayList<>();
				actions.add("UPDATE_PASSWORD");
				actions.add("VERIFY_EMAIL");
				actions.add("UPDATE_PROFILE");
				//actions.add("CONFIGURE_TOTP");
				actions.add("terms_and_conditions");
				
				KeycloakUtils.executeActions(keycloakUrl,realm, clientId, null, 600, uuid3, redirectUrl, actions, accessToken);


			System.out.println("Keylcoak Token text");
			}
			catch (Exception e) {
				System.out.println(e.getMessage());
			}
	  }
}
