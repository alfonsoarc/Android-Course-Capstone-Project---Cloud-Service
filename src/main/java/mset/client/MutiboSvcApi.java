package mset.client;

import java.util.Collection;

import mset.repository.Mset;
import mset.repository.Score;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.DELETE;
import retrofit.http.Path;
import retrofit.http.Query;

/**      
 * 
 * This interface defines an API for a Mutibo Service. The
 * interface is used to provide a contract for client/server
 * interactions. The interface is annotated with Retrofit
 * annotations so that clients can automatically convert the
 * interface into a client capable of sending the appropriate
 * HTTP requests.
 * 
 * The HTTP API that you must implement so that this interface
 * will work:
 * 
 * POST /oauth/token
 *    - The access point for the OAuth 2.0 Password Grant flow.
 *    - Clients should be able to submit a request with their username, password,
 *       client ID, and client secret, encoded.
 *    - The client ID for the Retrofit adapter is "mobile" with an empty password.
 *  
 * GET /mset
 *    - Returns the list of mutibo sets that have been added to the
 *      server as JSON. The list of sets is persisted
 *      using Spring Data. The list of Mset objects can
 *      be unmarshalled by the client into a Collection<Mset>.
 *    - The return content-type should be application/json, which
 *      will be the default if you use @ResponseBody
 * 
 *      
 * POST /mset
 *    - The mutibo set metadata is provided as an application/json request
 *      body. The JSON generates a valid instance of the 
 *      Mset class when deserialized by Spring's default 
 *      Jackson library.
 *    - Returns the JSON representation of the Mset object that
 *      was stored along with any updates to that object made by the server. 
 *    - The server stores the Mset in a Spring Data JPA repository.
 *      ID's generation is automatic
 * 
 * DELETE /mset/{id}
 * 	  - Delete the mutibo set with the given id. Return true if the set was deleted 
 * 		or false if it was not.
 * 
 * GET /mset/{id}
 *    - Returns the mutibo set with the given id or null if the set is not found.
 *      
 * POST /mset/{id}/rate/{score}
 *    - Allows the client to rate a mutibo set. Returns 200 Ok on success, 404 if the
 *      set is not found
 *    - A user can rate a set more than once
 *     
 * GET /mset/search/findByNegativesGreaterThan?negatives={negatives}
 *    - Returns a list of sets whose number of negative rates are greater than the 
 *    given parameter or an empty list if none are found.
 *    - A rate is considered to be negative if it is equal to 1 star	
 *     
 * POST /score/{value}
 * 	  - Allows the client to save the score of the user when the game is over.
 * 
 * GET /score/search/findMaximumScoreUser
 * 	  - Allows the client to retrieve the highest score of the user
 * 
 * POST /login
 * 	  - Allows users login
 */

public interface MutiboSvcApi {
	
	public static final String NEGATIVES_PARAMETER = "negatives";
	
	public static final String USER_PARAMETER = "user";

	public static final String TOKEN_PATH = "/oauth/token";

	// The path where we expect the MsetSvc to live
	public static final String MSET_SVC_PATH = "/mset";
	
	// The path where we expect the ScoreSvc to live
	public static final String SCORE_SVC_PATH = "/score";
	
	// The path to search mutibo sets by negative ratings
	public static final String MSET_NEGATIVES_SEARCH_PATH = MSET_SVC_PATH + "/search/findByNegativesGreaterThan";

	public static final String MAXIMUM_SCORE_USER_SEARCH_PATH = SCORE_SVC_PATH + "/search/findMaximumScoreUser";
	
	@GET(MSET_SVC_PATH)
	public Collection<Mset> getMsetList();
	
	@GET(MSET_SVC_PATH + "/{id}")
	public Mset getMsetById(@Path("id") long id);
	
	@POST(MSET_SVC_PATH)
	public Mset addMset(@Body Mset v);
	
	@DELETE(MSET_SVC_PATH + "/{id}")
	public boolean deleteMset(@Path("id") long id);
	
	@POST(MSET_SVC_PATH + "/{id}/{score}/rate")
	public Void rateMset(@Path("id") long id, @Path("score") int score);
	
	@GET(MSET_NEGATIVES_SEARCH_PATH)
	public Collection<Mset> findByNegativesGreaterThan(@Query(NEGATIVES_PARAMETER) long negatives);
	
	@GET(SCORE_SVC_PATH)
	public Collection<Score> getScoreList();
	
	@POST(SCORE_SVC_PATH + "/{value}")
	public Void saveScore(@Path("value") int score);
	
	@GET(MAXIMUM_SCORE_USER_SEARCH_PATH)
	public int getMaximumScore();
	
	@POST("/login")
	public boolean login();

}
