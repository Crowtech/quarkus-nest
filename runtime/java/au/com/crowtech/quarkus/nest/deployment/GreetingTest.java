package au.com.crowtech.quarkus.nest.deployment;

import io.quarkus.test.QuarkusUnitTest;
import io.restassured.RestAssured;
import io.vertx.core.json.JsonObject;
import life.genny.qwandautils.KeycloakUtils;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.hamcrest.Matchers.containsString;

import java.util.ArrayList;
import java.util.List;

public class GreetingTest {

  @RegisterExtension
  static final QuarkusUnitTest config = new QuarkusUnitTest()
    .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class)); 

  @Test
  public void testGreeting() {
    RestAssured.when().get("/greeting").then().statusCode(200).body(containsString("Hello")); 
  }

  
  @Test
  public void testKeycloak() {
		String keycloakUrl = System.getenv("KEYCLOAKURL");
		String clientId = "internmatch";
		
		String secret = System.getenv("CLIENT_SECRET");
		String master_admin_secret = "9e6dedaf-46f1-4f79-8a76-4bd2a942fb2f";
		String uuid = "5a666e64-021f-48ce-8111-be3d66901f9c";
		String uuid2 = "21a6da4a-1e35-4352-be65-70dcdb8494d5";
		String uuid3 = "a6445e32-a037-4e50-94a8-e8e1cee99905";
		String servicePassword = System.getenv("SERVICE_PASSWORD");
		String adminPassword = System.getenv("KEYCLOAK_PASSWORD");
		String realm = "internmatch";
		try {
			String accessToken = KeycloakUtils.getAccessToken(keycloakUrl, "internmatch", "internmatch",secret,
					"service", servicePassword);

//  	String userTokenStr = KeycloakUtils.getUserToken(keycloakUrl,uuid, serviceTokenStr, "internmatch");
//  	System.out.println(userTokenStr);
//  			    	String accessToken = null;
//			accessToken = KeycloakUtils.getAccessToken(keycloakUrl, "master",
//			 "admin-cli", null, "admin",
//			 System.getenv("KEYCLOAK_PASSWORD"));

			 String url = keycloakUrl + "/auth/admin/realms/" + realm + "/users/" + uuid;
			 String result = KeycloakUtils.sendGET(url,accessToken);

			 JsonObject userJson = new JsonObject(result);

			 String username = userJson.getString("username");

			String exchangedToken =accessToken;
			
			
			String userToken =null;//KeycloakUtils.getImpersonatedToken(keycloakUrl,
					//realm,uuid, accessToken);
		//	System.out.println("IMPERSONATE 1 "+userToken);
//			userToken = KeycloakUtils.getImpersonatedToken(keycloakUrl,
//			realm,clientId,secret, uuid, accessToken);

			System.out.println("IMPERSONATE 2 "+userToken);
			
			
			List<String> actions = new ArrayList<>();
			actions.add("UPDATE_PASSWORD");
			actions.add("VERIFY_EMAIL");
			actions.add("UPDATE_PROFILE");
			//actions.add("CONFIGURE_TOTP");
			actions.add("terms_and_conditions");
			
			KeycloakUtils.executeActions(keycloakUrl,realm, clientId, null, 600, uuid3, "https://www.aible.app", actions, exchangedToken);


		System.out.println("Keylcoak Token text");
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
  }
}