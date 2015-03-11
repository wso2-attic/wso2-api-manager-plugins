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

package org.wso2.apiManager.plugin;

public class ActionGroups {

    /*
     * Actions applied to the workspace. They will also be inserted into the File menu of SoapUI.
     * Have your action class extend AbstractSoapUIAction<WorkspaceImpl>.
     */
    public static final String WORKSPACE_ACTIONS = "WorkspaceImplActions";

    /*
     * Actions applied to all projects, regardless of the state (enabled, disabled; closed or opened; encrypted or not.
     * Have your action class extend AbstractSoapUIAction<WsdlProject>.
     */
    public static final String PROJECT_ACTIONS = "WsdlProjectActions";

    /*
     * Actions applied to opened projects. If you're writing a project level action, this is usually the action group
     * you want it in.
     *
     * Have your action class extend AbstractSoapUIAction<WsdlProject>.
     */
    public static final String OPEN_PROJECT_ACTIONS = "EnabledWsdlProjectActions";

    /*
     * Actions applied to opened composite projects. If you're writing a project level action that applies to composite
     * projects, you usually want to include this action group, e.g. by using the @ActionConfigurations annotation.
     *
     * Have your action class extend AbstractSoapUIAction<WsdlProject>.
     */
    public static final String OPEN_COMPOSITE_PROJECT_ACTIONS = "CompositeWsdlProjectActions";

    /*
    * Actions applied to closed projects.
    *
    * Have your action class extend AbstractSoapUIAction<WsdlProject>.
    */
    public static final String CLOSED_PROJECT_ACTIONS = "ClosedWsdlProjectActions";

    /*
     * Actions applied to encrypted projects.
     * Have your action class extend AbstractSoapUIAction<WsdlProject>.
     */
    public static final String ENCRYPTED_PROJECT_ACTIONS = "EncryptedWsdlProjectActions";

    /*
     * Actions applied to both SOAP and REST services.
     * Have your action class extend AbstractSoapUIAction<WsdlInterface>.
     */
    public static final String INTERFACE_ACTIONS = "WsdlInterfaceActions";

    /*
     * Actions applied only to REST services.
     * Have your action class extend AbstractSoapUIAction<RestService>.
     */
    public static final String REST_SERVICE_ACTIONS = "RestServiceActions";

    /*
     * Actions applied to SOAP services.
     * Have your action class extend AbstractSoapUIAction<RestResource>.
     */
    public static final String REST_RESOURCE_ACTIONS = "RestResourceActions";

    /*
     * Actions applied only to REST method nodes, i.e. the level below the REST resource, most often named GET or POST.
     * Have your action class extend AbstractSoapUIAction<RestMethod>.
     */
    public static final String REST_METHOD_ACTIONS = "RestMethodActions";

    /*
     * Actions applied to REST requests.
     * Have your action class extend AbstractSoapUIAction<RestRequest>.
     */
    public static final String REST_REQUEST_ACTIONS = "RestMethodActions";

    /*
     * Actions applied to SOAP operations.
     * Have your action class extend AbstractSoapUIAction<WsdlOperation>.
     */
    public static final String SOAP_OPERATION_ACTIONS = "WsdlOperationActions";

    /*
     * Actions applied to SOAP operations.
     * Have your action class extend AbstractSoapUIAction<WsdlOperation>.
     */
    public static final String SOAP_REQUEST_ACTIONS = "WsdlRequestActions";

    /*
     * Actions applied to test suites.
     * Have your action class extend AbstractSoapUIAction<WsdlTestSuite>.
     */
    public static final String TEST_SUITE_ACTIONS = "WsdlTestSuiteActions";

    /*
     * Actions applied to test cases.
     * Have your action class extend AbstractSoapUIAction<WsdlTestCase>.
     */
    public static final String TEST_CASE_ACTIONS = "WsdlTestSuiteActions";

    /*
     * Actions applied to all test steps.
     * Have your action class extend AbstractSoapUIAction<WsdlTestStep>.
     */
    public static final String TEST_STEP_ACTIONS = "WsdlTestStepActions";

    /*
     * Actions applied only to REST requests test steps.
     * Have your action class extend AbstractSoapUIAction<RestTestRequestStep>.
     */
    public static final String REST_TEST_REQUEST_ACTIONS = "RestTestRequestStepActions";

    /*
     * Actions applied to SOAP operations.
     * Have your action class extend AbstractSoapUIAction<WsdlOperation>.
     */
    public static final String SOAP_TEST_REQUEST_ACTIONS = "WsdlTestRequestStepActions";

    /*
     * Actions applied to assertions.
     * Have your action class extend AbstractSoapUIAction<WsdlMessageAssertion>.
     */
    public static final String ASSERTION_ACTIONS = "WsdlMessageAssertionActions";


}