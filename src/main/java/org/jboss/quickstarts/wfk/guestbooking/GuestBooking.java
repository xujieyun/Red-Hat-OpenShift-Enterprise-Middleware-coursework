package org.jboss.quickstarts.wfk.guestbooking;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.jboss.quickstarts.wfk.booking.Booking;
import org.jboss.quickstarts.wfk.customer.Customer;
import org.jboss.quickstarts.wfk.flight.Flight;

/**
 * <p>This is a the Domain object. The Booking class represents how booking resources are represented in the application
 * database.</p>
 *
 * <p>The class also specifies how a bookings are retrieved from the database (with @NamedQueries), and acceptable values
 * for Booking fields (with @NotNull, @Pattern etc...)<p/>
 *
 * @author Xujie
 */
/*
 * The @NamedQueries included here are for searching against the table that reflects this object.  This is the most efficient
 * form of query in JPA though is it more error prone due to the syntax being in a String.  This makes it harder to debug.
 */
//@Entity
@NamedQueries({
        @NamedQuery(name = GuestBooking.FIND_ALL, query = "SELECT c FROM GuestBooking c ORDER BY c.id"),
        @NamedQuery(name = GuestBooking.FIND_BY_CUSTOMER, query = "SELECT c FROM GuestBooking c ORDER BY c.customer"),
})
@XmlRootElement
@Table(name = "guestBooking"/*, uniqueConstraints = @UniqueConstraint(columnNames = "email")*/)
public class GuestBooking implements Serializable {
    /** Default value included to remove warning. Remove or modify at will. **/
    private static final long serialVersionUID = 1L;

    public static final String FIND_ALL = "guestBooking.id";
    public static final String FIND_BY_CUSTOMER = "guestBooking.customer";

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @ManyToOne // owning side
//  @Column(name = "first_name")
    private Customer customer;

    @ManyToOne // owning side
//  @Column(name = "flight")
    private Booking booking;

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Booking getBooking() {
		return booking;
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((booking == null) ? 0 : booking.hashCode());
		result = prime * result + ((customer == null) ? 0 : customer.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GuestBooking other = (GuestBooking) obj;
		if (booking == null) {
			if (other.booking != null)
				return false;
		} else if (!booking.equals(other.booking))
			return false;
		if (customer == null) {
			if (other.customer != null)
				return false;
		} else if (!customer.equals(other.customer))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GuestBooking [id=" + id + ", customer=" + customer + ", booking=" + booking + "]";
	}

}
