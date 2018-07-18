# Coding_Assessment
Coding Assessment for DevOps Role

# This repository can be used as a starting point for coding assessment.

# The Brief
You are tasked with developing Continuous Integration configuration for an ATM machine application. The software is responsible for validating customer account details and performing basic operations including balance inquiries and cash withdraws.

The code, https://github.com/ssaurabh61/Coding_Assessment/tree/development, is the current state of the project. We need to create a Jenkinsfile, gradle and dockerfile. We also need to include sonarqube check, no need for actual keys the format is important. 

The code now needs to be built on the development environment as soon as the code is pushed into the development branch. Once the tests are successful the code needs to be promoted to QA branch and retested and repeat similarly for Pre-Production branch and finally code is promoted to Release and tagged with timestamp, and a build is archived on the Release branch and tagged by a next version.

Ideally the code should report on code coverage, static code analysis and report on last check-in and if any future fails then report on who was the last contributor.

The build then needs to be deployed on an AWS/Virtual machine where load-balancing is achieved through Kubernetes for any future state where the application is then built for a performance intensive state.

The code should be able to be built and tested at a minimum without issues and deployment needs to take in account manual intervention and approval flows.

If there are any improvements that you find could be added in the code base to help with CI/CD process or on building better quality code they can be added here as well.
