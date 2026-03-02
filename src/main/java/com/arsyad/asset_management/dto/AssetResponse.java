package com.arsyad.asset_management.dto;

import com.arsyad.asset_management.entity.Asset;
import com.arsyad.asset_management.entity.AssetStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssetResponse {

    private Long id;
    private String name;
    private String serialNumber;
    private AssetStatus status;
    private String assignedTo;
}
