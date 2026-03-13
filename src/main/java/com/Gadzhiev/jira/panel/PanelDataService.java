package com.Gadzhiev.jira.panel;

/**
 * Service interface for managing custom panel data.
 */
public interface PanelDataService {

    /**
     * Get panel data for a specific issue.
     *
     * @param issueKey e.g. "PROJECT-123"
     * @return PanelDataBean or null if not found
     */
    PanelDataBean getPanelData(String issueKey);

    /**
     * Save or update panel data for a specific issue.
     *
     * @param issueKey   e.g. "PROJECT-123"
     * @param customText main text field value
     * @param customNote notes field value
     * @param updatedBy  username of the person saving
     */
    void savePanelData(String issueKey, String customText, String customNote, String updatedBy);
}
