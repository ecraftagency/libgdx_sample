package com.pixmeg.desktop;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class MB {
  public static final ArrayMap<Integer, String> nameMap = new ArrayMap<>();
  private static int valueMask = Integer.parseInt("00001111111111111", 2);
  private static TreeMap<Integer, Array<Integer>> simMap = new TreeMap<>();
  private static TreeMap<Integer, Array<Integer>> rsimMap = new TreeMap<>(Collections.reverseOrder());
  private static Array<Array<Integer>> color = new Array<>();

  static {
    for (int i = 0; i < 4; i++) color.add(new Array<>());
  }

  public static void simplify(int[] pattern, TreeMap<Integer, Array<Integer>> base) {
    base.clear();
    for (int i : pattern)
      if (base.containsKey(i&valueMask))
        base.get(i&valueMask).add(i);
      else {
        Array<Integer> v = new Array<>();
        v.add(i);
        base.put(i&valueMask, v);
      }
  }

  public static int[] colorMove(int[] pattern) {
    simplify(pattern, simMap);
    for (Array<Integer> a : color) a.clear();
    for (Map.Entry<Integer, Array<Integer>> entry : simMap.entrySet())
      for (Integer i : entry.getValue()) {
        if ((i>>13) == 8) color.get(3).add(i);
        if ((i>>13) == 4) color.get(2).add(i);
        if ((i>>13) == 2) color.get(1).add(i);
        if ((i>>13) == 1) color.get(0).add(i);
      }
    Array<Integer> max = null;
    int maxV = 0;
    for (Array<Integer> a : color)
      if (a.size >= 5 && a.get(a.size - 1) > maxV){
          maxV = a.get(a.size - 1);
          max = a;
      }
    if (max != null) {
      int[] res = new int[5];
      for (int i = 0; i < 5; i++) res[i] = max.get(max.size - 1 - i);
      return res;
    }
    return null;
  }

  public static int[] consecutiveMove(int[] pattern) {
    simplify(pattern, rsimMap);
    int[] frame = new int[] {4096, 2048, 1024, 512, 256, 128, 64, 32, 16, 8, 4, 2, 1, 4096};
    int[] res = new int[5];
    Array<Map.Entry<Integer, Array<Integer>>> base = new Array<>();
    for (int i : frame) if (!rsimMap.containsKey(i)) rsimMap.put(i, null);
    for (Map.Entry<Integer, Array<Integer>> entry : rsimMap.entrySet()) base.add(entry);
    base.add(base.get(0));
    for (int i = 0; i < base.size - 5; i++) {
      boolean isConsecutive = true;
      int idx = 0;
      for (int j = i; j < 5; j++) {
        if (base.get(i + j).getValue() == null) {
          isConsecutive = false;
          break;
        }
        res[idx++] = base.get(i + j).getValue().get(0);
      }
      if (isConsecutive && idx == 5)
        return res;
    }
    return null;
  }

  public static int[][] move(int[] pattern){
    int[] low = _move(pattern);
    //int[] remain = _trim(pattern, low);
    //int[] mid = _move(remain);
    //int[] high = _trim(remain, mid);
    int[][] res = new int[3][];
    //res[0] = high;
    //res[1] = mid;
    res[2] = low;
    return res;
  }

  public static int[] _move(int[] pattern) {
    simplify(pattern, simMap);
    Map.Entry<Integer, Array<Integer>> maxDupEntry = (Map.Entry<Integer, Array<Integer>>)simMap.entrySet().toArray()[0];
    for (Map.Entry<Integer, Array<Integer>> entry : simMap.entrySet()) {
      Array<Integer> v = entry.getValue();
      if (v.size >= maxDupEntry.getValue().size)
        maxDupEntry = entry;
    }
    Array<Map.Entry<Integer, Array<Integer>>> group = new Array<>();
    switch (maxDupEntry.getValue().size) {
      case 1:
        if (pattern.length == 8) {
          Array<Map.Entry<Integer, Array<Integer>>> four = extractCustom(4); /////X
          if (four != null) {
            for (Map.Entry<Integer, Array<Integer>> a : four) group.insert(0, a);
            group.insert(0, maxDupEntry);
            return rawExtract(group);
          }
        }
        return new int[] {36864, 36864, 36864, 36864, 36864};//3 sanh
      case 2:
        int[] s = colorMove(pattern);
        if (s != null) return s;
        int[] c = consecutiveMove(pattern);
        if (c != null) return c;

        Map.Entry<Integer, Array<Integer>> pair = extractPairEx(maxDupEntry);
        Map.Entry<Integer, Array<Integer>> one = extractOne();
        if (pair != null && one != null) {
          group.insert(0, one);
          group.insert(0, pair);
          group.insert(0, maxDupEntry);
          return rawExtract(group);
        }
        Array<Map.Entry<Integer, Array<Integer>>> three = extractCustom(3);
        if (three != null){
          group.insert(0, three.get(0));
          group.insert(0, three.get(1));
          group.insert(0, three.get(2));
          group.insert(0, maxDupEntry);
          return rawExtract(group);
        }//doi
        break;
      case 3:
        pair = extractPair(); ////////CU`
        if (pair != null) {
          group.insert(0, pair);
          group.insert(0, maxDupEntry);
          return rawExtract(group);
        }

        int[] ss = colorMove(pattern);
        if (ss != null) return ss;

        int[] cc = consecutiveMove(pattern); ////////Sanh
        if (cc != null) return cc;

        Array<Map.Entry<Integer, Array<Integer>>> two = extractCustom(2); /////X
        if (two != null) {
          group.insert(0, two.get(0));
          group.insert(0, two.get(1));
          group.insert(0, maxDupEntry);
          return rawExtract(group);
        }
        break;
      case 4: // tu quy
        Map.Entry<Integer, Array<Integer>> o = extractOne();
        if (o != null) {
          group.insert(0, o);
          group.insert(0, maxDupEntry);
          return rawExtract(group);
        }
        Map.Entry<Integer, Array<Integer>> p = extractPair();
        if (p != null) {
          p.getValue().removeIndex(0);
          group.insert(0, p);
          group.insert(0, maxDupEntry);
          return rawExtract(group);
        }
        break;
      default:
        break;
    }
    return null;
  }

  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private static Map.Entry<Integer, Array<Integer>> extractPair() {
    for (Map.Entry<Integer, Array<Integer>> entry : simMap.entrySet())
      if (entry.getValue().size == 2) return entry;
    return null;
  }

  private static Map.Entry<Integer, Array<Integer>> extractPairEx(Map.Entry<Integer, Array<Integer>> exc) {
    for (Map.Entry<Integer, Array<Integer>> entry : simMap.entrySet())
      if (!entry.equals(exc) && entry.getValue().size == 2)
        return entry;
    return null;
  }

  private static Map.Entry<Integer, Array<Integer>> extractOne() {
    for (Map.Entry<Integer, Array<Integer>> entry : simMap.entrySet())
      if (entry.getValue().size == 1) return entry;
    return null;
  }

  private static Array<Map.Entry<Integer, Array<Integer>>> extractCustom(int count) {
    Array<Map.Entry<Integer, Array<Integer>>> res = new Array<>();
    for (Map.Entry<Integer, Array<Integer>> entry : simMap.entrySet())
      if (entry.getValue().size == 1) {
        res.add(entry);
        if (res.size == count) return res;
      }
    return null;
  }

  private static int[] rawExtract(Array<Map.Entry<Integer, Array<Integer>>> a) {
    int[] res = new int[5];
    int idx = 0;
    for (Map.Entry<Integer, Array<Integer>> e : a) {
      Array<Integer> v = e.getValue();
      for (int c : v) res[idx++] = c;
    }
    return res;
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
    res.shuffle();
    return res;
  }

  private static int[] _trim(int[] src, int[] cut) {
    int[] res = new int[src.length - cut.length];
    boolean dup = false;
    int idx = 0;
    for (int i2 : src) {
      for (int i1 : cut) if (i2 == i1) dup = true;
      if (!dup) res[idx++] = i2;
      dup = false;
    }

    return res;
  }
}