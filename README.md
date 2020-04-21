# quarano - Epidemiemanagement für Gesundheitsbehörden

Epidemiemanagement für Gesundheitsbehörden durch Digitalisierung von Selbstauskünften bestätigter Quarantänefälle und Verdachtspersonen (inkl. Informationen zu Symptomen und Kontakten)

Hosted at <https://quarano.de> (prod) and <https://develop.quarano.de> (dev)


[![YouTube video](https://img.youtube.com/vi/z__mJRP8O0w/0.jpg)](https://www.youtube.com/watch?v=z__mJRP8O0wD)

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 9.0.7.

## Status

This is a project in development. It has been created during the [WirVsVirusHackathon](https://wirvsvirushackathon.org/).

## Licence
Licensed under the EUPL-1.2-or-later

## Backend

### Development Setup

Initial DB setup

- run docker-compose up
- connect to the database container `docker exec -it <CONTAINER_ID> bash`
- `su - postgres`
- `createuser --interactive --pwprompt`
  - rolename : `corona-report-app`
    _ password: `corona`
    _ superuser: yes
- `createdb -O corona-report-app corona-report`

### Dummy Data

Two health departments are created as dummy data upon startup with the following id and passcode:
Testamt1 aba0ec65-6c1d-4b7b-91b4-c31ef16ad0a2
Testamt2 ca3f3e9a-414a-4117-a623-59b109b269f1

### Run Locally

navigate to backend folder.
Run `./mvnw spring-boot:run`. `Runs at http://localhost:8080/`

## Frontend

### Development Server

Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The app will automatically reload if you change any of the source files.

### Code scaffolding

Run `ng generate component component-name` to generate a new component. You can also use `ng generate directive|pipe|service|class|guard|interface|enum|module`.

### Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory. Use the `--prod` flag for a production build.

### Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

### Running end-to-end tests

Run `ng e2e` to execute the end-to-end tests via [Protractor](http://www.protractortest.org/).

### Further help

To get more help on the Angular CLI use `ng help` or go check out the [Angular CLI README](https://github.com/angular/angular-cli/blob/master/README.md).

### json server

To start the server run `json-server --watch db.json`

runs at `http://localhost:3000`
further information at [github](https://github.com/typicode/json-server)

### CSS framework

We use [Angular Material](https://v7.material.angular.io)
