#!/usr/bin/env python3

import sys
import time
import requests
from pathlib import Path
from threading import Thread


if len(sys.argv) < 3:
  print('Usage: {0} PATH FUSEKI_ENDPOINT'.format(sys.argv[0]))
  sys.exit(1)

headers = {'Content-type': 'text/plain', 'Slug': 'triples.nt'}

good = 0
bad = 0
def stats():
  print('Files: good={0}  bad={1}'.format(good, bad))

def statsloop():
  while True:
    time.sleep(1)
    stats()

t = Thread(target=statsloop)
t.daemon = True
t.start()

for file in Path(sys.argv[1]).iterdir():
  r = requests.put(sys.argv[2], data=open(file, 'rb'), headers=headers)
  if r.status_code == 200:
    good += 1
  else:
    bad += 1

print('Finished!')
stats()
sys.exit(0)
