package mset.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.common.base.Objects;

/**
 * A Mset is a unit of data that contains four movies titles, optional
 * associated images for each movie, information identifying the one
 * movie that is not like the other three, and accompanying text,
 * explaining the relationship between the three related movies.
 */
@Entity
public class Mset {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	// Movies titles
	@ElementCollection
	private List<String> movies = new ArrayList<String>(4);
	
	// Movies images url
	//private List<String> moviesImages = new ArrayList<String>(4);
	
	// Index of the movie that is not like the other three
	private int unrelatedMovieIndex;
	
	// Explains the relationship between the three related movies
	private String relationshipExplanation;
	
	// Negative ratings (less than 2 stars)
	private long negatives;

	public Mset() {
	}

	public Mset(ArrayList<String> movies, int unrelatedMovieIndex, String relationshipExplanation) {
		super();
		this.movies = movies;
		this.unrelatedMovieIndex = unrelatedMovieIndex;
		this.relationshipExplanation = relationshipExplanation;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<String> getMovies() {
		return movies;
	}

	public void setMovies(List<String> movies) {
		this.movies = movies;
	}

	public int getUnrelatedMovieIndex() {
		return unrelatedMovieIndex;
	}

	public void setUnrelatedMovieIndex(int unrelatedMovieIndex) {
		this.unrelatedMovieIndex = unrelatedMovieIndex;
	}

	public String getRelationshipExplanation() {
		return relationshipExplanation;
	}

	public void setRelationshipExplanation(String relationshipExplanation) {
		this.relationshipExplanation = relationshipExplanation;
	}

	public long getNegatives() {
		return negatives;
	}

	public void setNegatives(long negatives) {
		this.negatives = negatives;
	}

	/**
	 * Two Msets will generate the same hashcode if they have exactly the same
	 * movies
	 * 
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(movies.get(0), movies.get(1), movies.get(2), movies.get(3));
	}

	/**
	 * Two Msets are considered equal if they have exactly the same movies and the
	 * index of the unrelated movie is the same
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Mset) {
			Mset other = (Mset) obj;
			return movies.get(0).equals(other.getMovies().get(0))
					&& movies.get(1).equals(other.getMovies().get(1))
					&& movies.get(2).equals(other.getMovies().get(2))
					&& movies.get(3).equals(other.getMovies().get(3))
					&& unrelatedMovieIndex == other.getUnrelatedMovieIndex();
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return movies.get(0) + ", " + movies.get(1) + ", " + movies.get(2) + ", " +
				movies.get(3) + ", intruder: " + unrelatedMovieIndex + ", reason: " + relationshipExplanation + 
				", negatives: " + negatives;
	}
}
