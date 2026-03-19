package dev.devdreamer.ecommerce.basketservice.configuration;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
@RequiredArgsConstructor
public class RedisConfiguration {

    @Bean
    @ConfigurationProperties(value = "spring.data.redis")
    public RedisProperties redisProperties() {
        return new RedisProperties();
    }

    /*
    RedisStandaloneConfiguration (O quê):
    Propósito: Armazenar detalhes de conexão de um único servidor Redis (Host, Porta, Password, Database).
    Tipo: É um objeto de configuração simples.
    Uso: Passado como argumento para criar o ConnectionFactory.
     */

    @Bean
    public RedisConnectionFactory redisConnectionFactory(RedisProperties redisProperties) {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisProperties.getHost());
        config.setPort(redisProperties.getPort());
        config.setPassword(redisProperties.getPassword());
        return new LettuceConnectionFactory(config);
    }
    /*
    RedisConnectionFactory (Como):
    Propósito: Criar conexões (RedisConnection) ativas com o servidor configurado.
    Tipo: Interface (implementada por JedisConnectionFactory ou LettuceConnectionFactory).
    Uso: Utilizado pelo RedisTemplate para realizar operações no Redis.
    */


    @Data
    public static class RedisProperties {
        private String host;
        private Integer port;
        private String password;
    }
}

    /* A principal diferença entre Lettuce e Jedis é a arquitetura de conexão: Lettuce é assíncrono,
    * thread-safe (compartilhável entre threads)
    * e ideal para alto desempenho,enquanto Jedis é síncrono e requer pool de conexões,
    * sendo mais simples, porém menos escalável. Lettuce usa Netty e é padrão no Spring Boot.
    */