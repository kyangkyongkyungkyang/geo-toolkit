package com.geotoolkit.service;

import com.geotoolkit.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 데모용 샘플 데이터 서비스.
 * 실제 운영에서는 DB 또는 API에서 데이터를 조회하도록 교체한다.
 */
@Service
public class DemoDataService {

    public Product getLinenWidePants() {
        Product p = new Product();
        p.setId("linen-wide-pants-ivory");
        p.setName("린넨 블렌드 와이드 팬츠 - 아이보리");
        p.setNameEn("Linen Blend Wide Pants - Ivory");
        p.setDescription("출근길 편안함과 세미 포멀 드레스코드를 동시에 충족하는 린넨 블렌드 와이드 팬츠입니다. "
                + "허리 뒤편 밴딩 디자인으로 장시간 앉아 있어도 편안하며, "
                + "린넨 55% 혼방 소재로 순수 린넨보다 구김이 현저히 적어 출장이나 여행에도 적합합니다. "
                + "155~170cm 여성에게 자연스러운 기장이며, "
                + "허벅지가 넉넉한 세미와이드 핏으로 다양한 체형에 어울립니다.");
        p.setBrand("무드앤무드");
        p.setSku("WP-2026-SS-001");
        p.setGtin13("8801234567890");
        p.setCategory("여성의류 > 하의 > 와이드팬츠");
        p.setMaterial("린넨 55%, 폴리에스터 40%, 스판덱스 5%");
        p.setColor("아이보리");
        p.setPattern("무지");

        // 여성복 특화 속성
        p.setFit("세미와이드");
        p.setLength("풀기장 (모델 착용 기준 164cm)");
        p.setThickness("중간 (여름 착용 적합)");
        p.setStretch("약간 있음");
        p.setSeeThrough("없음");
        p.setWashCare("찬물 손세탁 권장. 세탁기 사용 시 세탁망 + 울/섬세 코스.");
        p.setOccasion("출근룩, 하객룩, 여행, 브런치");
        p.setBodyType("허벅지가 넉넉한 체형, 하체 볼륨감이 있는 체형에 특히 추천");
        p.setStylingTip("크롭 니트, 블라우스, 오버핏 셔츠와 매칭");
        p.setModelInfo("164cm / 50kg / S 사이즈 착용");

        // 사이즈
        p.setSizes(Arrays.asList("S", "M", "L", "XL"));
        p.setSizeSpecs(Arrays.asList(
                new SizeSpec("S", 66, 94, 56, 102, "44~50kg"),
                new SizeSpec("M", 70, 98, 58, 103, "50~58kg"),
                new SizeSpec("L", 74, 102, 60, 104, "58~65kg"),
                new SizeSpec("XL", 78, 106, 62, 105, "65~72kg")
        ));

        // 가격
        p.setPrice(49900);
        p.setPriceValidUntil("2026-06-30");
        p.setShippingFee(3000);

        // 이미지 (데모용 placeholder)
        p.setImages(Arrays.asList(
                "/images/demo-product-main.jpg",
                "/images/demo-product-detail.jpg",
                "/images/demo-product-model.jpg"
        ));

        // 평점
        p.setRatingValue(4.6);
        p.setRatingCount(234);
        p.setReviewCount(189);

        // 리뷰
        p.setReviews(Arrays.asList(
                new Review("김**", "2026-02-15", 5,
                        "164cm 55kg M 사이즈 착용했어요. 허리 밴딩이라 편하고, 린넨이라 시원해요. "
                                + "회사에 입고 갔더니 어디서 샀냐고 많이 물어봤습니다. 하객룩으로도 좋을 것 같아요!"),
                new Review("이**", "2026-02-10", 4,
                        "158cm 50kg S 사이즈 구매. 기장이 조금 길어서 2cm 수선했어요. "
                                + "핏은 정말 예쁘고 사무실에서 입기 딱 좋습니다. 아이보리 색상이 고급스러워요."),
                new Review("박**", "2026-02-05", 5,
                        "170cm 60kg L 사이즈 입었는데 기장이 딱 좋아요! "
                                + "린넨인데 구김이 적어서 놀랐습니다. 블랙도 사고 싶어요.")
        ));

        // FAQ
        p.setFaqs(Arrays.asList(
                new FaqItem("이 팬츠는 키 작은 여성도 입을 수 있나요?",
                        "155cm 이하 여성분은 S 사이즈 기준 기장이 약 3~5cm 길 수 있습니다. "
                                + "무료 기장 수선 서비스를 제공하며, 2~3cm 줄이면 155cm 체형에도 자연스러운 실루엣이 완성됩니다."),
                new FaqItem("여름 결혼식 하객룩으로 적합한가요?",
                        "린넨 55% 블렌드 소재로 여름 야외 결혼식에 매우 적합합니다. "
                                + "아이보리 컬러에 새틴 블라우스를 매치하면 격식 있으면서도 시원한 하객 코디가 완성됩니다."),
                new FaqItem("허벅지가 두꺼운 체형인데 어떤 사이즈를 골라야 하나요?",
                        "세미와이드 핏이라 허벅지 부분에 여유가 있어 하체가 넉넉한 체형에 특히 잘 어울립니다. "
                                + "허리 기준으로 사이즈를 선택하시면 됩니다. 허벅지둘레가 56cm 이상이라면 한 사이즈 업을 권장합니다."),
                new FaqItem("린넨인데 구김이 많이 가나요?",
                        "린넨 55% + 폴리에스터 40% 혼방이라 일반 린넨보다 구김이 현저히 적습니다. "
                                + "장시간 앉아 있어도 무릎 뒤 구김이 심하지 않아 출퇴근이나 출장 시에도 편하게 착용 가능합니다."),
                new FaqItem("어울리는 상의 코디를 추천해 주세요",
                        "출근룩: 크롭 니트 또는 셔츠 인. 데이트룩: 프릴 블라우스. "
                                + "캐주얼: 오버핏 티셔츠 프론트 턱인. 하객룩: 새틴 블라우스 + 미니 클러치백.")
        ));

        return p;
    }

    public Product getCottonWidePants() {
        Product p = new Product();
        p.setId("cotton-wide-pants-black");
        p.setName("코튼 와이드 슬랙스 - 블랙");
        p.setDescription("사계절 데일리 출근룩의 기본 아이템. 코튼 100% 소재로 피부에 닿는 느낌이 부드럽고, "
                + "와이드 핏으로 하체 라인을 자연스럽게 커버합니다.");
        p.setBrand("무드앤무드");
        p.setSku("WP-2026-AW-002");
        p.setCategory("여성의류 > 하의 > 와이드팬츠");
        p.setMaterial("코튼 100%");
        p.setColor("블랙");
        p.setFit("와이드");
        p.setOccasion("사계절 출근룩, 캐주얼");
        p.setPrice(39900);
        p.setSizes(Arrays.asList("S", "M", "L", "XL"));
        p.setImages(Arrays.asList("/images/demo-product-main.jpg"));
        p.setRatingValue(4.4);
        p.setRatingCount(156);
        p.setReviewCount(120);
        p.setInStock(true);
        return p;
    }

    public Product getPintuckWidePants() {
        Product p = new Product();
        p.setId("pintuck-wide-pants-charcoal");
        p.setName("핀턱 와이드 팬츠 - 차콜");
        p.setDescription("핀턱 디테일로 밋밋하지 않은 와이드 핏. 폴리에스터 소재로 구김이 거의 없어 "
                + "출장이나 중요한 미팅이 있는 날에 적합합니다.");
        p.setBrand("무드앤무드");
        p.setSku("WP-2026-AW-003");
        p.setCategory("여성의류 > 하의 > 와이드팬츠");
        p.setMaterial("폴리에스터 95%, 스판덱스 5%");
        p.setColor("차콜");
        p.setFit("와이드");
        p.setOccasion("오피스 포멀, 미팅, 출장");
        p.setPrice(44900);
        p.setSizes(Arrays.asList("S", "M", "L"));
        p.setImages(Arrays.asList("/images/demo-product-main.jpg"));
        p.setRatingValue(4.7);
        p.setRatingCount(98);
        p.setReviewCount(76);
        p.setInStock(true);
        return p;
    }

    public CategoryInfo getWidePantsCategory() {
        CategoryInfo cat = new CategoryInfo();
        cat.setName("여성 와이드팬츠");
        cat.setSlug("wide-pants");
        cat.setDescription("다양한 체형에 어울리는 여성 와이드팬츠 컬렉션. "
                + "출근룩부터 하객룩까지. 린넨, 코튼, 폴리 소재별 다양한 옵션.");
        cat.setProducts(Arrays.asList(
                getLinenWidePants(),
                getCottonWidePants(),
                getPintuckWidePants()
        ));
        return cat;
    }

    public List<Product> getAllProducts() {
        return Arrays.asList(getLinenWidePants(), getCottonWidePants(), getPintuckWidePants());
    }
}
