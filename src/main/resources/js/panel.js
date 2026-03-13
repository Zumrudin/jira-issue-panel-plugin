/**
 * Gadzhiev Custom Issue Panel - JavaScript
 * Handles edit/save/cancel functionality via REST API.
 */
(function ($) {
    'use strict';

    var config = window.gadzhievPanelConfig || {};

    function showStatus(message, type) {
        var $status = $('#gadzhiev-panel-status');
        $status.text(message)
               .removeClass('success error')
               .addClass(type)
               .show();
        setTimeout(function () {
            $status.fadeOut();
        }, 3000);
    }

    function switchToEditMode() {
        $('#gadzhiev-panel-view').hide();
        $('#gadzhiev-panel-edit').show();
        $('#gadzhiev-input-customText').focus();
    }

    function switchToViewMode() {
        $('#gadzhiev-panel-edit').hide();
        $('#gadzhiev-panel-view').show();
    }

    function saveData() {
        var customText = $('#gadzhiev-input-customText').val().trim();
        var customNote = $('#gadzhiev-input-customNote').val().trim();

        var $saveBtn = $('#gadzhiev-save-btn');
        $saveBtn.prop('disabled', true).text('Saving...');

        $.ajax({
            url: config.restBaseUrl + '/panel/' + config.issueKey,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                customText: customText,
                customNote: customNote
            }),
            success: function (response) {
                // Update view mode values
                $('#gadzhiev-view-customText').text(customText || 'Not set');
                $('#gadzhiev-view-customNote').text(customNote || 'Not set');

                switchToViewMode();
                showStatus('Saved successfully!', 'success');
            },
            error: function (xhr) {
                var msg = 'Save failed';
                try {
                    var resp = JSON.parse(xhr.responseText);
                    msg = resp.error || msg;
                } catch (e) {}
                showStatus(msg, 'error');
            },
            complete: function () {
                $saveBtn.prop('disabled', false).text('Save');
            }
        });
    }

    // Wire up events when DOM is ready
    $(document).ready(function () {
        if (!$('#gadzhiev-custom-panel').length) {
            return;
        }

        $('#gadzhiev-edit-btn').on('click', function () {
            switchToEditMode();
        });

        $('#gadzhiev-cancel-btn').on('click', function () {
            switchToViewMode();
        });

        $('#gadzhiev-save-btn').on('click', function () {
            saveData();
        });

        // Allow Ctrl+Enter to save from textarea
        $('#gadzhiev-input-customNote').on('keydown', function (e) {
            if (e.ctrlKey && e.key === 'Enter') {
                saveData();
            }
        });
    });

})(AJS.$);
