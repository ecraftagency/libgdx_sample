package com.pixmeg.desktop;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

import java.security.InvalidParameterException;

public class P {
  public static final ArrayMap<Integer, String> nameMap = new ArrayMap<>();
  private static int valueMask = Integer.parseInt("00001111111111111", 2);
  private static int typeMask = Integer.parseInt( "11110000000000000", 2);
  private static ArrayMap<Integer, Integer> dupMap = new ArrayMap<>();
  private static CheckInterface[] checkInterfaces;
  public static ArrayMap<Integer, String> m = new ArrayMap<>();

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
    checkInterfaces = new CheckInterface[6];
    checkInterfaces[2] = (Array<Integer> cards) -> {
      if (cards.size != 2) throw new InvalidParameterException("not enough card");
      int value = cards.get(0);
      for (int i = 1; i < cards.size; i++)
        value |= cards.get(i);

      value &= valueMask;
      int type = 0;
      if (Integer.bitCount(value) == 1) type = 1;
      return (type<<13) + value;
    };
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
    };
  }

  /**
   * Tìm ra 5 lá bài có nước binh lớn nhất trong 5 (lá trên bàn) + 2 (lá trên tay)
   * với điều kiện kết quả phải có ít nhất 1 lá đến từ 2 lá trên tay
   * không áp dung với các trường hợp binh 2,3,4 lá.
   * @param deck 5 lá trên bàn
   * @param hand 2 lá trên tay
   * @return 5 lá lớn nhất theo luật nêu trên
   * @exception InvalidParameterException deck không đúng 5 lá bài
   */
  public static Array<Integer> move(Array<Integer> deck, Array<Integer> hand) {
    Array<Array<Integer>> sub3 = subset(deck, 3);
    Array<Array<Integer>> sub4 = subset(deck, 4);
    Array<Array<Integer>> sub44 = subset(deck, 4);

    for (Array<Integer> s : sub4) s.add(hand.get(0));
    for (Array<Integer> s : sub44) s.add(hand.get(1));

    for (Array<Integer> s : sub3)
      for (int i : hand) s.add(i);

    for (Array<Integer> s : sub3) sub4.add(s);
    for (Array<Integer> s : sub4) sub44.add(s);

    return findBiggest(sub44);
  }

  /**
   * Trả về 5 lá có nước binh lớn nhất trong danh sách các Array có 5 phần tử
   * @param arg danh sách các Array<Integer> (a1,a2,a3,....,aN) a kiểu Array<Integer>
   * @return phần tử có nước binh cao nhất
   * @exception InvalidParameterException nếu bất kì trong (a1,a2,a2...aN)
   * không đủ 5 phần tử
   */
  @SafeVarargs
  public static Array<Integer> findBiggest(Array<Integer> ...arg) {
    Array<Integer> result = null;
    Array<Array<Integer>> cardPacks = new Array<>();
    for (Array<Integer> a : arg) cardPacks.add(a);
    cardPacks.sort((c1, c2) -> compare(c2, c1));
    result = cardPacks.get(0);
    return result;
  }

  /**
   * Tương tu trên, thay varargs bằng Array<Array<int>>
   */
  public static Array<Integer> findBiggest(Array<Array<Integer>> cardPacks) {
    Array<Integer> result = null;
    cardPacks.sort((c1, c2) -> compare(c2, c1));
    result = cardPacks.get(0);
    return result;
  }

  /**
   * trả về nước binh lớn nhất có thể binh được trong 5 lá
   * @param cards 5 (đúng 5) lá cần kiểm tra nước binh
   * @return [0000][0000000000000]:int
   * 4 bit đầu (return>>13):
   * [0 - Mậu thầu, 1 - Dzách, 2 - Thú, 3 - Xám , 4 - Sảnh,
   * 5 Thùng, 6 - Cù, 7 - Tứ Quý, 9 - Thùng Phá Sảnh]
   * 13 bit sau: không cần quan tâm
   * @exception InvalidParameterException input không đủ 5 lá bài
   */
  public static int check5(Array<Integer> cards) {
    return checkInterfaces[cards.size].check(cards);
  }

  /**
   check tổng quát cho n lá bài trong đó n = {2,3,5}
   @return [a][b]int, trong đó a = {0,1,2,3,4,5,6,7,9} như trên
   b là 13 bit đầu trong số nguyên , a la 4 bit tiếp theo
   @exception InvalidParameterException nếu n != {2,3,5}
   */
  public static int check(Array<Integer> cards) {
    return checkInterfaces[cards.size].check(cards);
  }

  /**
   * so sánh 5 vs 5 theo thứ tự:
   * nước binh vs nước binh
   * giá trị vs giá trị
   * @return {1, -1} (không có TH 0)
   * @exception InvalidParameterException 1 trong 2 tham số không đủ 5 lá bài
   * @exception InvalidParameterException 2 bộ (5 lá) bài giống nhau
   */
  public static int compare5(Array<Integer> patternA, Array<Integer> patternB) {
    if (patternA.size != 5 || patternB.size != 5)
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
      sA += patternA.get(i);
      sB += patternB.get(i);
      aA += (patternA.get(i)&typeMask)*(patternA.get(i)&valueMask);
      aB += (patternB.get(i)&typeMask)*(patternB.get(i)&valueMask);

    }
    if (sA > sB) return 1;
    if (sA < sB) return -1;
    if (aA > aB) return 1;
    if (aA < aB) return -1;

    for (int i = 0; i < 5; i++){
      System.out.print(nameMap.get(patternA.get(i)) + " ");
      System.out.print(nameMap.get(patternB.get(i)) + " ");
      System.out.println();
    }
    System.out.println();
    throw new InvalidParameterException("card duplicate");
  }

  /**
  Tương tự compare5, áp dụng tổng quát cho TH 2,3,5. Ngoài ra sẽ exception
   */
  public static int compare(Array<Integer> patternA, Array<Integer> patternB) {
    int rA = check(patternA);
    int rB = check(patternB);

    int tA = rA&typeMask;
    int tB = rB&typeMask;
    if (tA > tB) return 1;
    if (tA < tB) return -1;

//    int vA = rA&valueMask;
//    int vB = rB&valueMask;
//    if (vA > vB) return 1;
//    if (vA < vB) return -1;

    int sA = 0,sB = 0,aA = 0, aB = 0;
    for (int i = 0; i < patternA.size; i++) {
      sA += patternA.get(i);
      sB += patternB.get(i);
      aA += (patternA.get(i)&typeMask)*(patternA.get(i)&valueMask);
      aB += (patternB.get(i)&typeMask)*(patternB.get(i)&valueMask);

    }
    if (sA > sB) return 1;
    if (sA < sB) return -1;
    if (aA > aB) return 1;
    if (aA < aB) return -1;

    for (int i = 0; i < patternA.size; i++){
      System.out.print(nameMap.get(patternA.get(i)) + " ");
      System.out.print(nameMap.get(patternB.get(i)) + " ");
      System.out.println();
    }
    System.out.println();
    throw new InvalidParameterException("card duplicate");
  }

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

  private static Array<Array<Integer>> subset(Array<Integer> cards, int subECount) {
    Array<Array<Integer>> res = new Array<>();
    int step = 1<<cards.size;
    for (int i = 0; i < step; i++)
      if (Integer.bitCount(i) == subECount) {
        Array<Integer> r = new Array<>();
        for (int j = 0; j < cards.size; j++) {
          if ((i & (1 << j)) > 0) {
            r.add(cards.get(j));
          }
        }
        res.add(r);
      }
    return res;
  }

  @FunctionalInterface
  private interface CheckInterface {
    int check(Array<Integer> cards);
  }

  public static void test(int count) {
    Array<Integer> cards = P.makeCards(); //tao bai
    for (int s = 0; s < 100; s++) {
      Array<Integer> a2 = new Array<>();
      Array<Integer> b2 = new Array<>();
      for (int i = 0; i < count; i++) a2.add(cards.get(i));
      for (int i = 0; i < count; i++) b2.add(cards.get(count + i));
      for (Integer i : a2) System.out.print(P.nameMap.get(i) + " ");
      System.out.println(m.get(P.check(a2)>>13));

      for (Integer i : b2) System.out.print(P.nameMap.get(i) + " ");
      System.out.println(m.get(P.check(b2)>>13));

      System.out.println(P.compare(a2, b2));
      cards.shuffle();
    }
  }

  public static void test2() {
    Array<Integer> cards = P.makeCards(); //tao bai
    Array<Integer> deck = new Array<>();
    Array<Integer> hand = new Array<>();
    for (int i = 0; i < 5; i++) deck.add(cards.get(i)); //tao 5 la tren ban
    for (int i = 5; i < 7; i++) hand.add(cards.get(i)); //tao 2 la tren tay
    //tim 5 la lon nhat trong 7 la (5 tren ban 2 tren tay) dieu kien la
    //phai co it nhat 1 la (nhieu nhat la 2) la tren tay
    Array<Integer> biggest = P.move(deck, hand);

    //in 5 la tren ban
    System.out.print("5 la tren ban ");
    for (Integer i : deck) System.out.print(P.nameMap.get(i) + " ");
    System.out.println();
    //in 2 la tren tay
    System.out.print("2 la tren tay ");
    for (Integer i : hand) System.out.print(P.nameMap.get(i) + " ");
    System.out.println();

    //in ket qua 5 la binh cao nhat lay tu 5 la tren ban + 2 la tren tay,
    //ket qua luon co it nhat 1 (nhieu nhat 2) la tren tay
    for (Integer i : biggest) System.out.print(P.nameMap.get(i) + " ");
    System.out.println(m.get(P.check5(biggest)>>13));
  }
}