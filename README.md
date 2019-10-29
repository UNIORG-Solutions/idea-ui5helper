# UI5 Helper

This plugin adds some codeInsight and framework knowledge for Open- and SAPUI5 to IDEA IDEs.
It does not do much in its current state, it is not "production ready" and it will not save your time for
now.


### Some features that might be useful

#### Completion in XMLView
It can show you possible values for properties,
[![](./doc/screenshots/xml_attribute_value_completion.png)](./doc/screenshots/xml_attribute_value_completion.png)

or existing event handler on the controller,<br />
[![](./doc/screenshots/xml_event_handler_completion.png)](./doc/screenshots/xml_event_handler_completion.png) 

the properties, aggregations and events available on a control,
[![](./doc/screenshots/xml_attribute_completion.png)](./doc/screenshots/xml_attribute_completion.png)

or even the controls and aggreagtions valid in the current context.
[![](./doc/screenshots/xml_tag_completion.png)](./doc/screenshots/xml_tag_completion.png)


### Things it may be able to do now (experimental):

 - XMLView: Go To Controller
 - Controller: Go To (XML)View
 - collapse the controller name in XMLViews
 - complete target names in manifest.json
 - Provide API Docs in XMLView
 - References to event handler implementation in XMLViews


### Contribution welcome

If you have feature request, hit a bug, or the plugin just does not work for you: Please contact us. Either via [GitHub](https://github.com/UNIORG-Solutions/idea-ui5helper/issues/new) or by mail.

I am not maintaining the plugin as actively as necessary since I do not work with UI5 anymore.  

To build this project it should be sufficient to open it using Intellij IDEA (Community version is fine) with the 
Plugin SDK Plugin & the Gradle Build Plugin enabled. The tests are almost certainly broken all the time (PR welcome!) so 
just run the "buildPlugin" target & install the result (`build/distributions/ui5helper.zip` in a PHP/WebStorm of your choice).