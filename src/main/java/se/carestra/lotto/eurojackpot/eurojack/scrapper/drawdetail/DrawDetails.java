package se.carestra.lotto.eurojackpot.eurojack.scrapper.drawdetail;

import se.carestra.lotto.eurojackpot.eurojack.scrapper.archive.DrawNumberURI;

public record DrawDetails(EurojackpotDraw draw,
                   JackpotDetail jackpotDetail,
                   DrawNumberURI resourceUri) {
  public DrawDetails {
    if (draw == null) {
      throw new NullPointerException("draw cannot be null.");
    }
    if (jackpotDetail == null) {
      throw new NullPointerException("jackpotDetail cannot be null.");
    }
    if (resourceUri == null) {
      throw new NullPointerException("resourceUri cannot be null.");
    }
  }
}
