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
import com.eviware.soapui.plugins.PluginAdapter;
import com.eviware.soapui.plugins.PluginConfiguration;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;

@PluginConfiguration(groupId = "org.wso2.plugins", name = "WSO2 API Manager Plugin", version = "1.0.3",
        autoDetect = true, description = "Plugin that supports integration with WSO2 API Manager",
        infoUrl = "")
public class PluginConfig extends PluginAdapter {
    private static boolean disabled = false;

    @Override
    public void initialize() {
//        disableSslSecurity();
    }

//    public static void disableSslSecurity() {
//        // Create a trust manager that does not validate certificate chains
//        final TrustManager[] trustAllCerts = new TrustManager[]{
//                new X509TrustManager() {
//                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//                        return new X509Certificate[0];
//                    }
//
//                    public void checkClientTrusted(
//                            java.security.cert.X509Certificate[] certs, String authType) {
//                    }
//
//                    public void checkServerTrusted(
//                            java.security.cert.X509Certificate[] certs, String authType) {
//                    }
//                }
//        };
//
//        // Create a hostname verifier that accept all hostnames
//        final HostnameVerifier allHostsValid = new HostnameVerifier() {
//            public boolean verify(String string, SSLSession ssls) {
//                return true;
//            }
//        };
//
//        // Install the all-trusting trust manager
//        try {
//            SSLContext sc = SSLContext.getInstance("TLS");
//            sc.init(null, trustAllCerts, new java.security.SecureRandom());
//            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
//            SoapUI.log.info("SSL Workaround Plugin initialized");
//            SoapUI.log.info("Thread name : " + Thread.currentThread().getName() + ". Thread id : " + Thread
//                    .currentThread().getId());
//        } catch (GeneralSecurityException e) {
//            SoapUI.logError(e, "SSL Workaround Plugin initialization error");
//        }
//    }
}
