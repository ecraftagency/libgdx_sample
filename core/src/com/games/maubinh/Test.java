package com.games.maubinh;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

public class Test {
  private static ArrayMap<Integer, String> m = new ArrayMap<>();
  static {
    m.put(0, "Mau Thau");//
    m.put(1, "Dzach");//
    m.put(2, "Thu");//
    m.put(3, "Xam");//
    m.put(4, "Sanh");
    m.put(5, "Thung");
    m.put(6, "Cu");//
    m.put(7, "Tu Quy");
    m.put(8, "Undefine");
    m.put(9, "Thung Pha Sanh");
  }

  public static void main(String[] args){
    MV2.makeCards();

    //Fix lỗi binh ngu chi giữa, tức là có sảnh mà ko ra sảnh
    move("AB", "9B", "8B", "7B", "2B", "5R", "6C", "7Ch", "8Ch", "9Ch", "10Ch", "QR", "KC");
    move("AC", "JC", "5C", "4C", "3C", "2Ch", "3B", "4B", "5Ch", "6Ch", "10R", "JR", "KR");

    //cây này bài cũ dồn hêt vô cái thùng A 9 6 4 3 rô, 2 chi trên mậu thầu không đều bài
    //fix ở đây rã thùng ra bình thành 2 đường sảnh
    move("AR", "9R", "6R", "4R", "3R", "KB", "3Ch", "5B", "6B", "7C", "JB", "10C", "QCh");

    //binh nhu cu
    move("ACh", "KCh", "QB", "JCh", "10B", "8R", "8C", "2C", "2R", "4Ch", "9C", "7R", "QC");
  }

  private static void move(String ...cards) {
    System.out.println();
    Array<Integer> pattern = makeCards(cards);
    Array<Array<Integer>> moves = MV2.move(pattern);
    printMove(moves);
  }

  private static Array<Integer> makeCards(String ...cards) {
    Array<Integer> res = new Array<>();
    for (String card : cards)
      res.add(MV2.nameMap.getKey(card, false));
    return res;
  }

  private static void printMove(Array<Array<Integer>> moves) {
    for (Array<Integer> move : moves) {
      for (Integer card : move)
        System.out.print(MV2.nameMap.get(card) + " ");
      int c = move.size == 3 ? MV2.check3(move)>>13 : MV2.check5(move)>>13;
      System.out.println(m.get(c));
    }
  }
}