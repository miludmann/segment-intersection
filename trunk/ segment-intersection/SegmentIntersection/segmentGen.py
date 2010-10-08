## python script
# Create file with randomized segments - user choose the number of segments
# run in a console, typing : "python segmentGen"
# tested with python 2.6

import random

nbSegments = input('Number of segments : ')
fileOut = raw_input('Output file: ')

fileRes = open(fileOut+'.txt', 'w')
for i in range(nbSegments):
    fileRes.write(str(random.uniform(0, 557))+" "+str(random.uniform(0, 521))+" "+str(random.uniform(0, 557))+" "+str(random.uniform(0, 521))+" "+"\r\n")

fileRes.close()
