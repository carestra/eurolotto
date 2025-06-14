package se.carestra.lotto.eurojackpot.eurojack.scrapper.archive;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import se.carestra.lotto.eurojackpot.eurojack.scrapper.JsoupScrapper;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
class EurojackpotDrawNumberURIScrapperTest {

  @Value("${eurojackpot.url.base}")
  private String eurojackpotBaseUrl;
  @Value("${eurojackpot.url.archive.year.prefix}")
  private String archiveYearPrefix;
  private static final String ARCHIVE_YEAR_UNDER_TEST = "2000";
  private String fullPathUnderTest;

  private DrawNumberURIScrapper scrapper;

  private JsoupScrapper jsoupMock;
  private Document documentMock;
  private Elements buttonElementsMock;
  private Elements tableElementsMock;
  private Element firstTableMock;
  private Elements tableBodyElementsMock;
  private Element firstTableBodyElementMock;
  private Elements tableRowsMock;

  @BeforeEach
  void setUp() {
    fullPathUnderTest = eurojackpotBaseUrl + archiveYearPrefix + ARCHIVE_YEAR_UNDER_TEST;
    jsoupMock = mock(JsoupScrapper.class);
    scrapper = new EurojackpotDrawNumberURIScrapper(eurojackpotBaseUrl, archiveYearPrefix, jsoupMock);

    documentMock = mock(Document.class);
    buttonElementsMock = mock(Elements.class);
    tableElementsMock = mock(Elements.class);
    firstTableMock = mock(Element.class);
    tableBodyElementsMock = mock(Elements.class);
    firstTableBodyElementMock = mock(Element.class);
    tableRowsMock = mock(Elements.class);
  }

  @Test
  void emptyListWhenNoArchiveFound() throws IOException {
    Optional<List<DrawNumberURI>> actual = scrapper.fetch("");

    assertTrue(actual.isEmpty());
    verify(jsoupMock, times(1)).getDocument(eurojackpotBaseUrl + archiveYearPrefix);
  }

  @Test
  void emptyListWhenNoTableFound() throws IOException {
    mockButtons(null);

    Optional<List<DrawNumberURI>> actual = scrapper.fetch(ARCHIVE_YEAR_UNDER_TEST);

    assertTrue(actual.isEmpty());
    verifyButtons();
  }

  @Test
  void emptyListWhenNoTableBodyFound() throws IOException {
    mockTable(null);

    Optional<List<DrawNumberURI>> actual = scrapper.fetch(ARCHIVE_YEAR_UNDER_TEST);

    assertTrue(actual.isEmpty());
    verifyTable();
  }

  @Test
  void emptyListWhenNoTableRowsFound() throws IOException {
    mockTableBody(null);

    Optional<List<DrawNumberURI>> actual = scrapper.fetch(ARCHIVE_YEAR_UNDER_TEST);

    assertTrue(actual.isEmpty());
    verifyTableBody();
  }

  @Test
  void emptyListWhenEmptyTableRowsFound() throws IOException {
    mockTableBody(tableRowsMock);
    when(tableRowsMock.stream()).thenReturn(Stream.of());

    Optional<List<DrawNumberURI>> actual = scrapper.fetch(ARCHIVE_YEAR_UNDER_TEST);

    assertTrue(actual.isEmpty());
    verifyTableBody();
    verify(tableRowsMock, times(1)).stream();
  }

  @Test
  void emptyListWhenEmptyRowDataFound() throws IOException {
    Element rowElementMock = mock(Element.class);
    mockOneTableRow(rowElementMock, null);

    Optional<List<DrawNumberURI>> actual = scrapper.fetch(ARCHIVE_YEAR_UNDER_TEST);

    assertTrue(actual.isEmpty());
    verifyOneTableRow();
    verify(rowElementMock, times(1)).selectFirst("td");
  }

  @Test
  void emptyListWhenNoAnchorFound() throws IOException {
    Element rowElementMock = mock(Element.class);
    Element anchorElementMock = mock(Element.class);
    mockOneTableRowWithAnchor(rowElementMock, anchorElementMock, null);

    Optional<List<DrawNumberURI>> actual = scrapper.fetch(ARCHIVE_YEAR_UNDER_TEST);

    assertTrue(actual.isEmpty());
    verifyOneTableRow();
    verify(rowElementMock, times(1)).selectFirst("td");
    verify(anchorElementMock, times(1)).select("a");
  }

  @Test
  void emptyListWhenNoHRefFound() throws IOException {
    Element rowElementMock = mock(Element.class);
    Element anchorElementMock = mock(Element.class);
    Elements hrefElementsMock = mock(Elements.class);
    mockOneTableRowWithAnchor(rowElementMock, anchorElementMock, hrefElementsMock);
    when(hrefElementsMock.getFirst()).thenReturn(null);

    Optional<List<DrawNumberURI>> actual = scrapper.fetch(ARCHIVE_YEAR_UNDER_TEST);

    assertTrue(actual.isEmpty());

    verifyOneTableRow();
    verify(rowElementMock, times(1)).selectFirst("td");
    verify(anchorElementMock, times(1)).select("a");
    verify(hrefElementsMock, times(1)).getFirst();
  }

  @Test
  void emptyListWhenHRefIsNull() throws IOException {
    Element rowElementMock = mock(Element.class);
    Element anchorElementMock = mock(Element.class);
    Elements hrefElementsMock = mock(Elements.class);
    Element hrefElement = mock(Element.class);
    mockOneTableRowWithAnchor(rowElementMock, anchorElementMock, hrefElementsMock);
    when(hrefElementsMock.isEmpty()).thenReturn(Boolean.FALSE);
    when(hrefElementsMock.getFirst()).thenReturn(hrefElement);
    when(hrefElement.attr("href")).thenReturn(null);

    Optional<List<DrawNumberURI>> actual = scrapper.fetch(ARCHIVE_YEAR_UNDER_TEST);

    assertTrue(actual.isEmpty());

    verifyOneTableRow();
    verify(rowElementMock, times(1)).selectFirst("td");
    verify(anchorElementMock, times(1)).select("a");
    verify(hrefElementsMock, times(1)).isEmpty();
    verify(hrefElementsMock, times(1)).getFirst();
    verify(hrefElement, times(1)).attr("href");
  }

  @Test
  void fetchSuccessfullyOneDrawNumberURI() throws IOException {
    Element rowElementMock = mock(Element.class);
    Element anchorElementMock = mock(Element.class);
    Elements hrefElementsMock = mock(Elements.class);
    Element hrefElement = mock(Element.class);
    mockOneTableRowWithAnchor(rowElementMock, anchorElementMock, hrefElementsMock);
    when(hrefElementsMock.getFirst()).thenReturn(hrefElement);
    String expectedURI = "/results/10-06-2012";
    when(hrefElement.attr("href")).thenReturn(expectedURI);

    Optional<List<DrawNumberURI>> actual = scrapper.fetch(ARCHIVE_YEAR_UNDER_TEST);

    assertTrue(actual.isPresent());
    assertEquals(1, actual.get().size());
    assertEquals(expectedURI, actual.get().getFirst().resourceUri());
    assertEquals(fullPathUnderTest, actual.get().getFirst().archiveUrl());

    verifyOneTableRow();
    verify(rowElementMock, times(1)).selectFirst("td");
    verify(anchorElementMock, times(1)).select("a");
    verify(hrefElementsMock, times(1)).getFirst();
    verify(hrefElement, times(1)).attr("href");
  }

  @Test
  void fetchSuccessfullySeveralDrawNumberURIs() throws IOException {
    Element rowElementMock = mock(Element.class);
    Element rowElementMock2 = mock(Element.class);
    Element anchorElementMock = mock(Element.class);
    Element anchorElementMock2 = mock(Element.class);
    Elements hrefElementsMock = mock(Elements.class);
    Elements hrefElementsMock2 = mock(Elements.class);
    Element hrefElement = mock(Element.class);
    Element hrefElement2 = mock(Element.class);
    mockTwoTableRowWithAnchor(rowElementMock, rowElementMock2, anchorElementMock, anchorElementMock2, hrefElementsMock, hrefElementsMock2);
    when(hrefElementsMock.getFirst()).thenReturn(hrefElement);
    String expectedURI = "/results/10-06-2012";
    when(hrefElement.attr("href")).thenReturn(expectedURI);
    when(hrefElementsMock2.getFirst()).thenReturn(hrefElement2);
    String expectedURI2 = "/results/10-06-2012";
    when(hrefElement2.attr("href")).thenReturn(expectedURI2);


    Optional<List<DrawNumberURI>> actual = scrapper.fetch(ARCHIVE_YEAR_UNDER_TEST);

    assertTrue(actual.isPresent());
    assertEquals(2, actual.get().size());
    assertEquals(expectedURI, actual.get().get(0).resourceUri());
    assertEquals(fullPathUnderTest, actual.get().get(0).archiveUrl());
    assertEquals(expectedURI2, actual.get().get(1).resourceUri());
    assertEquals(fullPathUnderTest, actual.get().get(1).archiveUrl());
    verifyOneTableRow();
    verify(rowElementMock, times(1)).selectFirst("td");
    verify(anchorElementMock, times(1)).select("a");
    verify(hrefElementsMock, times(1)).getFirst();
    verify(hrefElement, times(1)).attr("href");
    verify(rowElementMock2, times(1)).selectFirst("td");
    verify(anchorElementMock2, times(1)).select("a");
    verify(hrefElementsMock2, times(1)).getFirst();
    verify(hrefElement2, times(1)).attr("href");
  }

  private void verifyButtons() throws IOException {
    verify(jsoupMock, times(1)).getDocument(fullPathUnderTest);
    verify(documentMock, times(1)).select("table");
    verify(documentMock, times(1)).select("a.btn ");
    verify(buttonElementsMock, times(1)).attr("href");
  }

  private void mockButtons(Elements tableElements) throws IOException {
    when(jsoupMock.getDocument(fullPathUnderTest)).thenReturn(Optional.of(documentMock));
    when((documentMock.select("a.btn "))).thenReturn(buttonElementsMock);
    when(buttonElementsMock.attr("href")).thenReturn(archiveYearPrefix + ARCHIVE_YEAR_UNDER_TEST);
    when(documentMock.select("table")).thenReturn(tableElements);
  }

  private void verifyTable() throws IOException {
    verifyButtons();
    verify(tableElementsMock, times(1)).getFirst();
    verify(firstTableMock, times(1)).select("tbody");
  }

  private void mockTable(Elements tbodyElements) throws IOException {
    mockButtons(tableElementsMock);
    when(tableElementsMock.getFirst()).thenReturn(firstTableMock);
    when(firstTableMock.select("tbody")).thenReturn(tbodyElements);
  }

  private void verifyTableBody() throws IOException {
    verifyTable();
    verify(firstTableMock, times(1)).select("tbody");
    verify(tableBodyElementsMock, times(1)).getFirst();
    verify(firstTableBodyElementMock, times(1)).select("tr");
  }

  private void mockTableBody(Elements tableRows) throws IOException {
    mockTable(tableBodyElementsMock);
    when(tableBodyElementsMock.getFirst()).thenReturn(firstTableBodyElementMock);
    when(firstTableBodyElementMock.select("tr")).thenReturn(tableRows);
  }

  private void verifyOneTableRow() throws IOException {
    verifyTableBody();
    verify(firstTableBodyElementMock, times(1)).select("tr");
    verify(tableRowsMock, times(1)).stream();
  }

  private void mockOneTableRow(Element rowElementMock, Element tableDataElement) throws IOException {
    mockTableBody(tableRowsMock);
    when(tableRowsMock.stream()).thenReturn(Stream.of(rowElementMock));
    when(rowElementMock.selectFirst("td")).thenReturn(tableDataElement);
  }

  private void mockOneTableRowWithAnchor(Element rowElementMock, Element anchorElementMock, Elements href) throws IOException {
    mockTableBody(tableRowsMock);
    when(tableRowsMock.stream()).thenReturn(Stream.of(rowElementMock));
    when(rowElementMock.selectFirst("td")).thenReturn(anchorElementMock);
    when(anchorElementMock.select("a")).thenReturn(href);
  }

  private void mockTwoTableRowWithAnchor(Element rowElementMock, Element rowElementMock2, Element anchorElementMock, Element anchorElementMock2, Elements hrefElementsMock, Elements hrefElementsMock2) throws IOException {
    mockTableBody(tableRowsMock);
    when(tableRowsMock.stream()).thenReturn(Stream.of(rowElementMock, rowElementMock2));
    when(rowElementMock.selectFirst("td")).thenReturn(anchorElementMock);
    when(rowElementMock2.selectFirst("td")).thenReturn(anchorElementMock2);
    when(anchorElementMock.select("a")).thenReturn(hrefElementsMock);
    when(anchorElementMock2.select("a")).thenReturn(hrefElementsMock2);
  }
}