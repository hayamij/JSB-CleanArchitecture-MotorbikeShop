package com.motorbike.business.dto.accessory;

import com.motorbike.domain.entities.PhuKienXeMay;
import java.util.List;

public class FormatAccessoriesForDisplayInputData {
    private final List<PhuKienXeMay> accessories;

    public FormatAccessoriesForDisplayInputData(List<PhuKienXeMay> accessories) {
        this.accessories = accessories;
    }

    public List<PhuKienXeMay> getAccessories() {
        return accessories;
    }
}
