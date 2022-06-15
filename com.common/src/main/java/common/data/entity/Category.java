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
    private Long categoryId;

    @Column(length = 128, nullable = false, unique = true)
    private String name;

    @Column(length = 64, nullable = false, unique = true)
    private String alias;

    @Column(length = 128, nullable = false)
    private String image;


    private boolean enabled;

    @OneToOne
    @JoinColumn(name = "parent_category_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private Set<Category> children = new HashSet<>();


    public Category(String name) {
        this.name = name;
        this.alias = name;
        this.image = "default.png";
    }

    public Category(String name, Category parent) {
        this.name = name;
        this.alias = name;
        this.parent = parent;
        this.image = "default.png";
    }

    public Category(Long categoryId) {
        this.categoryId = categoryId;
        this.image = "default.png";

    }

    public Category() {
        this.image = "default.png";

    }

    public Category(Long categoryId, String name, String alias) {
        this.categoryId = categoryId;
        this.name = name;
        this.alias = alias;
    }

    public static Category copyIdAndName(Category category) {
        Category category1 = new Category();
        category1.setCategoryId(category.getCategoryId());
        category1.setName(category.getName());
        return category1;
    }

    public static Category copyIdAndName(Long id, String name) {
        Category category1 = new Category();
        category1.setCategoryId(id);
        category1.setName(name);
        return category1;
    }

    public static Category copyFull(Category category) {
        Category category1 = new Category();
        category1.setCategoryId(category.getCategoryId());
        category1.setName(category.getName());
        category1.setImage(category.getImage());
        category1.setAlias(category.getAlias());
        category1.setEnabled(category.isEnabled());
        category1.setHasChildren(category.getChildren().size()>0);
        return category1;
    }

    public static Category copyFull(Category category, String name) {
        Category copyCategory = copyFull(category);
        copyCategory.setName(name);
        return copyCategory;
    }

//    @Transient
//    public String getImagePath(){
//        return "/category-images/"+this.getCategory_id()+"/"+this.image;
//    }

    @Transient
    public String getImagePath() {
        if (this.categoryId == null) return "/images/image-thumbnail.png";
        return "/category-images/" + this.getCategoryId() + "/" + this.image;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren){
        this.hasChildren=hasChildren;
    }

    @Transient
    private boolean hasChildren;

    @Override
    public String toString() {
        return this.name;
    }
}
