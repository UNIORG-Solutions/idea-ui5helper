# ui5helper

This plugin adds some codeInsight and framework knowledge for Open- and SAPUI5 to IDEA IDEs.
It does not do much in its current state, it is not "production ready" and it will not save your time for
now.


### Things it may be able to do now (experimental):

 - XMLView: Go To Controller
 - Controller: Go To (XML)View
 - collapse the controller name in XMLViews
 - complete target names in manifest.json
 - Provide API Docs in XMLView
 - References to event handler implementation in XMLViews

### Things that will (or will not) come:

 - Settings to enable or disable certain features
 - Understanding & support for the UI5 binding syntax incl. completion, references, syntax checking, ...
 - Indexer for UI5 classes including metadata and inheritance tree
 - Go to formatter, ...
 - support for manifest metadata like routes etc.


#### Changelog

 __0.2.5__
 - References to event handlers
 - Caching for API docs
 - Version selection in settings actually works.

 __0.2.4__
 - First implementation of API docs in XMLViews. More to come!

 __0.2.3__
 - Bugfix: controllerName lookup for "sap.ui.core.mvc.XMLView" should work just like "sap.ui.core.mvc.View"</li>

 __0.2.2__
 - Feature: Controller: go to View