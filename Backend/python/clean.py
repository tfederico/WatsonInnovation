import os
import argparse
from os import listdir
from os.path import isfile, join

if __name__ == '__main__':

	parser = argparse.ArgumentParser(description='Cleaning files that are too big.')
	parser.add_argument('-dir', '--d', help="Directory name", required=True)    

	args = parser.parse_args()

	directory = args.d
	



onlyfiles = [f for f in listdir(directory) if isfile(join(directory, f))]

removed = 0
for f in onlyfiles:
	size = os.path.getsize(join(directory, f))
	if size > 100000:
		os.remove(join(directory, f))
		removed = removed + 1

print "Number of file removed: "+str(removed)
