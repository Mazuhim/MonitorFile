/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.monitorfiles.model;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author thiagomazuhimcosta
 */
public class Seller {
    private String name;
    private String cpf;
    private Double salary;
    private List<Sale> sales;

    public Seller() {
    }

    public Seller(String name, String cpf, Double income) {
        this.name = name;
        this.cpf = cpf;
        this.salary = income;
        this.sales = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Double getIncome() {
        return salary;
    }

    public void setIncome(Double income) {
        this.salary = income;
    }

    public List<Sale> getSales() {
        return sales;
    }

    public void setSales(List<Sale> sales) {
        this.sales = sales;
    }

    public void addSale(Sale sale) {
        if (sales == null) {
            sales = new ArrayList<>();
        }
        sales.add(sale);
    }

    public Double calculeTotalSales() {
        if (this.sales == null || this.sales.isEmpty()) {
            return 0.0;
        }
        Double totals = 0.0;
        for (Sale sale : sales) {
            totals += sale.calculeTotal();
        }
        return totals;
    }

    @Override
    public String toString()
    {
        return "Seller{" + "name=" + name + ", cpf=" + cpf + ", salary=" + salary + ", sales=" + sales + '}';
    }
}
