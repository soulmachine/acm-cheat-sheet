#!/bin/sh

OLDPDF=$1
NEWPDF=$2

if [ -e "$OLDPDF" -a -e "$NEWPDF" ]; then
  mkdir new old dif

  gs -dNOPAUSE -dBATCH -sDEVICE=pnggray \
    -r144 -dTextAlphaBits=4 -dUseCropBox -sOutputFile='old/page-%03d.png' $OLDPDF &

  gs -dNOPAUSE -dBATCH -sDEVICE=pnggray \
    -r144 -dTextAlphaBits=4 -dUseCropBox -sOutputFile='new/page-%03d.png' $NEWPDF

  wait %1

  diff old new | awk '/^Binary.*differ/{print "./diffpng.py", $3, $5, "xx"$5}'|sed 's/xxnew/dif/'
else
  echo "Usage $0 oldpdf newpdf"
fi
