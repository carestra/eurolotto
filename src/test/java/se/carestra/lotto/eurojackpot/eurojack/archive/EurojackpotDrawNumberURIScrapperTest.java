package se.carestra.lotto.eurojackpot.eurojack.archive;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import se.carestra.lotto.eurojackpot.eurojack.archive.api.DrawNumberURI;
import se.carestra.lotto.eurojackpot.eurojack.archive.api.DrawNumberURIScrapper;
import se.carestra.lotto.eurojackpot.eurojack.scrapper.api.JsoupScrapper;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ApplicationModuleTest
class EurojackpotDrawNumberURIScrapperTest {

  @Value("${eurojackpot.url.base}")
  private String eurojackpotBaseUrl;
  @Value("${eurojackpot.url.archive.year.prefix}")
  private String archiveYearPrefix;
  private static final String ARCHIVE_YEAR_UNDER_TEST = "2000";
  private String fullPathUnderTest;

  private DrawNumberURIScrapper scrapper;

  @MockitoBean
  private JsoupScrapper jsoupMock;
  private Document documentMock;
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
    when(jsoupMock.getDocument(fullPathUnderTest)).thenReturn(Optional.of(documentMock));
    when(documentMock.select("table")).thenReturn(null);

    Optional<List<DrawNumberURI>> actual = scrapper.fetch(ARCHIVE_YEAR_UNDER_TEST);

    assertTrue(actual.isEmpty());
    verify(jsoupMock, times(1)).getDocument(fullPathUnderTest);
    verify(documentMock, times(1)).select("table");
  }

  @Test
  void emptyListWhenNoTableBodyFound() throws IOException {
    when(jsoupMock.getDocument(fullPathUnderTest)).thenReturn(Optional.of(documentMock));
    when(documentMock.select("table")).thenReturn(tableElementsMock);
    when(tableElementsMock.getFirst()).thenReturn(firstTableMock);
    when(firstTableMock.select("tbody")).thenReturn(null);

    Optional<List<DrawNumberURI>> actual = scrapper.fetch(ARCHIVE_YEAR_UNDER_TEST);

    assertTrue(actual.isEmpty());
    verify(jsoupMock, times(1)).getDocument(fullPathUnderTest);
    verify(documentMock, times(1)).select("table");
    verify(tableElementsMock, times(1)).getFirst();
    verify(firstTableMock, times(1)).select("tbody");
  }

  @Test
  void emptyListWhenNoTableRowsFound() throws IOException {
    when(jsoupMock.getDocument(fullPathUnderTest)).thenReturn(Optional.of(documentMock));
    when(documentMock.select("table")).thenReturn(tableElementsMock);
    when(tableElementsMock.getFirst()).thenReturn(firstTableMock);
    when(firstTableMock.select("tbody")).thenReturn(tableBodyElementsMock);
    when(tableBodyElementsMock.getFirst()).thenReturn(firstTableBodyElementMock);
    when(firstTableBodyElementMock.select("tr")).thenReturn(null);

    Optional<List<DrawNumberURI>> actual = scrapper.fetch(ARCHIVE_YEAR_UNDER_TEST);

    assertTrue(actual.isEmpty());
    verify(jsoupMock, times(1)).getDocument(fullPathUnderTest);
    verify(documentMock, times(1)).select("table");
    verify(tableElementsMock, times(1)).getFirst();
    verify(firstTableMock, times(1)).select("tbody");
    verify(firstTableMock, times(1)).select("tbody");
    verify(tableBodyElementsMock, times(1)).getFirst();
    verify(firstTableBodyElementMock, times(1)).select("tr");
  }

  @Test
  void emptyListWhenEmptyTableRowsFound() throws IOException {
    when(jsoupMock.getDocument(fullPathUnderTest)).thenReturn(Optional.of(documentMock));
    when(documentMock.select("table")).thenReturn(tableElementsMock);
    when(tableElementsMock.getFirst()).thenReturn(firstTableMock);
    when(firstTableMock.select("tbody")).thenReturn(tableBodyElementsMock);
    when(tableBodyElementsMock.getFirst()).thenReturn(firstTableBodyElementMock);
    when(firstTableBodyElementMock.select("tr")).thenReturn(tableRowsMock);
    when(tableRowsMock.stream()).thenReturn(Stream.of());

    Optional<List<DrawNumberURI>> actual = scrapper.fetch(ARCHIVE_YEAR_UNDER_TEST);

    assertTrue(actual.isEmpty());
    verify(jsoupMock, times(1)).getDocument(fullPathUnderTest);
    verify(documentMock, times(1)).select("table");
    verify(tableElementsMock, times(1)).getFirst();
    verify(firstTableMock, times(1)).select("tbody");
    verify(tableBodyElementsMock, times(1)).getFirst();
    verify(firstTableBodyElementMock, times(1)).select("tr");
    verify(tableRowsMock, times(1)).stream();
  }

  @Test
  void emptyListWhenEmptyRowDataFound() throws IOException {
    Element rowElementMock = mock(Element.class);
    when(jsoupMock.getDocument(fullPathUnderTest)).thenReturn(Optional.of(documentMock));
    when(documentMock.select("table")).thenReturn(tableElementsMock);
    when(tableElementsMock.getFirst()).thenReturn(firstTableMock);
    when(firstTableMock.select("tbody")).thenReturn(tableBodyElementsMock);
    when(tableBodyElementsMock.getFirst()).thenReturn(firstTableBodyElementMock);
    when(firstTableBodyElementMock.select("tr")).thenReturn(tableRowsMock);
    when(tableRowsMock.stream()).thenReturn(Stream.of(rowElementMock));
    when(rowElementMock.selectFirst("td")).thenReturn(rowElementMock);

    Optional<List<DrawNumberURI>> actual = scrapper.fetch(ARCHIVE_YEAR_UNDER_TEST);

    assertTrue(actual.isEmpty());
    verify(jsoupMock, times(1)).getDocument(fullPathUnderTest);
    verify(documentMock, times(1)).select("table");
    verify(tableElementsMock, times(1)).getFirst();
    verify(firstTableMock, times(1)).select("tbody");
    verify(tableBodyElementsMock, times(1)).getFirst();
    verify(firstTableBodyElementMock, times(1)).select("tr");
    verify(tableRowsMock, times(1)).stream();
    verify(rowElementMock, times(1)).selectFirst("td");
  }

  @Test
  void emptyListWhenNoAnchorFound() throws IOException {
    Element rowElementMock = mock(Element.class);
    Element anchorElementMock = mock(Element.class);
    when(jsoupMock.getDocument(fullPathUnderTest)).thenReturn(Optional.of(documentMock));
    when(documentMock.select("table")).thenReturn(tableElementsMock);
    when(tableElementsMock.getFirst()).thenReturn(firstTableMock);
    when(firstTableMock.select("tbody")).thenReturn(tableBodyElementsMock);
    when(tableBodyElementsMock.getFirst()).thenReturn(firstTableBodyElementMock);
    when(firstTableBodyElementMock.select("tr")).thenReturn(tableRowsMock);
    when(tableRowsMock.stream()).thenReturn(Stream.of(rowElementMock));
    when(rowElementMock.selectFirst("td")).thenReturn(anchorElementMock);
    when(anchorElementMock.select("a")).thenReturn(null);

    Optional<List<DrawNumberURI>> actual = scrapper.fetch(ARCHIVE_YEAR_UNDER_TEST);

    assertTrue(actual.isEmpty());
    verify(jsoupMock, times(1)).getDocument(fullPathUnderTest);
    verify(documentMock, times(1)).select("table");
    verify(tableElementsMock, times(1)).getFirst();
    verify(firstTableMock, times(1)).select("tbody");
    verify(tableBodyElementsMock, times(1)).getFirst();
    verify(firstTableBodyElementMock, times(1)).select("tr");
    verify(tableRowsMock, times(1)).stream();
    verify(rowElementMock, times(1)).selectFirst("td");
    verify(anchorElementMock, times(1)).select("a");
  }

  @Test
  void emptyListWhenNoHRefFound() throws IOException {
    Element rowElementMock = mock(Element.class);
    Element anchorElementMock = mock(Element.class);
    Elements hrefElementsMock = mock(Elements.class);
    when(jsoupMock.getDocument(fullPathUnderTest)).thenReturn(Optional.of(documentMock));
    when(documentMock.select("table")).thenReturn(tableElementsMock);
    when(tableElementsMock.getFirst()).thenReturn(firstTableMock);
    when(firstTableMock.select("tbody")).thenReturn(tableBodyElementsMock);
    when(tableBodyElementsMock.getFirst()).thenReturn(firstTableBodyElementMock);
    when(firstTableBodyElementMock.select("tr")).thenReturn(tableRowsMock);
    when(tableRowsMock.stream()).thenReturn(Stream.of(rowElementMock));
    when(rowElementMock.selectFirst("td")).thenReturn(anchorElementMock);
    when(anchorElementMock.select("a")).thenReturn(hrefElementsMock);
    when(hrefElementsMock.getFirst()).thenReturn(null);

    Optional<List<DrawNumberURI>> actual = scrapper.fetch(ARCHIVE_YEAR_UNDER_TEST);

    assertTrue(actual.isEmpty());

    verify(jsoupMock, times(1)).getDocument(fullPathUnderTest);
    verify(documentMock, times(1)).select("table");
    verify(tableElementsMock, times(1)).getFirst();
    verify(firstTableMock, times(1)).select("tbody");
    verify(tableBodyElementsMock, times(1)).getFirst();
    verify(firstTableBodyElementMock, times(1)).select("tr");
    verify(tableRowsMock, times(1)).stream();
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
    when(jsoupMock.getDocument(fullPathUnderTest)).thenReturn(Optional.of(documentMock));
    when(documentMock.select("table")).thenReturn(tableElementsMock);
    when(tableElementsMock.getFirst()).thenReturn(firstTableMock);
    when(firstTableMock.select("tbody")).thenReturn(tableBodyElementsMock);
    when(tableBodyElementsMock.getFirst()).thenReturn(firstTableBodyElementMock);
    when(firstTableBodyElementMock.select("tr")).thenReturn(tableRowsMock);
    when(tableRowsMock.stream()).thenReturn(Stream.of(rowElementMock));
    when(rowElementMock.selectFirst("td")).thenReturn(anchorElementMock);
    when(anchorElementMock.select("a")).thenReturn(hrefElementsMock);
    when(hrefElementsMock.getFirst()).thenReturn(hrefElement);
    when(hrefElement.attr("href")).thenReturn(null);

    Optional<List<DrawNumberURI>> actual = scrapper.fetch(ARCHIVE_YEAR_UNDER_TEST);

    assertTrue(actual.isEmpty());

    verify(jsoupMock, times(1)).getDocument(fullPathUnderTest);
    verify(documentMock, times(1)).select("table");
    verify(tableElementsMock, times(1)).getFirst();
    verify(firstTableMock, times(1)).select("tbody");
    verify(tableBodyElementsMock, times(1)).getFirst();
    verify(firstTableBodyElementMock, times(1)).select("tr");
    verify(tableRowsMock, times(1)).stream();
    verify(rowElementMock, times(1)).selectFirst("td");
    verify(anchorElementMock, times(1)).select("a");
    verify(hrefElementsMock, times(1)).getFirst();
    verify(hrefElement, times(1)).attr("href");
  }

  @Test
  void fetchSuccessfullyOneDrawNumberURI() throws IOException {
    Element rowElementMock = mock(Element.class);
    Element anchorElementMock = mock(Element.class);
    Elements hrefElementsMock = mock(Elements.class);
    Element hrefElement = mock(Element.class);
    when(jsoupMock.getDocument(fullPathUnderTest)).thenReturn(Optional.of(documentMock));
    when(documentMock.select("table")).thenReturn(tableElementsMock);
    when(tableElementsMock.getFirst()).thenReturn(firstTableMock);
    when(firstTableMock.select("tbody")).thenReturn(tableBodyElementsMock);
    when(tableBodyElementsMock.getFirst()).thenReturn(firstTableBodyElementMock);
    when(firstTableBodyElementMock.select("tr")).thenReturn(tableRowsMock);
    when(tableRowsMock.stream()).thenReturn(Stream.of(rowElementMock));
    when(rowElementMock.selectFirst("td")).thenReturn(anchorElementMock);
    when(anchorElementMock.select("a")).thenReturn(hrefElementsMock);
    when(hrefElementsMock.getFirst()).thenReturn(hrefElement);
    String expectedURI = "/results/10-06-2012";
    when(hrefElement.attr("href")).thenReturn(expectedURI);

    Optional<List<DrawNumberURI>> actual = scrapper.fetch(ARCHIVE_YEAR_UNDER_TEST);

    assertTrue(actual.isPresent());
    assertEquals(1, actual.get().size());
    assertEquals(expectedURI, actual.get().getFirst().resourceUri());
    assertEquals(fullPathUnderTest, actual.get().getFirst().archiveUrl());

    verify(jsoupMock, times(1)).getDocument(fullPathUnderTest);
    verify(documentMock, times(1)).select("table");
    verify(tableElementsMock, times(1)).getFirst();
    verify(firstTableMock, times(1)).select("tbody");
    verify(tableBodyElementsMock, times(1)).getFirst();
    verify(firstTableBodyElementMock, times(1)).select("tr");
    verify(tableRowsMock, times(1)).stream();
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
    when(jsoupMock.getDocument(fullPathUnderTest)).thenReturn(Optional.of(documentMock));
    when(documentMock.select("table")).thenReturn(tableElementsMock);
    when(tableElementsMock.getFirst()).thenReturn(firstTableMock);
    when(firstTableMock.select("tbody")).thenReturn(tableBodyElementsMock);
    when(tableBodyElementsMock.getFirst()).thenReturn(firstTableBodyElementMock);
    when(firstTableBodyElementMock.select("tr")).thenReturn(tableRowsMock);
    when(tableRowsMock.stream()).thenReturn(Stream.of(rowElementMock, rowElementMock2));
    when(rowElementMock.selectFirst("td")).thenReturn(anchorElementMock);
    when(anchorElementMock.select("a")).thenReturn(hrefElementsMock);
    when(hrefElementsMock.getFirst()).thenReturn(hrefElement);
    String expectedURI = "/results/10-06-2012";
    when(hrefElement.attr("href")).thenReturn(expectedURI);
    when(rowElementMock2.selectFirst("td")).thenReturn(anchorElementMock2);
    when(anchorElementMock2.select("a")).thenReturn(hrefElementsMock2);
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

    verify(jsoupMock, times(1)).getDocument(fullPathUnderTest);
    verify(documentMock, times(1)).select("table");
    verify(tableElementsMock, times(1)).getFirst();
    verify(firstTableMock, times(1)).select("tbody");
    verify(tableBodyElementsMock, times(1)).getFirst();
    verify(firstTableBodyElementMock, times(1)).select("tr");
    verify(tableRowsMock, times(1)).stream();
    verify(rowElementMock, times(1)).selectFirst("td");
    verify(anchorElementMock, times(1)).select("a");
    verify(hrefElementsMock, times(1)).getFirst();
    verify(hrefElement, times(1)).attr("href");
    verify(rowElementMock2, times(1)).selectFirst("td");
    verify(anchorElementMock2, times(1)).select("a");
    verify(hrefElementsMock2, times(1)).getFirst();
    verify(hrefElement2, times(1)).attr("href");
  }
}