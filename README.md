# Granting Data Application

## Project Description

The Granting Data Application is being developed to be used as an internal tool by the help desk staff and the business analysts. The application is designed to have different staff designated as different roles.
The people designated with the admin roles will be able to: 
-	Upload a golden list of system funding opportunities
-	Upload a dataset containing the funding cycles from a granting system
-	Link funding opportunities to system funding opportunities.

A funding opportunity is a grant that researchers apply to and are awarded. Awards from funding opportunities are granted through one of several granting systems (AMIS, NAMIS, CRM, etc.). A funding cycle represents the period of time in the year that a funding opportunity is open for applications, assessments and awards. The golden list is a document with a list of system funding opportunities which are the official funding opportunities recognized by the agencies. Admins can upload the golden list to the application. Each granting system has various funding opportunities in which applications, assessments and awarding is processed. Sometimes there is a funding opportunity that is in more than one of the granting systems and sometimes with different name, but they refer to the same, singular funding opportunity recognized by the business. An admin for the Granting Data Application can link funding opportunities from the granting systems to the system funding opportunities from the uploaded golden list after uploading data containing the funding opportunities and funding cycles from a granting system.

All the data inputted into the application is displayed in a table that can be browsed anonymously. The table contains the entire list of system funding opportunities. Clicking on a system funding opportunity in the table will reveal additional info. An admin or the person designated as the program lead for a specific funding opportunity can edit the information for the chosen funding opportunity. An admin can also query the Active Directory and change the program lead of the chosen funding opportunity.
The Granting Data Application is created using Spring Boot and Spring MVC technologies. The source for the logic of the application and security configuration, the models, controller, repositories, and service layers are programmed as Java objects. All the templates for the web pages are created using HTML, and Bootstrap is used for the styling of the web pages. Liquibase, a Java library, is used for managing database changes. By maintaining changelogs each time it is ran, Liquibase works as a source/version control of the database.  A template engine called Thymeleaf is used for programming dynamic changes to the otherwise static web pages. 
