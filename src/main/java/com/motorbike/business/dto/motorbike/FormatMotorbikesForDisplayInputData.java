package com.motorbike.business.dto.motorbike;

import java.util.List;
import com.motorbike.domain.entities.XeMay;

/**
 * UC-74: Format Motorbikes For Display
 * Input data for formatting motorbike list to display DTOs
 */
public class FormatMotorbikesForDisplayInputData {
    
    private final List<XeMay> motorbikes;
    
    public FormatMotorbikesForDisplayInputData(List<XeMay> motorbikes) {
        this.motorbikes = motorbikes;
    }
    
    public List<XeMay> getMotorbikes() {
        return motorbikes;
    }
}
