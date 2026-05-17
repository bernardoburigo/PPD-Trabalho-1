# Pesquisa 3 - Pool Fixo de Threads

## O que implementa

Busca paralela usando `ExecutorService` com quantidade fixa de Threads.

## Estrategia de busca

Usa `String.contains` em cada linha.

## Estrategia de Thread

Os arquivos viram tarefas. Um pool fixo executa essas tarefas sem criar uma Thread nova para cada arquivo.

## Como executar

```bash
javac -d build/pesquisa_3 pesquisa_3/codigo/*.java
java -cp build/pesquisa_3 Pesquisa3App data/txt Mariana 8
```

O terceiro argumento e a quantidade de Threads. Se omitido, usa a quantidade de nucleos disponiveis.

## Pontos fortes

- Controla melhor o uso de recursos.
- Evita excesso de Threads.
- E uma abordagem mais profissional em Java.

## Pontos fracos

- A escolha da quantidade de Threads influencia o resultado.
- Muitos acessos simultaneos ao disco ainda podem limitar o ganho.

## Quando usar

Recomendada para a maioria dos cenarios paralelos, principalmente quando ha muitos arquivos.
