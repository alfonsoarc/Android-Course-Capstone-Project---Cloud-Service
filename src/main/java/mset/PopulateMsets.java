package mset;

import integration.test.UnsafeHttpsClient;

import java.util.ArrayList;
import java.util.Collection;

import mset.client.MutiboSvcApi;
import mset.client.SecuredRestBuilder;
import mset.repository.Mset;
import retrofit.client.ApacheClient;

public class PopulateMsets {

	private static MutiboSvcApi readWriteMutiboSvcUser1;
	private final static String TEST_URL = "https://localhost:8443";
	private final static String USERNAME1 = "admin";
	private final static String PASSWORD = "pass";
	private final static String CLIENT_ID = "mobile";

	public static void main(String[] args) {

		
		readWriteMutiboSvcUser1 = new SecuredRestBuilder()
				.setClient(
						new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
				.setEndpoint(TEST_URL)
				.setLoginEndpoint(TEST_URL + MutiboSvcApi.TOKEN_PATH)
				// .setLogLevel(LogLevel.FULL)
				.setUsername(USERNAME1).setPassword(PASSWORD)
				.setClientId(CLIENT_ID).build().create(MutiboSvcApi.class);
		
		Collection<Mset> msets = readWriteMutiboSvcUser1.getMsetList();
		for(Mset mset : msets) {
			readWriteMutiboSvcUser1.deleteMset(mset.getId());
		}
		
		addMset(
				"Shutter island",
				"The wolf of wall street",
				"Saving Private Ryan",
				"Taxi driver",
				2,
				"The correct aswer was Saving Private Ryan because is the only one whose director is not Martin Scorsese."
		);
		
		addMset(
				"Pocahontas",
				"Prince of Persia: The sands of time",
				"The avengers",
				"Toy Story",
				3,
				"The correct answer is Toy Story because is the only one from Pixar."
		);
		
		addMset(
				"The battle of the five armies",
				"The lord of the rings",
				"The two towers",
				"The return of the King",
				0,
				"The correct answer is The battle of the five armies as it is the only non Lord of the rings related movie."
		);
		
		addMset(
				"Titanic",
				"Inception",
				"The great Gatsby",
				"Gladiator",
				3,
				"The correct answer is Gladiator as it is the only movie without Leonardo DiCaprio."
		);
		
		addMset(
				"Life is Beatiful",
				"Her",
				"Amelie",
				"All about my mother",
				1,
				"The correct answer is Her, as it is the only American movie."
		);
		
		addMset(
				"Scary movie",
				"Scream",
				"The Texas chain Masacre",
				"Halloween",
				0,
				"The correct answer is Scary Movie because it is not a horror movie."
		);
		
		addMset(
				"Pearl Harbor",
				"Schindler's List",
				"Enemy at the gates",
				"Transcedence",
				3,
				"The correct answer is Trascendence. It is not based on the second world war."
		);
		
		addMset(
				"Bethoven",
				"Mighty Joe Young",
				"Eight below",
				"Hachiko",
				1,
				"The correct answer is Mighty Joe Young. It is the only one where the main character is not a dog."
		);
		
		addMset(
				"Independence day",
				"Signs",
				"Oblivion",
				"E.T",
				2,
				"The correct answer is Oblivion, which is the only movie that is is not about aliens."
		);

		addMset(
				"Practical magic",
				"Cold mountain",
				"Speed",
				"Premonition",
				1,
				"The correct answer is Cold mountain. In the other three the main actress is Sandra Bullock."
		);
		
		
	}
	
	private static void addMset(String movie1, String movie2, String movie3, String movie4, int intruder, String explanation) {
		ArrayList<String> movies = new ArrayList<String>();
		movies.add(movie1);
		movies.add(movie2);
		movies.add(movie3);
		movies.add(movie4);
		
		readWriteMutiboSvcUser1.addMset(new Mset(movies, intruder, explanation));
	}

}
