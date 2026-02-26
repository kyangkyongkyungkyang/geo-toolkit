package com.geotoolkit.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 카테고리 정보.
 * Schema.org CollectionPage + ItemList에 대응.
 */
public class CategoryInfo {

    private String name;
    private String slug;
    private String description;
    private String url;
    private List<Product> products = new ArrayList<>();

    public CategoryInfo() {}

    public CategoryInfo(String name, String slug, String description) {
        this.name = name;
        this.slug = slug;
        this.description = description;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }
}
