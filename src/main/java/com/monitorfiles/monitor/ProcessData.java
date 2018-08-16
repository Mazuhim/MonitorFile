/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.monitorfiles.monitor;

import com.monitorfiles.DataType;
import com.monitorfiles.exceptions.ParsingException;
import com.monitorfiles.model.Client;
import com.monitorfiles.model.Item;
import com.monitorfiles.model.Sale;
import com.monitorfiles.model.Seller;
import com.monitorfiles.utils.ExFile;
import com.monitorfiles.utils.MonitorFiles;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author thiagomazuhimcosta
 */
public class ProcessData
{
    private static String SEPARATOR_ENTITY = "ç";
    private static String SEPARATOR_ITENS = "-";
    private static String SEPARATOR_LIST = ",";

    private ExFile file;
    List<Seller> sellers;
    List<Client> clients;
    List<Sale> sales;

    public ProcessData(File file)
    {
        this.file = new ExFile(file);
        this.sellers = new ArrayList<>();
        this.clients = new ArrayList<>();
        this.sales = new ArrayList<>();
    }

    public void process() throws ParsingException
    {
        if (this.file != null && this.file.exists())
        {
            try
            {
                List<String> lines = this.file.readAllLines();
                this.file.finishReading();
                for (String line : lines)
                {
                    String[] split = line.split(SEPARATOR_ENTITY);

                    if (split[0].equals(DataType.SELLER.getValue()))
                    {
                        Seller seller;
                        seller = this.parseSeller(line, split);
                        sellers.add(seller);
                    } else if (split[0].equals(DataType.CLIENT.getValue()))
                    {
                        Client client = this.parseClient(line, split);
                        clients.add(client);
                    } else if (split[0].equals(DataType.SALE.getValue()))
                    {
                        Sale sale = this.parseSale(line, split);
                        sales.add(sale);
                    }
                }
                if (this.clients.isEmpty() || this.sales.isEmpty() || this.sellers.isEmpty())
                {
                    throw new ParsingException("Não foi possivel processar os dados do arquivo");
                }
                String name = this.file.getName().replace(".dat", ".done.dat");
                this.processReport(name);
            } catch (IOException ex)
            {
                System.out.println("erro ao ler o arquivo. " + ex);
            }
        }
    }

    public List<Item> parseItens(String arrayString) throws ParsingException
    {
        List<Item> itens = new ArrayList<>();
        arrayString = arrayString.replaceAll("[\\[\\]]", "");

        String[] splitItens = arrayString.split(SEPARATOR_LIST);

        for (String splitItem : splitItens)
        {
            Item item = parseItem(splitItem);
            if (item != null)
            {
                itens.add(item);
            }
        }
        return itens;
    }

    private Item parseItem(String splitItem) throws ParsingException
    {
        String[] split = splitItem.split(SEPARATOR_ITENS);
        try
        {
            return new Item(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Double.parseDouble(split[2]));
        } catch (Exception e)
        {
            throw new ParsingException("Falha ao processar os dados do Item: " + splitItem, e);
        }
    }

    private Seller parseSeller(String line, String[] split) throws ParsingException
    {
        try
        {
            return new Seller(split[2], split[1], Double.parseDouble(split[3]));
        } catch (Exception e)
        {
            throw new ParsingException("Falha ao processar os dados do vendedor: " + line, e);
        }
    }

    private Client parseClient(String line, String[] split) throws ParsingException
    {
        try
        {
            return new Client(split[2], split[1], split[3]);
        } catch (Exception e)
        {
            throw new ParsingException("Falha ao processar os dados do Cliente: " + split, e);
        }
    }

    private Sale parseSale(String line, String[] split) throws ParsingException
    {
        try
        {
            return new Sale(Integer.parseInt(split[1]), parseItens(split[2]), split[3]);
        } catch (Exception e)
        {
            throw new ParsingException("Falha ao processar os dados da venda: " + line, e);
        }
    }

    private void processReport(String name)
    {
        ExFile fileOutDirectory = new ExFile(MonitorFiles.getFilesOutdir());

        ExFile fileOut = new ExFile(fileOutDirectory.getFile(), name);

        StringBuilder builder = new StringBuilder("resultados!\n");
        builder.append("Quantidade de clientes: ").append(clients.size()).append("\n");
        builder.append("Quantidade de vendedores: ").append(sellers.size()).append("\n");
        builder.append("Quantidade de vendas: ").append(sales.size()).append("\n");

        sales.sort((o1, o2) ->
        {
            return o2.calculeTotal().compareTo(o1.calculeTotal());
        });
        if (!sales.isEmpty())
        {
            builder.append("Id da Maior venda: ").append(sales.get(0).getId()).append(" valor R$").append(sales.get(0).calculeTotal());
            builder.append("\nPior vendedor: ").append(this.processWorstSeller(sales, sellers));
        }
        System.out.println(builder);
        System.out.println("----------------------------------------------------------------------------------------------------");
        try
        {
            fileOut.write(builder.toString());
            fileOut.finishWriting();
        } catch (IOException ex)
        {
            System.out.println("falha ao criar relatorio de resultados");
        }
    }

    private String processWorstSeller(List<Sale> sales, List<Seller> sellers)
    {
        this.sales.forEach((sale) ->
        {
            this.sellers.forEach((seller) ->
            {
                if (sale.getSalesManName().equals(seller.getName()))
                {
                    seller.addSale(sale);
                }
            });
        });

        sellers.sort((o1, o2) ->
        {
            return o1.calculeTotalSales().compareTo(o2.calculeTotalSales());
        });
        StringBuilder builder = new StringBuilder(sellers.get(0).getName());
        builder.append(" Total das vendas R$").append(sellers.get(0).calculeTotalSales());
        return builder.toString();
    }
}
