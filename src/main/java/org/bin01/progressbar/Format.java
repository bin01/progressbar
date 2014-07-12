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
package org.bin01.progressbar;

public class Format {
  private char barStart;
  private char barEnd;
  private char empty;
  private char current;
  private char currentN;

  public Format(char barStart, char barEnd, char empty, char current, char currentN) {
    this.barStart = barStart;
    this.barEnd = barEnd;
    this.empty = empty;
    this.current = current;
    this.currentN = currentN;
  }

  public char barStart() {
    return barStart;
  }

  public char barEnd() {
    return barEnd;
  }

  public char empty() {
    return empty;
  }

  public char current() {
    return current;
  }

  public char currentN() {
    return currentN;
  }

  public static Format of(String format) {
    char[] c = format.toCharArray();
    if (c.length < 5 || c.length > 5) {
      throw new IllegalArgumentException("Invalid format " + format);
    }
    return new Format(c[0], c[4], c[3], c[1], c[2]);
  }

}
