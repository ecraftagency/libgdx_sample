package com.games.k13;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

@SuppressWarnings("deprecation")
public class c {
  private static final int vMask = 0b11111111111110000;
  //private static final int cMask = 0b1111;
  private static final ArrayMap<Integer, String> name;

  static {
    name = new ArrayMap<>();
  }

  static Integer card(String card) {
    return name.getKey(card, false);
  }

  static String card(Integer card) {
    return name.get(card);
  }

  static Array<Integer> makeCard() {
    Array<Integer> res = new Array<>();
    for (int i = 0; i < 13; i++)
      for (int j = 0; j < 4; j++) {
        int card = ((1 << i) << 4) | (8 >> j);
        String v = "";
        switch (i) {
          case 0:case 1:case 2:case 3:case 4:case 5:case 6:case 7: v = (i + 3) + "";break;
          case 8:   v = "J";break;
          case 9:   v = "Q";break;
          case 10:  v = "K";break;
          case 11:  v = "A";break;
          case 12:  v = "2";break;
          default:          break;
        }
        String c = "";
        switch (8 >> j) {
          case 8: c = "C";  break;
          case 4: c = "R";  break;
          case 2: c = "Ch"; break;
          case 1: c = "B";  break;
          default:          break;
        }
        name.put(card, v + c);
        res.add(card);
      }
    return res;
  }

  static boolean isConsecutive(Array<Integer> pattern) {
    int z = 0;
    for (Integer card : pattern) z |= (card&vMask)>>4;
    if (z >= 0b1000000000000) return false;
    while(z%2 == 0) z >>= 1;
    return z == ((1<<pattern.size) - 1);
  }

  static boolean isPair(Array<Integer> pattern) {
    return pattern.size == 2 && ((pattern.get(0)&vMask) == (pattern.get(1)&vMask));
  }

  static boolean isTri(Array<Integer> pattern) {
    return false;
  }
}