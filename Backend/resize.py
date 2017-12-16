import os, sys
from PIL import Image
import argparse
from os import listdir
from os.path import isfile, join

if __name__ == '__main__':
    maxheigth = 320
    maxwidth = 320
    parser = argparse.ArgumentParser(description='Cleaning files that are too big.')
    parser.add_argument('-dir', '--d', help="Directory name", required=True)
    args = parser.parse_args()
    directory = args.d
    directory = "res/images/"+directory
    print(directory)
    onlyfiles = [f for f in listdir(directory) if isfile(join(directory, f)) and not ".zip" in f]

    resizedDir = directory+"/resized"
    if not os.path.exists(resizedDir):
        os.makedirs(resizedDir)

    for f in onlyfiles:
        try:
            img = Image.open(directory+"/"+f)
            width, heigth = img.size
            ratio = min(maxwidth/width,maxheigth/heigth)
            size = ratio*width, ratio*heigth
            img.thumbnail(size, Image.ANTIALIAS)
            img.save(resizedDir+"/"+f)
            os.rename(resizedDir+"/"+f, directory+"/"+f)
        except IOError:
            print("cannot create thumbnail for '%s'" % f)

    os.rmdir(resizedDir)
