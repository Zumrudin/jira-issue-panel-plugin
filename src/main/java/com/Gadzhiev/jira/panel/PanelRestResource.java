package com.Gadzhiev.jira.panel;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.user.UserManager;
import org.codehaus.jackson.annotate.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST resource for reading and writing custom panel data.
 * Base path: /rest/gadzhiev-panel/1.0
 */
@Named
@Component
@Path("/panel")
public class PanelRestResource {

    private static final Logger log = LoggerFactory.getLogger(PanelRestResource.class);

    private final PanelDataService panelDataService;

    @Inject
    public PanelRestResource(PanelDataService panelDataService) {
        this.panelDataService = panelDataService;
    }

    /**
     * GET /rest/gadzhiev-panel/1.0/panel/{issueKey}
     * Returns panel data for the given issue.
     */
    @GET
    @Path("/{issueKey}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPanelData(@PathParam("issueKey") String issueKey,
                                 @Context HttpServletRequest request) {
        try {
            ApplicationUser user = getCurrentUser();
            if (user == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ErrorResponse("Not authenticated")).build();
            }

            PanelDataBean data = panelDataService.getPanelData(issueKey);
            if (data == null) {
                return Response.ok(new PanelResponse(issueKey, "", "", "", 0L)).build();
            }

            return Response.ok(new PanelResponse(
                data.getIssueKey(),
                data.getCustomText(),
                data.getCustomNote(),
                data.getUpdatedBy(),
                data.getUpdatedAt()
            )).build();

        } catch (Exception e) {
            log.error("Error getting panel data for: " + issueKey, e);
            return Response.serverError()
                .entity(new ErrorResponse("Internal server error")).build();
        }
    }

    /**
     * POST /rest/gadzhiev-panel/1.0/panel/{issueKey}
     * Saves panel data for the given issue.
     */
    @POST
    @Path("/{issueKey}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response savePanelData(@PathParam("issueKey") String issueKey,
                                  PanelSaveRequest saveRequest,
                                  @Context HttpServletRequest request) {
        try {
            ApplicationUser user = getCurrentUser();
            if (user == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ErrorResponse("Not authenticated")).build();
            }

            if (saveRequest == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Request body is required")).build();
            }

            panelDataService.savePanelData(
                issueKey,
                saveRequest.getCustomText(),
                saveRequest.getCustomNote(),
                user.getUsername()
            );

            return Response.ok(new SuccessResponse("Data saved successfully")).build();

        } catch (Exception e) {
            log.error("Error saving panel data for: " + issueKey, e);
            return Response.serverError()
                .entity(new ErrorResponse("Failed to save data")).build();
        }
    }

    private ApplicationUser getCurrentUser() {
        return ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
    }

    // ---- Inner request/response classes ----

    public static class PanelSaveRequest {
        @JsonProperty private String customText;
        @JsonProperty private String customNote;

        public String getCustomText() { return customText; }
        public void setCustomText(String customText) { this.customText = customText; }

        public String getCustomNote() { return customNote; }
        public void setCustomNote(String customNote) { this.customNote = customNote; }
    }

    public static class PanelResponse {
        @JsonProperty public String issueKey;
        @JsonProperty public String customText;
        @JsonProperty public String customNote;
        @JsonProperty public String updatedBy;
        @JsonProperty public Long updatedAt;

        public PanelResponse(String issueKey, String customText, String customNote,
                             String updatedBy, Long updatedAt) {
            this.issueKey = issueKey;
            this.customText = customText;
            this.customNote = customNote;
            this.updatedBy = updatedBy;
            this.updatedAt = updatedAt;
        }
    }

    public static class SuccessResponse {
        @JsonProperty public String message;
        public SuccessResponse(String message) { this.message = message; }
    }

    public static class ErrorResponse {
        @JsonProperty public String error;
        public ErrorResponse(String error) { this.error = error; }
    }
}
