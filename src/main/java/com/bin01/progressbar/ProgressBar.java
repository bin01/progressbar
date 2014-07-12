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

public interface ProgressBar {

  public void start();

  public void increment();

  public void set(long current);

  public void add(long add);

  public void finish();

  public interface OutputWriter {

    public void write(String output);

  }

  public interface Builder {

    Builder showPercent(boolean val);

    Builder showBar(boolean val);

    Builder showCounters(boolean val);

    Builder showTimeLeft(boolean val);

    Builder showSpeed(boolean val);

    Builder formatter(Formatter formatter);

    Builder total(long total);

    Builder format(Format format);

    Builder refreshRate(int ms);

    Builder outputWriter(OutputWriter writer);

    ProgressBar build();

  }

}
