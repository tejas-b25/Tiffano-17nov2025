package com.tiffino.subscriptionservice.dto;

import com.tiffino.subscriptionservice.enums.BillingCycle;
import com.tiffino.subscriptionservice.enums.SpiceLevel;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomizationDTO {

    private SpiceLevel spiceLevel;

    @Size(max = 255, message = "Additional requests must be within 255 characters")
    private String additionalRequests;

    @Pattern(regexp = "Small|Medium|Large", message = "Portion size must be Small, Medium or Large")
    private String portionSize;

    @NotNull(message = "Billing cycle is required")
    private BillingCycle billingCycle;

    @NotNull(message = "Auto-renew setting must be specified")
    private Boolean autoRenew;

    private Integer totalInstallments;
}