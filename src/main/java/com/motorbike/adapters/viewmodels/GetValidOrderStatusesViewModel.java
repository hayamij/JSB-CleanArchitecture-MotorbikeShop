package com.motorbike.adapters.viewmodels;

import java.util.List;

public class GetValidOrderStatusesViewModel {
    public boolean success;
    public List<StatusOption> validStatuses;
    public String errorCode;
    public String errorMessage;

    public static class StatusOption {
        public String code;
        public String display;

        public StatusOption(String code, String display) {
            this.code = code;
            this.display = display;
        }
    }
}
