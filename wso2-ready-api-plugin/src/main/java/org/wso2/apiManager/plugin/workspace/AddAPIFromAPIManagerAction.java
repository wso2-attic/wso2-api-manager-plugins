/*
*  Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/


package org.wso2.apiManager.plugin.workspace;

import com.eviware.soapui.impl.rest.RestService;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.plugins.ActionConfiguration;
import com.eviware.soapui.support.StringUtils;
import com.eviware.soapui.support.UISupport;
import com.eviware.soapui.support.action.support.AbstractSoapUIAction;
import com.eviware.x.form.ValidationMessage;
import com.eviware.x.form.XFormDialog;
import com.eviware.x.form.XFormField;
import com.eviware.x.form.XFormFieldValidator;
import com.eviware.x.form.support.ADialogBuilder;
import org.wso2.apiManager.plugin.ActionGroups;
import org.wso2.apiManager.plugin.Utils;
import org.wso2.apiManager.plugin.dataObjects.APIExtractionResult;
import org.wso2.apiManager.plugin.dataObjects.APIInfo;
import org.wso2.apiManager.plugin.dataObjects.APISelectionResult;
import org.wso2.apiManager.plugin.ui.ImportModel;
import org.wso2.apiManager.plugin.worker.APIExtractorWorker;
import org.wso2.apiManager.plugin.worker.APIImporterWorker;

import java.net.URL;
import java.util.List;

import static org.wso2.apiManager.plugin.constants.HelpMessageConstants.API_STORE_URL_VALIDATION_MSG;
import static org.wso2.apiManager.plugin.constants.HelpMessageConstants.INVALID_API_STORE_URL;
import static org.wso2.apiManager.plugin.constants.HelpMessageConstants.PASSWORD_VALIDATION_MSG;
import static org.wso2.apiManager.plugin.constants.HelpMessageConstants.USER_NAME_VALIDATION_MSG;

@ActionConfiguration(actionGroup = ActionGroups.OPEN_PROJECT_ACTIONS, separatorBefore = true)
public class AddAPIFromAPIManagerAction extends AbstractSoapUIAction<WsdlProject> {
    private XFormDialog dialog;

    public AddAPIFromAPIManagerAction() {
        super("Add API From WSO2 API Store", "Adds API from the specification on WSO2 API Store.");
    }

    @Override
    public void perform(WsdlProject wsdlProject, Object o) {
        APIExtractionResult listExtractionResult = null;
        if (dialog == null) {
            dialog = ADialogBuilder.buildDialog(ImportModel.class);
            dialog.getFormField(ImportModel.API_STORE_URL).addFormFieldValidator(new XFormFieldValidator() {
                @Override
                public ValidationMessage[] validateField(XFormField formField) {
                    if (StringUtils.isNullOrEmpty(dialog.getValue(ImportModel.API_STORE_URL))) {
                        return new ValidationMessage[]{new ValidationMessage(API_STORE_URL_VALIDATION_MSG, dialog
                                .getFormField(ImportModel.API_STORE_URL))};
                    }
                    if (StringUtils.isNullOrEmpty(dialog.getValue(ImportModel.USER_NAME))) {
                        return new ValidationMessage[]{new ValidationMessage(USER_NAME_VALIDATION_MSG, dialog
                                .getFormField(ImportModel.USER_NAME))};
                    }
                    if (StringUtils.isNullOrEmpty(dialog.getValue(ImportModel.PASSWORD))) {
                        return new ValidationMessage[]{new ValidationMessage(PASSWORD_VALIDATION_MSG, dialog
                                .getFormField(ImportModel.PASSWORD))};
                    }
                    URL storeUrl = Utils.validateURL(formField.getValue());
                    if (storeUrl == null) {
                        return new ValidationMessage[]{new ValidationMessage(INVALID_API_STORE_URL, formField)};
                    }
                    return new ValidationMessage[0];
                }
            });
        }

        while (dialog.show()) {
            String urlString = dialog.getValue(ImportModel.API_STORE_URL);
            String userName = dialog.getValue(ImportModel.USER_NAME);
            char[] password = dialog.getValue(ImportModel.PASSWORD).toCharArray();
            String tenantDomain = dialog.getValue(ImportModel.TENANT_DOMAIN);
            if (urlString == null) {
                return;
            }
            URL url = Utils.validateURL(urlString);
            if (url == null) {
                UISupport.showErrorMessage(INVALID_API_STORE_URL);
                continue;
            }
            listExtractionResult = APIExtractorWorker.downloadAPIList(url.toString(), userName, password, tenantDomain);
            if (listExtractionResult.isCanceled()) {
                return;
            }

            if (listExtractionResult.getApiList() != null) {
                break;
            }
            UISupport.showErrorMessage(listExtractionResult.getError());
        }
        if (listExtractionResult == null) {
            return;
        }

        APISelectionResult selectionResult = Utils.showSelectAPIDefDialog(listExtractionResult.getApiList());
        if(selectionResult == null){
            return;
        }

        List<APIInfo> selectedAPIs = selectionResult.getApiInfoList();
        if (selectedAPIs != null) {
            List<RestService> services = APIImporterWorker.importServices(selectionResult, wsdlProject);
            if (services != null && !services.isEmpty()) {
                UISupport.select(services.get(0));
            }
        }
    }
}
