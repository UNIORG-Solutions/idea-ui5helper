# ui5helper

This plugin adds some codeInsight and framework knowledge for Open- and SAPUI5 to IDEA IDEs.
It does not do much in its current state, it is not "production ready" and it will not save your time for
now.


### Things it may be able to do now (experimental):

 - XMLView: Go To Controller
 - Controller: Go To (XML)View
 - collapse the controller name in XMLViews
 - complete target names in manifest.json


### Things that will (or will not) come:

 - Settings to enable or disable certain features
 - Understanding & support for the UI5 binding syntax incl. completion, references, syntax checking, ...
 - Indexer for UI5 classes including metadata and inheritance tree
 - <s>Go to Controller, go to View,</s> go to formatter, go to event handler, ...
 - support for manifest metadata like routes etc.


#### Changelog

 __0.2.3__
 - Bugfix: controllerName lookup for "sap.ui.core.mvc.XMLView" should work just like "sap.ui.core.mvc.View"</li>

 __0.2.2__
 - Feature: Controller: go to View