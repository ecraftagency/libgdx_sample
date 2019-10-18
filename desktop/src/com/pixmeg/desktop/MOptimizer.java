package com.pixmeg.desktop;
import com.badlogic.gdx.utils.Array;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class MOptimizer {
  private static int valueMask = Integer.parseInt("00001111111111111", 2);
  private static HashMap<Integer, Integer> baseMap = new HashMap<>();
  private static ValueComparator bvc = new ValueComparator(baseMap);
  private static TreeMap<Integer, Integer> vSortMap = new TreeMap<>(bvc);

  private static class ValueComparator implements Comparator<Integer> {
    Map<Integer, Integer> base;

    ValueComparator(Map<Integer, Integer> base) {
      this.base = base;
    }

    public int compare(Integer a, Integer b) {
      if (base.get(a) > base.get(b)) return -1;
      if (base.get(a) < base.get(b)) return 1;
      if (a > b) return -1;
      else return 1;
    }
  }

  private static class Pair<T> {
    T dup, card;
    Pair(T dup, T card) {
      this.dup = dup;
      this.card = card;
    }

    @Override
    public String toString() {
      return dup.toString() + " " + card.toString();
    }
  }

  static Array<Pair<Integer>> divided(int[] move) {
    baseMap.clear();vSortMap.clear();
    baseMap.clear(); vSortMap.clear();
    for (int card : move) {
      Integer d = baseMap.get(card&valueMask);
      baseMap.put(card&valueMask, d == null ? 1 : d + 1);
    }
    vSortMap.putAll(baseMap);
    Array<Pair<Integer>> res = new Array<>();
    for (Map.Entry<Integer, Integer> entry : vSortMap.entrySet())
      res.add(new Pair<>(entry.getValue(), entry.getKey()));
    return res;
  }

  public static int[][] beautify(int[] pattern, int[][] move) {
    Array<Pair<Integer>> lows = divided(move[0]);
    Array<Pair<Integer>> mids = divided(move[1]);
    int low = M.check5(move[0])>>13;
    int mid = M.check5(move[1])>>13;
    int[] rlow = new int[5];
    int[] rmid = new int[5];
    //order of evaluation ---> stop as soon as the true/falsehood was found
    if (low <= 6 && low != 5 && low != 4 && mid <= 6 && mid != 5 && mid != 4 && lows.get(1).dup == mids.get(low != mid ? 0 : 1).dup) {
      Pair<Integer> temp = new Pair<>(lows.get(1).dup, lows.get(1).card);
      lows.get(1).dup = mids.get(low != mid ? 0 : 1).dup;
      lows.get(1).card = mids.get(low != mid ? 0 : 1).card;
      mids.get(low != mid ? 0 : 1).dup = temp.dup; mids.get(low != mid ? 0 : 1).card = temp.card;

      int[] c = new int[pattern.length];
      System.arraycopy(pattern, 0, c, 0, pattern.length);
      sync(c, lows, rlow);
      sync(c, mids, rmid);
      move[0] = rlow; move[1] = rmid;
    }
    return move;
  }

  private static void sync(int[] pattern, Array<Pair<Integer>> optimove, int[] move) {
    int idx = 0;
    for (Pair<Integer> pair : optimove) {
      for (int i = 0; i < pattern.length; i++)
        for (int j = 0; j < pair.dup; j++)
          if (pair.card == (pattern[i] & valueMask) && idx < 5) {
            move[idx++] = pattern[i];
            pattern[i] = 0;
          }
    }
  }
}