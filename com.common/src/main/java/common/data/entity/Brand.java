package common.data.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "brands")
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_id", nullable = false)
    private Long id;

    @Column(length = 45, nullable = false, unique = true)
    private String name;

    @Column(length = 128,nullable = false)
    private String logo;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="brand_categories", joinColumns = @JoinColumn(name = "brand_id"),
            inverseJoinColumns =@JoinColumn(name="category_id"))
    private Set<Category> categories = new HashSet<>();

    public Brand(String name) {
        this.name = name;
        this.logo="brand-logo.png";
    }

    public Brand() {
    }

    @Override
    public String toString() {
        return "Brand{" +
                "name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                ", categories=" + categories +
                '}';
    }

    @Transient
    public String getLogoPath(){
        if (this.id== null)return "/images/image-thumbnail.png";
        return "/brand-logos/"+this.id+"/"+this.logo;
    }
}
