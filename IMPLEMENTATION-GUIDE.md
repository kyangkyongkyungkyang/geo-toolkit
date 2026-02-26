# 여성복 쇼핑몰 GEO 구현 가이드

> 이 키트는 여성복 쇼핑몰의 GEO(Generative Engine Optimization) 구현에 필요한 모든 파일을 포함합니다.

## 디렉토리 구조

```
geo-toolkit/
├── IMPLEMENTATION-GUIDE.md     ← 이 파일 (구현 가이드)
├── crawlers/
│   ├── robots.txt              ← AI 크롤러 접근 제어 (웹사이트 루트에 배치)
│   └── llms.txt                ← AI 콘텐츠 사용 정책 (웹사이트 루트에 배치)
├── schemas/
│   ├── product-schema.jsonld   ← 상품 페이지 구조화 데이터 템플릿
│   ├── faq-schema.jsonld       ← FAQ 구조화 데이터 템플릿
│   ├── organization-schema.jsonld ← 조직(브랜드) 구조화 데이터
│   └── breadcrumb-schema.jsonld   ← 빵부스러기 네비게이션 스키마
├── templates/
│   ├── product-page-template.html  ← GEO 최적화 상품 페이지 HTML 템플릿
│   └── category-page-template.html ← GEO 최적화 카테고리 페이지 HTML 템플릿
└── scripts/
    ├── ai-crawler-monitor.py   ← AI 크롤러 방문 모니터링 스크립트
    └── schema-validator.py     ← Schema Markup GEO 적합성 검증 스크립트
```

## 적용 순서

### Step 1: 크롤러 설정 (즉시 적용, 5분)

1. `crawlers/robots.txt` 내용을 확인하고 `[브랜드명]` 등의 플레이스홀더를 수정
2. 기존 robots.txt 파일을 백업
3. 새 robots.txt를 웹사이트 루트(https://yourdomain.com/robots.txt)에 업로드
4. `crawlers/llms.txt`도 동일하게 수정 후 루트에 업로드

**검증**: 브라우저에서 `https://yourdomain.com/robots.txt` 접속하여 확인

### Step 2: 조직 스키마 적용 (1회, 10분)

1. `schemas/organization-schema.jsonld`의 플레이스홀더([브랜드명], URL 등)를 수정
2. 모든 페이지의 `<head>` 안에 `<script type="application/ld+json">` 태그로 삽입
3. 보통 공통 레이아웃/헤더 파일에 한 번만 추가하면 됨

### Step 3: 상품 페이지 스키마 적용 (매출 상위 10~20개 우선)

1. `schemas/product-schema.jsonld`를 참고하여 실제 상품 데이터로 채움
2. 각 상품 페이지의 `<head>`에 JSON-LD로 삽입
3. 쇼핑몰 플랫폼에 따라:
   - **Cafe24**: 상품상세 레이아웃에 스크립트 추가
   - **Shopify**: theme.liquid 또는 product.liquid에 추가
   - **NHN Commerce**: 상품상세 템플릿에 추가
   - **자체 개발**: 상품 상세 페이지 컴포넌트에 동적 생성

### Step 4: FAQ 스키마 적용 (상품별 3~7개 Q&A)

1. `schemas/faq-schema.jsonld`를 참고하여 실제 FAQ 작성
2. 상품 페이지 하단에 FAQ 섹션 + FAQPage Schema 삽입
3. `templates/product-page-template.html`의 FAQ 섹션 구조 참고

### Step 5: 상품 설명 재작성 (가장 중요!)

`templates/product-page-template.html`의 상품 설명 구조를 참고하여:

1. **대화형 도입부**: "이런 분께 추천합니다"로 시작
2. **상황별 코디 제안**: 출근룩, 하객룩, 데이트룩 등
3. **사이즈 & 핏 테이블**: 체형별 사이즈 추천 포함
4. **소재 상세 정보**: `<dl>` 태그로 구조화
5. **FAQ 섹션**: 실제 고객이 묻는 질문 기반

### Step 6: 카테고리 페이지 최적화

`templates/category-page-template.html` 참고:

1. 카테고리 도입부에 직접적인 답변형 설명 추가
2. 소재별/체형별 비교 테이블 추가
3. CollectionPage + ItemList Schema 적용

### Step 7: 모니터링 설정

```bash
# AI 크롤러 방문 현황 분석 (최근 30일)
python scripts/ai-crawler-monitor.py /var/log/nginx/access.log

# 7일간 분석 + JSON 리포트 저장
python scripts/ai-crawler-monitor.py /var/log/nginx/access.log --days 7 --output report.json

# Schema 검증
python scripts/schema-validator.py schemas/product-schema.jsonld
```

## 플레이스홀더 치환 목록

모든 파일에서 다음 플레이스홀더를 실제 값으로 교체하세요:

| 플레이스홀더 | 설명 | 예시 |
|------------|------|------|
| `[브랜드명]` | 쇼핑몰/브랜드 이름 | 무드앤무드 |
| `[Brand Name English]` | 영문 브랜드명 | Mood & Mood |
| `yourdomain.com` | 실제 도메인 | moodandmood.com |
| `[대표자명]` | 대표자 이름 | 김OO |
| `[상세주소]` | 사업장 주소 | 서울시 강남구... |
| `[시/구]`, `[도/시]`, `[우편번호]` | 주소 구성 요소 | - |
| `+82-XX-XXXX-XXXX` | 고객센터 전화번호 | - |
| `ai-partnership@yourdomain.com` | AI 관련 문의 이메일 | - |
| SNS URL들 | 각 소셜 미디어 URL | - |

## Schema 검증 방법

### 온라인 도구

1. **Google Rich Results Test**: https://search.google.com/test/rich-results
   - 실제 URL 또는 코드 스니펫으로 테스트
   - Product, FAQ, Breadcrumb 스키마 검증

2. **Schema.org Validator**: https://validator.schema.org/
   - JSON-LD 코드 직접 붙여넣기로 검증

### 로컬 검증

```bash
python scripts/schema-validator.py schemas/product-schema.jsonld
```

## 주의사항

- **가격/재고는 실시간 동기화**: Schema의 가격과 실제 페이지 가격이 다르면 Google 페널티 가능
- **리뷰 조작 금지**: 가짜 리뷰를 Schema에 넣으면 Google 정책 위반
- **날짜 최신화**: `priceValidUntil`, `datePublished` 등의 날짜를 정기적으로 업데이트
- **모바일 최적화**: AI 검색의 대부분이 모바일에서 발생하므로 모바일 우선 확인
