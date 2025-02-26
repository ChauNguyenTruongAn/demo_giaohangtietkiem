package vn.demo_shipping.shipping.domain;

import java.util.Date;
import java.util.Set;

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

    @OneToMany(mappedBy = "user")
    Set<Address> addresses;

    @OneToMany(mappedBy = "user")
    Set<Invoice> invoices;
}
