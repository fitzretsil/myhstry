/**
 * 
 */
package org.myhstry.db;

import java.util.List;

import org.myhstry.model.Marriage;
import org.myhstry.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author fitzretsil
 *
 */
public interface MarriageRepository extends JpaRepository<Marriage, Integer> {

	@Query("SELECT m FROM Marriage m WHERE m.pafId = ?1")
	Marriage findMarriageByPAFID(String id);
	
	@Query("SELECT m FROM Marriage m WHERE m.husband = ?1")
	List<Marriage> findMarriagesByHusbandId(Person husband);
}
