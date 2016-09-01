# WSO2 API Manager Plugin

A plugin for Ready! API that allows you to import APIs directly from a WSO2 API Manager based store.

Installation
------------

Install the plugin via the integrated Plugin Repository available via the Plugin Manager in SoapUI Pro 5.X or Ready! API 1.X


Build it yourself
-----------------

You can build the plugin locally by cloning this repository locally - make sure you have java and maven 3.X correctly
installed - and run

```mvn clean install assembly:single```

in the project folder. The plugin {PLUGIN_NAME}-dist.jar will be created in the target folder and can be installed via the
Plugin Managers' "Load from File" action.

Usage
-----

Once installed there will have two ways to import an API from a WSO2 API Store:

* Via the "Add API From WSO2 API Store" option on the Project menu in the "Projects" tab
* Via the "Import from WSO2 API Manager" option in the "Create project from..." drop-down when creating a new project

In either case you will be prompted for the URL of the WSO2 API Store that exposes API metadata, the user name, tenant domain and the password to login to the API Store.

Note:
  * The user name should be the tenant aware user name(it should NOT contain the tenant domain).
  * To access the super tenant store, there is no need to fill in the tenant domain. If left blank/empty the plugin will try to connect to the super tenant store using the given credentials. 

Once a valid API Store URL has been specified (for example https://localhost:9443/store/) you will be presented with a list of available APIs. You have the option of selecting multiple APIs from the above list and the plugin will download the APIs underlying Swagger description and configure a corresponding REST API in Ready! API.



