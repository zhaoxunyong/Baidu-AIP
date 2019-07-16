package com.zerofinance.aip.model;

import com.aeasycredit.commons.lang.base.BaseModel;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * MatchParams
 */
public class MatchParams extends BaseModel {
	private static final long serialVersionUID = 810018537486788833L;

	private String image;

    @JsonProperty("image_type")
    private String imageType;

    @JsonProperty("face_type")
    private String faceType;

    @JsonProperty("quality_control")
    private String qualityControl;

    @JsonProperty("liveness_control")
    private String livenessControl;


    public MatchParams(String image, String imageType, String faceType) {
        this.image = image;
        this.imageType = imageType;
        this.faceType = faceType;
    }


    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageType() {
        return this.imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getFaceType() {
        return this.faceType;
    }

    public void setFaceType(String faceType) {
        this.faceType = faceType;
    }

    public String getQualityControl() {
        return this.qualityControl;
    }

    public void setQualityControl(String qualityControl) {
        this.qualityControl = qualityControl;
    }

    public String getLivenessControl() {
        return this.livenessControl;
    }

    public void setLivenessControl(String livenessControl) {
        this.livenessControl = livenessControl;
    }

}