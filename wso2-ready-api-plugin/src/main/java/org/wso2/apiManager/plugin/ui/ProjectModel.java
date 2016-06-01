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
 * This class is used to generate the new project window for WSO2 API Manager projects.
 */
@AForm(name = "Create Project From API Specification on WSO2 API Store",
        description = "Creates a new Project from API specification on WSO2 API Store in this workspace")
public interface ProjectModel {
    @AField(name = "Project Name", description = "Name of the project", type = AField.AFieldType.STRING)
    public final static String PROJECT_NAME = "Project Name";

    @AField(name = "API Store URL",
            description = "API Store URL (i.e. https://localhost:9443/store/)",
            type = AField.AFieldType.STRING)
    public final static String API_STORE_URL = "API Store URL";

    @AField(name = "API Store User Name",
            description = "A user name to connect to the store",
            type = AField.AFieldType.STRING)
    public final static String USER_NAME = "API Store User Name";

    @AField(name = "API Store password",
            description = "The password of the above given user",
            type = AField.AFieldType.PASSWORD)
    public final static String PASSWORD = "API Store password";

    @AField(name = "Tenant Domain",
            description = "The tenant domain of the store",
            type = AField.AFieldType.STRING)
    public final static String TENANT_DOMAIN = "Tenant Domain";

    @AField(name = "API Manager Version",
            description = "The version of the API Manager that is been used",
            type = AField.AFieldType.COMBOBOX, values = {"2.0.0", "1.10.0", "1.9.x", "1.8.0"})
    public final static String PRODUCT_VERSION = "API Manager Version";

}
