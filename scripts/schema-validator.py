#!/usr/bin/env python3
"""
Schema Markup 검증 스크립트
========================================
여성복 쇼핑몰의 Product Schema JSON-LD가 GEO 요구사항을 충족하는지 검증합니다.

사용법:
  python schema-validator.py ../schemas/product-schema.jsonld
  python schema-validator.py https://yourdomain.com/products/example

검증 항목:
  - schema.org Product 필수 필드 확인
  - GEO 최적화 권장 필드 확인
  - 여성복 특화 필드 확인
  - 값의 유효성 검증
"""

import json
import sys
from pathlib import Path

# 필수 필드 (없으면 AI 검색 노출에 심각한 영향)
REQUIRED_FIELDS = {
    "name": "상품명 - AI가 제품을 식별하는 가장 기본적인 요소",
    "description": "상품 설명 - AI 인용의 핵심 소스. 대화형으로 작성 필요",
    "brand": "브랜드 정보 - 엔티티 권위 확립에 필수",
    "image": "상품 이미지 - 멀티모달 AI 검색 대응",
    "offers": "가격/재고 정보 - 쇼핑 쿼리 응답에 필수",
    "url": "상품 URL - AI 인용 시 출처 링크",
}

# 권장 필드 (GEO 효과를 높이는 필드)
RECOMMENDED_FIELDS = {
    "sku": "SKU 코드 - 제품 고유 식별",
    "gtin13": "GTIN 바코드 - AI의 제품 신뢰도를 크게 향상",
    "material": "소재 정보 - '린넨 팬츠 추천' 같은 소재 기반 쿼리 대응",
    "color": "색상 - 색상 기반 검색 쿼리 대응",
    "aggregateRating": "평점 집계 - AI가 추천 시 평점을 참조",
    "review": "리뷰 - AI가 제3자 의견으로 활용",
    "audience": "타겟 고객 - 연령/성별 기반 추천에 활용",
    "additionalProperty": "추가 속성 - 핏, 체형 추천, 상황 등 여성복 특화 정보",
    "category": "카테고리 - AI의 상품 분류 이해에 도움",
    "size": "사이즈 옵션 - 사이즈 관련 쿼리 대응",
}

# 여성복 특화 권장 additionalProperty
FASHION_PROPERTIES = {
    "핏": "슬림/레귤러/와이드 등 핏 정보 - 체형별 추천에 핵심",
    "추천 상황": "출근룩/하객룩/데이트룩 등 - 상황별 코디 쿼리 대응",
    "추천 체형": "어떤 체형에 어울리는지 - AI 체형별 추천의 핵심 소스",
    "두께감": "얇음/중간/두꺼움 - 계절별 추천에 활용",
    "신축성": "없음/약간/좋음 - 편안함 관련 쿼리 대응",
    "비침": "있음/없음 - 색상별 착용감 안내",
    "기장": "크롭/미디/풀기장 - 키별 추천에 활용",
    "세탁방법": "관리 편의성 정보 - 실용적 쿼리 대응",
}


class SchemaValidator:
    def __init__(self, schema_data: dict):
        self.data = schema_data
        self.errors = []
        self.warnings = []
        self.info = []
        self.score = 100  # GEO 최적화 점수 (100점 만점)

    def validate(self) -> int:
        """전체 검증 실행, GEO 점수 반환"""
        self._check_type()
        self._check_required_fields()
        self._check_recommended_fields()
        self._check_fashion_properties()
        self._check_description_quality()
        self._check_offers_detail()
        self._check_image_quality()
        self._check_review_data()
        return max(0, self.score)

    def _check_type(self):
        schema_type = self.data.get("@type")
        if schema_type != "Product":
            self.errors.append(f"@type이 'Product'가 아닙니다: '{schema_type}'")
            self.score -= 20

    def _check_required_fields(self):
        for field, desc in REQUIRED_FIELDS.items():
            if field not in self.data or not self.data[field]:
                self.errors.append(f"[필수] '{field}' 누락 - {desc}")
                self.score -= 10
            else:
                self.info.append(f"[OK] '{field}' 확인됨")

    def _check_recommended_fields(self):
        for field, desc in RECOMMENDED_FIELDS.items():
            if field not in self.data or not self.data[field]:
                self.warnings.append(f"[권장] '{field}' 누락 - {desc}")
                self.score -= 3
            else:
                self.info.append(f"[OK] '{field}' 확인됨")

    def _check_fashion_properties(self):
        props = self.data.get("additionalProperty", [])
        prop_names = {p.get("name", "") for p in props if isinstance(p, dict)}

        found = 0
        for prop_name, desc in FASHION_PROPERTIES.items():
            if prop_name in prop_names:
                found += 1
                self.info.append(f"[OK] 패션 속성 '{prop_name}' 확인됨")
            else:
                self.warnings.append(f"[패션특화] '{prop_name}' 누락 - {desc}")
                self.score -= 2

        if found == 0:
            self.errors.append(
                "[패션특화] additionalProperty에 여성복 특화 속성이 하나도 없습니다. "
                "핏, 추천 상황, 추천 체형 등을 추가하세요."
            )

    def _check_description_quality(self):
        desc = self.data.get("description", "")
        if not desc:
            return

        # 길이 체크
        if len(desc) < 50:
            self.warnings.append(
                f"[설명] 상품 설명이 너무 짧습니다 ({len(desc)}자). "
                "AI 인용을 위해 최소 100자 이상의 대화형 설명을 권장합니다."
            )
            self.score -= 5

        # 상황/체형 키워드 포함 여부
        context_keywords = ["출근", "여행", "결혼식", "하객", "데이트", "캐주얼", "오피스"]
        body_keywords = ["체형", "허벅지", "키", "cm", "사이즈"]

        has_context = any(kw in desc for kw in context_keywords)
        has_body = any(kw in desc for kw in body_keywords)

        if not has_context:
            self.warnings.append(
                "[설명] 착용 상황(출근, 여행, 하객 등)이 언급되지 않았습니다. "
                "AI가 '결혼식 뭐 입지' 같은 쿼리에 추천하려면 상황 정보가 필요합니다."
            )
            self.score -= 3

        if not has_body:
            self.warnings.append(
                "[설명] 체형/사이즈 관련 정보가 없습니다. "
                "AI가 '허벅지 두꺼운 체형 바지 추천' 같은 쿼리에 답하려면 체형 정보가 필요합니다."
            )
            self.score -= 3

    def _check_offers_detail(self):
        offers = self.data.get("offers", {})
        if not offers:
            return

        # 가격 확인
        price = offers.get("price") or offers.get("lowPrice")
        if not price:
            self.errors.append("[가격] 가격 정보가 없습니다. AI 쇼핑 추천의 필수 요소입니다.")
            self.score -= 8

        # 재고 상태
        if "availability" not in offers:
            self.warnings.append("[재고] availability가 없습니다. 재고 상태 표시를 권장합니다.")
            self.score -= 2

        # 배송 정보
        if "shippingDetails" not in offers:
            self.warnings.append("[배송] shippingDetails가 없습니다. 배송비/기간 정보 추가를 권장합니다.")
            self.score -= 1

        # 반품 정책
        if "hasMerchantReturnPolicy" not in offers:
            self.warnings.append("[반품] hasMerchantReturnPolicy가 없습니다. 반품 정책 추가를 권장합니다.")
            self.score -= 1

    def _check_image_quality(self):
        images = self.data.get("image", [])
        if isinstance(images, str):
            images = [images]

        if len(images) < 3:
            self.warnings.append(
                f"[이미지] 이미지가 {len(images)}개뿐입니다. "
                "멀티모달 AI 검색 대응을 위해 최소 3장(메인, 디테일, 모델컷)을 권장합니다."
            )
            self.score -= 2

    def _check_review_data(self):
        rating = self.data.get("aggregateRating", {})
        reviews = self.data.get("review", [])

        if not rating:
            self.warnings.append("[리뷰] aggregateRating이 없습니다. AI는 평점이 있는 제품을 우선 추천합니다.")
            self.score -= 3

        if not reviews:
            self.warnings.append("[리뷰] 개별 review가 없습니다. 대표 리뷰 2~3개를 Schema에 포함하면 AI 인용률이 높아집니다.")
            self.score -= 2

    def print_report(self):
        """검증 결과 출력"""
        score = self.validate()

        print("=" * 60)
        print("  GEO Schema Markup 검증 리포트")
        print("=" * 60)
        print()

        # 점수 등급
        if score >= 90:
            grade = "A+ (GEO 최적화 우수)"
        elif score >= 80:
            grade = "A  (양호, 약간의 개선 가능)"
        elif score >= 70:
            grade = "B  (기본 충족, 개선 권장)"
        elif score >= 50:
            grade = "C  (상당한 개선 필요)"
        else:
            grade = "D  (GEO 최적화 미흡)"

        print(f"  GEO 최적화 점수: {score}/100 ({grade})")
        print()

        if self.errors:
            print("-" * 60)
            print(f"  오류 ({len(self.errors)}건) - 반드시 수정 필요")
            print("-" * 60)
            for err in self.errors:
                print(f"  {err}")
            print()

        if self.warnings:
            print("-" * 60)
            print(f"  경고 ({len(self.warnings)}건) - 개선 권장")
            print("-" * 60)
            for warn in self.warnings:
                print(f"  {warn}")
            print()

        if self.info:
            print("-" * 60)
            print(f"  확인됨 ({len(self.info)}건)")
            print("-" * 60)
            for item in self.info:
                print(f"  {item}")
            print()


def main():
    if len(sys.argv) < 2:
        print("Usage: python schema-validator.py <schema.jsonld>")
        print("Example: python schema-validator.py ../schemas/product-schema.jsonld")
        sys.exit(1)

    schema_path = Path(sys.argv[1])
    if not schema_path.exists():
        print(f"Error: File not found: {schema_path}")
        sys.exit(1)

    with open(schema_path, "r", encoding="utf-8") as f:
        schema_data = json.load(f)

    validator = SchemaValidator(schema_data)
    validator.print_report()


if __name__ == "__main__":
    main()
