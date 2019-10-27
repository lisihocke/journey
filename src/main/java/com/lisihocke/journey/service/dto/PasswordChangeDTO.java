package com.lisihocke.journey.service.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * A DTO representing a password change required data - current and new password.
 */
@Getter
@Setter
public class PasswordChangeDTO {
    private String currentPassword;
    private String newPassword;

    public PasswordChangeDTO() {
        // Empty constructor needed for Jackson.
    }

    public PasswordChangeDTO(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }
}
