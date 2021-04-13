package testes;

import dominio.Pessoa;
import javax.transaction.Transactional;
import repository.PessoaRepository;

/**
 *
 * @author rosivaldo
 */
public class Teste {

    private final PessoaRepository pessoaRep;

    public Teste() {
        this.pessoaRep = new PessoaRepository();
    }


    @Transactional
    public Pessoa salvar(Pessoa pessoa) throws Exception {
        if(pessoaRep != null){
        return pessoaRep.guardar(pessoa);
        }
        System.out.println("Null");
        return new Pessoa();
    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws Exception {
        Pessoa pessoa = new Pessoa();
        pessoa.setDocumentoReceita("45654654");
        pessoa.setEmail("teste@teste.com.br");
        pessoa.setFoto("/resources/images/profiles/user/no-image.jpg");
        pessoa.setNome("Jonas Algusto");
        pessoa.setTelefone("94569-4565");
        Teste teste = new Teste();
        pessoa.setUsuario(null);
        pessoa.setAtivo(true);
        pessoa.setAtivo(true);
        teste.salvar(pessoa);
        System.exit(0);
    }
}
