/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.monitorfiles.model;

/**
 *
 * @author thiagomazuhimcosta
 */
public class Client {
    private String name;
    private String cnpj;
    private String businessArea;

    public Client() {
    }

    public Client(String name, String cnpj, String businessArea) {
        this.name = name;
        this.cnpj = cnpj;
        this.businessArea = businessArea;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getBusinessArea() {
        return businessArea;
    }

    public void setBusinessArea(String businessArea) {
        this.businessArea = businessArea;
    }

    @Override
    public String toString() {
        return "Cliente{" + "nome=" + name + ", cnpj=" + cnpj + ", area de negocio=" + businessArea + '}';
    }
}
