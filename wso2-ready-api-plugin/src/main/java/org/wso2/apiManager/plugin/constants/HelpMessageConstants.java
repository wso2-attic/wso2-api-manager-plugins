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

package org.wso2.apiManager.plugin.constants;

/**
 * This class contains all the help/validation messages that are used by the UIs.
 */
public class HelpMessageConstants {
    // Help messages for the ProjectModel and Import Model.
    public static final String API_STORE_URL_VALIDATION_MSG = "Please enter the API Store URL.";
    public static final String USER_NAME_VALIDATION_MSG = "Please enter user name.";
    public static final String PASSWORD_VALIDATION_MSG = "Please enter an valid password.";
    public static final String INVALID_API_STORE_URL = "Invalid API Store URL.";
    public static final String PROJECT_NAME_VALIDATION_MSG = "Please enter project name.";

    // Validation message for the APIModel table.
    public static final String API_SELECTION_VALIDATION_MSG = "Please select at least one API to proceed";

    // Tooltip texts for the radio buttons.
    public static final String TEST_SUITE_TOOLTIP_TEXT = "Select 'Yes' if you need to generate a set of test suites for the selected APIs";
    public static final String LOAD_TEST_TOOLTIP_TEXT = "Select 'Yes' if you need to generate a set of load tests for the selected APIs";
}
