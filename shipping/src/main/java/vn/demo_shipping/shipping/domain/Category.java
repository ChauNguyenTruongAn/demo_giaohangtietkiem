package vn.demo_shipping.shipping.domain;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category extends AbstractEntity<Long> {

    private String name;
    private String description;

    @OneToMany(mappedBy = "category", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Product> products;

    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product can't null");
        }
        if (!products.contains(product)) {
            product.setCategory(this);
            products.add(product);
        }
    }

    public void removeProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product can't null");
        }
        product.setCategory(null);
        products.remove(product);
    }
}
