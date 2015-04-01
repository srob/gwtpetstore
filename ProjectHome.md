gwt-Petstore README file

Introduction

---

The gwt-Petstore is a GWT implementation of the classic Sun Java Petstore JEE
application.

Project home: http://code.google.com/p/gwtpetstore/
Project src:  http://gwtpetstore.googlecode.com/svn/
Project group: http://groups.google.com/group/javaongems?lnk=li


Background

---

The gwt-Petstore was built with four purposes in mind:
1. Serve as an exercise to train up a new junior GWorker (Welcome Dimax Pradi).
2. Provide an example that I could demonstrate to Enterprise Java Australia
> as part of a GWT presentation.
3. Provide a familiar JEE example that can be compared and contrasted by those
> attempting to make the journey from traditional JEE web technology.
4. Serve as a reference example to help identify and resolve the issues with
> GWT design tools. Specifically to collaborate with Konstantin to improve
> GWT tooling for greater development agility.


How to install & run it

---

1. check out the codebase from http://gwtpetstore.googlecode.com/svn/trunk
> eg. c:\temp>svn co http://gwtpetstore.googlecode.com/svn/trunk gwt-petstore
2. deploy the following war file in your web container
> eg. c:\temp>copy c:\temp\gwt-petstore\bin\gwt-petstore.war c:\temp\tomcat-5.5.9\webapps
3. open a command shell, cd to the hsqldb folder and launch the hsqldb server
> eg. c:\temp\gwt-petstore\hsqldb>server
4. launch your web container
> eg. c:\temp\tomcat-5.5.9\bin>catalina start
5. open a browser to the application root context or host page
> eg. http://localhost:8080/gwt-petstore/ or http://localhost:8080/gwt-petstore/index.html


Non-functional requirements

---

**Leverage iBATIS JPetStore, by Clinton Begin** Leverage Spring JPetStore, by Juergen Hoeller
**Use GWTHandler spring controller for server side gwt-rpc, by George Georgovassilis** Use the same business services, domain model, dao, dto and schema of original JPetStore
**Support url addressability for product items such that the petstore will auto-open
> to correct product when launched via link** Support full petstore navigational history to make the experience consistent with the
> original web version
**Keep the user-interface as simple as possible and model it on the new "groups beta" ui** Provide keyboard driven support
**Use the GMail card deck panel for displaying associated items** Ensure that most if not all UI features are accessible via keyboard only navigation



Functional requirements

---

**Refer original Sun Java Petstore functional requirements**



Known Issues

---

**[IEv6.0.29000.2180] onModuleLoad() does not occasionally fire after repeated page refreshes** The Gems::Outlook bar's (ie. StackPanel variant) scroll panel causes resizing to be
> problematic.
**The Gems::Outlook bar has poor keyboard support**



Functionality not delivered in this build

---

**Taking the shopping cart to the checkout and creating an order** No help pages


Wish list

---

**Work out how to inject the current http request into the RPC impls to support
> those impls that need to persist server controller state into the http session.** Use streaming to let the client be aware of product item price and inventory
> level changes.


Contributions

---

We welcome any form of contributions to:
1. fix any defects with the gwt-Petstore
2. complete the gwt-Petstore functionality not delivered in this build
3. develop any of the wish list functionality
4. enhance the gwt-Petstore to better promote the adoption and education of GWT
> practices


Message to GWT widget builders

---

In the spirit of the Zen Garden (see http://www.csszengarden.com/), if you have
widgets that you would like to showcase in the gwt-Petstore, then please join the
gwt-Petstore project and create an svn branch for your release. This enables all
gwt-Petstore derivates to be co-located and the application of 3rd party widgets
to be better understood under a common reference example.


best wishes
ash