package mset.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MsetRepository extends CrudRepository<Mset, Long> {
	
	public Collection<Mset> findByNegativesGreaterThan(Long negatives);

}
