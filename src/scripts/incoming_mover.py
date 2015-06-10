#!/usr/bin/python3
import time, shutil, os, pygame
srcdir = "/home/pi/.aMule/Incoming"
trgdir = "/home/pi/all_music/incoming"
while True:
    files = os.listdir(srcdir)
    print("Files to move: " + ' '.join(files))
    for file in files:
        print(file)
        srcFile = srcdir + "/" + file
        shutil.move(srcFile, trgdir)
        print("Moved " + srcFile + " --> " + trgdir)

    time.sleep(60)
