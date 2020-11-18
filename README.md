# Tracker Nineteen

Tracker Nineteen is a COVID-19 tracker. It's a Spring Boot web application that tracks the full case and death history of COVID-19 in the United States. The backend consists of a scheduled service that fetches the latest updates from a Github dataset several times a day. The frontend was made using Thymeleaf, which is a Java templating engine that makes it easy to serve dynamic HTML without using a full-fledged JavaScript framework. The statistics are stored in a MySQL database.

## Datasets
The application pulls from [this](https://github.com/nytimes/covid-19-data) NY Times Github repo. The particular datasets used are listed below:
- U.S. All Data: https://raw.githubusercontent.com/nytimes/covid-19-data/master/us.csv
- State All Data: https://raw.githubusercontent.com/nytimes/covid-19-data/master/us-states.csv
- U.S. Live Data: https://raw.githubusercontent.com/nytimes/covid-19-data/master/live/us.csv
- State Live Data: https://raw.githubusercontent.com/nytimes/covid-19-data/master/live/us-states.csv

The "All" datasets contain the full case and death history. It is used to populate the database when the application is first started up. The "Live" datasets contain the most recent stats. It is used to update the database every day.

## How It Works
The backend makes use of the Spring scheduler, and uses a cron job to update the database at several points every day. When the scheduler is triggered, the fetch service will make a GET request to the "Live" datasets above, retrieve the raw CSV data, parse it, and persist the relevant bits to the MySQL database. The controller layer is responsible for getting the requested stats from the service layer and formatting it for the model layer. Speaking of, the frontend consists of the Thymeleaf template engine. The DataTables and Chart.js libraries are used to create the tables and graphs. There's some jQuery here to handle sorting/filtering the tables and configuring the graphs.

## Screenshots
Coming soon...
