package com.geotoolkit.controller;

import com.geotoolkit.model.Product;
import com.geotoolkit.service.DemoDataService;
import com.geotoolkit.service.SchemaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 데모 페이지 컨트롤러.
 * GEO 최적화 기능을 한 페이지에서 확인할 수 있는 대시보드를 제공한다.
 */
@Controller
public class DemoController {

    private final SchemaService schemaService;
    private final DemoDataService demoDataService;

    public DemoController(SchemaService schemaService, DemoDataService demoDataService) {
        this.schemaService = schemaService;
        this.demoDataService = demoDataService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/demo";
    }

    @GetMapping("/demo")
    public String demo(Model model) {
        Product mainProduct = demoDataService.getLinenWidePants();
        List<Product> allProducts = demoDataService.getAllProducts();

        String productSchema = schemaService.generateProductSchema(mainProduct);
        String faqSchema = schemaService.generateFaqSchema(mainProduct.getFaqs());
        String orgSchema = schemaService.generateOrganizationSchema();

        model.addAttribute("product", mainProduct);
        model.addAttribute("allProducts", allProducts);
        model.addAttribute("productSchemaJson", productSchema);
        model.addAttribute("faqSchemaJson", faqSchema);
        model.addAttribute("orgSchemaJson", orgSchema);

        return "demo/index";
    }
}
