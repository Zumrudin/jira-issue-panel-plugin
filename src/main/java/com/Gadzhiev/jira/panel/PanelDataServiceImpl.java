package com.Gadzhiev.jira.panel;

import com.Gadzhiev.jira.panel.ao.PanelData;
import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import net.java.ao.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Implementation of PanelDataService using Active Objects.
 */
@Named
@Component
public class PanelDataServiceImpl implements PanelDataService {

    private static final Logger log = LoggerFactory.getLogger(PanelDataServiceImpl.class);

    private final ActiveObjects ao;

    @Inject
    public PanelDataServiceImpl(@ComponentImport ActiveObjects ao) {
        this.ao = ao;
    }

    @Override
    public PanelDataBean getPanelData(String issueKey) {
        try {
            PanelData[] results = ao.find(
                PanelData.class,
                Query.select().where("ISSUE_KEY = ?", issueKey)
            );

            if (results.length == 0) {
                return null;
            }

            PanelData data = results[0];
            return new PanelDataBean(
                data.getIssueKey(),
                data.getCustomText(),
                data.getCustomNote(),
                data.getUpdatedBy(),
                data.getUpdatedAt()
            );
        } catch (Exception e) {
            log.error("Error fetching panel data for issue: " + issueKey, e);
            return null;
        }
    }

    @Override
    public void savePanelData(String issueKey, String customText, String customNote, String updatedBy) {
        try {
            PanelData[] results = ao.find(
                PanelData.class,
                Query.select().where("ISSUE_KEY = ?", issueKey)
            );

            PanelData data;
            if (results.length > 0) {
                data = results[0];
            } else {
                data = ao.create(PanelData.class);
                data.setIssueKey(issueKey);
            }

            data.setCustomText(customText);
            data.setCustomNote(customNote);
            data.setUpdatedBy(updatedBy);
            data.setUpdatedAt(System.currentTimeMillis());
            data.save();

            log.info("Panel data saved for issue: {}", issueKey);
        } catch (Exception e) {
            log.error("Error saving panel data for issue: " + issueKey, e);
            throw new RuntimeException("Failed to save panel data", e);
        }
    }
}
