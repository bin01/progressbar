package com.bin01.progressbar;

public class TestMain {

  public static void main(String[] args) throws InterruptedException {
    ProgressBar pb =
        ConsoleProgressBar.newBuilder().refreshRate(1000).showBar(true).showCounters(true)
            .showPercent(true).showSpeed(true).showTimeLeft(true).total(100).build();
    pb.start();
    for (int i = 0; i < 100; ++i) {
      pb.increment();
      Thread.sleep(1000);
    }
    pb.finish();
  }

}
