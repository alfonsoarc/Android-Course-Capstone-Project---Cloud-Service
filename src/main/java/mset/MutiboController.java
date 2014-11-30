package mset;

import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import mset.client.MutiboSvcApi;
import mset.repository.Mset;
import mset.repository.MsetRepository;
import mset.repository.Score;
import mset.repository.ScoreRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

@Controller
public class MutiboController {
	@Autowired
	private MsetRepository msets;
	@Autowired
	private ScoreRepository scores;

	@RequestMapping(value = MutiboSvcApi.MSET_SVC_PATH, method = RequestMethod.GET)
	public @ResponseBody Collection<Mset> getMsetList() {
		return Lists.newArrayList(msets.findAll());
	}
	
	@RequestMapping(value = MutiboSvcApi.MSET_SVC_PATH + "/{id}", method = RequestMethod.GET)
	public @ResponseBody Mset getMsetById(@PathVariable("id") Long msetId)
			throws IOException {
		return msets.findOne(msetId);
	}
	
	@RequestMapping(value = MutiboSvcApi.MSET_SVC_PATH, method = RequestMethod.POST)
	public @ResponseBody Mset addMset(@RequestBody Mset mset) {
		return msets.save(mset);
	}
	
	@RequestMapping(value = MutiboSvcApi.MSET_SVC_PATH + "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody boolean deleteMset(@PathVariable("id") Long msetId) {
		if(msets.exists(msetId)) {
			msets.delete(msetId);
			return true;
		}
		return false;
	}

	@RequestMapping(value = MutiboSvcApi.MSET_NEGATIVES_SEARCH_PATH, method = RequestMethod.GET)
	public @ResponseBody Collection<Mset> findByNegativesGreaterThan(
			@RequestParam(MutiboSvcApi.NEGATIVES_PARAMETER) Long negatives) {
		return msets.findByNegativesGreaterThan(negatives);
	}

	/*
	 * Allows a user to rate a mutibo set. Returns 200 Ok on success and 404 if the
	 * set is not found
	 */

	@RequestMapping(value = MutiboSvcApi.MSET_SVC_PATH + "/{id}/{score}/rate", method = RequestMethod.POST)
	public void rateMset(@PathVariable("id") Long msetId, @PathVariable("score") int score, Principal user,
			HttpServletResponse response) throws IOException {

		Mset mset = msets.findOne(msetId);
		if(mset != null) {
			if(score < 2) {
				mset.setNegatives(mset.getNegatives() + 1);
				msets.save(mset);
			}
		} else {
			response.setContentType("text/html");
			response.sendError(404, "Mset not found");
		}
	}
	
	@RequestMapping(value = MutiboSvcApi.SCORE_SVC_PATH, method = RequestMethod.GET)
	public @ResponseBody Collection<Score> getScoreList() {
		return Lists.newArrayList(scores.findAll());
	}
	
	@RequestMapping(value = MutiboSvcApi.SCORE_SVC_PATH + "/{value}", method = RequestMethod.POST)
	public @ResponseBody Score saveScore(@PathVariable("value") int score, Principal user) 
			throws IOException {

		Score s = new Score(user.getName(), score);
		return scores.save(s);
	}
	
	@RequestMapping(value = MutiboSvcApi.MAXIMUM_SCORE_USER_SEARCH_PATH, method = RequestMethod.GET)
	public @ResponseBody int getMaximumScore(Principal user) {
		Collection<Score> scoresUser = scores.findByUserOrderByScoreDesc(user.getName());
		Iterator<Score> iterator = scoresUser.iterator();
		if(iterator.hasNext()) {
			return iterator.next().getScore();
		}
		return 0;
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody boolean login() {
		return true;
	}

}
