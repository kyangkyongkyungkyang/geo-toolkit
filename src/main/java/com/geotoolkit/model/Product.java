package com.geotoolkit.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 여성복 상품 모델 - GEO 최적화에 필요한 모든 필드를 포함한다.
 * Schema.org Product 타입에 대응하는 구조.
 */
public class Product {

    private String id;
    private String name;
    private String nameEn;
    private String description;
    private String brand;
    private String sku;
    private String gtin13;
    private String category;
    private String material;
    private String color;
    private String pattern;
    private String url;

    // 여성복 특화 속성
    private String fit;              // 슬림, 레귤러, 세미와이드, 와이드
    private String length;           // 크롭, 미디, 풀기장
    private String thickness;        // 얇음, 중간, 두꺼움
    private String stretch;          // 없음, 약간, 좋음
    private String seeThrough;       // 있음, 없음
    private String washCare;         // 세탁 방법
    private String occasion;         // 추천 상황
    private String bodyType;         // 추천 체형
    private String stylingTip;       // 스타일링 팁
    private String modelInfo;        // 모델 착용 정보

    // 사이즈
    private List<String> sizes = new ArrayList<>();
    private List<SizeSpec> sizeSpecs = new ArrayList<>();

    // 가격/재고
    private int price;
    private String currency = "KRW";
    private boolean inStock = true;
    private String priceValidUntil;

    // 배송
    private int shippingFee;
    private int minHandlingDays = 1;
    private int maxHandlingDays = 2;
    private int minTransitDays = 1;
    private int maxTransitDays = 3;

    // 반품
    private int returnDays = 14;
    private boolean freeReturn = true;

    // 평점/리뷰
    private double ratingValue;
    private int ratingCount;
    private int reviewCount;
    private List<Review> reviews = new ArrayList<>();

    // 이미지
    private List<String> images = new ArrayList<>();

    // FAQ
    private List<FaqItem> faqs = new ArrayList<>();

    // 타겟 고객
    private int minAge = 25;
    private int maxAge = 40;

    // Getters and Setters

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getNameEn() { return nameEn; }
    public void setNameEn(String nameEn) { this.nameEn = nameEn; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getGtin13() { return gtin13; }
    public void setGtin13(String gtin13) { this.gtin13 = gtin13; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getPattern() { return pattern; }
    public void setPattern(String pattern) { this.pattern = pattern; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getFit() { return fit; }
    public void setFit(String fit) { this.fit = fit; }

    public String getLength() { return length; }
    public void setLength(String length) { this.length = length; }

    public String getThickness() { return thickness; }
    public void setThickness(String thickness) { this.thickness = thickness; }

    public String getStretch() { return stretch; }
    public void setStretch(String stretch) { this.stretch = stretch; }

    public String getSeeThrough() { return seeThrough; }
    public void setSeeThrough(String seeThrough) { this.seeThrough = seeThrough; }

    public String getWashCare() { return washCare; }
    public void setWashCare(String washCare) { this.washCare = washCare; }

    public String getOccasion() { return occasion; }
    public void setOccasion(String occasion) { this.occasion = occasion; }

    public String getBodyType() { return bodyType; }
    public void setBodyType(String bodyType) { this.bodyType = bodyType; }

    public String getStylingTip() { return stylingTip; }
    public void setStylingTip(String stylingTip) { this.stylingTip = stylingTip; }

    public String getModelInfo() { return modelInfo; }
    public void setModelInfo(String modelInfo) { this.modelInfo = modelInfo; }

    public List<String> getSizes() { return sizes; }
    public void setSizes(List<String> sizes) { this.sizes = sizes; }

    public List<SizeSpec> getSizeSpecs() { return sizeSpecs; }
    public void setSizeSpecs(List<SizeSpec> sizeSpecs) { this.sizeSpecs = sizeSpecs; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public boolean isInStock() { return inStock; }
    public void setInStock(boolean inStock) { this.inStock = inStock; }

    public String getPriceValidUntil() { return priceValidUntil; }
    public void setPriceValidUntil(String priceValidUntil) { this.priceValidUntil = priceValidUntil; }

    public int getShippingFee() { return shippingFee; }
    public void setShippingFee(int shippingFee) { this.shippingFee = shippingFee; }

    public int getMinHandlingDays() { return minHandlingDays; }
    public void setMinHandlingDays(int minHandlingDays) { this.minHandlingDays = minHandlingDays; }

    public int getMaxHandlingDays() { return maxHandlingDays; }
    public void setMaxHandlingDays(int maxHandlingDays) { this.maxHandlingDays = maxHandlingDays; }

    public int getMinTransitDays() { return minTransitDays; }
    public void setMinTransitDays(int minTransitDays) { this.minTransitDays = minTransitDays; }

    public int getMaxTransitDays() { return maxTransitDays; }
    public void setMaxTransitDays(int maxTransitDays) { this.maxTransitDays = maxTransitDays; }

    public int getReturnDays() { return returnDays; }
    public void setReturnDays(int returnDays) { this.returnDays = returnDays; }

    public boolean isFreeReturn() { return freeReturn; }
    public void setFreeReturn(boolean freeReturn) { this.freeReturn = freeReturn; }

    public double getRatingValue() { return ratingValue; }
    public void setRatingValue(double ratingValue) { this.ratingValue = ratingValue; }

    public int getRatingCount() { return ratingCount; }
    public void setRatingCount(int ratingCount) { this.ratingCount = ratingCount; }

    public int getReviewCount() { return reviewCount; }
    public void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }

    public List<Review> getReviews() { return reviews; }
    public void setReviews(List<Review> reviews) { this.reviews = reviews; }

    public List<String> getImages() { return images; }
    public void setImages(List<String> images) { this.images = images; }

    public List<FaqItem> getFaqs() { return faqs; }
    public void setFaqs(List<FaqItem> faqs) { this.faqs = faqs; }

    public int getMinAge() { return minAge; }
    public void setMinAge(int minAge) { this.minAge = minAge; }

    public int getMaxAge() { return maxAge; }
    public void setMaxAge(int maxAge) { this.maxAge = maxAge; }

    /** 가격을 콤마 포맷으로 반환 (예: 49,900) */
    public String getFormattedPrice() {
        return String.format("%,d", price);
    }

    /** Schema.org availability URL 반환 */
    public String getAvailabilitySchema() {
        return inStock ? "https://schema.org/InStock" : "https://schema.org/OutOfStock";
    }
}
