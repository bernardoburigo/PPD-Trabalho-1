# Pesquisa 4 - Divisao por Grupos de Arquivos

## O que implementa

Busca paralela em que os arquivos sao divididos em grupos. Cada Thread recebe um grupo e processa seus arquivos sequencialmente.

## Estrategia de busca

Usa `String.contains` em cada linha.

## Estrategia de Thread

Cria uma Thread por grupo de arquivos, reduzindo a quantidade de tarefas e o overhead de agendamento.

## Como executar

```bash
javac -d build/pesquisa_4 pesquisa_4/codigo/*.java
java -cp build/pesquisa_4 Pesquisa4App data/txt Mariana 8
```

## Pontos fortes

- Menos overhead que criar uma tarefa por arquivo.
- Cada Thread trabalha em um bloco claro de arquivos.
- Facilita explicar balanceamento de carga.

## Pontos fracos

- Se os grupos ficarem desbalanceados, uma Thread pode terminar muito depois das outras.
- Arquivos muito diferentes em tamanho podem reduzir o ganho.

## Quando usar

Boa quando existem muitos arquivos pequenos e o custo de criar muitas tarefas passa a ser relevante.
