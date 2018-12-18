/**
 * 
 */
package org.myhstry.db;

import org.myhstry.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author fitzretsil
 *
 */
public interface EventRepository extends JpaRepository<Event, Integer> {

}
