package com.collinson.vendingmachine.api;

import com.collinson.vendingmachine.entity.Bucket;
import com.collinson.vendingmachine.entity.Coin;
import com.collinson.vendingmachine.entity.Item;

import java.util.List;

public interface VendingMachine {

    public long selectItemAndGetPrice(Item item);

    public void insertCoin(Coin coin);

    public List<Coin> refund();

    public Bucket<Item, List<Coin>> collectItemAndChange();

    public void reset();

}
