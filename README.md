# ContractView

[![Waffle.io - Columns and their card count](https://badge.waffle.io/moritzrupp/contractview.svg?columns=all)](https://waffle.io/moritzrupp/contractview)

With ContractView you get the overview of your all contracts!

This project is currently in a very early stage of development. With the idea born, the plan is to add more and more features over time. If you are interested in collaborating 
on this project, please see [below](#information-and-contact) for more information.

There is currently no central place where is application is hosted. If you want to use it, feel free to host it yourself.

## Technology stack

The ContractView application is build with [JHipster]. JHipster is a great tool for creating new web applications based on [Spring Boot] and modern front-end frameworks like 
[Angular] or [React]. There are many possibilities with JHipster. Please, make yourself familiar with it by browsing through the [JHipster documentation].

## Development

Before you can build this project, you must install and configure the following dependencies on your machine:

1. [Node.js][]: The project uses Node to run a development web server and build the project.
   Depending on your system, you can install Node either from source or as a pre-packaged bundle.

After installing Node, you should be able to run the following command to install development tools.
You will only need to run this command when dependencies change in [package.json](package.json).

    npm install

The project uses npm scripts and [Webpack][] as the build system.

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

    ./mvnw
    npm start

Npm is also used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by
specifying a newer version in [package.json](package.json). You can also run `npm update` and `npm install` to manage dependencies.
Add the `help` flag on any command to see how you can use it. For example, `npm help update`.

The `npm run` command will list all of the scripts available to run for this project.

## Building for production

To optimize the ContractView application for production, run:

    ./mvnw -Pprod clean package

This will concatenate and minify the client CSS and JavaScript files. It will also modify `index.html` so it references these new files.
To ensure everything worked, run:

    java -jar target/*.war

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

## Testing

To launch your application's tests, run:

    ./mvnw clean test

### Client tests

Unit tests are run by [Jest][] and written with [Jasmine][]. They're located in [src/test/javascript/](src/test/javascript/) and can be run with:

    npm test


### Code quality

Sonar is used to analyse code quality. You can start a local Sonar server (accessible on http://localhost:9001) with:

```
docker-compose -f src/main/docker/sonar.yml up -d
```

Then, run a Sonar analysis:

```
./mvnw -Pprod clean test sonar:sonar
```

# Information and contact

This project was initiated by Moritz Rupp ([moritz.rupp@gmail.com](mailto:moritz.rupp@gmail.com)). For him, it was important to have an overview of of all his current contracts.
 Most applications on the market either cost money or they combine the basic functionality with comparison and selling intentions. Moreover, he wanted to learn some cool 
 technologies!
 
 If you have any feedback or questions reagarding this application, please feel free to get in touch. Either create a [new issue] or send an [e-mail](mailto:moritz.rupp@gmail.com)!

# License

Copyright (C) 2018 Moritz Rupp

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.

See [LICENSE](LICENSE) to see the full text.

[JHipster]: https://www.jhipster.tech
[JHipster documentation]: https://www.jhipster.tech/documentation-archive/v5.5.0/
[Spring Boot]: https://spring.io/projects/spring-boot
[Angular]: https://angular.io/
[React]: https://reactjs.org/


[Node.js]: https://nodejs.org/
[Webpack]: https://webpack.github.io/
[Jest]: https://facebook.github.io/jest/
[Jasmine]: http://jasmine.github.io/2.0/introduction.html

[new issue]: https://github.com/moritzrupp/contractview/issues/new
