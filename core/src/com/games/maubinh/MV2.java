package com.games.maubinh;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class MV2 {
  public static final ArrayMap<Integer, String> nameMap = new ArrayMap<>();
  public static final int valueMask = Integer.parseInt("00001111111111111", 2);
  public static final int typeMask = Integer.parseInt( "11110000000000000", 2);
  private static TreeMap<Integer, Array<Integer>> simMap;
  private static TreeMap<Integer, Array<Integer>> rsimMap = new TreeMap<>(Collections.reverseOrder());
  private static Array<Array<Integer>> color = new Array<>();
  private static ArrayMap<Integer, Integer> dupMap = new ArrayMap<>();
  private static CheckInterface[] checkInterfaces;

  static {
    for (int i = 0; i < 4; i++) color.add(new Array<>());
    simMap = new TreeMap<>();

    checkInterfaces = new CheckInterface[6];
    checkInterfaces[3] = (Array<Integer> cards) -> {
      if (cards.size != 3) throw new InvalidParameterException("not enough card");
      int value = cards.get(0);
      for (int i = 1; i < cards.size; i++)
        value |= cards.get(i);

      value &= valueMask;
      int type = 0;
      switch (Integer.bitCount(value)){
        case 1: //xam
          type = 3;
          break;
        case 2: //doi
          type  = 1;
          break;
        case 3:
          if ((value>>12) == 1 && (value & 3) == 3) {
            type = 4;
            value = 3;
            break;
          }
          while(value%2 == 0) value >>= 1;
          type += value == 7 ? 4 : 0;
          break;
        default:
          break;
      }
      return (type<<13) + value;
    };
    checkInterfaces[5] = (Array<Integer> cards) -> {
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
        case 2://aaabb, aaaab
          type += maxDup == 3 ? 7 : 6;
          break;
        case 3://aaabc aabbc
          type += type == 5 ? 0 : maxDup == 2 ? 3 : 2;
          break;
        case 4://abcdd
          type += type == 5 ? 0 : 1;
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
    };
  }

  private static Array<Integer> pairMove(Array<Integer> pattern, Array<Boolean> mask) {
    Map.Entry<Integer, Array<Integer>> max = simplify(pattern, simMap, false);
    if (max.getValue().size == 3) { //cù xám
      if (mask.get(0)) {
        Array<Integer> pair = getPair(max);
        if (pair != null) {
          max.getValue().addAll(pair);
          return max.getValue();
        }
      }

      Array<Integer> cMove = cMove(pattern, mask);
      if (cMove != null) return cMove;

      Array<Integer> two = getCustom(max, 2);
      max.getValue().addAll(two);
      return max.getValue();
    }
    if (max.getValue().size == 4) { //tứ quý
      Array<Integer> one = getCustom(max, 1);
      max.getValue().addAll(one);
      return max.getValue();
    }
    if (max.getValue().size == 2) { //chỉ có đôi, check sảnh thùng trước
      Array<Integer> cMove = cMove(pattern, mask);
      if (cMove != null) return cMove;

      Array<Integer> pair = getPair(max);
      Array<Integer> one = getCustom(max, 1);
      if (pair != null && one != null) {
        max.getValue().addAll(pair);
        max.getValue().addAll(one);
        return max.getValue();
      }

      Array<Integer> three = getCustom(max,3);
      max.getValue().addAll(three);
      return max.getValue();
    }
    if (max.getValue().size == 1) { //bài không có một đôi nào, (chi giữa, chi trên)
      Array<Integer> cMove = cMove(pattern, mask);
      if (cMove != null) return cMove;

      Array<Integer> four = getCustom(max, 4);
      if (four != null) {
        max.getValue().addAll(four);
        return max.getValue();
      }
    }
    throw new RuntimeException("cant not");
  }

  private static Array<Integer> getPair(Map.Entry<Integer, Array<Integer>> exc) {
    for (int s = 2; s <= 3; s++)
      for (Map.Entry<Integer, Array<Integer>> entry : simMap.entrySet())
        if (!entry.equals(exc) && entry.getValue().size == s){
          Array<Integer> r = new Array<>();
          for (int i = 0; i < 2; i++) r.add(entry.getValue().get(i));
          return r;
        }
    return null;
  }

  private static Array<Integer> colorMove(Array<Integer> pattern) {
    simplify(pattern, simMap, true);
    Array<Integer> max = null;
    int maxV = 0;
    for (Array<Integer> a : color)
      if (a.size >= 5 && a.get(a.size - 1) > maxV){
        maxV = a.get(a.size - 1);
        max = a;
      }
    if (max != null) {
      Array<Integer> res = new Array<>();
      for (int i = 0; i < 5; i++) res.add(max.get(max.size - 1 - i));
      return res;
    }
    return null;
  }

  private static Array<Integer> consecutiveMove(Array<Integer> pattern) {
    simplify(pattern, rsimMap, false);
    int[] frame = new int[] {4096, 2048, 1024, 512, 256, 128, 64, 32, 16, 8, 4, 2, 1, 4096};
    Array<Integer> res = new Array<>();
    Array<Map.Entry<Integer, Array<Integer>>> base = new Array<>();
    for (int i : frame) if (!rsimMap.containsKey(i)) rsimMap.put(i, null);
    for (Map.Entry<Integer, Array<Integer>> entry : rsimMap.entrySet()) base.add(entry);
    base.add(base.get(0));
    for (int i = 0; i < base.size - 5; i++) {
      boolean isConsecutive = true;
      for (int j = 0; j < 5; j++) {
        if (base.get(i + j).getValue() == null) {
          isConsecutive = false;
          break;
        }
        res.add(base.get(i + j).getValue().get(0));
      }
      if (isConsecutive && res.size == 5)
        return res;
      res.clear();
    }
    return null;
  }

  private static Array<Integer> cMove(Array<Integer> pattern, Array<Boolean> mask) {
    if (mask.get(1)) {
      Array<Integer> colorM = colorMove(pattern);
      if (colorM != null) return colorM;
    }

    Array<Integer> conseM = consecutiveMove((pattern));
    if (conseM != null) return  conseM;
    return null;
  }

  @SuppressWarnings("unchecked")
  private static Map.Entry<Integer, Array<Integer>> simplify(Array<Integer> pattern, TreeMap<Integer, Array<Integer>> base, boolean isColorize) {
    base.clear();
    for (int i : pattern)
      if (base.containsKey(i&valueMask))
        base.get(i&valueMask).add(i);
      else {
        Array<Integer> v = new Array<>();
        v.add(i);
        base.put(i&valueMask, v);
      }
    //Custom comparator implement is suuuuper slow, down know why!!
    Map.Entry<Integer, Array<Integer>> r = (Map.Entry<Integer, Array<Integer>>)simMap.entrySet().toArray()[0];
    for (Map.Entry<Integer, Array<Integer>> entry : simMap.entrySet())
      if (entry.getValue().size >= r.getValue().size)
        r = entry;
    if (isColorize) colorize();
    return r;
  }

  private static void colorize() {
    for (Array<Integer> a : color) a.clear();
    for (Map.Entry<Integer, Array<Integer>> entry : simMap.entrySet())
      for (Integer i : entry.getValue()) {
        if ((i>>13) == 8) color.get(3).add(i);
        if ((i>>13) == 4) color.get(2).add(i);
        if ((i>>13) == 2) color.get(1).add(i);
        if ((i>>13) == 1) color.get(0).add(i);
      }
  }

  private static Array<Integer> getCustom(Map.Entry<Integer, Array<Integer>> exc, int count) {
    Array<Integer> r = new Array<>();
    for (Map.Entry<Integer, Array<Integer>> entry : simMap.entrySet()) {
      if (!entry.equals(exc) && entry.getValue().size == 1) r.add(entry.getValue().get(0));
      if (r.size == count) return r;
    }

    r.clear();
    for (Map.Entry<Integer, Array<Integer>> entry : simMap.entrySet()) {
      if (!entry.equals(exc) && entry.getValue().size == 2) r.add(entry.getValue().get(0));
      if (r.size == count) return r;
    }

    r.clear();
    for (Map.Entry<Integer, Array<Integer>> entry : simMap.entrySet()) {
      if (!entry.equals(exc) && entry.getValue().size == 3) r.add(entry.getValue().get(0));
      if (r.size == count) return r;
    }
    throw  new RuntimeException("cant get " + count + " remain");
  }

  private static boolean isTripleConsecutive(Array<Integer> pattern) {
    Array<Integer> cpy = new Array<>();
    for (Integer c : pattern) cpy.add(c);
    Array<Integer> m = consecutiveMove(cpy);
    if (m != null){
      //for (Integer r : m) System.out.print(nameMap.get(r) + " ");
      cpy.removeAll(m, false);
      m = consecutiveMove(cpy);
      if (m != null) {
        //for (Integer r : m) System.out.print(nameMap.get(r) + " ");
        cpy.removeAll(m, false);
        //for (Integer r : cpy) System.out.print(nameMap.get(r) + " ");
        return (check(cpy) >> 13) == 4;
      }
    }
    return false;
  }

  public static int isSuperSaiyan(Array<Integer> pattern) {
    simplify(pattern, simMap, true);
    int zColor = pattern.get(0)&typeMask, zValue = pattern.get(0)&valueMask;
    for (int i = 1; i < pattern.size; i++) {
      zColor &= pattern.get(i)&typeMask;
      zValue |= pattern.get(i)&valueMask;
    }
    color.sort((a, b) -> Integer.compare(a.size, b.size));


    if (zValue == 0x1FFF && zColor != 0)//sanh cuon
      return 15;
    if (zValue == 0x1FFF)//sanh rong
      return 14;

    int c = 0;//3 thung
    for (int i = 0; i < color.size; i++)
      c = c*10 + color.get(i).size;
    if (c == 355 || c == 58 || c == 40)
      return 11;

    if (isTripleConsecutive(pattern))
      return 10;
    return 0;
  }

  public static Array<Integer> makeCards() {
    Array<Integer> res = new Array<>();
    nameMap.clear();
    for (int i = 0; i < 13; i++)
      for (int j = 0; j < 4; j++){
        int v = 1 << i;
        int c = (8>>j) << 13;
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
    //res.shuffle();
    return res;
  }

  public static Array<Array<Integer>> move(Array<Integer> pattern) {
    Array<Boolean> skipMask = new Array<>();
    skipMask.add(true);skipMask.add(true);
    Array<Integer> cpy = new Array<>();
    for (Integer i : pattern) cpy.add(i);

    Array<Integer> low = pairMove(pattern, skipMask);
    pattern.removeAll(low, false);
    Array<Integer> mid = pairMove(pattern, skipMask);
    pattern.removeAll(mid, false);
    Array<Array<Integer>> res = new Array<>();

    res.add(pattern);
    res.add(mid);
    res.add(low);

    if (check(mid)>>13 == 0) {
      skipMask.clear();
      skipMask.add(true);skipMask.add(false);
      low = pairMove(cpy, skipMask);
      cpy.removeAll(low, false);
      skipMask.clear();skipMask.add(true);skipMask.add(true);
      mid = pairMove(cpy, skipMask);
      cpy.removeAll(mid, false);
      res.clear();
      res.add(cpy);
      res.add(mid);
      res.add(low);
    }

    return res;
  }

//  public static int check3(Array<Integer> cards) {
//    if (cards.size != 3) throw new InvalidParameterException("not enough card");
//    int value = cards.get(0);
//    for (int i = 1; i < cards.size; i++)
//      value |= cards.get(i);
//
//    value &= valueMask;
//    switch (Integer.bitCount(value)){
//      case 1:
//        return 3;
//      case 2:
//        return 1;
//      case 3:
//        if ((value>>12) == 1 && (value & 3) == 3){
//          return 4;
//        }
//        while(value%2 == 0) value >>= 1;
//        return value == 7 ? 4 : 0;
//      default:
//        return 0;
//    }
//  }
//
//  public static int check5(Array<Integer> cards) {
//    if (cards.size != 5) throw new InvalidParameterException("not enough card");
//
//    dupMap.clear();
//    int color = cards.get(0);
//    int value = cards.get(0);
//    int maxDup = 0;
//
//    Integer d = dupMap.get(cards.get(0)&valueMask);
//    dupMap.put(cards.get(0)&valueMask, (d == null) ? 0 : d + 1);
//
//    for (int i = 1; i < cards.size; i++) {
//      color &= cards.get(i);
//      value |= cards.get(i);
//
//      Integer _d = dupMap.get(cards.get(i)&valueMask);
//      dupMap.put(cards.get(i)&valueMask, (_d == null) ? 0 : _d + 1);
//      _d = dupMap.get(cards.get(i)&valueMask);
//      maxDup = maxDup < _d ? _d : maxDup;
//    }
//    int type = Integer.bitCount(color&typeMask)*5;
//    value &= valueMask;
//    switch (Integer.bitCount(value)){
//      case 2://aaabb, aaaab
//        type += maxDup == 3 ? 7 : 6;
//        break;
//      case 3://aaabc aabbc
//        type += type == 5 ? 0 : maxDup == 2 ? 3 : 2;
//        break;
//      case 4://abcdd
//        type += type == 5 ? 0 : 1;
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

  public static int check(Array<Integer> cards) {
    return checkInterfaces[cards.size].check(cards);
  }

  @FunctionalInterface
  private interface CheckInterface {
    int check(Array<Integer> cards);
  }
}