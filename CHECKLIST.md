# Checklist do Projeto

## Exercicio 1: Criar um Clube
- [x] Criar ClubeController
- [x] Criar ClubeService
- [x] Criar ClubeRequestDTO
- [x] Criar ClubeResponseDTO

## Exercicio 2: Editar um Clube
- [x] Criar ClubeUpdateDTO
- [x] Criar método editar no controller e service
- [x] Criar validações e testes

## Exercicio 3: Inativar um Clube
- [x] Implementar método inativar
- [x] Criar validações e testes

## Exercicio 4: Criar um Estádio
- [x] Criar EstadioController
- [x] Criar EstadioService
- [x] Criar EstadioRequestDTO
- [x] Criar EstadioResponseDTO
- [x] Criar Testes com Mock e Assert

## Exercicio 5: Editar um Estádio
- [x] Criar EstadioService com regras de nomes duplicados
- [x] Criar validações e testes

## Exercicio 6: Criar uma Partida
- [x] Criar PartidaController
- [x] Criar PartidaService
- [x] Criar DTO's
- [x] Criar regra de integridade entre Clubes e Estádios
- [x] Criar validações e testes para cobrir casos positivos e inválidos

## Exercicio 7: Editar uma Partida
- [x] Criar lógica de edição reaproveitando validações da criação
- [x] Criar validações e testes

## Exercicio 8: Buscar Partida por Clube
- [x] Criar consulta implementada no Service e Controller
- [x] Criar testes com listas e clube inexistente

## Exercicio 9: Retrospecto por Clube
- [x] Criar lógica para contar vitórias, empates e derrotas
- [x] Criar DTO ConfrontoResumoDTO e testes válidados

## Exercicio 10: Confronto direto entre dois clubes
- [x] Criar agrupamento de partidas
- [x] Criar ConfrontoDiretoDTO

## Exercicio 11: Ranking de Clubes
- [x] Criar lógica para calcular a pontuação e saldo de gols
- [x] Criar ClubeRankingDTO
- [x] Criar validações e testes

## Exercicio 12: Paginação e Filtros
- [x] Implementar Pageable no repository
- [x] Criar validações e testes manuais
- [x] Criar validações e testes com Mocks

## Cobertura de Testes
- [x] Criar Controller de Teste com Mockito
- [x] Criar cobertura de testes simples de atributos nos DTO's
- [x] Criar Service de test com mocks e casos negativos
- [x] Criar Exceptions com testes individuais e Handler Global de testes