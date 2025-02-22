package com.heimdallauth.server.documents;

import com.heimdallauth.server.commons.models.hydra.AuthorizationServerModel;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthorizationServerDocument {
    @Id
    private String id;
    private String authorizationServerName;
    private String authorizationServerDescription;
    private String issueUrl;
    private String signingKeyId;
    private boolean isActive;
    private List<String> authorizedServerIds;

    public AuthorizationServerModel toAuthorizationServerModel() {
        return AuthorizationServerModel.builder()
                .id(this.getId())
                .authorizationServerName(this.getAuthorizationServerName())
                .authorizationServerDescription(this.getAuthorizationServerDescription())
                .issueUrl(this.getIssueUrl())
                .isActive(this.isActive())
                .build();
    }
}
