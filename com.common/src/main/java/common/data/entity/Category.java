package common.data.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false)
    private Long category_id;

    @Column(length = 128, nullable = false,unique = true)
    private String name;

    @Column(length = 64, nullable = false,unique = true)
    private String alias;

    @Column(length = 128, nullable = false)
    private String image;


    private boolean enabled;

    @OneToOne
    @JoinColumn(name = "parent_category_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private Set<Category> children= new HashSet<>();


    public Category(String name) {
        this.name = name;
        this.alias=name;
        this.image="default.png";
    }
    public Category(String name,Category parent) {
        this.name = name;
        this.alias=name;
        this.parent=parent;
        this.image="default.png";
    }

    public Category(Long category_id) {
        this.category_id = category_id;
    }

    public Category() {
    }

    public static Category copyIdAndName(Category category) {
        Category category1= new Category();
        category1.setCategory_id(category.getCategory_id());
        category1.setName(category.getName());
        return category1;
    }
    public static Category copyIdAndName(Long id, String name) {
        Category category1= new Category();
        category1.setCategory_id(id);
        category1.setName(name);
        return category1;
    }
}
