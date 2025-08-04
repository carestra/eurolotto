package se.carestra.lotto.eurojackpot.eurojack.archive;

import se.carestra.lotto.eurojackpot.eurojack.archive.service.DrawNumberURI;

import java.util.List;
import java.util.Optional;

record ResourceHrefElements(ResourceAnchorElements anchors) implements ResourceHref {

  ResourceHrefElements(TableBodyRowDataElement bodyRowDataElement) {
    this(new ResourceAnchorElements(bodyRowDataElement.rowsDataStream()));
  }

  ResourceHrefElements(TableBodyRowData.TableRowDataExtractor rowDataExtractor) {
    this(new TableBodyRowDataElement(rowDataExtractor));
  }

  ResourceHrefElements(TableBodyRowElements tableBodyRowElements) {
    this(new TableBodyRowData.TableRowDataExtractor(tableBodyRowElements.tableBodyRowElements()));
  }

  ResourceHrefElements(TableBodyRow.TableBodyRowExtractor bodyRowExtractor) {
    this(new TableBodyRowElements(bodyRowExtractor));
  }

  ResourceHrefElements(TableBodyElement tableBodyElement) {
    this(new TableBodyRow.TableBodyRowExtractor(tableBodyElement.tableBodyElement()));
  }

  ResourceHrefElements(TableBody.TableBodyElementExtractor tableBodyElementExtractor) {
    this(new TableBodyElement(tableBodyElementExtractor.tableBodyElement));
  }

  ResourceHrefElements(TableElements tableElements) {
    this(new TableBody.TableBodyElementExtractor(tableElements.tableElements()));
  }

  public Optional<List<DrawNumberURI>> getDrawNumberURIs(String fullPath) {
    return extractAndConvert(anchors.anchorStream(), fullPath);
  }
}
