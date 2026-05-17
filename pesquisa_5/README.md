# Pesquisa 5 - KMP com Pool Fixo de Threads

## O que implementa

Busca paralela usando pool fixo de Threads e algoritmo KMP.

## Estrategia de busca

Usa Knuth-Morris-Pratt, conhecido como KMP. A tabela LPS do padrao e calculada uma vez e reutilizada em todas as linhas.

## Estrategia de Thread

Usa `ExecutorService` com pool fixo. Cada arquivo e enviado como tarefa para o pool.

## Como executar

```bash
javac -d build/pesquisa_5 pesquisa_5/codigo/*.java
java -cp build/pesquisa_5 Pesquisa5App data/txt Mariana 8
```

## Pontos fortes

- Combina paralelismo com algoritmo de busca diferente.
- Evita retrocessos desnecessarios na comparacao de caracteres.
- Atende ao ponto extra de algoritmo otimizado.

## Pontos fracos

- Para textos pequenos, `String.contains` pode ser tao rapido quanto ou mais rapido, pois e otimizado pela JVM.
- KMP aumenta a complexidade do codigo.

## Quando usar

Recomendada para demonstrar que desempenho pode melhorar por Threads e tambem por escolha de algoritmo.
