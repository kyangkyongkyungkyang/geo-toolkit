package com.geotoolkit.controller;

import com.geotoolkit.model.CategoryInfo;
import com.geotoolkit.service.DemoDataService;
import com.geotoolkit.service.SchemaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class CategoryController {

    private final SchemaService schemaService;
    private final DemoDataService demoDataService;

    public CategoryController(SchemaService schemaService, DemoDataService demoDataService) {
        this.schemaService = schemaService;
        this.demoDataService = demoDataService;
    }

    @GetMapping("/categories/bottoms/wide-pants")
    public String widePantsCategory(Model model) {
        CategoryInfo category = demoDataService.getWidePantsCategory();

        List<String[]> breadcrumbs = Arrays.asList(
                new String[]{"홈", "/"},
                new String[]{"하의", "/categories/bottoms"},
                new String[]{"와이드팬츠", null}
        );

        model.addAttribute("category", category);
        model.addAttribute("breadcrumbSchema", schemaService.generateBreadcrumbSchema(breadcrumbs));
        model.addAttribute("orgSchema", schemaService.generateOrganizationSchema());
        model.addAttribute("breadcrumbs", breadcrumbs);
        model.addAttribute("pageTitle", category.getName() + " | 무드앤무드");
        model.addAttribute("pageDescription", category.getDescription());
        model.addAttribute("lastModified", "2026-02-26");

        return "category/list";
    }
}
