/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.monitorfiles.model;

import java.util.List;

/**
 *
 * @author thiagomazuhimcosta
 */
public class Sale {

    private Integer id;
    private List<Item> itens;
    private String salesManName;

    public Sale() {
    }

    public Sale(Integer id, List<Item> itens, String salesManName) {
        this.id = id;
        this.itens = itens;
        this.salesManName = salesManName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Item> getItens() {
        return itens;
    }

    public void setItens(List<Item> itens) {
        this.itens = itens;
    }

    public String getSalesManName() {
        return salesManName;
    }

    public void setSalesManName(String salesManName) {
        this.salesManName = salesManName;
    }

    public Double calculeTotal() {
        Double total = 0.0;
        if (this.itens.isEmpty()) {
            return total;
        }

        for (Item item : this.itens) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

    @Override
    public String toString() {
        return "Venda{" + "id=" + id + ", itens=" + itens + ", vendedor=" + salesManName + '}';
    }
}
