#!/usr/bin/env groovy

// put itextpdf-5.3.0.jar in your $CLASSPATH

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

PdfImportedPage getPage(PdfWriter writer, PdfReader reader, int page)
{
  if (page > 0 && page <= reader.getNumberOfPages())
    return writer.getImportedPage(reader, page);
  else
    return null;
}

void twoup(String input)
{
  String output = input.replace(".pdf", "-2up.pdf");
  if (input == output) {
    printf("Invalid input file name '%s', must end with '.pdf'\n", input);
    return;
  }
  printf("Two-up %s to %s\n", input, output);

  PdfReader reader = new PdfReader(input);
  final int n = reader.getNumberOfPages();
  Rectangle pageSize = reader.getPageSize(1);
  System.out.println("Input page size: " + pageSize);

  Document doc = new Document(PageSize.A4.rotate(), 0, 0, 0, 0);
  PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(output));
  doc.open();
  // splitLine(doc, writer);

  double kTextWidth = 390;
  double kTextHeight = 590;
  double kHMargin = 10;
  double kVOffset = 0;

  float bottom = (doc.top() - pageSize.getHeight()) / 2 + kVOffset;
  float left = doc.right() / 2 - (pageSize.getWidth() + kTextWidth) / 2 - kHMargin;
  float right = doc.right() / 2 - (pageSize.getWidth() - kTextWidth) / 2 + kHMargin;

  PdfContentByte cb = writer.getDirectContent();
  for (int i = 0; i <= n;) {
    PdfImportedPage page = getPage(writer, reader, i++);
    if (page != null)
      cb.addTemplate(page, left, bottom);

    page = getPage(writer, reader, i++);
    if (page != null)
      cb.addTemplate(page, right, bottom);

    doc.newPage();
  }
  doc.close();
}

for (input in args) {
  twoup(input);
}
