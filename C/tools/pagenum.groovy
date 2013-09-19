#!/usr/bin/env groovy

// put itextpdf-5.3.0.jar in your $CLASSPATH

import java.util.regex.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;


PdfPageLabels parsePageNum(String pagenum) {
  Pattern re = Pattern.compile("(\\d+)([nrel])(\\d+)?");
  def map = ['n': PdfPageLabels.DECIMAL_ARABIC_NUMERALS,
             'r': PdfPageLabels.LOWERCASE_ROMAN_NUMERALS,
             'e': PdfPageLabels.EMPTY,
             'l': PdfPageLabels.LOWERCASE_LETTERS,
            ];
  PdfPageLabels pageLabels = new PdfPageLabels();
  for (String item : pagenum.split(",")) {
    Matcher m = re.matcher(item);
    if (m.matches()) {
      int page = Integer.parseInt(m.group(1));
      int style = map[m.group(2)];
      int start = (m.group(3) != null) ? Integer.parseInt(m.group(3)) : 1;
      System.out.printf("page=%d style=%d start=%d\n", page, style, start);
      if (start == 1)
        pageLabels.addPageLabel(page, style);
      else
        pageLabels.addPageLabel(page, style, "", start);
    } else {
      println "WRONG pagedef: " + item;
    }
  }
  return pageLabels;
}

void pagenum(PdfPageLabels pageLabels, String input)
{
  String output = input.replace(".pdf", "-num.pdf");
  if (input == output) {
    printf("Invalid input file name '%s', must end with '.pdf'\n", input);
    return;
  }
  printf("Booklet %s to %s\n", input, output);

  PdfReader reader = new PdfReader(input);
  final int n = reader.getNumberOfPages();
  Rectangle pageSize = reader.getPageSize(1);
  System.out.println("Input page size: " + pageSize);

  PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(output));
  stamper.getWriter().setPageLabels(pageLabels);
  stamper.close();
}

if (args.length < 2) {
  println "Usage: pagenum.groovy pagedef input1 [input2 ...]"
  println "pagedef is like '1e,3r,17n'"
  return
}

pageLabels = parsePageNum(args[0]);

for (input in args[1..args.length-1]) {
  pagenum(pageLabels, input);
}
