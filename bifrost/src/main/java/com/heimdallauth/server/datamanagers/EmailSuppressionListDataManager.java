package com.heimdallauth.server.datamanagers;

import com.heimdallauth.server.commons.models.bifrost.EmailSuppressionList;

import java.util.List;
import java.util.Optional;

public interface EmailSuppressionListDataManager {
    EmailSuppressionList createEmailSuppressionList(String suppressionListName, List<String> emailAddresses, boolean blockDelivery);
    Optional<EmailSuppressionList> getEmailSuppressionListByName(String suppressionListName);
    Optional<EmailSuppressionList> getEmailSuppressionListById(String suppressionListId);
    void deleteEmailSuppressionList(String suppressionListId);
    List<EmailSuppressionList> getAllEmailSuppressionLists();
    EmailSuppressionList addEmailsToSuppressionList(String suppressionListId, List<String> emailAddresses, boolean blockDelivery);
    EmailSuppressionList removeEmailsFromSuppressionList(String suppressionListId, List<String> emailAddresses);
    EmailSuppressionList updateEmailSuppressionList(String suppressionListId, String suppressionListName, List<String> emailAddresses, boolean blockDelivery);
}
