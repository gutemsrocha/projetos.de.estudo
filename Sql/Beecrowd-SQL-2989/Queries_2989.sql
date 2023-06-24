WITH entrante AS (
    SELECT
        empregado.matr, empregado.nome AS "empregado", empregado.lotacao_div,
        SUM(vencimento.valor) OVER (PARTITION BY empregado.matr) AS "pagamento"
   
    FROM empregado
    INNER JOIN emp_venc ON emp_venc.matr = empregado.matr
    INNER JOIN vencimento ON vencimento.cod_venc = emp_venc.cod_venc
    ORDER BY empregado.matr ASC
),

descontos AS (
    SELECT
        empregado.matr, empregado.nome AS "empregado", empregado.lotacao_div,
        SUM(desconto.valor) OVER (PARTITION BY empregado.matr) AS "desconto_total"
    
	FROM empregado
    INNER JOIN emp_desc ON emp_desc.matr = empregado.matr
    INNER JOIN desconto ON desconto.cod_desc = emp_desc.cod_desc
    ORDER BY empregado.matr ASC
),

balancos AS (
    SELECT
        DISTINCT entrante.matr, entrante.empregado, entrante.lotacao_div,
        entrante.pagamento, COALESCE(descontos.desconto_total, 0) AS "desconto",
        (entrante.pagamento - COALESCE(descontos.desconto_total, 0)) AS "balanco"
    
	FROM entrante
    LEFT JOIN descontos ON descontos.matr = entrante.matr
    ORDER BY matr ASC )

SELECT
    "departamento", "divisao",  TRUNC(ROUND("avg", 2), 2) AS "media", TRUNC(ROUND("max", 2), 2) AS "maior"

FROM

	(SELECT
	 	DISTINCT divisao.nome AS "divisao", departamento.nome AS "departamento",
		AVG(balancos.balanco) OVER (PARTITION BY divisao.nome) AS "avg",
		MAX(balancos.balanco) OVER (PARTITION BY divisao.nome) AS "max"

	FROM balancos
	INNER JOIN divisao ON divisao.cod_divisao = balancos.lotacao_div
	INNER JOIN departamento ON departamento.cod_dep = divisao.cod_dep
	
	) AS saida

ORDER BY media DESC, saida.avg DESC;