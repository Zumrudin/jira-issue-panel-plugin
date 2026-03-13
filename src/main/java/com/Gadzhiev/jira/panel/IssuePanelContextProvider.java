package com.Gadzhiev.jira.panel;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.plugin.webfragment.contextproviders.AbstractJiraContextProvider;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.user.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

/**
 * Context provider for the custom issue panel.
 * Populates Velocity template with data from Active Objects.
 */
@Named
@Component
public class IssuePanelContextProvider extends AbstractJiraContextProvider {

    private static final Logger log = LoggerFactory.getLogger(IssuePanelContextProvider.class);

    private final PanelDataService panelDataService;

    @Inject
    public IssuePanelContextProvider(PanelDataService panelDataService) {
        this.panelDataService = panelDataService;
    }

    @Override
    public Map<String, Object> getContextMap(ApplicationUser user, JiraHelper jiraHelper) {
        Map<String, Object> context = new HashMap<>();

        try {
            Issue issue = (Issue) jiraHelper.getContextParams().get("issue");
            if (issue == null) {
                log.warn("Issue not found in context");
                context.put("error", "Issue not found");
                return context;
            }

            String issueKey = issue.getKey();
            context.put("issueKey", issueKey);
            context.put("currentUser", user != null ? user.getUsername() : "anonymous");

            // Load existing panel data
            PanelDataBean panelData = panelDataService.getPanelData(issueKey);
            if (panelData != null) {
                context.put("customText", panelData.getCustomText() != null ? panelData.getCustomText() : "");
                context.put("customNote", panelData.getCustomNote() != null ? panelData.getCustomNote() : "");
                context.put("updatedBy", panelData.getUpdatedBy() != null ? panelData.getUpdatedBy() : "");
                context.put("updatedAt", panelData.getUpdatedAt() != null ? panelData.getUpdatedAt() : 0L);
            } else {
                context.put("customText", "");
                context.put("customNote", "");
                context.put("updatedBy", "");
                context.put("updatedAt", 0L);
            }

            // REST API base URL for JS
            context.put("restBaseUrl", "/rest/gadzhiev-panel/1.0");

        } catch (Exception e) {
            log.error("Error building panel context", e);
            context.put("error", "Failed to load panel data");
        }

        return context;
    }
}
