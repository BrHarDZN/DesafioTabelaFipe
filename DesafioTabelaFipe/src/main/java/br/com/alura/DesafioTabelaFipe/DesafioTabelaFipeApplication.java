package br.com.alura.DesafioTabelaFipe;

import br.com.alura.DesafioTabelaFipe.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DesafioTabelaFipeApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(DesafioTabelaFipeApplication.class,args);
    }

    @Override
    public void run(String... args) throws Exception {
        Principal principal = new Principal();
        principal.exibirMenu();
    }
}
