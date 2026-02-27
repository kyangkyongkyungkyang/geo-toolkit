# GEO Toolkit - 여성복 쇼핑몰 AI 검색 최적화 구현 키트

여성복 쇼핑몰을 위한 **GEO(Generative Engine Optimization)** 구현 키트입니다.
ChatGPT, Perplexity, Google AI Overviews 등 생성형 AI 검색에서
자사 상품이 추천·인용되도록 최적화하는 데 필요한 전체 코드와 설정을 제공합니다.

## 기술 스택

| 항목 | 버전 |
|------|------|
| Java | 1.8+ |
| Spring Boot | 2.7.18 |
| Thymeleaf | 3.0 (Spring Boot 내장) |
| Build Tool | Maven |

## 프로젝트 구조

```
geo-toolkit/
├── pom.xml
├── src/main/
│   ├── java/com/geotoolkit/
│   │   ├── GeoToolkitApplication.java       # Spring Boot 메인
│   │   ├── controller/
│   │   │   ├── ProductController.java        # 상품 상세 페이지
│   │   │   ├── CategoryController.java       # 카테고리 페이지
│   │   │   └── DemoController.java           # 데모 대시보드
│   │   ├── model/
│   │   │   ├── Product.java                  # 상품 모델 (GEO 전체 필드)
│   │   │   ├── SizeSpec.java                 # 사이즈 실측 스펙
│   │   │   ├── FaqItem.java                  # FAQ 항목
│   │   │   ├── Review.java                   # 고객 리뷰
│   │   │   └── CategoryInfo.java             # 카테고리 정보
│   │   └── service/
│   │       ├── SchemaService.java            # JSON-LD Schema 동적 생성
│   │       └── DemoDataService.java          # 데모용 샘플 데이터
│   └── resources/
│       ├── application.properties             # 서버/브랜드 설정
│       ├── static/
│       │   ├── css/style.css                  # 데모 스타일시트
│       │   ├── robots.txt                     # AI 크롤러 접근 제어
│       │   └── llms.txt                       # AI 콘텐츠 사용 정책
│       └── templates/
│           ├── layout/default.html            # 공통 레이아웃
│           ├── product/detail.html            # 상품 상세 (GEO 최적화)
│           ├── category/list.html             # 카테고리 목록
│           └── demo/index.html                # 데모 대시보드
├── crawlers/                                  # 원본 크롤러 설정 (참고용)
├── schemas/                                   # 원본 JSON-LD 스키마 (참고용)
├── scripts/                                   # Python 유틸리티 스크립트
│   ├── ai-crawler-monitor.py                  # AI 크롤러 모니터링
│   └── schema-validator.py                    # Schema GEO 적합성 검증
└── templates/                                 # 원본 HTML 템플릿 (참고용)
```

## 실행 방법

### 사전 요구사항

- Java 8 이상 설치
- Maven 설치 (또는 Maven Wrapper 사용)

### 빌드 및 실행

```bash
# 프로젝트 빌드
mvn clean package

# 실행
java -jar target/geo-toolkit-1.0.0.jar

# 또는 Maven으로 바로 실행
mvn spring-boot:run
```

### 접속
- ** 데모 페이지 ** : https://kyangkyongkyungkyang.github.io/geo-toolkit/

## GEO 핵심 기능

### 1. Schema JSON-LD 동적 생성

`SchemaService`가 상품 데이터를 기반으로 4종의 Schema를 자동 생성합니다:

| Schema 타입 | 용도 | 삽입 위치 |
|-------------|------|----------|
| **Product** | 상품 정보 (가격, 소재, 사이즈, 리뷰) | 상품 상세 `<head>` |
| **FAQPage** | 자주 묻는 질문 (Q&A) | 상품 상세 `<head>` |
| **Organization** | 브랜드 엔티티 정보 | 전체 페이지 공통 |
| **BreadcrumbList** | 네비게이션 경로 | 전체 페이지 공통 |

Thymeleaf에서 `th:utext`를 사용하여 `<script type="application/ld+json">`에 삽입:

```html
<script type="application/ld+json" th:utext="${productSchema}"></script>
```

### 2. 여성복 특화 상품 모델

`Product.java`에 일반 이커머스 필드 외에 여성복 전용 속성을 포함:

- **핏** (슬림/레귤러/세미와이드/와이드)
- **추천 상황** (출근룩/하객룩/데이트룩)
- **추천 체형** (체형별 착용 가이드)
- **사이즈 실측 스펙** (허리/엉덩이/허벅지/기장 + 추천 체중)
- **소재 상세** (두께감/신축성/비침/세탁법)

### 3. AI 크롤러 관리

`robots.txt`에서 AI 크롤러를 목적별로 분류 관리:

- **검색용 (허용)**: ChatGPT-User, PerplexityBot, Googlebot
- **학습+검색 (부분 허용)**: GPTBot, ClaudeBot
- **학습 전용 (차단)**: Meta-ExternalAgent, Bytespider

### 4. 대화형 상품 설명 구조

AI가 인용하기 좋은 5W1H 프레임워크 적용:

- **Who**: 타겟 고객 (체형, 연령대)
- **When**: 계절, 시점
- **Where**: 착용 장소/상황
- **What**: 제품 특성
- **Why**: 해결하는 문제
- **How**: 스타일링 제안

## 커스터마이징

### 브랜드 정보 변경

`src/main/resources/application.properties`에서 브랜드 정보를 수정:

```properties
geo.brand.name=내 브랜드명
geo.brand.url=https://mybrand.com
geo.brand.description=브랜드 소개 문구
```

### 상품 데이터 연동

`DemoDataService.java`는 데모용 하드코딩 데이터입니다.
실제 운영 시에는 DB 연동 서비스로 교체하세요:

```java
@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Product findById(String id) {
        return productRepository.findById(id).orElseThrow();
    }
}
```

## 참고 자료

- [GEO 리서치 보고서](claudedocs/research_geo_ai_crawler_20260226.md) - 상세 리서치 결과
- [구현 가이드](IMPLEMENTATION-GUIDE.md) - 단계별 적용 가이드
- [Google Rich Results Test](https://search.google.com/test/rich-results) - Schema 검증
- [Schema.org Product](https://schema.org/Product) - Product 스키마 공식 문서

## 라이선스

MIT License
