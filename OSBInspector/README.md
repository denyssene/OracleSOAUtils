# OSB Inspector

Java utility for inspecting all Proxy Services in a Oracle Service Bus domain and its Proxies and Business references.
It outputs a XML file that is also good to open with Spreadsheet programs like Excel.

## Compilation

For this utility to work it depends on some Weblogic and Oracle Service Bus JAR libraries:

* **alsb.jar** from /<middleware_home>/Oracle_OSB1/lib
* **com.bea.common.configfwk_1.7.0.0.jar** from /<middleware_home>/Oracle_OSB1/modules
* **com.bea.core.management.jmx_1.4.2.0.jar** from /<middleware_home>/modules
* **sb-kernel-api.jar** from /<middleware_home>/Oracle_OSB1/lib
* **sb-kernel-impl.jar** from /<middleware_home>/Oracle_OSB1/lib
* **wlfullclient.jar**


## Basic Usage

usage: java -jar OSBInspector.jar
 * -h,--host <arg>     Server Host
 * -help               Help
 * -o,--output <arg>   Output File
 * -p,--port <arg>     Server Port
 * -P,--passwd <arg>   Password
 * -u,--user <arg>     Username


## License Information

This is an _**open source**_ project! 
Please review the LICENSE.md file for license information. 
If you have any questions or concerns on licensing, please contact denys.sene@gmail.com.
