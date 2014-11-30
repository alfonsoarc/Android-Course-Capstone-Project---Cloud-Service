package mset;

import integration.test.UnsafeHttpsClient;
import mset.client.MutiboSvcApi;
import mset.client.SecuredRestBuilder;
import retrofit.client.ApacheClient;

public class PopulateScores {
	private final static String TEST_URL = "https://localhost:8443";
	private final static String PASSWORD = "pass";
	private final static String CLIENT_ID = "mobile";

	public static void main(String[] args) {
		
		MutiboSvcApi alice = createNewMutiboUser("alicehouse");
		MutiboSvcApi charles = createNewMutiboUser("charles_88");
		MutiboSvcApi alfonso = createNewMutiboUser("alfonsor");
		MutiboSvcApi nerea = createNewMutiboUser("dugesia");
		MutiboSvcApi john = createNewMutiboUser("johnQ");
		
		alice.saveScore(10);
		charles.saveScore(20);
		alfonso.saveScore(30);
		john.saveScore(20);
		charles.saveScore(40);
		alice.saveScore(50);
		charles.saveScore(60);
		john.saveScore(70);
		charles.saveScore(80);
		nerea.saveScore(90);
		alfonso.saveScore(100);
	}
	
	private static MutiboSvcApi createNewMutiboUser(String username) {
		return new SecuredRestBuilder()
		.setClient(
				new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
		.setEndpoint(TEST_URL)
		.setLoginEndpoint(TEST_URL + MutiboSvcApi.TOKEN_PATH)
		// .setLogLevel(LogLevel.FULL)
		.setUsername(username).setPassword(PASSWORD)
		.setClientId(CLIENT_ID).build().create(MutiboSvcApi.class);
	}

}
