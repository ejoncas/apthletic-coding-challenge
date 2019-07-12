import scrapy
from scrapy.crawler import CrawlerProcess


class BlogSpider(scrapy.Spider):
    name = 'ironmanspider'
    start_urls = ['https://www.espn.com.au/afl/schedule/_/week/1']

    def parse(self, response):
        for title in response.css('.sched-container>h2'):
            yield {'day': title}


process = CrawlerProcess({
    'USER_AGENT': 'Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)'
})

process.crawl(BlogSpider)
process.start()