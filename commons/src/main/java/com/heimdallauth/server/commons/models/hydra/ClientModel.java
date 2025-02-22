package com.heimdallauth.server.commons.models.hydra;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClientModel {
    private String id;
    private String clientName;
    private String clientDescription;
}
