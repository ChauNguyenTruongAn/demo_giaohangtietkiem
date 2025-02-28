package vn.demo_shipping.shipping.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.demo_shipping.shipping.util.Gender;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends AbstractEntity<Long> {
    private String full_name;
    private Gender gender;
    private Date date_of_birth;
    private String tel;
    private String email;
    private String username;
    private String password;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Address> addresses = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    Set<Invoice> invoices = new HashSet<>();

    public void addAddress(Address address) {
        if (address == null) {
            throw new IllegalArgumentException("Address cannot be null");
        }

        if (!addresses.contains(address)) {
            addresses.add(address);
            address.setUser(this);
        }
    }

    public void removeAddress(Address address) {
        if (address == null) {
            throw new IllegalArgumentException("Address cannot be null");
        }
        if (addresses.remove(address)) {
            address.setUser(null);
        }
    }

    public void addInvoice(Invoice invoice) {
        if (invoice == null)
            throw new IllegalArgumentException("Invoice cannot be null");
        if (!invoices.contains(invoice)) {
            invoices.add(invoice);
            invoice.setUser(this);
        }
    }

    public void removeInvoice(Invoice invoice) {
        if (invoice == null) {
            throw new IllegalArgumentException("Invoice cannot be null");
        }
        if (invoices.remove(invoice)) {
            invoice.setUser(null);
        }
    }
}
