package com.arsyad.asset_management.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MonitoringResponse {

    private Long totalAsset;
    private Long available;
    private Long assigned;
    private Long maintenance;
    private Long retired;
}
