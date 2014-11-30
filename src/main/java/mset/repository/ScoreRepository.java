package mset.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;

public interface ScoreRepository extends CrudRepository<Score, Long> {
	Collection<Score> findByUserOrderByScoreDesc(String user);
}
