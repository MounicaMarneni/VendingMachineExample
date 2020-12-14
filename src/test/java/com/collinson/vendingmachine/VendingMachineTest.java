package com.collinson.vendingmachine;

import com.collinson.vendingmachine.entity.Bucket;
import com.collinson.vendingmachine.entity.Coin;
import com.collinson.vendingmachine.entity.Item;
import com.collinson.vendingmachine.exception.NotFullPaidException;
import com.collinson.vendingmachine.exception.NotSufficientChangeException;
import com.collinson.vendingmachine.exception.SoldOutException;
import com.collinson.vendingmachine.factory.VendingMachineFactory;
import com.collinson.vendingmachine.api.VendingMachine;
import com.collinson.vendingmachine.impl.VendingMachineImpl;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class VendingMachineTest {
    private static VendingMachine vendingMachine;

    @BeforeClass
    public static void setUp() {
        vendingMachine = VendingMachineFactory.createVendingMachine();
    }

    @AfterClass
    public static void tearDown() {
        vendingMachine = null;
    }

    @Test
    public void testBuyItemWithExactPrice() {
        //select item, price in cents
        long price = vendingMachine.selectItemAndGetPrice(Item.COKE);
        //price should be Coke's price
        assertEquals(Item.COKE.getPrice(), price);
        //25 cents paid
        vendingMachine.insertCoin(Coin.QUARTER);
        Bucket<Item, List<Coin>> bucket = vendingMachine.collectItemAndChange();
        Item item = bucket.getFirst();
        List<Coin> change = bucket.getSecond();
        //should be Coke
        assertEquals(Item.COKE, item);
        //there should not be any change
        assertTrue(change.isEmpty());
        vendingMachine.refund();
    }

    @Test
    public void testBuyItemWithMorePrice() {
        long price = vendingMachine.selectItemAndGetPrice(Item.SODA);
        assertEquals(Item.SODA.getPrice(), price);
        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);
        Bucket<Item, List<Coin>> bucket = vendingMachine.collectItemAndChange();
        Item item = bucket.getFirst();
        List<Coin> change = bucket.getSecond();
        //should be Coke
        assertEquals(Item.SODA, item);
        assertTrue(!change.isEmpty());
        //comparing change
        assertEquals(50 - Item.SODA.getPrice(), getTotal(change));
        vendingMachine.refund();
    }

    @Test
    public void testRefund() {
        long price = vendingMachine.selectItemAndGetPrice(Item.PEPSI);
        assertEquals(Item.PEPSI.getPrice(), price);
        vendingMachine.insertCoin(Coin.DIME);
        vendingMachine.insertCoin(Coin.NICKLE);
        vendingMachine.insertCoin(Coin.PENNY);
        vendingMachine.insertCoin(Coin.QUARTER);
        assertEquals(41, getTotal(vendingMachine.refund()));
    }

    @Test(expected = SoldOutException.class)
    public void testReset() {
        VendingMachine machine = VendingMachineFactory.createVendingMachine();
        machine.reset();
        machine.selectItemAndGetPrice(Item.COKE);
        vendingMachine.refund();
    }

    @Test(expected = SoldOutException.class)
    public void testSoldOut() {
        vendingMachine.refund();
        for (int i = 0; i < 5; i++) {
            vendingMachine.selectItemAndGetPrice(Item.COKE);
            vendingMachine.insertCoin(Coin.QUARTER);
            vendingMachine.collectItemAndChange();
        }
    }

    @Test(expected = NotSufficientChangeException.class)
    public void testNotSufficientChangeException() {
        vendingMachine.refund();
        for (int i = 0; i < 5; i++) {
            vendingMachine.selectItemAndGetPrice(Item.SODA);
            vendingMachine.insertCoin(Coin.QUARTER);
            vendingMachine.insertCoin(Coin.QUARTER);
            vendingMachine.collectItemAndChange();
            vendingMachine.selectItemAndGetPrice(Item.PEPSI);
            vendingMachine.insertCoin(Coin.QUARTER);
            vendingMachine.insertCoin(Coin.QUARTER);
            vendingMachine.collectItemAndChange();
        }
    }

    @Test(expected = NotFullPaidException.class)
    public void testNotFullPaidException() {
        vendingMachine.refund();
        vendingMachine.selectItemAndGetPrice(Item.SODA);
        vendingMachine.insertCoin(Coin.PENNY);
        vendingMachine.collectItemAndChange();
    }

    @Ignore
    public void testVendingMachineImpl() {
        VendingMachineImpl vm = new VendingMachineImpl();
    }

    private long getTotal(List<Coin> change) {
        long total = 0;
        for (Coin c : change) {
            total = total + c.getDenomination();
        }
        return total;
    }
}