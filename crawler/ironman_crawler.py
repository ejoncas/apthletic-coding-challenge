import json
import logging
import os
import requests
import scrapy
from datetime import datetime
from scrapy import signals
from scrapy.crawler import CrawlerProcess
from scrapy.signalmanager import dispatcher

logger = logging.getLogger()
logger.setLevel(logging.INFO)

def to_utc_time(date):
    return datetime.strptime(date + " +0000", '%m/%d/%Y %H:%M:%S %z')


class IronmanSpider(scrapy.Spider):
    name = 'ironmanspider'
    start_urls = ['http://ap.ironman.com/events/triathlon-races.aspx']

    def parse(self, response):
        for article in response.css('article'):
            yield {
                'id': article.css('::attr(data-guid)').get(),
                'type': 'ironman',
                'title': article.css('header>a>h2 ::text').get(),
                'location': article.css('header>span ::text').get(),
                'time': to_utc_time(article.css('time::attr(datetime)').get()).isoformat('T', 'milliseconds'),
                'category': article.css('.eventCta ::text').get(),
                'status': 'NOT_STARTED',
                'image': article.css('.eventOverlay>img::attr(src)').get()
            }


def crawl_events():
    results = []

    def crawler_results(signal, sender, item, response, spider):
        results.append(item)

    dispatcher.connect(crawler_results, signal=signals.item_passed)

    process = CrawlerProcess({
        'USER_AGENT': 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36'
    })
    process.crawl(IronmanSpider)
    process.start()
    return results


def load_events(events):
    if len(events) > 0:
        url = os.getenv('EVENTS_API_URL', 'http://localhost:8080')
        r = requests.post(url + "/v1/import/ironman", data=json.dumps(events),
                          headers={'Content-Type': 'application/json', 'Accept': 'application/json'})
        if r.status_code == 201:
            logger.info('%s Events successfully loaded to API', len(events))
    else:
        logger.warn("No events to load")


ironman_events = crawl_events()
load_events(ironman_events)
