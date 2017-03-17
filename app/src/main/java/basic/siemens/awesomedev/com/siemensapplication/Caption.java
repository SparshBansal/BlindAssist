package basic.siemens.awesomedev.com.siemensapplication;

/**
 * Created by sparsh on 3/17/17.
 */

public class Caption {

    public String caption;
    public float confidenceScore;

    public String getCaption() {
        return caption;
    }

    public float getConfidenceScore() {
        return confidenceScore;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setConfidenceScore(float confidenceScore) {
        this.confidenceScore = confidenceScore;
    }
}
