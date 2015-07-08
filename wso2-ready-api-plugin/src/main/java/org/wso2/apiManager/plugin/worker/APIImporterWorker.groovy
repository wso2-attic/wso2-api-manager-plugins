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

package org.wso2.apiManager.plugin.worker;

import com.eviware.soapui.SoapUI
import com.eviware.soapui.config.CredentialsConfig;
import com.eviware.soapui.config.TestStepConfig;
import com.eviware.soapui.impl.AuthRepository.AuthEntries;
import com.eviware.soapui.impl.AuthRepository.AuthRepository;
import com.eviware.soapui.impl.AuthRepository.Impl.AuthRepositoryImpl;
import com.eviware.soapui.impl.rest.RestMethod;
import com.eviware.soapui.impl.rest.RestRequest;
import com.eviware.soapui.impl.rest.RestResource;
import com.eviware.soapui.impl.rest.RestService;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.WsdlTestSuite;
import com.eviware.soapui.impl.wsdl.testcase.WsdlTestCase;
import com.eviware.soapui.impl.wsdl.teststeps.registry.RestRequestStepFactory;
import com.eviware.soapui.support.StringUtils;
import com.eviware.soapui.support.UISupport;
import com.eviware.x.dialogs.Worker;
import com.eviware.x.dialogs.XProgressDialog;
import com.eviware.x.dialogs.XProgressMonitor;
import org.wso2.apiManager.plugin.Utils;
import org.wso2.apiManager.plugin.constants.APIConstants;
import org.wso2.apiManager.plugin.dataObjects.APIInfo;
import org.wso2.apiManager.plugin.dataObjects.APISelectionResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class APIImporterWorker implements Worker {
    String errors = "";

    private XProgressDialog waitDialog;
    private boolean cancelled = false;
    private List<APIInfo> links;
    private boolean isTestSuiteSelected = false;
    private boolean isLoadTestSelected = false;
    private WsdlProject project;

    private List<RestService> addedServices = new ArrayList<RestService>();

    private APIImporterWorker(XProgressDialog waitDialog, APISelectionResult apiSelectionResult, WsdlProject project) {
        this.waitDialog = waitDialog;
        this.links = apiSelectionResult.getApiInfoList();
        this.isLoadTestSelected = apiSelectionResult.isLoadTestSelected();
        this.isTestSuiteSelected = apiSelectionResult.isTestSuiteSelected();
        this.project = project;
    }

    public static List<RestService> importServices(APISelectionResult selectionResult, WsdlProject project) {
        APIImporterWorker worker = new APIImporterWorker(
                UISupport.getDialogs().createProgressDialog("Importing APIs...", 100, "", true),
                selectionResult, project);
        try {
            worker.waitDialog.run(worker);
        } catch (Exception e) {
            UISupport.showErrorMessage(e.getMessage());
            SoapUI.logError(e);
        }
        if (worker.addedServices != null && worker.addedServices.size() > 0) {
            return worker.addedServices;
        } else {
            return null;
        }
    }

    @Override
    public Object construct(XProgressMonitor monitor) {
        for (APIInfo apiInfo : links) {
            if (cancelled) {
                break;
            }
            RestService[] restServices;
            try {
                WsdlTestSuite testSuite = null;
                WsdlTestCase testCase = null;
                boolean isSwagger2 = false;

                // We are importing the API definition from the swagger resource here.
                // Once imported, we get an array of RestServices as the result.
                restServices = Utils.importAPItoProject(apiInfo, project);

                // We set the Auth Profile here.
                setAuthProfile();

                // We are checking whether this service is swagger 2.0 or 1.x.
                isSwagger2 = checkIsSwagger2(apiInfo, isSwagger2);

                if (restServices != null) {
                    for (RestService restService : restServices) {
                        // We change the restServices name to the apiName/apiVersion
                        restService.setName(constructServiceName(apiInfo, restService.getName(), isSwagger2));

                        if (isTestSuiteSelected) {
                            //Check to see if the default test suite is there
                            testSuite = project.getTestSuiteByName(restService.getName());
                            if (testSuite == null) {
                                testSuite = project.addNewTestSuite(restService.getName());
                            }
                        }
                        List<RestResource> resources = restService.getAllResources();
                        for (RestResource resource : resources) {

                            // Add a test case for the newly created one
                            if (isTestSuiteSelected) {
                                if (testSuite != null) {
                                    testCase = testSuite.addNewTestCase(resource.getName());
                                }
                            }
                            List<RestMethod> methods = resource.getRestMethodList();
                            for (RestMethod method : methods) {
                                List<RestRequest> restRequests = method.getRequestList();
                                for (RestRequest restRequest : restRequests) {
                                    // Selecting the Auth profile

                                    if (restRequest.metaClass.respondsTo(restRequest, "setSelectedAuthProfileAndAuthType")) {
                                        restRequest.setSelectedAuthProfileAndAuthType(APIConstants.WSO2_API_MANAGER_DEFAULT,
                                                                                      CredentialsConfig.AuthType.O_AUTH_2_0);
                                    } else {
                                        restRequest.selectedAuthProfile = APIConstants.WSO2_API_MANAGER_DEFAULT;
                                    }

                                    // This will rename the request name to something similar to
                                    // get_repos/{user_name}/{repo_name} - default_request
                                    restRequest.setName(constructRequestName(method.getName()));

                                    // Add a test step for each request
                                    if (isTestSuiteSelected && testCase != null) {
                                        TestStepConfig testStepConfig = RestRequestStepFactory.createConfig(restRequest, restRequest.getName());
                                        testCase.addTestStep(testStepConfig);
                                    }
                                }
                            }
                            if (isLoadTestSelected) {
                                if (testCase != null) {
                                    testCase.addNewLoadTest(testCase.getName());
                                }
                            }
                        }
                    }
                }
            } catch (Throwable e) {
                if (errors.length() > 0) {
                    errors += "\n";
                }

                errors = String.format("Failed to read API description for[%s] - [%s]", apiInfo.getName(), e
                        .getMessage());
                SoapUI.logError(e);
                continue;
            }
            if (restServices != null) {
                addedServices.addAll(Arrays.asList(restServices));
            }
        }

        if (errors.length() > 0) {
            errors += "\nPlease contact WSO2 support for assistance";
        }

        return null;
    }

    @Override
    public void finished() {
        if (cancelled) {
            return;
        }
        waitDialog.setVisible(false);
        if (StringUtils.hasContent(errors)) {
            UISupport.showErrorMessage(errors);
        }
    }

    private boolean checkIsSwagger2(APIInfo apiInfo, boolean isSwagger2) {
        if (project.getPropertyValue(apiInfo.getSwaggerDocLink()).equals("2.0")) {
            isSwagger2 = true;
        }
        project.removeProperty(apiInfo.getSwaggerDocLink());
        return isSwagger2;
    }

    @Override
    public boolean onCancel() {
        cancelled = true;
        waitDialog.setVisible(false);
        return true;
    }

    private void setAuthProfile() {
        if (project.metaClass.hasProperty(project, 'authRepository')) {
            def authRepository = project.authRepository
            if (authRepository instanceof AuthRepositoryImpl) {
                boolean hasDefaultProfile = false;
                AuthRepositoryImpl authRepositoryImpl = (AuthRepositoryImpl) authRepository;
                List<AuthEntries.OAuth20AuthEntry> oAuth2ProfileList = authRepositoryImpl.getOAuth2ProfileList();
                for (AuthEntries.OAuth20AuthEntry oAuth20AuthEntry : oAuth2ProfileList) {
                    if (APIConstants.WSO2_API_MANAGER_DEFAULT.equals(oAuth20AuthEntry.getName())) {
                        hasDefaultProfile = true;
                        break;
                    }
                }
                if (!hasDefaultProfile) {
                    authRepositoryImpl.addNewOAuth2Profile(APIConstants.WSO2_API_MANAGER_DEFAULT);
                }
            }
        } else if (project.metaClass.hasProperty(project, 'oAuth2ProfileContainer')) {
            // This is the older code that was in use
            def profileContainer = project.oAuth2ProfileContainer
            if (profileContainer != null && profileContainer.getProfileByName(APIConstants.WSO2_API_MANAGER_DEFAULT) == null) {
                profileContainer.addNewOAuth2Profile(APIConstants.WSO2_API_MANAGER_DEFAULT);
            }
        }
    }

    private static String constructServiceName(APIInfo apiInfo, String resourceName, Boolean isSwagger2) {
        String serviceName = apiInfo.getName() + "/" + apiInfo.getVersion();
        if (!isSwagger2) {
            serviceName = serviceName + resourceName;
        }
        return serviceName;
    }

    private static String constructRequestName(String methodName) {
        return methodName + " - " + "default_request";
    }
}
