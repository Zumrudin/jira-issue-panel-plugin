package com.Gadzhiev.jira.panel;

/**
 * Simple data transfer object for panel data.
 */
public class PanelDataBean {

    private String issueKey;
    private String customText;
    private String customNote;
    private String updatedBy;
    private Long updatedAt;

    public PanelDataBean() {}

    public PanelDataBean(String issueKey, String customText, String customNote,
                         String updatedBy, Long updatedAt) {
        this.issueKey = issueKey;
        this.customText = customText;
        this.customNote = customNote;
        this.updatedBy = updatedBy;
        this.updatedAt = updatedAt;
    }

    public String getIssueKey() { return issueKey; }
    public void setIssueKey(String issueKey) { this.issueKey = issueKey; }

    public String getCustomText() { return customText; }
    public void setCustomText(String customText) { this.customText = customText; }

    public String getCustomNote() { return customNote; }
    public void setCustomNote(String customNote) { this.customNote = customNote; }

    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }

    public Long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Long updatedAt) { this.updatedAt = updatedAt; }
}
