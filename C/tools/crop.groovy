#!/usr/bin/env groovy

// put itextpdf-5.3.0.jar in your $CLASSPATH

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

void crop(String input)
{
  String output = input.replace(".pdf", "-crop.pdf");
  if (input == output) {
    printf("Invalid input file name '%s', must end with '.pdf'\n", input);
    return;
  }
  printf("Crop %s to %s\n", input, output);

  PdfReader reader = new PdfReader(input);
  final int n = reader.getNumberOfPages();
  Rectangle pageSize = reader.getPageSize(1);
  System.out.println("Input page size: " + pageSize);

  double kTextWidth = 400;
  double kTextHeight = 590;
  double kVOffset = 0;

  double left = (pageSize.getWidth() - kTextWidth) / 2;
  double right = pageSize.getWidth() - left;
  double bottom = (pageSize.getHeight() - kTextHeight) / 2;
  double top = pageSize.getHeight() - bottom;
  PdfRectangle rect = new PdfRectangle((float)(left),
                                       (float)(bottom + kVOffset),
                                       (float)(right),
                                       (float)(top + kVOffset));

  for (int i = 1; i <= n; i++) {
    PdfDictionary pageDict = reader.getPageN(i);
    pageDict.put(PdfName.CROPBOX, rect);
  }

  PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(output));
  stamper.close();
}

for (input in args) {
  crop(input);
}
