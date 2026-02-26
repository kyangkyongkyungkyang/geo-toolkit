package com.geotoolkit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.geotoolkit.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Schema.org JSON-LD 생성 서비스.
 * Product, FAQ, Organization, Breadcrumb 스키마를 동적으로 생성한다.
 * 생성된 JSON-LD는 Thymeleaf 템플릿에서 <script type="application/ld+json">으로 삽입된다.
 */
@Service
public class SchemaService {

    private final ObjectMapper objectMapper;

    @Value("${geo.brand.name}")
    private String brandName;

    @Value("${geo.brand.url}")
    private String brandUrl;

    public SchemaService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * Product Schema JSON-LD 생성
     */
    public String generateProductSchema(Product product) {
        Map<String, Object> schema = new LinkedHashMap<>();
        schema.put("@context", "https://schema.org/");
        schema.put("@type", "Product");
        schema.put("name", product.getName());
        schema.put("description", product.getDescription());

        // Brand
        Map<String, Object> brand = new LinkedHashMap<>();
        brand.put("@type", "Brand");
        brand.put("name", product.getBrand() != null ? product.getBrand() : brandName);
        schema.put("brand", brand);

        schema.put("sku", product.getSku());
        if (product.getGtin13() != null) {
            schema.put("gtin13", product.getGtin13());
        }
        schema.put("material", product.getMaterial());
        schema.put("color", product.getColor());
        schema.put("image", product.getImages());
        schema.put("url", product.getUrl() != null ? product.getUrl() : brandUrl + "/products/" + product.getId());

        // Audience
        Map<String, Object> audience = new LinkedHashMap<>();
        audience.put("@type", "PeopleAudience");
        audience.put("suggestedGender", "female");
        audience.put("suggestedMinAge", product.getMinAge());
        audience.put("suggestedMaxAge", product.getMaxAge());
        schema.put("audience", audience);

        // Additional Properties (여성복 특화)
        List<Map<String, Object>> additionalProps = new ArrayList<>();
        addProperty(additionalProps, "핏", product.getFit());
        addProperty(additionalProps, "기장", product.getLength());
        addProperty(additionalProps, "두께감", product.getThickness());
        addProperty(additionalProps, "신축성", product.getStretch());
        addProperty(additionalProps, "비침", product.getSeeThrough());
        addProperty(additionalProps, "세탁방법", product.getWashCare());
        addProperty(additionalProps, "추천 상황", product.getOccasion());
        addProperty(additionalProps, "추천 체형", product.getBodyType());
        if (!additionalProps.isEmpty()) {
            schema.put("additionalProperty", additionalProps);
        }

        // Offers
        Map<String, Object> offers = new LinkedHashMap<>();
        offers.put("@type", "Offer");
        offers.put("price", String.valueOf(product.getPrice()));
        offers.put("priceCurrency", product.getCurrency());
        offers.put("availability", product.getAvailabilitySchema());
        offers.put("itemCondition", "https://schema.org/NewCondition");
        if (product.getPriceValidUntil() != null) {
            offers.put("priceValidUntil", product.getPriceValidUntil());
        }

        // Shipping
        Map<String, Object> shipping = new LinkedHashMap<>();
        shipping.put("@type", "OfferShippingDetails");
        Map<String, Object> shippingRate = new LinkedHashMap<>();
        shippingRate.put("@type", "MonetaryAmount");
        shippingRate.put("value", String.valueOf(product.getShippingFee()));
        shippingRate.put("currency", "KRW");
        shipping.put("shippingRate", shippingRate);

        Map<String, Object> dest = new LinkedHashMap<>();
        dest.put("@type", "DefinedRegion");
        dest.put("addressCountry", "KR");
        shipping.put("shippingDestination", dest);
        offers.put("shippingDetails", shipping);

        // Return Policy
        Map<String, Object> returnPolicy = new LinkedHashMap<>();
        returnPolicy.put("@type", "MerchantReturnPolicy");
        returnPolicy.put("applicableCountry", "KR");
        returnPolicy.put("returnPolicyCategory", "https://schema.org/MerchantReturnFiniteReturnWindow");
        returnPolicy.put("merchantReturnDays", product.getReturnDays());
        returnPolicy.put("returnFees", product.isFreeReturn()
                ? "https://schema.org/FreeReturn"
                : "https://schema.org/ReturnShippingFees");
        offers.put("hasMerchantReturnPolicy", returnPolicy);

        schema.put("offers", offers);

        // Aggregate Rating
        if (product.getRatingValue() > 0) {
            Map<String, Object> rating = new LinkedHashMap<>();
            rating.put("@type", "AggregateRating");
            rating.put("ratingValue", String.valueOf(product.getRatingValue()));
            rating.put("bestRating", "5");
            rating.put("ratingCount", String.valueOf(product.getRatingCount()));
            rating.put("reviewCount", String.valueOf(product.getReviewCount()));
            schema.put("aggregateRating", rating);
        }

        // Reviews
        if (!product.getReviews().isEmpty()) {
            List<Map<String, Object>> reviewList = new ArrayList<>();
            for (Review r : product.getReviews()) {
                Map<String, Object> review = new LinkedHashMap<>();
                review.put("@type", "Review");
                Map<String, Object> author = new LinkedHashMap<>();
                author.put("@type", "Person");
                author.put("name", r.getAuthor());
                review.put("author", author);
                review.put("datePublished", r.getDatePublished());
                Map<String, Object> reviewRating = new LinkedHashMap<>();
                reviewRating.put("@type", "Rating");
                reviewRating.put("ratingValue", String.valueOf(r.getRating()));
                reviewRating.put("bestRating", "5");
                review.put("reviewRating", reviewRating);
                review.put("reviewBody", r.getBody());
                reviewList.add(review);
            }
            schema.put("review", reviewList);
        }

        return toJson(schema);
    }

    /**
     * FAQ Schema JSON-LD 생성
     */
    public String generateFaqSchema(List<FaqItem> faqs) {
        Map<String, Object> schema = new LinkedHashMap<>();
        schema.put("@context", "https://schema.org");
        schema.put("@type", "FAQPage");

        List<Map<String, Object>> mainEntity = new ArrayList<>();
        for (FaqItem faq : faqs) {
            Map<String, Object> question = new LinkedHashMap<>();
            question.put("@type", "Question");
            question.put("name", faq.getQuestion());
            Map<String, Object> answer = new LinkedHashMap<>();
            answer.put("@type", "Answer");
            answer.put("text", faq.getAnswer());
            question.put("acceptedAnswer", answer);
            mainEntity.add(question);
        }
        schema.put("mainEntity", mainEntity);

        return toJson(schema);
    }

    /**
     * Organization Schema JSON-LD 생성
     */
    public String generateOrganizationSchema() {
        Map<String, Object> schema = new LinkedHashMap<>();
        schema.put("@context", "https://schema.org");
        schema.put("@type", "Organization");
        schema.put("name", brandName);
        schema.put("url", brandUrl);

        // SearchAction
        Map<String, Object> searchAction = new LinkedHashMap<>();
        searchAction.put("@type", "SearchAction");
        Map<String, Object> target = new LinkedHashMap<>();
        target.put("@type", "EntryPoint");
        target.put("urlTemplate", brandUrl + "/search?q={search_term_string}");
        searchAction.put("target", target);
        searchAction.put("query-input", "required name=search_term_string");
        schema.put("potentialAction", searchAction);

        return toJson(schema);
    }

    /**
     * Breadcrumb Schema JSON-LD 생성
     */
    public String generateBreadcrumbSchema(List<String[]> breadcrumbs) {
        Map<String, Object> schema = new LinkedHashMap<>();
        schema.put("@context", "https://schema.org");
        schema.put("@type", "BreadcrumbList");

        List<Map<String, Object>> items = new ArrayList<>();
        for (int i = 0; i < breadcrumbs.size(); i++) {
            String[] bc = breadcrumbs.get(i);
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("@type", "ListItem");
            item.put("position", i + 1);
            item.put("name", bc[0]);
            if (bc.length > 1 && bc[1] != null) {
                item.put("item", bc[1]);
            }
            items.add(item);
        }
        schema.put("itemListElement", items);

        return toJson(schema);
    }

    private void addProperty(List<Map<String, Object>> list, String name, String value) {
        if (value != null && !value.isEmpty()) {
            Map<String, Object> prop = new LinkedHashMap<>();
            prop.put("@type", "PropertyValue");
            prop.put("name", name);
            prop.put("value", value);
            list.add(prop);
        }
    }

    private String toJson(Map<String, Object> map) {
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }
}
