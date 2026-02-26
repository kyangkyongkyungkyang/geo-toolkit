#!/usr/bin/env python3
"""
AI 크롤러 모니터링 스크립트
========================================
웹서버 access.log를 분석하여 AI 크롤러 방문 현황을 모니터링합니다.

사용법:
  python ai-crawler-monitor.py /path/to/access.log
  python ai-crawler-monitor.py /path/to/access.log --days 7
  python ai-crawler-monitor.py /path/to/access.log --output report.json

지원 로그 형식: Apache/Nginx Combined Log Format
"""

import re
import sys
import json
import argparse
from collections import Counter, defaultdict
from datetime import datetime, timedelta
from pathlib import Path

# AI 크롤러 User-Agent 패턴 정의
AI_CRAWLERS = {
    # OpenAI
    "GPTBot": {
        "pattern": r"GPTBot",
        "company": "OpenAI",
        "purpose": "학습 + 검색",
        "geo_impact": "높음 - ChatGPT 검색 결과에 직접 영향"
    },
    "ChatGPT-User": {
        "pattern": r"ChatGPT-User",
        "company": "OpenAI",
        "purpose": "실시간 검색",
        "geo_impact": "매우 높음 - 사용자 질문에 대한 실시간 답변"
    },
    "OAI-SearchBot": {
        "pattern": r"OAI-SearchBot",
        "company": "OpenAI",
        "purpose": "검색 전용",
        "geo_impact": "높음"
    },
    # Anthropic
    "ClaudeBot": {
        "pattern": r"ClaudeBot|claudebot",
        "company": "Anthropic",
        "purpose": "학습 + 검색",
        "geo_impact": "높음 - Claude 검색에 영향"
    },
    # Perplexity
    "PerplexityBot": {
        "pattern": r"PerplexityBot",
        "company": "Perplexity",
        "purpose": "실시간 검색",
        "geo_impact": "매우 높음 - Perplexity 답변에 직접 인용"
    },
    # Google
    "Google-Extended": {
        "pattern": r"Google-Extended",
        "company": "Google",
        "purpose": "AI 학습",
        "geo_impact": "중간 - Gemini 학습에 영향"
    },
    "Googlebot": {
        "pattern": r"Googlebot(?!.*Google-Extended)",
        "company": "Google",
        "purpose": "검색 인덱싱 + AI Overviews",
        "geo_impact": "매우 높음 - AI Overviews 소스"
    },
    # Microsoft
    "Bingbot": {
        "pattern": r"bingbot",
        "company": "Microsoft",
        "purpose": "검색 + Copilot",
        "geo_impact": "높음 - Copilot 답변 소스"
    },
    # Meta
    "Meta-ExternalAgent": {
        "pattern": r"Meta-ExternalAgent|FacebookExternalHit",
        "company": "Meta",
        "purpose": "AI 학습",
        "geo_impact": "낮음 - 학습 전용"
    },
    # ByteDance
    "Bytespider": {
        "pattern": r"Bytespider",
        "company": "ByteDance",
        "purpose": "AI 학습",
        "geo_impact": "낮음 - 학습 전용"
    },
    # Amazon
    "Amazonbot": {
        "pattern": r"Amazonbot",
        "company": "Amazon",
        "purpose": "Alexa/Rufus",
        "geo_impact": "중간 - Rufus 쇼핑 추천에 영향"
    },
    # Apple
    "Applebot": {
        "pattern": r"Applebot",
        "company": "Apple",
        "purpose": "Siri/Safari",
        "geo_impact": "중간 - Apple Intelligence 소스"
    },
    # Naver
    "Yeti": {
        "pattern": r"Yeti/",
        "company": "Naver",
        "purpose": "네이버 검색",
        "geo_impact": "높음 (한국) - 네이버 AI 검색에 영향"
    },
}

# Apache/Nginx Combined Log Format 파싱 정규식
LOG_PATTERN = re.compile(
    r'(?P<ip>\S+)\s+\S+\s+\S+\s+'
    r'\[(?P<datetime>[^\]]+)\]\s+'
    r'"(?P<method>\S+)\s+(?P<path>\S+)\s+\S+"\s+'
    r'(?P<status>\d+)\s+(?P<size>\S+)\s+'
    r'"(?P<referer>[^"]*)"\s+'
    r'"(?P<useragent>[^"]*)"'
)


def parse_log_line(line: str) -> dict | None:
    """로그 한 줄을 파싱하여 딕셔너리로 반환"""
    match = LOG_PATTERN.match(line)
    if not match:
        return None
    return match.groupdict()


def identify_crawler(user_agent: str) -> str | None:
    """User-Agent에서 AI 크롤러를 식별"""
    for name, info in AI_CRAWLERS.items():
        if re.search(info["pattern"], user_agent, re.IGNORECASE):
            return name
    return None


def parse_datetime(dt_str: str) -> datetime | None:
    """로그 날짜 문자열을 파싱"""
    try:
        return datetime.strptime(dt_str, "%d/%b/%Y:%H:%M:%S %z")
    except ValueError:
        try:
            return datetime.strptime(dt_str.split()[0], "%d/%b/%Y:%H:%M:%S")
        except ValueError:
            return None


def analyze_log(log_path: str, days: int = 30) -> dict:
    """로그 파일 분석"""
    cutoff = datetime.now() - timedelta(days=days)

    stats = {
        "total_requests": 0,
        "ai_crawler_requests": 0,
        "crawler_counts": Counter(),
        "crawler_paths": defaultdict(Counter),
        "crawler_status_codes": defaultdict(Counter),
        "daily_crawler_counts": defaultdict(Counter),
        "top_crawled_products": Counter(),
        "top_crawled_categories": Counter(),
        "top_crawled_blogs": Counter(),
    }

    log_file = Path(log_path)
    if not log_file.exists():
        print(f"Error: Log file not found: {log_path}")
        sys.exit(1)

    with open(log_file, "r", encoding="utf-8", errors="ignore") as f:
        for line in f:
            parsed = parse_log_line(line.strip())
            if not parsed:
                continue

            log_dt = parse_datetime(parsed["datetime"])
            if log_dt and log_dt.replace(tzinfo=None) < cutoff:
                continue

            stats["total_requests"] += 1
            crawler = identify_crawler(parsed["useragent"])

            if crawler:
                stats["ai_crawler_requests"] += 1
                stats["crawler_counts"][crawler] += 1
                stats["crawler_paths"][crawler][parsed["path"]] += 1
                stats["crawler_status_codes"][crawler][parsed["status"]] += 1

                if log_dt:
                    day_key = log_dt.strftime("%Y-%m-%d")
                    stats["daily_crawler_counts"][day_key][crawler] += 1

                path = parsed["path"]
                if "/products/" in path:
                    stats["top_crawled_products"][path] += 1
                elif "/categories/" in path:
                    stats["top_crawled_categories"][path] += 1
                elif "/blog/" in path:
                    stats["top_crawled_blogs"][path] += 1

    return stats


def generate_report(stats: dict) -> str:
    """분석 결과 보고서 생성"""
    lines = []
    lines.append("=" * 60)
    lines.append("  AI 크롤러 모니터링 리포트")
    lines.append(f"  생성일: {datetime.now().strftime('%Y-%m-%d %H:%M')}")
    lines.append("=" * 60)
    lines.append("")

    # 전체 요약
    total = stats["total_requests"]
    ai_total = stats["ai_crawler_requests"]
    ratio = (ai_total / total * 100) if total > 0 else 0

    lines.append(f"  전체 요청 수: {total:,}")
    lines.append(f"  AI 크롤러 요청: {ai_total:,} ({ratio:.1f}%)")
    lines.append("")

    # 크롤러별 상세
    lines.append("-" * 60)
    lines.append("  크롤러별 방문 현황")
    lines.append("-" * 60)
    lines.append(f"  {'크롤러':<20} {'요청수':>8} {'비율':>7} {'회사':<12} {'GEO 영향도'}")
    lines.append("-" * 60)

    for crawler, count in stats["crawler_counts"].most_common():
        pct = (count / ai_total * 100) if ai_total > 0 else 0
        info = AI_CRAWLERS.get(crawler, {})
        company = info.get("company", "Unknown")
        impact = info.get("geo_impact", "Unknown")
        lines.append(f"  {crawler:<20} {count:>8,} {pct:>6.1f}% {company:<12} {impact}")

    lines.append("")

    # GEO 중요 크롤러 알림
    lines.append("-" * 60)
    lines.append("  GEO 핵심 크롤러 상태")
    lines.append("-" * 60)

    geo_critical = ["ChatGPT-User", "GPTBot", "PerplexityBot", "ClaudeBot", "Googlebot"]
    for crawler in geo_critical:
        count = stats["crawler_counts"].get(crawler, 0)
        status = "ACTIVE" if count > 0 else "NOT DETECTED"
        icon = "[OK]" if count > 0 else "[!!]"
        lines.append(f"  {icon} {crawler:<20} {status:<15} ({count:,} requests)")

    lines.append("")

    # 가장 많이 크롤링된 상품
    if stats["top_crawled_products"]:
        lines.append("-" * 60)
        lines.append("  AI가 가장 많이 방문한 상품 페이지 (TOP 10)")
        lines.append("-" * 60)
        for path, count in stats["top_crawled_products"].most_common(10):
            lines.append(f"  {count:>6,}  {path}")
        lines.append("")

    # 가장 많이 크롤링된 블로그
    if stats["top_crawled_blogs"]:
        lines.append("-" * 60)
        lines.append("  AI가 가장 많이 방문한 블로그/가이드 (TOP 10)")
        lines.append("-" * 60)
        for path, count in stats["top_crawled_blogs"].most_common(10):
            lines.append(f"  {count:>6,}  {path}")
        lines.append("")

    # 권고사항
    lines.append("=" * 60)
    lines.append("  권고사항")
    lines.append("=" * 60)

    missing_critical = [c for c in geo_critical if stats["crawler_counts"].get(c, 0) == 0]
    if missing_critical:
        lines.append("")
        lines.append("  [WARNING] 다음 GEO 핵심 크롤러가 감지되지 않았습니다:")
        for c in missing_critical:
            info = AI_CRAWLERS[c]
            lines.append(f"    - {c} ({info['company']}): {info['geo_impact']}")
        lines.append("")
        lines.append("  조치사항:")
        lines.append("    1. robots.txt에서 해당 크롤러가 차단되어 있지 않은지 확인")
        lines.append("    2. 방화벽/CDN에서 해당 User-Agent가 차단되지 않았는지 확인")
        lines.append("    3. 서버 로그 기간을 늘려 재분석")

    if stats["top_crawled_products"]:
        lines.append("")
        lines.append("  [TIP] AI가 자주 방문하는 상품은 GEO 최적화 우선 대상입니다:")
        lines.append("    - 해당 상품의 Product Schema가 완비되어 있는지 확인")
        lines.append("    - FAQ 섹션이 추가되어 있는지 확인")
        lines.append("    - 대화형 상품 설명이 적용되어 있는지 확인")

    lines.append("")
    return "\n".join(lines)


def main():
    parser = argparse.ArgumentParser(
        description="AI 크롤러 모니터링 - 여성복 쇼핑몰 GEO 최적화"
    )
    parser.add_argument("logfile", help="분석할 웹서버 access.log 경로")
    parser.add_argument("--days", type=int, default=30, help="분석 기간 (일, 기본값: 30)")
    parser.add_argument("--output", help="JSON 결과 저장 경로 (선택)")
    args = parser.parse_args()

    print(f"\nAnalyzing: {args.logfile} (last {args.days} days)...\n")
    stats = analyze_log(args.logfile, args.days)
    report = generate_report(stats)
    print(report)

    if args.output:
        output_data = {
            "generated_at": datetime.now().isoformat(),
            "analysis_period_days": args.days,
            "total_requests": stats["total_requests"],
            "ai_crawler_requests": stats["ai_crawler_requests"],
            "crawler_counts": dict(stats["crawler_counts"]),
            "top_crawled_products": dict(stats["top_crawled_products"].most_common(20)),
            "top_crawled_blogs": dict(stats["top_crawled_blogs"].most_common(20)),
            "daily_counts": {
                day: dict(counts) for day, counts in stats["daily_crawler_counts"].items()
            },
        }
        with open(args.output, "w", encoding="utf-8") as f:
            json.dump(output_data, f, ensure_ascii=False, indent=2)
        print(f"\nJSON report saved to: {args.output}")


if __name__ == "__main__":
    main()
