import csv
import json

csvfile = open('res/smartdb.csv', 'r')

fieldnames = ("id","painting","author","description","thumbnailURL","museum")
reader = csv.DictReader( csvfile, fieldnames, delimiter="\t")
skipFirstRow = True
for row in reader:
    if(skipFirstRow == True):
        skipFirstRow = False
        continue
    jsonfile = open("res/json/"+row["id"]+'.json', 'w')
    json.dump(row, jsonfile)
    jsonfile.write('\n')

