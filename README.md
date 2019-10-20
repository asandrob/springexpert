# Sistema Brewer
### Baseado no curso Spring Expert da Algaworks (WIP)


1. Usei como leiaute, o [AdminLTE 2](http://embed.plnkr.co/XLCqnt/).
2. Usei o Criteria da JPA e não do Hibernate.
3. Para os combos de Estilos e Sabores, utilizei o [Selec2](https://adminlte.io/themes/AdminLTE/pages/forms/advanced.html), com isso pode-se escolher mais de um estilo/sabor nos filtros.
4. Para o headers das tabelas, utilizei um [componente](http://embed.plnkr.co/XLCqnt/) para fixá-los no topo da página, assim o header não é escondido ao rolar a tela:
5. Na tela de cadastro das Cervejas, a área de upload das imagens também aceita colar imagens capturadas por algum screenhunter.
6. Troquei o pool de conexões pelo Hikari
7. Para desabilitar os elementos na tela, criei um AttributeTagProcessor __brewer:enableforroles__="ROLE1,ROLE2,ROLE3", ao invés de ocultar, deixa o elemento não clicável. Este AttributeTagProcessor verifica se o usuário tem alguma das permissões passadas, caso tenha, habilita o elemento, do contrário, desabilita. É importante salientar que isso é só no front-end, ainda sim deve ser feita a verificação do back-end.
