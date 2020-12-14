package com.collinson.vendingmachine.factory;

import com.collinson.vendingmachine.api.VendingMachine;
import com.collinson.vendingmachine.entity.Bucket;
import com.collinson.vendingmachine.entity.Coin;
import com.collinson.vendingmachine.entity.Item;
import com.collinson.vendingmachine.impl.VendingMachineImpl;

import java.util.List;

public class VendingMachineFactory {
    public static VendingMachine createVendingMachine() {
        return new VendingMachineImpl();
    }

    public static void main(String[] args) {
        VendingMachine vendingMachine = VendingMachineFactory.createVendingMachine();
        System.out.println(vendingMachine.selectItemAndGetPrice(Item.PEPSI));
        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.DIME);
        Bucket<Item, List<Coin>> b = vendingMachine.collectItemAndChange();
        System.out.println("Item :" + b.getFirst().name() + ", Change:" + b.getSecond());
    }
}
