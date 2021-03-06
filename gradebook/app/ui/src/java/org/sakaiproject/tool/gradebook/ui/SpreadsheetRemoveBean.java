/*******************************************************************************
 * Copyright (c) 2006, 2008 The Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.opensource.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package org.sakaiproject.tool.gradebook.ui;

import org.sakaiproject.tool.gradebook.Spreadsheet;
import org.sakaiproject.tool.gradebook.jsf.FacesUtil;
import org.sakaiproject.service.gradebook.shared.StaleObjectModificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * User: louis
 * Date: Jun 14, 2006
 * Time: 11:12:51 AM
 */
public class SpreadsheetRemoveBean extends GradebookDependentBean implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(SpreadsheetRemoveBean.class);

    private Long spreadsheetId;
    private boolean removeConfirmed;
    private Spreadsheet spreadsheet;
    private String pageName;


    public void init() {

        if(logger.isDebugEnabled()) logger.debug("loading spreadsheetRemove().init()");
        if (spreadsheetId != null) {
            logger.debug("spreadsheet id is "+ spreadsheetId);
            spreadsheet = getGradebookManager().getSpreadsheet(spreadsheetId);
            if (spreadsheet == null) {
                // The assignment might have been removed since this link was set up.
                if(logger.isDebugEnabled()) logger.debug("No spreadsheetId=" + spreadsheetId + " in gradebookUid " + getGradebookUid());
                if(logger.isDebugEnabled()) logger.debug("spreadsheet is null");
            }            
        }
    }




    public Long getSpreadsheetId() {
        return spreadsheetId;
    }

    public void setSpreadsheetId(Long spreadsheetId) {
        this.spreadsheetId = spreadsheetId;
    }

    public boolean isRemoveConfirmed() {
        return removeConfirmed;
    }

    public void setRemoveConfirmed(boolean removeConfirmed) {
        this.removeConfirmed = removeConfirmed;
    }

    public Spreadsheet getSpreadsheet() {
        return spreadsheet;
    }

    public void setSpreadsheet(Spreadsheet spreadsheet) {
        this.spreadsheet = spreadsheet;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }


    public String removeSpreadsheet(){

        if(removeConfirmed) {
            try {
                getGradebookManager().removeSpreadsheet(spreadsheetId);
            } catch (StaleObjectModificationException e) {
                FacesUtil.addErrorMessage(getLocalizedString("remove_spreadsheet_locking_failure"));
                return null;
            }
            FacesUtil.addRedirectSafeMessage(getLocalizedString("remove_spreadsheet_success", new String[] {spreadsheet.getName()}));
            return "spreadsheetListing";
        } else {
            FacesUtil.addErrorMessage(getLocalizedString("remove_spreadsheet_confirmation_required"));
            return null;
        }

    }

}
