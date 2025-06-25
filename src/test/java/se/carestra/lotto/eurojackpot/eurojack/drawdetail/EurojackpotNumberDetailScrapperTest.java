package se.carestra.lotto.eurojackpot.eurojack.drawdetail;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import se.carestra.lotto.eurojackpot.eurojack.scrapper.JsoupScrapper;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
class EurojackpotNumberDetailScrapperTest {
  @Value("${eurojackpot.url.base}")
  private String eurojackpotUrl;

  @Value("${eurojackpot.drawnumber.resource.uri}")
  private String resourceUnderTest;

  private DrawDetailScrapper scrapper;
  private JsoupScrapper jsoupMock;
  private String fullResourceURLUnderTest;

  private Document documentMock;
  private Elements resultElementsMock;
  private Elements unOrderListElementsMock;
  private Element allBallsElementMock;
  private Elements ballsElementsMock;
  private Elements euroBallsElementsMock;
  private Elements ribbonElementsMock;
  private Elements ribbonSpanElementsMock;
  private Elements winnersElements;
  private Elements winnersCountElements;
  private Elements jackpotElements;
  private Elements jackpotAmountElements;
  private Element ballStreamElementMock;
  private Elements ballSpansElementsMock;
  private Element ballElementMock;
  private Element euroBallStreamElementMock;
  private Elements euroBallSpansElementsMock;
  private Element euroBallElementMock;
  private Element rolloverElementsMock;
  private Element winnersCountValueElements;
  private Element jackpotAmountValueElements;

  @BeforeEach
  void setUp() {
    jsoupMock = mock(JsoupScrapper.class);
    scrapper = new EurojackpotDramNumberDetailScrapper(eurojackpotUrl, jsoupMock);
    fullResourceURLUnderTest = eurojackpotUrl + resourceUnderTest;

    documentMock = mock(Document.class);
    resultElementsMock = mock(Elements.class);
    unOrderListElementsMock = mock(Elements.class);
    allBallsElementMock = mock(Element.class);
    ballsElementsMock = mock(Elements.class);
    euroBallsElementsMock = mock(Elements.class);
    ribbonElementsMock = mock(Elements.class);
    ribbonSpanElementsMock = mock(Elements.class);
    winnersElements = mock(Elements.class);
    winnersCountElements = mock(Elements.class);
    jackpotElements = mock(Elements.class);
    jackpotAmountElements = mock(Elements.class);
    ballSpansElementsMock = mock(Elements.class);
    ballElementMock = mock(Element.class);
    ballStreamElementMock = mock(Element.class);
    euroBallStreamElementMock = mock(Element.class);
    euroBallSpansElementsMock = mock(Elements.class);
    euroBallElementMock = mock(Element.class);
    rolloverElementsMock = mock(Element.class);
    winnersCountValueElements = mock(Element.class);
    jackpotAmountValueElements = mock(Element.class);
  }

  @Test
  void resourceNotFound() throws IOException {
    Optional<DrawDetails> actual = scrapper.fetchDetails(resourceUnderTest);
    assertTrue(actual.isEmpty());
    verify(jsoupMock, times(1)).getDocument(fullResourceURLUnderTest);
  }

  @Test
  void emptyListWhenNoResultElementsNotFound() throws IOException {
    resultElementsMock = null;
    mockDocument(resultElementsMock);

    Optional<DrawDetails> actual = scrapper.fetchDetails(resourceUnderTest);

    assertTrue(actual.isEmpty());
    verifyDocument();
  }

  @Test
  void emptyWhenListOfNumbersNotFound() throws IOException {
    unOrderListElementsMock = null;
    mockBallsList(unOrderListElementsMock);

    Optional<DrawDetails> actual = scrapper.fetchDetails(resourceUnderTest);

    assertTrue(actual.isEmpty());
    verifyBallsList();
  }

  @Test
  void emptyWhenBallNumbersNotFound() throws IOException {
    ballsElementsMock = null;
    mockBallsList(unOrderListElementsMock);
    when(unOrderListElementsMock.first()).thenReturn(allBallsElementMock);
    when(allBallsElementMock.select("li.ball")).thenReturn(ballsElementsMock);

    Optional<DrawDetails> actual = scrapper.fetchDetails(resourceUnderTest);

    assertTrue(actual.isEmpty());
    verifyBallsList();
    verify(unOrderListElementsMock, times(1)).first();
    verify(allBallsElementMock, times(1)).select("li.ball");
  }


  @Test
  void emptyWhenEuroBallNumbersNotFound() throws IOException {
    euroBallsElementsMock = null;
    mockBallsList(unOrderListElementsMock);
    when(unOrderListElementsMock.first()).thenReturn(allBallsElementMock);
    when(allBallsElementMock.select("li.euro")).thenReturn(euroBallsElementsMock);

    Optional<DrawDetails> actual = scrapper.fetchDetails(resourceUnderTest);

    assertTrue(actual.isEmpty());
    verifyBallsList();
    verify(unOrderListElementsMock, times(1)).first();
    verify(allBallsElementMock, times(1)).select("li.euro");
  }

  @Test
  void emptyWhenOnlyAllBallsFound() throws IOException {
    mockBallsList(unOrderListElementsMock);
    when(unOrderListElementsMock.first()).thenReturn(allBallsElementMock);
    when(allBallsElementMock.select("li.ball")).thenReturn(ballsElementsMock);
    when(allBallsElementMock.select("li.euro")).thenReturn(euroBallsElementsMock);

    Optional<DrawDetails> actual = scrapper.fetchDetails(resourceUnderTest);

    assertTrue(actual.isEmpty());
    verifyBallsList();
    verify(unOrderListElementsMock, times(1)).first();
    verify(allBallsElementMock, times(1)).select("li.ball");
    verify(allBallsElementMock, times(1)).select("li.euro");
  }

  @Test
  void emptyWhenRibbonNotFound() throws IOException {
    ribbonElementsMock = null;
    mockDocument(resultElementsMock);
    when(resultElementsMock.select("div.ribbon")).thenReturn(ribbonElementsMock);

    Optional<DrawDetails> actual = scrapper.fetchDetails(resourceUnderTest);

    assertTrue(actual.isEmpty());
    verifyDocument();
    verify(resultElementsMock, times(1)).select("div.ribbon");
  }

  @Test
  void emptyWhenRibbonSpanNotFound() throws IOException {
    ribbonSpanElementsMock = null;
    mockDocument(resultElementsMock);
    when(resultElementsMock.select("div.ribbon")).thenReturn(ribbonElementsMock);
    when(ribbonElementsMock.select("span")).thenReturn(ribbonSpanElementsMock);

    Optional<DrawDetails> actual = scrapper.fetchDetails(resourceUnderTest);

    assertTrue(actual.isEmpty());
    verifyDocument();
    verify(resultElementsMock, times(1)).select("div.ribbon");
    verify(ribbonElementsMock, times(1)).select("span");
  }

  @Test
  void emptyWhenWinnersNotFound() throws IOException {
    winnersElements = null;
    mockDocument(resultElementsMock);
    when(resultElementsMock.select("div.winners")).thenReturn(winnersElements);

    Optional<DrawDetails> actual = scrapper.fetchDetails(resourceUnderTest);

    assertTrue(actual.isEmpty());
    verifyDocument();
    verify(resultElementsMock, times(1)).select("div.winners");
  }

  @Test
  void emptyWhenWinnersCountNotFound() throws IOException {
    winnersCountElements = null;
    mockDocument(resultElementsMock);
    when(resultElementsMock.select("div.winners")).thenReturn(winnersElements);
    when(winnersElements.select("div.elem2")).thenReturn(winnersCountElements);

    Optional<DrawDetails> actual = scrapper.fetchDetails(resourceUnderTest);

    assertTrue(actual.isEmpty());
    verifyDocument();
    verify(resultElementsMock, times(1)).select("div.winners");
    verify(winnersElements, times(1)).select("div.elem2");
  }

  @Test
  void emptyWhenJackpotNotFound() throws IOException {
    jackpotElements = null;
    mockDocument(resultElementsMock);
    when(resultElementsMock.select("div.jackpot-amount")).thenReturn(jackpotElements);

    Optional<DrawDetails> actual = scrapper.fetchDetails(resourceUnderTest);

    assertTrue(actual.isEmpty());
    verifyDocument();
    verify(resultElementsMock, times(1)).select("div.jackpot-amount");
  }

  @Test
  void emptyWhenJackpotAmountNotFound() throws IOException {
    jackpotAmountElements = null;
    mockDocument(resultElementsMock);
    when(resultElementsMock.select("div.jackpot-amount")).thenReturn(jackpotElements);
    when(jackpotElements.select("div.element2")).thenReturn(jackpotAmountElements);

    Optional<DrawDetails> actual = scrapper.fetchDetails(resourceUnderTest);

    assertTrue(actual.isEmpty());
    verifyDocument();
    verify(resultElementsMock, times(1)).select("div.jackpot-amount");
    verify(jackpotElements, times(1)).select("div.element2");
  }

  @Test
  void foundDrawNumberDetails() throws IOException {
    mockBallsList(unOrderListElementsMock);
    when(unOrderListElementsMock.first()).thenReturn(allBallsElementMock);
    mockBalls();
    mockEuroBalls();
    mockRibbonRollover();
    mockWinnersCount();
    mockJackpotAmount();

    Optional<DrawDetails> actual = scrapper.fetchDetails(resourceUnderTest);

    assertTrue(actual.isPresent());
    DrawDetails drawDetails = actual.get();
    assertBalls(drawDetails);
    assertEuroBalls(drawDetails);
    assertJackpotAmount(drawDetails);
    assertResourceInfo(drawDetails);

    verifyBallsList();
    verify(unOrderListElementsMock, times(1)).first();
    verifyBalls();
    verifyEuroBalls();

    verifyRibbonRollover();
    verifyWinnersCount();
    verifyJackpotAmount();
  }

  private void verifyJackpotAmount() {
    verify(resultElementsMock, times(1)).select("div.jackpot-amount");
    verify(jackpotElements, times(1)).select("div.element2");
    verify(jackpotAmountElements, times(1)).first();
    verify(jackpotAmountValueElements, times(1)).text();
  }

  private void verifyWinnersCount() {
    verify(resultElementsMock, times(1)).select("div.winners");
    verify(winnersElements, times(1)).select("div.elem2");
    verify(winnersCountElements, times(1)).getFirst();
    verify(winnersCountValueElements, times(1)).text();
  }

  private void verifyRibbonRollover() {
    verify(resultElementsMock, times(1)).select("div.ribbon");
    verify(ribbonElementsMock, times(1)).select("span");
    verify(ribbonSpanElementsMock, times(1)).getFirst();
    verify(rolloverElementsMock, times(1)).text();
  }

  private void verifyEuroBalls() {
    verify(allBallsElementMock, times(1)).select("li.euro");
    verify(euroBallsElementsMock, times(1)).stream();
    verify(euroBallStreamElementMock, times(2)).select("span");
    verify(euroBallSpansElementsMock, times(2)).getFirst();
    verify(euroBallElementMock, times(2)).text();
  }

  private void verifyBalls() {
    verify(allBallsElementMock, times(1)).select("li.ball");
    verify(ballsElementsMock, times(1)).stream();
    verify(ballStreamElementMock, times(5)).select("span");
    verify(ballSpansElementsMock, times(5)).getFirst();
    verify(ballElementMock, times(5)).text();
  }

  private void assertResourceInfo(DrawDetails drawDetails) {
    LocalDate drawDateActual = drawDetails.drawDate();
    assertEquals("2000-05-07", drawDateActual.toString());
    String resourceUriActual = drawDetails.resourceUri();
    assertEquals(resourceUnderTest, resourceUriActual);
    String archiveUrlActual = drawDetails.fullPath();
    assertEquals(eurojackpotUrl + resourceUnderTest, archiveUrlActual);
  }

  private static void assertJackpotAmount(DrawDetails drawDetails) {
    String jackpotAmountActual = drawDetails.jackpotDetail().jackpotAmount().formattedAmount();
    assertEquals("340,000,000", jackpotAmountActual);
    String jackpotAmountCurrencySymbol = drawDetails.jackpotDetail().jackpotAmount().currencySymbol();
    assertEquals("kr", jackpotAmountCurrencySymbol);
  }

  private static void assertEuroBalls(DrawDetails drawDetails) {
    List<Integer> euroballsActual = drawDetails.draw().euroBallNumbers().numbers().stream().toList();
    List<Integer> euroballsExpected = List.of(9, 5);
    assertEquals(euroballsExpected, euroballsActual);
  }

  private static void assertBalls(DrawDetails drawDetails) {
    List<Integer> ballsActual = drawDetails.draw().ballNumbers().numbers().stream().toList();
    List<Integer> ballsExpected = List.of(46, 15, 27, 1, 8);
    assertEquals(ballsExpected, ballsActual);
  }

  private void mockJackpotAmount() {
    when(resultElementsMock.select("div.jackpot-amount")).thenReturn(jackpotElements);
    when(jackpotElements.select("div.element2")).thenReturn(jackpotAmountElements);
    when(jackpotAmountElements.first()).thenReturn(jackpotAmountValueElements);
    when(jackpotAmountValueElements.text()).thenReturn("kr.340,000,000");
  }

  private void mockWinnersCount() {
    when(resultElementsMock.select("div.winners")).thenReturn(winnersElements);
    when(winnersElements.select("div.elem2")).thenReturn(winnersCountElements);
    when(winnersCountElements.getFirst()).thenReturn(winnersCountValueElements);
    when(winnersCountValueElements.text()).thenReturn("0");
  }

  private void mockRibbonRollover() {
    when(resultElementsMock.select("div.ribbon")).thenReturn(ribbonElementsMock);
    when(ribbonElementsMock.select("span")).thenReturn(ribbonSpanElementsMock);
    when(ribbonSpanElementsMock.getFirst()).thenReturn(rolloverElementsMock);
    when(rolloverElementsMock.text()).thenReturn("4x");
  }

  private void mockEuroBalls() {
    when(allBallsElementMock.select("li.euro")).thenReturn(euroBallsElementsMock);
    when(euroBallsElementsMock.stream())
        .thenReturn(
            Stream.of(
                euroBallStreamElementMock,
                euroBallStreamElementMock)
        );
    when(euroBallStreamElementMock.select("span"))
        .thenReturn(euroBallSpansElementsMock)
        .thenReturn(euroBallSpansElementsMock);
    when(euroBallSpansElementsMock.getFirst())
        .thenReturn(euroBallElementMock)
        .thenReturn(euroBallElementMock);
    when(euroBallElementMock.text())
        .thenReturn("9")
        .thenReturn("5");
  }

  private void mockBalls() {
    when(allBallsElementMock.select("li.ball")).thenReturn(ballsElementsMock);
    when(ballsElementsMock.stream())
        .thenReturn(
            Stream.of(
                ballStreamElementMock,
                ballStreamElementMock,
                ballStreamElementMock,
                ballStreamElementMock,
                ballStreamElementMock)
        );
    when(ballStreamElementMock.select("span"))
        .thenReturn(ballSpansElementsMock)
        .thenReturn(ballSpansElementsMock)
        .thenReturn(ballSpansElementsMock)
        .thenReturn(ballSpansElementsMock)
        .thenReturn(ballSpansElementsMock);
    when(ballSpansElementsMock.getFirst())
        .thenReturn(ballElementMock)
        .thenReturn(ballElementMock)
        .thenReturn(ballElementMock)
        .thenReturn(ballElementMock)
        .thenReturn(ballElementMock);
    when(ballElementMock.text())
        .thenReturn("46")
        .thenReturn("15")
        .thenReturn("27")
        .thenReturn("1")
        .thenReturn("8");
  }

  private void mockDocument(Elements resultElementsMock) throws IOException {
    when(jsoupMock.getDocument(fullResourceURLUnderTest)).thenReturn(Optional.of(documentMock));
    when(documentMock.select("div.results")).thenReturn(resultElementsMock);
  }

  private void verifyDocument() throws IOException {
    verify(jsoupMock, times(1)).getDocument(fullResourceURLUnderTest);
    verify(documentMock, times(1)).select("div.results");
  }

  private void mockBallsList(Elements unOrderListElementsMock) throws IOException {
    mockDocument(resultElementsMock);
    when(resultElementsMock.select("ul#ballsDrawn")).thenReturn(unOrderListElementsMock);
  }

  private void verifyBallsList() throws IOException {
    verifyDocument();
    verify(resultElementsMock, times(1)).select("ul#ballsDrawn");
  }
}
