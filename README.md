# ContractView

[![Build Status](https://travis-ci.com/moritzrupp/contractview.svg?branch=develop)](https://travis-ci.com/moritzrupp/contractview)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=de.moritzrupp.contractview%3Acontractview&metric=alert_status)](https://sonarcloud.io/dashboard?id=de.moritzrupp.contractview%3Acontractview)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=de.moritzrupp.contractview%3Acontractview&metric=coverage)](https://sonarcloud.io/dashboard?id=de.moritzrupp.contractview%3Acontractview)

With ContractView you get the overview of your all contracts!

This project is currently in a very early stage of development. With the idea born, the plan is to add more and more features over time. If you are interested in collaborating
on this project, please see [below](#information-and-contact) for more information.

There is currently no central place where is application is hosted. If you want to use it, feel free to host it yourself.

## Technology stack

The ContractView application is build with [JHipster] 6.2.0. JHipster is a great tool for creating new web applications
based on [Spring Boot] and modern front-end frameworks like
[Angular] or [React]. There are many possibilities with JHipster. Please, make yourself familiar with it by browsing through the [JHipster documentation].

## Development

Before you can build this project, you must install and configure the following dependencies on your machine:

1. [Node.js][]: We use Node to run a development web server and build the project.
   Depending on your system, you can install Node either from source or as a pre-packaged bundle.

After installing Node, you should be able to run the following command to install development tools.
You will only need to run this command when dependencies change in [package.json](package.json).

    npm install

We use npm scripts and [Webpack][] as our build system.

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

    ./mvnw
    npm start

Npm is also used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by
specifying a newer version in [package.json](package.json). You can also run `npm update` and `npm install` to manage dependencies.
Add the `help` flag on any command to see how you can use it. For example, `npm help update`.

The `npm run` command will list all of the scripts available to run for this project.

### PWA Support

JHipster ships with PWA (Progressive Web App) support, and it's disabled by default. One of the main components of a PWA is a service worker.

The service worker initialization code is commented out by default. To enable it, uncomment the following code in `src/main/webapp/index.html`:

```html
<script>
  if ('serviceWorker' in navigator) {
    navigator.serviceWorker.register('./service-worker.js').then(function() {
      console.log('Service Worker Registered');
    });
  }
</script>
```

Note: [Workbox](https://developers.google.com/web/tools/workbox/) powers JHipster's service worker. It dynamically generates the `service-worker.js` file.

### Managing dependencies

For example, to add [Leaflet][] library as a runtime dependency of your application, you would run following command:

    npm install --save --save-exact leaflet

To benefit from TypeScript type definitions from [DefinitelyTyped][] repository in development, you would run following command:

    npm install --save-dev --save-exact @types/leaflet

Then you would import the JS and CSS files specified in library's installation instructions so that [Webpack][] knows about them:
Note: There are still a few other things remaining to do for Leaflet that we won't detail here.

For further instructions on how to develop with JHipster, have a look at [Using JHipster in development][].

## Building for production

### Packaging as jar

To build the final jar and optimize the contractview application for production, run:

    ./mvnw -Pprod clean verify

This will concatenate and minify the client CSS and JavaScript files. It will also modify `index.html` so it references these new files.
To ensure everything worked, run:

    java -jar target/*.jar

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

Refer to [Using JHipster in production][] for more details.

### Packaging as war

To package your application as a war in order to deploy it to an application server, run:

    ./mvnw -Pprod,war clean verify

## Testing

To launch your application's tests, run:

    ./mvnw verify

### Client tests

Unit tests are run by [Jest][] and written with [Jasmine][]. They're located in [src/test/javascript/](src/test/javascript/) and can be run with:

    npm test

For more information, refer to the [Running tests page][].

### Code quality

Sonar is used to analyse code quality. You can start a local Sonar server (accessible on http://localhost:9001) with:

```
docker-compose -f src/main/docker/sonar.yml up -d
```

You can run a Sonar analysis with using the [sonar-scanner](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner) or by using the maven plugin.

Then, run a Sonar analysis:

```
./mvnw -Pprod clean verify sonar:sonar
```

If you need to re-run the Sonar phase, please be sure to specify at least the `initialize` phase since Sonar properties are loaded from the sonar-project.properties file.

```
./mvnw initialize sonar:sonar
```

or

For more information, refer to the [Code quality page][].

## Using Docker to simplify development (optional)

You can use Docker to improve your JHipster development experience. A number of docker-compose configuration are available in the [src/main/docker](src/main/docker) folder to launch required third party services.

For example, to start a postgresql database in a docker container, run:

    docker-compose -f src/main/docker/postgresql.yml up -d

To stop it and remove the container, run:

    docker-compose -f src/main/docker/postgresql.yml down

You can also fully dockerize your application and all the services that it depends on.
To achieve this, first build a docker image of your app by running:

    ./mvnw -Pprod verify jib:dockerBuild

Then run:

    docker-compose -f src/main/docker/app.yml up -d

For more information refer to [Using Docker and Docker-Compose][], this page also contains information on the docker-compose sub-generator (`jhipster docker-compose`), which is able to generate docker configurations for one or several JHipster applications.

## Continuous Integration (optional)

To configure CI for your project, run the ci-cd sub-generator (`jhipster ci-cd`), this will let you generate configuration files for a number of Continuous Integration systems. Consult the [Setting up Continuous Integration][] page for more information.

# Information and contact

This project was initiated by Moritz Rupp ([moritz.rupp@gmail.com](mailto:moritz.rupp@gmail.com)). For him, it was important to have an overview of of all his current contracts.
Most applications on the market either cost money or they combine the basic functionality with comparison and selling intentions. Moreover, he wanted to learn some cool
technologies!

If you have any feedback or questions reagarding this application, please feel free to get in touch. Either create a [new issue] or send an [e-mail](mailto:moritz.rupp@gmail.com)!

# License

Copyright (C) 2018-2019 Moritz Rupp

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see <https://www.gnu.org/licenses/>.

See [LICENSE](LICENSE) to see the full text.

[jhipster]: https://www.jhipster.tech
[jhipster documentation]: https://www.jhipster.tech/documentation-archive/v5.5.0/
[spring boot]: https://spring.io/projects/spring-boot
[angular]: https://angular.io/
[react]: https://reactjs.org/
[jhipster homepage and latest documentation]: https://www.jhipster.tech
[jhipster 6.2.0 archive]: https://www.jhipster.tech/documentation-archive/v6.2.0
[using jhipster in development]: https://www.jhipster.tech/documentation-archive/v6.2.0/development/
[using docker and docker-compose]: https://www.jhipster.tech/documentation-archive/v6.2.0/docker-compose
[using jhipster in production]: https://www.jhipster.tech/documentation-archive/v6.2.0/production/
[running tests page]: https://www.jhipster.tech/documentation-archive/v6.2.0/running-tests/
[code quality page]: https://www.jhipster.tech/documentation-archive/v6.2.0/code-quality/
[setting up continuous integration]: https://www.jhipster.tech/documentation-archive/v6.2.0/setting-up-ci/
[node.js]: https://nodejs.org/
[yarn]: https://yarnpkg.org/
[webpack]: https://webpack.github.io/
[angular cli]: https://cli.angular.io/
[browsersync]: http://www.browsersync.io/
[jest]: https://facebook.github.io/jest/
[jasmine]: http://jasmine.github.io/2.0/introduction.html
[protractor]: https://angular.github.io/protractor/
[leaflet]: http://leafletjs.com/
[definitelytyped]: http://definitelytyped.org/
