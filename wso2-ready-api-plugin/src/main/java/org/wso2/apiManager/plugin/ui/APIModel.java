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

package org.wso2.apiManager.plugin.ui;

import com.eviware.x.form.support.AField;
import com.eviware.x.form.support.AForm;

/**
 * This class is used to generate the API selection UI.
 */
@AForm(name = "Select API to Import",
        description = "Please select from the list which API specification(s) you want to import to the project.")
public interface APIModel {
    @AField(description = "", type = AField.AFieldType.TABLE)
    public static final String API_LIST = "Api List";

    @AField(description = "Generate test suite", type = AField.AFieldType.RADIOGROUP, values = {"Yes","No"},
            defaultValue = "No")
    public static final String TEST_SUITE = "Generate Test Suite";

    @AField(description = "Generate load test", type = AField.AFieldType.RADIOGROUP, values = {"Yes","No"},
            defaultValue = "No", enabled = false)
    public static final String LOAD_TEST = "Generate Load test";
}
