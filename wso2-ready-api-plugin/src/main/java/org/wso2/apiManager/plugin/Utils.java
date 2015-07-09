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

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.impl.rest.RestService;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.support.StringUtils;
import com.eviware.x.form.ValidationMessage;
import com.eviware.x.form.XFormDialog;
import com.eviware.x.form.XFormField;
import com.eviware.x.form.XFormFieldListener;
import com.eviware.x.form.XFormFieldValidator;
import com.eviware.x.form.support.ADialogBuilder;
import com.eviware.x.form.support.XFormRadioGroup;
import com.eviware.x.impl.swing.JTableFormField;
import com.smartbear.swagger.SwaggerImporter;
import com.smartbear.swagger.SwaggerUtils;
import org.jdesktop.swingx.JXTable;
import org.wso2.apiManager.plugin.constants.APIConstants;
import org.wso2.apiManager.plugin.constants.HelpMessageConstants;
import org.wso2.apiManager.plugin.dataObjects.APIInfo;
import org.wso2.apiManager.plugin.dataObjects.APISelectionResult;
import org.wso2.apiManager.plugin.ui.APIModel;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    /**
     * This method checks whether the given URL is a correct one. If the given URL is correct, then it will
     * return a URL
     *
     * @param urlString the url String that need to be validated.
     * @return URL that is created from the given urlString
     */
    public static URL validateURL(String urlString) {
        // check for null or empty
        if (StringUtils.isNullOrEmpty(urlString)) {
            return null;
        }

        // check for a valid protocol
        if (!urlString.toLowerCase().startsWith("http://") && !urlString.toLowerCase().startsWith("https://")) {
            return null;
        }

        try {
            return new URL(urlString);
        } catch (MalformedURLException e) {
            SoapUI.logError(e);
            return null;
        }
    }

    /**
     * This method is used to create the API section UI from the given list of APIs.
     *
     * @param apis The list of APIs that the table is constructed.
     * @return APISelectionResult which contains all the selected APIs and whether test suites and load
     * are needed to be generated.
     */
    public static APISelectionResult showSelectAPIDefDialog(List<APIInfo> apis) {
        final XFormDialog dialog = ADialogBuilder.buildDialog(APIModel.class);
        final Object[][] tableData = convertToTableData(apis);

        // --------------- start of API List table population section ------------------
        // We create a table model here with the converted data.
        TableModel tableModel = new AbstractTableModel() {
            Object[][] data = tableData;
            String[] columnNames = {"Name", "Version", "Provider", "Description"};

            @Override
            public int getRowCount() {
                return data.length;
            }

            @Override
            public int getColumnCount() {
                // We have a hardcoded set of columns
                return columnNames.length;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                return data[rowIndex][columnIndex];
            }

            @Override
            public String getColumnName(int column) {
                return columnNames[column];
            }


        };

        // We get the table that was generated in the form and we set some properties there.
        XFormField apiListFormField = dialog.getFormField(APIModel.API_LIST);
        final JXTable table = ((JTableFormField) apiListFormField).getTable();
        table.setCellSelectionEnabled(false);
        table.setColumnSelectionAllowed(false);
        table.setRowSelectionAllowed(true);
        table.setFillsViewportHeight(true);

        // We set the preferred size of all the parent forms until we get to the JScrollPane
        table.setPreferredScrollableViewportSize(new Dimension(600, 200));
        table.getParent().setPreferredSize(new Dimension(600, 200));
        table.getParent().getParent().setPreferredSize(new Dimension(600, 200));

        // Setting the table model
        table.setModel(tableModel);

        // This is to show a toolTip when hovering over the table cells. We need this because there could be long
        // descriptions and api names.
        table.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point p = e.getPoint();
                int row = table.rowAtPoint(p);
                int col = table.columnAtPoint(p);

                if (row == -1 || col == -1) {
                    return;
                }
                try {
                    table.setToolTipText(tableData[row][col].toString());
                } catch (Exception e1) {
                    // If there are any exceptions, we ignore them since they are not important
                }
            }
        });

        // The purpose of this validator is the check whether there are at least one API selected from the table.
        apiListFormField.addFormFieldValidator(new XFormFieldValidator() {
            @Override
            public ValidationMessage[] validateField(XFormField xFormField) {
                if (table.getSelectedRowCount() <= 0) {
                    return new ValidationMessage[]{new ValidationMessage(HelpMessageConstants
                                                                                 .API_SELECTION_VALIDATION_MSG,
                                                                         dialog.getFormField(APIModel.API_LIST))};
                }
                return new ValidationMessage[0];
            }
        });
        // ------------------- End of API List Table population section ----------------------

        // We get the radio button group and add a listener there. The purpose of the listener is to 'enable',
        // 'disable' the Load test radio button group based on the selected value of this group.
        // The reason is that, there is no meaning to create a Load test without a test suite.
        XFormRadioGroup testSuiteSelection = (XFormRadioGroup) dialog.getFormField(APIModel.TEST_SUITE);
        testSuiteSelection.setValue(APIConstants.RADIO_BUTTON_OPTIONS_NO);
        testSuiteSelection.setToolTip(HelpMessageConstants.TEST_SUITE_TOOLTIP_TEXT);

        testSuiteSelection.addFormFieldListener(new XFormFieldListener() {
            @Override
            public void valueChanged(XFormField xFormField, String newValue, String oldValue) {
                XFormRadioGroup loadTestSelection = (XFormRadioGroup) dialog.getFormField(APIModel.LOAD_TEST);
                if (APIConstants.RADIO_BUTTON_OPTIONS_YES.equals(newValue)) {
                    loadTestSelection.setEnabled(true);
                } else if (APIConstants.RADIO_BUTTON_OPTIONS_NO.equals(newValue)) {
                    loadTestSelection.setEnabled(false);
                }
            }
        });

        XFormRadioGroup loadTestSelection = (XFormRadioGroup) dialog.getFormField(APIModel.LOAD_TEST);
        loadTestSelection.setToolTip(HelpMessageConstants.LOAD_TEST_TOOLTIP_TEXT);
        loadTestSelection.setValue(APIConstants.RADIO_BUTTON_OPTIONS_NO);

        if (dialog.show()) {
            int[] selected = table.getSelectedRows();
            ArrayList<APIInfo> selectedAPIs = new ArrayList<APIInfo>();
            for (int no : selected) {
                selectedAPIs.add(apis.get(no));
            }
            APISelectionResult selectionResult = new APISelectionResult();
            selectionResult.setApiInfoList(selectedAPIs);
            selectionResult.setTestSuiteSelected(APIConstants.RADIO_BUTTON_OPTIONS_YES.equals(testSuiteSelection
                                                                                                      .getValue()));
            selectionResult.setLoadTestSelected(APIConstants.RADIO_BUTTON_OPTIONS_YES.equals(loadTestSelection
                                                                                                     .getValue()));

            return selectionResult;
        } else {
            return null;
        }

    }

    /**
     * This method will create a set of rest services by reading the swagger definitions resource from the given URL
     * in APIInfo.
     *
     * @param apiLink The APIInfo object which contains the API details.
     * @param project The WsdlProject
     * @return an array of RestService
     */
    public static RestService[] importAPItoProject(APIInfo apiLink, WsdlProject project) {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(Utils.class.getClassLoader());

        try {
            SwaggerImporter importer = SwaggerUtils.createSwaggerImporter(apiLink.getSwaggerDocLink(), project);
            SoapUI.log("Importing Swagger from [" + apiLink.getName() + "]");

            if (importer.getClass().getName().contains("Swagger2Importer")) {
                project.setPropertyValue(apiLink.getSwaggerDocLink(), "2.0");
            } else if (importer.getClass().getName().contains("Swagger1XImporter")) {
                project.setPropertyValue(apiLink.getSwaggerDocLink(), "1.x");
            } else {
               SoapUI.log("Unable to determine the Swagger version of [" + apiLink.getSwaggerDocLink() + "]" );
            }

            return importer.importSwagger(apiLink.getSwaggerDocLink());
        } finally {
            Thread.currentThread().setContextClassLoader(contextClassLoader);
        }
    }

    private static Object[][] convertToTableData(List<APIInfo> apiList) {
        Object[][] convertedData = new Object[apiList.size()][4];

        for (int i = 0; i < apiList.size(); i++) {
            APIInfo apiInfo = apiList.get(i);

            convertedData[i][0] = apiInfo.getName();
            convertedData[i][1] = apiInfo.getVersion();
            convertedData[i][2] = apiInfo.getProvider();
            convertedData[i][3] = apiInfo.getDescription();
        }
        return convertedData;
    }
}
