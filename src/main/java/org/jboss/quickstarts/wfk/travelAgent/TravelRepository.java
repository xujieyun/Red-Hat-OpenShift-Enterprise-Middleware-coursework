/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.quickstarts.wfk.travelagent;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.jboss.quickstarts.wfk.contact.Contact;

import java.util.List;
import java.util.logging.Logger;

/**
 * <p>This is a Repository class and connects the Service/Control layer (see {@link ContactService} with the
 * Domain/Entity Object (see {@link Contact}).<p/>
 *
 * <p>There are no access modifiers on the methods making them 'package' scope.  They should only be accessed by a
 * Service/Control object.<p/>
 *
 * @author Xujie
 * @see Contact
 * @see javax.persistence.EntityManager
 */
public class TravelRepository {

    @Inject
    private @Named("logger") Logger log;

    @Inject
    private EntityManager em;

    /**
     * <p>Returns a List of all persisted {@link Contact} objects, sorted alphabetically by last name.</p>
     *
     * @return List of Contact objects
     */
    List<TravelAgent> findAllOrderedByName() {
        TypedQuery<TravelAgent> query = em.createNamedQuery(TravelAgent.FIND_ALL, TravelAgent.class);
        return query.getResultList();
    }
    List<TravelAgent> findAllOrderedByCustomerName() {
        TypedQuery<TravelAgent> query = em.createNamedQuery(TravelAgent.FIND_CUSTOMER_ALL, TravelAgent.class);
        return query.getResultList();
    }

    /**
     * <p>Returns a single TravelAgent object, specified by a Long id.<p/>
     *
     * @param id The id field of the TravelAgent to be returned
     * @return The TravelAgent with the specified id
     */
    TravelAgent findById(Long id) {
        return em.find(TravelAgent.class, id);
    }

    /**
     * <p>Returns a single TravelAgent object, specified by a String email.</p>
     *
     * <p>If there is more than one TravelAgent with the specified email, only the first encountered will be returned.<p/>
     *
     * @param email The email field of the TravelAgent to be returned
     * @return The first TravelAgent with the specified email
     */
   /* Contact findByEmail(String email) {
        TypedQuery<Contact> query = em.createNamedQuery(Contact.FIND_BY_EMAIL, Contact.class).setParameter("email", email);
        return query.getSingleResult();
    }*/

    /**
     * <p>Returns a list of Contact objects, specified by a String firstName.<p/>
     *
     * @param firstName The firstName field of the Contacts to be returned
     * @return The Contacts with the specified firstName
     */
    List<TravelAgent> findAllById(Long id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TravelAgent> criteria = cb.createQuery(TravelAgent.class);
        Root<TravelAgent> travelagent = criteria.from(TravelAgent.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new feature in JPA 2.0.
        // criteria.select(travelagent).where(cb.equal(travelagent.get(TravelAgent_.firstName), firstName));
        criteria.select(travelagent).where(cb.equal(travelagent.get("id"), id));
        return em.createQuery(criteria).getResultList();
    }

    /**
     * <p>Returns a single Contact object, specified by a String lastName.<p/>
     *
     * @param lastName The lastName field of the Contacts to be returned
     * @return The Contacts with the specified lastName
     */
    /*List<Contact> findAllByLastName(String lastName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Contact> criteria = cb.createQuery(Contact.class);
        Root<Contact> contact = criteria.from(Contact.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new feature in JPA 2.0.
        // criteria.select(contact).where(cb.equal(contact.get(Contact_.lastName), lastName));
        criteria.select(contact).where(cb.equal(contact.get("lastName"), lastName));
        return em.createQuery(criteria).getResultList();
    }*/

    /**
     * <p>Persists the provided TravelAgent object to the application database using the EntityManager.</p>
     *
     * <p>{@link javax.persistence.EntityManager#persist(Object) persist(Object)} takes an entity instance, adds it to the
     * context and makes that instance managed (ie future updates to the entity will be tracked)</p>
     *
     * <p>persist(Object) will set the @GeneratedValue @Id for an object.</p>
     *
     * @param travelagent The TravelAgent object to be persisted
     * @return The TravelAgent object that has been persisted
     * @throws ConstraintViolationException, ValidationException, Exception
     */
    TravelAgent create(TravelAgent travelagent) throws ConstraintViolationException, ValidationException, Exception {
        log.info("TravelAgentRepository.create() - Creating " + travelagent.getId());

        // Write the travelagent to the database.
        em.persist(travelagent);

        return travelagent;
    }

    /**
     * <p>Updates an existing Contact object in the application database with the provided Contact object.</p>
     *
     * <p>{@link javax.persistence.EntityManager#merge(Object) merge(Object)} creates a new instance of your entity,
     * copies the state from the supplied entity, and makes the new copy managed. The instance you pass in will not be
     * managed (any changes you make will not be part of the transaction - unless you call merge again).</p>
     *
     * <p>merge(Object) however must have an object with the @Id already generated.</p>
     *
     * @param contact The Contact object to be merged with an existing Contact
     * @return The Contact that has been merged
     * @throws ConstraintViolationException, ValidationException, Exception
     */
    /*Contact update(Contact contact) throws ConstraintViolationException, ValidationException, Exception {
        log.info("ContactRepository.update() - Updating " + contact.getFirstName() + " " + contact.getLastName());

        // Either update the contact or add it if it can't be found.
        em.merge(contact);

        return contact;
    }*/

    /**
     * <p>Deletes the provided Contact object from the application database if found there</p>
     *
     * @param contact The Contact object to be removed from the application database
     * @return The Contact object that has been successfully removed from the application database; or null
     * @throws Exception
     */
    TravelAgent delete(TravelAgent travelagent) throws Exception {
        log.info("TravelAgentRepository.delete() - Deleting " + travelagent.getId());

        if (travelagent.getId() != null) {
            /*
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
             */
            em.remove(em.merge(travelagent));

        } else {
            log.info("TravelAgentRepository.delete() - No ID was found so can't Delete.");
        }

        return travelagent;
    }

}