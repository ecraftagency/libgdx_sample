package com.pixmeg.desktop;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

public class Test {
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
  }
  public static void main(String[] arg) {
    //Array<Integer> cards = M.makeCards();
    //Array<Integer> cards = MB.makeCards();
    P.makeCards();
    //TEST CASE 1
//    int[] pattern = new int[] {
//      M.nameMap.getKey("AC", false),
//      M.nameMap.getKey("KC", false),
//      M.nameMap.getKey("QC", false),
//      M.nameMap.getKey("JC", false),
//      M.nameMap.getKey("10C", false)
//    };
//    int r = M.check5(pattern)>>13;
//    System.out.println(m.get(r));

    //TEST CASE 2
//    for (int i = 0; i < 1000; i++) {
//      int[] pattern = new int[] {
//        cards.get(0),
//        cards.get(1),
//        cards.get(2),
//        cards.get(3),
//        cards.get(4)
//      };
//      for (int j : pattern) System.out.print(M.nameMap.get(j) + " ");
//      int r = M.check5(pattern)>>13;
//      System.out.println(m.get(r));
//      cards.shuffle();
//    }

    //TEST CASE 3
//    int[] pattern = new int[] {cards.get(0), cards.get(1), cards.get(2), cards.get(3), cards.get(4)};
//    long startTime = System.nanoTime();
//    for (int i = 0; i < 1000000; i++) {
//      int r = M.check5(pattern)>>13;
//    }
//    long endTime = System.nanoTime();
//    System.out.println("Time elapsed: " + (endTime-startTime)/1000000);


    //TEST CASE 4
//    int[] patternA = new int[] {
//      M.nameMap.getKey("10C", false), //5Ch
//      M.nameMap.getKey("JC", false), //6B
//      M.nameMap.getKey("QC", false), //7C
//      M.nameMap.getKey("KCh", false), //8Ch
//      M.nameMap.getKey("AC", false) //9C
//    };
//
//    int[] patternB = new int[] {
//      M.nameMap.getKey("10C", false), //5B
//      M.nameMap.getKey("JC", false), //6B
//      M.nameMap.getKey("QC", false), //7C
//      M.nameMap.getKey("KC", false), //8Ch
//      M.nameMap.getKey("AC", false) //9Ch
//    };
//    System.out.println(M.compare5(patternA, patternB));

    //TEST CASE 5
//    for (int i = 0; i < 1000; i++) {
//      int[] patternA = new int[] {cards.get(0), cards.get(1), cards.get(2), cards.get(3), cards.get(4)};
//      int[] patternB = new int[] {cards.get(5), cards.get(6), cards.get(7), cards.get(8), cards.get(9)};
//      for (int j : patternA) System.out.print(M.nameMap.get(j) + " ");
//      System.out.print(m.get(M.check5(patternA)>>13) + " ");
//      System.out.print(M.compare5(patternA, patternB) + " ");
//      for (int j : patternB) System.out.print(M.nameMap.get(j) + " ");
//      System.out.print(m.get(M.check5(patternB)>>13) + " ");
//      System.out.println();
//      cards.shuffle();
//    }

    //TEST CASE 7
//    System.out.println();
//    for (int i = 0; i < 10000; i++) {
//      int[] pattern = new int[] {cards.get(0), cards.get(1), cards.get(2), cards.get(3), cards.get(4), cards.get(5),
//              cards.get(6), cards.get(7), cards.get(8), cards.get(9), cards.get(10), cards.get(11), cards.get(12)};
//      for (int j : pattern) System.out.print(M.nameMap.get(j) + " ");
//      System.out.println();
//      long startTime = System.nanoTime();
//      int[] mv = M.move135(pattern);
//      long endTime = System.nanoTime();
//      for (int j : mv) System.out.print(M.nameMap.get(j) + " ");
//      System.out.println(m.get(M.check5(mv)>>13));
//      System.out.println("Time elapsed: " + (endTime-startTime)/1000000);
//      cards.shuffle();
//    }

    //TEST 8
//    long totalTime = 0;
//    cards.shuffle();
//    int[] pattern = new int[] {cards.get(0), cards.get(1), cards.get(2), cards.get(3), cards.get(4), cards.get(5),
//            cards.get(6), cards.get(7), cards.get(8), cards.get(9), cards.get(10), cards.get(11), cards.get(12)};
//    System.out.println("Start");
//    long startTime = System.nanoTime();
//    for (int i = 0; i < 10000; i++)
//      M.move135(pattern);
//    long endTime = System.nanoTime();
//    totalTime += endTime - startTime;
//    System.out.println("Total time: " + totalTime/1000000 + " miliseconds " + totalTime/1000 + " nanoseconds");

    //TEST 9
//    int[] pattern = new int[] {
//      M.nameMap.getKey("AC", false),
//      M.nameMap.getKey("AR", false),
//      M.nameMap.getKey("ACh", false),
//      M.nameMap.getKey("9B", false),
//      M.nameMap.getKey("KC", false),
//      M.nameMap.getKey("KR", false),
//      M.nameMap.getKey("KCh", false),
//      M.nameMap.getKey("JB", false),
//      M.nameMap.getKey("2Ch", false),
//      M.nameMap.getKey("8C", false),
//      M.nameMap.getKey("8Ch", false),
//      M.nameMap.getKey("6B", false),
//      M.nameMap.getKey("6Ch", false),
//    };
//
//    int[] c = M.move85(pattern);
//    int r = M.check5(c);
//    for (int j : c) System.out.print(M.nameMap.get(j) + " ");
//    System.out.println();
//    System.out.print(m.get(r>>13));

    //TEST 10
//    long startTime, endTime, elapsed = 0;
//
//    for (int step = 0; step < 1000000; step++) {
//      int[] pattern = new int[] {cards.get(0), cards.get(1), cards.get(2), cards.get(3), cards.get(4), cards.get(5),
//              cards.get(6), cards.get(7), cards.get(8), cards.get(9), cards.get(10), cards.get(11), cards.get(12)};
//
//      startTime = System.nanoTime();
//      int[][] res = MB.move(pattern);
//      endTime = System.nanoTime();
////      for (int i : _move)
////        System.out.print(MB.nameMap.get(i) + " ");
////      System.out.println(m.get(M.check5(_move)>>13));
//
//      elapsed += endTime - startTime;
////      for (int i = 1; i < 3; i++) {
////        for (int j = 0; j < res[i].length; j++)
////          System.out.print(MB.nameMap.get(res[i][j]) + " ");
////        System.out.println(m.get(M.check5(res[i])>>13));
////      }
////      System.out.println();
//      cards.shuffle();
//    }
//    System.out.println("Total Elapsed Time: " + elapsed/1000000);


    //TEST 11
//    cards = MV2.makeCards();
//    long startTime, endTime, elapsed = 0;
//
//    for (int step = 0; step < 1000000; step++) {
//      Array<Integer> pattern = new Array<>();
//      for (int i = 0; i < 13; i++) pattern.add(cards.get(i));
//
//      startTime = System.nanoTime();
//      Array<Array<Integer>> r = MV2.move(pattern);
//      endTime = System.nanoTime();
//      elapsed += endTime - startTime;
//      cards.shuffle();
//
////      for (Array<Integer> a : r) {
////        for (int i : a)
////          System.out.print(MV2.nameMap.get(i) + " ");
////        if (a.size == 5) System.out.println(m.get(MV2.check5(a)>>13));
////        if (a.size == 3) System.out.println(m.get(MV2.check3(a)>>13));
////      }
////      System.out.println();
//
//    }
//    System.out.println("Total Elapsed Time: " + elapsed/1000000);




      //P.test(2);
      //P.test2();
      Array<Integer> c1 = new Array<>();
      c1.add(P.nameMap.getKey("10R", false));
      c1.add(P.nameMap.getKey("10C", false));
      c1.add(P.nameMap.getKey("9C", false));


      Array<Integer> c2 = new Array<>();
      c2.add(P.nameMap.getKey("10C", false));
      c2.add(P.nameMap.getKey("10Ch", false));
      c2.add(P.nameMap.getKey("9C", false));

      System.out.println(P.compare(c1, c2));
  }
}

/*
  rules:
  mid-MT & high-MT -> mid-2nd swap high-3rd
  mid-T & high-MT -> mid-swappable swap high-lowest
  mid-D & high-MT -> mid-1st swap high-3rd
  mid-X & high-MT -> mid-1st swap high-3rd
  mid-X & high-D -> mid-1st swap high-3rd

  data structure:{
      fix:int[]
      swap:list:int[]
  }
  {3,3,K} -> fix:[], swap:{[3,3], [K]}
  {3,3,3,K,K} -> fix:[3,3,3],swap:{[K,K]}
  {3,3,4,4,5} -> fix:[4,4], swap: {[3,3],[5]}
  {3,3,3,5,6} -> fix:[3,3,3], swap: {[5], [6]}
  {3,4,2,6,8} -> fix:[8], swap: {[3],[4],[2],[6]}
  {6,6,8,A,Q} -> fix:[6,6], swap: {[8],[A],[Q]}
  algorithm outline:
              for i:midMove -> 0
                if (check(highMove) eq i)
                  if (mid.swappale.type eq high.swappale.type) -> do swap
  */