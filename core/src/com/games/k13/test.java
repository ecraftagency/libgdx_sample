package com.games.k13;

import com.badlogic.gdx.utils.Array;

public class test {
  public static void main(String[] args) {
    Array<Integer> cards = c.makeCard();
    System.out.println(c.isConsecutive(makeCards("3R","4Ch","5B","6C","7Ch","8C","9C","10R","JC","QB","KC","AR")));
  }

  private static Array<Integer> makeCards(String ...cards) {
    Array<Integer> res = new Array<>();
    for (String card : cards)
      res.add(c.card(card));
    return res;
  }
}
