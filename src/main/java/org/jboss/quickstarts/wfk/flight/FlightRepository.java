package org.jboss.quickstarts.wfk.flight;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

public class FlightRepository {

    @Inject
    private @Named("logger") Logger log;

    @Inject
    private EntityManager em;

    /**
     * <p>Returns a List of all persisted {@link Flight} objects, sorted alphabetically by last name.</p>
     *
     * @return List of Flight objects
     */
    List<Flight> findAllOrderedByName() {
        TypedQuery<Flight> query = em.createNamedQuery(Flight.FIND_ALL, Flight.class);
        return query.getResultList();
    }

    /**
     * <p>Returns a single Flight object, specified by a Long id.<p/>
     *
     * @param id The id field of the Flight to be returned
     * @return The Flight with the specified id
     */
    Flight findById(Long id) {
        return em.find(Flight.class, id);
    }

    /**
     * <p>Returns a list of Flight objects, specified by a String firstName.<p/>
     *
     * @param firstName The firstName field of the Flights to be returned
     * @return The Flights with the specified firstName
     */
    List<Flight> findAllByFirstName(String firstName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Flight> criteria = cb.createQuery(Flight.class);
        Root<Flight> flight = criteria.from(Flight.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new feature in JPA 2.0.
        // criteria.select(flight).where(cb.equal(flight.get(flight_.firstName), firstName));
        criteria.select(flight).where(cb.equal(flight.get("firstName"), firstName));
        return em.createQuery(criteria).getResultList();
    }

    /**
     * <p>Persists the provided Flight object to the application database using the EntityManager.</p>
     *
     * <p>{@link javax.persistence.EntityManager#persist(Object) persist(Object)} takes an entity instance, adds it to the
     * context and makes that instance managed (ie future updates to the entity will be tracked)</p>
     *
     * <p>persist(Object) will set the @GeneratedValue @Id for an object.</p>
     *
     * @param Flight The Flight object to be persisted
     * @return The Flight object that has been persisted
     * @throws ConstraintViolationException, ValidationException, Exception
     */
    Flight create(Flight flight) throws ConstraintViolationException, ValidationException, Exception {
        log.info("FlightRepository.create() - Creating " + flight.getFlightNumber());

        // Write the Flight to the database.
        em.persist(flight);

        return flight;
    }

    /**
     * <p>Updates an existing Flight object in the application database with the provided Flight object.</p>
     *
     * <p>{@link javax.persistence.EntityManager#merge(Object) merge(Object)} creates a new instance of your entity,
     * copies the state from the supplied entity, and makes the new copy managed. The instance you pass in will not be
     * managed (any changes you make will not be part of the transaction - unless you call merge again).</p>
     *
     * <p>merge(Object) however must have an object with the @Id already generated.</p>
     *
     * @param Flight The Flight object to be merged with an existing Flight
     * @return The Flight that has been merged
     * @throws ConstraintViolationException, ValidationException, Exception
     */
    Flight update(Flight flight) throws ConstraintViolationException, ValidationException, Exception {
        log.info("FlightRepository.update() - Updating " + flight.getFlightNumber());

        // Either update the Flight or add it if it can't be found.
        em.merge(flight);

        return flight;
    }

}
