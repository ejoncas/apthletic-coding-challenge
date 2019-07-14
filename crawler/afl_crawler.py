import json
import logging
import os
import requests
import sys
from datetime import datetime

TOTAL_ROUNDS_AFL_SEASON = 23

logging.basicConfig(stream=sys.stdout, level=logging.INFO)
logger = logging.getLogger()

afl_events_endpoint = "https://www.afl.com.au/aflrender/get"


def find_utc_time(times):
    for time in times:
        if time.get('name') == 'utc':
            date_part = time.get('date')
            time_part = time.get('time')
            return datetime.strptime(date_part + " " + time_part + " +0000", '%Y-%m-%d %H:%M %p %z')


def to_status(match_status_text):
    if match_status_text == "LIVE":
        return "STARTED"
    elif match_status_text == "Full Time":
        return "FINISHED"
    else:
        return "NOT_STARTED"


def to_afl_result(team):
    return {
        'totalScore': team.get('totalScore'),
        'behind': team.get('behind'),
        'goals': team.get('goals')
    }


def to_team(away):
    return {
        'id': away.get('teamAbbr'),
        'name': away.get('teamName')
    }


## Looks like the pattern is as follow:
## Season = CD_S<year><seasonIdentifier>
## Round = CD_R<year><seasonIdentifier><RoundNumber>
def retrieve_afl_events(round_number):
    logger.info("Retrieving events for round %s", round_number)
    events = []
    response = requests.get(afl_events_endpoint, params={
        'service': 'fullFixture',
        'field': 'json',
        'site': 'AFL',
        'params': 'seasonId:CD_S2019014,competitionType:AFL,roundId:CD_R2019014' + format(round_number, '02')
    })
    if response.status_code == 200:
        for fixture in response.json().get('fixtures'):
            away = fixture.get('awayTeam')
            home = fixture.get('homeTeam')
            match = fixture.get('match')
            utc_time = find_utc_time(match.get('startDateTimes')).isoformat('T', 'milliseconds')
            home_team = to_team(home)
            away_team = to_team(away)
            match_code = "M_2019_R" + str(round_number) + "_" + home_team['id'] + "_" + away_team['id']
            events.append({
                'id': match_code,
                'type': 'afl',
                'title': home.get('teamName') + ' vs ' + away.get('teamName'),
                'time': utc_time,
                'status': to_status(match.get('status')),
                'venue': match.get('venueName'),
                'round': match.get('roundName'),
                'homeTeam': home_team,
                'awayTeam': away_team,
                'homeResult': to_afl_result(home),
                'awayResult': to_afl_result(away)
            })
        return events
    else:
        raise ValueError('Crawler not working, Received ' + str(response) + ' from  the AFL website. ')


def load_events(events):
    if len(events) > 0:
        url = os.getenv('EVENTS_API_URL', 'http://localhost:8080')
        r = requests.post(url + "/v1/import/afl", data=json.dumps(events),
                          headers={'Content-Type': 'application/json', 'Accept': 'application/json'})
        if r.status_code == 201:
            logger.info('%s Events successfully loaded to API', len(events))
        else:
            raise ValueError('Something went wrong while loading  to  API ' + str(r))
    else:
        logger.warn("No events to load")


for i in range(1, TOTAL_ROUNDS_AFL_SEASON):
    events = retrieve_afl_events(i)
    load_events(events)
