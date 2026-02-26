package com.geotoolkit.model;

/**
 * FAQ 항목.
 * Schema.org FAQPage의 Question/Answer 쌍에 대응.
 * AI 엔진이 Q&A 형식을 강하게 선호하므로 GEO의 핵심 요소이다.
 */
public class FaqItem {

    private String question;
    private String answer;

    public FaqItem() {}

    public FaqItem(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }
}
