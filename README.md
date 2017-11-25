# introsde-2017-assignment-2-server
Server repository for the second assignment of introsde2017 
Marco Michelotti, marco.michelotti-1@studenti.unitn.it
http://sde2.herokuapp.com

## Structure
The project is divided into 5 packages
- `introsde.rest.ehealth`: to start the application locally
- `introsde.rest.ehealth.dao`: to use a user dao
- `introsde.rest.ehealth.model`: to define the database entities
- `introsde.rest.ehealth.resources`: contains the classes that handle the REST response
The are two additional files:
- `build.xml` to create ant functions
- `ivy.xml` to import the dependencies

## Tasks
The server is meant to execute the requested tasks

## Deployment steps
- Clone the repository
- Go to inside the directory
- Start ant command to build the war file
```
ant create.war
```
- deploy the war file to Heroku
```
heroku war:deploy <WAR_PATH> --app <APP_NAME>
```
