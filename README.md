# MonitorFile
Monitoramento e processamento de arquivos

## Funcionamento
O sistema verifica se já existem os diretorios de entrada e saida, ("data/in", "data/out"),
caso não exista, o sistema cria os diretorios.

Depois o sistema registra um ˜serviço de monitoria" no diretorio de entrada para novos arquivos.
Verifica se existem arquivos pendentes de serem processados, que foram criados antes do sistema estar com o monitoramento ligado.

Após isso a cada novo arquivo criado no diretorio de entrada o sistema executa os seguintes passos:

1 - verifica se o arquivo tem a extenção .dat

    1.2 - caso o arquivo não comece com essa extenção o sistema ignora, e aguarda o próximo gatilho.
    
2 - tenta processar os dados

    2.1 - caso aconteça algum problema ao processar os dados
    o sistema renomeia o arquivo original para "file_name.error˜, para assim ignorar esse arquivo até que ele seja ajustado
    
    2.2 - cria um novo arquivo com o mesmo nome do arquivo original com a extenção ".ERROR-DETAIL"
    com os detalhes do erro
    
    2.3 - volta para o estado anterior aguardando que um novo arquivo seja criado.
    
3 - cria um novo arquivo no diretorio de saida com um relatorio.

4 - adiciona a extenção .ready ao arquivo original, para ele não ser processado novamente.


    Obs: O pior vendedor é aquele que vendeu menos em R$ e não quem fez menos venda.
    Ou seja um vendedor pode ter feito somente uma venda e ainda sim ser melhor do que outro que fez 5 vendas, desde que o valor total da venda seja maior que a soma das 5 vendas do outro vendedor.
