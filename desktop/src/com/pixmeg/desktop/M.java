package com.pixmeg.desktop;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import java.security.InvalidParameterException;

public class M {
//  private static int[][] cache13 = new int[1287][5]; //pre-cache13 all 1287 possible 5 elements set of a 13 elements set :)
//  private static int[][] cache8 = new int[56][5];
//  private static int[][] cache7 = new int[21][5];
  public static final ArrayMap<Integer, String> nameMap = new ArrayMap<>();
  private static int valueMask = Integer.parseInt("00001111111111111", 2);
  private static int typeMask = Integer.parseInt( "11110000000000000", 2);
  private static ArrayMap<Integer, Integer> dupMap = new ArrayMap<>();
  static {
//    preCache(13, cache13);
//    preCache(8, cache8);
//    preCache(7, cache7);
  }

  public static int check5(Array<Integer> cards) {
    if (cards.size != 5) throw new InvalidParameterException("not enough card");

    dupMap.clear();
    int color = cards.get(0);
    int value = cards.get(0);
    int maxDup = 0;

    Integer d = dupMap.get(cards.get(0)&valueMask);
    dupMap.put(cards.get(0)&valueMask, (d == null) ? 0 : d + 1);

    for (int i = 1; i < cards.size; i++) {
      color &= cards.get(i);
      value |= cards.get(i);

      Integer _d = dupMap.get(cards.get(i)&valueMask);
      dupMap.put(cards.get(i)&valueMask, (_d == null) ? 0 : _d + 1);
      _d = dupMap.get(cards.get(i)&valueMask);
      maxDup = maxDup < _d ? _d : maxDup;
    }
    int type = Integer.bitCount(color&typeMask)*5;
    value &= valueMask;
    switch (Integer.bitCount(value)){
      case 2:
        type += maxDup == 3 ? 7 : 6;
        break;
      case 3:
        type += maxDup == 2 ? 3 : 2;
        break;
      case 4:
        type += 1;
        break;
      case 5:
        if ((value>>12) == 1 && (value & 15) == 15){
          type += 4;
          value = 15;
          break;
        }
        while(value%2 == 0) value >>= 1;
        type += value == 31 ? 4 : 0;
        break;
      default:
        type += 0;
        break;
    }
    return (type<<13) + value;
  }

  public static int check5(int[] cards) {
    if (cards.length != 5) throw new InvalidParameterException("not enough card");
    dupMap.clear();
    int color = cards[0];
    int value = cards[0];
    int maxDup = 0;

    Integer d = dupMap.get(cards[0]&valueMask);
    dupMap.put(cards[0]&valueMask, (d == null) ? 0 : d + 1);

    for (int i = 1; i < cards.length; i++) {
      color &= cards[i];
      value |= cards[i];

      Integer _d = dupMap.get(cards[i]&valueMask);
      dupMap.put(cards[i]&valueMask, (_d == null) ? 0 : _d + 1);
      _d = dupMap.get(cards[i]&valueMask);
      maxDup = maxDup < _d ? _d : maxDup;
    }
    int type = Integer.bitCount(color&typeMask)*5;
    value &= valueMask;
    switch (Integer.bitCount(value)){
      case 2:
        type += maxDup == 3 ? 7 : 6;
        break;
      case 3:
        type += maxDup == 2 ? 3 : 2;
        break;
      case 4:
        type += 1;
        break;
      case 5:
        if ((value>>12) == 1 && (value & 15) == 15){
          type += 4;
          value = 15;
          break;
        }
        while(value%2 == 0) value >>= 1;
        type += value == 31 ? 4 : 0;
        break;
      default:
        type += 0;
        break;
    }
    return (type<<13) + value;
  }

//  public static int[][] move(int[] pattern) {
//    int[][] res = new int[3][];
//    int[] low = move135(pattern);
//    int[] remain8 = _trim(pattern, low);
//    int[] mid = move85(remain8);
//    int[] high = _trim(remain8, mid);
//    res[0] = low;
//    res[1] = mid;
//    res[2] = high;
//    return res;
//  }

  public static int compare5(int[] patternA, int[] patternB) {
    if (patternA.length != 5 || patternB.length != 5)
      throw new InvalidParameterException("not enough card");
    int rA = check5(patternA);
    int rB = check5(patternB);

    int tA = rA&typeMask;
    int tB = rB&typeMask;
    if (tA > tB) return 1;
    if (tA < tB) return -1;

    int vA = rA&valueMask;
    int vB = rB&valueMask;
    if (vA > vB) return 1;
    if (vA < vB) return -1;

    int sA = 0,sB = 0,aA = 0, aB = 0;
    for (int i = 0; i < 5; i++) {
      sA += patternA[i];
      sB += patternB[i];
      aA += (patternA[i]&typeMask)*(patternA[i]&valueMask);
      aB += (patternB[i]&typeMask)*(patternB[i]&valueMask);

    }
    if (sA > sB) return 1;
    if (sA < sB) return -1;
    if (aA > aB) return 1;
    if (aA < aB) return -1;

    for (int i = 0; i < 5; i++){
      System.out.print(nameMap.get(patternA[i]) + " ");
      System.out.print(nameMap.get(patternB[i]) + " ");
      System.out.println();
    }
    System.out.println();
    throw new InvalidParameterException("card duplicate");
  }

//  public static int compare13(int[][] m1, int[][] m2) {
//    //make sure you input the right thing
//    int c11,c12,c21,c22,c13,c23;
//    c11 = check5(m1[0]);c12 = check5(m2[0]);
//    c21 = check5(m1[1]);c22 = check5(m2[1]);
//    c13 = check5(m1[2]);c23 = check5(m2[2]);
//    int res = 0;
//    res += Integer.compare(c11,c12);
//    res += Integer.compare(c21,c22);
//    res += Integer.compare(c13,c23);
//    return res;
//  }

//  static int[] move135(int[] pattern) {
//    if (pattern.length != 13) throw new InvalidParameterException("pattern must have 13 elements");
//    return _move(pattern, 1287, cache13);
//  }

//  static int[] move85(int[] pattern) {
//    if (pattern.length != 8) throw new InvalidParameterException("pattern must have 8 elements");
//    return _move(pattern, 56, cache8);
//  }

//  static int[] move75(int[] pattern) {
//    if (pattern.length != 7) throw new InvalidParameterException("pattern must have 7 elements");
//    return _move(pattern, 21, cache7);
//  }

  public static Array<Integer> makeCards() {
    Array<Integer> res = new Array<>();
    nameMap.clear();
    for (int i = 0; i < 13; i++)
      for (int j = 0; j < 4; j++){
        int v = 1 << i;
        int c = (8 >> j) << 13;
        int card = c | v;

        String value = "";
        switch (i) {
          case 0: value = "2";break;
          case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: value = (i + 2) + ""; break;
          case 9: value = "J"; break;
          case 10: value = "Q"; break;
          case 11: value = "K"; break;
          case 12: value = "A"; break;
          default: break;
        }

        String color = "";
        switch (c >> 13) {
          case 8: color = "C"; break;
          case 4: color = "R"; break;
          case 2: color = "Ch"; break;
          case 1: color = "B"; break;
          default: break;
        }
        String cardName = value + color;
        nameMap.put(card, cardName);
        res.add(card);
      }
    res.shuffle();
    return res;
  }

  ////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  private static int _compare5(int[] patternA, int[] patternB, int rA, int rB) {
//    int tA = rA&typeMask;
//    int tB = rB&typeMask;
//    if (tA > tB) return 1;
//    if (tA < tB) return -1;
//
//    int vA = rA&valueMask;
//    int vB = rB&valueMask;
//    if (vA > vB) return 1;
//    if (vA < vB) return -1;
//
//    int sA = 0,sB = 0,aA = 0, aB = 0;
//    for (int i = 0; i < 5; i++) {
//      sA += patternA[i];
//      sB += patternB[i];
//      aA += (patternA[i]&typeMask)*(patternA[i]&valueMask);
//      aB += (patternB[i]&typeMask)*(patternB[i]&valueMask);
//
//    }
//    if (sA > sB) return 1;
//    if (sA < sB) return -1;
//    if (aA > aB) return 1;
//    if (aA < aB) return -1;
//
//    for (int i = 0; i < 5; i++){
//      System.out.print(nameMap.get(patternA[i]) + " ");
//      System.out.print(nameMap.get(patternB[i]) + " ");
//      System.out.println();
//    }
//    System.out.println();
//    throw new InvalidParameterException("card duplicate");
//  }
//
//  private static int _maxDup(int[] cards) {
//    dupMap.clear();
//    int maxDup = 0;
//
//    int key;
//    for (int card : cards) {
//      key = card & valueMask;
//      Integer _d = dupMap.get(key);
//      dupMap.put(key, (_d == null) ? 0 : _d + 1);
//      _d = dupMap.get(key);
//      maxDup = maxDup < _d ? _d : maxDup;
//    }
//    return maxDup;
//  }
//
//  private static int _check5(int[] cards, int color, int value) {
//    int type = (color&typeMask) == 0 ? 0 : 5;
//    value &= valueMask;
//    switch (Integer.bitCount(value)){
//      case 2:
//        type += _maxDup(cards) == 3 ? 7 : 6;
//        break;
//      case 3:
//        type += _maxDup(cards) == 2 ? 3 : 2;
//        break;
//      case 4:
//        type += 1;
//        break;
//      case 5:
//        if ((value>>12) == 1 && (value & 15) == 15){
//          type += 4;
//          value = 15;
//          break;
//        }
//        while(value%2 == 0) value >>= 1;
//        type += value == 31 ? 4 : 0;
//        break;
//      default:
//        type += 0;
//        break;
//    }
//    return (type<<13) + value;
//  }
//
//  private static int[] _move(int[] pattern, int subset, int[][] cache) {
//    int[] buffer = new int[5];
//    int[] res = new int[5];
//    int rA,rB, c, v;
//    for (int _i = 0; _i < 5; _i++) res[_i] = pattern[cache[0][_i]];
//    rA = check5(res);
//    for (int i = 1; i < subset; i++) {
//      buffer[0] = c = v = pattern[cache[i][0]];
//      for (int j = 1; j < 5; j++) {
//        buffer[j] = pattern[cache[i][j]];
//        c &= buffer[j];
//        v |= buffer[j];
//      }
//      rB = _check5(buffer, c, v);
//      if (rB == 0) continue;
//      if (_compare5(res, buffer, rA, rB) < 0) {
//        System.arraycopy(buffer, 0, res, 0, buffer.length);
//        rA = rB;
//      }
//    }
//    return res;
//  }
//
//  private static int[] _trim(int[] src, int[] cut) {
//    int[] res = new int[src.length - cut.length];
//    boolean dup = false;
//    int idx = 0;
//    for (int i2 : src) {
//      for (int i1 : cut) if (i2 == i1) dup = true;
//      if (!dup) res[idx++] = i2;
//      dup = false;
//    }
//
//    return res;
//  }
//
//  private static void preCache(int maxSize, int[][] cache) {
//    int step = 1<<maxSize;
//    int _i = 0;
//    for (int i = 0; i < step; i++) {
//      if (Integer.bitCount(i) == 5) {
//        int _j = 0;
//        for (int j = 0; j < 13; j++)
//          if ((i&(1<<j)) > 0)
//            cache[_i][_j++] = j;
//        _i++;
//      }
//    }
//  }
}