#!/usr/bin/env groovy

map = new TreeMap<Character, Integer>();

for (input in args) {
  content = new File(input).getText("UTF-8")
  // println content.size()
  for (ch in content) {
    if (ch in map) {
      map[ch] += 1;
    } else {
      map[ch] = 1;
    }
  }
}

map.entrySet().each {
  println it.key + " " + it.value
}

