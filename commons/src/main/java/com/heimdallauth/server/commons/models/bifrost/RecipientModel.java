package com.heimdallauth.server.commons.models.bifrost;

import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RecipientModel {
    private List<String> to;
    private List<String> cc;
    private List<String> bcc;
}
