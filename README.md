<a href="http://predixdev.github.io/rmd-datasource/javadocs/index.html" target="_blank" >
	<img height="50px" width="100px" src="images/javadoc.png" alt="view javadoc"></a>
&nbsp;
<a href="http://predixdev.github.io/rmd-datasource" target="_blank">
	<img height="50px" width="100px" src="images/pages.jpg" alt="view github pages">
</a>

Welcome to the Experience Datasource Bootstrap.  This project has Rest APIs that are used by the rmd-predix-ui Reference App and are instructive in how to create a custom Data Microservice that accesses multiple backend datastores and returns data for a UI. 

The services are focused on 2 main areas:   

- PX-Datagrid - The px-datagrid widget contract is satisfied.  Data is retrieved from Predix Asset and Timeseries APIs, mashed up to meet the requirements of the RMD Reference App.
 
- Analytic Summary Data - The summary widgets of the RMD Reference app require data retrieved from Predix Asset and Timeseries APIs.  The rest service formats this data for consumption by the RMD Reference App

##Tech Stack
- Spring
- SpringBoot
- SpringTest
- Maven

##Microcomponents
- [AssetBootstrap](https://github.com/predixdev/asset-bootstrap)
- [TimeseriesBootstrap](https://github.com/predixdev/timeseries-bootstrap)
- [PredixMicroServiceTemplates](https://github.com/predixdev/predix-microservice-templates)
- [PredixRestClient](https://github.com/predixdev/predix-rest-client)

### More Details
* [More GE resources](http://github.com/predixdev/predix-rmd-ref-app/docs/resources.md)
* [RMD Reference App](http://github.com/predixdev/predix-rmd-ref-app)

[![Analytics](https://ga-beacon.appspot.com/UA-82773213-1/rmd-datasource/readme?pixel)](https://github.com/PredixDev)
