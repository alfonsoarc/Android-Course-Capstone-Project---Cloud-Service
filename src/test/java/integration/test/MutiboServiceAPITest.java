package integration.test;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;
import mset.client.MutiboSvcApi;
import mset.client.SecuredRestBuilder;
import mset.repository.Mset;

import org.apache.http.HttpStatus;
import org.junit.Test;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.RetrofitError;
import retrofit.client.ApacheClient;

/**
 * A test for the Mutibo Service API
 */
public class MutiboServiceAPITest extends TestCase {
	private final String TEST_URL = "https://localhost:8443";

	private final String USERNAME1 = "admin";
	private final String USERNAME2 = "dugesia";
	private final String PASSWORD = "pass";
	
	private final String CLIENT_ID = "mobile";

	private MutiboSvcApi readWriteMutiboSvcUser1 = new SecuredRestBuilder()
			.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
			.setEndpoint(TEST_URL)
			.setLoginEndpoint(TEST_URL + MutiboSvcApi.TOKEN_PATH)
			// .setLogLevel(LogLevel.FULL)
			.setUsername(USERNAME1).setPassword(PASSWORD)
			.setClientId(CLIENT_ID).build().create(MutiboSvcApi.class);

	private MutiboSvcApi readWriteMutiboSvcUser2 = new SecuredRestBuilder()
			.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
			.setEndpoint(TEST_URL)
			.setLoginEndpoint(TEST_URL + MutiboSvcApi.TOKEN_PATH)
			// .setLogLevel(LogLevel.FULL)
			.setUsername(USERNAME2).setPassword(PASSWORD)
			.setClientId(CLIENT_ID).build().create(MutiboSvcApi.class);

	private ErrorRecorder error = new ErrorRecorder();

	MutiboSvcApi insecureMutiboService = new RestAdapter.Builder()
			.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
			.setEndpoint(TEST_URL).setLogLevel(LogLevel.FULL)
			.setErrorHandler(error).build().create(MutiboSvcApi.class);

	private Mset createMsetForTesting() {
		ArrayList<String> movies = new ArrayList<String>(4);
		movies.add("movie1");
		movies.add("movie2");
		movies.add("movie3");
		movies.add("movie4");
		Mset mset = new Mset(movies, 3, "Movie 4 is the only one made in USA");
		return mset;
	}

	private void cleanMsetRepository() {
		Collection<Mset> stored = readWriteMutiboSvcUser1.getMsetList();
		for (Mset mset : stored) {
			readWriteMutiboSvcUser1.deleteMset(mset.getId());
		}
	}

	@Override
	protected void tearDown() {
		cleanMsetRepository();
	}

	@Test
	public void testAddMset() throws Exception {
		Mset mset = createMsetForTesting();
		Mset addedMset = readWriteMutiboSvcUser1.addMset(mset);
		assertTrue(mset.equals(addedMset));
	}

	@Test
	public void testAddAndGetMset() throws Exception {
		Mset mset = createMsetForTesting();
		Mset addedMset = readWriteMutiboSvcUser1.addMset(mset);
		Collection<Mset> stored = readWriteMutiboSvcUser1.getMsetList();
		assertTrue(stored.contains(addedMset));
	}

	@Test
	public void testGetMsetById() throws Exception {
		Mset mset = createMsetForTesting();
		Mset addedMset = readWriteMutiboSvcUser1.addMset(mset);
		Mset byIdMset = readWriteMutiboSvcUser1.getMsetById(addedMset.getId());
		assertTrue(addedMset.equals(byIdMset));

		byIdMset = readWriteMutiboSvcUser1.getMsetById(addedMset.getId() + 100);
		assertTrue(byIdMset == null);
	}

	@Test
	public void testDeleteMset() throws Exception {
		Mset mset = createMsetForTesting();
		Mset addedMset = readWriteMutiboSvcUser1.addMset(mset);
		Collection<Mset> stored = readWriteMutiboSvcUser1.getMsetList();
		assertTrue(stored.contains(addedMset));

		boolean result = readWriteMutiboSvcUser1.deleteMset(addedMset.getId());
		stored = readWriteMutiboSvcUser1.getMsetList();
		assertTrue(result);
		assertFalse(stored.contains(addedMset));

		result = readWriteMutiboSvcUser1.deleteMset(addedMset.getId());
		assertFalse(result);
	}

	@Test
	public void testRateMset() throws Exception {
		Mset mset = createMsetForTesting();

		Mset addedMset = readWriteMutiboSvcUser1.addMset(mset);
		assertTrue(addedMset.getNegatives() == 0);

		readWriteMutiboSvcUser1.rateMset(addedMset.getId(), 2);
		addedMset = readWriteMutiboSvcUser1.getMsetById(addedMset.getId());
		assertTrue(addedMset.getNegatives() == 0);

		readWriteMutiboSvcUser1.rateMset(addedMset.getId(), 1);
		addedMset = readWriteMutiboSvcUser1.getMsetById(addedMset.getId());
		assertTrue(addedMset.getNegatives() == 1);
	}

	@Test
	public void testNegatives() throws Exception {
		Mset mset = createMsetForTesting();

		Mset addedMset = readWriteMutiboSvcUser1.addMset(mset);

		readWriteMutiboSvcUser1.rateMset(addedMset.getId(), 1);
		Collection<Mset> stored = readWriteMutiboSvcUser1
				.findByNegativesGreaterThan(1);
		assertTrue(stored.size() == 0);

		readWriteMutiboSvcUser1.rateMset(addedMset.getId(), 1);
		stored = readWriteMutiboSvcUser1.findByNegativesGreaterThan(1);
		assertTrue(stored.size() == 1);
	}

	@Test
	public void testScore() throws Exception {
		// No scores
		assertTrue(readWriteMutiboSvcUser1.getMaximumScore() == 0);
		assertTrue(readWriteMutiboSvcUser1.getScoreList().size() == 0);

		// Check one score
		readWriteMutiboSvcUser1.saveScore(5);
		assertTrue(readWriteMutiboSvcUser1.getMaximumScore() == 5);

		// Check one score
		readWriteMutiboSvcUser1.saveScore(10);
		assertTrue(readWriteMutiboSvcUser1.getMaximumScore() == 10);

		// Check one score
		readWriteMutiboSvcUser1.saveScore(2);
		assertTrue(readWriteMutiboSvcUser1.getMaximumScore() == 10);

		// Another user
		assertTrue(readWriteMutiboSvcUser2.getMaximumScore() == 0);
		assertTrue(readWriteMutiboSvcUser1.getScoreList().size() == 3);

		readWriteMutiboSvcUser2.saveScore(15);
		readWriteMutiboSvcUser2.saveScore(1);

		assertTrue(readWriteMutiboSvcUser2.getMaximumScore() == 15);
		assertTrue(readWriteMutiboSvcUser1.getScoreList().size() == 5);

	}

	@Test
	public void testLogin() throws Exception {
		try {
			insecureMutiboService.login();
			// The login should fail
			fail("The login should have failed as user and pass are not provided!!");

		} catch (Exception e) {
			// Ok, our security may have worked, ensure that we got a 401
			assertEquals(HttpStatus.SC_UNAUTHORIZED, error.getError()
					.getResponse().getStatus());
		}
	}

	@Test
	public void testDenyMsetAddWithoutOAuth() throws Exception {
		Mset mset = createMsetForTesting();
		try {
			// This should fail because we haven't logged in!
			insecureMutiboService.addMset(mset);
			fail("Yikes, the security setup is horribly broken and didn't require the user to authenticate!!");

		} catch (Exception e) {
			// Ok, our security may have worked, ensure that we got a 401
			assertEquals(HttpStatus.SC_UNAUTHORIZED, error.getError()
					.getResponse().getStatus());
		}

		// We should NOT get back the mutibo set that we added above!
		Collection<Mset> msets = readWriteMutiboSvcUser1.getMsetList();
		assertFalse(msets.contains(mset));
	}
	
	private class ErrorRecorder implements ErrorHandler {

		private RetrofitError error;

		@Override
		public Throwable handleError(RetrofitError cause) {
			error = cause;
			return error.getCause();
		}

		public RetrofitError getError() {
			return error;
		}
	}
}
