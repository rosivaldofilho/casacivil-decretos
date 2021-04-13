package service;

import dominio.Decreto;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import repository.DecretoRepository;
import util.FacesUtil;

/**
 *
 * @author rosivaldo.adm
 */
public class DecretoService implements Serializable {

    @Inject
    private DecretoRepository decretoRep;

    @Transactional
    public Decreto cadastrar(Decreto decreto){
        decreto.setDataCadastro(new Date());
        return decretoRep.guardar(decreto);
    }

    @Transactional
    public Decreto atualizar(Decreto decreto) {
        return decretoRep.guardar(decreto);
    }

    @Transactional
    public void remover(Decreto decreto) {
        decretoRep.remover(decreto);
    }

    @Transactional
    public Decreto desativar(Decreto decreto) {
        decreto.setAtivo(false);
        return decretoRep.guardar(decreto);
    }

    @Transactional
    public List<Decreto> listarTodos() {
        return decretoRep.listAllAtivos();
    }
    
    @Transactional
    public Decreto buscaUltimoDecreto() {
        return decretoRep.getUltimoDecreto();
    }
    
    @Transactional
    public List<Decreto> buscaUltimosDecretos() {
        return decretoRep.getUltimosDecretos();
    }

    @Transactional
    public List<Decreto> completaNome(String query) {
        return decretoRep.completaNome(query);
    }

    @Transactional
    public Decreto buscaDecretoPeloNumero(String query) throws NoResultException {
        return decretoRep.getDecretobyNumero(query);
    }

    @Transactional
    public Integer numeroTotalDeDecretos() {
        return decretoRep.getNumeroTotalDeDecretosAtivos();
    }

    public void retornaParaInfoDecreto(Decreto decreto) throws IOException {
        FacesUtil.redirecionarPara("/decreto/informacao.xhtml?decreto=" + decreto.getId());
    }

    public void retornaParaInicio() throws IOException {
        FacesUtil.redirecionarPara("/tonacorrida");
    }

    public void retornaParaLogin() throws IOException {
        FacesUtil.redirecionarPara(FacesUtil.getExternalContext().getRequestContextPath()+"/login.xhtml");
    }

    public void encaminharSucesso() throws IOException {
        FacesUtil.redirecionarPara("/site/success.xhtml");
    }

    public List<Decreto> filtraNumero(List<Decreto> decretos, Integer numeroDecreto) {
        if (numeroDecreto != null && !decretos.isEmpty()) {
            return decretos.stream().filter(d -> d.getNumero().equals(numeroDecreto)).collect(Collectors.toList());
        }
        return decretos;
    }

    public List<Decreto> filtraConteudo(List<Decreto> decretos, String conteudo) {
        if (!conteudo.equals("") && !decretos.isEmpty()) {
            return decretos.stream().filter(d -> d.getConteudo().toLowerCase().contains(conteudo.toLowerCase())).collect(Collectors.toList());
        }
        return decretos;
    }

}
