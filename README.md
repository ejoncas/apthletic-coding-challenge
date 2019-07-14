# Apthletic Coding Challenge

[![Build Status](https://travis-ci.com/nanducoder/apthletic-coding-challenge.svg?branch=master)](https://travis-ci.com/nanducoder/apthletic-coding-challenge)

# Requirements

```
1. Crawl a sports orientated website for sporting events and save to database ie. crawl ESPN and save 3 ~ 5 games.
2.  Provide an API to access the crawled data stored in the database
* Database and API can be local or hosted.
The API should be written in Java, the crawler can be any technology they desire to use.
```


# Crawler

After  investigating  how the websites were working, I  found two types:

* Dynamic Rendered Content:  This kind of website load the base  html and then they perform an ajax call to
retrieve all  the events. For this type of websites,  I  can simply  call their  API and read the json.
* Server side  html  generated: This type of  website renders the complete UI  on  the server side  and returns
a  bunch of HTML.  This kind of website is  a bit more complicated to read as HTML is  not always a structured language
so some parsers struggle. Here is where I made use of [Scrapy](https://scrapy.org)


## Ironman Crawler (HTML Scraper Crawler)

This crawler retrieves ironman events from `http://ap.ironman.com/events/triathlon-races.aspx`

This crawler operates reading the HTML and scraping the data I care about in order to create the event.


## AFL Crawler (API Crawler)

This crawler is a simple json API call because the  AFL website  has a nice API  that their  web UI uses to generate all
the content  dynamically.


## How to Run

The crawlers are dockerized, so no need for any dependency install other than docker.


### Run Ironman Crawler

```
cd crawler
docker-compose run -e "EVENTS_API_URL=http://<url>" crawler python ironman_crawler.py
```

### Run AFL Crawler

```
cd crawler
docker-compose run -e "EVENTS_API_URL=http://<url>" crawler python afl_crawler.py
```


# Events API

## Compile

```
./mvnw package
```

## Run locally

```
java -jar target/apthletic-events-api-0.0.1-SNAPSHOT.jar
```

## How to Query

This app is currently deployed in Heroku. Endpoint will be provided by email.

The following curl command will return all AFL events starting after 2000-07-25


```
curl -X GET \
  'http://localhost:8080/v1/events/afl?date=2000-07-25T00:00:00.000%2B00:00' \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json'
```

For Ironman events

```
curl -X GET \
  'http://localhost:8080/v1/events/ironman?date=2000-07-25T00:00:00.000%2B00:00' \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json'
```


The `date` query parameter is completely optional. If not provided it will default to current
date time and then you will get events starting  "soon".

The `page` query parameter can be added to paginate through results. Mostly useful for ironman events
as  there are quite a few.


### No Data in the API?

It is completely possible as it is an in-memory database and Heroku kills  the service if unused.
Just  re-run the crawlers:

```
cd crawler
docker-compose run -e "EVENTS_API_URL=http://<url>" crawler python ironman_crawler.py
docker-compose run -e "EVENTS_API_URL=http://<url>" crawler python afl_crawler.py
```

# Design  Decisions / Trade-offs


* Indexes are non-existent. This is fine for such a simple project but for a real use case we would need to add appropriate
indexes to the database, most likely one on event_datetime so we can easily search by  event  start time.

* Database: Uses H2 in memory database

* Deployment: This project is getting deployed to my heroku account. Deployment is just running
a java -jar command (see `Procfile`).

* Pagination:  I'm using the built-in pagination mechanisms from spring-jpa. This is usually good to start with but as the data
grows we will quickly find issues with COUNT(*) and OFFSET in queries. At that point, it  would be better to implement a token based
pagination system where the user can only know if there are more pages or not. But we won't let them know the total number
of items or pages. This is usually good enough for apps as they can implement  the infinite scrolling.

* Authentication has not been implemented but it is highly recommended. An stateless authentication mechanism
would be preferred so this app can scale horizontally. JWT would work.

* Crawler Testing: There is no unit test for the crawlers as I think they would add very little value at this stage. Mocking
the website and expect a response will usually work, but this crawlers are most likely to break if the original website
fails and a Unit test won't pick that up. The  way I would approach crawler testing at  this  stage would  be to  have
continuous integration  running them all the time against the original  website.  If  they ever  fail to  retrieve
the expected data I would fail  the build and raise some alerts for the team to fix it asap.
Of course, the more complicated the crawler gets it may start to have multiple classes. At that point, it may make
more sense to consider unit testing.

* Schema Decisions: This project is using Hibernate + H2 with  an autogenerated schema by Hibernate. This is usually good
enough to start with but depending on the underlying database and the performance  needed it may be a  better choice to roll
your own  schema plus queries. I have been in this situation multiple times and my favourite tools for the job are:
    * [Flyway](https://flywaydb.org/) for migrations
    * [JOOQ](https://www.jooq.org) for sql generation and mapping to your business model
I also decided to keep the `away` and `home` score as part of the same  `event` table.  This may be a bit more tricky to do stats
around overall scores but it is better for retrieval as there is no need for joining tables. Again, what is good here
depends a lot on how you're going  to use it. Here I decided to keep the scope small  and go for the simplest solution
that gets the job done.




