# QualiChain Analytics Intelligent Profiling

## How to install
    - Clone this repository (git clone https://github.com/QualiChain/analytics.git)

## Configuration
The QualiChain Analytics Intelligent Profiling server expects a knowledgebase for storage and retrival of data, for this purpose you can set your SPARQL endpoint address in the configuration file:
    - Set the address of Sparql Endpoint in src/IP/config/app.config file:
      for example: sparqlendpoint=http://localhost:3030/QualiChain/ 

Alternatively, You can use the Fuseki server docker contaier published with the QualiChain Analytics Intelligent Profiling server instead of your knowlegebase 
    - Set the address of Sparql Endpoint in src/IP/config/app.config file as 
      sparqlendpoint=http://fuseki:3030/QualiChain/ 

## Running the Server
if you want to use your own SPARQL endpoint, you only need to use the docker image of the Intelligent Profiling server:


    - Run docker build -t profiling .
    - Run docker run --publish 8000:8000 -t profiling

But if you want to use your Fuseki SPARQL endpoint, you need to use both docker images of the Intelligent Profiling server and Fuseki:


    - Run docker-compose up --build --force-recreate -d
