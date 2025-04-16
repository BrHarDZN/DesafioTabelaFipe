package br.com.alura.DesafioTabelaFipe.principal;

import br.com.alura.DesafioTabelaFipe.model.Dados;
import br.com.alura.DesafioTabelaFipe.model.Modelo;
import br.com.alura.DesafioTabelaFipe.model.Veiculo;
import br.com.alura.DesafioTabelaFipe.service.ConsumoApi;
import br.com.alura.DesafioTabelaFipe.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private static final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";

    private final Scanner scanner = new Scanner(System.in);
    private final ConsumoApi consumoApi = new ConsumoApi();
    private final ConverteDados conversor = new ConverteDados();

    public void exibirMenu() {
        int opcao = solicitarOpcaoUsuario();
        String tipoVeiculo = mapearTipoVeiculo(opcao);

        if (tipoVeiculo == null) {
            exibirMensagemErro("Opção inválida. Reiniciando o programa.");
            exibirMenu();
            return;
        }

        System.out.println("Você selecionou: " + tipoVeiculo.toUpperCase());

        List<Dados> marcas = obterListaDados(construirUrl(tipoVeiculo,"marcas"));
        exibirLista(marcas);

        String termoBuscaMarca = solicitarTexto("Digite parte do nome da marca a ser buscado: ");
        List<Dados> MarcasFiltrados = filtrarPorNome(marcas,termoBuscaMarca);

        if (MarcasFiltrados.isEmpty()) {
            exibirMensagemErro("Nenhum modelo encontrado com esse nome. Reiniciando o programa.");
            exibirMenu();
            return;
        }

        exibirLista(MarcasFiltrados);

        int codigoMarca = solicitarCodigo("Digite o código da marca desejada: ");
        Optional<Dados> marcaSelecionada = buscarPorCodigo(MarcasFiltrados,codigoMarca);

        if (marcaSelecionada.isEmpty()) {
            exibirMensagemErro("Marca não encontrada. Reiniciando o programa.");
            exibirMenu();
            return;
        }

        Modelo modelos = obterModelo(construirUrl(tipoVeiculo,"marcas",marcaSelecionada.get().getCodigo(),"modelos"));
        exibirLista(modelos.getModelos());

        String termoBuscamodelo = solicitarTexto("Digite parte do nome do modelo a ser buscado: ");
        List<Dados> modelosFiltrados = filtrarPorNome(modelos.getModelos(),termoBuscamodelo);

        if (modelosFiltrados.isEmpty()) {
            exibirMensagemErro("Nenhum modelo encontrado com esse nome. Reiniciando o programa.");
            exibirMenu();
            return;
        }

        System.out.println("\nModelos filtrados:");
        exibirLista(modelosFiltrados);

        int codigoModelo = solicitarCodigo("Digite o código do modelo desejado: ");
        if (codigoModelo == 0) {
            exibirMensagemErro("Opção invalida, Reiniciando programa.");
            exibirMenu();
            return;
        }
        List<Dados> anos = obterListaDados(construirUrl(tipoVeiculo,"marcas",String.valueOf(codigoMarca),"modelos",String.valueOf(codigoModelo),"anos"));

        if (anos.isEmpty()) {
            exibirMensagemErro("Nenhum registro de ano encontrado. Reiniciando o programa.");
            exibirMenu();
            return;
        }

        List<Veiculo> veiculos = buscarVeiculos(tipoVeiculo,codigoMarca,codigoModelo,anos);
        if (veiculos.isEmpty()) {
            exibirMensagemErro("Nenhum registro de ano encontrado. Reiniciando o programa.");
            exibirMenu();
        }
        System.out.println("\nVeículos encontrados:");
        veiculos.forEach(System.out::println);
    }

    private List<Veiculo> buscarVeiculos(String tipoVeiculo,int codigoMarca,int codigoModelo,List<Dados> anos) {
        List<Veiculo> veiculos = new ArrayList<>();
        for (Dados ano : anos) {
            String url = construirUrl(tipoVeiculo,"marcas",String.valueOf(codigoMarca),"modelos",String.valueOf(codigoModelo),"anos",ano.getCodigo());
            String json = consumoApi.obterDados(url);
            veiculos.add(conversor.obterDados(json,Veiculo.class));
        }
        return veiculos;
    }

    private List<Dados> obterListaDados(String url) {
        String json = consumoApi.obterDados(url);
        return conversor.obterLista(json,Dados.class);
    }

    private Modelo obterModelo(String url) {
        String json = consumoApi.obterDados(url);
        return conversor.obterDados(json,Modelo.class);
    }

    private Optional<Dados> buscarPorCodigo(List<Dados> lista,int codigo) {
        return lista.stream().filter(item -> Integer.parseInt(item.getCodigo()) == codigo).findFirst();
    }

    private List<Dados> filtrarPorNome(List<Dados> lista,String termo) {
        return lista.stream().filter(item -> item.getNome().toLowerCase().contains(termo.toLowerCase())).collect(Collectors.toList());
    }

    private void exibirLista(List<Dados> lista) {
        lista.forEach(System.out::println);
    }

    private int solicitarOpcaoUsuario() {
        try {
            System.out.println("Selecione o tipo de veículo:");
            System.out.println("1 - Carro");
            System.out.println("2 - Moto");
            System.out.println("3 - Caminhão");
            System.out.print("Opção: ");
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            scanner.nextLine();
            return 0;
        }
    }

    private int solicitarCodigo(String mensagem) {
        try {
            System.out.println("\n" + mensagem);
            return scanner.nextInt();
        }catch (InputMismatchException e) {
            scanner.nextLine();
            return 0;
        }

    }

    private String solicitarTexto(String mensagem) {
        System.out.println("\n" + mensagem);
        return scanner.next();
    }

    private String mapearTipoVeiculo(int opcao) {
        return switch (opcao) {
            case 1 -> "carros";
            case 2 -> "motos";
            case 3 -> "caminhoes";
            default -> null;
        };
    }

    private void exibirMensagemErro(String mensagem) {
        System.out.println("\n[ERRO] " + mensagem + "\n");
    }

    private String construirUrl(String... caminhos) {
        return URL_BASE + String.join("/",caminhos);
    }
}
