package org.jboss.quickstarts.wfk.guestbooking;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>This class provides methods to check Contact objects against arbitrary requirements.</p>
 *
 * @author Xujie
 * @see Booking
 * @see BookingRepository
 * @see javax.validation.Validator
 */
public class GuestBookingValidator {
    @Inject
    private Validator validator;

    @Inject
//    private GuestBookingValidator crud;

    /**
     * <p>Validates the given Booking object and throws validation exceptions based on the type of error. If the error is standard
     * bean validation errors then it will throw a ConstraintValidationException with the set of the constraints violated.<p/>
     *
     *
     * <p>If the error is caused because an existing booking with the same email is registered it throws a regular validation
     * exception so that it can be interpreted separately.</p>
     *
     *
     * @param booking The Booking object to be validated
     * @throws ConstraintViolationException If Bean Validation errors exist
     * @throws ValidationException If booking with the same email already exists
     */
    void validateGuestBooking(GuestBooking guestBooking) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<GuestBooking>> violations = validator.validate(guestBooking);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }
    }

}
