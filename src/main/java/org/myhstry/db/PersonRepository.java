package org.myhstry.db;

import java.util.List;

import org.myhstry.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
	
	List<Person> findAllByGender(String gender);
}
