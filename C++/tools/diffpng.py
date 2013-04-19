#!/usr/bin/python

import Image
import ImageChops
import sys

if len(sys.argv) == 4:
  orig = Image.open(sys.argv[1])
  diff = ImageChops.difference(orig, Image.open(sys.argv[2]))
  red = Image.new('RGB', orig.size, (255, 0, 0))
  new = Image.composite(red, orig.convert('RGB'), diff)
  new.save(sys.argv[3])
else:
  print "Usage: %s old.png new.png diff.png" % sys.argv[0]
