package com.Gadzhiev.jira.panel.ao;

import net.java.ao.Entity;
import net.java.ao.schema.Column;
import net.java.ao.schema.Table;

/**
 * Active Objects entity for storing custom panel data per issue.
 */
@Table("PANEL_DATA")
public interface PanelData extends Entity {

    @Column("ISSUE_KEY")
    String getIssueKey();
    void setIssueKey(String issueKey);

    @Column("CUSTOM_TEXT")
    String getCustomText();
    void setCustomText(String customText);

    @Column("CUSTOM_NOTE")
    String getCustomNote();
    void setCustomNote(String customNote);

    @Column("UPDATED_BY")
    String getUpdatedBy();
    void setUpdatedBy(String updatedBy);

    @Column("UPDATED_AT")
    Long getUpdatedAt();
    void setUpdatedAt(Long updatedAt);
}
