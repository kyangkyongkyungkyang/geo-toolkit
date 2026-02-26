package com.geotoolkit.model;

/**
 * 사이즈별 실측 스펙.
 * 여성복 GEO에서 체형별 사이즈 추천의 근거 데이터로 사용된다.
 */
public class SizeSpec {

    private String size;        // S, M, L, XL
    private int waist;          // 허리 (cm)
    private int hip;            // 엉덩이 (cm)
    private int thigh;          // 허벅지 (cm)
    private int totalLength;    // 총기장 (cm)
    private String weightRange; // 추천 체중 범위

    public SizeSpec() {}

    public SizeSpec(String size, int waist, int hip, int thigh, int totalLength, String weightRange) {
        this.size = size;
        this.waist = waist;
        this.hip = hip;
        this.thigh = thigh;
        this.totalLength = totalLength;
        this.weightRange = weightRange;
    }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public int getWaist() { return waist; }
    public void setWaist(int waist) { this.waist = waist; }

    public int getHip() { return hip; }
    public void setHip(int hip) { this.hip = hip; }

    public int getThigh() { return thigh; }
    public void setThigh(int thigh) { this.thigh = thigh; }

    public int getTotalLength() { return totalLength; }
    public void setTotalLength(int totalLength) { this.totalLength = totalLength; }

    public String getWeightRange() { return weightRange; }
    public void setWeightRange(String weightRange) { this.weightRange = weightRange; }
}
