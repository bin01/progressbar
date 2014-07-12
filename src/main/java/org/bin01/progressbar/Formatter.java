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

public enum Formatter {

  NO {
    @Override
    public String format(long n) {

      return String.valueOf(n);
    }
  },

  BYTES {
    @Override
    public String format(long n) {
      String result;
      long tb = 1024 * 1024 * 1024 * 1024l;
      long gb = 1024 * 1024 * 1024l;
      long mb = 1024 * 1024l;
      long kb = 1024l;
      if (n > tb) {
        result = String.format("%.02f TB", (double) n / tb);
      } else if (n > gb) {
        result = String.format("%.02f GB", (double) n / gb);
      } else if (n > mb) {
        result = String.format("%.02f MB", (double) n / mb);
      } else if (n > kb) {
        result = String.format("%.02f KB", (double) n / kb);
      } else {
        result = String.format("%d B", n);
      }
      return result;
    }
  },

  TIME {
    @Override
    public String format(long n) {
      StringBuilder result = new StringBuilder();
      long sec = 1000;
      long min = 60 * sec;
      long hour = 60 * min;
      long day = 24 * hour;
      long year = 365 * day;
      if (n > year) {
        result.append(n / year).append("y").append(format(n % year));
      } else if (n > day) {
        result.append(n / day).append("d").append(format(n % day));
      } else if (n > hour) {
        result.append(n / hour).append("h").append(format(n % hour));
      } else if (n > min) {
        result.append(n / min).append("m").append(format(n % min));
      } else if (n > sec) {
        result.append(n / sec).append("s").append(format(n % sec));
      }
      return result.toString();
    };
  };

  public abstract String format(long n);

}
