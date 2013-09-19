#!/usr/bin/env groovy

// put itextpdf-5.3.0.jar in your $CLASSPATH

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

void splitLine(Document doc, PdfWriter writer)
{
  PdfContentByte cb = writer.getDirectContentUnder();
  cb.moveTo((float)(doc.right() / 2), doc.bottom());
  cb.lineTo((float)(doc.right() / 2), doc.top());
  cb.stroke();
}

PdfImportedPage getPage(PdfWriter writer, PdfReader reader, int page)
{
  if (page > 0 && page <= reader.getNumberOfPages())
    return writer.getImportedPage(reader, page);
  else
    return null;
}

void booklet(String input)
{
  String output = input.replace(".pdf", "-booklet.pdf");
  if (input == output) {
    printf("Invalid input file name '%s', must end with '.pdf'\n", input);
    return;
  }
  printf("Booklet %s to %s\n", input, output);

  PdfReader reader = new PdfReader(input);
  final int n = reader.getNumberOfPages();
  Rectangle pageSize = reader.getPageSize(1);
  System.out.println("Input page size: " + pageSize);

  Document doc = new Document(PageSize.A4.rotate(), 0, 0, 0, 0);
  PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(output));
  doc.open();
  splitLine(doc, writer);

  double kTextWidth = 400;
  double kTextHeight = 590;
  double kHMargin = 10;
  double kVOffset = -3;

  float bottom = (doc.top() - pageSize.getHeight()) / 2 + kVOffset;
  float left = doc.right() / 2 - (pageSize.getWidth() + kTextWidth) / 2 - kHMargin;
  float right = doc.right() / 2 - (pageSize.getWidth() - kTextWidth) / 2 + kHMargin;

  int[] pages = new int[((int)((n + 3) / 4)) * 4];
  int x = 1, y = pages.length;
  for (int i = 0; i < pages.length;) {
    pages[i++] = y--;
    pages[i++] = x++;
    pages[i++] = x++;
    pages[i++] = y--;
  }

  PdfContentByte cb = writer.getDirectContent();
  for (int i = 0; i < pages.length;) {
    PdfImportedPage page = getPage(writer, reader, pages[i++]);
    if (page != null)
      cb.addTemplate(page, left, bottom);

    page = getPage(writer, reader, pages[i++]);
    if (page != null)
      cb.addTemplate(page, right, bottom);

    doc.newPage();
  }
  doc.close();
}

for (input in args) {
  booklet(input);
}
