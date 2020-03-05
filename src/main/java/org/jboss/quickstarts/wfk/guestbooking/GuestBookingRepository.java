package org.jboss.quickstarts.wfk.guestbooking;

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

public class GuestBookingRepository {

    @Inject
    private @Named("logger") Logger log;

    @Inject
    private EntityManager em;

    /**
     * <p>Returns a List of all persisted {@link Booking} objects, sorted alphabetically by last name.</p>
     *
     * @return List of Booking objects
     */
    List<GuestBooking> findAllOrderedByName() {
        TypedQuery<GuestBooking> query = em.createNamedQuery(GuestBooking.FIND_ALL, GuestBooking.class);
        return query.getResultList();
    }

    /**
     * <p>Returns a single Booking object, specified by a Long id.<p/>
     *
     * @param id The id field of the Booking to be returned
     * @return The Booking with the specified id
     */
    GuestBooking findById(Long id) {
        return em.find(GuestBooking.class, id);
    }


    /**
     * <p>Returns a list of Booking objects, specified by a String firstName.<p/>
     *
     * @param firstName The firstName field of the Bookings to be returned
     * @return The Bookings with the specified firstName
     */
    /*List<Booking> findAllByFirstName(String firstName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Booking> criteria = cb.createQuery(Booking.class);
        Root<Booking> booking = criteria.from(Booking.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new feature in JPA 2.0.
        // criteria.select(booking).where(cb.equal(booking.get(booking_.firstName), firstName));
        criteria.select(booking).where(cb.equal(booking.get("firstName"), firstName));
        return em.createQuery(criteria).getResultList();
    }*/

    /**
     * <p>Returns a single Booking object, specified by a String lastName.<p/>
     *
     * @param lastName The lastName field of the Bookings to be returned
     * @return The Bookings with the specified lastName
     */
    /*List<Booking> findAllByLastName(String lastName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Booking> criteria = cb.createQuery(Booking.class);
        Root<Booking> booking = criteria.from(Booking.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new feature in JPA 2.0.
        // criteria.select(booking).where(cb.equal(booking.get(Booking_.lastName), lastName));
        criteria.select(booking).where(cb.equal(booking.get("lastName"), lastName));
        return em.createQuery(criteria).getResultList();
    }*/

    /**
     * <p>Persists the provided Booking object to the application database using the EntityManager.</p>
     *
     * <p>{@link javax.persistence.EntityManager#persist(Object) persist(Object)} takes an entity instance, adds it to the
     * context and makes that instance managed (ie future updates to the entity will be tracked)</p>
     *
     * <p>persist(Object) will set the @GeneratedValue @Id for an object.</p>
     *
     * @param Booking The Booking object to be persisted
     * @return The Booking object that has been persisted
     * @throws ConstraintViolationException, ValidationException, Exception
     */
    /*Booking create(Booking booking) throws ConstraintViolationException, ValidationException, Exception {
        log.info("BookingRepository.create() - Creating " + booking.getCustomer().getFirstName() + " " + booking.getCustomer().getLastName());

        // Write the Booking to the database.
        em.persist(booking);

        return booking;
    }*/

    /**
     * <p>Updates an existing Booking object in the application database with the provided Booking object.</p>
     *
     * <p>{@link javax.persistence.EntityManager#merge(Object) merge(Object)} creates a new instance of your entity,
     * copies the state from the supplied entity, and makes the new copy managed. The instance you pass in will not be
     * managed (any changes you make will not be part of the transaction - unless you call merge again).</p>
     *
     * <p>merge(Object) however must have an object with the @Id already generated.</p>
     *
     * @param Booking The Booking object to be merged with an existing Booking
     * @return The Booking that has been merged
     * @throws ConstraintViolationException, ValidationException, Exception
     */
    GuestBooking update(GuestBooking guestBooking) throws ConstraintViolationException, ValidationException, Exception {
        log.info("GuestBookingRepository.update() - Updating " + guestBooking.getCustomer().getFirstName() + " " + guestBooking.getCustomer().getLastName());

        // Either update the GuestBooking or add it if it can't be found.
        ((EntityManager) em).merge(guestBooking);

        return guestBooking;
    }

    /**
     * <p>Deletes the provided Booking object from the application database if found there</p>
     *
     * @param Booking The Booking object to be removed from the application database
     * @return The Booking object that has been successfully removed from the application database; or null
     * @throws Exception
     */
   /* Booking delete(Booking booking) throws Exception {
        log.info("BookingRepository.delete() - Deleting " + booking.getCustomer().getFirstName() + " " + booking.getCustomer().getLastName());

        if (booking.getId() != null) {
            
             * The Hibernate session (aka EntityManager's persistent context) is closed and invalidated after the commit(), 
             * because it is bound to a transaction. The object goes into a detached status. If you open a new persistent 
             * context, the object isn't known as in a persistent state in this new context, so you have to merge it. 
             * 
             * Merge sees that the object has a primary key (id), so it knows it is not new and must hit the database 
             * to reattach it. 
             * 
             * Note, there is NO remove method which would just take a primary key (id) and a entity class as argument. 
             * You first need an object in a persistent state to be able to delete it.
             * 
             * Therefore we merge first and then we can remove it.
             
            em.remove(em.merge(booking));

        } else {
            log.info("BookingRepository.delete() - No ID was found so can't Delete.");
        }

        return booking;
    }*/

}