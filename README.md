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

This crawler is a simple json API because the  AFL website  has a nice API  that their  web UI uses to generate all
the content  dynamically.


## How to Run

The crawlers are dockerized, so no need for any dependency install other than docker.


### Run Ironman Crawler

```
cd crawler
docker-compose run crawler python ironman_crawler.py
```

### Run AFL Crawler

```
cd crawler
docker-compose run crawler python afl_crawler.py
```


# Events API


## How to Query

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


# Design  Decisions / Trade-offs


* Indexes are non-existent. This is fine for such a simple project but for a real use case we would need to add appropriate
indexes to the database, most likely one on event_datetime so we can easily search by  event  start time.

* Pagination:  I'm using the built-in pagination mechanisms from spring-jpa. This is usually good to start with but as the data
grows we will quickly find issues with COUNT(*) and OFFSET in queries. At that point, it  would be better to implement a token based
pagination system where the user can only know if there are more pages or not. But we won't let them know the total number
of items or pages. This is usually good enough for apps as they can implement  the infinite scrolling.

* Authentication has not been implemented but it is highly recommended. An stateless authentication mechanism
would be preferred so this app can scale horizontally. JWT would work.





