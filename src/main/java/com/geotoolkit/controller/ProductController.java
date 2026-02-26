package com.geotoolkit.controller;

import com.geotoolkit.model.Product;
import com.geotoolkit.service.DemoDataService;
import com.geotoolkit.service.SchemaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Arrays;
import java.util.List;

@Controller
public class ProductController {

    private final SchemaService schemaService;
    private final DemoDataService demoDataService;

    public ProductController(SchemaService schemaService, DemoDataService demoDataService) {
        this.schemaService = schemaService;
        this.demoDataService = demoDataService;
    }

    @GetMapping("/products/{id}")
    public String productDetail(@PathVariable String id, Model model) {
        // 데모: ID에 따라 상품 조회 (실제로는 DB 조회)
        Product product = demoDataService.getLinenWidePants();

        // Breadcrumb
        List<String[]> breadcrumbs = Arrays.asList(
                new String[]{"홈", "/"},
                new String[]{"하의", "/categories/bottoms"},
                new String[]{"와이드팬츠", "/categories/bottoms/wide-pants"},
                new String[]{product.getName(), null}
        );

        model.addAttribute("product", product);
        model.addAttribute("productSchema", schemaService.generateProductSchema(product));
        model.addAttribute("faqSchema", schemaService.generateFaqSchema(product.getFaqs()));
        model.addAttribute("breadcrumbSchema", schemaService.generateBreadcrumbSchema(breadcrumbs));
        model.addAttribute("orgSchema", schemaService.generateOrganizationSchema());
        model.addAttribute("breadcrumbs", breadcrumbs);
        model.addAttribute("pageTitle", product.getName() + " | 무드앤무드");
        model.addAttribute("pageDescription", product.getDescription());
        model.addAttribute("lastModified", "2026-02-26");

        return "product/detail";
    }
}
