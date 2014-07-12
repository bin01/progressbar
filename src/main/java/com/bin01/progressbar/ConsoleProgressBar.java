/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.bin01.progressbar;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;


public class ConsoleProgressBar implements ProgressBar {
  public static final int DEFAULT_REFRESH_RATE = 200;
  public static final Format DEFAULT_FORMAT = Format.of("[=>-]");

  private boolean showPercent;
  private boolean showBar;
  private boolean showCounters;
  private boolean showTimeLeft;
  private boolean showSpeed;
  private Formatter formatter;
  private Format format;
  private int refreshRate;
  private long total;
  private OutputWriter outputWriter;

  private AtomicLong current;
  private AtomicBoolean isFinish;
  private long startTime;

  private ConsoleProgressBar(BuilderImpl builder) {
    showPercent = builder.showPercent;
    showBar = builder.showBar;
    showCounters = builder.showCounters;
    showTimeLeft = builder.showTimeLeft;
    showSpeed = builder.showSpeed;
    total = builder.total;
    formatter = builder.formatter;
    format = builder.format;
    refreshRate = builder.refreshRate;
    outputWriter = builder.outputWriter;
    current = new AtomicLong(0);
    isFinish = new AtomicBoolean(false);
  }

  @Override
  public void start() {
    startTime = System.currentTimeMillis();
    if (total == 0) {
      showBar = false;
      showTimeLeft = false;
      showPercent = false;
    }
    writer();
  }

  @Override
  public void increment() {
    add(1);
  }

  @Override
  public void set(long current) {
    this.current.set(current);
  }

  @Override
  public void add(long add) {
    this.current.addAndGet(add);
  }

  @Override
  public void finish() {
    if (isFinish.compareAndSet(false, true)) {
      write(current.get());
      outputWriter.write("\n");
    }
  }

  public void writer() {
    Thread t = new Thread() {
      public void run() {
        boolean isInterrupted = false;
        while (!isFinish.get() && !isInterrupted) {

          write(current.get());

          try {
            Thread.sleep(refreshRate);
          } catch (InterruptedException e) {
            isInterrupted = true;
          }
        }
      }
    };
    t.start();
  }

  private void write(long currentVal) {
    int width = 80;
    String percentBox = "", countersBox = "", timeLeftBox = "", speedBox = "", barBox = "", end =
        "", out = "";

    if (showPercent) {
      percentBox = String.format(" %#.02f%% ", (double) (currentVal * 100) / total);
    }

    if (showCounters) {
      if (total > 0) {
        countersBox =
            String.format("%s/%s ", formatter.format(currentVal), formatter.format(total));
      } else {
        countersBox = formatter.format(currentVal);
      }
    }

    if (showTimeLeft && currentVal > 0) {
      long fromStart = System.currentTimeMillis() - startTime;
      long perEntry = fromStart / currentVal;
      long left = (total - currentVal) * perEntry;
      if (left > 0) {
        timeLeftBox = Formatter.TIME.format(left);
      }
    }

    if (showSpeed && currentVal > 0) {
      long fromStart = (System.currentTimeMillis() - startTime) / 1000;
      double speed = (double) currentVal / fromStart;
      speedBox = formatter.format((long) speed) + "/s ";
    }

    if (showBar) {
      int size =
          width
              - (countersBox + format.barStart() + format.barEnd() + percentBox + timeLeftBox + speedBox)
                  .length();
      if (size > 0) {
        int curCount = (int) Math.ceil(((double) currentVal / total) * size);
        int emptyCount = size - curCount;
        barBox = String.valueOf(format.barStart());
        if (emptyCount < 0) {
          emptyCount = 0;
        }
        if (curCount > size) {
          curCount = size;
        }
        if (emptyCount <= 0) {
          barBox += repeat(format.current(), curCount);
        } else if (curCount > 0) {
          barBox += repeat(format.current(), curCount - 1) + format.currentN();
        }

        barBox += repeat(format.empty(), emptyCount) + format.barEnd();
      }
    }

    out = countersBox + barBox + percentBox + speedBox + timeLeftBox;
    if (out.length() < width) {
      end = repeat(' ', width - out.length());
    }

    out = countersBox + barBox + percentBox + speedBox + timeLeftBox;

    outputWriter.write("\r" + out + end);
  }


  private static String repeat(char c, int count) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < count; ++i) {
      sb.append(c);
    }
    return sb.toString();
  }

  public static Builder newBuilder() {
    return new BuilderImpl();
  }

  public enum ConsoleOutputWriter implements OutputWriter {

    INSTANCE;

    @Override
    public void write(String output) {
      System.out.print(output);
    }
  }

  public static final class BuilderImpl implements Builder {
    private boolean showPercent;
    private boolean showBar;
    private boolean showCounters;
    private boolean showTimeLeft;
    private boolean showSpeed;
    private Formatter formatter;
    private Format format;
    private int refreshRate;
    private long total;
    private OutputWriter outputWriter;

    public BuilderImpl() {
      showPercent = true;
      showBar = true;
      showCounters = true;
      showTimeLeft = true;
      formatter = Formatter.NO;
      format = DEFAULT_FORMAT;
      refreshRate = DEFAULT_REFRESH_RATE;
      outputWriter = ConsoleOutputWriter.INSTANCE;
    }

    @Override
    public Builder showPercent(boolean val) {
      showPercent = val;
      return this;
    }

    @Override
    public Builder showBar(boolean val) {
      showBar = val;
      return this;
    }

    @Override
    public Builder showCounters(boolean val) {
      showCounters = true;
      return this;
    }

    @Override
    public Builder showTimeLeft(boolean val) {
      showTimeLeft = true;
      return this;
    }

    @Override
    public Builder showSpeed(boolean val) {
      showSpeed = true;
      return this;
    }

    @Override
    public Builder refreshRate(int ms) {
      refreshRate = ms;
      return this;
    }

    @Override
    public Builder total(long total) {
      this.total = total;
      return this;
    }

    @Override
    public Builder formatter(Formatter formatter) {
      this.formatter = formatter;
      return this;
    }

    @Override
    public Builder format(Format format) {
      this.format = format;
      return this;
    }

    @Override
    public Builder outputWriter(OutputWriter outputWriter) {
      this.outputWriter = outputWriter;
      return this;
    }

    @Override
    public ProgressBar build() {
      return new ConsoleProgressBar(this);
    }

  }

}
