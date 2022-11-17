# Sistema de rede social ConVari - Conteúdo Variado

Desenvolvi um sistema de Rede Social e fórum de discussões que chamei na época de CONVARI, Conteúdo Variado. Onde, era possível registrar tópicos e configurar as permissões de acesso ao tópico para outros usuários poderem realizar postagens. Era possível definir status: ocupado, disponível, ausente. Bem como, enviar solicitações de amizade e aceitar solicitações de outros usuários. Acessar o perfil de outro usuário com permissões limitadas. 

Foi criada uma solução em CAPTCHA, bem como, uma solução para recuperação de senha com link de redefinição enviado por e-mail para o usuário, conforme o email cadastrado no sistema.

# Como rodar?

Para rodar o sistema, você vai precisar do servidor de aplicação apache tomcat e do SGBD mysql server, bem como o arquivo WAR do projeto e o script para alimentação 
do banco de dados convari com alguns dados.

# Servidor de aplicação

Baixe o apache tomcat versão 7 (não funciona nas versões mais recentes do tomcat porque o projeto é antigo).
Após o download, instale o tomcat numa pasta de sua escolha (na versão binária é só descompactar o arquivo baixado).

# Servidor de banco de dados

Você necessitará do servidor mysql, versão 5.x. 
Com o mysql server instalado em sua máquina, crie um banco de dados com nome "convari", então crie um usuário com username "convari" com senha "71.@Fi4". 
Depois, conceda permissão para manipulação do banco de dados "convari", pelo usuário "convari". Você pode executar esses passos conforme os comandos a seguir:

>> create database convari;

>> create user 'convari'@'localhost' identified by '71.@Fi4';
>> grant all on convari.* to 'convari'@'localhost' with grant option;

Após executadas as operações acima, navegue até a pasta onde está o arquivo "backup.sql", e então, execute o seguinte:

>> use convari;
>> script backup.sql;

# Deploy

Agora, coloque o arquivo ConVari.war no diretório "webapps" do tomcat 7 instalado. Feito isto, 
rode o tomcat e não esqueça de startar também o mysql server.

# Testar

Para testar, basta abrir no navegador WEB com a seguinte url: http://localhost:8080/ConVari

Aparecerá a tela inicial do projeto. Vá na opção de login e entre com o seguinte usuário:

login: italo
password: italo

Aparecerá a página inicial, você pode testar o sistema!

